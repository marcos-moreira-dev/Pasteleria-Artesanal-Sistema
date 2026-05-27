# T25-HF2 — Corrección Angular: métodos bridge en ErpCoreFacade

## Motivo

Después de T23/T25, el build de Angular Admin fallaba porque `erp-workspace-page.component.ts` invocaba acciones bridge sobre `ErpCoreFacade`, pero la fachada no exponía esos métodos.

El error era de TypeScript/Angular, no del backend ni del storefront.

## Corrección aplicada

Se actualizó:

```text
frontend-admin-angular/src/app/features/erp-core/state/erp-core.facade.ts
```

Se agregaron métodos para ejecutar bridges ERP desde la fachada:

```text
generarDocumentoCobrarDesdePedido
generarAsientoVentaDesdeDocumentoCobrar
generarAsientoCobroDesdeCobranza
generarAsientoCompraDesdeDocumentoPagar
generarAsientoPagoProveedor
```

Cada método delega en `ApiClientService`, actualiza `loading`, limpia errores, guarda el último resultado bridge y muestra el mensaje operativo devuelto por backend.

## Alcance

No se modificaron endpoints, servicios backend, base de datos, migraciones, Astro storefront ni UX/UI general.

## Validación

Se ejecutó en el entorno de trabajo:

```text
cd frontend-admin-angular
npm install
npm run build
```

Resultado:

```text
Application bundle generation complete.
```
