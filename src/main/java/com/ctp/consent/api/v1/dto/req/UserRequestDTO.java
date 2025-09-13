package com.ctp.consent.api.v1.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {

    private Long id; // 수정 시 필요

    @NotBlank(message = "사용자명은 필수입니다")
    @Size(min = 4, max = 20, message = "사용자명은 4-20자 사이여야 합니다")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "사용자명은 영문, 숫자, 언더스코어만 가능합니다")
    private String username;

    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    private String password; // 신규 등록 시 필수, 수정 시 선택

    private String confirmPassword; // 비밀번호 확인

    private String currentPassword; // 프로필 수정 시 현재 비밀번호

    @NotBlank(message = "이름은 필수입니다")
    @Size(min = 2, max = 50, message = "이름은 2-50자 사이여야 합니다")
    private String name;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다 (예: 010-1234-5678)")
    private String phoneNumber;

    @NotBlank(message = "권한은 필수입니다")
    private String role; // ADMIN, SUPER_ADMIN

    private Boolean active;
}