# Backend — Terceros unificados

## Objetivo técnico

Preparar un módulo `terceros` sin romper clientes/proveedores existentes.

## Servicios creados

- `TerceroSynchronizationService`
- `TerceroQueryService`
- `TerceroController`

## Regla de compatibilidad

La sincronización con tercero se omite si una base local antigua no tiene todavía las tablas nuevas. Esto evita romper entornos de desarrollo desfasados, aunque la validación real debe hacerse con base limpia.

## Futuro

En tandas posteriores, cartera, compras, fiscalidad y contabilidad deberán migrar progresivamente de `cliente_id`/`proveedor_id` hacia `tercero_id` o mapeos ERP V2.
