package com.ctp.consent.api.v1.repository;

import com.ctp.consent.api.v1.dto.model.ConsentRecord;
import com.ctp.consent.api.v1.dto.model.ConsentStatus;
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
public interface ConsentRecordRepository extends JpaRepository<ConsentRecord, Long> {
    
    Page<ConsentRecord> findByApartmentId(Long apartmentId, Pageable pageable);
    
    Page<ConsentRecord> findByStatus(ConsentStatus status, Pageable pageable);
    
    @Query("SELECT cr FROM ConsentRecord cr WHERE cr.apartment.aptCode = :aptCode AND cr.aptDong = :aptDong AND cr.aptHo = :aptHo")
    Optional<ConsentRecord> findByApartmentInfo(@Param("aptCode") String aptCode, 
                                                @Param("aptDong") String aptDong, 
                                                @Param("aptHo") String aptHo);
    
    List<ConsentRecord> findByAgreedAtBetween(LocalDateTime start, LocalDateTime end);
    
    Long countByApartmentIdAndStatus(Long apartmentId, ConsentStatus status);
    
    @Query("SELECT COUNT(cr) FROM ConsentRecord cr WHERE cr.apartment.id = :apartmentId AND cr.status = 'AGREED'")
    Long countAgreedByApartmentId(@Param("apartmentId") Long apartmentId);
}