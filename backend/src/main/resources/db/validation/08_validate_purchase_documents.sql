\echo '== Validate purchase documents and accounts payable =='

DO $$
BEGIN
  IF EXISTS (
    SELECT 1
    FROM documento_compra dc
    WHERE dc.total <= 0 OR dc.subtotal < 0 OR dc.impuesto < 0
  ) THEN
    RAISE EXCEPTION 'Hay documentos de compra con totales invalidos';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM documento_pagar dp
    WHERE dp.saldo < 0 OR dp.saldo > dp.total
  ) THEN
    RAISE EXCEPTION 'Hay documentos por pagar con saldo invalido';
  END IF;

  IF EXISTS (
    SELECT documento_compra_id
    FROM documento_pagar
    GROUP BY documento_compra_id
    HAVING COUNT(*) > 1
  ) THEN
    RAISE EXCEPTION 'Hay documentos de compra con mas de una cuenta por pagar asociada';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM documento_compra dc
    WHERE dc.estado = 'REGISTRADO'
      AND NOT EXISTS (
        SELECT 1
        FROM documento_pagar dp
        WHERE dp.documento_compra_id = dc.documento_compra_id
      )
  ) THEN
    RAISE EXCEPTION 'Hay documentos de compra registrados sin cuenta por pagar';
  END IF;
END $$;
