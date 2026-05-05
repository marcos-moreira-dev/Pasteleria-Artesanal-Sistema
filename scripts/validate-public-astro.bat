@echo off
setlocal EnableExtensions
cd /d "%~dp0.."

for /f %%i in ('powershell -NoProfile -Command "Get-Date -Format yyyyMMdd_HHmmss"') do set TS=%%i
set "ROOT_DIR=%CD%"
set "PUBLIC_DIR=%ROOT_DIR%\frontend-publico-astro"
set "LOG_DIR=%ROOT_DIR%\.diagnostics\logs"
set "LOG_FILE=%LOG_DIR%\validate-public-astro_%TS%.log"

if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

echo == Pasteleria :: validacion publico Astro ==
echo Frontend publico: %PUBLIC_DIR%
echo Log: %LOG_FILE%
echo.

where node >nul 2>nul
if errorlevel 1 (
  echo ERROR: Node.js no esta disponible. > "%LOG_FILE%"
  echo ERROR: Node.js no esta disponible.
  exit /b 1
)

where npm >nul 2>nul
if errorlevel 1 (
  echo ERROR: npm no esta disponible. > "%LOG_FILE%"
  echo ERROR: npm no esta disponible.
  exit /b 1
)

pushd "%PUBLIC_DIR%"

echo [1/3] Versiones > "%LOG_FILE%"
node -v >> "%LOG_FILE%" 2>&1
call npm -v >> "%LOG_FILE%" 2>&1

echo [2/3] Dependencias >> "%LOG_FILE%"
if not exist node_modules (
  call npm install >> "%LOG_FILE%" 2>&1
  if errorlevel 1 goto :fail
) else (
  echo node_modules ya existe; se omite npm install. >> "%LOG_FILE%"
)

echo [3/3] Build Astro >> "%LOG_FILE%"
set "PUBLIC_API_BASE_URL=http://localhost:8080/api/v1"
call npm run build >> "%LOG_FILE%" 2>&1
if errorlevel 1 goto :fail

popd
echo OK - Publico Astro compila.
echo Log: %LOG_FILE%
endlocal
exit /b 0

:fail
popd
echo ERROR - Fallo la validacion del publico Astro.
echo Revisa: %LOG_FILE%
powershell -NoProfile -Command "Get-Content -Path '%LOG_FILE%' -Tail 80"
endlocal
exit /b 1
