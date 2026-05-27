# 09 - Baseline enterprise del sistema

## 1. Proposito

Este documento fija el nivel de diseño profesional que debe tener Pasteleria para no quedarse en un CRUD bonito ni en una arquitectura decorativa.

La idea es construir un sistema:

- serio,
- trazable,
- mantenible,
- y operable,

sin abandonar el alcance real del proyecto.

---

## 2. Estilo general recomendado

La solucion correcta para este proyecto es:

- monolito modular
- DDD-lite pragmatico
- base de datos relacional fuerte
- contratos API explicitos
- capas separadas
- varias superficies de producto con un nucleo comun

No se recomienda:

- microservicios
- CQRS ceremonioso
- broker distribuido desde el dia uno
- sobreingenieria de moda

---

## 3. Componentes enterprise minimos que si conviene tener

## 3.1. Auditoria formal

No solo logs tecnicos. Tambien auditoria de negocio:

- quien hizo el cambio
- cuando
- desde que modulo
- valor anterior y nuevo cuando aplique

Esto es especialmente util para:

- pedidos
- cotizaciones
- estados
- usuarios
- configuraciones sensibles

## 3.2. Notificaciones internas

Conviene al menos como capacidad simple:

- reporte listo
- pedido actualizado
- cotizacion atendida
- error en proceso masivo

Puede empezar con tabla de notificaciones y polling o SSE.

## 3.3. Scheduler

Sirve para tareas periodicas:

- limpieza
- vencimientos
- recordatorios
- reprocesos simples
- consolidaciones nocturnas

## 3.4. Storage de archivos

Debe separarse:

- metadata en BD
- archivo fisico en storage
- estrategia de nombre y permisos

Aplica a:

- reportes
- imagenes internas si luego aparecen
- adjuntos o evidencias si el sistema crece

## 3.5. Control de concurrencia

Conviene anticiparlo en:

- cambios de estado
- generacion de reportes
- doble envio de formularios
- conversion de cotizacion a pedido

Herramientas utiles:

- constraints unicos
- control transaccional
- idempotencia
- locking cuando haga falta

## 3.6. Configuracion centralizada

No todo debe quedar quemado en codigo.

Separar:

- application config
- variables de entorno
- parametros administrables

## 3.7. Observabilidad minima

Debe existir al menos:

- health checks
- logs utiles
- correlation id si luego se implementa
- errores estructurados
- trazabilidad de reportes y procesos

## 3.8. Permisos y roles

No basta con esconder botones.

El backend debe sostener:

- roles
- permisos por modulo o capacidad cuando haga falta
- endpoints protegidos

## 3.9. Reportes async

Ya forman parte natural del diseño serio del sistema.

Deben documentarse como capacidad enterprise controlada:

- cola
- estados
- worker
- storage
- errores

---

## 4. Componentes enterprise diferidos pero previstos

Estos no son obligatorios de entrada, pero si deben quedar previstos conceptualmente:

- cache
- outbox pattern
- importacion y exportacion masiva
- read models para consultas pesadas
- motor de reglas mas parametrizable

---

## 5. Regla de equilibrio

Enterprise no significa inflar.

En este proyecto, "enterprise" significa:

- decisiones claras,
- contratos estables,
- trazabilidad,
- operacion seria,
- seguridad real,
- y crecimiento controlado.

No significa meter infraestructura compleja sin necesidad.

---

## 6. Prioridad recomendada

Para Pasteleria, el orden correcto de fortalecimiento es:

1. auditoria
2. seguridad y errores
3. storage y reportes async
4. scheduler
5. concurrencia e idempotencia
6. configuracion centralizada
7. observabilidad
8. notificaciones

---

## 7. Regla de referencia inteligente

Este baseline puede apoyarse en referencias inteligentes como:

- `D:\Carrera Profesional\Práctica de habilidades profesionales\Programación\Java\Sistema UE Niñitos Soñadores`
- `D:\Carrera Profesional\Práctica de habilidades profesionales\Programación\Proyecto tienda Electronica promedio`

Eso sirve para estudiar:

- auditoria
- reportes async
- documentacion enterprise
- seguridad
- runbooks
- y cierre operativo

Siempre manda el dominio de Pasteleria.
