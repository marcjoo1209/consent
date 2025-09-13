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

### 3.3 동의서 템플릿 관리 시스템 (관리자용)
#### 기능 목표
- 관리자가 동의서 템플릿을 생성/관리하고 아파트와 연결

#### 상세 요구사항
- **동의서 템플릿 관리**
  - 템플릿 생성/수정/삭제 (CRUD)
  - WYSIWYG 에디터로 내용 편집
  - 템플릿 복사 기능
  - 템플릿 버전 관리
- **아파트-템플릿 연결**
  - 아파트별 템플릿 할당
  - 활성/비활성 상태 관리
  - 유효기간 설정
- **제출된 동의서 관리**
  - 제출된 동의서 목록 조회
  - 필터링 (아파트, 동/호, 상태, 날짜)
  - 상세 조회 및 Excel 다운로드
  - 승인/반려 처리

### 3.4 공개 동의서 작성 페이지 (입주민용 - 로그인 불필요)
#### 기능 목표
- 입주민이 로그인 없이 접속하여 동의서를 작성할 수 있는 모바일 최적화 페이지

#### 상세 요구사항
- **접근 방식**
  - URL: `/consent/{apartCode}/{templateId}`
  - 로그인 불필요 (permitAll)
  - 아파트 코드와 템플릿 ID로 접근
- **페이지 구성**
  - 아파트 정보 자동 표시 (템플릿에서 가져옴)
  - 동의서 내용 표시 (템플릿 내용)
  - 입력 폼 영역
- **필수 입력 항목**
  - 동/호수 선택
  - 대표자 정보: 이름, 생년월일, 연락처
  - 추가 가족 정보 (최대 3명, 선택)
  - 현 거주지 주소
  - 비상연락처
- **동의 항목**
  - 개인정보 수집/이용 동의 (필수)
  - 마케팅 정보 수신 동의 (선택)
- **디지털 서명**
  - Canvas API 기반 서명 패드
  - 서명 지우기/다시 그리기
  - 서명 이미지 Base64 저장
- **제출 처리**
  - 유효성 검증 (JavaScript)
  - POST /consent/submit로 전송
  - 제출 완료 페이지 표시
  - 확인 번호 발급
- **모바일 최적화**
  - 반응형 디자인 (Tailwind CSS)
  - 터치 친화적 UI
  - 전화번호 자동 포맷팅
  - 주소 검색 (Daum API)

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
CREATE TABLE aparts (
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
    apart_id BIGINT NOT NULL,
    form_id BIGINT NOT NULL,
    building VARCHAR(20) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    status VARCHAR(20) DEFAULT 'DRAFT', -- DRAFT, SUBMITTED, APPROVED, REJECTED
    submitted_at DATETIME,
    approved_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (apart_id) REFERENCES aparts(id),
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
    APT_CD as apart_code,
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


## 8. 부록

### 8.1 주요 URL 구조
```
# 관리자 (인증 필요)
/admin/login                    # 관리자 로그인
/admin/dashboard               # 관리자 대시보드
/admin/templates              # 동의서 템플릿 관리
/admin/consents               # 제출된 동의서 관리
/admin/aparts                 # 아파트 관리
/admin/users                  # 사용자 관리

# 공개 페이지 (입주민용 - 인증 불필요)
/consent/{apartCode}/{templateId}  # 동의서 작성 페이지
/consent/submit                    # 동의서 제출 (POST)
/consent/verify/{id}               # 제출된 동의서 확인
/api/address/search                # 주소 검색 API
```

### 8.2 개발 환경 설정
- **JDK**: OpenJDK 21
- **IDE**: IntelliJ IDEA / Eclipse
- **Database**: MariaDB 10.6+
- **Build Tool**: Maven 3.8+
- **CSS Framework**: Tailwind CSS 3.0+
- **Node.js**: 16+ (Tailwind CSS 빌드용)