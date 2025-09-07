package com.ctp.consent.api.v1.controller;

import com.ctp.consent.api.v1.dto.ApiResponse;
import com.ctp.consent.api.v1.dto.PageResponse;
import com.ctp.consent.api.v1.dto.req.ApartRequestDTO;
import com.ctp.consent.api.v1.dto.res.ApartResponseDTO;
import com.ctp.consent.api.v1.service.ApartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/admin/aparts")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class ApartController {

    private final ApartService apartService;

    @GetMapping
    public String list(@RequestParam(required = false) String search, @RequestParam(required = false) Boolean active, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        PageResponse<ApartResponseDTO> aparts = apartService.getAparts(search, active, pageable);
        model.addAttribute("aparts", aparts);
        model.addAttribute("search", search);
        model.addAttribute("active", active);
        return "admin/apart/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("apart", new ApartRequestDTO());
        return "admin/apart/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("apart") ApartRequestDTO requestDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/apart/form";
        }
        try {
            apartService.createApart(requestDTO);
            redirectAttributes.addFlashAttribute("message", "아파트가 등록되었습니다.");
            return "redirect:/admin/aparts";
        } catch (Exception e) {
            log.error("아파트 등록 실패", e);
            bindingResult.reject("global.error", e.getMessage());
            return "admin/apart/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ApartResponseDTO apart = apartService.getApart(id);
        // ResponseDTO를 RequestDTO로 변환
        ApartRequestDTO requestDTO = ApartRequestDTO.builder()
            .id(apart.getId())
            .aptCode(apart.getAptCode())
            .aptName(apart.getAptName())
            .address(apart.getAddress())
            .totalDong(apart.getTotalDong())
            .totalHousehold(apart.getTotalHousehold())
            .build();
        model.addAttribute("apart", requestDTO);
        return "admin/apart/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("apart") ApartRequestDTO requestDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/apart/form";
        }
        try {
            apartService.updateApart(id, requestDTO);
            redirectAttributes.addFlashAttribute("message", "아파트 정보가 수정되었습니다.");
            return "redirect:/admin/aparts";
        } catch (Exception e) {
            log.error("아파트 수정 실패", e);
            bindingResult.reject("global.error", e.getMessage());
            return "admin/apart/form";
        }
    }

    @PostMapping("/{id}/toggle-active")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> toggleActive(@PathVariable Long id) {
        apartService.toggleActive(id);
        return ResponseEntity.ok(ApiResponse.success("상태가 변경되었습니다."));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        apartService.deleteApart(id);
        return ResponseEntity.ok(ApiResponse.success("아파트가 삭제되었습니다."));
    }

    @GetMapping("/{id}/units")
    public String manageUnits(@PathVariable Long id, Model model) {
        ApartResponseDTO apart = apartService.getApart(id);
        model.addAttribute("apart", apart);
        model.addAttribute("units", apartService.getApartUnits(id));
        return "admin/apart/units";
    }

    @PostMapping("/{id}/units")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> updateUnits(@PathVariable Long id, @RequestBody ApartRequestDTO.UnitConfig unitConfig) {
        apartService.updateUnitConfig(id, unitConfig);
        return ResponseEntity.ok(ApiResponse.success("동/호수 체계가 설정되었습니다."));
    }
}