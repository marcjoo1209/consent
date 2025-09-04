package com.ctp.consent.api.v1.repository;

import com.ctp.consent.api.v1.dto.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByPersonId(Long personId);
    void deleteByPersonId(Long personId);
}