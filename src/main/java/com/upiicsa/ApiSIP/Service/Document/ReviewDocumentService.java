package com.upiicsa.ApiSIP.Service.Document;

import com.upiicsa.ApiSIP.Dto.Document.ReviewDocumentDto;
import com.upiicsa.ApiSIP.Model.Document_Process.Document;
import com.upiicsa.ApiSIP.Model.Document_Process.DocumentReview;
import com.upiicsa.ApiSIP.Model.Document_Process.StudentProcess;
import com.upiicsa.ApiSIP.Model.UserSIP;
import com.upiicsa.ApiSIP.Model.Catalogs.DocumentStatus;
import com.upiicsa.ApiSIP.Repository.Document_Process.DocumentReviewRepository;
import com.upiicsa.ApiSIP.Service.StudentService;
import com.upiicsa.ApiSIP.Service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewDocumentService {

    public final DocumentReviewRepository documentReviewRepository;
    public final UserService userService;
    public final StudentService studentService;
    public final StudentProcessService processService;
    public final DocumentService documentService;
    public final DocumentUtilsService utilsService;

    public ReviewDocumentService(DocumentReviewRepository documentReviewRepository, UserService userService,
                    StudentService studentService, StudentProcessService processService,
                    DocumentService documentService, DocumentUtilsService utilsService) {
        this.documentReviewRepository = documentReviewRepository;
        this.userService = userService;
        this.studentService = studentService;
        this.processService = processService;
        this.documentService = documentService;
        this.utilsService = utilsService;
    }

    @Transactional
    public void save(Document document, UserSIP user, Boolean approved, String comment) {
        String statusDescription = approved ? "CORRECTO" : "INCORRECTO";

        DocumentStatus newStatus = utilsService.findStatusByDescription(statusDescription);
        boolean alreadyExists = documentReviewRepository.existsById(document.getId());

        if (!alreadyExists) {
            documentService.changeStatus(newStatus, document);

            DocumentReview newReview = DocumentReview.builder()
                    .document(document)
                    .user(user)
                    .approved(approved)
                    .comment(comment)
                    .reviewDate(LocalDateTime.now())
                    .build();
            documentReviewRepository.save(newReview);
        }
    }

    @Transactional
    public void performReview(String enrollment, List<ReviewDocumentDto> reviewsDto, Integer userId) {
        StudentProcess process = processService.findByEnrollment(enrollment);
        UserSIP user = userService.getUserById(userId);

        for (ReviewDocumentDto dto : reviewsDto) {
            Document doc = documentService.findDocByProcessAndType(process, dto.typeName());

            save(doc, user, dto.approved(), dto.comment());
        }
        processService.validateUpdateStatus(process, documentService.findByProcessAndStatus(process.getProcessStatus()));
    }
}
