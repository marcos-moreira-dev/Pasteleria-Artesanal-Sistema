# Backend — Auditoría, soporte y evidencia

## Módulo de auditoría

El módulo `auditoria` expone consultas sobre `auditoria_evento` para que soporte pueda revisar qué ocurrió, cuándo, sobre qué entidad y con qué `requestId`.

Endpoints:

- `GET /api/v1/auditoria/resumen`
- `GET /api/v1/auditoria/eventos`

Parámetros de eventos:

- `modulo`, opcional;
- `limit`, entre 1 y 100.

## Módulo de soporte

El módulo `soporte` expone evidencia técnica-operativa de solo lectura.

Endpoints:

- `GET /api/v1/soporte/evidencia`
- `GET /api/v1/soporte/checklist`

La evidencia no reemplaza los logs ni los tests; sirve como fotografía administrativa para revisión, soporte y entrega.

## Decisión técnica

Se usó `JdbcTemplate` para consultas de solo lectura porque esta capa no necesita entidades JPA nuevas ni lógica de persistencia compleja.
