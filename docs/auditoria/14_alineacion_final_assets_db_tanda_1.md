# Tanda de alineación final 1 — Base canónica, assets de productos e iconos

## Objetivo

Cerrar los fallos detectados antes de la revisión visual manual:

- La base canónica fallaba durante `scripts\validate-all.bat` porque `caso_uso_operativo` no tenía la columna `orden_visual`, aunque el índice `idx_caso_uso_operativo_hub` la usaba.
- El catálogo extendido tenía 26 productos publicados en el seed canónico, pero solo 14 imágenes físicas dedicadas en `backend/storage/assets/products`.
- Angular referenciaba `assets/icons/abastecimiento/check.svg`, pero el archivo no existía.
- La auditoría de assets necesitaba ser más estricta para diferenciar imágenes reales de fallback por logo.

## Cambios aplicados

### Base de datos

Archivo actualizado:

- `db/V1/DATABASE_SCHEMA_CANONICO.sql`

Cambio:

- Se agregó `orden_visual INTEGER NOT NULL DEFAULT 999` a `caso_uso_operativo`.
- La tabla queda alineada con:
  - `CasoUsoOperativoEntity`
  - `V13__guia_operativa.sql`
  - índice `idx_caso_uso_operativo_hub`
  - seed de guía operativa

### Imágenes de productos

Carpeta actualizada:

- `backend/storage/assets/products/`

Se agregaron 12 imágenes físicas dedicadas:

- `torta-zanahoria-nuez.png`
- `naked-cake-boda.png`
- `tiramisu-familiar.png`
- `pie-limon-artesanal.png`
- `galletas-mix-mantequilla.png`
- `galleta-corporativa-logo.png`
- `frappe-mocha.png`
- `chocolate-caliente-casa.png`
- `cupcake-red-velvet.png`
- `cupcake-oreo.png`
- `brigadeiro-box-12.png`
- `mesa-dulce-80-personas.png`

Resultado esperado:

- 26 productos del seed canónico.
- 26 imágenes físicas dedicadas.
- El logo queda como fallback real, no como sustituto silencioso de productos principales.

### Icono faltante

Archivo agregado:

- `frontend-admin-angular/src/assets/icons/abastecimiento/check.svg`

Con esto, la pantalla de proveedores deja de depender de un icono inexistente.

### Auditoría de assets

Archivo actualizado:

- `scripts/audit-assets.ps1`

Mejoras:

- Lee productos desde `db/V1/DATABASE_SEED_CANONICO.sql`.
- Verifica imágenes dedicadas por slug.
- Verifica branding base.
- Verifica placeholders base.
- Verifica assets referenciados desde Angular admin.
- Genera reporte Markdown y JSON en `.diagnostics/assets/`.
- Soporta `-FailOnMissing` para volver estricta la validación.

### Suite integral

Archivo actualizado:

- `scripts/validate-system-full.ps1`

Corrección:

- Se ajustó la tabla esperada de `unidad_medida` a `umedida`, que es el nombre físico real de la base canónica.

## Validación estática realizada

- `caso_uso_operativo` ya contiene `orden_visual` en el SQL canónico.
- Los 26 slugs de productos del seed canónico tienen imagen física.
- Las 26 imágenes de producto se verificaron como archivos válidos.
- Las referencias de assets Angular detectadas tienen archivo existente, sea en `frontend-admin-angular/src/assets` o en `backend/storage/assets` cuando son rutas servidas por backend.

## Pendiente recomendado para la siguiente tanda

- Ejecutar `scripts\validate-all.bat` en Windows.
- Si pasa backend, revisar Angular/Astro.
- Luego decidir si generar capturas reales para `README.md` y limpiar referencias documentales faltantes tipo `readme/*.png`.
