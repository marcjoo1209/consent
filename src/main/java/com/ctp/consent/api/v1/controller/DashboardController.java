package com.ctp.consent.api.v1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ctp.consent.api.v1.dto.res.DashboardStatisticsDTO;
import com.ctp.consent.api.v1.service.DashboardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public String dashboard(Model model) {
        log.info("관리자 대시보드 접속");
        DashboardStatisticsDTO statistics = dashboardService.getDashboardStatistics();
        model.addAttribute("statistics", statistics);
        return "admin/dashboard";
    }
}