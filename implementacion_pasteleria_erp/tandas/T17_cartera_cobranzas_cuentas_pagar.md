# T17 — Cartera, cobranzas y cuentas por pagar

## Objetivo

Agregar la primera base funcional de cartera y cuentas por pagar para Pastelería ERP, sin tocar la UX/UI administrativa ni el storefront.

La tanda convierte la preparación financiera previa en comportamiento real básico:

- documentos por cobrar;
- cobranzas aplicadas contra saldo;
- documentos por pagar consultables desde un módulo dedicado;
- pagos a proveedor aplicados contra saldo;
- validaciones SQL de saldos y aplicaciones.

## Contexto heredado

T14 ya había dejado el flujo de compras:

```text
orden de compra recibida
→ documento de compra
→ documento por pagar
```

T16 agregó `ErpFinancialPolicy`, usada ahora para validar montos, saldos y aplicaciones contra saldo.

## Alcance implementado

### Cartera

Nuevo módulo backend:

```text
com.pasteleria.cartera
```

Endpoints:

```text
GET  /api/v1/cartera/documentos-cobrar
GET  /api/v1/cartera/documentos-cobrar/{id}
POST /api/v1/cartera/documentos-cobrar
GET  /api/v1/cartera/cobranzas
POST /api/v1/cartera/cobranzas
```

Reglas principales:

- un pedido no puede tener dos documentos por cobrar;
- el total debe ser positivo;
- la cobranza debe aplicar exactamente el monto total recibido;
- una cobranza no puede superar el saldo del documento;
- el saldo posterior actualiza el estado del documento.

Estados de documento por cobrar:

```text
PENDIENTE
PARCIAL
PAGADO
ANULADO
```

### Cuentas por pagar

Nuevo módulo backend:

```text
com.pasteleria.cuentaspagar
```

Endpoints:

```text
GET  /api/v1/cuentas-pagar/documentos
GET  /api/v1/cuentas-pagar/pagos
POST /api/v1/cuentas-pagar/pagos
```

Reglas principales:

- el pago debe tener monto positivo;
- la suma de aplicaciones debe coincidir con el monto total pagado;
- no se puede pagar más que el saldo pendiente;
- todos los documentos aplicados pertenecen al mismo proveedor;
- el saldo posterior actualiza el estado del documento por pagar.

Estados de documento por pagar:

```text
PENDIENTE
PAGADO_PARCIAL
PAGADO
ANULADO
```

## Base de datos

Se agregaron en V1/public:

```text
documento_cobrar
cobranza
cobranza_detalle
pago_proveedor
pago_proveedor_aplicacion
```

Se agregaron equivalentes transicionales en V2/schema `cartera`:

```text
cartera.documento_cobrar
cartera.cobranza
cartera.cobranza_detalle
cartera.pago_proveedor
cartera.pago_proveedor_aplicacion
```

## Validación SQL

Nuevo archivo:

```text
backend/src/main/resources/db/validation/10_validate_receivables_payables.sql
```

Valida:

- saldos de documentos por cobrar;
- estados de documentos por cobrar;
- aplicaciones de cobranza;
- saldos de documentos por pagar;
- estados de documentos por pagar;
- aplicaciones de pagos a proveedor.

También se actualizó:

```text
backend/src/main/resources/db/validation/00_run_all_validations.sql
backend/src/main/resources/db/validation/01_smoke_structure.sql
```

## Contratos API

Se actualizaron contratos para declarar los nuevos endpoints y enums:

```text
EstadoDocumentoCobrar
EstadoCobranza
EstadoPagoProveedor
```

## Fuera de alcance

Esta tanda no implementa todavía:

- contabilidad aplicada;
- asientos automáticos;
- documento fiscal real;
- bridge automático pedido/caja/contabilidad;
- interfaz Angular dedicada;
- integración obligatoria con movimiento de caja.

Esas responsabilidades quedan para T18, T19, T20 y T23.

## Riesgos y notas

La aplicación de pagos y cobranzas ya reduce saldos, pero todavía no genera asientos ni movimientos de tesorería completos. Esa separación es intencional para no mezclar cartera con contabilidad ni caja antes de los bridges ERP.

## Validación sugerida

En máquina local:

```bat
scripts\test-backend.bat
```

Prueba manual sugerida:

```http
GET  /api/v1/cartera/documentos-cobrar
POST /api/v1/cartera/documentos-cobrar
POST /api/v1/cartera/cobranzas
GET  /api/v1/cuentas-pagar/documentos
POST /api/v1/cuentas-pagar/pagos
```
