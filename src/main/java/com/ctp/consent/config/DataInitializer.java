package com.ctp.consent.config;

import com.ctp.consent.api.v1.dto.model.Admin;
import com.ctp.consent.api.v1.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(AdminRepository adminRepository) {
        return args -> {
            // Super Admin 계정이 없는 경우에만 생성
            if (!adminRepository.existsByUsername("superadmin")) {
                Admin superAdmin = Admin.builder()
                        .username("superadmin")
                        .password(passwordEncoder.encode("admin123!@#"))
                        .name("최고관리자")
                        .email("admin@consent.com")
                        .role(Admin.Role.SUPER_ADMIN)
                        .active(true)
                        .build();

                adminRepository.save(superAdmin);
                log.info("기본 Super Admin 계정 생성 완료 - username: superadmin");
            }

            // 테스트용 일반 관리자 계정
            if (!adminRepository.existsByUsername("admin")) {
                Admin admin = Admin.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .name("일반관리자")
                        .email("manager@consent.com")
                        .role(Admin.Role.ADMIN)
                        .active(true)
                        .build();

                adminRepository.save(admin);
                log.info("테스트 Admin 계정 생성 완료 - username: admin");
            }
        };
    }
}