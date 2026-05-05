param(
  [int]$Port = 8080
)

$ErrorActionPreference = "SilentlyContinue"

try {
  $health = Invoke-RestMethod -Uri "http://localhost:$Port/actuator/health" -TimeoutSec 2
  if ($health.status -eq "UP") {
    exit 0
  }
} catch {
}

$listener = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue | Select-Object -First 1
if ($listener) {
  exit 2
}

exit 1
