# T17-HF3 — Corrección de arranque Testcontainers en smoke integration

## Contexto

Después de T17-HF2, la compilación principal del backend y la compilación de tests ya avanzaban correctamente, pero el test de integración `BackendApiSmokeIntegrationTest` fallaba al levantar el contexto de Spring Boot.

El error raíz era:

```text
Mapped port can only be obtained after the container is started
```

Esto ocurría porque `DynamicPropertySource` intentaba resolver `POSTGRES.getJdbcUrl()` antes de que el contenedor PostgreSQL de Testcontainers estuviera arrancado.

## Cambio aplicado

Se actualizó:

```text
backend/src/test/java/com/pasteleria/support/AbstractPostgresIntegrationTest.java
```

Ahora `registerDatabaseProperties` llama primero a:

```text
ensurePostgresStarted()
```

La operación es sincronizada e idempotente: si el contenedor ya está corriendo, no hace nada; si todavía no está corriendo, lo arranca antes de registrar las propiedades del datasource.

## Fuera de alcance

No se tocaron:

- endpoints productivos;
- migraciones;
- tablas;
- Angular Admin;
- Astro storefront;
- UX/UI;
- flujos de cartera, cobranzas o cuentas por pagar.

## Validación esperada

En máquina local con Docker Desktop activo:

```bat
scripts\test-backend.bat
```

Luego:

```bat
scripts\test-admin.bat
scripts\test-storefront.bat
```

## Nota de continuidad

La siguiente tanda funcional sigue siendo T18 — Contabilidad aplicada, pero solo después de confirmar que el backend pasa completo.
