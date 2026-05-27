# Reglas de refactoring frontend

## Estado

Vigente.

## Principio

La UX/UI actual de la pastelería se respeta, pero la arquitectura Angular puede y debe refactorizarse cuando esté pesada o confusa.

## Reglas

1. No crear componentes gigantes.
2. No usar un `ApiClientService` monolítico para todo.
3. Crear servicios API por dominio.
4. Usar facades cuando el workspace tenga lógica compleja.
5. Separar componentes por responsabilidad.
6. No cargar todo el sistema al iniciar el shell.
7. Mantener estados de carga/error/vacío/sin permiso.
8. No copiar estética Cedro.
9. No cambiar UX/UI accidentalmente.
10. No mezclar lógica de negocio, HTTP y rendering en un solo archivo gigante.

## Patrón recomendado

```text
FeaturePage
  → Facade
  → ApiService de dominio
  → componentes pequeños
```
