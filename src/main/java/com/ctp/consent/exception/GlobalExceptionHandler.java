package com.ctp.consent.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@ControllerAdvice
public class GlobalExceptionHandler implements ErrorController {

    @GetMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String requestUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        if (statusCode == null) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        log.info("Error handler - Status: {}, URI: {}", statusCode, requestUri);
        // 각 에러 코드에 맞는 페이지 반환
        ModelAndView mav = new ModelAndView();
        switch (statusCode) {
            case 400:
                mav.setViewName("error/400");
                mav.addObject("message", "잘못된 요청입니다.");
                break;
            case 401:
                mav.setViewName("error/401");
                mav.addObject("message", "인증이 필요합니다.");
                break;
            case 403:
                mav.setViewName("error/403");
                mav.addObject("message", "접근 권한이 없습니다.");
                break;
            case 404:
                mav.setViewName("error/404");
                mav.addObject("message", "요청하신 페이지를 찾을 수 없습니다.");
                break;
            case 500:
                mav.setViewName("error/500");
                mav.addObject("message", "서버 내부 오류가 발생했습니다.");
                break;
            default:
                mav.setViewName("error/500");
                mav.addObject("message", "오류가 발생했습니다.");
        }
        return mav;
    }

    @ExceptionHandler({ConsentNotFoundException.class, NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleConsentNotFound(ConsentNotFoundException ex) {
        log.error("Consent not found: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("message", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView handleUnauthorized(UnauthorizedException ex) {
        log.error("Unauthorized access: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error/401");
        mav.addObject("message", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleAccessDenied(AccessDeniedException ex) {
        log.error("Access denied: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error/403");
        mav.addObject("message", "접근 권한이 없습니다.");
        return mav;
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleInvalidRequest(InvalidRequestException ex) {
        log.error("Invalid request: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("message", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleFileUpload(FileUploadException ex) {
        log.error("File upload error: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("message", "파일 업로드 오류: " + ex.getMessage());
        return mav;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleMaxSizeExceeded(MaxUploadSizeExceededException ex) {
        log.error("File size exceeded: {}", ex.getMessage());

        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("message", "파일 크기가 허용된 최대 크기를 초과했습니다.");
        return mav;
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleValidation(Exception ex) {
        log.error("Validation error: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("message", "입력값이 올바르지 않습니다.");
        return mav;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNoHandlerFound(NoHandlerFoundException ex) {
        log.error("No handler found: {}", ex.getMessage());
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("message", "요청하신 페이지를 찾을 수 없습니다.");
        return mav;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGeneral(Exception ex) {
        log.error("Internal server error: ", ex);
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("message", "서버 내부 오류가 발생했습니다.");
        return mav;
    }
}