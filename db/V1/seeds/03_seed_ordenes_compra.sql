-- Tabla orden_compra y datos de prueba

CREATE TABLE IF NOT EXISTS orden_compra (
    orden_compra_id BIGSERIAL PRIMARY KEY,
    proveedor_id BIGINT NOT NULL REFERENCES proveedor(proveedor_id),
    codigo VARCHAR(40) NOT NULL UNIQUE,
    estado VARCHAR(40) NOT NULL CHECK (estado IN ('BORRADOR', 'ENVIADA', 'RECIBIDA_PARCIAL', 'COMPLETADA', 'CANCELADA')),
    fecha_emision TIMESTAMPTZ DEFAULT NOW(),
    fecha_entrega_esperada TIMESTAMPTZ,
    total_estimado NUMERIC(12,2),
    observaciones TEXT,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    version BIGINT DEFAULT 0
);

-- Insert órdenes de compra de prueba
INSERT INTO orden_compra (proveedor_id, codigo, estado, fecha_emision, fecha_entrega_esperada, total_estimado, observaciones, activo, created_at, updated_at, version)
SELECT 
    p.proveedor_id,
    'OC-' || LPAD((ROW_NUMBER() OVER ())::text, 5, '0'),
    (ARRAY['BORRADOR', 'ENVIADA', 'RECIBIDA_PARCIAL', 'COMPLETADA'])[1 + floor(random() * 4)],
    NOW() - (random() * INTERVAL '10 days'),
    NOW() + (random() * INTERVAL '7 days'),
    100.0 + random() * 1000.0,
    'Orden de compra generada automáticamente',
    true,
    NOW(), NOW(), 0
FROM proveedor p
LIMIT 15;

-- Verificar
SELECT 'Ordenes de compra creadas' as mensaje, COUNT(*) as total FROM orden_compra;
