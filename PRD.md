# 아파트 동의서 관리 시스템 PRD (Product Requirements Document)

## 1. 프로젝트 개요

### 1.1 프로젝트 목적
아파트 입주민의 개인정보 수집 및 이용 동의서를 체계적으로 관리하는 현대적인 웹 기반 시스템 구축

### 1.2 현재 시스템 분석
- **기존 기술 스택**: Spring MVC 4.3.3, MyBatis, JSP, MariaDB
- **주요 문제점**: 복잡한 아키텍처, 반응형 미지원, 유지보수 어려움
- **개선 방향**: 완전 새로운 시스템 구축 (Spring Boot, JPA, Thymeleaf 기반)

### 1.3 신규 기술 스택
- **Backend**: Spring Boot 2.7+, Spring Data JPA, Spring Security
- **Frontend**: Thymeleaf, Tailwind CSS, HTML5 (모바일 친화적 반응형)
- **Database**: MariaDB (새 DB: ctp)
- **Build Tool**: Maven
- **Infrastructure**: 클라우드 환경 (Azure/AWS)

## 2. 사용자 정의

### 2.1 주요 사용자 그룹
- **입주민**: 동의서 작성 및 조회
- **관리자**: 동의서 관리, 아파트 정보 관리, 사용자 권한 관리

## 3. 핵심 기능 요구사항

### 3.1 관리자 로그인 화면
#### 기능 목표
- 안전하고 직관적인 관리자 인증 시스템

#### 상세 요구사항
- **로그인 폼**: 아이디/비밀번호 입력
- **보안 기능**: Spring Security 기반 인증, CSRF 보호
- **세션 관리**: 자동 로그아웃, 중복 로그인 방지
- **반응형 디자인**: 모바일/태블릿 지원
- **에러 처리**: 로그인 실패 시 명확한 메시지

### 3.2 관리자 페이지
#### 기능 목표
- 효율적인 동의서 및 시스템 관리 인터페이스

#### 상세 요구사항
- **대시보드**
  - 전체 동의서 현황 (총 건수, 최근 등록 현황)
  - 아파트별 동의서 통계
  - 월별 등록 트렌드 차트
- **동의서 관리**
  - 동의서 목록 조회 (페이징, 검색, 필터링)
  - 동의서 상세 조회 및 수정
  - 일괄 처리 기능 (승인, 삭제)
  - Excel 다운로드 기능
- **아파트 관리**
  - 아파트 등록/수정/삭제
  - 동/호수 체계 설정
  - 아파트 활성화/비활성화
- **사용자 관리**
  - 관리자 계정 관리
  - 권한 설정
- **시스템 설정**
  - 양식 관리
  - 시스템 환경 설정

### 3.3 동의서 관리 시스템
#### 기능 목표
- 체계적인 동의서 생성, 수정, 조회 기능

#### 상세 요구사항
- **동의서 템플릿 관리**
  - 다양한 동의서 양식 지원
  - 템플릿 생성/수정 기능
  - 양식별 필수/선택 항목 설정
- **동의서 처리 워크플로우**
  - 작성 → 임시저장 → 제출 → 검토 → 승인/반려
- **버전 관리**: 동의서 수정 이력 추적
- **파일 첨부**: 이미지 및 문서 첨부 지원
- **서명 기능**: 디지털 서명 (Canvas API 활용)

### 3.4 사용자 페이지 (모바일 친화적 동의서 페이지)
#### 기능 목표
- HTML5 기반 모바일 최적화된 반응형 동의서 작성 인터페이스

#### 상세 요구사항
- **반응형 디자인**
  - 모바일 우선 설계 (Mobile First)
  - 터치 친화적 UI/UX
  - 가로/세로 화면 대응
  - Tailwind CSS 유틸리티 클래스 활용
- **동의서 작성 기능**
  - 단계별 진행 인디케이터
  - 실시간 유효성 검증
  - 자동 저장 기능
  - 진행률 표시
- **개인정보 입력**
  - 기본 정보: 이름, 생년월일, 연락처
  - 주소 정보: 우편번호 검색 API 연동
  - 추가 사용자 정보 (최대 4명)
  - 비상연락처
- **주소 검색 기능**
  - Daum 우편번호 API 연동
  - 도로명/지번 주소 지원
- **파일 업로드**
  - 이미지 다중 업로드 (최대 10MB/파일)
  - 진행률 표시
  - 미리보기 기능
- **디지털 서명**
  - Canvas API 기반 서명 패드
  - 서명 지우기/다시 그리기
  - 서명 이미지 저장 (Base64)
- **동의서 조회**
  - 제출된 동의서 확인
  - PDF 다운로드
  - QR코드 생성 (공유 링크)

## 4. 데이터베이스 설계 (CTP)

### 4.1 신규 테이블 설계 (Human-Friendly 명명규칙)

#### 4.1.1 사용자 관리
```sql
-- 관리자 테이블
CREATE TABLE administrators (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 아파트 정보 테이블
CREATE TABLE apartments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    address VARCHAR(500),
    total_units INT,
    completion_date DATE,
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 동의서 양식 테이블
CREATE TABLE consent_forms (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(10) NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    template_path VARCHAR(500),
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 4.1.2 동의서 관리
```sql
-- 동의서 정보 테이블
CREATE TABLE consent_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    apartment_id BIGINT NOT NULL,
    form_id BIGINT NOT NULL,
    building VARCHAR(20) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'DRAFT', -- DRAFT, SUBMITTED, APPROVED, REJECTED
    submitted_at DATETIME,
    approved_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (apartment_id) REFERENCES apartments(id),
    FOREIGN KEY (form_id) REFERENCES consent_forms(id)
);

-- 개인정보 테이블
CREATE TABLE personal_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    consent_record_id BIGINT NOT NULL,
    primary_name VARCHAR(100) NOT NULL,
    primary_birth_date VARCHAR(8),
    primary_phone VARCHAR(50) NOT NULL,
    additional_name_1 VARCHAR(100),
    additional_birth_date_1 VARCHAR(8),
    additional_phone_1 VARCHAR(50),
    additional_name_2 VARCHAR(100),
    additional_birth_date_2 VARCHAR(8),
    additional_phone_2 VARCHAR(50),
    additional_name_3 VARCHAR(100),
    additional_birth_date_3 VARCHAR(8),
    additional_phone_3 VARCHAR(50),
    emergency_contact VARCHAR(50),
    privacy_agreement BOOLEAN DEFAULT FALSE,
    marketing_agreement BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (consent_record_id) REFERENCES consent_records(id) ON DELETE CASCADE
);

-- 주소 정보 테이블
CREATE TABLE addresses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    consent_record_id BIGINT NOT NULL,
    address_type VARCHAR(20) NOT NULL, -- CURRENT, PREVIOUS
    postal_code VARCHAR(10),
    road_address VARCHAR(200),
    jibun_address VARCHAR(200),
    detail_address VARCHAR(200),
    extra_address VARCHAR(200),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (consent_record_id) REFERENCES consent_records(id) ON DELETE CASCADE
);

-- 첨부파일 테이블
CREATE TABLE attachments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    consent_record_id BIGINT NOT NULL,
    original_filename VARCHAR(500) NOT NULL,
    stored_filename VARCHAR(500) NOT NULL,
    file_path VARCHAR(1000) NOT NULL,
    file_size BIGINT,
    content_type VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (consent_record_id) REFERENCES consent_records(id) ON DELETE CASCADE
);

-- 서명 정보 테이블
CREATE TABLE signatures (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    consent_record_id BIGINT NOT NULL,
    signature_data LONGTEXT NOT NULL, -- Base64 encoded signature
    signer_name VARCHAR(100) NOT NULL,
    signed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent TEXT,
    FOREIGN KEY (consent_record_id) REFERENCES consent_records(id) ON DELETE CASCADE
);

-- 동의서 처리 이력 테이블
CREATE TABLE consent_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    consent_record_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    comment TEXT,
    processed_by BIGINT,
    processed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (consent_record_id) REFERENCES consent_records(id) ON DELETE CASCADE,
    FOREIGN KEY (processed_by) REFERENCES administrators(id)
);
```

### 4.2 기존 데이터 연동 (View 기반)

#### 4.2.1 기존 테이블 매핑 View
```sql
-- 기존 CONSENT_INFO를 새 구조로 매핑하는 View
CREATE VIEW legacy_consent_mapping AS
SELECT 
    CONCAT(APT_CD, FORM_CD, APT_DONG, APT_HO) as legacy_key,
    APT_CD as apartment_code,
    FORM_CD as form_code,
    APT_DONG as building,
    APT_HO as unit,
    USER_NM1 as primary_name,
    USER_BIRTH_DT1 as primary_birth_date,
    USER_NUM1 as primary_phone,
    USER_NM2 as additional_name_1,
    USER_BIRTH_DT2 as additional_birth_date_1,
    USER_NUM2 as additional_phone_1,
    USER_NM3 as additional_name_2,
    USER_BIRTH_DT3 as additional_birth_date_2,
    USER_NUM3 as additional_phone_2,
    USER_NM11 as additional_name_3,
    USER_BIRTH_DT11 as additional_birth_date_3,
    USER_NUM11 as additional_phone_3,
    EMRG_NUM as emergency_contact,
    AGREE_PRIVACY_YN as privacy_agreement,
    AGREE_PRIVACY_YN2 as marketing_agreement,
    FST_RGST_DTM as created_at
FROM CONSENT_INFO 
WHERE DELYN != 'Y' OR DELYN IS NULL;
```

## 5. 비기능적 요구사항

### 5.1 성능 요구사항
- **응답 시간**: 페이지 로딩 2초 이내
- **동시 사용자**: 최대 1,000명 동시 접속 지원
- **파일 업로드**: 이미지당 최대 10MB, 총 100MB 제한

### 5.2 보안 요구사항
- **HTTPS**: 전체 통신 암호화
- **Spring Security**: 최신 버전 보안 적용
- **데이터 암호화**: 개인정보 AES 암호화
- **CSRF 보호**: 모든 폼에 CSRF 토큰 적용
- **XSS 방지**: Thymeleaf 자동 이스케이핑

### 5.3 사용성 요구사항
- **반응형 웹**: 모든 디바이스 완벽 지원 (Tailwind CSS 반응형 유틸리티)
- **PWA 지원**: Progressive Web App 기능
- **접근성**: WCAG 2.1 AA 수준 준수
- **다국어**: 한국어 기본, 확장 가능한 구조
- **디자인 시스템**: Tailwind CSS 기반 일관된 디자인 토큰

## 6. 기술 아키텍처

### 6.1 시스템 아키텍처
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │    Backend      │    │    Database     │
│                 │    │                 │    │                 │
│  Thymeleaf      │◄──►│  Spring Boot    │◄──►│    MariaDB      │
│  Tailwind CSS   │    │  Spring Data JPA│    │    (CTP)        │
│  HTML5/CSS3     │    │  Spring Security│    │                 │
│  JavaScript     │    │  REST API       │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ▼
                        ┌─────────────────┐
                        │  Infrastructure │
                        │                 │
                        │  Cloud Platform │
                        │  File Storage   │
                        │  SSL/TLS        │
                        └─────────────────┘
```

### 6.2 패키지 구조
```
src/main/java/com/ctp/consent/
├── ConsentApplication.java          # Spring Boot Main
├── config/                         # Configuration
│   ├── SecurityConfig.java
│   ├── WebConfig.java
│   └── DatabaseConfig.java
├── controller/                     # Controllers
│   ├── admin/                     # Admin controllers
│   ├── api/                       # REST API controllers
│   └── ConsentController.java     # User consent controller
├── service/                       # Business logic
├── repository/                    # JPA Repositories
├── entity/                        # JPA Entities
├── dto/                          # Data Transfer Objects
├── util/                         # Utility classes
└── exception/                    # Custom exceptions
```

## 7. 개발 계획

### 7.1 Phase 1: 기반 구조 구축
- Spring Boot 프로젝트 초기 설정
- 데이터베이스 스키마 생성 (CTP)
- 기존 데이터 마이그레이션 View 생성
- Spring Security 기본 설정

### 7.2 Phase 2: 관리자 기능 개발
- 관리자 로그인 시스템 구현
- 관리자 대시보드 개발
- 동의서 관리 기능 구현
- 아파트 관리 기능 구현

### 7.3 Phase 3: 사용자 동의서 시스템
- 반응형 동의서 작성 페이지 구현
- 파일 업로드 기능 구현
- 디지털 서명 기능 구현
- 동의서 조회 및 PDF 생성

## 8. 성공 지표

### 8.1 기술적 지표
- **코드 커버리지**: 80% 이상
- **성능**: 평균 응답시간 1초 이내
- **보안**: OWASP Top 10 취약점 제로

### 8.2 비즈니스 지표
- **사용자 만족도**: 모바일 UX 4.5/5.0 이상
- **시스템 가동률**: 99.9% 이상
- **처리 효율성**: 동의서 작성 시간 50% 단축

## 9. 위험 요소 및 대응 방안

### 9.1 기술적 위험
- **데이터 마이그레이션**: View 기반 점진적 마이그레이션
- **성능 이슈**: JPA N+1 문제 방지, 쿼리 최적화
- **브라우저 호환성**: Polyfill 및 Progressive Enhancement

### 9.2 비즈니스 위험
- **사용자 적응**: 단계적 롤아웃 및 사용자 교육
- **데이터 무결성**: 철저한 테스트 및 백업 체계
- **보안 이슈**: 정기적인 보안 감사 및 업데이트

## 10. 부록

### 10.1 주요 URL 구조
```
# 관리자
/admin/login                    # 관리자 로그인
/admin/dashboard               # 관리자 대시보드
/admin/consents               # 동의서 관리
/admin/apartments             # 아파트 관리

# 사용자 (모바일 친화적)
/consent/{apartmentCode}       # 동의서 작성
/consent/{id}/view            # 동의서 조회
/consent/{id}/pdf             # PDF 다운로드
/api/address/search           # 주소 검색 API
```

### 10.2 개발 환경 설정
- **JDK**: OpenJDK 21
- **IDE**: IntelliJ IDEA / Eclipse
- **Database**: MariaDB 10.6+
- **Build Tool**: Maven 3.8+
- **CSS Framework**: Tailwind CSS 3.0+
- **Node.js**: 16+ (Tailwind CSS 빌드용)

### 10.3 배포 전략
- **CI/CD**: GitHub Actions / Jenkins
- **컨테이너**: Docker + Kubernetes
- **모니터링**: Actuator + Micrometer
- **로그**: Logback + ELK Stack