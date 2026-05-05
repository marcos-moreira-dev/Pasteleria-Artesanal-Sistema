# Auditoría T11/T12 — Documentación, trazabilidad y cierre QA

## Objetivo

Cerrar la continuidad del proyecto después de T10 dejando documentación, trazabilidad y verificación pública alineadas con el código real.

## Cambios aplicados

- Actualizado `README.md` raíz con guía operativa, abastecimiento, scripts de validación y puertos reales.
- Actualizado `backend/README.md` con módulos actuales, endpoints y regla Flyway vs SQL canónico.
- Actualizado `frontend-admin-angular/README.md` con rutas y módulo de guía operativa.
- Actualizado `frontend-publico-astro/README.md` con rutas, variable pública y nota de build.
- Creado `docs/proyecto/14_guia_operativa_integrada.md`.
- Extendidos documentos de casos de uso, trazabilidad, API REST, operación, calidad, frontend admin y storage.
- Renombrado documento con caracteres escapados en el nombre a `docs/modelo_negocio_arquetipico_pastelerias_norte_guayaquil.md`.
- Eliminadas las tareas T11/T12 de `tandas-pendientes` porque ya fueron aplicadas.
- Eliminados artefactos temporales o diagnósticos que no deben formar parte del cierre.

## Verificación Astro

En el entorno de trabajo se ejecutó:

```bash
cd frontend-publico-astro
npm run build
```

Resultado: build estático correcto con 6 páginas generadas.

## Verificación Angular

Se dejó documentado el comando oficial:

```powershell
cd frontend-admin-angular
npm run build
```

No se empaquetan `node_modules` ni `dist` en el zip final. La validación real debe ejecutarse en Windows con dependencias instaladas mediante `npm install` o `npm ci`.

## Estado final

No quedan tandas pendientes. El proyecto está listo para revisión funcional local, capturas finales y subida a repositorio.
