# Tanda 16 — ErpFinancialPolicy

## Objetivo

Crear una política financiera común para Pastelería ERP, inspirada en Cedro Damasco pero adaptada al estado transicional de Pastelería.

La tanda no implementa todavía cartera completa, cuentas por pagar completas, contabilidad aplicada, fiscalidad ni bridges ERP. Su función es preparar una base compartida para que esas tandas no repitan reglas de dinero, saldos, líneas contables o totales fiscales en varios servicios.

## Contexto heredado

Pastelería ya tenía reglas parciales en:

- `CajaPolicy`.
- `CompraFinancieraPolicy`.
- `InventarioMovimientoPolicy`.
- `ProductionMaterialPolicy`.

La decisión de esta tanda es no reemplazar esas políticas operativas. La nueva política financiera centraliza solamente invariantes comunes.

## Patrón tomado de Cedro

Cedro Damasco usa una política financiera pura:

- no guarda datos;
- no llama repositorios;
- no conoce pantallas;
- valida montos, saldos, líneas contables, cuadre, fiscalidad y cuentas imputables.

Pastelería toma ese patrón, pero mantiene `BIGINT`/orígenes genéricos porque V1 sigue siendo la base viva del sistema.

## Cambios realizados

### Backend

Se agregó:

- `backend/src/main/java/com/pasteleria/erp/application/ErpFinancialPolicy.java`.
- `backend/src/main/java/com/pasteleria/erp/application/package-info.java`.

La política incluye:

- normalización de dinero a dos decimales;
- monto positivo;
- monto no negativo;
- aplicación contra saldo;
- línea contable con debe o haber, no ambos;
- cuadre contable;
- origen fiscal único;
- totales fiscales;
- estado por saldo;
- cuenta imputable.

### Integración ligera

Se ajustó de forma mínima:

- `CajaPolicy` delega normalización y validaciones monetarias comunes en `ErpFinancialPolicy`.
- `CompraFinancieraPolicy` delega normalización de impuesto y total en `ErpFinancialPolicy`.

No se cambiaron flujos de caja, compras, inventario, producción ni pantallas.

### Tests

Se agregó:

- `backend/src/test/java/com/pasteleria/erp/application/ErpFinancialPolicyTest.java`.

Los tests cubren:

- normalización monetaria;
- rechazo de montos inválidos;
- aplicación contra saldo;
- línea contable;
- cuadre contable;
- origen fiscal único;
- totales fiscales;
- estado por saldo;
- cuenta no imputable.

## Fuera de alcance

No se implementó todavía:

- cartera completa;
- cobranzas;
- pagos proveedor;
- contabilidad aplicada;
- documento fiscal real;
- integración SRI;
- bridges ERP;
- pantallas ERP nuevas.

Esos puntos quedan para las tandas siguientes.

## Riesgos controlados

- La política nueva es pura y testeable.
- La integración con políticas existentes fue mínima.
- No se toca UX/UI.
- No se modifica storefront.
- No se cambian migraciones de base de datos en esta tanda.

## Validación

En este entorno se validó compilación puntual de:

- `BusinessRuleException`.
- `ErpFinancialPolicy`.

No se ejecutó Maven completo porque el entorno no puede descargar Maven/dependencias desde internet.

En máquina local ejecutar:

```bat
scripts\test-backend.bat
```

Luego, si pasa backend:

```bat
scripts\test-admin.bat
scripts\test-storefront.bat
```

## Tanda siguiente

La siguiente tanda de implementación es:

- Tanda 17 — Cartera, cobranzas y cuentas por pagar.

Antes de implementarla conviene leer esta política como contrato base para saldos, pagos y aplicaciones.
