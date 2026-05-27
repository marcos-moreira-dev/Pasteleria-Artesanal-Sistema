@echo off
setlocal EnableExtensions
cd /d "%~dp0.."
echo == Pasteleria Backend :: package ==
call .\mvnw.cmd clean package -DskipTests=false
endlocal
