# 00 - Infraestructura enterprise canonica

## 1. Proposito

Este bloque formaliza la infraestructura transversal que convierte a Pasteleria
en un sistema administrativo serio y no solo en un conjunto de CRUDs.

Aqui se congelan los componentes que deben asumirse como parte del nucleo
tecnico:

- reportes async
- scheduler y jobs periodicos
- auditoria y request id
- notificaciones internas
- storage y archivos
- reglas de concurrencia e idempotencia

---

## 2. Alcance de esta capa

Esta capa no reemplaza:

- el dominio documentado en `docs/negocio`
- la DB formalizada en `docs/base-datos`
- el backend formalizado en `docs/backend`

Su funcion es sostener esas capas con infraestructura comun.

---

## 3. Linea base tecnica

La implementacion debe asumir:

- Spring Boot 4
- Java 21 con Eclipse Temurin 21
- PostgreSQL como fuente de verdad transaccional
- Flyway para migraciones
- monolito modular
- storage local o por volumen en V1

No se asume broker distribuido ni microservicios.

---

## 4. Componentes obligatorios en la V1 actual

### 4.1 Reportes async

Aplican a reportes operativos descargables.

Hoy importan:

- solicitud de job
- procesamiento asincrono
- persistencia de archivo
- descarga autenticada

### 4.2 Scheduler

Aplica a tareas periodicas controladas:

- procesamiento de cola de reportes
- limpieza de archivos expirados
- archivado de notificaciones leidas

### 4.3 Auditoria y trazabilidad tecnica

Debe cubrir al menos:

- request id
- actor autenticado
- cambios sensibles
- eventos relevantes de negocio

### 4.4 Notificaciones

Debe existir como inbox interno para:

- reporte listo
- reporte con error
- eventos operativos relevantes

### 4.5 Storage

Debe separar:

- metadatos en BD
- archivo fisico en disco o volumen
- reglas de descarga y retencion

---

## 5. Temas computacionales que debes dominar aqui

Si quieres estudiar esta capa como profesional, los temas clave son:

- jobs async y polling
- scheduler y tareas periodicas
- almacenamiento de archivos
- seguridad de descarga
- request id y correlacion tecnica
- notificaciones internas
- idempotencia y reintentos
- separacion entre dominio e infraestructura

---

## 6. Regla de equilibrio

La prioridad es aprender buena arquitectura sin inflar el proyecto.

Por eso:

- PostgreSQL sigue siendo la base principal
- la complejidad distribuida se difiere
- los componentes enterprise se implementan de forma local, explicita y trazable
