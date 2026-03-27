# Pastelería

Sistema integral para una pastelería artesanal con vitrina pública, operación comercial, producción, abastecimiento y soporte administrativo de punta a punta.

![Hero del proyecto](images%20readme/readme-hero.png)
_Capturas definitivas del README en `images readme/`._

## Qué demuestra este proyecto

- Una superficie pública pensada para vender: marca, catálogo y contacto.
- Un panel administrativo que recorre clientes, productos, cotizaciones, pedidos, producción, reportes y abastecimiento.
- Un backend Spring Boot modular con seguridad JWT, auditoría, reportes, notificaciones y persistencia PostgreSQL.
- Un set canónico de base de datos listo para demo local y revisión técnica.

## Stack

- Java 21 + Spring Boot 4
- PostgreSQL
- Angular para el panel administrativo
- Astro para la experiencia pública
- JWT, Flyway, Actuator y assets centralizados en backend

## Base de datos canónica

La ruta oficial de base de datos para levantar el demo completo es:

- Esquema: `db/V1/DATABASE_SCHEMA_CANONICO.sql`
- Seeds: `db/V1/DATABASE_SEED_CANONICO.sql`

El seed canónico ya cubre el sistema completo: roles, usuarios, catálogo, clientes, cotizaciones, pedidos, producción, ingredientes, insumos, proveedores, órdenes de compra, movimientos, recetas, archivos, jobs de reporte, notificaciones y auditoría.

Para este repositorio, esa es la referencia única. No hace falta mezclar SQL legacy.

## Arranque local

Atajo recomendado en Windows:

- `.\INICIAR_SISTEMA.bat`

1. Inicializa la base:
   - `.\scripts\init-db.ps1`
2. Inicia el backend:
   - `cd backend`
   - `.\scripts\start-backend-dev.cmd`
3. Inicia el admin:
   - `cd ..\frontend-admin-angular`
   - `npm start`
4. Inicia la vitrina pública:
   - `cd ..\frontend-publico-astro`
   - `npm run dev`

## Puertos esperados

- Backend: `http://localhost:8080`
- Admin: `http://localhost:4200`
- Landing pública: `http://localhost:4321`
- PostgreSQL local nativo: `localhost:5432`
- PostgreSQL vía Docker Compose: `localhost:5436`

## Acceso demo

- Usuario: `admin`
- Contraseña: `admin12345`

## Recorrido visual del frontend público

![Recorrido público](images%20readme/public-home.png)
_Las capturas públicas finales viven en `images readme/`._

| Ventana | Qué comunica en el demo | Archivo sugerido para la captura |
| --- | --- | --- |
| Landing / Home | Presenta la propuesta de valor de la marca, la estética del negocio y el punto de entrada más fuerte para convertir interés en pedido o cotización. | `images readme/public-home.png` |
| Catálogo | Expone los productos publicados con un recorrido claro para revisar oferta, categorías y productos destacados. | `images readme/public-catalogo.png` |
| Contacto | Funciona como cierre comercial: canaliza conversaciones, pedidos especiales y oportunidades de seguimiento. | `images readme/public-contacto.png` |
| Experiencia en inglés | Refuerza la percepción de producto cuidado y exportable al mostrar la misma propuesta pública en versión internacional. | `images readme/public-en.png` |

## Recorrido visual del frontend administrativo

![Recorrido administrativo](images%20readme/admin-dashboard-general.png)
_Las capturas administrativas finales viven en `images readme/`._

| Ventana | Qué comunica en el demo | Archivo sugerido para la captura |
| --- | --- | --- |
| Dashboard general | Da una lectura ejecutiva del negocio desde el primer vistazo: actividad, alertas y foco operativo. | `images readme/admin-dashboard-general.png` |
| Clientes | Muestra control de cartera, historial comercial y capacidad de seguimiento al cliente. | `images readme/admin-clientes.png` |
| Productos | Enseña cómo se administra el catálogo real que termina alimentando la vitrina pública. | `images readme/admin-productos.png` |
| Cotizaciones | Demuestra el puente entre interés comercial y venta potencial con estados y detalle de propuestas. | `images readme/admin-cotizaciones.png` |
| Pedidos | Refleja la operación viva del negocio: registro, seguimiento y priorización de pedidos. | `images readme/admin-pedidos.png` |
| Producción | Expone el corazón operativo del taller: qué está pendiente, en proceso y finalizado. | `images readme/admin-produccion.png` |
| Reportes | Sirve como evidencia de control gerencial, trazabilidad y lectura rápida del estado del negocio. | `images readme/admin-reportes.png` |
| Abastecimiento / Dashboard | Resume inventario, alertas, reposición sugerida, movimientos y proveedores activos. | `images readme/admin-abastecimiento-dashboard.png` |
| Abastecimiento / Inventario | Muestra gestión de stock, ajustes, movimientos recientes y navegación hacia compra. | `images readme/admin-abastecimiento-inventario.png` |
| Abastecimiento / Compras | Enseña creación, edición, envío y recepción de órdenes de compra con flujo operativo real. | `images readme/admin-abastecimiento-compras.png` |
| Abastecimiento / Proveedores | Refuerza la parte de abastecimiento con catálogo de proveedores y relación ítem-proveedor. | `images readme/admin-abastecimiento-proveedores.png` |
| Abastecimiento / Movimientos | Expone trazabilidad de inventario y evidencia de entradas, salidas y ajustes. | `images readme/admin-abastecimiento-movimientos.png` |

## Validación mínima antes de publicar

- Backend: `cd backend && .\mvnw.cmd test`
- Backend empaquetado: `cd backend && .\mvnw.cmd -DskipTests package`
- Admin Angular: `cd frontend-admin-angular && npm run build`
- Landing Astro: `cd frontend-publico-astro && npm run build`

## Documentación canónica

Si algún resumen o README discrepa con la documentación técnica, prevalece la documentación canónica:

1. [Índice canónico](docs/00_indice_documentacion_canonica.md)
2. [Negocio y dominio](docs/negocio/)
3. [Proyecto](docs/proyecto/)
4. [Base de datos](docs/base-datos/)
5. [Backend](docs/backend/)
6. [Frontend público Astro](docs/frontend-publico-astro/)
7. [Frontend admin Angular](docs/frontend-admin-angular/)
8. [Infraestructura](docs/infraestructura/)
9. [Operación](docs/operacion/)

## Notas operativas

- El backend sirve branding e imágenes de producto desde `backend/storage/assets/`.
- Los frontends consumen esas rutas; los assets de catálogo no viven duplicados en cada frontend.
- Para demo y revisión técnica, usa exclusivamente el esquema y seed canónicos.
