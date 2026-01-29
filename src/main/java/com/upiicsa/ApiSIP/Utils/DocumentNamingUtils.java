package com.upiicsa.ApiSIP.Utils;

import com.upiicsa.ApiSIP.Model.Catalogs.DocumentType;
import com.upiicsa.ApiSIP.Model.Student;
import com.upiicsa.ApiSIP.Model.Document_Process.StudentProcess;
import com.upiicsa.ApiSIP.Repository.Document_Process.DocumentRepository;
import org.springframework.stereotype.Component;

@Component
public class DocumentNamingUtils {

    private DocumentRepository docRepository;

    public DocumentNamingUtils(DocumentRepository documentRepository) {
        this.docRepository = documentRepository;
    }

    public String generateVersionedName(StudentProcess process, DocumentType type) {
        String cleanTypeName = type.getDescription().replaceAll("\\s+", "");

        Student student = process.getStudent();
        long currentCount = docRepository.countByStudentProcessAndDocumentType(process, type);
        int nextVersion = (int) currentCount + 1;
        String extension = ".pdf";

        return String.format("%s_%s_v%d%s",
                student.getEnrollment(),
                cleanTypeName,
                nextVersion,
                extension);
    }
    public String generateCertificateName(){
        return "certificate";
    }
}
