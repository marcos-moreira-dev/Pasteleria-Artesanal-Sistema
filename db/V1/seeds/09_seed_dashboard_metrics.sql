-- Actualizar dashboard: crear pedidos entregados recientes para métricas

-- 1. Actualizar algunos pedidos LISTO a ENTREGADO con fecha de hoy
UPDATE pedido 
SET estado_pedido = 'ENTREGADO', 
    fecha_entrega_real = NOW(), 
    updated_at = NOW() 
WHERE estado_pedido = 'LISTO' 
AND pedido_id IN (SELECT pedido_id FROM pedido WHERE estado_pedido = 'LISTO' LIMIT 3);

-- 2. Actualizar algunos pedidos EN_PREPARACION a ENTREGADO con fecha de esta semana
UPDATE pedido 
SET estado_pedido = 'ENTREGADO', 
    fecha_entrega_real = NOW() - INTERVAL '2 days', 
    updated_at = NOW() 
WHERE estado_pedido = 'EN_PREPARACION' 
AND pedido_id IN (SELECT pedido_id FROM pedido WHERE estado_pedido = 'EN_PREPARACION' LIMIT 4);

-- 3. Verificar resultados
SELECT 
    estado_pedido,
    COUNT(*) as total,
    SUM(CASE WHEN fecha_entrega_real >= CURRENT_DATE THEN 1 ELSE 0 END) as entregados_hoy,
    SUM(CASE WHEN fecha_entrega_real >= DATE_TRUNC('week', CURRENT_DATE) THEN 1 ELSE 0 END) as entregados_esta_semana,
    SUM(total_estimado) FILTER (WHERE estado_pedido = 'ENTREGADO') as valor_total_entregado
FROM pedido 
GROUP BY estado_pedido 
ORDER BY total DESC;

-- 4. Resumen de métricas para el dashboard
SELECT 
    'Caja del día' as metrica,
    COALESCE(SUM(total_estimado), 0) as valor,
    COUNT(*) as cantidad
FROM pedido 
WHERE estado_pedido = 'ENTREGADO' 
AND fecha_entrega_real >= CURRENT_DATE

UNION ALL

SELECT 
    'Ritmo semanal' as metrica,
    COALESCE(SUM(total_estimado), 0) as valor,
    COUNT(*) as cantidad
FROM pedido 
WHERE estado_pedido = 'ENTREGADO' 
AND fecha_entrega_real >= DATE_TRUNC('week', CURRENT_DATE)

UNION ALL

SELECT 
    'Ticket promedio' as metrica,
    COALESCE(AVG(total_estimado), 0) as valor,
    COUNT(*) as cantidad
FROM pedido 
WHERE estado_pedido = 'ENTREGADO' 
AND fecha_entrega_real >= DATE_TRUNC('week', CURRENT_DATE);
