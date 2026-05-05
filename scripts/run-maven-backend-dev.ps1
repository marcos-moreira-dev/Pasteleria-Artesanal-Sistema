param(
  [Parameter(Mandatory = $true)][string]$BackendDir,
  [Parameter(Mandatory = $true)][string]$LogFile,
  [string]$JvmArguments = ""
)

$ErrorActionPreference = "Stop"
Set-Location $BackendDir

$env:SPRING_PROFILES_ACTIVE = if ($env:SPRING_PROFILES_ACTIVE) { $env:SPRING_PROFILES_ACTIVE } else { "dev" }
$env:DB_HOST = if ($env:DB_HOST) { $env:DB_HOST } else { "127.0.0.1" }
$env:DB_PORT = if ($env:DB_PORT) { $env:DB_PORT } else { "5436" }
$env:DB_NAME = if ($env:DB_NAME) { $env:DB_NAME } else { "pasteleria" }
$env:DB_USER = if ($env:DB_USER) { $env:DB_USER } else { "postgres" }
$env:DB_PASSWORD = if ($env:DB_PASSWORD) { $env:DB_PASSWORD } else { "postgres" }
$env:SPRING_FLYWAY_ENABLED = if ($env:SPRING_FLYWAY_ENABLED) { $env:SPRING_FLYWAY_ENABLED } else { "false" }
$env:JWT_SECRET = if ($env:JWT_SECRET) { $env:JWT_SECRET } else { "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" }

$logDir = Split-Path -Parent $LogFile
New-Item -ItemType Directory -Force -Path $logDir | Out-Null

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

$args = @("spring-boot:run", "-Dmaven.test.skip=true")
if ($JvmArguments.Trim().Length -gt 0) {
  $args += "-Dspring-boot.run.jvmArguments=$JvmArguments"
}

& $mavenExecutable @args 2>&1 | Tee-Object -FilePath $LogFile
exit $LASTEXITCODE
