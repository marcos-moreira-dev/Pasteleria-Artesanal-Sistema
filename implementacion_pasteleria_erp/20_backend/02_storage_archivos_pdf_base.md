# Storage, archivos, assets y PDF base

## Regla general

La pastelería no debe exponer rutas físicas del servidor al frontend.

Se separan dos mundos:

```text
assets públicos
archivos internos
```

## Assets públicos

Son imágenes o recursos visuales aptos para catálogo/storefront:

```text
branding
products
categories
branches
placeholders
```

Se sirven por:

```text
/api/v1/assets/{type}/{filename}
```

También se conserva:

```text
/assets/{type}/{filename}
```

para compatibilidad.

## Archivos internos

Son recursos generados o administrativos:

```text
reportes PDF
recetas PDF
guías operativas PDF
comprobantes PDF
documentos fiscales preparados
exports CSV/XLSX
```

Se descargan por:

```text
/api/v1/archivos/{archivoId}/descargar
```

## Reglas de refactoring

- Reportes no deben saber cómo guardar físicamente cualquier archivo.
- Reportes pueden usar un adaptador, pero el storage físico debe ser transversal.
- PDFs deben tener fondo blanco explícito.
- Las rutas relativas deben validarse contra path traversal.
- El frontend recibe URLs públicas o IDs de archivo, nunca rutas locales.

## Actualización T06 — Manual PDF de Guía Operativa

El primer PDF transversal construido sobre esta base es:

```http
GET /api/v1/casos-uso/manual.pdf
```

El manual se genera en caliente desde el catálogo vivo de casos de uso. En una tanda posterior puede guardarse como `archivo_recurso` si se requiere auditoría de descarga, expiración o cache.
