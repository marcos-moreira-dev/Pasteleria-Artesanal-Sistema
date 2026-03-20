# 01 - Reportes async, worker y cola

## 1. Proposito

Este documento formaliza la generacion diferida de reportes cuando la consulta, el render o la exportacion no deben ejecutarse en la misma peticion HTTP.

---

## 2. Casos donde aplica

En Pasteleria aplica a:

- reportes de ventas por rango
- reportes de pedidos por estado
- reportes de produccion por dia o semana
- exportaciones operativas a CSV o Excel
- auditorias o historicos con volumen moderado

No aplica a consultas ligeras de pantalla.

---

## 3. Modelo minimo recomendado

Tabla principal sugerida: `job_reporte`

Campos recomendados:

- `id`
- `codigo_job`
- `tipo_reporte`
- `parametros_json`
- `estado`
- `solicitado_por_usuario_id`
- `fecha_solicitud`
- `fecha_inicio`
- `fecha_fin`
- `intentos`
- `mensaje_error`
- `archivo_id`
- `request_id`
- `version`

Estados recomendados:

- `PENDIENTE`
- `EN_PROCESO`
- `COMPLETADO`
- `ERROR`
- `CANCELADO`
- `EXPIRADO`

---

## 4. Flujo oficial

1. Un usuario solicita un reporte desde Angular.
2. El backend valida permisos y parametros.
3. El backend crea `job_reporte` en estado `PENDIENTE`.
4. Un worker interno reclama el job con control transaccional.
5. El worker genera el archivo.
6. El archivo se registra en storage y se enlaza con `archivo_recurso`.
7. El job pasa a `COMPLETADO` o `ERROR`.
8. Se publica evento interno y se crea notificacion.

---

## 5. Regla de reclamo del worker

El worker no debe tomar el mismo job dos veces.

Se recomienda:

- reclamo transaccional por lote pequeno
- actualizacion de estado de `PENDIENTE` a `EN_PROCESO`
- verificacion por `version` o condicion de estado
- marca de tiempo de inicio

Si algun dia hay varias instancias, el reclamo debe seguir descansando en garantias de BD.

---

## 6. Politica de errores y reintentos

Regla minima:

- hasta 3 intentos para errores recuperables
- sin reintento para parametros invalidos
- mensaje de error resumido para usuario
- detalle tecnico solo en logs y auditoria tecnica

Errores recuperables tipicos:

- timeout transitorio
- storage momentaneamente no disponible
- fallo temporal de render

---

## 7. Descarga y retencion

El archivo final no debe salir directamente de la carpeta fisica sin control.

Debe existir:

- endpoint autorizado de descarga
- validacion de vigencia
- verificacion de permisos
- retencion configurable

---

## 8. Integraciones esperadas

El subsistema de reportes async se integra con:

- `storage`
- `notificacion`
- `auditoria_evento`
- `scheduler`
- eventos internos del backend

---

## 9. Pruebas superficiales que deben existir

- crear job con parametros validos
- impedir creacion sin permiso
- evitar doble reclamo del mismo job
- marcar `ERROR` cuando el render falla
- enlazar `archivo_id` al completar
- notificar al usuario cuando el job termina
