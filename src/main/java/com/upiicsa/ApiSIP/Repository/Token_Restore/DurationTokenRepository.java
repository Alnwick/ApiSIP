package com.upiicsa.ApiSIP.Repository.Token_Restore;

import com.upiicsa.ApiSIP.Model.UserType;
import com.upiicsa.ApiSIP.Model.Token_Restore.DurationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DurationTokenRepository extends JpaRepository<DurationToken, Integer> {

    @Query("SELECT d.hours FROM DurationToken d WHERE d.user = :typeUser")
    Integer getHoursByUserType(@Param("typeUser") UserType userType);
}
