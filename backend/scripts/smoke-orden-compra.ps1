$ErrorActionPreference = "Stop"

$backendRoot = Split-Path -Parent $PSScriptRoot
$jarPath = Join-Path $backendRoot "target\pasteleria-backend-0.0.1-SNAPSHOT.jar"
$outLog = Join-Path $backendRoot "smoke-orden-compra.out.log"
$errLog = Join-Path $backendRoot "smoke-orden-compra.err.log"
$javaHome = "C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot"

if (-not (Test-Path $jarPath)) {
  throw "No se encontro el JAR del backend. Ejecuta '.\mvnw.cmd -DskipTests package' primero."
}

if (Test-Path $outLog) {
  Remove-Item $outLog -Force
}

if (Test-Path $errLog) {
  Remove-Item $errLog -Force
}

if (-not $env:DB_HOST) { $env:DB_HOST = "localhost" }
if (-not $env:DB_PORT) { $env:DB_PORT = "5436" }
if (-not $env:DB_USER) { $env:DB_USER = "postgres" }
if (-not $env:DB_PASSWORD) { $env:DB_PASSWORD = "postgres" }
if (-not $env:DB_NAME) { $env:DB_NAME = "pasteleria" }
if (-not $env:JWT_SECRET) { $env:JWT_SECRET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" }
if (-not $env:SPRING_FLYWAY_ENABLED) { $env:SPRING_FLYWAY_ENABLED = "false" }

$proc = Start-Process "$javaHome\bin\java.exe" `
  -ArgumentList "-jar `"$jarPath`"" `
  -WorkingDirectory $backendRoot `
  -RedirectStandardOutput $outLog `
  -RedirectStandardError $errLog `
  -PassThru

try {
  $healthy = $false
  for ($index = 0; $index -lt 90; $index++) {
    Start-Sleep -Seconds 2
    try {
      $health = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -TimeoutSec 2
      if ($health.status -eq "UP") {
        $healthy = $true
        break
      }
    } catch {
    }
  }

  if (-not $healthy) {
    if (Test-Path $outLog) {
      Get-Content $outLog -Tail 80
    }
    if (Test-Path $errLog) {
      Get-Content $errLog -Tail 80
    }
    throw "Backend no levanto en tiempo esperado."
  }

  $codigo = "OC-2026-" + (Get-Date -Format "HHmmss")
  $login = Invoke-RestMethod `
    -Uri "http://localhost:8080/api/v1/auth/login" `
    -Method Post `
    -ContentType "application/json" `
    -Body (@{ username = "admin"; password = "admin12345" } | ConvertTo-Json)

  $token = $login.data.accessToken
  $headers = @{
    Authorization = "Bearer $token"
    "X-Request-Id" = "req-smoke-oc-001"
  }

  $createBody = @{
    proveedorId = 1
    codigo = $codigo
    observaciones = "Smoke de compras"
    fechaEntregaEstimada = "2026-03-31"
    detalles = @(
      @{ itemTipo = "INGREDIENTE"; itemId = 1; cantidad = 10; precioUnitario = 1.50 },
      @{ itemTipo = "INSUMO"; itemId = 1; cantidad = 4; precioUnitario = 0.75 }
    )
  } | ConvertTo-Json -Depth 6

  $created = Invoke-RestMethod `
    -Uri "http://localhost:8080/api/v1/abastecimiento/ordenes-compra" `
    -Method Post `
    -Headers $headers `
    -ContentType "application/json" `
    -Body $createBody

  $ordenId = $created.data.id

  $sent = Invoke-RestMethod `
    -Uri "http://localhost:8080/api/v1/abastecimiento/ordenes-compra/$ordenId/estado" `
    -Method Patch `
    -Headers $headers `
    -ContentType "application/json" `
    -Body (@{ estado = "ENVIADA"; observaciones = "Enviada en smoke test" } | ConvertTo-Json)

  $detail = Invoke-RestMethod `
    -Uri "http://localhost:8080/api/v1/abastecimiento/ordenes-compra/$ordenId" `
    -Headers $headers

  $detalleIngrediente = $detail.data.detalles | Where-Object { $_.itemTipo -eq "INGREDIENTE" } | Select-Object -First 1
  $detalleInsumo = $detail.data.detalles | Where-Object { $_.itemTipo -eq "INSUMO" } | Select-Object -First 1

  $partialBody = @{
    items = @(
      @{ detalleId = $detalleIngrediente.id; cantidadRecibida = 3 },
      @{ detalleId = $detalleInsumo.id; cantidadRecibida = 2 }
    )
  } | ConvertTo-Json -Depth 6

  $partial = Invoke-RestMethod `
    -Uri "http://localhost:8080/api/v1/abastecimiento/ordenes-compra/$ordenId/recibir" `
    -Method Post `
    -Headers $headers `
    -ContentType "application/json" `
    -Body $partialBody

  $finalBody = @{
    items = @(
      @{ detalleId = $detalleIngrediente.id; cantidadRecibida = 7 },
      @{ detalleId = $detalleInsumo.id; cantidadRecibida = 2 }
    )
  } | ConvertTo-Json -Depth 6

  $final = Invoke-RestMethod `
    -Uri "http://localhost:8080/api/v1/abastecimiento/ordenes-compra/$ordenId/recibir" `
    -Method Post `
    -Headers $headers `
    -ContentType "application/json" `
    -Body $finalBody

  $movs = Invoke-RestMethod `
    -Uri "http://localhost:8080/api/v1/abastecimiento/inventario/referencia/ORDEN_COMPRA/$codigo" `
    -Headers $headers

  [pscustomobject]@{
    code = $codigo
    createdStatus = $created.data.estado
    createdId = $ordenId
    sentStatus = $sent.data.estado
    partialStatus = $partial.data.estado
    finalStatus = $final.data.estado
    ingredientReceived = (($final.data.detalles | Where-Object { $_.itemTipo -eq "INGREDIENTE" } | Select-Object -First 1).cantidadRecibida)
    insumoReceived = (($final.data.detalles | Where-Object { $_.itemTipo -eq "INSUMO" } | Select-Object -First 1).cantidadRecibida)
    movementCount = ($movs.data | Measure-Object).Count
  } | ConvertTo-Json -Compress
} finally {
  if ($proc -and -not $proc.HasExited) {
    Stop-Process -Id $proc.Id -Force -ErrorAction SilentlyContinue
  }
}
