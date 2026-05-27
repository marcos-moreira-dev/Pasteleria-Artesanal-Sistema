# T12 — Pedidos y producción con máquinas de estado

## 1. Objetivo

Convertir el cambio de estado de pedidos y producción en un flujo protegido por máquinas de estado, evitando transiciones improvisadas y evitando que un pedido cancelado deje producción activa.

Esta tanda mantiene compatibilidad con los estados V1 usados por Angular, pero ordena las reglas para que el backend sea la fuente de verdad.

## 2. Contexto heredado

Antes de T12 ya existían:

- `pedido` con estados `REGISTRADO`, `EN_PREPARACION`, `LISTO`, `ENTREGADO`, `CANCELADO`.
- `produccion` con estados `PENDIENTE`, `PREPARACION`, `DECORACION`, `EMPAQUE`, `FINALIZADO`.
- Creación automática de producción al crear pedido.
- Sincronización básica producción → pedido.
- Sincronización débil pedido cancelado → producción.
- Validación de transiciones dentro de services, con `IllegalStateException`.

Desde T09 ya existe `OperacionAutorizacionService`. Desde T10/T11 empezamos a usar permisos por sucursal transicional `MATRIZ` en módulos críticos.

## 3. Patrón Cedro rescatado

Cedro tiene máquinas de estado para pedidos/cocina y usa la operación confirmada como disparador de sincronización entre módulos.

El patrón rescatado es:

```text
estado actual
→ transición solicitada
→ máquina de estados valida
→ servicio aplica efectos secundarios
→ auditoría
→ notificación
```

No se copia el dominio restaurante. En pastelería:

```text
cocina → producción / obrador
menú → productos / catálogo
pedido restaurante → pedido pastelero
```

## 4. Estado actual de Pastelería

La app ya usa estados visibles en Angular. Por compatibilidad, T12 no renombra masivamente estados a `CONFIRMADO` o `EN_PRODUCCION`.

Se conserva:

```text
Pedido:
REGISTRADO
EN_PREPARACION
LISTO
ENTREGADO
CANCELADO

Producción:
PENDIENTE
PREPARACION
DECORACION
EMPAQUE
FINALIZADO
```

Se agrega:

```text
Producción:
CANCELADO
```

para que la cancelación de pedido no deje producción viva.

## 5. Alcance

Esta tanda implementa:

- `OrderStateMachine`.
- `ProductionStateMachine`.
- Uso de `BusinessRuleException` para transiciones inválidas.
- Autorización por sucursal transicional en pedidos y producción.
- Cancelación automática de producción cuando se cancela pedido.
- Rechazo de pedido `LISTO`/`ENTREGADO` si producción asociada no está `FINALIZADO`.
- Rechazo de avance de producción si el pedido ya está `CANCELADO` o `ENTREGADO`.
- Estado `CANCELADO` para producción.
- Validación SQL de coherencia pedido/producción.
- Tests unitarios de máquinas de estado.

## 6. Fuera de alcance

No se implementa todavía:

- consumo de materiales;
- recetas técnicas reales;
- lote de producción;
- producto terminado;
- asiento contable de producción;
- bridge ERP de producción;
- anulación formal contable;
- reapertura auditada de producción finalizada;
- rediseño visual de Angular.

Eso queda para T13, T18 y T19.

## 7. Archivos modificados

Backend:

```text
backend/src/main/java/com/pasteleria/pedidos/domain/service/OrderStateMachine.java
backend/src/main/java/com/pasteleria/produccion/domain/service/ProductionStateMachine.java
backend/src/main/java/com/pasteleria/pedidos/application/OrderCommandService.java
backend/src/main/java/com/pasteleria/produccion/application/ProductionCommandService.java
backend/src/main/java/com/pasteleria/produccion/application/ProductionQueryService.java
backend/src/main/java/com/pasteleria/produccion/domain/model/ProductionStatus.java
backend/src/main/java/com/pasteleria/contratos/application/ApiContractRegistry.java
```

Base de datos:

```text
backend/src/main/resources/db/migration/V1__pasteleria_base_actual.sql
db/V1/DATABASE_SCHEMA_CANONICO.sql
backend/src/main/resources/db/validation/06_validate_order_production_state.sql
backend/src/main/resources/db/validation/00_run_all_validations.sql
```

Frontend, sin cambio visual de UX/UI:

```text
frontend-admin-angular/src/app/features/produccion/models/production.models.ts
frontend-admin-angular/src/app/features/produccion/production-page.component.ts
frontend-admin-angular/src/app/features/dashboard/dashboard.component.ts
frontend-admin-angular/src/app/core/store/backoffice-store.service.ts
```

Tests:

```text
backend/src/test/java/com/pasteleria/pedidos/domain/service/OrderStateMachineTest.java
backend/src/test/java/com/pasteleria/produccion/domain/service/ProductionStateMachineTest.java
```

## 8. Reglas implementadas

### Pedido

```text
REGISTRADO → EN_PREPARACION | CANCELADO
EN_PREPARACION → LISTO | CANCELADO
LISTO → ENTREGADO | CANCELADO
ENTREGADO → terminal
CANCELADO → terminal
```

Reglas adicionales:

- Pedido `LISTO` exige producción `FINALIZADO` si existe producción asociada.
- Pedido `ENTREGADO` exige producción `FINALIZADO` si existe producción asociada.
- Si se cancela pedido, la producción asociada pasa a `CANCELADO` si aún no estaba finalizada.

### Producción

```text
PENDIENTE → PREPARACION | CANCELADO
PREPARACION → DECORACION | CANCELADO
DECORACION → EMPAQUE | CANCELADO
EMPAQUE → FINALIZADO | CANCELADO
FINALIZADO → terminal
CANCELADO → terminal
```

Reglas adicionales:

- No se avanza producción si el pedido ya está `CANCELADO`.
- No se cambia producción si el pedido ya está `ENTREGADO`.
- Producción `FINALIZADO` sincroniza pedido a `LISTO`.
- Producción `CANCELADO` sincroniza pedido a `CANCELADO`.

## 9. Decisión de compatibilidad

Aunque el diseño ERP futuro podría usar estados como `CONFIRMADO`, `EN_PRODUCCION` o `ANULADO`, T12 no los activa en V1 porque Angular y la base actual usan otros nombres.

La compatibilidad inmediata manda:

```text
EN_PREPARACION sigue existiendo en pedido.
PREPARACION/DECORACION/EMPAQUE siguen existiendo en producción.
```

`ANULADO` queda para una futura fase de trazabilidad/auditoría, no para esta tanda.

## 10. Riesgos

- Algunas pantallas Angular permitían retrocesos de producción. T12 los bloquea desde backend y oculta las acciones de retroceso libre para evitar errores operativos.
- Si existen datos históricos con pedido `LISTO` y producción no `FINALIZADO`, la validación SQL nueva lo detectará.
- La transición de cancelación debe usarse con cuidado porque deja producción cancelada y pedido cancelado.

## 11. Pruebas mínimas

Ejecutar en Windows:

```bat
scripts\test-backend.bat
```

Pruebas manuales sugeridas:

```http
POST /api/v1/auth/login
POST /api/v1/pedidos
PATCH /api/v1/produccion/{id}/estado  PREPARACION
PATCH /api/v1/produccion/{id}/estado  DECORACION
PATCH /api/v1/produccion/{id}/estado  EMPAQUE
PATCH /api/v1/produccion/{id}/estado  FINALIZADO
PATCH /api/v1/pedidos/{id}/estado     ENTREGADO
```

Caso negativo recomendado:

```http
PATCH /api/v1/pedidos/{id}/estado LISTO
```

cuando producción no está finalizada. Debe fallar con error de negocio.

## 12. Criterios de aceptación

- Hay máquinas de estado separadas para pedido y producción.
- Las transiciones inválidas fallan con `BusinessRuleException`.
- Pedido listo/entregado exige producción finalizada.
- Cancelar pedido cancela producción activa.
- Producción finalizada sincroniza pedido a listo.
- Producción cancelada sincroniza pedido a cancelado.
- Producción cancelada ya no cuenta como producción activa.
- La UX/UI actual no fue rediseñada.
- La base V1 acepta `CANCELADO` en producción.
- Existe validación SQL para coherencia pedido/producción.

## 13. Nota para la siguiente tanda

La siguiente tanda es:

```text
T13 — Recetas técnicas, consumo y producto terminado.
```

T13 debe conectar producción con receta técnica e inventario. No debe meter todavía contabilidad completa; eso queda para T18/T19.
