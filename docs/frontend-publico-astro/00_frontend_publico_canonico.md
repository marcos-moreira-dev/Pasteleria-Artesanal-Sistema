# 00 - Frontend publico canonico

## 1. Proposito

Este documento congela el criterio de ingenieria del frontend publico de
Pasteleria.

Su trabajo no es "hacer una landing bonita". Su trabajo real es:

- presentar la marca
- mostrar catalogo publicado
- guiar al cliente hacia contacto
- registrar una solicitud publica de cotizacion

---

## 2. Stack y versiones congeladas

La linea base documental del frontend publico queda asi:

- `Node.js 22.12.0+`
- `Astro 5.x`
- `TypeScript 5.x`
- estilos con `CSS` y variables propias del proyecto

---

## 3. Rol del producto

Este frontend existe para tres cosas:

1. captar interes real
2. explicar la oferta del negocio sin parecer ecommerce completo
3. convertir esa visita en una accion concreta: contacto o solicitud de cotizacion

No existe para:

- procesar pagos online
- reemplazar el backend
- duplicar logica compleja de negocio en cliente

---

## 4. Superficies visibles del producto actual

La aplicacion publica expone hoy estas rutas:

- `/`
- `/catalogo`
- `/contacto`
- `/en`
- `/en/catalog`
- `/en/contact`

Lectura correcta:

- la solicitud publica de cotizacion vive hoy en `contacto`
- no existe aun una pagina dedicada `/cotizador`
- no existe aun una ficha publica de producto por `slug`

---

## 5. Estilo arquitectonico recomendado

La opcion canonica es:

- Astro como framework principal
- renderizado ligero
- hidratacion minima
- consumo de `GET /api/v1/public/**` para datos publicos
- consumo de `POST /api/v1/public/cotizaciones` para la solicitud publica

Regla:

- una isla interactiva solo existe si aporta valor claro
- no convertir todo el sitio en SPA por comodidad

---

## 6. Integracion con backend

El frontend publico depende del backend central para:

- obtener productos publicables
- obtener categorias visibles
- obtener branding oficial
- registrar solicitudes de cotizacion

Regla de contrato:

- el frontend publico consume `ApiResponse<T>`
- el contrato actual usa `success`, `message`, `data`, `errorCode`, `requestId` y `timestamp`
- no interpreta entidades internas del backend

---

## 7. Estructura tecnica recomendada

La estructura base razonable del proyecto es:

```text
frontend-publico-astro/
  src/
    components/
    layouts/
    pages/
    lib/
    styles/
  public/
    assets/
  astro.config.mjs
  package.json
  tsconfig.json
```

Convencion util:

- `components/` para piezas visuales reutilizables
- `layouts/` para plantillas base
- `pages/` para rutas
- `lib/` para cliente API y utilidades
- `styles/` para tokens y capas globales

---

## 8. Temas computacionales que debes dominar aqui

Si quieres estudiar este producto con criterio profesional, los temas mas
importantes son:

- renderizado y composicion en Astro
- integracion de datos con `fetch`
- rutas multilenguaje simples
- arquitectura por layouts y componentes
- formularios publicos y manejo de estados
- accesibilidad, SEO y rendimiento
- consumo de assets servidos por backend

---

## 9. Calidad minima exigida

Antes de considerar cerrado este componente deben existir al menos:

- `npm run build`
- `npx astro check`
- smoke manual de home, catalogo y contacto
- smoke manual del formulario de cotizacion
- revision basica de accesibilidad

---

## 10. Cierre

El frontend publico de Pasteleria debe sentirse como una superficie comercial
seria:

- limpia
- clara
- rapida
- y conectada con el backend sin complejidad innecesaria
