@echo off
setlocal

if not defined JAVA_HOME set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"
if not defined DB_HOST set "DB_HOST=localhost"
if not defined DB_PORT set "DB_PORT=5432"
if not defined DB_USER set "DB_USER=postgres"
if not defined DB_PASSWORD set "DB_PASSWORD=postgres"
if not defined DB_NAME set "DB_NAME=pasteleria"
if not defined JWT_SECRET set "JWT_SECRET=ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"

pushd "%~dp0.."
call ".\mvnw.cmd" spring-boot:run -Dmaven.test.skip=true
popd
