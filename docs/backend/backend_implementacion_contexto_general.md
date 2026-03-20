# Backend - Contexto general de implementacion

## 1. Proposito

Este documento sirve como guia maestra para leer e implementar el bloque `docs/backend` de Pasteleria.

Su objetivo es que otra IA o una persona pueda tomar esta carpeta y convertirla en backend real con poca ambiguedad.

---

## 2. Naturaleza del backend

Este backend pertenece a un sistema para una pasteleria mediana con estas superficies:

1. frontend publico en Astro
2. cotizador publico
3. frontend administrativo en Angular
4. panel interno de produccion

Aunque existan varias superficies, tecnicamente hay:

- un solo backend
- una sola base de datos
- un solo nucleo de negocio

---

## 3. Stack y criterio fijo

La carpeta backend debe asumirse con esta base:

- Java 21 con Eclipse Temurin 21
- Spring Boot 4
- PostgreSQL
- Flyway
- Spring Security
- monolito modular
- `ApiResponse<T>` uniforme
- SSE ligero para panel operativo cuando aporte valor

---

## 4. Lo que el backend si debe resolver

- administrar clientes
- administrar productos y categorias
- registrar cotizaciones
- convertir cotizaciones en pedidos
- registrar pedidos
- controlar estados de pedido
- alimentar panel de produccion
- exponer catalogo publico
- soportar reportes async

---

## 5. Lo que el backend no debe asumir de inicio

- ecommerce completo
- pagos en linea
- carrito complejo
- microservicios
- integraciones externas duras
- broker distribuido
- delivery complejo

---

## 6. Ruta documental del backend

Orden recomendado:

1. `backend_implementacion_contexto_general.md`
2. `backend_00_vision_backend_y_modulos.md`
3. `backend_01_modelo_de_dominios_y_agregados.md`
4. `backend_02_diseno_de_api_rest.md`
5. `backend_03_dtos_y_contratos_api.md`
6. `backend_04_seguridad_testing_y_operacion.md`

---

## 7. Funcion de cada documento

### `backend_00_vision_backend_y_modulos.md`

Define arquitectura, modulos, capas y reglas de acoplamiento.

### `backend_01_modelo_de_dominios_y_agregados.md`

Define agregados, casos de uso, invariantes y puntos transaccionales.

### `backend_02_diseno_de_api_rest.md`

Define versionado, rutas, `ApiResponse`, paginacion y errores HTTP.

### `backend_03_dtos_y_contratos_api.md`

Define DTOs, filtros, mapeo y reglas de exposicion de datos.

### `backend_04_seguridad_testing_y_operacion.md`

Define seguridad, manejo de errores, auditoria, reportes async, testing y operacion.

---

## 8. Regla de implementacion

Cuando haya una duda, se aplica esta prioridad:

1. manda el dominio y el alcance de Pasteleria
2. manda la DB como fuente de verdad relacional
3. manda el backend como fuente de verdad de reglas tecnicas
4. el frontend consume contratos; no redefine negocio

---

## 9. Referencia inteligente permitida

Si hace falta reforzar el backend con ideas de ingenieria, documentacion o fragmentos puntuales, se puede consultar:

- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Java\Sistema UE Niñitos Soñadores`
- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Proyecto tienda Electronica promedio`

La referencia sirve, pero no reemplaza el dominio de Pasteleria.

