# T26-HF3 — Assets públicos y operatividad mínima del ERP interno

## Motivo

Durante la revisión visual se detectaron dos problemas de cierre:

1. En modo demo, las imágenes públicas podían no verse porque el perfil `presentation` usaba `storage-sit`, pero los assets reales estaban en `backend/storage/assets`.
2. El workspace ERP interno estaba demasiado orientado a consulta. El backend ya tenía endpoints de escritura para cartera, cobranzas, pagos, contabilidad, fiscalidad y bridges, pero la interfaz solo exponía una parte mínima de esos casos de uso.

## Correcciones

- `start-local-stack.ps1` ahora sincroniza `backend/storage/assets` hacia el storage activo antes de lanzar el backend.
- El storefront usa un fallback local SVG existente si el backend todavía no responde durante el primer render.
- El workspace ERP interno agrega casos de uso manuales:
  - registrar documento por cobrar;
  - registrar cobranza aplicada;
  - registrar pago proveedor;
  - registrar asiento simple;
  - preparar, emitir internamente y anular documento fiscal interno;
  - ejecutar bridges ERP idempotentes por ID.

## Alcance

No se cambió la estética general ni el storefront. Las acciones agregadas son formularios simples de administración interna, no un rediseño completo del ERP.
