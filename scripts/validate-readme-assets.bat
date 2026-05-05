@echo off
setlocal EnableExtensions
cd /d "%~dp0.."
powershell -NoProfile -ExecutionPolicy Bypass -File "%CD%\scripts\validate-readme-assets.ps1" -FailOnMissing
exit /b %ERRORLEVEL%
