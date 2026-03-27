@echo off
setlocal

set "PROJECT_ROOT=%~dp0"
set "START_SCRIPT=%PROJECT_ROOT%scripts\start-dev-stack.ps1"

if not exist "%START_SCRIPT%" (
  echo ERROR: No se encontro "%START_SCRIPT%".
  exit /b 1
)

powershell -NoProfile -ExecutionPolicy Bypass -File "%START_SCRIPT%" %*
exit /b %ERRORLEVEL%
