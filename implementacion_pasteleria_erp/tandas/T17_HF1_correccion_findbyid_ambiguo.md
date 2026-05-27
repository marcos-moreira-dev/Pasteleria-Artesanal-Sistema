# T17-HF1 — Corrección de compilación por findById ambiguo

## Contexto

Después de T17, la validación local del backend se detuvo durante la compilación. Maven sí alcanzó a resolver toolchain Java 21 Temurin y comenzó a compilar el backend, pero falló en tres llamadas a `findById` dentro de los servicios nuevos de cartera y cuentas por pagar.

El error indicaba ambigüedad entre:

- el método `findById(Long)` declarado en puertos propios del proyecto;
- el método genérico `findById(ID)` heredado de `JpaRepository` / `CrudRepository`.

## Causa

Los servicios nuevos inyectaban repositorios concretos de infraestructura:

- `ClientRepository`
- `OrderRepository`
- `ProveedorRepository`

Esos repositorios implementan a la vez un puerto de aplicación y `JpaRepository`, por lo que el compilador veía dos candidatos válidos para la llamada `findById(...)`.

## Corrección aplicada

Se cambiaron las dependencias de los servicios hacia los puertos de aplicación:

- `ClientRepositoryPort`
- `OrderRepositoryPort`
- `ProveedorRepositoryPort`

Archivos modificados:

```text
backend/src/main/java/com/pasteleria/cartera/application/CarteraCommandService.java
backend/src/main/java/com/pasteleria/cuentaspagar/application/CuentasPagarCommandService.java
```

## Decisión arquitectónica

La corrección es coherente con arquitectura limpia: la capa de aplicación debe depender de puertos, no de repositorios concretos de infraestructura, cuando solo necesita operaciones de dominio ya declaradas por el puerto.

## Qué no se tocó

- No se cambiaron tablas.
- No se cambiaron endpoints.
- No se cambió Angular Admin.
- No se cambió Storefront Astro.
- No se cambió la UX/UI.
- No se agregaron flujos nuevos.

## Validación esperada

En máquina local:

```bat
scripts\test-backend.bat
```

Luego:

```bat
scripts\test-admin.bat
scripts\test-storefront.bat
```
