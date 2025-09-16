package com.ctp.consent.api.v1.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatisticsDTO {

    private long total;
    private long issue;
    private long submitted;
    private long approve;
    private long agreed;
    private long reject;
}