# Base de datos - Pasteleria

Carpeta de documentacion SQL y scripts de base de datos.

## Ruta recomendada

La ruta canonica actual para carga manual quedo consolidada en:

- [V1/DATABASE_SCHEMA_CANONICO.sql](V1/DATABASE_SCHEMA_CANONICO.sql)
- [V1/DATABASE_SEED_CANONICO.sql](V1/DATABASE_SEED_CANONICO.sql)
- [V1/README.md](V1/README.md)

## Por que existe esta consolidacion

- la base local observada tenia 22 tablas y faltaban `orden_compra` y `orden_compra_detalle`
- habia scripts viejos validos solo parcialmente
- y los seeds mezclaban dos generaciones de codigos de producto

## Estado actual

- los SQL legacy de carga manual fueron retirados para evitar rutas duplicadas
- la unica ruta manual soportada es la pareja `DATABASE_SCHEMA_CANONICO.sql` + `DATABASE_SEED_CANONICO.sql`
- las migraciones en `backend/src/main/resources/db/migration/` se conservan como historia tecnica del backend, no como bootstrap manual recomendado
