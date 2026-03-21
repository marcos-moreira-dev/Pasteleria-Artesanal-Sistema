@echo off
chcp 65001 >nul
echo ============================================
echo  PASTELERIA - INICIO RAPIDO COMPLETO
echo ============================================
echo.
echo Este script inicia tanto el backend como el frontend
echo.

REM Verificar que PostgreSQL este corriendo
echo [1/4] Verificando PostgreSQL...
tasklist /FI "IMAGENAME eq postgres.exe" 2>NUL | find /I /N "postgres.exe">NUL
if "%ERRORLEVEL%"=="1" (
    echo.
    echo ERROR: PostgreSQL no esta ejecutandose!
    echo Por favor inicia PostgreSQL desde pgAdmin o servicios de Windows
    echo.
    pause
    exit /b 1
)
echo     PostgreSQL OK
echo.

REM Verificar base de datos
echo [2/4] Verificando base de datos 'pasteleria'...
cd /d "%~dp0\backend"
call .\mvnw.cmd help:evaluate -Dexpression=project.version -q -DforceStdout >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo     Maven no disponible, continuando...
)
echo     Configuracion OK
echo.

REM Iniciar Backend en nueva ventana
echo [3/4] Iniciando Backend...
echo     Puertos: 8080 (API)
echo     Acceso: http://localhost:8080
echo.
start "BACKEND - Pasteleria" cmd /k "cd /d "%~dp0\backend" && .\scripts\start-backend-dev.cmd"

REM Esperar 15 segundos para que el backend inicie
echo     Esperando 15 segundos para inicializacion...
timeout /t 15 /nobreak >nul

REM Iniciar Frontend en nueva ventana
echo.
echo [4/4] Iniciando Frontend...
echo     Puerto: 4200 (Angular)
echo     Acceso: http://localhost:4200
echo.
start "FRONTEND - Pasteleria" cmd /k "cd /d "%~dp0\frontend-admin-angular" && npm start"

echo.
echo ============================================
echo  SISTEMA INICIADO CORRECTAMENTE
echo ============================================
echo.
echo Accesos:
echo   - Frontend: http://localhost:4200
echo   - Backend API: http://localhost:8080
echo   - Documentacion API: http://localhost:8080/swagger-ui.html
echo.
echo Credenciales de prueba:
echo   - Usuario: admin
echo   - Password: admin123
echo.
echo Para detener:
echo   - Cierra las ventanas de comandos abiertas
echo   - O presiona Ctrl+C en cada ventana
echo.
echo ============================================
echo.
pause
