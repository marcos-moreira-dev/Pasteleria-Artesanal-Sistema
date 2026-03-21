-- Fix para actualizar los slugs y que coincidan con los nombres de archivo de las imágenes
UPDATE producto SET slug = 'alfajor-premium', nombre = 'Alfajor Premium' WHERE codigo = 'ALFAJOR-PREM';
UPDATE producto SET slug = 'brownie-individual', nombre = 'Brownie Individual' WHERE codigo = 'BROWNIE-IND';
UPDATE producto SET slug = 'cafe-americano', nombre = 'Café Americano' WHERE codigo = 'CAFE-AMER';
UPDATE producto SET slug = 'caja-mini-postres', nombre = 'Caja Mini Postres' WHERE codigo = 'CAJA-MINI';
UPDATE producto SET slug = 'cheesecake-frutos-rojos', nombre = 'Cheesecake Frutos Rojos' WHERE codigo = 'CHEESE-FRUT';
UPDATE producto SET slug = 'cupcake-chocolate', nombre = 'Cupcake Chocolate' WHERE codigo = 'CUPK-CHOC-01';
UPDATE producto SET slug = 'cupcake-vainilla', nombre = 'Cupcake Vainilla' WHERE codigo = 'CUPK-VAIN-01';
UPDATE producto SET slug = 'galleta-decorada', nombre = 'Galleta Decorada' WHERE codigo = 'GALLETA-DEC';
UPDATE producto SET slug = 'mesa-dulce-25-personas', nombre = 'Mesa Dulce 25 Personas' WHERE codigo = 'MESA-DULCE-25';
UPDATE producto SET slug = 'mesa-dulce-50-personas', nombre = 'Mesa Dulce 50 Personas' WHERE codigo = 'MESA-DULCE-50';
UPDATE producto SET slug = 'torta-chocolate-mediana', nombre = 'Torta Chocolate Mediana' WHERE codigo = 'TORT-CHOC-01';
UPDATE producto SET slug = 'torta-red-velvet-grande', nombre = 'Torta Red Velvet Grande' WHERE codigo = 'TORT-RED-VEL';
UPDATE producto SET slug = 'torta-tres-leches-pequena', nombre = 'Torta Tres Leches Pequeña' WHERE codigo = 'TORT-3-LECH';
UPDATE producto SET slug = 'torta-vainilla-frutos', nombre = 'Torta Vainilla Frutos' WHERE codigo = 'TORT-VAIN-FRUT';

-- Verificar
SELECT codigo, slug, nombre, publicado FROM producto WHERE publicado = TRUE ORDER BY slug;
