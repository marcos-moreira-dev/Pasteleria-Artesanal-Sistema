# 00 - Frontend admin canonico

## 1. Proposito

Este documento congela el criterio de ingenieria del frontend administrativo de
Pasteleria.

No es un escaparate visual. Es una consola de operacion.

---

## 2. Stack y versiones congeladas

La linea base documental del admin queda asi:

- `Node.js 22.12.0+`
- `Angular 21.x`
- `Angular CLI 21.x`
- `TypeScript 5.9.x`
- `RxJS 7.4+`

Reglas de baseline:

- usar APIs modernas de Angular compatibles con la version 21
- preferir componentes standalone
- no congelar una libreria externa de UI como obligatoria

---

## 3. Rol del producto

Este frontend existe para:

- operar clientes
- operar catalogo
- registrar y seguir cotizaciones
- registrar y seguir pedidos
- consultar y actualizar produccion
- solicitar y descargar reportes
- atender notificaciones internas
- operar abastecimiento

No existe para:

- marketing
- experiencia de cliente final
- resolver reglas criticas por fuera del backend

---

## 4. Superficies visibles del producto actual

La aplicacion administrativa expone hoy estas ventanas principales:

- login
- dashboard general
- clientes
- productos
- cotizaciones
- pedidos
- produccion
- reportes
- abastecimiento / dashboard
- abastecimiento / inventario
- abastecimiento / compras
- abastecimiento / proveedores
- abastecimiento / movimientos

Lectura correcta:

- categorias se administran dentro de productos, no como ruta separada
- produccion vive como modulo interno del admin
- abastecimiento ya es parte real del demo, no una idea futura

---

## 5. Alcance funcional de V1

La V1 debe cubrir:

- login administrativo
- shell con navegacion estable
- dashboard sobrio
- tablas y formularios operativos
- filtros paginados en modulos pesados
- feedback claro de carga, error y exito
- polling corto donde el backend trabaja async

---

## 6. Estilo arquitectonico recomendado

La opcion canonica es:

- Angular SPA para operacion interna
- estado local y de pantalla controlado
- consumo uniforme del backend central
- facades y modelos por feature

Regla clave:

- el frontend administra experiencia y coordinacion
- el backend sigue siendo la autoridad del negocio

---

## 7. Integracion con backend

Este admin consume principalmente:

- `POST /api/v1/auth/login`
- `GET|POST|PUT /api/v1/clientes`
- `GET|POST|PUT /api/v1/productos`
- `GET|POST /api/v1/cotizaciones`
- `GET|POST|PATCH /api/v1/pedidos`
- `GET|PATCH /api/v1/produccion`
- `POST|GET /api/v1/reportes`
- `GET|PATCH /api/v1/notificaciones`
- `GET|POST|PUT|PATCH /api/v1/abastecimiento/**`

Regla de contrato:

- todo se consume como `ApiResponse<T>`
- el contrato actual usa `success`, `message`, `data`, `errorCode`, `requestId` y `timestamp`
- el admin no interpreta entidades JPA
- los errores visibles deben traducirse a lenguaje operativo

---

## 8. Estructura tecnica recomendada

La estructura base razonable es:

```text
frontend-admin-angular/
  src/
    app/
      core/
      shared/
      features/
      layout/
    assets/
    styles/
  angular.json
  package.json
  tsconfig.json
```

Convencion util:

- `core/` para auth, cliente API, interceptores y guards
- `core/contracts/` para contratos transversales
- `core/api/` para cliente HTTP y config
- `core/store/` para estado coordinado del backoffice
- `shared/` para piezas reutilizables
- `features/` para modulos por dominio
- `layout/` para shell y navegacion

---

## 9. Temas computacionales que debes dominar aqui

Si quieres estudiar este producto con criterio profesional, los temas mas
importantes son:

- routing y proteccion de rutas
- componentes standalone
- formularios reactivos
- manejo de estado local con signals y RxJS
- integracion HTTP con contratos tipados
- tablas, filtros y paginacion
- feedback de UX operativa
- autenticacion, expiracion de sesion y guards
- separacion por features y facades

---

## 10. Calidad minima exigida

Antes de considerar cerrado este componente deben existir al menos:

- build del workspace
- smoke de login
- smoke de navegacion principal
- smoke de formularios y tablas criticas

---

## 11. Cierre

El admin de Pasteleria debe sentirse como una herramienta de trabajo real:

- sobria
- directa
- entendible
- y bien amarrada al backend
