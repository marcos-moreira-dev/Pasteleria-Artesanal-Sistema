# /base-datos/05_indices_y_consultas_clave.md

# 05 — Índices y consultas clave

## 1. Propósito del documento

Este documento identifica las **consultas más importantes del sistema** desde el punto de vista operativo y propone los **índices** que podrían ser necesarios para mejorar el rendimiento de la base de datos.

El objetivo es anticipar qué datos se consultarán con mayor frecuencia y cómo organizar la base de datos para responder de forma eficiente.

Este documento no define todavía optimizaciones avanzadas, pero sí establece una base razonable para el diseño físico de la base de datos.

---

## 2. Principios generales

Los índices deben crearse con criterio, considerando que:

- aceleran consultas frecuentes
- pueden mejorar filtros, búsquedas y ordenamientos
- también agregan costo en inserciones y actualizaciones

Por esta razón, se priorizan los índices sobre columnas utilizadas con frecuencia en:

- filtros
- búsquedas
- joins
- ordenamientos

---

## 3. Consultas operativas clave del sistema

### 3.1 Consultar pedidos pendientes

Uso típico:

- personal de atención
- personal de producción
- administrador

Filtros probables:

- estado del pedido
- fecha de entrega

Ejemplo conceptual:

```sql
SELECT *
FROM pedido
WHERE estado_pedido IN ('REGISTRADO', 'EN_PREPARACION', 'LISTO')
ORDER BY fecha_entrega ASC;
```

Índices sugeridos:

- índice sobre `pedido.estado_pedido`
- índice sobre `pedido.fecha_entrega`

---

### 3.2 Consultar pedidos del día

Uso típico:

- operación diaria
- panel de producción

Filtro probable:

- fecha de entrega

Ejemplo conceptual:

```sql
SELECT *
FROM pedido
WHERE DATE(fecha_entrega) = CURRENT_DATE;
```

Índice sugerido:

- índice sobre `pedido.fecha_entrega`

---

### 3.3 Consultar pedidos por cliente

Uso típico:

- atención al cliente
- historial comercial

Filtro probable:

- cliente_id

Ejemplo conceptual:

```sql
SELECT *
FROM pedido
WHERE cliente_id = ?
ORDER BY fecha_pedido DESC;
```

Índice sugerido:

- índice sobre `pedido.cliente_id`

---

### 3.4 Consultar producción pendiente

Uso típico:

- cocina
- administración

Filtros probables:

- estado de producción
- pedido asociado

Ejemplo conceptual:

```sql
SELECT *
FROM produccion
WHERE estado_produccion IN ('PENDIENTE', 'EN_PROCESO');
```

Índices sugeridos:

- índice sobre `produccion.estado_produccion`
- índice sobre `produccion.pedido_id`

---

### 3.5 Consultar cotizaciones pendientes

Uso típico:

- seguimiento comercial
- atención al cliente

Filtro probable:

- estado de cotización

Ejemplo conceptual:

```sql
SELECT *
FROM cotizacion
WHERE estado_cotizacion = 'PENDIENTE';
```

Índice sugerido:

- índice sobre `cotizacion.estado_cotizacion`

---

### 3.6 Consultar productos activos

Uso típico:

- frontend público
- registro de pedidos
- cotizaciones

Filtros probables:

- activo
- categoría

Ejemplo conceptual:

```sql
SELECT *
FROM producto
WHERE activo = TRUE
ORDER BY nombre ASC;
```

Índices sugeridos:

- índice sobre `producto.activo`
- índice sobre `producto.categoria_id`

---

## 4. Índices por claves foráneas

Además de los índices por consultas frecuentes, es conveniente indexar varias claves foráneas para mejorar joins y filtros.

Claves foráneas que deberían considerar índice:

- `producto.categoria_id`
- `pedido.cliente_id`
- `pedido_detalle.pedido_id`
- `pedido_detalle.producto_id`
- `cotizacion.cliente_id`
- `cotizacion_detalle.cotizacion_id`
- `produccion.pedido_id`
- `usuario_sistema.rol_id`

---

## 5. Índices sugeridos por tabla

### Tabla: cliente

Índices sugeridos:

- índice por `telefono` si se consulta con frecuencia
- índice por `correo` si se usa para búsqueda

### Tabla: categoria_producto

Índice sugerido:

- índice único por `nombre`

### Tabla: producto

Índices sugeridos:

- `categoria_id`
- `activo`
- índice único por `nombre` si el negocio lo requiere

### Tabla: pedido

Índices sugeridos:

- `cliente_id`
- `estado_pedido`
- `fecha_entrega`
- `fecha_pedido`

### Tabla: pedido_detalle

Índices sugeridos:

- `pedido_id`
- `producto_id`

### Tabla: cotizacion

Índices sugeridos:

- `cliente_id`
- `estado_cotizacion`
- `fecha_cotizacion`

### Tabla: cotizacion_detalle

Índice sugerido:

- `cotizacion_id`

### Tabla: produccion

Índices sugeridos:

- `pedido_id`
- `estado_produccion`

### Tabla: usuario_sistema

Índices sugeridos:

- índice único por `nombre_usuario`
- `rol_id`

---

## 6. Consultas para dashboard y reportes

Algunas consultas resumidas pueden ser útiles para dashboards:

- cantidad de pedidos por estado
- cantidad de cotizaciones por estado
- pedidos del día
- pedidos próximos a entregar
- producción pendiente

Estas consultas pueden requerir:

- agregaciones (`COUNT`, `GROUP BY`)
- filtros por fecha
- filtros por estado

En etapas posteriores podrían optimizarse mediante:

- vistas
- vistas materializadas
- consultas precalculadas

---

## 7. Estrategia inicial recomendada

Para una primera versión del sistema, se recomienda:

- crear índices sobre claves foráneas
- crear índices sobre estados operativos
- crear índices sobre fechas de pedido y entrega
- crear restricciones `UNIQUE` donde corresponda

Esto cubre la mayoría de consultas operativas del sistema sin agregar complejidad innecesaria.

---

## 8. Consideraciones futuras

Si el sistema creciera en volumen de datos o consultas, podrían evaluarse mejoras como:

- índices compuestos
- particionamiento por fecha
- vistas materializadas
- estrategias de archivado de pedidos antiguos

Estas decisiones no forman parte del alcance inicial.

---

## 9. Conclusión

La definición anticipada de consultas clave e índices sugeridos permite diseñar una base de datos más preparada para responder a las necesidades del negocio.

Este documento servirá como referencia para el diseño físico de PostgreSQL, la optimización del backend y la construcción de dashboards y paneles operativos.

