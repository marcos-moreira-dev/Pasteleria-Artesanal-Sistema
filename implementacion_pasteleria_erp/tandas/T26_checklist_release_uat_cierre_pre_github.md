# T26 — Checklist release, UAT y cierre pre-GitHub

## Objetivo

Cerrar la fase de repotenciación ERP con una estructura operativa más limpia, documentación de cierre y scripts finales humanos para pruebas y arranque local.

## Cambios realizados

1. Se simplificó la carpeta `scripts/` para contener únicamente archivos `.bat`.
2. Se agregaron puntos de entrada claros para tests:
   - `test-backend.bat`
   - `test-admin.bat`
   - `test-storefront.bat`
   - `test-all.bat`
3. Se agregaron puntos de entrada claros para ejecución:
   - `run-production.bat`
   - `run-demo.bat`
   - `stop-local.bat`
4. La lógica PowerShell auxiliar se movió fuera de `scripts/`, a `tools/powershell/`.
5. Se documentó el cierre pre-GitHub y la validación UAT mínima.

## Decisión importante

`run-production.bat` se interpreta como modo operativo local sin datos inventados, no como despliegue real productivo.

`run-demo.bat` recrea la base local y carga datos inventados de presentación/SIT.

## Qué no se hizo

No se modificó lógica de negocio, base de datos, endpoints productivos, UX/UI ni storefront.
