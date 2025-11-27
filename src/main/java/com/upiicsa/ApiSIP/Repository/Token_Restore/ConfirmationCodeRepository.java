package com.upiicsa.ApiSIP.Repository.Token_Restore;

import com.upiicsa.ApiSIP.Model.Token_Restore.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Integer> {

    Optional<ConfirmationCode> findByCode(String code);
}
