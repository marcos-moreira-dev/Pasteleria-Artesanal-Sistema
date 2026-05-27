# Bridges ERP separados

Los bridges ERP traducen eventos operativos en consecuencias financieras internas. No son controllers de negocio ni sustituyen los servicios de dominio.

## Regla central

Cada bridge debe ser pequeño, idempotente y de responsabilidad única.

## Flujos habilitados

- Pedido a documento por cobrar.
- Documento por cobrar a asiento de venta.
- Cobranza a asiento de cobro.
- Documento por pagar a asiento de compra.
- Pago proveedor a asiento de pago.

## Idempotencia

La idempotencia se basa en el origen contable:

```text
origen_tipo + origen_id
```

Esto evita duplicar asientos si una acción se ejecuta dos veces.
