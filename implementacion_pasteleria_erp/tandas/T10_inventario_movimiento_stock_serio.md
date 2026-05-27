# T10 — InventarioMovimientoService y stock serio

## 1. Objetivo

Fortalecer el módulo de inventario V1 sin romper la aplicación actual. La tanda convierte el registro de movimientos en un flujo más serio:

- cantidad siempre positiva;
- tipo de movimiento define si suma o resta;
- cálculo explícito de `saldoAnterior` y `saldoPosterior`;
- bloqueo pesimista del item mientras se actualiza stock;
- autorización transicional por sucursal `MATRIZ`;
- política funcional dedicada para no dispersar reglas;
- validación SQL de invariantes de inventario.

Esta tanda no implementa todavía el `item_maestro` ERP V2. Eso queda para las tandas posteriores de V2/producción/ERP.

## 2. Contexto heredado

El proyecto ya tenía `inventario_movimiento`, `IngredienteEntity`, `InsumoEntity` y un `InventarioMovimientoCommandService`. La lógica funcionaba, pero estaba demasiado concentrada en el servicio y todavía faltaban defensas importantes:

- no se guardaba `saldoAnterior`;
- no existía una política central de inventario;
- el item no se bloqueaba explícitamente para evitar carreras de stock;
- los comentarios antiguos todavía hablaban de cantidades negativas en salidas;
- la autorización por permisos/sucursal no se aplicaba a inventario;
- la validación SQL no revisaba movimientos sin trazabilidad.

## 3. Patrón Cedro rescatado

De Cedro se rescata la idea:

```text
movimiento operativo
→ validar permiso
→ bloquear stock
→ calcular saldo anterior/posterior
→ registrar movimiento inmutable
→ auditar
```

Cedro no se copia visualmente ni se copia el dominio restaurante. Se adapta el patrón al abastecimiento actual de la pastelería.

## 4. Estado actual de Pastelería después de esta tanda

Se mantiene el modelo V1:

- `ingrediente.stock_actual`;
- `insumo.stock_actual`;
- `inventario_movimiento.item_tipo`;
- `inventario_movimiento.item_id`.

Pero ahora los movimientos nuevos quedan mejor trazados con:

- `saldo_anterior`;
- `saldo_posterior`;
- referencia operativa por defecto si no viene una;
- motivo u observación obligatoria en salidas;
- permiso transicional por sucursal.

## 5. Alcance

Sí se hizo:

- crear `InventarioMovimientoPolicy`;
- refactorizar `InventarioMovimientoCommandService`;
- aplicar `OperacionAutorizacionService` a consultas y comandos de inventario;
- agregar `findByIdForUpdate` en repositorios de ingrediente e insumo;
- agregar `saldo_anterior` a la tabla `inventario_movimiento`;
- actualizar DTO/resumen Angular para aceptar `saldoAnterior`;
- agregar validación SQL de movimientos;
- agregar tests de política de inventario;
- actualizar documentación.

## 6. Fuera de alcance

No se hizo todavía:

- migrar a `inventario.item_maestro`;
- crear `stock_almacen`;
- crear almacenes/ubicaciones reales;
- cerrar periodos de inventario;
- integrar producción finalizada con inventario;
- integrar contabilidad;
- crear costeo promedio real;
- cambiar la UX/UI.

## 7. Archivos modificados principales

Backend:

```text
backend/src/main/java/com/pasteleria/abastecimiento/application/InventarioMovimientoPolicy.java
backend/src/main/java/com/pasteleria/abastecimiento/application/InventarioMovimientoCommandService.java
backend/src/main/java/com/pasteleria/abastecimiento/application/InventarioMovimientoQueryService.java
backend/src/main/java/com/pasteleria/abastecimiento/application/InventarioMovimientoSummary.java
backend/src/main/java/com/pasteleria/abastecimiento/application/mapper/InventarioMovimientoDtoMapper.java
backend/src/main/java/com/pasteleria/abastecimiento/application/port/IngredienteRepositoryPort.java
backend/src/main/java/com/pasteleria/abastecimiento/application/port/InsumoRepositoryPort.java
backend/src/main/java/com/pasteleria/abastecimiento/infrastructure/persistence/repository/IngredienteRepository.java
backend/src/main/java/com/pasteleria/abastecimiento/infrastructure/persistence/repository/InsumoRepository.java
backend/src/main/java/com/pasteleria/abastecimiento/infrastructure/persistence/entity/InventarioMovimientoEntity.java
```

Base de datos:

```text
backend/src/main/resources/db/migration/V1__pasteleria_base_actual.sql
db/V1/DATABASE_SCHEMA_CANONICO.sql
backend/src/main/resources/db/validation/04_validate_inventory_movements.sql
backend/src/main/resources/db/validation/00_run_all_validations.sql
```

Frontend:

```text
frontend-admin-angular/src/app/features/abastecimiento/models/abastecimiento.models.ts
```

Tests:

```text
backend/src/test/java/com/pasteleria/abastecimiento/application/InventarioMovimientoPolicyTest.java
backend/src/test/java/com/pasteleria/abastecimiento/api/InventarioMovimientoControllerTest.java
```

## 8. Riesgos

- `saldo_anterior` es nuevo; una base existente necesita migración o recreación limpia para validar con JPA.
- Los movimientos antiguos quedan con `saldo_anterior` nulo porque el dato no existía históricamente.
- La autorización usa `MATRIZ` como sucursal transicional. En V2 debe reemplazarse por sucursal real.
- `item_tipo + item_id` sigue siendo V1; en V2 debe reemplazarse por `item_maestro`.
- No se pudo ejecutar Maven en el entorno del chat.

## 9. Criterios de aceptación

La tanda se considera correcta si:

- el backend compila;
- `mvn test` pasa;
- se puede registrar entrada de inventario con cantidad positiva;
- se puede registrar salida de inventario con cantidad positiva y motivo/observación;
- una salida que excede stock falla;
- se guarda `saldoAnterior` y `saldoPosterior` en movimientos nuevos;
- `GET /api/v1/abastecimiento/inventario` exige usuario autenticado con permiso;
- la validación SQL detecta movimientos con cantidad no positiva o sin trazabilidad.

## 10. Pruebas mínimas

En Windows:

```bat
scripts\test-backend.bat
```

Manual:

```http
POST /api/v1/auth/login
GET  /api/v1/abastecimiento/inventario
POST /api/v1/abastecimiento/inventario
```

Payload de salida manual:

```json
{
  "itemTipo": "INGREDIENTE",
  "itemId": 1,
  "tipoMovimiento": "SALIDA_AJUSTE",
  "cantidad": 1.5,
  "motivoSalida": "Ajuste físico de inventario",
  "observaciones": "Conteo manual"
}
```

## 11. Notas para la siguiente tanda

La siguiente tanda es T11 — Caja operativa.

T11 debe seguir el mismo criterio:

- no hacer una mega clase;
- respetar UX/UI;
- documentar antes de tocar código;
- usar autorización transicional por `MATRIZ`;
- mantener compatibilidad V1;
- preparar el camino para `tesoreria.caja_operativa`, `turno_caja`, `movimiento_caja` y `arqueo_caja`.
