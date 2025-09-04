package com.ctp.consent.api.v1.repository;

import com.ctp.consent.api.v1.dto.model.Apart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApartRepository extends JpaRepository<Apart, Long> {

    Optional<Apart> findByAptCode(String aptCode);
    boolean existsByAptCode(String aptCode);
    Optional<Apart> findByAptCodeAndActiveTrue(String aptCode);
}