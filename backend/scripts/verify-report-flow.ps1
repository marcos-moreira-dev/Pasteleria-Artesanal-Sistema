$javaHome = "C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot"
$backendRoot = Split-Path -Parent $PSScriptRoot
$jarPath = Join-Path $backendRoot "target\pasteleria-backend-0.0.1-SNAPSHOT.jar"
$pdfPath = Join-Path $backendRoot "temp-report-check.pdf"
$logPath = Join-Path $backendRoot "temp-report-runtime.log"

if (Test-Path $pdfPath) {
  Remove-Item $pdfPath -Force
}

if (Test-Path $logPath) {
  Remove-Item $logPath -Force
}

$job = Start-Job -ScriptBlock {
  param($jh, $jp, $workdir, $runtimeLogPath)

  $env:JAVA_HOME = $jh
  $env:PATH = "$jh\bin;$env:PATH"
  $env:DB_PORT = "5434"
  $env:JWT_SECRET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"

  Set-Location $workdir
  & "$jh\bin\java.exe" -jar $jp *> $runtimeLogPath
} -ArgumentList $javaHome, $jarPath, $backendRoot, $logPath

try {
  $healthy = $false
  for ($index = 0; $index -lt 25; $index++) {
    Start-Sleep -Seconds 1
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
    throw "Backend temporal no alcanzo estado UP."
  }

  $login = Invoke-RestMethod `
    -Uri "http://localhost:8080/api/v1/auth/login" `
    -Method Post `
    -ContentType "application/json" `
    -Body (@{ username = "admin"; password = "admin12345" } | ConvertTo-Json)

  $token = $login.data.accessToken
  $headers = @{ Authorization = "Bearer $token" }

  $clients = Invoke-RestMethod `
    -Uri "http://localhost:8080/api/v1/clientes/paginado?page=0&size=2&query=sol" `
    -Headers $headers

  $created = Invoke-RestMethod `
    -Uri "http://localhost:8080/api/v1/reportes" `
    -Method Post `
    -Headers $headers `
    -ContentType "application/json" `
    -Body (@{ reportType = "RESUMEN_NEGOCIO" } | ConvertTo-Json)

  $jobId = $created.data.id
  $completed = $null

  for ($index = 0; $index -lt 24; $index++) {
    Start-Sleep -Seconds 1
    $page = Invoke-RestMethod `
      -Uri "http://localhost:8080/api/v1/reportes/paginado?page=0&size=8" `
      -Headers $headers
    $completed = $page.data.content | Where-Object { $_.id -eq $jobId } | Select-Object -First 1
    if ($null -ne $completed -and $completed.status -eq "COMPLETADO") {
      break
    }
  }

  if ($null -eq $completed) {
    throw "No se encontro el job de reporte recien creado."
  }

  if ($completed.status -ne "COMPLETADO") {
    throw "El job quedo en estado $($completed.status)."
  }

  Invoke-WebRequest `
    -Uri "http://localhost:8080/api/v1/reportes/$jobId/descargar" `
    -Headers $headers `
    -OutFile $pdfPath | Out-Null

  $fileBytes = [System.IO.File]::ReadAllBytes($pdfPath)
  $signature = [System.Text.Encoding]::ASCII.GetString($fileBytes, 0, 4)

  [pscustomobject]@{
    health = "UP"
    clientsFiltered = $clients.data.totalElements
    reportStatus = $completed.status
    fileBytes = $fileBytes.Length
    pdfSignature = $signature
    downloadedFile = (Split-Path $pdfPath -Leaf)
  } | ConvertTo-Json -Depth 5
} catch {
  if (Test-Path $logPath) {
    Write-Output "----- BACKEND LOG -----"
    Get-Content $logPath -Tail 120
    Write-Output "----- END BACKEND LOG -----"
  }
  throw
} finally {
  Stop-Job $job -ErrorAction SilentlyContinue | Out-Null
  Receive-Job $job -Keep -ErrorAction SilentlyContinue | Out-Null
  Remove-Job $job -Force -ErrorAction SilentlyContinue | Out-Null
}
