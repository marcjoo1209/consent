package com.ctp.consent.api.v1.dto.req;

import com.ctp.consent.config.enums.ConsentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsentSearchRequest {

    private Long apartmentId;

    private String dong;

    private String ho;

    private String participantName;

    private String phoneNumber;

    private ConsentStatus status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String keyword;
}