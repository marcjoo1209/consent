# 아파트 동의서 관리 시스템 개발 체크리스트

## 📋 Phase 1: 어플리케이션 공통 설정 및 기반 구조

### 🔧 1.1 프로젝트 초기 설정
- [x] Spring Boot 프로젝트 생성 (com.ctp.consent)
- [x] Maven 의존성 설정
  - [x] Spring Boot Starter Web
  - [x] Spring Boot Starter Data JPA
  - [x] Spring Boot Starter Security
  - [x] Spring Boot Starter Thymeleaf
  - [x] Spring Boot Starter Validation
  - [x] MariaDB Connector
  - [ ] Tailwind CSS 설정 (CDN 또는 빌드 설정)
- [x] 프로젝트 디렉토리 구조 생성
- [x] .gitignore 파일 설정

### 🔐 1.2 Spring Security 설정

- [x] SecurityConfig.java 생성
  - [x] HTTP 보안 설정 (HTTPS 강제, CSRF 보호)
  - [x] 인증 방식 설정 (Form 로그인)
  - [x] 권한별 URL 접근 제어 설정
  - [x] 로그인/로그아웃 페이지 설정
  - [x] 세션 관리 설정 (동시 로그인 제한)
- [x] PasswordEncoder 빈 등록 (BCrypt)
- [x] AuthenticationEntryPoint 커스텀 설정
- [x] AccessDeniedHandler 커스텀 설정

### 👤 1.3 사용자 인증 시스템

- [x] Admin 엔티티 생성
  - [x] 기본 필드 (id, username, password, name, email)
  - [x] 권한 필드 (role: SUPER_ADMIN, ADMIN)
  - [x] 상태 필드 (active, created_at, updated_at)
- [x] AdminRepository 인터페이스 생성
- [x] CustomUserDetails 클래스 구현 (UserDetails 구현)
- [x] CustomUserDetailsService 클래스 구현 (UserDetailsService 구현)
- [x] 기본 Super User 데이터 생성 (DataInitializer)

### 🛠️ 1.4 공통 설정 및 상수

- [x] Constants 클래스 생성
  - [x] 사용자 역할 상수 (Role.SUPER_ADMIN, Role.ADMIN)
  - [x] 동의서 상태 상수 (Status.DRAFT, SUBMITTED, APPROVED, REJECTED)
  - [x] 파일 업로드 제한 상수 (MAX_FILE_SIZE, ALLOWED_EXTENSIONS)
  - [x] 응답 메시지 상수 (SUCCESS_MESSAGE, ERROR_MESSAGE)
- [x] ApplicationProperties 클래스 생성 (@ConfigurationProperties)
  - [x] 파일 업로드 경로
  - [x] 시스템 설정값
  - [x] 외부 API 설정

### ⚙️ 1.5 Application Configuration

- [x] application.yml 설정
  - [x] 데이터베이스 연결 설정 (MariaDB CTP Database)
  - [x] JPA/Hibernate 설정
  - [x] 로깅 설정
  - [x] 파일 업로드 설정
  - [x] 서버 포트 및 컨텍스트 패스
  - [x] Thymeleaf 설정
- [x] 환경변수 기반 설정 구성
- [x] CORS 설정 (SecurityConfig에 통합)

### 🚨 1.6 공통 예외 처리

- [x] CustomException 클래스들 생성
  - [x] ConsentNotFoundException
  - [x] UnauthorizedException
  - [x] InvalidRequestException
  - [x] FileUploadException
- [x] GlobalExceptionHandler 클래스 생성 (@ControllerAdvice)
  - [x] 404 에러 처리
  - [x] 403 에러 처리 (접근 권한 없음)
  - [x] 401 에러 처리 (인증 실패)
  - [x] 500 에러 처리 (서버 내부 오류)
  - [x] Validation 에러 처리
  - [x] 파일 업로드 에러 처리
- [x] 공통 에러 페이지 템플릿 생성 (Thymeleaf + Tailwind CSS)

### 🗄️ 1.7 데이터베이스 스키마 및 기본 데이터

- [x] 새 데이터베이스(CTP) DDL 스크립트 생성
  - [x] administrators 테이블 (Admin 엔티티)
  - [x] aparts 테이블
  - [x] consent_form_templates 테이블
  - [x] consent_forms 테이블
  - [x] consent_records 테이블
  - [x] consent_persons 테이블 (구 personal_info)
  - [x] addresses 테이블 (@Embeddable로 구현)
  - [x] attachments 테이블
  - [x] signatures 테이블 (consent_persons에 통합)
  - [-] consent_history 테이블 (제거 예정)
- [-] 기존 데이터 연동 View 생성 (legacy_consent_mapping)
- [-] 기본 데이터 Insert 스크립트 생성
  - [x] 기본 관리자 계정 (Super Admin)
  - [x] 기본 동의서 템플릿 데이터
  - [x] 테스트용 아파트 데이터

### 🧪 1.8 공통 유틸리티 및 헬퍼

- [x] FileUtils 클래스 생성
  - [x] 파일 업로드 처리
  - [x] 파일 이름 생성 (UUID 기반)
  - [x] 파일 확장자 검증
- [x] DateUtils 클래스 생성
  - [x] 날짜 포맷팅
  - [x] 한국 시간대 처리
- [x] ValidationUtils 클래스 생성
  - [x] 전화번호 형식 검증
  - [x] 주민번호 형식 검증 (생년월일)
  - [x] 이메일 형식 검증
- [-] CryptoUtils 클래스 생성 (개인정보 암호화) - 제외됨
  - [-] AES 암호화/복호화
  - [-] Base64 인코딩/디코딩

### 📝 1.9 공통 DTO 및 응답 클래스

- [x] BaseDTO 클래스 생성 (공통 필드)
- [x] ApiResponse<T> 클래스 생성
  - [x] success, message, data 필드
  - [x] 성공/실패 응답 팩토리 메서드
- [x] PageResponse<T> 클래스 생성 (페이징 응답)
- [x] 공통 Validation 그룹 인터페이스 생성

### 🎨 1.10 공통 템플릿 및 레이아웃

- [x] Thymeleaf 레이아웃 설정
- [x] 기본 Layout 템플릿 생성 (Tailwind CSS)
  - [x] Header (네비게이션, 사용자 정보)
  - [x] Footer (저작권, 버전 정보)
  - [x] Sidebar (관리자용)
- [x] 공통 CSS 클래스 정의 (Tailwind 커스텀)
- [x] 공통 JavaScript 유틸리티 함수
- [x] 반응형 모바일 네비게이션 구현

---

## 📋 Phase 2: 관리자 기능 개발

### 🔑 2.1 관리자 로그인 시스템

- [x] 관리자 로그인 Controller 구현
- [x] 로그인 페이지 (Thymeleaf + Tailwind CSS)
  - [x] 반응형 로그인 폼
  - [x] 로그인 실패 메시지 표시
  - [x] "로그인 상태 유지" 체크박스
- [x] 로그아웃 처리 구현
- [x] 로그인 성공/실패 이벤트 로깅

### 📊 2.2 관리자 대시보드

- [x] DashboardController 구현
- [x] DashboardService 구현
- [x] 대시보드 페이지 템플릿 구현

### 📋 2.3 동의서 템플릿 관리 (관리자용)

- [x] ConsentTemplateController 구현 (관리자용)
- [x] ConsentTemplateService 구현
  - [x] 템플릿 CRUD 기능
  - [x] 템플릿 활성화/비활성화
  - [x] 템플릿 복사 기능
  - [x] 템플릿-아파트 연결 관리
- [x] 동의서 템플릿 관리 페이지 구현
  - [x] 템플릿 목록 조회
  - [x] 템플릿 생성/수정 폼 (WYSIWYG 에디터)
  - [x] 템플릿 미리보기 기능
  - [x] 아파트별 템플릿 할당

### 📋 2.4 제출된 동의서 관리

- [ ] AdminConsentController 구현
- [ ] AdminConsentService 구현
  - [ ] 제출된 동의서 목록 조회 (페이징, 검색, 필터)
  - [ ] 동의서 상세 조회
  - [ ] 동의서 승인/반려 처리
  - [ ] 동의서 일괄 처리
  - [ ] Excel 다운로드 기능
- [ ] 제출된 동의서 목록 페이지 구현
  - [ ] 검색 폼 (아파트, 이름, 상태별)
  - [ ] 데이터 테이블 (정렬, 페이징)
  - [ ] 일괄 선택 체크박스
- [ ] 동의서 상세 페이지 구현

### 🏠 2.5 아파트 관리

- [x] ApartController 구현
- [x] ApartService 구현
  - [x] 아파트 CRUD 기능
  - [x] 동/호수 체계 설정
  - [x] 아파트 활성화/비활성화
- [x] 아파트 관리 페이지 구현
  - [x] 아파트 목록 및 등록 폼
  - [x] 동/호수 설정 인터페이스

### 👥 2.6 사용자 관리

- [x] UserController 구현
- [x] 관리자 계정 관리 기능
- [x] 권한 설정 기능
- [x] 사용자 관리 페이지 구현

---

## 📋 Phase 3: 사용자 동의서 시스템

### 📱 3.1 공개 동의서 작성 페이지 (입주민용)

- [x] PublicConsentController 구현 (인증 불필요)
  - [x] GET /consent/{apartCode}/{templateId} - 동의서 폼 표시
  - [ ] POST /consent/submit - 동의서 제출 (permitAll) ⚠️ _데이터 적재 미구현_
  - [x] GET /consent/verify/{id} - 제출된 동의서 확인
- [x] PublicConsentService 구현
  - [x] 아파트-템플릿 연결 확인
  - [ ] 동의서 데이터 저장 ⚠️ _제출 기능 미완성_
  - [ ] 서명 이미지 처리
- [x] 모바일 최적화 동의서 작성 페이지
  - [x] 아파트 정보 자동 표시
  - [x] 동/호수 입력 폼
  - [x] 개인정보 입력 폼 (최대 4명)
  - [x] 전화번호 자동 포맷팅
  - [ ] 주소 입력 (Daum API 연동)
  - [ ] 디지털 서명 패드 (Canvas API)
  - [x] 개인정보 동의 체크박스
- [x] 실시간 유효성 검증 (JavaScript)
- [x] 제출 완료 페이지

### 🗂️ 3.2 파일 업로드 시스템

- [ ] FileUploadController 구현
- [ ] FileStorageService 구현
  - [ ] 멀티파일 업로드 처리
  - [ ] 파일 크기 및 확장자 검증
  - [ ] 파일 저장 및 경로 관리
- [ ] 파일 업로드 UI 구현
  - [ ] 드래그 앤 드롭 기능
  - [ ] 진행률 표시
  - [ ] 이미지 미리보기

### ✍️ 3.3 디지털 서명 기능

- [ ] 서명 패드 구현 (HTML5 Canvas)
- [ ] 서명 데이터 처리 (Base64)
- [ ] SignatureService 구현
- [ ] 서명 검증 및 저장

### 📄 3.4 동의서 조회 및 PDF 생성

- [ ] 동의서 조회 페이지 구현
- [ ] PDF 생성 Service 구현
- [ ] QR코드 생성 기능 구현

---

## 📋 Phase 4: API 및 통합 기능

### 🔌 4.1 REST API 개발

- [ ] API Controller 구현
  - [ ] 주소 검색 API
  - [ ] 파일 업로드 API
  - [ ] 동의서 상태 조회 API
- [ ] API 응답 표준화

### 🔄 4.2 기존 데이터 연동

- [ ] Legacy 데이터 마이그레이션 Service 구현
- [ ] View 기반 데이터 연동 테스트
- [ ] 데이터 일관성 검증

---


## 📈 진행 상황 추적

### 🎯 완료율

- **Phase 1 (기반 구조)**: 9/10 완료 (90%)
- **Phase 2 (관리자 기능)**: 5/6 완료 (83%)
- **Phase 3 (사용자 시스템)**: 1/4 완료 (25%) _일부 미완성_
- **Phase 4 (API/통합)**: 0/2 완료

**전체 진행률: 68% (15/22 완료)**

---

## ⚠️ 추후 작업 사항 (Known Issues)

### 🔴 Critical (우선순위 높음)
1. **동의서 제출 기능 미완성**
   - 현상: 사용자가 동의서 작성 후 "제출하기" 버튼 클릭 시 데이터가 DB에 저장되지 않음
   - 위치: `PublicConsentController.submitConsent()` 메소드
   - 필요 작업: ConsentRecord 엔티티 저장 로직 구현

2. **템플릿 변수 맵핑 문제**
   - 현상: 템플릿 편집기에서 `insertVariable`로 입력한 변수 ({아파트명}, {동}, {호}, {이름} 등)가 실제 동의서에서 실제 값으로 치환되지 않음
   - 위치: `src/main/resources/templates/admin/template/form.html`의 insertVariable 함수
   - 필요 작업: 템플릿 렌더링 시 변수 치환 로직 구현

### 🟡 Medium (기능 개선)
1. **디지털 서명 기능**
   - Canvas API를 이용한 서명 패드 구현 필요
   - Base64 이미지로 변환 후 저장

2. **주소 검색 API 연동**
   - Daum 우편번호 서비스 API 연동
   - 주소 자동완성 기능

### 🟢 Low (추가 기능)
1. **PDF 생성 기능**
   - 제출된 동의서를 PDF로 다운로드
   - QR코드 포함

2. **Excel 다운로드**
   - 제출된 동의서 목록 Excel 파일로 내보내기

---

## 📝 참고사항

### 🔧 개발 도구

- **IDE**: vscode
- **Database Tool**: DBeaver
- **API Testing**: Postman
- **Version Control**: Git

### 📖 주요 문서

- [PRD.md](./PRD.md) - 제품 요구사항 명세서
- [DB_ANALYSIS.md](./DB_ANALYSIS.md) - 기존 데이터베이스 분석
- [ASIS.md](./ASIS.md) - 기존 프로그램 분석
- [CLAUDE.md](./CLAUDE.md) - 개발가이드 문서 *필수참조*

### 🏷️ 업데이트 로그

- 2025-09-02: 초기 체크리스트 생성
- 2025-09-02: Phase 1.1, 1.2, 1.3, 1.4, 1.5 완료
- 2025-09-04: Phase 1.6 (예외 처리), 1.7 (데이터베이스 스키마) 완료
  - JPA 엔티티 생성 (Apart, ConsentForm, ConsentRecord 등)
  - @SoftDelete 어노테이션으로 소프트 삭제 구현
  - DataInitializer로 테스트 데이터 생성
- 2025-09-04: Phase 1.8 (공통 유틸리티), 1.9 (공통 DTO) 완료
  - FileUtils: 파일 업로드, UUID 기반 파일명 생성, 확장자 검증
  - DateUtils: 한국 시간대 처리, 다양한 날짜 포맷팅, 시간 계산
  - ValidationUtils: 전화번호, 이메일, 생년월일, 사업자번호 검증
  - BaseDTO, ApiResponse, PageResponse 클래스 생성
  - ValidationGroups 인터페이스 생성 (Create, Update, Delete 등)
- 2025-09-14: Phase 2.3 (동의서 템플릿 관리), Phase 3.1 (공개 동의서 작성) 완료
  - 동의서 템플릿 CRUD 기능 완성
  - 템플릿-아파트 연결 관리 구현
  - 공개 동의서 작성 페이지 구현
  - 동의서 조회 기능 구현
