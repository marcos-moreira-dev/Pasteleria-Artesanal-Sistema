# T07 — Base de datos V1/V2 y migraciones por ambiente

## 1. Objetivo

Ordenar la línea ejecutable futura de base de datos del proyecto de pastelería, separando:

- migraciones productivas compactas;
- seeds de desarrollo;
- seeds de presentación/SIT;
- validaciones SQL;
- historia Flyway legacy.

Esta tanda prepara la base para evolucionar hacia ERP completo sin borrar de golpe la V1 ni romper backend, administración Angular o storefront.

## 2. Contexto heredado

Antes de esta tanda existían dos realidades:

1. Una fuente manual canónica en `db/V1/`:
   - `DATABASE_SCHEMA_CANONICO.sql`
   - `DATABASE_SEED_CANONICO.sql`

2. Una línea Flyway legacy en `backend/src/main/resources/db/migration/` con `V1..V15`, útil como historia técnica, pero no como bootstrap principal porque ya estaba desalineada con la fuente canónica.

De Cedro se rescata la disciplina:

- `db/migration` para línea ejecutable;
- `db/dev-migration` para datos locales;
- `db/presentation-migration` para datos de presentación/SIT;
- repeatable views para reporting/semántica;
- validación de seeds y estructura.

## 3. Fuente Cedro usada como referencia

Cedro separa:

```text
backend/src/main/resources/db/migration/
backend/src/main/resources/db/dev-migration/
backend/src/main/resources/db/presentation-migration/
```

Además conserva la documentación histórica fuera de la línea ejecutable y valida los seeds de presentación.

La pastelería adopta esa metodología, pero no copia su dominio restaurante ni su esquema físico `public` como obligación.

## 4. Estado actual de Pastelería

La V1 canónica actual sigue representando la app funcional:

- clientes;
- productos;
- cotizaciones;
- pedidos;
- producción simple;
- ingredientes;
- insumos;
- compras básicas;
- reportes;
- archivos;
- guía operativa;
- auditoría.

La V2 todavía no implementa todo el ERP. En esta tanda se crea un scaffolding controlado con schemas objetivo y tabla de mapeo legacy. Las tandas posteriores expanden V2 por dominio.

## 5. Alcance

Sí se hizo:

- mover migraciones Flyway legacy a `db/legacy/flyway-history`;
- crear `V1__pasteleria_base_actual.sql` desde los SQL canónicos;
- crear `V2__erp_pasteleria_unificado_3fn.sql` como scaffolding transicional;
- crear repeatable views de reporting y capa semántica inicial;
- crear `dev-migration`;
- crear `presentation-migration`;
- crear `validation` con smoke checks SQL;
- actualizar documentación de base de datos;
- ajustar perfiles dev/local para que, si se activa Flyway, incluyan `dev-migration`.

## 6. Fuera de alcance

No se hizo todavía:

- implementar todo el ERP físico completo;
- migrar todos los datos V1 hacia V2;
- crear caja ERP definitiva;
- crear contabilidad completa;
- crear fiscalidad completa;
- activar Flyway por defecto en dev/local;
- borrar tablas V1;
- tocar UX/UI;
- tocar storefront.

## 7. Archivos principales tocados

```text
backend/src/main/resources/db/migration/V1__pasteleria_base_actual.sql
backend/src/main/resources/db/migration/V2__erp_pasteleria_unificado_3fn.sql
backend/src/main/resources/db/migration/R__pasteleria_reporting_views.sql
backend/src/main/resources/db/migration/R__pasteleria_semantic_views.sql
backend/src/main/resources/db/dev-migration/V100__seed_development_identity.sql
backend/src/main/resources/db/dev-migration/V101__seed_development_catalog.sql
backend/src/main/resources/db/dev-migration/V102__seed_development_operations.sql
backend/src/main/resources/db/presentation-migration/V200__seed_presentation_master_data.sql
backend/src/main/resources/db/presentation-migration/V201__seed_presentation_pasteleria_operations.sql
backend/src/main/resources/db/presentation-migration/V202__seed_presentation_erp_finance.sql
backend/src/main/resources/db/presentation-migration/V203__validate_presentation_seed.sql
backend/src/main/resources/db/validation/00_run_all_validations.sql
backend/src/main/resources/db/validation/01_smoke_structure.sql
backend/src/main/resources/db/validation/02_smoke_seeds.sql
backend/src/main/resources/db/validation/03_validate_v1_invariants.sql
backend/src/main/resources/db/README.md
db/README.md
backend/src/main/resources/application-dev.yml
backend/src/main/resources/application-local.yml
```

## 8. Diseño de V1

`V1__pasteleria_base_actual.sql` consolida:

```text
db/V1/DATABASE_SCHEMA_CANONICO.sql
+
db/V1/DATABASE_SEED_CANONICO.sql
```

Para Flyway se retiraron los `BEGIN/COMMIT` del seed, porque Flyway maneja transacciones de migración.

## 9. Diseño de V2

`V2__erp_pasteleria_unificado_3fn.sql` crea los schemas objetivo:

```text
core
seguridad
terceros
inventario
comercial
compras
produccion
cartera
tesoreria
fiscal
contabilidad
inteligencia
staging
auditoria
ayuda
```

También crea:

```text
core.erp_migration_marker
core.legacy_objeto_mapeo
```

La tabla `core.legacy_objeto_mapeo` servirá para mapear IDs V1 `BIGINT` hacia entidades ERP V2 futuras.

## 10. Repeatable views

Se crearon vistas iniciales:

```text
inteligencia.vw_pasteleria_pedidos_resumen
inteligencia.vw_pasteleria_stock_bajo
inteligencia.vw_pasteleria_reportes_estado
inteligencia.vw_semantic_ventas_operativas
inteligencia.vw_semantic_produccion_pendiente
inteligencia.vw_semantic_stock_actual
```

Estas vistas son de lectura. No reemplazan tablas transaccionales.

## 11. Riesgos

- Si se activa Flyway contra una base ya cargada manualmente, V1 intenta crear/droppear tablas. Usar base limpia para pruebas Flyway.
- V2 es scaffolding, no ERP completo todavía.
- Los seeds dev/presentation son placeholders controlados; se completarán en T24.
- Staging/prod usan Flyway activo por defecto. No usar sin revisar conexión y base limpia.

## 12. Criterios de aceptación

La tanda se considera correcta si:

- `db/migration` contiene solo la línea compacta nueva;
- `legacy/flyway-history` conserva V1..V15 antiguos;
- existen `dev-migration`, `presentation-migration` y `validation`;
- V1 proviene del SQL canónico;
- V2 no borra tablas V1;
- los perfiles dev/local apuntan también a `dev-migration` si se activa Flyway;
- no se tocó UX/UI.

## 13. Pruebas mínimas recomendadas

En una máquina con PostgreSQL y Maven disponibles:

```bat
scriptseset-infra.bat
cd backend
mvn test
```

Para prueba SQL manual:

```bash
psql -v ON_ERROR_STOP=1 -d pasteleria_test -f backend/src/main/resources/db/migration/V1__pasteleria_base_actual.sql
psql -v ON_ERROR_STOP=1 -d pasteleria_test -f backend/src/main/resources/db/migration/V2__erp_pasteleria_unificado_3fn.sql
psql -v ON_ERROR_STOP=1 -d pasteleria_test -f backend/src/main/resources/db/migration/R__pasteleria_reporting_views.sql
psql -v ON_ERROR_STOP=1 -d pasteleria_test -f backend/src/main/resources/db/migration/R__pasteleria_semantic_views.sql
```

## 14. Notas para el siguiente chat

La siguiente tanda es T08:

```text
T08 — Tests base y smoke API
```

T08 debe aprovechar esta estructura para crear tests reales de migración/smoke API con PostgreSQL real o Testcontainers, si el entorno lo permite.
