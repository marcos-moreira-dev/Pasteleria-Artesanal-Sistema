# 13 - Alineación de scripts estilo Cedro Damasco

## Motivo

La tanda T10 dejó scripts funcionales, pero no replicaba de forma suficientemente cercana la organización operativa de Cedro Damasco. Se corrigió la estructura para que Pastelería use el mismo patrón mental: scripts raíz para infraestructura, arranque, validación y smoke; scripts de backend como wrappers; e infraestructura Docker dev en `infra/compose`.

## Cambios principales

- Se creó `infra/compose/docker-compose.dev.yml` con PostgreSQL dev aislado.
- Se agregaron scripts raíz equivalentes al patrón Cedro:
  - `check-dev-env.bat`
  - `up-infra-dev.bat`
  - `down-infra-dev.bat`
  - `reset-infra-dev.bat`
  - `dev-backend.bat`
  - `dev-backend-inline.bat`
  - `run-maven-backend-dev.ps1`
  - `check-backend-running.ps1`
  - `stop-backend-dev.bat`
  - `stop-backend-dev.ps1`
  - `dev-frontend.bat`
  - `validate-frontend.bat`
  - `smoke-backend-manual.bat`
  - `smoke-backend-manual.ps1`
  - `package-backend.bat`
  - `freeze-backend-checklist.bat`
- Se agregaron wrappers en `backend/scripts/` al estilo Cedro.
- Se actualizó `scripts/README.md` para explicar el flujo completo.
- `init-db.ps1` y `repair-guia-operativa-db.ps1` ahora usan `infra/compose/docker-compose.dev.yml` y el contenedor `pasteleria-postgres-dev`.

## Decisión importante

Pastelería conserva su diferencia técnica frente a Cedro: para presentación local usa SQL canónico (`db/V1`) y arranca con `SPRING_FLYWAY_ENABLED=false`. Esto evita el error de Hibernate por tablas nuevas cuando la base local viene de una tanda anterior.

## Orden recomendado

```bat
scripts\reset-infra-dev.bat
scripts\up-infra-dev.bat
scripts\dev-backend.bat
```

En otra terminal:

```bat
scripts\dev-frontend.bat
```
