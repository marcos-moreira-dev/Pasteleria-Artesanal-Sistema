# T23 — Workspaces Angular por dominio

## Objetivo

Agregar espacios administrativos ERP visibles dentro del Angular Admin sin cambiar el look and feel existente de Pastelería Artesanal.

La tanda convierte la preparación de T22 en una primera pantalla operativa de consulta por dominios:

- inteligencia ERP;
- terceros;
- cartera;
- cuentas por pagar;
- contabilidad;
- fiscalidad interna;
- bridges ERP.

## Alcance

Se agregó una ruta nueva:

```text
/erp
```

La ruta usa la carcasa actual del sistema, el sidebar existente, los estilos transversales ya usados en otras pantallas y la fachada `ErpCoreFacade` creada en T22.

## Fuera de alcance

No se implementaron todavía:

- formularios avanzados para crear documentos por cobrar;
- formularios para registrar cobranzas o pagos;
- ejecución visual de bridges con confirmación;
- edición contable manual avanzada;
- emisión fiscal real;
- rediseño UX/UI;
- cambios en storefront.

## Cambios principales

- Se creó `ErpWorkspacesPageComponent`.
- Se agregó la ruta `/erp`.
- Se agregó la opción “ERP interno” al sidebar.
- Se agregó metadata de página para la carcasa.
- La pantalla consume `ErpCoreFacade` y `ApiClientService` de T22.

## Criterio de producto

La pantalla es una primera estación administrativa. Sirve para consultar y revisar lo que ya exponen T15–T21, sin convertir la interfaz en un ERP pesado todavía.

## Validación

Se ejecutó `npm install` y `npm run build` en `frontend-admin-angular`.

Resultado: build Angular exitoso.
