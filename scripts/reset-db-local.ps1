param(
  [string]$DatabaseName = "pasteleria"
)

$ErrorActionPreference = "Stop"
$scriptDir = $PSScriptRoot
$initScript = Join-Path $scriptDir "init-db.ps1"

Write-Host "== Pasteleria :: reset DB local =="
Write-Host "Base: $DatabaseName"
Write-Host "Modo: SQL canonico completo"
Write-Host ""

& $initScript -ForceReset -DatabaseName $DatabaseName

Write-Host ""
Write-Host "OK - Base local recreada con SQL canonico."
Write-Host "Nota: para arrancar el backend sobre esta base usa SPRING_FLYWAY_ENABLED=false."
