# Tanda 2 — Alineación Spring Boot 4, Flyway y perfiles

## Resultado

Flyway queda como fuente normal de creación y evolución del esquema. Hibernate valida el modelo, pero no recrea tablas en Docker.

## Cambios realizados

- Agregada dependencia `spring-boot-flyway`.
- `application-docker.yml` usa `ddl-auto: validate`.
- `spring.sql.init.mode` queda en `never`.
- `data.sql` fue movido a `db/legacy/` como referencia histórica.
- `APP_STORAGE_ROOT` y `REPORT_STORAGE_PATH` quedan parametrizados.

## Regla

Para una base nueva, levantar PostgreSQL y arrancar el backend. Flyway aplica migraciones desde `db/migration`.
