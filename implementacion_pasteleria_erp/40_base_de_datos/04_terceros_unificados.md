# Base de datos — Terceros unificados

## V1 transicional

Se crean tablas públicas:

```text
tercero
tercero_direccion
tercero_contacto
cliente_perfil
proveedor_perfil
empleado_perfil
```

`cliente` y `proveedor` reciben `tercero_id`.

## V2 ERP

Se crean tablas en schema:

```text
terceros.tercero
terceros.cliente_perfil
terceros.proveedor_perfil
terceros.empleado_perfil
```

Además, `core.legacy_objeto_mapeo` registra la relación entre IDs V1 y destino ERP.

## Validación

`09_validate_third_parties.sql` asegura que clientes y proveedores tengan tercero/perfil.
