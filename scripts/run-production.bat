@echo off
setlocal EnableExtensions
set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..") do set "ROOT_DIR=%%~fI"

echo == Pasteleria ERP :: ejecutar modo operativo local ==
echo Este modo NO carga datos inventados de demo.
echo Usa la base local existente o la inicializa si esta vacia.
echo.
powershell -NoProfile -ExecutionPolicy Bypass -File "%ROOT_DIR%\tools\powershell\start-local-stack.ps1" -Mode production %*
set "EXIT_CODE=%ERRORLEVEL%"
endlocal & exit /b %EXIT_CODE%
