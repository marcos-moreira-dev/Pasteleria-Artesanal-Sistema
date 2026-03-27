# Frontend publico Astro - contexto general de implementacion

## 1. Proposito

Este documento baja el frontend publico a un contexto de implementacion
suficientemente concreto para que no haya que improvisar.

---

## 2. Decision principal

La decision canonica es usar `Astro 5.x` como superficie publica principal con
`Node.js 22.12.0+`.

Motivos:

- encaja con un sitio de marca y catalogo
- permite paginas ligeras y rapidas
- soporta interaccion puntual donde hace falta
- no obliga a convertir toda la experiencia en SPA

---

## 3. Relacion con el sistema completo

Dentro del sistema de Pasteleria este frontend:

- no tiene logica de negocio soberana
- no administra usuarios internos
- no persiste por su cuenta informacion critica

Su relacion correcta es:

- `frontend publico -> backend central -> base de datos`

---

## 4. Responsabilidades reales

Este componente debe poder:

- presentar marca y propuesta de valor
- exponer catalogo y categorias visibles
- orientar al cliente a productos destacados
- capturar una solicitud publica de cotizacion desde contacto
- mostrar informacion de contacto y confianza

No debe asumir responsabilidades del admin ni del backend.

---

## 5. Modelo de renderizado recomendado

La distribucion pragmatica es esta:

- home y catalogo: renderizado ligero con datos del backend
- contacto: formulario simple con validacion minima y envio real
- ingles: variacion ligera de contenido, no un sistema de i18n complejo

Regla:

- si una vista puede resolverse sin gran estado cliente, no se hidrata de mas

---

## 6. Cliente API recomendado

Debe existir una capa pequena y explicita para consumir backend.

Responsabilidades:

- encapsular `fetch`
- resolver URL base por entorno
- normalizar `ApiResponse<T>`
- traducir errores tecnicos a mensajes manejables por UI

---

## 7. Variables y entorno

Variables minimas sugeridas:

- `PUBLIC_API_BASE_URL`
- `SITE_URL`

Regla:

- ninguna URL de backend debe quedar quemada en componentes

---

## 8. Riesgos a evitar

1. Convertir el sitio en una SPA innecesaria.
2. Describir una ruta publica que el producto no tiene.
3. Mezclar copy comercial con mensajes tecnicos del backend.
4. Duplicar reglas del formulario publico en varios componentes.

---

## 9. Pruebas y verificacion superficial

Controles minimos:

- `npx astro check`
- `npm run build`
- smoke manual de `home`, `catalogo` y `contacto`
- verificacion del formulario publico frente al backend real
