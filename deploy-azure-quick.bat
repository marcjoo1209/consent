@echo off
chcp 65001 >nul
REM Azure Quick Deploy Script (Windows)
echo [32m🚀 Azure Quick Deploy Starting...[0m

REM 변수 설정
set RESOURCE_GROUP=rg-consent-quick
set LOCATION=koreacentral
set DB_SERVER=consent-db-quick
set DB_ADMIN=cila10
set DB_PASSWORD=wndudwna4
set APP_PLAN=asp-consent-quick

REM 타임스탬프 생성 (간단한 방법)
for /f "tokens=2 delims==" %%a in ('wmic OS Get localdatetime /value') do set "dt=%%a"
set TIMESTAMP=%dt:~0,14%
set WEBAPP_NAME=consent-app-%TIMESTAMP%

echo [33m📦 1/6 Creating Resource Group...[0m
call az group create --name %RESOURCE_GROUP% --location %LOCATION%

echo [34m🗄️ 2/6 Creating MySQL Flexible Server... (about 2 minutes)[0m
call az mysql flexible-server create ^
  --resource-group %RESOURCE_GROUP% ^
  --name %DB_SERVER% ^
  --location %LOCATION% ^
  --admin-user %DB_ADMIN% ^
  --admin-password %DB_PASSWORD% ^
  --sku-name Standard_B1ms ^
  --tier Burstable ^
  --storage-size 20 ^
  --version 8.0.21

echo [35m🔥 3/6 Setting Firewall Rules...[0m
call az mysql flexible-server firewall-rule create ^
  --resource-group %RESOURCE_GROUP% ^
  --name %DB_SERVER% ^
  --rule-name AllowAzureServices ^
  --start-ip-address 0.0.0.0 ^
  --end-ip-address 0.0.0.0

echo [36m📊 4/6 Creating Database...[0m
call az mysql flexible-server db create ^
  --resource-group %RESOURCE_GROUP% ^
  --server-name %DB_SERVER% ^
  --database-name consent

echo [32m🌐 5/6 Creating Web App...[0m
call az appservice plan create ^
  --name %APP_PLAN% ^
  --resource-group %RESOURCE_GROUP% ^
  --sku B1 ^
  --is-linux

call az webapp create ^
  --resource-group %RESOURCE_GROUP% ^
  --plan %APP_PLAN% ^
  --name %WEBAPP_NAME% ^
  --runtime "JAVA:21-java21"

echo [33m⚙️ Setting Environment Variables...[0m
call az webapp config appsettings set ^
  --resource-group %RESOURCE_GROUP% ^
  --name %WEBAPP_NAME% ^
  --settings ^
  DB_URL="jdbc:mysql://%DB_SERVER%.mysql.database.azure.com:3306/consent?useSSL=true&serverTimezone=Asia/Seoul" ^
  DB_USERNAME=%DB_ADMIN% ^
  DB_PASSWORD=%DB_PASSWORD% ^
  SPRING_PROFILES_ACTIVE=prod ^
  SERVER_PORT=8080 ^
  JPA_DDL_AUTO=update

echo [34m🔨 6/6 Building and Deploying Application...[0m
call mvnd clean package -DskipTests

call az webapp deploy ^
  --resource-group %RESOURCE_GROUP% ^
  --name %WEBAPP_NAME% ^
  --src-path target\*.jar ^
  --type jar

REM Deployment Complete
echo.
echo [32m✅ Deployment Completed Successfully![0m
echo [36m🌐 Web App URL: https://%WEBAPP_NAME%.azurewebsites.net[0m
echo [33m📊 Admin Page: https://%WEBAPP_NAME%.azurewebsites.net/admin[0m
echo [35m🗄️ Database: %DB_SERVER%.mysql.database.azure.com[0m
echo.
echo [33m⏰ Application startup takes about 2-3 minutes.[0m
echo [36m📋 Check logs: az webapp log tail --name %WEBAPP_NAME% --resource-group %RESOURCE_GROUP%[0m
echo.
echo [32m💰 Monthly Cost: About 35,000 KRW (B1 App Service + B1ms MySQL)[0m

pause