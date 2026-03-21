-- Fix para login del admin y productos faltantes
-- Ejecutar en la base de datos PostgreSQL

-- 1. Actualizar contraseña del admin a 'admin12345'
UPDATE usuario_sistema 
SET password_hash = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjXd.JYTGjS4mTPKxwqDzXcxzJ9hX4e'
WHERE nombre_usuario = 'admin';

-- 2. Insertar categorías faltantes si no existen
INSERT INTO categoria_producto (codigo, nombre, descripcion, orden_visual)
VALUES
  ('CUPCAKES', 'CUPCAKES', 'Cupcakes decorados y tradicionales', 5),
  ('ESPECIALES', 'ESPECIALES', 'Productos especiales y de temporada', 6)
ON CONFLICT (codigo) DO NOTHING;

-- 3. Insertar todos los productos con sus imágenes correspondientes
-- Primero obtener los IDs de las categorías
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

  -- Insertar productos con sus imágenes
  INSERT INTO producto (categoria_id, codigo, slug, nombre, descripcion, precio_base, requiere_cotizacion, activo, publicado, ruta_imagen_principal, texto_alternativo_imagen)
  VALUES
    -- Tortas (ya existen algunas, actualizar imágenes)
    (v_tortas_id, 'TORT-CHOC-01', 'torta-chocolate', 'Torta de Chocolate', 'Deliciosa torta de chocolate', 150.00, FALSE, TRUE, TRUE, '/assets/products/torta-chocolate-mediana.png', 'Torta de Chocolate'),
    (v_tortas_id, 'TORT-VAIN-FRUT', 'torta-vainilla-frutos', 'Torta Vainilla con Frutos', 'Torta de vainilla con frutos rojos', 160.00, FALSE, TRUE, TRUE, '/assets/products/torta-vainilla-frutos.png', 'Torta Vainilla con Frutos'),
    (v_tortas_id, 'TORT-RED-VEL', 'torta-red-velvet', 'Torta Red Velvet', 'Elegante torta red velvet', 180.00, FALSE, TRUE, TRUE, '/assets/products/torta-red-velvet-grande.png', 'Torta Red Velvet'),
    (v_tortas_id, 'TORT-3-LECH', 'torta-tres-leches', 'Torta Tres Leches', 'Tradicional torta tres leches', 140.00, FALSE, TRUE, TRUE, '/assets/products/torta-tres-leches-pequena.png', 'Torta Tres Leches'),
    
    -- Postres
    (v_postres_id, 'BROWNIE-IND', 'brownie-individual', 'Brownie Individual', 'Brownie artesanal de porción individual', 25.00, FALSE, TRUE, TRUE, '/assets/products/brownie-individual.png', 'Brownie Individual'),
    (v_postres_id, 'ALFAJOR-PREM', 'alfajor-premium', 'Alfajor Premium', 'Alfajor premium con dulce de leche', 15.00, FALSE, TRUE, TRUE, '/assets/products/alfajor-premium.png', 'Alfajor Premium'),
    (v_postres_id, 'CHEESE-FRUT', 'cheesecake-frutos', 'Cheesecake de Frutos Rojos', 'Cheesecake con topping de frutos rojos', 45.00, FALSE, TRUE, TRUE, '/assets/products/cheesecake-frutos-rojos.png', 'Cheesecake de Frutos Rojos'),
    
    -- Galletas
    (v_galletas_id, 'GALLETA-DEC', 'galleta-decorada', 'Galleta Decorada', 'Galleta artesanal con decoración personalizada', 12.00, FALSE, TRUE, TRUE, '/assets/products/galleta-decorada.png', 'Galleta Decorada'),
    
    -- Bebidas
    (v_bebidas_id, 'CAFE-AMER', 'cafe-americano', 'Café Americano', 'Café americano recién preparado', 18.00, FALSE, TRUE, TRUE, '/assets/products/cafe-americano.png', 'Café Americano'),
    
    -- Cupcakes
    (v_cupcakes_id, 'CUPK-VAIN-01', 'cupcake-vainilla', 'Cupcake Vainilla', 'Cupcake de vainilla con frosting', 20.00, FALSE, TRUE, TRUE, '/assets/products/cupcake-vainilla.png', 'Cupcake Vainilla'),
    (v_cupcakes_id, 'CUPK-CHOC-01', 'cupcake-chocolate', 'Cupcake Chocolate', 'Cupcake de chocolate con ganache', 22.00, FALSE, TRUE, TRUE, '/assets/products/cupcake-chocolate.png', 'Cupcake Chocolate'),
    
    -- Especiales (mesas dulces)
    (v_especiales_id, 'MESA-DULCE-25', 'mesa-dulce-25', 'Mesa Dulce 25 Personas', 'Mesa dulce para 25 personas', 350.00, TRUE, TRUE, TRUE, '/assets/products/mesa-dulce-25-personas.png', 'Mesa Dulce 25 Personas'),
    (v_especiales_id, 'MESA-DULCE-50', 'mesa-dulce-50', 'Mesa Dulce 50 Personas', 'Mesa dulce para 50 personas', 650.00, TRUE, TRUE, TRUE, '/assets/products/mesa-dulce-50-personas.png', 'Mesa Dulce 50 Personas'),
    (v_especiales_id, 'CAJA-MINI', 'caja-mini-postres', 'Caja Mini Postres', 'Caja surtida de mini postres', 85.00, FALSE, TRUE, TRUE, '/assets/products/caja-mini-postres.png', 'Caja Mini Postres')
  ON CONFLICT (codigo) DO UPDATE SET
    publicado = TRUE,
    activo = TRUE,
    ruta_imagen_principal = EXCLUDED.ruta_imagen_principal,
    texto_alternativo_imagen = EXCLUDED.texto_alternativo_imagen;

END $$;

-- Verificar resultados
SELECT 'Contraseña del admin actualizada' as mensaje;
SELECT COUNT(*) as total_productos_publicados FROM producto WHERE publicado = TRUE;
