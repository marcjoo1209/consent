package com.ctp.consent.api.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private String error;
    private LocalDateTime timestamp;
    private Integer code;

    // 성공 응답 팩토리 메서드
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now())
                .code(200)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .code(200)
                .build();
    }

    public static ApiResponse<Void> success(String message) {
        return ApiResponse.<Void>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .code(200)
                .build();
    }

    public static ApiResponse<Void> success() {
        return ApiResponse.<Void>builder()
                .success(true)
                .message("성공적으로 처리되었습니다")
                .timestamp(LocalDateTime.now())
                .code(200)
                .build();
    }

    // 실패 응답 팩토리 메서드
    public static <T> ApiResponse<T> error(String error) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(error)
                .timestamp(LocalDateTime.now())
                .code(400)
                .build();
    }

    public static <T> ApiResponse<T> error(String error, Integer code) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(error)
                .timestamp(LocalDateTime.now())
                .code(code)
                .build();
    }

    public static <T> ApiResponse<T> error(String error, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(error)
                .message(message)
                .timestamp(LocalDateTime.now())
                .code(400)
                .build();
    }

    public static <T> ApiResponse<T> error(String error, String message, Integer code) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(error)
                .message(message)
                .timestamp(LocalDateTime.now())
                .code(code)
                .build();
    }

    // 유효성 검사 실패 응답
    public static <T> ApiResponse<T> validationError(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .error("VALIDATION_ERROR")
                .message(message)
                .timestamp(LocalDateTime.now())
                .code(422)
                .build();
    }

    // 권한 없음 응답
    public static <T> ApiResponse<T> unauthorized(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .error("UNAUTHORIZED")
                .message(message)
                .timestamp(LocalDateTime.now())
                .code(401)
                .build();
    }

    // 접근 금지 응답
    public static <T> ApiResponse<T> forbidden(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .error("FORBIDDEN")
                .message(message)
                .timestamp(LocalDateTime.now())
                .code(403)
                .build();
    }

    // 리소스 찾을 수 없음 응답
    public static <T> ApiResponse<T> notFound(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .error("NOT_FOUND")
                .message(message)
                .timestamp(LocalDateTime.now())
                .code(404)
                .build();
    }

    // 서버 오류 응답
    public static <T> ApiResponse<T> serverError(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .error("INTERNAL_SERVER_ERROR")
                .message(message)
                .timestamp(LocalDateTime.now())
                .code(500)
                .build();
    }
}