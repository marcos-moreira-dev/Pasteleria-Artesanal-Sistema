@echo off
setlocal EnableExtensions
cd /d "%~dp0.."

echo == Pasteleria :: smoke manual backend ==
echo Este script asume que el backend ya esta corriendo en http://localhost:8080
echo Puedes cambiar la URL con la variable PASTELERIA_API_URL.
echo.

powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0smoke-backend-manual.ps1"
set EXIT_CODE=%ERRORLEVEL%

if not "%EXIT_CODE%"=="0" (
  echo.
  echo ERROR - Smoke manual fallo.
  endlocal
  exit /b %EXIT_CODE%
)

echo.
echo OK - Smoke manual completado.
endlocal
exit /b 0
