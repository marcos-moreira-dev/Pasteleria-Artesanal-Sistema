# 📚 Documentación de API - Pastelería Backend

## 📖 Guía de Estudio

Este documento está diseñado como material educativo para entender:
- Cómo está estructurada la API REST
- Qué hace cada endpoint
- Cómo autenticarse
- Ejemplos de requests/responses

---

## 🔐 Autenticación

### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin12345"
}
```

**Response exitoso:**
```json
{
  "success": true,
  "message": "Autenticación completada correctamente.",
  "data": {
    "accessToken": "eyJhbGciOiJIUzM4NCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 7200,
    "username": "admin",
    "role": "ADMIN"
  }
}
```

### Uso del Token
Para todas las peticiones protegidas, incluir el header:
```http
Authorization: Bearer {accessToken}
```

---

## 📦 Módulo: Productos

### Listar Productos
```http
GET /api/v1/productos?page=0&size=10
Authorization: Bearer {token}
```

**Descripción:** Obtiene el catálogo paginado de productos.

**Parámetros de Query:**
- `page`: Número de página (0-based)
- `size`: Cantidad de elementos por página

**Response:**
```json
{
  "success": true,
  "message": "Productos obtenidos correctamente.",
  "data": {
    "content": [
      {
        "id": 1,
        "code": "TORTA-001",
        "name": "Torta Tres Leches",
        "slug": "torta-tres-leches-pequena",
        "description": "Deliciosa torta...",
        "basePrice": 45.00,
        "categoryId": 1,
        "categoryName": "Tortas",
        "active": true,
        "published": true,
        "imagePath": "/assets/products/torta-tres-leches-pequena.png",
        "receta": {
          "titulo": "Receta de Torta Tres Leches",
          "ingredientes": "• Leche evaporada\n• Leche condensada",
          "pasos": "1. Preparar base\n2. Agregar leches"
        }
      }
    ],
    "totalElements": 15,
    "totalPages": 2,
    "size": 10,
    "number": 0
  }
}
```

### Crear Producto
```http
POST /api/v1/productos
Authorization: Bearer {token}
Content-Type: application/json

{
  "categoryId": 1,
  "code": "NUEVO-001",
  "name": "Nuevo Producto",
  "description": "Descripción del producto",
  "basePrice": 25.00,
  "quotationRequired": false,
  "active": true,
  "published": true,
  "receta": {
    "titulo": "Receta",
    "ingredientes": "• Ingrediente 1",
    "pasos": "1. Paso 1",
    "observaciones": "Notas"
  }
}
```

### Subir Imagen
```http
POST /api/v1/productos/{productId}/imagen
Authorization: Bearer {token}
Content-Type: multipart/form-data

imagen: [archivo PNG/JPG/WEBP]
```

---

## 👥 Módulo: Clientes

### Listar Clientes
```http
GET /api/v1/clientes?page=0&size=10
Authorization: Bearer {token}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "fullName": "María González",
        "phone": "0991000001",
        "email": "maria@email.com",
        "notes": "Cliente VIP",
        "createdAt": "2026-03-21T12:00:00Z"
      }
    ]
  }
}
```

### Crear Cliente
```http
POST /api/v1/clientes
Authorization: Bearer {token}
Content-Type: application/json

{
  "fullName": "Nuevo Cliente",
  "phone": "0999999999",
  "email": "cliente@email.com",
  "notes": "Notas opcionales"
}
```

---

## 📋 Módulo: Pedidos

### Listar Pedidos
```http
GET /api/v1/pedidos?page=0&size=10
Authorization: Bearer {token}
```

**Estados posibles:**
- `REGISTRADO`: Pedido nuevo, pendiente de producción
- `EN_PREPARACION`: En proceso de elaboración
- `LISTO`: Terminado, esperando entrega
- `ENTREGADO`: Entregado al cliente
- `CANCELADO`: Cancelado

### Crear Pedido
```http
POST /api/v1/pedidos
Authorization: Bearer {token}
Content-Type: application/json

{
  "clientId": 1,
  "priority": "NORMAL",
  "source": "MOSTRADOR",
  "estimatedTotal": 150.00,
  "notes": "Notas del pedido",
  "estimatedDeliveryDate": "2026-03-25T15:00:00Z",
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "unitPrice": 45.00,
      "notes": "Sin azúcar"
    }
  ]
}
```

---

## 🏭 Módulo: Producción

### Listar Cola de Producción
```http
GET /api/v1/produccion?page=0&size=10
Authorization: Bearer {token}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "orderId": 15,
        "orderCode": "PED-00001",
        "clientName": "María González",
        "status": "PREPARACION",
        "priority": "URGENTE",
        "productionNotes": "Cliente solicita entrega antes de las 6pm",
        "startedAt": "2026-03-21T10:00:00Z",
        "finishedAt": null,
        "createdAt": "2026-03-21T08:00:00Z"
      }
    ]
  }
}
```

### Actualizar Estado de Producción
```http
PATCH /api/v1/produccion/{productionId}/estado
Authorization: Bearer {token}
Content-Type: application/json

{
  "status": "DECORACION",
  "reason": "Base lista, pasando a decoración"
}
```

**Flujo de Estados:**
```
PENDIENTE → PREPARACION → DECORACION → EMPAQUE → FINALIZADO
     ↑           ↑            ↑           ↑
     └───────────┴────────────┴───────────┘ (retroceso permitido)
```

**Transiciones válidas:**
- `PENDIENTE` → `PREPARACION` (iniciar)
- `PREPARACION` → `DECORACION` (avanzar) o `PENDIENTE` (retroceder)
- `DECORACION` → `EMPAQUE` (avanzar) o `PREPARACION` (retroceder)
- `EMPAQUE` → `FINALIZADO` (avanzar) o `DECORACION` (retroceder)
- `FINALIZADO` → `EMPAQUE` (reabrir)

---

## 📊 Dashboard y Métricas

### Obtener Dashboard
```http
GET /api/v1/dashboard
Authorization: Bearer {token}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "clients": 14,
    "quotationsPending": 2,
    "activeProduction": 8,
    "readyOrders": 3
  }
}
```

---

## 🔔 Notificaciones

### Listar Notificaciones
```http
GET /api/v1/notificaciones
Authorization: Bearer {token}
```

### Marcar como Leída
```http
PATCH /api/v1/notificaciones/{id}/leida
Authorization: Bearer {token}
```

---

## 🧪 Códigos de Error

| Código | Descripción | Acción Recomendada |
|--------|-------------|-------------------|
| `AUTENTICACION_INVALIDA` | Credenciales incorrectas | Verificar usuario/password |
| `TOKEN_EXPIRADO` | Sesión expirada | Volver a hacer login |
| `RECURSO_NO_ENCONTRADO` | ID no existe | Verificar el ID enviado |
| `ERROR_INTERNO` | Error del servidor | Reintentar o contactar soporte |
| `VALIDACION_FALLIDA` | Datos inválidos | Revisar formato de request |

---

## 📚 Ejemplos Completos

### Flujo: Crear Pedido y Seguir Producción

```bash
# 1. Login
TOKEN=$(curl -s -X POST http://localhost:8081/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin12345"}' \
  | grep -o '"accessToken":"[^"]*"' | cut -d'"' -f4)

# 2. Crear cliente
CLIENT_ID=$(curl -s -X POST http://localhost:8081/api/v1/clientes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Cliente Demo","phone":"0999999999"}' \
  | grep -o '"id":[0-9]*' | cut -d':' -f2)

# 3. Crear pedido
curl -X POST http://localhost:8081/api/v1/pedidos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"clientId\": $CLIENT_ID,
    \"priority\": \"NORMAL\",
    \"source\": \"MOSTRADOR\",
    \"estimatedTotal\": 100.00,
    \"notes\": \"Pedido de prueba\",
    \"items\": [{\"productId\":1,\"quantity\":1,\"unitPrice\":45.00}]
  }"

# 4. Ver cola de producción
curl -s http://localhost:8081/api/v1/produccion \
  -H "Authorization: Bearer $TOKEN" | jq '.data.content[0]'

# 5. Avanzar estado de producción
curl -X PATCH http://localhost:8081/api/v1/produccion/1/estado \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status":"PREPARACION","reason":"Iniciando preparación"}'
```

---

## 🎯 Consejos para Estudiar

1. **Empieza por Auth**: Siempre necesitas autenticarte primero
2. **Prueba en orden**: Clientes → Productos → Pedidos → Producción
3. **Revisa respuestas**: La API siempre devuelve estructura consistente
4. **Maneja errores**: Captura códigos de error específicos
5. **Usa Swagger**: Visita `/swagger-ui.html` para probar endpoints interactivamente

---

**Swagger UI:** http://localhost:8081/swagger-ui.html

*Documentación generada para fines educativos*
