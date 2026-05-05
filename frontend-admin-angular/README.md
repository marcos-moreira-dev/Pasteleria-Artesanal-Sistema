# Frontend Admin Angular

Consola administrativa y operativa para atención, cotizaciones, pedidos, producción, abastecimiento, reportes, notificaciones y guía operativa.

## Qué ya cubre

- Login conectado al backend.
- Shell de navegación por módulos.
- Carcasa parametrizable en `src/app/core/config/business-shell.config.ts`.
- Dashboard operativo.
- Clientes, productos, cotizaciones, pedidos y producción.
- Abastecimiento: dashboard, inventario, compras, proveedores y movimientos.
- Reportes y notificaciones.
- Pantalla **Guía operativa** en `/guia-operativa`, conectada a `/api/v1/casos-uso/hub`.
- Formularios base y acciones de estado para los flujos principales.

## Comandos principales

```powershell
npm install
npm start
npm run build
```

## Variables y backend esperado

El frontend consume el backend configurado en:

`src/app/core/config/api.config.ts`

Por defecto se espera:

```text
http://localhost:8080/api/v1
```

## Rutas principales

- `/` — Dashboard general
- `/clientes`
- `/productos`
- `/cotizaciones`
- `/pedidos`
- `/produccion`
- `/reportes`
- `/guia-operativa`
- `/abastecimiento/dashboard`
- `/abastecimiento/inventario`
- `/abastecimiento/compras`
- `/abastecimiento/proveedores`
- `/abastecimiento/movimientos`

## Regla visual

La pantalla debe hablar en lenguaje de negocio. Evitar mostrar enlaces o conceptos técnicos al usuario final dentro del panel administrativo.
