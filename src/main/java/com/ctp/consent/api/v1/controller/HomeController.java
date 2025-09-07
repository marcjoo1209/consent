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

    // 컴포넌트~
    @GetMapping("/component")
    public String index() {
        return "component/index";
    }

    @GetMapping("/component/basic")
    public String basicComponents() {
        return "component/basic";
    }

    @GetMapping("/component/templates")
    public String templateComponents() {
        return "component/templates";
    }

    @GetMapping("/component/card")
    public String cardDemo() {
        return "component/card";
    }

    @GetMapping("/component/login")
    public String loginDemo() {
        return "component/login";
    }

    @GetMapping("/component/dashboard")
    public String dashboardDemo() {
        return "component/dashboard";
    }
}