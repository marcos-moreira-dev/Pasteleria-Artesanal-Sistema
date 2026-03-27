# 00 - Operacion, calidad, despliegue y referencia

## 1. Proposito

Este documento cierra la capa operativa de Pasteleria para que el proyecto no
quede solo en analisis y arquitectura.

Su objetivo es fijar buenas practicas para:

- `README`
- `.gitignore`
- variables de entorno
- arranque local
- calidad
- runbooks
- release

---

## 2. Artefactos no codigo que tambien son ingenieria

En este proyecto deben considerarse parte formal del sistema:

- `README.md`
- `.gitignore`
- `.env.example`
- `docker-compose.yml`
- scripts de arranque
- checklists de release
- runbooks

Regla:

- si uno de estos archivos contradice el estado real del sistema, el problema es de ingenieria

---

## 3. Reproducibilidad local

La linea base recomendada es:

- PostgreSQL por Docker Compose
- backend local
- Astro local
- Angular local

Atajo principal en Windows:

- `INICIAR_SISTEMA.bat`

Ese punto de entrada debe ser entendible por una persona y utilizable por otra IA.

---

## 4. Calidad minima operativa

El proyecto no esta listo si:

- compila a veces
- depende de memoria tribal
- arranca distinto segun quien lo levante
- o sus docs contradicen los puertos, rutas y contratos reales

---

## 5. Temas computacionales que debes dominar aqui

Si quieres estudiar esta capa como profesional, los temas mas importantes son:

- configuracion por entorno
- reproducibilidad local
- automatizacion de arranque
- release readiness
- runbooks
- trazabilidad entre docs y operacion real

---

## 6. Resultado esperado

Pasteleria debe nacer:

- documentado
- reproducible
- mantenible
- y listo para demo seria
