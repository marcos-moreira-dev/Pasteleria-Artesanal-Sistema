# 03 - Bootstrap local, despliegue y storage

## 1. Proposito

Este documento define el arranque local reproducible y la base de despliegue del
proyecto.

---

## 2. Estrategia local recomendada

- PostgreSQL en Docker Compose
- inicializacion de BD via `scripts/init-db.ps1`
- backend local con Maven Wrapper y Java 21
- Astro local con Node.js 22.12.0+
- Angular local con Node.js 22.12.0+

Atajo principal:

- `INICIAR_SISTEMA.bat`

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

### Paso 3. Levantar backend

```powershell
cd backend
.\scripts\start-backend-dev.cmd
```

### Paso 4. Levantar Astro

```powershell
cd frontend-publico-astro
$env:PUBLIC_API_BASE_URL='http://localhost:8080/api/v1'
npm run dev
```

### Paso 5. Levantar Angular

```powershell
cd frontend-admin-angular
npm start
```

---

## 4. Defaults operativos reales

- PostgreSQL Docker: `5436`
- PostgreSQL nativo: `5432`
- Backend local: `8080`
- Backend Docker: `8081`
- Admin Angular: `4200`
- Frontend publico Astro: `4321`

---

## 5. Regla de storage

La ruta de reportes o archivos debe venir de configuracion.

No mezclar:

- metadata en DB
- archivo fisico
- politica de nombre

---

## 6. Temas computacionales que debes dominar aqui

- bootstrap reproducible
- puertos y dependencias locales
- storage de archivos
- configuracion de herramientas
- automatizacion del arranque
