# T25 — Auditoría, soporte y evidencia

## Objetivo

Cerrar la capa de trazabilidad y soporte operativo antes del release final, dejando consultas claras para eventos auditables, evidencia técnica y checklist de entrega.

## Alcance

T25 agrega:

- consulta de eventos de auditoría;
- resumen de auditoría;
- evidencia técnica-operativa del sistema;
- checklist de soporte y entrega;
- validación SQL de auditoría/soporte;
- cobertura en smoke API;
- contratos API documentados.

## Cambios principales

### Auditoría

Se agregó el módulo:

- `backend/src/main/java/com/pasteleria/auditoria/`

Endpoints:

- `GET /api/v1/auditoria/resumen`
- `GET /api/v1/auditoria/eventos`

Estos endpoints son de solo lectura. No crean eventos ni modifican datos.

### Soporte y evidencia

Se agregó el módulo:

- `backend/src/main/java/com/pasteleria/soporte/`

Endpoints:

- `GET /api/v1/soporte/evidencia`
- `GET /api/v1/soporte/checklist`

La evidencia incluye contadores operativos, marcadores de migración, scripts de validación esperados, endpoints críticos y estado del storage local.

### Validación

Se agregó:

- `db/validation/16_validate_audit_support_evidence.sql`

Valida estructura mínima de auditoría, eventos semilla y permisos de auditoría/soporte.

## Fuera de alcance

T25 no implementa:

- mesa de ayuda completa;
- tickets de soporte persistentes;
- sistema de monitoreo externo;
- alertas automáticas;
- frontend nuevo;
- cambios de UX/UI;
- integración fiscal real.

## Validación recomendada

Como la tanda toca backend, ejecutar:

```bat
scripts\test-backend.bat
```
