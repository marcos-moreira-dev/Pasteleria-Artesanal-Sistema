INSERT INTO categoria_producto (codigo, nombre, descripcion, orden_visual)
VALUES
  ('CUPCAKES', 'CUPCAKES', 'Cupcakes para mesas dulces y celebraciones.', 5),
  ('MESAS', 'MESAS DULCES', 'Paquetes base para eventos y celebraciones medianas.', 6);

INSERT INTO producto (categoria_id, codigo, slug, nombre, descripcion, precio_base, requiere_cotizacion, activo, publicado)
VALUES
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'TORTAS'), 'PROD-TORTA-RV-G', 'torta-red-velvet-grande', 'Torta red velvet grande', 'Torta para celebraciones con acabado elegante y porciones generosas.', 42.00, FALSE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'TORTAS'), 'PROD-TORTA-VAIN-FR', 'torta-vainilla-frutos', 'Torta de vainilla y frutos', 'Bizcocho suave con crema ligera y frutos frescos.', 36.00, FALSE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'POSTRES'), 'PROD-CHEESECAKE-FR', 'cheesecake-frutos-rojos', 'Cheesecake de frutos rojos', 'Postre entero para compartir en reuniones y celebraciones.', 34.00, FALSE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'POSTRES'), 'PROD-MINI-POSTRES-BOX', 'caja-mini-postres', 'Caja de mini postres', 'Seleccion de mini postres para eventos y regalos.', 32.00, FALSE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'CUPCAKES'), 'PROD-CUPCAKE-VAIN', 'cupcake-vainilla', 'Cupcake de vainilla', 'Cupcake artesanal con topping clasico.', 2.50, FALSE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'CUPCAKES'), 'PROD-CUPCAKE-CHOCO', 'cupcake-chocolate', 'Cupcake de chocolate', 'Cupcake de cacao intenso para mesas dulces.', 2.50, FALSE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'MESAS'), 'PROD-MESA-DULCE-25', 'mesa-dulce-25-personas', 'Mesa dulce 25 personas', 'Paquete base de mesa dulce para reuniones medianas.', 95.00, TRUE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'MESAS'), 'PROD-MESA-DULCE-50', 'mesa-dulce-50-personas', 'Mesa dulce 50 personas', 'Paquete base para eventos grandes con coordinacion previa.', 180.00, TRUE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'GALLETAS'), 'PROD-ALFAJOR-PREM', 'alfajor-premium', 'Alfajor premium', 'Alfajor relleno y decorado para regalos o mesas dulces.', 1.80, FALSE, TRUE, TRUE);

INSERT INTO cliente (nombre_completo, telefono, correo, observaciones)
VALUES
  ('Sofia Mena', '0990000010', 'sofia.mena@example.com', 'Pide tortas de cumpleanos y cupcakes para reuniones familiares.'),
  ('Valentina Ruiz', '0990000011', 'valentina.ruiz@example.com', 'Consulta por regalos dulces y entregas en oficina.'),
  ('Eventos Atlas', '0990000012', 'eventos.atlas@example.com', 'Cliente corporativo para coffee break y mesas dulces.'),
  ('Colegio Nuevo Horizonte', '0990000013', 'colegio.horizonte@example.com', 'Solicita cotizaciones para actos internos y celebraciones escolares.'),
  ('Camila Torres', '0990000014', 'camila.torres@example.com', 'Cliente frecuente de cheesecake y cupcakes.'),
  ('Lucia Andrade', '0990000015', 'lucia.andrade@example.com', 'Prefiere coordinacion por WhatsApp y retiro en local.'),
  ('Diego Santacruz', '0990000016', 'diego.santacruz@example.com', 'Hace pedidos para reuniones pequenas en oficina.'),
  ('Fernanda Solis', '0990000017', 'fernanda.solis@example.com', 'Interesada en mesas dulces y decoracion con paleta neutra.');

INSERT INTO cotizacion (cliente_id, codigo, estado_cotizacion, origen, observaciones, total_estimado)
VALUES
  ((SELECT cliente_id FROM cliente WHERE correo = 'sofia.mena@example.com'), 'COT-0101', 'PENDIENTE', 'PUBLICO', 'Cotizacion para cumpleanos con torta red velvet y cupcakes.', 72.00),
  ((SELECT cliente_id FROM cliente WHERE correo = 'eventos.atlas@example.com'), 'COT-0102', 'CONVERTIDA', 'INTERNO', 'Propuesta corporativa convertida a pedido para coffee break.', 145.00),
  ((SELECT cliente_id FROM cliente WHERE correo = 'lucia.andrade@example.com'), 'COT-0103', 'PENDIENTE', 'PUBLICO', 'Solicitud de mesa dulce pequena con cheesecake y mini postres.', 98.00),
  ((SELECT cliente_id FROM cliente WHERE correo = 'colegio.horizonte@example.com'), 'COT-0104', 'RECHAZADA', 'INTERNO', 'Evento institucional fuera del presupuesto disponible.', 180.00),
  ((SELECT cliente_id FROM cliente WHERE correo = 'camila.torres@example.com'), 'COT-0105', 'CONVERTIDA', 'PUBLICO', 'Cotizacion para reunion familiar convertida a pedido.', 64.00),
  ((SELECT cliente_id FROM cliente WHERE correo = 'fernanda.solis@example.com'), 'COT-0106', 'APROBADA', 'PUBLICO', 'Cotizacion aprobada pendiente de fecha definitiva.', 110.00);

INSERT INTO cotizacion_detalle (cotizacion_id, producto_id, descripcion_item, cantidad, precio_estimado, subtotal, notas)
VALUES
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0101'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-TORTA-RV-G'), 'Torta red velvet grande', 1, 42.00, 42.00, 'Acabado con tonos crema y topper sencillo.'),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0101'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-CUPCAKE-VAIN'), 'Cupcake de vainilla', 12, 2.50, 30.00, 'Cobertura en gama pastel.'),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0102'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-MESA-DULCE-25'), 'Mesa dulce 25 personas', 1, 95.00, 95.00, 'Incluye montaje basico en oficina.'),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0102'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-CAFE-AMER'), 'Cafe americano', 20, 1.90, 38.00, 'Servicio para jornada de tarde.'),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0102'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-BROWNIE-IND'), 'Brownie individual', 4, 3.00, 12.00, 'Ajuste comercial por volumen.'),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0103'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-MINI-POSTRES-BOX'), 'Caja de mini postres', 2, 32.00, 64.00, 'Variedad para mesa principal.'),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0103'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-CHEESECAKE-FR'), 'Cheesecake de frutos rojos', 1, 34.00, 34.00, NULL),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0104'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-MESA-DULCE-50'), 'Mesa dulce 50 personas', 1, 180.00, 180.00, 'Propuesta descartada por presupuesto.'),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0105'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-CHEESECAKE-FR'), 'Cheesecake de frutos rojos', 1, 34.00, 34.00, NULL),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0105'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-CUPCAKE-CHOCO'), 'Cupcake de chocolate', 12, 2.50, 30.00, 'Presentacion para reunion familiar.'),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0106'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-TORTA-VAIN-FR'), 'Torta de vainilla y frutos', 1, 36.00, 36.00, NULL),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0106'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-MINI-POSTRES-BOX'), 'Caja de mini postres', 1, 32.00, 32.00, NULL),
  ((SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0106'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-CUPCAKE-VAIN'), 'Cupcake de vainilla', 12, 3.50, 42.00, 'Version decorada para mesa principal.');

INSERT INTO pedido (
  cliente_id,
  cotizacion_id,
  codigo,
  fecha_entrega_estimada,
  estado_pedido,
  prioridad,
  origen,
  observaciones,
  total_estimado
)
VALUES
  ((SELECT cliente_id FROM cliente WHERE correo = 'sofia.mena@example.com'), NULL, 'PED-0101', NOW() + INTERVAL '3 day', 'REGISTRADO', 'NORMAL', 'INTERNO', 'Pedido directo para celebracion familiar.', 51.00),
  ((SELECT cliente_id FROM cliente WHERE correo = 'eventos.atlas@example.com'), (SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0102'), 'PED-0102', NOW() + INTERVAL '1 day', 'EN_PREPARACION', 'URGENTE', 'INTERNO', 'Pedido corporativo con entrega temprana.', 145.00),
  ((SELECT cliente_id FROM cliente WHERE correo = 'camila.torres@example.com'), (SELECT cotizacion_id FROM cotizacion WHERE codigo = 'COT-0105'), 'PED-0103', NOW() + INTERVAL '2 day', 'LISTO', 'NORMAL', 'PUBLICO', 'Pedido convertido desde la landing publica.', 64.00),
  ((SELECT cliente_id FROM cliente WHERE correo = 'valentina.ruiz@example.com'), NULL, 'PED-0104', NOW() + INTERVAL '2 day', 'REGISTRADO', 'NORMAL', 'PUBLICO', 'Pedido para regalo corporativo pequeno.', 60.00),
  ((SELECT cliente_id FROM cliente WHERE correo = 'diego.santacruz@example.com'), NULL, 'PED-0105', NOW() + INTERVAL '1 day', 'ENTREGADO', 'NORMAL', 'INTERNO', 'Pedido de brownies y cafe para oficina.', 58.00),
  ((SELECT cliente_id FROM cliente WHERE correo = 'lucia.andrade@example.com'), NULL, 'PED-0106', NOW() + INTERVAL '4 day', 'CANCELADO', 'NORMAL', 'PUBLICO', 'Cliente cambio la fecha del evento.', 55.00),
  ((SELECT cliente_id FROM cliente WHERE correo = 'fernanda.solis@example.com'), NULL, 'PED-0107', NOW() + INTERVAL '5 day', 'REGISTRADO', 'URGENTE', 'INTERNO', 'Mesa dulce reservada para evento de fin de semana.', 95.00);

INSERT INTO pedido_detalle (pedido_id, producto_id, descripcion_item, cantidad, precio_unitario, subtotal, notas)
VALUES
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0101'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-TORTA-VAIN-FR'), 'Torta de vainilla y frutos', 1, 36.00, 36.00, NULL),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0101'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-CUPCAKE-VAIN'), 'Cupcake de vainilla', 6, 2.50, 15.00, 'Caja mixta para acompanar la torta.'),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0102'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-MESA-DULCE-25'), 'Mesa dulce 25 personas', 1, 95.00, 95.00, NULL),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0102'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-CAFE-AMER'), 'Cafe americano', 20, 1.90, 38.00, 'Servicio corporativo.'),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0102'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-BROWNIE-IND'), 'Brownie individual', 4, 3.00, 12.00, NULL),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0103'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-CHEESECAKE-FR'), 'Cheesecake de frutos rojos', 1, 34.00, 34.00, NULL),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0103'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-CUPCAKE-CHOCO'), 'Cupcake de chocolate', 12, 2.50, 30.00, NULL),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0104'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-TORTA-RV-G'), 'Torta red velvet grande', 1, 42.00, 42.00, NULL),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0104'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-ALFAJOR-PREM'), 'Alfajor premium', 10, 1.80, 18.00, 'Empaque para regalo.'),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0105'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-BROWNIE-IND'), 'Brownie individual', 12, 3.25, 39.00, NULL),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0105'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-CAFE-AMER'), 'Cafe americano', 10, 1.90, 19.00, NULL),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0106'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-TORTA-CHOCO-M'), 'Torta de chocolate mediana', 1, 28.50, 28.50, NULL),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0106'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-CUPCAKE-VAIN'), 'Cupcake de vainilla', 8, 3.31, 26.48, 'Pedido cancelado antes de produccion.'),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0107'), (SELECT producto_id FROM producto WHERE codigo = 'PROD-MESA-DULCE-25'), 'Mesa dulce 25 personas', 1, 95.00, 95.00, 'Se reserva montaje para evento interno.');

INSERT INTO produccion (pedido_id, estado_produccion, prioridad_produccion, fecha_inicio, fecha_finalizacion, observaciones_produccion)
VALUES
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0101'), 'PENDIENTE', 'NORMAL', NULL, NULL, 'En espera de asignacion a cocina.'),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0102'), 'EN_PROCESO', 'URGENTE', NOW() - INTERVAL '2 hour', NULL, 'Mesa dulce en armado y bebidas en despacho.'),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0103'), 'FINALIZADO', 'NORMAL', NOW() - INTERVAL '1 day', NOW() - INTERVAL '3 hour', 'Pedido listo para retiro.'),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0104'), 'PENDIENTE', 'NORMAL', NULL, NULL, 'Pendiente de confirmacion de detalles finales.'),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0105'), 'FINALIZADO', 'NORMAL', NOW() - INTERVAL '1 day', NOW() - INTERVAL '6 hour', 'Pedido entregado en oficina.'),
  ((SELECT pedido_id FROM pedido WHERE codigo = 'PED-0107'), 'PENDIENTE', 'URGENTE', NULL, NULL, 'Reservado para produccion anticipada.');

INSERT INTO archivo_recurso (
  codigo_archivo,
  origen_modulo,
  tipo_archivo,
  nombre_original,
  nombre_fisico,
  mime_type,
  extension,
  tamano_bytes,
  checksum,
  ruta_relativa,
  estado,
  fecha_expiracion,
  creado_por_usuario_id
)
VALUES
  ('ARC-REP-0101', 'REPORTES', 'CSV', 'pedidos-dia.csv', 'pedidos-dia-20260316.csv', 'text/csv', '.csv', 48210, 'chk-pedidos-0101', 'reportes/2026/03/pedidos-dia-20260316.csv', 'DISPONIBLE', NOW() + INTERVAL '7 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
  ('ARC-REP-0102', 'REPORTES', 'XLSX', 'resumen-semanal.xlsx', 'resumen-semanal-20260316.xlsx', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', '.xlsx', 125430, 'chk-reportes-0102', 'reportes/2026/03/resumen-semanal-20260316.xlsx', 'DISPONIBLE', NOW() + INTERVAL '7 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin')),
  ('ARC-COT-0105', 'COTIZACIONES', 'PDF', 'cotizacion-camila.pdf', 'cotizacion-camila-0105.pdf', 'application/pdf', '.pdf', 84211, 'chk-cotizacion-0105', 'cotizaciones/2026/03/cotizacion-camila-0105.pdf', 'DISPONIBLE', NOW() + INTERVAL '30 day', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'atencion1'));

INSERT INTO job_reporte (
  codigo_job,
  tipo_reporte,
  parametros_json,
  estado,
  solicitado_por_usuario_id,
  fecha_solicitud,
  fecha_inicio,
  fecha_fin,
  intentos,
  mensaje_error,
  archivo_id,
  request_id,
  version
)
VALUES
  ('JOB-REP-0101', 'RESUMEN_NEGOCIO', '{"fecha":"2026-03-16","estado":"TODOS"}'::jsonb, 'COMPLETADO', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), NOW() - INTERVAL '3 hour', NOW() - INTERVAL '2 hour 58 minute', NOW() - INTERVAL '2 hour 57 minute', 1, NULL, (SELECT archivo_id FROM archivo_recurso WHERE codigo_archivo = 'ARC-REP-0101'), 'req-reporte-0101', 1),
  ('JOB-REP-0102', 'RESUMEN_NEGOCIO', '{"semana":"2026-W11"}'::jsonb, 'COMPLETADO', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), NOW() - INTERVAL '1 day', NOW() - INTERVAL '23 hour 55 minute', NOW() - INTERVAL '23 hour 52 minute', 1, NULL, (SELECT archivo_id FROM archivo_recurso WHERE codigo_archivo = 'ARC-REP-0102'), 'req-reporte-0102', 1),
  ('JOB-REP-0103', 'RESUMEN_NEGOCIO', '{"origen":"PUBLICO"}'::jsonb, 'ERROR', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'atencion1'), NOW() - INTERVAL '40 minute', NOW() - INTERVAL '39 minute', NOW() - INTERVAL '39 minute', 2, 'No se pudo completar por timeout del proceso de exportacion.', NULL, 'req-reporte-0103', 2);

INSERT INTO notificacion (
  tipo_notificacion,
  titulo,
  mensaje,
  modulo,
  referencia_tipo,
  referencia_id,
  usuario_destino_id,
  estado,
  prioridad,
  payload_json
)
VALUES
  ('REPORTE_LISTO', 'Reporte diario disponible', 'El archivo de pedidos del dia ya puede descargarse.', 'REPORTES', 'JOB_REPORTE', 'JOB-REP-0101', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 'NO_LEIDA', 'MEDIA', '{"archivo":"ARC-REP-0101"}'::jsonb),
  ('PEDIDO_URGENTE', 'Pedido urgente en preparacion', 'El pedido PED-0102 requiere seguimiento prioritario.', 'PEDIDOS', 'PEDIDO', 'PED-0102', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1'), 'NO_LEIDA', 'ALTA', '{"pedidoCodigo":"PED-0102"}'::jsonb),
  ('COTIZACION_PUBLICA', 'Nueva solicitud web', 'Se recibio una cotizacion publica de Sofia Mena.', 'COTIZACIONES', 'COTIZACION', 'COT-0101', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'atencion1'), 'LEIDA', 'MEDIA', '{"cliente":"Sofia Mena"}'::jsonb),
  ('ERROR_REPORTE', 'Reporte con error', 'El reporte de cotizaciones pendientes fallo y requiere revision.', 'REPORTES', 'JOB_REPORTE', 'JOB-REP-0103', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 'NO_LEIDA', 'ALTA', '{"jobCodigo":"JOB-REP-0103"}'::jsonb);

INSERT INTO auditoria_evento (
  codigo_evento,
  modulo,
  entidad,
  entidad_id,
  accion,
  actor_usuario_id,
  actor_rol,
  fecha_evento,
  valor_anterior_json,
  valor_nuevo_json,
  motivo,
  request_id,
  ip_origen
)
VALUES
  ('CLIENTE_CREADO', 'CLIENTES', 'cliente', 'sofia.mena@example.com', 'CREAR_CLIENTE', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'atencion1'), 'ATENCION', NOW() - INTERVAL '2 day', NULL, '{"correo":"sofia.mena@example.com","telefono":"0990000010"}'::jsonb, 'Alta manual desde mostrador.', 'req-audit-0101', '127.0.0.1'),
  ('COTIZACION_PUBLICA_CREADA', 'COTIZACIONES', 'cotizacion', 'COT-0101', 'CREAR_COTIZACION_PUBLICA', NULL, 'PUBLICO', NOW() - INTERVAL '6 hour', NULL, '{"estado":"PENDIENTE","origen":"PUBLICO"}'::jsonb, 'Formulario publico recibido.', 'req-audit-0102', '127.0.0.1'),
  ('PEDIDO_CREADO', 'PEDIDOS', 'pedido', 'PED-0102', 'CREAR_PEDIDO', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'atencion1'), 'ATENCION', NOW() - INTERVAL '5 hour', NULL, '{"estado":"EN_PREPARACION","prioridad":"URGENTE"}'::jsonb, 'Conversion de cotizacion corporativa.', 'req-audit-0103', '127.0.0.1'),
  ('PRODUCCION_ESTADO_ACTUALIZADO', 'PRODUCCION', 'produccion', 'PED-0102', 'ACTUALIZAR_ESTADO_PRODUCCION', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'produccion1'), 'PRODUCCION', NOW() - INTERVAL '2 hour', '{"estado":"PENDIENTE"}'::jsonb, '{"estado":"EN_PROCESO"}'::jsonb, 'Inicio de armado de mesa dulce.', 'req-audit-0104', '127.0.0.1'),
  ('PEDIDO_ENTREGADO', 'PEDIDOS', 'pedido', 'PED-0105', 'ENTREGAR_PEDIDO', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'atencion1'), 'ATENCION', NOW() - INTERVAL '4 hour', '{"estado":"LISTO"}'::jsonb, '{"estado":"ENTREGADO"}'::jsonb, 'Entrega confirmada en oficina.', 'req-audit-0105', '127.0.0.1'),
  ('REPORTE_GENERADO', 'REPORTES', 'job_reporte', 'JOB-REP-0102', 'GENERAR_REPORTE', (SELECT usuario_id FROM usuario_sistema WHERE nombre_usuario = 'admin'), 'ADMIN', NOW() - INTERVAL '1 day', '{"estado":"EN_PROCESO"}'::jsonb, '{"estado":"COMPLETADO"}'::jsonb, 'Resumen semanal generado correctamente.', 'req-audit-0106', '127.0.0.1');
