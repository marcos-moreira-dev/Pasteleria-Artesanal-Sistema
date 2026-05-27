# UX/UI actual y arquitectura Angular

## Estado

Vigente inicial.

## Regla

La UX/UI actual de la pastelería se conserva. La arquitectura Angular puede inspirarse en Cedro en cuanto a separación de responsabilidades, no en cuanto a estilo visual.

## Estructura objetivo

```text
core/
  api/
  auth/
  context/
  errors/
  session/
  config/

features/
  dashboard/
  clientes/
  productos/
  cotizaciones/
  pedidos/
  produccion/
  recetas/
  inventario/
  compras/
  caja/
  reportes/
  guia-operativa/
  auditoria/
  soporte/
  erp/
```

## Principio

El shell visual puede mantenerse, pero sus responsabilidades internas deben mejorar: sesión, permisos, sucursal activa, navegación y carga modular.
