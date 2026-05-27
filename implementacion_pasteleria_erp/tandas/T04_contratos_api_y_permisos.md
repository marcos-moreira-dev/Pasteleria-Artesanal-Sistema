# T04 — Contratos API y permisos

## 1. Objetivo

Crear un registro central de contratos API para que backend, frontend administrativo y pruebas hablen el mismo idioma.

La tanda evita que Angular consuma rutas fantasma, que los permisos queden como strings sueltos y que la paginación se documente de forma informal.

Esta tanda sí toca backend y una pequeña parte de contratos TypeScript del frontend, pero no cambia UX/UI, pantallas ni estilos.

## 2. Contexto heredado

Hasta T03 el backend ya tenía:

- `ApiResponse<T>` con forma nueva `ok/data/error/meta` y compatibilidad legacy;
- errores normalizados;
- `requestId` transversal;
- 401/403 en JSON estándar;
- paginación desacoplada mediante `PageResponseDto`.

Cedro aportó como referencia el patrón `ApiContractRegistry`, donde los endpoints, permisos, enums y paginación se declaran de forma explícita para evitar desalineaciones.

## 3. Fuente Cedro usada como referencia

Se rescata de Cedro:

- registro central de endpoints;
- catálogo de permisos como constantes técnicas;
- endpoint `/api/v1/contratos`;
- endpoint `/api/v1/contratos/endpoints`;
- endpoint `/api/v1/contratos/permisos`;
- endpoint `/api/v1/contratos/enums`;
- endpoint `/api/v1/contratos/paginacion`;
- matriz endpoint-permiso como base futura;
- paginación declarada como contrato.

No se copia:

- nombres Cedro;
- permisos de restaurante;
- rutas de cocina/menú/delivery;
- estética visual;
- reglas de UX/UI.

## 4. Estado actual de Pastelería

El backend ya tiene muchos controladores funcionales:

- autenticación;
- catálogo público;
- clientes;
- productos;
- cotizaciones;
- pedidos;
- producción;
- reportes;
- notificaciones;
- guía operativa;
- abastecimiento;
- ingredientes;
- insumos;
- inventario;
- proveedores;
- órdenes de compra;
- recetas;
- unidades de medida.

Pero no existía una fuente central que declarara qué endpoints son parte del contrato público/administrativo, qué permiso requieren, qué endpoints son paginados y qué enums técnicos debería conocer el frontend.

## 5. Alcance

Esta tanda implementa:

- `Permisos.java` como catálogo central de permisos;
- records de contrato API;
- `ApiContractRegistry`;
- `ApiContractController`;
- tipos TypeScript para consumir contratos;
- método `getApiContracts()` en `ApiClientService`;
- prueba unitaria de `ApiContractRegistry`;
- documentación de contratos API.

## 6. Fuera de alcance

Esta tanda no implementa todavía:

- autorización real por permiso;
- roles globales/locales;
- sucursales operables;
- seed SQL de permisos;
- pantalla Angular para ver contratos;
- cambios visuales;
- refactor masivo del `ApiClientService`;
- migración de todos los módulos a permisos reales.

Eso queda para T09, T22 y T23.

## 7. Archivos leídos antes de modificar

- `backend/src/main/java/com/pasteleria/common/api/ApiResponse.java`
- `backend/src/main/java/com/pasteleria/common/api/util/ResponseFactory.java`
- `backend/src/main/java/com/pasteleria/common/config/SecurityConfig.java`
- controladores `*Controller.java` bajo `backend/src/main/java/com/pasteleria/**/api/`
- `frontend-admin-angular/src/app/core/contracts/api-contracts.ts`
- `frontend-admin-angular/src/app/core/api/api-client.service.ts`
- `implementacion_pasteleria_erp/25_contratos_api/00_contratos_api_objetivo.md`

## 8. Cambios realizados

### Backend

Se agregó:

- `com.pasteleria.common.security.Permisos`
- `com.pasteleria.contratos.application.EndpointContract`
- `com.pasteleria.contratos.application.PermissionContract`
- `com.pasteleria.contratos.application.EnumContract`
- `com.pasteleria.contratos.application.PaginationContract`
- `com.pasteleria.contratos.application.ApiContractSnapshot`
- `com.pasteleria.contratos.application.ApiContractRegistry`
- `com.pasteleria.contratos.api.ApiContractController`

Endpoints nuevos:

```text
GET /api/v1/contratos
GET /api/v1/contratos/endpoints
GET /api/v1/contratos/permisos
GET /api/v1/contratos/enums
GET /api/v1/contratos/paginacion
```

### Frontend

Se ampliaron los tipos en:

```text
frontend-admin-angular/src/app/core/contracts/api-contracts.ts
```

con:

- `EndpointContract`
- `PermissionContract`
- `EnumContract`
- `PaginationContract`
- `ApiContractSnapshot`

Se agregó en `ApiClientService`:

```ts
getApiContracts(): Observable<ApiContractSnapshot>
```

### Tests

Se agregó:

```text
backend/src/test/java/com/pasteleria/contratos/application/ApiContractRegistryTest.java
```

Valida:

- existencia de secciones principales;
- endpoints principales;
- permisos principales;
- política de paginación.

## 9. Riesgos

### Riesgo 1 — Contrato incompleto

El registro es manual. Si se agrega un endpoint nuevo y no se registra, el contrato quedará incompleto.

Mitigación futura: agregar tests de cobertura por controlador en una tanda posterior.

### Riesgo 2 — Permisos declarados pero no aplicados

`Permisos.java` declara permisos, pero todavía no significa que todos los endpoints los exijan realmente.

Mitigación: T09 implementará identity/permisos/sucursales y T04 deja la base de contrato.

### Riesgo 3 — ApiClientService sigue grande

Se agregó un método mínimo, pero el servicio sigue concentrando muchas llamadas.

Mitigación: T22/T23 dividirán servicios API por dominio sin cambiar UX/UI.

## 10. Criterios de aceptación

La tanda se considera terminada si:

- existe `Permisos.java`;
- existe `ApiContractRegistry`;
- existe `ApiContractController`;
- `/api/v1/contratos` devuelve endpoints, permisos, enums y paginación;
- el frontend tiene tipos TypeScript para esos contratos;
- existe prueba unitaria del registry;
- no se modificó la UX/UI;
- no se copiaron nombres Cedro/restaurante literal;
- los Markdown siguen dentro de `implementacion_pasteleria_erp/`.

## 11. Pruebas mínimas sugeridas

En Windows o entorno con Maven disponible:

```bat
scripts\test-backend.bat
```

O directamente:

```bat
cd backend
mvn test
```

Endpoints manuales sugeridos con sesión válida:

```text
GET http://localhost:8080/api/v1/contratos
GET http://localhost:8080/api/v1/contratos/endpoints
GET http://localhost:8080/api/v1/contratos/permisos
GET http://localhost:8080/api/v1/contratos/enums
GET http://localhost:8080/api/v1/contratos/paginacion
```

## 12. Notas para el siguiente chat

La siguiente tanda es T05 — Storage, archivos, assets y PDF base.

T05 debe rescatar de Cedro:

- `StorageService`;
- `LocalStorageService`;
- `ArchivoRecurso`;
- `ArchivoAccessPolicy`;
- `AssetPublicController`;
- `DescargarArchivoService`;
- `PdfWhiteBackgroundPageEvent`.

Debe cuidar:

- no devolver rutas físicas;
- separar assets públicos de archivos internos;
- mantener UX/UI actual;
- no activar fiscalidad real;
- no hacer refactor gigante fuera de alcance.
