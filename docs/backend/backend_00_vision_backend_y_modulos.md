# 00 - Vision backend y modulos principales

## 1. Proposito

Este documento fija la forma oficial del backend de Pasteleria para que siga
siendo un backend serio, coherente y estudiable.

El backend debe ser:

- unico
- modular
- trazable
- fuerte en validaciones
- y suficientemente profesional para estudio real

---

## 2. Stack congelado

- Java 21 con Eclipse Temurin 21
- Spring Boot 4
- Spring Web
- Spring Validation
- Spring Security
- Spring Data JPA / Hibernate
- PostgreSQL
- Flyway
- Spring Boot Actuator
- OpenAPI solo en entornos controlados

---

## 3. Decision arquitectonica principal

El backend se implementa como un **monolito modular con DDD-lite pragmatico**.

Eso significa:

- una sola aplicacion desplegable
- modulos alineados al dominio
- capas internas por responsabilidad
- reglas importantes encapsuladas
- cero microservicios en V1

---

## 4. Estructura de modulos oficial

Los modulos que ya tienen sentido real en el sistema son:

- `auth`
- `clientes`
- `catalogos`
- `productos`
- `cotizaciones`
- `pedidos`
- `produccion`
- `reportes`
- `notificaciones`
- `abastecimiento`
- `common`

### Responsabilidad resumida por modulo

- `auth`: login, token y endurecimiento de acceso
- `clientes`: registro, consulta y actualizacion de clientes
- `catalogos`: lectura publica de categorias y branding
- `productos`: catalogo comercial e imagenes
- `cotizaciones`: flujo previo al pedido, publico e interno
- `pedidos`: nucleo transaccional comercial
- `produccion`: cola operativa y seguimiento
- `reportes`: jobs, archivos y descarga autenticada
- `notificaciones`: inbox interno y acciones de lectura o archivo
- `abastecimiento`: ingredientes, insumos, recetas, proveedores, compras y movimientos
- `common`: contratos compartidos, errores, utilidades, auditoria tecnica y soporte API

---

## 5. Capas internas recomendadas

Dentro de cada modulo conviene usar estas capas:

- `api`
- `application`
- `application.port`
- `application.mapper`
- `domain`
- `infrastructure`

Forma oficial en Pasteleria:

- `domain.model` para enums y tipos de workflow
- `application.port` para contratos de salida y persistencia
- `application.mapper` para mapeo controlado a DTO
- `infrastructure.persistence.entity` para entidades JPA
- `infrastructure.persistence.repository` para adapters JPA concretos

Cuando el modulo tiene suficiente peso operativo, dentro de `application` se
recomienda separar:

- `XQueryService`
- `XCommandService`

### Regla de responsabilidades

- `api`: controladores REST, validacion superficial, entrada y salida HTTP
- `application`: casos de uso, transacciones, orquestacion y reglas de proceso
- `application.port`: contratos que `application` consume sin depender de Spring Data directo
- `application.mapper`: traduccion entre entidades y DTOs
- `domain`: lenguaje del negocio, estados e invariantes
- `infrastructure`: framework, seguridad tecnica, JPA, archivos y detalles de integracion

---

## 6. Capacidades concretas del backend actual

El backend actual no es generico. Hoy resuelve estas capacidades:

1. `POST /api/v1/auth/login` para acceso administrativo.
2. `GET /api/v1/public/catalogo/*` para catalogo, categorias y branding publico.
3. `POST /api/v1/public/cotizaciones` para solicitudes publicas desde Astro.
4. Clientes, productos, cotizaciones, pedidos y produccion para el admin.
5. Reportes async con descarga autenticada.
6. Notificaciones internas con lectura y archivado.
7. Abastecimiento con inventario, proveedores, recetas y ordenes de compra.
8. Entrega de assets e imagenes de producto desde el backend.

---

## 7. Dependencias funcionales clave

- `pedidos` depende funcionalmente de `clientes`, `productos` y `cotizaciones`
- `produccion` depende funcionalmente de `pedidos`
- `reportes` depende de lectura sobre varios modulos
- `notificaciones` depende de eventos operativos
- `abastecimiento` depende de productos, recetas y movimientos de stock
- `auth` es transversal

Regla:

- el backend centraliza la verdad del negocio
- Astro y Angular consumen contratos; no inventan reglas

---

## 8. Temas computacionales que debes dominar aqui

Si quieres estudiar este backend con criterio profesional, los temas clave son:

- diseno de monolito modular
- modelado de endpoints REST y versionado
- contratos DTO y `ApiResponse<T>`
- transacciones y cambios de estado
- validacion de entrada y manejo de excepciones
- seguridad stateless con JWT
- paginacion, filtros y consultas operativas
- JPA/Hibernate sobre PostgreSQL
- jobs async, archivos y descarga segura
- request id, auditoria y trazabilidad tecnica

---

## 9. Decisiones que no deben romperse

- no convertir el backend en CRUD plano por tabla
- no exponer entidades JPA como API
- no mover reglas importantes al frontend
- no introducir microservicios
- no mezclar contratos publicos con internos sin control

---

## 10. Resultado esperado

El backend de Pasteleria debe poder leerse como una arquitectura de negocio real,
no como una coleccion de controladores sin criterio.
