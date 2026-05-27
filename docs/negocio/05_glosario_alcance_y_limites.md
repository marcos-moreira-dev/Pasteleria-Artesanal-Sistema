# 05 — Glosario, alcance y límites del sistema

## 1. Propósito del documento

Este documento define el vocabulario principal utilizado dentro del proyecto, así como el alcance funcional del sistema y los límites de lo que se incluirá en esta primera etapa.

El objetivo es evitar ambigüedades en el uso de términos del dominio y establecer claramente qué partes del negocio serán cubiertas por el sistema.

---

## 2. Glosario del dominio

### Cliente
Persona que compra productos de la pastelería o solicita un pedido especial.

### Producto
Elemento que la pastelería ofrece para la venta o preparación. Puede ser un producto estándar o formar parte de un pedido personalizado.

### Categoría de producto
Clasificación general de productos, utilizada para organizar el catálogo.

Ejemplos:

- tortas
- cupcakes
- postres
- porciones

### Pedido
Solicitud formal registrada en el sistema que representa un compromiso de preparación y entrega de uno o varios productos.

### Detalle de pedido
Elemento específico dentro de un pedido que describe un producto solicitado, su cantidad y posibles observaciones.

### Cotización
Propuesta preliminar de precio para un pedido, especialmente en casos de productos personalizados.

Una cotización puede convertirse posteriormente en un pedido confirmado.

### Producción
Proceso interno mediante el cual se preparan los productos solicitados en los pedidos.

### Usuario del sistema
Persona autorizada para utilizar el sistema interno de la pastelería.

Ejemplos:

- administrador
- personal de atención
- personal de producción

---

## 3. Alcance del sistema

En esta etapa, el sistema se enfocará en apoyar las operaciones internas del negocio.

Las áreas principales que cubrirá el sistema incluyen:

- gestión de clientes
- gestión de productos
- registro y seguimiento de pedidos
- planificación básica de producción
- consulta de información operativa

Además, el sistema podrá incluir una funcionalidad pública limitada para solicitar cotizaciones o pedidos personalizados.

---

## 4. Componentes principales del sistema

El proyecto contempla los siguientes componentes de software:

### Backend
Sistema central que gestiona la lógica del negocio, el acceso a datos y los servicios utilizados por los frontends.

### Base de datos
Sistema encargado de almacenar la información estructurada del negocio.

### Frontend público
Sitio web orientado a clientes que presenta información del negocio y permite solicitar cotizaciones.

### Frontend administrativo
Aplicación utilizada por el personal del negocio para gestionar clientes, productos, pedidos y producción.

---

## 5. Límites del sistema

Para mantener el alcance del proyecto manejable, algunas funcionalidades no se incluirán en la primera etapa.

Entre ellas:

- comercio electrónico completo
- procesamiento de pagos en línea
- integración directa con plataformas externas de mensajería
- aplicaciones móviles dedicadas
- sistemas avanzados de logística o reparto

Estas funcionalidades podrían evaluarse en etapas futuras.

---

## 6. Límites del modelo del negocio

El modelo conceptual del dominio busca representar los aspectos principales del negocio, pero no pretende capturar todos los detalles posibles de una operación real.

Por ejemplo, inicialmente:

- el inventario puede modelarse de forma simple
- los procesos internos de producción no se modelarán en múltiples etapas complejas
- el sistema se enfocará en pedidos y planificación básica

---

## 7. Conclusión

El glosario, el alcance y los límites del sistema permiten establecer un marco claro para el desarrollo del proyecto.

Este documento ayuda a mantener consistencia en el uso de términos y evita que el proyecto crezca de forma descontrolada al definir explícitamente qué se incluirá y qué se dejará fuera en esta primera etapa.