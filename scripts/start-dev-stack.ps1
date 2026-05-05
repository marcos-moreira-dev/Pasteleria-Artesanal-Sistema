param(
  [switch]$SkipInstall,
  [switch]$DryRun,
  [switch]$NoBrowser
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$backendRoot = Join-Path $projectRoot "backend"
$adminRoot = Join-Path $projectRoot "frontend-admin-angular"
$publicRoot = Join-Path $projectRoot "frontend-publico-astro"
$initDbScript = Join-Path $PSScriptRoot "init-db.ps1"
$jwtSecret = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"

function Ensure-Command {
  param([string]$Name)

  if (-not (Get-Command $Name -ErrorAction SilentlyContinue)) {
    throw "No se encontro '$Name' en PATH."
  }
}

function Start-WorkspaceWindow {
  param(
    [string]$Title,
    [string]$Workdir,
    [string]$Command
  )

  $escapedWorkdir = $Workdir.Replace("'", "''")
  $scriptBlock = "Set-Location -LiteralPath '$escapedWorkdir'; $Command"

  if ($DryRun) {
    Write-Host "[dry-run] $Title"
    Write-Host "          $scriptBlock"
    return
  }

  Start-Process powershell `
    -WorkingDirectory $Workdir `
    -ArgumentList @("-NoExit", "-ExecutionPolicy", "Bypass", "-Command", $scriptBlock) `
    -WindowStyle Normal `
    | Out-Null
}

function Start-BrowserLauncher {
  param([string[]]$Urls)

  $lines = @(
    '$ErrorActionPreference = "SilentlyContinue"',
    'function Wait-Url($url) {',
    '  for ($i = 0; $i -lt 90; $i++) {',
    '    try {',
    '      Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 2 | Out-Null',
    '      return $true',
    '    } catch {',
    '      Start-Sleep -Seconds 2',
    '    }',
    '  }',
    '  return $false',
    '}'
  )

  foreach ($url in $Urls) {
    $escapedUrl = $url.Replace("'", "''")
    $lines += "if (Wait-Url '$escapedUrl') { Start-Process '$escapedUrl' | Out-Null; Start-Sleep -Seconds 1 }"
  }

  $scriptBlock = ($lines -join "; ")

  if ($DryRun) {
    Write-Host "[dry-run] Browser tabs"
    foreach ($url in $Urls) {
      Write-Host "          $url"
    }
    return
  }

  Start-Process powershell `
    -ArgumentList @("-NoProfile", "-ExecutionPolicy", "Bypass", "-WindowStyle", "Hidden", "-Command", $scriptBlock) `
    -WindowStyle Hidden `
    | Out-Null
}

Write-Host "============================================"
Write-Host " PASTELERIA - ARRANQUE LOCAL UNIFICADO"
Write-Host "============================================"
Write-Host ""
Write-Host "Modo canonico:"
Write-Host "- PostgreSQL por Docker"
Write-Host "- Backend local en 8080"
Write-Host "- Admin Angular en 4200"
Write-Host "- Frontend publico Astro en 4321"
Write-Host ""

if (-not $DryRun) {
  Ensure-Command "docker"
  Ensure-Command "npm"
}

if ($DryRun) {
  Write-Host "[dry-run] Se omitira el arranque real de la base."
} else {
  & $initDbScript
}

$backendCommand = @'
$env:DB_HOST='localhost'
$env:DB_PORT='5436'
$env:DB_USER='postgres'
$env:DB_PASSWORD='postgres'
$env:DB_NAME='pasteleria'
$env:JWT_SECRET='__JWT_SECRET__'
$env:SPRING_FLYWAY_ENABLED='false'
.\scripts\start-backend-dev.cmd
'@.Replace("__JWT_SECRET__", $jwtSecret)

$adminCommand = if ($SkipInstall) {
  "npm start"
} else {
  "if (-not (Test-Path 'node_modules')) { npm install }; npm start"
}

$publicCommand = if ($SkipInstall) {
  '$env:PUBLIC_API_BASE_URL=''http://localhost:8080/api/v1''; npm run dev'
} else {
  '$env:PUBLIC_API_BASE_URL=''http://localhost:8080/api/v1''; if (-not (Test-Path ''node_modules'')) { npm install }; npm run dev'
}

Write-Host ""
Write-Host "Lanzando servicios..."

Start-WorkspaceWindow -Title "Backend" -Workdir $backendRoot -Command $backendCommand
Start-Sleep -Seconds 3
Start-WorkspaceWindow -Title "Admin Angular" -Workdir $adminRoot -Command $adminCommand
Start-WorkspaceWindow -Title "Frontend publico Astro" -Workdir $publicRoot -Command $publicCommand

if (-not $NoBrowser) {
  Start-BrowserLauncher -Urls @(
    "http://localhost:4200",
    "http://localhost:4321"
  )
}

Write-Host ""
Write-Host "Servicios solicitados."
Write-Host "- PostgreSQL Docker: localhost:5436"
Write-Host "- Backend: http://localhost:8080"
Write-Host "- Admin: http://localhost:4200"
Write-Host "- Publico: http://localhost:4321"
Write-Host "- Credenciales locales de revisión: admin / admin12345"
if (-not $NoBrowser) {
  Write-Host "- Se abriran automaticamente las pestanas de admin y landing"
}
Write-Host ""
Write-Host "Si es el primer arranque, npm puede tardar en instalar dependencias."
