# Migraciones por ambiente

## Productivo / staging

Usan solo:

```text
classpath:db/migration
```

No deben cargar seeds dev ni presentation.

## Desarrollo

Si se activa Flyway, usa:

```text
classpath:db/migration,classpath:db/dev-migration
```

Por defecto sigue apagado en local/dev para proteger bases cargadas manualmente.

## Presentación / SIT

Si se activa Flyway, usa:

```text
classpath:db/migration,classpath:db/dev-migration,classpath:db/presentation-migration
```

La presentación debe tener datos ricos y coherentes, no datos productivos.

## Validación

Los SQL de validación viven en:

```text
backend/src/main/resources/db/validation
```

Se ejecutan manualmente con `psql -v ON_ERROR_STOP=1`.
