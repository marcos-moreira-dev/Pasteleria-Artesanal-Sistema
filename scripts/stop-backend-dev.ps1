param(
  [int]$Port = 8080
)

$ErrorActionPreference = "SilentlyContinue"

$rootDir = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
$backendDir = Join-Path $rootDir "backend"

$processes = Get-CimInstance Win32_Process |
  Where-Object {
    $_.ProcessId -ne $PID -and (
      $_.CommandLine -like "*com.pasteleria.PasteleriaApplication*" -or
      ($_.CommandLine -like "*spring-boot:run*" -and $_.CommandLine -like "*$backendDir*") -or
      ($_.CommandLine -like "*run-maven-backend-dev.ps1*" -and $_.CommandLine -like "*$backendDir*") -or
      ($_.CommandLine -like "*dev-backend.bat*" -and $_.CommandLine -like "*$rootDir*") -or
      ($_.CommandLine -like "*dev-backend-inline.bat*" -and $_.CommandLine -like "*$rootDir*")
    )
  }

$portOwners = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue |
  Select-Object -ExpandProperty OwningProcess -Unique

if (-not $processes -and -not $portOwners) {
  Write-Host "OK - No hay backend Pasteleria corriendo en el puerto $Port."
  exit 0
}

$processes = $processes | Sort-Object ProcessId -Descending -Unique

foreach ($process in $processes) {
  Write-Host "Deteniendo proceso backend Pasteleria PID $($process.ProcessId)..."
  Stop-Process -Id $process.ProcessId -Force -ErrorAction SilentlyContinue
}

foreach ($ownerPid in $portOwners) {
  $alreadyStopped = $processes | Where-Object { $_.ProcessId -eq $ownerPid }
  if ($alreadyStopped) { continue }

  $owner = Get-CimInstance Win32_Process -Filter "ProcessId = $ownerPid"
  if ($owner.CommandLine -like "*PasteleriaApplication*" -or $owner.CommandLine -like "*pasteleria-backend*") {
    Write-Host "Deteniendo proceso que ocupa $Port PID $ownerPid..."
    Stop-Process -Id $ownerPid -Force -ErrorAction SilentlyContinue
  }
}

Start-Sleep -Seconds 2

$remaining = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue
if ($remaining) {
  Write-Host "ADVERTENCIA - El puerto $Port sigue ocupado. Revisa el proceso manualmente."
  exit 1
}

Write-Host "OK - Backend Pasteleria detenido."
