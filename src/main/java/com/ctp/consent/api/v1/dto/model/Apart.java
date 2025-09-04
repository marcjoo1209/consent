package com.ctp.consent.api.v1.dto.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

/**
 * 아파트 정보를 관리하는 엔티티
 * - 아파트 기본 정보 (이름, 주소, 동/세대수)
 * - 동의서 작성 대상 아파트 관리
 * - 각 아파트별 동의서 양식 연결
 */
@Entity
@Table(name = "aparts")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Apart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("아파트 ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    @Comment("아파트 코드")
    private String aptCode;

    @Column(nullable = false, length = 100)
    @Comment("아파트명")
    private String aptName;

    @Column(length = 200)
    @Comment("주소")
    private String address;

    @Comment("총 동 수")
    private Integer totalDong;

    @Comment("총 세대수")
    private Integer totalHousehold;

    @Column(nullable = false)
    @Comment("활성화 여부")
    @Builder.Default
    private Boolean active = true;
}