-- Data initialization for Docker (Hibernate schema)

-- Insert roles
INSERT INTO rol_usuario (codigo, nombre_rol, descripcion, activo, created_at) VALUES
('ADMIN', 'ADMINISTRADOR', 'Control completo del sistema', true, NOW()),
('ATENCION', 'ATENCION', 'Registro y seguimiento comercial', true, NOW()),
('PRODUCCION', 'PRODUCCION', 'Seguimiento del flujo operativo', true, NOW());

-- Insert admin user (password: admin12345)
INSERT INTO usuario_sistema (rol_id, nombre_usuario, password_hash, nombres, apellidos, correo, activo, created_at, updated_at, version)
SELECT 
    r.rol_id,
    'admin',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjXd.JYTGjS4mTPKxwqDzXcxzJ9hX4e',
    'Admin',
    'Pasteleria',
    'admin@pasteleria.local',
    true,
    NOW(),
    NOW(),
    0
FROM rol_usuario r
WHERE r.codigo = 'ADMIN';

-- Insert categories
INSERT INTO categoria_producto (codigo, nombre, descripcion, orden_visual, activo, created_at, updated_at, version) VALUES
('TORTAS', 'TORTAS', 'Tortas principales del catalogo', 1, true, NOW(), NOW(), 0),
('POSTRES', 'POSTRES', 'Postres individuales y familiares', 2, true, NOW(), NOW(), 0),
('GALLETAS', 'GALLETAS', 'Galletas decoradas y artesanales', 3, true, NOW(), NOW(), 0),
('BEBIDAS', 'BEBIDAS', 'Bebidas de apoyo para el catalogo', 4, true, NOW(), NOW(), 0),
('CUPCAKES', 'CUPCAKES', 'Cupcakes decorados y tradicionales', 5, true, NOW(), NOW(), 0),
('ESPECIALES', 'ESPECIALES', 'Productos especiales y mesas dulces', 6, true, NOW(), NOW(), 0);

-- Insert products
INSERT INTO producto (categoria_id, codigo, slug, nombre, descripcion, precio_base, requiere_cotizacion, activo, publicado, created_at, updated_at, version)
SELECT 
    c.categoria_id,
    p.codigo,
    p.slug,
    p.nombre,
    p.descripcion,
    p.precio_base,
    p.requiere_cotizacion,
    p.activo,
    p.publicado,
    NOW(),
    NOW(),
    0
FROM (
    VALUES
        ('TORTAS', 'TORT-CHOC-01', 'torta-chocolate-mediana', 'Torta de Chocolate', 'Deliciosa torta de chocolate', 150.00, false, true, true),
        ('TORTAS', 'TORT-VAIN-FRUT', 'torta-vainilla-frutos', 'Torta Vainilla con Frutos', 'Torta de vainilla con frutos rojos', 160.00, false, true, true),
        ('TORTAS', 'TORT-RED-VEL', 'torta-red-velvet-grande', 'Torta Red Velvet', 'Elegante torta red velvet', 180.00, false, true, true),
        ('TORTAS', 'TORT-3-LECH', 'torta-tres-leches-pequena', 'Torta Tres Leches', 'Tradicional torta tres leches', 140.00, false, true, true),
        ('POSTRES', 'BROWNIE-IND', 'brownie-individual', 'Brownie Individual', 'Brownie artesanal', 25.00, false, true, true),
        ('POSTRES', 'ALFAJOR-PREM', 'alfajor-premium', 'Alfajor Premium', 'Alfajor premium', 15.00, false, true, true),
        ('POSTRES', 'CHEESE-FRUT', 'cheesecake-frutos-rojos', 'Cheesecake Frutos Rojos', 'Cheesecake con frutos', 45.00, false, true, true),
        ('GALLETAS', 'GALLETA-DEC', 'galleta-decorada', 'Galleta Decorada', 'Galleta decorada artesanal', 12.00, false, true, true),
        ('BEBIDAS', 'CAFE-AMER', 'cafe-americano', 'Cafe Americano', 'Cafe americano', 18.00, false, true, true),
        ('CUPCAKES', 'CUPK-VAIN-01', 'cupcake-vainilla', 'Cupcake Vainilla', 'Cupcake de vainilla', 20.00, false, true, true),
        ('CUPCAKES', 'CUPK-CHOC-01', 'cupcake-chocolate', 'Cupcake Chocolate', 'Cupcake de chocolate', 22.00, false, true, true),
        ('ESPECIALES', 'MESA-DULCE-25', 'mesa-dulce-25-personas', 'Mesa Dulce 25', 'Mesa dulce 25 personas', 350.00, true, true, true),
        ('ESPECIALES', 'MESA-DULCE-50', 'mesa-dulce-50-personas', 'Mesa Dulce 50', 'Mesa dulce 50 personas', 650.00, true, true, true),
        ('ESPECIALES', 'CAJA-MINI', 'caja-mini-postres', 'Caja Mini Postres', 'Caja mini postres', 85.00, false, true, true)
) AS p(categoria_codigo, codigo, slug, nombre, descripcion, precio_base, requiere_cotizacion, activo, publicado)
JOIN categoria_producto c ON c.codigo = p.categoria_codigo;
