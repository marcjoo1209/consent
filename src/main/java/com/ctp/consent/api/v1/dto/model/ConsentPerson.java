package com.ctp.consent.api.v1.dto.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Comment;

import com.ctp.consent.config.enums.PersonRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
 * 동의서 참여자(거주자) 정보를 관리하는 엔티티
 * - 한 동의서에 여러 명의 참여자 정보 저장
 * - 각 참여자별 개인정보, 주소, 서명, 첨부파일 관리
 * - 소유자, 공동소유자, 가족 등 역할 구분
 */
@Entity
@Table(name = "consent_person")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ConsentPerson extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("참여자 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consent_record_id", nullable = false)
    @Comment("동의서")
    private ConsentRecord consentRecord;

    @Column(nullable = false, length = 100)
    @Comment("이름")
    private String name;

    @Column(length = 8)
    @Comment("생년월일 (YYYYMMDD)")
    private String birthDate;

    @Column(nullable = false, length = 50)
    @Comment("연락처")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Comment("역할")
    @Builder.Default
    private PersonRole role = PersonRole.OWNER;

    @Comment("순서")
    @Builder.Default
    private Integer orderIndex = 1;

    @Embedded
    @Comment("주소")
    private Address address;

    @Lob
    @Column(columnDefinition = "TEXT")
    @Comment("서명 데이터 (Base64)")
    private String signatureData;

    @Column
    @Comment("서명 시간")
    private LocalDateTime signedAt;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Attachment> attachments = new ArrayList<>();

    public String getRelation() {
        return this.role != null ? this.role.getDescription() : "";
    }
    
    public String getSignatureUrl() {
        return this.signatureData != null ? "/api/consent/signature/" + this.id : null;
    }
    
    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
        attachment.setPerson(this);
    }

    public void removeAttachment(Attachment attachment) {
        attachments.remove(attachment);
        attachment.setPerson(null);
    }

    public void sign(String signatureData) {
        this.signatureData = signatureData;
        this.signedAt = LocalDateTime.now();
    }

    public boolean isSigned() {
        return signatureData != null && !signatureData.isEmpty();
    }
}