# 04 - Seguridad, testing y operacion del backend

## 1. Proposito

Este documento cierra la capa transversal del backend para que Pasteleria no se
lea solo como modulos y endpoints.

Su foco es:

- seguridad
- manejo de errores
- trazabilidad
- reportes y procesos async
- testing
- operacion

---

## 2. Seguridad recomendada para la V1

Se recomienda una API stateless con JWT para la zona administrativa.

### Endpoints publicos

- catalogo publico
- branding e imagenes publicas
- solicitud publica de cotizacion
- health publico minimo

### Endpoints protegidos

- clientes
- productos internos
- cotizaciones internas
- pedidos
- produccion
- reportes
- notificaciones
- abastecimiento

### Roles base

- `ADMIN`
- `ATENCION`
- `PRODUCCION`

---

## 3. Reglas tecnicas de seguridad

- password con hash seguro
- `SecurityFilterChain` explicita
- CSRF deshabilitado si la API es stateless
- CORS definido por entorno
- expiracion de token configurable
- ningun endpoint interno debe quedar publico por accidente

Regla profesional:

- la seguridad no vive en el frontend
- la seguridad no vive en comentarios
- la seguridad no se asume, se configura y se prueba

---

## 4. Manejo de errores y contrato comun

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
- loggear con `requestId`
- propagar el `requestId` en request, response y logs

---

## 5. Trazabilidad tecnica y auditoria

Minimos esperados:

- `created_at`
- `updated_at`
- `created_by` cuando aplique
- `updated_by` cuando aplique
- `requestId`
- actor autenticado

Eventos que conviene registrar:

- pedido creado
- cotizacion convertida
- produccion actualizada
- reporte solicitado
- recepcion de orden de compra

---

## 6. Procesos async, storage y scheduler

### Notificaciones internas

Flujo recomendado:

1. un evento operativo genera una notificacion
2. se persiste en tabla `notificacion`
3. el admin consulta por polling
4. el usuario la marca como leida o la archiva

### Reportes async

Flujo recomendado:

1. el usuario solicita un reporte
2. el backend persiste el job
3. un worker lo procesa
4. guarda metadata y archivo
5. el cliente consulta estado
6. descarga cuando esta listo

Minimos de V1:

- cola persistida en BD
- worker `@Scheduled`
- polling corto en frontend
- reintentos basicos
- descarga autenticada
- archivo guardado fuera del repo

### Scheduler

Minimos:

- procesamiento de cola de reportes
- limpieza de archivos expirados
- archivado de notificaciones leidas

---

## 7. Logging tecnico minimo

La V1 debe dejar un sistema de logs comprensible y correlacionable:

- filtro que genere `requestId` si el cliente no lo envia
- `X-Request-Id` tambien en la respuesta
- logs compactos por request con metodo, ruta, estado HTTP y duracion
- sin payloads sensibles ni credenciales en consola

---

## 8. Testing minimo profesional

### Unitarias

- servicios de aplicacion
- validadores y reglas de dominio
- mappers con logica no trivial

### Slice tests

- `@WebMvcTest` para controladores clave
- `@DataJpaTest` para repositorios criticos

### Integracion

- `@SpringBootTest`
- PostgreSQL real o Testcontainers cuando el costo lo justifique

### Casos criticos

- login
- crear pedido
- convertir cotizacion en pedido
- rechazar conversion duplicada
- actualizar produccion
- generar y descargar reporte
- acceso denegado por rol
- recepcion de orden de compra

---

## 9. Temas computacionales que debes dominar aqui

Si quieres estudiar esta capa como profesional, los temas mas importantes son:

- seguridad stateless con JWT
- diseno de errores de negocio
- manejo global de excepciones
- correlacion de logs con `requestId`
- testing por capas
- jobs async y scheduler
- storage de archivos
- configuracion por entorno

---

## 10. Operacion minima

Variables importantes:

- `SPRING_PROFILES_ACTIVE`
- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_SECONDS`
- `REPORT_STORAGE_PATH`
- `REPORT_WORKER_ENABLED`

Buenas practicas:

- perfiles `local`, `test`, `prod`
- Flyway obligatorio al arrancar
- `open-in-view=false`
- health checks con Actuator
- paginacion real en tablas pesadas
- OpenAPI solo en entornos controlados

---

## 11. Resultado esperado

El backend de Pasteleria debe estar documentado como software profesional:

- seguro
- testeable
- operable
- trazable
- y coherente con sus contratos
