package com.ctp.consent.api.v1.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ctp.consent.api.v1.dto.ApiResponse;
import com.ctp.consent.api.v1.dto.PageResponse;
import com.ctp.consent.api.v1.dto.req.ConsentTemplateRequestDTO;
import com.ctp.consent.api.v1.dto.res.ConsentTemplateResponseDTO;
import com.ctp.consent.api.v1.service.ApartService;
import com.ctp.consent.api.v1.service.ConsentTemplateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/templates")
@RequiredArgsConstructor
public class ConsentTemplateController {

    private final ConsentTemplateService templateService;
    private final ApartService apartService;

    @GetMapping
    public String listTemplates(@RequestParam(required = false) String search, @RequestParam(required = false) Boolean active, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,Model model) { 
        PageResponse<ConsentTemplateResponseDTO> templates = templateService.getTemplates(search, active, pageable);
        model.addAttribute("templates", templates);
        model.addAttribute("search", search);
        model.addAttribute("active", active);
        return "admin/template/list";
    }

    @GetMapping("/new")
    public String newTemplate(Model model) {
        model.addAttribute("template", new ConsentTemplateRequestDTO());
        model.addAttribute("aparts", apartService.getAllActiveAparts());
        return "admin/template/form";
    }

    @PostMapping("/new")
    public String createTemplate(@Valid @ModelAttribute("template") ConsentTemplateRequestDTO requestDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/template/form";
        }
        try {
            ConsentTemplateResponseDTO created = templateService.createTemplate(requestDTO);
            redirectAttributes.addFlashAttribute("successMessage", "템플릿이 성공적으로 생성되었습니다.");
            return "redirect:/admin/templates/" + created.getId();
        } catch (Exception e) {
            log.error("템플릿 생성 실패", e);
            redirectAttributes.addFlashAttribute("errorMessage", "템플릿 생성에 실패했습니다: " + e.getMessage());
            return "redirect:/admin/templates/new";
        }
    }

    @GetMapping("/{id}")
    public String viewTemplate(@PathVariable Long id, Model model) {
        ConsentTemplateResponseDTO template = templateService.getTemplate(id);
        model.addAttribute("template", template);
        model.addAttribute("aparts", apartService.getAllActiveAparts());
        return "admin/template/view";
    }

    @GetMapping("/{id}/edit")
    public String editTemplate(@PathVariable Long id, Model model) {
        ConsentTemplateResponseDTO template = templateService.getTemplate(id);
        ConsentTemplateRequestDTO requestDTO = ConsentTemplateRequestDTO.builder()
                .title(template.getTitle())
                .description(template.getDescription())
                .content(template.getContent())
                .active(template.getActive())
                .build();

        model.addAttribute("template", requestDTO);
        model.addAttribute("templateId", id);
        model.addAttribute("assignedApartIds", template.getAssignedApartIds());
        model.addAttribute("aparts", apartService.getAllActiveAparts());
        return "admin/template/form";
    }

    @PostMapping("/{id}/edit")
    public String updateTemplate(@PathVariable Long id, @Valid @ModelAttribute("template") ConsentTemplateRequestDTO requestDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/template/form";
        }
        try {
            templateService.updateTemplate(id, requestDTO);
            redirectAttributes.addFlashAttribute("successMessage", "템플릿이 성공적으로 수정되었습니다.");
            return "redirect:/admin/templates/" + id;
        } catch (Exception e) {
            log.error("템플릿 수정 실패", e);
            redirectAttributes.addFlashAttribute("errorMessage", "템플릿 수정에 실패했습니다: " + e.getMessage());
            return "redirect:/admin/templates/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteTemplate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            templateService.deleteTemplate(id);
            redirectAttributes.addFlashAttribute("successMessage", "템플릿이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("템플릿 삭제 실패", e);
            redirectAttributes.addFlashAttribute("errorMessage", "템플릿 삭제에 실패했습니다: " + e.getMessage());
        }
        return "redirect:/admin/templates";
    }

    @PostMapping("/{templateId}/assign/{apartId}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> assignTemplate(@PathVariable Long templateId,
            @PathVariable Long apartId) {
        try {
            templateService.assignTemplateToApart(templateId, apartId);
            return ResponseEntity.ok(ApiResponse.success(null, "템플릿이 아파트에 할당되었습니다."));
        } catch (Exception e) {
            log.error("템플릿 할당 실패", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{templateId}/unassign/{apartId}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> unassignTemplate(@PathVariable Long templateId,
            @PathVariable Long apartId) {
        try {
            templateService.unassignTemplateFromApart(templateId, apartId);
            return ResponseEntity.ok(ApiResponse.success(null, "템플릿 할당이 해제되었습니다."));
        } catch (Exception e) {
            log.error("템플릿 할당 해제 실패", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}