package com.upiicsa.ApiSIP.Repository.Token_Restore;

import com.upiicsa.ApiSIP.Model.TipoUsuario;
import com.upiicsa.ApiSIP.Model.Token_Restore.DuracionToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DuracionTokenRepository extends JpaRepository<DuracionToken, Integer> {

    Integer getDuracionTokenByTipoUsuario(TipoUsuario tipoUsuario);
}
