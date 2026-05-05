param(
  [string]$LogFile,
  [switch]$ResetDatabase,
  [switch]$SkipFrontendBuild,
  [switch]$SkipBackendPackage,
  [switch]$KeepBackendRunning,
  [int]$BackendStartupTimeoutSeconds = 150,
  [string]$BaseUrl = "http://localhost:8080",
  [string]$Username = "admin",
  [string]$Password = "admin12345"
)

$ErrorActionPreference = "Stop"
$RootDir = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
if (-not $LogFile) {
  $timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
  $LogFile = Join-Path $RootDir ".diagnostics\logs\validate-system-full_$timestamp.log"
}
$LogDir = Split-Path -Parent $LogFile
New-Item -ItemType Directory -Force -Path $LogDir | Out-Null
$BackendLog = Join-Path $LogDir ("backend-validation_" + (Get-Date -Format "yyyyMMdd_HHmmss") + ".log")
$Summary = [ordered]@{
  startedAt = (Get-Date).ToString("s"); rootDir = $RootDir; baseUrl = $BaseUrl;
  logFile = $LogFile; backendLog = $BackendLog; resetDatabase = [bool]$ResetDatabase;
  skipFrontendBuild = [bool]$SkipFrontendBuild; skipBackendPackage = [bool]$SkipBackendPackage;
  checks = @()
}
function Write-Log { param([string]$Message = "") Write-Host $Message; Add-Content -Path $LogFile -Value $Message -Encoding UTF8 }
function Add-Check { param([string]$Name,[string]$Status,[string]$Detail="") $script:Summary.checks += [ordered]@{name=$Name;status=$Status;detail=$Detail} }
function Run-StepCommand {
  param([Parameter(Mandatory=$true)][string]$Name,[Parameter(Mandatory=$true)][string]$Command)
  Write-Log ""; Write-Log "== $Name =="; Write-Log "> $Command"; Push-Location $RootDir
  try { cmd.exe /c $Command 2>&1 | Tee-Object -FilePath $LogFile -Append; $exit=$LASTEXITCODE; if ($exit -ne 0) { Add-Check $Name "FAIL" "exit code $exit"; throw "$Name fallo con exit code $exit" }; Add-Check $Name "OK" "exit code 0" }
  finally { Pop-Location }
}
function Invoke-PsqlScalar { param([Parameter(Mandatory=$true)][string]$Sql) $output=& docker exec pasteleria-postgres-dev psql -U postgres -d pasteleria -t -A -c $Sql 2>&1; if ($LASTEXITCODE -ne 0) { throw "psql fallo: $output" }; return ($output | Out-String).Trim() }
function Assert-DbTableExists { param([Parameter(Mandatory=$true)][string]$TableName) $exists=Invoke-PsqlScalar "SELECT CASE WHEN to_regclass('public.$TableName') IS NULL THEN 'NO' ELSE 'YES' END;"; if ($exists -ne "YES") { throw "Falta la tabla $TableName" }; Write-Log "[OK] Tabla presente: $TableName" }
function Assert-DbMinCount { param([Parameter(Mandatory=$true)][string]$TableName,[Parameter(Mandatory=$true)][int]$MinCount,[string]$Where="TRUE") $countText=Invoke-PsqlScalar "SELECT COUNT(*) FROM $TableName WHERE $Where;"; $count=[int]$countText; if ($count -lt $MinCount) { throw "La tabla $TableName esperaba al menos $MinCount registros y tiene $count." }; Write-Log "[OK] ${TableName}: $count registros" }
function Test-BackendIsPasteleria { try { $health=Invoke-RestMethod -Uri "$BaseUrl/api/v1/public/health" -TimeoutSec 3; return ($health.success -eq $true -and $health.data.status -eq "UP") } catch { return $false } }
function Wait-ForBackend { param([int]$TimeoutSeconds) Write-Log "Esperando backend en $BaseUrl ..."; $deadline=(Get-Date).AddSeconds($TimeoutSeconds); do { if (Test-BackendIsPasteleria) { Write-Log "[OK] Backend disponible."; return }; Start-Sleep -Seconds 3 } while ((Get-Date) -lt $deadline); throw "Backend no respondio en $TimeoutSeconds segundos. Revisa $BackendLog" }
function Assert-ApiResponse { param([Parameter(Mandatory=$true)]$Response,[Parameter(Mandatory=$true)][string]$Name) if ($null -eq $Response) { throw "$Name no devolvio respuesta." }; if ($Response.PSObject.Properties.Name -contains "success") { if ($Response.success -ne $true) { throw "$Name devolvio success=false. Mensaje: $($Response.message) Error: $($Response.errorCode)" } } }
function Get-PayloadCount { param($Data) if ($null -eq $Data) { return 0 }; if ($Data -is [System.Array]) { return @($Data).Count }; if ($Data.PSObject.Properties.Name -contains "content") { return @($Data.content).Count }; if ($Data.PSObject.Properties.Name -contains "items") { return @($Data.items).Count }; if ($Data.PSObject.Properties.Name -contains "modulos") { return @($Data.modulos).Count }; return 1 }
function Invoke-ApiGetCheck {
  param([Parameter(Mandatory=$true)][string]$Name,[Parameter(Mandatory=$true)][string]$Path,[hashtable]$Headers=@{},[Nullable[int]]$MinCount=$null)
  $url = if ($Path.StartsWith("http")) { $Path } else { "$BaseUrl/api/v1$Path" }
  Write-Log "Probando GET $Path"
  $response=Invoke-RestMethod -Uri $url -Headers $Headers -TimeoutSec 20
  Assert-ApiResponse -Response $response -Name $Name
  if ($null -ne $MinCount) { $count=Get-PayloadCount $response.data; if ($count -lt $MinCount) { throw "$Name esperaba al menos $MinCount elementos y devolvio $count." }; Write-Log "[OK] $Name -> $count elementos" } else { Write-Log "[OK] $Name" }
  Add-Check $Name "OK" $Path
  return $response
}
$backendProcess=$null; $backendStartedBySuite=$false
try {
  Set-Location $RootDir; Write-Log "== Pasteleria :: suite integral funcional =="; Write-Log "Inicio: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"; Write-Log "Raiz: $RootDir"; Write-Log "Base URL: $BaseUrl"; Write-Log "Log backend: $BackendLog"; Write-Log ""
  Run-StepCommand -Name "Entorno dev" -Command "scripts\check-dev-env.bat"
  Run-StepCommand -Name "Infraestructura PostgreSQL dev" -Command "scripts\up-infra-dev.bat"
  if ($ResetDatabase) { Write-Log ""; Write-Log "== Base de datos canonica: reset completo =="; powershell -NoProfile -ExecutionPolicy Bypass -File (Join-Path $RootDir "scripts\reset-db-local.ps1") 2>&1 | Tee-Object -FilePath $LogFile -Append; if ($LASTEXITCODE -ne 0) { throw "reset-db-local.ps1 fallo." }; Add-Check "Reset DB canonica" "OK" "Base recreada" }
  else { Write-Log ""; Write-Log "== Base de datos canonica: inicializacion no destructiva =="; powershell -NoProfile -ExecutionPolicy Bypass -File (Join-Path $RootDir "scripts\init-db.ps1") 2>&1 | Tee-Object -FilePath $LogFile -Append; if ($LASTEXITCODE -ne 0) { throw "init-db.ps1 fallo." }; powershell -NoProfile -ExecutionPolicy Bypass -File (Join-Path $RootDir "scripts\repair-guia-operativa-db.ps1") 2>&1 | Tee-Object -FilePath $LogFile -Append; if ($LASTEXITCODE -ne 0) { throw "repair-guia-operativa-db.ps1 fallo." }; Add-Check "DB canonica no destructiva" "OK" "Init + repair guia operativa" }
  Write-Log ""; Write-Log "== Verificacion directa de base de datos =="
  $tables=@("rol_usuario","usuario_sistema","categoria_producto","producto","cliente","pedido","produccion","cotizacion","notificacion","proveedor","ingrediente","insumo","umedida","receta","inventario_movimiento","caso_uso_modulo","caso_uso_operativo","paso_caso_uso")
  foreach($table in $tables){ Assert-DbTableExists $table }
  Assert-DbMinCount "usuario_sistema" 3; Assert-DbMinCount "producto" 8 "activo = TRUE"; Assert-DbMinCount "cliente" 3; Assert-DbMinCount "pedido" 3; Assert-DbMinCount "produccion" 2; Assert-DbMinCount "cotizacion" 2; Assert-DbMinCount "notificacion" 3; Assert-DbMinCount "proveedor" 3; Assert-DbMinCount "ingrediente" 5; Assert-DbMinCount "insumo" 3; Assert-DbMinCount "receta" 3; Assert-DbMinCount "inventario_movimiento" 5; Assert-DbMinCount "caso_uso_modulo" 10; Assert-DbMinCount "caso_uso_operativo" 14 "activo = TRUE"; Assert-DbMinCount "paso_caso_uso" 40
  Add-Check "DB tablas y seeds" "OK" "Tablas principales y datos minimos presentes"
  if (-not $SkipBackendPackage) {
    Run-StepCommand -Name "Backend package" -Command "cd backend && (where mvn >nul 2>nul && mvn -DskipTests package || mvnw.cmd -DskipTests package)"
  } else { Add-Check "Backend package" "SKIP" "Omitido por parametro" }
  if (Test-BackendIsPasteleria) { Write-Log ""; Write-Log "== Backend =="; Write-Log "[OK] Ya habia un backend de Pasteleria corriendo en $BaseUrl"; Add-Check "Backend running" "OK" "Ya estaba levantado" }
  else { Write-Log ""; Write-Log "== Levantando backend para pruebas funcionales =="; $runner=Join-Path $RootDir "scripts\run-backend-validation.ps1"; $backendDir=Join-Path $RootDir "backend"; $backendProcess=Start-Process powershell.exe -PassThru -WindowStyle Minimized -ArgumentList @("-NoProfile","-ExecutionPolicy","Bypass","-File",$runner,"-BackendDir",$backendDir,"-LogFile",$BackendLog,"-AppPort","8080","-DbPort","5436"); $backendStartedBySuite=$true; Write-Log "Proceso backend PID: $($backendProcess.Id)"; Add-Check "Backend start" "OK" "PID $($backendProcess.Id)"; Wait-ForBackend -TimeoutSeconds $BackendStartupTimeoutSeconds }
  Write-Log ""; Write-Log "== Pruebas API publicas =="; Invoke-ApiGetCheck -Name "Health publico" -Path "$BaseUrl/api/v1/public/health"; Invoke-ApiGetCheck -Name "Catalogo publico - branding" -Path "/public/catalogo/branding"; Invoke-ApiGetCheck -Name "Catalogo publico - categorias" -Path "/public/catalogo/categorias" -MinCount 1; Invoke-ApiGetCheck -Name "Catalogo publico - productos" -Path "/public/catalogo/productos" -MinCount 1
  Write-Log ""; Write-Log "== Login administrativo =="; $loginBody=@{username=$Username;password=$Password}|ConvertTo-Json; $login=Invoke-RestMethod -Uri "$BaseUrl/api/v1/auth/login" -Method Post -ContentType "application/json" -Body $loginBody -TimeoutSec 20; Assert-ApiResponse -Response $login -Name "Login administrativo"; $token=$login.data.accessToken; if(-not $token){throw "Login no devolvio accessToken."}; $headers=@{Authorization="Bearer $token"}; Write-Log "[OK] Login como $($login.data.username) / rol $($login.data.role)"; Add-Check "Login administrativo" "OK" "$($login.data.username) / $($login.data.role)"
  Write-Log ""; Write-Log "== Pruebas API administrativas de lectura =="
  $readChecks=@(
    @{Name="Productos lista";Path="/productos";Min=1},@{Name="Productos paginado";Path="/productos/paginado?page=0&size=5";Min=1},@{Name="Clientes lista";Path="/clientes";Min=1},@{Name="Clientes paginado";Path="/clientes/paginado?page=0&size=5";Min=1},@{Name="Pedidos lista";Path="/pedidos";Min=1},@{Name="Pedidos paginado";Path="/pedidos/paginado?page=0&size=5";Min=1},@{Name="Produccion lista";Path="/produccion";Min=1},@{Name="Produccion paginado";Path="/produccion/paginado?page=0&size=5";Min=1},@{Name="Cotizaciones lista";Path="/cotizaciones";Min=1},@{Name="Cotizaciones paginado";Path="/cotizaciones/paginado?page=0&size=5";Min=1},@{Name="Reportes lista";Path="/reportes";Min=1},@{Name="Reportes paginado";Path="/reportes/paginado?page=0&size=5";Min=1},@{Name="Notificaciones lista";Path="/notificaciones?limit=5";Min=1},@{Name="Notificaciones resumen";Path="/notificaciones/resumen";Min=$null},@{Name="Abastecimiento dashboard";Path="/abastecimiento/dashboard";Min=$null},@{Name="Proveedores lista";Path="/abastecimiento/proveedores";Min=1},@{Name="Proveedores paginado";Path="/abastecimiento/proveedores/paginado?page=0&size=5";Min=1},@{Name="Ingredientes lista";Path="/abastecimiento/ingredientes";Min=1},@{Name="Ingredientes paginado";Path="/abastecimiento/ingredientes/paginado?page=0&size=5";Min=1},@{Name="Insumos lista";Path="/abastecimiento/insumos";Min=1},@{Name="Insumos paginado";Path="/abastecimiento/insumos/paginado?page=0&size=5";Min=1},@{Name="Unidades medida lista";Path="/abastecimiento/unidades-medida";Min=1},@{Name="Recetas lista";Path="/abastecimiento/recetas";Min=1},@{Name="Recetas paginado";Path="/abastecimiento/recetas/paginado?page=0&size=5";Min=1},@{Name="Inventario movimientos";Path="/abastecimiento/inventario";Min=1},@{Name="Ordenes compra paginado";Path="/abastecimiento/ordenes-compra/paginado?page=0&size=5";Min=1}
  )
  foreach($check in $readChecks){ if($null -eq $check.Min){ Invoke-ApiGetCheck -Name $check.Name -Path $check.Path -Headers $headers } else { Invoke-ApiGetCheck -Name $check.Name -Path $check.Path -Headers $headers -MinCount ([int]$check.Min) } }
  Write-Log ""; Write-Log "== Pruebas especificas de Guia operativa / casos de uso =="; $hub=Invoke-ApiGetCheck -Name "Guia operativa hub" -Path "/casos-uso/hub" -Headers $headers; if([int]$hub.data.totalCasos -lt 14){throw "Guia operativa esperaba al menos 14 casos y devolvio $($hub.data.totalCasos)."}; if(@($hub.data.modulos).Count -lt 10){throw "Guia operativa esperaba al menos 10 modulos y devolvio $(@($hub.data.modulos).Count)."}; Write-Log "[OK] Guia hub: $($hub.data.totalCasos) casos / $(@($hub.data.modulos).Count) modulos"; Invoke-ApiGetCheck -Name "Guia operativa lista" -Path "/casos-uso" -Headers $headers -MinCount 14; Invoke-ApiGetCheck -Name "Guia operativa modulo PEDIDOS" -Path "/casos-uso?modulo=PEDIDOS" -Headers $headers -MinCount 1; $case001=Invoke-ApiGetCheck -Name "Guia operativa CU-GO-001" -Path "/casos-uso/CU-GO-001" -Headers $headers; if(@($case001.data.pasos).Count -lt 3){throw "CU-GO-001 tiene menos de 3 pasos."}; $case014=Invoke-ApiGetCheck -Name "Guia operativa CU-GO-014" -Path "/casos-uso/CU-GO-014" -Headers $headers; if(@($case014.data.pasos).Count -lt 3){throw "CU-GO-014 tiene menos de 3 pasos."}; Add-Check "Guia operativa consistencia" "OK" "Hub, filtros y detalle con pasos"
  if (-not $SkipFrontendBuild) { Run-StepCommand -Name "Angular admin build" -Command "scripts\validate-admin-angular.bat"; Run-StepCommand -Name "Astro publico build" -Command "scripts\validate-public-astro.bat"; Run-StepCommand -Name "Auditoria de assets" -Command "scripts\audit-assets.bat" } else { Add-Check "Frontend builds" "SKIP" "Omitido por parametro" }
  $Summary.finishedAt=(Get-Date).ToString("s"); $Summary.status="OK"; $Summary.totalChecks=$Summary.checks.Count; $summaryPath=Join-Path $LogDir ("validate-system-full_summary_"+(Get-Date -Format "yyyyMMdd_HHmmss")+".json"); ($Summary|ConvertTo-Json -Depth 8)|Set-Content -Path $summaryPath -Encoding UTF8; Write-Log ""; Write-Log "== RESUMEN =="; Write-Log "OK - Suite integral funcional completada."; Write-Log "Checks registrados: $($Summary.checks.Count)"; Write-Log "Resumen JSON: $summaryPath"; Write-Log "Log backend: $BackendLog"; exit 0
} catch { $Summary.finishedAt=(Get-Date).ToString("s"); $Summary.status="FAIL"; $Summary.error=$_.Exception.Message; $Summary.totalChecks=$Summary.checks.Count; $summaryPath=Join-Path $LogDir ("validate-system-full_summary_FAIL_"+(Get-Date -Format "yyyyMMdd_HHmmss")+".json"); ($Summary|ConvertTo-Json -Depth 8)|Set-Content -Path $summaryPath -Encoding UTF8; Write-Log ""; Write-Log "== ERROR =="; Write-Log $_.Exception.Message; Write-Log "Resumen JSON: $summaryPath"; Write-Log "Log backend: $BackendLog"; exit 1 }
finally { if($backendStartedBySuite -and -not $KeepBackendRunning){ Write-Log ""; Write-Log "== Limpieza backend de validacion =="; try{ if($backendProcess -and -not $backendProcess.HasExited){ Stop-Process -Id $backendProcess.Id -Force -ErrorAction SilentlyContinue; Write-Log "Backend de validacion detenido: PID $($backendProcess.Id)" } } catch { Write-Log "No se pudo detener el backend de validacion automaticamente: $($_.Exception.Message)" } } }
