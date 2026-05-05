# Auditoría npm del frontend Angular

## Contexto
Durante el cierre del proyecto se ejecutó `npm audit fix` en `frontend-admin-angular`. El reporte señaló vulnerabilidades en dependencias de desarrollo/transitivas vinculadas a `@angular/build`, `vite`, `undici` y `picomatch`.

## Lectura correcta
Estas dependencias pertenecen principalmente al tooling de desarrollo y build. No significan por sí solas que el backend o la app compilada estén comprometidos, pero sí conviene mantener el toolchain actualizado antes de publicar o desplegar.

## Recomendación segura
No hacer `npm audit fix --force` sin revisar, porque puede saltar de versión mayor y romper Angular. Preferir:

```powershell
cd frontend-admin-angular
npm install
npm audit fix
npm run build
```

Si `npm audit` sigue reportando vulnerabilidades en `@angular/build`, `vite`, `undici` o `picomatch`, actualizar el bloque Angular dentro de la misma línea menor estable y volver a generar `package-lock.json` con `npm install`.

## Criterio aplicado en el proyecto
- Se documenta el hallazgo.
- No se fuerza una actualización mayor automática.
- Se prioriza que `npm run build` siga funcionando.
- Se deja la corrección de dependencias como paso controlado de mantenimiento, no como parche ciego de última hora.
