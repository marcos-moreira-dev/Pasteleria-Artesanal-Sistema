# Backend - Contexto general de implementacion

## 1. Proposito

Este documento sirve como puerta de entrada a la documentacion del backend.

Su trabajo es fijar el contexto real del proyecto para que nadie implemente una
API distinta a la que el sistema necesita hoy.

---

## 2. Naturaleza del backend

Este backend pertenece a un sistema que ya tiene varias superficies reales:

1. frontend publico en Astro
2. formulario publico de cotizacion dentro de contacto
3. frontend administrativo en Angular
4. panel de produccion dentro del admin
5. vertical de abastecimiento dentro del admin

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
- jobs y procesos async locales cuando corresponda

---

## 4. Lo que el backend debe resolver hoy

- login y seguridad
- catalogo publico y branding
- activos publicos de producto
- clientes
- productos
- cotizaciones publicas e internas
- conversion de cotizacion a pedido
- pedidos y cambios de estado
- produccion
- reportes async
- notificaciones internas
- abastecimiento: inventario, proveedores, recetas, compras y movimientos

---

## 5. Lo que el backend no debe prometer en esta fase

- ecommerce completo
- pagos en linea
- carrito complejo
- microservicios
- broker distribuido
- integraciones externas duras

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

## 7. Regla de implementacion

Cuando haya duda, se aplica esta prioridad:

1. manda el dominio y el alcance de Pasteleria
2. manda la DB como fuente de verdad relacional
3. manda el backend como fuente de verdad tecnica
4. los frontends consumen contratos; no redefinen negocio

---

## 8. Resultado esperado

El backend no debe sentirse como una suma de CRUDs.

Debe sentirse como la autoridad central de:

- reglas
- estados
- trazabilidad
- contratos
- y coherencia operativa
