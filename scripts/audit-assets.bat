@echo off
setlocal
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0audit-assets.ps1" %*
endlocal
