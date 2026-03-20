# 00 - Frontend admin canonico

## 1. Proposito

Este documento congela el criterio de ingenieria del frontend administrativo de Pasteleria.

No es un escaparate visual. Es una consola de operacion para el negocio.

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
- no congelar una libreria de componentes externa como obligatoria en esta fase

Si luego se adopta `Angular Material` u otra libreria, debe registrarse como decision aparte.

---

## 3. Rol del producto

Este frontend existe para:

- operar clientes
- operar catalogo y categorias
- registrar y seguir cotizaciones
- registrar y seguir pedidos
- consultar panel de produccion
- ejecutar reportes internos
- atender notificaciones internas del backoffice

No existe para:

- marketing
- experiencia de cliente final
- resolver reglas criticas por fuera del backend

---

## 4. Alcance de V1

La V1 debe cubrir:

- login administrativo
- shell con navegacion por modulos
- dashboard sobrio
- clientes
- productos y categorias
- cotizaciones
- pedidos
- panel de produccion
- reportes basicos
- descarga autenticada de reportes generados
- filtros operativos incrementales para clientes cuando el volumen crece
- calculo automatico de precios cuando se elige un producto real del catalogo
- polling corto en reportes y mensajes que desaparecen sin ensuciar el tablero

---

## 5. Estilo arquitectonico recomendado

La opcion canonica es:

- Angular SPA para operacion interna
- rutas lazy por modulo cuando tenga sentido
- estado local y de pantalla controlado
- consumo uniforme del backend central

Regla clave:

- el frontend administra experiencia y coordinacion
- el backend sigue siendo la autoridad del negocio

Decision actual de V1:

- la busqueda de clientes se desacopla en una utilidad propia para no repetir filtros de texto en cada pantalla
- clientes, cotizaciones, pedidos y reportes ya consumen paginacion y filtros de forma mas limpia
- el formulario no debe resetearse antes de recibir confirmacion real del backend

---

## 6. Principios de implementacion

1. Shell estable y repetible.
2. Formularios reactivos y predecibles.
3. Tablas y filtros operativos antes que decoracion.
4. Mensajes de carga, error y exito claros.
5. Nada de copiar reglas complejas del backend por comodidad.

---

## 7. Integracion con backend

Este admin consume principalmente:

- `POST /api/v1/auth/login`
- `GET|POST|PUT /api/v1/admin/clientes`
- `GET|POST|PUT|PATCH /api/v1/admin/productos`
- `GET|PATCH|POST /api/v1/admin/cotizaciones`
- `GET|POST|PATCH /api/v1/admin/pedidos`
- `GET|PATCH /api/v1/admin/produccion`
- `POST|GET /api/v1/admin/reportes`

Regla de contrato:

- todo se consume como `ApiResponse<T>`
- el admin no interpreta entidades JPA
- los errores visibles deben traducirse a lenguaje de operacion

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

- `core/` para auth, cliente API, interceptores, guards y servicios transversales
- `core/contracts/` para contratos API verdaderamente transversales como `ApiResponse`, `PageResponseDto` y `AuthResponse`
- `core/api/` para cliente HTTP y config transversal
- `core/store/` para estado compartido del backoffice y operaciones coordinadas entre modulos
- `shared/` para componentes, estilos y utilidades reutilizables
- `features/` para modulos por dominio
- `layout/` para shell y piezas de navegacion

Dentro de `shared/` conviene mantener un pequeno kit visual o de estilos transversales para que clientes, cotizaciones, pedidos, productos y produccion no terminen con pantallas aisladas entre si.

La implementacion actual ya quedo mas cerca del criterio profesional del proyecto de referencia:

- cada pagina consume un facade por feature en `features/*/state`
- el shell consume un facade propio
- y `core` ya no concentra una sola fachada gigante mezclada con HTTP
- los modelos de TypeScript ya no viven en un solo archivo bolsa
- cada dominio mantiene sus contratos en `features/*/models`

---

## 9. Calidad minima exigida

Antes de considerar cerrado este componente deben existir al menos:

- build del workspace
- smoke de login
- smoke de navegacion principal
- smoke de formularios y tablas criticas

Si existe target de pruebas del workspace, tambien debe quedar verde.

---

## 10. Referencia inteligente

Si hace falta estudiar patrones adicionales de operacion, desacople de pantallas o UX administrativa, se puede revisar como referencia inteligente:

- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Proyecto tienda Electronica promedio\docs\04_ADMIN_APP_OPERACION_Y_UX.md`
- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Java\Sistema UE Ninitos Sonadores\desktop\docs\18_desktop_arquitectura_paquetes_y_capas_mvvm.md`

---

## 11. Cierre

El admin de Pasteleria debe sentirse como una herramienta de trabajo real:

- sobria
- directa
- entendible
- y bien amarrada al backend

---

## 12. Regla adicional de imagenes y branding

La consola administrativa no debe duplicar assets del catalogo.

Reglas:

- si el backend expone imagen de producto, el admin la consume
- si no existe imagen real, el admin usa placeholder oficial del backend
- el logo del acceso administrativo puede consumirse desde branding central del backend

Esto permite que el equipo agregue o reemplace una imagen en backend y la vea en panel y landing sin tocar Angular.
