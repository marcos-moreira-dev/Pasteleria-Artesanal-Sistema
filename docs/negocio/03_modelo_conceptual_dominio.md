# 03 — Modelo conceptual del dominio

## 1. Propósito del documento

Este documento describe el modelo conceptual del dominio del sistema para la pastelería. Su objetivo es identificar las principales entidades del negocio y las relaciones generales entre ellas.

El modelo conceptual representa la información importante del dominio desde una perspectiva de negocio. En esta etapa no se definen tablas, tipos de datos, claves primarias, claves foráneas ni detalles de implementación técnica.

---

## 2. Objetivo del modelo conceptual

El modelo conceptual permite:

- identificar los elementos principales del negocio
- comprender cómo se relaciona la información
- establecer una base para el diseño lógico de la base de datos
- facilitar la comunicación entre análisis del negocio y diseño del sistema

---

## 3. Entidades principales del dominio

A continuación se describen las entidades conceptuales más relevantes del dominio de la pastelería.

### 3.1 Cliente

Representa a la persona que compra productos o solicita pedidos especiales.

Información relevante:

- nombre
- teléfono de contacto
- observaciones

Un cliente puede realizar uno o varios pedidos.

---

### 3.2 Categoría de producto

Representa una clasificación general de los productos que ofrece la pastelería.

Ejemplos:

- porciones
- tortas completas
- cupcakes
- postres individuales

Una categoría agrupa a varios productos.

---

### 3.3 Producto

Representa un producto que la pastelería ofrece para la venta o como base para un pedido.

Ejemplos:

- porción de torta de chocolate
- torta completa de vainilla
- cupcake decorado

Información relevante:

- nombre del producto
- categoría
- descripción general
- precio base
- disponibilidad

Un producto pertenece a una categoría.

---

### 3.4 Pedido

Representa una solicitud formal realizada por un cliente.

Puede tratarse de:

- compra programada de un producto estándar
- pedido de torta completa
- pedido personalizado

Información relevante:

- cliente asociado
- fecha de registro
- fecha de entrega
- estado del pedido
- observaciones generales

Un pedido pertenece a un cliente y puede incluir uno o varios productos solicitados.

---

### 3.5 Detalle de pedido

Representa cada elemento o producto específico incluido dentro de un pedido.

Permite describir con mayor precisión qué se solicitó.

Información relevante:

- producto asociado
- cantidad
- precio acordado
- descripción específica
- observaciones particulares

Un pedido puede tener uno o varios detalles de pedido.

---

### 3.6 Cotización

Representa una propuesta preliminar asociada a una solicitud, especialmente en el caso de tortas personalizadas.

Información relevante:

- cliente asociado
- descripción de la solicitud
- precio estimado
- fecha estimada de entrega
- estado de la cotización

Una cotización puede convertirse posteriormente en un pedido confirmado.

---

### 3.7 Producción

Representa la planificación o seguimiento de la preparación de un pedido o de parte de un pedido.

Información relevante:

- pedido asociado
- fecha planificada
- estado de producción
- observaciones internas

Esta entidad permite reflejar la parte operativa del trabajo de cocina o taller.

---

### 3.8 Usuario del sistema

Representa a una persona autorizada para utilizar el sistema interno.

Ejemplos:

- administrador
- personal de atención
- personal de producción

Información relevante:

- nombre
- rol
- estado

Un usuario puede registrar información o consultar módulos del sistema según su función.

---

## 4. Relaciones conceptuales principales

A continuación se presentan las relaciones más importantes entre las entidades del dominio.

### 4.1 Cliente — Pedido

- un cliente puede realizar uno o varios pedidos
- cada pedido pertenece a un único cliente

### 4.2 Categoría de producto — Producto

- una categoría puede agrupar uno o varios productos
- cada producto pertenece a una única categoría

### 4.3 Pedido — Detalle de pedido

- un pedido puede contener uno o varios detalles
- cada detalle de pedido pertenece a un único pedido

### 4.4 Producto — Detalle de pedido

- un producto puede aparecer en varios detalles de pedido
- cada detalle de pedido hace referencia a un producto

### 4.5 Cliente — Cotización

- un cliente puede solicitar una o varias cotizaciones
- cada cotización pertenece a un único cliente

### 4.6 Cotización — Pedido

- una cotización puede derivar en un pedido confirmado
- un pedido puede originarse a partir de una cotización previa

### 4.7 Pedido — Producción

- un pedido puede generar una planificación de producción
- la producción se encuentra asociada al pedido que debe elaborarse

---

## 5. Observaciones sobre el dominio

El dominio de la pastelería combina actividades comerciales y operativas.

Desde el punto de vista comercial, se requiere gestionar clientes, productos, pedidos y cotizaciones.

Desde el punto de vista operativo, se necesita dar seguimiento a la preparación de pedidos y a la coordinación de entregas.

El modelo conceptual refleja esta combinación de dimensiones dentro del mismo sistema.

---

## 6. Límites del modelo conceptual

En esta etapa todavía no se detallan:

- atributos técnicos
- tipos de datos
- identificadores formales
- restricciones físicas de base de datos
- estructuras de pantallas
- endpoints o servicios

Estos elementos se desarrollarán en documentos posteriores.

---

## 7. Conclusión

El modelo conceptual del dominio identifica las principales entidades del negocio y muestra las relaciones generales entre ellas.

Este documento servirá como base para construir el modelo lógico relacional y la documentación posterior de la base de datos y del backend.