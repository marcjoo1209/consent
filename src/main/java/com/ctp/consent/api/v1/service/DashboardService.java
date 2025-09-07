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
        long pending = consentRecordRepository.countByStatus(ConsentStatus.PENDING);
        long approve = consentRecordRepository.countByStatus(ConsentStatus.APPROVED);
        long reject = consentRecordRepository.countByStatus(ConsentStatus.REJECTED);


        return DashboardStatisticsDTO.builder()
                .total(total)
                .pending(pending)
                .approve(approve)
                .reject(reject)
                .build();
    }

}