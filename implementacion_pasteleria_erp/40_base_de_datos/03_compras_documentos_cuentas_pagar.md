# Base de datos — compras, documentos y cuentas por pagar

## V1

Tablas transicionales:

```text
documento_compra
documento_pagar
```

La V1 mantiene su modelo operativo actual y agrega la obligación financiera mínima sin exigir aún contabilidad completa.

## V2

Schemas preparados:

```text
compras.documento_compra
cartera.documento_pagar
```

La V2 conserva enfoque aditivo y no destructivo.

## Validaciones

El archivo `08_validate_purchase_documents.sql` revisa:

- totales de documento de compra válidos;
- saldos de cuentas por pagar válidos;
- una sola cuenta por pagar por documento;
- documentos registrados con cuenta por pagar asociada.
