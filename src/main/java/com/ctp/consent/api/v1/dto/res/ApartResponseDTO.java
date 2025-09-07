package com.ctp.consent.api.v1.dto.res;

import com.ctp.consent.api.v1.dto.BaseDTO;
import lombok.*;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartResponseDTO extends BaseDTO {

    private Long id;
    private String aptCode;
    private String aptName;
    private String address;
    private Integer totalDong;
    private Integer totalHousehold;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer consentCount;
    private Integer completedCount;
    private Integer pendingCount;

    public String getStatusBadgeClass() {
        return active ? "bg-green-100 text-green-800" : "bg-gray-100 text-gray-800";
    }

    public String getStatusText() {
        return active ? "활성" : "비활성";
    }

    public String getCompletionRate() {
        if (totalHousehold == null || totalHousehold == 0 || completedCount == null) {
            return "0%";
        }
        double rate = (double) completedCount / totalHousehold * 100;
        return String.format("%.1f%%", rate);
    }
}