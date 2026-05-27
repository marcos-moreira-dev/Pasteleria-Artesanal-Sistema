# Cartera, cobranzas y cuentas por pagar

En Pastelería ERP, cartera y cuentas por pagar se tratan como módulos financieros operativos, no como contabilidad todavía.

## Cartera

La cartera representa dinero que clientes deben a la pastelería.

Flujo mínimo:

```text
pedido o cliente
→ documento por cobrar
→ cobranza
→ aplicación contra saldo
```

La regla central es que una cobranza no puede superar el saldo pendiente.

## Cuentas por pagar

Las cuentas por pagar representan obligaciones con proveedores.

Flujo mínimo:

```text
documento de compra
→ documento por pagar
→ pago a proveedor
→ aplicación contra saldo
```

La regla central es que un pago aplicado no puede superar el saldo del documento.

## Separación importante

Cartera y cuentas por pagar actualizan saldos. Contabilidad y fiscalidad se implementan después.

Esto evita mezclar responsabilidades:

- cartera cobra;
- cuentas por pagar paga;
- tesorería/caja mueve dinero;
- contabilidad registra asientos;
- fiscalidad prepara comprobantes.
