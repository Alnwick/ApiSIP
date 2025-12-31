package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.Catalogs.DocumentType;
import com.upiicsa.ApiSIP.Model.Document;
import com.upiicsa.ApiSIP.Model.StudentProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    Optional<Document> findByStudentProcessAndDocumentTypeAndCancellationDateIsNull
            (StudentProcess process, DocumentType type);

    Integer countByStudentProcessAndDocumentType(StudentProcess process, DocumentType type);
}
