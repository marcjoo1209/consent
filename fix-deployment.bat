@echo off
chcp 65001 >nul
echo [33m🔧 Fixing deployment issues...[0m

REM MySQL Server creation with correct version
echo [34m📊 Creating MySQL Server with correct version...[0m
az mysql flexible-server create ^
  --resource-group rg-consent-quick ^
  --name consent-mysql-quick2 ^
  --location koreacentral ^
  --admin-user cila10 ^
  --admin-password "wndudwna4" ^
  --sku-name Standard_B1ms ^
  --tier Burstable ^
  --storage-size 20 ^
  --version 8.0.21

echo [35m🔥 Setting firewall rules...[0m
az mysql flexible-server firewall-rule create ^
  --resource-group rg-consent-quick ^
  --name consent-mysql-quick2 ^
  --rule-name AllowAzureServices ^
  --start-ip-address 0.0.0.0 ^
  --end-ip-address 0.0.0.0

echo [36m📊 Creating database...[0m
az mysql flexible-server db create ^
  --resource-group rg-consent-quick ^
  --server-name consent-mysql-quick2 ^
  --database-name consent

echo [33m⚙️ Updating environment variables...[0m
az webapp config appsettings set ^
  --resource-group rg-consent-quick ^
  --name consent-app-20250917125613 ^
  --settings ^
  DB_URL="jdbc:mysql://consent-mysql-quick2.mysql.database.azure.com:3306/consent?useSSL=true&serverTimezone=Asia/Seoul" ^
  DB_USERNAME="cila10" ^
  DB_PASSWORD="wndudwna4" ^
  SPRING_PROFILES_ACTIVE="prod" ^
  SERVER_PORT="8080" ^
  JPA_DDL_AUTO="update"

echo [34m🚀 Deploying JAR file...[0m
az webapp deploy ^
  --resource-group rg-consent-quick ^
  --name consent-app-20250917125613 ^
  --src-path target/consent-0.0.1-SNAPSHOT.jar ^
  --type jar

echo [32m✅ Deployment completed![0m
echo [36m🌐 Web App URL: https://consent-app-20250917125613.azurewebsites.net[0m
echo [33m📊 Admin Page: https://consent-app-20250917125613.azurewebsites.net/admin[0m
echo [35m⏰ Application startup takes about 2-3 minutes.[0m

pause