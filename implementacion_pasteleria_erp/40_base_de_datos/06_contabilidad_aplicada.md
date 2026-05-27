# Base de datos — Contabilidad aplicada

## V1/public

Se agregaron tablas operativas compatibles con el backend actual:

```text
tipo_diario_contable
cuenta_contable
asiento_contable
asiento_contable_detalle
```

## V2/contabilidad

Se agregaron equivalentes transicionales en el schema ERP:

```text
contabilidad.tipo_diario_contable
contabilidad.cuenta_contable
contabilidad.asiento_contable
contabilidad.asiento_contable_detalle
```

## Validaciones

`11_validate_accounting.sql` valida cuentas base, cuentas imputables, asientos cuadrados y líneas debe/haber.
