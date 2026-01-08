package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.History;
import com.upiicsa.ApiSIP.Model.StudentProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

    List<History> findByProcessOrderByUpdateDateAsc(StudentProcess process);
}
