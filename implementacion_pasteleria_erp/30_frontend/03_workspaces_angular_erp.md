# Workspaces Angular ERP

## Propósito

Los workspaces ERP convierten las capacidades backend de T15–T21 en superficies administrativas visibles dentro del Admin Angular.

No reemplazan las pantallas operativas existentes de clientes, pedidos, producción o abastecimiento. Funcionan como vistas administrativas por dominio para revisar información ERP y ejecutar acciones controladas.

## Dominios visibles

- Terceros: consulta de terceros unificados y perfiles.
- Cartera: documentos por cobrar y cobranzas.
- Cuentas por pagar: documentos y pagos proveedor.
- Contabilidad: cuentas, diarios y asientos.
- Fiscalidad: documentos fiscales internos.
- Inteligencia: dashboard y vistas semánticas.
- Bridges ERP: acciones manuales e idempotentes operación → ERP.

## Decisión de diseño

Se usa un componente de workspace reutilizable con rutas por dominio. Esto evita duplicar siete pantallas grandes desde el inicio y permite mantener coherencia visual.

El componente consume `ErpCoreFacade`, no llama al `ApiClientService` directamente.

## Restricción visual

Debe conservarse la estética actual de Pastelería:

- cards sobrias;
- tablas administrativas;
- botones rectangulares;
- colores tierra/crema;
- iconografía ya existente;
- sin copiar la pantalla ERP de Cedro.
