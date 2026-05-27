# Terceros unificados

La pastelería debe conservar las pantallas de Clientes y Proveedores porque son lenguaje natural del negocio. Sin embargo, internamente el ERP necesita una entidad común llamada `tercero`.

## Modelo conceptual

```text
tercero
  ├─ cliente_perfil
  ├─ proveedor_perfil
  └─ empleado_perfil
```

Una misma persona o empresa puede tener más de un perfil operativo.

## Decisión transicional

Durante la transición:

- `cliente` sigue existiendo.
- `proveedor` sigue existiendo.
- Cada uno apunta a `tercero_id`.
- El endpoint `/api/v1/terceros` permite auditar la vista unificada.

## No copiar de Cedro

No copiar nombres de restaurante ni datos Cedro. Se copia el patrón de tercero común.
