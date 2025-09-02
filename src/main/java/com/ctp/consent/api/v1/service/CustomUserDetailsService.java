package com.ctp.consent.api.v1.service;

import com.ctp.consent.api.v1.dto.model.Admin;
import com.ctp.consent.api.v1.repository.AdminRepository;
import com.ctp.consent.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final AdminRepository adminRepository;
    
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsernameAndActiveTrue(username)
            .orElseThrow(() -> {
                log.warn("로그인 실패 - 사용자를 찾을 수 없음: {}", username);
                return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
            });
        
        log.info("로그인 시도 - 사용자: {}", username);
        return new CustomUserDetails(admin);
    }
}