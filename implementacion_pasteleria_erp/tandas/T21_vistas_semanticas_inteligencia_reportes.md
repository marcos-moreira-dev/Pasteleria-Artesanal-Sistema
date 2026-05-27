# T21 — Vistas semánticas e inteligencia/reportes

## Objetivo

Agregar una capa de lectura para inteligencia administrativa sobre los módulos ERP ya preparados: cartera, cuentas por pagar, caja, contabilidad, fiscalidad, producción, pedidos y stock bajo.

## Contexto heredado

Hasta T20 el sistema ya cuenta con:

- reglas financieras comunes (`ErpFinancialPolicy`),
- cartera y cuentas por pagar,
- contabilidad aplicada,
- bridges ERP separados,
- fiscalidad interna prudente.

Faltaba una superficie de consulta consolidada para tablero/reportes sin mezclar reglas operativas con consultas analíticas.

## Alcance

Esta tanda agrega:

- vistas semánticas nuevas en schema `inteligencia`,
- endpoint de dashboard ERP,
- endpoints de consulta semántica por dominio,
- validación SQL de vistas/reporting,
- contratos API actualizados,
- smoke backend ampliado con `/api/v1/inteligencia/dashboard`.

## Fuera de alcance

No se implementa todavía:

- frontend Angular de inteligencia,
- BI avanzado,
- reportes PDF nuevos,
- materialized views,
- cubos OLAP,
- automatización de bridges,
- cambios en storefront.

## Vistas agregadas

- `inteligencia.vw_semantic_dashboard_erp`
- `inteligencia.vw_semantic_cartera_documentos`
- `inteligencia.vw_semantic_cuentas_pagar_documentos`
- `inteligencia.vw_semantic_caja_movimientos`
- `inteligencia.vw_semantic_contabilidad_asientos`
- `inteligencia.vw_semantic_fiscal_documentos`
- `inteligencia.vw_semantic_stock_actual`

También se mantienen y fortalecen:

- `inteligencia.vw_semantic_ventas_operativas`
- `inteligencia.vw_semantic_produccion_pendiente`

## Endpoints agregados

- `GET /api/v1/inteligencia/dashboard`
- `GET /api/v1/inteligencia/cartera`
- `GET /api/v1/inteligencia/cuentas-pagar`
- `GET /api/v1/inteligencia/caja`
- `GET /api/v1/inteligencia/contabilidad`
- `GET /api/v1/inteligencia/fiscal`
- `GET /api/v1/inteligencia/stock-bajo`

Todos son de solo lectura y usan `REPORTES_VER` como permiso contractual.

## Riesgos controlados

- Las vistas no actualizan datos.
- La capa Java usa `JdbcTemplate` solo contra vistas semánticas.
- No se declara BI final ni dashboard productivo terminado.
- No se toca Angular todavía para evitar alterar UX/UI antes de T22/T23.

## Validación

Se agrega:

- `db/validation/14_validate_intelligence_reporting.sql`

Y se conecta a:

- `00_run_all_validations.sql`
- `BackendMigrationIntegrationTest`

El smoke API ahora también consulta el dashboard ERP después de login.
