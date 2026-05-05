@echo off
setlocal
pushd "%~dp0.."
powershell -NoProfile -ExecutionPolicy Bypass -File ".\scripts\repair-guia-operativa-db.ps1"
set EXIT_CODE=%ERRORLEVEL%
popd
exit /b %EXIT_CODE%
