\echo '== Validate ERP bridges and accounting origins =='

DO $$
BEGIN
  IF EXISTS (
    SELECT origen_tipo, origen_id
    FROM asiento_contable
    WHERE origen_tipo IS NOT NULL
      AND origen_id IS NOT NULL
    GROUP BY origen_tipo, origen_id
    HAVING COUNT(*) > 1
  ) THEN
    RAISE EXCEPTION 'Hay asientos contables duplicados para un mismo origen ERP';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM asiento_contable
    WHERE origen_tipo IN ('DOCUMENTO_COBRAR', 'COBRANZA', 'DOCUMENTO_PAGAR', 'PAGO_PROVEEDOR')
      AND (total_debe <= 0 OR total_haber <= 0 OR total_debe <> total_haber)
  ) THEN
    RAISE EXCEPTION 'Hay asientos bridge ERP sin partida doble valida';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM asiento_contable a
    WHERE a.origen_tipo = 'DOCUMENTO_COBRAR'
      AND NOT EXISTS (
        SELECT 1 FROM documento_cobrar d WHERE d.documento_cobrar_id::TEXT = a.origen_id
      )
  ) THEN
    RAISE EXCEPTION 'Hay asientos de venta con origen documento_cobrar inexistente';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM asiento_contable a
    WHERE a.origen_tipo = 'COBRANZA'
      AND NOT EXISTS (
        SELECT 1 FROM cobranza c WHERE c.cobranza_id::TEXT = a.origen_id
      )
  ) THEN
    RAISE EXCEPTION 'Hay asientos de cobro con origen cobranza inexistente';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM asiento_contable a
    WHERE a.origen_tipo = 'DOCUMENTO_PAGAR'
      AND NOT EXISTS (
        SELECT 1 FROM documento_pagar d WHERE d.documento_pagar_id::TEXT = a.origen_id
      )
  ) THEN
    RAISE EXCEPTION 'Hay asientos de compra con origen documento_pagar inexistente';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM asiento_contable a
    WHERE a.origen_tipo = 'PAGO_PROVEEDOR'
      AND NOT EXISTS (
        SELECT 1 FROM pago_proveedor p WHERE p.pago_proveedor_id::TEXT = a.origen_id
      )
  ) THEN
    RAISE EXCEPTION 'Hay asientos de pago proveedor con origen pago_proveedor inexistente';
  END IF;
END $$;
