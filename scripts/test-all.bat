@echo off
setlocal EnableExtensions
set "SCRIPT_DIR=%~dp0"

echo == Pasteleria ERP :: test completo local ==
echo.
call "%SCRIPT_DIR%test-backend.bat" || exit /b 1
call "%SCRIPT_DIR%test-admin.bat" || exit /b 1
call "%SCRIPT_DIR%test-storefront.bat" || exit /b 1
echo.
echo OK - Backend, Admin Angular y Publico Astro validados.
endlocal & exit /b 0
