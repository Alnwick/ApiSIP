package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Integer> {

    Optional<UserType> findByDescription(String description);
}
