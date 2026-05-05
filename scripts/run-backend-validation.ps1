param(
  [Parameter(Mandatory = $true)][string]$BackendDir,
  [Parameter(Mandatory = $true)][string]$LogFile,
  [int]$AppPort = 8080,
  [int]$DbPort = 5436
)

$ErrorActionPreference = "Stop"
$logDir = Split-Path -Parent $LogFile
New-Item -ItemType Directory -Force -Path $logDir | Out-Null
Set-Location $BackendDir

$env:APP_PORT = "$AppPort"
$env:DB_HOST = "127.0.0.1"
$env:DB_PORT = "$DbPort"
$env:DB_NAME = "pasteleria"
$env:DB_USER = "postgres"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "postgres"
$env:SPRING_PROFILES_ACTIVE = "dev"
$env:SPRING_FLYWAY_ENABLED = "false"
$env:JWT_SECRET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"
$env:REPORT_WORKER_ENABLED = "false"

$mvnw = Join-Path $BackendDir "mvnw.cmd"
$mvnCommand = Get-Command mvn.cmd -ErrorAction SilentlyContinue
if (-not $mvnCommand) {
  $mvnCommand = Get-Command mvn -ErrorAction SilentlyContinue
}

if ($mvnCommand) {
  $mavenExecutable = $mvnCommand.Source
  Write-Host "[INFO] Maven global disponible; usando toolchain JDK 21 configurado por el proyecto."
} elseif (Test-Path $mvnw) {
  $mavenExecutable = $mvnw
  Write-Host "[INFO] Maven global no disponible; usando mvnw.cmd."
} else {
  throw "No se encontro Maven global ni mvnw.cmd en $BackendDir"
}

"== Pasteleria :: backend validation runner ==" | Tee-Object -FilePath $LogFile
"BackendDir: $BackendDir" | Tee-Object -FilePath $LogFile -Append
"APP_PORT=$($env:APP_PORT)" | Tee-Object -FilePath $LogFile -Append
"DB_PORT=$($env:DB_PORT)" | Tee-Object -FilePath $LogFile -Append
"SPRING_FLYWAY_ENABLED=$($env:SPRING_FLYWAY_ENABLED)" | Tee-Object -FilePath $LogFile -Append
"REPORT_WORKER_ENABLED=$($env:REPORT_WORKER_ENABLED)" | Tee-Object -FilePath $LogFile -Append
"" | Tee-Object -FilePath $LogFile -Append

& $mavenExecutable spring-boot:run "-Dmaven.test.skip=true" 2>&1 | Tee-Object -FilePath $LogFile -Append
exit $LASTEXITCODE
