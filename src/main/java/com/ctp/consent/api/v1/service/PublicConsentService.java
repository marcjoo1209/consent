package com.ctp.consent.api.v1.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctp.consent.api.v1.dto.model.Apart;
import com.ctp.consent.api.v1.dto.model.ConsentPerson;
import com.ctp.consent.api.v1.dto.model.ConsentSubmission;
import com.ctp.consent.api.v1.dto.model.ConsentTemplate;
import com.ctp.consent.api.v1.dto.req.ConsentSubmissionRequestDTO;
import com.ctp.consent.api.v1.dto.req.ConsentSubmissionRequestDTO.AdditionalPersonDTO;
import com.ctp.consent.api.v1.dto.res.ConsentSubmissionResponseDTO;
import com.ctp.consent.api.v1.dto.res.ConsentTemplateResponseDTO;
import com.ctp.consent.api.v1.repository.ApartRepository;
import com.ctp.consent.api.v1.repository.ConsentSubmissionRepository;
import com.ctp.consent.api.v1.repository.ConsentTemplateRepository;
import com.ctp.consent.exception.ConsentNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicConsentService {

    private final ConsentTemplateRepository templateRepository;
    private final ConsentSubmissionRepository submissionRepository;
    private final ApartRepository apartRepository;
    private final ConsentTemplateService templateService;

    public ConsentTemplateResponseDTO getConsentForm(String apartCode, Long templateId) {
        // 아파트 확인
        apartRepository.findByAptCode(apartCode).orElseThrow(() -> new ConsentNotFoundException("아파트를 찾을 수 없습니다: " + apartCode));
        // 템플릿 확인 및 아파트 연결 확인
        Optional<ConsentTemplate> template = templateRepository.findByApartCodeAndTemplateId(apartCode, templateId);
        if (template.isEmpty()) {
            throw new ConsentNotFoundException("해당 아파트에 할당된 템플릿을 찾을 수 없습니다");
        }
        if (!template.get().getActive()) {
            throw new IllegalStateException("비활성화된 템플릿입니다");
        }
        return templateService.getTemplate(templateId);
    }

    @Transactional
    public ConsentSubmissionResponseDTO submitConsent(String apartCode, Long templateId, ConsentSubmissionRequestDTO requestDTO, HttpServletRequest request) {
        // 아파트 확인
        Apart apart = apartRepository.findByAptCode(apartCode).orElseThrow(() -> new ConsentNotFoundException("아파트를 찾을 수 없습니다: " + apartCode));
        // 템플릿 확인
        ConsentTemplate template = templateRepository.findByApartCodeAndTemplateId(apartCode, templateId).orElseThrow(() -> new ConsentNotFoundException("템플릿을 찾을 수 없습니다"));
        // 중복 제출 확인
        Optional<ConsentSubmission> existing = submissionRepository.findTopByApartIdAndBuildingAndUnitOrderBySubmittedAtDesc(apart.getId(), requestDTO.getBuilding(), requestDTO.getUnit());
        if (existing.isPresent() && "SUBMITTED".equals(existing.get().getStatus())) {
            LocalDateTime lastSubmission = existing.get().getSubmittedAt();
            if (lastSubmission != null && lastSubmission.isAfter(LocalDateTime.now().minusHours(24))) {
                throw new IllegalStateException("24시간 이내에 이미 동의서를 제출하셨습니다");
            }
        }

        // 동의서 제출 정보 생성
        ConsentSubmission submission = ConsentSubmission.builder()
                .template(template)
                .apart(apart)
                .building(requestDTO.getBuilding())
                .unit(requestDTO.getUnit())
                .representativeName(requestDTO.getRepresentativeName())
                .representativeBirth(requestDTO.getRepresentativeBirth())
                .representativePhone(formatPhoneNumber(requestDTO.getRepresentativePhone()))
                .emergencyContact(formatPhoneNumber(requestDTO.getEmergencyContact()))
                .currentAddress(requestDTO.getCurrentAddress())
                .privacyAgreement(requestDTO.getPrivacyAgreement())
                .marketingAgreement(requestDTO.getMarketingAgreement())
                .signatureData(requestDTO.getSignatureData())
                .status("SUBMITTED")
                .ipAddress(getClientIp(request))
                .userAgent(request.getHeader("User-Agent"))
                .build();

        ConsentSubmission saved = submissionRepository.save(submission);

        // 추가 가족 정보 저장
        if (requestDTO.getAdditionalPersons() != null) {
            for (AdditionalPersonDTO personDTO : requestDTO.getAdditionalPersons()) {
                if (personDTO.getName() != null && !personDTO.getName().isBlank()) {
                    ConsentPerson person = ConsentPerson.builder()
                            .submission(saved)
                            .name(personDTO.getName())
                            .birthDate(personDTO.getBirthDate())
                            .phoneNumber(formatPhoneNumber(personDTO.getPhoneNumber()))
                            .relation(personDTO.getRelation())
                            .build();
                    saved.getAdditionalPersons().add(person);
                }
            }
        }

        saved = submissionRepository.save(saved);
        // 확인 번호 생성
        String confirmationNumber = generateConfirmationNumber(saved.getId());

        log.info("동의서 제출 완료 - 아파트: {}, 동/호: {}/{}, 대표자: {}",apart.getAptName(), requestDTO.getBuilding(), requestDTO.getUnit(),requestDTO.getRepresentativeName());
        return ConsentSubmissionResponseDTO.builder()
                .id(saved.getId())
                .confirmationNumber(confirmationNumber)
                .apartName(apart.getAptName())
                .building(saved.getBuilding())
                .unit(saved.getUnit())
                .representativeName(saved.getRepresentativeName())
                .status(saved.getStatus())
                .submittedAt(saved.getSubmittedAt())
                .message("동의서가 성공적으로 제출되었습니다")
                .build();
    }

    public ConsentSubmissionResponseDTO verifySubmission(Long id) {
        ConsentSubmission submission = submissionRepository.findById(id).orElseThrow(() -> new ConsentNotFoundException("제출된 동의서를 찾을 수 없습니다"));
        return ConsentSubmissionResponseDTO.builder()
                .id(submission.getId())
                .confirmationNumber(generateConfirmationNumber(submission.getId()))
                .apartName(submission.getApart().getAptName())
                .building(submission.getBuilding())
                .unit(submission.getUnit())
                .representativeName(submission.getRepresentativeName())
                .status(submission.getStatus())
                .submittedAt(submission.getSubmittedAt())
                .build();
    }

    private String formatPhoneNumber(String phone) {
        if (phone == null || phone.isBlank()) {
            return null;
        }
        phone = phone.replaceAll("[^0-9]", "");
        if (phone.length() == 11) {
            return phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7);
        } else if (phone.length() == 10) {
            return phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6);
        }
        return phone;
    }

    private String generateConfirmationNumber(Long id) {
        return String.format("CST%s%06d", LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")), id);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}