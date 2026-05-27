param(
  [switch]$ForceReset,
  [switch]$IncludePresentation,
  [string]$DatabaseName = "pasteleria"
)

$ErrorActionPreference = "Stop"
if (Get-Variable PSNativeCommandUseErrorActionPreference -Scope Global -ErrorAction SilentlyContinue) {
  $global:PSNativeCommandUseErrorActionPreference = $false
}

$projectRoot = Split-Path -Parent (Split-Path -Parent $PSScriptRoot)
$composeFile = Join-Path $projectRoot "infra\compose\docker-compose.dev.yml"
$composeProjectName = "pasteleria_dev"
$containerName = "pasteleria-postgres-dev"
$dbUser = "postgres"
$dbPassword = "postgres"

$migrationFiles = @(
  (Join-Path $projectRoot "backend\src\main\resources\db\migration\V1__pasteleria_base_actual.sql"),
  (Join-Path $projectRoot "backend\src\main\resources\db\migration\V2__erp_pasteleria_unificado_3fn.sql"),
  (Join-Path $projectRoot "backend\src\main\resources\db\migration\R__pasteleria_reporting_views.sql"),
  (Join-Path $projectRoot "backend\src\main\resources\db\migration\R__pasteleria_semantic_views.sql")
)

$presentationFiles = @(
  (Join-Path $projectRoot "backend\src\main\resources\db\presentation-migration\V200__seed_presentation_master_data.sql"),
  (Join-Path $projectRoot "backend\src\main\resources\db\presentation-migration\V201__seed_presentation_pasteleria_operations.sql"),
  (Join-Path $projectRoot "backend\src\main\resources\db\presentation-migration\V202__seed_presentation_erp_finance.sql"),
  (Join-Path $projectRoot "backend\src\main\resources\db\presentation-migration\V203__validate_presentation_seed.sql"),
  (Join-Path $projectRoot "backend\src\main\resources\db\validation\15_validate_presentation_sit.sql")
)

if (-not (Test-Path $composeFile)) {
  throw "No se encontro $composeFile."
}

function Ensure-Command {
  param([string]$Name)
  if (-not (Get-Command $Name -ErrorAction SilentlyContinue)) {
    throw "No se encontro '$Name' en PATH."
  }
}

function Remove-ContainerIfExists {
  param([string]$Name)
  $containers = docker ps -a --format "{{.Names}}" 2>$null
  if ($LASTEXITCODE -ne 0) { throw "No se pudo consultar Docker. Verifica que Docker Desktop este abierto." }
  if ($containers -contains $Name) {
    Write-Host "Eliminando contenedor legacy: $Name"
    docker rm -f $Name | Out-Null
    if ($LASTEXITCODE -ne 0) { throw "No se pudo eliminar el contenedor legacy $Name." }
  }
}

function Invoke-PsqlFileUtf8 {
  param([string]$FilePath, [string]$Label)
  if (-not (Test-Path $FilePath)) { throw "No se encontro el archivo SQL: $FilePath" }
  $containerSqlPath = "/tmp/" + ([IO.Path]::GetFileName($FilePath))
  docker cp $FilePath "${containerName}:$containerSqlPath" | Out-Null
  if ($LASTEXITCODE -ne 0) { throw "No se pudo copiar $Label al contenedor PostgreSQL." }
  docker exec $containerName sh -lc "PGPASSWORD=$dbPassword psql -v ON_ERROR_STOP=1 -h localhost -U $dbUser -d $DatabaseName -f $containerSqlPath" | Out-Null
  if ($LASTEXITCODE -ne 0) { throw "Fallo aplicando $Label." }
}

function Invoke-SqlFiles {
  param([string[]]$Files, [string]$GroupLabel)
  foreach ($file in $Files) {
    $label = "$GroupLabel :: $([IO.Path]::GetFileName($file))"
    Write-Host "Aplicando $label..."
    Invoke-PsqlFileUtf8 -FilePath $file -Label $label
  }
}

Ensure-Command "docker"
Write-Host "Levantando PostgreSQL local en localhost:5436..."
Remove-ContainerIfExists "pasteleria-postgres"
docker compose -p $composeProjectName -f $composeFile up -d postgres | Out-Null
if ($LASTEXITCODE -ne 0) { throw "No se pudo levantar PostgreSQL local con Docker Compose." }

Write-Host "Esperando disponibilidad de PostgreSQL..."
$ready = $false
for ($i = 0; $i -lt 45; $i++) {
  docker exec $containerName sh -lc "PGPASSWORD=$dbPassword pg_isready -h localhost -U $dbUser -d postgres" *> $null
  if ($LASTEXITCODE -eq 0) { $ready = $true; break }
  Start-Sleep -Seconds 2
}
if (-not $ready) { throw "PostgreSQL no respondio dentro del tiempo esperado." }

if ($ForceReset) {
  Write-Host "Recreando base $DatabaseName..."
  "drop database if exists $DatabaseName with (force);" | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d postgres" | Out-Null
  "create database $DatabaseName;" | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d postgres" | Out-Null
}

$databaseExistsQuery = "select exists (select 1 from pg_database where datname = '$DatabaseName');"
$databaseExists = $databaseExistsQuery | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d postgres -tA"
if ($databaseExists.Trim() -ne "t") {
  Write-Host "Creando base $DatabaseName..."
  "create database $DatabaseName;" | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d postgres" | Out-Null
}

$tableExistsQuery = "select exists (select 1 from information_schema.tables where table_schema = 'public' and table_name = 'rol_usuario');"
$tableExists = $tableExistsQuery | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d $DatabaseName -tA"

if ($tableExists.Trim() -eq "t") {
  Write-Host "La base $DatabaseName ya tiene esquema principal."
  if ($IncludePresentation) {
    Write-Host "Aplicando/validando datos de presentacion sobre base existente..."
    Invoke-SqlFiles -Files $presentationFiles -GroupLabel "presentation"
  } else {
    Write-Host "Modo operativo listo. No se aplican datos demo."
  }
  exit 0
}

Write-Host "Aplicando linea SQL actual V1/V2/R..."
Invoke-SqlFiles -Files $migrationFiles -GroupLabel "migration"

if ($IncludePresentation) {
  Write-Host "Aplicando datos de presentacion/SIT..."
  Invoke-SqlFiles -Files $presentationFiles -GroupLabel "presentation"
}

Write-Host "Base $DatabaseName inicializada correctamente."
