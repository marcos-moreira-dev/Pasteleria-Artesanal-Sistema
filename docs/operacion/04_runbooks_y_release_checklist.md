# 04 - Runbooks y release checklist

## 1. Proposito

Este documento cierra la capa operativa antes de release, presentación seria o arranque
disciplinado.

---

## 2. Checklist minimo antes de release

1. Confirmar variables y secretos.
2. Confirmar migraciones y seeds.
3. Validar health check del backend.
4. Validar login y roles.
5. Validar catalogo publico.
6. Validar contacto con solicitud de cotizacion.
7. Validar frontend administrativo, produccion y abastecimiento.
8. Validar reportes async y storage.
9. Confirmar que el README publico pueda actualizarse con logo y capturas reales.

---

## 3. Runbooks minimos sugeridos

### OPS-01. Backend no conecta a PostgreSQL

Revisar:

- Docker Compose
- credenciales
- puerto
- variables

### OPS-02. Flyway falla

Revisar:

- orden de migraciones
- checksum
- schema

### OPS-03. Solicitud publica no se registra

Revisar:

- endpoint publico
- validaciones backend
- CORS
- logs de error

### OPS-04. Admin no consume backend

Revisar:

- `API_BASE_URL`
- backend arriba
- auth
- errores `401`, `403` o `5xx`

### OPS-05. Reporte o archivo no aparece

Revisar:

- `REPORT_STORAGE_PATH`
- worker o job backend
- metadata en DB
- permisos de ruta

---

## 4. Temas computacionales que debes dominar aqui

- runbooks
- troubleshooting disciplinado
- checklist de release
- criterio de publicacion
- operacion basada en evidencia y no en memoria
