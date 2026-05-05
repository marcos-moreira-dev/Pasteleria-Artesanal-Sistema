@echo off
setlocal EnableExtensions

set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..") do set "ROOT_DIR=%%~fI"

set "COMPOSE_FILE=%ROOT_DIR%\infra\compose\docker-compose.dev.yml"
set "COMPOSE_PROJECT_NAME=pasteleria_dev"

echo == Pasteleria :: detener infraestructura dev ==
echo Proyecto Docker Compose: %COMPOSE_PROJECT_NAME%
echo.
echo Nota: este script detiene PostgreSQL/Docker, no el backend Java.
echo Para detener el backend: scripts\stop-backend-dev.bat
echo.

cd /d "%ROOT_DIR%"

docker compose -p "%COMPOSE_PROJECT_NAME%" -f "%COMPOSE_FILE%" down --remove-orphans

echo OK - Infraestructura dev detenida o no presente.
endlocal
exit /b 0
