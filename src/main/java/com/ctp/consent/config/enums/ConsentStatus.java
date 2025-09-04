package com.ctp.consent.config.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 동의서 상태를 나타내는 열거형
 * - DRAFT: 임시저장 상태
 * - SUBMITTED: 제출 완료
 * - AGREED: 동의 처리
 * - REJECTED: 거부 처리
 */
@Getter
@RequiredArgsConstructor
public enum ConsentStatus {
    DRAFT("임시저장"),
    PENDING("대기중"),
    SUBMITTED("제출됨"),
    APPROVED("승인"),
    AGREED("동의"),
    REJECTED("반려");

    private final String description;
}