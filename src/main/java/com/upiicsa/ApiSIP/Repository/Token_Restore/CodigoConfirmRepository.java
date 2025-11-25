package com.upiicsa.ApiSIP.Repository.Token_Restore;

import com.upiicsa.ApiSIP.Model.Token_Restore.CodigoConfirm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodigoConfirmRepository extends JpaRepository<CodigoConfirm, Integer> {

    Optional<CodigoConfirm> findByCodigo(String code);

    Optional<CodigoConfirm> findByUsuarioId(Integer usuarioId);
}
