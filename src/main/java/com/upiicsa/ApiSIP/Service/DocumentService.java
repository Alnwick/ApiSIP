package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Exception.ValidationException;
import com.upiicsa.ApiSIP.Model.Catalogs.DocumentType;
import com.upiicsa.ApiSIP.Model.Document;
import com.upiicsa.ApiSIP.Model.ReviewDocument;
import com.upiicsa.ApiSIP.Model.StudentProcess;
import com.upiicsa.ApiSIP.Repository.DocumentRepository;
import com.upiicsa.ApiSIP.Repository.DocumentTypeRepository;
import com.upiicsa.ApiSIP.Repository.ReviewDocumentRepository;
import com.upiicsa.ApiSIP.Repository.StudentProcessRepository;
import com.upiicsa.ApiSIP.Utils.AuthHelper;
import com.upiicsa.ApiSIP.Utils.DocumentNamingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentTypeRepository typeRepository;

    @Autowired
    private StudentProcessRepository processRepository;

    @Autowired
    private ReviewDocumentRepository reviewRepository;

    @Autowired
    private DocumentNamingUtils documentNaming;

    @Autowired
    private FileStorageService fileStorage;

    @Transactional
    public void saveDoc(MultipartFile file, String typeName) {
        Integer userId = AuthHelper.getAuthenticatedUserId();
        StudentProcess process =  processRepository.findByStudentId(userId)
                .orElseThrow(()->new IllegalArgumentException("Student not found"));

        DocumentType type = typeRepository.findByDescription(typeName).orElse(null);

        Optional<Document> document = documentRepository.findByStudentProcessAndDocumentTypeAndCancellationDateIsNull
                (process, type);

        if(document.isPresent()){
            Document currentDoc = document.get();

            ReviewDocument review = reviewRepository.findFirstByDocumentOrderByIdDesc(currentDoc);
            if (review != null) {
                if (review.getStatus().getId() == 2) {
                    throw new ValidationException("Este documento ya fue aprobado y no puede modificarse.");
                } else if (review.getStatus().getId() == 3) {
                    currentDoc.setCancellationDate(LocalDateTime.now());
                    documentRepository.save(currentDoc);
                    createNewDocument(process, type, file);
                }
            } else{
                updateDoc(currentDoc, typeName, file);
            }
        } else{
            createNewDocument(process, type, file);
        }
    }

    public void createNewDocument(StudentProcess process, DocumentType type, MultipartFile file){
        String finalName = documentNaming.generateVersionedName(process, type);

        Document newDocument = Document.builder()
                .studentProcess(process)
                .UploadDate(LocalDateTime.now())
                .URL(finalName)
                .documentType(type)
                .build();

        documentRepository.save(newDocument);
        fileStorage.store(file, finalName);
    }

    public void updateDoc(Document currentDoc, String typeName, MultipartFile file) {
        if(!currentDoc.getDocumentType().getDescription().equals(typeName)){
            throw new ValidationException("Type for document not coincided.");
        }
        currentDoc.setUploadDate(LocalDateTime.now());
        documentRepository.save(currentDoc);
        fileStorage.store(file, currentDoc.getURL());
    }
}
