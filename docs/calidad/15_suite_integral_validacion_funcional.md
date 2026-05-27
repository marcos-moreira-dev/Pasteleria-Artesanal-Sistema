# Suite integral de validación funcional

## Objetivo

Después de incorporar la guía operativa y reorganizar scripts al patrón de Cedro Damasco, este proyecto necesita una prueba de cierre que no se limite a compilar. La suite integral valida que el sistema arranque y que los módulos principales respondan contra una base real de PostgreSQL local.

## Comando principal

Desde la raíz del proyecto:

```bat
scripts\validate-system-full.bat
```

También existe el alias:

```bat
scripts\validate-functional-suite.bat
```

## Qué valida

1. Entorno local: Java, Maven wrapper, Docker, Node/npm y archivos clave.
2. Infraestructura dev: PostgreSQL local en Docker Compose.
3. Base canónica: carga o reparación no destructiva de la base `pasteleria`.
4. Tablas obligatorias: usuarios, productos, clientes, pedidos, producción, cotizaciones, reportes, abastecimiento y guía operativa.
5. Seeds mínimos: verifica cantidades mínimas por tabla para evitar una base vacía o incompleta.
6. Backend: empaquetado con Maven y arranque real de Spring Boot.
7. Seguridad: login con JWT usando el usuario `admin`.
8. API pública: health, branding, categorías y productos publicados.
9. API administrativa: productos, clientes, pedidos, producción, cotizaciones, reportes, notificaciones y abastecimiento.
10. Guía operativa: hub, lista completa, filtro por módulo y detalle por código con pasos.
11. Frontend admin: build Angular.
12. Frontend público: build Astro.
13. Assets: auditoría de recursos visuales.

## Logs

La suite genera logs en:

```text
.diagnostics/logs/
```

Los archivos más importantes son:

```text
validate-system-full_YYYYMMDD_HHMMSS.log
backend-validation_YYYYMMDD_HHMMSS.log
validate-system-full_summary_YYYYMMDD_HHMMSS.json
```

Si algo falla, pasar el `.log` completo permite diagnosticar en qué capa ocurrió el problema.

## Opciones útiles

Resetear completamente la base local antes de probar:

```bat
scripts\validate-system-full.bat -ResetDatabase
```

Omitir builds de Angular/Astro para enfocarse solo en backend + API:

```bat
scripts\validate-system-full.bat -SkipFrontendBuild
```

Dejar el backend corriendo al final de la prueba:

```bat
scripts\validate-system-full.bat -KeepBackendRunning
```

## Criterio de cierre

La tanda se considera sana si el script termina con:

```text
OK - Validacion integral funcional completada.
```

Si falla, no asumir que todo el proyecto está roto. La suite está diseñada para indicar si el fallo corresponde a entorno, base, backend, endpoint específico, Angular, Astro o assets.
