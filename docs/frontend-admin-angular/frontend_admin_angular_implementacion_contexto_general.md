# Frontend admin Angular - contexto general de implementacion

## 1. Proposito

Este documento baja el admin de Pasteleria a un contexto de implementacion concreto y util para construccion asistida por IA.

---

## 2. Decision principal

La decision canonica es usar `Angular 21.x` con `Node.js 22.12.0+`, `TypeScript 5.9.x` y `RxJS 7.4+`.

Motivos:

- Angular encaja bien con una SPA administrativa
- su sistema de rutas, formularios y cliente HTTP favorece operacion repetible
- permite separar `core`, `shared`, `layout` y `features` con claridad

---

## 3. Relacion con el sistema completo

Este frontend:

- depende del backend central para toda operacion real
- no es autoridad de reglas de negocio
- no sustituye auditoria, seguridad ni trazabilidad del backend

Relacion correcta:

- `frontend admin -> backend central -> base de datos`

---

## 4. Responsabilidades reales

Debe poder:

- iniciar sesion
- navegar modulos internos
- listar, filtrar y editar datos operativos
- ejecutar cambios de estado
- visualizar produccion y reportes

No debe:

- definir transiciones de estado por su cuenta
- duplicar permisos criticos en cliente
- cargar al navegador con logica de dominio pesada

---

## 5. Modelo de arquitectura recomendado

La distribucion pragmatica es esta:

- `layout` para shell y navegacion
- `core` para auth, API, guards, interceptores y configuracion
- `shared` para componentes reutilizables
- `features` por dominio operativo

Regla:

- los componentes de pagina coordinan UI
- los servicios y facades orquestan
- la autoridad funcional sigue en backend

---

## 6. Estado y reactividad

La recomendacion sobria es combinar:

- `signals` para estado de pantalla y UI local
- `RxJS` para flujos HTTP y asincronia

Esto permite:

- no sobredisenar el estado global
- conservar claridad en formularios, tablas y filtros

No conviene introducir una solucion de estado mas pesada salvo necesidad real.

---

## 7. Cliente API recomendado

Debe existir una capa central para:

- resolver URL base
- adjuntar token de autenticacion cuando aplique
- normalizar `ApiResponse<T>`
- mapear errores HTTP a mensajes operativos

Ademas conviene:

- interceptor de auth
- interceptor de errores
- servicio de sesion

---

## 8. Variables y entorno

Variables minimas sugeridas:

- `API_BASE_URL`
- `APP_NAME`
- `APP_ENV`

Regla:

- ninguna URL operativa debe quedar quemada en servicios o componentes

---

## 9. Riesgos a evitar

1. Componentes gigantes con demasiada logica.
2. Copiar validaciones de negocio del backend sin control.
3. Hacer un dashboard inflado que no aporta a la operacion.
4. Dejar auth, roles y navegacion repartidos por la app.
5. Acoplar tablas, formularios y modales a una sola pantalla monolitica.

---

## 10. Pruebas y verificacion superficial

Para esta fase documental basta dejar definidos los controles minimos:

- build del admin en verde
- smoke de login
- smoke de clientes, productos, cotizaciones y pedidos
- smoke del panel de produccion
- verificacion de expiracion de sesion y error de permisos

---

## 11. Regla de apoyo

Si este componente necesita inspiracion adicional, se puede revisar:

- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Proyecto tienda Electronica promedio\docs\04_ADMIN_APP_OPERACION_Y_UX.md`
- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Java\Sistema UE Ninitos Sonadores\backend\docs\backend_v_1\19_backend_v_1_contexto_integracion_y_diseno_frontend.md`

La referencia aporta criterio de cierre, no dominio.
