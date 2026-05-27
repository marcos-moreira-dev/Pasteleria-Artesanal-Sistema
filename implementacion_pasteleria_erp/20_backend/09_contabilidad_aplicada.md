# Backend — Contabilidad aplicada

## Módulo agregado

```text
backend/src/main/java/com/pasteleria/contabilidad/
```

Incluye controller, servicios de consulta/comando, mapper, DTOs, entidades JPA y repositorios.

## Endpoints

```http
GET  /api/v1/contabilidad/cuentas
GET  /api/v1/contabilidad/diarios
GET  /api/v1/contabilidad/asientos
GET  /api/v1/contabilidad/asientos/{id}
POST /api/v1/contabilidad/asientos
```

## Reglas

- `CONTABILIDAD_VER` permite consultar.
- `ASIENTOS_REGISTRAR` permite registrar asientos.
- `ErpFinancialPolicy` valida líneas y cuadre contable.
- Un asiento con origen no puede duplicarse por `origenTipo + origenId`.

## Nota

No se generan asientos automáticos todavía. Eso queda para T19.
