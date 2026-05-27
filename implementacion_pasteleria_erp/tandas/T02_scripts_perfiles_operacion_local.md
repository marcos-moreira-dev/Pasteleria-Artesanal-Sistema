# T02 — Scripts, perfiles y operación local

## 1. Objetivo

Ordenar la operación local del proyecto para que una persona pueda levantar, validar y presentar la pastelería con pocos comandos visibles.

Esta tanda implementa la disciplina aprendida de Cedro: scripts simples para humanos, perfiles separados, storage normal vs storage de presentación, puertos oficiales y configuración centralizada mínima.

## 2. Contexto heredado

La T01 dejó reglas maestras:

- La UX/UI actual de la pastelería se respeta.
- Cedro es canon de ingeniería, no de estética.
- No se copia restaurante literal.
- Las tandas se trabajan aquí en el chat, no como dependencia obligatoria de Codex.
- No es solo extender comportamiento; también se harán refactors decentes cuando el diseño actual lo pida.
- Todos los Markdown de implementación viven dentro de `implementacion_pasteleria_erp/`.

## 3. Fuente Cedro usada como referencia

Cedro aporta esta idea:

```text
pocos scripts visibles
DEV separado de presentation/SIT
storage normal separado de storage-sit
perfiles dev/presentation/staging/prod
.env.example, .env.staging.example, .env.production.example
Docker local solo para PostgreSQL
```

## 4. Estado anterior de Pastelería

El proyecto ya tenía bastantes scripts útiles, pero la superficie visible estaba mezclada:

- existían scripts con sufijo `-dev` como entrada principal;
- `INICIAR_SISTEMA.bat` apuntaba directamente al PowerShell viejo;
- `.env.example` favorecía `DB_PORT=5432` aunque el camino canónico local usa Docker `5436`;
- `application-local.yml` dejaba Flyway activo por defecto;
- no existía un perfil `presentation` explícito con `storage-sit`;
- no existían `.env.staging.example` ni `.env.production.example`;
- faltaban wrappers humanos tipo `dev.bat`, `pasteleria-demo.bat`, `test-admin.bat`, `test-storefront.bat`.

## 5. Alcance de la tanda

Se hizo:

- crear scripts humanos principales;
- conservar scripts antiguos como compatibilidad;
- alinear `.env.example` con PostgreSQL local Docker `5436`;
- crear `.env.staging.example` y `.env.production.example`;
- crear `application-presentation.yml`;
- crear `application-staging.yml`;
- endurecer `application-prod.yml` con storage por variables;
- cambiar `application-local.yml` para Flyway apagado por defecto;
- actualizar `scripts/start-dev-stack.ps1` para soportar `-Presentation` y `-ResetDatabase`;
- permitir que `dev-backend-inline.bat` respete variables externas de perfil/storage;
- actualizar `scripts/README.md`;
- actualizar `.gitignore` para `.pasteleria-dev/` y `storage-sit/`.

## 6. Fuera de alcance

No se hizo todavía:

- backend transversal;
- ApiResponse;
- contratos API;
- V1/V2 Flyway finales;
- Testcontainers;
- cambios Angular de arquitectura;
- cambios visuales;
- cambios en storefront;
- fiscalidad real.

## 7. Archivos modificados/creados

### Creados

```text
scripts/dev.bat
scripts/pasteleria-demo.bat
scripts/up-infra.bat
scripts/down-infra.bat
scripts/reset-infra.bat
scripts/dev-admin.bat
scripts/dev-storefront.bat
scripts/test-backend.bat
scripts/test-admin.bat
scripts/test-storefront.bat
.env.staging.example
.env.production.example
backend/src/main/resources/application-presentation.yml
backend/src/main/resources/application-staging.yml
implementacion_pasteleria_erp/tandas/T02_scripts_perfiles_operacion_local.md
```

### Modificados

```text
INICIAR_SISTEMA.bat
.env.example
.gitignore
docker-compose.yml
scripts/README.md
scripts/start-dev-stack.ps1
scripts/dev-backend-inline.bat
backend/src/main/resources/application-dev.yml
backend/src/main/resources/application-local.yml
backend/src/main/resources/application-prod.yml
```

## 8. Decisiones tomadas

### Superficie nueva recomendada

```bat
scripts\dev.bat
scripts\pasteleria-demo.bat
scripts\test-backend.bat
scripts\test-admin.bat
scripts\test-storefront.bat
```

### Scripts antiguos

No se borraron. Quedan como compatibilidad.

### Presentation/SIT

`pasteleria-demo.bat` reinicia infraestructura y usa:

```text
SPRING_PROFILES_ACTIVE=presentation
APP_STORAGE_ROOT=./storage-sit
SPRING_FLYWAY_ENABLED=false
```

Mientras V1/V2 no estén compactadas, presentation todavía usa SQL canónico estabilizado.

### Flyway

Local/presentation usan Flyway apagado porque la fuente actual estable sigue siendo SQL canónico.

```text
SPRING_FLYWAY_ENABLED=false
```

Staging/prod quedan preparados para Flyway activo cuando V1/V2 estén limpias.

## 9. Riesgos

- Los scripts `.bat` no se ejecutaron aquí por estar en entorno Linux.
- Los builds no se ejecutaron en esta tanda.
- `presentation` todavía no tiene seeds ricos tipo Cedro; eso queda para T24.
- `docker-compose.yml` raíz se hizo más seguro con Flyway apagado, pero la ruta recomendada sigue siendo `infra/compose/docker-compose.dev.yml`.

## 10. Criterios de aceptación

La tanda se acepta si:

- existe `scripts/dev.bat`;
- existe `scripts/pasteleria-demo.bat`;
- existen wrappers `up-infra.bat`, `down-infra.bat`, `reset-infra.bat`;
- existe `application-presentation.yml`;
- `.env.example` usa `DB_PORT=5436` como local canónico;
- `application-local.yml` usa Flyway apagado por defecto;
- `scripts/README.md` documenta la nueva superficie;
- la UX/UI no fue tocada;
- backend/frontend/sql funcional no fue refactorizado aún.

## 11. Pruebas mínimas sugeridas en Windows

Desde la raíz:

```bat
scripts\dev.bat -DryRun
scripts\pasteleria-demo.bat -DryRun
scripts\up-infra.bat
scripts\down-infra.bat
scripts\test-backend.bat
scripts\test-admin.bat
scripts\test-storefront.bat
```

## 12. Notas para el siguiente chat

La siguiente tanda es T03:

```text
T03 — Backend transversal: ApiResponse, errores, requestId y paginación.
```

T03 ya tocará backend. Debe leer primero:

```text
backend/src/main/java/**/common/**
backend/src/main/java/**/config/**
backend/src/main/java/**/security/**
backend/src/main/java/**/controller/**
```

y debe implementar/refactorizar con cuidado, sin romper contratos actuales del Angular.
