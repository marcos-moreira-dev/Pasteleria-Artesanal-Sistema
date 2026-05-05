@echo off
setlocal EnableExtensions
cd /d "%~dp0.."

for /f %%i in ('powershell -NoProfile -Command "Get-Date -Format yyyyMMdd_HHmmss"') do set TS=%%i
set "ROOT_DIR=%CD%"
set "LOG_DIR=%ROOT_DIR%\.diagnostics\logs"
set "LOG_FILE=%LOG_DIR%\validate-backend_%TS%.log"

if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

echo == Pasteleria :: validacion backend ==
echo Log: %LOG_FILE%
echo.

echo == Pasteleria :: validacion backend == > "%LOG_FILE%"
echo Inicio: %DATE% %TIME% >> "%LOG_FILE%"
echo Raiz: %ROOT_DIR% >> "%LOG_FILE%"
echo. >> "%LOG_FILE%"

echo [1/3] Chequeo de entorno dev
call scripts\check-dev-env.bat >> "%LOG_FILE%" 2>&1
if errorlevel 1 goto :fail

echo [2/3] Reset de base canonica local
powershell -NoProfile -ExecutionPolicy Bypass -File scripts\reset-db-local.ps1 >> "%LOG_FILE%" 2>&1
if errorlevel 1 goto :fail

echo [3/3] Compilacion backend sin ejecutar tests
cd /d "%ROOT_DIR%\backend"
set "DB_HOST=localhost"
set "DB_PORT=5436"
set "DB_USER=postgres"
set "DB_PASSWORD=postgres"
set "DB_NAME=pasteleria"
set "SPRING_FLYWAY_ENABLED=false"
set "JWT_SECRET=ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"
where mvn >nul 2>nul
if errorlevel 1 (
  echo [INFO] Maven global no disponible; usando backend\mvnw.cmd >> "%LOG_FILE%"
  if not exist "%ROOT_DIR%\backend\mvnw.cmd" (
    echo [ERROR] No existe backend\mvnw.cmd y Maven global no esta disponible. >> "%LOG_FILE%"
    goto :fail
  )
  call "%ROOT_DIR%\backend\mvnw.cmd" -DskipTests compile >> "%LOG_FILE%" 2>&1
) else (
  echo [INFO] Maven global disponible; usando toolchain JDK 21 configurado por el proyecto >> "%LOG_FILE%"
  call mvn -DskipTests compile >> "%LOG_FILE%" 2>&1
)
if errorlevel 1 goto :fail

cd /d "%ROOT_DIR%"
echo. >> "%LOG_FILE%"
echo OK - Backend validado. >> "%LOG_FILE%"
echo.
echo OK - Backend validado por etapas.
echo Log: %LOG_FILE%
endlocal
exit /b 0

:fail
cd /d "%ROOT_DIR%"
echo.
echo ERROR - La validacion backend se detuvo.
echo Revisa el log: %LOG_FILE%
echo.
echo Ultimas lineas del log:
powershell -NoProfile -Command "Get-Content -Path '%LOG_FILE%' -Tail 80"
endlocal
exit /b 1
