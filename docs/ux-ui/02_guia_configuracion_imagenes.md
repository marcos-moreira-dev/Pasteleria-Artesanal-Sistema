# Guia de Configuracion de Imagenes

Documento auxiliar de operacion para assets visuales.

## Regla base

El backend es dueno del branding y de las imagenes de producto. Los frontends
solo consumen rutas resueltas por el backend.

## Ubicaciones reales en esta V1

- Branding: `backend/storage/assets/branding/`
- Productos: `backend/storage/assets/products/`
- Placeholders: `backend/storage/assets/placeholders/`

## Convencion

- La imagen publica del producto se resuelve por `slug`.
- Si agregas `storage/assets/products/<slug>.png|jpg|jpeg|webp`, el sistema la toma.
- Si no existe, el backend responde el placeholder oficial.
- El landing y el admin no necesitan hardcodear rutas de archivos ni copiar assets.

## Nota

La especificacion tecnica completa vive en:
- [backend_03_dtos_y_contratos_api.md](C:\Users\MARCOS MOREIRA\Downloads\Pastelería\docs\backend\backend_03_dtos_y_contratos_api.md)
- [00_frontend_publico_canonico.md](C:\Users\MARCOS MOREIRA\Downloads\Pastelería\docs\frontend-publico-astro\00_frontend_publico_canonico.md)
- [00_frontend_admin_canonico.md](C:\Users\MARCOS MOREIRA\Downloads\Pastelería\docs\frontend-admin-angular\00_frontend_admin_canonico.md)
