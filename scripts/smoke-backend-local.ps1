param(
  [string]$BaseUrl = "http://localhost:8080",
  [string]$Username = "admin",
  [string]$Password = "admin12345"
)

$ErrorActionPreference = "Stop"
$apiUrl = "$BaseUrl/api/v1"

Write-Host "== Pasteleria :: smoke backend local =="
Write-Host "API: $apiUrl"
Write-Host ""

$health = Invoke-RestMethod -Uri "$BaseUrl/actuator/health" -TimeoutSec 5
if ($health.status -ne "UP") {
  throw "Backend no esta UP. Estado: $($health.status)"
}

$login = Invoke-RestMethod `
  -Uri "$apiUrl/auth/login" `
  -Method Post `
  -ContentType "application/json" `
  -Body (@{ username = $Username; password = $Password } | ConvertTo-Json)

$token = $login.data.accessToken
if (-not $token) {
  throw "Login no devolvio token."
}

$headers = @{ Authorization = "Bearer $token" }

$productos = Invoke-RestMethod -Uri "$apiUrl/productos" -Headers $headers -TimeoutSec 10
$pedidos = Invoke-RestMethod -Uri "$apiUrl/pedidos/paginado?page=0&size=5" -Headers $headers -TimeoutSec 10
$guia = Invoke-RestMethod -Uri "$apiUrl/casos-uso/hub" -Headers $headers -TimeoutSec 10
$publicBranding = Invoke-RestMethod -Uri "$apiUrl/public/catalogo/branding" -TimeoutSec 10

$result = [pscustomobject]@{
  health = $health.status
  user = $login.data.username
  role = $login.data.role
  products = ($productos.data | Measure-Object).Count
  ordersPageElements = $pedidos.data.numberOfElements
  guideAreas = ($guia.data.modulos | Measure-Object).Count
  guideCases = $guia.data.totalCasos
  logoSquarePath = $publicBranding.data.logoSquarePath
}

$result | ConvertTo-Json -Depth 5
