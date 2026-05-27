# T22 — Angular core sin tocar UX/UI

## Objetivo

Preparar el frontend administrativo Angular para consumir las capacidades ERP agregadas entre T15 y T21 sin cambiar todavía la interfaz visual ni crear workspaces nuevos.

La tanda deja una base de tipos, endpoints y fachada transversal para que T23 pueda construir pantallas por dominio sin improvisar contratos, `any` ni llamadas HTTP dispersas.

## Contexto heredado

Hasta T21 el backend ya expone capacidades de:

- terceros unificados;
- cartera y cobranzas;
- cuentas por pagar y pagos proveedor;
- contabilidad aplicada;
- bridges ERP separados;
- fiscalidad preparada;
- inteligencia/reporting de solo lectura.

El Admin Angular compilaba antes de esta tanda, pero todavía no tenía un núcleo tipado para esas capacidades ERP.

## Alcance

Se agregó:

- modelos TypeScript para los contratos ERP nuevos;
- métodos en `ApiClientService` para endpoints T15–T21;
- una fachada `ErpCoreFacade` con signals para cargar datos ERP;
- documentación de continuidad para T23.

## Fuera de alcance

No se implementó:

- nuevas pantallas;
- rutas ERP visibles;
- rediseño visual;
- cambios en shell;
- cambios en storefront Astro;
- automatización adicional de bridges;
- lógica operativa nueva.

## Archivos principales modificados

```text
frontend-admin-angular/src/app/core/api/api-client.service.ts
frontend-admin-angular/src/app/features/erp-core/models/erp-core.models.ts
frontend-admin-angular/src/app/features/erp-core/state/erp-core.facade.ts
```

## Criterio técnico

`ApiClientService` queda como cliente HTTP central.

`ErpCoreFacade` queda como capa de estado/fachada para los futuros workspaces. La idea es que T23 no llene componentes de llamadas directas al cliente HTTP ni duplique manejo de carga/error.

## Validación

Se ejecutó:

```bat
npm install
npm run build
```

Resultado local en este entorno:

```text
Application bundle generation complete.
```

## Nota de continuidad

T23 debe crear workspaces Angular por dominio usando `ErpCoreFacade` y manteniendo la estética actual de Pastelería.
