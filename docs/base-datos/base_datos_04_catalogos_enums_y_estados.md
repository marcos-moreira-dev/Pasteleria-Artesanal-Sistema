# /base-datos/04_catalogos_enums_y_estados.md

# 04 — Catálogos, enums y estados del sistema

## 1. Propósito del documento

Este documento define los **valores controlados del sistema** que representan estados o clasificaciones. Estos valores pueden implementarse mediante:

- ENUMs en la base de datos
- tablas catálogo
- enumeraciones en el backend

Su objetivo es evitar valores arbitrarios en campos importantes y mantener consistencia en la lógica del sistema.

---

# 2. Estrategia general

En el sistema se utilizarán dos enfoques principales:

**ENUMs** para estados pequeños y estables.

**Tablas catálogo** cuando los valores puedan cambiar o expandirse con el tiempo.

---

# 3. Estados de pedido

Campo relacionado:

```
pedido.estado_pedido
```

Valores permitidos:

```
REGISTRADO
EN_PREPARACION
LISTO
ENTREGADO
CANCELADO
```

### Significado

| Estado | Descripción |
|------|-------------|
| REGISTRADO | Pedido creado en el sistema |
| EN_PREPARACION | Pedido en proceso de elaboración |
| LISTO | Pedido terminado y disponible |
| ENTREGADO | Pedido entregado al cliente |
| CANCELADO | Pedido cancelado |

---

# 4. Estados de cotización

Campo relacionado:

```
cotizacion.estado_cotizacion
```

Valores permitidos:

```
PENDIENTE
APROBADA
RECHAZADA
CONVERTIDA
```

### Significado

| Estado | Descripción |
|------|-------------|
| PENDIENTE | Cotización enviada o registrada |
| APROBADA | Cliente acepta la cotización |
| RECHAZADA | Cliente decide no continuar |
| CONVERTIDA | Cotización transformada en pedido |

---

# 5. Estados de producción

Campo relacionado:

```
produccion.estado_produccion
```

Valores permitidos:

```
PENDIENTE
EN_PROCESO
FINALIZADO
```

### Significado

| Estado | Descripción |
|------|-------------|
| PENDIENTE | Producción aún no inicia |
| EN_PROCESO | Producto en elaboración |
| FINALIZADO | Producción completada |

---

# 6. Estados de producto

Campo relacionado:

```
producto.activo
```

Valores posibles:

```
TRUE
FALSE
```

### Significado

| Valor | Descripción |
|------|-------------|
| TRUE | Producto disponible para pedidos |
| FALSE | Producto deshabilitado o fuera de catálogo |

---

# 7. Catálogo de roles de usuario

Tabla relacionada:

```
rol_usuario
```

Valores iniciales sugeridos:

| Rol | Descripción |
|----|-------------|
| ADMINISTRADOR | Control completo del sistema |
| ATENCION | Registro de clientes y pedidos |
| PRODUCCION | Gestión del estado de producción |

Estos valores pueden ampliarse en el futuro.

---

# 8. Catálogo de categorías de producto

Tabla relacionada:

```
categoria_producto
```

Ejemplos iniciales:

| Categoría |
|----------|
| TORTAS |
| POSTRES |
| GALLETAS |
| BEBIDAS |

Este catálogo es completamente extensible.

---

# 9. Beneficios del uso de catálogos y enums

El uso de valores controlados permite:

- evitar errores de datos
- simplificar validaciones
- mejorar consistencia del sistema
- facilitar lógica de negocio en backend

Además, permite representar estados de forma clara en la interfaz del usuario.

---

# 10. Conclusión

Los catálogos y enums permiten mantener consistencia en valores críticos del sistema.

Este documento servirá como referencia para implementar enumeraciones en el backend, constraints en la base de datos y validaciones en los frontends.

