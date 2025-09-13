package com.ctp.consent.api.v1.dto.res;

import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsentSubmissionResponseDTO {

    private Long id;
    private String confirmationNumber;
    private String apartName;
    private String building;
    private String unit;
    private String representativeName;
    private String status;
    private LocalDateTime submittedAt;
    private String message;
}