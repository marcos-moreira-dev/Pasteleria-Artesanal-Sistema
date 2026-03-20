# 01 - Frontend publico: rutas, componentes y contenido

## 1. Proposito

Este documento fija la estructura navegable del frontend publico y la forma correcta de repartir componentes y contenido.

---

## 2. Mapa de rutas recomendado

Rutas base sugeridas para V1:

- `/`
- `/catalogo`
- `/catalogo/[slug]` o `/productos/[slug]`
- `/cotizador`
- `/contacto`
- `/nosotros` si el relato de marca lo justifica
- `/politica-privacidad` y equivalentes si se requieren

Regla:

- el mapa final debe ser pequeno, claro y facil de mantener

---

## 3. Objetivo por ruta

### Home

Debe resolver:

- propuesta de valor
- productos o categorias destacadas
- confianza
- CTA hacia cotizador y contacto

### Catalogo

Debe resolver:

- exploracion simple
- filtros ligeros si hacen falta
- visualizacion de categorias o productos destacados

### Detalle

Debe resolver:

- descripcion corta
- imagenes
- atributos basicos
- CTA hacia cotizador o contacto

### Cotizador

Debe resolver:

- captura guiada de requerimientos
- resumen claro de seleccion
- envio confiable

### Contacto

Debe resolver:

- canales oficiales
- horario
- ubicacion si aplica
- formulario corto o CTA de WhatsApp

---

## 4. Arquitectura de componentes

Piezas base recomendadas:

- `SiteHeader`
- `SiteFooter`
- `HeroSection`
- `CategoryGrid`
- `ProductCard`
- `FeaturedCakeSection`
- `TestimonialsSection` solo si existe contenido real
- `ContactBlock`
- `QuoteWizard` o `QuoteForm`
- `FormStatusMessage`

Regla:

- cada componente debe tener una responsabilidad visual clara
- evitar componentes gigantes con demasiadas variantes ocultas

---

## 5. Layouts sugeridos

Layouts utiles:

- `PublicLayout`
- `CatalogLayout` si el catalogo necesita estructura propia
- `LegalLayout` si las paginas legales comparten patron

El layout debe centralizar:

- header
- footer
- metadata basica
- enlaces globales

---

## 6. Contenido y ownership

Separacion recomendada:

- contenido institucional estable en archivos locales o content collections
- datos catalogo desde backend
- mensajes de flujo desde componentes o capa de UI
- assets visuales previstos con placeholder estable y prompt documentado

Esto evita que:

- el equipo duplique textos en varios sitios
- el catalogo se vuelva hardcoded
- y los cambios de copy rompan la estructura tecnica

---

## 7. Convenciones de nombres

Ejemplos correctos:

- `ProductCard.astro`
- `QuoteWizard.astro`
- `public-api.ts`
- `catalogo.astro`
- `contacto.astro`

Evitar nombres vagos como:

- `Section1`
- `Utils2`
- `NewComponent`

---

## 8. SEO y contenido minimo

Cada ruta publica debe definir como minimo:

- `title`
- `description`
- encabezado principal claro
- estructura semantica razonable

En `home`, `catalogo` y `cotizador` esto es obligatorio.

---

## 9. Cierre

Si se respeta esta estructura, el frontend publico queda lo bastante profesional para crecer sin convertirse en una coleccion desordenada de paginas sueltas.

La politica visual complementaria vive en:

- `docs/ux-ui/01_assets_placeholders_y_prompts_ia.md`
