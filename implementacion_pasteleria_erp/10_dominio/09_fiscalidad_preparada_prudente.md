# Fiscalidad preparada y prudente

La fiscalidad de Pastelería ERP se implementa como una capa interna de preparación y trazabilidad. No se afirma cumplimiento tributario productivo ni integración final con el SRI.

## Conceptos

- **Documento fiscal interno**: representación administrativa de un comprobante asociado a una venta/cobro o compra.
- **Origen fiscal**: exactamente un origen por documento: `DOCUMENTO_COBRAR` o `DOCUMENTO_COMPRA`.
- **Estado fiscal interno**: `BORRADOR`, `EMITIDO_INTERNO`, `ANULADO`.
- **Campos preparados**: clave de acceso, número de autorización y fecha de autorización existen como campos futuros, pero no se llenan automáticamente en T20.

## Reglas

1. Un documento fiscal interno nace desde un solo origen.
2. El total fiscal debe ser igual a subtotal más impuesto.
3. Un documento por cobrar no puede tener más de un documento fiscal interno.
4. Un documento de compra no puede tener más de un documento fiscal interno.
5. `EMITIDO_INTERNO` no equivale a autorización SRI.
