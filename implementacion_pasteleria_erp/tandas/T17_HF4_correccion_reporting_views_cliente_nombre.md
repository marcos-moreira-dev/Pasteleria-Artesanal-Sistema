# T17-HF4 — Corrección de vistas reporting/semánticas para cliente.nombre_completo

## Contexto

Después de T17-HF3, Testcontainers ya arrancaba PostgreSQL correctamente y Flyway avanzaba hasta las migraciones V1/V2. El nuevo bloqueo aparecía en la migración repeatable:

```text
R__pasteleria_reporting_views.sql
```

El error raíz era:

```text
ERROR: column c.nombre does not exist
```

La tabla `public.cliente` usa la columna real:

```text
nombre_completo
```

no `nombre`.

## Cambio aplicado

Se actualizaron las vistas de lectura para usar la columna correcta:

```text
backend/src/main/resources/db/migration/R__pasteleria_reporting_views.sql
backend/src/main/resources/db/migration/R__pasteleria_semantic_views.sql
```

Cambios principales:

```text
c.nombre AS cliente_nombre
→ c.nombre_completo AS cliente_nombre
```

Y en la vista semántica de ventas también se ajustó el `GROUP BY` correspondiente.

## Fuera de alcance

No se tocaron:

- backend productivo;
- endpoints;
- tablas;
- servicios de cartera/cobranzas/cuentas por pagar;
- Angular Admin;
- Astro storefront;
- UX/UI.

## Validación esperada

Ejecutar:

```bat
scripts	est-backend.bat
```

Luego:

```bat
scripts	est-admin.bat
scripts	est-storefront.bat
```

## Nota de continuidad

La siguiente tanda funcional sigue siendo T18 — Contabilidad aplicada, pero solo después de confirmar que el backend pase completo con Flyway + tests.
