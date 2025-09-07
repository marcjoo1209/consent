package com.ctp.consent.api.v1.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartRequestDTO {

    private Long id;  // 수정 시 필요한 ID 필드 추가
    
    @NotBlank(message = "아파트 코드는 필수입니다")
    @Pattern(regexp = "^[A-Z0-9]{3,10}$", message = "아파트 코드는 3-10자의 대문자 및 숫자만 가능합니다")
    private String aptCode;

    @NotBlank(message = "아파트명은 필수입니다")
    private String aptName;

    private String address;

    @Positive(message = "총 동 수는 양수여야 합니다")
    private Integer totalDong;

    @Positive(message = "총 세대수는 양수여야 합니다")
    private Integer totalHousehold;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UnitConfig {
        @NotNull(message = "총 동 수는 필수입니다")
        @Positive(message = "총 동 수는 양수여야 합니다")
        private Integer totalDong;

        private List<DongConfig> dongConfigs;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class DongConfig {
            private String dongName;
            private Integer startFloor;
            private Integer endFloor;
            private Integer unitsPerFloor;
        }

        public Integer calculateTotalHousehold() {
            if (dongConfigs == null || dongConfigs.isEmpty()) {
                return totalDong * 15 * 4; // 기본값: 15층, 층당 4호
            }
            return dongConfigs.stream()
                    .mapToInt(config -> {
                        int floors = config.getEndFloor() - config.getStartFloor() + 1;
                        return floors * config.getUnitsPerFloor();
                    }).sum();
        }
    }
}