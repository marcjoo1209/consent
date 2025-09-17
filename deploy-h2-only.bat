@echo off
chcp 65001 >nul
echo 🚀 H2 데이터베이스로 Azure 배포 (MySQL 없이)

echo 📦 환경 변수 설정 중...
az webapp config appsettings set ^
  --resource-group rg-consent-quick ^
  --name consent-app-20250917125613 ^
  --settings ^
  SPRING_DATASOURCE_URL="jdbc:h2:mem:testdb" ^
  SPRING_DATASOURCE_DRIVER_CLASS_NAME="org.h2.Driver" ^
  SPRING_DATASOURCE_USERNAME="sa" ^
  SPRING_DATASOURCE_PASSWORD="" ^
  SPRING_JPA_DATABASE_PLATFORM="org.hibernate.dialect.H2Dialect" ^
  SPRING_H2_CONSOLE_ENABLED="true" ^
  SPRING_PROFILES_ACTIVE="dev" ^
  SERVER_PORT="8080" ^
  JPA_DDL_AUTO="create"

echo 🚀 JAR 파일 배포 중...
az webapp deploy ^
  --resource-group rg-consent-quick ^
  --name consent-app-20250917125613 ^
  --src-path target/consent-0.0.1-SNAPSHOT.jar ^
  --type jar

echo ✅ 배포 완료!
echo 🌐 웹 앱 URL: https://consent-app-20250917125613.azurewebsites.net
echo 📊 관리자 페이지: https://consent-app-20250917125613.azurewebsites.net/admin
echo 💾 H2 콘솔: https://consent-app-20250917125613.azurewebsites.net/h2-console
echo ⏰ 애플리케이션 시작까지 약 2-3분 소요

pause