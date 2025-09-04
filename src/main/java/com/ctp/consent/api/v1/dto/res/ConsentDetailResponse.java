package com.ctp.consent.api.v1.dto.res;

import com.ctp.consent.api.v1.dto.BaseDTO;
import com.ctp.consent.api.v1.dto.model.Attachment;
import com.ctp.consent.api.v1.dto.model.ConsentRecord;
import com.ctp.consent.api.v1.dto.model.ConsentPerson;
import com.ctp.consent.config.enums.ConsentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ConsentDetailResponse extends BaseDTO {

    // 기본 필드는 BaseDTO에서 상속받음 (id, createdAt, updatedAt, createdBy, updatedBy,
    // active)

    private String apartmentName;
    private String dong;
    private String ho;
    private ConsentStatus status;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private String approvalComment;
    private String rejectionReason;

    private List<ParticipantInfo> participants;
    private List<AttachmentInfo> attachments;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipantInfo {
        private Long id;
        private String name;
        private String phoneNumber;
        private String birthDate;
        private String relation;
        private String signatureUrl;

        public static ParticipantInfo from(ConsentPerson participant) {
            return ParticipantInfo.builder()
                    .id(participant.getId())
                    .name(participant.getName())
                    .phoneNumber(participant.getPhoneNumber())
                    .birthDate(participant.getBirthDate())
                    .relation(participant.getRelation())
                    .signatureUrl(participant.getSignatureUrl())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentInfo {
        private Long id;
        private String fileName;
        private String fileType;
        private Long fileSize;
        private String downloadUrl;
        private LocalDateTime uploadedAt;

        public static AttachmentInfo from(Attachment attachment) {
            return AttachmentInfo.builder()
                    .id(attachment.getId())
                    .fileName(attachment.getOriginalFileName())
                    .fileType(attachment.getFileType())
                    .fileSize(attachment.getFileSize())
                    .downloadUrl("/api/consent/attachment/" + attachment.getId())
                    .uploadedAt(attachment.getCreatedAt())
                    .build();
        }
    }

    public static ConsentDetailResponse from(ConsentRecord consent) {
        ConsentDetailResponse response = new ConsentDetailResponse();

        // BaseDTO 필드 설정
        response.setId(consent.getId());
        response.setCreatedAt(consent.getCreatedAt());
        response.setUpdatedAt(consent.getUpdatedAt());
        // createdBy와 updatedBy는 ConsentRecord에 없을 수 있으므로 null로 설정
        response.setCreatedBy(null);
        response.setUpdatedBy(null);

        // ConsentDetailResponse 필드 설정
        response.setApartmentName(consent.getApart() != null ? consent.getApart().getAptName() : null);
        response.setDong(consent.getAptDong());
        response.setHo(consent.getAptHo());
        response.setStatus(consent.getStatus());
        response.setApprovedAt(consent.getApprovedAt());
        response.setRejectedAt(consent.getRejectedAt());
        response.setApprovalComment(consent.getApprovalComment());
        response.setRejectionReason(consent.getRejectionReason());

        // 참여자 정보 변환
        response.setParticipants(consent.getPersons().stream()
                .map(ParticipantInfo::from)
                .collect(Collectors.toList()));

        // 첨부파일 정보 (현재는 빈 리스트, 추후 구현 필요)
        response.setAttachments(new ArrayList<>());

        return response;
    }
}