# 02 - Normalizacion del modelo a 3FN

## 1. Proposito

Este documento consolida el paso desde la 2FN a la Tercera Forma Normal (3FN) para la base de datos de Pasteleria.

La meta es remover dependencias transitivas y dejar un modelo comun de negocio real:

- limpio
- mantenible
- trazable
- y listo para pasar al modelo logico relacional oficial

---

## 2. Criterio de 3FN aplicado

Una tabla esta en 3FN cuando:

- ya cumple 2FN
- y sus atributos no dependen de otro atributo no clave

Traducido a este proyecto: una tabla no debe cargar datos descriptivos que en realidad pertenecen a otra entidad.

---

## 3. Correcciones tipicas que se evitan

### 3.1 Producto y categoria

Incorrecto:

- guardar `categoria_nombre` dentro de `producto`

Correcto:

- guardar `categoria_id` en `producto`
- mantener la descripcion de categoria en `categoria_producto`

### 3.2 Pedido y cliente

Incorrecto:

- guardar `cliente_nombre` y `cliente_telefono` directamente en `pedido`

Correcto:

- guardar `cliente_id` en `pedido`

### 3.3 Usuario y rol

Incorrecto:

- guardar `nombre_rol`, `descripcion_rol` y permisos textuales en cada usuario

Correcto:

- guardar `rol_id` en `usuario_sistema`
- y mantener la semantica del rol en `rol_usuario`

### 3.4 Cotizacion y pedido

Incorrecto:

- fusionar ambas entidades por comodidad

Correcto:

- mantener `cotizacion` separada de `pedido`
- y sostener la conversion con una relacion controlada

### 3.5 Pedido y produccion

Incorrecto:

- convertir `pedido` en una tabla mixta comercial y operativa

Correcto:

- mantener `produccion` como entidad separada

---

## 4. Dependencias transitivas removidas

Al cerrar 3FN, el modelo evita al menos estas dependencias transitivas:

- categoria descrita dentro del producto
- cliente descrito dentro del pedido
- producto descrito dentro del detalle de pedido mas alla de lo estrictamente pactado
- rol descrito dentro del usuario
- estado comercial o de produccion resuelto con texto libre incontrolado

---

## 5. Beneficios practicos

Con 3FN el sistema gana:

- menos redundancia
- menos inconsistencias al actualizar datos
- mejor trazabilidad entre negocio y DB
- mejor base para constraints e indices
- mejor base para backend modular

---

## 6. Paso siguiente

Una vez consolidada la 3FN, el siguiente documento canonico es:

- `base_datos_00_modelo_logico_relacional.md`

Ese archivo representa el modelo logico relacional oficial de la V1.

