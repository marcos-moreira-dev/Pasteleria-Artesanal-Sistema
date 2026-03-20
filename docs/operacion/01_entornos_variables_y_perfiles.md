# 01 - Entornos, variables y perfiles

## 1. Proposito

Este documento fija la politica de configuracion del proyecto de Pasteleria.

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

## 6. Reglas de secretos

- no hardcodear secretos
- `.env.example` solo con placeholders
- URLs por entorno
- rutas de storage configurables

