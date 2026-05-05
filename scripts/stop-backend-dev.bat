@echo off
setlocal EnableExtensions

set "SCRIPT_DIR=%~dp0"
set "APP_PORT=8080"

echo == Pasteleria :: detener backend dev ==
powershell -NoProfile -ExecutionPolicy Bypass -File "%SCRIPT_DIR%stop-backend-dev.ps1" -Port %APP_PORT%
set "EXIT_CODE=%ERRORLEVEL%"

endlocal
exit /b %EXIT_CODE%
