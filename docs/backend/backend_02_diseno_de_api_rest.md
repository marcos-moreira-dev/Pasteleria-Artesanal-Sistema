# 02 - Diseno de API REST

## 1. Proposito

Este documento fija el contrato HTTP general del backend de Pasteleria.

La API debe ser:

- consistente
- versionada
- clara para Astro y Angular
- y estable para futura implementacion automatizada

---

## 2. Convenciones globales

- prefijo: `/api/v1`
- recursos nombrados con sustantivos
- respuestas en `application/json`
- fechas en ISO-8601
- paginacion uniforme
- errores estructurados

Separacion funcional recomendada:

- `/api/v1/public/**`
- `/api/v1/admin/**`
- `/api/v1/internal/**` solo si se justifica para worker o infraestructura

---

## 3. Estandar `ApiResponse<T>`

Respuesta exitosa sugerida:

```json
{
  "ok": true,
  "data": {},
  "meta": {
    "requestId": "uuid",
    "timestamp": "2026-03-16T10:15:30Z"
  }
}
```

Respuesta con error sugerida:

```json
{
  "ok": false,
  "error": {
    "code": "COTIZACION_NO_CONVERTIBLE",
    "message": "La cotizacion no puede convertirse",
    "details": []
  },
  "meta": {
    "requestId": "uuid",
    "timestamp": "2026-03-16T10:15:30Z"
  }
}
```

---

## 4. Paginacion y filtros

Convencion sugerida para listados:

- `page`
- `size`
- `sort`
- filtros especificos por modulo

`meta` puede incluir:

- `page`
- `size`
- `totalElements`
- `totalPages`

---

## 5. Endpoints principales

### Auth y usuarios

- `POST /api/v1/auth/login`
- `GET /api/v1/admin/usuarios`
- `POST /api/v1/admin/usuarios`
- `PATCH /api/v1/admin/usuarios/{id}/estado`

### Clientes

- `GET /api/v1/admin/clientes`
- `GET /api/v1/admin/clientes/{id}`
- `POST /api/v1/admin/clientes`
- `PUT /api/v1/admin/clientes/{id}`

### Catalogo y productos

- `GET /api/v1/public/productos`
- `GET /api/v1/public/productos/{id}`
- `GET /api/v1/admin/productos`
- `POST /api/v1/admin/productos`
- `PUT /api/v1/admin/productos/{id}`
- `PATCH /api/v1/admin/productos/{id}/estado`

### Cotizaciones

- `POST /api/v1/public/cotizaciones`
- `GET /api/v1/admin/cotizaciones`
- `GET /api/v1/admin/cotizaciones/{id}`
- `PATCH /api/v1/admin/cotizaciones/{id}/estado`
- `POST /api/v1/admin/cotizaciones/{id}/convertir`

### Pedidos

- `GET /api/v1/admin/pedidos`
- `GET /api/v1/admin/pedidos/{id}`
- `POST /api/v1/admin/pedidos`
- `PATCH /api/v1/admin/pedidos/{id}/estado`
- `POST /api/v1/admin/pedidos/{id}/entrega`

### Produccion

- `GET /api/v1/admin/produccion`
- `GET /api/v1/admin/produccion/panel`
- `PATCH /api/v1/admin/produccion/{id}/estado`
- `GET /api/v1/admin/produccion/stream`

### Reportes

- `POST /api/v1/admin/reportes`
- `GET /api/v1/admin/reportes`
- `GET /api/v1/admin/reportes/{id}`
- `GET /api/v1/admin/reportes/{id}/descarga`

---

## 6. Errores de negocio esperados

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

