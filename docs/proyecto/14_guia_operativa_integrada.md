# 14 - Guía operativa integrada

## 1. Propósito

La guía operativa convierte procedimientos internos de la pastelería en pasos consultables dentro del panel administrativo.

No es una pantalla técnica para programadores. Es un manual vivo para el equipo: atención, producción, compras, administración y revisión diaria.

## 2. Decisión de nombre

- Nombre visible para usuario: **Guía operativa**.
- Nombre técnico del endpoint: `/api/v1/casos-uso`.
- Paquete backend: `com.pasteleria.casosuso`.

La palabra `casosuso` se acepta internamente porque describe el patrón técnico, pero no debe dominar la interfaz visible.

## 3. Backend

Archivos principales:

```text
backend/src/main/java/com/pasteleria/casosuso/api/CasoUsoOperativoController.java
backend/src/main/java/com/pasteleria/casosuso/application/CasoUsoOperativoService.java
backend/src/main/java/com/pasteleria/casosuso/infrastructure/persistence/entity/CasoUsoModuloEntity.java
backend/src/main/java/com/pasteleria/casosuso/infrastructure/persistence/entity/CasoUsoOperativoEntity.java
backend/src/main/java/com/pasteleria/casosuso/infrastructure/persistence/entity/PasoCasoUsoEntity.java
```

Endpoints:

```text
GET /api/v1/casos-uso
GET /api/v1/casos-uso?modulo=PEDIDOS
GET /api/v1/casos-uso/hub
GET /api/v1/casos-uso/{codigo}
```

## 4. Base de datos

Tablas agregadas:

- `caso_uso_modulo`
- `caso_uso_operativo`
- `paso_caso_uso`

Fuentes actualizadas:

- `backend/src/main/resources/db/migration/V13__guia_operativa.sql`
- `db/V1/DATABASE_SCHEMA_CANONICO.sql`
- `db/V1/DATABASE_SEED_CANONICO.sql`

## 5. Frontend Angular

Archivos principales:

```text
frontend-admin-angular/src/app/features/guia-operativa/guia-operativa-page.component.ts
frontend-admin-angular/src/app/features/guia-operativa/models/guia-operativa.models.ts
frontend-admin-angular/src/app/core/api/api-client.service.ts
frontend-admin-angular/src/app/app.routes.ts
frontend-admin-angular/src/app/layout/shell/shell.component.ts
frontend-admin-angular/src/app/core/config/business-shell.config.ts
```

Ruta visible:

```text
/guia-operativa
```

## 6. Datos iniciales

La semilla incluye áreas como:

- resumen del día
- clientes
- productos
- cotizaciones
- pedidos
- producción
- reportes
- inventario
- compras
- proveedores
- movimientos
- notificaciones

La intención no es documentar cada detalle legal o contable, sino dar una ruta inicial de operación diaria para revisar con el negocio.

## 7. Regla de mantenimiento

Cada vez que se agregue un módulo importante al admin, debe evaluarse si necesita una guía operativa nueva. Si la pantalla representa un flujo real de trabajo, debería tener al menos un procedimiento consultable.

## 8. QA mínimo

- Entrar al admin.
- Abrir `/guia-operativa`.
- Confirmar que aparecen áreas documentadas.
- Seleccionar un área.
- Seleccionar una guía.
- Confirmar que se muestran responsable, punto de inicio, objetivo y pasos.
- Probar fallback: si `/hub` falla, la pantalla intenta listar `/casos-uso`.
