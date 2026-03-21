-- Seeds corregidos para Producción y más variedad de datos

-- 1. PRODUCCION - Estados válidos: PENDIENTE, PREPARACION, DECORACION, EMPAQUE, FINALIZADO
INSERT INTO produccion (pedido_id, fecha_inicio, fecha_finalizacion, estado_produccion, prioridad_produccion, observaciones_produccion, created_at, updated_at, version)
SELECT 
    p.pedido_id,
    NOW() - (random() * INTERVAL '5 days'),
    CASE WHEN random() > 0.7 THEN NOW() + (random() * INTERVAL '2 days') ELSE NULL END,
    (ARRAY['PENDIENTE', 'PREPARACION', 'DECORACION', 'EMPAQUE', 'FINALIZADO'])[1 + floor(random() * 5)],
    (ARRAY['NORMAL', 'URGENTE'])[1 + floor(random() * 2)],
    'Trabajo en producción',
    NOW(),
    NOW(),
    0
FROM pedido p
WHERE p.estado_pedido IN ('REGISTRADO', 'EN_PREPARACION', 'LISTO')
AND NOT EXISTS (SELECT 1 FROM produccion prod WHERE prod.pedido_id = p.pedido_id)
LIMIT 15;

-- 2. Actualizar pedidos con diferentes estados para el "pastel estadístico"
-- Primero verificamos cuántos hay de cada estado
SELECT 'Antes - Pedidos por estado' as info, estado_pedido, COUNT(*) 
FROM pedido GROUP BY estado_pedido;

-- Distribuir pedidos en diferentes estados
UPDATE pedido SET estado_pedido = 'REGISTRADO' 
WHERE pedido_id IN (SELECT pedido_id FROM pedido WHERE estado_pedido = 'PENDIENTE' LIMIT 5);

UPDATE pedido SET estado_pedido = 'EN_PREPARACION' 
WHERE pedido_id IN (SELECT pedido_id FROM pedido WHERE estado_pedido = 'PENDIENTE' LIMIT 4);

UPDATE pedido SET estado_pedido = 'LISTO' 
WHERE pedido_id IN (SELECT pedido_id FROM pedido WHERE estado_pedido = 'PENDIENTE' LIMIT 3);

UPDATE pedido SET estado_pedido = 'ENTREGADO' 
WHERE pedido_id IN (SELECT pedido_id FROM pedido WHERE estado_pedido = 'PENDIENTE' LIMIT 4);

UPDATE pedido SET estado_pedido = 'CANCELADO' 
WHERE pedido_id IN (SELECT pedido_id FROM pedido WHERE estado_pedido = 'PENDIENTE' LIMIT 2);

-- 3. Más cotizaciones con estados variados
INSERT INTO cotizacion (cliente_id, codigo, estado_cotizacion, origen, total_estimado, observaciones, created_at, updated_at, version)
SELECT 
    c.cliente_id,
    'COT-' || LPAD(((SELECT COUNT(*) FROM cotizacion) + (ROW_NUMBER() OVER ()))::text, 5, '0'),
    (ARRAY['PENDIENTE', 'APROBADA', 'RECHAZADA', 'CONVERTIDA'])[1 + floor(random() * 4)],
    (ARRAY['PUBLICO', 'INTERNO'])[1 + floor(random() * 2)],
    150.0 + random() * 800.0,
    'Cotización adicional de prueba',
    NOW() - (random() * INTERVAL '15 days'), NOW(), 0
FROM cliente c
LIMIT 10;

-- 4. Verificar resultados
SELECT 
    '=== RESULTADOS ===' as info,
    NULL as valor,
    NULL as total
UNION ALL
SELECT 
    'Pedidos por estado:',
    estado_pedido,
    COUNT(*)::text
FROM pedido
GROUP BY estado_pedido
UNION ALL
SELECT 
    'Producciones por estado:',
    estado_produccion,
    COUNT(*)::text
FROM produccion
GROUP BY estado_produccion
UNION ALL
SELECT 
    'Cotizaciones por estado:',
    estado_cotizacion,
    COUNT(*)::text
FROM cotizacion
GROUP BY estado_cotizacion;
