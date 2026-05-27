# T06 — Guía Operativa / Casos de uso

## 1. Objetivo

Fortalecer el módulo de Guía Operativa usando Cedro como canon de ingeniería: el catálogo de casos debe vivir en base de datos, exponerse por API, verse en Angular con la UX/UI actual de la pastelería y poder descargarse como manual PDF.

Esta tanda no cambia la estética de la aplicación. Solo agrega capacidad funcional y refactor transversal razonable.

## 2. Contexto heredado

Antes de T06 ya existían:

- tablas `caso_uso_modulo`, `caso_uso_operativo` y `paso_caso_uso`;
- endpoint `GET /api/v1/casos-uso`;
- endpoint `GET /api/v1/casos-uso/hub`;
- endpoint `GET /api/v1/casos-uso/{codigo}`;
- pantalla Angular `Guía operativa`;
- contratos API iniciales;
- storage y PDF base con fondo blanco desde T05.

Cedro mostró que la guía operativa debe ser un producto vivo, no Markdown suelto: SQL + backend + Angular + PDF + tests + matriz canónica.

## 3. Fuente Cedro usada como referencia

Se rescata de Cedro:

- hub de casos por módulo;
- detalle por código;
- manual PDF descargable;
- matriz canónica auditable;
- documentación de convención de nombres;
- tests que protegen PDF y contratos.

No se copia:

- estética visual de Cedro;
- nombres restaurante;
- `cocina` como módulo visible;
- rutas de restaurante;
- colores o assets Cedro.

## 4. Estado actual de Pastelería

El módulo ya era funcional, pero faltaba:

- manual PDF descargable desde backend;
- botón de descarga desde Angular;
- contrato API para `/api/v1/casos-uso/manual.pdf`;
- documentación canónica dentro de `implementacion_pasteleria_erp/`;
- matriz CSV de casos de uso actuales;
- test unitario de generación PDF.

## 5. Alcance

Esta tanda hace:

1. Crear `CasoUsoManualPdfDocumentService`.
2. Agregar `GET /api/v1/casos-uso/manual.pdf`.
3. Registrar el endpoint en `ApiContractRegistry`.
4. Agregar `downloadGuiaOperativaManual()` al cliente Angular.
5. Agregar botón `Descargar manual PDF` en la pantalla de Guía Operativa, respetando la UX/UI actual.
6. Crear documentación canónica en `implementacion_pasteleria_erp/10_dominio/casos_uso_operativos/`.
7. Crear matriz CSV canónica desde los seeds actuales.
8. Agregar test unitario de generación de PDF.

## 6. Fuera de alcance

No se hace todavía:

- rediseño visual de la pantalla;
- edición de casos desde UI;
- permisos reales por endpoint;
- auditoría de descarga del manual;
- storage persistente del PDF como `archivo_recurso`;
- migración V2 ERP;
- nuevos casos ERP completos de caja/contabilidad/fiscalidad.

Eso queda para tandas posteriores.

## 7. Archivos leídos antes de modificar

- `backend/src/main/java/com/pasteleria/casosuso/api/CasoUsoOperativoController.java`
- `backend/src/main/java/com/pasteleria/casosuso/application/CasoUsoOperativoService.java`
- `backend/src/main/java/com/pasteleria/casosuso/api/dto/*.java`
- `backend/src/main/resources/db/migration/V13__guia_operativa.sql`
- `backend/src/main/resources/db/migration/V14__contenido_operativo_final.sql`
- `frontend-admin-angular/src/app/features/guia-operativa/guia-operativa-page.component.ts`
- `frontend-admin-angular/src/app/core/api/api-client.service.ts`
- `backend/src/main/java/com/pasteleria/contratos/application/ApiContractRegistry.java`

## 8. Cambios realizados

### Backend

Se creó:

- `CasoUsoManualPdfDocumentService`

Se actualizó:

- `CasoUsoOperativoController`
- `ApiContractRegistry`

Nuevo endpoint:

```http
GET /api/v1/casos-uso/manual.pdf
```

Devuelve un PDF generado en caliente desde `service.hub()`.

### Frontend Angular

Se actualizó:

- `ApiClientService`
- `GuiaOperativaPageComponent`

Nuevo método:

```ts
downloadGuiaOperativaManual(): Observable<Blob>
```

La pantalla ahora permite descargar el manual sin cambiar su estética base.

### Documentación

Se creó:

- `10_dominio/casos_uso_operativos/00_indice_casos_de_uso.md`
- `10_dominio/casos_uso_operativos/01_convencion_nombres_casos_de_uso.md`
- `10_dominio/casos_uso_operativos/02_matriz_canonica.csv`
- `10_dominio/casos_uso_operativos/CU-GO-001_revisar_resumen_dia.md`
- `10_dominio/casos_uso_operativos/CU-GO-040_registrar_pedido.md`
- `10_dominio/casos_uso_operativos/CU-GO-050_revisar_cola_produccion.md`

### Tests

Se creó:

- `CasoUsoManualPdfDocumentServiceTest`

Y se reforzó:

- `ApiContractRegistryTest`

## 9. Riesgos

- No se pudo compilar en este entorno porque Maven no está disponible y el wrapper necesita descargar Maven.
- El PDF se genera en caliente; si el catálogo crece mucho, en el futuro puede convenir generarlo como `archivo_recurso` cacheado.
- La matriz CSV refleja los seeds actuales; cuando V2 tenga nuevos casos ERP, debe regenerarse o actualizarse.

## 10. Criterios de aceptación

La tanda queda aceptada si:

- `GET /api/v1/casos-uso/manual.pdf` descarga PDF;
- la pantalla de Guía Operativa muestra botón de descarga;
- el contrato API declara el endpoint;
- el PDF inicia con `%PDF` en test unitario;
- la UX/UI actual no fue reemplazada;
- todos los `.md` nuevos están dentro de `implementacion_pasteleria_erp/`.

## 11. Pruebas mínimas

En Windows:

```bat
scripts\test-backend.bat
scripts\test-admin.bat
```

Prueba manual:

```http
GET http://localhost:8080/api/v1/casos-uso/manual.pdf
```

Y en Angular:

```text
Guía operativa → Descargar manual PDF
```

## 12. Notas para la siguiente tanda

La siguiente tanda es T07 — Base de datos V1/V2 y migraciones por ambiente.

Antes de esa tanda conviene leer:

- `db/V1/DATABASE_SCHEMA_CANONICO.sql`
- `db/V1/DATABASE_SEED_CANONICO.sql`
- `backend/src/main/resources/db/migration/`
- `implementacion_pasteleria_erp/40_base_de_datos/01_estrategia_v1_v2.md`
