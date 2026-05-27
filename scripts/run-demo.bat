@echo off
setlocal EnableExtensions
set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..") do set "ROOT_DIR=%%~fI"

echo == Pasteleria ERP :: ejecutar modo demo / presentacion ==
echo ATENCION: este modo recrea la base local y carga registros inventados.
echo.
powershell -NoProfile -ExecutionPolicy Bypass -File "%ROOT_DIR%\tools\powershell\start-local-stack.ps1" -Mode demo %*
set "EXIT_CODE=%ERRORLEVEL%"
endlocal & exit /b %EXIT_CODE%
