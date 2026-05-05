param(
  [switch]$ForceReset,
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
$schemaFile = Join-Path $projectRoot "db\V1\DATABASE_SCHEMA_CANONICO.sql"
$seedFile = Join-Path $projectRoot "db\V1\DATABASE_SEED_CANONICO.sql"
$contentPatchFile = Join-Path $projectRoot "backend\src\main\resources\db\migration\V14__contenido_operativo_final.sql"
$utf8RecipePatchFile = Join-Path $projectRoot "backend\src\main\resources\db\migration\V15__correccion_utf8_recetas_pdf.sql"

if (-not (Test-Path $composeFile)) {
  throw "No se encontro $composeFile. La infraestructura dev debe vivir en infra\compose, igual que Cedro Damasco."
}

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


function Apply-ContentPatchIfExists {
  if (Test-Path $contentPatchFile) {
    Write-Host "Aplicando contenido operativo final idempotente (recetas y guia ampliada)..."
    Invoke-PsqlFileUtf8 -FilePath $contentPatchFile -Label "V14__contenido_operativo_final.sql"
  }

  if (Test-Path $utf8RecipePatchFile) {
    Write-Host "Aplicando correccion UTF-8 de recetas PDF..."
    Invoke-PsqlFileUtf8 -FilePath $utf8RecipePatchFile -Label "V15__correccion_utf8_recetas_pdf.sql"
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
  $guiaTableExistsQuery = "select exists (select 1 from information_schema.tables where table_schema = 'public' and table_name = 'caso_uso_modulo');"
  $guiaTableExists = $guiaTableExistsQuery | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d $DatabaseName -tA"

  if ($guiaTableExists.Trim() -ne "t") {
    $guiaMigrationFile = Join-Path $projectRoot "backend\src\main\resources\db\migration\V13__guia_operativa.sql"
    if (-not (Test-Path $guiaMigrationFile)) {
      throw "La base ya existe, pero falta caso_uso_modulo y no se encontro V13__guia_operativa.sql."
    }

    Write-Host "La base $DatabaseName ya tenia esquema, pero faltaba la Guia operativa."
    Write-Host "Aplicando parche no destructivo V13__guia_operativa.sql..."
    Invoke-PsqlFileUtf8 -FilePath $guiaMigrationFile -Label "V13__guia_operativa.sql"
    Apply-ContentPatchIfExists
    Write-Host "Guia operativa agregada correctamente a la base existente."
    exit 0
  }

  Apply-ContentPatchIfExists
  Write-Host "La base $DatabaseName ya tiene esquema completo y contenido operativo actualizado."
  Write-Host "Usa -ForceReset para recrearla con el SQL canonico."
  exit 0
}

Write-Host "Aplicando DATABASE_SCHEMA_CANONICO.sql..."
Invoke-PsqlFileUtf8 -FilePath $schemaFile -Label "DATABASE_SCHEMA_CANONICO.sql"

Write-Host "Aplicando DATABASE_SEED_CANONICO.sql..."
Invoke-PsqlFileUtf8 -FilePath $seedFile -Label "DATABASE_SEED_CANONICO.sql"

Write-Host "Base $DatabaseName inicializada correctamente con SQL canonico."
