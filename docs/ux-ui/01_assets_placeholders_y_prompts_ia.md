# 01 - Assets, Placeholders y Prompts IA

## 1. Propósito

Este documento fija la política visual mínima del proyecto mientras una imagen
definitiva no exista o deba reemplazarse después.

La regla obligatoria es esta:

**ninguna pantalla debe depender de que el asset final ya exista.**

## 2. Regla placeholder-first

En `Pastelería` no se bloquea implementación por falta de imagen final.

Por eso:

- cada imagen prevista debe tener identificador estable
- cada espacio visual debe aceptar placeholder
- cada prompt aprobado debe poder recuperarse
- y el frontend debe tolerar que el asset final llegue después

## 3. Alcance

Aplica a:

- logo y variantes de marca
- banner principal del landing
- imágenes de producto
- placeholders de producto en admin
- imágenes de apoyo visual
- iconos y tipografías locales

## 4. Criterio de centralización

Para esta V1 no se usa storage externo ni servicios complejos de assets.

La solución oficial es:

- branding y productos viven en `backend/storage/assets`
- el backend resuelve y sirve esas imágenes por HTTP
- el nombre canónico de imagen de producto es el `slug`

Ejemplo:

- producto: `cheesecake-frutos-rojos`
- archivo esperado: `backend/storage/assets/products/cheesecake-frutos-rojos.png`

Si existe:

- landing y admin la consumen automáticamente

Si no existe:

- backend devuelve el placeholder oficial

## 5. Estructura mínima

```text
backend/
  storage/
    assets/
      branding/
      products/
      placeholders/
```

## 6. Convención de nombres

Formato recomendado:

- `logo-horizontal.png`
- `logo-cuadrado.png`
- `banner-chicas.png`
- `cheesecake-frutos-rojos.png`
- `torta-red-velvet-grande.png`
- `producto-default.png`

## 7. Reglas de implementación

1. El componente no debe romper si falta la imagen final.
2. Todo asset debe tener fallback a placeholder.
3. El `slug` del producto debe ser estable y usable como nombre de archivo.
4. Logo, iconos y tipografías deben vivir localmente.
5. Si una imagen final cambia, la ruta pública no debería cambiar sin necesidad.

## 8. Tipografías locales activas

Mientras el negocio no entregue tipografía definitiva, el proyecto queda
cerrado con esta pareja local:

- `Cormorant Garamond` para titulares y marca
- `Source Sans 3` para lectura, formularios y operación diaria

Ambas fuentes viven dentro del proyecto, sin dependencia de CDN.

## 9. Relación con la documentación canónica

Este documento es auxiliar. La especificación técnica completa vive en:

- [backend_03_dtos_y_contratos_api.md](C:\Users\MARCOS MOREIRA\Downloads\Pastelería\docs\backend\backend_03_dtos_y_contratos_api.md)
- [00_frontend_publico_canonico.md](C:\Users\MARCOS MOREIRA\Downloads\Pastelería\docs\frontend-publico-astro\00_frontend_publico_canonico.md)
- [00_frontend_admin_canonico.md](C:\Users\MARCOS MOREIRA\Downloads\Pastelería\docs\frontend-admin-angular\00_frontend_admin_canonico.md)
- [02_guia_configuracion_imagenes.md](C:\Users\MARCOS MOREIRA\Downloads\Pastelería\docs\ux-ui\02_guia_configuracion_imagenes.md)
