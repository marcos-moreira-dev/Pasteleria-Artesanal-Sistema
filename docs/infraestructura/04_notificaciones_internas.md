# 04 - Notificaciones internas

## 1. Proposito

Las notificaciones internas permiten avisar eventos relevantes del sistema sin acoplar la experiencia de usuario al flujo exacto que origino el evento.

---

## 2. Alcance de V1

Canal minimo:

- inbox interno persistido en BD
- polling desde Angular
- SSE opcional si se implementa temprano

No se requiere correo ni WhatsApp en V1.

---

## 3. Eventos que generan notificacion

- reporte completado
- reporte con error
- pedido actualizado de estado
- cotizacion atendida
- recordatorio de cotizacion pendiente
- evento critico de scheduler

---

## 4. Modelo recomendado

Tabla sugerida: `notificacion`

Campos recomendados:

- `id`
- `tipo_notificacion`
- `titulo`
- `mensaje`
- `modulo`
- `referencia_tipo`
- `referencia_id`
- `usuario_destino_id`
- `estado`
- `fecha_creacion`
- `fecha_lectura`
- `prioridad`
- `payload_json`

Estados recomendados:

- `NO_LEIDA`
- `LEIDA`
- `ARCHIVADA`

---

## 5. Reglas de diseno

- la notificacion no reemplaza la fuente de verdad
- siempre debe enlazar a una referencia operativa
- el texto debe ser corto y util
- los mensajes se pueden generar desde eventos internos

---

## 6. Consumo desde UI

El frontend interno debe soportar:

- contador de no leidas
- lista reciente
- marcar como leida
- abrir el detalle relacionado

---

## 7. Pruebas superficiales que deben existir

- crear notificacion al completar reporte
- listar solo notificaciones del usuario
- marcar como leida sin duplicar
- no perder referencia a la entidad relacionada
