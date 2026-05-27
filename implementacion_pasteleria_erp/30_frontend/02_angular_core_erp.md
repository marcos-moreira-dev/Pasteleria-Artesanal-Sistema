# Angular core ERP

## Propósito

Esta capa prepara el Admin Angular para consumir dominios ERP sin modificar todavía la UX/UI.

## Componentes agregados

```text
features/erp-core/models/erp-core.models.ts
features/erp-core/state/erp-core.facade.ts
```

## Reglas

- No usar `any` para contratos ERP.
- No llamar endpoints ERP directamente desde futuros componentes si puede hacerse mediante `ErpCoreFacade`.
- No mezclar pantallas de Pastelería con jerga interna de backend.
- No copiar el frontend de Cedro como estética.
- Mantener textos de usuario final: cartera, pagos, documentos, contabilidad, fiscalidad interna, reportes.

## Dominios cubiertos

- terceros;
- cartera;
- cuentas por pagar;
- contabilidad;
- fiscalidad interna;
- bridges ERP;
- inteligencia/reporting.

## Pendiente para T23

Crear workspaces visibles por dominio usando esta base sin romper el look and feel actual.
