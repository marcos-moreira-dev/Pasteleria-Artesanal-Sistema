# Base de datos de Pasteleria

Desde T07 la carpeta `backend/src/main/resources/db/` queda ordenada en cuatro rutas:

- `migration/`: línea ejecutable futura de Flyway.
- `dev-migration/`: seeds locales de desarrollo, nunca productivos.
- `presentation-migration/`: seeds de presentación/SIT, nunca productivos.
- `validation/`: SQL de smoke checks e invariantes.

## Línea ejecutable futura

La línea nueva compacta es:

- `migration/V1__pasteleria_base_actual.sql`
- `migration/V2__erp_pasteleria_unificado_3fn.sql`
- `migration/R__pasteleria_reporting_views.sql`
- `migration/R__pasteleria_semantic_views.sql`

La migración V1 se generó desde:

- `db/V1/DATABASE_SCHEMA_CANONICO.sql`
- `db/V1/DATABASE_SEED_CANONICO.sql`

## Historia legacy

Las migraciones Flyway antiguas `V1..V15` se conservan en:

- `legacy/flyway-history/`

No deben usarse como bootstrap principal porque pertenecen a una línea histórica que ya estaba desalineada con el SQL canónico.

## Perfiles

- `dev`: puede usar `db/migration` + `db/dev-migration` si se activa Flyway.
- `presentation`: usa `db/migration` + `db/dev-migration` + `db/presentation-migration` si se activa Flyway.
- `staging/prod`: solo deben usar `db/migration`.

En desarrollo local, Flyway sigue apagado por defecto para no romper bases cargadas manualmente con el SQL canónico.

## Caja operativa transicional

Desde T11, `V1__pasteleria_base_actual.sql` incluye las tablas públicas `caja_operativa`, `turno_caja`, `movimiento_caja` y `arqueo_caja` para que JPA pueda validar el módulo de caja actual. `V2__erp_pasteleria_unificado_3fn.sql` prepara el espejo conceptual en `tesoreria.*` para la evolución ERP futura.

## Presentación/SIT desde T24

La carpeta `presentation-migration/` ya no contiene placeholders. Desde T24 incluye datos ricos para demostración y validación SIT.

La inicialización local final se hace desde `scripts\run-production.bat` o `scripts\run-demo.bat`. Internamente esos `.bat` delegan en `tools\powershell\init-db.ps1`, que aplica la línea ejecutable actual del backend (`db/migration/V1`, `V2` y vistas repeatable). En modo demo aplica también `V200` a `V203` y la validación `15_validate_presentation_sit.sql`.

Comandos principales:

```bat
backend\scripts\seed-presentation.bat
scripts\pasteleria-demo.bat
```

Estos datos son de demostración y no deben cargarse contra bases reales.



## T25 — Auditoría y soporte

La validación `db/validation/16_validate_audit_support_evidence.sql` revisa la estructura mínima de auditoría, permisos de soporte y eventos semilla para evidencia operativa.
