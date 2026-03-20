# 02 - Frontend publico: API, formularios y cotizador

## 1. Proposito

Este documento fija la integracion del frontend publico con el backend y el comportamiento esperado de formularios y cotizador.

---

## 2. Endpoints publicos relevantes

El frontend publico debe trabajar principalmente con:

- `GET /api/v1/public/productos`
- `GET /api/v1/public/productos/{id}` o equivalente por `slug`
- `POST /api/v1/public/cotizaciones`

Si se habilita contacto estructurado, puede existir:

- `POST /api/v1/public/contacto`

---

## 3. Regla de consumo de API

Toda llamada debe pasar por una capa pequena de cliente HTTP.

Esa capa debe:

- enviar headers basicos correctos
- interpretar `ApiResponse<T>`
- capturar errores de red
- devolver estados utiles a la interfaz

No debe:

- filtrar reglas de negocio por su cuenta
- inventar estados que el backend no conoce

---

## 4. Cotizador como flujo guiado

La forma recomendada es un flujo por pasos o bloques con resumen persistente.

Campos razonables:

- tipo de torta o categoria
- tamano
- sabor
- relleno
- decoracion
- fecha estimada
- observaciones
- datos de contacto del cliente

Reglas UX:

- cada paso debe tener objetivo claro
- el usuario debe ver progreso
- el resumen no debe desaparecer
- la confirmacion final debe dejar claro que se genero una solicitud, no una compra pagada

---

## 5. Validacion de formularios

La validacion del frontend solo debe cubrir:

- campos requeridos
- formato de correo o telefono
- longitudes razonables
- coherencia basica de fechas

La validacion fuerte sigue siendo del backend.

---

## 6. Estados de interfaz obligatorios

Todo formulario publico debe contemplar:

- estado inicial
- estado enviando
- estado exito
- estado error de validacion
- estado error de red o backend

Los mensajes deben sonar humanos, no tecnicos.

---

## 7. Manejo de errores recomendado

Casos esperables:

- backend no disponible
- request invalido
- cotizacion rechazada por regla de negocio

Regla:

- el frontend muestra mensaje claro
- el detalle tecnico queda para logs o soporte

Ejemplos de tono correcto:

- "No pudimos enviar tu solicitud. Intenta nuevamente."
- "Faltan datos obligatorios para continuar."

---

## 8. Seguridad basica del componente

Reglas minimas:

- no exponer secretos
- no dejar URLs privadas en cliente
- no confiar en validaciones de navegador como unico control
- evitar logs con datos sensibles del formulario en consola de produccion

---

## 9. Pruebas superficiales sugeridas

1. Enviar cotizacion valida.
2. Intentar enviar cotizacion incompleta.
3. Simular backend caido.
4. Verificar que el mensaje final no prometa compra ni pago.

---

## 10. Cierre

El cotizador es la capacidad publica mas importante del proyecto. Si su contrato, mensajes y estados quedan claros desde ahora, la implementacion con IA va a desviarse mucho menos.
