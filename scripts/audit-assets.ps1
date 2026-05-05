param(
  [switch]$FailOnMissing
)

$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$productsDir = Join-Path $root "backend\storage\assets\products"
$brandingDir = Join-Path $root "backend\storage\assets\branding"
$placeholderDir = Join-Path $root "backend\storage\assets\placeholders"
$readmeDir = Join-Path $root "readme"
$assetsReadmeCapturasDir = Join-Path $root "assets-readme\capturas"
$assetsReadmeLogoDir = Join-Path $root "assets-readme\logo"
$adminAssetsDir = Join-Path $root "frontend-admin-angular\src\assets"
$seedFile = Join-Path $root "db\V1\DATABASE_SEED_CANONICO.sql"
$reportDir = Join-Path $root ".diagnostics\assets"
New-Item -ItemType Directory -Force -Path $reportDir | Out-Null
$timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$reportPath = Join-Path $reportDir ("assets-audit_" + $timestamp + ".md")
$jsonPath = Join-Path $reportDir ("assets-audit_" + $timestamp + ".json")

$extensions = @(".png", ".jpg", ".jpeg", ".webp")

function Test-AssetBySlug {
  param([string]$Directory, [string]$Slug)
  foreach ($ext in $extensions) {
    if (Test-Path (Join-Path $Directory ($Slug + $ext))) { return $true }
  }
  return $false
}

function Get-ProductSlugsFromSeed {
  if (-not (Test-Path $seedFile)) { throw "No se encontro $seedFile" }
  $content = Get-Content -Raw -Path $seedFile -Encoding UTF8
  $matches = [regex]::Matches($content, "'([a-z0-9]+(?:-[a-z0-9]+)+)'\s*,\s*'[^']+'\s*,\s*'[^']+'\s*,")
  $slugs = New-Object System.Collections.Generic.List[string]
  foreach ($m in $matches) {
    $candidate = $m.Groups[1].Value
    if ($candidate -match "^(torta|brownie|alfajor|cheesecake|galleta|galletas|cafe|cupcake|mesa|caja|naked|tiramisu|pie|frappe|chocolate|brigadeiro)-") {
      if (-not $slugs.Contains($candidate)) { $slugs.Add($candidate) }
    }
  }
  return @($slugs | Sort-Object)
}

function Get-ReferencedAdminAssets {
  $refs = New-Object System.Collections.Generic.List[string]
  $sourceDirs = @(
    (Join-Path $root "frontend-admin-angular\src\app"),
    (Join-Path $root "frontend-admin-angular\src\index.html")
  )
  foreach ($source in $sourceDirs) {
    if (Test-Path $source -PathType Container) {
      Get-ChildItem -Path $source -Recurse -Include *.ts,*.html,*.scss,*.css | ForEach-Object {
        $content = Get-Content -Raw -Path $_.FullName -Encoding UTF8
        foreach ($m in [regex]::Matches($content, "assets/[A-Za-z0-9_./-]+\.(svg|png|jpg|jpeg|webp)")) {
          if (-not $refs.Contains($m.Value)) { $refs.Add($m.Value) }
        }
      }
    } elseif (Test-Path $source) {
      $content = Get-Content -Raw -Path $source -Encoding UTF8
      foreach ($m in [regex]::Matches($content, "assets/[A-Za-z0-9_./-]+\.(svg|png|jpg|jpeg|webp)")) {
        if (-not $refs.Contains($m.Value)) { $refs.Add($m.Value) }
      }
    }
  }
  return @($refs | Sort-Object)
}

$productSlugs = Get-ProductSlugsFromSeed
$missingProducts = @()
foreach ($slug in $productSlugs) {
  if (-not (Test-AssetBySlug -Directory $productsDir -Slug $slug)) { $missingProducts += $slug }
}

$requiredBranding = @(
  "logo-cuadrado.png",
  "logo-horizontal.png",
  "banner-chicas.png",
  "banner-pastel.png"
)
$missingBranding = @()
foreach ($file in $requiredBranding) {
  if (-not (Test-Path (Join-Path $brandingDir $file))) { $missingBranding += $file }
}

$requiredPlaceholders = @(
  "product-placeholder.png",
  "landing-placeholder.png"
)
$missingPlaceholders = @()
foreach ($file in $requiredPlaceholders) {
  if (-not (Test-Path (Join-Path $placeholderDir $file))) { $missingPlaceholders += $file }
}


$requiredReadmeImages = @(
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
$missingReadmeImages = @()
foreach ($file in $requiredReadmeImages) {
  if (-not (Test-Path (Join-Path $readmeDir $file))) { $missingReadmeImages += $file }
}

$requiredAssetsReadmeCapturas = @(
  "01-landing-home.png",
  "02-catalogo.png",
  "03-contacto.png",
  "04-admin-dashboard.png",
  "05-admin-clientes.png",
  "06-admin-pedidos.png",
  "07-admin-produccion.png"
)
$missingAssetsReadmeCapturas = @()
foreach ($file in $requiredAssetsReadmeCapturas) {
  if (-not (Test-Path (Join-Path $assetsReadmeCapturasDir $file))) { $missingAssetsReadmeCapturas += $file }
}

$missingAssetsReadmeLogo = @()
if (-not (Test-Path (Join-Path $assetsReadmeLogoDir "logo-principal.png"))) { $missingAssetsReadmeLogo += "logo-principal.png" }

$adminRefs = Get-ReferencedAdminAssets
$missingAdminRefs = @()
foreach ($ref in $adminRefs) {
  $relative = $ref -replace '^assets/', ''
  $frontendPath = Join-Path $adminAssetsDir $relative
  $backendPath = Join-Path (Join-Path $root "backend\storage\assets") $relative
  if (-not (Test-Path $frontendPath) -and -not (Test-Path $backendPath)) {
    $missingAdminRefs += $ref
  }
}

$okProducts = $productSlugs.Count - $missingProducts.Count
$status = if (($missingProducts.Count + $missingBranding.Count + $missingPlaceholders.Count + $missingAdminRefs.Count + $missingReadmeImages.Count + $missingAssetsReadmeCapturas.Count + $missingAssetsReadmeLogo.Count) -eq 0) { "OK" } else { "WARN" }

$lines = @()
$lines += "# Auditoria de assets"
$lines += ""
$lines += "Estado general: **$status**"
$lines += "Fecha: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"
$lines += ""
$lines += "## Productos publicados / seed canonico"
$lines += ""
$lines += "- Productos con imagen dedicada: $okProducts/$($productSlugs.Count)"
if ($missingProducts.Count -eq 0) { $lines += "- OK: todos los productos del seed canonico tienen imagen fisica dedicada." }
else { $lines += "- Faltantes:"; $lines += ($missingProducts | ForEach-Object { "  - $_" }) }
$lines += ""
$lines += "## Branding"
if ($missingBranding.Count -eq 0) { $lines += "- OK: branding principal completo." }
else { $lines += "- Faltantes:"; $lines += ($missingBranding | ForEach-Object { "  - $_" }) }
$lines += ""
$lines += "## Placeholders"
if ($missingPlaceholders.Count -eq 0) { $lines += "- OK: placeholders base completos." }
else { $lines += "- Faltantes:"; $lines += ($missingPlaceholders | ForEach-Object { "  - $_" }) }
$lines += ""
$lines += "## Iconos referenciados por Angular admin"
$lines += ""
$lines += "- Referencias detectadas: $($adminRefs.Count)"
if ($missingAdminRefs.Count -eq 0) { $lines += "- OK: todos los iconos/assets referenciados por Angular existen." }
else { $lines += "- Faltantes:"; $lines += ($missingAdminRefs | ForEach-Object { "  - $_" }) }

$lines += ""
$lines += "## Imagenes README"
$lines += ""
$lines += "- Requeridas: $($requiredReadmeImages.Count)"
if ($missingReadmeImages.Count -eq 0) { $lines += "- OK: todas las laminas README existen." }
else { $lines += "- Faltantes:"; $lines += ($missingReadmeImages | ForEach-Object { "  - $_" }) }
$lines += ""
$lines += "## Assets README heredados"
$lines += ""
if ($missingAssetsReadmeCapturas.Count -eq 0) { $lines += "- OK: capturas base de assets-readme completas." }
else { $lines += "- Capturas faltantes:"; $lines += ($missingAssetsReadmeCapturas | ForEach-Object { "  - $_" }) }
if ($missingAssetsReadmeLogo.Count -eq 0) { $lines += "- OK: logo principal de assets-readme presente." }
else { $lines += "- Logo faltante:"; $lines += ($missingAssetsReadmeLogo | ForEach-Object { "  - $_" }) }

$lines | Set-Content -Path $reportPath -Encoding UTF8

$summary = [ordered]@{
  status = $status
  generatedAt = (Get-Date).ToString("s")
  products = [ordered]@{ total = $productSlugs.Count; withImage = $okProducts; missing = $missingProducts }
  branding = [ordered]@{ required = $requiredBranding; missing = $missingBranding }
  placeholders = [ordered]@{ required = $requiredPlaceholders; missing = $missingPlaceholders }
  angularAdmin = [ordered]@{ references = $adminRefs.Count; missing = $missingAdminRefs }
  readmeImages = [ordered]@{ required = $requiredReadmeImages.Count; missing = $missingReadmeImages }
  assetsReadme = [ordered]@{ capturasRequired = $requiredAssetsReadmeCapturas.Count; capturasMissing = $missingAssetsReadmeCapturas; logoMissing = $missingAssetsReadmeLogo }
  reportPath = $reportPath
}
$summary | ConvertTo-Json -Depth 6 | Set-Content -Path $jsonPath -Encoding UTF8

Write-Host "Auditoria generada: $reportPath"
Write-Host "Resumen JSON: $jsonPath"
Write-Host "Productos con imagen dedicada: $okProducts/$($productSlugs.Count)"
Write-Host "Branding faltante: $($missingBranding.Count)"
Write-Host "Placeholders faltantes: $($missingPlaceholders.Count)"
Write-Host "Assets Angular faltantes: $($missingAdminRefs.Count)"
Write-Host "Imagenes README faltantes: $($missingReadmeImages.Count)"
Write-Host "Capturas assets-readme faltantes: $($missingAssetsReadmeCapturas.Count)"
Write-Host "Logo assets-readme faltante: $($missingAssetsReadmeLogo.Count)"

if ($FailOnMissing -and $status -ne "OK") {
  throw "Hay assets faltantes. Revisa $reportPath"
}
