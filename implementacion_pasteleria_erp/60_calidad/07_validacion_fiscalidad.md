# Calidad — Validación fiscalidad

T20 agrega una validación SQL específica para fiscalidad:

- existencia de `public.documento_fiscal`;
- existencia de `fiscal.documento_fiscal`;
- totales fiscales coherentes;
- origen fiscal único;
- máximo un documento fiscal por documento por cobrar;
- máximo un documento fiscal por documento de compra.

La validación se ejecuta desde `BackendMigrationIntegrationTest` junto con el resto de scripts SQL.
