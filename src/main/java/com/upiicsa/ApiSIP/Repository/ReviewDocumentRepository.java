package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.Document;
import com.upiicsa.ApiSIP.Model.ReviewDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewDocumentRepository extends JpaRepository<ReviewDocument, Integer> {

    ReviewDocument findFirstByDocumentOrderByIdDesc(Document document);
}
