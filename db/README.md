# Base de datos - Pasteleria

Esta carpeta conserva la fuente SQL canónica manual de V1 y documentación auxiliar.

## Fuente canónica V1 manual

- `V1/DATABASE_SCHEMA_CANONICO.sql`
- `V1/DATABASE_SEED_CANONICO.sql`
- `V1/README.md`

## Fuente ejecutable futura Flyway

Desde T07, la línea Flyway compacta vive en:

- `backend/src/main/resources/db/migration/V1__pasteleria_base_actual.sql`
- `backend/src/main/resources/db/migration/V2__erp_pasteleria_unificado_3fn.sql`

La V1 Flyway se generó desde los SQL canónicos de esta carpeta.

## Regla

- Para reset manual local, puedes seguir usando `db/V1/*.sql`.
- Para evolución ejecutable por ambiente, usa la línea `backend/src/main/resources/db/`.
- No mezclar scripts legacy con la línea V1/V2 compacta.
