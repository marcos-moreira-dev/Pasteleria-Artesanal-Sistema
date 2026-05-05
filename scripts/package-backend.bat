@echo off
setlocal EnableExtensions
cd /d "%~dp0..\backend"

echo == Pasteleria :: package backend ==
where mvn >nul 2>nul
if errorlevel 1 (
  set "MVN_CMD=.\mvnw.cmd"
  echo [INFO] Maven global no disponible; usando backend\mvnw.cmd
) else (
  set "MVN_CMD=mvn"
  echo [INFO] Maven global disponible; usando toolchain JDK 21 configurado por el proyecto
)
call "%MVN_CMD%" clean package -DskipTests=false
endlocal
