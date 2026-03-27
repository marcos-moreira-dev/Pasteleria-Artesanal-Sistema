# 02 - Frontend admin: servicios, estado y auth

## 1. Proposito

Este documento fija los servicios transversales del admin y el manejo correcto
de sesion, estado y errores.

---

## 2. Servicios transversales minimos

En `core/` deben existir al menos:

- `auth.service`
- `session.service`
- `api-client.service`
- `notification.service`
- `error-mapper.service`

Si se usan facades en flujos complejos, deben vivir cerca del modulo que
coordinan.

---

## 3. Autenticacion

La autenticacion administrativa debe seguir el backend central:

- login por `POST /api/v1/auth/login`
- almacenamiento controlado de token
- expiracion de sesion manejada con claridad
- redireccion al login si la sesion ya no es valida

Regla:

- no guardar datos sensibles innecesarios en cliente

---

## 4. Autorizacion

Controles recomendados en cliente:

- guard de autenticacion
- filtro de navegacion por rol
- control de visibilidad de acciones

La autorizacion real la sigue definiendo el backend.

---

## 5. Manejo de `ApiResponse<T>`

Todo servicio HTTP debe esperar:

- `success`
- `message`
- `data`
- `errorCode`
- `requestId`
- `timestamp`

El admin no debe manejar respuestas heterogeneas por modulo.

---

## 6. Estado de pantalla

Cada pantalla operativa debe modelar con claridad:

- `idle`
- `loading`
- `success`
- `empty`
- `error`

Las tablas, formularios y paneles no deben inventar estados ambiguos.

---

## 7. Estrategia de estado recomendada

Conviene usar:

- `signals` para filtros, seleccion, flags de carga y estado local
- `RxJS` para secuencias HTTP, refresh y composicion asincrona

No conviene introducir estado global pesado sin una necesidad objetiva.

---

## 8. Manejo de errores

Casos comunes que deben resolverse bien:

- `401` sesion vencida
- `403` permiso insuficiente
- `404` recurso inexistente
- `409` conflicto de negocio
- `422` regla de negocio incumplida

Los mensajes deben sonar operativos, no tecnicos.

---

## 9. Notificaciones y feedback

El admin debe usar un patron uniforme para:

- exito
- advertencia
- error
- confirmacion de accion critica

Regla:

- no abusar de modales
- no depender solo del color para comunicar estado

---

## 10. Pruebas superficiales sugeridas

1. Login correcto.
2. Expiracion de sesion.
3. Rechazo por falta de permisos.
4. Error controlado cuando el backend no responde.
5. Consumo correcto de `ApiResponse<T>` en una tabla y en un formulario.
