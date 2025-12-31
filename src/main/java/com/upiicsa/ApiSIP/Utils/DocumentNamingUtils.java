package com.upiicsa.ApiSIP.Utils;

import com.upiicsa.ApiSIP.Model.Catalogs.DocumentType;
import com.upiicsa.ApiSIP.Model.Student;
import com.upiicsa.ApiSIP.Model.StudentProcess;
import com.upiicsa.ApiSIP.Repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentNamingUtils {

    @Autowired
    private DocumentRepository docRepository;

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
}
