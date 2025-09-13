package com.ctp.consent.api.v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ctp.consent.api.v1.dto.model.ConsentTemplate;

@Repository
public interface ConsentTemplateRepository extends JpaRepository<ConsentTemplate, Long>, JpaSpecificationExecutor<ConsentTemplate> {

    List<ConsentTemplate> findByActiveTrue();

    Optional<ConsentTemplate> findByIdAndActiveTrue(Long id);

    @Query("SELECT ct FROM ConsentTemplate ct WHERE ct.active = true ORDER BY ct.createdAt DESC")
    Page<ConsentTemplate> findActiveTemplates(Pageable pageable);

    @Query("SELECT ct FROM ConsentTemplate ct " +
           "JOIN ct.apartTemplates at " +
           "WHERE at.apart.id = :apartId AND at.active = true AND ct.active = true")
    List<ConsentTemplate> findByApartIdAndActive(@Param("apartId") Long apartId);

    @Query("SELECT ct FROM ConsentTemplate ct " +
           "JOIN ct.apartTemplates at " +
           "WHERE at.apart.aptCode = :apartCode AND at.active = true AND ct.active = true")
    List<ConsentTemplate> findByApartCodeAndActive(@Param("apartCode") String apartCode);

    @Query("SELECT ct FROM ConsentTemplate ct " +
           "JOIN ct.apartTemplates at " +
           "WHERE at.apart.aptCode = :apartCode AND ct.id = :templateId " +
           "AND at.active = true AND ct.active = true")
    Optional<ConsentTemplate> findByApartCodeAndTemplateId(@Param("apartCode") String apartCode,
                                                          @Param("templateId") Long templateId);

    boolean existsByTitle(String title);

    @Query("SELECT COUNT(ct) FROM ConsentTemplate ct WHERE ct.active = true")
    long countActiveTemplates();
}