package com.ctp.consent.api.v1.dto.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

/**
 * 아파트별 커스터마이징된 동의서 양식을 관리하는 엔티티
 * - 템플릿 기반으로 아파트별 맞춤 동의서 생성
 * - 서명/첨부파일 필수 여부 설정
 * - 최소 참여자 수 등 양식별 규칙 관리
 */
@Entity
@Table(name = "consent_forms")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@SoftDelete(strategy = SoftDeleteType.DELETED, columnName = "deleted_at")
public class ConsentForm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("동의서 양식 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id", nullable = false)
    @Comment("아파트")
    private Apartment apartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    @Comment("기반 템플릿 (nullable)")
    private ConsentFormTemplate template;

    @Column(nullable = false, unique = true, length = 30)
    @Comment("양식 코드")
    private String formCode;

    @Column(nullable = false, length = 200)
    @Comment("양식명")
    private String formName;

    @Lob
    @Column(columnDefinition = "TEXT")
    @Comment("HTML 내용")
    private String htmlContent;

    @Column(nullable = false)
    @Comment("서명 필수 여부")
    @Builder.Default
    private Boolean requiresSignature = true;

    @Column(nullable = false)
    @Comment("첨부파일 필수 여부")
    @Builder.Default
    private Boolean requiresAttachment = false;

    @Comment("최소 참여자 수")
    @Builder.Default
    private Integer minPersons = 1;

    @Column(nullable = false)
    @Comment("활성화 여부")
    @Builder.Default
    private Boolean active = true;
}