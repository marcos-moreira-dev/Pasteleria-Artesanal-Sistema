# Assets públicos en modo local y demo

Los assets visibles del catálogo se guardan en `backend/storage/assets`.

Cuando se usa `scripts/run-demo.bat`, el sistema trabaja con `backend/storage-sit` para no contaminar el storage operativo. Por eso el arranque sincroniza automáticamente las imágenes públicas hacia `backend/storage-sit/assets`.

Esto permite que el admin y el storefront puedan cargar:

- logos;
- banners;
- placeholders;
- imágenes de productos.
