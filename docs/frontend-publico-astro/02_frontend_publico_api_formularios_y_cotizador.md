# 02 - Frontend publico: API, formularios y cotizacion publica

## 1. Proposito

Este documento fija la integracion del frontend publico con el backend y el
comportamiento esperado del formulario publico de cotizacion.

---

## 2. Endpoints publicos relevantes

El frontend publico trabaja principalmente con:

- `GET /api/v1/public/catalogo/categorias`
- `GET /api/v1/public/catalogo/productos`
- `GET /api/v1/public/catalogo/branding`
- `POST /api/v1/public/cotizaciones`

---

## 3. Regla de consumo de API

Toda llamada debe pasar por una capa pequena de cliente HTTP.

Esa capa debe:

- enviar headers basicos correctos
- interpretar `ApiResponse<T>`
- capturar errores de red
- devolver estados utiles a la interfaz

No debe:

- inventar estados que el backend no conoce
- duplicar validaciones profundas de negocio

---

## 4. Solicitud publica de cotizacion

En la V1 actual no existe una pagina dedicada `/cotizador`.

La capacidad publica vive como formulario dentro de `contacto`.

Campos base del flujo actual:

- nombre completo
- telefono
- email
- tipo de celebracion
- producto solicitado
- porciones estimadas
- presupuesto estimado
- notas

Regla UX:

- la confirmacion final debe dejar claro que se genero una solicitud, no una compra pagada

---

## 5. Validacion de formularios

La validacion del frontend solo debe cubrir:

- campos requeridos
- formato de correo o telefono
- longitudes razonables
- coherencia basica de numeros

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

## 7. Pruebas superficiales sugeridas

1. Enviar una solicitud valida.
2. Intentar enviar una solicitud incompleta.
3. Simular backend caido.
4. Verificar que el mensaje final no prometa compra ni pago.
