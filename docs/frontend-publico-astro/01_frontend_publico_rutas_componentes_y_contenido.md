# 01 - Frontend publico: rutas, componentes y contenido

## 1. Proposito

Este documento fija la estructura navegable real del frontend publico y la forma
correcta de repartir componentes y contenido.

---

## 2. Mapa de rutas de la V1 actual

Rutas visibles hoy:

- `/`
- `/catalogo`
- `/contacto`
- `/en`
- `/en/catalog`
- `/en/contact`

Regla:

- el mapa final debe ser pequeno, claro y facil de mantener

Nota:

- una ficha publica por `slug` puede existir despues, pero no es parte del producto actual
- la cotizacion publica vive dentro de `contacto`

---

## 3. Objetivo por ruta

### Home

Debe resolver:

- propuesta de valor
- categorias destacadas
- productos destacados
- CTA hacia catalogo y contacto

### Catalogo

Debe resolver:

- exploracion simple
- lectura clara de la carta publica
- evidencia de productos publicados desde el backend

### Contacto

Debe resolver:

- canales oficiales
- confianza comercial
- formulario de solicitud de cotizacion

### Variantes en ingles

Deben resolver:

- consistencia internacional de la marca
- misma navegacion base
- misma idea comercial, sin duplicar logica

---

## 4. Arquitectura de componentes

Piezas base recomendadas:

- `BaseLayout`
- `HeroSection`
- `CategoryStrip`
- `ProductGrid`
- `MarketingBand`
- `QuoteForm` o formulario equivalente en contacto

Regla:

- cada componente debe tener una responsabilidad visual clara
- evitar componentes gigantes con demasiadas variantes ocultas

---

## 5. Layouts y ownership

El layout debe centralizar:

- header
- footer
- metadata base
- enlaces globales

Separacion recomendada:

- copy institucional y comercial en la capa publica
- catalogo y branding desde backend
- mensajes de flujo desde la UI

---

## 6. SEO y contenido minimo

Cada ruta publica debe definir como minimo:

- `title`
- `description`
- encabezado principal claro
- estructura semantica razonable

En `home`, `catalogo` y `contacto` esto es obligatorio.
