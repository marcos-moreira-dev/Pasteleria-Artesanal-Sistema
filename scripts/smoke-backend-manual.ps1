$ErrorActionPreference = "Stop"

$apiUrl = if ($env:PASTELERIA_API_URL) { $env:PASTELERIA_API_URL.TrimEnd('/') } else { "http://localhost:8080" }
$base = "$apiUrl/api/v1"

Write-Host "API: $base"

$health = Invoke-RestMethod -Uri "$apiUrl/actuator/health" -TimeoutSec 5
if ($health.status -ne "UP") {
  throw "Health no esta UP. Estado: $($health.status)"
}
Write-Host "[OK] health UP"

$login = Invoke-RestMethod `
  -Uri "$base/auth/login" `
  -Method Post `
  -ContentType "application/json" `
  -Body (@{ username = "admin"; password = "admin12345" } | ConvertTo-Json)

$token = $login.data.accessToken
if (-not $token) {
  throw "Login no devolvio accessToken."
}
Write-Host "[OK] login admin"

$headers = @{ Authorization = "Bearer $token"; "X-Request-Id" = "req-smoke-pasteleria-001" }

$guia = Invoke-RestMethod -Uri "$base/casos-uso/hub" -Headers $headers -TimeoutSec 10
if (-not $guia.data.modulos -or $guia.data.modulos.Count -lt 1) {
  throw "Guia operativa no devolvio modulos."
}
Write-Host "[OK] guia operativa: $($guia.data.modulos.Count) modulo(s)"

$productos = Invoke-RestMethod -Uri "$base/productos?page=0&size=5" -Headers $headers -TimeoutSec 10
Write-Host "[OK] productos consultados"

$notificaciones = Invoke-RestMethod -Uri "$base/notificaciones/resumen" -Headers $headers -TimeoutSec 10
Write-Host "[OK] notificaciones consultadas"

[pscustomobject]@{
  health = $health.status
  guiaModulos = $guia.data.modulos.Count
  api = $base
} | ConvertTo-Json -Depth 5
