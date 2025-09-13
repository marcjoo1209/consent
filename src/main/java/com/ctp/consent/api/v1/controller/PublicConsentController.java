package com.ctp.consent.api.v1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ctp.consent.api.v1.dto.req.ConsentSubmissionRequestDTO;
import com.ctp.consent.api.v1.dto.res.ConsentSubmissionResponseDTO;
import com.ctp.consent.api.v1.dto.res.ConsentTemplateResponseDTO;
import com.ctp.consent.api.v1.service.ApartService;
import com.ctp.consent.api.v1.service.PublicConsentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/consent")
@RequiredArgsConstructor
public class PublicConsentController {

    private final PublicConsentService consentService;
    private final ApartService apartService;

    /**
     * 동의서 작성 페이지 (로그인 불필요)
     */
    @GetMapping("/{apartCode}/{templateId}")
    public String showConsentForm(@PathVariable String apartCode, @PathVariable Long templateId, Model model) {
        try {
            // 템플릿 정보 조회
            ConsentTemplateResponseDTO template = consentService.getConsentForm(apartCode, templateId);
            // 아파트 정보 조회
            var apart = apartService.getApartByCode(apartCode);
            model.addAttribute("template", template);
            model.addAttribute("apart", apart);
            model.addAttribute("apartCode", apartCode);
            model.addAttribute("templateId", templateId);
            model.addAttribute("submission", new ConsentSubmissionRequestDTO());
            return "public/consent/form";
        } catch (Exception e) {
            log.error("동의서 폼 조회 실패", e);
            model.addAttribute("error", e.getMessage());
            return "public/consent/error";
        }
    }

    /**
     * 동의서 제출 (로그인 불필요)
     */
    @PostMapping("/{apartCode}/{templateId}/submit")
    public String submitConsent(@PathVariable String apartCode, @PathVariable Long templateId, @Valid @ModelAttribute("submission") ConsentSubmissionRequestDTO requestDTO, BindingResult result, HttpServletRequest request, Model model) {
        log.debug("동의서 제출 apartCode {} ", apartCode);
        if (result.hasErrors()) {
            // 폼 유효성 검증 실패 시 다시 폼 표시
            var template = consentService.getConsentForm(apartCode, templateId);
            var apart = apartService.getApartByCode(apartCode);
            model.addAttribute("template", template);
            model.addAttribute("apart", apart);
            model.addAttribute("apartCode", apartCode);
            model.addAttribute("templateId", templateId);
            return "public/consent/form";
        }
        try {
            // 동의서 제출 처리
            ConsentSubmissionResponseDTO response = consentService.submitConsent(apartCode, templateId, requestDTO, request);
            model.addAttribute("result", response);
            return "public/consent/complete";
        } catch (Exception e) {
            log.error("동의서 제출 실패", e);
            model.addAttribute("error", e.getMessage());
            // 에러 발생 시 다시 폼 표시
            var template = consentService.getConsentForm(apartCode, templateId);
            var apart = apartService.getApartByCode(apartCode);
            model.addAttribute("template", template);
            model.addAttribute("apart", apart);
            model.addAttribute("apartCode", apartCode);
            model.addAttribute("templateId", templateId);
            return "public/consent/form";
        }
    }

    /**
     * 제출된 동의서 확인
     */
    @GetMapping("/verify/{id}")
    public String verifySubmission(@PathVariable Long id, Model model) {
        try {
            ConsentSubmissionResponseDTO submission = consentService.verifySubmission(id);
            model.addAttribute("submission", submission);
            return "public/consent/verify";
        } catch (Exception e) {
            log.error("동의서 확인 실패", e);
            model.addAttribute("error", e.getMessage());
            return "public/consent/error";
        }
    }
}