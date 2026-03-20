param(
  [string]$DatabaseName = "pasteleria"
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$composeFile = Join-Path $projectRoot "docker-compose.yml"
$containerName = "pasteleria-postgres"
$dbUser = "postgres"
$dbPassword = "postgres"
$deltaFile = Join-Path $projectRoot "backend\src\main\resources\db\migration\V4__seed_enterprise_demo.sql"

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

$markerQuery = "select exists (select 1 from categoria_producto where codigo = 'CUPCAKES');"
$alreadyApplied = $markerQuery | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -h localhost -U $dbUser -d $DatabaseName -tA"

if ($alreadyApplied -eq "t") {
  Write-Host "El seed enriquecido ya fue aplicado en $DatabaseName."
  exit 0
}

Write-Host "Aplicando V4__seed_enterprise_demo.sql sobre $DatabaseName..."
Get-Content -Raw $deltaFile | docker exec -i $containerName sh -lc "PGPASSWORD=$dbPassword psql -v ON_ERROR_STOP=1 -h localhost -U $dbUser -d $DatabaseName" | Out-Null

if ($LASTEXITCODE -ne 0) {
  throw "Fallo aplicando el seed enriquecido."
}

Write-Host "Seed enriquecido aplicado correctamente en $DatabaseName."
