# Backend transversal objetivo

## Estado

Actualizado en T03.

## Piezas objetivo inspiradas en Cedro

- `ApiResponse<T>`
- `ApiError`
- `ApiErrorDetail`
- `ApiMeta`
- `ResponseFactory`
- `RequestIdSupport`
- `GlobalExceptionHandler`
- `SecurityProblemSupport`
- `PageResponseDto<T>`
- `PageRequestFactory`
- `ApiContractRegistry`
- `Permisos.java`
- `OperacionAutorizacionService`
- `StorageService`
- `ArchivoRecurso`
- `ProductionReadinessGuard`

## Estado implementado en T03

Ya existe una primera columna transversal compatible:

```text
ApiResponse<T> con forma nueva ok/data/error/meta
ApiResponse<T> con campos legacy temporales success/message/errorCode/requestId/timestamp
ApiMeta
ApiError
ApiErrorDetail
ErrorCode
ResponseFactory
GlobalExceptionHandler
SecurityProblemSupport
PageRequestFactory
PageResponseDto
PageMapper
```

## Contrato objetivo

Respuesta exitosa nueva:

```json
{
  "ok": true,
  "data": {},
  "error": null,
  "meta": {
    "requestId": "...",
    "timestamp": "..."
  }
}
```

Durante transición también se conservan:

```json
{
  "success": true,
  "message": "...",
  "errorCode": null,
  "requestId": "...",
  "timestamp": "..."
}
```

Error nuevo:

```json
{
  "ok": false,
  "data": null,
  "error": {
    "code": "VALIDACION_INVALIDA",
    "message": "La solicitud no cumple las validaciones requeridas.",
    "details": []
  },
  "meta": {
    "requestId": "...",
    "timestamp": "..."
  }
}
```

## Reglas

- Toda respuesta debe tener `requestId`.
- 401 y 403 deben devolver JSON estándar.
- No se debe devolver `Page<T>` crudo.
- Los campos legacy no deben ser base de nuevas integraciones.
- Angular debe migrar gradualmente hacia `ok/error/meta`.
- Los errores de validación deben incluir detalles.

## Pendiente

- `ApiContractRegistry`.
- `Permisos.java`.
- Tests transversales más fuertes.
- Migración gradual del frontend a `ok/error/meta`.
- Retiro futuro de `ApiErrorResponse` legacy.
