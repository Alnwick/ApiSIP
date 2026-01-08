package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.UserType;
import com.upiicsa.ApiSIP.Model.UserSIP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserSIP, Integer> {

    Optional<UserSIP> findByEmail(String email);

    Optional<UserSIP> findById(Integer id);

    @Query("SELECT u.userType FROM UserSIP u WHERE u.email = :email")
    Optional<UserType> findUserTypeByEmail(@Param("email") String email);
}
