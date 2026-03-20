# Frontend publico Astro - contexto general de implementacion

## 1. Proposito

Este documento baja el frontend publico a un contexto de implementacion suficientemente concreto para que otra IA o una implementacion manual no tenga que improvisar.

---

## 2. Decision principal

La decision canonica es usar `Astro 5.x` como superficie publica principal con `Node.js 22.12.0+`.

Motivos:

- encaja bien con un sitio de marca y contenido
- permite paginas ligeras y rapidas
- soporta islas interactivas puntuales para el cotizador
- no obliga a convertir toda la experiencia publica en SPA

---

## 3. Relacion con el sistema completo

Dentro del sistema de Pasteleria este frontend:

- no tiene logica de negocio soberana
- no administra usuarios internos
- no persiste por su cuenta informacion operativa critica

Su relacion correcta es:

- `frontend publico -> backend central -> base de datos`

---

## 4. Responsabilidades reales

Este componente debe poder:

- presentar marca y propuesta de valor
- exponer catalogo y categorias visibles
- orientar al cliente a productos destacados
- ejecutar el flujo de cotizacion
- mostrar informacion de contacto y confianza

No debe asumir responsabilidades que le pertenecen al admin o al backend.

---

## 5. Modelo de renderizado recomendado

La distribucion pragmatica es esta:

- paginas de presentacion: renderizado liviano y altamente cacheable
- catalogo y detalle: renderizado ligero con datos del backend
- cotizador: interaccion guiada con una isla o flujo controlado
- contacto: formulario simple con validacion minima

Regla:

- si una vista puede resolverse sin gran estado cliente, no se hidrata de mas

---

## 6. Cliente API recomendado

Debe existir una capa pequena y explicita para consumir backend.

Responsabilidades de esa capa:

- encapsular `fetch`
- resolver URL base por entorno
- normalizar `ApiResponse<T>`
- traducir errores tecnicos a errores manejables por UI

No debe:

- contener reglas de negocio complejas
- duplicar validaciones profundas del backend

---

## 7. Variables y entorno

Variables minimas sugeridas:

- `PUBLIC_API_BASE_URL`
- `SITE_URL`
- `PUBLIC_CONTACT_PHONE` si se decide parametrizar
- `PUBLIC_WHATSAPP_URL` si aplica

Regla:

- ninguna URL de backend debe quedar quemada en componentes

---

## 8. Sistema visual y contenido

El frontend publico debe trabajar con:

- tokens visuales globales
- layouts consistentes
- componentes reutilizables
- copy comercial sobrio

Conviene separar:

- contenido institucional estable
- catalogo dinamico
- mensajes transaccionales del cotizador

---

## 9. Riesgos a evitar

1. Convertir el sitio en una SPA innecesaria.
2. Duplicar reglas del cotizador en varios componentes.
3. Mezclar copy comercial con mensajes tecnicos del backend.
4. Atar el contenido a componentes rigidos y poco reutilizables.
5. Construir el cotizador como formulario gigante sin pasos ni resumen.

---

## 10. Pruebas y verificacion superficial

Para esta fase documental basta dejar definidos los controles minimos:

- `npx astro check`
- `npm run build`
- smoke manual de `home`, `catalogo`, `cotizador` y `contacto`
- verificacion de errores del cotizador frente a respuestas reales del backend

---

## 11. Regla de apoyo

Si la documentacion o implementacion de este componente necesita inspiracion adicional, se puede revisar:

- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Proyecto tienda Electronica promedio\docs\05_STOREFRONT_Y_CHECKOUT.md`
- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Proyecto tienda Electronica promedio\docs\10_OPERACION_LOCAL_Y_RUNBOOKS.md`

La idea es aprender nivel de cierre, no copiar dominio.
