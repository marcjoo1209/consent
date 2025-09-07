package com.ctp.consent.api.v1.repository;

import com.ctp.consent.api.v1.dto.model.Apart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApartRepository extends JpaRepository<Apart, Long>, JpaSpecificationExecutor<Apart> {
    
    boolean existsByAptCode(String aptCode);
    
    Optional<Apart> findByAptCode(String aptCode);
    
    List<Apart> findByActiveTrue();
    
    List<Apart> findByAptNameContainingIgnoreCaseOrAptCodeContainingIgnoreCase(String name, String code);
}