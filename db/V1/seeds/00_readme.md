# db/V1/seeds/ — Indice de scripts de datos semilla

| Archivo                                          | Contenido                                                                                                         | Dependencias     |
| ------------------------------------------------ | ----------------------------------------------------------------------------------------------------------------- | ---------------- |
| [01_seed_base.sql](01_seed_base.sql)             | Roles, 3 usuarios, 4 categorias, 5 productos base                                                                 | Ejecutar primero |
| [02_seed_demo.sql](02_seed_demo.sql)             | 4 clientes, 4 cotizaciones, 5 pedidos, produccion demo                                                            | Requiere 01      |
| [03_seed_enterprise.sql](03_seed_enterprise.sql) | 2 categorias extra, 9 productos, 8 clientes, 6 cotizaciones, 7 pedidos, archivos, jobs, notificaciones, auditoria | Requiere 01 y 02 |

## Secuencia

```
01_seed_base.sql
  └─ 02_seed_demo.sql
        └─ 03_seed_enterprise.sql
```

## Credenciales de los usuarios demo

| nombre_usuario | contrasenia   | rol           |
| -------------- | ------------- | ------------- |
| admin          | admin12345    | ADMINISTRADOR |
| atencion1      | atencion123   | ATENCION      |
| produccion1    | produccion123 | PRODUCCION    |
