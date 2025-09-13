# 아파트 동의서 관리 시스템 (Consent Management System)

## 프로젝트 개요

Spring Boot 3 기반의 아파트 동의서 관리 웹 애플리케이션입니다.

## 기술 스택

- **Backend**: Spring Boot 3.5.5, Java 21
- **Database**: MariaDB
- **Template Engine**: Thymeleaf
- **Security**: Spring Security 6
- **Build Tool**: Maven

## 프로젝트 구조

```
src/main/java/com/ctp/consent/
├── config/             # 설정 클래스 (Security, Web, Constants)
├── api/v1/controller   # MVC 컨트롤러
        ├─ /dto         # 데이터 전송 객체
           ├─ /req      # request dto
           ├─ /res      # response dto
           └─ /model    # JPA 엔티티
        ├─ /repository  # repository
        └─ /service     # service 비즈니스 로직
├── exception/      # 예외 처리
└── util/           # 유틸리티
```

## 개발 원칙 및 가이드라인

### 1. 심플함 최우선

- **오버엔지니어링 금지**: 필요한 기능만 구현
- **기본값 활용**: Spring Boot 기본 설정 최대한 활용
- **불필요한 추상화 금지**: 인터페이스는 꼭 필요한 경우만 생성

### 2. 설정 관리

- **단일 application.yml**: 프로필별 파일 분리 대신 환경변수 활용
- **환경변수 우선**: `${ENV_VAR:default}` 패턴 사용
- **기본값 제거**: Spring Boot 기본값과 동일한 설정은 명시하지 않음

### 3. 코드 스타일

- **Lombok 적극 활용**: 보일러플레이트 코드 최소화
- **주석 최소화**: 코드 자체가 설명이 되도록 작성
- **Constants 활용**: 매직 넘버/문자열은 Constants 클래스에 정의

### 4. 보안

- **Spring Security 기본 활용**: 커스텀 필터는 최소화
- **환경변수로 민감정보 관리**: 패스워드, 시크릿 키 등
- **역할 기반 접근 제어**: ADMIN, SUPER_ADMIN 역할 활용

## 환경 설정

### 개발 환경 실행

```bash
# 환경변수로 로그 레벨 설정
LOG_LEVEL_APP=DEBUG LOG_LEVEL_SQL=DEBUG mvn spring-boot:run

# 또는 .env 파일 사용
mvn spring-boot:run
```

### 프로덕션 환경 실행

```bash
LOG_LEVEL_ROOT=WARN LOG_LEVEL_APP=INFO java -jar target/consent-*.jar
```

## 주요 기능

1. **동의서 관리**: 생성, 조회, 수정, 삭제
2. **관리자 인증**: Spring Security 기반 로그인
3. **파일 업로드**: 동의서 첨부파일 관리
4. **Excel 처리**: Apache POI 활용
5. **PDF 생성**: iText 라이브러리 활용

## 데이터베이스 설계

- **User**: 관리자 정보
- **Consent**: 동의서 기본 정보
- **ConsentDetail**: 동의서 상세 내용
- **Address**: 주소 정보
- **FileAttachment**: 첨부파일

## API 엔드포인트

- `/admin/**`: 관리자 페이지 (인증 필요)
- `/consent/**`: 동의서 작성 페이지 (공개)
- `/api/admin/**`: 관리자 API (인증 필요)
- `/api/consent/public/**`: 공개 API

## 빌드 및 배포

```bash
# 빌드
mvn clean package

# 테스트 스킵 빌드
mvn clean package -DskipTests

# JAR 실행
java -jar target/consent-0.0.1-SNAPSHOT.jar
```

## 주의사항

1. **Actuator**: 프로덕션에서는 비활성화 (보안상 이유)
2. **DevTools**: 프로덕션에서는 비활성화
3. **CORS**: 프로덕션에서는 실제 도메인으로 제한
4. **파일 업로드**: 확장자 및 크기 제한 확인

## 향후 개선사항

- QueryDSL 도입 (복잡한 쿼리 최적화)

## 개발 규칙

1. **새 기능 추가 시**: 정말 필요한지 먼저 검토 및 사용자 승인 대기.
2. **의존성 추가 시**: Spring Boot Starter 우선 사용
3. **설정 추가 시**: 기본값으로 충분한지 확인
4. **추상화 시**: YAGNI (You Aren't Gonna Need It) 원칙 적용

## 공통 클래스 활용 지침

### 필수 활용 클래스

프로젝트에 이미 구현된 공통 클래스들을 **반드시** 활용하여 일관성 있는 코드를 작성하세요:

#### 1. DTO 클래스
- **`BaseDTO`** (`com.ctp.consent.api.v1.dto.BaseDTO`)
  - 모든 DTO는 BaseDTO를 상속받아 구현
  - id, createdAt, updatedAt, createdBy, updatedBy, active 필드 자동 포함
  - 중복 코드 제거 및 일관성 유지

#### 2. API 응답
- **`ApiResponse<T>`** (`com.ctp.consent.api.v1.dto.ApiResponse`)
  - 모든 REST API 응답은 ApiResponse 사용
  - 성공: `ApiResponse.success()`, `ApiResponse.success(data)`, `ApiResponse.success(data, message)`
  - 실패: `ApiResponse.error()`, `ApiResponse.validationError()`, `ApiResponse.notFound()`
  - 일관된 응답 형식 보장

#### 3. 페이징 응답
- **`PageResponse<T>`** (`com.ctp.consent.api.v1.dto.PageResponse`)
  - 모든 페이징 처리는 PageResponse 사용
  - Spring Data Page 객체를 PageResponse로 변환: `PageResponse.of(page)`
  - 클라이언트 친화적인 페이징 정보 제공

#### 4. 유틸리티 클래스 (`com.ctp.consent.util.*`)
- **`FileUtils`**: 파일 업로드, UUID 기반 파일명 생성, 확장자 검증
- **`DateUtils`**: 날짜 포맷팅, 한국 시간대 처리
- **`ValidationUtils`**: 전화번호, 이메일, 생년월일 검증

### 사용 예제

```java
// DTO 작성
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends BaseDTO {
    private String username;
    private String email;
    // BaseDTO의 필드들은 자동 상속
}

// Controller에서 ApiResponse 사용
@GetMapping("/{id}")
public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable Long id) {
    UserDTO user = userService.getUser(id);
    return ResponseEntity.ok(ApiResponse.success(user, "조회 성공"));
}

// 페이징 처리
@GetMapping
public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> getUsers(Pageable pageable) {
    Page<User> userPage = userService.getUsers(pageable);
    PageResponse<UserDTO> response = PageResponse.of(
        userPage.map(UserMapper::toDTO)
    );
    return ResponseEntity.ok(ApiResponse.success(response));
}
```

### 주의사항

1. **중복 생성 금지**: 이미 존재하는 공통 클래스와 동일한 기능의 클래스를 새로 만들지 마세요
2. **일관성 유지**: 프로젝트 전체에서 동일한 패턴으로 공통 클래스 활용
3. **확장 우선**: 기능 추가가 필요한 경우 새로 만들기보다 기존 클래스 확장 검토

## 프론트엔드 개발 가이드

### HTML/CSS 개발 원칙

프론트엔드 개발 시 이미 구축된 리소스와 템플릿 구조를 **반드시** 활용하세요:

#### 1. CSS 프레임워크
- **Tailwind CSS 사용**: `/static/dist/css/output.css` 활용
  - 컴파일된 Tailwind CSS 파일 사용 (이미 빌드 완료)
  - CDN 대신 로컬 빌드된 파일 우선 사용
  - 추가 CSS 작성 최소화, Tailwind 유틸리티 클래스 활용

```html
<!-- 올바른 사용 예시 -->
<link rel="stylesheet" th:href="@{/dist/css/output.css}">

<!-- CDN은 개발 시에만 임시 사용 -->
<script src="https://cdn.tailwindcss.com"></script>
```

#### 2. JavaScript 리소스
- **메인 JavaScript**: `/static/resource/js/main.js` 활용
  - Alpine.js 통합 완료
  - Chart.js 대시보드 차트 구현 완료
  - 모달, 토스트 등 공통 기능 구현됨

```html
<!-- JavaScript 포함 -->
<script th:src="@{/resource/js/main.js}" type="module"></script>
```

#### 3. Thymeleaf 레이아웃

##### 레이아웃 디렉토리 구조
```
templates/
├── fragments/      # 재사용 가능한 HTML 조각
│   ├── header.html    # 공통 헤더
│   ├── footer.html    # 공통 푸터
│   └── sidebar.html   # 사이드바 메뉴
├── layouts/        # 페이지 레이아웃 템플릿
│   └── default.html   # 기본 레이아웃
└── admin/          # 관리자 페이지
    └── ...
```

##### Fragment 사용법
```html
<!-- header fragment 포함 -->
<div th:replace="fragments/header :: header"></div>

<!-- footer fragment 포함 -->
<div th:replace="fragments/footer :: footer"></div>

<!-- 레이아웃 적용 -->
<html xmlns:th="http://www.thymeleaf.org"
      th:replace="~{layouts/default :: layout(~{::title}, ~{::content})}">
```

#### 4. 페이지 작성 규칙

새로운 HTML 페이지 작성 시:

1. **레이아웃 상속**: `layouts/default.html` 활용
2. **Fragment 재사용**: header, footer, sidebar 등 공통 요소는 fragment 사용
3. **CSS 클래스**: Tailwind CSS 유틸리티 클래스 사용
4. **JavaScript**: `ConsentApp` 전역 객체 활용
5. **아이콘**: Font Awesome 사용 (이미 포함됨)

##### 페이지 템플릿 예시
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      th:replace="~{layouts/default :: layout(~{::title}, ~{::content})}">
<head>
    <title>페이지 제목</title>
</head>
<body>
    <div th:fragment="content">
        <!-- 페이지 고유 컨텐츠 -->
        <div class="container mx-auto px-4 py-8">
            <h1 class="text-3xl font-bold text-gray-900">제목</h1>
            <!-- Tailwind 클래스 사용 -->
        </div>
    </div>
</body>
</html>
```

## 컴포넌트 기반 개발 (필수)

### 컴포넌트 라이브러리 활용

모든 화면 개발 시 `/templates/component/` 디렉토리의 컴포넌트를 **필수적으로** 사용해야 합니다.

#### 사용 가능한 컴포넌트 (`/templates/component/templates.html`)

##### 1. Card 컴포넌트
```html
<!-- Basic Card -->
<div class="bg-white rounded-lg shadow-md p-6">
    <h3 class="text-xl font-semibold mb-2">Card Title</h3>
    <p class="text-gray-600 mb-4">Card content</p>
</div>

<!-- Image Card -->
<div class="bg-white rounded-lg shadow-md overflow-hidden">
    <div class="h-48 bg-gradient-to-r from-blue-500 to-purple-600"></div>
    <div class="p-6">
        <h3 class="text-xl font-semibold mb-2">Title</h3>
    </div>
</div>

<!-- Feature Card with Icon -->
<div class="bg-white rounded-lg shadow-md p-6">
    <div class="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center mb-4">
        <i class="fas fa-check text-green-600 text-xl"></i>
    </div>
    <h3 class="text-xl font-semibold mb-2">Feature</h3>
</div>
```

##### 2. Form 컴포넌트
```html
<!-- Form with Grid Layout -->
<form class="space-y-4">
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Label</label>
            <input type="text" class="w-full px-4 py-2 border border-gray-300 rounded-lg 
                   focus:ring-2 focus:ring-blue-500 focus:border-transparent">
        </div>
    </div>
</form>
```

##### 3. Notification/Alert 컴포넌트
```html
<!-- Success Alert -->
<div class="bg-green-50 border-l-4 border-green-500 p-4">
    <div class="flex items-center">
        <i class="fas fa-check-circle text-green-500 mr-3"></i>
        <div>
            <p class="text-green-800 font-medium">Success!</p>
            <p class="text-green-700 text-sm">Message here</p>
        </div>
    </div>
</div>

<!-- Warning Alert -->
<div class="bg-yellow-50 border-l-4 border-yellow-500 p-4">
    <div class="flex items-center">
        <i class="fas fa-exclamation-triangle text-yellow-500 mr-3"></i>
        <div>
            <p class="text-yellow-800 font-medium">Warning</p>
            <p class="text-yellow-700 text-sm">Message here</p>
        </div>
    </div>
</div>
```

##### 4. Table 컴포넌트
```html
<div class="bg-white rounded-lg shadow overflow-hidden">
    <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
            <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 
                          uppercase tracking-wider">Column</th>
            </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
            <tr>
                <td class="px-6 py-4 whitespace-nowrap">Data</td>
            </tr>
        </tbody>
    </table>
</div>
```

##### 5. Modal 컴포넌트
```html
<div id="modal" class="hidden fixed inset-0 bg-gray-600 bg-opacity-50 
                       overflow-y-auto h-full w-full z-50">
    <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg 
                rounded-md bg-white">
        <div class="mt-3">
            <h3 class="text-lg leading-6 font-medium text-gray-900">Modal Title</h3>
            <div class="mt-2 px-7 py-3">
                <p class="text-sm text-gray-500">Modal content</p>
            </div>
        </div>
    </div>
</div>
```

### 컴포넌트 사용 규칙

1. **필수 사용**: 모든 새 화면은 컴포넌트 기반으로 구성
2. **일관성 유지**: 동일한 유형의 UI는 동일한 컴포넌트 사용
3. **커스터마이징**: 컴포넌트 구조는 유지하되 클래스로 스타일 조정
4. **재사용 우선**: 새 컴포넌트 생성보다 기존 컴포넌트 활용

### 페이지 구성 예시

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" 
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="~{layouts/default}">
<head>
    <title>페이지 제목</title>
</head>
<body>
    <div layout:fragment="content">
        <div class="container mx-auto px-4 py-8 max-w-6xl">
            <!-- Page Header -->
            <div class="mb-8">
                <h1 class="text-3xl font-bold text-gray-900">페이지 제목</h1>
            </div>

            <!-- Card Component -->
            <div class="bg-white rounded-lg shadow-md p-6 mb-6">
                <h3 class="text-xl font-semibold mb-4">섹션 제목</h3>
                <!-- Content -->
            </div>

            <!-- Alert Component for Messages -->
            <div th:if="${message}" class="bg-green-50 border-l-4 border-green-500 p-4">
                <!-- Alert content -->
            </div>

            <!-- Table Component -->
            <div class="bg-white rounded-lg shadow overflow-hidden">
                <table class="min-w-full divide-y divide-gray-200">
                    <!-- Table content -->
                </table>
            </div>
        </div>
    </div>

    <!-- Page-specific scripts -->
    <th:block layout:fragment="script">
        <script>
            // JavaScript using ConsentApp global
        </script>
    </th:block>
</body>
</html>
```

### JavaScript 통합

컴포넌트와 함께 사용할 JavaScript는 ConsentApp 전역 객체 활용:

```javascript
// Toast notification
ConsentApp.showToast('메시지', 'success');

// Modal control
ConsentApp.openModal('modalId');
ConsentApp.closeModal('modalId');

// Alpine.js 컴포넌트
Alpine.data('componentName', () => ({
    // Component logic
}));

### 주의사항

1. **새로운 CSS 파일 생성 금지**: output.css와 Tailwind 클래스만 사용
2. **jQuery 사용 금지**: vanilla JavaScript 또는 Alpine.js 사용
3. **인라인 스타일 최소화**: Tailwind 클래스 우선 사용
4. **Fragment 중복 생성 금지**: 기존 fragments 디렉토리 활용
5. **레이아웃 일관성**: 모든 페이지는 layouts 활용

## 트러블슈팅

- **로그 레벨 변경**: 환경변수 LOG*LEVEL*\* 설정
- **DB 연결 실패**: DB_URL, DB_USERNAME, DB_PASSWORD 확인
- **파일 업로드 실패**: app.file.upload-dir 디렉토리 권한 확인

---

_이 문서는 Claude에 의해 자동 생성되었습니다._
_최종 업데이트: 2025-09-04_
