# Tests base y smoke API

## Estado

Vigente desde T08.

## Objetivo

La pastelería no debe depender solo de tests unitarios aislados. Debe existir una columna de pruebas que confirme que el sistema completo arranca con base real.

## Capas protegidas

```text
Flyway V1/V2
PostgreSQL real
JPA validate
Spring Boot real
Spring Security real
Login seed
Contratos API
Guía Operativa
RequestId
Errores 401 estándar
```

## Pruebas creadas

```text
AbstractPostgresIntegrationTest
BackendMigrationIntegrationTest
BackendApiSmokeIntegrationTest
```

## Regla de calidad

Si una migración cambia una tabla crítica, debe actualizarse o ampliarse el test de migración.

Si un endpoint se vuelve parte del contrato público/administrativo, debe aparecer en `ApiContractRegistry` y, cuando sea crítico, en smoke API.

## Docker

Las pruebas de integración usan Testcontainers. Si Docker no está disponible, se omiten para no bloquear máquinas sin Docker. Para validar seriamente la base, Docker debe estar activo.
