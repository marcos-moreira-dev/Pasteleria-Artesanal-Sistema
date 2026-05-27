# T18 — Contabilidad aplicada

## Objetivo

Agregar una primera capa de contabilidad interna para Pastelería ERP: plan de cuentas mínimo, tipos de diario, asientos contables y detalle debe/haber.

## Contexto

T16 dejó `ErpFinancialPolicy` para validar dinero, saldos y partida doble. T17 dejó cartera, cobranzas y cuentas por pagar, pero sin asientos contables. Esta tanda crea la base contable sin activar todavía bridges automáticos.

## Alcance implementado

- Tablas V1/public para:
  - `tipo_diario_contable`
  - `cuenta_contable`
  - `asiento_contable`
  - `asiento_contable_detalle`
- Tablas equivalentes V2 en schema `contabilidad`.
- Seeds mínimos de diario y plan de cuentas.
- Módulo backend `com.pasteleria.contabilidad`.
- Endpoints:
  - `GET /api/v1/contabilidad/cuentas`
  - `GET /api/v1/contabilidad/diarios`
  - `GET /api/v1/contabilidad/asientos`
  - `GET /api/v1/contabilidad/asientos/{id}`
  - `POST /api/v1/contabilidad/asientos`
- Validación de partida doble mediante `ErpFinancialPolicy`.
- Validación SQL `11_validate_accounting.sql`.
- Contratos API actualizados.

## Fuera de alcance

- No se genera contabilidad automáticamente desde ventas, compras, caja o producción.
- No se implementa mayor contable pesado ni cierres contables.
- No se declara contabilidad fiscal final ni cumplimiento tributario productivo.
- No se toca Angular ni storefront.

## Criterio de diseño

La contabilidad se trata como una capa interna de trazabilidad financiera. Los servicios operativos no deben guardar líneas contables directamente; esa conexión vendrá con bridges ERP separados en T19.

## Pruebas esperadas

Ejecutar:

```bat
scripts\test-backend.bat
```

