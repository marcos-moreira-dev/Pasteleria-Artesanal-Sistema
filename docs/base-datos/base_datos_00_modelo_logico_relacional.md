# /base-datos/00_modelo_logico_relacional.md

# 00 — Modelo lógico relacional

## 1. Propósito del documento

Este documento describe el **modelo lógico relacional** del sistema. Su objetivo es transformar el **modelo conceptual del dominio del negocio** en una estructura de datos que pueda implementarse en una base de datos relacional.

El modelo lógico define:

- entidades del sistema  
- relaciones entre entidades  
- atributos principales  
- claves primarias  
- claves foráneas  

Este modelo servirá como base para el diseño final de la base de datos y para la implementación del backend.

---

# 2. Principios del diseño del modelo

El modelo lógico del sistema sigue los siguientes principios:

- estructura relacional clara
- normalización razonable
- separación de entidades principales
- integridad referencial mediante claves foráneas
- facilidad de consulta para operaciones del negocio

El objetivo es obtener una estructura de datos **simple, clara y extensible**.

---

# 3. Entidades principales del sistema

Las entidades identificadas para el sistema son:

- cliente  
- producto  
- categoria_producto  
- pedido  
- pedido_detalle  
- cotizacion  
- cotizacion_detalle  
- produccion  
- usuario_sistema  
- rol_usuario  

Estas entidades representan los elementos principales del dominio del negocio.

---

# 4. Entidad: cliente

Representa a las personas que realizan pedidos en la pastelería.

### Atributos principales

- cliente_id (PK)
- nombre
- telefono
- correo
- observaciones
- fecha_registro

### Descripción

Un cliente puede realizar **varios pedidos y cotizaciones**.

---

# 5. Entidad: categoria_producto

Permite organizar los productos del catálogo.

### Atributos

- categoria_id (PK)
- nombre
- descripcion

### Ejemplo

- tortas
- postres
- galletas
- bebidas

---

# 6. Entidad: producto

Representa los productos ofrecidos por la pastelería.

### Atributos

- producto_id (PK)
- categoria_id (FK)
- nombre
- descripcion
- precio_base
- activo

### Relaciones

Un producto pertenece a una **categoría**.

---

# 7. Entidad: pedido

Representa un pedido realizado por un cliente.

### Atributos

- pedido_id (PK)
- cliente_id (FK)
- fecha_pedido
- fecha_entrega
- estado_pedido
- observaciones
- total_estimado

### Relaciones

Un pedido pertenece a **un cliente**.

Un pedido puede tener **varios detalles de pedido**.

---

# 8. Entidad: pedido_detalle

Representa los productos incluidos dentro de un pedido.

### Atributos

- pedido_detalle_id (PK)
- pedido_id (FK)
- producto_id (FK)
- cantidad
- precio_unitario
- subtotal

### Relaciones

- muchos detalles pertenecen a un pedido
- cada detalle hace referencia a un producto

---

# 9. Entidad: cotizacion

Representa una propuesta de pedido antes de confirmarse.

### Atributos

- cotizacion_id (PK)
- cliente_id (FK)
- fecha_cotizacion
- estado_cotizacion
- observaciones
- total_estimado

### Relaciones

Una cotización pertenece a un cliente.

Una cotización puede convertirse en **un pedido**.

---

# 10. Entidad: cotizacion_detalle

Representa los elementos incluidos dentro de una cotización.

### Atributos

- cotizacion_detalle_id (PK)
- cotizacion_id (FK)
- descripcion_item
- cantidad
- precio_estimado
- subtotal

---

# 11. Entidad: produccion

Representa el seguimiento de la preparación de un pedido.

### Atributos

- produccion_id (PK)
- pedido_id (FK)
- estado_produccion
- fecha_inicio
- fecha_finalizacion
- observaciones_produccion

### Relaciones

Cada registro de producción se asocia a un pedido.

---

# 12. Entidad: usuario_sistema

Representa a los usuarios internos que utilizan el sistema.

### Atributos

- usuario_id (PK)
- nombre_usuario
- contraseña_hash
- rol_id (FK)
- activo

---

# 13. Entidad: rol_usuario

Define los roles disponibles dentro del sistema.

### Atributos

- rol_id (PK)
- nombre_rol
- descripcion

### Ejemplos

- administrador
- atención
- producción

---

# 14. Relaciones principales del modelo

Las relaciones más importantes del modelo son:

Cliente  
→ Pedidos  
→ Cotizaciones  

Pedido  
→ Pedido_detalle  
→ Produccion  

Cotizacion  
→ Cotizacion_detalle  

Producto  
→ Categoria_producto  

Usuario_sistema  
→ Rol_usuario

---

# 15. Diagrama conceptual simplificado

Modelo relacional simplificado:

```
cliente
   |
   | 1
   |
   N
pedido -------- pedido_detalle -------- producto
   |
   |
produccion

cliente
   |
   |
cotizacion -------- cotizacion_detalle

producto -------- categoria_producto

usuario_sistema -------- rol_usuario
```

---

# 16. Consideraciones para la implementación

Al implementar este modelo en la base de datos se deberán considerar:

- claves primarias autogeneradas
- claves foráneas para mantener integridad
- índices para consultas frecuentes
- normalización adecuada de datos

---

# 17. Conclusión

El modelo lógico relacional define la estructura básica de datos del sistema. Este modelo permite representar de manera organizada la información necesaria para la operación de la pastelería.

En los siguientes documentos se profundizará en:

- normalización del modelo
- diccionario de datos
- restricciones de integridad
- optimización de consultas

