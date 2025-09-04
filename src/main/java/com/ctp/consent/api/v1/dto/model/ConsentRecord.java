package com.ctp.consent.api.v1.dto.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Comment;

import com.ctp.consent.config.enums.ConsentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 실제 작성된 동의서 기록을 관리하는 핵심 엔티티
 * - 동의서 작성 상태 관리 (임시저장, 제출, 동의, 거부)
 * - 아파트 동/호수별 동의서 관리
 * - 참여자 정보와 연결하여 세대별 동의 현황 추적
 */
@Entity
@Table(name = "consent_records")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ConsentRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("동의서 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apart_id", nullable = false)
    @Comment("아파트")
    private Apart apart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consent_form_id", nullable = false)
    @Comment("동의서 양식")
    private ConsentForm consentForm;

    @Column(nullable = false, length = 20)
    @Comment("동")
    private String aptDong;

    @Column(nullable = false, length = 20)
    @Comment("호수")
    private String aptHo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Comment("상태")
    @Builder.Default
    private ConsentStatus status = ConsentStatus.DRAFT;

    @Column
    @Comment("개인정보 동의")
    private Boolean agreePrivacy;

    @Column
    @Comment("제출 시간")
    private LocalDateTime submittedAt;

    @Column
    @Comment("동의 시간")
    private LocalDateTime agreedAt;

    @Column(length = 500)
    @Comment("거부 사유")
    private String rejectReason;

    @Column
    @Comment("승인 시간")
    private LocalDateTime approvedAt;

    @Column
    @Comment("반려 시간")
    private LocalDateTime rejectedAt;

    @Column(length = 500)
    @Comment("승인 코멘트")
    private String approvalComment;

    @Column(length = 500)
    @Comment("반려 사유")
    private String rejectionReason;

    @OneToMany(mappedBy = "consentRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ConsentPerson> persons = new ArrayList<>();

    public void addPerson(ConsentPerson person) {
        persons.add(person);
        person.setConsentRecord(this);
    }

    public void removePerson(ConsentPerson person) {
        persons.remove(person);
        person.setConsentRecord(null);
    }

    public void submit() {
        this.status = ConsentStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
    }

    public void agree() {
        this.status = ConsentStatus.AGREED;
        this.agreedAt = LocalDateTime.now();
    }

    public void reject(String reason) {
        this.status = ConsentStatus.REJECTED;
        this.rejectReason = reason;
    }
}