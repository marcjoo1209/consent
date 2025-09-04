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
- JWT 기반 인증 (현재는 세션 기반)
- Redis 캐싱 (성능 개선 필요시)

## 개발 규칙

1. **새 기능 추가 시**: 정말 필요한지 먼저 검토
2. **의존성 추가 시**: Spring Boot Starter 우선 사용
3. **설정 추가 시**: 기본값으로 충분한지 확인
4. **추상화 시**: YAGNI (You Aren't Gonna Need It) 원칙 적용

## 트러블슈팅

- **로그 레벨 변경**: 환경변수 LOG*LEVEL*\* 설정
- **DB 연결 실패**: DB_URL, DB_USERNAME, DB_PASSWORD 확인
- **파일 업로드 실패**: app.file.upload-dir 디렉토리 권한 확인

---

_이 문서는 Claude에 의해 자동 생성되었습니다._
_최종 업데이트: 2025-09-02_
