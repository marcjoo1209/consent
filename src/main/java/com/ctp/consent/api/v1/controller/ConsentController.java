package com.ctp.consent.api.v1.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ctp.consent.api.v1.dto.ApiResponse;
import com.ctp.consent.api.v1.dto.req.ConsentSearchRequest;
import com.ctp.consent.api.v1.dto.res.ConsentStatisticsResponse;
import com.ctp.consent.api.v1.service.ConsentService;
import com.ctp.consent.config.enums.ConsentStatus;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/consent")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class ConsentController {

    private final ConsentService consentService;

    @GetMapping
    public String list(@ModelAttribute ConsentSearchRequest searchRequest, @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        log.debug("동의서 목록 조회: {}", searchRequest);
        var page = consentService.searchConsents(searchRequest, pageable);
        model.addAttribute("consents", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("searchRequest", searchRequest);
        model.addAttribute("statuses", ConsentStatus.values());
        return "admin/consent/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        log.debug("동의서 상세 조회: {}", id);
        var consent = consentService.getConsentDetail(id);
        model.addAttribute("consent", consent);
        return "admin/consent/detail";
    }

    @PostMapping("/{id}/approve")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> approve(@PathVariable Long id, @RequestParam(required = false) String comment) {
        log.info("동의서 승인 처리: id={}, comment={}", id, comment);
        consentService.approveConsent(id, comment);
        return ResponseEntity.ok(ApiResponse.success("동의서가 승인되었습니다."));
    }

    @PostMapping("/{id}/reject")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> reject(@PathVariable Long id, @RequestParam String reason) {
        log.info("동의서 반려 처리: id={}, reason={}", id, reason);
        consentService.rejectConsent(id, reason);
        return ResponseEntity.ok(ApiResponse.success("동의서가 반려되었습니다."));
    }

    @PostMapping("/bulk-approve")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> bulkApprove(@RequestBody List<Long> ids) {
        log.info("동의서 일괄 승인: {} 건", ids.size());

        int processed = consentService.bulkApprove(ids);
        return ResponseEntity.ok(ApiResponse.success(processed + "건이 승인되었습니다."));
    }

    @PostMapping("/bulk-reject")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> bulkReject(@RequestBody List<Long> ids, @RequestParam String reason) {
        log.info("동의서 일괄 반려: {} 건", ids.size());
        int processed = consentService.bulkReject(ids, reason);
        return ResponseEntity.ok(ApiResponse.success(processed + "건이 반려되었습니다."));
    }

    @GetMapping("/export")
    public void exportExcel(@ModelAttribute ConsentSearchRequest searchRequest, HttpServletResponse response) {
        log.info("동의서 Excel 다운로드");
        consentService.exportToExcel(searchRequest, response);
    }

    @GetMapping("/statistics")
    @ResponseBody
    public ResponseEntity<ApiResponse<ConsentStatisticsResponse>> getStatistics(@RequestParam(required = false) Long apartmentId) {
        return ResponseEntity.ok(ApiResponse.success(consentService.getStatistics(apartmentId), "통계 조회 성공"));
    }
}