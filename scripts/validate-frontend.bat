@echo off
setlocal EnableExtensions
cd /d "%~dp0.."

echo == Pasteleria :: validate frontend principal ==
echo Delegando a scripts\validate-admin-angular.bat para conservar compatibilidad tipo Cedro.
echo.

call scripts\validate-admin-angular.bat
set "EXIT_CODE=%ERRORLEVEL%"
endlocal
exit /b %EXIT_CODE%
