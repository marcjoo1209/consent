package com.ctp.consent.api.v1.dto.req;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsentSubmissionRequestDTO {

    @NotBlank(message = "동은 필수입니다")
    @Size(max = 20, message = "동은 20자 이하로 입력해주세요")
    private String building;

    @NotBlank(message = "호수는 필수입니다")
    @Size(max = 20, message = "호수는 20자 이하로 입력해주세요")
    private String unit;

    @NotBlank(message = "대표자 이름은 필수입니다")
    @Size(max = 100, message = "이름은 100자 이하로 입력해주세요")
    private String representativeName;

    @Pattern(regexp = "^\\d{8}$", message = "생년월일은 8자리 숫자로 입력해주세요 (YYYYMMDD)")
    private String representativeBirth;

    @NotBlank(message = "대표자 연락처는 필수입니다")
    @Pattern(regexp = "^\\d{10,11}$", message = "연락처는 10-11자리 숫자로 입력해주세요")
    private String representativePhone;

    @Pattern(regexp = "^\\d{10,11}$", message = "비상연락처는 10-11자리 숫자로 입력해주세요")
    private String emergencyContact;

    @Size(max = 500, message = "주소는 500자 이하로 입력해주세요")
    private String currentAddress;

    @NotNull(message = "개인정보 수집 동의는 필수입니다")
    private Boolean privacyAgreement;

    private Boolean marketingAgreement;

    private String signatureData;

    @Builder.Default
    private List<AdditionalPersonDTO> additionalPersons = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AdditionalPersonDTO {

        @Size(max = 100, message = "이름은 100자 이하로 입력해주세요")
        private String name;

        @Pattern(regexp = "^\\d{8}$", message = "생년월일은 8자리 숫자로 입력해주세요 (YYYYMMDD)")
        private String birthDate;

        @Pattern(regexp = "^\\d{10,11}$", message = "연락처는 10-11자리 숫자로 입력해주세요")
        private String phoneNumber;

        @Size(max = 50, message = "관계는 50자 이하로 입력해주세요")
        private String relation;
    }
}