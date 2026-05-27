# T17-HF5 — Corrección de reutilización de ApplicationContext con Testcontainers

## Contexto

Después de T17-HF4, el backend ya compilaba, los tests unitarios avanzaban y el smoke HTTP con PostgreSQL real pasaba. Sin embargo, al ejecutar `BackendMigrationIntegrationTest` después de `BackendApiSmokeIntegrationTest`, los tests intentaban usar una conexión JDBC hacia el puerto anterior del contenedor PostgreSQL.

El síntoma observado era:

```text
Connection to localhost:<puerto anterior> refused
```

La causa era una interacción entre:

- el ciclo de vida de `@Container` en Testcontainers;
- el caché de `ApplicationContext` de Spring Test;
- las propiedades dinámicas `spring.datasource.*` registradas con el puerto mapeado del contenedor.

Al terminar una clase de integración, Testcontainers podía detener/recrear el contenedor. Luego Spring podía reutilizar un contexto ya creado con la URL JDBC anterior.

## Cambios realizados

### 1. Contexto Spring marcado como sucio después de cada clase de integración

Se actualizó:

```text
backend/src/test/java/com/pasteleria/support/AbstractPostgresIntegrationTest.java
```

Se agregó:

```java
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
```

Con esto, cuando termina una clase de integración, Spring no reutiliza un `ApplicationContext` que quedó asociado a un puerto viejo de PostgreSQL.

### 2. Validación de cartera incluida en el test de migración

Se actualizó:

```text
backend/src/test/java/com/pasteleria/integration/BackendMigrationIntegrationTest.java
```

Ahora también ejecuta:

```text
db/validation/10_validate_receivables_payables.sql
```

Esto hace que la validación de T17 quede cubierta por el test de migración.

## Fuera de alcance

No se tocaron:

- servicios productivos;
- endpoints;
- migraciones;
- vistas SQL;
- Angular Admin;
- Astro storefront;
- UX/UI;
- flujos de cartera, cobranzas o cuentas por pagar.

## Validación esperada

En máquina local ejecutar:

```bat
scripts\test-backend.bat
```

Luego:

```bat
scripts\test-admin.bat
scripts\test-storefront.bat
```

## Continuidad

Si esta corrección pasa, recién se puede continuar con:

```text
T18 — Contabilidad aplicada.
```
