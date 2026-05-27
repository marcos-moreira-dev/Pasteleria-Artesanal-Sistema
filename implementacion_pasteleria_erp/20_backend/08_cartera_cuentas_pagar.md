# Backend — Cartera y cuentas por pagar

T17 agrega dos módulos de backend:

```text
com.pasteleria.cartera
com.pasteleria.cuentaspagar
```

## Reglas técnicas

- Usar `ErpFinancialPolicy` para dinero, saldos y aplicaciones.
- Usar permisos existentes: `CARTERA_VER`, `COBRANZAS_REGISTRAR`, `CUENTAS_PAGAR_VER`, `PAGOS_PROVEEDOR_REGISTRAR`.
- No crear controllers con lógica de negocio.
- Registrar auditoría en operaciones que mutan saldos.
- Mantener salida con `ApiResponse` y `ResponseFactory`.

## Decisión de arquitectura

Los pagos y cobranzas no generan todavía asientos contables ni movimientos automáticos de caja. Esa conexión debe hacerse por bridges ERP separados para mantener trazabilidad e idempotencia.
