-- =============================================================================
-- Pasteleria — Reset demo: trunca todas las tablas del esquema
-- Tools: 99_reset_demo.sql
-- ADVERTENCIA: elimina todos los datos de las tablas
-- Ejecutar solo en entorno de desarrollo o pruebas
-- =============================================================================

SET session_replication_role = 'replica';

TRUNCATE TABLE
  auditoria_evento,
  notificacion,
  job_reporte,
  archivo_recurso,
  produccion,
  pedido_detalle,
  pedido,
  cotizacion_detalle,
  cotizacion,
  cliente,
  producto,
  categoria_producto,
  usuario_sistema,
  rol_usuario
RESTART IDENTITY CASCADE;

SET session_replication_role = DEFAULT;

-- Para re-popular luego del reset, ejecutar:
--   db/V1/seeds/01_seed_base.sql
--   db/V1/seeds/02_seed_demo.sql
--   db/V1/seeds/03_seed_enterprise.sql
