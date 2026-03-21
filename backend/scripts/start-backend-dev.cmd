@echo off
setlocal

set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"
set "DB_HOST=localhost"
set "DB_PORT=5432"
set "DB_USER=postgres"
set "DB_PASSWORD=SoyResponsableX8bmBAR"
set "DB_NAME=pasteleria"
set "JWT_SECRET=ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"

pushd "%~dp0.."
call ".\mvnw.cmd" spring-boot:run -Dmaven.test.skip=true
popd
