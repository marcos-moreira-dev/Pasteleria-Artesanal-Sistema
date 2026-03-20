# 04 - Runbooks y release checklist

## 1. Proposito

Este documento cierra la capa operativa antes de release, demo seria o arranque disciplinado.

---

## 2. Checklist minimo antes de release

1. Confirmar variables y secretos.
2. Confirmar migraciones y seeds.
3. Validar health check backend.
4. Validar login y roles.
5. Validar catalogo publico.
6. Validar cotizador.
7. Validar frontend administrativo y panel de produccion.
8. Validar reportes async y storage.
9. Confirmar que el README publico pueda actualizarse con logo y capturas reales cuando la version ya merezca publicacion.

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

### OPS-03. Cotizador no registra solicitud

Revisar:

- endpoint publico
- validaciones backend
- CORS
- logs de error

### OPS-04. Angular o panel no consume backend

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

## 4. Regla profesional

Si el proyecto no tiene checklist ni runbooks, la operacion sigue dependiendo de memoria e intuicion.

Eso no es una base enterprise.
