# 12 - Registro minimo de ADRs

## 1. Proposito

Este documento registra las decisiones arquitectonicas minimas que no conviene volver a discutir en cada iteracion con IA.

Una ADR corta pero estable vale mas que muchas decisiones dispersas en mensajes o prompts.

---

## 2. Formato usado

Cada ADR se resume con:

- contexto
- decision
- impacto principal

---

## 3. ADRs activas

### ADR-01. Monolito modular como estilo del sistema

**Contexto:** el dominio es moderado, la V1 no justifica microservicios y el objetivo es estudiar buena arquitectura sin sobredisenar.

**Decision:** el sistema se implementa como monolito modular con una sola base de datos y backend central.

**Impacto principal:** menor complejidad operativa, mayor coherencia para IA y mejor velocidad de construccion.

---

### ADR-02. PostgreSQL + Flyway como base disciplinada de persistencia

**Contexto:** el proyecto necesita una DB relacional moderada, trazable y facil de versionar.

**Decision:** la persistencia oficial se apoya en PostgreSQL y migraciones versionadas con Flyway.

**Impacto principal:** mejor control de cambios, reproducibilidad y coherencia con Spring Boot 4.

---

### ADR-03. Astro para la superficie publica y Angular para la superficie administrativa

**Contexto:** la cara publica y la cara interna no tienen el mismo trabajo ni el mismo lenguaje de interfaz.

**Decision:** se usa `Astro 5.x` para la superficie publica y `Angular 21.x` para la superficie administrativa, ambos sobre `Node.js 22.12.0+`.

**Impacto principal:** cada superficie usa el tipo de framework mas coherente con su funcion, sin forzar una unica herramienta para todo.

---

### ADR-04. El cotizador es una solicitud guiada, no un checkout

**Contexto:** el dominio acordado excluye ecommerce completo y pagos online en V1.

**Decision:** el cotizador se trata como flujo de solicitud de cotizacion y no como simulacion de tienda con pago.

**Impacto principal:** evita promesas falsas de producto y mantiene el alcance funcional realista.

---

### ADR-05. El panel de produccion vive como superficie interna especializada

**Contexto:** produccion necesita una vista distinta, pero no un producto aislado con backend propio.

**Decision:** el panel de produccion se modela como superficie operativa dentro del ecosistema administrativo.

**Impacto principal:** menor fragmentacion, menos duplicidad y mejor trazabilidad de estados de pedido.

---

### ADR-06. Reportes y archivos se manejan de forma asincrona cuando la carga lo justifique

**Contexto:** ciertos reportes o exportaciones pueden crecer y no conviene bloquear la operacion.

**Decision:** el sistema reserva una capacidad de reportes async y storage controlado desde backend y operacion.

**Impacto principal:** mejor experiencia operativa, menos acoplamiento y base mas enterprise para evolucion futura.

---

### ADR-07. Los proyectos de referencia son apoyo inteligente, no fuente de verdad

**Contexto:** existen proyectos previos utiles para arquitectura, runbooks, pruebas e ideas de implementacion.

**Decision:** `Sistema UE Ninitos Sonadores` y `Proyecto tienda Electronica promedio` pueden revisarse como referencia inteligente, pero nunca desplazan el dominio de Pasteleria.

**Impacto principal:** se aprovecha aprendizaje previo sin contaminar el alcance ni el lenguaje del proyecto actual.

---

## 4. Regla de mantenimiento

Si una decision cambia de verdad, no se edita silenciosamente: se crea una nueva ADR o se deja constancia explicita del cambio.
