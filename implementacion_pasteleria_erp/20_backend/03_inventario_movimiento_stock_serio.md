# InventarioMovimientoService y stock serio

## Principio

El inventario no debe modificarse como una simple actualización de `stock_actual` sin historia. Cada cambio debe quedar representado como movimiento.

## Regla funcional

```text
cantidad > 0 siempre
ENTRADA_* suma stock
SALIDA_* resta stock
```

Las salidas deben tener motivo u observación. Los movimientos deben tener referencia operativa.

## Estado transicional

La V1 todavía usa:

```text
ingrediente.stock_actual
insumo.stock_actual
inventario_movimiento.item_tipo
inventario_movimiento.item_id
```

La V2 deberá migrar hacia:

```text
inventario.item_maestro
inventario.stock_almacen
inventario.movimiento_inventario
inventario.movimiento_inventario_detalle
```

## Patrón de servicio

```text
validar permiso
validar tipo item
validar tipo movimiento
validar cantidad
bloquear item con PESSIMISTIC_WRITE
calcular saldo anterior
calcular saldo posterior
actualizar stock
crear movimiento inmutable
auditar
```

## Reglas de refactoring

- No meter reglas de inventario en controllers.
- No duplicar validación de saldo en compras/producción.
- Usar `InventarioMovimientoPolicy` para reglas de naturaleza/cantidad/saldo.
- Usar `InventarioMovimientoCommandService` como punto de entrada para mutaciones.
- En V2, separar item/almacén/detalle de movimiento.
