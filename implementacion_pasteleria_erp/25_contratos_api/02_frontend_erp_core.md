# Contratos frontend ERP core

## Objetivo

Concentrar los contratos TypeScript de las capacidades ERP expuestas por backend entre T15 y T21.

## Archivo fuente

```text
frontend-admin-angular/src/app/features/erp-core/models/erp-core.models.ts
```

## Decisión

Los contratos frontend se mantienen separados de los componentes visuales para que las futuras pantallas puedan evolucionar sin duplicar tipos ni depender de estructuras improvisadas.

## Endpoints cubiertos por ApiClientService

- `/api/v1/terceros`
- `/api/v1/cartera/*`
- `/api/v1/cuentas-pagar/*`
- `/api/v1/contabilidad/*`
- `/api/v1/fiscal/*`
- `/api/v1/erp-bridges/*`
- `/api/v1/inteligencia/*`

## Nota

La existencia del cliente Angular no implica que todas las acciones deban mostrarse todavía en pantalla. T23 decidirá qué workspaces se exponen y con qué flujo de usuario.
