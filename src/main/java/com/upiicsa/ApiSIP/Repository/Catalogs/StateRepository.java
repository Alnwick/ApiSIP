package com.upiicsa.ApiSIP.Repository.Catalogs;

import com.upiicsa.ApiSIP.Model.Catalogs.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State,Integer> {
    Optional<State> findById(Integer id);

}
