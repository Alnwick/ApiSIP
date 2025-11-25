package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.Catalogs.Estatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstatusRepository extends JpaRepository<Estatus, Integer> {

    Optional<Estatus> findByDescripcion(String descripcion);
}
