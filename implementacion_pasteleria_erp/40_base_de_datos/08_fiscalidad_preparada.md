# Base de datos — Fiscalidad preparada

T20 agrega `documento_fiscal` en V1/public y `fiscal.documento_fiscal` en V2.

## Tabla V1

`documento_fiscal` guarda:

- tipo de comprobante;
- estado interno;
- origen único: documento por cobrar o documento de compra;
- tercero asociado;
- establecimiento, punto de emisión y secuencial;
- subtotal, impuesto y total;
- campos futuros de clave/autorización;
- auditoría básica.

## Tabla V2

`fiscal.documento_fiscal` replica el concepto ERP transicional sin desplazar aún la V1.

## Validación

`13_validate_fiscal_documents.sql` valida estructura, totales, origen único y unicidad por origen.
