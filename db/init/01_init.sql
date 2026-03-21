-- ============================================================
-- INICIALIZACIÓN COMPLETA - Pastelería
-- Se ejecuta automáticamente al crear el contenedor
-- ============================================================

-- 1. CREAR ROLES
INSERT INTO rol_usuario (codigo, nombre_rol, descripcion)
VALUES
  ('ADMIN',     'ADMINISTRADOR', 'Control completo del sistema'),
  ('ATENCION',  'ATENCION',      'Registro y seguimiento comercial'),
  ('PRODUCCION','PRODUCCION',    'Seguimiento del flujo operativo')
ON CONFLICT (codigo) DO NOTHING;

-- 2. CREAR USUARIO ADMIN (Password: admin12345)
INSERT INTO usuario_sistema (rol_id, nombre_usuario, password_hash, nombres, apellidos, correo)
VALUES
  ((SELECT rol_id FROM rol_usuario WHERE codigo = 'ADMIN'),
   'admin',     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjXd.JYTGjS4mTPKxwqDzXcxzJ9hX4e',
   'Admin',     'Pasteleria', 'admin@pasteleria.local')
ON CONFLICT (nombre_usuario) DO UPDATE SET
   password_hash = EXCLUDED.password_hash;

-- 3. CREAR CATEGORÍAS
INSERT INTO categoria_producto (codigo, nombre, descripcion, orden_visual)
VALUES
  ('TORTAS',   'TORTAS',   'Tortas principales del catalogo',    1),
  ('POSTRES',  'POSTRES',  'Postres individuales y familiares',  2),
  ('GALLETAS', 'GALLETAS', 'Galletas decoradas y artesanales',   3),
  ('BEBIDAS',  'BEBIDAS',  'Bebidas de apoyo para el catalogo',  4),
  ('CUPCAKES', 'CUPCAKES', 'Cupcakes decorados y tradicionales', 5),
  ('ESPECIALES', 'ESPECIALES', 'Productos especiales y mesas dulces', 6)
ON CONFLICT (codigo) DO NOTHING;

-- 4. CREAR PRODUCTOS CON IMÁGENES
DO $$
DECLARE
  v_tortas_id INTEGER;
  v_postres_id INTEGER;
  v_galletas_id INTEGER;
  v_bebidas_id INTEGER;
  v_cupcakes_id INTEGER;
  v_especiales_id INTEGER;
BEGIN
  SELECT categoria_id INTO v_tortas_id FROM categoria_producto WHERE codigo = 'TORTAS';
  SELECT categoria_id INTO v_postres_id FROM categoria_producto WHERE codigo = 'POSTRES';
  SELECT categoria_id INTO v_galletas_id FROM categoria_producto WHERE codigo = 'GALLETAS';
  SELECT categoria_id INTO v_bebidas_id FROM categoria_producto WHERE codigo = 'BEBIDAS';
  SELECT categoria_id INTO v_cupcakes_id FROM categoria_producto WHERE codigo = 'CUPCAKES';
  SELECT categoria_id INTO v_especiales_id FROM categoria_producto WHERE codigo = 'ESPECIALES';

  INSERT INTO producto (categoria_id, codigo, slug, nombre, descripcion, precio_base, requiere_cotizacion, activo, publicado)
  VALUES
    -- Tortas (4 productos)
    (v_tortas_id, 'TORT-CHOC-01', 'torta-chocolate-mediana', 'Torta de Chocolate', 'Deliciosa torta de chocolate para celebraciones', 150.00, FALSE, TRUE, TRUE),
    (v_tortas_id, 'TORT-VAIN-FRUT', 'torta-vainilla-frutos', 'Torta Vainilla con Frutos', 'Torta de vainilla con frutos rojos', 160.00, FALSE, TRUE, TRUE),
    (v_tortas_id, 'TORT-RED-VEL', 'torta-red-velvet-grande', 'Torta Red Velvet', 'Elegante torta red velvet para ocasiones especiales', 180.00, FALSE, TRUE, TRUE),
    (v_tortas_id, 'TORT-3-LECH', 'torta-tres-leches-pequena', 'Torta Tres Leches', 'Tradicional torta tres leches', 140.00, FALSE, TRUE, TRUE),
    
    -- Postres (3 productos)
    (v_postres_id, 'BROWNIE-IND', 'brownie-individual', 'Brownie Individual', 'Brownie artesanal de porción individual', 25.00, FALSE, TRUE, TRUE),
    (v_postres_id, 'ALFAJOR-PREM', 'alfajor-premium', 'Alfajor Premium', 'Alfajor premium con dulce de leche', 15.00, FALSE, TRUE, TRUE),
    (v_postres_id, 'CHEESE-FRUT', 'cheesecake-frutos-rojos', 'Cheesecake Frutos Rojos', 'Cheesecake con topping de frutos rojos', 45.00, FALSE, TRUE, TRUE),
    
    -- Galletas (1 producto)
    (v_galletas_id, 'GALLETA-DEC', 'galleta-decorada', 'Galleta Decorada', 'Galleta artesanal con decoración personalizada', 12.00, FALSE, TRUE, TRUE),
    
    -- Bebidas (1 producto)
    (v_bebidas_id, 'CAFE-AMER', 'cafe-americano', 'Café Americano', 'Café americano recién preparado', 18.00, FALSE, TRUE, TRUE),
    
    -- Cupcakes (2 productos)
    (v_cupcakes_id, 'CUPK-VAIN-01', 'cupcake-vainilla', 'Cupcake Vainilla', 'Cupcake de vainilla con frosting', 20.00, FALSE, TRUE, TRUE),
    (v_cupcakes_id, 'CUPK-CHOC-01', 'cupcake-chocolate', 'Cupcake Chocolate', 'Cupcake de chocolate con ganache', 22.00, FALSE, TRUE, TRUE),
    
    -- Especiales (3 productos)
    (v_especiales_id, 'MESA-DULCE-25', 'mesa-dulce-25-personas', 'Mesa Dulce 25 Personas', 'Mesa dulce completa para 25 personas', 350.00, TRUE, TRUE, TRUE),
    (v_especiales_id, 'MESA-DULCE-50', 'mesa-dulce-50-personas', 'Mesa Dulce 50 Personas', 'Mesa dulce completa para 50 personas', 650.00, TRUE, TRUE, TRUE),
    (v_especiales_id, 'CAJA-MINI', 'caja-mini-postres', 'Caja Mini Postres', 'Caja surtida de mini postres', 85.00, FALSE, TRUE, TRUE)
  ON CONFLICT (codigo) DO UPDATE SET
    publicado = TRUE,
    activo = TRUE;

END $$;

-- 5. CREAR CLIENTES DE EJEMPLO
INSERT INTO cliente (nombre_completo, telefono, correo, observaciones)
VALUES
  ('Maria Lopez', '0990000001', 'maria.lopez@example.com', 'Cliente frecuente'),
  ('Juan Perez', '0990000002', 'juan.perez@example.com', 'Prefiere retiro en local')
ON CONFLICT DO NOTHING;
