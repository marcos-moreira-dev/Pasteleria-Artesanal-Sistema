@echo off
setlocal EnableExtensions
cd /d "%~dp0.."
echo == Pasteleria Backend :: clean-generated-files.bat ==

if exist "target" (
  echo Eliminando target...
  rmdir /s /q target
)

if exist "backend-run.log" del /q backend-run.log
if exist "temp-report-runtime.log" del /q temp-report-runtime.log
if exist "temp-report-check.pdf" del /q temp-report-check.pdf

echo OK - Archivos generados de backend limpiados.
endlocal
