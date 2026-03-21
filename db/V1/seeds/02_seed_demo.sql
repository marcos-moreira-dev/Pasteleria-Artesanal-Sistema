-- =============================================================================
-- Pasteleria — Semilla demo ligera: clientes, cotizaciones, pedidos y produccion
-- Seeds: 02_seed_demo.sql
-- Ejecutar luego de 01_seed_base.sql
-- =============================================================================

INSERT INTO cliente (nombre_completo, telefono, correo, observaciones)
VALUES
  ('Maria Lopez',   '0990000001', 'maria.lopez@example.com',   'Cliente frecuente de tortas de cumpleanos'),
  ('Juan Perez',     '0990000002', 'juan.perez@example.com',    'Prefiere retiro en local'),
  ('Andrea Gomez',   '0990000003', 'andrea.gomez@example.com',  'Solicita decoraciones personalizadas'),
  ('Carlos Vera',   '0990000004', 'carlos.vera@example.com',   'Cliente corporativo ocasional');

INSERT INTO cotizacion (cliente_id, codigo, estado_cotizacion, origen, observaciones, total_estimado)
VALUES
  ((SELECT cliente_id FROM cliente WHERE correo = 'maria.lopez@example.com'),
   'COT-0001', 'PENDIENTE',   'PUBLICO', 'Cotizacion de torta tematica infantil.', 45.00),
  ((SELECT cliente_id FROM cliente WHERE correo = 'juan.perez@example.com'),
   'COT-0002', 'APROBADA',   'INTERNO', 'Cotizacion de postres para mesa dulce.', 32.50),
  ((SELECT cliente_id FROM cliente WHERE correo = 'carlos.vera@example.com'),
   'COT-0003', 'RECHAZADA',  'INTERNO', 'Propuesta para coffe break pequeno.', 58.00),
  ((SELECT cliente_id FROM cliente WHERE correo = 'andrea.gomez@example.com'),
   'COT-0004', 'CONVERTIDA', 'PUBLICO', 'Cotizacion convertida a pedido de evento.', 64.80);

INSERT INTO cotizacion_detalle (cotizacion_id, producto_id, descripcion_item, cantidad, precio_estimado, subtotal, notas)
VALUES
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0001'), NULL,
   'Torta tematica de dos pisos con decoracion personalizada', 1, 45.00, 45.00,
   'Detalle abierto para cotizacion artesanal'),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0002'),
   (SELECT producto_id FROM producto WHERE codigo = 'PROD-BROWNIE-IND'),
   'Brownie individual', 10, 3.25, 32.50, NULL),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0003'), NULL,
   'Bandeja variada para coffe break', 1, 58.00, 58.00,
   'Cliente no continuo con el proceso'),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0004'),
   (SELECT producto_id FROM producto WHERE codigo = 'PROD-TORTA-CHOCO-M'),
   'Torta de chocolate mediana', 1, 28.50, 28.50, NULL),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0004'),
   (SELECT producto_id FROM producto WHERE codigo = 'PROD-GALLETA-DEC'),
   'Galleta decorada', 12, 2.10, 25.20, 'Recuerdo para invitados'),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0004'),
   (SELECT producto_id FROM producto WHERE codigo = 'PROD-CAFE-AMER'),
   'Cafe americano', 6, 1.85, 11.10, 'Ajuste promocional');

INSERT INTO pedido (
  cliente_id, cotizacion_id, codigo, fecha_entrega_estimada, estado_pedido,
  prioridad, origen, observaciones, total_estimado
)
VALUES
  ((SELECT cliente_id FROM cliente WHERE correo = 'maria.lopez@example.com'),
   NULL, 'PED-0001', NOW() + INTERVAL '2 day', 'REGISTRADO', 'NORMAL', 'INTERNO',
   'Pedido directo de mostrador.', 28.50),
  ((SELECT cliente_id FROM cliente WHERE correo = 'andrea.gomez@example.com'),
   (SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0004'),
   'PED-0002', NOW() + INTERVAL '3 day', 'EN_PREPARACION', 'URGENTE', 'PUBLICO',
   'Pedido convertido desde cotizacion.', 64.80),
  ((SELECT cliente_id FROM cliente WHERE correo = 'juan.perez@example.com'),
   NULL, 'PED-0003', NOW() + INTERVAL '1 day', 'LISTO', 'NORMAL', 'INTERNO',
   'Pedido para retiro en tienda.', 13.00),
  ((SELECT cliente_id FROM cliente WHERE correo = 'carlos.vera@example.com'),
   NULL, 'PED-0004', NOW() + INTERVAL '1 day', 'ENTREGADO', 'NORMAL', 'INTERNO',
   'Pedido entregado a cliente corporativo.', 57.00),
  ((SELECT cliente_id FROM cliente WHERE correo = 'maria.lopez@example.com'),
   NULL, 'PED-0005', NOW() + INTERVAL '4 day', 'CANCELADO', 'NORMAL', 'PUBLICO',
   'Cliente cancelo por cambio de fecha.', 24.00);

INSERT INTO pedido_detalle (pedido_id, producto_id, descripcion_item, cantidad, precio_unitario, subtotal, notas)
VALUES
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0001'),
   (SELECT producto_id FROM producto WHERE codigo = 'PROD-TORTA-CHOCO-M'),
   'Torta de chocolate mediana', 1, 28.50, 28.50, NULL),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0002'),
   (SELECT producto_id FROM producto WHERE codigo = 'PROD-TORTA-CHOCO-M'),
   'Torta de chocolate mediana', 1, 28.50, 28.50, NULL),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0002'),
   (SELECT producto_id FROM producto WHERE codigo = 'PROD-GALLETA-DEC'),
   'Galleta decorada', 12, 2.10, 25.20, NULL),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0002'),
   (SELECT producto_id FROM producto WHERE codigo = 'PROD-CAFE-AMER'),
   'Cafe americano', 6, 1.85, 11.10, 'Precio promocional'),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0003'),
   (SELECT producto_id FROM producto WHERE codigo = 'PROD-BROWNIE-IND'),
   'Brownie individual', 4, 3.25, 13.00, NULL),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0004'),
   (SELECT producto_id FROM producto WHERE codigo = 'PROD-TORTA-3L-P'),
   'Torta tres leches pequena', 1, 24.00, 24.00, NULL),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0004'),
   (SELECT producto_id FROM producto WHERE codigo = 'PROD-BROWNIE-IND'),
   'Brownie individual', 6, 3.25, 19.50, NULL),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0004'),
   (SELECT producto_id FROM producto WHERE codigo = 'PROD-CAFE-AMER'),
   'Cafe americano', 7, 1.93, 13.50, NULL),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0005'),
   (SELECT producto_id FROM producto WHERE codigo = 'PROD-TORTA-3L-P'),
   'Torta tres leches pequena', 1, 24.00, 24.00, 'Pedido cancelado');

INSERT INTO produccion (pedido_id, estado_produccion, prioridad_produccion, fecha_inicio, fecha_finalizacion, observaciones_produccion)
VALUES
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0001'), 'PENDIENTE',    'NORMAL', NULL,              NULL,               'Pendiente de asignacion a produccion'),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0002'), 'EN_PROCESO',  'URGENTE', NOW() - INTERVAL '4 hour', NULL,         'Decoracion personalizada en curso'),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0003'), 'FINALIZADO',  'NORMAL', NOW() - INTERVAL '1 day', NOW() - INTERVAL '2 hour', 'Pedido listo para retiro');
