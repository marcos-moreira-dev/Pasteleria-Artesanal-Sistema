@echo off
setlocal EnableExtensions

set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..\..") do set "ROOT_DIR=%%~fI"

echo == Pasteleria Backend :: dev ==
echo Delegando al arranque estabilizado de la raiz.
echo.

call "%ROOT_DIR%\scripts\dev-backend.bat"
endlocal
exit /b %ERRORLEVEL%
