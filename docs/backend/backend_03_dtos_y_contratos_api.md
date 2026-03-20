# 03 - DTOs, contratos API y mapeo

## 1. Proposito

Este documento fija como viajan los datos entre backend y clientes.

La regla central es:

- entidades JPA no se exponen
- la API habla en DTOs
- los mappers aislan persistencia de contrato HTTP

---

## 2. Convenciones de DTO

### Tipos recomendados

- `CreateXRequest`
- `UpdateXRequest`
- `XSummary`
- `XDetailSummary`
- `XResponseDto` si el modulo requiere un contrato mas explicito hacia API
- `XFilterRequest`
- `PageResponseDto<T>`

Para Java 21 conviene usar `record` en DTOs inmutables cuando no se necesite comportamiento adicional.

En el backend actual de Pasteleria ya se adopto una linea intermedia y profesional:

- los contratos viven como `record` o DTO ligeros en `application`
- el controlador no expone entidades
- y el mapeo manual vive en `application.mapper`
- los servicios de aplicacion consumen contratos de repositorio en `application.port`
- y los repositorios JPA concretos quedan en `infrastructure.persistence.repository`

---

## 3. Contratos por modulo

### Clientes

- `CreateClienteRequest`
- `UpdateClienteRequest`
- `ClienteResponse`
- `ClienteDetailResponse`

### Productos

- `CreateProductoRequest`
- `UpdateProductoRequest`
- `ProductoResponse`
- `ProductoDetailResponse`
- `ProductoPublicResponse`

### Cotizaciones

- `CreateCotizacionRequest`
- `CotizacionDetalleRequest`
- `CotizacionResponse`
- `CotizacionDetailResponse`

### Pedidos

- `CreatePedidoRequest`
- `PedidoDetalleRequest`
- `PedidoResponse`
- `PedidoDetailResponse`
- `CambioEstadoPedidoRequest`

### Produccion

- `ProduccionResponse`
- `ProduccionPanelResponse`
- `CambioEstadoProduccionRequest`

### Usuarios y auth

- `LoginRequest`
- `LoginResponse`
- `CreateUsuarioRequest`
- `UsuarioResponse`

### Reportes

- `CreateReporteRequest`
- `ReporteResponse`
- `ReporteDetailResponse`

---

## 4. Convenciones de mapeo

Se recomienda mapeo manual o con estrategia muy controlada.

Reglas:

- cada modulo debe concentrar sus mappers en `application.mapper`
- el mapper no consulta base de datos
- el mapper transforma
- las validaciones de negocio no viven en el mapper
- los DTOs de escritura no deben aceptar campos internos sin sentido para el cliente

Referencia ya aplicada en el backend actual:

- `ClientDtoMapper`
- `ProductDtoMapper`
- `ProductCategoryDtoMapper`
- `QuotationDtoMapper`
- `OrderDtoMapper`
- `ProductionDtoMapper`

Ademas, la implementacion actual ya separa:

- `domain.model` para estados, prioridades, origenes y tipos de reporte
- `application.port` para puertos de persistencia usados por casos de uso y workers
- `infrastructure.persistence.entity` para entidades JPA
- `infrastructure.persistence.repository` para adapters JPA

---

## 5. Contratos de listado

Todos los listados paginados deben responder con:

- `data.content`
- `data.page`
- `data.size`
- `data.totalElements`
- `data.totalPages`
- `requestId`

Esto evita que cada modulo invente su propia paginacion.

Para eso conviene una base comun en `common.pagination`, con `PageResponseDto<T>` y `PageMapper`.

Ademas, la respuesta HTTP debe construirse desde una fabrica comun tipo `ResponseFactory` para no repetir en cada controlador:

- mensaje
- `requestId`
- timestamp
- contrato `ApiResponse`

---

## 6. Filtros recomendados

### Productos

- nombre
- categoria
- activo

### Cotizaciones

- clienteId
- estado
- fechaDesde
- fechaHasta

### Pedidos

- clienteId
- estado
- fechaEntregaDesde
- fechaEntregaHasta
- prioridad

### Produccion

- estado
- fechaEntrega
- prioridad

---

## 7. Regla de seguridad de contratos

No deben salir por API:

- hashes de password
- campos internos de auditoria sin necesidad
- rutas fisicas internas de almacenamiento
- detalles tecnicos del framework

---

## 8. Resultado esperado

Con estas reglas el backend gana:

- contratos estables
- menos acoplamiento con JPA
- menos caos entre modulos
- y mejor base para Angular, Astro y futuras pruebas

---

## 9. Regla adicional de assets centralizados

En la V1 actual de Pasteleria el backend es el duenio del branding y de las imagenes de producto.

Reglas concretas:

- `ProductSummary` debe exponer `imagePath` e `imageAlt`
- el backend resuelve `imagePath` usando el `slug` del producto
- si no existe imagen para ese slug, el backend devuelve placeholder comun
- solo se exponen rutas HTTP tipo `/assets/...`
- nunca se exponen rutas fisicas del sistema operativo

Branding publico:

- `GET /api/v1/public/catalogo/branding`
- devuelve logo cuadrado, logo horizontal, banner principal y placeholder de producto

Convencion operativa:

- si se registra un producto nuevo y luego se copia una imagen al backend con el mismo `slug`,
- el landing y el admin deben verla sin cambios adicionales de codigo
