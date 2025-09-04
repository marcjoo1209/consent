package com.ctp.consent.api.v1.dto.res;

import com.ctp.consent.api.v1.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ConsentStatisticsResponse extends BaseDTO {

    // 통계 필드 - BaseDTO의 기본 필드(id, createdAt, updatedAt 등)은 통계 데이터에 불필요할 수 있지만
    // 일관성을 위해 BaseDTO를 상속받고, 사용하지 않는 필드는 null로 둠

    private Long total;
    private Long pending;
    private Long approved;
    private Long rejected;
    private Double approvalRate;

    public ConsentStatisticsResponse(Long total, Long pending, Long approved, Long rejected) {
        this.total = total != null ? total : 0L;
        this.pending = pending != null ? pending : 0L;
        this.approved = approved != null ? approved : 0L;
        this.rejected = rejected != null ? rejected : 0L;
        this.approvalRate = calculateApprovalRate();
    }

    public static ConsentStatisticsResponse empty() {
        ConsentStatisticsResponse response = new ConsentStatisticsResponse();
        response.setTotal(0L);
        response.setPending(0L);
        response.setApproved(0L);
        response.setRejected(0L);
        response.setApprovalRate(0.0);
        return response;
    }

    private Double calculateApprovalRate() {
        if (total == null || total == 0) {
            return 0.0;
        }
        return (approved != null ? approved : 0.0) / total * 100;
    }

    // Setter 오버라이드 - approvalRate 자동 계산
    public void setTotal(Long total) {
        this.total = total;
        this.approvalRate = calculateApprovalRate();
    }

    public void setApproved(Long approved) {
        this.approved = approved;
        this.approvalRate = calculateApprovalRate();
    }
}