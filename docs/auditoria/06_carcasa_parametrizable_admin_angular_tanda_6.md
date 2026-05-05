# Tanda 6 — Carcasa parametrizable del admin Angular

## Resultado

El admin Angular conserva su estética, pero ahora concentra textos de marca, login, encabezados de rutas y metas base en un archivo de configuración.

## Archivo central

`frontend-admin-angular/src/app/core/config/business-shell.config.ts`

## Cambios realizados

- El shell lateral usa `businessShellConfig.brand` para kicker, título, copy y logo.
- El login usa `businessShellConfig.login` para título, copy, accesos locales y mensaje de soporte.
- El header del admin resuelve títulos y cejas desde `businessShellConfig.pageMetaByRoute`.
- El dashboard toma metas base desde `businessShellConfig.dashboard`.

## Regla de mantenimiento

Para adaptar la carcasa a otro negocio, primero se modifica el archivo central de configuración. No se deben perseguir cadenas de identidad en componentes sueltos.

## Responsabilidad única

Esta tanda solo centraliza identidad y textos de carcasa. No convierte la aplicación en multiempresa real, no crea tablas nuevas y no rediseña el landing público.
