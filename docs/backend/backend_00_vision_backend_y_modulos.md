# 00 - Vision backend y modulos principales

## 1. Proposito

Este documento fija la forma oficial del backend de Pasteleria para que pueda implementarse con criterio profesional y sin improvisacion.

El backend debe ser:

- unico
- modular
- trazable
- fuerte en validaciones
- y suficientemente enterprise para estudio serio

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
- `springdoc-openapi` o equivalente en ambientes no productivos

---

## 3. Decision arquitectonica principal

El backend se implementa como un **monolito modular con DDD-lite pragmatico**.

Eso significa:

- una sola aplicacion desplegable
- modulos alineados al dominio
- capas internas por responsabilidad
- reglas importantes encapsuladas
- y cero microservicios en V1

---

## 4. Estructura de modulos oficial

Se recomiendan estos modulos:

- `auth`
- `usuarios`
- `clientes`
- `catalogos`
- `productos`
- `cotizaciones`
- `pedidos`
- `produccion`
- `reportes`
- `common`

### Responsabilidad resumida por modulo

- `auth`: login, token y endurecimiento de acceso
- `usuarios`: usuarios internos, roles y estado
- `clientes`: registro y consulta de clientes
- `catalogos`: categorias y catalogos controlados
- `productos`: catalogo comercial
- `cotizaciones`: flujo previo al pedido
- `pedidos`: nucleo transaccional comercial
- `produccion`: cola operativa y seguimiento
- `reportes`: reportes async y descarga
- `common`: contratos compartidos, errores, utilidades, auditoria tecnica

---

## 5. Capas internas recomendadas

Dentro de cada modulo conviene usar estas capas:

- `api`
- `application`
- `application.port`
- `application.mapper`
- `domain`
- `infrastructure`

Para la implementacion real ya aplicada en Pasteleria, la forma oficial queda asi:

- `domain.model` para enums y tipos de workflow del negocio
- `application.port` para contratos de repositorio y dependencias de salida usadas por los casos de uso
- `infrastructure.persistence.entity` para entidades JPA
- `infrastructure.persistence.repository` para adapters JPA concretos basados en Spring Data

Cuando el modulo ya tiene suficiente peso operativo, dentro de `application` se recomienda separar:

- `XQueryService`
- `XCommandService`

### Regla de responsabilidades

- `api`: controladores REST, request/response DTO, validacion superficial
- `application`: casos de uso, transacciones, orquestacion; idealmente separando lectura y escritura
- `application.port`: contratos que `application` consume sin depender directamente del detalle Spring Data
- `application.mapper`: mapeo manual entre entidad y DTO, y apoyo controlado para request -> entity cuando aporte claridad
- `domain`: enums, tipos de estado, reglas y piezas que expresan lenguaje del negocio
- `infrastructure`: JPA, repositorios adapter, seguridad tecnica, eventos tecnicos y detalles de framework

---

## 6. Arbol base sugerido

```text
com.pasteleria
  common
    api
    error
    config
    audit
    pagination
    util
  auth
    api
    application
    domain
    infrastructure
  usuarios
    domain.model
    infrastructure.persistence.entity
    infrastructure.persistence.repository
  clientes
    api
    application
    application.port
    application.mapper
    infrastructure.persistence.entity
    infrastructure.persistence.repository
  catalogos
    api
    application
    application.port
    application.mapper
    infrastructure.persistence.entity
    infrastructure.persistence.repository
  productos
    api
    application
    application.port
    application.mapper
    infrastructure.persistence.entity
    infrastructure.persistence.repository
  cotizaciones
    api
    application
    application.port
    application.mapper
    domain.model
    infrastructure.persistence.entity
    infrastructure.persistence.repository
  pedidos
    api
    application
    application.port
    application.mapper
    domain.model
    infrastructure.persistence.entity
    infrastructure.persistence.repository
  produccion
    api
    application
    application.port
    application.mapper
    domain.model
    infrastructure.persistence.entity
    infrastructure.persistence.repository
  reportes
    api
    application
    application.port
    application.mapper
    domain.model
    infrastructure.persistence.entity
    infrastructure.persistence.repository
```

---

## 7. Reglas de acoplamiento

1. `api` no contiene logica de negocio.
2. `application` coordina y define `@Transactional` cuando aplique.
3. `application` depende de contratos en `application.port`, no de interfaces Spring Data directas.
4. `domain` no conoce detalles HTTP.
5. `infrastructure` no decide reglas de negocio por su cuenta.
6. Un modulo consume otro por servicios de aplicacion o identificadores, no por manipular entidades ajenas libremente.

---

## 8. Dependencias funcionales clave

- `pedidos` depende funcionalmente de `clientes`, `productos` y `cotizaciones`
- `produccion` depende funcionalmente de `pedidos`
- `reportes` depende de lectura sobre varios modulos
- `auth` y `usuarios` son transversales

Regla:

- el backend centraliza la verdad del negocio
- Astro y Angular consumen contratos; no inventan reglas

---

## 9. Decisiones que no deben romperse

- no convertir el backend en CRUD plano por tabla
- no exponer entidades JPA como API
- no mover reglas importantes al frontend
- no introducir microservicios
- no acoplar SSE al flujo principal como si reemplazara REST

---

## 10. Regla de referencia inteligente

Si hace falta reforzar arquitectura, documentacion o implementacion puntual, se puede consultar como referencia inteligente:

- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Java\Sistema UE Niñitos Soñadores`
- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Proyecto tienda Electronica promedio`

Siempre manda el dominio y el alcance de Pasteleria.
