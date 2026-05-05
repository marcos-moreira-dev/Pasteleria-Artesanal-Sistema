# Base de datos de Pasteleria

Este backend conserva migraciones Flyway en `db/migration/`, pero la carga manual
canónica del proyecto quedó consolidada fuera del backend en:

- `db/V1/DATABASE_SCHEMA_CANONICO.sql`
- `db/V1/DATABASE_SEED_CANONICO.sql`

## Criterio operativo

- usa la ruta canónica para recrear una base manualmente o para un reset limpio
- usa las migraciones Flyway como historial técnico del backend y referencia de evolución
- no uses `V3__seed_datos_arranque.sql` ni `V4__seed_datos_operativos.sql` como bootstrap manual, porque pertenecen a una línea presentación funcional legacy

## Nota de implementación

El mapeo JPA vigente valida el esquema al arrancar. Si la base fue cargada con los
SQL canónicos, el backend debe correr contra ese estado, no contra seeds legacy.
