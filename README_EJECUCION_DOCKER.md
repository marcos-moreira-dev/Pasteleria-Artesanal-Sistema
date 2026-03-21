# 🚀 Guía de Ejecución - Pastelería (Docker)

## ⚡ Requisitos

- Docker Desktop (con Docker Compose)
- Node.js 22.12.0+
- Java 21 (para compilar el backend)

## 🎯 Ejecución Completa

### Paso 1: Backend y Base de Datos (Docker)

```bash
cd C:\Users\MARCOS MOREIRA\Downloads\Pastelería
docker-compose up -d
```

**Espera 30 segundos** y verifica:
- Backend: http://localhost:8081/api/v1/public/catalogo/productos
- Debe mostrar 14 productos publicados

### Paso 2: Landing Público (Astro)

En una **nueva terminal**:

```bash
cd C:\Users\MARCOS MOREIRA\Downloads\Pastelería\frontend-publico-astro
npm run dev
```

**URL:** http://localhost:4321

### Paso 3: Admin Angular

En una **nueva terminal**:

```bash
cd C:\Users\MARCOS MOREIRA\Downloads\Pastelería\frontend-admin-angular
npm start
```

**URL:** http://localhost:4200

---

## 🔑 Credenciales

| Sistema | Usuario | Password |
|---------|---------|----------|
| Admin Angular | `admin` | `admin12345` |

---

## 🌐 URLs del Sistema

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **Landing** | http://localhost:4321 | Catálogo público con 14 postres |
| **Admin** | http://localhost:4200 | Panel de administración |
| **Backend API** | http://localhost:8081 | API REST |
| **Productos API** | http://localhost:8081/api/v1/public/catalogo/productos | JSON con productos |

---

## 🛑 Detener el Sistema

```bash
# Detener Docker (desde la carpeta del proyecto)
docker-compose down

# Detener frontends: Ctrl+C en cada terminal
```

---

## 🔄 Reiniciar todo

```bash
# 1. Detener todo
docker-compose down

# 2. Volver a iniciar
docker-compose up -d
# ... y luego los frontends en sus terminales
```

---

## ⚠️ Notas Importantes

- El backend usa puerto **8081** (no 8080 como antes)
- PostgreSQL corre en Docker, **no necesitas pgAdmin**
- Los datos se persisten en el volumen `pasteleria-postgres-data`
- Si eliminas el volumen, los datos se pierden y se recrean desde cero

---

## 🆘 Solución de Problemas

### "El backend no responde"
```bash
docker-compose logs pasteleria-backend
```

### "Puerto 8081 en uso"
```bash
# Busca qué usa el puerto
netstat -ano | findstr :8081
# Mata el proceso o cambia el puerto en docker-compose.yml
```

### "npm run dev no funciona"
```bash
npm install
npm run dev
```

---

**¡Listo! Abre http://localhost:4321 para ver los postres** 🧁
