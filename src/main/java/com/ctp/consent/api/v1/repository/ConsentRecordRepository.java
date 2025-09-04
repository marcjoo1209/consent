package com.ctp.consent.api.v1.repository;

import com.ctp.consent.api.v1.dto.model.ConsentRecord;
import com.ctp.consent.config.enums.ConsentStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsentRecordRepository extends JpaRepository<ConsentRecord, Long>, org.springframework.data.jpa.repository.JpaSpecificationExecutor<ConsentRecord> {

    Page<ConsentRecord> findByApartId(Long apartId, Pageable pageable);
    Page<ConsentRecord> findByStatus(ConsentStatus status, Pageable pageable);
    @Query("SELECT cr FROM ConsentRecord cr WHERE cr.apart.aptCode = :aptCode AND cr.aptDong = :aptDong AND cr.aptHo = :aptHo")
    Optional<ConsentRecord> findByApartInfo(@Param("aptCode") String aptCode, @Param("aptDong") String aptDong, @Param("aptHo") String aptHo);
    List<ConsentRecord> findByAgreedAtBetween(LocalDateTime start, LocalDateTime end);
    Long countByApartIdAndStatus(Long apartId, ConsentStatus status);
    @Query("SELECT COUNT(cr) FROM ConsentRecord cr WHERE cr.apart.id = :apartId AND cr.status = 'AGREED'")
    Long countAgreedByApartId(@Param("apartId") Long apartId);
    
    Long countByApartId(Long apartId);
    Long countByStatus(ConsentStatus status);
}