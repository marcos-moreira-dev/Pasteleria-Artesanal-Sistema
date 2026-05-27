# T19 — Bridges ERP separados

## Objetivo

Conectar operaciones ya existentes con consecuencias ERP internas sin convertir un servicio en una clase gigante ni automatizar flujos todavía.

La tanda agrega bridges idempotentes para:

- pedido → documento por cobrar;
- documento por cobrar → asiento de venta;
- cobranza → asiento de cobro;
- documento por pagar → asiento de compra;
- pago proveedor → asiento de pago.

## Criterio de diseño

Cedro usa un bridge ERP amplio como referencia funcional. En Pastelería se decidió separar la responsabilidad para evitar un `PasteleriaErpBridgeService` monstruoso.

Los bridges quedan separados por dominio:

- `VentaErpBridgeService`;
- `CajaErpBridgeService`;
- `CompraErpBridgeService`;
- `CuentasPagarErpBridgeService`;
- `ContabilidadBridgeService` como fachada contable común.

## Alcance

Se agregan endpoints internos bajo:

```text
/api/v1/erp-bridges
```

Todos son idempotentes: si la consecuencia ERP ya existe para el origen, devuelven la consecuencia existente y no duplican documentos ni asientos.

## Fuera de alcance

No se automatiza todavía dentro de los servicios de pedidos, caja, compras o cuentas por pagar. La ejecución automática debe decidirse luego de más validación funcional.

No se agrega fiscalidad/SRI, no se generan documentos fiscales y no se toca frontend.

## Cuentas usadas inicialmente

- `1.2.01` — Clientes nacionales.
- `4.1.01` — Ventas de pastelería.
- `1.1.01` — Caja principal.
- `2.1.01` — Proveedores nacionales.
- `5.1.01` — Costo de insumos de producción.

Son cuentas internas iniciales, suficientes para trazabilidad ERP básica.

## Validación

Se agrega `12_validate_erp_bridges.sql` para revisar:

- no duplicar asientos por `origen_tipo + origen_id`;
- que los asientos bridge estén cuadrados;
- que los orígenes referencien documentos/cobranzas/pagos existentes.

## Nota de continuidad

La siguiente tanda, T20, debe implementar fiscalidad preparada y prudente. Los bridges actuales no deben prometer emisión fiscal real.
