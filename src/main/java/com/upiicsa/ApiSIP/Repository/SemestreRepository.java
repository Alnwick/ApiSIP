package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.Catalogs.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SemestreRepository extends JpaRepository<Semestre, Integer> {

    Optional<Semestre> findByDescripcion(String descripcion);
}
