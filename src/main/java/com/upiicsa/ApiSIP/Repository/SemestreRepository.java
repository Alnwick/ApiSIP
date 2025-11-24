package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.Catalogs.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SemestreRepository extends JpaRepository<Semestre, Integer> {

    Optional<Semestre> findByDescripcion(String descripcion);
}
