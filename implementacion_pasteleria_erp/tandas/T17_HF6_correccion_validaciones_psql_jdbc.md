# T17-HF6 — Corrección de validaciones SQL ejecutadas por JDBC

## Contexto

Después de T17-HF5, la compilación principal del backend, `testCompile`, el arranque de Testcontainers, Flyway y el smoke HTTP avanzaron correctamente. El bloqueo restante apareció en `BackendMigrationIntegrationTest.shouldPassSqlValidationScripts`.

El test ejecuta los scripts de `db/validation/*.sql` mediante `JdbcTemplate`. Algunos scripts de validación fueron pensados también para uso manual con `psql` e incluyen metacomandos como:

```sql
\echo '== Validate purchase documents and accounts payable =='
```

Ese comando es válido en `psql`, pero no es SQL estándar ejecutable por JDBC. PostgreSQL lo rechaza con error de sintaxis cuando se envía por `JdbcTemplate`.

## Decisión

No se eliminó `\echo` de los scripts porque sigue siendo útil cuando se ejecutan manualmente desde consola `psql`.

Se corrigió el test de integración para adaptar los scripts al contexto JDBC, ignorando líneas que empiecen por `\` antes de ejecutar el contenido con `JdbcTemplate`.

## Archivo modificado

```text
backend/src/test/java/com/pasteleria/integration/BackendMigrationIntegrationTest.java
```

## Cambio aplicado

Se agregó un método local:

```java
private String stripPsqlMetaCommands(String sql)
```

Ese método elimina líneas de metacomandos `psql`, por ejemplo `\echo`, antes de ejecutar el script con JDBC.

## Alcance

No se modificó:

- backend productivo;
- endpoints;
- servicios de cartera/cobranzas/cuentas por pagar;
- migraciones;
- tablas;
- Angular Admin;
- Astro público;
- UX/UI.

## Validación esperada

Ejecutar:

```bat
scripts\test-backend.bat
```

El objetivo es que el test de migración pueda ejecutar las validaciones SQL sin fallar por metacomandos `psql`.

