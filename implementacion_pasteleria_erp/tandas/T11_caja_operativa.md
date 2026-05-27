# T11 — Caja operativa

## 1. Objetivo

Implementar una primera caja operativa seria para la pastelería, inspirada en Cedro pero adaptada al dominio actual. La tanda introduce caja, turno, movimiento y arqueo sin todavía mezclar contabilidad, cartera o tesorería bancaria completa.

## 2. Contexto heredado

Hasta T10 el proyecto ya tiene:

- contratos API centralizados;
- permisos técnicos `CAJA_VER` y `CAJA_OPERAR`;
- autorización transicional por sucursal `MATRIZ`;
- storage y archivos;
- guía operativa;
- migración compacta V1/V2;
- tests base con Testcontainers preparados;
- inventario con política y movimientos más serios.

La caja era un hueco operativo importante. Una pastelería puede vender y cobrar en local, así que necesita turno, movimientos, cierre y arqueo.

## 3. Referencia Cedro usada

Se rescata de Cedro:

- caja como operación real, no solo campo en venta;
- apertura de caja;
- movimiento de caja;
- cierre con monto esperado, monto declarado y diferencia;
- auditoría de eventos sensibles;
- autorización backend, no solo botones ocultos.

No se copia la UI de Cedro ni nombres de restaurante.

## 4. Estado actual de Pastelería

Antes de esta tanda no existía módulo backend de caja. Había permisos y contratos reservados, pero no tablas ni endpoints funcionales.

## 5. Alcance

Se implementa:

- `caja_operativa`;
- `turno_caja`;
- `movimiento_caja`;
- `arqueo_caja`;
- seed de `CAJA_MATRIZ`;
- endpoint de estado;
- endpoint de turno abierto;
- endpoint de movimientos por turno;
- endpoint de apertura;
- endpoint de movimiento;
- endpoint de cierre;
- auditoría básica;
- contratos API;
- validación SQL;
- test unitario de política.

## 6. Fuera de alcance

No se implementa todavía:

- cartera formal;
- cobranza ERP;
- asiento contable de caja;
- depósito caja→banco;
- transferencia entre cajas;
- anulación/reversa completa de movimientos;
- pantalla Angular nueva de caja;
- integración con pedidos;
- facturación o fiscalidad.

Eso queda para T17–T20 y T23.

## 7. Archivos creados o modificados

Backend:

- `backend/src/main/java/com/pasteleria/caja/**`
- `backend/src/main/java/com/pasteleria/contratos/application/ApiContractRegistry.java`
- `backend/src/test/java/com/pasteleria/caja/application/CajaPolicyTest.java`
- `backend/src/test/java/com/pasteleria/contratos/application/ApiContractRegistryTest.java`

Base de datos:

- `backend/src/main/resources/db/migration/V1__pasteleria_base_actual.sql`
- `backend/src/main/resources/db/migration/V2__erp_pasteleria_unificado_3fn.sql`
- `backend/src/main/resources/db/validation/05_validate_cash_register.sql`
- `backend/src/main/resources/db/validation/00_run_all_validations.sql`
- `db/V1/DATABASE_SCHEMA_CANONICO.sql`
- `db/V1/DATABASE_SEED_CANONICO.sql`

Documentación:

- `implementacion_pasteleria_erp/tandas/T11_caja_operativa.md`
- `implementacion_pasteleria_erp/50_operacion/01_caja_operativa.md`
- `implementacion_pasteleria_erp/80_roadmap/00_roadmap_tandas.md`

## 8. Decisiones técnicas

### 8.1 Caja transicional

La caja usa `MATRIZ` como sucursal transicional. Cuando el modelo ERP multi-sucursal esté más maduro, la caja deberá relacionarse con `core.sucursal_operativa` real.

### 8.2 Monto declarado al cierre

Al cerrar, la caja queda con el monto declarado. La diferencia queda registrada en el arqueo. En fases posteriores se decidirá si la diferencia genera movimiento automático y asiento contable.

### 8.3 Política de caja

Las reglas de monto, naturaleza y saldo están en `CajaPolicy`, no regadas por el controller.

### 8.4 Auditoría

Apertura, movimiento y cierre quedan auditados con `AuditTrailService`.

## 9. Endpoints implementados

```http
GET  /api/v1/caja/estado
GET  /api/v1/caja/turnos/abierto
GET  /api/v1/caja/turnos/{turnoId}/movimientos
POST /api/v1/caja/abrir
POST /api/v1/caja/movimientos
POST /api/v1/caja/cerrar
```

## 10. Riesgos

- Si V1 no se carga limpia, las tablas nuevas pueden faltar.
- Si la caja queda abierta en datos de prueba, una segunda apertura debe fallar.
- Si el frontend espera una pantalla de caja, todavía no existe; se hará en T23.
- Si se pretende contabilidad automática, todavía no está implementada.

## 11. Criterios de aceptación

- Existe `CAJA_MATRIZ` activa.
- No puede haber dos turnos abiertos para una misma caja.
- No se puede registrar movimiento sin turno abierto.
- No se puede registrar salida mayor al saldo actual.
- El cierre calcula monto sistema, declarado y diferencia.
- La apertura, movimiento y cierre auditan evento.
- Los endpoints aparecen en contratos API.
- La validación SQL revisa caja.

## 12. Pruebas mínimas

```bat
scripts\test-backend.bat
```

Prueba manual sugerida:

```http
POST /api/v1/auth/login
GET  /api/v1/caja/estado
POST /api/v1/caja/abrir
POST /api/v1/caja/movimientos
POST /api/v1/caja/cerrar
```

## 13. Nota para la siguiente tanda

La siguiente tanda es T12: pedidos y producción con máquinas de estado. Ahí se debe empezar a cerrar la brecha entre pedidos, producción y futuros movimientos de caja/cobros, pero sin mezclar todavía contabilidad.
