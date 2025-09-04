package com.ctp.consent.api.v1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/component")
public class ComponentController {

    @GetMapping
    public String index() {
        return "component/index";
    }

    @GetMapping("/basic")
    public String basicComponents(Model model) {
        return "component/basic";
    }

    @GetMapping("/templates")
    public String templateComponents(Model model) {
        return "component/templates";
    }

    @GetMapping("/card")
    public String cardDemo() {
        return "component/card";
    }

    @GetMapping("/login")
    public String loginDemo() {
        return "component/login";
    }

    @GetMapping("/dashboard")
    public String dashboardDemo() {
        return "component/dashboard";
    }
}