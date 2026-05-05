@echo off
setlocal EnableExtensions
set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..\..") do set "ROOT_DIR=%%~fI"

echo == Pasteleria Backend :: seed-presentation.bat ==
echo Cargando datos de presentacion desde SQL canonico.

powershell -NoProfile -ExecutionPolicy Bypass -File "%ROOT_DIR%\scripts\init-db.ps1"
endlocal
exit /b %ERRORLEVEL%
