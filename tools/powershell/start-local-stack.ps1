param(
  [ValidateSet("production", "demo")]
  [string]$Mode = "production",
  [switch]$SkipInstall,
  [switch]$NoBrowser,
  [switch]$ResetDatabase,
  [switch]$DryRun
)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent (Split-Path -Parent $PSScriptRoot)
$backendRoot = Join-Path $projectRoot "backend"
$adminRoot = Join-Path $projectRoot "frontend-admin-angular"
$publicRoot = Join-Path $projectRoot "frontend-publico-astro"
$initDbScript = Join-Path $PSScriptRoot "init-db.ps1"

$jwtSecret = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"
$isDemo = $Mode -eq "demo"
$profileName = if ($isDemo) { "presentation" } else { "dev" }
$storageRoot = if ($isDemo) { "./storage-sit" } else { "./storage" }
$reportStoragePath = "$storageRoot/reportes"
$modeLabel = if ($isDemo) { "DEMO / PRESENTACION con datos inventados" } else { "OPERACION LOCAL sin datos demo" }

function Ensure-Command {
  param([string]$Name)
  if (-not (Get-Command $Name -ErrorAction SilentlyContinue)) { throw "No se encontro '$Name' en PATH." }
}

function Start-WorkspaceWindow {
  param([string]$Title, [string]$Workdir, [string]$Command)
  $escapedWorkdir = $Workdir.Replace("'", "''")
  $scriptBlock = "Set-Location -LiteralPath '$escapedWorkdir'; `$Host.UI.RawUI.WindowTitle = '$Title'; $Command"
  if ($DryRun) {
    Write-Host "[dry-run] $Title"
    Write-Host "          $scriptBlock"
    return
  }
  Start-Process powershell -WorkingDirectory $Workdir -ArgumentList @("-NoExit", "-ExecutionPolicy", "Bypass", "-Command", $scriptBlock) -WindowStyle Normal | Out-Null
}

function Start-BrowserLauncher {
  param([string[]]$Urls)
  $lines = @(
    '$ErrorActionPreference = "SilentlyContinue"',
    'function Wait-Url($url) {',
    '  for ($i = 0; $i -lt 90; $i++) {',
    '    try { Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 2 | Out-Null; return $true }',
    '    catch { Start-Sleep -Seconds 2 }',
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
    $Urls | ForEach-Object { Write-Host "          $_" }
    return
  }
  Start-Process powershell -ArgumentList @("-NoProfile", "-ExecutionPolicy", "Bypass", "-WindowStyle", "Hidden", "-Command", $scriptBlock) -WindowStyle Hidden | Out-Null
}

function Sync-LocalCatalogAssets {
  $sourceAssets = Join-Path $backendRoot "storage\assets"
  $normalizedStorage = $storageRoot.Replace("/", "\").TrimStart(".\")
  $targetRoot = Join-Path $backendRoot $normalizedStorage
  $targetAssets = Join-Path $targetRoot "assets"
  $targetReports = Join-Path $targetRoot "reportes"

  if (-not (Test-Path $sourceAssets)) {
    Write-Host "No se encontro storage base de assets: $sourceAssets"
    return
  }

  New-Item -ItemType Directory -Force -Path $targetAssets | Out-Null
  New-Item -ItemType Directory -Force -Path $targetReports | Out-Null

  $sourceFull = (Resolve-Path $sourceAssets).Path
  $targetFull = (Resolve-Path $targetAssets).Path
  if ($sourceFull -ne $targetFull) {
    Write-Host "Sincronizando imagenes publicas hacia $targetAssets..."
    Copy-Item -Path (Join-Path $sourceAssets "*") -Destination $targetAssets -Recurse -Force
  } else {
    Write-Host "Storage de assets operativo listo en $targetAssets."
  }
}

Write-Host "============================================"
Write-Host " PASTELERIA ERP - ARRANQUE LOCAL"
Write-Host "============================================"
Write-Host "Modo: $modeLabel"
Write-Host "Backend: http://localhost:8080"
Write-Host "Admin:   http://localhost:4200"
Write-Host "Publico: http://localhost:4321"
Write-Host "Perfil Spring: $profileName"
Write-Host "Storage: $storageRoot"
Write-Host ""

if (-not $DryRun) {
  Ensure-Command "docker"
  Ensure-Command "npm"
}

if ($DryRun) {
  Write-Host "[dry-run] Se omitira inicializacion de base."
} else {
  if ($isDemo) {
    & $initDbScript -ForceReset -IncludePresentation
  } elseif ($ResetDatabase) {
    & $initDbScript -ForceReset
  } else {
    & $initDbScript
  }
  Sync-LocalCatalogAssets
}

$backendCommand = @'
$env:APP_PORT='8080'
$env:DB_HOST='localhost'
$env:DB_PORT='5436'
$env:DB_USER='postgres'
$env:DB_PASSWORD='postgres'
$env:DB_NAME='pasteleria'
$env:JWT_SECRET='__JWT_SECRET__'
$env:SPRING_PROFILES_ACTIVE='__PROFILE_NAME__'
$env:SPRING_FLYWAY_ENABLED='false'
$env:APP_STORAGE_ROOT='__STORAGE_ROOT__'
$env:REPORT_STORAGE_PATH='__REPORT_STORAGE_PATH__'
.\mvnw.cmd spring-boot:run
'@.Replace("__JWT_SECRET__", $jwtSecret).Replace("__PROFILE_NAME__", $profileName).Replace("__STORAGE_ROOT__", $storageRoot).Replace("__REPORT_STORAGE_PATH__", $reportStoragePath)

$adminCommand = if ($SkipInstall) { "npm start" } else { "if (-not (Test-Path 'node_modules')) { npm install }; npm start" }
$publicCommand = if ($SkipInstall) { "`$env:PUBLIC_API_BASE_URL='http://localhost:8080/api/v1'; npm run dev" } else { "`$env:PUBLIC_API_BASE_URL='http://localhost:8080/api/v1'; if (-not (Test-Path 'node_modules')) { npm install }; npm run dev" }

Write-Host "Lanzando ventanas de backend, admin y publico..."
Start-WorkspaceWindow -Title "Pasteleria Backend" -Workdir $backendRoot -Command $backendCommand
Start-Sleep -Seconds 3
Start-WorkspaceWindow -Title "Pasteleria Admin Angular" -Workdir $adminRoot -Command $adminCommand
Start-WorkspaceWindow -Title "Pasteleria Publico Astro" -Workdir $publicRoot -Command $publicCommand

if (-not $NoBrowser) {
  Start-BrowserLauncher -Urls @("http://localhost:4200", "http://localhost:4321")
}

Write-Host ""
Write-Host "Servicios solicitados. Credenciales locales: admin / admin12345"
Write-Host "Para detener las ventanas, cierra las consolas abiertas."
