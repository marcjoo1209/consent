package com.ctp.consent.api.v1.service;

import com.ctp.consent.api.v1.dto.PageResponse;
import com.ctp.consent.api.v1.dto.model.Apart;
import com.ctp.consent.api.v1.dto.req.ApartRequestDTO;
import com.ctp.consent.api.v1.dto.res.ApartResponseDTO;
import com.ctp.consent.api.v1.repository.ApartRepository;
import com.ctp.consent.exception.ConsentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApartService {

    private final ApartRepository apartRepository;

    public PageResponse<ApartResponseDTO> getAparts(String search, Boolean active, Pageable pageable) {
        Specification<Apart> spec = Specification.anyOf();

        if (search != null && !search.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("aptName")), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("aptCode")), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("address")), "%" + search.toLowerCase() + "%")));
        }

        if (active != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), active));
        }

        Page<Apart> apartPage = apartRepository.findAll(spec, pageable);
        Page<ApartResponseDTO> dtoPage = apartPage.map(this::toResponseDTO);

        return PageResponse.of(dtoPage);
    }

    public List<ApartResponseDTO> getAllActiveAparts() {
        List<Apart> aparts = apartRepository.findByActiveTrue();
        List<ApartResponseDTO> result = new ArrayList<>();
        for (Apart apart : aparts) {
            result.add(toResponseDTO(apart));
        }
        return result;
    }

    public ApartResponseDTO getApart(Long id) {
        Apart apart = apartRepository.findById(id)
                .orElseThrow(() -> new ConsentNotFoundException("아파트를 찾을 수 없습니다. ID: " + id));
        return toResponseDTO(apart);
    }

    public ApartResponseDTO getApartByCode(String aptCode) {
        Apart apart = apartRepository.findByAptCode(aptCode)
                .orElseThrow(() -> new ConsentNotFoundException("아파트를 찾을 수 없습니다. 코드: " + aptCode));
        return toResponseDTO(apart);
    }

    @Transactional
    public ApartResponseDTO createApart(ApartRequestDTO requestDTO) {
        if (apartRepository.existsByAptCode(requestDTO.getAptCode())) {
            throw new IllegalArgumentException("이미 존재하는 아파트 코드입니다: " + requestDTO.getAptCode());
        }

        Apart apart = Apart.builder()
                .aptCode(requestDTO.getAptCode())
                .aptName(requestDTO.getAptName())
                .address(requestDTO.getAddress())
                .totalDong(requestDTO.getTotalDong())
                .totalHousehold(requestDTO.getTotalHousehold())
                .active(true)
                .build();

        Apart savedApart = apartRepository.save(apart);
        log.info("아파트 생성: {}", savedApart.getAptName());

        return toResponseDTO(savedApart);
    }

    @Transactional
    public ApartResponseDTO updateApart(Long id, ApartRequestDTO requestDTO) {
        Apart apart = apartRepository.findById(id)
                .orElseThrow(() -> new ConsentNotFoundException("아파트를 찾을 수 없습니다. ID: " + id));

        if (!apart.getAptCode().equals(requestDTO.getAptCode()) &&
                apartRepository.existsByAptCode(requestDTO.getAptCode())) {
            throw new IllegalArgumentException("이미 존재하는 아파트 코드입니다: " + requestDTO.getAptCode());
        }

        apart.setAptCode(requestDTO.getAptCode());
        apart.setAptName(requestDTO.getAptName());
        apart.setAddress(requestDTO.getAddress());
        apart.setTotalDong(requestDTO.getTotalDong());
        apart.setTotalHousehold(requestDTO.getTotalHousehold());

        Apart updatedApart = apartRepository.save(apart);
        log.info("아파트 수정: {}", updatedApart.getAptName());

        return toResponseDTO(updatedApart);
    }

    @Transactional
    public void toggleActive(Long id) {
        Apart apart = apartRepository.findById(id).orElseThrow(() -> new ConsentNotFoundException("아파트를 찾을 수 없습니다. ID: " + id));
        apart.setActive(!apart.getActive());
        apartRepository.save(apart);
        log.info("아파트 상태 변경: {} -> {}", apart.getAptName(), apart.getActive() ? "활성" : "비활성");
    }

    @Transactional
    public void deleteApart(Long id) {
        Apart apart = apartRepository.findById(id).orElseThrow(() -> new ConsentNotFoundException("아파트를 찾을 수 없습니다. ID: " + id));
        apartRepository.delete(apart);
        log.info("아파트 삭제: {}", apart.getAptName());
    }

    public Map<String, List<String>> getApartUnits(Long id) {
        Apart apart = apartRepository.findById(id).orElseThrow(() -> new ConsentNotFoundException("아파트를 찾을 수 없습니다. ID: " + id));

        Map<String, List<String>> units = new HashMap<>();

        // 동/호수 체계 생성 (예시 데이터)
        if (apart.getTotalDong() != null) {
            for (int dong = 101; dong <= 100 + apart.getTotalDong(); dong++) {
                List<String> hoList = new ArrayList<>();
                for (int floor = 1; floor <= 15; floor++) {
                    for (int ho = 1; ho <= 4; ho++) {
                        hoList.add(String.format("%d%02d", floor, ho));
                    }
                }
                units.put(dong + "동", hoList);
            }
        }

        return units;
    }

    @Transactional
    public void updateUnitConfig(Long id, ApartRequestDTO.UnitConfig unitConfig) {
        Apart apart = apartRepository.findById(id)
                .orElseThrow(() -> new ConsentNotFoundException("아파트를 찾을 수 없습니다. ID: " + id));

        // 동/호수 체계 업데이트 로직
        apart.setTotalDong(unitConfig.getTotalDong());
        apart.setTotalHousehold(unitConfig.calculateTotalHousehold());

        apartRepository.save(apart);
        log.info("아파트 동/호수 체계 업데이트: {}", apart.getAptName());
    }

    private ApartResponseDTO toResponseDTO(Apart apart) {
        return ApartResponseDTO.builder()
                .id(apart.getId())
                .aptCode(apart.getAptCode())
                .aptName(apart.getAptName())
                .address(apart.getAddress())
                .totalDong(apart.getTotalDong())
                .totalHousehold(apart.getTotalHousehold())
                .active(apart.getActive())
                .createdAt(apart.getCreatedAt())
                .updatedAt(apart.getUpdatedAt())
                .build();
    }
}