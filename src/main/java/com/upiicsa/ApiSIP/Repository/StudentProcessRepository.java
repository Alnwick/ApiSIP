package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.StudentProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentProcessRepository extends JpaRepository<StudentProcess, Integer> {

    Optional<StudentProcess> findByStudentId(Integer userId);
}
