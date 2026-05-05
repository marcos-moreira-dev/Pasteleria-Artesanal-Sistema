# Scripts raíz

Scripts operativos mínimos para desarrollo local de Pastelería. La carpeta queda alineada con el patrón usado en Cedro Damasco: infraestructura en `scripts/`, wrappers de backend en `backend/scripts/` y logs locales fuera del versionado.

## Orden recomendado

```bat
scripts\check-dev-env.bat
scripts\up-infra-dev.bat
scripts\validate-all.bat
```

Para arrancar el backend en modo desarrollo en una consola visible:

```bat
scripts\dev-backend.bat
```

Para probar manualmente el backend ya levantado:

```bat
scripts\smoke-backend-manual.bat
```



## Validación integral funcional

Después de cambios grandes, usa este comando desde la raíz:

```bat
scripts\validate-system-full.bat
```

Este script genera un log único en `.diagnostics/logs/` y valida entorno, PostgreSQL, SQL canónico, datos mínimos, empaquetado del backend, arranque real, login JWT, endpoints públicos, endpoints administrativos principales, guía operativa/casos de uso, build Angular, build Astro y assets.

Opciones útiles:

```bat
scripts\validate-system-full.bat -ResetDatabase
scripts\validate-system-full.bat -SkipFrontendBuild
scripts\validate-system-full.bat -KeepBackendRunning
```

Si falla, copia el archivo `validate-system-full_YYYYMMDD_HHMMSS.log` y pásalo completo.

## Scripts estilo Cedro

| Script | Uso |
|---|---|
| `check-dev-env.bat` | Verifica JDK, Docker, Docker Compose, Node, npm y archivos canónicos mínimos. |
| `up-infra-dev.bat` | Levanta PostgreSQL local con Docker Compose y espera `pg_isready`. |
| `down-infra-dev.bat` | Detiene PostgreSQL local. |
| `reset-infra-dev.bat` | Elimina contenedor/red/volumen local de PostgreSQL dev. |
| `dev-backend.bat` | Abre una consola visible para el backend dev. |
| `dev-backend-inline.bat` | Script interno: levanta infraestructura, inicializa DB canónica y ejecuta Spring Boot. |
| `stop-backend-dev.bat` | Detiene el proceso Java local de Pastelería en `localhost:8080`. |
| `validate-all.bat` | Ejecuta validación por etapas: entorno, backend, Angular, Astro y assets. |
| `validate-system-full.bat` | Suite integral funcional: levanta infraestructura, valida DB, arranca backend, prueba endpoints principales, guía operativa, Angular, Astro y assets. |
| `validate-functional-suite.bat` | Alias compatible de la suite integral funcional. |
| `validate-frontend.bat` | Alias compatible estilo Cedro; valida el Angular admin. |
| `smoke-backend-manual.bat` | Prueba endpoints reales contra un backend ya corriendo en `localhost:8080`. |
| `package-backend.bat` | Empaqueta el backend usando `backend\mvnw.cmd`. |
| `freeze-backend-checklist.bat` | Checklist manual de cierre backend. |

## Scripts específicos de Pastelería

| Script | Uso |
|---|---|
| `init-db.ps1` | Inicializa la base `pasteleria` con SQL canónico. Si falta la guía operativa, aplica el parche V13 sin destruir datos. |
| `reset-db-local.ps1` | Recrea la base canónica completa. |
| `repair-guia-operativa-db.bat` | Aplica solo `V13__guia_operativa.sql` sobre una base existente. |
| `validate-admin-angular.bat` | Build del admin Angular. |
| `validate-public-astro.bat` | Build del frontend público Astro. |
| `audit-assets.bat` | Auditoría de assets físicos del negocio, iconos Angular e imágenes README. |
| `validate-readme-assets.bat` | Verifica que existan las láminas/capturas usadas por el README y `assets-readme`. |
| `dev-publico-astro.bat` | Levanta Astro público en desarrollo. |

## Logs

Los scripts de arranque estilo Cedro guardan logs en:

```text
.pasteleria-dev/logs/
```

Los scripts de validación heredados guardan logs en:

```text
.diagnostics/logs/
```

Ambas carpetas son locales y no deben versionarse.

## Infraestructura Docker local

La infraestructura dev vive en:

```text
infra/compose/docker-compose.dev.yml
```

Usa el project name fijo `pasteleria_dev` y el contenedor `pasteleria-postgres-dev` para evitar confusiones con otros proyectos. PostgreSQL dev se publica en:

```text
localhost:5436
```

El backend dev corre en:

```text
http://localhost:8080
```

## Importante sobre Flyway y SQL canónico

Para presentación local, Pastelería usa SQL canónico en `db/V1/` y arranca con:

```text
SPRING_FLYWAY_ENABLED=false
```

Las migraciones Flyway quedan como referencia técnica/evolutiva, pero el arranque local estabilizado no debe mezclar una base creada con SQL canónico y Flyway activo.
