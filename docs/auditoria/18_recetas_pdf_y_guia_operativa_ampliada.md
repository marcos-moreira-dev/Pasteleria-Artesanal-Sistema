# Hotfix de cierre — recetas PDF y guía operativa ampliada

## Motivo
Durante la revisión visual del admin se detectaron tres problemas:

1. La Guía operativa tenía textos con acentos dañados cuando la base era inicializada desde PowerShell.
2. Cada módulo mostraba solo un caso de uso, lo que hacía que la guía pareciera demasiado pobre para un manual operativo real.
3. La descarga de receta existía conceptualmente, pero no estaba suficientemente visible ni conectada al endpoint PDF del backend desde el admin.

## Cambios aplicados

### Guía operativa
- Se amplió la guía a 42 casos operativos visibles.
- Cada módulo principal queda con varios procedimientos.
- Los pasos ahora usan instrucciones más explícitas: “Haz clic en…”, “Busca…”, “Revisa…”, “Guarda…”.
- Se corrigieron textos y acentos en el seed canónico y en la migración de contenido final.
- Se añadió `V14__contenido_operativo_final.sql` como parche idempotente para bases ya existentes.

### Codificación UTF-8
- `scripts/init-db.ps1` ahora lee SQL con `-Encoding UTF8`.
- `scripts/repair-guia-operativa-db.ps1` también aplica SQL con `-Encoding UTF8`.
- Los SQL canónicos declaran `SET client_encoding = 'UTF8';`.

### Recetas PDF
- Se añadieron recetas JSON para los 26 productos del catálogo canónico.
- El admin muestra el botón “Descargar receta PDF” en las tarjetas de producto cuando la receta existe.
- La descarga usa el endpoint real del backend:
  - `GET /api/v1/abastecimiento/recetas/producto/{productoId}/pdf`
- El PDF del backend ahora intenta incluir:
  - logo de la pastelería;
  - imagen del producto;
  - código, categoría, precio base, tipo y slug;
  - ingredientes, preparación y notas.

## Archivos principales tocados
- `db/V1/DATABASE_SEED_CANONICO.sql`
- `db/V1/DATABASE_SCHEMA_CANONICO.sql`
- `backend/src/main/resources/db/migration/V13__guia_operativa.sql`
- `backend/src/main/resources/db/migration/V14__contenido_operativo_final.sql`
- `backend/src/main/java/com/pasteleria/abastecimiento/application/RecetaPdfService.java`
- `frontend-admin-angular/src/app/core/api/api-client.service.ts`
- `frontend-admin-angular/src/app/features/productos/state/products.facade.ts`
- `frontend-admin-angular/src/app/features/productos/products-page.component.ts`
- `frontend-admin-angular/src/app/features/guia-operativa/guia-operativa-page.component.ts`
- `scripts/init-db.ps1`
- `scripts/repair-guia-operativa-db.ps1`

## Nota operativa
Si la base ya existe, basta con levantar el backend usando los scripts nuevos; `init-db.ps1` aplicará el parche idempotente de contenido operativo. Si se quiere limpiar completamente la base, usar el reset canónico.
