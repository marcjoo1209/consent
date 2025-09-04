package com.ctp.consent.api.v1.dto.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "administrators")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("관리자 ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Comment("로그인 아이디")
    private String username;

    @Column(nullable = false)
    @Comment("비밀번호 (암호화)")
    private String password;

    @Column(nullable = false, length = 50)
    @Comment("관리자 이름")
    private String name;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @Comment("권한 (SUPER_ADMIN, ADMIN)")
    private Role role;

    @Column(nullable = false)
    @Comment("활성화 상태")
    @Builder.Default
    private Boolean active = true;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Comment("생성일시")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Comment("수정일시")
    private LocalDateTime updatedAt;

    @Column
    @Comment("마지막 로그인 일시")
    private LocalDateTime lastLoginAt;

    public enum Role {
        SUPER_ADMIN,
        ADMIN
    }
}