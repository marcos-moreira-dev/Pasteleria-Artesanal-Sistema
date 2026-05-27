-- =============================================================================
-- PASTELERIA - SEED CANÓNICO DE ARRANQUE
-- Version: 2026-03-26
-- Criterio:
-- - usa los codigos de producto actuales
-- - usa estados vigentes del backend
-- - cubre datos de arranque para comercial, produccion, abastecimiento y reportes
-- =============================================================================

BEGIN;

INSERT INTO rol_usuario (codigo, nombre_rol, descripcion, activo, created_at) VALUES
('ADMIN', 'ADMINISTRADOR', 'Control completo del sistema', TRUE, NOW()),
('ATENCION', 'ATENCION', 'Registro y seguimiento comercial', TRUE, NOW()),
('PRODUCCION', 'PRODUCCION', 'Seguimiento del flujo operativo', TRUE, NOW());

INSERT INTO usuario_sistema (
  rol_id, nombre_usuario, password_hash, nombres, apellidos, correo, activo, created_at, updated_at, version
) VALUES
((SELECT rol_id FROM rol_usuario WHERE codigo = 'ADMIN'), 'admin', '$2a$10$FD9cVwV0RU1adR/UbFsdH.eXX/ko0nb8is1UIf7DjPwnc4gl7hTA6', 'Admin', 'Pasteleria', 'admin@pasteleria.local', TRUE, NOW(), NOW(), 0),
((SELECT rol_id FROM rol_usuario WHERE codigo = 'ATENCION'), 'atencion1', '$2a$10$bzIr6m.x1zKTWPC5EsiFye0SDV.kU8ZWzpodr5lfHr9shBZ/UelEC', 'Ana', 'Atencion', 'atencion1@pasteleria.local', TRUE, NOW(), NOW(), 0),
((SELECT rol_id FROM rol_usuario WHERE codigo = 'PRODUCCION'), 'produccion1', '$2a$10$jVVgAGAti/qofTb5LFB5kOaT/.h3Bt8tIdhpcjj.hPY4flDHVjdrq', 'Pedro', 'Produccion', 'produccion1@pasteleria.local', TRUE, NOW(), NOW(), 0);

INSERT INTO caja_operativa (sucursal_codigo, codigo, nombre, tipo_caja, moneda, saldo_actual, activa, created_at, updated_at, version)
VALUES ('MATRIZ', 'CAJA_MATRIZ', 'Caja principal', 'VENTA', 'USD', 0.00, TRUE, NOW(), NOW(), 0)
ON CONFLICT (codigo) DO NOTHING;

INSERT INTO umedida (codigo, nombre, abreviatura, tipo, decimales, activo, created_at, updated_at, version) VALUES
('g', 'gramo', 'g', 'PESO', 2, TRUE, NOW(), NOW(), 0),
('kg', 'kilogramo', 'kg', 'PESO', 3, TRUE, NOW(), NOW(), 0),
('ml', 'mililitro', 'ml', 'VOLUMEN', 3, TRUE, NOW(), NOW(), 0),
('l', 'litro', 'L', 'VOLUMEN', 3, TRUE, NOW(), NOW(), 0),
('u', 'unidad', 'u', 'UNIDAD', 0, TRUE, NOW(), NOW(), 0),
('paq', 'paquete', 'paq', 'ENVASE', 0, TRUE, NOW(), NOW(), 0),
('bandeja', 'bandeja', 'band.', 'ENVASE', 0, TRUE, NOW(), NOW(), 0);

INSERT INTO categoria_producto (codigo, nombre, descripcion, orden_visual, activo, created_at, updated_at, version) VALUES
('TORTAS', 'TORTAS', 'Tortas principales del catalogo', 1, TRUE, NOW(), NOW(), 0),
('POSTRES', 'POSTRES', 'Postres individuales y familiares', 2, TRUE, NOW(), NOW(), 0),
('GALLETAS', 'GALLETAS', 'Galletas decoradas y artesanales', 3, TRUE, NOW(), NOW(), 0),
('BEBIDAS', 'BEBIDAS', 'Bebidas de apoyo para el catalogo', 4, TRUE, NOW(), NOW(), 0),
('CUPCAKES', 'CUPCAKES', 'Cupcakes decorados y tradicionales', 5, TRUE, NOW(), NOW(), 0),
('ESPECIALES', 'ESPECIALES', 'Productos especiales y mesas dulces', 6, TRUE, NOW(), NOW(), 0);

INSERT INTO producto (
  categoria_id, codigo, slug, nombre, descripcion, precio_base, requiere_cotizacion, activo, publicado, receta_json, created_at, updated_at, version
) VALUES
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'TORTAS'), 'TORT-CHOC-01', 'torta-chocolate-mediana', 'Torta de Chocolate', 'Deliciosa torta de chocolate para celebraciones', 150.00, FALSE, TRUE, TRUE, NULL, NOW(), NOW(), 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'TORTAS'), 'TORT-VAIN-FRUT', 'torta-vainilla-frutos', 'Torta Vainilla con Frutos', 'Torta de vainilla con frutos rojos', 160.00, FALSE, TRUE, TRUE, NULL, NOW(), NOW(), 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'TORTAS'), 'TORT-RED-VEL', 'torta-red-velvet-grande', 'Torta Red Velvet', 'Elegante torta red velvet', 180.00, FALSE, TRUE, TRUE, NULL, NOW(), NOW(), 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'TORTAS'), 'TORT-3-LECH', 'torta-tres-leches-pequena', 'Torta Tres Leches', 'Tradicional torta tres leches', 140.00, FALSE, TRUE, TRUE, NULL, NOW(), NOW(), 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'POSTRES'), 'BROWNIE-IND', 'brownie-individual', 'Brownie Individual', 'Brownie artesanal', 25.00, FALSE, TRUE, TRUE, NULL, NOW(), NOW(), 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'POSTRES'), 'ALFAJOR-PREM', 'alfajor-premium', 'Alfajor Premium', 'Alfajor premium con dulce de leche', 15.00, FALSE, TRUE, TRUE, NULL, NOW(), NOW(), 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'POSTRES'), 'CHEESE-FRUT', 'cheesecake-frutos-rojos', 'Cheesecake Frutos Rojos', 'Cheesecake con topping de frutos rojos', 45.00, FALSE, TRUE, TRUE, NULL, NOW(), NOW(), 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'GALLETAS'), 'GALLETA-DEC', 'galleta-decorada', 'Galleta Decorada', 'Galleta artesanal con decoracion personalizada', 12.00, FALSE, TRUE, TRUE, NULL, NOW(), NOW(), 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'BEBIDAS'), 'CAFE-AMER', 'cafe-americano', 'Cafe Americano', 'Cafe americano recien preparado', 18.00, FALSE, TRUE, TRUE, NULL, NOW(), NOW(), 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'CUPCAKES'), 'CUPK-VAIN-01', 'cupcake-vainilla', 'Cupcake Vainilla', 'Cupcake de vainilla con frosting', 20.00, FALSE, TRUE, TRUE, NULL, NOW(), NOW(), 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'CUPCAKES'), 'CUPK-CHOC-01', 'cupcake-chocolate', 'Cupcake Chocolate', 'Cupcake de chocolate con ganache', 22.00, FALSE, TRUE, TRUE, NULL, NOW(), NOW(), 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'ESPECIALES'), 'MESA-DULCE-25', 'mesa-dulce-25-personas', 'Mesa Dulce 25', 'Mesa dulce completa para 25 personas', 350.00, TRUE, TRUE, TRUE, NULL, NOW(), NOW(), 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'ESPECIALES'), 'MESA-DULCE-50', 'mesa-dulce-50-personas', 'Mesa Dulce 50', 'Mesa dulce completa para 50 personas', 650.00, TRUE, TRUE, TRUE, NULL, NOW(), NOW(), 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'ESPECIALES'), 'CAJA-MINI', 'caja-mini-postres', 'Caja Mini Postres', 'Caja surtida de mini postres', 85.00, FALSE, TRUE, TRUE, NULL, NOW(), NOW(), 0);

INSERT INTO cliente (nombre_completo, telefono, correo, observaciones, fecha_registro, created_at, updated_at, version) VALUES
('Maria Lopez', '0990000001', 'maria.lopez@example.com', 'Cliente frecuente', NOW(), NOW(), NOW(), 0),
('Juan Perez', '0990000002', 'juan.perez@example.com', 'Prefiere retiro en local', NOW(), NOW(), NOW(), 0),
('Eventos Atlas', '0990000012', 'eventos.atlas@example.com', 'Cliente corporativo para coffee break y mesas dulces.', NOW(), NOW(), NOW(), 0),
('Camila Torres', '0990000014', 'camila.torres@example.com', 'Cliente frecuente de cheesecake y cupcakes.', NOW(), NOW(), NOW(), 0),
('Fernanda Solis', '0990000017', 'fernanda.solis@example.com', 'Interesada en mesas dulces para eventos.', NOW(), NOW(), NOW(), 0);

INSERT INTO cotizacion (cliente_id, codigo, estado_cotizacion, origen, observaciones, total_estimado, created_at, updated_at, version) VALUES
((SELECT cliente_id FROM cliente WHERE correo = 'maria.lopez@example.com'), 'COT-0001', 'PENDIENTE', 'PUBLICO', 'Cotizacion de torta tematica infantil.', 45.00, NOW(), NOW(), 0),
((SELECT cliente_id FROM cliente WHERE correo = 'juan.perez@example.com'), 'COT-0002', 'APROBADA', 'INTERNO', 'Cotizacion de postres para mesa dulce.', 250.00, NOW(), NOW(), 0),
((SELECT cliente_id FROM cliente WHERE correo = 'eventos.atlas@example.com'), 'COT-0102', 'CONVERTIDA', 'INTERNO', 'Propuesta corporativa convertida a pedido.', 810.00, NOW(), NOW(), 0),
((SELECT cliente_id FROM cliente WHERE correo = 'camila.torres@example.com'), 'COT-0105', 'CONVERTIDA', 'PUBLICO', 'Cotizacion familiar convertida a pedido.', 309.00, NOW(), NOW(), 0),
((SELECT cliente_id FROM cliente WHERE correo = 'fernanda.solis@example.com'), 'COT-0106', 'APROBADA', 'PUBLICO', 'Cotizacion aprobada pendiente de fecha definitiva.', 485.00, NOW(), NOW(), 0);

INSERT INTO cotizacion_detalle (cotizacion_id, producto_id, descripcion_item, cantidad, precio_estimado, subtotal, notas, created_at) VALUES
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0001'), NULL, 'Torta tematica de dos pisos con decoracion personalizada', 1, 45.00, 45.00, 'Detalle abierto para cotizacion artesanal', NOW()),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0002'), (SELECT producto_id FROM producto WHERE codigo = 'BROWNIE-IND'), 'Brownie individual', 10, 25.00, 250.00, NULL, NOW()),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0102'), (SELECT producto_id FROM producto WHERE codigo = 'MESA-DULCE-25'), 'Mesa dulce 25 personas', 1, 350.00, 350.00, 'Incluye montaje basico en oficina.', NOW()),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0102'), (SELECT producto_id FROM producto WHERE codigo = 'CAFE-AMER'), 'Cafe americano', 20, 18.00, 360.00, 'Servicio para jornada de tarde.', NOW()),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0102'), (SELECT producto_id FROM producto WHERE codigo = 'BROWNIE-IND'), 'Brownie individual', 4, 25.00, 100.00, NULL, NOW()),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0105'), (SELECT producto_id FROM producto WHERE codigo = 'CHEESE-FRUT'), 'Cheesecake de frutos rojos', 1, 45.00, 45.00, NULL, NOW()),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0105'), (SELECT producto_id FROM producto WHERE codigo = 'CUPK-CHOC-01'), 'Cupcake de chocolate', 12, 22.00, 264.00, NULL, NOW()),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0106'), (SELECT producto_id FROM producto WHERE codigo = 'TORT-VAIN-FRUT'), 'Torta de vainilla y frutos', 1, 160.00, 160.00, NULL, NOW()),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0106'), (SELECT producto_id FROM producto WHERE codigo = 'CAJA-MINI'), 'Caja de mini postres', 1, 85.00, 85.00, NULL, NOW()),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0106'), (SELECT producto_id FROM producto WHERE codigo = 'CUPK-VAIN-01'), 'Cupcake de vainilla', 12, 20.00, 240.00, NULL, NOW());

INSERT INTO pedido (
  cliente_id, cotizacion_id, codigo, fecha_entrega_estimada, estado_pedido, prioridad, origen, observaciones, total_estimado, fecha_pedido, created_at, updated_at, version
) VALUES
((SELECT cliente_id FROM cliente WHERE correo = 'maria.lopez@example.com'), NULL, 'PED-0001', NOW() + INTERVAL '2 day', 'REGISTRADO', 'NORMAL', 'INTERNO', 'Pedido directo de mostrador.', 150.00, NOW(), NOW(), NOW(), 0),
((SELECT cliente_id FROM cliente WHERE correo = 'eventos.atlas@example.com'), (SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0102'), 'PED-0102', NOW() + INTERVAL '1 day', 'EN_PREPARACION', 'URGENTE', 'INTERNO', 'Pedido corporativo con entrega temprana.', 810.00, NOW(), NOW(), NOW(), 0),
((SELECT cliente_id FROM cliente WHERE correo = 'camila.torres@example.com'), (SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0105'), 'PED-0103', NOW() + INTERVAL '2 day', 'LISTO', 'NORMAL', 'PUBLICO', 'Pedido convertido desde la landing publica.', 309.00, NOW(), NOW(), NOW(), 0),
((SELECT cliente_id FROM cliente WHERE correo = 'juan.perez@example.com'), NULL, 'PED-0003', NOW() + INTERVAL '1 day', 'LISTO', 'NORMAL', 'INTERNO', 'Pedido para retiro en tienda.', 100.00, NOW(), NOW(), NOW(), 0),
((SELECT cliente_id FROM cliente WHERE correo = 'fernanda.solis@example.com'), NULL, 'PED-0107', NOW() + INTERVAL '5 day', 'REGISTRADO', 'URGENTE', 'INTERNO', 'Mesa dulce reservada para evento de fin de semana.', 350.00, NOW(), NOW(), NOW(), 0);

INSERT INTO pedido_detalle (pedido_id, producto_id, descripcion_item, cantidad, precio_unitario, subtotal, notas, created_at) VALUES
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0001'), (SELECT producto_id FROM producto WHERE codigo = 'TORT-CHOC-01'), 'Torta de chocolate mediana', 1, 150.00, 150.00, NULL, NOW()),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0102'), (SELECT producto_id FROM producto WHERE codigo = 'MESA-DULCE-25'), 'Mesa dulce 25 personas', 1, 350.00, 350.00, NULL, NOW()),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0102'), (SELECT producto_id FROM producto WHERE codigo = 'CAFE-AMER'), 'Cafe americano', 20, 18.00, 360.00, NULL, NOW()),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0102'), (SELECT producto_id FROM producto WHERE codigo = 'BROWNIE-IND'), 'Brownie individual', 4, 25.00, 100.00, NULL, NOW()),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0103'), (SELECT producto_id FROM producto WHERE codigo = 'CHEESE-FRUT'), 'Cheesecake de frutos rojos', 1, 45.00, 45.00, NULL, NOW()),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0103'), (SELECT producto_id FROM producto WHERE codigo = 'CUPK-CHOC-01'), 'Cupcake de chocolate', 12, 22.00, 264.00, NULL, NOW()),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0003'), (SELECT producto_id FROM producto WHERE codigo = 'BROWNIE-IND'), 'Brownie individual', 4, 25.00, 100.00, NULL, NOW()),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0107'), (SELECT producto_id FROM producto WHERE codigo = 'MESA-DULCE-25'), 'Mesa dulce 25 personas', 1, 350.00, 350.00, NULL, NOW());

INSERT INTO produccion (
  pedido_id, estado_produccion, prioridad_produccion, fecha_inicio, fecha_finalizacion, observaciones_produccion, created_at, updated_at, version
) VALUES
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0001'), 'PENDIENTE', 'NORMAL', NULL, NULL, 'Pendiente de asignacion a produccion', NOW(), NOW(), 0),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0102'), 'PREPARACION', 'URGENTE', NOW() - INTERVAL '4 hour', NULL, 'Mesa dulce en armado y bebidas en despacho.', NOW(), NOW(), 0),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0103'), 'FINALIZADO', 'NORMAL', NOW() - INTERVAL '1 day', NOW() - INTERVAL '2 hour', 'Pedido listo para retiro', NOW(), NOW(), 0),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0003'), 'FINALIZADO', 'NORMAL', NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 hour', 'Pedido listo para retiro en tienda', NOW(), NOW(), 0),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0107'), 'PENDIENTE', 'URGENTE', NULL, NULL, 'Reservado para produccion anticipada.', NOW(), NOW(), 0);

INSERT INTO ingrediente (umedida_id, codigo, nombre, descripcion, stock_minimo, stock_actual, costo_referencial, activo, created_at, updated_at, version) VALUES
((SELECT umedida_id FROM umedida WHERE codigo = 'kg'), 'HAR-001', 'Harina de trigo 0000', 'Harina refinada para reposteria', 5.0000, 25.0000, 1.20, TRUE, NOW(), NOW(), 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'kg'), 'AZU-001', 'Azucar blanca refinada', 'Azucar fina para reposteria', 3.0000, 15.0000, 1.10, TRUE, NOW(), NOW(), 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'u'), 'HUE-001', 'Huevos frescos', 'Huevos de gallina categoria A', 200.0000, 800.0000, 0.50, TRUE, NOW(), NOW(), 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'kg'), 'MAN-001', 'Mantequilla sin sal', 'Mantequilla premium', 2.0000, 8.0000, 4.50, TRUE, NOW(), NOW(), 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'kg'), 'CHO-001', 'Chocolate cobertura negro', 'Cobertura 55% cacao', 2.0000, 6.0000, 5.80, TRUE, NOW(), NOW(), 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'l'), 'ESC-001', 'Esencia de vainilla', 'Esencia pura de vainilla', 0.0500, 0.2000, 8.00, TRUE, NOW(), NOW(), 0);

INSERT INTO insumo (umedida_id, codigo, nombre, descripcion, stock_minimo, stock_actual, costo_referencial, activo, created_at, updated_at, version) VALUES
((SELECT umedida_id FROM umedida WHERE codigo = 'paq'), 'MOL-001', 'Molde papel redondo 20cm', 'Molde desechable mediano', 150.0000, 500.0000, 0.45, TRUE, NOW(), NOW(), 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'paq'), 'CAJ-001', 'Caja pastel pequena', 'Caja carton 20x20x10cm', 100.0000, 300.0000, 0.80, TRUE, NOW(), NOW(), 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'paq'), 'CAJ-004', 'Caja cupcake 6 unidades', 'Caja especial para cupcakes', 150.0000, 600.0000, 0.65, TRUE, NOW(), NOW(), 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'bandeja'), 'BAN-001', 'Bandeja aluminio', 'Bandeja aluminio desechable', 50.0000, 150.0000, 1.50, TRUE, NOW(), NOW(), 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'paq'), 'VEL-001', 'Velas numericas', 'Velas numeros surtidas', 100.0000, 400.0000, 0.20, TRUE, NOW(), NOW(), 0);

INSERT INTO proveedor (codigo, nombre, telefono, correo, direccion, observaciones, activo, created_at, updated_at, version) VALUES
('PROV-001', 'Distribuidora El Norte S.A.', '04-2100-9901', 'ventas@elnorte.com', 'Av. Carlos Julio Arosemena Km 8, Guayaquil', 'Proveedor principal de harinas y azucares.', TRUE, NOW(), NOW(), 0),
('PROV-002', 'Lacteos RioCasa Ecuador', '03-2400-1234', 'pedidos@riocasa.com', 'Panamericana Norte Km 5, Riobamba', 'Lacteos frescos y mantequilla.', TRUE, NOW(), NOW(), 0),
('PROV-003', 'Importadora Sabores Premium', '04-2300-5678', 'compras@saborespremium.com', 'Cdla. Kennedy Norte, Guayaquil', 'Chocolate, vainilla y esencias importadas.', TRUE, NOW(), NOW(), 0),
('PROV-005', 'Papeles y Empaques del Guayas', '04-2600-7788', 'ventas@pyeguayas.com', 'Via a Daule Km 4.5, Guayaquil', 'Cajas, moldes y bolsas.', TRUE, NOW(), NOW(), 0);

INSERT INTO item_proveedor (item_tipo, item_id, proveedor_id, precio_suministro, es_principal, activo, created_at) VALUES
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'HAR-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-001'), 1.15, TRUE, TRUE, NOW()),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'AZU-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-001'), 1.05, TRUE, TRUE, NOW()),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'MAN-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-002'), 4.30, TRUE, TRUE, NOW()),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'CHO-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-003'), 5.50, TRUE, TRUE, NOW()),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CAJ-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-005'), 0.75, TRUE, TRUE, NOW()),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CAJ-004'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-005'), 0.62, TRUE, TRUE, NOW());

INSERT INTO orden_compra (
  proveedor_id, codigo, estado, fecha_emision, fecha_entrega_esperada, fecha_entrega_real, total_estimado, observaciones, activo, created_at, updated_at, version
) VALUES
((SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-001'), 'OC-2026-0014', 'BORRADOR', NOW() - INTERVAL '1 day', NOW() + INTERVAL '7 day', NULL, 19900.00, 'Reposicion mensual harinas y azucares', TRUE, NOW(), NOW(), 0),
((SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-002'), 'OC-2026-0015', 'RECIBIDA_PARCIAL', NOW() - INTERVAL '3 day', NOW() - INTERVAL '1 day', NULL, 17400.00, 'Pedido lacteos con recepcion parcial', TRUE, NOW(), NOW(), 0),
((SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-003'), 'OC-2026-0016', 'ENVIADA', NOW() - INTERVAL '2 day', NOW() + INTERVAL '3 day', NULL, 11780.00, 'Chocolates y esencias - pedido urgente', TRUE, NOW(), NOW(), 0),
((SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-005'), 'OC-2026-0017', 'RECIBIDA', NOW() - INTERVAL '6 day', NOW() - INTERVAL '3 day', NOW() - INTERVAL '3 day', 274.00, 'Empaques para temporada alta', TRUE, NOW(), NOW(), 0);

INSERT INTO orden_compra_detalle (
  orden_compra_id, item_tipo, item_id, item_nombre, item_codigo, cantidad, precio_unitario, subtotal, cantidad_recibida, observaciones, created_at
) VALUES
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0014'), 'INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'HAR-001'), 'Harina de trigo 0000', 'HAR-001', 10000, 1.15, 11500.00, 0, 'Pendiente de envio', NOW()),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0014'), 'INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'AZU-001'), 'Azucar blanca refinada', 'AZU-001', 8000, 1.05, 8400.00, 0, 'Pendiente de envio', NOW()),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0015'), 'INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'MAN-001'), 'Mantequilla sin sal', 'MAN-001', 4000, 4.30, 17200.00, 2000, 'Primera entrega recibida', NOW()),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0016'), 'INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'CHO-001'), 'Chocolate cobertura negro', 'CHO-001', 2000, 5.50, 11000.00, 0, 'En espera del proveedor', NOW()),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0017'), 'INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CAJ-001'), 'Caja pastel pequena', 'CAJ-001', 200, 0.75, 150.00, 200, 'Recepcion completa', NOW()),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0017'), 'INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CAJ-004'), 'Caja cupcake 6 unidades', 'CAJ-004', 200, 0.62, 124.00, 200, 'Recepcion completa', NOW());

INSERT INTO inventario_movimiento (
  item_tipo, item_id, tipo_movimiento, cantidad, saldo_posterior, referencia_tipo, referencia_id, motivo_salida, observaciones, fecha_movimiento, registrado_por_user_id
) VALUES
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'HAR-001'), 'ENTRADA_COMPRA', 15.000, 25.000, 'ORDEN_COMPRA', 'OC-2026-0014', NULL, 'Compra inicial harina 0000', NOW() - INTERVAL '10 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'AZU-001'), 'ENTRADA_COMPRA', 10.000, 15.000, 'ORDEN_COMPRA', 'OC-2026-0014', NULL, 'Compra inicial azucar blanca', NOW() - INTERVAL '10 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'MAN-001'), 'ENTRADA_COMPRA', 3.000, 8.000, 'ORDEN_COMPRA', 'OC-2026-0015', NULL, 'Recepcion mantequilla', NOW() - INTERVAL '4 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CAJ-001'), 'ENTRADA_COMPRA', 200.000, 300.000, 'ORDEN_COMPRA', 'OC-2026-0017', NULL, 'Recepcion de cajas', NOW() - INTERVAL '3 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'HAR-001'), 'SALIDA_PRODUCCION', 2.500, 22.500, 'PEDIDO', 'PED-0102', 'Produccion regular', 'Consumo para mesa dulce corporativa', NOW() - INTERVAL '2 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1'));

INSERT INTO receta (producto_id, nombre, rendimiento_base, costo_estimado, observaciones, es_activa, created_at, updated_at, created_by_user_id, version) VALUES
((SELECT producto_id FROM producto WHERE codigo = 'TORT-CHOC-01'), 'Torta de Chocolate Mediana - Receta Base', 1.00, 18.50, 'Receta para torta de chocolate mediana.', TRUE, NOW(), NOW(), (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 0),
((SELECT producto_id FROM producto WHERE codigo = 'BROWNIE-IND'), 'Brownie Individual - Receta Base', 12.00, 8.20, 'Receta para brownie estilo americano.', TRUE, NOW(), NOW(), (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 0),
((SELECT producto_id FROM producto WHERE codigo = 'CUPK-VAIN-01'), 'Cupcake de Vainilla - Receta Base', 12.00, 9.50, 'Receta para cupcakes de vainilla.', TRUE, NOW(), NOW(), (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 0);

INSERT INTO detalle_receta (receta_id, ingrediente_id, cantidad_base, rendimiento_por_unidad, es_para_porcion, observaciones, created_at) VALUES
((SELECT receta_id FROM receta WHERE nombre = 'Torta de Chocolate Mediana - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'HAR-001'), 500.0000, 1.0000, TRUE, 'Harina 0000 cernida', NOW()),
((SELECT receta_id FROM receta WHERE nombre = 'Torta de Chocolate Mediana - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'AZU-001'), 400.0000, 1.0000, TRUE, 'Azucar blanca refinada', NOW()),
((SELECT receta_id FROM receta WHERE nombre = 'Torta de Chocolate Mediana - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'HUE-001'), 6.0000, 1.0000, TRUE, 'Huevos frescos', NOW()),
((SELECT receta_id FROM receta WHERE nombre = 'Torta de Chocolate Mediana - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'MAN-001'), 250.0000, 1.0000, TRUE, 'Mantequilla a temperatura ambiente', NOW()),
((SELECT receta_id FROM receta WHERE nombre = 'Brownie Individual - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'HAR-001'), 200.0000, 12.0000, TRUE, 'Harina 0000', NOW()),
((SELECT receta_id FROM receta WHERE nombre = 'Brownie Individual - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'AZU-001'), 350.0000, 12.0000, TRUE, 'Azucar blanca', NOW()),
((SELECT receta_id FROM receta WHERE nombre = 'Cupcake de Vainilla - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'HAR-001'), 200.0000, 12.0000, TRUE, 'Harina cernida', NOW()),
((SELECT receta_id FROM receta WHERE nombre = 'Cupcake de Vainilla - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'ESC-001'), 0.0100, 12.0000, TRUE, 'Esencia de vainilla', NOW());

INSERT INTO archivo_recurso (
  codigo_archivo, origen_modulo, tipo_archivo, nombre_original, nombre_fisico, mime_type, extension, tamano_bytes, checksum, ruta_relativa, estado, fecha_creacion, fecha_expiracion, creado_por_usuario_id
) VALUES
('ARC-REP-0101', 'REPORTES', 'CSV', 'pedidos-dia.csv', 'pedidos-dia.csv', 'text/csv', '.csv', 48210, 'chk-pedidos-0101', 'pedidos-dia.csv', 'DISPONIBLE', NOW() - INTERVAL '2 hour', NOW() + INTERVAL '7 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'));

INSERT INTO job_reporte (
  codigo_job, tipo_reporte, parametros_json, estado, solicitado_por_usuario_id, fecha_solicitud, fecha_inicio, fecha_fin, intentos, mensaje_error, archivo_id, request_id, version
) VALUES
('JOB-REP-0101', 'RESUMEN_NEGOCIO', '{"fecha":"2026-03-21","estado":"TODOS"}'::jsonb, 'COMPLETADO', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), NOW() - INTERVAL '3 hour', NOW() - INTERVAL '2 hour 58 minute', NOW() - INTERVAL '2 hour 57 minute', 1, NULL, (SELECT archivo_id FROM archivo_recurso WHERE codigo_archivo = 'ARC-REP-0101'), 'req-reporte-0101', 1);

INSERT INTO notificacion (
  tipo_notificacion, titulo, mensaje, modulo, referencia_tipo, referencia_id, usuario_destino_id, estado, prioridad, payload_json, fecha_creacion
) VALUES
('REPORTE_LISTO', 'Reporte diario disponible', 'El archivo de pedidos del dia ya puede descargarse.', 'REPORTES', 'JOB_REPORTE', 'JOB-REP-0101', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 'NO_LEIDA', 'MEDIA', '{"archivo":"ARC-REP-0101"}'::jsonb, NOW() - INTERVAL '1 hour'),
('PEDIDO_URGENTE', 'Pedido urgente en preparacion', 'El pedido PED-0102 requiere seguimiento prioritario.', 'PEDIDOS', 'PEDIDO', 'PED-0102', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1'), 'NO_LEIDA', 'ALTA', '{"pedidoCodigo":"PED-0102"}'::jsonb, NOW() - INTERVAL '30 minute');

INSERT INTO auditoria_evento (
  codigo_evento, modulo, entidad, entidad_id, accion, actor_usuario_id, actor_rol, fecha_evento, valor_anterior_json, valor_nuevo_json, motivo, request_id, ip_origen
) VALUES
('CLIENTE_CREADO', 'CLIENTES', 'cliente', 'maria.lopez@example.com', 'CREAR_CLIENTE', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'atencion1'), 'ATENCION', NOW() - INTERVAL '2 day', NULL, '{"correo":"maria.lopez@example.com"}'::jsonb, 'Alta manual desde mostrador.', 'req-audit-0101', '127.0.0.1'),
('PRODUCCION_ESTADO_ACTUALIZADO', 'PRODUCCION', 'produccion', 'PED-0102', 'ACTUALIZAR_ESTADO_PRODUCCION', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1'), 'PRODUCCION', NOW() - INTERVAL '2 hour', '{"estado":"PENDIENTE"}'::jsonb, '{"estado":"PREPARACION"}'::jsonb, 'Inicio de armado de mesa dulce.', 'req-audit-0102', '127.0.0.1');

-- =============================================================================
-- EXTENSION MASIVA DE DATOS OPERATIVOS
-- =============================================================================

INSERT INTO producto (
  categoria_id, codigo, slug, nombre, descripcion, precio_base, requiere_cotizacion, activo, publicado, receta_json, created_at, updated_at, version
) VALUES
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'TORTAS'), 'TORT-ZANA-01', 'torta-zanahoria-nuez', 'Torta Zanahoria y Nuez', 'Torta especiada con frosting de queso crema', 155.00, FALSE, TRUE, TRUE, NULL, NOW() - INTERVAL '20 day', NOW() - INTERVAL '2 day', 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'TORTAS'), 'TORT-BODA-01', 'naked-cake-boda', 'Naked Cake de Boda', 'Torta de varios pisos para bodas y celebraciones grandes', 480.00, TRUE, TRUE, TRUE, NULL, NOW() - INTERVAL '18 day', NOW() - INTERVAL '2 day', 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'POSTRES'), 'TIRA-FAM-01', 'tiramisu-familiar', 'Tiramisu Familiar', 'Postre familiar con cacao y crema suave', 58.00, FALSE, TRUE, TRUE, NULL, NOW() - INTERVAL '17 day', NOW() - INTERVAL '1 day', 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'POSTRES'), 'PIE-LIMON-01', 'pie-limon-artesanal', 'Pie de Limon', 'Pie artesanal con crema de limon y merengue', 48.00, FALSE, TRUE, TRUE, NULL, NOW() - INTERVAL '16 day', NOW() - INTERVAL '1 day', 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'GALLETAS'), 'GALL-MIX-01', 'galletas-mix-mantequilla', 'Galletas Mix Mantequilla', 'Surtido de galletas de mantequilla y cacao', 14.00, FALSE, TRUE, TRUE, NULL, NOW() - INTERVAL '15 day', NOW() - INTERVAL '1 day', 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'GALLETAS'), 'GALL-CORP-01', 'galleta-corporativa-logo', 'Galleta Corporativa Logo', 'Galleta decorada para regalos y activaciones de marca', 16.00, TRUE, TRUE, TRUE, NULL, NOW() - INTERVAL '15 day', NOW() - INTERVAL '1 day', 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'BEBIDAS'), 'FRAP-MOCHA', 'frappe-mocha', 'Frappe Mocha', 'Bebida fria de cafe y cacao', 19.00, FALSE, TRUE, TRUE, NULL, NOW() - INTERVAL '14 day', NOW() - INTERVAL '1 day', 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'BEBIDAS'), 'CHOC-CAL-01', 'chocolate-caliente-casa', 'Chocolate Caliente Casa', 'Chocolate caliente espeso para acompanar postres', 16.00, FALSE, TRUE, TRUE, NULL, NOW() - INTERVAL '14 day', NOW() - INTERVAL '1 day', 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'CUPCAKES'), 'CUPK-RED-01', 'cupcake-red-velvet', 'Cupcake Red Velvet', 'Cupcake red velvet con frosting cremoso', 21.00, FALSE, TRUE, TRUE, NULL, NOW() - INTERVAL '13 day', NOW() - INTERVAL '1 day', 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'CUPCAKES'), 'CUPK-OREO-01', 'cupcake-oreo', 'Cupcake Oreo', 'Cupcake oscuro con crema y galleta triturada', 23.00, FALSE, TRUE, TRUE, NULL, NOW() - INTERVAL '13 day', NOW() - INTERVAL '1 day', 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'ESPECIALES'), 'BRIGA-BOX-12', 'brigadeiro-box-12', 'Caja Brigadeiro 12', 'Caja surtida de brigadeiros premium', 72.00, FALSE, TRUE, TRUE, NULL, NOW() - INTERVAL '12 day', NOW() - INTERVAL '1 day', 0),
((SELECT categoria_id FROM categoria_producto WHERE codigo = 'ESPECIALES'), 'MESA-DULCE-80', 'mesa-dulce-80-personas', 'Mesa Dulce 80', 'Mesa dulce ampliada para eventos medianos y grandes', 980.00, TRUE, TRUE, TRUE, NULL, NOW() - INTERVAL '12 day', NOW() - INTERVAL '1 day', 0);

INSERT INTO cliente (nombre_completo, telefono, correo, observaciones, fecha_registro, created_at, updated_at, version) VALUES
('Lucia Herrera', '0990000020', 'lucia.herrera@example.com', 'Busca productos para celebraciones en casa.', NOW() - INTERVAL '14 day', NOW() - INTERVAL '14 day', NOW() - INTERVAL '14 day', 0),
('Sofia Almeida', '0990000021', 'sofia.almeida@example.com', 'Compra tortas y galletas para reuniones familiares.', NOW() - INTERVAL '12 day', NOW() - INTERVAL '12 day', NOW() - INTERVAL '12 day', 0),
('Diego Mera', '0990000022', 'diego.mera@example.com', 'Cliente ocasional para postres de oficina.', NOW() - INTERVAL '11 day', NOW() - INTERVAL '11 day', NOW() - INTERVAL '11 day', 0),
('Corporacion Brisa', '04-3900-2201', 'compras@corporacionbrisa.com', 'Cuenta corporativa para coffee break y cajas de regalo.', NOW() - INTERVAL '10 day', NOW() - INTERVAL '10 day', NOW() - INTERVAL '10 day', 0),
('Hotel Mirador', '04-3800-8812', 'eventos@hotelmirador.com', 'Solicita desayunos dulces y amenidades para eventos.', NOW() - INTERVAL '10 day', NOW() - INTERVAL '10 day', NOW() - INTERVAL '10 day', 0),
('Esteban Rojas', '0990000025', 'esteban.rojas@example.com', 'Prefiere coordinar pedidos por WhatsApp.', NOW() - INTERVAL '9 day', NOW() - INTERVAL '9 day', NOW() - INTERVAL '9 day', 0),
('Paola Cevallos', '0990000026', 'paola.cevallos@example.com', 'Cliente recurrente de bebidas y postres frios.', NOW() - INTERVAL '8 day', NOW() - INTERVAL '8 day', NOW() - INTERVAL '8 day', 0),
('Andrea Ponce', '0990000027', 'andrea.ponce@example.com', 'Solicita pedidos pequenos para oficina.', NOW() - INTERVAL '7 day', NOW() - INTERVAL '7 day', NOW() - INTERVAL '7 day', 0),
('Universidad Costa', '04-3600-1100', 'protocolo@ucosta.edu.ec', 'Eventos estudiantiles y coffee breaks institucionales.', NOW() - INTERVAL '7 day', NOW() - INTERVAL '7 day', NOW() - INTERVAL '7 day', 0),
('Bistro Nube', '04-3200-4455', 'compras@bistronube.com', 'Compra cajas dulces y postres para colaboraciones.', NOW() - INTERVAL '6 day', NOW() - INTERVAL '6 day', NOW() - INTERVAL '6 day', 0),
('Martina Vega', '0990000030', 'martina.vega@example.com', 'Le interesan tortas de temporada y cupcakes.', NOW() - INTERVAL '5 day', NOW() - INTERVAL '5 day', NOW() - INTERVAL '5 day', 0),
('Nicolas Vera', '0990000031', 'nicolas.vera@example.com', 'Solicito propuesta pero postergo la compra.', NOW() - INTERVAL '5 day', NOW() - INTERVAL '5 day', NOW() - INTERVAL '5 day', 0),
('Karen Montalvo', '0990000032', 'karen.montalvo@example.com', 'Necesita entregas rapidas para cumpleanos.', NOW() - INTERVAL '4 day', NOW() - INTERVAL '4 day', NOW() - INTERVAL '4 day', 0),
('Agencia Prisma', '04-3110-2200', 'produccion@agenciaprisma.com', 'Activa la marca con regalos corporativos y mesas dulces.', NOW() - INTERVAL '4 day', NOW() - INTERVAL '4 day', NOW() - INTERVAL '4 day', 0),
('Isabel Luna', '0990000034', 'isabel.luna@example.com', 'Cliente frecuente de cheesecake y cajas premium.', NOW() - INTERVAL '3 day', NOW() - INTERVAL '3 day', NOW() - INTERVAL '3 day', 0);

INSERT INTO cotizacion (cliente_id, codigo, estado_cotizacion, origen, observaciones, total_estimado, created_at, updated_at, version) VALUES
((SELECT cliente_id FROM cliente WHERE correo = 'lucia.herrera@example.com'), 'COT-0201', 'PENDIENTE', 'PUBLICO', 'Consulta por pie y galletas para brunch familiar.', 236.00, NOW() - INTERVAL '2 day', NOW() - INTERVAL '2 day', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'sofia.almeida@example.com'), 'COT-0202', 'APROBADA', 'PUBLICO', 'Propuesta para galletas personalizadas y torta principal.', 800.00, NOW() - INTERVAL '5 day', NOW() - INTERVAL '5 day', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'diego.mera@example.com'), 'COT-0203', 'RECHAZADA', 'INTERNO', 'El cliente comparo opciones y pauso la compra.', 116.00, NOW() - INTERVAL '9 day', NOW() - INTERVAL '9 day', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'compras@corporacionbrisa.com'), 'COT-0204', 'CONVERTIDA', 'INTERNO', 'Pack corporativo convertido a pedido para activacion de marca.', 1006.00, NOW() - INTERVAL '6 day', NOW() - INTERVAL '4 day', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'eventos@hotelmirador.com'), 'COT-0205', 'CONVERTIDA', 'INTERNO', 'Pedido para experiencia dulce en salones y lobby.', 1364.00, NOW() - INTERVAL '7 day', NOW() - INTERVAL '2 day', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'esteban.rojas@example.com'), 'COT-0206', 'PENDIENTE', 'PUBLICO', 'Quiere una torta lista y bebidas para retiro.', 191.00, NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'paola.cevallos@example.com'), 'COT-0207', 'APROBADA', 'INTERNO', 'Cotizacion para degustacion y servicio interno.', 304.00, NOW() - INTERVAL '3 day', NOW() - INTERVAL '3 day', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'andrea.ponce@example.com'), 'COT-0208', 'RECHAZADA', 'PUBLICO', 'El cliente decidio reducir presupuesto y no continuo.', 90.00, NOW() - INTERVAL '4 day', NOW() - INTERVAL '4 day', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'protocolo@ucosta.edu.ec'), 'COT-0209', 'CONVERTIDA', 'INTERNO', 'Propuesta para feria universitaria convertida a orden.', 1540.00, NOW() - INTERVAL '8 day', NOW() - INTERVAL '1 day', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'compras@bistronube.com'), 'COT-0210', 'APROBADA', 'INTERNO', 'Caja dulce y bebidas para colaboracion gastronomica.', 500.00, NOW() - INTERVAL '3 day', NOW() - INTERVAL '2 day', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'martina.vega@example.com'), 'COT-0211', 'PENDIENTE', 'PUBLICO', 'Torta y cupcakes para celebracion de fin de semana.', 318.00, NOW() - INTERVAL '2 day', NOW() - INTERVAL '2 day', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'produccion@agenciaprisma.com'), 'COT-0212', 'CONVERTIDA', 'INTERNO', 'Accion corporativa con mesa dulce y bebidas calientes.', 702.00, NOW() - INTERVAL '6 day', NOW() - INTERVAL '2 day', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'karen.montalvo@example.com'), 'COT-0213', 'PENDIENTE', 'PUBLICO', 'Cumpleanos pequeno con torta y cupcakes clasicos.', 260.00, NOW() - INTERVAL '8 hour', NOW() - INTERVAL '8 hour', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'isabel.luna@example.com'), 'COT-0214', 'APROBADA', 'PUBLICO', 'Caja premium y cheesecakes para regalo.', 225.00, NOW() - INTERVAL '12 day', NOW() - INTERVAL '11 day', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'nicolas.vera@example.com'), 'COT-0215', 'RECHAZADA', 'INTERNO', 'Cotizacion suspendida por cambio de fecha del evento.', 96.00, NOW() - INTERVAL '11 day', NOW() - INTERVAL '10 day', 0);

INSERT INTO cotizacion_detalle (cotizacion_id, producto_id, descripcion_item, cantidad, precio_estimado, subtotal, notas, created_at) VALUES
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0201'), (SELECT producto_id FROM producto WHERE codigo = 'PIE-LIMON-01'), 'Pie de limon artesanal', 2, 48.00, 96.00, NULL, NOW() - INTERVAL '2 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0201'), (SELECT producto_id FROM producto WHERE codigo = 'GALL-MIX-01'), 'Galletas mix mantequilla', 10, 14.00, 140.00, 'Ideal para brunch de fin de semana.', NOW() - INTERVAL '2 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0202'), (SELECT producto_id FROM producto WHERE codigo = 'TORT-BODA-01'), 'Naked cake principal', 1, 480.00, 480.00, 'Version de un piso alto con frutas.', NOW() - INTERVAL '5 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0202'), (SELECT producto_id FROM producto WHERE codigo = 'GALL-CORP-01'), 'Galletas decoradas personalizadas', 20, 16.00, 320.00, 'Con iniciales y paleta neutra.', NOW() - INTERVAL '5 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0203'), (SELECT producto_id FROM producto WHERE codigo = 'TIRA-FAM-01'), 'Tiramisu familiar', 2, 58.00, 116.00, NULL, NOW() - INTERVAL '9 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0204'), (SELECT producto_id FROM producto WHERE codigo = 'MESA-DULCE-50'), 'Mesa dulce 50 personas', 1, 650.00, 650.00, NULL, NOW() - INTERVAL '6 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0204'), (SELECT producto_id FROM producto WHERE codigo = 'FRAP-MOCHA'), 'Frappe mocha', 12, 19.00, 228.00, 'Servicio frio para activacion corporativa.', NOW() - INTERVAL '6 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0204'), (SELECT producto_id FROM producto WHERE codigo = 'GALL-CORP-01'), 'Galleta corporativa', 8, 16.00, 128.00, 'Con logo impreso en glaseado.', NOW() - INTERVAL '6 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0205'), (SELECT producto_id FROM producto WHERE codigo = 'MESA-DULCE-80'), 'Mesa dulce 80 personas', 1, 980.00, 980.00, NULL, NOW() - INTERVAL '7 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0205'), (SELECT producto_id FROM producto WHERE codigo = 'CHOC-CAL-01'), 'Chocolate caliente casa', 15, 16.00, 240.00, 'Servicio nocturno para lobby.', NOW() - INTERVAL '7 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0205'), (SELECT producto_id FROM producto WHERE codigo = 'BRIGA-BOX-12'), 'Caja brigadeiro 12', 2, 72.00, 144.00, NULL, NOW() - INTERVAL '7 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0206'), (SELECT producto_id FROM producto WHERE codigo = 'TORT-ZANA-01'), 'Torta zanahoria y nuez', 1, 155.00, 155.00, NULL, NOW() - INTERVAL '1 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0206'), (SELECT producto_id FROM producto WHERE codigo = 'CAFE-AMER'), 'Cafe americano', 2, 18.00, 36.00, NULL, NOW() - INTERVAL '1 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0207'), (SELECT producto_id FROM producto WHERE codigo = 'PIE-LIMON-01'), 'Pie de limon', 3, 48.00, 144.00, NULL, NOW() - INTERVAL '3 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0207'), (SELECT producto_id FROM producto WHERE codigo = 'CHOC-CAL-01'), 'Chocolate caliente casa', 10, 16.00, 160.00, 'Consumo interno para degustacion.', NOW() - INTERVAL '3 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0208'), (SELECT producto_id FROM producto WHERE codigo = 'ALFAJOR-PREM'), 'Alfajor premium', 6, 15.00, 90.00, NULL, NOW() - INTERVAL '4 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0209'), (SELECT producto_id FROM producto WHERE codigo = 'MESA-DULCE-80'), 'Mesa dulce 80 personas', 1, 980.00, 980.00, NULL, NOW() - INTERVAL '8 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0209'), (SELECT producto_id FROM producto WHERE codigo = 'CAFE-AMER'), 'Cafe americano', 20, 18.00, 360.00, 'Servicio para feria estudiantil.', NOW() - INTERVAL '8 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0209'), (SELECT producto_id FROM producto WHERE codigo = 'BROWNIE-IND'), 'Brownie individual', 8, 25.00, 200.00, NULL, NOW() - INTERVAL '8 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0210'), (SELECT producto_id FROM producto WHERE codigo = 'BRIGA-BOX-12'), 'Caja brigadeiro 12', 3, 72.00, 216.00, NULL, NOW() - INTERVAL '3 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0210'), (SELECT producto_id FROM producto WHERE codigo = 'CAJA-MINI'), 'Caja mini postres', 2, 85.00, 170.00, NULL, NOW() - INTERVAL '3 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0210'), (SELECT producto_id FROM producto WHERE codigo = 'FRAP-MOCHA'), 'Frappe mocha', 6, 19.00, 114.00, NULL, NOW() - INTERVAL '3 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0211'), (SELECT producto_id FROM producto WHERE codigo = 'TORT-RED-VEL'), 'Torta red velvet', 1, 180.00, 180.00, NULL, NOW() - INTERVAL '2 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0211'), (SELECT producto_id FROM producto WHERE codigo = 'CUPK-OREO-01'), 'Cupcake oreo', 6, 23.00, 138.00, NULL, NOW() - INTERVAL '2 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0212'), (SELECT producto_id FROM producto WHERE codigo = 'MESA-DULCE-25'), 'Mesa dulce 25 personas', 1, 350.00, 350.00, NULL, NOW() - INTERVAL '6 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0212'), (SELECT producto_id FROM producto WHERE codigo = 'CHOC-CAL-01'), 'Chocolate caliente casa', 10, 16.00, 160.00, NULL, NOW() - INTERVAL '6 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0212'), (SELECT producto_id FROM producto WHERE codigo = 'GALL-CORP-01'), 'Galleta corporativa', 12, 16.00, 192.00, NULL, NOW() - INTERVAL '6 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0213'), (SELECT producto_id FROM producto WHERE codigo = 'TORT-3-LECH'), 'Torta tres leches', 1, 140.00, 140.00, NULL, NOW() - INTERVAL '8 hour'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0213'), (SELECT producto_id FROM producto WHERE codigo = 'CUPK-VAIN-01'), 'Cupcake vainilla', 6, 20.00, 120.00, NULL, NOW() - INTERVAL '8 hour'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0214'), (SELECT producto_id FROM producto WHERE codigo = 'CHEESE-FRUT'), 'Cheesecake frutos rojos', 2, 45.00, 90.00, NULL, NOW() - INTERVAL '12 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0214'), (SELECT producto_id FROM producto WHERE codigo = 'CAJA-MINI'), 'Caja mini postres', 1, 85.00, 85.00, NULL, NOW() - INTERVAL '12 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0214'), (SELECT producto_id FROM producto WHERE codigo = 'BROWNIE-IND'), 'Brownie individual', 2, 25.00, 50.00, NULL, NOW() - INTERVAL '12 day'),
((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0215'), (SELECT producto_id FROM producto WHERE codigo = 'PIE-LIMON-01'), 'Pie de limon artesanal', 2, 48.00, 96.00, 'No continuo por cambio de fecha.', NOW() - INTERVAL '11 day');

INSERT INTO pedido (
  cliente_id, cotizacion_id, codigo, fecha_entrega_estimada, fecha_entrega_real, estado_pedido, prioridad, origen, observaciones, total_estimado, fecha_pedido, created_at, updated_at, version
) VALUES
((SELECT cliente_id FROM cliente WHERE correo = 'compras@corporacionbrisa.com'), (SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0204'), 'PED-0201', NOW() + INTERVAL '1 day', NULL, 'EN_PREPARACION', 'URGENTE', 'INTERNO', 'Produccion activa para entrega corporativa.', 1006.00, NOW() - INTERVAL '3 day', NOW() - INTERVAL '3 day', NOW() - INTERVAL '2 hour', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'eventos@hotelmirador.com'), (SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0205'), 'PED-0202', NOW() + INTERVAL '2 day', NULL, 'REGISTRADO', 'URGENTE', 'INTERNO', 'Pendiente de confirmacion final de montaje.', 1364.00, NOW() - INTERVAL '2 day', NOW() - INTERVAL '2 day', NOW() - INTERVAL '6 hour', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'protocolo@ucosta.edu.ec'), (SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0209'), 'PED-0203', NOW() + INTERVAL '1 day', NULL, 'EN_PREPARACION', 'URGENTE', 'INTERNO', 'Produccion abierta para feria universitaria.', 1540.00, NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day', NOW() - INTERVAL '4 hour', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'produccion@agenciaprisma.com'), (SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0212'), 'PED-0204', NOW() + INTERVAL '8 hour', NULL, 'LISTO', 'NORMAL', 'INTERNO', 'Pedido listo para despacho a la agencia.', 702.00, NOW() - INTERVAL '2 day', NOW() - INTERVAL '2 day', NOW() - INTERVAL '1 hour', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'lucia.herrera@example.com'), NULL, 'PED-0205', NOW() - INTERVAL '1 hour', NOW() - INTERVAL '2 hour', 'ENTREGADO', 'NORMAL', 'PUBLICO', 'Entrega completada el mismo dia.', 236.00, NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day', NOW() - INTERVAL '2 hour', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'sofia.almeida@example.com'), NULL, 'PED-0206', NOW() - INTERVAL '4 hour', NOW() - INTERVAL '5 hour', 'ENTREGADO', 'NORMAL', 'INTERNO', 'Pedido entregado para reunion familiar.', 210.00, NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day', NOW() - INTERVAL '5 hour', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'diego.mera@example.com'), NULL, 'PED-0207', NOW() - INTERVAL '4 day', NULL, 'CANCELADO', 'NORMAL', 'INTERNO', 'Cancelado por falta de confirmacion del cliente.', 116.00, NOW() - INTERVAL '6 day', NOW() - INTERVAL '6 day', NOW() - INTERVAL '4 day', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'esteban.rojas@example.com'), NULL, 'PED-0208', NOW() + INTERVAL '1 day', NULL, 'LISTO', 'NORMAL', 'PUBLICO', 'Pedido listo para retiro al final de la tarde.', 191.00, NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day', NOW() - INTERVAL '30 minute', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'paola.cevallos@example.com'), NULL, 'PED-0209', NOW() - INTERVAL '2 day', NOW() - INTERVAL '2 day', 'ENTREGADO', 'NORMAL', 'INTERNO', 'Venta cerrada para consumo corporativo.', 304.00, NOW() - INTERVAL '3 day', NOW() - INTERVAL '3 day', NOW() - INTERVAL '2 day', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'andrea.ponce@example.com'), NULL, 'PED-0210', NOW() + INTERVAL '2 day', NULL, 'REGISTRADO', 'NORMAL', 'PUBLICO', 'Pedido pequeno para oficina.', 90.00, NOW() - INTERVAL '5 hour', NOW() - INTERVAL '5 hour', NOW() - INTERVAL '5 hour', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'martina.vega@example.com'), NULL, 'PED-0211', NOW() - INTERVAL '4 day', NOW() - INTERVAL '4 day', 'ENTREGADO', 'NORMAL', 'PUBLICO', 'Pedido entregado para cumpleanos de fin de semana.', 318.00, NOW() - INTERVAL '5 day', NOW() - INTERVAL '5 day', NOW() - INTERVAL '4 day', 0),
((SELECT cliente_id FROM cliente WHERE correo = 'karen.montalvo@example.com'), NULL, 'PED-0212', NOW() + INTERVAL '1 day', NULL, 'REGISTRADO', 'URGENTE', 'PUBLICO', 'Pedido de ultima hora para cumpleanos infantil.', 260.00, NOW() - INTERVAL '6 hour', NOW() - INTERVAL '6 hour', NOW() - INTERVAL '40 minute', 0);

INSERT INTO pedido_detalle (pedido_id, producto_id, descripcion_item, cantidad, precio_unitario, subtotal, notas, created_at) VALUES
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0201'), (SELECT producto_id FROM producto WHERE codigo = 'MESA-DULCE-50'), 'Mesa dulce 50 personas', 1, 650.00, 650.00, NULL, NOW() - INTERVAL '3 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0201'), (SELECT producto_id FROM producto WHERE codigo = 'FRAP-MOCHA'), 'Frappe mocha', 12, 19.00, 228.00, NULL, NOW() - INTERVAL '3 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0201'), (SELECT producto_id FROM producto WHERE codigo = 'GALL-CORP-01'), 'Galleta corporativa logo', 8, 16.00, 128.00, NULL, NOW() - INTERVAL '3 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0202'), (SELECT producto_id FROM producto WHERE codigo = 'MESA-DULCE-80'), 'Mesa dulce 80 personas', 1, 980.00, 980.00, NULL, NOW() - INTERVAL '2 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0202'), (SELECT producto_id FROM producto WHERE codigo = 'CHOC-CAL-01'), 'Chocolate caliente casa', 15, 16.00, 240.00, NULL, NOW() - INTERVAL '2 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0202'), (SELECT producto_id FROM producto WHERE codigo = 'BRIGA-BOX-12'), 'Caja brigadeiro 12', 2, 72.00, 144.00, NULL, NOW() - INTERVAL '2 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0203'), (SELECT producto_id FROM producto WHERE codigo = 'MESA-DULCE-80'), 'Mesa dulce 80 personas', 1, 980.00, 980.00, NULL, NOW() - INTERVAL '1 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0203'), (SELECT producto_id FROM producto WHERE codigo = 'CAFE-AMER'), 'Cafe americano', 20, 18.00, 360.00, NULL, NOW() - INTERVAL '1 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0203'), (SELECT producto_id FROM producto WHERE codigo = 'BROWNIE-IND'), 'Brownie individual', 8, 25.00, 200.00, NULL, NOW() - INTERVAL '1 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0204'), (SELECT producto_id FROM producto WHERE codigo = 'MESA-DULCE-25'), 'Mesa dulce 25 personas', 1, 350.00, 350.00, NULL, NOW() - INTERVAL '2 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0204'), (SELECT producto_id FROM producto WHERE codigo = 'CHOC-CAL-01'), 'Chocolate caliente casa', 10, 16.00, 160.00, NULL, NOW() - INTERVAL '2 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0204'), (SELECT producto_id FROM producto WHERE codigo = 'GALL-CORP-01'), 'Galleta corporativa logo', 12, 16.00, 192.00, NULL, NOW() - INTERVAL '2 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0205'), (SELECT producto_id FROM producto WHERE codigo = 'PIE-LIMON-01'), 'Pie de limon artesanal', 2, 48.00, 96.00, NULL, NOW() - INTERVAL '1 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0205'), (SELECT producto_id FROM producto WHERE codigo = 'GALL-MIX-01'), 'Galletas mix mantequilla', 10, 14.00, 140.00, NULL, NOW() - INTERVAL '1 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0206'), (SELECT producto_id FROM producto WHERE codigo = 'TORT-3-LECH'), 'Torta tres leches', 1, 140.00, 140.00, NULL, NOW() - INTERVAL '1 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0206'), (SELECT producto_id FROM producto WHERE codigo = 'GALL-MIX-01'), 'Galletas mix mantequilla', 5, 14.00, 70.00, NULL, NOW() - INTERVAL '1 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0207'), (SELECT producto_id FROM producto WHERE codigo = 'TIRA-FAM-01'), 'Tiramisu familiar', 2, 58.00, 116.00, 'No se produjo por cancelacion previa.', NOW() - INTERVAL '6 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0208'), (SELECT producto_id FROM producto WHERE codigo = 'TORT-ZANA-01'), 'Torta zanahoria y nuez', 1, 155.00, 155.00, NULL, NOW() - INTERVAL '1 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0208'), (SELECT producto_id FROM producto WHERE codigo = 'CAFE-AMER'), 'Cafe americano', 2, 18.00, 36.00, NULL, NOW() - INTERVAL '1 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0209'), (SELECT producto_id FROM producto WHERE codigo = 'PIE-LIMON-01'), 'Pie de limon artesanal', 3, 48.00, 144.00, NULL, NOW() - INTERVAL '3 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0209'), (SELECT producto_id FROM producto WHERE codigo = 'CHOC-CAL-01'), 'Chocolate caliente casa', 10, 16.00, 160.00, NULL, NOW() - INTERVAL '3 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0210'), (SELECT producto_id FROM producto WHERE codigo = 'ALFAJOR-PREM'), 'Alfajor premium', 6, 15.00, 90.00, NULL, NOW() - INTERVAL '5 hour'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0211'), (SELECT producto_id FROM producto WHERE codigo = 'TORT-RED-VEL'), 'Torta red velvet', 1, 180.00, 180.00, NULL, NOW() - INTERVAL '5 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0211'), (SELECT producto_id FROM producto WHERE codigo = 'CUPK-OREO-01'), 'Cupcake oreo', 6, 23.00, 138.00, NULL, NOW() - INTERVAL '5 day'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0212'), (SELECT producto_id FROM producto WHERE codigo = 'TORT-3-LECH'), 'Torta tres leches', 1, 140.00, 140.00, NULL, NOW() - INTERVAL '6 hour'),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0212'), (SELECT producto_id FROM producto WHERE codigo = 'CUPK-VAIN-01'), 'Cupcake vainilla', 6, 20.00, 120.00, NULL, NOW() - INTERVAL '6 hour');

INSERT INTO produccion (
  pedido_id, estado_produccion, prioridad_produccion, fecha_inicio, fecha_finalizacion, observaciones_produccion, created_at, updated_at, version
) VALUES
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0201'), 'DECORACION', 'URGENTE', NOW() - INTERVAL '5 hour', NULL, 'Decoracion de galletas de marca y cierre de montaje.', NOW() - INTERVAL '3 day', NOW() - INTERVAL '25 minute', 0),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0202'), 'PENDIENTE', 'URGENTE', NULL, NULL, 'Espera confirmacion final del hotel antes de pasar a cocina.', NOW() - INTERVAL '2 day', NOW() - INTERVAL '2 hour', 0),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0203'), 'EMPAQUE', 'URGENTE', NOW() - INTERVAL '2 hour', NULL, 'Empaque final y armado de zona de entrega para feria.', NOW() - INTERVAL '1 day', NOW() - INTERVAL '10 minute', 0),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0204'), 'FINALIZADO', 'NORMAL', NOW() - INTERVAL '1 day', NOW() - INTERVAL '3 hour', 'Pedido cerrado y listo para retiro por agencia.', NOW() - INTERVAL '2 day', NOW() - INTERVAL '3 hour', 0),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0205'), 'FINALIZADO', 'NORMAL', NOW() - INTERVAL '8 hour', NOW() - INTERVAL '2 hour', 'Despacho express completado.', NOW() - INTERVAL '1 day', NOW() - INTERVAL '2 hour', 0),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0206'), 'FINALIZADO', 'NORMAL', NOW() - INTERVAL '12 hour', NOW() - INTERVAL '5 hour', 'Entrega confirmada por cliente final.', NOW() - INTERVAL '1 day', NOW() - INTERVAL '5 hour', 0),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0208'), 'FINALIZADO', 'NORMAL', NOW() - INTERVAL '7 hour', NOW() - INTERVAL '30 minute', 'Listo para retiro en mostrador.', NOW() - INTERVAL '1 day', NOW() - INTERVAL '30 minute', 0),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0209'), 'FINALIZADO', 'NORMAL', NOW() - INTERVAL '3 day', NOW() - INTERVAL '2 day', 'Produccion cerrada y entregada en oficina.', NOW() - INTERVAL '3 day', NOW() - INTERVAL '2 day', 0),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0210'), 'PENDIENTE', 'NORMAL', NULL, NULL, 'Pendiente de confirmar horario de retiro.', NOW() - INTERVAL '5 hour', NOW() - INTERVAL '45 minute', 0),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0211'), 'FINALIZADO', 'NORMAL', NOW() - INTERVAL '5 day', NOW() - INTERVAL '4 day', 'Torta y cupcakes entregados para cumpleanos.', NOW() - INTERVAL '5 day', NOW() - INTERVAL '4 day', 0),
((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0212'), 'PREPARACION', 'URGENTE', NOW() - INTERVAL '30 minute', NULL, 'Se inicio mise en place para pedido urgente.', NOW() - INTERVAL '6 hour', NOW() - INTERVAL '15 minute', 0);

INSERT INTO ingrediente (umedida_id, codigo, nombre, descripcion, stock_minimo, stock_actual, costo_referencial, activo, created_at, updated_at, version) VALUES
((SELECT umedida_id FROM umedida WHERE codigo = 'kg'), 'CAC-001', 'Cacao puro en polvo', 'Cacao oscuro para masas y bebidas', 3.0000, 1.2000, 4.90, TRUE, NOW() - INTERVAL '15 day', NOW() - INTERVAL '3 hour', 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'kg'), 'FRE-001', 'Fresa fresca', 'Fresa roja para topping y rellenos', 5.0000, 2.0000, 3.20, TRUE, NOW() - INTERVAL '15 day', NOW() - INTERVAL '2 hour', 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'l'), 'CRE-001', 'Crema de leche', 'Base cremosa para rellenos y toppings', 4.0000, 1.5000, 2.80, TRUE, NOW() - INTERVAL '14 day', NOW() - INTERVAL '1 day', 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'kg'), 'QCR-001', 'Queso crema', 'Queso crema para frosting y cheesecakes', 3.0000, 1.0000, 5.60, TRUE, NOW() - INTERVAL '14 day', NOW() - INTERVAL '6 hour', 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'kg'), 'FRI-001', 'Frambuesa congelada', 'Fruta congelada para rellenos premium', 2.0000, 0.4000, 6.90, TRUE, NOW() - INTERVAL '13 day', NOW() - INTERVAL '1 day', 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'kg'), 'DDL-001', 'Dulce de leche repostero', 'Dulce de leche para rellenos y alfajores', 3.0000, 1.8000, 4.10, TRUE, NOW() - INTERVAL '13 day', NOW() - INTERVAL '8 hour', 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'kg'), 'CAF-001', 'Cafe molido de altura', 'Cafe tostado para bebidas y postres', 1.5000, 3.5000, 7.20, TRUE, NOW() - INTERVAL '12 day', NOW() - INTERVAL '2 day', 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'kg'), 'LIM-001', 'Limon sutil', 'Limon para curd, pie y bebidas', 4.0000, 6.0000, 1.30, TRUE, NOW() - INTERVAL '11 day', NOW() - INTERVAL '1 day', 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'kg'), 'ZAN-001', 'Zanahoria rallada', 'Zanahoria fresca para tortas y rellenos', 5.0000, 9.0000, 0.90, TRUE, NOW() - INTERVAL '11 day', NOW() - INTERVAL '1 day', 0);

INSERT INTO insumo (umedida_id, codigo, nombre, descripcion, stock_minimo, stock_actual, costo_referencial, activo, created_at, updated_at, version) VALUES
((SELECT umedida_id FROM umedida WHERE codigo = 'paq'), 'CAJ-002', 'Caja pastel mediana', 'Caja para torta mediana con visor', 120.0000, 40.0000, 1.10, TRUE, NOW() - INTERVAL '10 day', NOW() - INTERVAL '1 hour', 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'paq'), 'CAJ-003', 'Caja pastel grande', 'Caja reforzada para torta grande', 80.0000, 15.0000, 1.45, TRUE, NOW() - INTERVAL '10 day', NOW() - INTERVAL '1 day', 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'paq'), 'CIN-001', 'Cinta satin decorativa', 'Cinta para acabados de cajas y regalos', 60.0000, 18.0000, 0.18, TRUE, NOW() - INTERVAL '9 day', NOW() - INTERVAL '4 hour', 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'paq'), 'BOL-001', 'Bolsa delivery kraft', 'Bolsa reforzada para pedidos medianos', 300.0000, 180.0000, 0.12, TRUE, NOW() - INTERVAL '9 day', NOW() - INTERVAL '2 hour', 0),
((SELECT umedida_id FROM umedida WHERE codigo = 'paq'), 'ETI-001', 'Etiqueta gracias por tu compra', 'Etiqueta adhesiva para empaques y regalos', 150.0000, 35.0000, 0.05, TRUE, NOW() - INTERVAL '9 day', NOW() - INTERVAL '1 hour', 0);

INSERT INTO proveedor (codigo, nombre, telefono, correo, direccion, observaciones, activo, created_at, updated_at, version) VALUES
('PROV-006', 'Frutas de la Sierra', '02-2800-5566', 'ventas@frutasdelasierra.com', 'Mercado Mayorista Norte, Quito', 'Frutas frescas para topping, rellenos y decoracion.', TRUE, NOW() - INTERVAL '12 day', NOW() - INTERVAL '2 day', 0),
('PROV-007', 'DecoraPack Studio', '04-2550-7000', 'pedidos@decorapack.ec', 'Via a Samborondon Km 3.5, Guayaquil', 'Empaques premium, cintas y etiquetas.', TRUE, NOW() - INTERVAL '12 day', NOW() - INTERVAL '2 day', 0),
('PROV-008', 'Cafe Andino', '07-2800-9900', 'comercial@cafeandino.ec', 'Cuenca - Parque Industrial', 'Cafe de altura para bebidas y postres especiales.', TRUE, NOW() - INTERVAL '11 day', NOW() - INTERVAL '2 day', 0);

INSERT INTO item_proveedor (item_tipo, item_id, proveedor_id, precio_suministro, es_principal, activo, created_at) VALUES
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'CAC-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-003'), 4.70, TRUE, TRUE, NOW() - INTERVAL '10 day'),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'FRE-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-006'), 3.00, TRUE, TRUE, NOW() - INTERVAL '10 day'),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'FRI-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-006'), 6.55, TRUE, TRUE, NOW() - INTERVAL '10 day'),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'CRE-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-002'), 2.65, TRUE, TRUE, NOW() - INTERVAL '10 day'),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'QCR-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-002'), 5.20, TRUE, TRUE, NOW() - INTERVAL '10 day'),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'DDL-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-002'), 3.95, TRUE, TRUE, NOW() - INTERVAL '10 day'),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'CAF-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-008'), 6.90, TRUE, TRUE, NOW() - INTERVAL '10 day'),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'LIM-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-006'), 1.10, TRUE, TRUE, NOW() - INTERVAL '10 day'),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'ZAN-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-006'), 0.82, TRUE, TRUE, NOW() - INTERVAL '10 day'),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CAJ-002'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-005'), 1.02, TRUE, TRUE, NOW() - INTERVAL '10 day'),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CAJ-003'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-005'), 1.38, TRUE, TRUE, NOW() - INTERVAL '10 day'),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CIN-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-007'), 0.16, TRUE, TRUE, NOW() - INTERVAL '10 day'),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'BOL-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-007'), 0.10, TRUE, TRUE, NOW() - INTERVAL '10 day'),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'ETI-001'), (SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-007'), 0.04, TRUE, TRUE, NOW() - INTERVAL '10 day');

INSERT INTO orden_compra (
  proveedor_id, codigo, estado, fecha_emision, fecha_entrega_esperada, fecha_entrega_real, total_estimado, observaciones, activo, created_at, updated_at, version
) VALUES
((SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-006'), 'OC-2026-0018', 'RECIBIDA_PARCIAL', NOW() - INTERVAL '5 day', NOW() - INTERVAL '2 day', NULL, 140.50, 'Fruta fresca recibida parcialmente por temperatura.', TRUE, NOW() - INTERVAL '5 day', NOW() - INTERVAL '2 day', 0),
((SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-007'), 'OC-2026-0019', 'ENVIADA', NOW() - INTERVAL '3 day', NOW() + INTERVAL '1 day', NULL, 82.40, 'Empaques premium y accesorios visuales.', TRUE, NOW() - INTERVAL '3 day', NOW() - INTERVAL '3 day', 0),
((SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-008'), 'OC-2026-0020', 'BORRADOR', NOW() - INTERVAL '1 day', NOW() + INTERVAL '4 day', NULL, 138.00, 'Pendiente aprobacion de cafe de temporada.', TRUE, NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day', 0),
((SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-002'), 'OC-2026-0021', 'CANCELADA', NOW() - INTERVAL '8 day', NOW() - INTERVAL '5 day', NULL, 79.95, 'Se cancelo por cambio de proveedor en ultima hora.', TRUE, NOW() - INTERVAL '8 day', NOW() - INTERVAL '5 day', 0),
((SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-005'), 'OC-2026-0022', 'RECIBIDA', NOW() - INTERVAL '2 day', NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day', 71.00, 'Empaques recibidos para reposicion rapida.', TRUE, NOW() - INTERVAL '2 day', NOW() - INTERVAL '1 day', 0),
((SELECT proveedor_id FROM proveedor WHERE codigo = 'PROV-003'), 'OC-2026-0023', 'ENVIADA', NOW() - INTERVAL '4 day', NOW() + INTERVAL '2 day', NULL, 94.00, 'Reposicion de cacao de alta rotacion.', TRUE, NOW() - INTERVAL '4 day', NOW() - INTERVAL '1 day', 0);

INSERT INTO orden_compra_detalle (
  orden_compra_id, item_tipo, item_id, item_nombre, item_codigo, cantidad, precio_unitario, subtotal, cantidad_recibida, observaciones, created_at
) VALUES
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0018'), 'INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'FRE-001'), 'Fresa fresca', 'FRE-001', 25, 3.00, 75.00, 10, 'Se recibio solo una parte del lote.', NOW() - INTERVAL '5 day'),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0018'), 'INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'FRI-001'), 'Frambuesa congelada', 'FRI-001', 10, 6.55, 65.50, 4, 'Pendiente segundo despacho.', NOW() - INTERVAL '5 day'),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0019'), 'INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CAJ-003'), 'Caja pastel grande', 'CAJ-003', 40, 1.38, 55.20, 0, 'Despacho aun no recibido.', NOW() - INTERVAL '3 day'),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0019'), 'INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CIN-001'), 'Cinta satin decorativa', 'CIN-001', 120, 0.16, 19.20, 0, 'Incluye colores de temporada.', NOW() - INTERVAL '3 day'),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0019'), 'INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'ETI-001'), 'Etiqueta gracias por tu compra', 'ETI-001', 200, 0.04, 8.00, 0, 'Pendiente despacho del proveedor.', NOW() - INTERVAL '3 day'),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0020'), 'INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'CAF-001'), 'Cafe molido de altura', 'CAF-001', 20, 6.90, 138.00, 0, 'Compra en revision interna.', NOW() - INTERVAL '1 day'),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0021'), 'INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'CRE-001'), 'Crema de leche', 'CRE-001', 10, 2.65, 26.50, 0, 'Orden cancelada antes de recepcion.', NOW() - INTERVAL '8 day'),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0021'), 'INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'QCR-001'), 'Queso crema', 'QCR-001', 8, 5.20, 41.60, 0, 'Orden cancelada antes de recepcion.', NOW() - INTERVAL '8 day'),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0021'), 'INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'DDL-001'), 'Dulce de leche repostero', 'DDL-001', 3, 3.95, 11.85, 0, 'Orden cancelada por ajuste de proveedor.', NOW() - INTERVAL '8 day'),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0022'), 'INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CAJ-002'), 'Caja pastel mediana', 'CAJ-002', 20, 1.02, 20.40, 20, 'Recepcion completa', NOW() - INTERVAL '2 day'),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0022'), 'INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'BOL-001'), 'Bolsa delivery kraft', 'BOL-001', 300, 0.10, 30.00, 300, 'Recepcion completa', NOW() - INTERVAL '2 day'),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0022'), 'INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CAJ-001'), 'Caja pastel pequena', 'CAJ-001', 20, 0.75, 15.00, 20, 'Recepcion completa', NOW() - INTERVAL '2 day'),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0022'), 'INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'ETI-001'), 'Etiqueta gracias por tu compra', 'ETI-001', 140, 0.04, 5.60, 140, 'Recepcion completa', NOW() - INTERVAL '2 day'),
((SELECT orden_compra_id FROM orden_compra WHERE codigo = 'OC-2026-0023'), 'INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'CAC-001'), 'Cacao puro en polvo', 'CAC-001', 20, 4.70, 94.00, 0, 'Pedido urgente para bebidas y cupcakes.', NOW() - INTERVAL '4 day');

INSERT INTO inventario_movimiento (
  item_tipo, item_id, tipo_movimiento, cantidad, saldo_posterior, referencia_tipo, referencia_id, motivo_salida, observaciones, fecha_movimiento, registrado_por_user_id
) VALUES
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'FRE-001'), 'ENTRADA_COMPRA', 6.000, 6.000, 'ORDEN_COMPRA', 'OC-2026-0018', NULL, 'Ingreso parcial de fresa fresca.', NOW() - INTERVAL '5 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'FRI-001'), 'ENTRADA_COMPRA', 2.400, 2.400, 'ORDEN_COMPRA', 'OC-2026-0018', NULL, 'Ingreso parcial de frambuesa congelada.', NOW() - INTERVAL '5 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'CRE-001'), 'ENTRADA_COMPRA', 5.000, 5.000, 'AJUSTE', 'AJ-2026-0008', NULL, 'Ajuste inicial de crema de leche.', NOW() - INTERVAL '9 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'QCR-001'), 'ENTRADA_COMPRA', 4.000, 4.000, 'AJUSTE', 'AJ-2026-0009', NULL, 'Ajuste inicial de queso crema.', NOW() - INTERVAL '8 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'CAC-001'), 'ENTRADA_COMPRA', 3.000, 3.000, 'ORDEN_COMPRA', 'OC-2026-0023', NULL, 'Ingreso de cacao de alta rotacion.', NOW() - INTERVAL '4 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CAJ-003'), 'ENTRADA_AJUSTE', 40.000, 40.000, 'AJUSTE', 'AJ-2026-0010', NULL, 'Ajuste inicial de cajas grandes.', NOW() - INTERVAL '7 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CIN-001'), 'ENTRADA_AJUSTE', 40.000, 40.000, 'AJUSTE', 'AJ-2026-0011', NULL, 'Ajuste inicial de cintas.', NOW() - INTERVAL '6 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'FRI-001'), 'SALIDA_PRODUCCION', 2.000, 0.400, 'PEDIDO', 'PED-0211', 'Produccion regular', 'Uso de frambuesa para topping premium.', NOW() - INTERVAL '1 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1')),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'CRE-001'), 'SALIDA_PRODUCCION', 3.500, 1.500, 'PEDIDO', 'PED-0204', 'Produccion regular', 'Rellenos y acabados para pedido corporativo.', NOW() - INTERVAL '1 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1')),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'QCR-001'), 'SALIDA_PRODUCCION', 3.000, 1.000, 'PEDIDO', 'PED-0206', 'Produccion regular', 'Uso en frosting para torta familiar.', NOW() - INTERVAL '6 hour', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1')),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CAJ-002'), 'ENTRADA_COMPRA', 50.000, 50.000, 'ORDEN_COMPRA', 'OC-2026-0022', NULL, 'Recepcion de cajas medianas.', NOW() - INTERVAL '5 hour', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'CAC-001'), 'SALIDA_PRODUCCION', 1.800, 1.200, 'PEDIDO', 'PED-0201', 'Produccion urgente', 'Consumo para bebidas y cupcakes especiales.', NOW() - INTERVAL '3 hour', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1')),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'BOL-001'), 'ENTRADA_COMPRA', 300.000, 300.000, 'ORDEN_COMPRA', 'OC-2026-0022', NULL, 'Ingreso de bolsas kraft.', NOW() - INTERVAL '2 hour', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
('INGREDIENTE', (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'FRE-001'), 'SALIDA_PRODUCCION', 4.000, 2.000, 'PEDIDO', 'PED-0205', 'Entrega express', 'Consumo de fruta fresca para postres del dia.', NOW() - INTERVAL '2 hour', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1')),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CAJ-002'), 'SALIDA_PRODUCCION', 10.000, 40.000, 'PEDIDO', 'PED-0208', 'Empaque final', 'Uso de cajas medianas para retiro en mostrador.', NOW() - INTERVAL '1 hour 30 minute', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1')),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'ETI-001'), 'ENTRADA_COMPRA', 120.000, 120.000, 'ORDEN_COMPRA', 'OC-2026-0022', NULL, 'Ingreso parcial de etiquetas.', NOW() - INTERVAL '1 hour', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'BOL-001'), 'SALIDA_PRODUCCION', 120.000, 180.000, 'PEDIDO', 'PED-0203', 'Despacho operativo', 'Uso para empaques de feria universitaria.', NOW() - INTERVAL '45 minute', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1')),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CIN-001'), 'SALIDA_PRODUCCION', 22.000, 18.000, 'PEDIDO', 'PED-0201', 'Decoracion final', 'Cierre visual de empaques corporativos.', NOW() - INTERVAL '30 minute', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1')),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'ETI-001'), 'SALIDA_PRODUCCION', 85.000, 35.000, 'PEDIDO', 'PED-0204', 'Empaque final', 'Etiquetas aplicadas a pedidos listos.', NOW() - INTERVAL '15 minute', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1')),
('INSUMO', (SELECT insumo_id FROM insumo WHERE codigo = 'CAJ-003'), 'SALIDA_PRODUCCION', 25.000, 15.000, 'PEDIDO', 'PED-0202', 'Reserva de empaque', 'Separacion de cajas para montaje grande.', NOW() - INTERVAL '10 minute', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1'));

INSERT INTO receta (producto_id, nombre, rendimiento_base, costo_estimado, observaciones, es_activa, created_at, updated_at, created_by_user_id, version) VALUES
((SELECT producto_id FROM producto WHERE codigo = 'TORT-ZANA-01'), 'Torta Zanahoria y Nuez - Receta Base', 1.00, 19.80, 'Base de torta especiada para vitrina y pedidos familiares.', TRUE, NOW() - INTERVAL '10 day', NOW() - INTERVAL '2 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 0),
((SELECT producto_id FROM producto WHERE codigo = 'PIE-LIMON-01'), 'Pie de Limon - Receta Base', 1.00, 11.40, 'Receta para pie de limon con crema suave.', TRUE, NOW() - INTERVAL '10 day', NOW() - INTERVAL '2 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 0),
((SELECT producto_id FROM producto WHERE codigo = 'CHEESE-FRUT'), 'Cheesecake Frutos Rojos - Receta Base', 1.00, 17.90, 'Receta para cheesecake premium de vitrina.', TRUE, NOW() - INTERVAL '9 day', NOW() - INTERVAL '2 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 0),
((SELECT producto_id FROM producto WHERE codigo = 'GALL-MIX-01'), 'Galletas Mix Mantequilla - Receta Base', 24.00, 10.60, 'Produccion de galletas surtidas para caja y vitrina.', TRUE, NOW() - INTERVAL '9 day', NOW() - INTERVAL '2 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 0),
((SELECT producto_id FROM producto WHERE codigo = 'CUPK-OREO-01'), 'Cupcake Oreo - Receta Base', 12.00, 10.90, 'Base chocolatosa para cupcakes con galleta.', TRUE, NOW() - INTERVAL '8 day', NOW() - INTERVAL '2 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 0);

INSERT INTO detalle_receta (receta_id, ingrediente_id, cantidad_base, rendimiento_por_unidad, es_para_porcion, observaciones, created_at) VALUES
((SELECT receta_id FROM receta WHERE nombre = 'Torta Zanahoria y Nuez - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'HAR-001'), 420.0000, 1.0000, TRUE, 'Harina base cernida.', NOW() - INTERVAL '10 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Torta Zanahoria y Nuez - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'AZU-001'), 320.0000, 1.0000, TRUE, 'Azucar base.', NOW() - INTERVAL '10 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Torta Zanahoria y Nuez - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'HUE-001'), 5.0000, 1.0000, TRUE, 'Huevos frescos.', NOW() - INTERVAL '10 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Torta Zanahoria y Nuez - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'ZAN-001'), 350.0000, 1.0000, TRUE, 'Zanahoria rallada fina.', NOW() - INTERVAL '10 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Pie de Limon - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'HAR-001'), 280.0000, 1.0000, TRUE, 'Masa base del pie.', NOW() - INTERVAL '10 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Pie de Limon - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'AZU-001'), 180.0000, 1.0000, TRUE, 'Azucar para crema y merengue.', NOW() - INTERVAL '10 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Pie de Limon - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'HUE-001'), 4.0000, 1.0000, TRUE, 'Huevos para crema.', NOW() - INTERVAL '10 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Pie de Limon - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'LIM-001'), 250.0000, 1.0000, TRUE, 'Limon recien exprimido.', NOW() - INTERVAL '10 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Cheesecake Frutos Rojos - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'QCR-001'), 1200.0000, 1.0000, TRUE, 'Base cremosa principal.', NOW() - INTERVAL '9 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Cheesecake Frutos Rojos - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'AZU-001'), 250.0000, 1.0000, TRUE, 'Azucar refinada.', NOW() - INTERVAL '9 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Cheesecake Frutos Rojos - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'HUE-001'), 4.0000, 1.0000, TRUE, 'Huevos para estructura.', NOW() - INTERVAL '9 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Cheesecake Frutos Rojos - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'FRI-001'), 180.0000, 1.0000, TRUE, 'Fruta para topping.', NOW() - INTERVAL '9 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Galletas Mix Mantequilla - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'HAR-001'), 300.0000, 24.0000, TRUE, 'Harina cernida.', NOW() - INTERVAL '9 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Galletas Mix Mantequilla - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'AZU-001'), 180.0000, 24.0000, TRUE, 'Azucar refinada.', NOW() - INTERVAL '9 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Galletas Mix Mantequilla - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'MAN-001'), 220.0000, 24.0000, TRUE, 'Mantequilla sin sal.', NOW() - INTERVAL '9 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Galletas Mix Mantequilla - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'CAC-001'), 45.0000, 24.0000, TRUE, 'Cacao para mitad del lote.', NOW() - INTERVAL '9 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Cupcake Oreo - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'HAR-001'), 220.0000, 12.0000, TRUE, 'Harina de base.', NOW() - INTERVAL '8 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Cupcake Oreo - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'AZU-001'), 260.0000, 12.0000, TRUE, 'Azucar refinada.', NOW() - INTERVAL '8 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Cupcake Oreo - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'HUE-001'), 4.0000, 12.0000, TRUE, 'Huevos frescos.', NOW() - INTERVAL '8 day'),
((SELECT receta_id FROM receta WHERE nombre = 'Cupcake Oreo - Receta Base'), (SELECT ingrediente_id FROM ingrediente WHERE codigo = 'CAC-001'), 40.0000, 12.0000, TRUE, 'Toque de cacao oscuro.', NOW() - INTERVAL '8 day');

INSERT INTO archivo_recurso (
  codigo_archivo, origen_modulo, tipo_archivo, nombre_original, nombre_fisico, mime_type, extension, tamano_bytes, checksum, ruta_relativa, estado, fecha_creacion, fecha_expiracion, creado_por_usuario_id
) VALUES
('ARC-REP-0102', 'REPORTES', 'CSV', 'cola-produccion-semanal.csv', 'cola-produccion-semanal.csv', 'text/csv', '.csv', 88412, 'chk-cola-prod-0102', 'cola-produccion-semanal.csv', 'DISPONIBLE', NOW() - INTERVAL '1 day', NOW() + INTERVAL '6 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
('ARC-REP-0103', 'REPORTES', 'TXT', 'resumen-semanal-ejecutivo.txt', 'resumen-semanal-ejecutivo.txt', 'text/plain', '.txt', 92344, 'chk-resumen-0103', 'resumen-semanal-ejecutivo.txt', 'DISPONIBLE', NOW() - INTERVAL '4 day', NOW() + INTERVAL '3 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
('ARC-REP-0104', 'REPORTES', 'TXT', 'resumen-expirado.txt', 'resumen-expirado.txt', 'text/plain', '.txt', 71200, 'chk-resumen-0104', 'resumen-expirado.txt', 'EXPIRADO', NOW() - INTERVAL '20 day', NOW() - INTERVAL '5 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'));

INSERT INTO job_reporte (
  codigo_job, tipo_reporte, parametros_json, estado, solicitado_por_usuario_id, fecha_solicitud, fecha_inicio, fecha_fin, intentos, mensaje_error, archivo_id, request_id, version
) VALUES
('JOB-REP-0102', 'COLA_PRODUCCION', '{"corte":"manana","estado":"ACTIVO"}'::jsonb, 'COMPLETADO', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day' + INTERVAL '2 minute', NOW() - INTERVAL '1 day' + INTERVAL '4 minute', 1, NULL, (SELECT archivo_id FROM archivo_recurso WHERE codigo_archivo = 'ARC-REP-0102'), 'req-reporte-0102', 1),
('JOB-REP-0103', 'RESUMEN_NEGOCIO', '{"fecha":"2026-03-24","estado":"ENTREGADOS"}'::jsonb, 'ERROR', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), NOW() - INTERVAL '12 hour', NOW() - INTERVAL '12 hour' + INTERVAL '1 minute', NOW() - INTERVAL '12 hour' + INTERVAL '2 minute', 2, 'No se pudo renderizar el encabezado del PDF en el primer intento.', NULL, 'req-reporte-0103', 1),
('JOB-REP-0104', 'COLA_PRODUCCION', '{"turno":"tarde"}'::jsonb, 'CANCELADO', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), NOW() - INTERVAL '9 hour', NOW() - INTERVAL '9 hour' + INTERVAL '1 minute', NOW() - INTERVAL '9 hour' + INTERVAL '1 minute', 1, 'Cancelado por duplicidad de solicitud.', NULL, 'req-reporte-0104', 1),
('JOB-REP-0105', 'RESUMEN_NEGOCIO', '{"fecha":"2026-03-10","estado":"TODOS"}'::jsonb, 'EXPIRADO', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), NOW() - INTERVAL '18 day', NOW() - INTERVAL '18 day' + INTERVAL '1 minute', NOW() - INTERVAL '18 day' + INTERVAL '2 minute', 1, NULL, (SELECT archivo_id FROM archivo_recurso WHERE codigo_archivo = 'ARC-REP-0104'), 'req-reporte-0105', 1),
('JOB-REP-0106', 'RESUMEN_NEGOCIO', '{"fecha":"2026-03-24","estado":"TODOS"}'::jsonb, 'COMPLETADO', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), NOW() - INTERVAL '4 day', NOW() - INTERVAL '4 day' + INTERVAL '2 minute', NOW() - INTERVAL '4 day' + INTERVAL '4 minute', 1, NULL, (SELECT archivo_id FROM archivo_recurso WHERE codigo_archivo = 'ARC-REP-0103'), 'req-reporte-0106', 1);

INSERT INTO notificacion (
  tipo_notificacion, titulo, mensaje, modulo, referencia_tipo, referencia_id, usuario_destino_id, estado, prioridad, payload_json, fecha_creacion
) VALUES
('COTIZACION_APROBADA', 'Cotizacion aprobada por cliente', 'La cotizacion COT-0202 fue aprobada y espera fecha de pedido.', 'COTIZACIONES', 'COTIZACION', 'COT-0202', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'atencion1'), 'LEIDA', 'MEDIA', '{"cotizacionCodigo":"COT-0202"}'::jsonb, NOW() - INTERVAL '5 day'),
('ABASTECIMIENTO_CRITICO', 'Ingredientes bajo minimo', 'Hay varios ingredientes criticos pendientes de reposicion inmediata.', 'ABASTECIMIENTO', 'DASHBOARD', 'ABAST-001', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 'NO_LEIDA', 'ALTA', '{"criticos":5}'::jsonb, NOW() - INTERVAL '2 hour'),
('PEDIDO_LISTO', 'Pedido listo para retiro', 'El pedido PED-0208 ya esta listo para coordinar entrega o retiro.', 'PEDIDOS', 'PEDIDO', 'PED-0208', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'atencion1'), 'NO_LEIDA', 'MEDIA', '{"pedidoCodigo":"PED-0208"}'::jsonb, NOW() - INTERVAL '35 minute'),
('REPORTE_ERROR', 'Reporte con error', 'El job JOB-REP-0103 termino con error y requiere nuevo intento.', 'REPORTES', 'JOB_REPORTE', 'JOB-REP-0103', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 'NO_LEIDA', 'ALTA', '{"jobCodigo":"JOB-REP-0103"}'::jsonb, NOW() - INTERVAL '12 hour'),
('REPORTE_EXPIRADO', 'Archivo expirado', 'El archivo del job JOB-REP-0105 ya vencio su ventana de descarga.', 'REPORTES', 'JOB_REPORTE', 'JOB-REP-0105', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 'LEIDA', 'BAJA', '{"jobCodigo":"JOB-REP-0105"}'::jsonb, NOW() - INTERVAL '4 day'),
('PRODUCCION_RETRASO', 'Produccion en decoracion', 'PED-0201 sigue en decoracion y requiere seguimiento del equipo.', 'PRODUCCION', 'PEDIDO', 'PED-0201', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1'), 'NO_LEIDA', 'MEDIA', '{"pedidoCodigo":"PED-0201"}'::jsonb, NOW() - INTERVAL '20 minute'),
('ORDEN_COMPRA_RECIBIDA', 'Recepcion completada', 'La orden OC-2026-0022 se recibio completamente y actualizo stock.', 'ABASTECIMIENTO', 'ORDEN_COMPRA', 'OC-2026-0022', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 'LEIDA', 'MEDIA', '{"ordenCompra":"OC-2026-0022"}'::jsonb, NOW() - INTERVAL '2 hour'),
('PEDIDO_CANCELADO', 'Pedido cancelado por cliente', 'El pedido PED-0207 fue cancelado antes de iniciar produccion.', 'PEDIDOS', 'PEDIDO', 'PED-0207', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'atencion1'), 'ARCHIVADA', 'BAJA', '{"pedidoCodigo":"PED-0207"}'::jsonb, NOW() - INTERVAL '4 day'),
('REPORTE_LISTO', 'Resumen ejecutivo listo', 'El archivo resumen-semanal-ejecutivo.pdf esta disponible para descarga.', 'REPORTES', 'JOB_REPORTE', 'JOB-REP-0106', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 'NO_LEIDA', 'MEDIA', '{"archivo":"ARC-REP-0103"}'::jsonb, NOW() - INTERVAL '3 day');

INSERT INTO auditoria_evento (
  codigo_evento, modulo, entidad, entidad_id, accion, actor_usuario_id, actor_rol, fecha_evento, valor_anterior_json, valor_nuevo_json, motivo, request_id, ip_origen
) VALUES
('COTIZACION_CONVERTIDA', 'COTIZACIONES', 'cotizacion', 'COT-0204', 'CONVERTIR_COTIZACION', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'atencion1'), 'ATENCION', NOW() - INTERVAL '3 day', '{"estado":"APROBADA"}'::jsonb, '{"estado":"CONVERTIDA"}'::jsonb, 'Se confirmo pedido corporativo.', 'req-audit-0201', '127.0.0.1'),
('PEDIDO_ENTREGADO', 'PEDIDOS', 'pedido', 'PED-0205', 'ENTREGAR_PEDIDO', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'atencion1'), 'ATENCION', NOW() - INTERVAL '2 hour', '{"estado":"LISTO"}'::jsonb, '{"estado":"ENTREGADO"}'::jsonb, 'Entrega confirmada en mostrador.', 'req-audit-0202', '127.0.0.1'),
('PEDIDO_CANCELADO', 'PEDIDOS', 'pedido', 'PED-0207', 'CANCELAR_PEDIDO', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'atencion1'), 'ATENCION', NOW() - INTERVAL '4 day', '{"estado":"REGISTRADO"}'::jsonb, '{"estado":"CANCELADO"}'::jsonb, 'Cancelacion solicitada por el cliente.', 'req-audit-0203', '127.0.0.1'),
('ORDEN_COMPRA_RECIBIDA', 'ABASTECIMIENTO', 'orden_compra', 'OC-2026-0022', 'RECIBIR_ORDEN_COMPRA', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 'ADMIN', NOW() - INTERVAL '2 hour', '{"estado":"ENVIADA"}'::jsonb, '{"estado":"RECIBIDA"}'::jsonb, 'Recepcion y validacion de empaques.', 'req-audit-0204', '127.0.0.1'),
('PRODUCCION_AVANZA_A_EMPAQUE', 'PRODUCCION', 'produccion', 'PED-0203', 'ACTUALIZAR_ESTADO_PRODUCCION', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1'), 'PRODUCCION', NOW() - INTERVAL '2 hour', '{"estado":"DECORACION"}'::jsonb, '{"estado":"EMPAQUE"}'::jsonb, 'Pedido pasa a cierre y despacho.', 'req-audit-0205', '127.0.0.1'),
('REPORTE_FALLIDO', 'REPORTES', 'job_reporte', 'JOB-REP-0103', 'PROCESAR_REPORTE', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 'ADMIN', NOW() - INTERVAL '12 hour', '{"estado":"EN_PROCESO"}'::jsonb, '{"estado":"ERROR"}'::jsonb, 'Error en el render del reporte.', 'req-audit-0206', '127.0.0.1'),
('REPORTE_COMPLETADO', 'REPORTES', 'job_reporte', 'JOB-REP-0106', 'PROCESAR_REPORTE', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 'ADMIN', NOW() - INTERVAL '3 day', '{"estado":"EN_PROCESO"}'::jsonb, '{"estado":"COMPLETADO"}'::jsonb, 'Reporte ejecutivo generado correctamente.', 'req-audit-0207', '127.0.0.1');


-- =============================================================================
-- Guía operativa: flujos consultivos para operación interna
-- =============================================================================
INSERT INTO caso_uso_modulo (codigo, nombre, descripcion, grupo, orden_visual, activo, created_at, updated_at, version) VALUES
  ('RESUMEN_DIA', 'Resumen del día', 'Vista inicial para revisar el pulso diario: encargos, producción, avisos y pendientes de mostrador.', 'OPERACION', 1, TRUE, NOW(), NOW(), 0),
  ('CLIENTES', 'Clientes', 'Registro y consulta de clientes que compran en mostrador o solicitan encargos personalizados.', 'COMERCIAL', 2, TRUE, NOW(), NOW(), 0),
  ('CATALOGO', 'Catálogo de productos', 'Administración de productos, precios, disponibilidad y vitrina digital de la pastelería.', 'COMERCIAL', 3, TRUE, NOW(), NOW(), 0),
  ('COTIZACIONES', 'Cotizaciones', 'Preparación de propuestas para tortas, mesas dulces y pedidos especiales antes de confirmarlos.', 'COMERCIAL', 4, TRUE, NOW(), NOW(), 0),
  ('PEDIDOS', 'Pedidos', 'Seguimiento de encargos confirmados desde el registro hasta la entrega o retiro.', 'OPERACION', 5, TRUE, NOW(), NOW(), 0),
  ('PRODUCCION', 'Producción', 'Organización de horneo, relleno, decoración y avance del trabajo interno.', 'OPERACION', 6, TRUE, NOW(), NOW(), 0),
  ('DECORACION_EMPAQUE', 'Decoración y empaque', 'Cierre visual del producto, control de empaque, retiro y entrega final.', 'OPERACION', 7, TRUE, NOW(), NOW(), 0),
  ('ABASTECIMIENTO', 'Abastecimiento', 'Control de insumos, mínimos, reposición y movimientos de entrada o salida.', 'OPERACION', 8, TRUE, NOW(), NOW(), 0),
  ('RECETAS', 'Recetas técnicas', 'Consulta de recetas base, cantidades, rendimiento y notas de preparación.', 'OPERACION', 9, TRUE, NOW(), NOW(), 0),
  ('PROVEEDORES', 'Proveedores', 'Gestión de proveedores, productos ofrecidos y compras pendientes.', 'ADMINISTRACION', 10, TRUE, NOW(), NOW(), 0),
  ('REPORTES', 'Reportes', 'Generación y revisión de reportes para decisiones semanales del negocio.', 'CONTROL', 11, TRUE, NOW(), NOW(), 0),
  ('NOTIFICACIONES', 'Notificaciones', 'Atención de avisos internos sobre pedidos, producción, reportes e inventario.', 'CONTROL', 12, TRUE, NOW(), NOW(), 0),
  ('USUARIOS', 'Usuarios y permisos', 'Administración de accesos según rol: administración, atención y producción.', 'ADMINISTRACION', 13, TRUE, NOW(), NOW(), 0),
  ('GUIA_OPERATIVA', 'Guía operativa', 'Consulta de flujos de trabajo para recordar cómo operar cada área del sistema.', 'CONTROL', 14, TRUE, NOW(), NOW(), 0)
ON CONFLICT (codigo) DO UPDATE SET
  nombre = EXCLUDED.nombre,
  descripcion = EXCLUDED.descripcion,
  grupo = EXCLUDED.grupo,
  orden_visual = EXCLUDED.orden_visual,
  activo = EXCLUDED.activo,
  updated_at = NOW();


-- Recetas técnicas de productos para descarga PDF desde el admin
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Torta de Chocolate", "tituloIngredientes": "Ingredientes", "ingredientes": "• 500 g harina preparada\n• 300 g azúcar\n• 120 g cacao amargo\n• 6 huevos\n• 350 ml leche\n• 250 g mantequilla\n• Ganache de chocolate", "tituloPasos": "Preparación", "pasos": "1. Precalienta el horno a 175 °C.\n2. Mezcla ingredientes secos.\n3. Integra huevos, leche y mantequilla.\n4. Hornea hasta que el centro esté firme.\n5. Enfría, rellena y cubre con ganache.", "tituloObservaciones": "Notas de producción", "observaciones": "• Conservar refrigerada y retirar 20 minutos antes de servir."}', updated_at = NOW() WHERE codigo = 'TORT-CHOC-01';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Torta de Vainilla con Frutos", "tituloIngredientes": "Ingredientes", "ingredientes": "• 500 g harina preparada\n• 280 g azúcar\n• 6 huevos\n• 300 ml leche\n• 200 g mantequilla\n• Esencia de vainilla\n• Frutos rojos", "tituloPasos": "Preparación", "pasos": "1. Bate mantequilla y azúcar.\n2. Agrega huevos y vainilla.\n3. Integra harina y leche por tandas.\n4. Hornea y deja enfriar.\n5. Rellena con crema y frutos rojos.", "tituloObservaciones": "Notas de producción", "observaciones": "• Usar frutos firmes para evitar exceso de humedad."}', updated_at = NOW() WHERE codigo = 'TORT-VAIN-FRUT';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Torta Red Velvet", "tituloIngredientes": "Ingredientes", "ingredientes": "• 480 g harina\n• 300 g azúcar\n• 30 g cacao\n• 6 huevos\n• 320 ml buttermilk\n• Colorante rojo alimentario\n• Frosting de queso crema", "tituloPasos": "Preparación", "pasos": "1. Prepara moldes y horno a 170 °C.\n2. Mezcla secos y líquidos por separado.\n3. Une la mezcla y añade colorante.\n4. Hornea en capas uniformes.\n5. Enfría y monta con frosting de queso crema.", "tituloObservaciones": "Notas de producción", "observaciones": "• Mantener refrigerada por el frosting."}', updated_at = NOW() WHERE codigo = 'TORT-RED-VEL';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Torta Tres Leches", "tituloIngredientes": "Ingredientes", "ingredientes": "• Bizcocho base\n• Leche evaporada\n• Leche condensada\n• Crema de leche\n• Merengue o crema batida\n• Canela opcional", "tituloPasos": "Preparación", "pasos": "1. Hornea el bizcocho y deja enfriar.\n2. Perfora suavemente la superficie.\n3. Mezcla las tres leches.\n4. Baña el bizcocho hasta absorber.\n5. Decora con crema o merengue.", "tituloObservaciones": "Notas de producción", "observaciones": "• Reposar en frío mínimo 4 horas."}', updated_at = NOW() WHERE codigo = 'TORT-3-LECH';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Brownie Individual", "tituloIngredientes": "Ingredientes", "ingredientes": "• Chocolate semiamargo\n• Mantequilla\n• Azúcar\n• Huevos\n• Harina\n• Cacao\n• Nueces opcionales", "tituloPasos": "Preparación", "pasos": "1. Derrite chocolate con mantequilla.\n2. Bate huevos con azúcar.\n3. Integra chocolate, harina y cacao.\n4. Vierte en molde bajo.\n5. Hornea y corta porciones individuales.", "tituloObservaciones": "Notas de producción", "observaciones": "• No sobrehornear para conservar textura húmeda."}', updated_at = NOW() WHERE codigo = 'BROWNIE-IND';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Alfajor Premium", "tituloIngredientes": "Ingredientes", "ingredientes": "• Maicena\n• Harina\n• Mantequilla\n• Azúcar impalpable\n• Yemas\n• Dulce de leche\n• Coco rallado", "tituloPasos": "Preparación", "pasos": "1. Prepara una masa suave con mantequilla, secos y yemas.\n2. Refrigera la masa antes de estirar.\n3. Corta discos y hornea sin dorar demasiado.\n4. Rellena con dulce de leche.\n5. Pasa los bordes por coco rallado.", "tituloObservaciones": "Notas de producción", "observaciones": "• Manipular con cuidado porque la masa es frágil."}', updated_at = NOW() WHERE codigo = 'ALFAJOR-PREM';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Cheesecake de Frutos Rojos", "tituloIngredientes": "Ingredientes", "ingredientes": "• Galleta molida\n• Mantequilla\n• Queso crema\n• Azúcar\n• Huevos\n• Crema de leche\n• Salsa de frutos rojos", "tituloPasos": "Preparación", "pasos": "1. Prepara base de galleta y compacta.\n2. Bate queso crema con azúcar.\n3. Agrega huevos y crema.\n4. Hornea a temperatura baja.\n5. Enfría y cubre con frutos rojos.", "tituloObservaciones": "Notas de producción", "observaciones": "• Reposar en refrigeración antes de cortar."}', updated_at = NOW() WHERE codigo = 'CHEESE-FRUT';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Galleta Decorada", "tituloIngredientes": "Ingredientes", "ingredientes": "• Harina\n• Mantequilla\n• Azúcar impalpable\n• Huevo\n• Vainilla\n• Glaseado real\n• Colorantes alimentarios", "tituloPasos": "Preparación", "pasos": "1. Prepara masa de mantequilla.\n2. Refrigera, estira y corta figuras.\n3. Hornea hasta bordes ligeramente dorados.\n4. Deja enfriar por completo.\n5. Decora con glaseado real.", "tituloObservaciones": "Notas de producción", "observaciones": "• Esperar secado completo antes de empacar."}', updated_at = NOW() WHERE codigo = 'GALLETA-DEC';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Café Americano", "tituloIngredientes": "Ingredientes", "ingredientes": "• Café molido\n• Agua filtrada\n• Azúcar opcional\n• Vaso o taza de servicio", "tituloPasos": "Preparación", "pasos": "1. Prepara espresso o café concentrado.\n2. Agrega agua caliente según intensidad deseada.\n3. Sirve en taza limpia.\n4. Ofrece azúcar aparte si aplica.", "tituloObservaciones": "Notas de producción", "observaciones": "• Servir inmediatamente."}', updated_at = NOW() WHERE codigo = 'CAFE-AMER';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Cupcake de Vainilla", "tituloIngredientes": "Ingredientes", "ingredientes": "• Harina preparada\n• Azúcar\n• Huevos\n• Mantequilla\n• Leche\n• Vainilla\n• Frosting de vainilla", "tituloPasos": "Preparación", "pasos": "1. Bate mantequilla y azúcar.\n2. Agrega huevos y vainilla.\n3. Integra harina y leche.\n4. Llena capacillos a dos tercios.\n5. Hornea, enfría y decora con frosting.", "tituloObservaciones": "Notas de producción", "observaciones": "• No decorar mientras estén calientes."}', updated_at = NOW() WHERE codigo = 'CUPK-VAIN-01';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Cupcake de Chocolate", "tituloIngredientes": "Ingredientes", "ingredientes": "• Harina\n• Cacao\n• Azúcar\n• Huevos\n• Leche\n• Aceite o mantequilla\n• Ganache o buttercream", "tituloPasos": "Preparación", "pasos": "1. Mezcla secos.\n2. Integra líquidos hasta obtener masa uniforme.\n3. Llena capacillos.\n4. Hornea y deja enfriar.\n5. Decora con crema o ganache.", "tituloObservaciones": "Notas de producción", "observaciones": "• Mantener cubiertos para evitar resequedad."}', updated_at = NOW() WHERE codigo = 'CUPK-CHOC-01';
UPDATE producto SET receta_json = '{"titulo": "Ficha técnica - Mesa Dulce 25 Personas", "tituloIngredientes": "Ingredientes", "ingredientes": "• Mini cupcakes\n• Mini postres\n• Galletas decoradas\n• Macarons opcionales\n• Base o torta central\n• Empaques y bandejas", "tituloPasos": "Preparación", "pasos": "1. Define combinación según evento.\n2. Prepara productos por familias.\n3. Monta por niveles y colores.\n4. Verifica cantidad total para 25 personas.\n5. Empaca o instala según acuerdo con el cliente.", "tituloObservaciones": "Notas de producción", "observaciones": "• Requiere coordinación previa de horario y montaje."}', updated_at = NOW() WHERE codigo = 'MESA-DULCE-25';
UPDATE producto SET receta_json = '{"titulo": "Ficha técnica - Mesa Dulce 50 Personas", "tituloIngredientes": "Ingredientes", "ingredientes": "• Cupcakes\n• Mini postres\n• Cake pops\n• Galletas\n• Macarons\n• Torta central\n• Decoración de mesa", "tituloPasos": "Preparación", "pasos": "1. Confirma temática y paleta de color.\n2. Produce por tandas para controlar tiempos.\n3. Clasifica productos por fragilidad.\n4. Monta bandejas y soportes.\n5. Verifica inventario de empaque antes de salida.", "tituloObservaciones": "Notas de producción", "observaciones": "• Asignar responsable de montaje si hay entrega externa."}', updated_at = NOW() WHERE codigo = 'MESA-DULCE-50';
UPDATE producto SET receta_json = '{"titulo": "Ficha técnica - Caja Mini Postres", "tituloIngredientes": "Ingredientes", "ingredientes": "• Mini brownies\n• Mini cheesecakes\n• Vasitos dulces\n• Alfajores\n• Base de caja\n• Separadores", "tituloPasos": "Preparación", "pasos": "1. Arma variedad según disponibilidad.\n2. Controla tamaño uniforme de cada pieza.\n3. Coloca separadores en caja.\n4. Ubica productos frágiles al final.\n5. Etiqueta y refrigera si corresponde.", "tituloObservaciones": "Notas de producción", "observaciones": "• Ideal para regalos o degustaciones."}', updated_at = NOW() WHERE codigo = 'CAJA-MINI';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Torta de Zanahoria y Nuez", "tituloIngredientes": "Ingredientes", "ingredientes": "• Zanahoria rallada\n• Harina\n• Azúcar morena\n• Huevos\n• Aceite vegetal\n• Canela\n• Nueces\n• Frosting de queso crema", "tituloPasos": "Preparación", "pasos": "1. Mezcla secos con canela.\n2. Integra huevos, aceite y zanahoria.\n3. Agrega nueces al final.\n4. Hornea hasta que el centro esté firme.\n5. Enfría y cubre con frosting de queso crema.", "tituloObservaciones": "Notas de producción", "observaciones": "• Mantener refrigerada por la cobertura."}', updated_at = NOW() WHERE codigo = 'TORT-ZANA-01';
UPDATE producto SET receta_json = '{"titulo": "Ficha técnica - Naked Cake de Boda", "tituloIngredientes": "Ingredientes", "ingredientes": "• Bizcocho de vainilla\n• Crema de mantequilla\n• Relleno de frutos rojos\n• Flores comestibles o decorativas\n• Base rígida", "tituloPasos": "Preparación", "pasos": "1. Hornea capas parejas.\n2. Nivela cada bizcocho antes de montar.\n3. Alterna crema y relleno.\n4. Aplica cobertura ligera dejando capas visibles.\n5. Decora y refrigera antes del traslado.", "tituloObservaciones": "Notas de producción", "observaciones": "• Transportar en base firme y caja alta."}', updated_at = NOW() WHERE codigo = 'TORT-BODA-01';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Tiramisú Familiar", "tituloIngredientes": "Ingredientes", "ingredientes": "• Bizcotelas\n• Café concentrado\n• Queso mascarpone o crema\n• Azúcar\n• Cacao en polvo\n• Huevos pasteurizados o crema batida", "tituloPasos": "Preparación", "pasos": "1. Prepara café y deja enfriar.\n2. Bate crema con queso y azúcar.\n3. Remoja bizcotelas sin saturarlas.\n4. Arma capas alternando crema y bizcotela.\n5. Refrigera y espolvorea cacao antes de servir.", "tituloObservaciones": "Notas de producción", "observaciones": "• Mantener siempre refrigerado."}', updated_at = NOW() WHERE codigo = 'TIRA-FAM-01';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Pie de Limón Artesanal", "tituloIngredientes": "Ingredientes", "ingredientes": "• Base de masa quebrada\n• Jugo de limón\n• Leche condensada\n• Yemas\n• Merengue o crema\n• Ralladura de limón", "tituloPasos": "Preparación", "pasos": "1. Hornea la base hasta dorar.\n2. Mezcla limón, leche condensada y yemas.\n3. Rellena la base y hornea brevemente.\n4. Enfría y decora con merengue.\n5. Refrigera antes de cortar.", "tituloObservaciones": "Notas de producción", "observaciones": "• Controlar acidez y dulzor según preferencia del cliente."}', updated_at = NOW() WHERE codigo = 'PIE-LIMON-01';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Galletas Mix de Mantequilla", "tituloIngredientes": "Ingredientes", "ingredientes": "• Mantequilla\n• Harina\n• Azúcar impalpable\n• Huevo\n• Vainilla\n• Chocolate o mermelada opcional", "tituloPasos": "Preparación", "pasos": "1. Prepara masa base de mantequilla.\n2. Divide para sabores si aplica.\n3. Forma piezas uniformes.\n4. Hornea por tandas controlando color.\n5. Enfría antes de empacar.", "tituloObservaciones": "Notas de producción", "observaciones": "• Guardar en envase hermético."}', updated_at = NOW() WHERE codigo = 'GALL-MIX-01';
UPDATE producto SET receta_json = '{"titulo": "Ficha técnica - Galleta Corporativa con Logo", "tituloIngredientes": "Ingredientes", "ingredientes": "• Masa de mantequilla\n• Glaseado o fondant\n• Colorantes\n• Plantilla o impresión comestible\n• Empaque individual", "tituloPasos": "Preparación", "pasos": "1. Hornea galletas del mismo tamaño.\n2. Deja enfriar por completo.\n3. Aplica base de glaseado o fondant.\n4. Coloca diseño corporativo con cuidado.\n5. Empaca individualmente cuando esté seco.", "tituloObservaciones": "Notas de producción", "observaciones": "• Confirmar diseño con el cliente antes de producir."}', updated_at = NOW() WHERE codigo = 'GALL-CORP-01';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Frappé Mocha", "tituloIngredientes": "Ingredientes", "ingredientes": "• Café frío\n• Leche\n• Chocolate\n• Hielo\n• Crema batida\n• Sirope de chocolate", "tituloPasos": "Preparación", "pasos": "1. Licúa café, leche, chocolate e hielo.\n2. Sirve en vaso limpio.\n3. Agrega crema batida.\n4. Decora con sirope de chocolate.\n5. Entrega inmediatamente.", "tituloObservaciones": "Notas de producción", "observaciones": "• No preparar con mucha anticipación para evitar separación."}', updated_at = NOW() WHERE codigo = 'FRAP-MOCHA';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Chocolate Caliente de la Casa", "tituloIngredientes": "Ingredientes", "ingredientes": "• Leche\n• Chocolate semiamargo\n• Cacao\n• Azúcar\n• Canela opcional\n• Marshmallows opcionales", "tituloPasos": "Preparación", "pasos": "1. Calienta la leche sin hervir fuerte.\n2. Integra chocolate y cacao.\n3. Endulza al gusto.\n4. Sirve caliente en taza.\n5. Decora con marshmallows si aplica.", "tituloObservaciones": "Notas de producción", "observaciones": "• Mantener temperatura segura de servicio."}', updated_at = NOW() WHERE codigo = 'CHOC-CAL-01';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Cupcake Red Velvet", "tituloIngredientes": "Ingredientes", "ingredientes": "• Harina\n• Cacao\n• Azúcar\n• Huevos\n• Buttermilk\n• Colorante rojo\n• Frosting de queso crema", "tituloPasos": "Preparación", "pasos": "1. Mezcla secos.\n2. Integra líquidos y colorante.\n3. Llena capacillos a dos tercios.\n4. Hornea y enfría.\n5. Decora con frosting y migas rojas.", "tituloObservaciones": "Notas de producción", "observaciones": "• Refrigerar si el frosting contiene queso crema."}', updated_at = NOW() WHERE codigo = 'CUPK-RED-01';
UPDATE producto SET receta_json = '{"titulo": "Receta técnica - Cupcake Oreo", "tituloIngredientes": "Ingredientes", "ingredientes": "• Harina\n• Cacao\n• Azúcar\n• Huevos\n• Leche\n• Galletas trituradas\n• Crema cookies and cream", "tituloPasos": "Preparación", "pasos": "1. Prepara masa de chocolate.\n2. Agrega trozos de galleta.\n3. Hornea en capacillos.\n4. Enfría por completo.\n5. Decora con crema y galleta.", "tituloObservaciones": "Notas de producción", "observaciones": "• Mantener en caja cerrada para conservar textura."}', updated_at = NOW() WHERE codigo = 'CUPK-OREO-01';
UPDATE producto SET receta_json = '{"titulo": "Ficha técnica - Caja Brigadeiro 12", "tituloIngredientes": "Ingredientes", "ingredientes": "• Leche condensada\n• Cacao\n• Mantequilla\n• Chispas de chocolate\n• Capacillos\n• Caja de 12 unidades", "tituloPasos": "Preparación", "pasos": "1. Cocina leche condensada, cacao y mantequilla hasta punto firme.\n2. Enfría la mezcla.\n3. Forma 12 bolitas uniformes.\n4. Cubre con chispas o toppings.\n5. Coloca en capacillos y caja.", "tituloObservaciones": "Notas de producción", "observaciones": "• Conservar en lugar fresco o refrigerado según clima."}', updated_at = NOW() WHERE codigo = 'BRIGA-BOX-12';
UPDATE producto SET receta_json = '{"titulo": "Ficha técnica - Mesa Dulce 80 Personas", "tituloIngredientes": "Ingredientes", "ingredientes": "• Torta central\n• Cupcakes\n• Mini postres\n• Cake pops\n• Galletas\n• Macarons\n• Decoración y soportes", "tituloPasos": "Preparación", "pasos": "1. Confirma cantidad de invitados, horario y lugar.\n2. Planifica producción por familias de producto.\n3. Prepara bases, rellenos y decoración con anticipación.\n4. Monta la mesa con niveles, simetría y etiquetas internas.\n5. Verifica transporte, empaque y responsable de montaje.", "tituloObservaciones": "Notas de producción", "observaciones": "• Requiere agenda previa y confirmación de logística."}', updated_at = NOW() WHERE codigo = 'MESA-DULCE-80';

-- Guía operativa ampliada
INSERT INTO caso_uso_operativo (modulo, codigo, titulo, actor_principal, objetivo, punto_inicio, orden_visual, estado, version_flujo, actualizado_en, activo, created_at, updated_at, version) VALUES
  ('RESUMEN_DIA', 'CU-GO-001', 'Revisar el resumen del día', 'Administrador o encargada de turno', 'Entender en pocos minutos qué se debe atender primero: encargos, producción, avisos y pendientes del local.', 'Haz clic en Resumen desde el menú lateral.', 1, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('RESUMEN_DIA', 'CU-GO-002', 'Usar las notificaciones del resumen', 'Administrador o encargada de turno', 'Atender avisos críticos sin revisar módulo por módulo al inicio del turno.', 'Haz clic en el botón Notificaciones del resumen.', 2, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('RESUMEN_DIA', 'CU-GO-003', 'Interpretar la meta semanal', 'Administrador', 'Comparar ventas cerradas contra una meta mínima para decidir si hay que impulsar productos o pedidos.', 'Haz clic en Resumen y mira la tarjeta Meta mínima semanal.', 3, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('CLIENTES', 'CU-GO-010', 'Registrar cliente de mostrador o encargo', 'Atención al cliente', 'Guardar los datos mínimos de una persona que compra, consulta o solicita una torta personalizada.', 'Haz clic en Clientes desde el menú lateral.', 1, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('CLIENTES', 'CU-GO-011', 'Buscar y corregir datos de cliente', 'Atención al cliente', 'Evitar duplicados y mantener información de contacto útil para pedidos y cotizaciones.', 'Haz clic en Clientes y revisa la lista.', 2, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('CLIENTES', 'CU-GO-012', 'Usar cliente en cotización o pedido', 'Atención al cliente', 'Relacionar cada venta o propuesta con una persona real para seguimiento y recompra.', 'Empieza desde Clientes, Cotizaciones o Pedidos.', 3, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('CATALOGO', 'CU-GO-020', 'Crear producto de catálogo', 'Administrador', 'Agregar un producto nuevo con precio, categoría, imagen y estado de publicación.', 'Haz clic en Productos desde el menú lateral.', 1, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('CATALOGO', 'CU-GO-021', 'Editar producto y descargar receta PDF', 'Administrador o producción', 'Actualizar información de producto y bajar la receta técnica cuando exista.', 'Haz clic en Productos y busca la tarjeta del producto.', 2, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('CATALOGO', 'CU-GO-022', 'Publicar u ocultar producto de la vitrina', 'Administrador', 'Controlar qué productos aparecen en el catálogo público sin borrar información interna.', 'Haz clic en Productos y edita el producto.', 3, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('COTIZACIONES', 'CU-GO-030', 'Crear cotización para torta o mesa dulce', 'Atención al cliente', 'Preparar una propuesta antes de confirmar un encargo especial.', 'Haz clic en Cotizaciones desde el menú lateral.', 1, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('COTIZACIONES', 'CU-GO-031', 'Convertir cotización aprobada en pedido', 'Atención al cliente', 'Pasar una propuesta aceptada a compromiso real de entrega.', 'Haz clic en Cotizaciones y ubica la propuesta aprobada.', 2, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('COTIZACIONES', 'CU-GO-032', 'Dar seguimiento a cotizaciones pendientes', 'Administrador o atención', 'Evitar que propuestas comerciales queden olvidadas.', 'Haz clic en Cotizaciones y filtra pendientes.', 3, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('PEDIDOS', 'CU-GO-040', 'Registrar pedido confirmado', 'Atención al cliente', 'Crear el pedido real cuando ya existe acuerdo de compra o entrega.', 'Haz clic en Pedidos desde el menú lateral.', 1, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('PEDIDOS', 'CU-GO-041', 'Actualizar estado de pedido', 'Atención o producción', 'Mantener visible si el pedido está registrado, en preparación, listo o entregado.', 'Haz clic en Pedidos y ubica el pedido.', 2, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('PEDIDOS', 'CU-GO-042', 'Cerrar pedido entregado', 'Atención al cliente', 'Cerrar correctamente la venta cuando el cliente retira o recibe el producto.', 'Haz clic en Pedidos y revisa pedidos listos.', 3, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('PRODUCCION', 'CU-GO-050', 'Organizar cola de producción', 'Producción', 'Priorizar horneo, relleno, decoración y empaque según fechas de entrega.', 'Haz clic en Producción desde el menú lateral.', 1, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('PRODUCCION', 'CU-GO-051', 'Registrar avance de cocina', 'Producción', 'Dejar constancia del avance real para que atención no dependa de preguntar en cocina.', 'Haz clic en Producción y abre el pedido correspondiente.', 2, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('PRODUCCION', 'CU-GO-052', 'Consultar receta antes de preparar', 'Producción', 'Usar la receta técnica del producto para mantener consistencia.', 'Puedes iniciar desde Producción o Productos.', 3, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('DECORACION_EMPAQUE', 'CU-GO-060', 'Verificar decoración final', 'Producción o decoración', 'Comparar el producto terminado contra las notas del pedido antes de empacar.', 'Haz clic en Producción o Pedidos y abre el encargo.', 1, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('DECORACION_EMPAQUE', 'CU-GO-061', 'Confirmar empaque y salida', 'Producción o atención', 'Evitar entregar productos sin protección o con datos de cliente equivocados.', 'Haz clic en Pedidos y revisa pedidos listos.', 2, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('DECORACION_EMPAQUE', 'CU-GO-062', 'Registrar incidencia de entrega', 'Atención al cliente', 'Dejar anotado cualquier cambio, daño, retraso o ajuste antes de cerrar el pedido.', 'Haz clic en Pedidos y abre el pedido afectado.', 3, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('ABASTECIMIENTO', 'CU-GO-070', 'Revisar insumos críticos', 'Administrador o compras', 'Detectar mínimos de ingredientes, empaques o insumos antes de afectar producción.', 'Haz clic en Abastecimiento desde el menú lateral.', 1, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('ABASTECIMIENTO', 'CU-GO-071', 'Registrar movimiento de inventario', 'Administrador o compras', 'Mantener entradas, salidas, mermas y ajustes visibles para el negocio.', 'Haz clic en Abastecimiento y luego Inventario.', 2, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('ABASTECIMIENTO', 'CU-GO-072', 'Crear orden de compra', 'Administrador o compras', 'Preparar una compra con proveedor antes de recibir mercadería.', 'Haz clic en Abastecimiento y entra a Compras.', 3, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('RECETAS', 'CU-GO-080', 'Consultar receta técnica desde Productos', 'Producción', 'Descargar una ficha clara con logo, imagen del postre, ingredientes y preparación.', 'Haz clic en Productos desde el menú lateral.', 1, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('RECETAS', 'CU-GO-081', 'Editar receta de producto', 'Administrador o producción autorizada', 'Actualizar ingredientes, pasos y observaciones cuando cambie la preparación.', 'Haz clic en Productos y edita el producto.', 2, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('RECETAS', 'CU-GO-082', 'Usar receta para calcular producción', 'Producción', 'Leer cantidades y pasos antes de preparar varios productos similares.', 'Haz clic en Productos o Producción según el punto de partida.', 3, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('PROVEEDORES', 'CU-GO-090', 'Registrar proveedor nuevo', 'Administrador o compras', 'Guardar contacto y datos básicos de quien abastece insumos o empaques.', 'Haz clic en Abastecimiento y luego Proveedores.', 1, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('PROVEEDORES', 'CU-GO-091', 'Asociar insumo con proveedor', 'Administrador o compras', 'Saber quién vende cada insumo y con qué precio o presentación.', 'Haz clic en Abastecimiento y revisa proveedor o insumo.', 2, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('PROVEEDORES', 'CU-GO-092', 'Desactivar proveedor problemático', 'Administrador', 'Evitar usar proveedores que ya no trabajan con el local o entregan mal.', 'Haz clic en Abastecimiento y luego Proveedores.', 3, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('REPORTES', 'CU-GO-100', 'Generar reporte de negocio', 'Administrador', 'Crear un archivo PDF o reporte interno para revisar ventas, pedidos y producción.', 'Haz clic en Reportes desde el menú lateral.', 1, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('REPORTES', 'CU-GO-101', 'Revisar reportes generados', 'Administrador', 'Consultar archivos anteriores sin volver a calcular todo innecesariamente.', 'Haz clic en Reportes y revisa la lista.', 2, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('REPORTES', 'CU-GO-102', 'Usar reportes para decidir compras', 'Administrador', 'Relacionar ventas y producción con necesidades de abastecimiento.', 'Empieza desde Reportes y luego Abastecimiento.', 3, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('NOTIFICACIONES', 'CU-GO-110', 'Leer notificaciones pendientes', 'Usuario del sistema', 'Enterarse de avisos internos sin revisar todos los módulos.', 'Haz clic en Notificaciones desde el resumen o menú superior.', 1, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('NOTIFICACIONES', 'CU-GO-111', 'Archivar notificaciones atendidas', 'Usuario del sistema', 'Mantener la bandeja limpia sin perder señales importantes.', 'Haz clic en Notificaciones y revisa las atendidas.', 2, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('NOTIFICACIONES', 'CU-GO-112', 'Usar avisos para cambiar de módulo', 'Usuario del sistema', 'Saltar desde un aviso hacia el área que necesita atención.', 'Haz clic en Notificaciones.', 3, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('USUARIOS', 'CU-GO-120', 'Crear usuario interno', 'Administrador', 'Dar acceso individual a una persona del equipo sin compartir credenciales.', 'Haz clic en Usuarios si el módulo está habilitado para administración.', 1, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('USUARIOS', 'CU-GO-121', 'Cambiar rol o permiso', 'Administrador', 'Ajustar accesos cuando una persona cambia de responsabilidad.', 'Haz clic en Usuarios y busca la cuenta.', 2, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('USUARIOS', 'CU-GO-122', 'Desactivar usuario', 'Administrador', 'Bloquear acceso cuando una persona ya no debe entrar al sistema.', 'Haz clic en Usuarios y localiza la cuenta.', 3, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('GUIA_OPERATIVA', 'CU-GO-130', 'Consultar la guía operativa', 'Cualquier usuario interno', 'Recordar el flujo correcto de cada área sin depender de memoria ni instrucciones sueltas.', 'Haz clic en Guía operativa desde el menú lateral.', 1, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('GUIA_OPERATIVA', 'CU-GO-131', 'Capacitar a alguien con la guía', 'Administrador o responsable de turno', 'Usar la guía como apoyo de entrenamiento para personal nuevo.', 'Haz clic en Guía operativa durante la capacitación.', 2, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0),
  ('GUIA_OPERATIVA', 'CU-GO-132', 'Detectar procedimiento desactualizado', 'Usuario interno', 'Reportar cuando la operación real ya no coincide con la guía escrita.', 'Haz clic en Guía operativa y abre el caso dudoso.', 3, 'LISTO', 2, NOW(), TRUE, NOW(), NOW(), 0)
ON CONFLICT (codigo) DO UPDATE SET
  modulo = EXCLUDED.modulo,
  titulo = EXCLUDED.titulo,
  actor_principal = EXCLUDED.actor_principal,
  objetivo = EXCLUDED.objetivo,
  punto_inicio = EXCLUDED.punto_inicio,
  orden_visual = EXCLUDED.orden_visual,
  estado = EXCLUDED.estado,
  version_flujo = EXCLUDED.version_flujo,
  actualizado_en = NOW(),
  activo = EXCLUDED.activo,
  updated_at = NOW();

WITH step_seed(codigo, numero, descripcion) AS (
  VALUES
  ('CU-GO-001', 1, 'Haz clic en Resumen en el menú lateral izquierdo.'),
  ('CU-GO-001', 2, 'Revisa la tarjeta Caja del día para confirmar el dinero cerrado en pedidos entregados.'),
  ('CU-GO-001', 3, 'Revisa Clientes activos, Cotizaciones pendientes, Pedidos listos y Producción activa.'),
  ('CU-GO-001', 4, 'Haz clic en Actualizar datos si necesitas refrescar el pulso del negocio.'),
  ('CU-GO-001', 5, 'Usa las alertas superiores para decidir si debes entrar a Pedidos, Producción, Abastecimiento o Reportes.'),
  ('CU-GO-002', 1, 'Haz clic en Notificaciones, arriba a la derecha del resumen.'),
  ('CU-GO-002', 2, 'Lee primero los avisos marcados como pendientes o de prioridad alta.'),
  ('CU-GO-002', 3, 'Haz clic en el módulo relacionado si el aviso menciona pedidos, producción, reportes o abastecimiento.'),
  ('CU-GO-002', 4, 'Cuando el aviso ya esté resuelto, márcalo como leído o archívalo desde Notificaciones.'),
  ('CU-GO-002', 5, 'No cierres avisos críticos si todavía falta una acción humana.'),
  ('CU-GO-003', 1, 'Haz clic en Resumen en el menú lateral.'),
  ('CU-GO-003', 2, 'Mira la tarjeta Meta mínima semanal y compara el porcentaje con el valor esperado.'),
  ('CU-GO-003', 3, 'Si el porcentaje está bajo, haz clic en Cotizaciones para revisar propuestas pendientes.'),
  ('CU-GO-003', 4, 'Si hay pedidos listos, haz clic en Pedidos para confirmar entrega o cierre.'),
  ('CU-GO-003', 5, 'Usa esta lectura como señal; no reemplaza revisar el detalle comercial.'),
  ('CU-GO-010', 1, 'Haz clic en Clientes en el menú lateral izquierdo.'),
  ('CU-GO-010', 2, 'Haz clic en el formulario Nuevo cliente o en el área de registro disponible.'),
  ('CU-GO-010', 3, 'Escribe nombre completo, teléfono y correo si el cliente lo proporciona.'),
  ('CU-GO-010', 4, 'Agrega observaciones útiles: alergias informadas, retiro en local, entrega o preferencia recurrente.'),
  ('CU-GO-010', 5, 'Haz clic en Registrar cliente y verifica que aparezca en la lista.'),
  ('CU-GO-011', 1, 'Haz clic en Clientes en el menú lateral.'),
  ('CU-GO-011', 2, 'Usa la lista o búsqueda para ubicar al cliente antes de crear otro registro.'),
  ('CU-GO-011', 3, 'Haz clic en Editar en la tarjeta o fila del cliente.'),
  ('CU-GO-011', 4, 'Corrige teléfono, correo u observaciones necesarias.'),
  ('CU-GO-011', 5, 'Haz clic en Guardar cambios y confirma que el dato actualizado se mantenga.'),
  ('CU-GO-012', 1, 'Haz clic en Clientes y confirma que la persona existe.'),
  ('CU-GO-012', 2, 'Si no existe, registra primero al cliente.'),
  ('CU-GO-012', 3, 'Haz clic en Cotizaciones o Pedidos según el caso.'),
  ('CU-GO-012', 4, 'Selecciona el cliente en el formulario correspondiente.'),
  ('CU-GO-012', 5, 'Guarda la operación para que el historial comercial quede asociado.'),
  ('CU-GO-020', 1, 'Haz clic en Productos en el menú lateral.'),
  ('CU-GO-020', 2, 'En el formulario Nuevo producto, elige la categoría y escribe código, nombre y descripción.'),
  ('CU-GO-020', 3, 'Completa precio base y marca si requiere cotización previa.'),
  ('CU-GO-020', 4, 'Haz clic en Foto del producto para subir una imagen cuadrada sobre fondo limpio.'),
  ('CU-GO-020', 5, 'Haz clic en Registrar producto y revisa que aparezca en el catálogo administrativo.'),
  ('CU-GO-021', 1, 'Haz clic en Productos en el menú lateral.'),
  ('CU-GO-021', 2, 'Busca la tarjeta del producto que necesitas revisar.'),
  ('CU-GO-021', 3, 'Haz clic en Editar para cargar sus datos en el formulario.'),
  ('CU-GO-021', 4, 'Si solo necesitas la ficha técnica, haz clic en Descargar receta PDF en la tarjeta del producto.'),
  ('CU-GO-021', 5, 'Abre el PDF descargado y verifica logo, imagen del postre, ingredientes y preparación.'),
  ('CU-GO-022', 1, 'Haz clic en Productos en el menú lateral.'),
  ('CU-GO-022', 2, 'Haz clic en Editar sobre el producto correspondiente.'),
  ('CU-GO-022', 3, 'Marca Producto activo si se puede vender internamente.'),
  ('CU-GO-022', 4, 'Marca Visible en catálogo público si debe mostrarse en la landing.'),
  ('CU-GO-022', 5, 'Haz clic en Guardar cambios y revisa la vitrina pública si corresponde.'),
  ('CU-GO-030', 1, 'Haz clic en Cotizaciones en el menú lateral.'),
  ('CU-GO-030', 2, 'Haz clic en Nueva cotización o usa el formulario visible.'),
  ('CU-GO-030', 3, 'Selecciona el cliente; si no existe, ve primero a Clientes y regístralo.'),
  ('CU-GO-030', 4, 'Agrega productos, cantidades, notas de decoración, fecha tentativa y precio estimado.'),
  ('CU-GO-030', 5, 'Haz clic en Guardar cotización y revisa que quede pendiente o aprobada según el caso.'),
  ('CU-GO-031', 1, 'Haz clic en Cotizaciones en el menú lateral.'),
  ('CU-GO-031', 2, 'Busca la cotización aprobada por el cliente.'),
  ('CU-GO-031', 3, 'Revisa productos, cantidades, total y observaciones antes de convertir.'),
  ('CU-GO-031', 4, 'Haz clic en Convertir a pedido si la opción está disponible, o registra el pedido manualmente con los mismos datos.'),
  ('CU-GO-031', 5, 'Confirma fecha de entrega y prioridad en el módulo Pedidos.'),
  ('CU-GO-032', 1, 'Haz clic en Cotizaciones en el menú lateral.'),
  ('CU-GO-032', 2, 'Revisa las cotizaciones con estado Pendiente.'),
  ('CU-GO-032', 3, 'Haz clic en la cotización para leer notas, productos y fecha probable.'),
  ('CU-GO-032', 4, 'Contacta al cliente por el canal registrado y actualiza observaciones si aplica.'),
  ('CU-GO-032', 5, 'Cambia el estado cuando el cliente apruebe, rechace o pida ajuste.'),
  ('CU-GO-040', 1, 'Haz clic en Pedidos en el menú lateral.'),
  ('CU-GO-040', 2, 'Haz clic en Nuevo pedido o usa el formulario de registro.'),
  ('CU-GO-040', 3, 'Selecciona cliente, fecha estimada, prioridad y origen del pedido.'),
  ('CU-GO-040', 4, 'Agrega productos, cantidades, precio y notas de preparación o decoración.'),
  ('CU-GO-040', 5, 'Haz clic en Registrar pedido y confirma que aparezca en la lista.'),
  ('CU-GO-041', 1, 'Haz clic en Pedidos en el menú lateral.'),
  ('CU-GO-041', 2, 'Busca el pedido por código, cliente o fecha cercana.'),
  ('CU-GO-041', 3, 'Haz clic en el control de estado del pedido.'),
  ('CU-GO-041', 4, 'Cambia el estado solo cuando la acción real ya ocurrió.'),
  ('CU-GO-041', 5, 'Verifica que el cambio se refleje también en Resumen y Producción si corresponde.'),
  ('CU-GO-042', 1, 'Haz clic en Pedidos en el menú lateral.'),
  ('CU-GO-042', 2, 'Filtra o localiza los pedidos listos para salida.'),
  ('CU-GO-042', 3, 'Confirma físicamente el producto, empaque, nombre del cliente y observaciones.'),
  ('CU-GO-042', 4, 'Haz clic en el estado Entregado solo cuando la entrega o retiro ya fue confirmado.'),
  ('CU-GO-042', 5, 'Revisa que el cierre alimente la caja del día y el resumen semanal.'),
  ('CU-GO-050', 1, 'Haz clic en Producción en el menú lateral.'),
  ('CU-GO-050', 2, 'Revisa los pedidos ordenados por fecha de entrega y prioridad.'),
  ('CU-GO-050', 3, 'Haz clic en el pedido o tarjeta de producción que vas a trabajar.'),
  ('CU-GO-050', 4, 'Lee notas de relleno, decoración, empaque y horario.'),
  ('CU-GO-050', 5, 'Actualiza el estado cuando avances de etapa.'),
  ('CU-GO-051', 1, 'Haz clic en Producción en el menú lateral.'),
  ('CU-GO-051', 2, 'Ubica el pedido en preparación.'),
  ('CU-GO-051', 3, 'Haz clic en el control de estado o acción disponible.'),
  ('CU-GO-051', 4, 'Cambia a horneo, decoración, listo o el estado equivalente según el flujo visible.'),
  ('CU-GO-051', 5, 'Agrega observaciones si falta insumo, hubo retraso o se cambió la decoración.'),
  ('CU-GO-052', 1, 'Haz clic en Productos si necesitas la receta técnica del producto.'),
  ('CU-GO-052', 2, 'Busca la tarjeta del producto que vas a preparar.'),
  ('CU-GO-052', 3, 'Haz clic en Descargar receta PDF.'),
  ('CU-GO-052', 4, 'Revisa ingredientes, pasos y observaciones antes de iniciar el lote.'),
  ('CU-GO-052', 5, 'Vuelve a Producción y actualiza el avance del pedido.'),
  ('CU-GO-060', 1, 'Haz clic en Producción o Pedidos según dónde estés trabajando.'),
  ('CU-GO-060', 2, 'Abre el pedido que está en etapa de decoración.'),
  ('CU-GO-060', 3, 'Lee notas de color, dedicatoria, relleno, tamaño y empaque.'),
  ('CU-GO-060', 4, 'Compara la decoración física con lo solicitado por el cliente.'),
  ('CU-GO-060', 5, 'Si está correcto, avanza el estado a listo para empaque o salida.'),
  ('CU-GO-061', 1, 'Haz clic en Pedidos en el menú lateral.'),
  ('CU-GO-061', 2, 'Busca los pedidos en estado Listo o Listo para salida.'),
  ('CU-GO-061', 3, 'Confirma nombre del cliente, producto, cantidad y hora de entrega.'),
  ('CU-GO-061', 4, 'Verifica que el empaque sea adecuado para retiro o entrega.'),
  ('CU-GO-061', 5, 'Cambia el estado cuando el producto realmente sale del local.'),
  ('CU-GO-062', 1, 'Haz clic en Pedidos en el menú lateral.'),
  ('CU-GO-062', 2, 'Abre o edita el pedido afectado.'),
  ('CU-GO-062', 3, 'Escribe una observación clara: qué pasó, quién avisó y qué solución se dio.'),
  ('CU-GO-062', 4, 'Si hace falta, mantén el pedido sin cerrar hasta resolverlo.'),
  ('CU-GO-062', 5, 'Cuando se solucione, actualiza el estado y deja la trazabilidad mínima.'),
  ('CU-GO-070', 1, 'Haz clic en Abastecimiento en el menú lateral.'),
  ('CU-GO-070', 2, 'Entra al Dashboard de abastecimiento.'),
  ('CU-GO-070', 3, 'Revisa alertas de stock mínimo e insumos críticos.'),
  ('CU-GO-070', 4, 'Haz clic en Inventario para revisar existencias y movimientos.'),
  ('CU-GO-070', 5, 'Prioriza la reposición de lo que afecte pedidos cercanos.'),
  ('CU-GO-071', 1, 'Haz clic en Abastecimiento en el menú lateral.'),
  ('CU-GO-071', 2, 'Haz clic en Inventario o Movimientos.'),
  ('CU-GO-071', 3, 'Elige el insumo o ingrediente afectado.'),
  ('CU-GO-071', 4, 'Registra entrada, salida, merma o ajuste con cantidad y motivo.'),
  ('CU-GO-071', 5, 'Guarda el movimiento y revisa que el stock haya cambiado correctamente.'),
  ('CU-GO-072', 1, 'Haz clic en Abastecimiento en el menú lateral.'),
  ('CU-GO-072', 2, 'Haz clic en Compras.'),
  ('CU-GO-072', 3, 'Selecciona proveedor y agrega los insumos o productos necesarios.'),
  ('CU-GO-072', 4, 'Revisa cantidades, costo estimado y observaciones.'),
  ('CU-GO-072', 5, 'Haz clic en Guardar orden de compra y cambia el estado cuando sea recibida.'),
  ('CU-GO-080', 1, 'Haz clic en Productos en el menú lateral.'),
  ('CU-GO-080', 2, 'Busca la tarjeta del producto que vas a preparar.'),
  ('CU-GO-080', 3, 'Haz clic en Descargar receta PDF o en el botón Receta PDF.'),
  ('CU-GO-080', 4, 'Abre el archivo descargado y revisa ingredientes, pasos y observaciones.'),
  ('CU-GO-080', 5, 'Usa la receta como guía técnica antes de producir.'),
  ('CU-GO-081', 1, 'Haz clic en Productos en el menú lateral.'),
  ('CU-GO-081', 2, 'Haz clic en Editar sobre el producto correspondiente.'),
  ('CU-GO-081', 3, 'Baja hasta la sección Receta del producto.'),
  ('CU-GO-081', 4, 'Edita título, ingredientes, preparación y notas.'),
  ('CU-GO-081', 5, 'Haz clic en Guardar cambios y descarga el PDF para verificar el resultado.'),
  ('CU-GO-082', 1, 'Haz clic en Productos para abrir la ficha técnica del producto.'),
  ('CU-GO-082', 2, 'Descarga la receta PDF si necesitas imprimirla o compartirla en cocina.'),
  ('CU-GO-082', 3, 'Revisa rendimiento, cantidades y pasos antes de preparar el lote.'),
  ('CU-GO-082', 4, 'Ve a Producción para ubicar pedidos relacionados.'),
  ('CU-GO-082', 5, 'Actualiza estados cuando el lote avance de etapa.'),
  ('CU-GO-090', 1, 'Haz clic en Abastecimiento en el menú lateral.'),
  ('CU-GO-090', 2, 'Haz clic en Proveedores.'),
  ('CU-GO-090', 3, 'Haz clic en Nuevo proveedor o usa el formulario visible.'),
  ('CU-GO-090', 4, 'Escribe nombre, teléfono, correo y observaciones útiles.'),
  ('CU-GO-090', 5, 'Haz clic en Guardar proveedor y verifica que aparezca activo.'),
  ('CU-GO-091', 1, 'Haz clic en Abastecimiento en el menú lateral.'),
  ('CU-GO-091', 2, 'Entra a Proveedores o Items proveedor.'),
  ('CU-GO-091', 3, 'Selecciona el proveedor correspondiente.'),
  ('CU-GO-091', 4, 'Asocia el insumo, presentación, costo o referencia comercial.'),
  ('CU-GO-091', 5, 'Guarda la relación para que las compras futuras sean más rápidas.'),
  ('CU-GO-092', 1, 'Haz clic en Abastecimiento en el menú lateral.'),
  ('CU-GO-092', 2, 'Haz clic en Proveedores.'),
  ('CU-GO-092', 3, 'Busca el proveedor que ya no debe usarse.'),
  ('CU-GO-092', 4, 'Haz clic en Desactivar o cambia su estado a inactivo.'),
  ('CU-GO-092', 5, 'Escribe una observación si hubo incumplimiento, cambio de precio o entrega incompleta.'),
  ('CU-GO-100', 1, 'Haz clic en Reportes en el menú lateral.'),
  ('CU-GO-100', 2, 'Elige el tipo de reporte que necesitas generar.'),
  ('CU-GO-100', 3, 'Haz clic en Generar reporte.'),
  ('CU-GO-100', 4, 'Espera a que el estado cambie a Completado.'),
  ('CU-GO-100', 5, 'Haz clic en Descargar cuando el archivo esté listo.'),
  ('CU-GO-101', 1, 'Haz clic en Reportes en el menú lateral.'),
  ('CU-GO-101', 2, 'Revisa la tabla o tarjetas de reportes recientes.'),
  ('CU-GO-101', 3, 'Mira estado, fecha y tipo de reporte.'),
  ('CU-GO-101', 4, 'Haz clic en Descargar si el archivo todavía está disponible.'),
  ('CU-GO-101', 5, 'Limpia o archiva reportes antiguos si ya no aportan valor.'),
  ('CU-GO-102', 1, 'Haz clic en Reportes y genera un resumen de negocio o producción.'),
  ('CU-GO-102', 2, 'Revisa productos más pedidos, carga de producción y pedidos cercanos.'),
  ('CU-GO-102', 3, 'Haz clic en Abastecimiento para revisar mínimos de insumos.'),
  ('CU-GO-102', 4, 'Crea una orden de compra si el reporte muestra riesgo de faltante.'),
  ('CU-GO-102', 5, 'Usa el reporte como apoyo, no como reemplazo del criterio humano.'),
  ('CU-GO-110', 1, 'Haz clic en Notificaciones en la parte superior del panel.'),
  ('CU-GO-110', 2, 'Lee primero las notificaciones nuevas o críticas.'),
  ('CU-GO-110', 3, 'Identifica si pertenecen a pedidos, producción, abastecimiento o reportes.'),
  ('CU-GO-110', 4, 'Haz clic en el módulo relacionado para resolver la causa.'),
  ('CU-GO-110', 5, 'Marca la notificación como leída cuando la acción esté atendida.'),
  ('CU-GO-111', 1, 'Haz clic en Notificaciones.'),
  ('CU-GO-111', 2, 'Revisa qué avisos ya fueron resueltos.'),
  ('CU-GO-111', 3, 'Selecciona los avisos atendidos o usa la acción de archivar leídas.'),
  ('CU-GO-111', 4, 'Haz clic en Archivar.'),
  ('CU-GO-111', 5, 'No archives avisos críticos si todavía falta una acción real.'),
  ('CU-GO-112', 1, 'Haz clic en Notificaciones.'),
  ('CU-GO-112', 2, 'Lee el texto del aviso y detecta qué módulo menciona.'),
  ('CU-GO-112', 3, 'Haz clic en Pedidos, Producción, Abastecimiento o Reportes según corresponda.'),
  ('CU-GO-112', 4, 'Resuelve la acción pendiente en ese módulo.'),
  ('CU-GO-112', 5, 'Vuelve a Notificaciones y marca el aviso como atendido.'),
  ('CU-GO-120', 1, 'Haz clic en Usuarios o en la sección de administración de accesos.'),
  ('CU-GO-120', 2, 'Haz clic en Nuevo usuario.'),
  ('CU-GO-120', 3, 'Escribe usuario, nombre visible y contraseña inicial segura.'),
  ('CU-GO-120', 4, 'Asigna el rol correcto: administración, atención o producción.'),
  ('CU-GO-120', 5, 'Haz clic en Guardar y comunica la cuenta solo a la persona correspondiente.'),
  ('CU-GO-121', 1, 'Haz clic en Usuarios.'),
  ('CU-GO-121', 2, 'Busca la cuenta de la persona.'),
  ('CU-GO-121', 3, 'Haz clic en Editar.'),
  ('CU-GO-121', 4, 'Cambia el rol solo si la responsabilidad real cambió.'),
  ('CU-GO-121', 5, 'Guarda y pide al usuario cerrar sesión e ingresar nuevamente.'),
  ('CU-GO-122', 1, 'Haz clic en Usuarios.'),
  ('CU-GO-122', 2, 'Busca la cuenta que debe quedar sin acceso.'),
  ('CU-GO-122', 3, 'Haz clic en Desactivar o cambia el estado a inactivo.'),
  ('CU-GO-122', 4, 'Confirma la acción y registra una observación interna si aplica.'),
  ('CU-GO-122', 5, 'No borres cuentas si sirven para trazabilidad histórica.'),
  ('CU-GO-130', 1, 'Haz clic en Guía operativa en el menú lateral izquierdo.'),
  ('CU-GO-130', 2, 'Haz clic en el área de trabajo que corresponde a tu duda.'),
  ('CU-GO-130', 3, 'Haz clic en el procedimiento específico dentro de esa área.'),
  ('CU-GO-130', 4, 'Lee objetivo, responsable, punto de inicio y pasos.'),
  ('CU-GO-130', 5, 'Vuelve al módulo real y ejecuta la acción siguiendo el criterio del negocio.'),
  ('CU-GO-131', 1, 'Haz clic en Guía operativa.'),
  ('CU-GO-131', 2, 'Selecciona el área que la persona va a usar primero.'),
  ('CU-GO-131', 3, 'Lee en voz alta el procedimiento y muestra dónde se hace clic en el sistema.'),
  ('CU-GO-131', 4, 'Pide a la persona repetir el flujo en el módulo real.'),
  ('CU-GO-131', 5, 'Anota qué pasos causaron duda para mejorar la guía después.'),
  ('CU-GO-132', 1, 'Haz clic en Guía operativa.'),
  ('CU-GO-132', 2, 'Abre el procedimiento que parece desactualizado.'),
  ('CU-GO-132', 3, 'Compara los pasos con lo que realmente se hace en el local.'),
  ('CU-GO-132', 4, 'Anota qué botón, módulo o decisión ya cambió.'),
  ('CU-GO-132', 5, 'Avisa a administración o soporte para actualizar la documentación.')
)
INSERT INTO paso_caso_uso (caso_uso_id, numero, descripcion, created_at, updated_at, version)
SELECT cu.caso_uso_id, step_seed.numero, step_seed.descripcion, NOW(), NOW(), 0
FROM step_seed
JOIN caso_uso_operativo cu ON cu.codigo = step_seed.codigo
ON CONFLICT (caso_uso_id, numero) DO UPDATE SET
  descripcion = EXCLUDED.descripcion,
  updated_at = NOW();


COMMIT;

-- TANDA FINAL: CORRECCION UTF8 RECETAS PDF
SET client_encoding = 'UTF8';
-- Corrección UTF-8 y normalización de recetas PDF. Se usa guion ASCII en listas para máxima compatibilidad PDF.
UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Torta de Chocolate',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Harina\n- Cacao\n- Azúcar\n- Huevos\n- Leche\n- Mantequilla\n- Ganache de chocolate',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Mezcla ingredientes secos.\n2. Integra huevos, leche y mantequilla.\n3. Hornea en molde mediano.\n4. Deja enfriar antes de rellenar.\n5. Cubre con ganache y refrigera.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Mantener refrigerada si lleva relleno o cobertura sensible.'
)::text, updated_at = NOW() WHERE codigo = 'TORT-CHOC-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Torta Vainilla con Frutos',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Harina\n- Azúcar\n- Huevos\n- Mantequilla\n- Leche\n- Vainilla\n- Frutos rojos',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Bate mantequilla con azúcar.\n2. Agrega huevos y vainilla.\n3. Integra harina y leche.\n4. Hornea hasta que el centro esté firme.\n5. Decora con crema y frutos frescos.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Usar frutos frescos y refrigerar después de decorar.'
)::text, updated_at = NOW() WHERE codigo = 'TORT-VAIN-FRUT';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Torta Red Velvet',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Harina\n- Cacao\n- Azúcar\n- Huevos\n- Buttermilk\n- Colorante rojo\n- Frosting de queso crema',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Mezcla ingredientes secos.\n2. Integra líquidos, colorante y huevos.\n3. Hornea por capas.\n4. Enfría completamente.\n5. Rellena y cubre con frosting de queso crema.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Refrigerar por el frosting y sacar unos minutos antes de servir.'
)::text, updated_at = NOW() WHERE codigo = 'TORT-RED-VEL';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Torta Tres Leches',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Bizcocho base\n- Leche evaporada\n- Leche condensada\n- Crema de leche\n- Vainilla\n- Merengue o crema batida',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Hornea el bizcocho base.\n2. Mezcla las tres leches.\n3. Perfora el bizcocho y baña lentamente.\n4. Refrigera hasta absorber.\n5. Decora con merengue o crema.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Mantener siempre refrigerada.'
)::text, updated_at = NOW() WHERE codigo = 'TORT-3-LECH';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Brownie Individual',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Chocolate semiamargo\n- Mantequilla\n- Azúcar\n- Huevos\n- Harina\n- Cacao\n- Nueces opcionales',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Derrite chocolate con mantequilla.\n2. Bate huevos con azúcar.\n3. Integra chocolate, harina y cacao.\n4. Vierte en molde bajo.\n5. Hornea y corta porciones individuales.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- No sobrehornear para conservar textura húmeda.'
)::text, updated_at = NOW() WHERE codigo = 'BROWNIE-IND';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Alfajor Premium',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Maicena\n- Harina\n- Mantequilla\n- Azúcar impalpable\n- Yemas\n- Dulce de leche\n- Coco rallado',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Prepara una masa suave con mantequilla, secos y yemas.\n2. Refrigera la masa antes de estirar.\n3. Corta discos y hornea sin dorar demasiado.\n4. Rellena con dulce de leche.\n5. Pasa los bordes por coco rallado.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Manipular con cuidado porque la masa es frágil.'
)::text, updated_at = NOW() WHERE codigo = 'ALFAJOR-PREM';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Cheesecake de Frutos Rojos',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Galleta molida\n- Mantequilla\n- Queso crema\n- Azúcar\n- Huevos\n- Crema de leche\n- Salsa de frutos rojos',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Prepara base de galleta y compacta.\n2. Bate queso crema con azúcar.\n3. Agrega huevos y crema.\n4. Hornea a temperatura baja.\n5. Enfría y cubre con frutos rojos.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Reposar en refrigeración antes de cortar.'
)::text, updated_at = NOW() WHERE codigo = 'CHEESE-FRUT';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Galleta Decorada',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Harina\n- Mantequilla\n- Azúcar impalpable\n- Huevo\n- Vainilla\n- Glaseado real\n- Colorantes alimentarios',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Prepara masa de mantequilla.\n2. Refrigera, estira y corta figuras.\n3. Hornea hasta bordes ligeramente dorados.\n4. Deja enfriar por completo.\n5. Decora con glaseado real.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Esperar secado completo antes de empacar.'
)::text, updated_at = NOW() WHERE codigo = 'GALLETA-DEC';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Café Americano',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Café molido\n- Agua filtrada\n- Azúcar opcional\n- Vaso o taza de servicio',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Prepara espresso o café concentrado.\n2. Agrega agua caliente según intensidad deseada.\n3. Sirve en taza limpia.\n4. Ofrece azúcar aparte si aplica.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Servir inmediatamente.'
)::text, updated_at = NOW() WHERE codigo = 'CAFE-AMER';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Cupcake de Vainilla',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Harina preparada\n- Azúcar\n- Huevos\n- Mantequilla\n- Leche\n- Vainilla\n- Frosting de vainilla',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Bate mantequilla y azúcar.\n2. Agrega huevos y vainilla.\n3. Integra harina y leche.\n4. Llena capacillos a dos tercios.\n5. Hornea, enfría y decora con frosting.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- No decorar mientras estén calientes.'
)::text, updated_at = NOW() WHERE codigo = 'CUPK-VAIN-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Cupcake de Chocolate',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Harina\n- Cacao\n- Azúcar\n- Huevos\n- Leche\n- Aceite o mantequilla\n- Ganache o buttercream',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Mezcla secos.\n2. Integra líquidos hasta obtener masa uniforme.\n3. Llena capacillos.\n4. Hornea y deja enfriar.\n5. Decora con crema o ganache.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Mantener cubiertos para evitar resequedad.'
)::text, updated_at = NOW() WHERE codigo = 'CUPK-CHOC-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Ficha técnica - Mesa Dulce 25 personas',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Mini postres\n- Cupcakes\n- Galletas decoradas\n- Alfajores\n- Base de mesa\n- Etiquetas\n- Bandejas',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Define cantidad por persona.\n2. Arma variedad según disponibilidad.\n3. Agrupa por tipo de postre.\n4. Monta en bandejas limpias.\n5. Verifica transporte y presentación final.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Confirmar colores y temática antes de producir.'
)::text, updated_at = NOW() WHERE codigo = 'MESA-DULCE-25';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Ficha técnica - Mesa Dulce 50 personas',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Mini postres\n- Cupcakes\n- Cake pops\n- Macarons o galletas\n- Bases decorativas\n- Bandejas\n- Etiquetas',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Calcula volumen total.\n2. Produce piezas por tandas.\n3. Clasifica postres fríos y secos.\n4. Monta bandejas y soportes.\n5. Revisa cantidades antes de entrega.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Preparar checklist de montaje y transporte.'
)::text, updated_at = NOW() WHERE codigo = 'MESA-DULCE-50';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Ficha técnica - Caja Mini Postres',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Mini brownies\n- Mini cheesecakes\n- Vasitos dulces\n- Alfajores\n- Base de caja\n- Separadores',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Arma variedad según disponibilidad.\n2. Controla tamaño uniforme de cada pieza.\n3. Coloca separadores en caja.\n4. Ubica productos frágiles al final.\n5. Etiqueta y refrigera si corresponde.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Ideal para regalos o degustaciones.'
)::text, updated_at = NOW() WHERE codigo = 'CAJA-MINI';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Torta de Zanahoria y Nuez',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Zanahoria rallada\n- Harina\n- Azúcar morena\n- Huevos\n- Aceite vegetal\n- Canela\n- Nueces\n- Frosting de queso crema',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Mezcla secos con canela.\n2. Integra huevos, aceite y zanahoria.\n3. Agrega nueces al final.\n4. Hornea hasta que el centro esté firme.\n5. Enfría y cubre con frosting de queso crema.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Mantener refrigerada por la cobertura.'
)::text, updated_at = NOW() WHERE codigo = 'TORT-ZANA-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Ficha técnica - Naked Cake de Boda',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Bizcocho de vainilla\n- Crema de relleno\n- Frutos rojos\n- Flores comestibles o decorativas\n- Base rígida\n- Soportes internos',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Hornea capas uniformes.\n2. Nivela cada capa antes de montar.\n3. Rellena con crema y fruta controlada.\n4. Aplica acabado naked cake.\n5. Refrigera y transporta con soporte.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Confirmar decoración y altura antes de producir.'
)::text, updated_at = NOW() WHERE codigo = 'TORT-BODA-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Tiramisú Familiar',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Bizcotelas\n- Café concentrado\n- Queso mascarpone o crema\n- Azúcar\n- Cacao en polvo\n- Huevos pasteurizados o crema batida',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Prepara café y deja enfriar.\n2. Bate crema con queso y azúcar.\n3. Remoja bizcotelas sin saturarlas.\n4. Arma capas alternando crema y bizcotela.\n5. Refrigera y espolvorea cacao antes de servir.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Mantener siempre refrigerado.'
)::text, updated_at = NOW() WHERE codigo = 'TIRA-FAM-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Pie de Limón Artesanal',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Masa sablé\n- Jugo de limón\n- Leche condensada\n- Yemas\n- Azúcar\n- Claras\n- Ralladura de limón',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Hornea la base.\n2. Prepara crema de limón.\n3. Rellena y lleva a frío.\n4. Bate merengue.\n5. Decora y dora suavemente.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Conservar refrigerado y evitar calor directo.'
)::text, updated_at = NOW() WHERE codigo = 'PIE-LIMON-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Galletas Mix de Mantequilla',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Mantequilla\n- Harina\n- Azúcar impalpable\n- Huevo\n- Vainilla\n- Chocolate o mermelada opcional',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Prepara masa base de mantequilla.\n2. Divide para sabores si aplica.\n3. Forma piezas uniformes.\n4. Hornea por tandas controlando color.\n5. Enfría antes de empacar.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Guardar en envase hermético.'
)::text, updated_at = NOW() WHERE codigo = 'GALL-MIX-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Ficha técnica - Galleta Corporativa con Logo',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Masa de galleta\n- Glaseado real\n- Colorantes\n- Plantilla o impresión comestible\n- Empaque individual',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Prepara y corta galletas uniformes.\n2. Hornea y deja enfriar.\n3. Aplica base de glaseado.\n4. Coloca logo o decoración.\n5. Empaca individualmente cuando seque.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Validar logo y colores con el cliente.'
)::text, updated_at = NOW() WHERE codigo = 'GALL-CORP-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Frappé Mocha',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Café frío\n- Leche\n- Hielo\n- Chocolate\n- Azúcar o jarabe\n- Crema batida\n- Sirope de chocolate',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Licúa café, leche, hielo y chocolate.\n2. Ajusta dulzor.\n3. Sirve en vaso frío.\n4. Agrega crema batida.\n5. Decora con sirope.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Servir inmediatamente para conservar textura.'
)::text, updated_at = NOW() WHERE codigo = 'FRAP-MOCHA';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Chocolate Caliente de la Casa',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Leche\n- Chocolate semiamargo\n- Cacao\n- Azúcar\n- Canela opcional\n- Marshmallows opcionales',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Calienta la leche sin hervir fuerte.\n2. Integra chocolate y cacao.\n3. Endulza al gusto.\n4. Sirve caliente en taza.\n5. Decora con marshmallows si aplica.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Mantener temperatura segura de servicio.'
)::text, updated_at = NOW() WHERE codigo = 'CHOC-CAL-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Cupcake Red Velvet',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Harina\n- Cacao\n- Azúcar\n- Huevos\n- Buttermilk\n- Colorante rojo\n- Frosting de queso crema',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Mezcla secos.\n2. Integra líquidos y colorante.\n3. Llena capacillos a dos tercios.\n4. Hornea y enfría.\n5. Decora con frosting y migas rojas.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Refrigerar si el frosting contiene queso crema.'
)::text, updated_at = NOW() WHERE codigo = 'CUPK-RED-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Receta técnica - Cupcake Oreo',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Harina\n- Cacao\n- Azúcar\n- Huevos\n- Leche\n- Galletas trituradas\n- Crema cookies and cream',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Prepara masa de chocolate.\n2. Agrega trozos de galleta.\n3. Hornea en capacillos.\n4. Enfría por completo.\n5. Decora con crema y galleta.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Mantener en caja cerrada para conservar textura.'
)::text, updated_at = NOW() WHERE codigo = 'CUPK-OREO-01';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Ficha técnica - Caja Brigadeiro 12',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Leche condensada\n- Cacao\n- Mantequilla\n- Chocolate granulado\n- Capacillos\n- Caja rígida',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Cocina leche condensada con cacao y mantequilla.\n2. Enfría la mezcla.\n3. Forma bolitas uniformes.\n4. Pasa por granulado.\n5. Coloca en capacillos y caja.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Mantener en lugar fresco y evitar sol directo.'
)::text, updated_at = NOW() WHERE codigo = 'BRIGA-BOX-12';

UPDATE producto SET receta_json = jsonb_build_object(
  'titulo', 'Ficha técnica - Mesa Dulce 80 personas',
  'tituloIngredientes', 'Ingredientes',
  'ingredientes', E'- Mini postres variados\n- Cupcakes\n- Cake pops\n- Galletas\n- Macarons o alfajores\n- Bases decorativas\n- Etiquetas\n- Bandejas grandes',
  'tituloPasos', 'Preparación',
  'pasos', E'1. Define distribución por tipo de postre.\n2. Produce por tandas y controla inventario.\n3. Agrupa fríos y secos por separado.\n4. Prepara bandejas y soportes.\n5. Revisa montaje, transporte y reposición.',
  'tituloObservaciones', 'Notas de producción',
  'observaciones', E'- Requiere checklist de montaje y apoyo en entrega.'
)::text, updated_at = NOW() WHERE codigo = 'MESA-DULCE-80';




-- =============================================================================
-- T15 - Backfill transicional de terceros unificados desde clientes/proveedores V1
-- =============================================================================
DO $$
DECLARE
  rec RECORD;
  v_tercero_id BIGINT;
BEGIN
  FOR rec IN SELECT * FROM cliente WHERE tercero_id IS NULL ORDER BY cliente_id LOOP
    INSERT INTO tercero (nombre_legal, nombre_comercial, telefono, correo, observaciones, activo, created_at, updated_at)
    VALUES (rec.nombre_completo, rec.nombre_completo, rec.telefono, lower(rec.correo), rec.observaciones, TRUE, rec.created_at, rec.updated_at)
    RETURNING tercero_id INTO v_tercero_id;

    UPDATE cliente SET tercero_id = v_tercero_id WHERE cliente_id = rec.cliente_id;

    INSERT INTO cliente_perfil (tercero_id, cliente_id, estado, fecha_alta)
    VALUES (v_tercero_id, rec.cliente_id, 'ACTIVO', coalesce(rec.fecha_registro, rec.created_at))
    ON CONFLICT (cliente_id) DO NOTHING;
  END LOOP;

  FOR rec IN SELECT * FROM cliente WHERE tercero_id IS NOT NULL ORDER BY cliente_id LOOP
    INSERT INTO cliente_perfil (tercero_id, cliente_id, estado, fecha_alta)
    VALUES (rec.tercero_id, rec.cliente_id, 'ACTIVO', coalesce(rec.fecha_registro, rec.created_at))
    ON CONFLICT (cliente_id) DO NOTHING;
  END LOOP;

  FOR rec IN SELECT * FROM proveedor WHERE tercero_id IS NULL ORDER BY proveedor_id LOOP
    INSERT INTO tercero (nombre_legal, nombre_comercial, telefono, correo, direccion_principal, observaciones, activo, created_at, updated_at)
    VALUES (rec.nombre, rec.nombre, rec.telefono, lower(rec.correo), rec.direccion, rec.observaciones, rec.activo, rec.created_at, rec.updated_at)
    RETURNING tercero_id INTO v_tercero_id;

    UPDATE proveedor SET tercero_id = v_tercero_id WHERE proveedor_id = rec.proveedor_id;

    INSERT INTO proveedor_perfil (tercero_id, proveedor_id, codigo_proveedor, estado, fecha_alta)
    VALUES (v_tercero_id, rec.proveedor_id, rec.codigo, CASE WHEN rec.activo THEN 'ACTIVO' ELSE 'INACTIVO' END, rec.created_at)
    ON CONFLICT (proveedor_id) DO NOTHING;
  END LOOP;

  FOR rec IN SELECT * FROM proveedor WHERE tercero_id IS NOT NULL ORDER BY proveedor_id LOOP
    INSERT INTO proveedor_perfil (tercero_id, proveedor_id, codigo_proveedor, estado, fecha_alta)
    VALUES (rec.tercero_id, rec.proveedor_id, rec.codigo, CASE WHEN rec.activo THEN 'ACTIVO' ELSE 'INACTIVO' END, rec.created_at)
    ON CONFLICT (proveedor_id) DO NOTHING;
  END LOOP;
END $$;
