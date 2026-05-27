# T08 — Tests base y smoke API

## 1. Objetivo

Fortalecer la calidad base del backend después de la reorganización de migraciones V1/V2 hecha en T07.

Esta tanda agrega pruebas de integración reales inspiradas en Cedro:

- PostgreSQL real mediante Testcontainers.
- Flyway real aplicando `db/migration`.
- Validación JPA contra el schema generado por V1/V2.
- Smoke API sobre Spring Boot real.
- Validación SQL de los scripts `db/validation`.
- Protección mínima de contratos API, guía operativa y seguridad estándar.

La tanda no busca cubrir todos los módulos funcionales todavía. Crea la columna vertebral para que las tandas siguientes no avancen a ciegas.

---

## 2. Contexto heredado

Hasta T07 se dejó una línea de base nueva:

```text
backend/src/main/resources/db/migration/
  V1__pasteleria_base_actual.sql
  V2__erp_pasteleria_unificado_3fn.sql
  R__pasteleria_reporting_views.sql
  R__pasteleria_semantic_views.sql
```

También se crearon:

```text
backend/src/main/resources/db/dev-migration/
backend/src/main/resources/db/presentation-migration/
backend/src/main/resources/db/validation/
```

El problema es que una migración puede verse correcta en lectura estática, pero fallar cuando:

- se aplica en PostgreSQL real;
- JPA valida entidades contra tablas reales;
- Spring Security intercepta endpoints;
- Angular/backend esperan contratos diferentes;
- los seeds no cargan como se esperaba.

Cedro resolvía esto usando pruebas de migración, smoke API y validación real con PostgreSQL/Testcontainers.

---

## 3. Patrón Cedro rescatado

Se rescata la disciplina, no el dominio restaurante:

```text
Migración real
→ Spring Boot real
→ PostgreSQL real
→ smoke API
→ contratos protegidos
→ evidencia de que el sistema arranca
```

Cedro enseñó que no basta con que el SQL exista. Debe ejecutarse.

---

## 4. Estado actual de Pastelería

Antes de esta tanda ya existían tests unitarios y WebMvc por controller, pero faltaba una prueba central que validara:

- que Flyway aplique V1/V2;
- que JPA arranque con `ddl-auto=validate`;
- que los schemas V2 existan;
- que los seeds mínimos estén cargados;
- que un endpoint público responda;
- que un endpoint protegido responda 401 estándar;
- que login funcione con seed canónico;
- que contratos y guía operativa respondan con JWT.

---

## 5. Alcance

Esta tanda implementa:

1. Dependencias Testcontainers para PostgreSQL.
2. Base abstracta de integración con PostgreSQL real.
3. `BackendMigrationIntegrationTest`.
4. `BackendApiSmokeIntegrationTest`.
5. Validación de scripts SQL de `db/validation`.
6. Documentación de calidad para pruebas base.

---

## 6. Fuera de alcance

No se implementa todavía:

- tests profundos de caja;
- tests profundos de inventario;
- tests profundos de producción;
- tests de cartera, contabilidad o fiscalidad;
- Playwright/E2E;
- refactor frontend;
- cambios visuales;
- cambios de UX/UI;
- cambios en storefront.

Esos llegarán en tandas posteriores.

---

## 7. Archivos leídos/modificados

### Modificados

```text
backend/pom.xml
implementacion_pasteleria_erp/tandas/T08_tests_base_y_smoke_api.md
implementacion_pasteleria_erp/60_calidad/02_tests_base_y_smoke_api.md
implementacion_pasteleria_erp/80_roadmap/00_roadmap_tandas.md
```

### Nuevos

```text
backend/src/test/java/com/pasteleria/support/AbstractPostgresIntegrationTest.java
backend/src/test/java/com/pasteleria/integration/BackendMigrationIntegrationTest.java
backend/src/test/java/com/pasteleria/integration/BackendApiSmokeIntegrationTest.java
```

---

## 8. Cambios realizados

### 8.1 Dependencias de Testcontainers

Se agregaron dependencias de test para:

```text
org.testcontainers:junit-jupiter
org.testcontainers:postgresql
```

Estas dependencias permiten levantar PostgreSQL real en pruebas.

### 8.2 Base de integración

Se creó `AbstractPostgresIntegrationTest` con:

- `@Testcontainers(disabledWithoutDocker = true)`;
- PostgreSQL 16 Alpine;
- `@SpringBootTest(webEnvironment = RANDOM_PORT)`;
- propiedades dinámicas para datasource;
- Flyway habilitado;
- ubicación `classpath:db/migration`;
- `ddl-auto=validate`;
- JWT secret de test;
- worker de reportes desactivado.

### 8.3 Test de migración

`BackendMigrationIntegrationTest` valida:

- tablas críticas V1;
- schemas V2 `core` e `inteligencia`;
- tabla `core.legacy_objeto_mapeo`;
- vista semántica inicial;
- usuario admin;
- productos activos;
- casos de uso activos;
- scripts SQL de validación.

### 8.4 Smoke API

`BackendApiSmokeIntegrationTest` valida:

- `/api/v1/public/health` responde `ok=true`;
- `X-Request-Id` se conserva;
- endpoint protegido sin token responde 401 con contrato estándar;
- login con `admin/admin12345` funciona;
- `/api/v1/contratos` responde con token;
- `/api/v1/casos-uso/hub` responde con token.

---

## 9. Riesgos

### Riesgo 1 — Docker no disponible

Si Docker no está instalado o no está corriendo, Testcontainers omitirá estas pruebas por `disabledWithoutDocker = true`.

Eso evita falsos negativos en máquinas sin Docker, pero para validar seriamente V1/V2 sí debe ejecutarse con Docker activo.

### Riesgo 2 — Maven no disponible en el entorno actual

En este entorno no se pudo ejecutar Maven porque el wrapper necesita descargar Maven desde internet. Debe probarse en Windows/local.

### Riesgo 3 — V1/V2 pueden fallar en PostgreSQL real

Precisamente esta tanda existe para detectar eso. Si falla, la prioridad siguiente no es tapar el test sino corregir la migración o el mapeo JPA.

---

## 10. Criterios de aceptación

La tanda se considera correcta si:

- los tests nuevos compilan;
- con Docker activo, Testcontainers levanta PostgreSQL;
- Flyway aplica V1/V2 y vistas repeatable;
- Spring Boot arranca con JPA validate;
- `/api/v1/public/health` responde;
- un endpoint protegido sin token devuelve 401 estándar;
- login seed funciona;
- contratos y guía operativa responden autenticados;
- los scripts SQL de validación pasan.

---

## 11. Pruebas mínimas

En Windows/local:

```bat
scripts\test-backend.bat
```

O manualmente:

```bat
cd backend
mvn test
```

Con Docker activo se deben ejecutar los tests de integración. Sin Docker, se omiten.

---

## 12. Notas para la siguiente tanda

La siguiente tanda es:

```text
T09 — Identity, roles, permisos y sucursales
```

T09 debe tomar en cuenta:

- ya existe catálogo `Permisos.java`;
- ya existen contratos API;
- ya existe smoke API autenticado;
- todavía no hay modelo avanzado de roles globales/locales;
- backend aún autoriza principalmente por JWT/rol simple;
- la seguridad real por permisos/sucursales debe hacerse con refactoring decente, no parcheando controllers.
