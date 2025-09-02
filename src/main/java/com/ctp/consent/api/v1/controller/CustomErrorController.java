package com.ctp.consent.api.v1.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                // 404 에러 - 잘못된 경로 접근 시 리다이렉트 페이지로
                log.info("404 에러 발생 - 요청 경로: {}", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
                return "redirect";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                // 403 에러 - 권한 없음
                model.addAttribute("errorCode", "403");
                model.addAttribute("errorMessage", "접근 권한이 없습니다.");
                return "error/403";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                // 500 에러
                model.addAttribute("errorCode", "500");
                model.addAttribute("errorMessage", "서버 오류가 발생했습니다.");
                return "error/500";
            }
        }

        // 기타 에러도 리다이렉트 페이지로
        return "redirect";
    }
}