# T20 — Fiscalidad preparada y prudente

## Objetivo

Agregar una base fiscal interna para Pastelería ERP sin declarar integración productiva con SRI. La tanda prepara documentos fiscales internos asociados a documentos por cobrar o documentos de compra, validando origen único y totales.

## Alcance

- Crear módulo backend `fiscal`.
- Crear tabla V1 `documento_fiscal`.
- Crear tabla V2 `fiscal.documento_fiscal`.
- Registrar endpoints REST para consultar, preparar, emitir internamente y anular documentos fiscales.
- Validar totales mediante `ErpFinancialPolicy`.
- Registrar contratos API y enums fiscales.
- Agregar validación SQL `13_validate_fiscal_documents.sql`.

## Fuera de alcance

- Firma electrónica.
- XML autorizado.
- Envío real al SRI.
- RIDE final.
- Autorización oficial.
- Retenciones.
- Notas de crédito/débito avanzadas.
- Cambios en Angular Admin o storefront.

## Decisión clave

El estado `EMITIDO_INTERNO` solo significa que el documento quedó marcado internamente como emitido para trazabilidad administrativa. No significa autorización SRI.

## Archivos principales

- `backend/src/main/java/com/pasteleria/fiscal/`
- `backend/src/main/resources/db/migration/V1__pasteleria_base_actual.sql`
- `backend/src/main/resources/db/migration/V2__erp_pasteleria_unificado_3fn.sql`
- `backend/src/main/resources/db/validation/13_validate_fiscal_documents.sql`
- `backend/src/main/java/com/pasteleria/contratos/application/ApiContractRegistry.java`

## Validación esperada

Ejecutar:

```bat
scripts\test-backend.bat
```

## Nota de continuidad

T20 deja fiscalidad preparada. T21 podrá crear vistas semánticas y reportes combinando fiscalidad, cartera, cuentas por pagar, contabilidad y bridges.
