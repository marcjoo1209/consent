package com.ctp.consent.api.v1.dto.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "consent_submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private ConsentTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apart_id", nullable = false)
    private Apart apart;

    @Column(name = "building", nullable = false, length = 20)
    private String building;

    @Column(name = "unit", nullable = false, length = 20)
    private String unit;

    @Column(name = "representative_name", nullable = false, length = 100)
    private String representativeName;

    @Column(name = "representative_birth", length = 8)
    private String representativeBirth;

    @Column(name = "representative_phone", nullable = false, length = 50)
    private String representativePhone;

    @Column(name = "emergency_contact", length = 50)
    private String emergencyContact;

    @Column(name = "current_address", length = 500)
    private String currentAddress;

    @Column(name = "privacy_agreement", nullable = false)
    @Builder.Default
    private Boolean privacyAgreement = false;

    @Column(name = "marketing_agreement")
    @Builder.Default
    private Boolean marketingAgreement = false;

    @Column(name = "signature_data", columnDefinition = "LONGTEXT")
    private String signatureData;

    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "SUBMITTED";

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ConsentPerson> additionalPersons = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        submittedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}