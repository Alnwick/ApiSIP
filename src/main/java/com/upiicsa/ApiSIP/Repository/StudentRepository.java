package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("SELECT s FROM Student s JOIN s.offer o JOIN o.career c WHERE c.acronym = :acronym")
    List<Student> findAllByCareerAcronym(@Param("acronym") String acronym);
}
