# Presentation seed/SIT

T24 convierte `db/presentation-migration` en una fuente útil para revisar la aplicación administrativa de Pastelería ERP con datos realistas.

## Archivos

- `V200__seed_presentation_master_data.sql`: clientes, proveedores, terceros, productos, ingredientes e insumos.
- `V201__seed_presentation_pasteleria_operations.sql`: pedidos, producción, caja, inventario, orden de compra, documento de compra y cuenta por pagar.
- `V202__seed_presentation_erp_finance.sql`: cartera, cobranzas, pagos proveedor, asientos, fiscalidad interna, reporte y auditoría.
- `V203__validate_presentation_seed.sql`: validación directa del seed SIT.

## Criterio

El seed de presentación no reemplaza los datos canónicos mínimos de arranque. Sirve para revisión, demostración, SIT y pruebas humanas del flujo ERP.

## Marcador

El seed registra:

```text
T24_PRESENTATION_SIT_SEED
```

en `core.erp_migration_marker`.

La validación `15_validate_presentation_sit.sql` usa ese marcador para saber si debe validar datos SIT o saltarse en bases normales.
