-- Seeds masivos simplificados

-- 1. MÁS CLIENTES
INSERT INTO cliente (nombre_completo, telefono, correo, observaciones, fecha_registro, created_at, updated_at, version)
VALUES 
('María González', '0991000001', 'maria.g@email.com', 'Cliente VIP', NOW() - INTERVAL '25 days', NOW(), NOW(), 0),
('Roberto Sánchez', '0991000002', 'roberto.s@email.com', 'Eventos corporativos', NOW() - INTERVAL '20 days', NOW(), NOW(), 0),
('Carmen Ruiz', '0991000003', 'carmen.r@email.com', 'Cumpleaños familiares', NOW() - INTERVAL '18 days', NOW(), NOW(), 0),
('Fernando López', '0991000004', 'fernando.l@email.com', 'Pedidos grandes', NOW() - INTERVAL '15 days', NOW(), NOW(), 0),
('Isabel Torres', '0991000005', 'isabel.t@email.com', 'Cliente frecuente', NOW() - INTERVAL '12 days', NOW(), NOW(), 0),
('Diego Morales', '0991000006', 'diego.m@email.com', 'Eventos mensuales', NOW() - INTERVAL '10 days', NOW(), NOW(), 0),
('Lucía Castro', '0991000007', 'lucia.c@email.com', 'Recomendada por Pedro', NOW() - INTERVAL '8 days', NOW(), NOW(), 0),
('Andrés Vargas', '0991000008', 'andres.v@email.com', 'Empresa XYZ', NOW() - INTERVAL '5 days', NOW(), NOW(), 0),
('Paula Mendoza', '0991000009', 'paula.m@email.com', 'Instagram', NOW() - INTERVAL '3 days', NOW(), NOW(), 0),
('Santiago Reyes', '0991000010', 'santiago.r@email.com', 'Facebook', NOW() - INTERVAL '1 days', NOW(), NOW(), 0)
ON CONFLICT DO NOTHING;

-- 2. MUCHOS PEDIDOS
DO $$
DECLARE
    v_cliente_id BIGINT;
    v_estado TEXT;
    v_codigo TEXT;
    v_total NUMERIC;
    i INTEGER;
BEGIN
    FOR i IN 1..40 LOOP
        SELECT cliente_id INTO v_cliente_id 
        FROM cliente ORDER BY random() LIMIT 1;
        
        v_estado := (ARRAY['REGISTRADO', 'EN_PREPARACION', 'LISTO', 'ENTREGADO', 'CANCELADO'])[1 + floor(random() * 5)];
        v_codigo := 'PED-' || LPAD((5 + i)::text, 5, '0');
        v_total := 50.0 + random() * 800.0;
        
        INSERT INTO pedido (cliente_id, codigo, fecha_pedido, fecha_entrega_estimada, estado_pedido, prioridad, origen, total_estimado, observaciones, created_at, updated_at, version)
        VALUES (v_cliente_id, v_codigo, NOW() - (random() * INTERVAL '30 days'), NOW() + (random() * INTERVAL '15 days'), v_estado, 'NORMAL', 'PUBLICO', v_total, 'Pedido demo', NOW(), NOW(), 0)
        ON CONFLICT DO NOTHING;
    END LOOP;
END $$;

-- 3. DETALLES PARA PEDIDOS NUEVOS
INSERT INTO pedido_detalle (pedido_id, producto_id, cantidad, precio_unitario, subtotal, descripcion_item, notas, created_at)
SELECT 
    p.pedido_id,
    (SELECT producto_id FROM producto WHERE publicado = true ORDER BY random() LIMIT 1),
    1 + floor(random() * 8),
    25.0 + random() * 200.0,
    0,
    'Item del pedido',
    'Auto',
    NOW()
FROM pedido p
WHERE p.codigo LIKE 'PED-000%'
AND NOT EXISTS (SELECT 1 FROM pedido_detalle pd WHERE pd.pedido_id = p.pedido_id);

-- 4. PRODUCCIONES EN TODOS LOS ESTADOS CON OBSERVACIONES REALES
INSERT INTO produccion (pedido_id, fecha_inicio, fecha_finalizacion, estado_produccion, prioridad_produccion, observaciones_produccion, created_at, updated_at, version)
SELECT 
    p.pedido_id,
    NOW() - (random() * INTERVAL '10 days'),
    CASE WHEN random() > 0.5 THEN NOW() - (random() * INTERVAL '3 days') ELSE NULL END,
    (ARRAY['PENDIENTE', 'PREPARACION', 'DECORACION', 'EMPAQUE', 'FINALIZADO'])[1 + floor(random() * 5)],
    (ARRAY['NORMAL', 'URGENTE'])[1 + floor(random() * 2)],
    (ARRAY[
        'Cliente solicita entrega antes de las 6pm',
        'Sin azúcar - usar edulcorante',
        'Decoración con temática unicornio',
        'Mensaje: "Feliz cumpleaños mamá"',
        'Evitar nueces por alergia',
        'Cajas individuales para mesa dulce',
        'Diseño minimalista en blanco',
        'Agregar velas número 25',
        'Empaque especial para transporte',
        'Foto del cliente en impresión comestible',
        'Colores corporativos: azul y gris',
        'Entrega directo en el salón de eventos',
        'Doble capa de crema',
        'Temática mariposas',
        'Sin gluten - usar harina de almendra'
    ])[1 + floor(random() * 15)],
    NOW(), NOW(), 0
FROM pedido p
WHERE p.estado_pedido IN ('REGISTRADO', 'EN_PREPARACION', 'LISTO')
AND NOT EXISTS (SELECT 1 FROM produccion prod WHERE prod.pedido_id = p.pedido_id)
LIMIT 25;

-- Verificar
SELECT 'RESUMEN FINAL' as reporte;
SELECT 
    'Pedidos: ' || COUNT(*) || ' (REG:' || COUNT(*) FILTER (WHERE estado_pedido = 'REGISTRADO') || 
    ' PREP:' || COUNT(*) FILTER (WHERE estado_pedido = 'EN_PREPARACION') ||
    ' LIST:' || COUNT(*) FILTER (WHERE estado_pedido = 'LISTO') ||
    ' ENT:' || COUNT(*) FILTER (WHERE estado_pedido = 'ENTREGADO') ||
    ' CAN:' || COUNT(*) FILTER (WHERE estado_pedido = 'CANCELADO') || ')'
FROM pedido;
