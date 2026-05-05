@echo off
setlocal EnableExtensions
cd /d "%~dp0.."

echo == Pasteleria :: frontend Angular admin dev ==
set "FRONTEND_DIR=%CD%\frontend-admin-angular"
echo Frontend dir: %FRONTEND_DIR%
echo URL local esperada: http://localhost:4200

where npm >nul 2>nul
if errorlevel 1 (
  echo ERROR: npm no esta disponible.
  exit /b 1
)

pushd "%FRONTEND_DIR%"
if not exist node_modules (
  echo Instalando dependencias frontend admin...
  call npm install
  if errorlevel 1 exit /b 1
)
call npm start
popd
endlocal
