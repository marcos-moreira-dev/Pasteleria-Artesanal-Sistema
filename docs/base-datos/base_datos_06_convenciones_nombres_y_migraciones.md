# /base-datos/06_convenciones_nombres_y_migraciones.md

# 06 — Convenciones de nombres y migraciones

## 1. Propósito del documento

Este documento define las **convenciones de nomenclatura** y la **estrategia de migraciones** para la base de datos del sistema.

El objetivo es mantener consistencia en:

- nombres de tablas
- nombres de columnas
- claves primarias y foráneas
- constraints
- índices
- archivos de migración

Además, este documento sirve como guía para que el esquema evolucione de forma ordenada junto con el backend.

---

# 2. Principios generales de nomenclatura

Las convenciones elegidas buscan que la base de datos sea:

- legible
- consistente
- fácil de mantener
- fácil de mapear al backend

Principios adoptados:

- nombres en **snake_case**
- nombres en singular para tablas principales
- nombres descriptivos y explícitos
- evitar abreviaturas innecesarias

---

# 3. Convención para nombres de tablas

Formato general:

```text
nombre_entidad
```

Ejemplos:

- `cliente`
- `producto`
- `categoria_producto`
- `pedido`
- `pedido_detalle`
- `cotizacion`
- `cotizacion_detalle`
- `produccion`
- `usuario_sistema`
- `rol_usuario`

Criterios:

- usar sustantivos del dominio
- mantener singular cuando represente una entidad
- usar nombre compuesto cuando ayude a la claridad

---

# 4. Convención para nombres de columnas

Formato general:

```text
nombre_atributo
```

Ejemplos:

- `nombre`
- `telefono`
- `fecha_registro`
- `fecha_entrega`
- `estado_pedido`
- `precio_base`

Criterios:

- evitar columnas ambiguas como `descripcion1`, `valor`, `dato`
- usar prefijos de contexto cuando sea necesario
- mantener coherencia semántica entre tablas

---

# 5. Convención para claves primarias

Formato general:

```text
{tabla}_id
```

Ejemplos:

- `cliente_id`
- `producto_id`
- `pedido_id`
- `cotizacion_id`
- `produccion_id`

Ventajas:

- claridad al hacer joins
- fácil identificación de relaciones
- coherencia con entidades del backend

---

# 6. Convención para claves foráneas

Las claves foráneas reutilizan el nombre de la clave primaria de la tabla referenciada.

Ejemplos:

- `pedido.cliente_id`
- `producto.categoria_id`
- `pedido_detalle.pedido_id`
- `pedido_detalle.producto_id`
- `produccion.pedido_id`
- `usuario_sistema.rol_id`

Esto facilita la lectura del modelo y el mapeo ORM.

---

# 7. Convención para constraints

Formato sugerido:

### Foreign keys

```text
fk_{tabla_origen}_{tabla_destino}
```

Ejemplos:

- `fk_pedido_cliente`
- `fk_producto_categoria`
- `fk_detalle_pedido`
- `fk_detalle_producto`
- `fk_produccion_pedido`

### Unique constraints

```text
uq_{tabla}_{campo}
```

Ejemplos:

- `uq_categoria_producto_nombre`
- `uq_usuario_sistema_nombre_usuario`

### Check constraints

```text
chk_{tabla}_{regla}
```

Ejemplos:

- `chk_pedido_fecha_entrega`
- `chk_producto_precio_base`
- `chk_pedido_detalle_cantidad`

---

# 8. Convención para índices

Formato sugerido:

```text
idx_{tabla}_{campo}
```

Ejemplos:

- `idx_pedido_cliente_id`
- `idx_pedido_estado_pedido`
- `idx_pedido_fecha_entrega`
- `idx_produccion_estado_produccion`
- `idx_cotizacion_estado_cotizacion`

Para índices compuestos:

```text
idx_{tabla}_{campo1}_{campo2}
```

Ejemplo:

- `idx_pedido_estado_fecha_entrega`

---

# 9. Convención para columnas de auditoría

Si el sistema usa auditoría común en varias tablas, se recomienda estandarizar nombres como:

- `created_at`
- `updated_at`
- `deleted_at`
- `created_by`
- `updated_by`

Si se desea mantener todo en español, la convención podría ser:

- `fecha_creacion`
- `fecha_actualizacion`
- `fecha_eliminacion`
- `creado_por`
- `actualizado_por`

Lo importante es **elegir una sola convención y mantenerla** en todo el proyecto.

---

# 10. Estrategia de migraciones

La evolución del esquema debe gestionarse mediante **migraciones versionadas**, no mediante cambios manuales directos sobre la base de datos.

Herramienta sugerida para Spring Boot:

- **Flyway**

Ventajas:

- control de versiones
- historial claro
- reproducibilidad
- despliegues más seguros

---

# 11. Convención para archivos de migración

Formato general:

```text
V{numero}__descripcion.sql
```

Ejemplos:

- `V1__init_schema.sql`
- `V2__seed_roles_y_categorias.sql`
- `V3__crear_indices_iniciales.sql`
- `V4__agregar_tabla_inventario.sql`

Reglas:

- usar números consecutivos
- descripción corta pero explícita
- no modificar migraciones ya ejecutadas
- cada cambio estructural debe ir en un archivo nuevo

---

# 12. Estructura recomendada para migraciones

Dentro del backend Spring Boot:

```text
src/main/resources/db/migration/
```

Ejemplo:

```text
src/main/resources/db/migration/
  V1__init_schema.sql
  V2__seed_roles_y_categorias.sql
  V3__indices_iniciales.sql
```

---

# 13. Reglas de trabajo con migraciones

Buenas prácticas recomendadas:

- nunca editar una migración ya ejecutada en otros ambientes
- crear una nueva migración para cada cambio relevante
- mantener migraciones pequeñas y con un propósito claro
- registrar cambios estructurales antes de modificar código dependiente
- validar las migraciones en ambiente local antes de subirlas al repositorio

---

# 14. Relación con el backend

Estas convenciones ayudan a que exista consistencia entre:

- tablas SQL
- entidades JPA
- repositorios
- DTOs
- documentación del proyecto

Por ejemplo:

- tabla `pedido` → entidad `Pedido`
- columna `fecha_entrega` → atributo `fechaEntrega`
- constraint `fk_pedido_cliente` → relación `@ManyToOne`

---

# 15. Conclusión

Las convenciones de nombres y migraciones permiten mantener una base de datos consistente, legible y fácil de evolucionar.

Este documento sirve como referencia para la creación del esquema, el versionado con Flyway y la integración limpia entre la base de datos y el backend del sistema.
