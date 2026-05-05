param(
  [string]$DatabaseName = "pasteleria"
)

$ErrorActionPreference = "Stop"
# En PowerShell 7+, evita que comandos nativos opcionales con codigo != 0 detengan el script.
# Los comandos criticos se validan explicitamente con $LASTEXITCODE.
if (Get-Variable PSNativeCommandUseErrorActionPreference -Scope Global -ErrorAction SilentlyContinue) {
  $global:PSNativeCommandUseErrorActionPreference = $false
}

$projectRoot = Split-Path -Parent $PSScriptRoot
$composeFile = Join-Path $projectRoot "infra\compose\docker-compose.dev.yml"
$composeProjectName = "pasteleria_dev"
$containerName = "pasteleria-postgres-dev"
$dbUser = "postgres"
$dbPassword = "postgres"
$migrationFile = Join-Path $projectRoot "backend\src\main\resources\db\migration\V13__guia_operativa.sql"
$contentPatchFile = Join-Path $projectRoot "backend\src\main\resources\db\migration\V14__contenido_operativo_final.sql"
$utf8RecipePatchFile = Join-Path $projectRoot "backend\src\main\resources\db\migration\V15__correccion_utf8_recetas_pdf.sql"

if (-not (Test-Path $migrationFile)) {
  throw "No se encontro la migracion V13__guia_operativa.sql en: $migrationFile"
}

Write-Host "== Pasteleria :: reparacion DB guia operativa =="
Write-Host "Base: $DatabaseName"
Write-Host "Modo: no destructivo, aplica V13 y luego V14 de contenido operativo final"
Write-Host ""

function Remove-ContainerIfExists {
  param([string]$Name)

  try {
    $containers = docker ps -a --format "{{.Names}}" 2>$null
  } catch {
    throw "No se pudo consultar Docker. Verifica que Docker Desktop este abierto."
  }

  if ($LASTEXITCODE -ne 0) {
    throw "No se pudo consultar Docker. Verifica que Docker Desktop este abierto."
  }

  if ($containers -contains $Name) {
    Write-Host "Eliminando contenedor legacy: $Name"
    docker rm -f $Name | Out-Null
    if ($LASTEXITCODE -ne 0) {
      throw "No se pudo eliminar el contenedor legacy $Name."
    }
  }
}

function Invoke-PsqlFileUtf8 {
  param(
    [string]$FilePath,
    [string]$Label
  )

  if (-not (Test-Path $FilePath)) {
    throw "No se encontro el archivo SQL: $FilePath"
  }

  $containerSqlPath = "/tmp/" + ([IO.Path]::GetFileName($FilePath))
  docker cp $FilePath "${containerName}:$containerSqlPath" | Out-Null
  if ($LASTEXITCODE -ne 0) {
    throw "No se pudo copiar $Label al contenedor PostgreSQL."
  }

  docker exec $containerName sh -lc "PGPASSWORD=$dbPassword psql -v ON_ERROR_STOP=1 -h localhost -U $dbUser -d $DatabaseName -f $containerSqlPath" | Out-Null
  if ($LASTEXITCODE -ne 0) {
    throw "Fallo aplicando $Label."
  }
}

Write-Host "Levantando PostgreSQL dev del proyecto en localhost:5436 (Docker)..."
Remove-ContainerIfExists "pasteleria-postgres"
docker compose -p $composeProjectName -f $composeFile up -d postgres | Out-Null
if ($LASTEXITCODE -ne 0) {
  throw "No se pudo levantar PostgreSQL dev con Docker Compose."
}

Write-Host "Esperando disponibilidad de PostgreSQL..."
$ready = $false
for ($i = 0; $i -lt 30; $i++) {
  & docker exec $containerName sh -lc "PGPASSWORD=$dbPassword pg_isready -h localhost -U $dbUser -d postgres" *> $null
  if ($LASTEXITCODE -eq 0) {
    $ready = $true
    break
  }
  Start-Sleep -Seconds 2
}

if (-not $ready) {
  throw "PostgreSQL no respondio dentro del tiempo esperado. Revisa Docker Desktop y el contenedor $containerName."
}

$databaseExistsQuery = "select exists (select 1 from pg_database where datname = '$DatabaseName');"
$databaseExists = $databaseExistsQuery | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d postgres -tA"

if ($databaseExists.Trim() -ne "t") {
  throw "La base '$DatabaseName' no existe. Ejecuta scripts\reset-db-local.ps1 para crearla desde cero."
}

Write-Host "Aplicando V13__guia_operativa.sql..."
Invoke-PsqlFileUtf8 -FilePath $migrationFile -Label "V13__guia_operativa.sql"


if (Test-Path $contentPatchFile) {
  Write-Host "Aplicando V14__contenido_operativo_final.sql..."
  Invoke-PsqlFileUtf8 -FilePath $contentPatchFile -Label "V14__contenido_operativo_final.sql"
}

if (Test-Path $utf8RecipePatchFile) {
  Write-Host "Aplicando V15__correccion_utf8_recetas_pdf.sql..."
  Invoke-PsqlFileUtf8 -FilePath $utf8RecipePatchFile -Label "V15__correccion_utf8_recetas_pdf.sql"
}

$checkQuery = "select count(*) from information_schema.tables where table_schema = 'public' and table_name in ('caso_uso_modulo','caso_uso_operativo','paso_caso_uso');"
$createdTables = $checkQuery | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d $DatabaseName -tA"

if ($createdTables.Trim() -ne "3") {
  throw "La reparacion termino, pero no se detectaron las 3 tablas de guia operativa."
}

Write-Host ""
Write-Host "OK - Guia operativa reparada en la base local."
Write-Host "Ya puedes arrancar el backend con scripts\dev-backend.bat o backend\scripts\run-backend-dev.bat."
