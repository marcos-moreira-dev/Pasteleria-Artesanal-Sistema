# Calidad — validación de inteligencia/reporting

T21 agrega `14_validate_intelligence_reporting.sql`.

La validación comprueba:

- existencia de vistas semánticas críticas,
- que las vistas puedan consultarse,
- que el dashboard ERP no exponga métricas negativas incoherentes.

También se amplía `BackendMigrationIntegrationTest` y el smoke API para cubrir `/api/v1/inteligencia/dashboard`.
