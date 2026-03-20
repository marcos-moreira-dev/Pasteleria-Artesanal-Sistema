# 03 - Auditoria de negocio

## 1. Proposito

La auditoria de negocio permite saber que paso, quien lo hizo, cuando lo hizo y sobre que entidad opero.

No reemplaza los logs tecnicos.

---

## 2. Operaciones que deben auditarse en V1

- creacion y cambio de estado de pedido
- creacion y atencion de cotizacion
- cambios manuales de prioridad o produccion
- alta, bloqueo o cambio de rol de usuario interno
- cambios de parametros sensibles
- generacion o cancelacion de reportes relevantes

---

## 3. Modelo recomendado

Tabla sugerida: `auditoria_evento`

Campos recomendados:

- `id`
- `codigo_evento`
- `modulo`
- `entidad`
- `entidad_id`
- `accion`
- `actor_usuario_id`
- `actor_rol`
- `fecha_evento`
- `valor_anterior_json`
- `valor_nuevo_json`
- `motivo`
- `request_id`
- `ip_origen`

---

## 4. Reglas de captura

- auditar cambios relevantes, no ruido irrelevante
- evitar guardar contrasenas o secretos en claro
- resumir antes y despues cuando aplique
- registrar el motivo cuando una accion lo justifique
- heredar `request_id` o `correlation_id`

---

## 5. Ejemplos de codigos

- `AUD-PEDIDO-CREADO`
- `AUD-PEDIDO-ESTADO-CAMBIADO`
- `AUD-COTIZACION-ATENDIDA`
- `AUD-USUARIO-BLOQUEADO`
- `AUD-REPORTE-SOLICITADO`

---

## 6. Consultas operativas que debe soportar

- auditoria por entidad
- auditoria por usuario
- auditoria por modulo
- auditoria por rango de fechas
- auditoria por codigo de evento

---

## 7. Pruebas superficiales que deben existir

- registrar auditoria al crear pedido
- registrar antes y despues al cambiar estado
- no exponer secretos en `valor_nuevo_json`
- permitir consulta filtrada por entidad y fecha
