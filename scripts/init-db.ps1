param(
  [switch]$ForceReset,
  [string]$DatabaseName = "pasteleria"
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$composeFile = Join-Path $projectRoot "docker-compose.yml"
$containerName = "pasteleria-postgres"
$dbUser = "postgres"
$dbPassword = "postgres"
$schemaFile = Join-Path $projectRoot "db\V1\DATABASE_SCHEMA_CANONICO.sql"
$seedFile = Join-Path $projectRoot "db\V1\DATABASE_SEED_CANONICO.sql"

Write-Host "Levantando PostgreSQL del proyecto en localhost:5436 (Docker)..."
docker compose -f $composeFile up -d postgres | Out-Null

Write-Host "Esperando disponibilidad de PostgreSQL..."
for ($i = 0; $i -lt 20; $i++) {
  $ready = docker exec $containerName sh -lc "PGPASSWORD=$dbPassword pg_isready -h localhost -U $dbUser -d postgres"
  if ($LASTEXITCODE -eq 0) {
    break
  }
  Start-Sleep -Seconds 2
}

if ($ForceReset) {
  Write-Host "Recreando base $DatabaseName..."
  "drop database if exists $DatabaseName with (force);" | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d postgres" | Out-Null
  "create database $DatabaseName;" | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d postgres" | Out-Null
}

$databaseExistsQuery = "select exists (select 1 from pg_database where datname = '$DatabaseName');"
$databaseExists = $databaseExistsQuery | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d postgres -tA"

if ($databaseExists -ne "t") {
  Write-Host "Creando base $DatabaseName..."
  "create database $DatabaseName;" | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d postgres" | Out-Null
}

$tableExistsQuery = "select exists (select 1 from information_schema.tables where table_schema = 'public' and table_name = 'rol_usuario');"
$tableExists = $tableExistsQuery | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d $DatabaseName -tA"

if ($tableExists -eq "t") {
  Write-Host "La base $DatabaseName ya tiene esquema."
  Write-Host "Usa -ForceReset para recrearla con el SQL canónico."
  exit 0
}

Write-Host "Aplicando DATABASE_SCHEMA_CANONICO.sql..."
Get-Content -Raw $schemaFile | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -v ON_ERROR_STOP=1 -h localhost -U $dbUser -d $DatabaseName" | Out-Null
if ($LASTEXITCODE -ne 0) {
  throw "Fallo aplicando DATABASE_SCHEMA_CANONICO.sql."
}

Write-Host "Aplicando DATABASE_SEED_CANONICO.sql..."
Get-Content -Raw $seedFile | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -v ON_ERROR_STOP=1 -h localhost -U $dbUser -d $DatabaseName" | Out-Null
if ($LASTEXITCODE -ne 0) {
  throw "Fallo aplicando DATABASE_SEED_CANONICO.sql."
}

Write-Host "Base $DatabaseName inicializada correctamente con SQL canónico."
