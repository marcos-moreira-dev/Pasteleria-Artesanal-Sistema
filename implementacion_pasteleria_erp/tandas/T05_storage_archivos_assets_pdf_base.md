# T05 — Storage, archivos, assets y PDF base

## 1. Objetivo

Implementar una base transversal para storage local, assets públicos, archivos internos descargables y PDFs con fondo blanco, inspirada en Cedro pero adaptada al proyecto de pastelería.

La tanda no rediseña la UX/UI. Su objetivo es corregir arquitectura interna: dejar de tratar archivos como rutas sueltas y separar claramente:

- assets públicos del catálogo;
- archivos internos generados;
- PDFs de reportes/recetas;
- descarga segura por identificador.

## 2. Contexto heredado

Antes de esta tanda ya existía `archivo_recurso` y el módulo de reportes guardaba PDFs mediante `LocalReportStorageService`, pero el almacenamiento estaba demasiado acoplado a reportes.

También existía `StaticCatalogAssetService`, pero devolvía rutas tipo `/assets/...` sin un controlador robusto que sirviera assets desde `storage/assets` con whitelist.

Cedro mostró un patrón más sólido:

```text
assets públicos → endpoint público validado
archivos internos → descarga por id con política
PDFs → archivo_recurso + storage + auditoría futura
```

## 3. Fuente Cedro usada como referencia

Se rescata de Cedro:

- separación entre assets públicos y archivos internos;
- storage configurable;
- descarga por id;
- metadatos de archivo;
- path traversal bloqueado;
- PDF con fondo blanco;
- no exponer rutas físicas al frontend.

No se copia:

- estética Cedro;
- assets Cedro;
- rutas de restaurante;
- política final de permisos, porque eso queda para T09.

## 4. Estado actual de Pastelería

El proyecto tenía:

- `StorageProperties`;
- `StaticCatalogAssetService`;
- `archivo_recurso`;
- `FileResourceEntity`;
- `LocalReportStorageService`;
- PDFs de reportes y recetas;
- imágenes de producto bajo `storage/assets/products`.

Problemas detectados:

- `LocalReportStorageService` mezclaba storage físico y metadata de reportes;
- no había endpoint genérico `/api/v1/archivos/{id}/descargar`;
- no había endpoint robusto `/api/v1/assets/...`;
- los PDFs no tenían una defensa transversal de fondo blanco;
- `RecetaPdfService` cargaba logos desde `Path.of("storage", ...)`, ignorando `app.storage.root`.

## 5. Alcance

Esta tanda implementa:

- `StorageService`;
- `LocalStorageService`;
- `StoreFileCommand`;
- `StoredFile`;
- `FileStorageException`;
- `PublicAssetService`;
- `PublicAssetController`;
- `ArchivoController`;
- `DescargarArchivoService`;
- `ArchivoAccessPolicy`;
- `PdfWhiteBackgroundPageEvent`;
- refactor de `LocalReportStorageService` para usar storage genérico;
- refactor de `StaticCatalogAssetService` para devolver `/api/v1/assets/...`;
- refactor de `ProductImageService` para guardar en `storage/assets/products` y devolver URL pública nueva;
- refactor de `RecetaPdfService` para usar `StorageProperties`;
- actualización de contratos API;
- tests unitarios de storage y assets.

## 6. Fuera de alcance

No se implementa todavía:

- permisos finos por archivo;
- auditoría de descarga;
- limpieza automática de archivos expirados;
- S3/minio/cloud storage;
- subida genérica de archivos;
- documentos fiscales reales;
- rediseño visual del frontend;
- refactor completo de `FileResourceEntity` hacia un paquete `archivos`.

## 7. Archivos modificados o creados

### Creados

```text
backend/src/main/java/com/pasteleria/common/storage/FileStorageException.java
backend/src/main/java/com/pasteleria/common/storage/StoreFileCommand.java
backend/src/main/java/com/pasteleria/common/storage/StoredFile.java
backend/src/main/java/com/pasteleria/common/storage/StorageService.java
backend/src/main/java/com/pasteleria/common/storage/LocalStorageService.java
backend/src/main/java/com/pasteleria/common/assets/PublicAssetResource.java
backend/src/main/java/com/pasteleria/common/assets/PublicAssetService.java
backend/src/main/java/com/pasteleria/common/assets/PublicAssetController.java
backend/src/main/java/com/pasteleria/common/pdf/PdfWhiteBackgroundPageEvent.java
backend/src/main/java/com/pasteleria/archivos/application/ArchivoDownload.java
backend/src/main/java/com/pasteleria/archivos/application/ArchivoAccessPolicy.java
backend/src/main/java/com/pasteleria/archivos/application/DescargarArchivoService.java
backend/src/main/java/com/pasteleria/archivos/api/ArchivoController.java
backend/src/test/java/com/pasteleria/common/storage/LocalStorageServiceTest.java
backend/src/test/java/com/pasteleria/common/assets/PublicAssetServiceTest.java
```

### Modificados

```text
backend/src/main/java/com/pasteleria/common/assets/StaticCatalogAssetService.java
backend/src/main/java/com/pasteleria/productos/application/ProductImageService.java
backend/src/main/java/com/pasteleria/abastecimiento/application/RecetaPdfService.java
backend/src/main/java/com/pasteleria/reportes/application/ReportPdfDocumentService.java
backend/src/main/java/com/pasteleria/reportes/application/LocalReportStorageService.java
backend/src/main/java/com/pasteleria/reportes/application/port/FileResourceRepositoryPort.java
backend/src/main/java/com/pasteleria/common/config/SecurityConfig.java
backend/src/main/java/com/pasteleria/common/error/ErrorCode.java
backend/src/main/java/com/pasteleria/common/error/GlobalExceptionHandler.java
backend/src/main/java/com/pasteleria/common/security/Permisos.java
backend/src/main/java/com/pasteleria/contratos/application/ApiContractRegistry.java
backend/src/test/java/com/pasteleria/contratos/application/ApiContractRegistryTest.java
```

## 8. Decisiones técnicas

### 8.1. Assets públicos

Se sirven por:

```text
GET /api/v1/assets/{type}/{filename}
GET /assets/{type}/{filename}
```

El segundo endpoint se conserva como compatibilidad para rutas antiguas.

Tipos permitidos:

```text
branding
products
categories
branches
placeholders
```

Extensiones permitidas:

```text
png
jpg
jpeg
webp
svg
```

### 8.2. Archivos internos

Se descargan por:

```text
GET /api/v1/archivos/{archivoId}/descargar
```

La descarga valida:

- que el archivo exista;
- que esté `DISPONIBLE`;
- que no haya expirado;
- que la ruta no salga del storage.

La autorización fina por permisos queda para T09.

### 8.3. Storage genérico

`LocalStorageService` guarda archivos bajo `app.storage.root`, calcula checksum SHA-256 y bloquea path traversal.

Los reportes nuevos se guardan bajo:

```text
storage/reportes/
```

Los reportes históricos que guardaban solo el nombre físico se resuelven con compatibilidad usando `app.storage.report-path`.

### 8.4. PDF base

Se agregó `PdfWhiteBackgroundPageEvent` para forzar fondo blanco en PDFs.

Se aplica a:

- reportes;
- recetas PDF.

## 9. Riesgos

- Algún frontend antiguo puede seguir esperando `/assets/...`; por eso se dejó compatibilidad.
- La política de descarga aún no valida permisos finos; se endurecerá en T09.
- `FileResourceEntity` sigue físicamente en el paquete de reportes por compatibilidad; una migración total a `archivos` queda para una refactorización futura.

## 10. Criterios de aceptación

La tanda está completa si:

- assets públicos se resuelven desde storage con whitelist;
- imágenes nuevas de producto devuelven `/api/v1/assets/products/...`;
- reportes siguen guardándose y resolviéndose;
- existe endpoint genérico de descarga de archivos;
- PDFs tienen fondo blanco;
- hay tests de storage/assets;
- no se tocó la UX/UI;
- contratos API incluyen assets y archivos.

## 11. Pruebas mínimas

En Windows/local:

```bat
scripts\test-backend.bat
```

o:

```bat
cd backend
mvn test
```

Pruebas manuales sugeridas:

```text
GET http://localhost:8080/api/v1/assets/branding/logo-cuadrado.png
GET http://localhost:8080/assets/branding/logo-cuadrado.png
GET http://localhost:8080/api/v1/archivos/{id}/descargar
```

## 12. Notas para el siguiente chat

La siguiente tanda es:

```text
T06 — Guía Operativa / Casos de uso
```

T06 debe usar la infraestructura de archivos/PDF de esta tanda para preparar el futuro endpoint:

```text
GET /api/v1/casos-uso/manual.pdf
```
