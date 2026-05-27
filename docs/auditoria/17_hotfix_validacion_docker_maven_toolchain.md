# Hotfix — validación Docker y Maven toolchain

## Contexto
Durante `scripts\validate-all.bat`, la validación se detenía en la etapa de backend antes de aplicar la base canónica.

El error no provenía del código Java ni de la compilación, sino de `scripts/init-db.ps1`: el script intentaba ejecutar `docker rm -f pasteleria-postgres` para limpiar un contenedor legacy. Cuando dicho contenedor no existía, Docker devolvía un mensaje en stderr y PowerShell lo trataba como error fatal.

## Correcciones aplicadas

### `scripts/init-db.ps1`
- Se reemplazó el borrado directo de contenedor legacy por `Remove-ContainerIfExists`.
- El contenedor `pasteleria-postgres` solo se elimina si realmente existe.
- Se conserva el contenedor actual del proyecto: `pasteleria-postgres-dev`.
- La espera de PostgreSQL se volvió más tolerante durante el arranque.

### `scripts/repair-guia-operativa-db.ps1`
- Se aplicó el mismo patrón defensivo de limpieza de contenedor legacy.

### `scripts/up-infra-dev.bat` y `scripts/reset-infra-dev.bat`
- La limpieza de contenedores legacy ahora verifica existencia antes de intentar eliminarlos.

### Maven / JDK 21
- `scripts/validate-backend.bat` ahora prefiere Maven global si existe.
- Si Maven global no existe, usa `backend\mvnw.cmd`.
- El proyecto mantiene el uso del Maven Toolchains Plugin para compilar con JDK 21 Temurin cuando está configurado.

## Resultado esperado
El error:

```text
Error response from daemon: No such container: pasteleria-postgres
```

ya no debe detener la validación.
