package com.upiicsa.ApiSIP.Service.Infrastructure;

import com.upiicsa.ApiSIP.Exception.BusinessException;
import com.upiicsa.ApiSIP.Model.Catalogs.DocumentType;
import com.upiicsa.ApiSIP.Model.Document_Process.Document;
import com.upiicsa.ApiSIP.Model.Document_Process.StudentProcess;
import com.upiicsa.ApiSIP.Model.Enum.ErrorCode;
import com.upiicsa.ApiSIP.Utils.DocumentNamingUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path root;
    private final DocumentNamingUtils namingUtils;

    public FileStorageService(@Value("${storage.location}") String storagePath,
                              DocumentNamingUtils namingUtils) {
        this.root = Paths.get(storagePath).toAbsolutePath().normalize();
        this.namingUtils = namingUtils;
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_STORAGE_ERROR);
        }
    }

    public String store(MultipartFile file, StudentProcess process, DocumentType type) {
        String fileName = namingUtils.generateVersionedName(process, type);
        try {
            Files.copy(file.getInputStream(), this.root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_STORAGE_ERROR);
        }
        return fileName;
    }
    public void store(MultipartFile file, Document document) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(document.getURL()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_STORAGE_ERROR);
        }
    }

    public String store(MultipartFile file, String enrollment){
        String fileName = enrollment + "_CARTA_PRESENTACION.pdf";
        try {
            Files.copy(file.getInputStream(), this.root.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_STORAGE_ERROR);
        }
        return fileName;
    }
}
