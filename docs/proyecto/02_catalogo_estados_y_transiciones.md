# 02 — Catálogo de estados y transiciones

## 1. Propósito del documento

Este documento define los diferentes **estados operativos** que pueden tener los elementos principales del sistema, así como las **transiciones permitidas entre dichos estados**.

El objetivo es establecer un lenguaje claro y consistente para describir el progreso de procesos como pedidos, cotizaciones y producción.

Este catálogo servirá posteriormente para:

- definir reglas de negocio en el backend
- implementar validaciones
- diseñar flujos de interfaz en los frontends
- mantener coherencia en la operación del sistema

---

## 2. Estados de pedido

Los pedidos representan compromisos de preparación y entrega de productos solicitados por los clientes.

### Estados posibles

- **Registrado**  
  El pedido ha sido creado en el sistema, pero aún no ha iniciado su preparación.

- **En preparación**  
  El pedido ya está siendo elaborado por el personal de producción.

- **Listo para entrega**  
  El pedido ha sido terminado y está disponible para ser retirado o entregado.

- **Entregado**  
  El pedido ha sido entregado al cliente y el proceso se considera finalizado.

- **Cancelado**  
  El pedido ha sido cancelado antes de su preparación o entrega.

### Transiciones permitidas

Registrado → En preparación  
Registrado → Cancelado  
En preparación → Listo para entrega  
En preparación → Cancelado  
Listo para entrega → Entregado

No se permite pasar directamente de **Registrado → Entregado** sin pasar por etapas intermedias.

---

## 3. Estados de cotización

Las cotizaciones representan propuestas preliminares para pedidos personalizados.

### Estados posibles

- **Pendiente**  
  La cotización ha sido registrada pero el cliente aún no ha tomado una decisión.

- **Aprobada**  
  El cliente acepta la cotización y se procede a registrar el pedido.

- **Rechazada**  
  El cliente decide no continuar con la propuesta.

- **Convertida en pedido**  
  La cotización se ha transformado formalmente en un pedido dentro del sistema.

### Transiciones permitidas

Pendiente → Aprobada  
Pendiente → Rechazada  
Aprobada → Convertida en pedido

---

## 4. Estados de producción

La producción representa la preparación operativa de los productos solicitados en los pedidos.

### Estados posibles

- **Pendiente de producción**  
  El pedido está registrado pero aún no ha iniciado su preparación.

- **En proceso**  
  El producto está siendo elaborado por el personal de producción.

- **Finalizado**  
  La preparación del producto ha concluido.

### Transiciones permitidas

Pendiente de producción → En proceso  
En proceso → Finalizado

---

## 5. Estados de cotizador público

El cotizador de tortas permite registrar solicitudes preliminares realizadas por clientes desde el frontend público.

### Estados posibles

- **Solicitud registrada**  
  El cliente ha enviado una solicitud mediante el cotizador.

- **En revisión**  
  El personal revisa la solicitud y prepara una propuesta.

- **Cotización generada**  
  Se ha generado una cotización formal.

### Transiciones permitidas

Solicitud registrada → En revisión  
En revisión → Cotización generada

---

## 6. Importancia de los estados

El uso de estados permite:

- seguir el progreso de los procesos del negocio
- organizar mejor el trabajo del personal
- evitar confusiones en la operación diaria
- implementar reglas claras dentro del sistema

Además, los estados facilitan la visualización de información en dashboards y paneles operativos.

---

## 7. Consideraciones para el diseño del sistema

En la implementación del sistema, los estados probablemente se representarán mediante:

- enumeraciones
- catálogos de estados
- reglas de transición validadas por el backend

Esto permitirá asegurar que los procesos del negocio se sigan de manera consistente.

---

## 8. Conclusión

El catálogo de estados y transiciones establece las bases para controlar el flujo de los procesos principales del sistema.

Este documento será una referencia importante para el diseño del backend y para la construcción de los módulos del frontend administrativo y del panel de producción.

