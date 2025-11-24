package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.Catalogs.Estatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstatusRepository extends JpaRepository<Estatus, Integer> {

    Optional<Estatus> findByDescripcion(String descripcion);
}
