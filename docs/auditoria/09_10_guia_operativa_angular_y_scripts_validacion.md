# Auditoría — Tandas 9 y 10

## Alcance

Se implementaron las dos tandas siguientes después del backend y seeds de guía operativa:

- Tanda 9: pantalla Angular de Guía operativa.
- Tanda 10: scripts de validación estilo Cedro Damasco.

## Tanda 9 — Admin Angular

Archivos principales agregados o modificados:

- `frontend-admin-angular/src/app/features/guia-operativa/guia-operativa-page.component.ts`
- `frontend-admin-angular/src/app/features/guia-operativa/models/guia-operativa.models.ts`
- `frontend-admin-angular/src/app/app.routes.ts`
- `frontend-admin-angular/src/app/core/api/api-client.service.ts`
- `frontend-admin-angular/src/app/core/config/business-shell.config.ts`
- `frontend-admin-angular/src/app/layout/shell/shell.component.ts`
- `frontend-admin-angular/src/assets/icons/guide.svg`

Decisiones:

- La ruta visible es `/guia-operativa`.
- La etiqueta visible es `Guía operativa`, no `casos de uso`.
- La pantalla consume primero `GET /api/v1/casos-uso/hub`.
- Si el endpoint agrupado falla, intenta construir el hub desde `GET /api/v1/casos-uso`.
- La UI mantiene tarjetas, chips, paleta cálida, bordes discretos y estilo de Pastelería.

## Tanda 10 — Scripts y validación

Archivos principales agregados:

- `scripts/README.md`
- `scripts/check-dev-env.bat`
- `scripts/reset-db-local.ps1`
- `scripts/validate-backend.bat`
- `scripts/validate-admin-angular.bat`
- `scripts/validate-public-astro.bat`
- `scripts/validate-all.bat`
- `scripts/smoke-backend-local.ps1`

Archivos ajustados:

- `scripts/start-dev-stack.ps1`
- `backend/scripts/start-backend-dev.cmd`
- `backend/scripts/start-backend-background.ps1`
- `backend/scripts/smoke-orden-compra.ps1`
- `backend/scripts/verify-report-flow.ps1`
- `.gitignore`
- `frontend-publico-astro/src/lib/api.ts`

Decisiones:

- El flujo local canónico usa PostgreSQL en `localhost:5436`.
- La base local se crea desde `db/V1/DATABASE_SCHEMA_CANONICO.sql` y `db/V1/DATABASE_SEED_CANONICO.sql`.
- Para evitar conflicto entre base canónica ya creada y migraciones Flyway, los scripts locales fijan `SPRING_FLYWAY_ENABLED=false` por defecto.
- Los logs de validación se guardan en `.diagnostics/logs/`.
- La carpeta `.diagnostics/` queda ignorada por Git.
- Se corrigió el fallback de `BrandingAssets` en Astro para incluir `categoryBannerPath` y evitar un posible error de contrato TypeScript durante build.

## Pendiente posterior

- Tanda 11 fue aplicada posteriormente: documentación y trazabilidad humana.
- Tanda 12 fue aplicada posteriormente: verificación Astro y cierre QA.
