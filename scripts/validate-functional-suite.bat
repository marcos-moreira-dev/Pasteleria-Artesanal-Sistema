@echo off
setlocal EnableExtensions
cd /d "%~dp0.."
call scripts\validate-system-full.bat %*
endlocal
exit /b %ERRORLEVEL%
