# Validación Angular core ERP

## Validación aplicada

En T22 se validó el build del Admin Angular después de agregar modelos, métodos de API y fachada ERP.

```bat
npm install
npm run build
```

Resultado:

```text
Application bundle generation complete.
```

## Alcance de calidad

Esta validación confirma que:

- los tipos TypeScript compilan;
- `ApiClientService` conserva compatibilidad;
- `ErpCoreFacade` puede inyectarse y compilar;
- no se rompió el shell existente.

## Pendiente

Cuando T23 agregue pantallas, deberá validarse manualmente navegación, estados vacíos, errores, permisos y conservación visual.
