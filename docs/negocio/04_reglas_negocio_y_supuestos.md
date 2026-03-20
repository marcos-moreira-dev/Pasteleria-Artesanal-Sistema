# 04 — Reglas de negocio y supuestos

## 1. Propósito del documento

Este documento identifica las reglas de negocio generales y los supuestos iniciales del sistema para la pastelería.

Las reglas de negocio representan condiciones, restricciones o criterios que deben cumplirse dentro de la operación del negocio. Los supuestos representan decisiones preliminares tomadas para poder avanzar en el análisis y diseño del sistema mientras no exista una validación definitiva con un negocio real.

Este documento se mantiene en un nivel conceptual y funcional. No define todavía restricciones técnicas de base de datos, validaciones de interfaz ni reglas de implementación específicas.

---

## 2. Reglas generales del negocio

### 2.1 Sobre los clientes

- Todo pedido debe estar asociado a un cliente identificado.
- Un cliente puede tener uno o varios pedidos registrados.
- Un cliente puede solicitar una o varias cotizaciones.
- El teléfono de contacto del cliente es importante para coordinar entregas o confirmar pedidos.

---

### 2.2 Sobre los productos

- Todo producto ofrecido por la pastelería debe pertenecer a una categoría.
- Un producto puede estar disponible o no disponible para venta según la operación del negocio.
- El precio base de un producto puede servir como referencia para pedidos estándar.
- Los productos personalizados pueden requerir ajustes de precio respecto al producto base.

---

### 2.3 Sobre los pedidos

- Todo pedido debe tener una fecha de registro.
- Todo pedido programado debe tener una fecha de entrega.
- Un pedido debe encontrarse en un estado operativo identificable.
- Un pedido puede contener uno o varios detalles.
- Un pedido no debe considerarse entregado si todavía no ha sido marcado como preparado o listo para entrega.
- Un pedido puede originarse directamente o a partir de una cotización previa.

---

### 2.4 Sobre los detalles de pedido

- Cada detalle de pedido debe estar asociado a un producto.
- Cada detalle de pedido debe indicar al menos una cantidad.
- Un detalle de pedido puede incluir observaciones específicas diferentes a las observaciones generales del pedido.
- El precio acordado en un detalle de pedido puede diferir del precio base del producto, especialmente en pedidos personalizados.

---

### 2.5 Sobre las cotizaciones

- Una cotización representa una propuesta preliminar y no implica confirmación automática del pedido.
- Una cotización puede estar pendiente, aprobada, rechazada o convertida en pedido, según la operación definida por el negocio.
- Una cotización aprobada puede dar origen a un pedido formal.

---

### 2.6 Sobre producción

- Todo pedido que requiera preparación debe poder visualizarse en la planificación de producción.
- La producción debe organizarse considerando la fecha de entrega comprometida con el cliente.
- Un pedido no debería pasar a estado entregado sin haber pasado antes por una etapa operativa previa.
- La producción puede incluir observaciones internas que no necesariamente son visibles para el cliente.

---

### 2.7 Sobre pagos y anticipos

- Un pedido puede requerir anticipo según el tipo de producto o nivel de personalización.
- El negocio puede registrar pagos parciales antes de la entrega.
- El saldo pendiente, si existe, debe quedar claro antes de la entrega del pedido.
- Un pedido puede quedar registrado aunque aún no esté totalmente pagado, siempre que el negocio lo permita.

---

### 2.8 Sobre usuarios del sistema

- Todo usuario del sistema debe tener un rol definido.
- Los permisos de acceso pueden variar según el rol del usuario.
- No todas las personas del negocio necesitan acceso a toda la información del sistema.

---

## 3. Reglas operativas sugeridas

Estas reglas no necesariamente representan políticas universales, pero son razonables para una pastelería seria de tamaño pequeño a mediano.

- Los pedidos personalizados deben registrarse con suficiente detalle para evitar errores de producción.
- Los pedidos con fecha de entrega cercana deben ser visibles con prioridad operativa.
- El sistema debe permitir diferenciar entre pedidos estándar y pedidos personalizados.
- Los pedidos pendientes del día y de días próximos deben poder consultarse fácilmente.
- La información importante para producción debe estar separada de la información de atención al cliente cuando sea necesario.

---

## 4. Supuestos iniciales del proyecto

Para efectos del análisis inicial se asumen las siguientes condiciones.

### 4.1 Sobre el tamaño del negocio

- La pastelería tiene un solo local principal.
- El número de usuarios internos del sistema será reducido.
- El negocio combina venta directa y pedidos por encargo.

### 4.2 Sobre el alcance del sistema

- El sistema estará orientado primero a la operación interna.
- No se implementará inicialmente comercio electrónico completo.
- No se implementará inicialmente integración real con plataformas externas de mensajería.
- El sistema podrá contemplar un módulo de cotización pública o solicitud pública, pero no una plataforma de pagos compleja en la primera etapa.

### 4.3 Sobre la operación diaria

- La mayor parte de los pedidos se gestionará localmente o por canales simples de atención.
- La producción se coordina con base en pedidos programados y reposición de productos de vitrina.
- El negocio necesita más organización operativa que automatización compleja en una primera etapa.

### 4.4 Sobre el uso del sistema

- El sistema será utilizado principalmente por personal administrativo y operativo del negocio.
- El uso del sistema debe ser sencillo y directo.
- El sistema debe priorizar rapidez de registro y consulta sobre complejidad funcional innecesaria.

---

## 5. Supuestos de modelado del dominio

Para continuar con la documentación posterior se asumirán, por ahora, las siguientes decisiones conceptuales:

- Cliente, producto, pedido, detalle de pedido, cotización, producción y usuario del sistema son entidades principales del dominio.
- La cotización se modelará como un objeto separado del pedido.
- La producción se modelará como una parte operativa asociada al pedido.
- El seguimiento de estados será importante tanto para pedidos como para producción.
- El módulo de cotización pública y el sistema administrativo compartirán el mismo núcleo de negocio en el backend.

---

## 6. Riesgos o puntos a validar más adelante

Aunque estos supuestos son útiles para avanzar, algunos elementos deberán validarse posteriormente:

- si el negocio manejará inventario detallado por insumos o solo control básico
- si toda cotización aprobada se convierte obligatoriamente en pedido
- si los anticipos serán obligatorios en ciertos tipos de pedido
- si la producción se controlará por pedido completo o por etapas internas más detalladas
- si el sistema necesitará múltiples roles muy diferenciados o una estructura simple de acceso

---

## 7. Conclusión

Las reglas de negocio y los supuestos iniciales permiten delimitar mejor el comportamiento esperado del sistema antes de pasar al diseño lógico y técnico.

Este documento ayuda a evitar ambigüedades y servirá como referencia para la documentación de base de datos, backend y frontends en etapas posteriores.