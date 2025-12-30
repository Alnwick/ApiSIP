package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Model.Document;
import com.upiicsa.ApiSIP.Repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    private final Path fileStorageLocation = Paths.get("./uploads").toAbsolutePath().normalize();

    public DocumentService() throws IOException {
        try{
            Files.createDirectories(fileStorageLocation);
        }catch (Exception e){
            throw new IOException("Not able to create directory", e);
        }
    }

    @Transactional
    public String saveFile(MultipartFile file, String type, Long id) throws IOException {
        return "";
    }

}
