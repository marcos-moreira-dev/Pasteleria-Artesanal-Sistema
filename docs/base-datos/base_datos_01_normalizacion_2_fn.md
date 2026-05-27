# 01 - Normalizacion del modelo a 2FN

## 1. Proposito

Este documento explica como la propuesta de persistencia de Pasteleria se estabiliza en Segunda Forma Normal (2FN) antes de consolidarse en 3FN.

La idea no es hacer teoria vacia, sino justificar por que la DB se descompone en tablas utiles y comunes de negocio real.

---

## 2. Punto de partida que se evita

En un sistema pobremente modelado, podrian mezclarse en una sola estructura datos de:

- cliente
- pedido
- productos
- cotizacion
- produccion

Eso provocaria:

- duplicacion de datos del cliente
- repeticion de datos del producto
- filas gigantes con muchos nulos
- y dificultad para controlar transiciones operativas

---

## 3. Criterio de 2FN aplicado

La 2FN exige que los atributos de una tabla dependan completamente de su clave.

En Pasteleria esto se refleja asi:

- los datos del cliente viven en `cliente`, no en `pedido`
- los datos base del producto viven en `producto`, no en `pedido_detalle`
- los datos del encabezado del pedido viven en `pedido`
- los items del pedido viven en `pedido_detalle`
- los datos de la cotizacion viven en `cotizacion`
- los items o componentes de la cotizacion viven en `cotizacion_detalle`

---

## 4. Descomposiciones principales

### 4.1 Pedido y detalle de pedido

En vez de guardar multiples productos dentro del pedido, se separa:

- `pedido`
- `pedido_detalle`

Con eso:

- el pedido conserva sus datos de cabecera
- el detalle conserva cantidad, precio pactado y subtotal

### 4.2 Cotizacion y detalle de cotizacion

En vez de mezclar todos los componentes personalizados en una sola fila, se separa:

- `cotizacion`
- `cotizacion_detalle`

Esto permite registrar propuestas personalizadas sin forzar que todo sea un producto catalogado.

### 4.3 Producto y categoria

El producto no arrastra los datos completos de la categoria en cada fila.

Se separa:

- `categoria_producto`
- `producto`

### 4.4 Usuario y rol

El usuario no repite los atributos descriptivos del rol en cada fila.

Se separa:

- `rol_usuario`
- `usuario_sistema`

---

## 5. Resultado de 2FN

Al cerrar 2FN, la DB ya debe dejar separadas estas responsabilidades:

- identidad del cliente
- catalogo comercial
- transaccion comercial
- detalle transaccional
- flujo previo de cotizacion
- seguimiento operativo de produccion
- acceso al sistema

---

## 6. Verificaciones minimas

Antes de pasar a 3FN conviene verificar:

- que `pedido_detalle` no replique descripcion completa del cliente
- que `pedido` no replique nombre de producto por comodidad
- que `cotizacion` no replique datos del cliente que ya existen en `cliente`
- que `produccion` no intente absorber todo el pedido dentro de la misma tabla

Si eso se cumple, el modelo ya esta en una 2FN funcional y profesional para V1.

