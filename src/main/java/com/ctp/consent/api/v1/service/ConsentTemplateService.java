package com.ctp.consent.api.v1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctp.consent.api.v1.dto.PageResponse;
import com.ctp.consent.api.v1.dto.model.*;
import com.ctp.consent.api.v1.dto.req.ConsentTemplateRequestDTO;
import com.ctp.consent.api.v1.dto.res.ConsentTemplateResponseDTO;
import com.ctp.consent.api.v1.dto.res.ConsentTemplateResponseDTO.ApartInfoDTO;
import com.ctp.consent.api.v1.repository.*;
import com.ctp.consent.exception.ConsentNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsentTemplateService {

    private final ConsentTemplateRepository templateRepository;
    private final ApartConsentTemplateRepository apartTemplateRepository;
    private final AdminRepository adminRepository;
    private final ApartRepository apartRepository;

    public PageResponse<ConsentTemplateResponseDTO> getTemplates(String search, Boolean active, Pageable pageable) {
        Specification<ConsentTemplate> spec = Specification.anyOf();

        if (search != null && !search.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("title")), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("description")), "%" + search.toLowerCase() + "%")));
        }

        if (active != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), active));
        }

        Page<ConsentTemplate> templatePage = templateRepository.findAll(spec, pageable);
        Page<ConsentTemplateResponseDTO> dtoPage = templatePage.map(this::toResponseDTO);

        return PageResponse.of(dtoPage);
    }

    public ConsentTemplateResponseDTO getTemplate(Long id) {
        ConsentTemplate template = templateRepository.findById(id).orElseThrow(() -> new ConsentNotFoundException("템플릿을 찾을 수 없습니다. ID: " + id));
        return toResponseDTO(template);
    }

    @Transactional
    public ConsentTemplateResponseDTO createTemplate(ConsentTemplateRequestDTO requestDTO) {
        if (templateRepository.existsByTitle(requestDTO.getTitle())) {
            throw new IllegalArgumentException("이미 존재하는 템플릿 제목입니다: " + requestDTO.getTitle());
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Admin admin = adminRepository.findByUsername(username).orElseThrow(() -> new ConsentNotFoundException("관리자를 찾을 수 없습니다"));

        ConsentTemplate template = ConsentTemplate.builder()
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .content(requestDTO.getContent())
                .active(requestDTO.getActive())
                .createdBy(admin)
                .build();

        ConsentTemplate savedTemplate = templateRepository.save(template);
        log.info("동의서 템플릿 생성: {}", savedTemplate.getTitle());

        return toResponseDTO(savedTemplate);
    }

    @Transactional
    public ConsentTemplateResponseDTO updateTemplate(Long id, ConsentTemplateRequestDTO requestDTO) {
        ConsentTemplate template = templateRepository.findById(id).orElseThrow(() -> new ConsentNotFoundException("템플릿을 찾을 수 없습니다. ID: " + id));

        template.setTitle(requestDTO.getTitle());
        template.setDescription(requestDTO.getDescription());
        template.setContent(requestDTO.getContent());
        template.setActive(requestDTO.getActive());
        template.setVersion(template.getVersion() + 1);

        ConsentTemplate updatedTemplate = templateRepository.save(template);
        log.info("동의서 템플릿 수정: {}", updatedTemplate.getTitle());

        return toResponseDTO(updatedTemplate);
    }

    @Transactional
    public void deleteTemplate(Long id) {
        ConsentTemplate template = templateRepository.findById(id).orElseThrow(() -> new ConsentNotFoundException("템플릿을 찾을 수 없습니다. ID: " + id));
        templateRepository.delete(template);
        log.info("동의서 템플릿 삭제: {}", template.getTitle());
    }

    @Transactional
    public void assignTemplateToApart(Long templateId, Long apartId) {
        ConsentTemplate template = templateRepository.findById(templateId).orElseThrow(() -> new ConsentNotFoundException("템플릿을 찾을 수 없습니다. ID: " + templateId));
        Apart apart = apartRepository.findById(apartId).orElseThrow(() -> new ConsentNotFoundException("아파트를 찾을 수 없습니다. ID: " + apartId));
        if (apartTemplateRepository.existsByApartIdAndTemplateIdAndActiveTrue(apartId, templateId)) {
            throw new IllegalArgumentException("이미 해당 아파트에 템플릿이 할당되어 있습니다");
        }
        ApartConsentTemplate apartTemplate = ApartConsentTemplate.builder()
                .apart(apart)
                .template(template)
                .active(true)
                .build();
        apartTemplateRepository.save(apartTemplate);
        log.info("템플릿 {} 을(를) 아파트 {} 에 할당", template.getTitle(), apart.getAptName());
    }

    @Transactional
    public void unassignTemplateFromApart(Long templateId, Long apartId) {
        apartTemplateRepository.deleteByApartIdAndTemplateId(apartId, templateId);
        log.info("템플릿 ID {} 을(를) 아파트 ID {} 에서 해제", templateId, apartId);
    }

    public List<ConsentTemplateResponseDTO> getTemplatesByApart(Long apartId) {
        List<ConsentTemplate> templates = templateRepository.findByApartIdAndActive(apartId);
        return templates.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private ConsentTemplateResponseDTO toResponseDTO(ConsentTemplate template) {
        List<ApartInfoDTO> assignedAparts = template.getApartTemplates().stream()
                .filter(at -> at.getActive())
                .map(at -> ApartInfoDTO.builder()
                        .id(at.getApart().getId())
                        .code(at.getApart().getAptCode())
                        .name(at.getApart().getAptName())
                        .active(at.getActive())
                        .build())
                .collect(Collectors.toList());

        List<Long> assignedApartIds = assignedAparts.stream()
                .map(ApartInfoDTO::getId)
                .collect(Collectors.toList());

        return ConsentTemplateResponseDTO.builder()
                .id(template.getId())
                .title(template.getTitle())
                .description(template.getDescription())
                .content(template.getContent())
                .active(template.getActive())
                .version(template.getVersion())
                .createdAt(template.getCreatedAt())
                .updatedAt(template.getUpdatedAt())
                .createdByName(template.getCreatedBy() != null ? template.getCreatedBy().getName() : null)
                .assignedAparts(assignedAparts)
                .assignedApartIds(assignedApartIds)
                .build();
    }
}