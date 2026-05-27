# Base de datos — inteligencia y reporting

T21 fortalece las vistas repeatable:

- `R__pasteleria_semantic_views.sql`

## Vistas principales

- `vw_semantic_dashboard_erp`
- `vw_semantic_cartera_documentos`
- `vw_semantic_cuentas_pagar_documentos`
- `vw_semantic_caja_movimientos`
- `vw_semantic_contabilidad_asientos`
- `vw_semantic_fiscal_documentos`
- `vw_semantic_stock_actual`

## Criterio

Las vistas son una interfaz de lectura estable para reportes. Si cambian detalles operativos internos, el frontend/reporting debería leer estas vistas y no tablas sueltas.
