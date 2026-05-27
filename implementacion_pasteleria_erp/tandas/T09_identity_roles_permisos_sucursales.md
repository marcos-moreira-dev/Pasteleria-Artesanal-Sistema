# T09 — Identity, roles, permisos y sucursales

## 1. Objetivo

Fortalecer la identidad del backend para que deje de depender únicamente de `usuario_sistema + rol_usuario` como rol plano y empiece a exponer un contexto operativo estilo ERP:

- usuario autenticado;
- rol actual;
- roles globales;
- permisos efectivos;
- permisos globales;
- sucursales operables;
- permisos por sucursal;
- endpoint `/api/v1/auth/me`.

La tanda es transicional: no rompe el login V1 ni exige migrar todos los usuarios a tablas ERP completas todavía.

## 2. Contexto heredado

Hasta T08 el backend ya tenía:

- JWT básico;
- `ApiResponse` compatible nuevo + legacy;
- `SecurityProblemSupport` para 401/403;
- `Permisos.java` como catálogo técnico;
- contratos API;
- V2 con schemas ERP;
- tests base con Testcontainers.

La deuda pendiente era que el usuario seguía funcionando como:

```text
usuario_sistema
→ rol_usuario
→ ROLE_ADMIN / ROLE_ATENCION / ROLE_PRODUCCION
```

Eso sirve para MVP, pero no para ERP con caja, producción, inventario, sucursales, contabilidad y permisos finos.

## 3. Fuente Cedro usada como referencia

Cedro aporta estos patrones:

- contexto `/auth/me` rico;
- roles globales y locales;
- sucursales operables;
- permisos efectivos;
- `OperacionAutorizacionService`;
- permisos centralizados;
- backend autoriza, Angular solo oculta acciones.

## 4. Estado actual de Pastelería

La pastelería aún conserva el modelo V1:

- `rol_usuario`;
- `usuario_sistema`;
- roles `ADMIN`, `ATENCION`, `PRODUCCION`.

No se migra destructivamente todavía.

## 5. Alcance

Se implementa:

- `AuthenticatedUserContext`;
- `SucursalOperable`;
- `UserAccessPolicy`;
- `OperacionAutorizacionService`;
- `/api/v1/auth/me`;
- `AuthResponse` ampliado;
- authorities de Spring con permisos técnicos;
- tabla V2 transicional de empresa/sucursal/roles/permisos;
- tests unitarios de política de permisos;
- smoke API extendido para `/auth/me`.

## 6. Fuera de alcance

No se implementa todavía:

- refresh token persistente;
- revocación de sesiones;
- pantalla de administración de usuarios;
- migración completa de `usuario_sistema` a `seguridad.cuenta_usuario`;
- permisos aplicados en cada controller;
- roles editables desde UI;
- multi-sucursal real con varias sucursales de negocio.

Eso queda para tandas posteriores.

## 7. Archivos tocados

Backend:

```text
backend/src/main/java/com/pasteleria/auth/api/AuthController.java
backend/src/main/java/com/pasteleria/auth/application/AuthResponse.java
backend/src/main/java/com/pasteleria/auth/application/AuthService.java
backend/src/main/java/com/pasteleria/auth/infrastructure/DatabaseUserDetailsService.java
backend/src/main/java/com/pasteleria/common/security/AuthenticatedUserContext.java
backend/src/main/java/com/pasteleria/common/security/SucursalOperable.java
backend/src/main/java/com/pasteleria/common/security/UserAccessPolicy.java
backend/src/main/java/com/pasteleria/common/security/OperacionAutorizacionService.java
backend/src/main/java/com/pasteleria/common/security/AuthenticatedUserService.java
backend/src/main/java/com/pasteleria/common/error/ForbiddenOperationException.java
backend/src/main/java/com/pasteleria/common/error/GlobalExceptionHandler.java
backend/src/main/java/com/pasteleria/contratos/application/ApiContractRegistry.java
backend/src/main/resources/db/migration/V2__erp_pasteleria_unificado_3fn.sql
```

Frontend, solo contratos/sesión, sin cambiar UX/UI:

```text
frontend-admin-angular/src/app/core/contracts/api-contracts.ts
frontend-admin-angular/src/app/core/auth/auth.service.ts
```

Tests:

```text
backend/src/test/java/com/pasteleria/common/security/UserAccessPolicyTest.java
backend/src/test/java/com/pasteleria/auth/api/AuthControllerTest.java
backend/src/test/java/com/pasteleria/integration/BackendApiSmokeIntegrationTest.java
```

## 8. Cambios realizados

### Contexto de usuario

Se creó `AuthenticatedUserContext` con:

- `userId`;
- `username`;
- `displayName`;
- `role`;
- `rolesGlobales`;
- `roles`;
- `permisosGlobales`;
- `permisos`;
- `sucursalesOperables`.

### Sucursal operable

Se creó `SucursalOperable` con:

- `id`;
- `codigo`;
- `nombre`;
- `principal`;
- `permisos`.

Durante la transición, la sucursal por defecto es:

```text
MATRIZ — Sucursal principal
```

### Política de permisos

`UserAccessPolicy` mapea roles V1 hacia permisos ERP:

- `ADMIN` recibe todos los permisos.
- `ATENCION` recibe clientes, cotizaciones, pedidos, caja, reportes, guía y archivos.
- `PRODUCCION` recibe producción, recetas, inventario, compras de consulta, reportes, guía y archivos.

### Endpoint `/auth/me`

Se agregó:

```http
GET /api/v1/auth/me
```

Devuelve el contexto operativo del usuario autenticado.

### Servicio de autorización

`OperacionAutorizacionService` permite:

- exigir permiso global;
- exigir permiso por sucursal;
- exigir sucursal operable;
- consultar sucursales operables con permiso.

Todavía no se aplica masivamente a todos los módulos. Esa aplicación gradual empieza en tandas posteriores.

### V2 ERP preparada

Se agregaron tablas transicionales:

- `core.organizacion_empresa`;
- `core.sucursal_operativa`;
- `seguridad.permiso_operacion`;
- `seguridad.rol_operacion`;
- `seguridad.rol_permiso`;
- `seguridad.usuario_rol_global_mapeo`;
- `seguridad.usuario_sucursal_mapeo`;
- `seguridad.usuario_sucursal_rol_mapeo`.

Esto no rompe V1 ni cambia el login actual.

## 9. Riesgos

- Angular antiguo puede ignorar campos nuevos; eso está bien.
- Los permisos todavía no protegen todos los endpoints; la infraestructura queda lista.
- La sucursal es una proyección transicional, no multi-sucursal real todavía.
- La V2 sigue siendo baseline editable antes de producción real.

## 10. Criterios de aceptación

La tanda está completa si:

- `/api/v1/auth/login` sigue funcionando;
- login devuelve permisos y sucursales operables;
- `/api/v1/auth/me` devuelve contexto operativo;
- `ADMIN` tiene todos los permisos;
- `ATENCION` no tiene permisos de producción;
- `PRODUCCION` no tiene permisos de caja;
- el contrato API declara `/api/v1/auth/me`;
- no se modificó UX/UI visual;
- V2 tiene tablas de seguridad transicional.

## 11. Pruebas mínimas

En máquina local:

```bat
scripts\test-backend.bat
```

Pruebas puntuales:

```http
POST /api/v1/auth/login
GET  /api/v1/auth/me
GET  /api/v1/contratos
```

## 12. Notas para el siguiente chat

La siguiente tanda es T10:

```text
T10 — InventarioMovimientoService y stock serio
```

Ahí se debe empezar a usar `OperacionAutorizacionService` en operaciones críticas de inventario. La regla será: backend autoriza operación real; Angular solo oculta o facilita acciones.
