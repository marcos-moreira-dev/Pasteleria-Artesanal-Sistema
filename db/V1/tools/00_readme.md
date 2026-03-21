# db/V1/tools/ — Indice de herramientas y utilidades

| Archivo                                                | Proposito                                                                  |
| ------------------------------------------------------ | -------------------------------------------------------------------------- |
| [00_database_bootstrap.sql](00_database_bootstrap.sql) | Crear la base de datos `pasteleria` en PostgreSQL. Ejecutar antes del DDL. |
| [99_reset_demo.sql](99_reset_demo.sql)                 | TRUNCATE de todas las tablas para reiniciar los datos demo.                |

## Uso rapido en linea de comandos

```bash
# Con psql (ajusta host/puerto/usuario segun tu entorno)
psql -U postgres -h localhost -p 5434 -f db/V1/tools/00_database_bootstrap.sql
psql -U postgres -d pasteleria -h localhost -p 5434 -f "db/V1/docs/Diagramas y query de creacion/V1_3FN.sql"
psql -U postgres -d pasteleria -h localhost -p 5434 -f db/V1/seeds/01_seed_base.sql
psql -U postgres -d pasteleria -h localhost -p 5434 -f db/V1/seeds/02_seed_demo.sql
psql -U postgres -d pasteleria -h localhost -p 5434 -f db/V1/seeds/03_seed_enterprise.sql
```
