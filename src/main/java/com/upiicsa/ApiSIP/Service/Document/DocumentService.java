package com.upiicsa.ApiSIP.Service.Document;

import com.upiicsa.ApiSIP.Dto.Document.DocumentStatusDto;
import com.upiicsa.ApiSIP.Exception.BusinessException;
import com.upiicsa.ApiSIP.Model.Catalogs.DocumentType;
import com.upiicsa.ApiSIP.Model.Document_Process.Document;
import com.upiicsa.ApiSIP.Model.Document_Process.DocumentReview;
import com.upiicsa.ApiSIP.Model.Document_Process.StudentProcess;
import com.upiicsa.ApiSIP.Model.Enum.ErrorCode;
import com.upiicsa.ApiSIP.Model.UserSIP;
import com.upiicsa.ApiSIP.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final DocumentPersistenceService persistenceService;
    private final UserRepository userRepository;
    private final DocumentUtilsService utilsService;
    private final StudentProcessService processService;

    public DocumentService(DocumentPersistenceService persistenceService,UserRepository userRepository,
                           DocumentUtilsService utilsService, StudentProcessService processService) {

        this.persistenceService = persistenceService;
        this.userRepository = userRepository;
        this.utilsService = utilsService;
        this.processService = processService;
    }

    @Transactional
    public void saveDoc(MultipartFile file, String typeName, Integer userId) {
        StudentProcess process = processService.findByStudentId(userId);
        DocumentType type = utilsService.findTypeByDescription(typeName);

        Document doc = persistenceService.findDocByProcessAndType(process, typeName);

        if(doc != null){

            switch (doc.getDocumentStatus().getDescription()) {
                case "CORRECTO":
                    throw new BusinessException(ErrorCode.DOCUMENT_ALREADY_APPROVED);
                case "INCORRECTO":
                    persistenceService.cancelDocument(doc);
                    persistenceService.createDocument(process, type, file);
                    break;
                case "PENDIENTE":
                    if(!doc.getDocumentType().getDescription().equals(typeName))
                        throw new BusinessException(ErrorCode.INTERNAL_ERROR);
                    persistenceService.updateDocument(doc, file);
                    break;
                default:
                    throw new BusinessException(ErrorCode.INTERNAL_ERROR);
            }
        } else {
            persistenceService.createDocument(process, type, file);
        }
        if (process.getProcessStatus().getId() == 1)
            processService.updateStatus(process);
    }

    @Transactional(readOnly = true)
    public DocumentStatusDto getLetter(Integer userId){
        StudentProcess process = processService.findByStudentId(userId);
        Document doc = persistenceService.findDocByProcessAndType(process, "CARTA_PRESENTACION");

        if(doc == null) throw new BusinessException(ErrorCode.RESOURCE_NOT_FOUND);

        return new DocumentStatusDto(doc.getDocumentType().getDescription(),
                doc.getDocumentStatus().getDescription(),
                doc.getURL(), " ",
                "/view-document" + doc.getURL(), doc.getUploadDate());
    }

    @Transactional
    public void saveLetter(MultipartFile file, String enrollment, Integer userId) {
        UserSIP user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        StudentProcess process = processService.findByEnrollment(enrollment);
        persistenceService.createLetter(user, process, file);
    }

    @Transactional(readOnly = true)
    public List<DocumentStatusDto> getDocuments(Integer userId, String processStatus) {
        StudentProcess process = processService.findByStudentId(userId);
        List<DocumentType> requiredTypes = utilsService.findRequiredTypesStatus(processStatus);

        List<Document> activeDocuments = persistenceService.findActiveDocumentsOrdered(process);

        Map<DocumentType, Document> activeDocsMap = activeDocuments.stream()
                .collect(Collectors.toMap(
                        Document::getDocumentType,
                        doc -> doc,
                        (doc1, doc2) -> getHighestPriorityDoc(doc1, doc2)
                ));

        return requiredTypes.stream().map(type -> {
            Document targetDoc = activeDocsMap.get(type);

            if (targetDoc == null) {
                return new DocumentStatusDto(type.getDescription(), "SIN_CARGA", null, "",
                        "", null);
            }

            DocumentReview docReview = utilsService.getReviewByDocument(targetDoc);
            String comment = (docReview != null && docReview.getComment() != null) ? docReview.getComment() : "";

            return new DocumentStatusDto(type.getDescription(), targetDoc.getDocumentStatus().getDescription(),
                    targetDoc.getURL(), comment, "/view-document/" + targetDoc.getURL(),
                    targetDoc.getUploadDate()
            );
        }).collect(Collectors.toList());
    }

    private Document getHighestPriorityDoc(Document doc1, Document doc2) {
        String status1 = doc1.getDocumentStatus().getDescription();
        String status2 = doc2.getDocumentStatus().getDescription();

        if (status1.equals("CORRECTO") || status2.equals("CORRECTO")) {
            return status1.equals("CORRECTO") ? doc1 : doc2;
        }
        if (status1.equals("PENDIENTE") || status2.equals("PENDIENTE")) {
            return status1.equals("PENDIENTE") ? doc1 : doc2;
        }
        return doc1.getUploadDate().isAfter(doc2.getUploadDate()) ? doc1 : doc2;
    }
}
