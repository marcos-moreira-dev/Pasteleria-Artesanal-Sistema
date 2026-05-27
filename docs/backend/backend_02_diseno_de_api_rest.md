# 02 - Diseno de API REST

## 1. Proposito

Este documento fija el contrato HTTP general del backend de Pasteleria segun el
codigo real del proyecto.

La API debe ser:

- consistente
- versionada
- clara para Astro y Angular
- y estable para evolucion controlada

---

## 2. Convenciones globales

- prefijo: `/api/v1`
- recursos nombrados con sustantivos
- respuestas en `application/json`
- fechas en ISO-8601
- paginacion uniforme
- errores estructurados

La separacion funcional real hoy es:

- `/api/v1/public/**` para catalogo y cotizacion publica
- `/api/v1/auth/**` para autenticacion
- `/api/v1/{modulo}` para modulos protegidos del admin
- `/api/v1/abastecimiento/**` para la vertical de abastecimiento

No se usa un prefijo `/admin` en los endpoints protegidos actuales.

---

## 3. Estandar `ApiResponse<T>`

El contrato comun actual del proyecto es:

```json
{
  "success": true,
  "message": "Operacion completada",
  "data": {},
  "errorCode": null,
  "requestId": "uuid",
  "timestamp": "2026-03-26T10:15:30Z"
}
```

Respuesta de error:

```json
{
  "success": false,
  "message": "La cotizacion no puede convertirse",
  "data": null,
  "errorCode": "COTIZACION_NO_CONVERTIBLE",
  "requestId": "uuid",
  "timestamp": "2026-03-26T10:15:30Z"
}
```

Regla:

- `message` debe sonar operativo
- `errorCode` debe ser estable
- `requestId` debe permitir trazabilidad tecnica

---

## 4. Paginacion y filtros

Convencion base para listados:

- `page`
- `size`
- filtros especificos por modulo

La paginacion viaja dentro de `data` como `PageResponseDto<T>`.

Campos minimos esperados:

- `content`
- `page`
- `size`
- `totalElements`
- `totalPages`

---

## 5. Familias de endpoints principales

### Auth

- `POST /api/v1/auth/login`

### Catalogo publico

- `GET /api/v1/public/catalogo/categorias`
- `GET /api/v1/public/catalogo/productos`
- `GET /api/v1/public/catalogo/branding`

### Cotizacion publica

- `POST /api/v1/public/cotizaciones`

### Clientes

- `GET /api/v1/clientes`
- `GET /api/v1/clientes/paginado`
- `POST /api/v1/clientes`
- `PUT /api/v1/clientes/{clientId}`

### Productos

- `GET /api/v1/productos`
- `GET /api/v1/productos/paginado`
- `POST /api/v1/productos`
- `PUT /api/v1/productos/{productId}`
- `POST /api/v1/productos/{productId}/imagen`

### Cotizaciones internas

- `GET /api/v1/cotizaciones`
- `GET /api/v1/cotizaciones/paginado`
- `POST /api/v1/cotizaciones`

### Pedidos

- `GET /api/v1/pedidos`
- `GET /api/v1/pedidos/paginado`
- `POST /api/v1/pedidos`
- `PATCH /api/v1/pedidos/{orderId}/estado`

### Produccion

- `GET /api/v1/produccion`
- `GET /api/v1/produccion/paginado`
- `PATCH /api/v1/produccion/{productionId}/estado`

### Reportes

- `POST /api/v1/reportes`
- `GET /api/v1/reportes`
- `GET /api/v1/reportes/paginado`
- `GET /api/v1/reportes/{jobId}/descargar`

### Notificaciones

- `GET /api/v1/notificaciones`
- `GET /api/v1/notificaciones/resumen`
- `PATCH /api/v1/notificaciones/{notificationId}/leer`
- `PATCH /api/v1/notificaciones/{notificationId}/archivar`

### Abastecimiento

- `GET /api/v1/abastecimiento/dashboard`
- `GET|POST /api/v1/abastecimiento/inventario`
- `GET|POST|PUT|PATCH /api/v1/abastecimiento/proveedores`
- `GET|POST|PUT /api/v1/abastecimiento/ordenes-compra`
- `PATCH /api/v1/abastecimiento/ordenes-compra/{id}/estado`
- `POST /api/v1/abastecimiento/ordenes-compra/{id}/recibir`
- `GET|POST|PUT|PATCH /api/v1/abastecimiento/recetas`
- `GET /api/v1/abastecimiento/ingredientes`
- `GET /api/v1/abastecimiento/insumos`

---

## 6. Temas computacionales que debes entender aqui

Los conceptos mas importantes de esta capa son:

- versionado de API
- contratos estables entre frontend y backend
- endpoints publicos vs protegidos
- paginacion y filtros
- semantica correcta de HTTP
- diseno de errores de negocio
- trazabilidad con `requestId`
- integracion uniforme con `ApiResponse<T>`

---

## 7. Errores de negocio esperados

Ejemplos minimos:

- `CLIENTE_NO_ENCONTRADO`
- `PRODUCTO_NO_ENCONTRADO`
- `PRODUCTO_INACTIVO`
- `PEDIDO_NO_ENCONTRADO`
- `TRANSICION_ESTADO_INVALIDA`
- `COTIZACION_NO_CONVERTIBLE`
- `USUARIO_NO_AUTORIZADO`
- `REPORTE_NO_DISPONIBLE`

Mapeo HTTP sugerido:

- `400` validacion o request invalido
- `401` autenticacion requerida
- `403` autorizacion insuficiente
- `404` recurso inexistente
- `409` conflicto de negocio
- `422` regla de negocio incumplida
- `500` error inesperado

---

## 8. Guía operativa

La guía operativa usa endpoints técnicos bajo `/api/v1/casos-uso`, pero su nombre visible en la interfaz es **Guía operativa**.

Endpoints:

```text
GET /api/v1/casos-uso
GET /api/v1/casos-uso?modulo=PEDIDOS
GET /api/v1/casos-uso/hub
GET /api/v1/casos-uso/{codigo}
```

Contrato resumido de `/hub`:

```json
{
  "totalCasos": 14,
  "modulos": [
    {
      "codigo": "PEDIDOS",
      "nombre": "Pedidos",
      "descripcion": "Seguimiento de encargos...",
      "grupo": "OPERACION",
      "ordenVisual": 5,
      "casos": [
        {
          "codigo": "CU-GO-006",
          "titulo": "Registrar un pedido confirmado",
          "actorPrincipal": "Encargada de mostrador",
          "objetivo": "Formalizar un encargo...",
          "puntoInicio": "Abrir Pedidos...",
          "pasos": []
        }
      ]
    }
  ]
}
```

La respuesta sigue usando `ApiResponse<T>` como el resto del backend.
