# 00 - Infraestructura enterprise canonica

## 1. Proposito

Este bloque documental formaliza la infraestructura transversal que convierte a Pasteleria en un sistema administrativo serio y no solo en un conjunto de CRUDs.

Aqui se congelan los componentes que la IA debe asumir como parte del nucleo tecnico:

- reportes async con cola y worker
- scheduler y jobs periodicos
- auditoria de negocio
- notificaciones internas
- storage y archivos
- concurrencia, idempotencia y eventos internos

---

## 2. Alcance de esta capa

Esta capa no reemplaza:

- el dominio documentado en `docs/negocio`
- la DB formalizada en `docs/base-datos`
- ni el backend formalizado en `docs/backend`

Su funcion es cerrar la infraestructura comun que sostiene esas capas.

---

## 3. Linea base tecnica

La implementacion debe asumir:

- Spring Boot 4
- Java 21 con Eclipse Temurin 21
- PostgreSQL como fuente de verdad transaccional
- Flyway para migraciones
- monolito modular
- storage local o montado por volumen en V1

No se asume broker distribuido ni arquitectura de microservicios.

---

## 4. Componentes obligatorios en V1

### 4.1. Reportes async

Aplican a reportes operativos o pesados:

- ventas por periodo
- pedidos por estado
- produccion por rango
- cotizaciones atendidas
- auditoria basica

### 4.2. Scheduler

Aplica a tareas periodicas controladas:

- limpieza de temporales
- expiracion de enlaces de descarga
- recordatorios de cotizaciones pendientes
- consolidaciones nocturnas
- recalculo de metricas derivadas simples

### 4.3. Auditoria

Debe cubrir al menos:

- pedidos
- cotizaciones
- estados
- usuarios internos
- configuraciones sensibles

### 4.4. Notificaciones

Debe existir como inbox interno para:

- reporte listo
- pedido actualizado
- cotizacion atendida
- error de job

### 4.5. Storage

Debe separar:

- metadatos en BD
- archivo fisico en disco o volumen
- reglas de descarga y retencion

### 4.6. Concurrencia e idempotencia

Debe cubrir:

- doble envio de formularios
- doble conversion de cotizacion a pedido
- doble reclamo de job por worker
- cambios de estado concurrentes

### 4.7. Eventos internos

Se permite un bus interno dentro del monolito para desacoplar reacciones sin meter complejidad distribuida.

---

## 5. Componentes diferidos pero previstos

No son obligatorios en V1, pero conviene dejarlos previstos:

- cache para catalogos o parametros
- outbox pattern
- importacion y exportacion masiva
- read models o tablas resumen
- motor de reglas mas parametrizable

---

## 6. Regla de equilibrio

La prioridad es aprender buena arquitectura sin inflar el proyecto.

Por eso:

- PostgreSQL sigue siendo la base principal
- la complejidad distribuida se difiere
- y los componentes enterprise se implementan de forma local, explicita y trazable

---

## 7. Regla de referencia inteligente

Si hace falta afinar ideas, documentacion o fragmentos de implementacion, se pueden revisar como referencia inteligente:

- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Java\Sistema UE Niñitos Soñadores`
- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Proyecto tienda Electronica promedio`

Siempre manda el dominio y alcance real de Pasteleria.
