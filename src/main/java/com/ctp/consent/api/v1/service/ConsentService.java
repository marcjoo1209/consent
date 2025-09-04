package com.ctp.consent.api.v1.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ctp.consent.api.v1.dto.model.ConsentRecord;
import com.ctp.consent.api.v1.dto.req.ConsentSearchRequest;
import com.ctp.consent.api.v1.dto.res.ConsentDetailResponse;
import com.ctp.consent.api.v1.dto.res.ConsentStatisticsResponse;
import com.ctp.consent.api.v1.repository.ConsentRecordRepository;
import com.ctp.consent.config.enums.ConsentStatus;
import com.ctp.consent.exception.ConsentNotFoundException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsentService {

    private final ConsentRecordRepository consentRecordRepository;

    public Page<ConsentRecord> searchConsents(ConsentSearchRequest request, Pageable pageable) {
        Specification<ConsentRecord> spec = createSpecification(request);
        return consentRecordRepository.findAll(spec, pageable);
    }

    private Specification<ConsentRecord> createSpecification(ConsentSearchRequest request) {
        Specification<ConsentRecord> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (request.getApartmentId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("apart").get("id"), request.getApartmentId()));
        }

        if (StringUtils.hasText(request.getDong())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("aptDong"), request.getDong()));
        }

        if (StringUtils.hasText(request.getHo())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("aptHo"), request.getHo()));
        }

        if (request.getStatus() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), request.getStatus()));
        }

        if (request.getStartDate() != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"),
                    request.getStartDate().atStartOfDay()));
        }

        if (request.getEndDate() != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"),
                    request.getEndDate().atTime(23, 59, 59)));
        }

        if (StringUtils.hasText(request.getKeyword())) {
            String keyword = "%" + request.getKeyword() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(root.get("aptDong"), keyword),
                    cb.like(root.get("aptHo"), keyword)));
        }

        if (StringUtils.hasText(request.getParticipantName())) {
            spec = spec.and((root, query, cb) -> {
                var subquery = query.subquery(Long.class);
                var participantRoot = subquery.from(ConsentRecord.class);
                var join = participantRoot.join("persons");

                subquery.select(participantRoot.get("id"))
                        .where(cb.like(join.get("name"), "%" + request.getParticipantName() + "%"));

                return cb.in(root.get("id")).value(subquery);
            });
        }

        return spec;
    }

    public ConsentDetailResponse getConsentDetail(Long id) {
        ConsentRecord consent = consentRecordRepository.findById(id)
                .orElseThrow(() -> new ConsentNotFoundException("동의서를 찾을 수 없습니다. ID: " + id));

        return ConsentDetailResponse.from(consent);
    }

    @Transactional
    public void approveConsent(Long id, String comment) {
        ConsentRecord consent = consentRecordRepository.findById(id)
                .orElseThrow(() -> new ConsentNotFoundException("동의서를 찾을 수 없습니다. ID: " + id));

        consent.setStatus(ConsentStatus.APPROVED);
        consent.setApprovedAt(LocalDateTime.now());
        consent.setApprovalComment(comment);

        consentRecordRepository.save(consent);
        log.info("동의서 승인 완료: {}", id);
    }

    @Transactional
    public void rejectConsent(Long id, String reason) {
        ConsentRecord consent = consentRecordRepository.findById(id)
                .orElseThrow(() -> new ConsentNotFoundException("동의서를 찾을 수 없습니다. ID: " + id));

        consent.setStatus(ConsentStatus.REJECTED);
        consent.setRejectedAt(LocalDateTime.now());
        consent.setRejectionReason(reason);

        consentRecordRepository.save(consent);
        log.info("동의서 반려 완료: {}", id);
    }

    @Transactional
    public int bulkApprove(List<Long> ids) {
        int count = 0;
        for (Long id : ids) {
            try {
                approveConsent(id, null);
                count++;
            } catch (Exception e) {
                log.error("동의서 승인 실패: {}", id, e);
            }
        }
        return count;
    }

    @Transactional
    public int bulkReject(List<Long> ids, String reason) {
        int count = 0;
        for (Long id : ids) {
            try {
                rejectConsent(id, reason);
                count++;
            } catch (Exception e) {
                log.error("동의서 반려 실패: {}", id, e);
            }
        }
        return count;
    }

    public void exportToExcel(ConsentSearchRequest request, HttpServletResponse response) {
        try {
            Specification<ConsentRecord> spec = createSpecification(request);
            List<ConsentRecord> consents = consentRecordRepository.findAll(spec);
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("동의서 목록");
            createExcelHeader(sheet);
            fillExcelData(sheet, consents);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=consent_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx");
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            log.error("Excel 다운로드 실패", e);
            throw new RuntimeException("Excel 파일 생성 중 오류가 발생했습니다.");
        }
    }

    private void createExcelHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = { "번호", "아파트", "동", "호", "신청자", "전화번호", "상태", "신청일", "승인일", "반려일", "반려사유" };
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 15 * 256);
        }
    }

    private void fillExcelData(Sheet sheet, List<ConsentRecord> consents) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        int rowNum = 1;
        for (ConsentRecord consent : consents) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(consent.getId());
            row.createCell(1).setCellValue(consent.getApart() != null ? consent.getApart().getAptName() : "");
            row.createCell(2).setCellValue(consent.getAptDong());
            row.createCell(3).setCellValue(consent.getAptHo());
            String participantName = consent.getPersons().isEmpty() ? "" : consent.getPersons().get(0).getName();
            row.createCell(4).setCellValue(participantName);
            String phoneNumber = consent.getPersons().isEmpty() ? "" : consent.getPersons().get(0).getPhoneNumber();
            row.createCell(5).setCellValue(phoneNumber);
            row.createCell(6).setCellValue(consent.getStatus() != null ? consent.getStatus().getDescription() : "");
            row.createCell(7).setCellValue(consent.getCreatedAt() != null ? consent.getCreatedAt().format(formatter) : "");
            row.createCell(8).setCellValue(consent.getApprovedAt() != null ? consent.getApprovedAt().format(formatter) : "");
            row.createCell(9).setCellValue(consent.getRejectedAt() != null ? consent.getRejectedAt().format(formatter) : "");
            row.createCell(10).setCellValue(consent.getRejectionReason() != null ? consent.getRejectionReason() : "");
        }
    }

    public ConsentStatisticsResponse getStatistics(Long apartmentId) {
        ConsentStatisticsResponse stats = new ConsentStatisticsResponse();

        if (apartmentId != null) {
            stats.setTotal(consentRecordRepository.countByApartId(apartmentId));
            stats.setPending(consentRecordRepository.countByApartIdAndStatus(apartmentId, ConsentStatus.PENDING));
            stats.setApproved(consentRecordRepository.countByApartIdAndStatus(apartmentId, ConsentStatus.APPROVED));
            stats.setRejected(consentRecordRepository.countByApartIdAndStatus(apartmentId, ConsentStatus.REJECTED));
        } else {
            stats.setTotal(consentRecordRepository.count());
            stats.setPending(consentRecordRepository.countByStatus(ConsentStatus.PENDING));
            stats.setApproved(consentRecordRepository.countByStatus(ConsentStatus.APPROVED));
            stats.setRejected(consentRecordRepository.countByStatus(ConsentStatus.REJECTED));
        }

        stats.setApprovalRate(stats.getTotal() > 0 ? (double) stats.getApproved() / stats.getTotal() * 100 : 0);

        return stats;
    }
}