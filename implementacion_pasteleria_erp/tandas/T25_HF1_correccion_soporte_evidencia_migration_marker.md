# T25-HF1 — Corrección de soporte/evidencia sobre marcadores ERP

## Contexto

Después de T25, `scripts\test-backend.bat` confirmó que el backend compilaba y que los tests avanzaban hasta el smoke de integración. El fallo apareció al consultar:

```http
GET /api/v1/soporte/evidencia
```

La causa fue una consulta SQL incorrecta en `SupportEvidenceService`:

```sql
SELECT marcador
FROM core.erp_migration_marker
ORDER BY codigo
```

La tabla `core.erp_migration_marker` no tiene columna `marcador`; su columna de identificación es `codigo`.

## Cambio aplicado

Se corrigió `SupportEvidenceService.migrationMarkers()` para consultar:

```sql
SELECT codigo
FROM core.erp_migration_marker
ORDER BY codigo
```

## Alcance

Este hotfix solo corrige el endpoint de evidencia de soporte.

No modifica:

- migraciones;
- tablas;
- endpoints públicos;
- Angular Admin;
- Astro storefront;
- UX/UI;
- módulos ERP previos.

## Validación recomendada

Ejecutar:

```bat
scripts\test-backend.bat
```

Si pasa, continuar con T26.
