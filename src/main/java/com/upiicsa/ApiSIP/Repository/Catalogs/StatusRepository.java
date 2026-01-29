package com.upiicsa.ApiSIP.Repository.Catalogs;

import com.upiicsa.ApiSIP.Model.Catalogs.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {

    Optional<Status> findByDescription(String description);
}
