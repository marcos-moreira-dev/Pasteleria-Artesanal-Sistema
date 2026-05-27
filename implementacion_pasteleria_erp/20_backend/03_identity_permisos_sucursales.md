# Identity, permisos y sucursales

## Principio

La pastelería evoluciona de rol simple hacia contexto operativo ERP.

## Modelo transicional

V1 mantiene:

```text
usuario_sistema
rol_usuario
```

T09 agrega proyección:

```text
AuthenticatedUserContext
SucursalOperable
UserAccessPolicy
OperacionAutorizacionService
```

## Reglas

- Angular puede ocultar botones.
- Backend debe autorizar operaciones.
- El rol simple no basta para caja, inventario, producción y ERP.
- Las sucursales operables se exponen desde `/auth/me`.
- Los permisos vienen de `Permisos.java` y más adelante de tablas V2.

## Aplicación futura

T10 en adelante debe empezar a usar `OperacionAutorizacionService` en servicios críticos:

- inventario;
- caja;
- producción;
- compras;
- contabilidad;
- fiscalidad.
