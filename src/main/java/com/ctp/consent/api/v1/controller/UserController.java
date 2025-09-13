package com.ctp.consent.api.v1.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ctp.consent.api.v1.dto.ApiResponse;
import com.ctp.consent.api.v1.dto.PageResponse;
import com.ctp.consent.api.v1.dto.req.UserRequestDTO;
import com.ctp.consent.api.v1.dto.res.UserResponseDTO;
import com.ctp.consent.api.v1.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {
        PageResponse<UserResponseDTO> users = userService.getUsers(search, role, active, pageable);
        model.addAttribute("users", users);
        model.addAttribute("search", search);
        model.addAttribute("role", role);
        model.addAttribute("active", active);
        model.addAttribute("roles", new String[] { "ADMIN", "SUPER_ADMIN" });
        return "admin/user/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new UserRequestDTO());
        model.addAttribute("roles", new String[] { "ADMIN", "SUPER_ADMIN" });
        return "admin/user/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("user") UserRequestDTO requestDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) return "admin/user/form";
        try {
            userService.createUser(requestDTO);
            redirectAttributes.addFlashAttribute("message", "사용자가 등록되었습니다.");
            return "redirect:/admin/users";
        } catch (Exception e) {
            log.error("사용자 등록 실패", e);
            bindingResult.reject("global.error", e.getMessage());
            return "admin/user/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        UserResponseDTO user = userService.getUser(id);

        // ResponseDTO를 RequestDTO로 변환
        UserRequestDTO requestDTO = UserRequestDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .active(user.getActive())
                .build();

        model.addAttribute("user", requestDTO);
        model.addAttribute("roles", new String[] { "ADMIN", "SUPER_ADMIN" });
        return "admin/user/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("user") UserRequestDTO requestDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) return "admin/user/form";
        try {
            userService.updateUser(id, requestDTO);
            redirectAttributes.addFlashAttribute("message", "사용자 정보가 수정되었습니다.");
            return "redirect:/admin/users";
        } catch (Exception e) {
            log.error("사용자 수정 실패", e);
            bindingResult.reject("global.error", e.getMessage());
            return "admin/user/form";
        }
    }

    @PostMapping("/{id}/toggle-active")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> toggleActive(@PathVariable Long id) {
        userService.toggleActive(id);
        return ResponseEntity.ok(ApiResponse.success("상태가 변경되었습니다."));
    }

    @PostMapping("/{id}/reset-password")
    @ResponseBody
    public ResponseEntity<ApiResponse<String>> resetPassword(@PathVariable Long id) {
        String tempPassword = userService.resetPassword(id);
        return ResponseEntity.ok(ApiResponse.success(tempPassword, "임시 비밀번호가 생성되었습니다."));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("사용자가 삭제되었습니다."));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public String profile(Model model) {
        UserResponseDTO user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "admin/user/profile";
    }

    @PostMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public String updateProfile(@Valid @ModelAttribute("user") UserRequestDTO requestDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "admin/user/profile";
        }

        try {
            userService.updateProfile(requestDTO);
            redirectAttributes.addFlashAttribute("message", "프로필이 수정되었습니다.");
            return "redirect:/admin/users/profile";
        } catch (Exception e) {
            log.error("프로필 수정 실패", e);
            bindingResult.reject("global.error", e.getMessage());
            return "admin/user/profile";
        }
    }
}