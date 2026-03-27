# 04 - Frontend publico: i18n, branding y assets locales

## 1. Proposito

Este documento congela la politica de internacionalizacion y ownership de assets
del frontend publico de Pasteleria.

---

## 2. Politica de i18n de V1

La V1 soporta solo estos idiomas:

- `es`
- `en`

No se contemplan mas idiomas en esta etapa.

---

## 3. Idioma por defecto

El idioma por defecto del proyecto es:

- `es`

Motivo:

- el negocio opera primero en contexto hispano
- el ingles existe como capacidad comercial complementaria

---

## 4. Estrategia de rutas

La estrategia real y sobria hoy es:

- `/` para espanol
- `/catalogo`
- `/contacto`
- `/en`
- `/en/catalog`
- `/en/contact`

Regla:

- no crear `/es/` en V1 salvo decision futura
- si se agregan versiones localizadas de pagina, deben mantener equivalencia clara
- no documentar rutas publicas que el producto todavia no tiene

---

## 5. Ownership de textos

Los textos visibles no deben quedar dispersos en componentes sin criterio.

La recomendacion canonica es:

- archivos o diccionarios por locale
- contenido institucional con ownership claro
- claves semanticas, no textos sueltos desordenados

---

## 6. Reglas de i18n

1. No mezclar idiomas en la misma pantalla salvo nombres propios.
2. Los `alt`, `title` y metadata deben respetar el locale.
3. Los formularios deben mostrar validaciones en el idioma de la vista.
4. La navegacion debe poder cambiar entre `es` y `en` sin romper equivalencias.
5. El idioma por defecto no debe depender del navegador en V1.

---

## 7. Ownership de branding y assets locales

Los recursos visuales principales del frontend publico se consumen del backend
cuando son branding o imagen de producto.

Los assets locales del proyecto quedan para:

- iconos propios
- tipografias locales
- decoracion no critica

Regla:

- el frontend no inventa nombres de archivo de producto
- el backend sigue siendo la fuente de verdad visual para branding y catalogo
