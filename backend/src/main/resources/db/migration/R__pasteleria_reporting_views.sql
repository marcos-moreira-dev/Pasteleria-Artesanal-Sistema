-- =============================================================================
-- PASTELERIA ERP - REPORTING VIEWS
-- Tanda T07: vistas de lectura inicial sobre V1/public.
-- =============================================================================

CREATE OR REPLACE VIEW inteligencia.vw_pasteleria_pedidos_resumen AS
SELECT
    p.pedido_id,
    p.codigo,
    p.estado_pedido,
    p.prioridad,
    p.origen,
    p.fecha_pedido,
    p.fecha_entrega_estimada,
    p.fecha_entrega_real,
    p.total_estimado,
    c.nombre_completo AS cliente_nombre,
    c.telefono AS cliente_telefono
FROM public.pedido p
JOIN public.cliente c ON c.cliente_id = p.cliente_id;

CREATE OR REPLACE VIEW inteligencia.vw_pasteleria_stock_bajo AS
SELECT 'INGREDIENTE'::varchar(20) AS tipo_item,
       i.ingrediente_id AS item_id,
       i.codigo,
       i.nombre,
       i.stock_actual,
       i.stock_minimo,
       u.abreviatura AS unidad
FROM public.ingrediente i
JOIN public.umedida u ON u.umedida_id = i.umedida_id
WHERE i.activo = true
  AND i.stock_minimo IS NOT NULL
  AND i.stock_actual <= i.stock_minimo
UNION ALL
SELECT 'INSUMO'::varchar(20) AS tipo_item,
       s.insumo_id AS item_id,
       s.codigo,
       s.nombre,
       s.stock_actual,
       s.stock_minimo,
       u.abreviatura AS unidad
FROM public.insumo s
JOIN public.umedida u ON u.umedida_id = s.umedida_id
WHERE s.activo = true
  AND s.stock_minimo IS NOT NULL
  AND s.stock_actual <= s.stock_minimo;

CREATE OR REPLACE VIEW inteligencia.vw_pasteleria_reportes_estado AS
SELECT
    jr.job_reporte_id,
    jr.codigo_job,
    jr.tipo_reporte,
    jr.estado,
    jr.fecha_solicitud,
    jr.fecha_inicio,
    jr.fecha_fin,
    jr.request_id,
    ar.archivo_id,
    ar.codigo_archivo,
    ar.nombre_original,
    ar.estado AS estado_archivo
FROM public.job_reporte jr
LEFT JOIN public.archivo_recurso ar ON ar.archivo_id = jr.archivo_id;
