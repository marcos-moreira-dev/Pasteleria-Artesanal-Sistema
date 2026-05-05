@echo off
setlocal EnableExtensions
set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..\..") do set "ROOT_DIR=%%~fI"

echo == Pasteleria Backend :: reset-presentation-data.bat ==
echo Recreando base local de presentacion con SQL canonico.

powershell -NoProfile -ExecutionPolicy Bypass -File "%ROOT_DIR%\scripts\reset-db-local.ps1"
endlocal
exit /b %ERRORLEVEL%
