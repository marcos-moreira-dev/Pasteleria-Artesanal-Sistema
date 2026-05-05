param(
  [switch]$FailOnMissing
)

$ErrorActionPreference = "Stop"
$RootDir = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
$ReadmeDir = Join-Path $RootDir "readme"
$AssetsReadmeCapturasDir = Join-Path $RootDir "assets-readme\capturas"
$AssetsReadmeLogoDir = Join-Path $RootDir "assets-readme\logo"

$requiredReadme = @(
  "readme-hero.png",
  "public-home.png",
  "public-catalogo.png",
  "public-contacto.png",
  "public-en.png",
  "admin-dashboard-general.png",
  "admin-clientes.png",
  "admin-productos.png",
  "admin-cotizaciones.png",
  "admin-pedidos.png",
  "admin-produccion.png",
  "admin-reportes.png",
  "admin-guia-operativa.png",
  "admin-abastecimiento-dashboard.png",
  "admin-abastecimiento-inventario.png",
  "admin-abastecimiento-compras.png",
  "admin-abastecimiento-proveedores.png",
  "admin-abastecimiento-movimientos.png"
)

$requiredLegacy = @(
  "01-landing-home.png",
  "02-catalogo.png",
  "03-contacto.png",
  "04-admin-dashboard.png",
  "05-admin-clientes.png",
  "06-admin-pedidos.png",
  "07-admin-produccion.png"
)

$missing = @()

Write-Host "== Pasteleria :: validacion de imagenes README =="

foreach ($file in $requiredReadme) {
  $path = Join-Path $ReadmeDir $file
  if (Test-Path $path) { Write-Host "[OK] readme\$file" }
  else { Write-Host "[MISS] readme\$file"; $missing += "readme\$file" }
}

foreach ($file in $requiredLegacy) {
  $path = Join-Path $AssetsReadmeCapturasDir $file
  if (Test-Path $path) { Write-Host "[OK] assets-readme\capturas\$file" }
  else { Write-Host "[MISS] assets-readme\capturas\$file"; $missing += "assets-readme\capturas\$file" }
}

$logo = Join-Path $AssetsReadmeLogoDir "logo-principal.png"
if (Test-Path $logo) { Write-Host "[OK] assets-readme\logo\logo-principal.png" }
else { Write-Host "[MISS] assets-readme\logo\logo-principal.png"; $missing += "assets-readme\logo\logo-principal.png" }

Write-Host ""
Write-Host "Faltantes: $($missing.Count)"

if ($FailOnMissing -and $missing.Count -gt 0) {
  throw "Faltan imagenes README: $($missing -join ', ')"
}

if ($missing.Count -gt 0) { exit 1 }
exit 0
