# Azure 배포 현황 및 진행 상태

## 📅 최종 업데이트: 2025-09-17

## 🎯 배포 목표
- Spring Boot 애플리케이션을 Azure App Service에 배포
- **H2 메모리 데이터베이스 사용** (MySQL 대신)
- 개발/테스트 환경 구축

## ✅ 완료된 작업

### 1. JAR 파일 빌드 완료
- **파일**: `target/consent-0.0.1-SNAPSHOT.jar` (101MB)
- **빌드 명령**: `./mvnd clean package -DskipTests`
- **상태**: ✅ 성공

### 2. Azure 리소스 생성 완료
- **리소스 그룹**: `rg-consent-quick`
- **App Service Plan**: `asp-consent-quick`
- **Web App**: `consent-app-20250917125613`
- **상태**: ✅ 생성됨

## ❌ 현재 문제점

### 1. Azure CLI 미설치
- 현재 개발 환경에 Azure CLI가 설치되어 있지 않음
- 모든 `az` 명령어가 실행되지 않음

### 2. 컨테이너 런타임 문제
- Java 21 설정했지만 PHP 컨테이너가 실행되는 문제 발생
- `linuxFxVersion: "JAVA:21-java21"` 설정이 적용되지 않음

## 🔧 해결 방법

### 옵션 1: Azure CLI 설치 후 배포
```bash
# 1. Azure CLI 설치
winget install Microsoft.AzureCLI
# 또는 https://aka.ms/installazurecliwindows 에서 다운로드

# 2. Azure 로그인
az login

# 3. H2 배포 스크립트 실행
./deploy-h2-only.bat
```

### 옵션 2: Azure Portal에서 수동 배포
1. Azure Portal (https://portal.azure.com) 접속
2. App Service `consent-app-20250917125613` 선택
3. Deployment Center에서 JAR 파일 업로드
4. Configuration에서 환경 변수 설정:
   - `SPRING_DATASOURCE_URL`: `jdbc:h2:mem:testdb`
   - `SPRING_DATASOURCE_DRIVER_CLASS_NAME`: `org.h2.Driver`
   - `SPRING_DATASOURCE_USERNAME`: `sa`
   - `SPRING_DATASOURCE_PASSWORD`: (빈 값)
   - `SPRING_JPA_DATABASE_PLATFORM`: `org.hibernate.dialect.H2Dialect`
   - `SPRING_H2_CONSOLE_ENABLED`: `true`
   - `SPRING_PROFILES_ACTIVE`: `dev`
   - `SERVER_PORT`: `8080`
   - `JPA_DDL_AUTO`: `create`

## 📝 생성된 배포 스크립트

### 1. `deploy-h2-only.bat`
- H2 메모리 DB로 배포하는 스크립트
- MySQL 불필요
- 환경 변수 자동 설정

### 2. `fix-deployment.bat`
- MySQL 서버 생성 및 배포 (현재 사용 안 함)
- H2로 변경하기로 결정

## 🌐 접속 URL (배포 완료 시)

- **메인 애플리케이션**: https://consent-app-20250917125613.azurewebsites.net
- **관리자 페이지**: https://consent-app-20250917125613.azurewebsites.net/admin
- **H2 콘솔**: https://consent-app-20250917125613.azurewebsites.net/h2-console

## ⚠️ 중요 사항

1. **H2 데이터베이스 사용 결정됨**
   - MySQL 프로바이더 등록 문제로 H2 사용
   - 메모리 DB이므로 재시작 시 데이터 초기화됨
   - 개발/테스트 용도로 적합

2. **Azure CLI 필수**
   - 현재 환경에 미설치 상태
   - 배포 진행을 위해 설치 필요

3. **JAR 파일 준비 완료**
   - 빌드 성공, 배포 준비 완료
   - `target/consent-0.0.1-SNAPSHOT.jar`

## 🚀 다음 단계

1. Azure CLI 설치
2. `./deploy-h2-only.bat` 실행
3. 2-3분 대기 후 애플리케이션 접속 확인

---
*이 문서는 배포 진행 상황을 추적하기 위해 작성되었습니다.*