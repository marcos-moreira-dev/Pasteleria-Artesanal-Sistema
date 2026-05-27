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

## Base de datos local

La ruta ejecutable actual para levantar la base local usa los SQL compactos del backend:

- `backend/src/main/resources/db/migration/V1__pasteleria_base_actual.sql`
- `backend/src/main/resources/db/migration/V2__erp_pasteleria_unificado_3fn.sql`
- `backend/src/main/resources/db/migration/R__pasteleria_reporting_views.sql`
- `backend/src/main/resources/db/migration/R__pasteleria_semantic_views.sql`

Para presentación/SIT se agregan además los seeds de `backend/src/main/resources/db/presentation-migration/` (`V200` a `V203`).

No mezclar SQL legacy ni activar Flyway sobre una base creada por los scripts locales.

## Arranque local

Atajo recomendado en Windows:

- `.\INICIAR_SISTEMA.bat`

Comando explícito equivalente:

- `.\scripts\dev.bat`

Ese comando levanta PostgreSQL por Docker, inicializa la base local actual, abre el backend, el admin Angular y la vitrina pública Astro.

Para una presentación local/SIT con storage separado:

- `.\scripts\pasteleria-demo.bat`

Los scripts antiguos con sufijo `-dev` se conservan por compatibilidad, pero la superficie recomendada es `scripts\dev.bat`, `scripts\pasteleria-demo.bat`, `scripts\test-backend.bat`, `scripts\test-admin.bat` y `scripts\test-storefront.bat`.

## Puertos esperados

- Backend: `http://localhost:8080`
- Admin: `http://localhost:4200`
- Landing pública: `http://localhost:4321`
- PostgreSQL canónico vía Docker Compose: `localhost:5436`
- PostgreSQL local nativo alternativo: `localhost:5432`

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

Comandos recomendados desde la raíz:

- Backend: `.\scripts\test-backend.bat`
- Admin Angular: `.\scripts\test-admin.bat`
- Landing Astro: `.\scripts\test-storefront.bat`
- Validación heredada por etapas: `.\scripts\validate-all.bat`

## Documentación canónica

La carpeta `implementacion_pasteleria_erp/` concentra las tandas de implementación y las reglas de evolución hacia ERP. Ahí se documenta que la UX/UI actual de la pastelería se respeta y que Cedro se usa como canon de ingeniería, no como estética visual.

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


## T25 — Auditoría, soporte y evidencia

El backend incluye endpoints de auditoría y soporte para consultar eventos, evidencia técnica-operativa y checklist de entrega antes del cierre pre-GitHub.


## Scripts finales de uso local

La carpeta `scripts/` contiene únicamente puntos de entrada `.bat`:

```bat
scripts\test-backend.bat
scripts\test-admin.bat
scripts\test-storefront.bat
scripts\test-all.bat
scripts\run-production.bat
scripts\run-demo.bat
scripts\stop-local.bat
```

`run-production.bat` levanta el sistema completo en modo operativo local sin datos inventados. `run-demo.bat` recrea la base local, carga registros inventados de presentación/SIT y levanta backend, Admin Angular y storefront.


## T26-HF1 — Corrección de scripts frontend npm/call

Se corrigieron `scripts/test-admin.bat` y `scripts/test-storefront.bat` para invocar `npm -v` mediante `call npm -v`, evitando el error de etiqueta `Run` en Windows. La carpeta `scripts/` conserva solo entradas `.bat` humanas.
