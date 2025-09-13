package com.ctp.consent.api.v1.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctp.consent.api.v1.dto.PageResponse;
import com.ctp.consent.api.v1.dto.model.Admin;
import com.ctp.consent.api.v1.dto.req.UserRequestDTO;
import com.ctp.consent.api.v1.dto.res.UserResponseDTO;
import com.ctp.consent.api.v1.repository.AdminRepository;
import com.ctp.consent.exception.ConsentNotFoundException;
import com.ctp.consent.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public PageResponse<UserResponseDTO> getUsers(String search, String role, Boolean active, Pageable pageable) {
        Specification<Admin> spec = Specification.anyOf();

        if (search != null && !search.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("username")), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("email")), "%" + search.toLowerCase() + "%")));
        }

        if (role != null && !role.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("role"), role));
        }

        if (active != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), active));
        }

        Page<Admin> userPage = adminRepository.findAll(spec, pageable);
        Page<UserResponseDTO> dtoPage = userPage.map(this::toResponseDTO);

        return PageResponse.of(dtoPage);
    }

    public UserResponseDTO getUser(Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new ConsentNotFoundException("사용자를 찾을 수 없습니다. ID: " + id));
        return toResponseDTO(admin);
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        if (adminRepository.existsByUsername(requestDTO.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다: " + requestDTO.getUsername());
        }

        if (adminRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + requestDTO.getEmail());
        }

        // 비밀번호 확인 검증
        if (requestDTO.getPassword() == null || requestDTO.getPassword().isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }

        if (requestDTO.getConfirmPassword() != null &&
                !requestDTO.getPassword().equals(requestDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Admin admin = Admin.builder()
                .username(requestDTO.getUsername())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .phoneNumber(requestDTO.getPhoneNumber())
                .role(Admin.Role.valueOf(requestDTO.getRole()))
                .active(true)
                .build();

        Admin savedAdmin = adminRepository.save(admin);
        log.info("사용자 생성: {}", savedAdmin.getUsername());

        return toResponseDTO(savedAdmin);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new ConsentNotFoundException("사용자를 찾을 수 없습니다. ID: " + id));

        // 본인이 아닌데 다른 사용자의 username 변경 시 중복 체크
        if (!admin.getUsername().equals(requestDTO.getUsername()) && adminRepository.existsByUsername(requestDTO.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다: " + requestDTO.getUsername());
        }

        if (!admin.getEmail().equals(requestDTO.getEmail()) && adminRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + requestDTO.getEmail());
        }

        admin.setUsername(requestDTO.getUsername());
        admin.setName(requestDTO.getName());
        admin.setEmail(requestDTO.getEmail());
        admin.setPhoneNumber(requestDTO.getPhoneNumber());
        admin.setRole(Admin.Role.valueOf(requestDTO.getRole()));

        // 비밀번호가 입력된 경우에만 변경
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            // 비밀번호 확인 검증
            if (requestDTO.getConfirmPassword() != null && !requestDTO.getPassword().equals(requestDTO.getConfirmPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
            admin.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }

        Admin updatedAdmin = adminRepository.save(admin);
        log.info("사용자 수정: {}", updatedAdmin.getUsername());

        return toResponseDTO(updatedAdmin);
    }

    @Transactional
    public void toggleActive(Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new ConsentNotFoundException("사용자를 찾을 수 없습니다. ID: " + id));

        admin.setActive(!admin.getActive());
        adminRepository.save(admin);

        log.info("사용자 상태 변경: {} -> {}", admin.getUsername(), admin.getActive() ? "활성" : "비활성");
    }

    @Transactional
    public String resetPassword(Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new ConsentNotFoundException("사용자를 찾을 수 없습니다. ID: " + id));

        // 임시 비밀번호 생성
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        admin.setPassword(passwordEncoder.encode(tempPassword));
        adminRepository.save(admin);

        log.info("사용자 비밀번호 재설정: {}", admin.getUsername());

        return tempPassword;
    }

    @Transactional
    public void deleteUser(Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new ConsentNotFoundException("사용자를 찾을 수 없습니다. ID: " + id));

        // 본인 계정은 삭제 불가
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (admin.getUsername().equals(currentUsername)) {
            throw new IllegalArgumentException("본인 계정은 삭제할 수 없습니다.");
        }

        adminRepository.delete(admin);
        log.info("사용자 삭제: {}", admin.getUsername());
    }

    public UserResponseDTO getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Admin admin = adminRepository.findByUsername(username).orElseThrow(() -> new UnauthorizedException("현재 사용자를 찾을 수 없습니다."));
        return toResponseDTO(admin);
    }

    @Transactional
    public void updateProfile(UserRequestDTO requestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Admin admin = adminRepository.findByUsername(username).orElseThrow(() -> new UnauthorizedException("현재 사용자를 찾을 수 없습니다."));

        admin.setName(requestDTO.getName());
        admin.setEmail(requestDTO.getEmail());
        admin.setPhoneNumber(requestDTO.getPhoneNumber());

        // 비밀번호 변경
        if (requestDTO.getCurrentPassword() != null && !requestDTO.getCurrentPassword().isBlank()) {
            if (!passwordEncoder.matches(requestDTO.getCurrentPassword(), admin.getPassword())) {
                throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
            }

            if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
                // 비밀번호 확인 검증
                if (requestDTO.getConfirmPassword() != null && !requestDTO.getPassword().equals(requestDTO.getConfirmPassword())) {
                    throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
                }
                admin.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
            }
        }

        adminRepository.save(admin);
        log.info("프로필 수정: {}", admin.getUsername());
    }

    private UserResponseDTO toResponseDTO(Admin admin) {
        return UserResponseDTO.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .name(admin.getName())
                .email(admin.getEmail())
                .phoneNumber(admin.getPhoneNumber())
                .role(admin.getRole().toString())
                .active(admin.getActive())
                .lastLoginAt(admin.getLastLoginAt())
                .createdAt(admin.getCreatedAt())
                .updatedAt(admin.getUpdatedAt())
                .build();
    }
}