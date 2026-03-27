$backendRoot = Split-Path -Parent $PSScriptRoot
$javaHome = "C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot"
$logPath = Join-Path $backendRoot "backend-run.log"
$dbPort = if ($env:DB_PORT) { $env:DB_PORT } else { "5436" }

Set-Location $backendRoot
$env:JAVA_HOME = $javaHome
$env:PATH = "$javaHome\bin;$env:PATH"
$env:DB_PORT = $dbPort
$env:JWT_SECRET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"

if (Test-Path $logPath) {
  Remove-Item $logPath -Force
}

& ".\mvnw.cmd" spring-boot:run *> $logPath
