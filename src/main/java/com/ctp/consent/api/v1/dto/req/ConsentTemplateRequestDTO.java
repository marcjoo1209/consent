package com.ctp.consent.api.v1.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsentTemplateRequestDTO {

    @NotBlank(message = "템플릿 제목은 필수입니다")
    @Size(max = 200, message = "템플릿 제목은 200자 이하로 입력해주세요")
    private String title;

    @Size(max = 1000, message = "설명은 1000자 이하로 입력해주세요")
    private String description;

    @NotBlank(message = "템플릿 내용은 필수입니다")
    private String content;

    @Builder.Default
    private Boolean active = true;
}