package com.upiicsa.ApiSIP.Repository.Catalogs;

import com.upiicsa.ApiSIP.Model.Catalogs.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {

    Optional<School> findSchoolByAcronym(String acronym);
}
