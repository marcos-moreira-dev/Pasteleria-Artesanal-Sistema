-- Seeds CORREGIDOS para Abastecimiento con estructura real de BD

-- 1. UNIDADES DE MEDIDA
INSERT INTO umedida (codigo, nombre, abreviatura, tipo, decimales, activo, created_at, updated_at, version) VALUES
('KG', 'Kilogramo', 'kg', 'PESO', 2, true, NOW(), NOW(), 0),
('GR', 'Gramo', 'g', 'PESO', 0, true, NOW(), NOW(), 0),
('LT', 'Litro', 'L', 'VOLUMEN', 2, true, NOW(), NOW(), 0),
('ML', 'Mililitro', 'ml', 'VOLUMEN', 0, true, NOW(), NOW(), 0),
('UN', 'Unidad', 'und', 'UNIDAD', 0, true, NOW(), NOW(), 0),
('DOC', 'Docena', 'doc', 'UNIDAD', 0, true, NOW(), NOW(), 0),
('CAJA', 'Caja', 'caja', 'ENVASE', 0, true, NOW(), NOW(), 0),
('BOL', 'Bolsa', 'bol', 'ENVASE', 0, true, NOW(), NOW(), 0);

-- 2. INSUMOS (solo campos que existen)
INSERT INTO insumo (codigo, nombre, descripcion, umedida_id, stock_minimo, stock_actual, costo_referencial, activo, created_at, updated_at, version)
SELECT 
    i.codigo, i.nombre, i.descripcion, u.umedida_id, 
    i.stock_minimo, i.stock_actual, i.costo,
    true, NOW(), NOW(), 0
FROM (VALUES
    ('INS-001', 'Harina de trigo', 'Harina todo uso para repostería', 'KG', 10.0, 45.0, 1.50),
    ('INS-002', 'Azúcar blanca', 'Azúcar refinada', 'KG', 5.0, 25.0, 1.20),
    ('INS-003', 'Mantequilla', 'Mantequilla sin sal', 'KG', 3.0, 12.0, 4.50),
    ('INS-004', 'Huevos', 'Huevos frescos medianos', 'DOC', 5.0, 24.0, 3.00),
    ('INS-005', 'Levadura', 'Levadura seca instantánea', 'GR', 100.0, 500.0, 0.05),
    ('INS-006', 'Chocolate negro', 'Chocolate 70% cacao', 'KG', 2.0, 8.0, 8.00),
    ('INS-007', 'Vainilla', 'Esencia de vainilla', 'ML', 250.0, 800.0, 0.02),
    ('INS-008', 'Leche entera', 'Leche pasteurizada', 'LT', 10.0, 35.0, 1.10),
    ('INS-009', 'Crema de leche', 'Crema para batir', 'LT', 5.0, 12.0, 2.50),
    ('INS-010', 'Fresas', 'Fresas frescas', 'KG', 2.0, 5.0, 4.00),
    ('INS-011', 'Dulce de leche', 'Dulce de leche artesanal', 'KG', 3.0, 10.0, 3.50),
    ('INS-012', 'Cacao en polvo', 'Cacao puro en polvo', 'KG', 1.0, 4.0, 5.00)
) AS i(codigo, nombre, descripcion, umedida_cod, stock_minimo, stock_actual, costo)
JOIN umedida u ON u.codigo = i.umedida_cod;

-- 3. PROVEEDORES (sin columna contacto)
INSERT INTO proveedor (codigo, nombre, telefono, correo, direccion, observaciones, activo, created_at, updated_at, version)
VALUES
('PROV-001', 'Distribuidora Andrade', '0991234567', 'carlos@andrade.com', 'Av. Principal 123', 'Proveedor principal de harina', true, NOW(), NOW(), 0),
('PROV-002', 'Insumos Delicados', '0992345678', 'maria@delicados.com', 'Calle Secundaria 456', 'Especializado en chocolates', true, NOW(), NOW(), 0),
('PROV-003', 'Lácteos San José', '0993456789', 'pedro@lacteos.com', 'Sector Industrial', 'Productos lácteos frescos', true, NOW(), NOW(), 0),
('PROV-004', 'Chocolates Premium', '0994567890', 'ana@chocolates.com', 'Zona Franca', 'Chocolate importado', true, NOW(), NOW(), 0),
('PROV-005', 'Frutas Selectas', '0995678901', 'luisa@frutas.com', 'Mercado Mayorista', 'Frutas frescas diarias', true, NOW(), NOW(), 0);

-- 4. ITEMS DE PROVEEDOR (usa item_id y item_tipo)
INSERT INTO item_proveedor (proveedor_id, item_id, item_tipo, precio_suministro, es_principal, activo, created_at)
SELECT p.proveedor_id, i.insumo_id, 'INSUMO', i.costo_referencial * 0.9, true, true, NOW()
FROM proveedor p
CROSS JOIN insumo i
WHERE p.codigo = 'PROV-001' AND i.codigo IN ('INS-001', 'INS-002', 'INS-003');

-- 5. INGREDIENTES (estructura similar a insumo)
INSERT INTO ingrediente (codigo, nombre, descripcion, umedida_id, stock_minimo, stock_actual, costo_referencial, activo, created_at, updated_at, version)
SELECT 
    'ING-' || substr(i.codigo, 5), 
    'Ing. ' || i.nombre,
    i.descripcion,
    i.umedida_id,
    i.stock_minimo * 0.5,
    i.stock_actual * 0.5,
    i.costo_referencial * 1.2,
    true, NOW(), NOW(), 0
FROM insumo i
LIMIT 8;

-- 6. RECETAS (sin version_receta)
INSERT INTO receta (producto_id, activa, created_at, updated_at, version)
SELECT p.producto_id, true, NOW(), NOW(), 0
FROM producto p
WHERE p.publicado = true;

-- 7. DETALLE DE RECETAS (campos correctos)
INSERT INTO detalle_receta (receta_id, ingrediente_id, cantidad_base, rendimiento_por_unidad, es_para_porcion, observaciones, created_at)
SELECT 
    r.receta_id,
    (SELECT ingrediente_id FROM ingrediente ORDER BY random() LIMIT 1),
    100.0 + random() * 400.0,
    1.0,
    false,
    'Mezclar cuidadosamente',
    NOW()
FROM receta r
LIMIT 20;

-- 8. CLIENTES ADICIONALES (ya funcionó antes)
INSERT INTO cliente (nombre_completo, telefono, correo, observaciones, fecha_registro, created_at, updated_at, version)
VALUES
('Juan Pérez', '0991112222', 'juan@email.com', 'Cliente frecuente', NOW() - INTERVAL '10 days', NOW(), NOW(), 0),
('Carlos López', '0993334444', 'carlos@email.com', 'Eventos corporativos', NOW() - INTERVAL '15 days', NOW(), NOW(), 0),
('Ana Martínez', '0994445555', 'ana@email.com', 'Cumpleaños familiares', NOW() - INTERVAL '3 days', NOW(), NOW(), 0),
('Pedro Ruiz', '0995556666', 'pedro@email.com', 'Pedidos grandes', NOW() - INTERVAL '20 days', NOW(), NOW(), 0)
ON CONFLICT DO NOTHING;

-- 9. PEDIDOS (campos correctos)
INSERT INTO pedido (cliente_id, codigo, fecha_pedido, fecha_entrega_estimada, estado_pedido, prioridad, origen, total_estimado, observaciones, created_at, updated_at, version)
SELECT 
    c.cliente_id,
    'PED-' || LPAD((ROW_NUMBER() OVER ())::text, 5, '0'),
    NOW() - (random() * INTERVAL '15 days'),
    NOW() + (random() * INTERVAL '5 days'),
    'PENDIENTE',
    'NORMAL',
    'MOSTRADOR',
    50.0 + random() * 300.0,
    'Pedido de prueba',
    NOW(), NOW(), 0
FROM cliente c
WHERE c.cliente_id IS NOT NULL
LIMIT 10;

-- 10. DETALLE DE PEDIDOS (campos correctos)
INSERT INTO pedido_detalle (pedido_id, producto_id, cantidad, precio_unitario, subtotal, descripcion_item, notas, created_at)
SELECT 
    p.pedido_id,
    (SELECT producto_id FROM producto WHERE publicado = true ORDER BY random() LIMIT 1),
    1 + floor(random() * 5),
    15.0 + random() * 100.0,
    0,
    'Producto solicitado',
    'Sin notas',
    NOW()
FROM pedido p;

-- 11. COTIZACIONES (campos correctos)
INSERT INTO cotizacion (cliente_id, codigo, fecha_cotizacion, estado_cotizacion, origen, total_estimado, observaciones, created_at, updated_at, version)
SELECT 
    c.cliente_id,
    'COT-' || LPAD((ROW_NUMBER() OVER ())::text, 5, '0'),
    NOW() - (random() * INTERVAL '10 days'),
    'PENDIENTE',
    'WEB',
    100.0 + random() * 500.0,
    'Cotización de prueba',
    NOW(), NOW(), 0
FROM cliente c
WHERE c.cliente_id IS NOT NULL
LIMIT 8;

-- 12. DETALLE DE COTIZACIONES (campos correctos)
INSERT INTO cotizacion_detalle (cotizacion_id, producto_id, cantidad, precio_estimado, subtotal, descripcion_item, notas, created_at)
SELECT 
    c.cotizacion_id,
    (SELECT producto_id FROM producto WHERE requiere_cotizacion = true ORDER BY random() LIMIT 1),
    1 + floor(random() * 3),
    200.0 + random() * 400.0,
    0,
    'Item cotizado',
    'Cliente interesado',
    NOW()
FROM cotizacion c;

-- Verificar inserciones
SELECT 'Unidades de medida' as tabla, COUNT(*) as total FROM umedida
UNION ALL SELECT 'Insumos', COUNT(*) FROM insumo
UNION ALL SELECT 'Proveedores', COUNT(*) FROM proveedor
UNION ALL SELECT 'Items proveedor', COUNT(*) FROM item_proveedor
UNION ALL SELECT 'Ingredientes', COUNT(*) FROM ingrediente
UNION ALL SELECT 'Recetas', COUNT(*) FROM receta
UNION ALL SELECT 'Detalle recetas', COUNT(*) FROM detalle_receta
UNION ALL SELECT 'Clientes totales', COUNT(*) FROM cliente
UNION ALL SELECT 'Pedidos', COUNT(*) FROM pedido
UNION ALL SELECT 'Detalle pedidos', COUNT(*) FROM pedido_detalle
UNION ALL SELECT 'Cotizaciones', COUNT(*) FROM cotizacion
UNION ALL SELECT 'Detalle cotizaciones', COUNT(*) FROM cotizacion_detalle;
