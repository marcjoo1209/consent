package com.ctp.consent.api.v1.dto.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

/**
 * 동의서 기본 템플릿을 관리하는 엔티티
 * - 재건축, 리모델링 등 카테고리별 템플릿 제공
 * - HTML 형식의 동의서 양식 저장
 * - 아파트별 커스터마이징의 기반이 되는 템플릿
 */
@Entity
@Table(name = "consent_form_templates")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@SoftDelete(strategy = SoftDeleteType.DELETED, columnName = "deleted_at")
public class ConsentFormTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("템플릿 ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    @Comment("템플릿 코드")
    private String templateCode;

    @Column(nullable = false, length = 100)
    @Comment("템플릿명")
    private String templateName;

    @Column(length = 50)
    @Comment("카테고리 (재건축, 리모델링 등)")
    private String category;

    @Lob
    @Column(columnDefinition = "TEXT")
    @Comment("HTML 템플릿 내용")
    private String htmlContent;

    @Column(length = 500)
    @Comment("템플릿 설명")
    private String description;

    @Column(nullable = false)
    @Comment("활성화 여부")
    @Builder.Default
    private Boolean active = true;
}