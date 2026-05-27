# T17-HF7 — Corrección de escape Java en validaciones JDBC

## Objetivo

Corregir un error introducido en T17-HF6 dentro del test de integración `BackendMigrationIntegrationTest`.

El objetivo de T17-HF6 era permitir que los scripts de validación SQL siguieran teniendo metacomandos de `psql`, como `\echo`, pero que el test los pudiera ejecutar mediante JDBC ignorando esas líneas.

## Problema detectado

La corrección anterior dejó cadenas Java mal escapadas en:

```text
backend/src/test/java/com/pasteleria/integration/BackendMigrationIntegrationTest.java
```

El compilador reportaba errores como:

```text
illegal character: '\'
unclosed string literal
class, interface, enum, or record expected
```

## Corrección aplicada

Se corrigió el método auxiliar:

```java
private String stripPsqlMetaCommands(String sql) {
  StringBuilder jdbcSql = new StringBuilder();
  for (String line : sql.split("\\R")) {
    if (!line.stripLeading().startsWith("\\")) {
      jdbcSql.append(line).append(System.lineSeparator());
    }
  }
  return jdbcSql.toString();
}
```

La regla queda así:

- los scripts SQL mantienen `\echo` para ejecución manual con `psql`;
- el test de integración ignora esas líneas antes de ejecutar por JDBC;
- no se toca código productivo.

## Archivos modificados

```text
backend/src/test/java/com/pasteleria/integration/BackendMigrationIntegrationTest.java
implementacion_pasteleria_erp/80_roadmap/00_roadmap_tandas.md
implementacion_pasteleria_erp/README.md
```

## Alcance

No se modificaron:

- endpoints;
- servicios productivos;
- entidades;
- migraciones;
- vistas;
- Angular Admin;
- Astro storefront;
- UX/UI.

## Validación sugerida

```bat
scripts\test-backend.bat
```
