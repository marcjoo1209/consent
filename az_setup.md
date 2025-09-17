
● Azure에 현재 프로젝트를 즉시 실행하는 가장 간단한 방법을 알려드리겠습니다.

● 🚀 Azure에 즉시 배포하는 방법 (5분 완성)

  1️⃣ Azure 계정 및 CLI 준비 (1분)

  # Azure CLI 설치 (Windows)
  winget install Microsoft.AzureCLI

  # 로그인
  az login───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
  ⏵⏵ accept edits on (shift+tab to cycle)
  # 구독 확인
  az account show

  2️⃣ 리소스 그룹 생성 (30초)

  # 리소스 그룹 생성
  az group create --name rg-consent-quick --location koreacentral

  3️⃣ 데이터베이스 생성 (2분)

  # MariaDB 서버 생성 (가장 저렴한 옵션)
  az mariadb server create --resource-group rg-consent-quick --name consent-db-quick --location koreacentral --admin-user consent_admin --admin-password "wndudwna4" --sku-name B_Gen5_1 --storage-size 5120 --version 10.3

  # 방화벽 규칙 (Azure 서비스 허용)
  az mariadb server firewall-rule create --resource-group rg-consent-quick --server consent-db-quick --name AllowAzureServices --start-ip-address 0.0.0.0 --end-ip-address 0.0.0.0

  # 데이터베이스 생성
  az mariadb db create --resource-group rg-consent-quick --server-name consent-db-quick --name consent

  4️⃣ 웹 앱 생성 및 배포 (2분)

  # App Service Plan 생성
  az appservice plan create --name asp-consent-quick --resource-group rg-consent-quick --sku B1 --is-linux

  # Web App 생성
  az webapp create --resource-group rg-consent-quick --plan asp-consent-quick --name consent-app-quick-$(date +%s) --runtime "JAVA:21-java21"

  # 환경 변수 설정
  WEBAPP_NAME=$(az webapp list --resource-group rg-consent-quick --query '[0].name' -o tsv)

  az webapp config appsettings set --resource-group rg-consent-quick --name $WEBAPP_NAME --settings DB_URL="jdbc:mariadb://consent-db-quick.mariadb.database.azure.com:3306/consent?useSSL=true&serverTimezone=Asia/Seoul" DB_USERNAME="consent_admin" DB_PASSWORD="ConsentPass123!" SPRING_PROFILES_ACTIVE="prod" SERVER_PORT="8080"

  5️⃣ 코드 빌드 및 배포 (1분)

● # 현재 프로젝트에서 빌드
  mvn clean package -DskipTests

  # WAR/JAR 파일 배포
  az webapp deploy \
    --resource-group rg-consent-quick \
    --name $WEBAPP_NAME \
    --src-path target/*.jar \
    --type jar

  📋 전체 스크립트 (원클릭 배포)

● Write(deploy-azure-quick.sh)
  ⎿  Wrote 1 lines to deploy-azure-quick.sh
     #!/bin/bash
     # Azure 즉시 배포 스크립트
     echo "🚀 Azure 즉시 배포를 시작합니다..."
     # 변수 설정
     RESOURCE_GROUP="rg-consent-quick"
     LOCATION="koreacentral"
     DB_SERVER="consent-db-quick"
     DB_ADMIN="consent_admin"

● Write(deploy-azure-quick.bat)
  ⎿  Wrote 1 lines to deploy-azure-quick.bat
     @echo off
     REM Azure 즉시 배포 스크립트 (Windows)
     echo 🚀 Azure 즉시 배포를 시작합니다...
     REM 변수 설정
     set RESOURCE_GROUP=rg-consent-quick
     set LOCATION=koreacentral
     set DB_SERVER=consent-db-quick
     set DB_ADMIN=consent_admin
     set DB_PASSWORD=ConsentPass123!

● 🚀 즉시 실행 방법 (3가지 옵션)

  🥇 옵션 1: 원클릭 배포 (추천)

  # Windows에서
  deploy-azure-quick.bat

  # Linux/Mac에서
  chmod +x deploy-azure-quick.sh
  ./deploy-azure-quick.sh

  🥈 옵션 2: 수동 단계별 실행

  # 1. Azure 로그인
  az login

  # 2. 리소스 그룹 생성
  az group create --name rg-consent-quick --location koreacentral

  # 3. DB 생성 (2분 소요)
  az mariadb server create \
    --resource-group rg-consent-quick \
    --name consent-db-quick \
    --admin-user consent_admin \
    --admin-password "ConsentPass123!" \
    --sku-name B_Gen5_1 \
    --location koreacentral

  # 4. 방화벽 설정
  az mariadb server firewall-rule create \
    --resource-group rg-consent-quick \
    --server consent-db-quick \
    --name AllowAzureServices \
    --start-ip-address 0.0.0.0 \
    --end-ip-address 0.0.0.0

  # 5. 웹 앱 생성
  az webapp create \
    --resource-group rg-consent-quick \
    --plan asp-consent-quick \
    --name consent-app-$(date +%s) \
    --runtime "JAVA:21-java21"

  # 6. 환경 변수 설정
  az webapp config appsettings set \
    --resource-group rg-consent-quick \
    --name [YOUR_WEBAPP_NAME] \
    --settings \
    DB_URL="jdbc:mariadb://consent-db-quick.mariadb.database.azure.com:3306/consent?useSSL=true" \
    DB_USERNAME="consent_admin" \
    DB_PASSWORD="ConsentPass123!"

  # 7. 빌드 및 배포
  mvn clean package -DskipTests
  az webapp deploy \
    --resource-group rg-consent-quick \
    --name [YOUR_WEBAPP_NAME] \
    --src-path target/*.jar \
    --type jar

  🥉 옵션 3: Azure Portal 사용

  1. Azure Portal (https://portal.azure.com) 접속
  2. App Service 생성 → Java 21 선택
  3. Database for MariaDB 생성
  4. Configuration에서 환경 변수 설정
  5. Deployment Center에서 JAR 파일 업로드

  ⚡ 배포 후 확인 방법

● 1. 애플리케이션 상태 확인

  # 로그 실시간 확인
  az webapp log tail --name [YOUR_WEBAPP_NAME] --resource-group rg-consent-quick

  # 앱 상태 확인
  az webapp show --name [YOUR_WEBAPP_NAME] --resource-group rg-consent-quick --query "state"

  2. 웹 브라우저 테스트

  ✅ 메인 페이지: https://[YOUR_WEBAPP_NAME].azurewebsites.net
  ✅ 관리자 페이지: https://[YOUR_WEBAPP_NAME].azurewebsites.net/admin
  ✅ 헬스체크: https://[YOUR_WEBAPP_NAME].azurewebsites.net/actuator/health

  3. 데이터베이스 연결 확인

  # MariaDB 클라이언트로 연결 테스트
  mysql -h consent-db-quick.mariadb.database.azure.com \
        -u consent_admin \
        -p \
        consent

  🔧 문제 해결

  가장 흔한 문제들

  1. 빌드 실패

  # Java 21 확인
  java -version

  # Maven 빌드 재시도
  mvn clean install -DskipTests

  2. 데이터베이스 연결 실패

  # 방화벽 규칙 재설정
  az mariadb server firewall-rule create \
    --resource-group rg-consent-quick \
    --server consent-db-quick \
    --name AllowAll \
    --start-ip-address 0.0.0.0 \
    --end-ip-address 255.255.255.255

  3. 앱 시작 실패

  # 환경 변수 재확인
  az webapp config appsettings list \
    --name [YOUR_WEBAPP_NAME] \
    --resource-group rg-consent-quick

  💰 예상 비용

  | 서비스         | 스펙       | 월 비용      |
  |-------------|----------|-----------|
  | App Service | B1 Basic | ₩22,000   |
  | MariaDB     | B_Gen5_1 | ₩35,000   |
  | 합계          |          | ₩57,000/월 |

  🎯 성공 확인 체크리스트

  - Azure CLI 로그인 성공
  - 리소스 그룹 생성 완료
  - MariaDB 서버 생성 완료 (2-3분 소요)
  - 웹 앱 생성 완료
  - 환경 변수 설정 완료
  - JAR 파일 빌드 성공
  - 배포 완료
  - 웹 사이트 접속 확인
  - 관리자 페이지 접속 확인

  총 소요 시간: 약 5-7분

  지금 당장 deploy-azure-quick.bat (Windows) 또는 deploy-azure-quick.sh (Linux/Mac) 파일을 실행하시면 자동으로 모든 과정이 진행됩니다!