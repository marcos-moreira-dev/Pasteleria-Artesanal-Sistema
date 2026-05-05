@echo off
setlocal EnableExtensions

set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..") do set "ROOT_DIR=%%~fI"

set "COMPOSE_FILE=%ROOT_DIR%\infra\compose\docker-compose.dev.yml"
set "COMPOSE_PROJECT_NAME=pasteleria_dev"
set "CONTAINER_NAME=pasteleria-postgres-dev"

set "DB_NAME=pasteleria"
set "DB_USER=postgres"
set "DB_PASSWORD=postgres"
set "DB_HOST=127.0.0.1"
set "DB_PORT=5436"
set "DB_URL=jdbc:postgresql://localhost:5436/pasteleria"

echo == Pasteleria :: infraestructura dev ==
echo Proyecto Docker Compose: %COMPOSE_PROJECT_NAME%
echo Compose: infra\compose\docker-compose.dev.yml
echo PostgreSQL: localhost:%DB_PORT%/%DB_NAME%
echo.

cd /d "%ROOT_DIR%"

echo Limpiando contenedores legacy si existen...
for %%C in (pasteleria-postgres optica-postgres cedro-damasco-postgres-dev) do (
  docker ps -a --format "{{.Names}}" | findstr /x "%%C" >nul 2>nul
  if not errorlevel 1 docker rm -f "%%C" >nul 2>nul
)

docker compose -p "%COMPOSE_PROJECT_NAME%" -f "%COMPOSE_FILE%" up -d
if errorlevel 1 (
  echo ERROR - No se pudo levantar PostgreSQL dev.
  exit /b 1
)

echo.
echo Esperando PostgreSQL dev...
set "READY="
for /L %%i in (1,1,40) do (
  docker exec "%CONTAINER_NAME%" sh -lc "PGPASSWORD=%DB_PASSWORD% pg_isready -h localhost -U %DB_USER% -d postgres" >nul 2>nul
  if not errorlevel 1 (
    set "READY=1"
    goto :ready
  )
  timeout /t 2 /nobreak >nul
)

:ready
if not defined READY (
  echo ERROR - PostgreSQL no respondio en el tiempo esperado.
  docker logs "%CONTAINER_NAME%" --tail 80
  exit /b 1
)

echo OK - PostgreSQL dev disponible en localhost:%DB_PORT%.
endlocal
exit /b 0
