\echo '== Validate presentation/SIT seed when enabled =='

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM core.erp_migration_marker WHERE codigo = 'T24_PRESENTATION_SIT_SEED') THEN
    RAISE NOTICE 'Validation skipped: T24 presentation seed marker not present.';
    RETURN;
  END IF;

  IF (SELECT COUNT(*) FROM public.pedido WHERE codigo LIKE 'PED-SIT-%') < 3 THEN
    RAISE EXCEPTION 'SIT inválido: faltan pedidos de presentación.';
  END IF;

  IF (SELECT COUNT(*) FROM public.documento_cobrar WHERE codigo LIKE 'CXC-SIT-%') < 3 THEN
    RAISE EXCEPTION 'SIT inválido: faltan documentos por cobrar de presentación.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM public.documento_pagar WHERE codigo = 'CXP-SIT-001') THEN
    RAISE EXCEPTION 'SIT inválido: falta cuenta por pagar de presentación.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM public.asiento_contable WHERE codigo LIKE 'ASI-SIT-%' AND estado = 'REGISTRADO') THEN
    RAISE EXCEPTION 'SIT inválido: faltan asientos contables de presentación.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM public.documento_fiscal WHERE codigo LIKE 'FIS-SIT-%' AND estado = 'EMITIDO_INTERNO') THEN
    RAISE EXCEPTION 'SIT inválido: faltan documentos fiscales internos de presentación.';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM public.asiento_contable ac
    WHERE ac.codigo LIKE 'ASI-SIT-%'
      AND ac.total_debe <> ac.total_haber
  ) THEN
    RAISE EXCEPTION 'SIT inválido: hay asientos de presentación descuadrados.';
  END IF;
END $$;
