package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.TipoUsuario;
import com.upiicsa.ApiSIP.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    Optional<TipoUsuario> findTipoUsuarioByEmail(String email);
}
