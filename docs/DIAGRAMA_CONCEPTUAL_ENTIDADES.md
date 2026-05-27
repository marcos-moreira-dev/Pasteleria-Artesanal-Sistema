# 📊 Diagrama Conceptual - Pastelería App

## 🎯 Propósito de este documento

Este es un **diagrama conceptual** (no técnico, no normalizado) para entender:
- Qué entidades existen en el negocio
- Qué atributos tiene cada una
- Cómo se relacionan entre sí

**Ideal para:** Dibujar a mano en papel o crear diagramas en draw.io/pgAdmin

---

## 🏢 ENTIDADES PRINCIPALES

### 1. CLIENTE 👤
**Definición:** Persona que compra en la pastelería

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| cliente_id | BIGINT (PK) | Identificador único |
| nombre_completo | VARCHAR | Nombre del cliente |
| telefono | VARCHAR | Teléfono de contacto |
| correo | VARCHAR | Email |
| observaciones | VARCHAR | Notas especiales |
| fecha_registro | TIMESTAMP | Cuándo se registró |

**Relaciones:**
- Tiene MUCHOS **Pedidos** (1:N)
- Tiene MUCHAS **Cotizaciones** (1:N)

---

### 2. PRODUCTO 🧁
**Definición:** Lo que vende la pastelería (tortas, cupcakes, etc.)

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| producto_id | BIGINT (PK) | ID único |
| codigo | VARCHAR | Código interno (ej: TORTA-001) |
| nombre | VARCHAR | Nombre del producto |
| slug | VARCHAR | Nombre para URLs (torta-chocolate) |
| descripcion | VARCHAR | Descripción detallada |
| precio_base | NUMERIC | Precio de venta |
| activo | BOOLEAN | ¿Está disponible? |
| publicado | BOOLEAN | ¿Aparece en catálogo? |
| requiere_cotizacion | BOOLEAN | ¿Precio variable? |
| receta_json | TEXT | Receta en formato JSON |
| categoria_id | BIGINT (FK) | A qué categoría pertenece |

**Relaciones:**
- Pertenece a UNA **Categoría** (N:1)
- Tiene UNA **Receta** (1:1)
- Aparece en MUCHOS **Pedidos** (1:N)
- Aparece en MUCHAS **Cotizaciones** (1:N)

---

### 3. CATEGORIA_PRODUCTO 📁
**Definición:** Agrupación de productos (Tortas, Cupcakes, etc.)

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| categoria_id | BIGINT (PK) | ID único |
| codigo | VARCHAR | Código de categoría |
| nombre | VARCHAR | Nombre (ej: "Tortas") |
| descripcion | VARCHAR | Descripción |
| activo | BOOLEAN | ¿Activa? |
| orden_visual | INTEGER | Orden en catálogo |

**Relaciones:**
- Tiene MUCHOS **Productos** (1:N)

---

### 4. PEDIDO 📋
**Definición:** Orden de compra confirmada por un cliente

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| pedido_id | BIGINT (PK) | ID único del pedido |
| codigo | VARCHAR | Código (ej: PED-00001) |
| cliente_id | BIGINT (FK) | Quién pidió |
| cotizacion_id | BIGINT (FK) | De qué cotización viene (opcional) |
| estado_pedido | VARCHAR | Estado: REGISTRADO, EN_PREPARACION, LISTO, ENTREGADO, CANCELADO |
| prioridad | VARCHAR | NORMAL o URGENTE |
| origen | VARCHAR | MOSTRADOR, PUBLICO, TELEFONO |
| total_estimado | NUMERIC | Valor total del pedido |
| fecha_pedido | TIMESTAMP | Cuándo se hizo |
| fecha_entrega_estimada | TIMESTAMP | Cuándo se entrega |
| fecha_entrega_real | TIMESTAMP | Cuándo se entregó (real) |
| observaciones | VARCHAR | Notas especiales |

**Relaciones:**
- Pertenece a UN **Cliente** (N:1)
- Puede venir de UNA **Cotizacion** (N:1)
- Tiene MUCHOS **Items de Pedido** (1:N)
- Genera UNA **Produccion** (1:1)

---

### 5. PEDIDO_DETALLE 📦
**Definición:** Cada producto dentro de un pedido

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| pedido_detalle_id | BIGINT (PK) | ID único |
| pedido_id | BIGINT (FK) | A qué pedido pertenece |
| producto_id | BIGINT (FK) | Qué producto es |
| cantidad | INTEGER | Cuántas unidades |
| precio_unitario | NUMERIC | Precio de cada una |
| subtotal | NUMERIC | cantidad × precio |
| descripcion_item | VARCHAR | Descripción específica |
| notas | VARCHAR | Notas (ej: "Sin azúcar") |

**Relaciones:**
- Pertenece a UN **Pedido** (N:1)
- Es UN **Producto** (N:1)

---

### 6. COTIZACION 💰
**Definición:** Presupuesto antes de confirmar un pedido

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| cotizacion_id | BIGINT (PK) | ID único |
| codigo | VARCHAR | Código (ej: COT-00001) |
| cliente_id | BIGINT (FK) | Para quién es |
| estado_cotizacion | VARCHAR | PENDIENTE, ACEPTADA, RECHAZADA |
| origen | VARCHAR | Desde dónde llegó |
| total_estimado | NUMERIC | Valor presupuestado |
| observaciones | VARCHAR | Notas |
| fecha_creacion | TIMESTAMP | Cuándo se creó |

**Relaciones:**
- Pertenece a UN **Cliente** (N:1)
- Tiene MUCHOS **Detalles de Cotización** (1:N)
- Puede convertirse en UN **Pedido** (1:0..1)

---

### 7. COTIZACION_DETALLE 📄
**Definición:** Cada producto dentro de una cotización

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| cotizacion_detalle_id | BIGINT (PK) | ID único |
| cotizacion_id | BIGINT (FK) | A qué cotización |
| producto_id | BIGINT (FK) | Qué producto |
| cantidad | INTEGER | Cuántas |
| precio_estimado | NUMERIC | Precio estimado |
| subtotal | NUMERIC | cantidad × precio |
| descripcion_item | VARCHAR | Descripción |
| notas | VARCHAR | Notas adicionales |

**Relaciones:**
- Pertenece a UNA **Cotizacion** (N:1)
- Es UN **Producto** (N:1)

---

### 8. PRODUCCION 🏭
**Definición:** El proceso de hacer el pedido (cocina + decoración)

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| produccion_id | BIGINT (PK) | ID único |
| pedido_id | BIGINT (FK) | Qué pedido se está haciendo |
| estado_produccion | VARCHAR | PENDIENTE, PREPARACION, DECORACION, EMPAQUE, FINALIZADO |
| prioridad_produccion | VARCHAR | NORMAL o URGENTE |
| fecha_inicio | TIMESTAMP | Cuándo empezó |
| fecha_finalizacion | TIMESTAMP | Cuándo terminó |
| observaciones_produccion | VARCHAR | Notas para cocina (ej: "Cliente quiere antes de las 6pm") |

**Relaciones:**
- Pertenece a UN **Pedido** (1:1)

---

### 9. RECETA 📖
**Definición:** La "fórmula" para hacer un producto

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| receta_id | BIGINT (PK) | ID único |
| producto_id | BIGINT (FK) | Para qué producto |
| nombre | VARCHAR | Nombre de la receta |
| rendimiento_base | NUMERIC | Para cuántas porciones rinde |
| costo_estimado | NUMERIC | Costo total de ingredientes |
| es_activa | BOOLEAN | ¿Está vigente? |
| observaciones | VARCHAR | Notas de preparación |
| created_by_user_id | BIGINT (FK) | Quién la creó |

**Relaciones:**
- Pertenece a UN **Producto** (1:1)
- Tiene MUCHOS **Detalles de Receta** (1:N)
- Creada por UN **Usuario** (N:1)

---

### 10. DETALLE_RECETA 🥚
**Definición:** Cada ingrediente que lleva una receta

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| detalle_receta_id | BIGINT (PK) | ID único |
| receta_id | BIGINT (FK) | A qué receta |
| ingrediente_id | BIGINT (FK) | Qué ingrediente |
| cantidad_base | NUMERIC | Cuánto necesita |
| es_para_porcion | BOOLEAN | ¿Es por cada porción? |
| rendimiento_por_unidad | NUMERIC | Factor de rendimiento |
| observaciones | VARCHAR | Notas específicas |

**Relaciones:**
- Pertenece a UNA **Receta** (N:1)
- Usa UN **Ingrediente** (N:1)

---

### 11. INGREDIENTE 🧂
**Definición:** Componentes de las recetas (ej: harina, huevos)

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| ingrediente_id | BIGINT (PK) | ID único |
| codigo | VARCHAR | Código (ej: HAR-001) |
| nombre | VARCHAR | Nombre ("Harina de trigo") |
| descripcion | VARCHAR | Descripción |
| stock_actual | NUMERIC | Cuánto hay en bodega |
| stock_minimo | NUMERIC | Cantidad mínima antes de reordenar |
| costo_referencial | NUMERIC | Precio aproximado |
| activo | BOOLEAN | ¿Se sigue usando? |
| umedida_id | BIGINT (FK) | Unidad de medida (gramos, litros) |

**Relaciones:**
- Tiene UNA **Unidad de Medida** (N:1)
- Aparece en MUCHAS **Recetas** (1:N)
- Se consume en **Movimientos de Inventario** (1:N)

---

### 12. INSUMO 📦
**Definición:** Materiales de empaque y operación (no comestibles)

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| insumo_id | BIGINT (PK) | ID único |
| codigo | VARCHAR | Código |
| nombre | VARCHAR | Nombre ("Caja para torta 30cm") |
| descripcion | VARCHAR | Descripción |
| stock_actual | NUMERIC | Cantidad disponible |
| stock_minimo | NUMERIC | Mínimo para alerta |
| costo_referencial | NUMERIC | Precio aproximado |
| activo | BOOLEAN | ¿Activo? |
| umedida_id | BIGINT (FK) | Unidad (unidades, docenas) |

**Relaciones:**
- Tiene UNA **Unidad de Medida** (N:1)
- Se compra a **Proveedores** (N:M)
- Aparece en **Órdenes de Compra** (1:N)

---

### 13. PROVEEDOR 🚚
**Definición:** Quién vende los insumos/ingredientes

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| proveedor_id | BIGINT (PK) | ID único |
| codigo | VARCHAR | Código interno |
| nombre | VARCHAR | Nombre empresa |
| correo | VARCHAR | Email |
| telefono | VARCHAR | Teléfono |
| direccion | VARCHAR | Dirección |
| observaciones | VARCHAR | Notas |
| activo | BOOLEAN | ¿Sigue siendo proveedor? |

**Relaciones:**
- Recibe MUCHAS **Órdenes de Compra** (1:N)
- Suministra MUCHOS **Items** (1:N)

---

### 14. ORDEN_COMPRA 📥
**Definición:** Pedido que se hace a un proveedor

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| orden_compra_id | BIGINT (PK) | ID único |
| codigo | VARCHAR | Código (OC-00001) |
| proveedor_id | BIGINT (FK) | A quién se compra |
| estado | VARCHAR | PENDIENTE, ENVIADA, RECIBIDA, CANCELADA |
| total_estimado | NUMERIC | Valor total |
| fecha_emision | TIMESTAMP | Cuándo se envió |
| fecha_entrega_esperada | TIMESTAMP | Cuándo debería llegar |
| fecha_entrega_real | TIMESTAMP | Cuándo llegó |
| observaciones | VARCHAR | Notas |
| activo | BOOLEAN | ¿Vigente? |

**Relaciones:**
- Va a UN **Proveedor** (N:1)
- Tiene MUCHOS **Detalles** (1:N)

---

### 15. ORDEN_COMPRA_DETALLE 📋
**Definición:** Cada item dentro de una orden de compra

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| orden_compra_detalle_id | BIGINT (PK) | ID único |
| orden_compra_id | BIGINT (FK) | A qué orden |
| item_id | BIGINT | ID del insumo/ingrediente |
| item_tipo | VARCHAR | "INSUMO" o "INGREDIENTE" |
| item_codigo | VARCHAR | Código del item |
| item_nombre | VARCHAR | Nombre del item |
| cantidad | INTEGER | Cuánto se pidió |
| cantidad_recibida | INTEGER | Cuánto llegó |
| precio_unitario | NUMERIC | Precio de cada uno |
| subtotal | NUMERIC | cantidad × precio |
| observaciones | VARCHAR | Notas |

**Relaciones:**
- Pertenece a UNA **Orden de Compra** (N:1)

---

### 16. INVENTARIO_MOVIMIENTO 📊
**Definición:** Cada entrada o salida del inventario

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| inventario_movimiento_id | BIGINT (PK) | ID único |
| item_id | BIGINT | Qué item se movió |
| item_tipo | VARCHAR | "INSUMO" o "INGREDIENTE" |
| cantidad | NUMERIC | Cuánto (positivo=entrada, negativo=salida) |
| saldo_posterior | NUMERIC | Stock después del movimiento |
| tipo_movimiento | VARCHAR | ENTRADA, SALIDA, AJUSTE |
| fecha_movimiento | TIMESTAMP | Cuándo ocurrió |
| referencia_tipo | VARCHAR | Qué lo causó (PEDIDO, ORDEN_COMPRA, etc.) |
| referencia_id | VARCHAR | ID de la referencia |
| motivo_salida | VARCHAR | Por qué salió |
| observaciones | VARCHAR | Notas |
| registrado_por_user_id | BIGINT (FK) | Quién registró |

**Relaciones:**
- Registrado por UN **Usuario** (N:1)

---

### 17. USUARIO_SISTEMA 👨‍💼
**Definición:** Personas que usan el sistema administrativo

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| usuario_id | BIGINT (PK) | ID único |
| nombre_usuario | VARCHAR | Username (login) |
| password_hash | VARCHAR | Contraseña encriptada |
| nombres | VARCHAR | Nombres reales |
| apellidos | VARCHAR | Apellidos |
| correo | VARCHAR | Email |
| activo | BOOLEAN | ¿Puede usar el sistema? |
| rol_id | BIGINT (FK) | Qué rol tiene |

**Relaciones:**
- Tiene UN **Rol** (N:1)
- Genera MUCHOS **Movimientos de Inventario** (1:N)
- Recibe MUCHAS **Notificaciones** (1:N)

---

### 18. ROL_USUARIO 🎭
**Definición:** Tipos de usuario y sus permisos

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| rol_id | BIGINT (PK) | ID único |
| codigo | VARCHAR | Código (ADMIN, PRODUCCION, etc.) |
| nombre_rol | VARCHAR | Nombre legible |
| descripcion | VARCHAR | Qué puede hacer |
| activo | BOOLEAN | ¿Está activo? |

**Relaciones:**
- Tiene MUCHOS **Usuarios** (1:N)

**Roles típicos:**
- **ADMIN:** Todo el sistema
- **PRODUCCION:** Solo módulo de producción
- **ATENCION:** Pedidos y clientes
- **ABASTECIMIENTO:** Inventario y compras

---

### 19. NOTIFICACION 🔔
**Definición:** Mensajes del sistema para usuarios

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| notificacion_id | BIGINT (PK) | ID único |
| usuario_destino_id | BIGINT (FK) | Para quién es |
| tipo_notificacion | VARCHAR | Categoría |
| prioridad | VARCHAR | BAJA, MEDIA, ALTA |
| titulo | VARCHAR | Título breve |
| mensaje | VARCHAR | Contenido |
| modulo | VARCHAR | De qué módulo viene |
| estado | VARCHAR | PENDIENTE, LEIDA |
| fecha_creacion | TIMESTAMP | Cuándo se creó |
| fecha_lectura | TIMESTAMP | Cuándo la leyeron |
| payload_json | JSONB | Datos adicionales |

**Relaciones:**
- Va a UN **Usuario** (N:1)

---

### 20. AUDITORIA_EVENTO 📝
**Definición:** Registro de todo lo que pasa en el sistema

**Atributos:**
| Atributo | Tipo | Descripción |
|----------|------|-------------|
| auditoria_evento_id | BIGINT (PK) | ID único |
| actor_usuario_id | BIGINT (FK) | Quién hizo la acción |
| actor_rol | VARCHAR | Rol en ese momento |
| accion | VARCHAR | Qué hizo (CREAR, ACTUALIZAR, ELIMINAR) |
| entidad | VARCHAR | Sobre qué tabla |
| entidad_id | VARCHAR | ID del registro afectado |
| modulo | VARCHAR | Módulo del sistema |
| motivo | VARCHAR | Por qué lo hizo |
| valor_anterior_json | JSONB | Datos antes del cambio |
| valor_nuevo_json | JSONB | Datos después del cambio |
| fecha_evento | TIMESTAMP | Cuándo ocurrió |
| ip_origen | VARCHAR | IP desde donde se hizo |
| request_id | VARCHAR | ID de la petición HTTP |
| codigo_evento | VARCHAR | Código del evento |

**Relaciones:**
- Hecho por UN **Usuario** (N:1)

---

## 🔗 RESUMEN DE RELACIONES

```
CLIENTE (1) ────────┬────────────────┐
                    │                │
                    ▼                ▼
                 PEDIDO (N)     COTIZACION (N)
                    │                │
                    ▼                ▼
            PEDIDO_DETALLE     COTIZACION_DETALLE
                 (N)              (N)
                    │                │
                    └────────┬───────┘
                             │
                             ▼
                        PRODUCTO (N) ←──────┐
                             │               │
                             ▼               │
                        CATEGORIA_PRODUCTO (1)
                             │
                             ▼
                        RECETA (1)
                             │
                             ▼
                    DETALLE_RECETA (N)
                             │
                             ▼
                       INGREDIENTE (N) ←────┬──────────┐
                             │               │          │
                             ▼               ▼          ▼
                       UMEDIDA (1)    PROVEEDOR (N) ───┘
                             │               │
                             │               ▼
                             │        ITEM_PROVEEDOR
                             │              (N)
                             │               │
                             │               ▼
                             │        ORDEN_COMPRA (N)
                             │               │
                             │               ▼
                             │        ORDEN_COMPRA_DETALLE
                             │               (N)
                             │
                             ▼
                    INVENTARIO_MOVIMIENTO

PEDIDO (1) ←────── PRODUCCION (1)

USUARIO_SISTEMA (1) ────────┬────────────────┐
                            │                │
                            ▼                ▼
                    NOTIFICACION (N)   ROL_USUARIO (1)
                            │
                            ▼
                    AUDITORIA_EVENTO (N)
```

---

## 💡 NOTAS PARA DIBUJAR EL DIAGRAMA

### Colores sugeridos:
- 🟦 **Azul:** Entidades de negocio (Cliente, Pedido, Producto)
- 🟩 **Verde:** Inventario y abastecimiento (Ingrediente, Proveedor)
- 🟨 **Amarillo:** Producción (Produccion, Receta)
- 🟥 **Rojo:** Seguridad y auditoría (Usuario, Auditoria)
- ⬜ **Gris:** Catálogos auxiliares (Categoria, Unidad de Medida)

### Cardinalidad (números en relaciones):
- **1:1** = Uno a uno (Pedido → Produccion)
- **1:N** = Uno a muchos (Cliente → Pedidos)
- **N:M** = Muchos a muchos (Implementado con tabla intermedia)

### Tips:
1. No dibujes todas las entidades si es muy grande
2. Agrupa por módulos: Ventas, Producción, Inventario, Seguridad
3. Pon solo los atributos más importantes (nombre, estado, total)
4. Las FK (Foreign Keys) conectan las líneas entre entidades

---

## 🎓 CONCEPTOS IMPORTANTES

### ¿Qué es el "Dominio"?
El **dominio** es el negocio real que estamos modelando: una pastelería con clientes, pedidos, producción, etc.

### ¿Qué es el "Modelo Conceptual"?
Es un dibujo de alto nivel para entender:
- Qué cosas existen en el negocio (ENTIDADES)
- Qué información tienen (ATRIBUTOS)
- Cómo se relacionan (RELACIONES)

### ¿Por qué no es 3FN?
Porque:
- 3FN (Tercera Forma Normal) es para base de datos técnicas
- El modelo conceptual es para ENTENDER, no para implementar
- En conceptual popresentación funcionals tener redundancias si ayudan a entender
- Es como un boceto a lápiz antes del dibujo final

### Flujo típico de trabajo:
```
1. LEVANTAMIENTO (entrevistas) → Entender el negocio
2. MODELO CONCEPTUAL (este documento) → Dibujar entidades y relaciones
3. MODELO LÓGICO (ER) → Normalizar, definir claves
4. MODELO FÍSICO (SQL) → Crear tablas, índices, constraints
```

---

**Documento creado para dibujar diagramas conceptuales del sistema de pastelería**
