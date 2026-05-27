# Tanda 4 — Storage e imágenes

## Resultado

El storage se parametriza con `app.storage.root` y deja de depender de una ruta quemada en clases Java.

## Cambios realizados

- `StorageProperties` centraliza raíz, reportes y resolución de rutas públicas.
- `StaticCatalogAssetService` usa la raíz configurada.
- `ProductImageService` guarda imágenes en la ruta configurada.
- Docker y `.env.example` declaran `APP_STORAGE_ROOT`.
- Se agregaron scripts de auditoría de assets.
