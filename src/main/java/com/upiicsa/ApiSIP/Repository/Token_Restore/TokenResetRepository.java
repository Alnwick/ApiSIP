package com.upiicsa.ApiSIP.Repository.Token_Restore;

import com.upiicsa.ApiSIP.Model.Token_Restore.TokenReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenResetRepository extends JpaRepository<TokenReset, Integer> {

    Optional<TokenReset> findByToken(String token);
}
