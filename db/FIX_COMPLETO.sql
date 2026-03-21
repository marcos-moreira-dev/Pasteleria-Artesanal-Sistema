-- ============================================================
-- FIX COMPLETO PARA PASTELERÍA - PostgreSQL Local (puerto 5432)
-- Ejecutar todo este script en pgAdmin o psql
-- ============================================================

-- 1. ACTUALIZAR CONTRASEÑA DEL ADMIN
-- Password hasheada para 'admin12345'
UPDATE usuario_sistema 
SET password_hash = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjXd.JYTGjS4mTPKxwqDzXcxzJ9hX4e'
WHERE nombre_usuario = 'admin';

-- 2. INSERTAR CATEGORÍAS FALTANTES
INSERT INTO categoria_producto (codigo, nombre, descripcion, orden_visual)
VALUES
  ('CUPCAKES', 'CUPCAKES', 'Cupcakes decorados y tradicionales', 5),
  ('ESPECIALES', 'ESPECIALES', 'Productos especiales y de temporada', 6)
ON CONFLICT (codigo) DO NOTHING;

-- 3. OBTENER IDs DE CATEGORÍAS
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

  -- 4. INSERTAR TODOS LOS PRODUCTOS CON SUS IMÁGENES
  INSERT INTO producto (categoria_id, codigo, slug, nombre, descripcion, precio_base, requiere_cotizacion, activo, publicado)
  VALUES
    -- Tortas
    (v_tortas_id, 'TORT-CHOC-01', 'torta-chocolate', 'Torta de Chocolate', 'Deliciosa torta de chocolate', 150.00, FALSE, TRUE, TRUE),
    (v_tortas_id, 'TORT-VAIN-FRUT', 'torta-vainilla-frutos', 'Torta Vainilla con Frutos', 'Torta de vainilla con frutos rojos', 160.00, FALSE, TRUE, TRUE),
    (v_tortas_id, 'TORT-RED-VEL', 'torta-red-velvet', 'Torta Red Velvet', 'Elegante torta red velvet', 180.00, FALSE, TRUE, TRUE),
    (v_tortas_id, 'TORT-3-LECH', 'torta-tres-leches', 'Torta Tres Leches', 'Tradicional torta tres leches', 140.00, FALSE, TRUE, TRUE),
    
    -- Postres
    (v_postres_id, 'BROWNIE-IND', 'brownie-individual', 'Brownie Individual', 'Brownie artesanal de porción individual', 25.00, FALSE, TRUE, TRUE),
    (v_postres_id, 'ALFAJOR-PREM', 'alfajor-premium', 'Alfajor Premium', 'Alfajor premium con dulce de leche', 15.00, FALSE, TRUE, TRUE),
    (v_postres_id, 'CHEESE-FRUT', 'cheesecake-frutos', 'Cheesecake de Frutos Rojos', 'Cheesecake con topping de frutos rojos', 45.00, FALSE, TRUE, TRUE),
    
    -- Galletas
    (v_galletas_id, 'GALLETA-DEC', 'galleta-decorada', 'Galleta Decorada', 'Galleta artesanal con decoración personalizada', 12.00, FALSE, TRUE, TRUE),
    
    -- Bebidas
    (v_bebidas_id, 'CAFE-AMER', 'cafe-americano', 'Café Americano', 'Café americano recién preparado', 18.00, FALSE, TRUE, TRUE),
    
    -- Cupcakes
    (v_cupcakes_id, 'CUPK-VAIN-01', 'cupcake-vainilla', 'Cupcake Vainilla', 'Cupcake de vainilla con frosting', 20.00, FALSE, TRUE, TRUE),
    (v_cupcakes_id, 'CUPK-CHOC-01', 'cupcake-chocolate', 'Cupcake Chocolate', 'Cupcake de chocolate con ganache', 22.00, FALSE, TRUE, TRUE),
    
    -- Especiales (mesas dulces)
    (v_especiales_id, 'MESA-DULCE-25', 'mesa-dulce-25', 'Mesa Dulce 25 Personas', 'Mesa dulce para 25 personas', 350.00, TRUE, TRUE, TRUE),
    (v_especiales_id, 'MESA-DULCE-50', 'mesa-dulce-50', 'Mesa Dulce 50 Personas', 'Mesa dulce para 50 personas', 650.00, TRUE, TRUE, TRUE),
    (v_especiales_id, 'CAJA-MINI', 'caja-mini-postres', 'Caja Mini Postres', 'Caja surtida de mini postres', 85.00, FALSE, TRUE, TRUE)
  ON CONFLICT (codigo) DO UPDATE SET
    publicado = TRUE,
    activo = TRUE;

END $$;

-- 5. VERIFICAR RESULTADOS
SELECT '=== RESULTADOS ===' as info;
SELECT 'Usuarios:' as info, COUNT(*) as total FROM usuario_sistema;
SELECT 'Productos publicados:' as info, COUNT(*) as total FROM producto WHERE publicado = TRUE;
SELECT 'Admin password actualizada:' as info, 
  CASE WHEN password_hash IS NOT NULL THEN 'SI' ELSE 'NO' END as status
FROM usuario_sistema WHERE nombre_usuario = 'admin';

-- Listar productos publicados
SELECT codigo, slug, nombre, publicado FROM producto WHERE publicado = TRUE ORDER BY slug;
