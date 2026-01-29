package com.upiicsa.ApiSIP.Repository.Catalogs;

import com.upiicsa.ApiSIP.Model.Catalogs.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CareerRepository extends JpaRepository<Career, Integer> {

    Optional<Career> findCareerByAcronym(String acronym);
}
