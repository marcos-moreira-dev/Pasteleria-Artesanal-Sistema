@echo off
setlocal

set "PROJECT_ROOT=%~dp0"
set "START_SCRIPT=%PROJECT_ROOT%scripts\dev.bat"

if not exist "%START_SCRIPT%" (
  echo ERROR: No se encontro "%START_SCRIPT%".
  exit /b 1
)

call "%START_SCRIPT%" %*
exit /b %ERRORLEVEL%
