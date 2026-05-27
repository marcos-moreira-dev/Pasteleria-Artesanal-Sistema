-- =============================================================================
-- PRESENTATION SEED - Operación pastelera
-- T24: pedidos, producción, caja, compras e inventario para demo/SIT.
-- No usar contra datos reales.
-- =============================================================================

\echo '== T24 presentation seed: bakery operations =='

-- Pedidos de presentación.
INSERT INTO pedido (
  cliente_id, codigo, estado_pedido, origen, prioridad, fecha_pedido, fecha_entrega_estimada, fecha_entrega_real, total_estimado, observaciones, created_at, updated_at, version
)
SELECT c.cliente_id, 'PED-SIT-001', 'ENTREGADO', 'INTERNO', 'NORMAL', NOW() - INTERVAL '5 day', NOW() - INTERVAL '2 day', NOW() - INTERVAL '2 day', 96.00,
       'Pedido SIT entregado; alimenta cartera, cobranza, asiento y documento fiscal interno.', NOW(), NOW(), 0
FROM cliente c
WHERE c.correo = 'laura.mendoza@sit.test'
  AND NOT EXISTS (SELECT 1 FROM pedido WHERE codigo = 'PED-SIT-001');

INSERT INTO pedido (
  cliente_id, codigo, estado_pedido, origen, prioridad, fecha_pedido, fecha_entrega_estimada, total_estimado, observaciones, created_at, updated_at, version
)
SELECT c.cliente_id, 'PED-SIT-002', 'EN_PREPARACION', 'INTERNO', 'URGENTE', NOW() - INTERVAL '1 day', NOW() + INTERVAL '1 day', 150.00,
       'Pedido SIT corporativo en preparación; valida producción y cartera parcial.', NOW(), NOW(), 0
FROM cliente c
WHERE c.correo = 'eventos@dulceaurora.test'
  AND NOT EXISTS (SELECT 1 FROM pedido WHERE codigo = 'PED-SIT-002');

INSERT INTO pedido (
  cliente_id, codigo, estado_pedido, origen, prioridad, fecha_pedido, fecha_entrega_estimada, total_estimado, observaciones, created_at, updated_at, version
)
SELECT c.cliente_id, 'PED-SIT-003', 'REGISTRADO', 'PUBLICO', 'NORMAL', NOW(), NOW() + INTERVAL '4 day', 72.00,
       'Pedido SIT pendiente para revisar dashboard y cola de trabajo.', NOW(), NOW(), 0
FROM cliente c
WHERE c.correo = 'compras@santaclara.test'
  AND NOT EXISTS (SELECT 1 FROM pedido WHERE codigo = 'PED-SIT-003');

INSERT INTO pedido_detalle (pedido_id, producto_id, descripcion_item, cantidad, precio_unitario, subtotal, notas, created_at)
SELECT p.pedido_id, pr.producto_id, pr.nombre, 1, 96.00, 96.00, 'Torta principal de demo.', NOW()
FROM pedido p, producto pr
WHERE p.codigo = 'PED-SIT-001' AND pr.codigo = 'TORT-SIT-CHOCO-20'
  AND NOT EXISTS (SELECT 1 FROM pedido_detalle d WHERE d.pedido_id = p.pedido_id AND d.producto_id = pr.producto_id);

INSERT INTO pedido_detalle (pedido_id, producto_id, descripcion_item, cantidad, precio_unitario, subtotal, notas, created_at)
SELECT p.pedido_id, pr.producto_id, pr.nombre, 1, 150.00, 150.00, 'Mesa dulce para 30 personas.', NOW()
FROM pedido p, producto pr
WHERE p.codigo = 'PED-SIT-002' AND pr.codigo = 'MESA-SIT-COFFEE-30'
  AND NOT EXISTS (SELECT 1 FROM pedido_detalle d WHERE d.pedido_id = p.pedido_id AND d.producto_id = pr.producto_id);

INSERT INTO pedido_detalle (pedido_id, producto_id, descripcion_item, cantidad, precio_unitario, subtotal, notas, created_at)
SELECT p.pedido_id, pr.producto_id, pr.nombre, 2, 36.00, 72.00, 'Dos cajas de cupcakes surtidos.', NOW()
FROM pedido p, producto pr
WHERE p.codigo = 'PED-SIT-003' AND pr.codigo = 'CUPK-SIT-SURTIDO-12'
  AND NOT EXISTS (SELECT 1 FROM pedido_detalle d WHERE d.pedido_id = p.pedido_id AND d.producto_id = pr.producto_id);

-- Producción asociada a pedidos de presentación.
INSERT INTO produccion (pedido_id, estado_produccion, prioridad_produccion, fecha_inicio, fecha_finalizacion, observaciones_produccion, created_at, updated_at, version)
SELECT p.pedido_id, 'FINALIZADO', 'NORMAL', NOW() - INTERVAL '4 day', NOW() - INTERVAL '2 day', 'Producción SIT finalizada para pedido entregado.', NOW(), NOW(), 0
FROM pedido p
WHERE p.codigo = 'PED-SIT-001'
  AND NOT EXISTS (SELECT 1 FROM produccion WHERE pedido_id = p.pedido_id);

INSERT INTO produccion (pedido_id, estado_produccion, prioridad_produccion, fecha_inicio, observaciones_produccion, created_at, updated_at, version)
SELECT p.pedido_id, 'DECORACION', 'URGENTE', NOW() - INTERVAL '6 hour', 'Producción SIT en decoración para revisar cola de trabajo.', NOW(), NOW(), 0
FROM pedido p
WHERE p.codigo = 'PED-SIT-002'
  AND NOT EXISTS (SELECT 1 FROM produccion WHERE pedido_id = p.pedido_id);

INSERT INTO produccion (pedido_id, estado_produccion, prioridad_produccion, observaciones_produccion, created_at, updated_at, version)
SELECT p.pedido_id, 'PENDIENTE', 'NORMAL', 'Producción SIT pendiente para agenda próxima.', NOW(), NOW(), 0
FROM pedido p
WHERE p.codigo = 'PED-SIT-003'
  AND NOT EXISTS (SELECT 1 FROM produccion WHERE pedido_id = p.pedido_id);

-- Receta técnica de presentación.
INSERT INTO receta (producto_id, nombre, rendimiento_base, costo_estimado, observaciones, es_activa, created_at, updated_at, created_by_user_id, version)
SELECT pr.producto_id, 'Receta SIT chocolate 20 porciones', 20.00, 28.50, 'Receta de presentación para revisar costos y materiales.', TRUE, NOW(), NOW(), u.usuario_id, 0
FROM producto pr, usuario_sistema u
WHERE pr.codigo = 'TORT-SIT-CHOCO-20' AND u.nombre_usuario = 'admin'
  AND NOT EXISTS (SELECT 1 FROM receta r WHERE r.producto_id = pr.producto_id AND r.nombre = 'Receta SIT chocolate 20 porciones');

INSERT INTO detalle_receta (receta_id, ingrediente_id, cantidad_base, rendimiento_por_unidad, es_para_porcion, observaciones, created_at)
SELECT r.receta_id, i.ingrediente_id, 2.5000, 20.0000, FALSE, 'Harina para torta de presentación.', NOW()
FROM receta r, ingrediente i
WHERE r.nombre = 'Receta SIT chocolate 20 porciones' AND i.codigo = 'ING-SIT-HARINA'
  AND NOT EXISTS (SELECT 1 FROM detalle_receta d WHERE d.receta_id = r.receta_id AND d.ingrediente_id = i.ingrediente_id);

INSERT INTO detalle_receta (receta_id, ingrediente_id, cantidad_base, rendimiento_por_unidad, es_para_porcion, observaciones, created_at)
SELECT r.receta_id, i.ingrediente_id, 1.8000, 20.0000, FALSE, 'Azúcar para torta de presentación.', NOW()
FROM receta r, ingrediente i
WHERE r.nombre = 'Receta SIT chocolate 20 porciones' AND i.codigo = 'ING-SIT-AZUCAR'
  AND NOT EXISTS (SELECT 1 FROM detalle_receta d WHERE d.receta_id = r.receta_id AND d.ingrediente_id = i.ingrediente_id);

-- Inventario y caja de presentación.
UPDATE caja_operativa
SET saldo_actual = GREATEST(saldo_actual, 180.00), updated_at = NOW()
WHERE codigo = 'CAJA_MATRIZ';

INSERT INTO turno_caja (caja_id, usuario_apertura_id, fecha_apertura, fecha_cierre, monto_apertura, monto_cierre_sistema, monto_cierre_declarado, diferencia_cierre, estado, observaciones_apertura, observaciones_cierre, created_at, updated_at, version)
SELECT c.caja_id, u.usuario_id, NOW() - INTERVAL '8 hour', NOW() - INTERVAL '1 hour', 80.00, 180.00, 180.00, 0.00, 'CERRADO', 'Turno SIT abierto para validación.', 'Turno SIT cerrado sin diferencia.', NOW(), NOW(), 0
FROM caja_operativa c, usuario_sistema u
WHERE c.codigo = 'CAJA_MATRIZ' AND u.nombre_usuario = 'admin'
  AND NOT EXISTS (SELECT 1 FROM turno_caja t WHERE t.observaciones_apertura = 'Turno SIT abierto para validación.');

INSERT INTO movimiento_caja (turno_caja_id, tipo_movimiento, naturaleza, fecha_movimiento, monto, moneda, referencia_tipo, referencia_id, descripcion, estado, creado_por_usuario_id)
SELECT t.turno_caja_id, 'APERTURA', 'ENTRADA', t.fecha_apertura, 80.00, 'USD', 'TURNO_CAJA', t.turno_caja_id::TEXT, 'Apertura de caja SIT.', 'REGISTRADO', u.usuario_id
FROM turno_caja t, usuario_sistema u
WHERE t.observaciones_apertura = 'Turno SIT abierto para validación.' AND u.nombre_usuario = 'admin'
  AND NOT EXISTS (SELECT 1 FROM movimiento_caja m WHERE m.referencia_tipo = 'TURNO_CAJA' AND m.referencia_id = t.turno_caja_id::TEXT AND m.tipo_movimiento = 'APERTURA');

INSERT INTO inventario_movimiento (item_tipo, item_id, tipo_movimiento, cantidad, saldo_anterior, saldo_posterior, referencia_tipo, referencia_id, motivo_salida, observaciones, registrado_por_user_id, fecha_movimiento)
SELECT 'INGREDIENTE', i.ingrediente_id, 'ENTRADA', 10.000, 16.000, 26.000, 'SIT_PRESENTACION', 'T24-INGRESO-HARINA', NULL, 'Ingreso SIT de harina para demo.', u.usuario_id, NOW() - INTERVAL '2 day'
FROM ingrediente i, usuario_sistema u
WHERE i.codigo = 'ING-SIT-HARINA' AND u.nombre_usuario = 'admin'
  AND NOT EXISTS (SELECT 1 FROM inventario_movimiento WHERE referencia_id = 'T24-INGRESO-HARINA');

-- Orden de compra, recepción y cuenta por pagar de presentación.
INSERT INTO orden_compra (proveedor_id, codigo, estado, fecha_emision, fecha_entrega_esperada, fecha_entrega_real, total_estimado, observaciones, activo, created_at, updated_at, version)
SELECT p.proveedor_id, 'OC-SIT-001', 'RECIBIDA', NOW() - INTERVAL '3 day', NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day', 120.00, 'Orden SIT recibida para alimentar documento de compra y CXP.', TRUE, NOW(), NOW(), 0
FROM proveedor p
WHERE p.codigo = 'PROV-SIT-HARINA'
  AND NOT EXISTS (SELECT 1 FROM orden_compra WHERE codigo = 'OC-SIT-001');

INSERT INTO orden_compra_detalle (orden_compra_id, item_tipo, item_id, item_nombre, item_codigo, cantidad, precio_unitario, subtotal, cantidad_recibida, observaciones, created_at)
SELECT oc.orden_compra_id, 'INGREDIENTE', i.ingrediente_id, i.nombre, i.codigo, 50, 2.40, 120.00, 50, 'Compra SIT de ingredientes principales.', NOW()
FROM orden_compra oc, ingrediente i
WHERE oc.codigo = 'OC-SIT-001' AND i.codigo = 'ING-SIT-HARINA'
  AND NOT EXISTS (SELECT 1 FROM orden_compra_detalle d WHERE d.orden_compra_id = oc.orden_compra_id AND d.item_codigo = i.codigo);

INSERT INTO documento_compra (orden_compra_id, proveedor_id, numero_documento, estado, fecha_emision, subtotal, impuesto, total, observaciones, created_at, updated_at, version)
SELECT oc.orden_compra_id, oc.proveedor_id, 'COMP-SIT-001', 'REGISTRADO', NOW() - INTERVAL '1 day', 107.14, 12.86, 120.00, 'Documento de compra SIT registrado.', NOW(), NOW(), 0
FROM orden_compra oc
WHERE oc.codigo = 'OC-SIT-001'
  AND NOT EXISTS (SELECT 1 FROM documento_compra WHERE numero_documento = 'COMP-SIT-001');

INSERT INTO documento_pagar (documento_compra_id, proveedor_id, codigo, estado, fecha_emision, fecha_vencimiento, total, saldo, observaciones, created_at, updated_at, version)
SELECT dc.documento_compra_id, dc.proveedor_id, 'CXP-SIT-001', 'PAGADO_PARCIAL', dc.fecha_emision, NOW() + INTERVAL '15 day', dc.total, 70.00, 'Cuenta por pagar SIT parcialmente pagada.', NOW(), NOW(), 0
FROM documento_compra dc
WHERE dc.numero_documento = 'COMP-SIT-001'
  AND NOT EXISTS (SELECT 1 FROM documento_pagar WHERE codigo = 'CXP-SIT-001');
