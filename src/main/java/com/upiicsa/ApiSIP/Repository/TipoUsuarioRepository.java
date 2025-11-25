package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Integer> {

    Optional<TipoUsuario> findByDescripcion(String descripcion);
}
