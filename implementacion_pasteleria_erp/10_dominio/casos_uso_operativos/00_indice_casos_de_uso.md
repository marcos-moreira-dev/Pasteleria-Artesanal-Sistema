# Índice de casos de uso operativos

Esta carpeta concentra la documentación canónica de la Guía Operativa de Pastelería ERP.

La fuente ejecutable actual está en:

- `backend/src/main/resources/db/migration/V13__guia_operativa.sql`
- `backend/src/main/resources/db/migration/V14__contenido_operativo_final.sql`
- `db/V1/DATABASE_SEED_CANONICO.sql`

El backend expone:

- `GET /api/v1/casos-uso`
- `GET /api/v1/casos-uso/hub`
- `GET /api/v1/casos-uso/{codigo}`
- `GET /api/v1/casos-uso/manual.pdf`

## Regla

La Guía Operativa no debe ser documentación suelta. Debe existir en:

1. base de datos;
2. backend;
3. frontend;
4. PDF;
5. matriz canónica;
6. Markdown de referencia.

## Archivos de esta carpeta

- `01_convencion_nombres_casos_de_uso.md`
- `02_matriz_canonica.csv`
- ejemplos representativos `CU-GO-*.md`

## Pendiente futuro

Cuando se implemente V2 ERP, se deben agregar casos para:

- caja operativa;
- cartera;
- cuentas por pagar;
- tesorería;
- contabilidad;
- fiscalidad preparada;
- producción técnica con consumos;
- auditoría y soporte.
