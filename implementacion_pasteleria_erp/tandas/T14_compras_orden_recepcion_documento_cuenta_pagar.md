# T14 — Compras: orden, recepción, documento y cuenta por pagar

## 1. Objetivo

Fortalecer el flujo de compras de la pastelería para que no sea solo una orden de compra visual, sino un flujo operativo trazable:

```text
orden_compra
→ recepción de mercadería
→ entrada de inventario
→ documento de compra
→ cuenta por pagar
```

Esta tanda no implementa pagos a proveedor ni contabilidad completa. Eso queda para T17, T18 y T19.

## 2. Contexto heredado

Hasta T13 el proyecto ya tiene:

- UX/UI propia de la pastelería respetada.
- Backend transversal con `ApiResponse`, errores, `requestId`, seguridad JSON y contratos API.
- Storage y archivos descargables.
- Guía Operativa con PDF.
- V1/V2 de base de datos compacta/transicional.
- Tests base con Testcontainers preparados.
- Identity transicional con permisos y sucursal `MATRIZ`.
- Inventario con movimientos, saldo anterior/posterior y bloqueo pesimista.
- Caja operativa.
- Pedidos y producción con máquinas de estado.
- Producción técnica con consumos/lotes/entradas documentales.

Compras ya tenía una base funcional de órdenes y recepciones, pero faltaba cerrar el flujo financiero mínimo: documento de compra y cuenta por pagar.

## 3. Fuente Cedro usada como referencia

De Cedro se rescata el patrón:

```text
compra/recepción operativa
→ documento_compra
→ documento_pagar
→ futura contabilidad/cartera
```

También se rescata la separación entre:

- operación de abastecimiento;
- documento financiero;
- cuenta por pagar;
- posterior pago/contabilidad.

No se copia restaurante literal ni estética Cedro.

## 4. Estado actual de Pastelería antes de la tanda

El módulo de abastecimiento ya tenía:

- proveedores;
- ingredientes;
- insumos;
- item_proveedor;
- orden_compra;
- orden_compra_detalle;
- recepción parcial/completa;
- entrada de inventario por recepción;
- autorización transicional por `COMPRAS_GESTIONAR`.

El problema era que la orden recibida no dejaba formalizada una obligación financiera consultable.

## 5. Alcance

Esta tanda implementa o consolida:

- `documento_compra` en V1.
- `documento_pagar` en V1.
- `DocumentoCompraEntity`.
- `DocumentoPagarEntity`.
- `CompraFinancieraService`.
- `CompraFinancieraPolicy`.
- `CompraFinancieraController`.
- DTOs de documento de compra y cuenta por pagar.
- Endpoints para registrar/consultar documento de compra y listar cuentas por pagar.
- Validación SQL de documentos de compra/cuentas por pagar.
- Preparación V2 transicional en schemas `compras` y `cartera`.
- Contratos API de compras financieras.
- Tipos TypeScript para Angular sin rediseñar UX/UI.

## 6. Fuera de alcance

No se implementa todavía:

- pago a proveedor;
- aplicación de pagos;
- asiento contable de compra;
- asiento contable de pago;
- fiscalidad real de compra;
- SRI;
- cuentas por pagar avanzadas;
- vencimientos por cuotas;
- aprobación jerárquica de compras;
- rediseño visual de Angular.

## 7. Cambios backend

### Nuevas piezas o piezas consolidadas

```text
CompraFinancieraController
CompraFinancieraService
CompraFinancieraPolicy
CompraFinancieraSummary
DocumentoCompraSummary
DocumentoPagarSummary
RegistrarDocumentoCompraRequest
DocumentoCompraEntity
DocumentoPagarEntity
DocumentoCompraRepository
DocumentoPagarRepository
EstadoDocumentoCompra
EstadoDocumentoPagar
```

### Endpoints

```http
POST /api/v1/abastecimiento/ordenes-compra/{id}/documento-compra
GET  /api/v1/abastecimiento/ordenes-compra/{id}/documento-compra
GET  /api/v1/abastecimiento/cuentas-pagar
```

### Reglas principales

- Solo se puede documentar una orden `RECIBIDA` o `RECIBIDA_PARCIAL`.
- La orden debe tener al menos una cantidad recibida.
- No se puede registrar dos documentos de compra para la misma orden.
- No se puede duplicar número de documento de compra.
- El subtotal se calcula desde cantidades recibidas, no desde cantidades pedidas.
- El impuesto no puede ser negativo.
- El total debe ser mayor a cero.
- La cuenta por pagar nace con `saldo = total`.
- La cuenta por pagar queda en estado `PENDIENTE`.

## 8. Cambios de base de datos

### V1

Se agregan o consolidan:

```text
documento_compra
documento_pagar
```

Con constraints:

- documento de compra único por orden;
- número de documento único;
- cuenta por pagar única por documento de compra;
- saldo no negativo;
- saldo menor o igual al total.

### V2

Se preparan tablas transicionales:

```text
compras.documento_compra
cartera.documento_pagar
```

Estas tablas no sustituyen todavía el modelo V1; preparan la migración ERP por fases.

## 9. Cambios frontend

Se agregan modelos TypeScript para:

```text
DocumentoCompraSummary
DocumentoPagarSummary
CompraFinancieraSummary
RegistrarDocumentoCompraRequest
```

Y métodos en `ApiClientService` para:

```text
registrarDocumentoCompraOrden
getDocumentoCompraOrden
getCuentasPagar
```

No se rediseña la UX/UI.

## 10. Riesgos

- Si una orden se documenta antes de recibir mercadería, se crean saldos falsos.
- Si se calcula el subtotal desde lo pedido y no desde lo recibido, se sobredimensiona la cuenta por pagar.
- Si se permite duplicar documento por orden, se duplican obligaciones.
- Si se mezcla pago/contabilidad en esta tanda, la clase crece demasiado.

## 11. Criterios de aceptación

La tanda se considera terminada si:

- una orden recibida puede generar documento de compra;
- el documento de compra genera cuenta por pagar;
- no se puede generar documento dos veces para la misma orden;
- no se puede documentar una orden sin recepción;
- la cuenta por pagar queda con saldo válido;
- el contrato API declara los endpoints nuevos;
- la validación SQL incluye compras/cuentas por pagar;
- no se rediseña la UX/UI.

## 12. Pruebas mínimas

En máquina local:

```bat
scripts\test-backend.bat
```

Prueba manual sugerida:

```http
POST /api/v1/auth/login
POST /api/v1/abastecimiento/ordenes-compra/{id}/recibir
POST /api/v1/abastecimiento/ordenes-compra/{id}/documento-compra
GET  /api/v1/abastecimiento/ordenes-compra/{id}/documento-compra
GET  /api/v1/abastecimiento/cuentas-pagar
```

## 13. Nota para siguiente tanda

La siguiente tanda es T15 — Terceros unificados.

T15 debe empezar a preparar la unificación conceptual de cliente/proveedor/empleado bajo tercero común, sin romper las pantallas actuales de Clientes y Proveedores.
