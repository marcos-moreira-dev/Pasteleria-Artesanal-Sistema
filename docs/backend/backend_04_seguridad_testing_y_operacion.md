# 04 - Seguridad, errores, testing y operacion

## 1. Proposito

Este documento cierra la capa transversal del backend de Pasteleria para que no quede solo en modulos y endpoints.

Su foco es:

- seguridad
- manejo de errores
- auditoria y trazabilidad
- reportes async y SSE
- testing
- operacion minima

---

## 2. Seguridad recomendada para V1

Se recomienda una API stateless con JWT para la zona administrativa.

### Endpoints publicos

- catalogo publico
- detalle publico de producto si aplica
- envio de solicitud de cotizacion
- health publico minimo

### Endpoints protegidos

- clientes
- productos internos
- cotizaciones internas
- pedidos
- produccion
- reportes
- usuarios

### Roles base

- `ADMIN`
- `ATENCION`
- `PRODUCCION`

---

## 3. Reglas tecnicas de seguridad

- password con hash seguro (`Argon2PasswordEncoder` o `BCryptPasswordEncoder`)
- `SecurityFilterChain` explicita
- CSRF deshabilitado si la API es stateless
- CORS definido por entorno
- en local, permitir `localhost` y `127.0.0.1` en puertos de desarrollo sin abrir dominios externos
- expiracion de token configurable
- ningun endpoint interno debe quedar publico por accidente

---

## 4. Manejo de errores

Debe existir un `GlobalExceptionHandler` que traduzca excepciones a `ApiResponse`.

Catalogo base de errores:

- `VALIDACION_INVALIDA`
- `RECURSO_NO_ENCONTRADO`
- `CONFLICTO_NEGOCIO`
- `AUTENTICACION_INVALIDA`
- `AUTORIZACION_INVALIDA`
- `ERROR_INTERNO`

Regla:

- no devolver HTML
- no devolver stack trace al cliente
- loggear con `requestId` o `correlationId`

---

## 5. Auditoria y trazabilidad

Minimos esperados:

- `created_at`
- `updated_at`
- `created_by` cuando aplique
- `updated_by` cuando aplique

Ademas conviene registrar eventos como:

- pedido creado
- cotizacion convertida
- estado de produccion actualizado
- reporte solicitado

La implementacion V1 fuerte debe dejar:

- `requestId`
- `ip_origen`
- actor autenticado
- `valor_anterior_json`
- `valor_nuevo_json`

para cambios sensibles de cliente, producto, pedido y produccion.

---

## 6. Notificaciones, reportes async y scheduler

### Notificaciones internas

Flujo recomendado:

1. un evento operativo genera notificacion
2. se persiste en tabla `notificacion`
3. el admin consulta por polling
4. el usuario marca como leida o la archiva

Casos minimos:

- pedido creado
- pedido cambiado de estado
- produccion en movimiento
- reporte solicitado
- reporte listo o con error

### Reportes async

Flujo recomendado:

1. el usuario solicita reporte
2. el backend persiste `SolicitudReporte`
3. un worker procesa
4. guarda metadata y archivo
5. el cliente consulta estado
6. descarga cuando este listo

Minimos de V1:

- cola persistida en BD
- worker `@Scheduled`
- polling corto en el frontend para no obligar al usuario a recargar a ciegas
- disparo asincrono inmediato `afterCommit` para reducir latencia en local y dejar al scheduler como respaldo
- servicio transaccional separado para que el scheduler no procese jobs por auto-invocacion
- reintentos basicos
- retencion y expiracion de archivos
- notificacion de exito o error
- descarga autenticada del archivo generado
- generacion en `PDF` con formato legible, secciones claras y footer sencillo

Decision actual de V1:

- el worker usa un retraso corto de desarrollo para que un job pase de `PENDIENTE` a `COMPLETADO` en pocos segundos
- el archivo se guarda fuera del repositorio, en una ruta configurable por `REPORT_STORAGE_PATH`
- el frontend descarga el PDF terminado sin guardar binarios dentro del repo

### Scheduler orquestado

Minimos:

- archivado programado de notificaciones leidas
- procesamiento periodico de cola de reportes
- limpieza de archivos expirados
- archivado de notificaciones leidas sin inflar el buzon

### SSE

Uso recomendado:

- panel de produccion
- avisos operativos ligeros

Regla:

- SSE complementa REST
- no reemplaza el flujo principal de negocio

---

## 7. Testing minimo profesional

### Unitarias

- servicios de aplicacion
- validadores y reglas de dominio
- mappers con logica no trivial

### Slice tests

- `@WebMvcTest` para controladores clave
- `@DataJpaTest` para repositorios criticos

### Integracion

- `@SpringBootTest`
- PostgreSQL real con Testcontainers en casos criticos si el alcance lo permite

### Casos criticos

- crear pedido
- convertir cotizacion en pedido
- rechazar cotizacion convertida dos veces
- actualizar produccion
- calcular automaticamente precio estimado cuando la cotizacion usa un producto del catalogo
- calcular automaticamente precio unitario cuando el pedido usa un producto del catalogo
- acceso denegado por rol
- generacion y consulta de reporte

---

## 8. Documentacion viva del codigo y de la API

El backend debe incluir:

- OpenAPI/Swagger alineado al contrato real
- controladores con `@Tag`, `@Operation` y validacion explicita cuando aplique
- `ResponseEntity` y `ResponseFactory` para que la capa API no improvise respuestas
- `ApiResponse` y errores reflejados con ejemplos utiles
- Javadocs en casos de uso importantes
- comentarios solo cuando expliquen reglas de negocio, concurrencia, seguridad o decisiones de infraestructura

No se recomiendan comentarios triviales que solo repiten lo obvio del codigo.

---

## 9. Operacion minima

Variables importantes:

- `SPRING_PROFILES_ACTIVE`
- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_SECONDS`
- `NOTIFICATION_DEFAULT_LIMIT`
- `NOTIFICATION_ARCHIVE_READ_AFTER_DAYS`
- `NOTIFICATION_ARCHIVE_CRON`
- `REPORT_WORKER_ENABLED`
- `REPORT_WORKER_DELAY_MS`
- `REPORT_WORKER_BATCH_SIZE`
- `REPORT_RETENTION_DAYS`
- `REPORT_CLEANUP_CRON`
- `REPORT_STORAGE_PATH`

Buenas practicas:

- perfiles `local`, `test`, `prod`
- Flyway obligatorio al arrancar
- `open-in-view=false`
- health checks con Actuator
- logs estructurados con `requestId`
- paginacion real en tablas pesadas del admin
- locking optimista en tablas transaccionales principales
- OpenAPI solo en entornos de desarrollo o controlados

---

## 10. Regla de referencia inteligente

Este backend puede consultar como referencia inteligente:

- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Java\Sistema UE Niñitos Soñadores`
- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Proyecto tienda Electronica promedio`

Eso ayuda en:

- `ApiResponse`
- seguridad
- reportes async
- testing
- y buenas practicas operativas

Siempre manda el dominio de Pasteleria.
