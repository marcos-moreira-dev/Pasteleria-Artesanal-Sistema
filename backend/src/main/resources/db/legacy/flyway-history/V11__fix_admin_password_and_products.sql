-- Migration: V11__fix_admin_password_and_products.sql
-- Purpose: Update admin password and ensure all products are published

-- Update admin password to 'admin12345'
UPDATE usuario_sistema 
SET password_hash = '$2a$10$FD9cVwV0RU1adR/UbFsdH.eXX/ko0nb8is1UIf7DjPwnc4gl7hTA6'
WHERE nombre_usuario = 'admin';

-- Insert missing product categories
INSERT INTO categoria_producto (codigo, nombre, descripcion, orden_visual)
VALUES
  ('CUPCAKES', 'CUPCAKES', 'Cupcakes decorados y tradicionales', 5),
  ('ESPECIALES', 'ESPECIALES', 'Productos especiales y mesas dulces', 6)
ON CONFLICT (codigo) DO NOTHING;

-- Insert/update all products with proper slugs matching image files
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

  -- Insert all 14 products
  INSERT INTO producto (categoria_id, codigo, slug, nombre, descripcion, precio_base, requiere_cotizacion, activo, publicado)
  VALUES
    -- Tortas (4)
    (v_tortas_id, 'TORT-CHOC-01', 'torta-chocolate-mediana', 'Torta de Chocolate', 'Deliciosa torta de chocolate', 150.00, FALSE, TRUE, TRUE),
    (v_tortas_id, 'TORT-VAIN-FRUT', 'torta-vainilla-frutos', 'Torta Vainilla con Frutos', 'Torta de vainilla con frutos rojos', 160.00, FALSE, TRUE, TRUE),
    (v_tortas_id, 'TORT-RED-VEL', 'torta-red-velvet-grande', 'Torta Red Velvet', 'Elegante torta red velvet', 180.00, FALSE, TRUE, TRUE),
    (v_tortas_id, 'TORT-3-LECH', 'torta-tres-leches-pequena', 'Torta Tres Leches', 'Tradicional torta tres leches', 140.00, FALSE, TRUE, TRUE),
    
    -- Postres (3)
    (v_postres_id, 'BROWNIE-IND', 'brownie-individual', 'Brownie Individual', 'Brownie artesanal', 25.00, FALSE, TRUE, TRUE),
    (v_postres_id, 'ALFAJOR-PREM', 'alfajor-premium', 'Alfajor Premium', 'Alfajor premium', 15.00, FALSE, TRUE, TRUE),
    (v_postres_id, 'CHEESE-FRUT', 'cheesecake-frutos-rojos', 'Cheesecake Frutos Rojos', 'Cheesecake con frutos', 45.00, FALSE, TRUE, TRUE),
    
    -- Galletas (1)
    (v_galletas_id, 'GALLETA-DEC', 'galleta-decorada', 'Galleta Decorada', 'Galleta decorada artesanal', 12.00, FALSE, TRUE, TRUE),
    
    -- Bebidas (1)
    (v_bebidas_id, 'CAFE-AMER', 'cafe-americano', 'Café Americano', 'Café americano', 18.00, FALSE, TRUE, TRUE),
    
    -- Cupcakes (2)
    (v_cupcakes_id, 'CUPK-VAIN-01', 'cupcake-vainilla', 'Cupcake Vainilla', 'Cupcake de vainilla', 20.00, FALSE, TRUE, TRUE),
    (v_cupcakes_id, 'CUPK-CHOC-01', 'cupcake-chocolate', 'Cupcake Chocolate', 'Cupcake de chocolate', 22.00, FALSE, TRUE, TRUE),
    
    -- Especiales (3)
    (v_especiales_id, 'MESA-DULCE-25', 'mesa-dulce-25-personas', 'Mesa Dulce 25', 'Mesa dulce 25 personas', 350.00, TRUE, TRUE, TRUE),
    (v_especiales_id, 'MESA-DULCE-50', 'mesa-dulce-50-personas', 'Mesa Dulce 50', 'Mesa dulce 50 personas', 650.00, TRUE, TRUE, TRUE),
    (v_especiales_id, 'CAJA-MINI', 'caja-mini-postres', 'Caja Mini Postres', 'Caja mini postres', 85.00, FALSE, TRUE, TRUE)
  ON CONFLICT (codigo) DO UPDATE SET
    publicado = TRUE,
    activo = TRUE,
    slug = EXCLUDED.slug;

END $$;
