# Backend — Bridges ERP separados

T19 agrega el paquete:

```text
com.pasteleria.erpbridge
```

## Servicios

- `VentaErpBridgeService`.
- `CajaErpBridgeService`.
- `CompraErpBridgeService`.
- `CuentasPagarErpBridgeService`.
- `ContabilidadBridgeService`.

## Controller

```text
ErpBridgeController
```

Expone endpoints administrativos para ejecutar bridges de forma explícita.

## Seguridad

Los bridges reutilizan permisos de los servicios llamados:

- creación de documento por cobrar: permiso de cobranzas;
- creación de asientos: permiso de registrar asientos.

## Decisión importante

No se insertó lógica bridge dentro de `PedidoService`, `CarteraCommandService`, `CuentasPagarCommandService` ni compras. La automatización queda para una fase posterior si el flujo completo ya está validado.
