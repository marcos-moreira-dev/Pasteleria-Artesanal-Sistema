-- =============================================================================
-- PASTELERIA ERP - SEMANTIC VIEWS
-- Tanda T21: capa semantica de lectura para inteligencia/reportes.
--
-- Estas vistas no modifican datos ni reemplazan servicios de dominio. Funcionan
-- como superficie de consulta para tableros, reportes y validaciones de gestion.
-- =============================================================================

CREATE OR REPLACE VIEW inteligencia.vw_semantic_ventas_operativas AS
SELECT
    p.pedido_id,
    p.codigo AS pedido_codigo,
    p.estado_pedido,
    p.prioridad,
    p.origen,
    p.fecha_pedido::date AS fecha,
    p.total_estimado,
    c.cliente_id,
    c.nombre_completo AS cliente_nombre,
    COUNT(pd.pedido_detalle_id) AS lineas,
    COALESCE(SUM(pd.cantidad), 0) AS unidades
FROM public.pedido p
JOIN public.cliente c ON c.cliente_id = p.cliente_id
LEFT JOIN public.pedido_detalle pd ON pd.pedido_id = p.pedido_id
GROUP BY p.pedido_id, p.codigo, p.estado_pedido, p.prioridad, p.origen, p.fecha_pedido, p.total_estimado, c.cliente_id, c.nombre_completo;

CREATE OR REPLACE VIEW inteligencia.vw_semantic_produccion_pendiente AS
SELECT
    pr.produccion_id,
    pr.estado_produccion,
    pr.prioridad_produccion,
    pr.fecha_inicio,
    pr.fecha_finalizacion,
    p.pedido_id,
    p.codigo AS pedido_codigo,
    p.fecha_entrega_estimada,
    c.nombre_completo AS cliente_nombre
FROM public.produccion pr
JOIN public.pedido p ON p.pedido_id = pr.pedido_id
JOIN public.cliente c ON c.cliente_id = p.cliente_id
WHERE pr.estado_produccion <> 'FINALIZADO';

CREATE OR REPLACE VIEW inteligencia.vw_semantic_stock_actual AS
SELECT * FROM inteligencia.vw_pasteleria_stock_bajo;

CREATE OR REPLACE VIEW inteligencia.vw_semantic_cartera_documentos AS
SELECT
    dc.documento_cobrar_id,
    dc.codigo,
    dc.estado,
    dc.fecha_emision,
    dc.fecha_vencimiento,
    dc.total,
    dc.saldo,
    (dc.total - dc.saldo) AS monto_cobrado,
    CASE
        WHEN dc.fecha_vencimiento IS NOT NULL
             AND dc.fecha_vencimiento::date < CURRENT_DATE
             AND dc.saldo > 0
        THEN CURRENT_DATE - dc.fecha_vencimiento::date
        ELSE 0
    END AS dias_vencido,
    c.cliente_id,
    c.nombre_completo AS cliente_nombre,
    p.pedido_id,
    p.codigo AS pedido_codigo
FROM public.documento_cobrar dc
JOIN public.cliente c ON c.cliente_id = dc.cliente_id
LEFT JOIN public.pedido p ON p.pedido_id = dc.pedido_id;

CREATE OR REPLACE VIEW inteligencia.vw_semantic_cuentas_pagar_documentos AS
SELECT
    dp.documento_pagar_id,
    dp.codigo,
    dp.estado,
    dp.fecha_emision,
    dp.fecha_vencimiento,
    dp.total,
    dp.saldo,
    (dp.total - dp.saldo) AS monto_pagado,
    CASE
        WHEN dp.fecha_vencimiento IS NOT NULL
             AND dp.fecha_vencimiento::date < CURRENT_DATE
             AND dp.saldo > 0
        THEN CURRENT_DATE - dp.fecha_vencimiento::date
        ELSE 0
    END AS dias_vencido,
    pr.proveedor_id,
    pr.nombre AS proveedor_nombre,
    dc.documento_compra_id,
    dc.numero_documento AS numero_documento_compra
FROM public.documento_pagar dp
JOIN public.proveedor pr ON pr.proveedor_id = dp.proveedor_id
JOIN public.documento_compra dc ON dc.documento_compra_id = dp.documento_compra_id;

CREATE OR REPLACE VIEW inteligencia.vw_semantic_caja_movimientos AS
SELECT
    mc.movimiento_caja_id,
    mc.fecha_movimiento,
    mc.tipo_movimiento,
    mc.naturaleza,
    mc.monto,
    mc.moneda,
    mc.estado,
    mc.referencia_tipo,
    mc.referencia_id,
    mc.descripcion,
    tc.turno_caja_id,
    tc.estado AS estado_turno,
    co.caja_id,
    co.codigo AS caja_codigo,
    co.nombre AS caja_nombre
FROM public.movimiento_caja mc
JOIN public.turno_caja tc ON tc.turno_caja_id = mc.turno_caja_id
JOIN public.caja_operativa co ON co.caja_id = tc.caja_id;

CREATE OR REPLACE VIEW inteligencia.vw_semantic_contabilidad_asientos AS
SELECT
    ac.asiento_contable_id,
    ac.codigo,
    ac.fecha_asiento,
    ac.estado,
    ac.descripcion,
    ac.origen_tipo,
    ac.origen_id,
    ac.total_debe,
    ac.total_haber,
    td.codigo AS diario_codigo,
    td.nombre AS diario_nombre,
    COUNT(ad.asiento_contable_detalle_id) AS lineas
FROM public.asiento_contable ac
JOIN public.tipo_diario_contable td ON td.tipo_diario_contable_id = ac.tipo_diario_contable_id
LEFT JOIN public.asiento_contable_detalle ad ON ad.asiento_contable_id = ac.asiento_contable_id
GROUP BY ac.asiento_contable_id, ac.codigo, ac.fecha_asiento, ac.estado, ac.descripcion,
         ac.origen_tipo, ac.origen_id, ac.total_debe, ac.total_haber, td.codigo, td.nombre;

CREATE OR REPLACE VIEW inteligencia.vw_semantic_fiscal_documentos AS
SELECT
    df.documento_fiscal_id,
    df.codigo,
    df.tipo_comprobante,
    df.estado,
    df.origen_tipo,
    df.tercero_tipo,
    df.tercero_id,
    df.tercero_nombre,
    df.fecha_emision,
    df.numero_comprobante,
    df.subtotal,
    df.impuesto,
    df.total,
    df.ambiente,
    df.clave_acceso,
    df.numero_autorizacion,
    df.fecha_autorizacion
FROM public.documento_fiscal df;

CREATE OR REPLACE VIEW inteligencia.vw_semantic_dashboard_erp AS
SELECT
    NOW() AS fecha_corte,
    (SELECT COUNT(*) FROM public.pedido WHERE estado_pedido NOT IN ('ENTREGADO','CANCELADO')) AS pedidos_activos,
    (SELECT COUNT(*) FROM public.produccion WHERE estado_produccion NOT IN ('FINALIZADO','CANCELADO')) AS producciones_activas,
    (SELECT COUNT(*) FROM public.documento_cobrar WHERE estado IN ('PENDIENTE','PARCIAL')) AS documentos_cobrar_abiertos,
    (SELECT COALESCE(SUM(saldo), 0) FROM public.documento_cobrar WHERE estado IN ('PENDIENTE','PARCIAL')) AS saldo_cartera,
    (SELECT COUNT(*) FROM public.documento_pagar WHERE estado IN ('PENDIENTE','PAGADO_PARCIAL')) AS documentos_pagar_abiertos,
    (SELECT COALESCE(SUM(saldo), 0) FROM public.documento_pagar WHERE estado IN ('PENDIENTE','PAGADO_PARCIAL')) AS saldo_cuentas_pagar,
    (SELECT COALESCE(SUM(saldo_actual), 0) FROM public.caja_operativa WHERE activa = true) AS saldo_caja,
    (SELECT COUNT(*) FROM public.asiento_contable WHERE estado = 'REGISTRADO') AS asientos_registrados,
    (SELECT COUNT(*) FROM public.documento_fiscal WHERE estado = 'BORRADOR') AS documentos_fiscales_borrador,
    (SELECT COUNT(*) FROM inteligencia.vw_pasteleria_stock_bajo) AS items_stock_bajo;
