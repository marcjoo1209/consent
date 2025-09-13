package com.ctp.consent.api.v1.dto.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "consent_persons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsentPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private ConsentSubmission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consent_record_id")
    private ConsentRecord consentRecord;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "birth_date", length = 8)
    private String birthDate;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Column(name = "relation", length = 50)
    private String relation;

    @Column(name = "signature_data", columnDefinition = "LONGTEXT")
    private String signatureData;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}