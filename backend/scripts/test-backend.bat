@echo off
setlocal EnableExtensions
cd /d "%~dp0.."
echo == Pasteleria Backend :: tests estabilizados ==
echo.
echo [1/2] Compilacion backend sin ejecutar tests
call .\mvnw.cmd -DskipTests compile
if errorlevel 1 goto :error

echo.
echo [2/2] Tests backend disponibles
call .\mvnw.cmd test
if errorlevel 1 goto :error

echo.
echo OK - Tests backend completados.
endlocal
exit /b 0

:error
echo.
echo ERROR - Tests backend detenidos en la etapa anterior.
endlocal
exit /b 1
