@echo off
setlocal EnableExtensions

set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..\..") do set "ROOT_DIR=%%~fI"

set "APP_PORT=8080"
set "DB_HOST=127.0.0.1"
set "DB_PORT=5436"
set "DB_NAME=pasteleria"
set "DB_USER=postgres"
set "DB_PASSWORD=postgres"
set "SPRING_PROFILES_ACTIVE=dev"
set "SPRING_FLYWAY_ENABLED=false"
set "JWT_SECRET=ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"

echo == Pasteleria Backend :: presentation ==
echo Perfil: dev
echo API: http://localhost:%APP_PORT%/api/v1
echo.

call "%ROOT_DIR%\scripts\up-infra-dev.bat"
if errorlevel 1 exit /b 1

powershell -NoProfile -ExecutionPolicy Bypass -File "%ROOT_DIR%\scripts\init-db.ps1"
if errorlevel 1 exit /b 1

cd /d "%ROOT_DIR%\backend"
call .\mvnw.cmd spring-boot:run -Dmaven.test.skip=true
endlocal
