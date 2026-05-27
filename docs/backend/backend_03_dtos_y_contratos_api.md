# 03 - DTOs, contratos API y mapeo

## 1. Proposito

Este documento fija como viajan los datos entre backend y clientes.

La regla central es:

- entidades JPA no se exponen
- la API habla en DTOs
- los mappers aislan persistencia de contrato HTTP

---

## 2. Convenciones de DTO

Tipos recomendados en el proyecto actual:

- `CreateXRequest`
- `UpdateXRequest`
- `XSummary`
- `XDetailSummary`
- `PageResponseDto<T>`
- `ApiResponse<T>`

Para Java 21 conviene usar `record` cuando el DTO no necesita comportamiento
adicional.

---

## 3. Contratos por modulo en el codigo actual

### Clientes

- `CreateClientRequest`
- `UpdateClientRequest`
- `ClientSummary`

### Productos

- `CreateProductRequest`
- `UpdateProductRequest`
- `ProductSummary`
- `ProductCategorySummary`

### Cotizaciones

- `CreateQuotationRequest`
- `QuotationSummary`

### Pedidos

- `CreateOrderRequest`
- `OrderSummary`
- `UpdateOrderStatusRequest`

### Produccion

- `ProductionSummary`
- `UpdateProductionStatusRequest`

### Reportes

- `CreateReportJobRequest`
- `ReportJobSummary`

### Notificaciones

- `NotificationSummary`
- `NotificationCounters`
- `NotificationActionResult`

### Abastecimiento

- `IngredienteSummary`
- `InsumoSummary`
- `ProveedorSummary`
- `RecetaSummary`
- `RecetaDetailSummary`
- `OrdenCompraSummaryDto`
- `OrdenCompraDetailDto`
- `InventarioMovimientoSummary`

---

## 4. Convenciones de mapeo

Se recomienda mapeo manual o con estrategia muy controlada.

Reglas:

- cada modulo concentra sus mappers en `application.mapper`
- el mapper no consulta base de datos
- el mapper transforma
- las validaciones de negocio no viven en el mapper
- los DTOs de escritura no aceptan campos internos sin sentido para el cliente

---

## 5. Contratos de listado y paginacion

Todos los listados paginados deben responder como:

- `ApiResponse<PageResponseDto<T>>`

Campos minimos dentro de `data`:

- `content`
- `page`
- `size`
- `totalElements`
- `totalPages`

Campos transversales del `ApiResponse`:

- `success`
- `message`
- `data`
- `errorCode`
- `requestId`
- `timestamp`

Esto evita que cada modulo invente su propia paginacion o su propio sobre HTTP.

---

## 6. Regla de seguridad de contratos

No deben salir por API:

- hashes de password
- campos internos de auditoria sin necesidad
- rutas fisicas internas de almacenamiento
- detalles tecnicos del framework

---

## 7. Regla adicional de assets centralizados

En la V1 actual de Pasteleria el backend es el duenio del branding y de las
imagenes de producto.

Reglas concretas:

- `ProductSummary` expone `imagePath` e `imageAlt`
- el backend resuelve `imagePath` usando el `slug` del producto
- si no existe imagen para ese slug, el backend devuelve un placeholder comun
- solo se exponen rutas HTTP tipo `/assets/...`
- nunca se exponen rutas fisicas del sistema operativo
