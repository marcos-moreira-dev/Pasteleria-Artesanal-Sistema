# Ejecucion Local Rapida

Referencia corta para desarrollo local.

No reemplaza al `README.md`; solo resume el recorrido minimo cuando ya conoces
el proyecto.

---

## Script automatico

- Ejecuta `INICIAR_SISTEMA.bat`

---

## Flujo manual minimo

### Backend

```powershell
cd C:\Users\MARCOS MOREIRA\Downloads\Pastelería\backend
.\scripts\start-backend-dev.cmd
```

### Admin Angular

```powershell
cd C:\Users\MARCOS MOREIRA\Downloads\Pastelería\frontend-admin-angular
npm start
```

### Landing Astro

```powershell
cd C:\Users\MARCOS MOREIRA\Downloads\Pastelería\frontend-publico-astro
npm run dev
```

---

## Verificacion minima

- `http://localhost:8080/actuator/health`
- `http://localhost:4200`
- `http://localhost:4321`
