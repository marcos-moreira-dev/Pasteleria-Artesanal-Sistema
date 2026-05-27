INSERT INTO rol_usuario (codigo, nombre_rol, descripcion)
VALUES
  ('ADMIN', 'ADMINISTRADOR', 'Control completo del sistema'),
  ('ATENCION', 'ATENCION', 'Registro y seguimiento comercial'),
  ('PRODUCCION', 'PRODUCCION', 'Seguimiento del flujo operativo');

INSERT INTO usuario_sistema (rol_id, nombre_usuario, password_hash, nombres, apellidos, correo)
VALUES
  ((SELECT rol_id FROM rol_usuario WHERE codigo = 'ADMIN'), 'admin', '$2a$10$FD9cVwV0RU1adR/UbFsdH.eXX/ko0nb8is1UIf7DjPwnc4gl7hTA6', 'Admin', 'Pasteleria', 'admin@pasteleria.local'),
  ((SELECT rol_id FROM rol_usuario WHERE codigo = 'ATENCION'), 'atencion1', '$2a$10$bzIr6m.x1zKTWPC5EsiFye0SDV.kU8ZWzpodr5lfHr9shBZ/UelEC', 'Ana', 'Atencion', 'atencion1@pasteleria.local'),
  ((SELECT rol_id FROM rol_usuario WHERE codigo = 'PRODUCCION'), 'produccion1', '$2a$10$jVVgAGAti/qofTb5LFB5kOaT/.h3Bt8tIdhpcjj.hPY4flDHVjdrq', 'Pedro', 'Produccion', 'produccion1@pasteleria.local');

INSERT INTO categoria_producto (codigo, nombre, descripcion, orden_visual)
VALUES
  ('TORTAS', 'TORTAS', 'Tortas principales del catalogo', 1),
  ('POSTRES', 'POSTRES', 'Postres individuales y familiares', 2),
  ('GALLETAS', 'GALLETAS', 'Galletas decoradas y artesanales', 3),
  ('BEBIDAS', 'BEBIDAS', 'Bebidas de apoyo para el catalogo', 4),
  ('CUPCAKES', 'CUPCAKES', 'Cupcakes decorados y tradicionales', 5),
  ('ESPECIALES', 'ESPECIALES', 'Productos especiales y mesas dulces', 6);

INSERT INTO producto (categoria_id, codigo, slug, nombre, descripcion, precio_base, requiere_cotizacion, activo, publicado)
VALUES
  -- Tortas (4)
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'TORTAS'), 'TORT-CHOC-01', 'torta-chocolate-mediana', 'Torta de Chocolate', 'Deliciosa torta de chocolate', 150.00, FALSE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'TORTAS'), 'TORT-VAIN-FRUT', 'torta-vainilla-frutos', 'Torta Vainilla con Frutos', 'Torta de vainilla con frutos rojos', 160.00, FALSE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'TORTAS'), 'TORT-RED-VEL', 'torta-red-velvet-grande', 'Torta Red Velvet', 'Elegante torta red velvet', 180.00, FALSE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'TORTAS'), 'TORT-3-LECH', 'torta-tres-leches-pequena', 'Torta Tres Leches', 'Tradicional torta tres leches', 140.00, FALSE, TRUE, TRUE),
  -- Postres (3)
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'POSTRES'), 'BROWNIE-IND', 'brownie-individual', 'Brownie Individual', 'Brownie artesanal', 25.00, FALSE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'POSTRES'), 'ALFAJOR-PREM', 'alfajor-premium', 'Alfajor Premium', 'Alfajor premium', 15.00, FALSE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'POSTRES'), 'CHEESE-FRUT', 'cheesecake-frutos-rojos', 'Cheesecake Frutos Rojos', 'Cheesecake con frutos', 45.00, FALSE, TRUE, TRUE),
  -- Galletas (1)
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'GALLETAS'), 'GALLETA-DEC', 'galleta-decorada', 'Galleta Decorada', 'Galleta decorada artesanal', 12.00, FALSE, TRUE, TRUE),
  -- Bebidas (1)
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'BEBIDAS'), 'CAFE-AMER', 'cafe-americano', 'Cafe Americano', 'Cafe americano', 18.00, FALSE, TRUE, TRUE),
  -- Cupcakes (2)
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'CUPCAKES'), 'CUPK-VAIN-01', 'cupcake-vainilla', 'Cupcake Vainilla', 'Cupcake de vainilla', 20.00, FALSE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'CUPCAKES'), 'CUPK-CHOC-01', 'cupcake-chocolate', 'Cupcake Chocolate', 'Cupcake de chocolate', 22.00, FALSE, TRUE, TRUE),
  -- Especiales (3)
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'ESPECIALES'), 'MESA-DULCE-25', 'mesa-dulce-25-personas', 'Mesa Dulce 25', 'Mesa dulce 25 personas', 350.00, TRUE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'ESPECIALES'), 'MESA-DULCE-50', 'mesa-dulce-50-personas', 'Mesa Dulce 50', 'Mesa dulce 50 personas', 650.00, TRUE, TRUE, TRUE),
  ((SELECT categoria_id FROM categoria_producto WHERE codigo = 'ESPECIALES'), 'CAJA-MINI', 'caja-mini-postres', 'Caja Mini Postres', 'Caja mini postres', 85.00, FALSE, TRUE, TRUE);
