# Operación — Soporte y evidencia

## Objetivo operativo

T25 permite revisar evidencia básica del sistema desde API sin abrir base de datos manualmente.

## Consultas útiles

Con sesión administrativa activa:

- `GET /api/v1/auditoria/resumen`
- `GET /api/v1/auditoria/eventos`
- `GET /api/v1/soporte/evidencia`
- `GET /api/v1/soporte/checklist`

## Uso recomendado

Antes de entregar una versión:

1. Ejecutar `scripts\test-backend.bat`.
2. Ejecutar frontend solo si se tocó Angular o storefront.
3. Revisar `/api/v1/soporte/evidencia`.
4. Revisar `/api/v1/soporte/checklist`.
5. Confirmar que el roadmap diga qué está hecho y qué queda pendiente.

## Nota

La evidencia es de soporte, no auditoría legal ni certificación externa.
