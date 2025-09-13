package com.ctp.consent.api.v1.dto.res;

import java.time.LocalDateTime;
import java.util.List;

import com.ctp.consent.api.v1.dto.BaseDTO;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ConsentTemplateResponseDTO extends BaseDTO {

    private String title;
    private String description;
    private String content;
    private Boolean active;
    private Integer version;
    private String createdByName;
    private List<ApartInfoDTO> assignedAparts;
    private List<Long> assignedApartIds;

    @Builder
    public ConsentTemplateResponseDTO(Long id, LocalDateTime createdAt, LocalDateTime updatedAt,
                                     String title, String description, String content,
                                     Boolean active, Integer version, String createdByName,
                                     List<ApartInfoDTO> assignedAparts, List<Long> assignedApartIds) {
        this.setId(id);
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
        this.setActive(active);
        this.title = title;
        this.description = description;
        this.content = content;
        this.version = version;
        this.createdByName = createdByName;
        this.assignedAparts = assignedAparts;
        this.assignedApartIds = assignedApartIds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ApartInfoDTO {
        private Long id;
        private String code;
        private String name;
        private Boolean active;
    }
}