# 06 - Concurrencia, idempotencia y eventos internos

## 1. Proposito

Este documento evita que el sistema falle en escenarios reales de doble clic, reintentos o acciones simultaneas.

Tambien formaliza el uso de eventos internos del monolito para desacoplar reacciones.

---

## 2. Zonas de riesgo en Pasteleria

- crear pedido desde cotizacion
- cambiar estado de pedido
- actualizar prioridad de produccion
- solicitar el mismo reporte varias veces por error de usuario
- reclamo duplicado de jobs

---

## 3. Mecanismos minimos

### 3.1. Concurrencia

- `@Version` para locking optimista donde aplique
- constraints unicos en datos naturalmente unicos
- transacciones claras en servicios de aplicacion

### 3.2. Idempotencia

- `request_id` o clave de idempotencia en operaciones sensibles
- validacion de repeticion reciente cuando aplique
- respuesta consistente si la accion ya fue procesada

### 3.3. Worker y scheduler

- reclamo por estado
- verificacion de version o condicion
- ejecuciones idempotentes

---

## 4. Eventos internos recomendados

- `CotizacionSolicitada`
- `CotizacionAtendida`
- `PedidoCreado`
- `PedidoEstadoActualizado`
- `ReporteGenerado`
- `ReporteFallido`

---

## 5. Regla de uso de eventos

Los eventos internos sirven para disparar:

- auditoria
- notificaciones
- tareas secundarias

No deben usarse para ocultar reglas de negocio criticas ni para volver opaco el flujo principal.

---

## 6. Outbox y broker

En V1 no se exige outbox formal ni broker externo.

Si el sistema crece o aparecen integraciones externas sensibles, el siguiente paso recomendado es:

- outbox en PostgreSQL
- consumidor interno dedicado

---

## 7. Pruebas superficiales que deben existir

- evitar doble conversion de cotizacion a pedido
- rechazar segundo reclamo del mismo job
- publicar evento interno una sola vez por accion principal
- crear auditoria y notificacion desde el evento esperado
