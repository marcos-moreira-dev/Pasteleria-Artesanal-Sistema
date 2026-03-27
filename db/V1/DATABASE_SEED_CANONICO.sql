-- =============================================================================
-- PASTELERIA - SEED DEMO CANONICO
-- Version: 2026-03-26
-- Criterio:
-- - usa los codigos de producto actuales
-- - usa estados vigentes del backend
-- - cubre datos demo para comercial, produccion, abastecimiento y reportes
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
-- EXTENSION MASIVA DE DATOS DEMO
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

COMMIT;
