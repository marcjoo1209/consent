package com.ctp.consent.api.v1.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctp.consent.api.v1.dto.res.DashboardStatisticsDTO;
import com.ctp.consent.api.v1.repository.ConsentRecordRepository;
import com.ctp.consent.config.enums.ConsentStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final ConsentRecordRepository consentRecordRepository;

    public DashboardStatisticsDTO getDashboardStatistics() {
        long total = consentRecordRepository.count();
        long issue = consentRecordRepository.countByStatus(ConsentStatus.ISSUE);
        long submitted = consentRecordRepository.countByStatus(ConsentStatus.SUBMITTED);
        long approve = consentRecordRepository.countByStatus(ConsentStatus.APPROVED);
        long agreed = consentRecordRepository.countByStatus(ConsentStatus.AGREED);
        long reject = consentRecordRepository.countByStatus(ConsentStatus.REJECTED);

    // ISSUE("발행"),
    // SUBMITTED("제출됨"),
    // APPROVED("승인"),
    // AGREED("동의"),
    // REJECTED("반려");

        return DashboardStatisticsDTO.builder()
                .total(total)
                .issue(issue)
                .issue(submitted)
                .approve(approve)
                .issue(agreed)
                .reject(reject)
                .build();
    }

}