# 03 - Bootstrap local, despliegue y storage

## 1. Proposito

Este documento define el arranque local reproducible y la base de despliegue del proyecto.

---

## 2. Estrategia local recomendada

- PostgreSQL en Docker Compose
- inicializacion de BD via `scripts/init-db.ps1`
- backend local con `Maven Wrapper + Temurin 21`
- Astro local con `Node.js 22.12.0+`
- Angular local con `Node.js 22.12.0+`

---

## 3. Secuencia sugerida

### Paso 1. Levantar PostgreSQL

```powershell
docker compose up -d postgres
```

### Paso 2. Inicializar esquema y seeds

```powershell
.\scripts\init-db.ps1
```

Notas:

- el puerto operativo documentado para PostgreSQL es `5434`
- esta ruta hoy es mas confiable que depender solo del autoarranque de Flyway dentro del backend
- el bootstrap limpio aplica `V1` a `V4`, incluyendo seed enriquecido para pedidos, produccion, reportes, notificaciones y auditoria
- si la base ya existia y solo quieres sumar el delta enriquecido, usa:

```powershell
.\scripts\apply-demo-delta.ps1
```

### Paso 3. Levantar backend

```powershell
cd backend
.\scripts\start-backend-dev.cmd
```

Si se necesita ejecucion manual:

```powershell
cd backend
$env:DB_PORT='5434'
$env:JWT_SECRET='ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ'
$env:REPORT_WORKER_ENABLED='true'
.\mvnw.cmd spring-boot:run
```

### Paso 4. Levantar Astro

```powershell
cd frontend-publico-astro
npm run dev
```

### Paso 5. Levantar Angular

```powershell
cd frontend-admin-angular
npm start
```

---

## 4. Nota operativa sobre el admin

Para que el admin cargue correctamente en local:

- el backend debe responder en `http://localhost:8080`
- el frontend admin puede resolverse en cualquier puerto local temporal, por ejemplo `http://localhost:4200` o `http://localhost:59305`
- el backend debe permitir CORS para `localhost:*` y `127.0.0.1:*` durante desarrollo local
- la URL base del admin debe venir de configuracion runtime, no de codigo de dominio

---

## 5. Regla de storage

La ruta de reportes o archivos debe venir de configuracion.

No mezclar:

- metadata en DB
- archivo fisico
- politica de nombre

Sin ese orden, el sistema se ensucia rapido.

Variables de V1 fuerte que conviene dejar visibles tambien en `.env`:

- `NOTIFICATION_DEFAULT_LIMIT`
- `NOTIFICATION_ARCHIVE_READ_AFTER_DAYS`
- `NOTIFICATION_ARCHIVE_CRON`
- `REPORT_WORKER_ENABLED`
- `REPORT_WORKER_DELAY_MS`
- `REPORT_WORKER_BATCH_SIZE`
- `REPORT_RETENTION_DAYS`
- `REPORT_CLEANUP_CRON`

Adicionalmente, las tablas administrativas de clientes, productos, pedidos y produccion ya deben consumir endpoints paginados para que el tablero no cargue listas completas cuando el seed o la operacion crecen.
