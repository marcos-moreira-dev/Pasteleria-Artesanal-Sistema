# Reglas financieras compartidas

Pastelería ERP necesita un idioma financiero común antes de endurecer cartera, cuentas por pagar, contabilidad y fiscalidad.

## Principio

Las reglas genéricas de dinero no deben vivir repetidas en cada módulo. Caja, compras, cartera, contabilidad y fiscalidad pueden tener reglas propias, pero deben coincidir en invariantes básicos:

- los montos se normalizan a dos decimales;
- un monto aplicado debe ser positivo;
- un saldo no puede ser negativo;
- una aplicación no puede superar el saldo pendiente;
- una línea contable tiene debe o haber, no ambos;
- un asiento debe cuadrar;
- un documento fiscal interno nace desde un solo origen;
- total fiscal equivale a subtotal más impuesto;
- una cuenta contable debe ser imputable para recibir movimientos.

## Relación con módulos existentes

`ErpFinancialPolicy` no reemplaza las políticas de cada módulo.

- Caja conserva reglas de turno, naturaleza y saldo físico de caja.
- Compras conserva reglas de recepción y documentabilidad.
- Inventario conserva reglas de stock y movimiento.
- Producción conserva reglas de consumo, rendimiento y producto terminado.

La política financiera común solo evita que cada módulo invente su propia forma de validar dinero y saldos.

## Uso previsto en tandas posteriores

- Cartera: validar cobros aplicados contra saldos pendientes.
- Cuentas por pagar: validar pagos aplicados contra documentos por pagar.
- Contabilidad: validar líneas y cuadre de asientos.
- Fiscalidad: validar origen único y totales internos.
- Reportes: confiar en estados financieros consistentes.
