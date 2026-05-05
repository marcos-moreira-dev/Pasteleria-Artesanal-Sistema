@echo off
setlocal EnableExtensions

set "SCRIPT_DIR=%~dp0"

echo == Pasteleria :: backend dev ==
echo Abriendo consola visible para la salida de Maven/Spring Boot...
echo La ventana se queda abierta aunque el backend ya este corriendo.
echo.
echo Para detenerlo despues:
echo   desde scripts: .\stop-backend-dev.bat
echo   desde la raiz: scripts\stop-backend-dev.bat
echo.

start "Pasteleria Backend 8080" cmd /k ""%SCRIPT_DIR%dev-backend-inline.bat""

endlocal
exit /b 0
