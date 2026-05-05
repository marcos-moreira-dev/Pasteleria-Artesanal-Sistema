@echo off
setlocal EnableExtensions
cd /d "%~dp0.."

echo == Pasteleria :: frontend publico Astro dev ==
set "PUBLIC_DIR=%CD%\frontend-publico-astro"
echo Frontend publico dir: %PUBLIC_DIR%
echo URL local esperada: http://localhost:4321

where npm >nul 2>nul
if errorlevel 1 (
  echo ERROR: npm no esta disponible.
  exit /b 1
)

pushd "%PUBLIC_DIR%"
if not exist node_modules (
  echo Instalando dependencias frontend publico...
  call npm install
  if errorlevel 1 exit /b 1
)
set "PUBLIC_API_BASE_URL=http://localhost:8080/api/v1"
call npm run dev
popd
endlocal
