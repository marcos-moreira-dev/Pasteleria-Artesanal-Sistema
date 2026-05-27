# 04 - Frontend admin: build, testing y operacion

## 1. Proposito

Este documento fija la operacion minima y profesional del frontend admin.

---

## 2. Versiones de trabajo

La linea base operativa es:

- `Node.js 22.12.0+`
- `Angular 21.x`
- `Angular CLI 21.x`
- `TypeScript 5.9.x`
- `RxJS 7.4+`

---

## 3. Scripts minimos esperados

El proyecto debe exponer al menos:

- `npm start`
- `npm run build`
- `npm run test` o el target oficial de pruebas del workspace

Si se agrega lint, debe declararse formalmente.

---

## 4. Variables minimas

Variables operativas sugeridas:

- `API_BASE_URL`
- `APP_ENV`
- `APP_NAME`

La recomendacion canonica es resolverlas con configuracion runtime del frontend, por ejemplo mediante un asset local como `src/assets/app-config.js`, y no quemarlas dentro de servicios o facades de dominio.

Con la refactorizacion actual, tambien queda congelado este criterio:

- `core/api` solo habla con HTTP y contratos comunes
- `core/contracts` concentra solo contratos verdaderamente transversales
- `core/store` mantiene el estado compartido del panel
- `features/*/state` expone facades por modulo para que cada pantalla no dependa de un servicio dios
- `features/*/models` concentra los contratos de cada dominio para evitar un archivo unico de tipos mezclados

---

## 5. Calidad minima exigible

Antes de marcar listo este componente:

1. instalacion limpia con la version de Node correcta
2. build del admin en verde
3. smoke de login
4. smoke de modulos clave
5. smoke de permisos y error de sesion

Si existe suite automatizada del workspace, tambien debe pasar.

---

## 6. Pruebas superficiales pertinentes

En esta fase basta con dejar definidos estos controles:

- autenticacion base
- navegacion protegida
- listado + filtro + paginacion en clientes, cotizaciones, pedidos, produccion y reportes
- filtro incremental de clientes mientras el usuario escribe
- formulario reactivo con validacion visible
- autocalculo de precio cuando se selecciona un producto ya existente del catalogo
- consumo correcto de errores del backend
- descarga de archivos cuando el worker de reportes marca un job como completado
- desaparicion automatica de mensajes transitorios despues de unos segundos sin dejar banners pegados

---

## 7. Despliegue recomendado

La opcion mas sobria es publicar el admin como servicio web interno o protegido, separado del backend pero alineado con el mismo entorno operativo.

Reglas:

- no mezclar secretos en el bundle
- no depender de configuraciones manuales de ultimo minuto
- mantener una URL base por entorno

---

## 8. Runbooks minimos utiles

Incidencias razonables a documentar cuando exista codigo:

- login administrativo falla
- el admin no conecta al backend
- tabla no carga por token vencido
- build falla por entorno mal configurado
- usuario ve menu sin permisos efectivos

Para entorno local debe contemplarse ademas:

- CORS bloquea login o carga de modulos desde un puerto local temporal, por ejemplo `http://localhost:4200` o `http://localhost:59305`
- `API_BASE_URL` apunta al backend equivocado
- existe sesion vieja en `localStorage` y el panel queda con estado inconsistente
- el worker de reportes tarda mas de lo aceptable y el panel debe poder forzar `Actualizar datos` sin perder el contexto
- la descarga debe salir del navegador hacia `Downloads` u otra carpeta del usuario, sin escribir binarios dentro del repositorio

---

## 9. Referencia inteligente

Para ideas de cierre operativo se puede revisar:

- `D:\Carrera Profesional\Practica de habilidades profesionales\Programacion\Proyecto tienda Electronica promedio\docs\10_OPERACION_LOCAL_Y_RUNBOOKS.md`

---

## 10. Cierre

El admin no esta realmente cerrado hasta que construye, autentica, falla de forma entendible y puede desplegarse sin rituales manuales raros.
