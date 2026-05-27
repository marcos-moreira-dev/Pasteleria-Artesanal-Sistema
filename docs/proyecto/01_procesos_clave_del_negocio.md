# 01 — Procesos clave del negocio

## 1. Propósito del documento

Este documento describe los procesos operativos más importantes dentro del funcionamiento de la pastelería. Su objetivo es identificar los flujos principales de trabajo que ocurren durante la operación diaria del negocio.

Estos procesos servirán posteriormente como base para:

- definir casos de uso del sistema
- identificar funcionalidades del backend
- diseñar pantallas del frontend administrativo
- organizar la información necesaria en la base de datos

---

## 2. Proceso de registro de pedido

Este proceso ocurre cuando un cliente solicita un producto que requiere preparación o reserva.

### Flujo general

1. El cliente solicita un producto o torta.
2. El personal de atención consulta disponibilidad.
3. Se registran los datos del cliente si no existen.
4. Se registra el pedido en el sistema.
5. Se define la fecha de entrega.
6. Se registran observaciones si el pedido es personalizado.

### Información generada

- cliente
- pedido
- detalle de pedido
- fecha de entrega

---

## 3. Proceso de cotización de torta personalizada

Este proceso ocurre cuando un cliente solicita un producto que requiere diseño o personalización.

### Flujo general

1. El cliente describe la torta deseada.
2. El personal registra las características principales.
3. Se calcula un precio estimado.
4. Se registra una cotización.
5. El cliente decide si acepta o no la propuesta.

### Posibles resultados

- la cotización se convierte en pedido
- la cotización queda rechazada

---

## 4. Proceso de planificación de producción

La cocina debe organizar el trabajo de acuerdo con los pedidos registrados.

### Flujo general

1. El sistema muestra pedidos pendientes.
2. Se identifican pedidos con fecha de entrega próxima.
3. El personal de producción revisa observaciones.
4. Se planifica la preparación del producto.

Este proceso permite evitar retrasos en entregas.

---

## 5. Proceso de preparación de pedido

Cuando llega el momento de elaborar un producto, el personal de producción prepara el pedido.

### Flujo general

1. El pedido aparece en el panel de producción.
2. El personal inicia la preparación.
3. Se revisan observaciones del pedido.
4. El producto se prepara.
5. El estado del pedido se actualiza.

---

## 6. Proceso de entrega de pedido

Este proceso ocurre cuando el cliente llega a retirar el pedido.

### Flujo general

1. El cliente llega al local.
2. El personal consulta el pedido en el sistema.
3. Se verifica el estado del pedido.
4. Se cobra el saldo pendiente si existe.
5. El pedido es entregado.
6. El estado del pedido se actualiza a entregado.

---

## 7. Proceso de consulta operativa

El personal del negocio puede consultar información relevante para la operación diaria.

Ejemplos:

- pedidos del día
- pedidos pendientes
- pedidos por cliente
- pedidos próximos a entregar

Este proceso ayuda a organizar el trabajo diario del negocio.

---

## 8. Observaciones sobre los procesos

Los procesos descritos representan los flujos principales del negocio.

Aunque el negocio puede tener variaciones operativas, estos procesos cubren la mayoría de las situaciones que ocurren durante la operación normal de la pastelería.

---

## 9. Conclusión

Los procesos clave del negocio permiten entender cómo fluye la información dentro de la pastelería y qué acciones deben ser soportadas por el sistema.

Estos procesos servirán como base para definir casos de uso, funcionalidades del backend y módulos del frontend administrativo.

