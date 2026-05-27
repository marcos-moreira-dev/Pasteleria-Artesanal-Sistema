# T24 — Presentation seed y demo/SIT validada

## Objetivo

Dejar una base local de presentación/SIT reproducible para revisar Pastelería ERP como producto administrativo completo, sin tocar datos reales y sin depender de edición manual posterior.

## Alcance

T24 agrega datos de presentación para:

- clientes y proveedores representativos;
- productos, ingredientes e insumos de pastelería;
- pedidos en distintos estados;
- producción pendiente/en curso/finalizada;
- orden de compra, documento de compra y cuenta por pagar;
- documentos por cobrar, cobranzas y pago proveedor;
- movimientos de caja;
- asientos contables internos;
- documentos fiscales internos;
- evidencia mínima de reporte/auditoría;
- validación SQL de la coherencia SIT.

## Cambios principales

### Presentation migrations

Se reemplazaron placeholders por seeds reales en:

- `V200__seed_presentation_master_data.sql`
- `V201__seed_presentation_pasteleria_operations.sql`
- `V202__seed_presentation_erp_finance.sql`
- `V203__validate_presentation_seed.sql`

### Validación

Se agregó:

- `db/validation/15_validate_presentation_sit.sql`

La validación es condicional: si no existe el marcador `T24_PRESENTATION_SIT_SEED`, no falla. Esto permite que los tests normales de backend sigan validando la base canónica sin exigir datos de presentación.

### Scripts

Se actualizó `scripts/init-db.ps1` para usar la línea SQL ejecutable actual:

- `V1__pasteleria_base_actual.sql`
- `V2__erp_pasteleria_unificado_3fn.sql`
- `R__pasteleria_reporting_views.sql`
- `R__pasteleria_semantic_views.sql`

Cuando se usa `-IncludePresentation`, también aplica:

- `V200` a `V203` de presentación;
- `15_validate_presentation_sit.sql`.

También se actualizaron:

- `scripts/start-dev-stack.ps1`
- `backend/scripts/seed-presentation.bat`
- `backend/scripts/run-backend-presentation.bat`

## Fuera de alcance

T24 no cambia:

- backend productivo;
- Angular Admin;
- storefront Astro;
- UX/UI;
- integración real SRI;
- generación de PDF final;
- cierres contables reales.

## Validación recomendada

Para revisar solo presentación local:

```bat
backend\scripts\seed-presentation.bat
```

Para levantar todo el stack en modo presentación:

```bat
scripts\pasteleria-demo.bat
```

Para validar backend después de la tanda:

```bat
scripts\test-backend.bat
```

## Nota de prudencia

Los documentos fiscales creados por este seed son internos y de presentación. No son comprobantes autorizados por SRI.
