# Pedidos y producción — Máquina de estados operativa

## Principio

La pastelería no debe cambiar estados de pedido o producción como simples valores sueltos. Cada transición debe obedecer una máquina de estados y dejar trazabilidad.

## Pedido

Estados V1 conservados por compatibilidad:

```text
REGISTRADO
EN_PREPARACION
LISTO
ENTREGADO
CANCELADO
```

Flujo válido:

```text
REGISTRADO → EN_PREPARACION → LISTO → ENTREGADO
```

Cancelación válida antes de entrega:

```text
REGISTRADO → CANCELADO
EN_PREPARACION → CANCELADO
LISTO → CANCELADO
```

Terminales:

```text
ENTREGADO
CANCELADO
```

## Producción

Estados V1 conservados, con agregado `CANCELADO`:

```text
PENDIENTE
PREPARACION
DECORACION
EMPAQUE
FINALIZADO
CANCELADO
```

Flujo válido:

```text
PENDIENTE → PREPARACION → DECORACION → EMPAQUE → FINALIZADO
```

Cancelación válida antes de finalizar:

```text
PENDIENTE → CANCELADO
PREPARACION → CANCELADO
DECORACION → CANCELADO
EMPAQUE → CANCELADO
```

Terminales:

```text
FINALIZADO
CANCELADO
```

## Sincronización

- Producción `FINALIZADO` mueve pedido a `LISTO`.
- Pedido `ENTREGADO` exige producción `FINALIZADO`.
- Pedido `CANCELADO` cancela producción activa.
- Producción no puede avanzar si el pedido está cancelado o entregado.

## Pendiente para T13

- receta técnica;
- consumo de materiales;
- lote de producción;
- entrada de producto terminado;
- integración con inventario.
