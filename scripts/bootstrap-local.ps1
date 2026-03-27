$ErrorActionPreference = "Stop"

Write-Host "Inicializando base del proyecto..."
& (Join-Path $PSScriptRoot "init-db.ps1")

Write-Host "Servicios base listos."
Write-Host ""
Write-Host "Siguientes pasos sugeridos:"
Write-Host "1. cd backend && `$env:DB_PORT='5436'; `$env:DB_PASSWORD='postgres'; `$env:JWT_SECRET='ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ'; .\\scripts\\start-backend-dev.cmd"
Write-Host "2. cd frontend-publico-astro && npm install && npm run dev"
Write-Host "3. cd frontend-admin-angular && npm install && npm start"
