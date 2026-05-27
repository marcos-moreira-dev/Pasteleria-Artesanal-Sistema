# Fuentes de verdad de base de datos

## Fuente manual canónica V1

La fuente manual canónica de la pastelería actual sigue en:

```text
db/V1/DATABASE_SCHEMA_CANONICO.sql
db/V1/DATABASE_SEED_CANONICO.sql
```

Sirve para resets manuales y para entender el estado funcional actual.

## Fuente ejecutable futura por Flyway

Desde T07, la fuente ejecutable futura queda en:

```text
backend/src/main/resources/db/migration/
```

Contiene:

```text
V1__pasteleria_base_actual.sql
V2__erp_pasteleria_unificado_3fn.sql
R__pasteleria_reporting_views.sql
R__pasteleria_semantic_views.sql
```

## Historia legacy

Las migraciones antiguas se conservan en:

```text
backend/src/main/resources/db/legacy/flyway-history/
```

No deben usarse como bootstrap principal.

## Regla de oro

No mezclar fuentes. Si se prueba Flyway, usar base limpia. Si se hace reset manual, usar `db/V1`.
