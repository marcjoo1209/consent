package com.ctp.consent.api.v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ctp.consent.api.v1.dto.model.ConsentForm;

@Repository
public interface ConsentFormRepository extends JpaRepository<ConsentForm, Long> {

    Optional<ConsentForm> findByFormCode(String formCode);
    List<ConsentForm> findByApartIdAndActiveTrue(Long apartId);
    @Query("SELECT cf FROM ConsentForm cf WHERE cf.apart.aptCode = :aptCode AND cf.active = true")
    List<ConsentForm> findByApartCodeAndActiveTrue(@Param("aptCode") String aptCode);
    boolean existsByFormCode(String formCode);
}