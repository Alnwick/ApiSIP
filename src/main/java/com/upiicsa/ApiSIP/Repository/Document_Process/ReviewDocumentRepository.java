package com.upiicsa.ApiSIP.Repository.Document_Process;

import com.upiicsa.ApiSIP.Model.Document_Process.Document;
import com.upiicsa.ApiSIP.Model.Document_Process.ReviewDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewDocumentRepository extends JpaRepository<ReviewDocument, Integer> {

    ReviewDocument findFirstByDocumentOrderByIdDesc(Document document);
}
