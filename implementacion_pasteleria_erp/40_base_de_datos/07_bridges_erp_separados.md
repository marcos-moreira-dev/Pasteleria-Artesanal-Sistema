# Base de datos — Bridges ERP separados

T19 no agrega tablas nuevas. Aprovecha columnas de origen ya creadas en T18:

```text
asiento_contable.origen_tipo
asiento_contable.origen_id
```

## Orígenes usados

- `DOCUMENTO_COBRAR`.
- `COBRANZA`.
- `DOCUMENTO_PAGAR`.
- `PAGO_PROVEEDOR`.

## Validación

Se agrega:

```text
db/validation/12_validate_erp_bridges.sql
```

Valida idempotencia contable y referencias básicas de origen.
