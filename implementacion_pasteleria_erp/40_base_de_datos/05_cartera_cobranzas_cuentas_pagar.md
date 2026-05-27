# Base de datos — Cartera y cuentas por pagar

T17 agrega tablas V1 operativas y tablas V2 transicionales en schema `cartera`.

## V1/public

```text
documento_cobrar
cobranza
cobranza_detalle
pago_proveedor
pago_proveedor_aplicacion
```

## V2/cartera

```text
cartera.documento_cobrar
cartera.cobranza
cartera.cobranza_detalle
cartera.pago_proveedor
cartera.pago_proveedor_aplicacion
```

## Invariantes

- saldo >= 0;
- saldo <= total;
- monto aplicado > 0;
- saldo posterior <= saldo anterior;
- estado debe coincidir con saldo.

La validación principal está en:

```text
backend/src/main/resources/db/validation/10_validate_receivables_payables.sql
```
