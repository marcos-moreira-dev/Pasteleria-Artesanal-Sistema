# T15 — Terceros unificados

## 1. Objetivo

Preparar la transición desde clientes y proveedores separados hacia un modelo de tercero común, sin romper las pantallas actuales de Clientes y Proveedores.

La tanda implementa un puente transicional:

```text
cliente/proveedor V1
→ tercero public transicional
→ cliente_perfil / proveedor_perfil
→ terceros.* ERP V2
→ core.legacy_objeto_mapeo
```

## 2. Contexto heredado

La pastelería V1 tenía clientes y proveedores como entidades separadas. Eso funciona para la app actual, pero no escala bien hacia ERP porque una misma persona o empresa podría ser cliente, proveedor, empleado o contacto operativo.

Cedro mostró el patrón correcto: `tercero` como entidad común y perfiles específicos por rol operativo.

## 3. Fuente Cedro usada como referencia

Se rescata el concepto:

```text
tercero
cliente_perfil
proveedor_perfil
empleado_perfil
```

No se copia dominio restaurante ni nombres Cedro.

## 4. Estado actual de Pastelería

Antes de esta tanda:

- `cliente` existía como tabla propia.
- `proveedor` existía como tabla propia.
- Las pantallas actuales dependían de esos endpoints.
- No había una vista común de terceros.

## 5. Alcance

Esta tanda sí hace:

- Crear tablas transicionales `tercero`, `cliente_perfil`, `proveedor_perfil`, `empleado_perfil` en V1.
- Agregar `tercero_id` a `cliente` y `proveedor`.
- Sincronizar altas/actualizaciones de cliente/proveedor hacia tercero.
- Crear endpoint de consulta `/api/v1/terceros`.
- Preparar schema `terceros` en V2 con perfiles.
- Agregar validación SQL de terceros.
- Actualizar contratos API y permisos.

## 6. Fuera de alcance

No se hace todavía:

- Reemplazar pantallas de Clientes/Proveedores.
- Migrar todas las relaciones operativas a `tercero_id`.
- Implementar empleados/trabajadores reales.
- Eliminar `cliente` o `proveedor`.
- Unificar fiscalidad/identificación final.

## 7. Archivos principales modificados

- `db/V1/DATABASE_SCHEMA_CANONICO.sql`
- `db/V1/DATABASE_SEED_CANONICO.sql`
- `backend/src/main/resources/db/migration/V1__pasteleria_base_actual.sql`
- `backend/src/main/resources/db/migration/V2__erp_pasteleria_unificado_3fn.sql`
- `backend/src/main/resources/db/validation/09_validate_third_parties.sql`
- `backend/src/main/java/com/pasteleria/terceros/**`
- `ClientCommandService`
- `ProveedorCommandService`
- `ApiContractRegistry`
- `Permisos`

## 8. Cambios de diseño

La UX/UI actual se conserva. Clientes y Proveedores siguen existiendo como módulos visibles.

Internamente, cada alta o actualización sincroniza el tercero común cuando las tablas existen. Si una base local antigua todavía no tiene las tablas, la sincronización se omite para no romper desarrollo transicional.

## 9. Riesgos

- Bases locales antiguas pueden necesitar reset para probar toda la funcionalidad.
- Si dos registros tienen datos iguales, la unificación automática es prudente y no intenta deduplicación agresiva.
- La identificación fiscal real queda para T20.

## 10. Criterios de aceptación

- Todo cliente nuevo puede quedar enlazado a `tercero`.
- Todo proveedor nuevo puede quedar enlazado a `tercero`.
- `/api/v1/terceros` lista terceros unificados.
- V2 crea tablas `terceros.tercero`, `terceros.cliente_perfil`, `terceros.proveedor_perfil` y `terceros.empleado_perfil`.
- La validación `09_validate_third_parties.sql` no falla en una base limpia.

## 11. Pruebas mínimas

```bat
scripts\test-backend.bat
```

Pruebas manuales sugeridas:

```http
POST /api/v1/auth/login
GET  /api/v1/terceros
GET  /api/v1/terceros?perfil=CLIENTE
GET  /api/v1/terceros?perfil=PROVEEDOR
```

## 12. Notas para la siguiente tanda

La siguiente tanda es T16 — `ErpFinancialPolicy`. Ahí se deben centralizar reglas financieras antes de endurecer cartera, cuentas por pagar, contabilidad y bridges ERP.
