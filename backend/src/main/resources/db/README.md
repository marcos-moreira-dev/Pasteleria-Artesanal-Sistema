# Base de datos de Pasteleria

Este backend usa PostgreSQL 17 y Flyway como fuente oficial de evolucion del esquema.

## Ruta oficial

- `db/migration/V1__init_schema.sql`: esquema base y tablas del dominio.
- `db/migration/V2__seed_base.sql`: catalogos y usuarios operativos minimos.
- `db/migration/V3__seed_demo.sql`: datos demostrativos para desarrollo local.
- `db/migration/V4__seed_enterprise_demo.sql`: seed enriquecido con pedidos, produccion, reportes, notificaciones y auditoria.

## Criterios aplicados

- Modelo moderado y realista: clientes, catalogo, cotizaciones, pedidos y produccion.
- Infraestructura minima integrada desde V1: archivos, jobs de reportes, notificaciones y auditoria.
- Convencion de claves: `*_id` bigint autoincremental.
- Estados de negocio restringidos con `CHECK`.
- Integridad referencial e indices para consultas operativas frecuentes.

## Nota de implementacion

La base ya tiene un primer mapeo JPA en `com.pasteleria.*` para que el backend no quede desconectado del esquema fisico mientras avanza la implementacion por modulos.

Para bases ya existentes que nacieron antes de `V4`, el proyecto incluye `scripts/apply-demo-delta.ps1` como ruta segura para sumar el seed enriquecido sin reset completo.
