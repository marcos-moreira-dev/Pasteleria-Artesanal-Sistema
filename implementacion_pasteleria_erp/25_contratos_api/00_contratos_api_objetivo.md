# Contratos API objetivo

## Estado

Actualizado en T04.

## Propósito

Pastelería ERP debe tener una fuente central de contratos API para evitar que frontend y backend se desalineen.

El contrato API no reemplaza OpenAPI. OpenAPI documenta la API técnicamente; el contrato interno declara, con trazabilidad humana:

- endpoint;
- método HTTP;
- módulo;
- resumen operativo;
- permiso requerido;
- alcance;
- si es público;
- si es paginado.

## Implementación actual

T04 agregó:

```text
backend/src/main/java/com/pasteleria/common/security/Permisos.java
backend/src/main/java/com/pasteleria/contratos/application/ApiContractRegistry.java
backend/src/main/java/com/pasteleria/contratos/api/ApiContractController.java
```

Endpoints disponibles:

```text
GET /api/v1/contratos
GET /api/v1/contratos/endpoints
GET /api/v1/contratos/permisos
GET /api/v1/contratos/enums
GET /api/v1/contratos/paginacion
```

## Regla de uso

Cuando se agregue un endpoint administrativo importante, debe agregarse al `ApiContractRegistry`.

Cuando se agregue un permiso nuevo, debe agregarse a:

1. `Permisos.java`;
2. `ApiContractRegistry.permissions()`;
3. seeds de seguridad cuando existan;
4. pruebas de contrato si aplica.

## Permisos

`Permisos.java` concentra permisos técnicos. La base de datos será la fuente persistente futura, pero el backend no debe tener strings de permisos regados por controladores y servicios.

## Paginación

La política actual declara:

```text
pageParam: page
sizeParam: size
defaultPage: 0
defaultSize: 8
maxSize: 25
responseType: PageResponseDto<T>
```

Ningún endpoint nuevo debería exponer `Page<T>` de Spring Data directamente.

## Pendiente

- aplicar permisos reales por endpoint;
- crear seed SQL de permisos;
- crear cobertura automática endpoint-controller vs registry;
- dividir `ApiClientService` por dominios;
- consumir contratos desde Angular para navegación, diagnóstico o pruebas.

## Actualización T06 — Guía Operativa

La Guía Operativa suma el contrato descargable:

```http
GET /api/v1/casos-uso/manual.pdf
```

Este endpoint devuelve `application/pdf` y no usa `ApiResponse<T>` porque es una descarga binaria. Debe mantenerse registrado en `ApiContractRegistry` para que Angular no dependa de rutas no declaradas.
