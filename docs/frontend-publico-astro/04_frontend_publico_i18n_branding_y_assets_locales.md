# 04 - Frontend publico: i18n, branding y assets locales

## 1. Proposito

Este documento congela la politica de internacionalizacion y ownership de assets del frontend publico de Pasteleria.

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

- el negocio y su operacion principal estan orientados primero al contexto hispano
- el ingles existe como capacidad comercial complementaria, no como idioma dominante

---

## 4. Estrategia de rutas recomendada

La estrategia mas sobria es:

- `/` para espanol
- `/en/` para ingles

Regla:

- no crear `/es/` en V1 salvo que una decision futura lo exija
- si se agregan versiones localizadas de pagina, deben mantener equivalencia clara entre `es` y `en`

Ejemplos:

- `/`
- `/catalogo`
- `/cotizador`
- `/en/`
- `/en/catalog`
- `/en/quote`

---

## 5. Ownership de textos

Los textos visibles no deben quedar dispersos en componentes sin criterio.

La recomendacion canonica es:

- diccionarios o archivos de mensajes por locale
- contenido institucional editable o localizable con ownership claro
- y claves semanticas, no textos sueltos desordenados

Ejemplos de ownership:

- `src/content/` o `src/i18n/`
- `messages.es.ts`
- `messages.en.ts`

---

## 6. Reglas de i18n

1. No mezclar idiomas en la misma pantalla salvo nombres propios.
2. Los `alt`, `title` y metadata deben respetar el locale.
3. Los formularios deben mostrar validaciones en el idioma de la vista.
4. La navegacion debe poder cambiar entre `es` y `en` sin romper rutas equivalentes.
5. El idioma por defecto no debe depender del navegador de forma obligatoria en V1.

---

## 7. SEO e i18n

Si existen dos versiones de una misma pagina, debe contemplarse:

- `canonical` correcto
- equivalencias por locale
- `hreflang` cuando aplique
- metadata localizada

Esto es parte del cierre profesional de la landing, no un extra opcional.

---

## 8. Ownership de branding y assets locales

Los recursos visuales principales deben vivir dentro del proyecto.

Estructura sugerida:

```text
public/
  assets/
    branding/
      logo-primary.svg
      logo-mark.svg
      favicon.ico
      apple-touch-icon.png
      site.webmanifest
    icons/
      whatsapp.svg
      instagram.svg
      location.svg
    fonts/
      fraunces/
      inter/
```

---

## 9. Reglas para logos, iconos y tipografias

### Logos

- usar versiones locales
- preferir `SVG` cuando sea posible
- separar `logo principal`, `isotipo` y `favicon`

### Iconos

- preferir `SVG`
- descargar y guardar localmente los iconos de uso real
- no depender de librerias remotas si con assets propios basta

### Tipografias

- almacenar `woff2` o formatos adecuados en carpeta local
- declarar con `@font-face`
- no depender de Google Fonts por CDN como base obligatoria

---

## 10. Registro minimo de origen

Cuando se descargue o incorpore un asset conviene registrar como minimo:

- nombre del asset
- origen
- licencia o nota de uso si aplica
- fecha de incorporacion

Esto puede resolverse con un `README.md` corto dentro de la carpeta del recurso.

---

## 11. Regla profesional

La landing no debe depender de que "internet cargue la fuente" o "ya luego bajamos el icono".

Debe nacer lista con:

- `i18n` cerrada
- branding local
- iconografia local
- tipografias locales
- y placeholders cuando falten imagenes finales
