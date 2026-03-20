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

$migrationFiles = Get-ChildItem (Join-Path $projectRoot "backend\src\main\resources\db\migration") -Filter "V*.sql" |
  Sort-Object Name |
  Select-Object -ExpandProperty FullName

Write-Host "Levantando PostgreSQL del proyecto en localhost:5434..."
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

$tableExistsQuery = "select exists (select 1 from information_schema.tables where table_schema = 'public' and table_name = 'rol_usuario');"
$tableExists = $tableExistsQuery | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d $DatabaseName -tA"

if ($tableExists -eq "t") {
  Write-Host "La base $DatabaseName ya tiene esquema. Verificando deltas estructurales pendientes..."

  $productionConstraintQuery = @"
select exists (
  select 1
  from pg_constraint c
  join pg_class t on t.oid = c.conrelid
  where t.relname = 'produccion'
    and c.conname = 'ck_produccion_estado'
    and pg_get_constraintdef(c.oid) like '%PREPARACION%'
);
"@
  $productionWorkflowReady = $productionConstraintQuery |
    docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d $DatabaseName -tA"

  if ($productionWorkflowReady -ne "t") {
    $v5File = Join-Path $projectRoot "backend\src\main\resources\db\migration\V5__expand_production_workflow.sql"
    Write-Host "Aplicando delta V5__expand_production_workflow.sql..."
    Get-Content -Raw $v5File | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -v ON_ERROR_STOP=1 -h localhost -U $dbUser -d $DatabaseName" | Out-Null
  }

  $versionColumnQuery = @"
select exists (
  select 1
  from information_schema.columns
  where table_schema = 'public'
    and table_name = 'categoria_producto'
    and column_name = 'version'
);
"@
  $optimisticLockingReady = $versionColumnQuery |
    docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d $DatabaseName -tA"

  if ($optimisticLockingReady -ne "t") {
    $v6File = Join-Path $projectRoot "backend\src\main\resources\db\migration\V6__optimistic_locking_core_tables.sql"
    Write-Host "Aplicando delta V6__optimistic_locking_core_tables.sql..."
    Get-Content -Raw $v6File | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -v ON_ERROR_STOP=1 -h localhost -U $dbUser -d $DatabaseName" | Out-Null
  }

  $legacyReportTypeQuery = @"
select exists (
  select 1
  from job_reporte
  where tipo_reporte not in ('RESUMEN_NEGOCIO', 'COLA_PRODUCCION')
);
"@
  $legacyReportTypes = $legacyReportTypeQuery |
    docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d $DatabaseName -tA"

  if ($legacyReportTypes -eq "t") {
    $v7File = Join-Path $projectRoot "backend\src\main\resources\db\migration\V7__normalize_legacy_report_types.sql"
    Write-Host "Aplicando delta V7__normalize_legacy_report_types.sql..."
    Get-Content -Raw $v7File | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -v ON_ERROR_STOP=1 -h localhost -U $dbUser -d $DatabaseName" | Out-Null
  }

  Write-Host "Verificacion de base existente completada."
  exit 0
}

foreach ($file in $migrationFiles) {
  Write-Host "Aplicando $(Split-Path -Leaf $file)..."
  Get-Content -Raw $file | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -v ON_ERROR_STOP=1 -h localhost -U $dbUser -d $DatabaseName" | Out-Null
  if ($LASTEXITCODE -ne 0) {
    throw "Fallo aplicando $(Split-Path -Leaf $file)."
  }
}

Write-Host "Base $DatabaseName inicializada correctamente."
