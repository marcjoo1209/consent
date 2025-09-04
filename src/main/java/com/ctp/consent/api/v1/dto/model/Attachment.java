package com.ctp.consent.api.v1.dto.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

/**
 * 동의서 첨부파일을 관리하는 엔티티
 * - 참여자별 첨부파일 관리 (등기부등본, 인감증명서 등)
 * - 원본 파일명과 서버 저장 파일명 분리 관리
 * - 파일 타입 및 크기 정보 저장
 */
@Entity
@Table(name = "attachments")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Attachment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("첨부파일 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    @Comment("참여자")
    private ConsentPerson person;

    @Column(nullable = false, length = 255)
    @Comment("원본 파일명")
    private String originalFileName;

    @Column(nullable = false, length = 255)
    @Comment("서버 파일명")
    private String serverFileName;

    @Comment("파일 크기 (bytes)")
    private Long fileSize;

    @Column(length = 50)
    @Comment("파일 유형 (등기부등본, 인감증명서 등)")
    private String fileType;

    @Column(length = 100)
    @Comment("MIME 타입")
    private String mimeType;
}