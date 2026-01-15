package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Dto.Document.DocumentStatusDto;
import com.upiicsa.ApiSIP.Exception.ValidationException;
import com.upiicsa.ApiSIP.Model.Catalogs.DocumentType;
import com.upiicsa.ApiSIP.Model.Document;
import com.upiicsa.ApiSIP.Model.ReviewDocument;
import com.upiicsa.ApiSIP.Model.StudentProcess;
import com.upiicsa.ApiSIP.Repository.DocumentRepository;
import com.upiicsa.ApiSIP.Repository.ReviewDocumentRepository;
import com.upiicsa.ApiSIP.Utils.DocumentNamingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ReviewDocumentRepository reviewRepository;

    @Autowired
    private DocumentTypeService docTypeService;

    @Autowired
    private StudentProcessService processService;

    @Autowired
    private DocumentNamingUtils documentNaming;

    @Autowired
    private FileStorageService fileStorage;

    @Transactional
    public void saveDoc(MultipartFile file, String typeName, Integer userId) {
        StudentProcess process = processService.getByStudentId(userId);

        DocumentType type = docTypeService.getByDescription(typeName);

        Optional<Document> document = documentRepository.findByStudentProcessAndDocumentTypeAndCancellationDateIsNull
                (process, type);

        if(document.isPresent()){
            Document currentDoc = document.get();

            ReviewDocument review = reviewRepository.findFirstByDocumentOrderByIdDesc(currentDoc);
            if (review != null) {
                if (review.getStatus().getId() == 2) {
                    throw new ValidationException("Este documento ya fue aprobado y no puede modificarse.");
                } else if (review.getStatus().getId() == 3) {
                    cancelledAndCreated(currentDoc, type, file, process);
                }
            } else{
                updateDoc(currentDoc, typeName, file);
            }
        } else{
            createNewDocument(process, type, file);
        }
        processService.updateProcessStatus(process.getId(), 2);
    }

    @Transactional(readOnly = true)
    public List<DocumentStatusDto> getStatus(Integer userId) {

        StudentProcess process = processService.getByStudentId(userId);

        List<DocumentType> requiredTypes = docTypeService.getRequiredTypes(process);

        return requiredTypes.stream().map(type -> {

            Optional<Document> docOpt = documentRepository
                    .findByStudentProcessAndDocumentTypeAndCancellationDateIsNull(process, type);

            if (docOpt.isPresent()) {
                Document doc = docOpt.get();
                ReviewDocument review = reviewRepository.findFirstByDocumentOrderByIdDesc(doc);

                if (review != null) {
                    return new DocumentStatusDto(type.getDescription(),
                            review.getStatus().getId() == 2 ? "REVISADO_CORRECTO" : "REVISADO_INCORRECTO",
                            doc.getURL(), review.getComment(), "/view-document/" + doc.getURL()
                    );
                }
                return new DocumentStatusDto(type.getDescription(), "EN_REVISION", doc.getURL(), "", "");
            }
            return new DocumentStatusDto(type.getDescription(), "PENDING", null, "Pendiente de cargar", "");

        }).collect(Collectors.toList());
    }

    @Transactional
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

    @Transactional
    public void updateDoc(Document currentDoc, String typeName, MultipartFile file) {
        if(!currentDoc.getDocumentType().getDescription().equals(typeName)){
            throw new ValidationException("Type for document not coincided.");
        }
        currentDoc.setUploadDate(LocalDateTime.now());
        documentRepository.save(currentDoc);
        fileStorage.store(file, currentDoc.getURL());
    }

    @Transactional
    public void cancelledAndCreated(Document currentDoc, DocumentType type,  MultipartFile file, StudentProcess process) {
        currentDoc.setCancellationDate(LocalDateTime.now());
        documentRepository.save(currentDoc);
        createNewDocument(process, type, file);
    }
}
