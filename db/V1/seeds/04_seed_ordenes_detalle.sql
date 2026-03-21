-- Tabla de detalles de orden de compra
CREATE TABLE IF NOT EXISTS orden_compra_detalle (
    orden_compra_detalle_id BIGSERIAL PRIMARY KEY,
    orden_compra_id BIGINT NOT NULL REFERENCES orden_compra(orden_compra_id) ON DELETE CASCADE,
    item_tipo VARCHAR(40) NOT NULL CHECK (item_tipo IN ('INSUMO', 'INGREDIENTE')),
    item_id BIGINT NOT NULL,
    item_nombre VARCHAR(160) NOT NULL,
    item_codigo VARCHAR(40) NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario NUMERIC(12,4),
    subtotal NUMERIC(12,2),
    cantidad_recibida INTEGER DEFAULT 0,
    observaciones TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- Insert detalles de prueba
INSERT INTO orden_compra_detalle (orden_compra_id, item_tipo, item_id, item_nombre, item_codigo, cantidad, precio_unitario, subtotal, cantidad_recibida, observaciones)
SELECT 
    oc.orden_compra_id,
    'INSUMO',
    i.insumo_id,
    i.nombre,
    i.codigo,
    10 + floor(random() * 50),
    i.costo_referencial,
    (10 + floor(random() * 50)) * i.costo_referencial,
    0,
    'Item de prueba'
FROM orden_compra oc
CROSS JOIN (SELECT insumo_id, nombre, codigo, costo_referencial FROM insumo LIMIT 3) i
WHERE oc.orden_compra_id IN (SELECT orden_compra_id FROM orden_compra LIMIT 5);

-- Verificar
SELECT 'Detalles de orden creados' as mensaje, COUNT(*) as total FROM orden_compra_detalle;
