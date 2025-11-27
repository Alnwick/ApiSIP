package com.upiicsa.ApiSIP.Repository.Token_Restore;

import com.upiicsa.ApiSIP.Model.UserType;
import com.upiicsa.ApiSIP.Model.Token_Restore.DuracionToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DuracionTokenRepository extends JpaRepository<DuracionToken, Integer> {

    @Query("SELECT d.duracionHoras FROM DuracionToken d WHERE d.tipoUsuario = :tipoUsuario")
    Integer getDuracionHorasByTipoUsuario(@Param("tipoUsuario") UserType userType);
}
