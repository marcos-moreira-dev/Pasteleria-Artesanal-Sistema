@echo off
setlocal EnableExtensions
cd /d "%~dp0.."

echo == Pasteleria :: checklist cierre backend ==
echo.
echo [1] Ejecuta: scripts\check-dev-env.bat
echo [2] Levanta infraestructura: scripts\up-infra-dev.bat
echo [3] Inicializa base canonica: powershell -ExecutionPolicy Bypass -File scripts\init-db.ps1
echo [4] Valida backend: scripts\validate-backend.bat
echo [5] Levanta backend: scripts\dev-backend.bat
echo [6] En otra terminal: scripts\smoke-backend-manual.bat
echo [7] Confirma health: http://localhost:8080/actuator/health
echo [8] Verifica que no se versionen target, logs, node_modules, .angular, dist ni storage generado.
echo.
echo Si todo lo anterior pasa, el backend queda congelado para demo y cierre.
endlocal
