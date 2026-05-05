@echo off
setlocal EnableExtensions

set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..\..") do set "ROOT_DIR=%%~fI"

echo == Pasteleria Backend :: start-backend-dev.cmd ==
echo Compatibilidad: delegando a scripts\dev-backend-inline.bat.
echo.

call "%ROOT_DIR%\scripts\dev-backend-inline.bat"
endlocal
exit /b %ERRORLEVEL%
