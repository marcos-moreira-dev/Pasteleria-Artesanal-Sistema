@echo off
setlocal EnableExtensions
cd /d "%~dp0.."

for /f %%i in ('powershell -NoProfile -Command "Get-Date -Format yyyyMMdd_HHmmss"') do set TS=%%i
set "ROOT_DIR=%CD%"
set "LOG_DIR=%ROOT_DIR%\.diagnostics\logs"
set "LOG_FILE=%LOG_DIR%\validate-all_%TS%.log"

if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

echo == Pasteleria :: validacion total ==
echo Log: %LOG_FILE%
echo.

echo == Pasteleria :: validacion total == > "%LOG_FILE%"
echo Inicio: %DATE% %TIME% >> "%LOG_FILE%"
echo Raiz: %ROOT_DIR% >> "%LOG_FILE%"
echo. >> "%LOG_FILE%"

echo [1/6] Entorno dev
call scripts\check-dev-env.bat >> "%LOG_FILE%" 2>&1
if errorlevel 1 goto :fail

echo [2/6] Backend
call scripts\validate-backend.bat >> "%LOG_FILE%" 2>&1
if errorlevel 1 goto :fail

echo [3/6] Admin Angular
call scripts\validate-admin-angular.bat >> "%LOG_FILE%" 2>&1
if errorlevel 1 goto :fail

echo [4/6] Publico Astro
call scripts\validate-public-astro.bat >> "%LOG_FILE%" 2>&1
if errorlevel 1 goto :fail

echo [5/6] Auditoria de assets
call scripts\audit-assets.bat >> "%LOG_FILE%" 2>&1
if errorlevel 1 goto :fail

echo [6/6] Imagenes README
call scripts\validate-readme-assets.bat >> "%LOG_FILE%" 2>&1
if errorlevel 1 goto :fail

echo. >> "%LOG_FILE%"
echo OK - Validacion total completada. >> "%LOG_FILE%"
echo.
echo OK - Proyecto validado por etapas.
echo Log: %LOG_FILE%
endlocal
exit /b 0

:fail
echo.
echo ERROR - La validacion total se detuvo en la etapa indicada.
echo Revisa el log: %LOG_FILE%
echo.
echo Ultimas lineas del log:
powershell -NoProfile -Command "Get-Content -Path '%LOG_FILE%' -Tail 100"
endlocal
exit /b 1
