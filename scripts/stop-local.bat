@echo off
setlocal EnableExtensions
set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..") do set "ROOT_DIR=%%~fI"
set "COMPOSE_FILE=%ROOT_DIR%\infra\compose\docker-compose.dev.yml"

echo == Pasteleria ERP :: detener infraestructura local ==
docker compose -p pasteleria_dev -f "%COMPOSE_FILE%" down
endlocal & exit /b %ERRORLEVEL%
