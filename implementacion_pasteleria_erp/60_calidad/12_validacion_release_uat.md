# T26 — Validación release, UAT y cierre pre-GitHub

## Validación esperada

Antes de subir a GitHub o entregar el ZIP final, ejecutar:

```bat
scripts\test-all.bat
```

También se pueden ejecutar por separado:

```bat
scripts\test-backend.bat
scripts\test-admin.bat
scripts\test-storefront.bat
```

## Revisión manual mínima

1. Ejecutar `scripts\run-demo.bat`.
2. Abrir Admin Angular en `http://localhost:4200`.
3. Iniciar sesión con `admin / admin12345`.
4. Revisar módulos operativos y workspace ERP.
5. Abrir storefront en `http://localhost:4321`.
6. Confirmar catálogo, contacto y navegación pública.
7. Ejecutar `scripts\stop-local.bat` al terminar.

## Criterio de cierre

El sistema puede considerarse cerrado para pre-GitHub si:

- backend compila y pasa tests;
- Admin Angular compila;
- storefront Astro compila;
- modo demo levanta con datos inventados;
- modo operativo local levanta sin datos demo;
- la documentación no declara SRI/fiscalidad productiva completa;
- la carpeta `scripts/` solo contiene puntos de entrada `.bat`.
