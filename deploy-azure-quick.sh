#!/bin/bash

# Azure 즉시 배포 스크립트
echo "🚀 Azure 즉시 배포를 시작합니다..."

# 변수 설정
RESOURCE_GROUP="rg-consent-quick"
LOCATION="koreacentral"
DB_SERVER="consent-db-quick"
DB_ADMIN="consent_admin"
DB_PASSWORD="ConsentPass123!"
APP_PLAN="asp-consent-quick"
TIMESTAMP=$(date +%s)
WEBAPP_NAME="consent-app-$TIMESTAMP"

echo "📦 1/6 리소스 그룹 생성 중..."
az group create \
  --name $RESOURCE_GROUP \
  --location $LOCATION

echo "🗄️ 2/6 MariaDB 서버 생성 중... (약 2분 소요)"
az mariadb server create \
  --resource-group $RESOURCE_GROUP \
  --name $DB_SERVER \
  --location $LOCATION \
  --admin-user $DB_ADMIN \
  --admin-password $DB_PASSWORD \
  --sku-name B_Gen5_1 \
  --storage-size 5120 \
  --version 10.3

echo "🔥 3/6 방화벽 규칙 설정 중..."
az mariadb server firewall-rule create \
  --resource-group $RESOURCE_GROUP \
  --server $DB_SERVER \
  --name AllowAzureServices \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0

echo "📊 4/6 데이터베이스 생성 중..."
az mariadb db create \
  --resource-group $RESOURCE_GROUP \
  --server-name $DB_SERVER \
  --name consent

echo "🌐 5/6 웹 앱 생성 중..."
az appservice plan create \
  --name $APP_PLAN \
  --resource-group $RESOURCE_GROUP \
  --sku B1 \
  --is-linux

az webapp create \
  --resource-group $RESOURCE_GROUP \
  --plan $APP_PLAN \
  --name $WEBAPP_NAME \
  --runtime "JAVA:21-java21"

echo "⚙️ 환경 변수 설정 중..."
az webapp config appsettings set \
  --resource-group $RESOURCE_GROUP \
  --name $WEBAPP_NAME \
  --settings \
  DB_URL="jdbc:mariadb://${DB_SERVER}.mariadb.database.azure.com:3306/consent?useSSL=true&serverTimezone=Asia/Seoul" \
  DB_USERNAME=$DB_ADMIN \
  DB_PASSWORD=$DB_PASSWORD \
  SPRING_PROFILES_ACTIVE="prod" \
  SERVER_PORT="8080" \
  JPA_DDL_AUTO="update"

echo "🔨 6/6 애플리케이션 빌드 및 배포 중..."
mvn clean package -DskipTests

az webapp deploy \
  --resource-group $RESOURCE_GROUP \
  --name $WEBAPP_NAME \
  --src-path target/*.jar \
  --type jar

# 배포 완료 정보 출력
echo ""
echo "✅ 배포가 완료되었습니다!"
echo "🌐 웹 앱 URL: https://${WEBAPP_NAME}.azurewebsites.net"
echo "📊 관리 페이지: https://${WEBAPP_NAME}.azurewebsites.net/admin"
echo "🗄️ 데이터베이스: ${DB_SERVER}.mariadb.database.azure.com"
echo ""
echo "⏰ 애플리케이션 시작까지 약 2-3분 정도 소요됩니다."
echo "📋 로그 확인: az webapp log tail --name $WEBAPP_NAME --resource-group $RESOURCE_GROUP"
echo ""
echo "💰 월 예상 비용: 약 ₩25,000 (B1 App Service + B1 MariaDB)"