package com.ctp.consent.config;

import com.ctp.consent.api.v1.dto.model.*;
import com.ctp.consent.api.v1.repository.*;
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
    CommandLineRunner initDatabase(AdminRepository adminRepository,
            ApartmentRepository apartmentRepository,
            ConsentFormTemplateRepository templateRepository,
            ConsentFormRepository consentFormRepository) {
        return args -> {
            // Super Admin 계정이 없는 경우에만 생성
            if (!adminRepository.existsByUsername("superadmin")) {
                Admin superAdmin = Admin.builder()
                        .username("superadmin")
                        .password(passwordEncoder.encode("admin123!@#"))
                        .name("최고관리자")
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
                        .role(Admin.Role.ADMIN)
                        .active(true)
                        .build();

                adminRepository.save(admin);
                log.info("테스트 Admin 계정 생성 완료 - username: admin");
            }

            // 테스트용 아파트 데이터
            if (!apartmentRepository.existsByAptCode("APT01")) {
                Apartment apartment1 = Apartment.builder()
                        .aptCode("APT01")
                        .aptName("태성아파트")
                        .address("서울시 강남구 삼성동")
                        .active(true)
                        .totalDong(5)
                        .totalHousehold(500)
                        .build();

                apartmentRepository.save(apartment1);
                log.info("테스트 아파트 생성 완료 - 태성아파트");
            }

            if (!apartmentRepository.existsByAptCode("APT02")) {
                Apartment apartment2 = Apartment.builder()
                        .aptCode("APT02")
                        .aptName("한강아파트")
                        .address("서울시 용산구 한강로")
                        .active(true)
                        .totalDong(3)
                        .totalHousehold(300)
                        .build();

                apartmentRepository.save(apartment2);
                log.info("테스트 아파트 생성 완료 - 한강아파트");
            }

            // 테스트용 동의서 템플릿
            if (!templateRepository.existsByTemplateCode("TEMPLATE01")) {
                ConsentFormTemplate template1 = ConsentFormTemplate.builder()
                        .templateCode("TEMPLATE01")
                        .templateName("재건축 기본 동의서")
                        .category("재건축")
                        .htmlContent(
                                "<h1>{{apartment_name}} 재건축 동의서</h1><p>본인은 {{apartment_name}} {{dong}}동 {{ho}}호의 소유자로서 재건축 사업에 동의합니다.</p>")
                        .description("재건축 사업을 위한 기본 동의서 템플릿")
                        .active(true)
                        .build();

                templateRepository.save(template1);
                log.info("테스트 템플릿 생성 완료 - 재건축 기본 동의서");
            }

            if (!templateRepository.existsByTemplateCode("TEMPLATE02")) {
                ConsentFormTemplate template2 = ConsentFormTemplate.builder()
                        .templateCode("TEMPLATE02")
                        .templateName("리모델링 기본 동의서")
                        .category("리모델링")
                        .htmlContent(
                                "<h1>{{apartment_name}} 리모델링 동의서</h1><p>본인은 {{apartment_name}} {{dong}}동 {{ho}}호의 소유자로서 리모델링 사업에 동의합니다.</p>")
                        .description("리모델링 사업을 위한 기본 동의서 템플릿")
                        .active(true)
                        .build();

                templateRepository.save(template2);
                log.info("테스트 템플릿 생성 완료 - 리모델링 기본 동의서");
            }

            // 아파트별 동의서 양식
            Apartment apt1 = apartmentRepository.findByAptCode("APT01").orElse(null);
            ConsentFormTemplate template1 = templateRepository.findByTemplateCode("TEMPLATE01").orElse(null);

            if (apt1 != null && template1 != null && !consentFormRepository.existsByFormCode("APT01_FORM01")) {
                ConsentForm form1 = ConsentForm.builder()
                        .apartment(apt1)
                        .template(template1)
                        .formCode("APT01_FORM01")
                        .formName("태성아파트 재건축 동의서")
                        .htmlContent(
                                "<h1>태성아파트 재건축 동의서</h1><p>본인은 태성아파트의 소유자로서 재건축 사업에 동의합니다.</p><p>사업 시행: 2025년 예정</p>")
                        .requiresSignature(true)
                        .requiresAttachment(true)
                        .minPersons(1)
                        .active(true)
                        .build();

                consentFormRepository.save(form1);
                log.info("아파트별 동의서 양식 생성 완료 - 태성아파트 재건축 동의서");
            }

            Apartment apt2 = apartmentRepository.findByAptCode("APT02").orElse(null);
            ConsentFormTemplate template2 = templateRepository.findByTemplateCode("TEMPLATE02").orElse(null);

            if (apt2 != null && template2 != null && !consentFormRepository.existsByFormCode("APT02_FORM01")) {
                ConsentForm form2 = ConsentForm.builder()
                        .apartment(apt2)
                        .template(template2)
                        .formCode("APT02_FORM01")
                        .formName("한강아파트 리모델링 동의서")
                        .htmlContent("<h1>한강아파트 리모델링 동의서</h1><p>본인은 한강아파트의 소유자로서 리모델링 사업에 동의합니다.</p><p>예상 기간: 18개월</p>")
                        .requiresSignature(true)
                        .requiresAttachment(false)
                        .minPersons(1)
                        .active(true)
                        .build();

                consentFormRepository.save(form2);
                log.info("아파트별 동의서 양식 생성 완료 - 한강아파트 리모델링 동의서");
            }
        };
    }
}