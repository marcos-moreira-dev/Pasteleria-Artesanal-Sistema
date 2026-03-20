# 00 - Frontend publico canonico

## 1. Proposito

Este documento congela el criterio de ingenieria del frontend publico de Pasteleria.

Su trabajo no es "hacer una landing bonita". Su trabajo real es:

- presentar la marca
- mostrar catalogo
- guiar al cliente hacia contacto y cotizacion
- y entregar una superficie publica consistente con el backend central

---

## 2. Stack y versiones congeladas

La linea base documental del frontend publico queda asi:

- `Node.js 22.12.0+`
- `Astro 5.x`
- `TypeScript 5.x`
- estilos con `CSS` y variables de diseno propias del proyecto

Reglas de alcance tecnico:

- no se congela `Tailwind`, `React`, `Vue` ni otra libreria de UI como parte del baseline
- si algun dia se adopta una libreria extra, debe registrarse como decision aparte
- la compatibilidad debe seguir siendo coherente con `Node.js 22.12.0+`

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

## 4. Alcance de V1

La V1 del frontend publico debe cubrir:

- home de marca
- presentacion del negocio
- catalogo navegable
- detalle simple de producto o categoria destacada
- cotizador de tortas
- contacto y datos del negocio
- paginas legales minimas si se requieren

No debe venderse como:

- ecommerce completo
- checkout transaccional
- portal de seguimiento de pedidos para cliente final

---

## 5. Estilo arquitectonico recomendado

La opcion canonica es:

- Astro como framework principal
- renderizado mayormente ligero
- hidratacion selectiva solo donde haga falta interaccion real
- consumo de `GET /api/v1/public/**` para catalogo y contenido dinamico
- consumo de `POST /api/v1/public/cotizaciones` para el cotizador

Regla importante:

- una isla interactiva solo existe si aporta valor claro
- no convertir todo el sitio en SPA por comodidad

---

## 6. Superficies incluidas

Este componente cubre:

- home
- catalogo
- detalle de producto destacado o ficha simple
- cotizador
- contacto
- secciones institucionales necesarias

No cubre:

- panel administrativo
- panel de produccion
- autenticacion interna

---

## 7. Principios de implementacion

1. Contenido claro antes que decoracion.
2. Jerarquia visual comercial sin exagerar.
3. Minima logica en cliente.
4. Formularios con validacion humana y mensajes entendibles.
5. SEO, accesibilidad y rendimiento tratados como requisitos reales.
6. `i18n` cerrada a `es` y `en`, sin idiomas extra en V1.
7. Logos, iconos y tipografias servidos desde assets locales del proyecto.

---

## 8. Integracion con backend

El frontend publico depende del backend central para:

- obtener productos publicables
- obtener categorias visibles
- registrar solicitudes de cotizacion
- registrar contacto si se habilita formulario

Regla de contrato:

- el frontend publico consume `ApiResponse<T>`
- no interpreta entidades internas del backend
- no conoce detalles de persistencia

---

## 9. Estructura tecnica recomendada

La estructura base razonable del proyecto es:

```text
frontend-publico-astro/
  src/
    components/
    layouts/
    pages/
    content/
    lib/
    styles/
  public/
    assets/
      branding/
      icons/
      fonts/
  astro.config.mjs
  package.json
  tsconfig.json
```

Convencion util:

- `components/` para piezas visuales reutilizables
- `layouts/` para plantillas de pagina
- `pages/` para rutas
- `lib/` para cliente API, utilidades y validaciones livianas
- `styles/` para tokens, resets y capas visuales globales
- `public/assets/branding/` para logos, favicons y recursos de marca
- `public/assets/icons/` para iconos propios o descargados
- `public/assets/fonts/` para tipografias locales listas para `@font-face`

---

## 10. Calidad minima exigida

Antes de considerar cerrado este componente deben existir al menos:

- `npm run build`
- `npx astro check`
- smoke manual de rutas principales
- smoke manual del cotizador
- revision basica de accesibilidad

---

## 11. Referencia inteligente

Si hace falta estudiar patrones adicionales de documentacion, shell comercial o operacion, se puede revisar como referencia inteligente:

- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Proyecto tienda Electronica promedio`
- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Java\Sistema UE Ninitos Sonadores`

La referencia ayuda, pero el dominio de Pasteleria sigue mandando.

---

## 12. Cierre

El frontend publico de Pasteleria debe sentirse como una superficie comercial seria:

- limpia
- clara
- rapida
- y conectada con el backend sin sobrecargarse de complejidad innecesaria

---

## 13. Regla adicional de assets servidos por backend

La V1 actual ya consolida una regla mas fuerte:

- el backend sirve branding oficial y imagenes de producto
- Astro consume esas rutas como contrato publico
- el frontend no decide nombres de archivo de producto

Implementacion esperada:

- `GET /api/v1/public/catalogo/branding` entrega logo y banner
- `GET /api/v1/public/catalogo/productos` ya entrega `imagePath` e `imageAlt`
- para agregar una imagen nueva basta con copiar el archivo al backend usando el `slug` del producto como nombre base

Los assets locales del frontend publico quedan reservados para:

- iconos propios
- tipografias locales
- decoracion no critica del sitio
