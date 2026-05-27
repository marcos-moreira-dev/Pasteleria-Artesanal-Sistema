# 00 - UX/UI por producto

## 1. Proposito

Este documento baja la capa UX/UI del proyecto a decisiones usables por otra IA
o por implementacion manual.

Su criterio es:

- marca y captacion para la cara publica
- claridad guiada para la solicitud publica
- sobriedad operativa para el admin
- alta legibilidad para produccion

---

## 2. Principio rector

No conviene usar el mismo lenguaje visual para todas las superficies.

Regla practica:

- si la superficie vende o presenta marca, puede ser mas calida
- si la superficie administra, debe ser sistematica
- si la superficie opera bajo presion, debe ser utilitaria y muy clara

---

## 3. Frontend publico en Astro

### Objetivo

Presentar el negocio, mostrar catalogo y guiar al cliente hacia contacto con
solicitud de cotizacion.

### Usuario principal

Cliente potencial o recurrente.

### Lenguaje visual recomendado

- calido
- limpio
- comercial
- artesanal sin exceso decorativo

### Evitar

- parecer ecommerce completo
- tablas de backoffice a la vista del cliente
- ruido visual gratuito

---

## 4. Solicitud publica de cotizacion

### Objetivo

Convertir una necesidad del cliente en una solicitud clara y profesional.

### Patron UX recomendado

En la V1 actual vive en la pagina de contacto.

Debe sentirse:

- guiada
- amable
- clara
- sin lenguaje de checkout

### Evitar

- lenguaje de pago
- formularios gigantes sin contexto
- ambiguedad sobre lo que se envio

---

## 5. Frontend administrativo en Angular

### Objetivo

Operar clientes, productos, cotizaciones, pedidos, produccion, reportes y
abastecimiento.

### Usuario principal

Personal interno.

### Lenguaje visual recomendado

- sobrio
- legible
- repetible
- centrado en tablas, filtros y formularios

### Evitar

- visual de marketing dentro del admin
- componentes gigantes sin jerarquia
- formularios kilométricos sin agrupacion

---

## 6. Panel de produccion

### Objetivo

Permitir lectura rapida de pedidos, prioridad, observaciones y cambios de
estado.

### Patron recomendado

- lista operativa o tablero
- prioridad visible
- acciones claras
- densidad controlada

---

## 7. Reglas transversales de UX/UI

1. Contraste suficiente y foco visible.
2. El color no debe ser la unica senal de estado.
3. Un boton debe parecer boton.
4. Los formularios deben diferenciar obligatorio y opcional.
5. Los errores del backend deben traducirse a mensajes humanos.
6. La parte publica puede expresar marca; la interna no debe perder sobriedad.

---

## 8. Temas profesionales que puedes estudiar aqui

- jerarquia visual por contexto
- diseno de formularios
- UX operativa
- accesibilidad
- feedback de error y exito
- separacion entre experiencia comercial y experiencia administrativa
