package com.ctp.consent.api.v1.repository;

import com.ctp.consent.api.v1.dto.model.ConsentFormTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsentFormTemplateRepository extends JpaRepository<ConsentFormTemplate, Long> {

    Optional<ConsentFormTemplate> findByTemplateCode(String templateCode);
    List<ConsentFormTemplate> findByActiveTrue();
    List<ConsentFormTemplate> findByCategoryAndActiveTrue(String category);
    boolean existsByTemplateCode(String templateCode);
}