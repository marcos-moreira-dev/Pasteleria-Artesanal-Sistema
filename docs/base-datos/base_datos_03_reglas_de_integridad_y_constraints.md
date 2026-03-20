# /base-datos/03_reglas_de_integridad_y_constraints.md

# 03 — Reglas de integridad y constraints

## 1. Propósito del documento

Este documento define las **reglas de integridad de datos** que deben cumplirse en la base de datos del sistema.

Las reglas de integridad garantizan que la información almacenada sea:

- consistente
- válida
- coherente con el dominio del negocio

Estas reglas se implementarán principalmente mediante **constraints en la base de datos** y validaciones en el backend.

---

# 2. Tipos de integridad considerados

El sistema utilizará principalmente cuatro tipos de integridad:

- integridad de entidad
- integridad referencial
- integridad de dominio
- integridad de negocio

---

# 3. Integridad de entidad

La integridad de entidad garantiza que cada registro pueda identificarse de forma única.

Reglas aplicadas:

- todas las tablas poseen **clave primaria**
- las claves primarias no pueden ser NULL

Ejemplo:

```
cliente.cliente_id
pedido.pedido_id
producto.producto_id
```

Estas claves serán autogeneradas.

---

# 4. Integridad referencial

La integridad referencial asegura que las relaciones entre tablas sean válidas.

Se implementa mediante **claves foráneas (FOREIGN KEY)**.

### Relaciones principales

```
pedido.cliente_id → cliente.cliente_id
producto.categoria_id → categoria_producto.categoria_id
pedido_detalle.pedido_id → pedido.pedido_id
pedido_detalle.producto_id → producto.producto_id
cotizacion.cliente_id → cliente.cliente_id
cotizacion_detalle.cotizacion_id → cotizacion.cotizacion_id
produccion.pedido_id → pedido.pedido_id
usuario_sistema.rol_id → rol_usuario.rol_id
```

Estas relaciones impiden referencias a registros inexistentes.

---

# 5. Integridad de dominio

La integridad de dominio define qué valores son válidos para ciertos campos.

### Ejemplos

Cantidad de productos:

```
cantidad > 0
```

Precio:

```
precio_base >= 0
precio_unitario >= 0
subtotal >= 0
```

Campos booleanos:

```
activo = TRUE | FALSE
```

---

# 6. Estados válidos

Los estados del sistema deben limitarse a valores definidos.

### Estados de pedido

Valores permitidos:

```
REGISTRADO
EN_PREPARACION
LISTO
ENTREGADO
CANCELADO
```

### Estados de cotización

```
PENDIENTE
APROBADA
RECHAZADA
CONVERTIDA
```

### Estados de producción

```
PENDIENTE
EN_PROCESO
FINALIZADO
```

Estos estados pueden implementarse mediante:

- CHECK constraints
- ENUMs
- tablas catálogo

---

# 7. Restricciones de unicidad

Algunos campos deben ser únicos dentro del sistema.

### Ejemplos

Usuarios del sistema:

```
usuario_sistema.nombre_usuario UNIQUE
```

Categorías:

```
categoria_producto.nombre UNIQUE
```

---

# 8. Reglas de consistencia de fechas

Existen reglas temporales que deben cumplirse.

Ejemplo:

```
fecha_entrega >= fecha_pedido
```

Esto evita inconsistencias en los pedidos.

---

# 9. Reglas derivadas

Algunos valores pueden derivarse de otros.

Ejemplo:

```
subtotal = cantidad * precio_unitario
```

Esta regla puede aplicarse en:

- backend
- triggers
- lógica de aplicación

---

# 10. Estrategia de implementación

Las reglas de integridad se implementarán en tres niveles:

1. **Base de datos**

   - primary keys
   - foreign keys
   - check constraints
   - unique constraints

2. **Backend**

   - validaciones de dominio
   - reglas de negocio

3. **Frontend**

   - validaciones de formulario

Este enfoque reduce la probabilidad de datos inválidos.

---

# 11. Conclusión

Las reglas de integridad son fundamentales para mantener la calidad y consistencia de la información del sistema.

Este documento define las bases para implementar constraints robustos en la base de datos y mantener la coherencia del modelo relacional.

