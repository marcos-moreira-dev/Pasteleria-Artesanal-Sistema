# 01 - Entornos, variables y perfiles

## 1. Proposito

Este documento fija la politica de configuracion del proyecto.

---

## 2. Entornos minimos

- `local`
- `test`
- `prod`

Aplican sobre todo al backend, pero tambien deben reflejarse en Astro y Angular.

---

## 3. Variables backend recomendadas

- `SPRING_PROFILES_ACTIVE`
- `APP_PORT`
- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_SECONDS`
- `JWT_ISSUER`
- `REPORT_STORAGE_PATH`
- `PUBLIC_BASE_URL`

---

## 4. Variables frontend publico

- `PUBLIC_API_BASE_URL`
- `PUBLIC_SITE_URL`

---

## 5. Variables frontend administrativo

- `API_BASE_URL`

---

## 6. Reglas de configuracion

- no hardcodear secretos
- `.env.example` solo con valores seguros o placeholders
- URLs por entorno
- rutas de storage configurables
- defaults locales coherentes con los scripts de arranque

---

## 7. Temas computacionales que debes dominar aqui

- configuracion por entorno
- secretos vs configuracion publica
- precedence de variables
- defaults seguros
- consistencia entre app, docs y scripts
