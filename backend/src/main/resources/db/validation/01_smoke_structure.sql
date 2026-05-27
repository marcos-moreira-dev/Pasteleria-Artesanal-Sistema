DO $$
DECLARE
  missing_count integer;
BEGIN
  SELECT COUNT(*) INTO missing_count
  FROM (VALUES
    ('rol_usuario'), ('usuario_sistema'), ('producto'), ('cliente'), ('pedido'),
    ('produccion'), ('archivo_recurso'), ('job_reporte'), ('caso_uso_modulo'),
    ('caso_uso_operativo'), ('paso_caso_uso'), ('caja_operativa'),
    ('turno_caja'), ('movimiento_caja'), ('arqueo_caja'), ('documento_compra'), ('documento_pagar'),
    ('documento_cobrar'), ('cobranza'), ('cobranza_detalle'), ('pago_proveedor'), ('pago_proveedor_aplicacion'),
    ('tercero'), ('cliente_perfil'), ('proveedor_perfil'),
    ('tipo_diario_contable'), ('cuenta_contable'), ('asiento_contable'), ('asiento_contable_detalle'), ('documento_fiscal')
  ) AS expected(table_name)
  WHERE NOT EXISTS (
    SELECT 1 FROM information_schema.tables t
    WHERE t.table_schema = 'public' AND t.table_name = expected.table_name
  );

  IF missing_count > 0 THEN
    RAISE EXCEPTION 'Faltan tablas V1 críticas: %', missing_count;
  END IF;

  SELECT COUNT(*) INTO missing_count
  FROM (VALUES
    ('core'), ('seguridad'), ('terceros'), ('inventario'), ('comercial'),
    ('compras'), ('produccion'), ('cartera'), ('tesoreria'), ('fiscal'),
    ('contabilidad'), ('inteligencia'), ('staging'), ('auditoria'), ('ayuda')
  ) AS expected(schema_name)
  WHERE NOT EXISTS (
    SELECT 1 FROM information_schema.schemata s
    WHERE s.schema_name = expected.schema_name
  );

  IF missing_count > 0 THEN
    RAISE EXCEPTION 'Faltan schemas V2 ERP: %', missing_count;
  END IF;

  SELECT COUNT(*) INTO missing_count
  FROM (VALUES
    ('core','organizacion_empresa'),
    ('core','sucursal_operativa'),
    ('core','legacy_objeto_mapeo'),
    ('seguridad','permiso_operacion'),
    ('seguridad','rol_operacion'),
    ('seguridad','rol_permiso'),
    ('tesoreria','caja_operativa'),
    ('tesoreria','turno_caja'),
    ('tesoreria','movimiento_caja'),
    ('tesoreria','arqueo_caja'),
    ('compras','documento_compra'),
    ('cartera','documento_pagar'),
    ('cartera','documento_cobrar'),
    ('cartera','cobranza'),
    ('cartera','cobranza_detalle'),
    ('cartera','pago_proveedor'),
    ('cartera','pago_proveedor_aplicacion'),
    ('terceros','tercero'),
    ('terceros','cliente_perfil'),
    ('terceros','proveedor_perfil'),
    ('terceros','empleado_perfil'),
    ('contabilidad','tipo_diario_contable'),
    ('contabilidad','cuenta_contable'),
    ('contabilidad','asiento_contable'),
    ('contabilidad','asiento_contable_detalle'),
    ('fiscal','documento_fiscal')
  ) AS expected(schema_name, table_name)
  WHERE NOT EXISTS (
    SELECT 1 FROM information_schema.tables t
    WHERE t.table_schema = expected.schema_name AND t.table_name = expected.table_name
  );

  IF missing_count > 0 THEN
    RAISE EXCEPTION 'Faltan tablas de seguridad/control V2: %', missing_count;
  END IF;
  SELECT COUNT(*) INTO missing_count
  FROM (VALUES
    ('inteligencia','vw_semantic_dashboard_erp'),
    ('inteligencia','vw_semantic_cartera_documentos'),
    ('inteligencia','vw_semantic_cuentas_pagar_documentos'),
    ('inteligencia','vw_semantic_caja_movimientos'),
    ('inteligencia','vw_semantic_contabilidad_asientos'),
    ('inteligencia','vw_semantic_fiscal_documentos'),
    ('inteligencia','vw_semantic_stock_actual')
  ) AS expected(schema_name, view_name)
  WHERE NOT EXISTS (
    SELECT 1 FROM information_schema.views v
    WHERE v.table_schema = expected.schema_name AND v.table_name = expected.view_name
  );

  IF missing_count > 0 THEN
    RAISE EXCEPTION 'Faltan vistas semanticas de inteligencia: %', missing_count;
  END IF;
END $$;
