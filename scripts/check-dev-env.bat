@echo off
setlocal EnableExtensions
cd /d "%~dp0.."

echo == Pasteleria :: chequeo de entorno dev ==
echo Raiz: %CD%
echo.

if not exist "backend\pom.xml" (
  echo ERROR: No se encontro backend\pom.xml. Ejecuta este script desde el paquete completo del proyecto.
  exit /b 1
)

if not exist "backend\mvnw.cmd" (
  echo ERROR: No se encontro backend\mvnw.cmd.
  exit /b 1
)

if not exist "infra\compose\docker-compose.dev.yml" (
  echo ERROR: No se encontro infra\compose\docker-compose.dev.yml.
  exit /b 1
)

if not exist "backend\src\main\resources\application.yml" (
  echo ERROR: No se encontro backend\src\main\resources\application.yml.
  exit /b 1
)

if not exist "backend\src\main\resources\db\migration\V13__guia_operativa.sql" (
  echo ERROR: No se encontro backend\src\main\resources\db\migration\V13__guia_operativa.sql.
  echo La guia operativa no esta completa.
  exit /b 1
)

if not exist "db\V1\DATABASE_SCHEMA_CANONICO.sql" (
  echo ERROR: No se encontro db\V1\DATABASE_SCHEMA_CANONICO.sql.
  exit /b 1
)

if not exist "db\V1\DATABASE_SEED_CANONICO.sql" (
  echo ERROR: No se encontro db\V1\DATABASE_SEED_CANONICO.sql.
  exit /b 1
)

where java >nul 2>nul
if errorlevel 1 (
  echo ERROR: Java no esta en PATH. Instala/configura JDK 21.
  exit /b 1
)
echo [OK] Java detectado
java -version

echo.
where docker >nul 2>nul
if errorlevel 1 (
  echo ERROR: Docker no esta en PATH o Docker Desktop no esta disponible.
  exit /b 1
)
echo [OK] Docker detectado
docker --version
docker compose version
if errorlevel 1 (
  echo ERROR: Docker Compose v2 no esta disponible.
  exit /b 1
)

echo.
where node >nul 2>nul
if errorlevel 1 (
  echo ERROR: Node.js no esta en PATH.
  exit /b 1
)
echo [OK] Node detectado
node -v

echo.
where npm >nul 2>nul
if errorlevel 1 (
  echo ERROR: npm no esta en PATH.
  exit /b 1
)
echo [OK] npm detectado
call npm -v

echo.
where mvn >nul 2>nul
if errorlevel 1 (
  echo [INFO] Maven global no esta en PATH; se usara backend\mvnw.cmd.
) else (
  echo [OK] Maven global detectado
  call mvn -version
)

echo.
echo OK - Entorno minimo detectado para validar Pasteleria.
endlocal
exit /b 0
