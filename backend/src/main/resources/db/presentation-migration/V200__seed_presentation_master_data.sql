-- =============================================================================
-- PRESENTATION SEED - Master data
-- T24: datos ricos de presentación/SIT para demo ERP de pastelería.
-- No usar contra datos reales.
-- =============================================================================

\echo '== T24 presentation seed: master data =='

INSERT INTO core.erp_migration_marker (codigo, descripcion)
VALUES ('T24_PRESENTATION_SIT_SEED', 'Datos de presentación/SIT para validar la pastelería ERP de extremo a extremo.')
ON CONFLICT (codigo) DO NOTHING;

-- Clientes de presentación.
INSERT INTO cliente (nombre_completo, telefono, correo, observaciones, fecha_registro, created_at, updated_at, version)
SELECT 'Dulce Aurora Eventos', '0998100101', 'eventos@dulceaurora.test', 'Cliente corporativo para mesas dulces y coffee breaks.', NOW(), NOW(), NOW(), 0
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE correo = 'eventos@dulceaurora.test');

INSERT INTO cliente (nombre_completo, telefono, correo, observaciones, fecha_registro, created_at, updated_at, version)
SELECT 'Laura Mendoza', '0998100102', 'laura.mendoza@sit.test', 'Cliente recurrente para tortas familiares.', NOW(), NOW(), NOW(), 0
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE correo = 'laura.mendoza@sit.test');

INSERT INTO cliente (nombre_completo, telefono, correo, observaciones, fecha_registro, created_at, updated_at, version)
SELECT 'Colegio Santa Clara', '0998100103', 'compras@santaclara.test', 'Cliente institucional para pedidos por evento.', NOW(), NOW(), NOW(), 0
WHERE NOT EXISTS (SELECT 1 FROM cliente WHERE correo = 'compras@santaclara.test');

-- Proveedores de presentación.
INSERT INTO proveedor (codigo, nombre, telefono, correo, direccion, observaciones, activo, created_at, updated_at, version)
VALUES
('PROV-SIT-HARINA', 'Molinos del Puerto SIT', '0998200201', 'ventas@molinos-puerto.test', 'Guayaquil - zona norte', 'Proveedor de harina, azúcar y secos para demo/SIT.', TRUE, NOW(), NOW(), 0),
('PROV-SIT-EMPAQUES', 'Empaques Dulces SIT', '0998200202', 'contacto@empaques-dulces.test', 'Guayaquil - centro', 'Proveedor de cajas, bases y empaques para presentación.', TRUE, NOW(), NOW(), 0)
ON CONFLICT (codigo) DO UPDATE SET
  nombre = EXCLUDED.nombre,
  telefono = EXCLUDED.telefono,
  correo = EXCLUDED.correo,
  direccion = EXCLUDED.direccion,
  observaciones = EXCLUDED.observaciones,
  activo = TRUE,
  updated_at = NOW();

-- Productos adicionales para revisar catálogo y pedidos ERP.
INSERT INTO producto (
  categoria_id, codigo, slug, nombre, descripcion, precio_base, requiere_cotizacion, activo, publicado, receta_json, created_at, updated_at, version
) VALUES
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'TORTAS'), 'TORT-SIT-CHOCO-20', 'torta-sit-chocolate-20-porciones', 'Torta SIT Chocolate 20 porciones', 'Producto de presentación para pedidos, cartera y fiscalidad interna.', 96.00, FALSE, TRUE, TRUE, NULL, NOW(), NOW(), 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'ESPECIALES'), 'MESA-SIT-COFFEE-30', 'mesa-sit-coffee-break-30', 'Mesa SIT Coffee Break 30', 'Mesa dulce de presentación para cliente corporativo.', 150.00, TRUE, TRUE, TRUE, NULL, NOW(), NOW(), 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'CUPCAKES'), 'CUPK-SIT-SURTIDO-12', 'cupcakes-sit-surtidos-12', 'Cupcakes SIT surtidos 12', 'Caja de cupcakes para pruebas de cotización y venta.', 36.00, FALSE, TRUE, TRUE, NULL, NOW(), NOW(), 0)
ON CONFLICT (codigo) DO UPDATE SET
  nombre = EXCLUDED.nombre,
  descripcion = EXCLUDED.descripcion,
  precio_base = EXCLUDED.precio_base,
  requiere_cotizacion = EXCLUDED.requiere_cotizacion,
  activo = TRUE,
  publicado = TRUE,
  updated_at = NOW();

-- Ingredientes e insumos para demo de stock, producción y compras.
INSERT INTO ingrediente (umedida_id, codigo, nombre, descripcion, stock_minimo, stock_actual, costo_referencial, activo, created_at, updated_at, version)
VALUES
((SELECT umedida_id FROM umedida WHERE codigo = 'kg'), 'ING-SIT-HARINA', 'Harina pastelera SIT', 'Harina base para tortas de presentación.', 8.0000, 26.0000, 1.65, TRUE, NOW(), NOW(), 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'kg'), 'ING-SIT-AZUCAR', 'Azúcar blanca SIT', 'Azúcar para masas y coberturas.', 6.0000, 18.0000, 1.20, TRUE, NOW(), NOW(), 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'u'), 'ING-SIT-HUEVO', 'Huevo fresco SIT', 'Huevos para producción de tortas y cupcakes.', 48.0000, 96.0000, 0.16, TRUE, NOW(), NOW(), 0)
ON CONFLICT (codigo) DO UPDATE SET
  nombre = EXCLUDED.nombre,
  descripcion = EXCLUDED.descripcion,
  stock_minimo = EXCLUDED.stock_minimo,
  stock_actual = EXCLUDED.stock_actual,
  costo_referencial = EXCLUDED.costo_referencial,
  activo = TRUE,
  updated_at = NOW();

INSERT INTO insumo (umedida_id, codigo, nombre, descripcion, stock_minimo, stock_actual, costo_referencial, activo, created_at, updated_at, version)
VALUES
((SELECT umedida_id FROM umedida WHERE codigo = 'u'), 'INS-SIT-CAJA20', 'Caja torta 20 SIT', 'Caja para torta de 20 porciones.', 10.0000, 34.0000, 0.85, TRUE, NOW(), NOW(), 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'u'), 'INS-SIT-BASE', 'Base dorada SIT', 'Base rígida para torta de presentación.', 10.0000, 28.0000, 0.55, TRUE, NOW(), NOW(), 0)
ON CONFLICT (codigo) DO UPDATE SET
  nombre = EXCLUDED.nombre,
  descripcion = EXCLUDED.descripcion,
  stock_minimo = EXCLUDED.stock_minimo,
  stock_actual = EXCLUDED.stock_actual,
  costo_referencial = EXCLUDED.costo_referencial,
  activo = TRUE,
  updated_at = NOW();

-- Relación proveedor-artículo.
INSERT INTO item_proveedor (item_tipo, item_id, proveedor_id, precio_suministro, es_principal, activo, created_at)
SELECT 'INGREDIENTE', i.ingrediente_id, p.proveedor_id, 1.60, TRUE, TRUE, NOW()
FROM ingrediente i, proveedor p
WHERE i.codigo = 'ING-SIT-HARINA'
  AND p.codigo = 'PROV-SIT-HARINA'
  AND NOT EXISTS (
    SELECT 1 FROM item_proveedor ip WHERE ip.item_tipo = 'INGREDIENTE' AND ip.item_id = i.ingrediente_id AND ip.proveedor_id = p.proveedor_id
  );

INSERT INTO item_proveedor (item_tipo, item_id, proveedor_id, precio_suministro, es_principal, activo, created_at)
SELECT 'INSUMO', ins.insumo_id, p.proveedor_id, 0.80, TRUE, TRUE, NOW()
FROM insumo ins, proveedor p
WHERE ins.codigo = 'INS-SIT-CAJA20'
  AND p.codigo = 'PROV-SIT-EMPAQUES'
  AND NOT EXISTS (
    SELECT 1 FROM item_proveedor ip WHERE ip.item_tipo = 'INSUMO' AND ip.item_id = ins.insumo_id AND ip.proveedor_id = p.proveedor_id
  );

-- Unificación de terceros para los clientes/proveedores de presentación.
DO $$
DECLARE
  r RECORD;
  v_tercero_id BIGINT;
BEGIN
  FOR r IN
    SELECT cliente_id AS legacy_id, nombre_completo AS nombre, telefono, correo, observaciones, 'CLIENTE' AS perfil, NULL::TEXT AS codigo
    FROM cliente
    WHERE correo IN ('eventos@dulceaurora.test', 'laura.mendoza@sit.test', 'compras@santaclara.test')
    UNION ALL
    SELECT proveedor_id AS legacy_id, nombre, telefono, correo, observaciones, 'PROVEEDOR' AS perfil, codigo
    FROM proveedor
    WHERE codigo IN ('PROV-SIT-HARINA', 'PROV-SIT-EMPAQUES')
  LOOP
    SELECT tercero_id INTO v_tercero_id
    FROM tercero
    WHERE correo = r.correo
    LIMIT 1;

    IF v_tercero_id IS NULL THEN
      INSERT INTO tercero (tipo_identificacion, numero_identificacion, nombre_legal, nombre_comercial, telefono, correo, observaciones, activo, created_at, updated_at, version)
      VALUES ('NO_ESPECIFICADA', NULL, r.nombre, r.nombre, r.telefono, r.correo, r.observaciones, TRUE, NOW(), NOW(), 0)
      RETURNING tercero_id INTO v_tercero_id;
    END IF;

    IF r.perfil = 'CLIENTE' THEN
      UPDATE cliente SET tercero_id = v_tercero_id, updated_at = NOW() WHERE cliente_id = r.legacy_id;
      INSERT INTO cliente_perfil (tercero_id, cliente_id, estado, fecha_alta)
      VALUES (v_tercero_id, r.legacy_id, 'ACTIVO', NOW())
      ON CONFLICT (cliente_id) DO NOTHING;
    ELSE
      UPDATE proveedor SET tercero_id = v_tercero_id, updated_at = NOW() WHERE proveedor_id = r.legacy_id;
      INSERT INTO proveedor_perfil (tercero_id, proveedor_id, codigo_proveedor, estado, fecha_alta)
      VALUES (v_tercero_id, r.legacy_id, r.codigo, 'ACTIVO', NOW())
      ON CONFLICT (proveedor_id) DO NOTHING;
    END IF;
  END LOOP;
END $$;
