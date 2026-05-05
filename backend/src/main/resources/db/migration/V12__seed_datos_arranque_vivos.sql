-- =============================================================================
-- Pastelería — Datos de arranque vivos
-- Migration: V12__seed_datos_arranque_vivos.sql
-- Objetivo: mantener el admin con actividad comercial, producción y seguimiento.
-- =============================================================================

UPDATE producto
SET activo = TRUE,
    publicado = TRUE
WHERE slug IS NOT NULL;

UPDATE cliente
SET activo = TRUE
WHERE correo IN (
  'sofia.mena@example.com',
  'eventos.atlas@example.com',
  'camila.torres@example.com',
  'valentina.ruiz@example.com',
  'diego.santacruz@example.com'
);

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
  payload_json,
  fecha_creacion
)
SELECT
  seed.tipo_notificacion,
  seed.titulo,
  seed.mensaje,
  seed.modulo,
  seed.referencia_tipo,
  seed.referencia_id,
  usuario.usuario_id,
  seed.estado,
  seed.prioridad,
  seed.payload_json,
  seed.fecha_creacion
FROM (
  VALUES
    ('PEDIDO_LISTO', 'Pedido listo para retiro', 'El mostrador puede coordinar el retiro de un pedido terminado.', 'PEDIDOS', 'PEDIDO', 'PED-0103', 'admin', 'NO_LEIDA', 'MEDIA', '{"origen":"arranque"}'::jsonb, NOW()),
    ('PRODUCCION_DECORACION', 'Producción en decoración', 'Hay una torta personalizada esperando revisión de decoración.', 'PRODUCCION', 'PRODUCCION', 'PROD-0102', 'produccion1', 'NO_LEIDA', 'ALTA', '{"area":"decoracion"}'::jsonb, NOW()),
    ('STOCK_BAJO', 'Stock bajo de insumos', 'Revisar cajas, bases y empaques antes de cerrar la jornada.', 'ABASTECIMIENTO', 'INVENTARIO', 'INSUMOS', 'admin', 'NO_LEIDA', 'MEDIA', '{"area":"empaque"}'::jsonb, NOW())
) AS seed(tipo_notificacion, titulo, mensaje, modulo, referencia_tipo, referencia_id, usuario_destino, estado, prioridad, payload_json, fecha_creacion)
JOIN usuario_sistema usuario ON usuario.nombre_usuario = seed.usuario_destino
WHERE NOT EXISTS (
  SELECT 1
  FROM notificacion n
  WHERE n.tipo_notificacion = seed.tipo_notificacion
    AND n.referencia_id = seed.referencia_id
);

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
SELECT
  'REP-ARRANQUE-001',
  'RESUMEN_NEGOCIO',
  '{}'::jsonb,
  'COMPLETADO',
  usuario_id,
  NOW(),
  NOW(),
  NOW(),
  1,
  NULL,
  NULL,
  'req-arranque-001',
  0
FROM usuario_sistema
WHERE nombre_usuario = 'admin'
  AND NOT EXISTS (
    SELECT 1 FROM job_reporte WHERE codigo_job = 'REP-ARRANQUE-001'
  );
