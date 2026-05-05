@echo off
setlocal EnableExtensions

set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..") do set "ROOT_DIR=%%~fI"

set "APP_PORT=8080"
set "DB_NAME=pasteleria"
set "DB_USER=postgres"
set "DB_USERNAME=postgres"
set "DB_PASSWORD=postgres"
set "DB_HOST=127.0.0.1"
set "DB_PORT=5436"
set "DB_URL=jdbc:postgresql://localhost:5436/pasteleria"
set "SPRING_PROFILES_ACTIVE=dev"
set "SPRING_FLYWAY_ENABLED=false"
set "JWT_SECRET=ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"
set "BACKEND_DIR=%ROOT_DIR%\backend"
set "LOG_DIR=%ROOT_DIR%\.pasteleria-dev\logs"

if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

for /f %%i in ('powershell -NoProfile -Command "Get-Date -Format yyyyMMdd_HHmmss"') do set TS=%%i
set "LOG_FILE=%LOG_DIR%\dev-backend_%TS%.log"

echo == Pasteleria :: backend dev ==
echo API: http://localhost:%APP_PORT%/api/v1
echo Health: http://localhost:%APP_PORT%/actuator/health
echo Log: %LOG_FILE%
echo.
echo Variables dev:
echo DB_URL=%DB_URL%
echo DB_USER=%DB_USER%
echo DB_NAME=%DB_NAME%
echo SPRING_FLYWAY_ENABLED=%SPRING_FLYWAY_ENABLED%
echo.

cd /d "%ROOT_DIR%"

call "%SCRIPT_DIR%up-infra-dev.bat"
if errorlevel 1 (
  echo ERROR - Infraestructura dev no lista.
  exit /b 1
)

powershell -NoProfile -ExecutionPolicy Bypass -File "%SCRIPT_DIR%init-db.ps1"
if errorlevel 1 (
  echo ERROR - Base canonica no lista.
  exit /b 1
)

powershell -NoProfile -ExecutionPolicy Bypass -File "%SCRIPT_DIR%check-backend-running.ps1" -Port %APP_PORT% >nul 2>nul
set "BACKEND_RUNNING=%ERRORLEVEL%"
if "%BACKEND_RUNNING%"=="0" (
  echo OK - Backend Pasteleria ya esta corriendo en http://localhost:%APP_PORT%
  echo No se inicia otra instancia de Maven.
  exit /b 0
)
if "%BACKEND_RUNNING%"=="2" (
  echo ERROR - El puerto %APP_PORT% esta ocupado por otro proceso que no parece Pasteleria.
  echo Libera el puerto o cambia APP_PORT.
  exit /b 1
)

cd /d "%BACKEND_DIR%"

echo Iniciando Spring Boot con perfil dev...
echo La salida de Maven se vera en esta consola y tambien se guardara en:
echo %LOG_FILE%
echo.

powershell -NoProfile -ExecutionPolicy Bypass -File "%SCRIPT_DIR%run-maven-backend-dev.ps1" ^
  -BackendDir "%BACKEND_DIR%" ^
  -LogFile "%LOG_FILE%" ^
  -JvmArguments "-DAPP_PORT=%APP_PORT% -DDB_HOST=%DB_HOST% -DDB_PORT=%DB_PORT% -DDB_NAME=%DB_NAME% -DDB_USER=%DB_USER% -DDB_PASSWORD=%DB_PASSWORD% -DSPRING_FLYWAY_ENABLED=%SPRING_FLYWAY_ENABLED% -DJWT_SECRET=%JWT_SECRET%"

if errorlevel 1 (
  echo.
  echo ERROR - Backend dev termino con error. Ultimas lineas:
  powershell -NoProfile -Command "Get-Content -Path '%LOG_FILE%' -Tail 80"
  exit /b 1
)

endlocal
exit /b 0
