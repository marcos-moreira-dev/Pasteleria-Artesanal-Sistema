@echo off
setlocal EnableExtensions
cd /d "%~dp0.."

for /f %%i in ('powershell -NoProfile -Command "Get-Date -Format yyyyMMdd_HHmmss"') do set TS=%%i
set "ROOT_DIR=%CD%"
set "LOG_DIR=%ROOT_DIR%\.diagnostics\logs"
set "LOG_FILE=%LOG_DIR%\validate-system-full_%TS%.log"

if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

echo == Pasteleria :: validacion integral funcional ==
echo Log: %LOG_FILE%
echo.

powershell -NoProfile -ExecutionPolicy Bypass -File "%ROOT_DIR%\scripts\validate-system-full.ps1" -LogFile "%LOG_FILE%" %*
set "EXIT_CODE=%ERRORLEVEL%"

if "%EXIT_CODE%"=="0" (
  echo.
  echo OK - Validacion integral funcional completada.
  echo Log: %LOG_FILE%
) else (
  echo.
  echo ERROR - Validacion integral funcional fallo.
  echo Revisa el log: %LOG_FILE%
  echo.
  echo Ultimas lineas del log:
  powershell -NoProfile -Command "Get-Content -Path '%LOG_FILE%' -Tail 120"
)

endlocal
exit /b %EXIT_CODE%
