package com.ctp.consent.api.v1.dto.res;

import com.ctp.consent.api.v1.dto.model.ConsentPerson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantInfo {
    private Long id;
    private String name;
    private String phoneNumber;
    private String birthDate;
    private String relation;
    private String signatureData;

    public static ParticipantInfo from(ConsentPerson participant) {
        return ParticipantInfo.builder()
                .id(participant.getId())
                .name(participant.getName())
                .phoneNumber(participant.getPhoneNumber())
                .birthDate(participant.getBirthDate())
                .relation(participant.getRelation())
                .signatureData(participant.getSignatureData())
                .build();
    }
}