# /base-datos/02_diccionario_de_datos.md

# 02 — Diccionario de datos

## 1. Propósito del documento

El diccionario de datos describe **cada tabla y cada columna del modelo relacional**, explicando su significado dentro del dominio del negocio.

Este documento sirve como puente entre:

- el modelo lógico de base de datos
- la implementación SQL
- el backend (entidades, DTOs, validaciones)

También ayuda a evitar ambigüedades sobre qué representa cada campo.

---

# 2. Tabla: cliente

Representa a las personas que realizan pedidos o solicitan cotizaciones.

| Campo | Tipo sugerido | Null | Descripción |
|------|---------------|------|-------------|
| cliente_id | BIGSERIAL | NO | Identificador único del cliente |
| nombre | VARCHAR(120) | NO | Nombre completo del cliente |
| telefono | VARCHAR(30) | SI | Teléfono de contacto |
| correo | VARCHAR(120) | SI | Correo electrónico |
| observaciones | TEXT | SI | Notas internas sobre el cliente |
| fecha_registro | TIMESTAMP | NO | Fecha de registro en el sistema |

---

# 3. Tabla: categoria_producto

Agrupa los productos del catálogo en categorías.

| Campo | Tipo sugerido | Null | Descripción |
|------|---------------|------|-------------|
| categoria_id | BIGSERIAL | NO | Identificador de la categoría |
| nombre | VARCHAR(100) | NO | Nombre de la categoría |
| descripcion | TEXT | SI | Descripción opcional |

---

# 4. Tabla: producto

Representa productos ofrecidos por la pastelería.

| Campo | Tipo sugerido | Null | Descripción |
|------|---------------|------|-------------|
| producto_id | BIGSERIAL | NO | Identificador del producto |
| categoria_id | BIGINT | NO | Categoría a la que pertenece |
| nombre | VARCHAR(150) | NO | Nombre del producto |
| descripcion | TEXT | SI | Descripción del producto |
| precio_base | NUMERIC(10,2) | NO | Precio base del producto |
| activo | BOOLEAN | NO | Indica si el producto está disponible |

---

# 5. Tabla: pedido

Representa pedidos realizados por clientes.

| Campo | Tipo sugerido | Null | Descripción |
|------|---------------|------|-------------|
| pedido_id | BIGSERIAL | NO | Identificador del pedido |
| cliente_id | BIGINT | NO | Cliente que realiza el pedido |
| fecha_pedido | TIMESTAMP | NO | Fecha en que se registra el pedido |
| fecha_entrega | TIMESTAMP | NO | Fecha estimada de entrega |
| estado_pedido | VARCHAR(40) | NO | Estado actual del pedido |
| observaciones | TEXT | SI | Notas del pedido |
| total_estimado | NUMERIC(10,2) | SI | Total estimado del pedido |

---

# 6. Tabla: pedido_detalle

Contiene los productos incluidos en un pedido.

| Campo | Tipo sugerido | Null | Descripción |
|------|---------------|------|-------------|
| pedido_detalle_id | BIGSERIAL | NO | Identificador del detalle |
| pedido_id | BIGINT | NO | Pedido al que pertenece |
| producto_id | BIGINT | NO | Producto solicitado |
| cantidad | INTEGER | NO | Cantidad solicitada |
| precio_unitario | NUMERIC(10,2) | NO | Precio unitario aplicado |
| subtotal | NUMERIC(10,2) | NO | Subtotal calculado |

---

# 7. Tabla: cotizacion

Representa propuestas previas a un pedido confirmado.

| Campo | Tipo sugerido | Null | Descripción |
|------|---------------|------|-------------|
| cotizacion_id | BIGSERIAL | NO | Identificador de la cotización |
| cliente_id | BIGINT | NO | Cliente asociado |
| fecha_cotizacion | TIMESTAMP | NO | Fecha de creación |
| estado_cotizacion | VARCHAR(40) | NO | Estado actual |
| observaciones | TEXT | SI | Notas o especificaciones |
| total_estimado | NUMERIC(10,2) | SI | Valor estimado |

---

# 8. Tabla: cotizacion_detalle

Describe elementos incluidos en una cotización.

| Campo | Tipo sugerido | Null | Descripción |
|------|---------------|------|-------------|
| cotizacion_detalle_id | BIGSERIAL | NO | Identificador del detalle |
| cotizacion_id | BIGINT | NO | Cotización asociada |
| descripcion_item | TEXT | NO | Descripción del producto solicitado |
| cantidad | INTEGER | NO | Cantidad estimada |
| precio_estimado | NUMERIC(10,2) | NO | Precio estimado |
| subtotal | NUMERIC(10,2) | NO | Subtotal estimado |

---

# 9. Tabla: produccion

Registra el progreso de preparación de pedidos.

| Campo | Tipo sugerido | Null | Descripción |
|------|---------------|------|-------------|
| produccion_id | BIGSERIAL | NO | Identificador del registro |
| pedido_id | BIGINT | NO | Pedido asociado |
| estado_produccion | VARCHAR(40) | NO | Estado del proceso |
| fecha_inicio | TIMESTAMP | SI | Inicio de preparación |
| fecha_finalizacion | TIMESTAMP | SI | Finalización |
| observaciones_produccion | TEXT | SI | Notas internas |

---

# 10. Tabla: rol_usuario

Define roles disponibles en el sistema.

| Campo | Tipo sugerido | Null | Descripción |
|------|---------------|------|-------------|
| rol_id | BIGSERIAL | NO | Identificador del rol |
| nombre_rol | VARCHAR(80) | NO | Nombre del rol |
| descripcion | TEXT | SI | Descripción del rol |

---

# 11. Tabla: usuario_sistema

Usuarios internos que operan el sistema.

| Campo | Tipo sugerido | Null | Descripción |
|------|---------------|------|-------------|
| usuario_id | BIGSERIAL | NO | Identificador del usuario |
| nombre_usuario | VARCHAR(120) | NO | Nombre de usuario |
| contraseña_hash | VARCHAR(255) | NO | Hash de contraseña |
| rol_id | BIGINT | NO | Rol asociado |
| activo | BOOLEAN | NO | Indica si el usuario está activo |

---

# 12. Conclusión

El diccionario de datos proporciona una descripción clara y estructurada de cada tabla y campo del sistema.

Este documento será utilizado posteriormente para:

- generar el esquema SQL
- definir entidades del backend
- validar reglas de negocio

