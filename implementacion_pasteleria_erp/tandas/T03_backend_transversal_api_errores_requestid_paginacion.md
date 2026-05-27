# T03 — Backend transversal: ApiResponse, errores, requestId y paginación

## 1. Objetivo

Fortalecer la columna transversal del backend de Pastelería para acercarla al canon de ingeniería rescatado de Cedro, sin cambiar todavía módulos de negocio grandes.

La tanda introduce/refuerza:

- contrato API nuevo tipo `ok/data/error/meta`;
- compatibilidad temporal con el contrato legacy `success/message/errorCode/requestId/timestamp`;
- errores normalizados con detalles de validación;
- `requestId` obligatorio en respuestas normales y errores;
- 401/403 con JSON estándar;
- paginación estable sin exponer `Page<T>` crudo;
- documentación para continuar el refactor transversal.

## 2. Contexto heredado

El backend ya tenía una base razonable:

- `ApiResponse<T>` legacy;
- `ResponseFactory`;
- `RequestIdSupport`;
- `GlobalExceptionHandler`;
- `RequestCorrelationLoggingFilter`;
- `PageRequestFactory`;
- `PageResponseDto`;
- `PageMapper`.

Pero todavía faltaban piezas del canon Cedro:

- `ApiError` y `ApiMeta` separados;
- detalles por campo en validaciones;
- 401/403 uniformes desde Spring Security;
- contrato nuevo sin romper Angular;
- `X-Request-Id` expuesto por CORS;
- catálogo central de códigos de error.

## 3. Fuente Cedro usada como referencia

Se rescata de Cedro:

- forma conceptual `ok/data/error/meta`;
- `SecurityProblemSupport` para 401/403 JSON;
- separación entre metadatos, error y datos;
- `requestId` como eje de trazabilidad;
- paginación desacoplada de Spring Data;
- errores de validación con detalles.

No se copia estética, dominio restaurante ni nombres Cedro.

## 4. Estado actual de Pastelería antes de la tanda

Antes de esta tanda, la respuesta tenía forma legacy:

```json
{
  "success": true,
  "message": "...",
  "data": {},
  "errorCode": null,
  "requestId": "...",
  "timestamp": "..."
}
```

Ese contrato todavía era consumido por Angular y tests existentes. Por eso esta tanda no elimina esos campos.

## 5. Alcance

Sí se hizo:

- crear `ApiMeta`;
- crear `ApiError`;
- crear `ApiErrorDetail`;
- refactorizar `ApiResponse<T>` para incluir contrato nuevo y campos legacy;
- crear `ErrorCode`;
- actualizar `ResponseFactory`;
- refactorizar `GlobalExceptionHandler`;
- crear `SecurityProblemSupport`;
- conectar `SecurityProblemSupport` en `SecurityConfig`;
- exponer `X-Request-Id` por CORS;
- actualizar contrato TypeScript del frontend para aceptar la forma nueva;
- documentar la tanda.

## 6. Fuera de alcance

No se hizo todavía:

- eliminar campos legacy del contrato;
- reescribir todos los controladores;
- crear `ApiContractRegistry`;
- crear `Permisos.java`;
- cambiar identity/refresh token;
- crear tests profundos de seguridad;
- refactorizar el ApiClient gigante del frontend;
- modificar UX/UI.

Eso queda para tandas posteriores.

## 7. Archivos leídos antes de modificar

- `backend/src/main/java/com/pasteleria/common/api/ApiResponse.java`
- `backend/src/main/java/com/pasteleria/common/api/RequestIdSupport.java`
- `backend/src/main/java/com/pasteleria/common/api/util/ResponseFactory.java`
- `backend/src/main/java/com/pasteleria/common/error/GlobalExceptionHandler.java`
- `backend/src/main/java/com/pasteleria/common/error/ApiErrorResponse.java`
- `backend/src/main/java/com/pasteleria/common/logging/RequestCorrelationLoggingFilter.java`
- `backend/src/main/java/com/pasteleria/common/config/SecurityConfig.java`
- `backend/src/main/java/com/pasteleria/auth/infrastructure/JwtAuthenticationFilter.java`
- `backend/src/main/java/com/pasteleria/common/pagination/*`
- `frontend-admin-angular/src/app/core/contracts/api-contracts.ts`

## 8. Cambios implementados

### 8.1 Contrato API nuevo y compatible

`ApiResponse<T>` ahora expone:

```text
ok
data
error
meta
```

Y conserva temporalmente:

```text
success
message
errorCode
requestId
timestamp
```

Esto evita romper Angular actual y tests existentes mientras se avanza hacia el contrato nuevo.

### 8.2 Error normalizado

Se agregaron:

```text
ApiError
ApiErrorDetail
ApiMeta
ErrorCode
```

`MethodArgumentNotValidException` ahora devuelve detalles de campos.

### 8.3 Seguridad uniforme

Se agregó:

```text
SecurityProblemSupport
```

Sirve para que Spring Security responda 401/403 con JSON estándar, no con HTML o cuerpo vacío.

### 8.4 RequestId

- `ResponseFactory` usa `resolveOrGenerate`.
- `SecurityProblemSupport` devuelve `X-Request-Id`.
- CORS ahora expone `X-Request-Id`.

### 8.5 Frontend contract

`api-contracts.ts` acepta también:

```text
ok?: boolean
error?: ApiError | null
meta?: ApiMeta | null
```

sin romper el consumo actual de `response.data`.

## 9. Riesgos

- Algunos tests viejos pueden seguir esperando solo `success/message`; se conserva compatibilidad.
- Algunos consumidores nuevos podrían empezar a usar campos legacy; queda documentado que son temporales.
- `ApiErrorResponse` queda como clase legacy sin uso principal; puede retirarse en una futura tanda de limpieza.
- Si algún filtro escribe respuesta antes de `SecurityProblemSupport`, no se debe sobreescribir respuesta ya comprometida.

## 10. Criterios de aceptación

La tanda se considera correcta si:

- el backend compila;
- una respuesta exitosa contiene `ok=true`, `data`, `meta` y también `success=true`;
- un error contiene `ok=false`, `error`, `meta` y también `success=false`;
- un 401 devuelve JSON estándar;
- un 403 devuelve JSON estándar;
- las validaciones devuelven detalles por campo;
- `X-Request-Id` se expone por CORS;
- Angular sigue pudiendo leer `response.data`.

## 11. Pruebas mínimas sugeridas

En Windows:

```bat
scripts\test-backend.bat
```

O directamente:

```bat
cd backend
mvn test
```

Pruebas manuales sugeridas:

```text
GET /api/v1/clientes sin token → 401 JSON
POST inválido a cualquier endpoint con validación → 400 JSON con details
GET recurso inexistente → 404 JSON
GET endpoint protegido sin permiso cuando exista caso → 403 JSON
```

## 12. Notas para el siguiente chat

La siguiente tanda es T04:

```text
T04 — Contratos API y permisos
```

T04 debe crear `ApiContractRegistry`, endpoint `/api/v1/contratos` y `Permisos.java`. Debe aprovechar el contrato transversal creado en esta tanda.

No eliminar todavía campos legacy de `ApiResponse`. Eso debe esperar hasta que Angular esté migrado al contrato nuevo.
