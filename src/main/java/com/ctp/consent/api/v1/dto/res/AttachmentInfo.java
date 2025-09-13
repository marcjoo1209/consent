package com.ctp.consent.api.v1.dto.res;

import com.ctp.consent.api.v1.dto.model.Attachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentInfo {
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