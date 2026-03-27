# db/V1 - Base de datos Pasteleria

## Archivos canonicos

Usa estos dos archivos como fuente de verdad actual:

- `DATABASE_SCHEMA_CANONICO.sql`
- `DATABASE_SEED_CANONICO.sql`

La consolidacion existe porque el proyecto tenia tres problemas reales:

- el snapshot JPA previo no contemplaba `orden_compra` ni `orden_compra_detalle`
- los seeds legacy mezclaban codigos `PROD-*` con los codigos actuales del catalogo
- y la carpeta SQL tenia varias rutas validas a medias pero ninguna ruta unica y clara

## Uso recomendado

```sql
DROP DATABASE IF EXISTS pasteleria;
CREATE DATABASE pasteleria;
\c pasteleria
\i db/V1/DATABASE_SCHEMA_CANONICO.sql
\i db/V1/DATABASE_SEED_CANONICO.sql
```

## Que contiene el schema canonico

- tablas activas del backend actual
- tablas de abastecimiento usadas por el admin
- `orden_compra` y `orden_compra_detalle`, que eran el hueco mas evidente en la base local
- indices minimos para operacion y consultas

## Que contiene el seed canonico

- roles y usuarios demo
- categorias y productos con los codigos vigentes
- clientes, cotizaciones, pedidos y produccion
- ingredientes, insumos, proveedores e inventario
- ordenes de compra y sus detalles
- recetas tecnicas basicas
- archivos, reportes, notificaciones y auditoria de ejemplo

## Criterio de compatibilidad

El seed canonico evita mezclar:

- codigos legacy tipo `PROD-*`
- codigos actuales tipo `TORT-*`, `BROWNIE-*`, `CUPK-*`, `MESA-DULCE-*`

Tambien usa los estados vigentes del backend actual para produccion:

- `PENDIENTE`
- `PREPARACION`
- `DECORACION`
- `EMPAQUE`
- `FINALIZADO`

## Legacy

- la ruta legacy de carga manual fue eliminada de `db/V1/` para evitar duplicidad
- las migraciones en `backend/src/main/resources/db/migration/` siguen existiendo porque forman parte del backend, pero no son la ruta manual canónica
