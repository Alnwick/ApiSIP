package com.upiicsa.ApiSIP.Repository.Catalogs;

import com.upiicsa.ApiSIP.Model.Catalogs.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Integer> {

    Optional<DocumentType> findByDescription(String description);
}
