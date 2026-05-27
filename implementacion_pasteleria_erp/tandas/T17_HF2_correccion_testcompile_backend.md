# T17-HF2 — Corrección de testCompile backend

## Contexto

Después de T17-HF1, la compilación principal del backend volvió a quedar en verde, pero la etapa de compilación de tests falló en `testCompile`.

El log reportó dos problemas:

1. `BackendApiSmokeIntegrationTest` importaba `TestRestTemplate` desde `org.springframework.boot.test.web.client`, paquete no disponible con la combinación actual de Spring Boot 4 usada por el proyecto.
2. `ProductImageServiceTest` seguía construyendo `ProductImageService` con el constructor antiguo de un solo parámetro, aunque el servicio productivo ahora requiere `ProductRepositoryPort` y `StorageProperties`.

## Cambios realizados

### 1. BackendApiSmokeIntegrationTest

Se reemplazó el uso de `TestRestTemplate` por `java.net.http.HttpClient` y `@LocalServerPort`.

La prueba sigue ejecutando HTTP real contra el servidor levantado por `@SpringBootTest(webEnvironment = RANDOM_PORT)`, pero ya no depende del cliente de test removido o reubicado.

Se conservaron las validaciones sobre:

- `/api/v1/public/health`
- respuesta 401 para endpoint protegido sin token
- login de administrador
- `/api/v1/auth/me`
- `/api/v1/contratos`
- `/api/v1/casos-uso/hub`
- `/api/v1/terceros`

### 2. ProductImageServiceTest

Se actualizó el setup del test para crear `ProductImageService` con:

- `ProductRepositoryPort` mockeado
- `StorageProperties` apuntando a un directorio temporal con `@TempDir`

También se actualizaron las expectativas de URL pública para alinearlas con el servicio actual:

- antes: `/assets/products/...`
- ahora: `/api/v1/assets/products/...`

## Qué no se tocó

- No se tocaron endpoints productivos.
- No se tocaron migraciones SQL.
- No se tocó Angular Admin.
- No se tocó Astro Storefront.
- No se modificaron flujos de cartera ni cuentas por pagar.

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

## Estado

T17-HF2 corrige deuda de tests detectada después de que la compilación principal del backend ya quedó resuelta.
