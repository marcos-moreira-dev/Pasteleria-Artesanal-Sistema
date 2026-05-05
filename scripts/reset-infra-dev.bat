@echo off
setlocal EnableExtensions

set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..") do set "ROOT_DIR=%%~fI"

set "COMPOSE_FILE=%ROOT_DIR%\infra\compose\docker-compose.dev.yml"
set "COMPOSE_PROJECT_NAME=pasteleria_dev"

echo == Pasteleria :: reset infraestructura dev ==
echo Esto elimina contenedores, red y volumen local de PostgreSQL dev.
echo No usar contra datos reales.
echo.

cd /d "%ROOT_DIR%"

docker compose -p "%COMPOSE_PROJECT_NAME%" -f "%COMPOSE_FILE%" down -v --remove-orphans

echo Limpiando contenedores legacy si existen...
for %%C in (pasteleria-postgres pasteleria-postgres-dev) do (
  docker ps -a --format "{{.Names}}" | findstr /x "%%C" >nul 2>nul
  if not errorlevel 1 docker rm -f "%%C" >nul 2>nul
)

echo OK - Infraestructura dev reseteada.
echo Ahora ejecuta: scripts\up-infra-dev.bat
endlocal
exit /b 0
