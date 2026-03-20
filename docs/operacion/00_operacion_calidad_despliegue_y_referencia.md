# 00 - Operacion, calidad, despliegue y referencia inteligente

## 1. Proposito

Este documento cierra la capa operativa de Pasteleria para que el proyecto no quede solo en analisis de negocio y diseño de componentes.

Su objetivo es fijar buenas practicas para:

- `README`
- `.gitignore`
- variables de entorno
- despliegue local
- testing
- calidad
- runbooks
- observabilidad minima
- y referencia inteligente hacia proyectos previos

---

## 2. Artefactos no codigo que forman parte de la ingenieria

En este proyecto deben considerarse parte formal del sistema:

- `README.md`
- `.gitignore`
- `.env.example`
- `docker-compose.yml`
- `Dockerfile`
- guias de despliegue
- checklists de release
- runbooks

Regla:

- si uno de estos archivos contradice la arquitectura, el problema es de ingenieria, no de "documentacion secundaria".

---

## 3. Variables de entorno minimas recomendadas

## 3.1. Backend

- `SPRING_PROFILES_ACTIVE`
- `APP_PORT`
- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_SECONDS`
- `JWT_ISSUER`
- `REPORT_STORAGE_PATH`
- `PUBLIC_BASE_URL`

## 3.2. Frontend publico en Astro

- `PUBLIC_API_BASE_URL`
- `PUBLIC_SITE_URL`

## 3.3. Frontend administrativo en Angular

- `API_BASE_URL`

---

## 4. Perfiles y entorno local reproducible

La linea base recomendada es:

- PostgreSQL en Docker Compose
- backend Spring Boot corriendo local o contenedorizado
- Astro local
- Angular local

Perfiles minimos:

- `dev`
- `prod`
- `test` cuando empiecen pruebas automatizadas formales

---

## 5. Buenas practicas de despliegue

## 5.1. Backend

- no hardcodear secretos
- exponer health checks
- separar config comun y config por perfil
- no depender de `ddl-auto=create` fuera de pruebas puntuales
- manejar migraciones con Flyway

## 5.2. Frontends

- URLs de API por entorno
- builds reproducibles
- copy visible sin lenguaje de infraestructura
- manejo claro de errores de red y backend

## 5.3. Archivos y reportes

- definir ruta de storage desde config
- no mezclar metadata con archivo fisico
- controlar nombres, permisos y politica de retencion

---

## 6. Calidad minima por componente

## 6.1. DB

- schema consistente
- constraints utiles
- catalogos claros
- seeds reproducibles

## 6.2. Backend

- contrato `ApiResponse`
- errores consistentes
- auth y roles
- validaciones fuertes
- pruebas de casos criticos

## 6.3. Frontend publico

- CTA claros
- formularios cortos
- accesibilidad razonable
- cotizador comprensible

## 6.4. Frontend administrativo y produccion

- tablas legibles
- filtros consistentes
- formularios agrupados
- estados visibles
- feedback claro

---

## 7. Testing minimo recomendado

### 7.1. Backend

- unitarias de servicios
- integracion de repositorios
- integracion de controladores
- pruebas de auth
- pruebas de reportes async y cambios de estado

### 7.2. Frontend administrativo

- build
- pruebas de componentes criticos
- validacion de filtros, formularios y estados

### 7.3. Frontend publico

- build
- smoke de rutas principales
- validacion del cotizador

---

## 8. Observabilidad minima

Se recomienda como linea base:

- health checks
- logs estructurados
- correlation id cuando sea viable
- logging de errores funcionales y tecnicos
- trazabilidad de solicitudes de reporte y de cambios relevantes

---

## 9. Runbooks minimos sugeridos

1. login o auth falla
2. backend no conecta a PostgreSQL
3. migracion Flyway falla
4. cotizador no registra solicitud
5. reportes quedan en cola o error
6. archivos o descargas no aparecen
7. SSE o actualizaciones operativas no refrescan

---

## 10. Regla de referencia inteligente

Pasteleria puede revisar como referencia inteligente:

- `D:\Carrera Profesional\Práctica de habilidades profesionales\Programación\Java\Sistema UE Niñitos Soñadores`
- `D:\Carrera Profesional\Práctica de habilidades profesionales\Programación\Proyecto tienda Electronica promedio`

Esa consulta sirve para:

- estructuras de README
- buenas practicas de `.gitignore`
- operaciones locales
- runbooks
- patrones de seguridad
- CI/CD minimo
- y documentacion de despliegue

Regla importante:

- la referencia ayuda,
- pero no debe meter complejidad ajena ni romper el tipo de producto acordado para Pasteleria.

---

## 11. Checklist minimo antes de release o arranque serio

1. Confirmar variables de entorno y secretos.
2. Confirmar migraciones y seeds.
3. Validar health check del backend.
4. Validar login y permisos base.
5. Validar cotizador y reportes clave.
6. Validar rutas publicas y consumo desde Angular o Astro.
7. Revisar logs, storage de archivos y paths configurados.
8. Revisar que `README`, `.gitignore` y `.env.example` sigan consistentes con el estado real del proyecto.

---

## 12. Cierre

Pasteleria no debe pasar a implementacion con IA solo con dominio y modulos. Tambien necesita una base operativa clara.

Esta capa existe para que el proyecto nazca:

- documentado,
- reproducible,
- mantenible,
- y mas cercano a software real que a maqueta academica.
