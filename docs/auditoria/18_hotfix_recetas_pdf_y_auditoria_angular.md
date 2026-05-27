# Hotfix — Recetas PDF, limpieza visual y auditoría Angular

## Problemas observados
- El botón `Descargar receta PDF` aparecía, pero la descarga fallaba con un mensaje genérico.
- La tarjeta de producto mostraba un recuadro informativo redundante sobre la receta técnica.
- El usuario ejecutó `npm audit fix` y obtuvo vulnerabilidades transitorias en dependencias del tooling Angular.

## Correcciones aplicadas

### Recetas PDF
- Se agregó `@Transactional(readOnly = true)` al endpoint de descarga para evitar problemas de entidades lazy con `open-in-view=false`.
- Se declaró explícitamente `produces = MediaType.APPLICATION_PDF_VALUE`.
- Se cambió el error de producto inexistente por `ResponseStatusException(HttpStatus.NOT_FOUND, ...)`.
- Se expuso `Content-Disposition` en CORS para descargas.

### Productos Angular
- Se eliminó el recuadro informativo `recipe-hint` de cada tarjeta.
- Se mantuvo solo el botón principal `Descargar receta PDF`.
- Se mejoró el mensaje de error para mostrar el código HTTP cuando falle la descarga.

### Auditoría npm
- Se creó `docs/seguridad/01_auditoria_frontend_angular.md` con lectura segura del reporte.
- No se aplicó `npm audit fix --force` para evitar saltos mayores o ruptura del build.

## Archivos modificados
- `backend/src/main/java/com/pasteleria/abastecimiento/api/RecetaPdfController.java`
- `backend/src/main/java/com/pasteleria/common/config/SecurityConfig.java`
- `frontend-admin-angular/src/app/features/productos/products-page.component.ts`
- `docs/seguridad/01_auditoria_frontend_angular.md`
