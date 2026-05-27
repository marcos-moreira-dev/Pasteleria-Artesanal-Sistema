\echo '== Validate fiscal documents =='

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.tables
    WHERE table_schema = 'public'
      AND table_name = 'documento_fiscal'
  ) THEN
    RAISE EXCEPTION 'No existe la tabla public.documento_fiscal';
  END IF;

  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.tables
    WHERE table_schema = 'fiscal'
      AND table_name = 'documento_fiscal'
  ) THEN
    RAISE EXCEPTION 'No existe la tabla fiscal.documento_fiscal';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM documento_fiscal
    WHERE subtotal < 0 OR impuesto < 0 OR total < 0 OR subtotal + impuesto <> total
  ) THEN
    RAISE EXCEPTION 'Hay documentos fiscales con totales invalidos';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM documento_fiscal
    WHERE NOT (
      (documento_cobrar_id IS NOT NULL AND documento_compra_id IS NULL AND origen_tipo = 'DOCUMENTO_COBRAR' AND tercero_tipo = 'CLIENTE') OR
      (documento_cobrar_id IS NULL AND documento_compra_id IS NOT NULL AND origen_tipo = 'DOCUMENTO_COMPRA' AND tercero_tipo = 'PROVEEDOR')
    )
  ) THEN
    RAISE EXCEPTION 'Hay documentos fiscales con origen o tercero incoherente';
  END IF;

  IF EXISTS (
    SELECT documento_cobrar_id
    FROM documento_fiscal
    WHERE documento_cobrar_id IS NOT NULL
    GROUP BY documento_cobrar_id
    HAVING COUNT(*) > 1
  ) THEN
    RAISE EXCEPTION 'Hay documentos por cobrar con mas de un documento fiscal';
  END IF;

  IF EXISTS (
    SELECT documento_compra_id
    FROM documento_fiscal
    WHERE documento_compra_id IS NOT NULL
    GROUP BY documento_compra_id
    HAVING COUNT(*) > 1
  ) THEN
    RAISE EXCEPTION 'Hay documentos de compra con mas de un documento fiscal';
  END IF;
END $$;
