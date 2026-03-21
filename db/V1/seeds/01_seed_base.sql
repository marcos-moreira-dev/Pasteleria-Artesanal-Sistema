-- =============================================================================
-- Pasteleria — Semilla base: roles, usuarios y catalogo inicial
-- Seeds: 01_seed_base.sql
-- Ejecutar luego de V1_3FN.sql
-- =============================================================================

INSERT INTO rol_usuario (codigo, nombre_rol, descripcion)
VALUES
  ('ADMIN',     'ADMINISTRADOR', 'Control completo del sistema'),
  ('ATENCION',  'ATENCION',      'Registro y seguimiento comercial'),
  ('PRODUCCION','PRODUCCION',    'Seguimiento del flujo operativo');

INSERT INTO usuario_sistema (rol_id, nombre_usuario, password_hash, nombres, apellidos, correo)
VALUES
  ((SELECT rol_id FROM rol_usuario WHERE codigo = 'ADMIN'),
   'admin',     '$2a$10$QJ0k5X2CPUg00KqzT9Y8H.Voc7TESihb2R2F/CybecLaPCFj3G03u',
   'Admin',     'Pasteleria', 'admin@pasteleria.local'),
  ((SELECT rol_id FROM rol_usuario WHERE codigo = 'ATENCION'),
   'atencion1', '$2a$10$bzIr6m.x1zKTWPC5EsiFye0SDV.kU8ZWzpodr5lfHr9shBZ/UelEC',
   'Ana',       'Atencion',   'atencion1@pasteleria.local'),
  ((SELECT rol_id FROM rol_usuario WHERE codigo = 'PRODUCCION'),
   'produccion1','$2a$10$jVVgAGAti/qofTb5LFB5kOaT/.h3Bt8tIdhpcjj.hPY4flDHVjdrq',
   'Pedro',     'Produccion',  'produccion1@pasteleria.local');

INSERT INTO categoria_producto (codigo, nombre, descripcion, orden_visual)
VALUES
  ('TORTAS',   'TORTAS',   'Tortas principales del catalogo',                          1),
  ('POSTRES',  'POSTRES',  'Postres individuales y familiares',                      2),
  ('GALLETAS', 'GALLETAS', 'Galletas decoradas y artesanales',                        3),
  ('BEBIDAS',  'BEBIDAS',  'Bebidas de apoyo para el catalogo',                       4);

INSERT INTO producto (categoria_id, codigo, slug, nombre, descripcion, precio_base, requiere_cotizacion, activo, publicado)
VALUES
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'TORTAS'),
   'PROD-TORTA-CHOCO-M', 'torta-chocolate-mediana', 'Torta de chocolate mediana',
   'Torta clasica de chocolate para celebraciones.', 28.50, FALSE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'TORTAS'),
   'PROD-TORTA-3L-P',    'torta-tres-leches-pequena', 'Torta tres leches pequena',
   'Version pequena para reuniones familiares.', 24.00, FALSE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'POSTRES'),
   'PROD-BROWNIE-IND',   'brownie-individual', 'Brownie individual',
   'Brownie artesanal de porcion individual.', 3.25, FALSE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'GALLETAS'),
   'PROD-GALLETA-DEC',   'galleta-decorada', 'Galleta decorada',
   'Galleta artesanal con decoracion personalizada.', 2.10, FALSE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'BEBIDAS'),
   'PROD-CAFE-AMER',     'cafe-americano', 'Cafe americano',
   'Bebida de apoyo para pedidos de mostrador.', 1.90, FALSE, TRUE, TRUE);
