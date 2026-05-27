# 07 — Catálogo de reportes y consultas

## 1. Propósito del documento

Este documento identifica los principales reportes y consultas que el sistema debe permitir para apoyar la operación diaria y la supervisión general del negocio.

Su objetivo es definir qué información necesita consultar el personal de la pastelería, con qué propósito y en qué contexto operativo.

Este documento no define todavía consultas SQL ni implementaciones técnicas específicas. Su enfoque es funcional y orientado a la necesidad de información del negocio.

---

## 2. Importancia de reportes y consultas

Además del registro de información, el sistema debe permitir recuperar datos útiles para la toma de decisiones y la operación diaria.

Las consultas y reportes permiten, por ejemplo:

- revisar pedidos pendientes
- organizar la producción del día
- consultar pedidos por cliente
- verificar pedidos próximos a entregar
- tener una visión general del estado operativo del negocio

---

## 3. Consultas operativas principales

### 3.1 Pedidos pendientes

Permite visualizar los pedidos que aún no han sido entregados.

Información esperada:

- cliente
- fecha de entrega
- estado del pedido
- observaciones relevantes

Uso principal:

- personal de atención
- personal de producción
- administrador

---

### 3.2 Pedidos del día

Permite consultar los pedidos que deben atenderse o entregarse en la fecha actual.

Información esperada:

- cliente
- productos solicitados
- hora o fecha de entrega
- estado del pedido

Uso principal:

- personal de atención
- personal de producción

---

### 3.3 Pedidos próximos a entregar

Permite identificar pedidos cuya fecha de entrega se acerca.

Uso principal:

- planificación de producción
- prevención de retrasos

---

### 3.4 Pedidos por cliente

Permite consultar el historial de pedidos asociados a un cliente específico.

Información esperada:

- pedidos realizados
- fechas
- estados
- observaciones generales

Uso principal:

- atención al cliente
- seguimiento comercial

---

### 3.5 Cotizaciones pendientes

Permite consultar las cotizaciones registradas que aún no han sido aprobadas, rechazadas o convertidas en pedido.

Uso principal:

- seguimiento comercial
- atención al cliente

---

### 3.6 Producción pendiente

Permite visualizar los pedidos que aún requieren preparación.

Información esperada:

- pedido asociado
- fecha de entrega
- estado de producción
- observaciones internas

Uso principal:

- personal de producción
- administrador

---

## 4. Reportes operativos sugeridos

### 4.1 Resumen de pedidos por estado

Reporte que muestra cuántos pedidos existen en cada estado operativo.

Ejemplos:

- registrados
- en preparación
- listos para entrega
- entregados
- cancelados

Uso principal:

- administrador
- supervisión general

---

### 4.2 Resumen de cotizaciones por estado

Reporte que permite conocer cuántas cotizaciones están pendientes, aprobadas, rechazadas o convertidas en pedido.

Uso principal:

- seguimiento comercial
- análisis del proceso de cotización

---

### 4.3 Resumen de producción

Reporte orientado a visualizar la carga operativa de producción.

Información posible:

- cantidad de pedidos en preparación
- cantidad de pedidos finalizados
- pedidos pendientes por fecha

Uso principal:

- personal de producción
- administrador

---

## 5. Filtros y criterios de consulta

Las consultas del sistema deberían permitir, al menos en una etapa razonable del proyecto, filtrar información por criterios como:

- fecha de entrega
- estado
- cliente
- tipo de pedido

Estos filtros ayudan a que el personal encuentre rápidamente la información que necesita.

---

## 6. Prioridad dentro del proyecto

En la primera etapa del sistema, las consultas y reportes más prioritarios son:

- pedidos pendientes
- pedidos del día
- producción pendiente
- pedidos por cliente
- cotizaciones pendientes

Los reportes más analíticos o avanzados pueden dejarse para una etapa posterior.

---

## 7. Relación con otros documentos del proyecto

Este documento se relaciona directamente con:

- los procesos clave del negocio
- el catálogo de estados y transiciones
- los casos de uso prioritarios
- el diseño de la base de datos
- el diseño del frontend administrativo

Las consultas y reportes deben estar alineados con la información que el sistema registra y con las decisiones operativas que el negocio necesita tomar.

---

## 8. Conclusión

El catálogo de reportes y consultas define la información más útil que el sistema debe poner a disposición de los usuarios internos.

Este documento será una referencia importante para el diseño de consultas de base de datos, endpoints del backend y pantallas del frontend administrativo.

