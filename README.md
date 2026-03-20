# Pastelería

Sistema integral para una pastelería del norte de Guayaquil, pensado para coordinar vitrina pública, atención comercial, cotizaciones, pedidos y producción desde una arquitectura coherente y mantenible.

La V1 no se plantea como ecommerce completo. El producto acordado combina:

- `PostgreSQL + Flyway` como base transaccional
- `Spring Boot 4` como backend central
- `Astro` como superficie pública comercial
- `Angular` como consola administrativa
- flujo de producción conectado al pedido
- canal público de solicitud de cotización

## Estado actual

La base documental está prácticamente cerrada y el código ya tiene una primera implementación funcional:

- backend modular con seguridad JWT, catálogo, clientes, cotizaciones, pedidos, producción y auditoría básica
- frontend administrativo con login, consulta operativa, formularios base y transiciones de estado
- frontend público con landing bilingüe `es/en`, catálogo, contacto y solicitud pública de cotización
- pruebas web iniciales del backend
- seed enriquecido de demo con datos de operación, reportes, notificaciones y auditoría para estudiar escenarios más reales

## Stack congelado

- `Java 21` con `Eclipse Temurin 21`
- `Spring Boot 4`
- `PostgreSQL 17`
- `Flyway`
- `Node.js 22.12.0+`
- `Astro 5.x`
- `Angular 21.x`
- `TypeScript 5.x` en Astro y `5.9.x` en Angular
- `RxJS 7.8+`
- `Monolito modular`

## Arranque local

1. Configura variables base desde [.env.example](C:\Users\MARCOS MOREIRA\Downloads\Pastelería\.env.example).
2. Inicializa la base del proyecto:
   `.\scripts\init-db.ps1`
   Si ya tenías una base previa y solo quieres sumar el seed empresarial sin resetear:
   `.\scripts\apply-demo-delta.ps1`
3. Build del backend (una vez, incluye assets):
   `cd backend`
   `mvnw.cmd clean package -DskipTests`
4. Inicia el backend:
   `.\scripts\start-backend-dev.cmd`

   Si prefieres levantarlo manualmente:
   `java -jar target\pasteleria-backend-0.0.1-SNAPSHOT.jar`

   > El backend sirve imágenes desde `backend/storage/assets/`. Ejecuta desde la carpeta `backend/`.

5. Build del frontend público (backend debe estar corriendo):
   `cd ..\frontend-publico-astro`
   `npm install`
   `npm run build`
6. Inicia el frontend administrativo:
   `cd ..\frontend-admin-angular`
   `npm install`
   `npm start`

Puertos esperados:

- backend: `http://localhost:8080`
- Astro público: `http://localhost:4321`
- Angular admin: `http://localhost:4200`
- PostgreSQL del proyecto: `localhost:5434`

## Despliegue con Docker

```bash
docker compose up --build
```

El volumen de `storage/` se monta automáticamente en el contenedor del backend.

## Demo rápida

Si quieres recorrer el sistema como producto funcional, sigue esta guía:

- [Demo local y checklist de presentación](C:\Users\MARCOS MOREIRA\Downloads\Pastelería\docs\operacion\05_demo_local_y_checklist_presentacion.md)

Acceso administrativo demo:

- usuario: `admin`
- contraseña: `admin12345`

El seed enriquecido deja listo un entorno de estudio con categorías adicionales, más productos, más clientes, cotizaciones, pedidos, producción, archivos de reportes, jobs, notificaciones y eventos de auditoría.

## Ruta canónica de lectura

1. [Índice canónico](C:\Users\MARCOS MOREIRA\Downloads\Pastelería\docs\00_indice_documentacion_canonica.md)
2. `docs/negocio/`
3. `docs/proyecto/`
4. `docs/ux-ui/`
5. `docs/base-datos/`
6. `docs/backend/`
7. `docs/frontend-publico-astro/`
8. `docs/frontend-admin-angular/`
9. `docs/operacion/`

## Referencia inteligente

Si hace falta revisar decisiones de arquitectura, documentación, operación, despliegue o un fragmento puntual de implementación, este proyecto puede apoyarse en:

- `D:\Carrera Profesional\Práctica de habilidades profesionales\Programación\Java\Sistema UE Niñitos Soñadores`
- `D:\Carrera Profesional\Práctica de habilidades profesionales\Programación\Proyecto tienda Electronica promedio`

La referencia orienta, pero no sustituye el dominio ni el alcance de `Pastelería`.

## Cierre de presentación

Cuando el sistema ya esté más pulido y listo para mostrarse, este repositorio debe evolucionar a un README de producto más propagandístico con:

1. logo del negocio
2. capturas reales de la landing, catálogo y panel admin
3. resumen corto del producto
4. stack y arquitectura
5. acceso rápido a la documentación

Las capturas deben salir de la app funcionando, no de mockups.

Para organizar esa etapa:

- [Guía de assets y capturas para README](C:\Users\MARCOS MOREIRA\Downloads\Pastelería\docs\presentacion\00_guia_assets_y_capturas_para_readme.md)
- [Placeholders de logo y capturas](C:\Users\MARCOS MOREIRA\Downloads\Pastelería\assets-readme)
