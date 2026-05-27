@echo off
setlocal EnableExtensions
set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..\..") do set "ROOT_DIR=%%~fI"

echo == Pasteleria ERP :: reset presentation data ==
powershell -NoProfile -ExecutionPolicy Bypass -File "%ROOT_DIR%\tools\powershell\init-db.ps1" -ForceReset -IncludePresentation
set "EXIT_CODE=%ERRORLEVEL%"
endlocal & exit /b %EXIT_CODE%
