# Ejecucion con Docker

Documento auxiliar para levantar PostgreSQL y backend con Docker.

No sustituye el `README.md`; solo resume el caso Docker.

---

## Inicio

```powershell
cd C:\Users\MARCOS MOREIRA\Downloads\Pastelería
docker-compose up -d
```

---

## Superficies esperadas

- PostgreSQL Docker: `localhost:5436`
- Backend Docker: `http://localhost:8081`

Los frontends siguen levantandose localmente:

- Landing Astro: `http://localhost:4321`
- Admin Angular: `http://localhost:4200`

---

## Frontends locales

```powershell
cd C:\Users\MARCOS MOREIRA\Downloads\Pastelería\frontend-publico-astro
npm run dev
```

```powershell
cd C:\Users\MARCOS MOREIRA\Downloads\Pastelería\frontend-admin-angular
npm start
```

---

## Cierre

```powershell
docker-compose down
```
