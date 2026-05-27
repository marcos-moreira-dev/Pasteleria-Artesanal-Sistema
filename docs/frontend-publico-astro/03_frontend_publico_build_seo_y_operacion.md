# 03 - Frontend publico: build, SEO y operacion

## 1. Proposito

Este documento fija la operacion minima y profesional del frontend publico.

---

## 2. Versiones de trabajo

La linea base operativa es:

- `Node.js 22.12.0+`
- `Astro 5.x`
- `TypeScript 5.x`

---

## 3. Scripts minimos esperados

El proyecto debe exponer al menos:

- `npm run dev`
- `npm run build`
- `npm run preview`
- `npx astro check`

Si luego se agrega lint, debe quedar declarado de forma explicita.

---

## 4. Variables minimas

Variables operativas sugeridas:

- `PUBLIC_API_BASE_URL`
- `SITE_URL`

Si algun canal de contacto se parametriza:

- `PUBLIC_CONTACT_PHONE`
- `PUBLIC_WHATSAPP_URL`
- `PUBLIC_DEFAULT_LOCALE`

---

## 5. SEO minimo exigible

La superficie publica debe cuidar como minimo:

- metadata por pagina
- canonical URL
- `hreflang` cuando exista equivalencia `es/en`
- encabezados semanticos
- imagenes con `alt`
- sitemap y robots coherentes si se habilitan

No conviene dejar SEO como "se vera despues", porque es parte del valor del producto publico.

---

## 6. Accesibilidad base

Controles minimos:

- contraste correcto
- foco visible
- labels en formularios
- navegacion usable con teclado
- mensajes de error entendibles

---

## 7. Despliegue recomendado

La opcion mas sobria es desplegar este frontend como servicio separado del backend, pero coordinado con el mismo dominio o subdominio operativo.

Reglas:

- no mezclar configuracion de backend en el bundle
- no depender de variables manuales cambiadas a mano en cada deploy
- mantener la URL base por entorno
- servir logos, iconos y tipografias desde assets locales del proyecto

---

## 8. Checklist minimo de salida

Antes de marcar listo este componente:

1. `npm install` limpio con la version de Node correcta.
2. `npx astro check` en verde.
3. `npm run build` en verde.
4. smoke de rutas principales.
5. smoke del cotizador conectado al backend esperado.
6. smoke de `es` y `en`.
7. verificacion de assets locales de branding, iconos y fuentes.

---

## 9. Runbooks minimos utiles

Incidencias razonables a documentar cuando exista codigo:

- frontend publico no conecta al backend
- metadata publica incorrecta
- cotizador no envia solicitudes
- build falla por variables de entorno faltantes

---

## 10. Referencia inteligente

Para ideas de cierre operativo se puede revisar:

- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Proyecto tienda Electronica promedio\docs\10_OPERACION_LOCAL_Y_RUNBOOKS.md`

---

## 11. Cierre

La calidad del frontend publico no depende solo de verse bien. Tambien depende de:

- construir bien
- desplegar bien
- y fallar de forma diagnosticable
