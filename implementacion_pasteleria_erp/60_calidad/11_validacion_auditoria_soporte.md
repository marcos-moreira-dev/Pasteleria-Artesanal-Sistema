# Calidad — Validación de auditoría, soporte y evidencia

## Script agregado

- `db/validation/16_validate_audit_support_evidence.sql`

## Qué valida

- existe `public.auditoria_evento`;
- los eventos tienen campos obligatorios;
- existe al menos un evento semilla;
- existen permisos `AUDITORIA_VER`, `SOPORTE_VER` y `SOPORTE_GESTIONAR`.

## Smoke API

`BackendApiSmokeIntegrationTest` ahora consulta:

- `GET /api/v1/auditoria/resumen`;
- `GET /api/v1/soporte/evidencia`.

## Validación recomendada

```bat
scripts\test-backend.bat
```
