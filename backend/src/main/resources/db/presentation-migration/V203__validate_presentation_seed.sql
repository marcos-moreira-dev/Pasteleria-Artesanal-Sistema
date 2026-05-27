-- =============================================================================
-- PRESENTATION VALIDATION - Coherencia mínima de demo/SIT
-- T24: valida que el seed de presentación cubra operación, ERP financiero,
-- fiscalidad interna, contabilidad e inteligencia.
-- =============================================================================

\echo '== T24 presentation seed validation =='

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM core.erp_migration_marker WHERE codigo = 'T24_PRESENTATION_SIT_SEED') THEN
    RAISE EXCEPTION 'Presentation seed inválido: falta marcador T24_PRESENTATION_SIT_SEED.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM public.usuario_sistema WHERE nombre_usuario = 'admin') THEN
    RAISE EXCEPTION 'Presentation seed inválido: falta usuario admin.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM public.producto WHERE codigo IN ('TORT-SIT-CHOCO-20', 'MESA-SIT-COFFEE-30', 'CUPK-SIT-SURTIDO-12')) THEN
    RAISE EXCEPTION 'Presentation seed inválido: faltan productos SIT.';
  END IF;

  IF (SELECT COUNT(*) FROM public.pedido WHERE codigo LIKE 'PED-SIT-%') < 3 THEN
    RAISE EXCEPTION 'Presentation seed inválido: deben existir al menos 3 pedidos SIT.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM public.produccion pr JOIN public.pedido p ON p.pedido_id = pr.pedido_id WHERE p.codigo = 'PED-SIT-002' AND pr.estado_produccion = 'DECORACION') THEN
    RAISE EXCEPTION 'Presentation seed inválido: falta producción SIT en curso.';
  END IF;

  IF (SELECT COUNT(*) FROM public.documento_cobrar WHERE codigo LIKE 'CXC-SIT-%') < 3 THEN
    RAISE EXCEPTION 'Presentation seed inválido: faltan documentos por cobrar SIT.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM public.cobranza WHERE codigo = 'COB-SIT-001') THEN
    RAISE EXCEPTION 'Presentation seed inválido: falta cobranza SIT total.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM public.documento_pagar WHERE codigo = 'CXP-SIT-001' AND estado = 'PAGADO_PARCIAL') THEN
    RAISE EXCEPTION 'Presentation seed inválido: falta cuenta por pagar SIT parcial.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM public.asiento_contable WHERE codigo = 'ASI-SIT-VTA-001' AND total_debe = total_haber) THEN
    RAISE EXCEPTION 'Presentation seed inválido: falta asiento SIT de venta cuadrado.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM public.documento_fiscal WHERE codigo = 'FIS-SIT-VTA-001' AND estado = 'EMITIDO_INTERNO') THEN
    RAISE EXCEPTION 'Presentation seed inválido: falta documento fiscal interno SIT.';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM public.asiento_contable ac
    WHERE ac.codigo LIKE 'ASI-SIT-%'
      AND ac.total_debe <> ac.total_haber
  ) THEN
    RAISE EXCEPTION 'Presentation seed inválido: hay asientos SIT descuadrados.';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM public.documento_fiscal df
    WHERE df.codigo LIKE 'FIS-SIT-%'
      AND df.subtotal + df.impuesto <> df.total
  ) THEN
    RAISE EXCEPTION 'Presentation seed inválido: hay documentos fiscales SIT con totales incoherentes.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM inteligencia.vw_semantic_dashboard_erp) THEN
    RAISE EXCEPTION 'Presentation seed inválido: dashboard semántico ERP no responde.';
  END IF;
END $$;
