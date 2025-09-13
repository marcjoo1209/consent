package com.ctp.consent.api.v1.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ctp.consent.api.v1.dto.model.ConsentSubmission;

@Repository
public interface ConsentSubmissionRepository extends JpaRepository<ConsentSubmission, Long>, JpaSpecificationExecutor<ConsentSubmission> {

    Page<ConsentSubmission> findByApartIdOrderBySubmittedAtDesc(Long apartId, Pageable pageable);

    Page<ConsentSubmission> findByStatusOrderBySubmittedAtDesc(String status, Pageable pageable);

    List<ConsentSubmission> findByApartIdAndBuildingAndUnit(Long apartId, String building, String unit);

    @Query("SELECT cs FROM ConsentSubmission cs WHERE cs.apart.aptCode = :apartCode " +
           "AND cs.building = :building AND cs.unit = :unit " +
           "ORDER BY cs.submittedAt DESC")
    List<ConsentSubmission> findByApartCodeAndBuildingAndUnit(@Param("apartCode") String apartCode,
                                                              @Param("building") String building,
                                                              @Param("unit") String unit);

    @Query("SELECT COUNT(cs) FROM ConsentSubmission cs WHERE cs.status = :status")
    long countByStatus(@Param("status") String status);

    @Query("SELECT COUNT(cs) FROM ConsentSubmission cs WHERE cs.submittedAt BETWEEN :startDate AND :endDate")
    long countBySubmittedAtBetween(@Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT cs FROM ConsentSubmission cs " +
           "WHERE cs.representativeName LIKE %:name% " +
           "ORDER BY cs.submittedAt DESC")
    Page<ConsentSubmission> findByRepresentativeNameContaining(@Param("name") String name, Pageable pageable);

    @Query("SELECT cs FROM ConsentSubmission cs " +
           "WHERE cs.representativePhone = :phone " +
           "ORDER BY cs.submittedAt DESC")
    List<ConsentSubmission> findByRepresentativePhone(@Param("phone") String phone);

    Optional<ConsentSubmission> findTopByApartIdAndBuildingAndUnitOrderBySubmittedAtDesc(
            Long apartId, String building, String unit);

    @Query("SELECT cs.apart.aptName as apartName, COUNT(cs) as count " +
           "FROM ConsentSubmission cs " +
           "GROUP BY cs.apart.id, cs.apart.aptName")
    List<Object[]> countByApart();

    @Query("SELECT DATE(cs.submittedAt) as date, COUNT(cs) as count " +
           "FROM ConsentSubmission cs " +
           "WHERE cs.submittedAt >= :startDate " +
           "GROUP BY DATE(cs.submittedAt) " +
           "ORDER BY DATE(cs.submittedAt)")
    List<Object[]> countByDateSince(@Param("startDate") LocalDateTime startDate);
}