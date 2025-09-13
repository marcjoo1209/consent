package com.ctp.consent.api.v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ctp.consent.api.v1.dto.model.ApartConsentTemplate;

@Repository
public interface ApartConsentTemplateRepository extends JpaRepository<ApartConsentTemplate, Long> {

    List<ApartConsentTemplate> findByApartId(Long apartId);

    List<ApartConsentTemplate> findByTemplateId(Long templateId);

    List<ApartConsentTemplate> findByApartIdAndActiveTrue(Long apartId);

    Optional<ApartConsentTemplate> findByApartIdAndTemplateId(Long apartId, Long templateId);

    @Query("SELECT act FROM ApartConsentTemplate act " +
            "WHERE act.apart.aptCode = :apartCode AND act.template.id = :templateId " +
            "AND act.active = true")
    Optional<ApartConsentTemplate> findByApartCodeAndTemplateId(@Param("apartCode") String apartCode, @Param("templateId") Long templateId);

    boolean existsByApartIdAndTemplateIdAndActiveTrue(Long apartId, Long templateId);

    @Query("SELECT COUNT(act) FROM ApartConsentTemplate act WHERE act.active = true")
    long countActiveAssignments();

    void deleteByApartIdAndTemplateId(Long apartId, Long templateId);
}