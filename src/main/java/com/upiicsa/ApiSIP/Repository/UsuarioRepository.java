package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.TipoUsuario;
import com.upiicsa.ApiSIP.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreo(String correo);

    @Query("SELECT u.tipoUsuario FROM Usuario u WHERE u.correo = :correo")
    Optional<TipoUsuario> findTipoUsuarioByCorreo(@Param("correo") String correo);
}
