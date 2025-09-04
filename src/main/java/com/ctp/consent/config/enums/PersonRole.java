package com.ctp.consent.config.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 동의서 참여자의 역할을 나타내는 열거형
 * - OWNER: 주 소유자
 * - CO_OWNER: 공동 소유자
 * - FAMILY: 가족 구성원
 */
@Getter
@RequiredArgsConstructor
public enum PersonRole {
    OWNER("소유자"),
    CO_OWNER("공동소유자"),
    FAMILY("가족");

    private final String description;
}