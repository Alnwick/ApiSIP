package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.Catalogs.ProcessState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessStateRepository extends JpaRepository<ProcessState, Integer> {
    Optional<ProcessState> findByDescription(String description);
}
