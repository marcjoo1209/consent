package com.ctp.consent.api.v1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * 루트 경로 접속 시 리다이렉트 페이지 표시
     */
    @GetMapping("/")
    public String home() {
        return "error/403";
    }
}