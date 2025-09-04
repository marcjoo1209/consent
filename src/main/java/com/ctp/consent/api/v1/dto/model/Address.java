package com.ctp.consent.api.v1.dto.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 주소 정보를 담는 임베디드 타입
 * - 참여자의 주소 정보를 구조화하여 저장
 * - 도로명주소, 지번주소, 상세주소 등
 * - @Embeddable로 별도 테이블 없이 엔티티에 포함됨
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    private String postcode;

    private String roadAddress;

    private String jibunAddress;

    private String detailAddress;

    private String extraAddress;

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (roadAddress != null) {
            sb.append(roadAddress);
        } else if (jibunAddress != null) {
            sb.append(jibunAddress);
        }
        if (detailAddress != null) {
            sb.append(" ").append(detailAddress);
        }
        if (extraAddress != null) {
            sb.append(" ").append(extraAddress);
        }
        return sb.toString().trim();
    }
}