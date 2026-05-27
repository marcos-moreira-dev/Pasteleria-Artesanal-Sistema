-- =============================================================================
-- PRESENTATION SEED - ERP financiero
-- T24: cartera, cobranzas, pagos, contabilidad y fiscalidad interna para demo/SIT.
-- No usar contra datos reales.
-- =============================================================================

\echo '== T24 presentation seed: ERP finance =='

-- Documentos por cobrar.
INSERT INTO documento_cobrar (cliente_id, pedido_id, codigo, estado, fecha_emision, fecha_vencimiento, total, saldo, observaciones, created_at, updated_at, version)
SELECT p.cliente_id, p.pedido_id, 'CXC-SIT-001', 'PAGADO', NOW() - INTERVAL '2 day', NOW() + INTERVAL '5 day', 96.00, 0.00, 'Documento SIT totalmente cobrado.', NOW(), NOW(), 0
FROM pedido p
WHERE p.codigo = 'PED-SIT-001'
  AND NOT EXISTS (SELECT 1 FROM documento_cobrar WHERE codigo = 'CXC-SIT-001');

INSERT INTO documento_cobrar (cliente_id, pedido_id, codigo, estado, fecha_emision, fecha_vencimiento, total, saldo, observaciones, created_at, updated_at, version)
SELECT p.cliente_id, p.pedido_id, 'CXC-SIT-002', 'PARCIAL', NOW() - INTERVAL '1 day', NOW() + INTERVAL '6 day', 150.00, 90.00, 'Documento SIT parcialmente cobrado.', NOW(), NOW(), 0
FROM pedido p
WHERE p.codigo = 'PED-SIT-002'
  AND NOT EXISTS (SELECT 1 FROM documento_cobrar WHERE codigo = 'CXC-SIT-002');

INSERT INTO documento_cobrar (cliente_id, pedido_id, codigo, estado, fecha_emision, fecha_vencimiento, total, saldo, observaciones, created_at, updated_at, version)
SELECT p.cliente_id, p.pedido_id, 'CXC-SIT-003', 'PENDIENTE', NOW(), NOW() + INTERVAL '10 day', 72.00, 72.00, 'Documento SIT pendiente para dashboard.', NOW(), NOW(), 0
FROM pedido p
WHERE p.codigo = 'PED-SIT-003'
  AND NOT EXISTS (SELECT 1 FROM documento_cobrar WHERE codigo = 'CXC-SIT-003');

-- Cobranzas y aplicaciones.
INSERT INTO cobranza (cliente_id, codigo, fecha_cobranza, monto_total, medio_pago, referencia_pago, estado, observaciones, created_at, updated_at, version)
SELECT dc.cliente_id, 'COB-SIT-001', NOW() - INTERVAL '1 day', 96.00, 'EFECTIVO', 'REC-SIT-001', 'REGISTRADA', 'Cobranza SIT total.', NOW(), NOW(), 0
FROM documento_cobrar dc
WHERE dc.codigo = 'CXC-SIT-001'
  AND NOT EXISTS (SELECT 1 FROM cobranza WHERE codigo = 'COB-SIT-001');

INSERT INTO cobranza_detalle (cobranza_id, documento_cobrar_id, monto_aplicado, saldo_anterior, saldo_posterior, created_at)
SELECT c.cobranza_id, dc.documento_cobrar_id, 96.00, 96.00, 0.00, NOW()
FROM cobranza c, documento_cobrar dc
WHERE c.codigo = 'COB-SIT-001' AND dc.codigo = 'CXC-SIT-001'
  AND NOT EXISTS (SELECT 1 FROM cobranza_detalle d WHERE d.cobranza_id = c.cobranza_id AND d.documento_cobrar_id = dc.documento_cobrar_id);

INSERT INTO cobranza (cliente_id, codigo, fecha_cobranza, monto_total, medio_pago, referencia_pago, estado, observaciones, created_at, updated_at, version)
SELECT dc.cliente_id, 'COB-SIT-002', NOW(), 60.00, 'TRANSFERENCIA', 'TRF-SIT-002', 'REGISTRADA', 'Cobranza SIT parcial.', NOW(), NOW(), 0
FROM documento_cobrar dc
WHERE dc.codigo = 'CXC-SIT-002'
  AND NOT EXISTS (SELECT 1 FROM cobranza WHERE codigo = 'COB-SIT-002');

INSERT INTO cobranza_detalle (cobranza_id, documento_cobrar_id, monto_aplicado, saldo_anterior, saldo_posterior, created_at)
SELECT c.cobranza_id, dc.documento_cobrar_id, 60.00, 150.00, 90.00, NOW()
FROM cobranza c, documento_cobrar dc
WHERE c.codigo = 'COB-SIT-002' AND dc.codigo = 'CXC-SIT-002'
  AND NOT EXISTS (SELECT 1 FROM cobranza_detalle d WHERE d.cobranza_id = c.cobranza_id AND d.documento_cobrar_id = dc.documento_cobrar_id);

-- Pago proveedor parcial y aplicación.
INSERT INTO pago_proveedor (proveedor_id, codigo, fecha_pago, monto_total, medio_pago, referencia_pago, estado, observaciones, created_at, updated_at, version)
SELECT dp.proveedor_id, 'PAG-SIT-001', NOW(), 50.00, 'EFECTIVO', 'PAG-SIT-REF-001', 'REGISTRADO', 'Pago proveedor SIT parcial.', NOW(), NOW(), 0
FROM documento_pagar dp
WHERE dp.codigo = 'CXP-SIT-001'
  AND NOT EXISTS (SELECT 1 FROM pago_proveedor WHERE codigo = 'PAG-SIT-001');

INSERT INTO pago_proveedor_aplicacion (pago_proveedor_id, documento_pagar_id, monto_aplicado, saldo_anterior, saldo_posterior, created_at)
SELECT pp.pago_proveedor_id, dp.documento_pagar_id, 50.00, 120.00, 70.00, NOW()
FROM pago_proveedor pp, documento_pagar dp
WHERE pp.codigo = 'PAG-SIT-001' AND dp.codigo = 'CXP-SIT-001'
  AND NOT EXISTS (SELECT 1 FROM pago_proveedor_aplicacion a WHERE a.pago_proveedor_id = pp.pago_proveedor_id AND a.documento_pagar_id = dp.documento_pagar_id);

-- Movimiento de caja asociado a cobranzas y pago proveedor.
INSERT INTO movimiento_caja (turno_caja_id, tipo_movimiento, naturaleza, fecha_movimiento, monto, moneda, referencia_tipo, referencia_id, descripcion, estado, creado_por_usuario_id)
SELECT t.turno_caja_id, 'COBRO_CLIENTE', 'ENTRADA', NOW() - INTERVAL '1 day', 96.00, 'USD', 'COBRANZA', c.cobranza_id::TEXT, 'Cobro SIT total a cliente.', 'REGISTRADO', u.usuario_id
FROM turno_caja t, cobranza c, usuario_sistema u
WHERE t.observaciones_apertura = 'Turno SIT abierto para validación.' AND c.codigo = 'COB-SIT-001' AND u.nombre_usuario = 'admin'
  AND NOT EXISTS (SELECT 1 FROM movimiento_caja m WHERE m.referencia_tipo = 'COBRANZA' AND m.referencia_id = c.cobranza_id::TEXT);

INSERT INTO movimiento_caja (turno_caja_id, tipo_movimiento, naturaleza, fecha_movimiento, monto, moneda, referencia_tipo, referencia_id, descripcion, estado, creado_por_usuario_id)
SELECT t.turno_caja_id, 'PAGO_PROVEEDOR', 'SALIDA', NOW() - INTERVAL '2 hour', 50.00, 'USD', 'PAGO_PROVEEDOR', pp.pago_proveedor_id::TEXT, 'Pago SIT parcial a proveedor.', 'REGISTRADO', u.usuario_id
FROM turno_caja t, pago_proveedor pp, usuario_sistema u
WHERE t.observaciones_apertura = 'Turno SIT abierto para validación.' AND pp.codigo = 'PAG-SIT-001' AND u.nombre_usuario = 'admin'
  AND NOT EXISTS (SELECT 1 FROM movimiento_caja m WHERE m.referencia_tipo = 'PAGO_PROVEEDOR' AND m.referencia_id = pp.pago_proveedor_id::TEXT);

-- Asientos contables de presentación.
DO $$
DECLARE
  v_diario_ventas BIGINT;
  v_diario_compras BIGINT;
  v_diario_caja BIGINT;
  v_caja BIGINT;
  v_clientes BIGINT;
  v_ventas BIGINT;
  v_proveedores BIGINT;
  v_costo BIGINT;
  v_asiento BIGINT;
  v_origen_id TEXT;
BEGIN
  SELECT tipo_diario_contable_id INTO v_diario_ventas FROM tipo_diario_contable WHERE codigo = 'VENTAS';
  SELECT tipo_diario_contable_id INTO v_diario_compras FROM tipo_diario_contable WHERE codigo = 'COMPRAS';
  SELECT tipo_diario_contable_id INTO v_diario_caja FROM tipo_diario_contable WHERE codigo = 'CAJA';
  SELECT cuenta_contable_id INTO v_caja FROM cuenta_contable WHERE codigo = '1.1.01';
  SELECT cuenta_contable_id INTO v_clientes FROM cuenta_contable WHERE codigo = '1.2.01';
  SELECT cuenta_contable_id INTO v_proveedores FROM cuenta_contable WHERE codigo = '2.1.01';
  SELECT cuenta_contable_id INTO v_ventas FROM cuenta_contable WHERE codigo = '4.1.01';
  SELECT cuenta_contable_id INTO v_costo FROM cuenta_contable WHERE codigo = '5.1.01';

  SELECT documento_cobrar_id::TEXT INTO v_origen_id FROM documento_cobrar WHERE codigo = 'CXC-SIT-001';
  IF v_origen_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM asiento_contable WHERE origen_tipo = 'DOCUMENTO_COBRAR' AND origen_id = v_origen_id) THEN
    INSERT INTO asiento_contable (tipo_diario_contable_id, codigo, fecha_asiento, descripcion, origen_tipo, origen_id, estado, total_debe, total_haber, created_at, updated_at, version)
    VALUES (v_diario_ventas, 'ASI-SIT-VTA-001', NOW() - INTERVAL '2 day', 'Asiento SIT por documento por cobrar.', 'DOCUMENTO_COBRAR', v_origen_id, 'REGISTRADO', 96.00, 96.00, NOW(), NOW(), 0)
    RETURNING asiento_contable_id INTO v_asiento;
    INSERT INTO asiento_contable_detalle (asiento_contable_id, cuenta_contable_id, descripcion, debe, haber, created_at) VALUES
    (v_asiento, v_clientes, 'Clientes nacionales por venta SIT.', 96.00, 0.00, NOW()),
    (v_asiento, v_ventas, 'Ventas de pastelería SIT.', 0.00, 96.00, NOW());
  END IF;

  SELECT cobranza_id::TEXT INTO v_origen_id FROM cobranza WHERE codigo = 'COB-SIT-001';
  IF v_origen_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM asiento_contable WHERE origen_tipo = 'COBRANZA' AND origen_id = v_origen_id) THEN
    INSERT INTO asiento_contable (tipo_diario_contable_id, codigo, fecha_asiento, descripcion, origen_tipo, origen_id, estado, total_debe, total_haber, created_at, updated_at, version)
    VALUES (v_diario_caja, 'ASI-SIT-COB-001', NOW() - INTERVAL '1 day', 'Asiento SIT por cobranza cliente.', 'COBRANZA', v_origen_id, 'REGISTRADO', 96.00, 96.00, NOW(), NOW(), 0)
    RETURNING asiento_contable_id INTO v_asiento;
    INSERT INTO asiento_contable_detalle (asiento_contable_id, cuenta_contable_id, descripcion, debe, haber, created_at) VALUES
    (v_asiento, v_caja, 'Ingreso de caja por cobranza SIT.', 96.00, 0.00, NOW()),
    (v_asiento, v_clientes, 'Cierre de saldo cliente SIT.', 0.00, 96.00, NOW());
  END IF;

  SELECT documento_pagar_id::TEXT INTO v_origen_id FROM documento_pagar WHERE codigo = 'CXP-SIT-001';
  IF v_origen_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM asiento_contable WHERE origen_tipo = 'DOCUMENTO_PAGAR' AND origen_id = v_origen_id) THEN
    INSERT INTO asiento_contable (tipo_diario_contable_id, codigo, fecha_asiento, descripcion, origen_tipo, origen_id, estado, total_debe, total_haber, created_at, updated_at, version)
    VALUES (v_diario_compras, 'ASI-SIT-COM-001', NOW() - INTERVAL '1 day', 'Asiento SIT por documento por pagar.', 'DOCUMENTO_PAGAR', v_origen_id, 'REGISTRADO', 120.00, 120.00, NOW(), NOW(), 0)
    RETURNING asiento_contable_id INTO v_asiento;
    INSERT INTO asiento_contable_detalle (asiento_contable_id, cuenta_contable_id, descripcion, debe, haber, created_at) VALUES
    (v_asiento, v_costo, 'Costo de insumos de producción SIT.', 120.00, 0.00, NOW()),
    (v_asiento, v_proveedores, 'Proveedor nacional por compra SIT.', 0.00, 120.00, NOW());
  END IF;

  SELECT pago_proveedor_id::TEXT INTO v_origen_id FROM pago_proveedor WHERE codigo = 'PAG-SIT-001';
  IF v_origen_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM asiento_contable WHERE origen_tipo = 'PAGO_PROVEEDOR' AND origen_id = v_origen_id) THEN
    INSERT INTO asiento_contable (tipo_diario_contable_id, codigo, fecha_asiento, descripcion, origen_tipo, origen_id, estado, total_debe, total_haber, created_at, updated_at, version)
    VALUES (v_diario_caja, 'ASI-SIT-PAG-001', NOW() - INTERVAL '2 hour', 'Asiento SIT por pago proveedor.', 'PAGO_PROVEEDOR', v_origen_id, 'REGISTRADO', 50.00, 50.00, NOW(), NOW(), 0)
    RETURNING asiento_contable_id INTO v_asiento;
    INSERT INTO asiento_contable_detalle (asiento_contable_id, cuenta_contable_id, descripcion, debe, haber, created_at) VALUES
    (v_asiento, v_proveedores, 'Disminución de cuenta por pagar SIT.', 50.00, 0.00, NOW()),
    (v_asiento, v_caja, 'Salida de caja por pago proveedor SIT.', 0.00, 50.00, NOW());
  END IF;
END $$;

-- Documentos fiscales internos de presentación.
INSERT INTO documento_fiscal (codigo, tipo_comprobante, estado, documento_cobrar_id, documento_compra_id, origen_tipo, tercero_tipo, tercero_id, tercero_nombre, fecha_emision, establecimiento, punto_emision, secuencial, numero_comprobante, subtotal, impuesto, total, ambiente, observaciones, created_at, updated_at, version)
SELECT 'FIS-SIT-VTA-001', 'FACTURA', 'EMITIDO_INTERNO', dc.documento_cobrar_id, NULL, 'DOCUMENTO_COBRAR', 'CLIENTE', c.cliente_id, c.nombre_completo, NOW() - INTERVAL '1 day', '001', '001', '900000001', '001-001-900000001', 85.71, 10.29, 96.00, 'INTERNO', 'Documento fiscal interno SIT; no autorizado por SRI.', NOW(), NOW(), 0
FROM documento_cobrar dc JOIN cliente c ON c.cliente_id = dc.cliente_id
WHERE dc.codigo = 'CXC-SIT-001'
  AND NOT EXISTS (SELECT 1 FROM documento_fiscal WHERE codigo = 'FIS-SIT-VTA-001');

INSERT INTO documento_fiscal (codigo, tipo_comprobante, estado, documento_compra_id, documento_cobrar_id, origen_tipo, tercero_tipo, tercero_id, tercero_nombre, fecha_emision, establecimiento, punto_emision, secuencial, numero_comprobante, subtotal, impuesto, total, ambiente, observaciones, created_at, updated_at, version)
SELECT 'FIS-SIT-COM-001', 'COMPROBANTE_COMPRA_INTERNO', 'EMITIDO_INTERNO', dc.documento_compra_id, NULL, 'DOCUMENTO_COMPRA', 'PROVEEDOR', p.proveedor_id, p.nombre, NOW(), '001', '001', '900000002', '001-001-900000002', 107.14, 12.86, 120.00, 'INTERNO', 'Documento fiscal interno de compra SIT; no autorizado por SRI.', NOW(), NOW(), 0
FROM documento_compra dc JOIN proveedor p ON p.proveedor_id = dc.proveedor_id
WHERE dc.numero_documento = 'COMP-SIT-001'
  AND NOT EXISTS (SELECT 1 FROM documento_fiscal WHERE codigo = 'FIS-SIT-COM-001');

-- Reporte y auditoría de evidencia SIT.
INSERT INTO job_reporte (codigo_job, tipo_reporte, parametros_json, estado, solicitado_por_usuario_id, fecha_solicitud, fecha_inicio, fecha_fin, intentos, mensaje_error, archivo_id, request_id, version)
SELECT 'JOB-SIT-ERP-001', 'RESUMEN_NEGOCIO', '{"modo":"SIT","origen":"T24"}'::jsonb, 'COMPLETADO', u.usuario_id, NOW(), NOW(), NOW(), 1, NULL, NULL, 'req-sit-erp-001', 0
FROM usuario_sistema u
WHERE u.nombre_usuario = 'admin'
  AND NOT EXISTS (SELECT 1 FROM job_reporte WHERE codigo_job = 'JOB-SIT-ERP-001');

INSERT INTO auditoria_evento (codigo_evento, modulo, entidad, entidad_id, accion, actor_usuario_id, actor_rol, fecha_evento, valor_anterior_json, valor_nuevo_json, motivo, request_id, ip_origen)
SELECT 'SIT_T24_PREPARADO', 'SIT', 'presentacion', 'T24', 'PREPARAR_DATOS_PRESENTACION', u.usuario_id, 'ADMIN', NOW(), '{}'::jsonb, '{"estado":"LISTO"}'::jsonb, 'Seed de presentación T24 aplicado.', 'req-sit-t24', '127.0.0.1'
FROM usuario_sistema u
WHERE u.nombre_usuario = 'admin'
  AND NOT EXISTS (SELECT 1 FROM auditoria_evento WHERE codigo_evento = 'SIT_T24_PREPARADO' AND entidad_id = 'T24');
