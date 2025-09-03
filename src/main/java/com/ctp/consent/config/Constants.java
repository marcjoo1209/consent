package com.ctp.consent.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Role {
        public static final String SUPER_ADMIN = "SUPER_ADMIN";
        public static final String ADMIN = "ADMIN";
        public static final String ANONYMOUS = "ANONYMOUS";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Status {
        public static final String DRAFT = "DRAFT";
        public static final String SUBMITTED = "SUBMITTED";
        public static final String APPROVED = "APPROVED";
        public static final String REJECTED = "REJECTED";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class FileUpload {
        public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
        public static final long MAX_REQUEST_SIZE = 100 * 1024 * 1024; // 100MB
        public static final String[] ALLOWED_EXTENSIONS = { "jpg", "jpeg", "png", "webp", "gif", "pdf", "doc", "docx",
                "xls", "xlsx" };
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Message {
        public static final String SUCCESS = "처리가 완료되었습니다.";
        public static final String ERROR = "처리 중 오류가 발생했습니다.";
        public static final String NOT_FOUND = "요청한 정보를 찾을 수 없습니다.";
        public static final String UNAUTHORIZED = "인증이 필요합니다.";
        public static final String FORBIDDEN = "접근 권한이 없습니다.";
        public static final String INVALID_REQUEST = "잘못된 요청입니다.";
        public static final String FILE_UPLOAD_ERROR = "파일 업로드 중 오류가 발생했습니다.";
        public static final String LOGIN_SUCCESS = "로그인되었습니다.";
        public static final String LOGIN_FAILURE = "아이디 또는 비밀번호가 올바르지 않습니다.";
        public static final String LOGOUT_SUCCESS = "로그아웃되었습니다.";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Paging {
        public static final int DEFAULT_PAGE_SIZE = 10;
        public static final int MAX_PAGE_SIZE = 100;
    }

    public static final class Url {
        public static final String ADMIN_URL = "/admin/**";
        public static final String ADMIN_API = "/api/admin/**";
        public static final String ADMIN_LOGIN = "/admin/login";
        public static final String ADMIN_LOGOUT = "/admin/logout";
        public static final String DASHBOARD = "/admin/dashboard";
        public static final String[] ALLOW_CONSENT = {
                ADMIN_LOGIN,
                "/consent/**",
                "/api/address/**",
                "/api/consent/public/**"
        };
        public static final String[] ALLOW_STATIC = {
                "/css/**",
                "/js/**",
                "/dist/**",
                "/resource/**",
                "/image/**",
                "/favicon.ico",
                "/error",
                "/component/**"
        };
    }
}