package com.ctp.consent.api.v1.repository;

import com.ctp.consent.api.v1.dto.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    
    Optional<Apartment> findByAptCode(String aptCode);
    
    boolean existsByAptCode(String aptCode);
    
    Optional<Apartment> findByAptCodeAndActiveTrue(String aptCode);
}