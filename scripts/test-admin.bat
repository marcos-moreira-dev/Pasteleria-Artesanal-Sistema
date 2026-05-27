@echo off
setlocal EnableExtensions
set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..") do set "ROOT_DIR=%%~fI"
set "FRONTEND_DIR=%ROOT_DIR%\frontend-admin-angular"
set "LOG_DIR=%ROOT_DIR%\.diagnostics\logs"
if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"
for /f %%I in ('powershell -NoProfile -Command "Get-Date -Format yyyyMMdd_HHmmss"') do set "TS=%%I"
set "LOG=%LOG_DIR%\validate-admin-angular_%TS%.log"

call :Run > "%LOG%" 2>&1
set "EXIT_CODE=%ERRORLEVEL%"
if not "%EXIT_CODE%"=="0" (
  echo ERROR - Fallo la validacion del admin Angular.
  echo Revisa: %LOG%
  type "%LOG%"
) else (
  echo OK - Admin Angular compila.
  echo Log: %LOG%
)
endlocal & exit /b %EXIT_CODE%

:Run
echo [1/3] Versiones
node -v
call npm -v
echo.
echo [2/3] Dependencias
cd /d "%FRONTEND_DIR%" || exit /b 1
call npm install || exit /b 1
echo.
echo [3/3] Build Angular
call npm run build || exit /b 1
exit /b 0
