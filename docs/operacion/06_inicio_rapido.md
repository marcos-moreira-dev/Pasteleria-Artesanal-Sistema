# Inicio Rapido

Este documento es un atajo operativo.

La fuente de verdad para arranque y contexto sigue siendo:

- `README.md`
- `docs/00_indice_documentacion_canonica.md`
- `docs/operacion/00_operacion_calidad_despliegue_y_referencia.md`

---

## Flujo minimo recomendado

Atajo recomendado en Windows:

- `INICIAR_SISTEMA.bat`

Ese punto de entrada deja la base lista, levanta backend y ambos frontends, y
abre automaticamente el admin y la landing en el navegador.

Flujo manual equivalente:

1. Inicializa la base con el esquema y seed canonicos.
2. Inicia backend.
3. Inicia admin Angular.
4. Inicia frontend publico Astro.

---

## Puertos esperados en local

- Backend: `http://localhost:8080`
- Admin Angular: `http://localhost:4200`
- Landing Astro: `http://localhost:4321`

---

## Credenciales locales de revisión

- Usuario: `admin`
- Contrasena: `admin12345`
