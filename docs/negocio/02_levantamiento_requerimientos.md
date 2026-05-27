# 02 — Levantamiento de requerimientos del sistema

## 1. Propósito del documento

Este documento identifica los requerimientos del sistema de software que apoyará las operaciones de la pastelería descrita en los documentos anteriores.

Los requerimientos se derivan del análisis del funcionamiento actual del negocio y de las necesidades observadas durante el levantamiento de información.

En esta etapa **no se define la implementación técnica** del sistema. El objetivo es describir qué debe permitir hacer el sistema desde el punto de vista del negocio.

---

## 2. Objetivos generales del sistema

El sistema debe apoyar la organización y registro de las operaciones principales del negocio.

Los objetivos principales incluyen:

- organizar la información de clientes
- registrar pedidos de forma estructurada
- facilitar el seguimiento de pedidos
- apoyar la planificación de producción
- reducir errores en fechas de entrega
- permitir consultar información de pedidos y clientes

---

## 3. Alcance general del sistema

El sistema estará orientado principalmente al uso interno de la pastelería.

Sus principales áreas de apoyo serán:

- gestión de clientes
- gestión de productos
- registro de pedidos
- seguimiento de producción
- consulta de información operativa

El sistema no contempla inicialmente funciones avanzadas como comercio electrónico completo o integración directa con plataformas externas.

---

## 4. Requerimientos funcionales

### 4.1 Gestión de clientes

El sistema debe permitir:

- registrar nuevos clientes
- consultar información de clientes
- actualizar datos de contacto
- consultar pedidos realizados por un cliente

Información mínima de un cliente:

- nombre
- teléfono de contacto
- observaciones opcionales

---

### 4.2 Gestión de productos

El sistema debe permitir:

- registrar productos que ofrece la pastelería
- clasificar productos por categoría
- consultar productos disponibles
- actualizar información de productos

Ejemplos de productos:

- porciones de torta
- cupcakes
- tortas completas

---

### 4.3 Registro de pedidos

El sistema debe permitir registrar pedidos realizados por clientes.

Cada pedido debe incluir información como:

- cliente asociado
- producto solicitado
- descripción del pedido
- fecha de entrega
- observaciones

El sistema debe permitir consultar pedidos registrados.

---

### 4.4 Seguimiento de pedidos

El sistema debe permitir identificar el estado de un pedido.

Ejemplos de estados posibles:

- registrado
- en preparación
- listo para entrega
- entregado

El sistema debe permitir consultar pedidos pendientes.

---

### 4.5 Planificación de producción

El sistema debe permitir visualizar qué pedidos deben prepararse según la fecha de entrega.

Esto permite que el personal de producción organice el trabajo diario.

---

### 4.6 Cotización de tortas personalizadas

El sistema debe permitir registrar solicitudes de tortas personalizadas.

La información relevante puede incluir:

- tamaño de la torta
- sabor
- relleno
- tipo de decoración
- mensaje personalizado

Estas solicitudes pueden convertirse posteriormente en pedidos confirmados.

---

### 4.7 Consulta de información operativa

El sistema debe permitir consultar información relevante para la operación diaria.

Ejemplos:

- pedidos pendientes
- pedidos por fecha
- pedidos de un cliente específico

---

## 5. Requerimientos no funcionales

El sistema debe cumplir con ciertas características generales:

- ser fácil de usar para el personal del negocio
- permitir registrar información de forma rápida
- facilitar la consulta de pedidos y clientes

---

## 6. Supuestos iniciales

Durante esta etapa se consideran algunos supuestos:

- el sistema será utilizado por un número reducido de usuarios
- el sistema se utilizará principalmente dentro del local
- la información registrada debe mantenerse organizada y accesible

Estos supuestos pueden revisarse en etapas posteriores.

---

## 7. Conclusión

Este documento define los requerimientos principales del sistema desde el punto de vista del negocio.

En el siguiente documento se elaborará el modelo conceptual del dominio, donde se identificarán las principales entidades y relaciones que representan la información del sistema.

