# 01 - Checklist QA por componente

## 1. Proposito

Este checklist sirve como gate simple y profesional antes de considerar que un componente esta suficientemente sano para integrarse.

---

## 2. DB

- migraciones aplican desde cero sin error
- seeds minimos cargan correctamente
- relaciones criticas respetan integridad
- consultas de pedidos pendientes responden como se espera
- conversion de cotizacion a pedido no rompe consistencia

---

## 3. Backend

- login responde con contrato estable
- `ApiResponse<T>` se mantiene uniforme
- alta y actualizacion de cliente funcionan
- alta y consulta de pedido funcionan
- conversion de cotizacion a pedido respeta `RN-14` y `RN-15`
- entrega de pedido respeta `RN-09`
- errores `400`, `401`, `403`, `404`, `409` y `422` se traducen correctamente

---

## 4. Frontend publico

- home y catalogo cargan sin errores visibles
- el catalogo no promete ecommerce completo
- el cotizador guia al usuario y conserva resumen
- errores del cotizador suenan humanos
- el build del frontend publico pasa
- accesibilidad basica de formularios y foco visible queda correcta

---

## 5. Frontend administrativo

- login y expiracion de sesion funcionan
- tablas principales cargan y filtran
- formularios muestran validacion clara
- cambios de estado de pedido y produccion no dejan UI incoherente
- modulo de reportes comunica pendiente, listo y error
- el build del admin pasa

---

## 6. Produccion y reportes

- panel de produccion muestra prioridad y estado
- refresco o stream no duplica ni oculta pedidos
- solicitud de reporte queda registrada
- descarga de reporte solo aparece cuando corresponde

---

## 7. Resultado esperado

Si este checklist se cumple, el componente ya no depende solo de intuicion para pasar a la siguiente fase.
