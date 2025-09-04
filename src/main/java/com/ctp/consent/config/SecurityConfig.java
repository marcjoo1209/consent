package com.ctp.consent.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Value("${app.security.remember-me.key:mySecretRememberMeKey}")
    private String rememberMeKey;

    @Value("${app.security.remember-me.validity:1209600}")
    private int rememberMeValidity;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CORS 설정
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // CSRF 보호 설정
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
            // 헤더 설정
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
            // 권한 설정
            .authorizeHttpRequests(auth -> auth
                    // 정적 리소스 및 공개 경로
                    .requestMatchers(Constants.Url.ALLOW_STATIC).permitAll()
                    // 루트 경로와 에러 페이지 허용
                    .requestMatchers("/", "/error").permitAll()
                    // 사용자 동의서 페이지 (인증 불필요)
                    .requestMatchers(Constants.Url.ALLOW_CONSENT).permitAll()
                    // 관리자 페이지 (인증 필요)
                    .requestMatchers(Constants.Url.ADMIN_URL)
                    .hasAnyRole(Constants.Role.ADMIN, Constants.Role.SUPER_ADMIN)
                    // API 엔드포인트
                    .requestMatchers(Constants.Url.ADMIN_API)
                    .hasAnyRole(Constants.Role.ADMIN, Constants.Role.SUPER_ADMIN)
                    // 나머지 모든 요청은 인증 필요
                    .anyRequest().authenticated())
            // 로그인 설정
            .formLogin(login -> login
                    .loginPage(Constants.Url.ADMIN_LOGIN)
                    .loginProcessingUrl(Constants.Url.ADMIN_LOGIN)
                    .successHandler(authenticationSuccessHandler())
                    .failureHandler(authenticationFailureHandler())
                    .permitAll())
            // 로그아웃 설정
            .logout(logout -> logout
                    .logoutUrl(Constants.Url.ADMIN_LOGOUT)
                    .logoutSuccessHandler(logoutSuccessHandler())
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .clearAuthentication(true)
                    .permitAll())
            // Remember Me 설정
            .rememberMe(remember -> remember
                    .key(rememberMeKey)
                    .tokenValiditySeconds(rememberMeValidity)
                    .userDetailsService(customUserDetailsService)
                    .rememberMeParameter("remember-me")
                    .rememberMeCookieName("remember-me"))
            // 세션 관리
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .sessionFixation().migrateSession()
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(true)
                    .expiredUrl(Constants.Url.ADMIN_LOGIN + "?expired"))
            // 예외 처리
            .exceptionHandling(exception -> exception
                    .authenticationEntryPoint((request, response, authException) -> {
                        if (request.getRequestURI().startsWith("/api/")) {
                            response.sendError(401, "Unauthorized");
                        } else {
                            response.sendRedirect(Constants.Url.ADMIN_LOGIN);
                        }
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        if (request.getRequestURI().startsWith("/api/")) {
                            response.sendError(403, "Access Denied");
                        } else {
                            response.sendRedirect("/error/403");
                        }
                    }));
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            response.sendRedirect(Constants.Url.DASHBOARD);
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.sendRedirect(Constants.Url.ADMIN_LOGIN + "?error=true");
        };
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            response.sendRedirect(Constants.Url.ADMIN_LOGIN + "?logout=true");
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}