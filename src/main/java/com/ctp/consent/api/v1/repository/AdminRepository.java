package com.ctp.consent.api.v1.repository;

import com.ctp.consent.api.v1.dto.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>, JpaSpecificationExecutor<Admin> {

    Optional<Admin> findByUsername(String username);
    Optional<Admin> findByUsernameAndActiveTrue(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}