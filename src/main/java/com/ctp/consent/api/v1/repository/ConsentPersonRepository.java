package com.ctp.consent.api.v1.repository;

import com.ctp.consent.api.v1.dto.model.ConsentPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsentPersonRepository extends JpaRepository<ConsentPerson, Long> {

    List<ConsentPerson> findByConsentRecordId(Long consentRecordId);
    List<ConsentPerson> findByConsentRecordIdOrderByOrderIndex(Long consentRecordId);
}