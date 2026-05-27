\echo '== Validate intelligence and semantic reporting views =='

DO $$
DECLARE
  missing_count integer;
BEGIN
  SELECT COUNT(*) INTO missing_count
  FROM (VALUES
    ('inteligencia', 'vw_semantic_dashboard_erp'),
    ('inteligencia', 'vw_semantic_cartera_documentos'),
    ('inteligencia', 'vw_semantic_cuentas_pagar_documentos'),
    ('inteligencia', 'vw_semantic_caja_movimientos'),
    ('inteligencia', 'vw_semantic_contabilidad_asientos'),
    ('inteligencia', 'vw_semantic_fiscal_documentos'),
    ('inteligencia', 'vw_semantic_stock_actual'),
    ('inteligencia', 'vw_semantic_ventas_operativas'),
    ('inteligencia', 'vw_semantic_produccion_pendiente')
  ) AS expected(schema_name, view_name)
  WHERE NOT EXISTS (
    SELECT 1
    FROM information_schema.views v
    WHERE v.table_schema = expected.schema_name
      AND v.table_name = expected.view_name
  );

  IF missing_count > 0 THEN
    RAISE EXCEPTION 'Faltan vistas semanticas de inteligencia/reportes: %', missing_count;
  END IF;

  PERFORM 1 FROM inteligencia.vw_semantic_dashboard_erp;
  PERFORM 1 FROM inteligencia.vw_semantic_cartera_documentos LIMIT 1;
  PERFORM 1 FROM inteligencia.vw_semantic_cuentas_pagar_documentos LIMIT 1;
  PERFORM 1 FROM inteligencia.vw_semantic_caja_movimientos LIMIT 1;
  PERFORM 1 FROM inteligencia.vw_semantic_contabilidad_asientos LIMIT 1;
  PERFORM 1 FROM inteligencia.vw_semantic_fiscal_documentos LIMIT 1;
  PERFORM 1 FROM inteligencia.vw_semantic_stock_actual LIMIT 1;

  IF EXISTS (
    SELECT 1
    FROM inteligencia.vw_semantic_dashboard_erp
    WHERE saldo_cartera < 0
       OR saldo_cuentas_pagar < 0
       OR saldo_caja < 0
       OR pedidos_activos < 0
       OR producciones_activas < 0
       OR documentos_cobrar_abiertos < 0
       OR documentos_pagar_abiertos < 0
       OR asientos_registrados < 0
       OR documentos_fiscales_borrador < 0
       OR items_stock_bajo < 0
  ) THEN
    RAISE EXCEPTION 'El dashboard ERP expone metricas negativas incoherentes';
  END IF;
END $$;
