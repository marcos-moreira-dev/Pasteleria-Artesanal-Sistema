-- =============================================================================
-- Pasteleria — Seed Abastecimiento (Refactored)
-- Archivo: 04_seed_abastecimiento.sql
-- Proposito: Datos completos y consistentes para el modulo de abastecimiento
-- Dependencias: V9__abastecimiento_inventario_recetas.sql, seeds 01-03
-- Secuencia: Ejecutar DESPUES de 01_seed_base.sql, 02_seed_demo.sql, 03_seed_enterprise.sql
-- =============================================================================

BEGIN;

-- =============================================================================
-- SECCION 1: INGREDIENTES (20 ingredientes variados)
-- umedida_id: 1=g, 2=kg, 3=ml, 4=l, 5=u, 10=docena
-- =============================================================================

INSERT INTO ingrediente (umedida_id, codigo, nombre, descripcion, stock_minimo, stock_actual, costo_referencial) VALUES
  -- Ingredientes base para masas (1-7)
  (1,  'HAR-001',  'Harina de trigo 0000',      'Harina refinada para reposteria',              5000,  25000, 1.20),
  (1,  'HAR-002',  'Harina de trigo leudante',  'Harina con polvo de hornear incorporado',      2000,   8000, 1.35),
  (1,  'AZU-001',  'Azucar blanca refinada',   'Azucar fina para reposteria',                  3000,  15000, 1.10),
  (1,  'AZU-002',  'Azucar impalpable',        'Azucar glass para decoracion',                 1000,   5000, 2.50),
  (1,  'AZU-003',  'Azucar mascabo',           'Azucar morena para textura',                    500,   2500, 1.80),
  (2,  'MAN-001',  'Mantequilla sin sal',      'Mantequilla 82% MG, importada',                2000,   8000, 4.50),
  (1,  'MAN-002',  'Margarina vegetal',        'Margarina para masas hojaldres',               1500,   6000, 2.80),
  
  -- Lacteos y huevos (8-11)
  (4,  'LEC-001',  'Leche entera UHT',         'Leche fresca larga vida 1L',                   1000,   5000, 1.80),
  (3,  'LEC-002',  'Leche condensada',         'Leche condensada azucarada 397g',               300,   1500, 3.20),
  (3,  'CRE-001',  'Crema de leche',           'Crema para batir 250ml',                        200,   1000, 2.20),
  (5,  'HUE-001',  'Huevos frescos',           'Huevos de gallina categoria A',                 200,    800, 0.50),
  
  -- Chocolate y cacao (12-14)
  (1,  'CHO-001',  'Chocolate cobertura negro', 'Cobertura 55% cacao',                         2000,   6000, 5.80),
  (1,  'CHO-002',  'Chocolate cobertura blanco', 'Cobertura blanco premium',                   1000,   3000, 6.20),
  (1,  'CAC-001',  'Cacao en polvo',           'Cacao puro en polvo',                          500,   2000, 4.50),
  
  -- Esencias y aromas (15-16)
  (4,  'ESC-001',  'Esencia de vainilla',      'Esencia pura de vainilla 100ml',                 50,    200, 8.00),
  (4,  'ESC-002',  'Esencia de almendra',      'Esencia de almendra 50ml',                       30,    120, 6.50),
  
  -- Frutas y complementos (17-20)
  (1,  'COC-001',  'Coco rallado natural',     'Coco deshidratado sin azucar',                  500,   2000, 3.50),
  (1,  'NUE-001',  'Nueces picadas',           'Nueces naturales troceadas',                    300,   1200, 8.50),
  (1,  'FEC-001',  'Fecula de maiz',           'Maicena para espesantes',                      1000,   4000, 1.40),
  (1,  'SAL-001',  'Sal refinada',             'Sal de mesa fina',                              500,   2000, 0.60);

-- =============================================================================
-- SECCION 2: INSUMOS / EMPAQUES (15 insumos)
-- umedida_id: 5=u, 6=caja, 7=bandeja, 8=paq, 9=bolsa
-- =============================================================================

INSERT INTO insumo (umedida_id, codigo, nombre, descripcion, stock_minimo, stock_actual, costo_referencial) VALUES
  -- Moldes desechables (1-5)
  (8,  'MOL-001',  'Molde papel redondo 15cm', 'Molde desechable redondo chico',                200,    800, 0.30),
  (8,  'MOL-002',  'Molde papel redondo 20cm', 'Molde desechable redondo mediano',              150,    500, 0.45),
  (8,  'MOL-003',  'Molde papel redondo 25cm', 'Molde desechable redondo grande',               100,    400, 0.60),
  (8,  'MOL-004',  'Molde papel cuadrado 20cm', 'Molde desechable cuadrado',                    120,    480, 0.40),
  (8,  'MOL-005',  'Molde papel rectangular',   'Molde desechable rectangular 30x20',            80,    320, 0.55),
  
  -- Cajas y empaques (6-9)
  (8,  'CAJ-001',  'Caja pastel pequena',      'Caja carton 20x20x10cm',                        100,    300, 0.80),
  (8,  'CAJ-002',  'Caja pastel mediana',      'Caja carton 25x25x12cm',                         80,    240, 1.00),
  (8,  'CAJ-003',  'Caja pastel grande',       'Caja carton 30x30x15cm',                         60,    180, 1.20),
  (8,  'CAJ-004',  'Caja cupcake 6 unidades',  'Caja especial para 6 cupcakes',                 150,    600, 0.65),
  
  -- Decoracion y presentacion (10-13)
  (7,  'BAN-001',  'Bandeja aluminio',         'Bandeja aluminio desechable',                    50,    150, 1.50),
  (8,  'VEL-001',  'Velas numericas',          'Velas numeros 0-9 surtidas',                     100,    400, 0.20),
  (8,  'VEL-002',  'Velas cilindricas pack',   'Velas cilindricas colores x12',                  50,    200, 0.90),
  (8,  'TOP-001',  'Topper feliz cumpleanos',  'Topper decorativo acrilico',                     30,    120, 1.80),
  
  -- Utensilios y otros (14-15)
  (8,  'BOL-001',  'Bolsa kraft pequena',      'Bolsa papel kraft 15x20cm',                     200,   1000, 0.15),
  (5,  'ESP-001',  'Espatula de plastico',     'Espatula para decorar desechable',               20,     80, 0.45);

-- =============================================================================
-- SECCION 3: PROVEEDORES (8 proveedores)
-- =============================================================================

INSERT INTO proveedor (codigo, nombre, telefono, correo, direccion, observaciones) VALUES
  ('PROV-001', 'Distribuidora El Norte S.A.',      '04-2100-9901', 'ventas@elnorte.com',        'Av. Carlos Julio Arosemena Km 8, Guayaquil',      'Proveedor principal de harinas y azucares. Entrega 24-48h.'),
  ('PROV-002', 'Lacteos RioCasa Ecuador',          '03-2400-1234', 'pedidos@riocasa.com',       'Panamericana Norte Km 5, Riobamba',               'Lacteos frescos y mantequilla. Excelente calidad.'),
  ('PROV-003', 'Importadora Sabores Premium',      '04-2300-5678', 'compras@saborespremium.com', 'Cdla. Kennedy Norte, Av. Principal, Guayaquil',   'Chocolate, vainilla, esencias importadas.'),
  ('PROV-004', 'Mayorista del Litoral',            '04-2500-3344', 'ventas@litoralmayorista.ec', 'Urdesa Central, Guayaquil',                       'Insumos varios de respaldo. Precios competitivos.'),
  ('PROV-005', 'Papeles y Empaques del Guayas',    '04-2600-7788', 'ventas@pyeguayas.com',       'Vía a Daule Km 4.5, Guayaquil',                   'Cajas, moldes, bolsas. Especialistas en empaques.'),
  ('PROV-006', 'Huevos Frescos San Mateo',         '02-2200-4455', 'pedidos@sanmateo.com',       'Valle de los Chillos, Quito',                     'Huevos frescos diarios. Entrega tres veces por semana.'),
  ('PROV-007', 'Frutos Secos Andinos',             '03-2800-9900', 'ventas@andinossecos.com',    'Ambato, sector industrial',                       'Nueces, almendras, frutos secos. Producto nacional.'),
  ('PROV-008', 'Decoraciones Dulces Express',      '099-1234-567', 'info@dulcesexpress.com',     'Via Perimetral, Guayaquil',                       'Velas, toppers, decoraciones. Entrega express.');

-- =============================================================================
-- SECCION 4: LIGAS PROVEEDOR-ITEM (Relaciones completas)
-- =============================================================================

INSERT INTO item_proveedor (item_tipo, item_id, proveedor_id, precio_suministro, es_principal) VALUES
  -- Harinas y azucares desde El Norte (PROV-001)
  ('INGREDIENTE', 1, 1, 1.15, TRUE),   -- Harina 0000
  ('INGREDIENTE', 2, 1, 1.30, TRUE),   -- Harina leudante
  ('INGREDIENTE', 3, 1, 1.05, TRUE),   -- Azucar blanca
  ('INGREDIENTE', 4, 1, 2.40, TRUE),   -- Azucar impalpable
  ('INGREDIENTE', 5, 1, 1.70, TRUE),   -- Azucar mascabo
  
  -- Lacteos desde RioCasa (PROV-002)
  ('INGREDIENTE', 6, 2, 4.30, TRUE),   -- Mantequilla
  ('INGREDIENTE', 8, 2, 1.70, TRUE),   -- Leche entera
  ('INGREDIENTE', 9, 2, 3.10, TRUE),   -- Leche condensada
  ('INGREDIENTE', 10, 2, 2.15, TRUE),  -- Crema de leche
  
  -- Margarina alternativa desde Litoral
  ('INGREDIENTE', 7, 4, 2.65, FALSE),  -- Margarina
  
  -- Chocolates y cacao desde Sabores Premium (PROV-003)
  ('INGREDIENTE', 12, 3, 5.50, TRUE),  -- Chocolate negro
  ('INGREDIENTE', 13, 3, 5.90, TRUE),  -- Chocolate blanco
  ('INGREDIENTE', 14, 3, 4.30, TRUE),  -- Cacao en polvo
  ('INGREDIENTE', 15, 3, 7.80, TRUE),  -- Esencia vainilla
  ('INGREDIENTE', 16, 3, 6.35, TRUE),  -- Esencia almendra
  
  -- Huevos desde San Mateo (PROV-006)
  ('INGREDIENTE', 11, 6, 0.48, TRUE),  -- Huevos
  
  -- Frutos secos desde Andinos (PROV-007)
  ('INGREDIENTE', 18, 7, 8.20, TRUE),  -- Nueces
  ('INGREDIENTE', 17, 7, 3.40, TRUE),  -- Coco rallado
  
  -- Fecula y sal desde Litoral (PROV-004)
  ('INGREDIENTE', 19, 4, 1.35, TRUE),  -- Fecula
  ('INGREDIENTE', 20, 4, 0.58, TRUE),  -- Sal
  
  -- Insumos desde Papeles y Empaques (PROV-005)
  ('INSUMO', 1, 5, 0.28, TRUE),        -- Molde 15cm
  ('INSUMO', 2, 5, 0.42, TRUE),        -- Molde 20cm
  ('INSUMO', 3, 5, 0.56, TRUE),        -- Molde 25cm
  ('INSUMO', 4, 5, 0.38, TRUE),        -- Molde cuadrado
  ('INSUMO', 6, 5, 0.75, TRUE),        -- Caja pequena
  ('INSUMO', 7, 5, 0.95, TRUE),        -- Caja mediana
  ('INSUMO', 8, 5, 1.15, TRUE),        -- Caja grande
  ('INSUMO', 14, 5, 0.14, TRUE),       -- Bolsa kraft
  
  -- Insumos alternativos desde Litoral
  ('INSUMO', 6, 4, 0.78, FALSE),       -- Caja pequena alternativa
  ('INSUMO', 9, 4, 0.62, FALSE),       -- Caja cupcake alternativa
  
  -- Decoraciones desde Dulces Express (PROV-008)
  ('INSUMO', 11, 8, 1.45, TRUE),       -- Bandeja aluminio
  ('INSUMO', 12, 8, 0.18, TRUE),       -- Velas numericas
  ('INSUMO', 13, 8, 0.85, TRUE),       -- Velas cilindricas
  ('INSUMO', 15, 8, 1.70, TRUE);       -- Topper

-- =============================================================================
-- SECCION 5: MOVIMIENTOS DE INVENTARIO (30 movimientos realistas)
-- =============================================================================

INSERT INTO inventario_movimiento (item_tipo, item_id, tipo_movimiento, cantidad, saldo_posterior, referencia_tipo, referencia_id, observaciones, fecha_movimiento) VALUES
  -- Entradas iniciales de stock (1-15)
  ('INGREDIENTE', 1, 'ENTRADA_COMPRA', 15000, 15000, 'ORDEN_COMPRA', 'OC-2026-0001', 'Compra inicial harina 0000', NOW() - INTERVAL '30 days'),
  ('INGREDIENTE', 2, 'ENTRADA_COMPRA', 5000, 5000, 'ORDEN_COMPRA', 'OC-2026-0001', 'Compra inicial harina leudante', NOW() - INTERVAL '30 days'),
  ('INGREDIENTE', 3, 'ENTRADA_COMPRA', 10000, 10000, 'ORDEN_COMPRA', 'OC-2026-0002', 'Compra inicial azucar blanca', NOW() - INTERVAL '30 days'),
  ('INGREDIENTE', 4, 'ENTRADA_COMPRA', 3000, 3000, 'ORDEN_COMPRA', 'OC-2026-0002', 'Compra inicial azucar impalpable', NOW() - INTERVAL '30 days'),
  ('INGREDIENTE', 6, 'ENTRADA_COMPRA', 5000, 5000, 'ORDEN_COMPRA', 'OC-2026-0003', 'Compra inicial mantequilla', NOW() - INTERVAL '28 days'),
  ('INGREDIENTE', 8, 'ENTRADA_COMPRA', 3000, 3000, 'ORDEN_COMPRA', 'OC-2026-0003', 'Compra inicial leche entera', NOW() - INTERVAL '28 days'),
  ('INGREDIENTE', 11, 'ENTRADA_COMPRA', 500, 500, 'ORDEN_COMPRA', 'OC-2026-0004', 'Compra inicial huevos', NOW() - INTERVAL '28 days'),
  ('INGREDIENTE', 12, 'ENTRADA_COMPRA', 3000, 3000, 'ORDEN_COMPRA', 'OC-2026-0004', 'Compra inicial chocolate negro', NOW() - INTERVAL '27 days'),
  ('INGREDIENTE', 15, 'ENTRADA_COMPRA', 150, 150, 'ORDEN_COMPRA', 'OC-2026-0005', 'Compra inicial esencia vainilla', NOW() - INTERVAL '27 days'),
  ('INGREDIENTE', 19, 'ENTRADA_COMPRA', 2000, 2000, 'ORDEN_COMPRA', 'OC-2026-0005', 'Compra inicial fecula', NOW() - INTERVAL '27 days'),
  ('INSUMO', 1, 'ENTRADA_COMPRA', 500, 500, 'ORDEN_COMPRA', 'OC-2026-0006', 'Compra inicial moldes 15cm', NOW() - INTERVAL '25 days'),
  ('INSUMO', 2, 'ENTRADA_COMPRA', 300, 300, 'ORDEN_COMPRA', 'OC-2026-0006', 'Compra inicial moldes 20cm', NOW() - INTERVAL '25 days'),
  ('INSUMO', 6, 'ENTRADA_COMPRA', 200, 200, 'ORDEN_COMPRA', 'OC-2026-0007', 'Compra inicial cajas pequenas', NOW() - INTERVAL '25 days'),
  ('INSUMO', 12, 'ENTRADA_COMPRA', 300, 300, 'ORDEN_COMPRA', 'OC-2026-0007', 'Compra inicial velas numericas', NOW() - INTERVAL '25 days'),
  ('INGREDIENTE', 10, 'ENTRADA_COMPRA', 800, 800, 'ORDEN_COMPRA', 'OC-2026-0008', 'Compra inicial crema de leche', NOW() - INTERVAL '20 days'),
  
  -- Segunda tanda de entradas (16-22)
  ('INGREDIENTE', 1, 'ENTRADA_COMPRA', 10000, 25000, 'ORDEN_COMPRA', 'OC-2026-0010', 'Reposicion harina 0000', NOW() - INTERVAL '15 days'),
  ('INGREDIENTE', 3, 'ENTRADA_COMPRA', 5000, 15000, 'ORDEN_COMPRA', 'OC-2026-0010', 'Reposicion azucar blanca', NOW() - INTERVAL '15 days'),
  ('INGREDIENTE', 12, 'ENTRADA_COMPRA', 3000, 6000, 'ORDEN_COMPRA', 'OC-2026-0011', 'Reposicion chocolate', NOW() - INTERVAL '14 days'),
  ('INGREDIENTE', 6, 'ENTRADA_COMPRA', 3000, 8000, 'ORDEN_COMPRA', 'OC-2026-0011', 'Reposicion mantequilla', NOW() - INTERVAL '14 days'),
  ('INGREDIENTE', 11, 'ENTRADA_COMPRA', 300, 800, 'ORDEN_COMPRA', 'OC-2026-0012', 'Reposicion huevos', NOW() - INTERVAL '10 days'),
  ('INSUMO', 6, 'ENTRADA_COMPRA', 100, 300, 'ORDEN_COMPRA', 'OC-2026-0013', 'Reposicion cajas', NOW() - INTERVAL '10 days'),
  ('INSUMO', 1, 'ENTRADA_COMPRA', 300, 800, 'ORDEN_COMPRA', 'OC-2026-0013', 'Reposicion moldes', NOW() - INTERVAL '10 days'),
  
  -- Salidas por produccion (23-30)
  ('INGREDIENTE', 1, 'SALIDA_PRODUCCION', 2500, 22500, 'PRODUCCION', 'PROD-001', 'Consumo produccion tortas', NOW() - INTERVAL '7 days'),
  ('INGREDIENTE', 3, 'SALIDA_PRODUCCION', 2000, 13000, 'PRODUCCION', 'PROD-001', 'Consumo produccion tortas', NOW() - INTERVAL '7 days'),
  ('INGREDIENTE', 6, 'SALIDA_PRODUCCION', 1200, 6800, 'PRODUCCION', 'PROD-001', 'Consumo produccion tortas', NOW() - INTERVAL '7 days'),
  ('INGREDIENTE', 12, 'SALIDA_PRODUCCION', 800, 5200, 'PRODUCCION', 'PROD-001', 'Consumo produccion tortas', NOW() - INTERVAL '7 days'),
  ('INGREDIENTE', 1, 'SALIDA_PRODUCCION', 1500, 21000, 'PRODUCCION', 'PROD-002', 'Consumo produccion brownies', NOW() - INTERVAL '5 days'),
  ('INGREDIENTE', 3, 'SALIDA_PRODUCCION', 1200, 11800, 'PRODUCCION', 'PROD-002', 'Consumo produccion brownies', NOW() - INTERVAL '5 days'),
  ('INSUMO', 6, 'SALIDA_PRODUCCION', 50, 250, 'PRODUCCION', 'PROD-003', 'Consumo empaques', NOW() - INTERVAL '3 days'),
  ('INSUMO', 12, 'SALIDA_PRODUCCION', 20, 280, 'PRODUCCION', 'PROD-003', 'Consumo velas', NOW() - INTERVAL '3 days');

-- =============================================================================
-- SECCION 6: ORDENES DE COMPRA (10 ordenes en diferentes estados)
-- =============================================================================

-- OC-2026-0001 a 0008 ya fueron recibidas (en movimientos)
-- Creamos ordenes adicionales

INSERT INTO orden_compra (proveedor_id, codigo, estado, observaciones, fecha_entrega_estimada, created_by_user_id) VALUES
  -- Borrador
  (1, 'OC-2026-0014', 'BORRADOR', 'Reposicion mensual harinas y azucares', NOW() + INTERVAL '7 days', 1),
  (2, 'OC-2026-0015', 'BORRADOR', 'Pedido lacteos para proxima semana', NOW() + INTERVAL '5 days', 1),
  
  -- Enviadas
  (3, 'OC-2026-0016', 'ENVIADA', 'Chocolates y esencias - pedido urgente', NOW() + INTERVAL '3 days', 1),
  (5, 'OC-2026-0017', 'ENVIADA', 'Empaques para temporada alta', NOW() + INTERVAL '4 days', 1),
  
  -- Recibidas parcial
  (6, 'OC-2026-0018', 'RECIBIDA_PARCIAL', 'Huevos frescos - primera entrega', NOW() - INTERVAL '2 days', 1),
  
  -- Recibidas completas (estas no tienen movimientos para no duplicar stock)
  (7, 'OC-2026-0019', 'RECIBIDA', 'Frutos secos - recibido completo', NOW() - INTERVAL '5 days', 1),
  
  -- Canceladas
  (4, 'OC-2026-0020', 'CANCELADA', 'Pedido cancelado por cambio de proveedor', NOW() + INTERVAL '10 days', 1),
  
  -- Ordenes adicionales
  (1, 'OC-2026-0021', 'BORRADOR', 'Stock de seguridad harina', NOW() + INTERVAL '14 days', 1),
  (8, 'OC-2026-0022', 'ENVIADA', 'Decoraciones para eventos', NOW() + INTERVAL '2 days', 1),
  (2, 'OC-2026-0023', 'RECIBIDA_PARCIAL', 'Lacteos - pendiente crema', NOW() - INTERVAL '1 day', 1);

-- Detalles de ordenes de compra
INSERT INTO orden_compra_detalle (orden_compra_id, item_tipo, item_id, cantidad, precio_unitario, cantidad_recibida) VALUES
  -- OC-2026-0014 (BORRADOR) - Distribuidora El Norte
  (1, 'INGREDIENTE', 1, 10000, 1.15, 0),
  (1, 'INGREDIENTE', 2, 5000, 1.30, 0),
  (1, 'INGREDIENTE', 3, 8000, 1.05, 0),
  (1, 'INGREDIENTE', 4, 3000, 2.40, 0),
  
  -- OC-2026-0015 (BORRADOR) - Lacteos RioCasa
  (2, 'INGREDIENTE', 6, 4000, 4.30, 0),
  (2, 'INGREDIENTE', 8, 2500, 1.70, 0),
  (2, 'INGREDIENTE', 10, 500, 2.15, 0),
  
  -- OC-2026-0016 (ENVIADA) - Sabores Premium
  (3, 'INGREDIENTE', 12, 4000, 5.50, 0),
  (3, 'INGREDIENTE', 13, 2000, 5.90, 0),
  (3, 'INGREDIENTE', 15, 100, 7.80, 0),
  
  -- OC-2026-0017 (ENVIADA) - Papeles y Empaques
  (4, 'INSUMO', 6, 200, 0.75, 0),
  (4, 'INSUMO', 7, 150, 0.95, 0),
  (4, 'INSUMO', 8, 100, 1.15, 0),
  (4, 'INSUMO', 2, 300, 0.42, 0),
  
  -- OC-2026-0018 (RECIBIDA_PARCIAL) - Huevos San Mateo
  (5, 'INGREDIENTE', 11, 400, 0.48, 250),
  
  -- OC-2026-0019 (RECIBIDA) - Frutos Secos Andinos
  (6, 'INGREDIENTE', 17, 2000, 3.40, 2000),
  (6, 'INGREDIENTE', 18, 1000, 8.20, 1000),
  
  -- OC-2026-0020 (CANCELADA) - Mayorista del Litoral
  (7, 'INGREDIENTE', 7, 2000, 2.65, 0),
  (7, 'INGREDIENTE', 19, 1500, 1.35, 0),
  
  -- OC-2026-0021 (BORRADOR) - Distribuidora El Norte
  (8, 'INGREDIENTE', 1, 5000, 1.15, 0),
  (8, 'INGREDIENTE', 5, 1500, 1.70, 0),
  
  -- OC-2026-0022 (ENVIADA) - Dulces Express
  (9, 'INSUMO', 12, 200, 0.18, 0),
  (9, 'INSUMO', 13, 100, 0.85, 0),
  (9, 'INSUMO', 15, 80, 1.70, 0),
  
  -- OC-2026-0023 (RECIBIDA_PARCIAL) - Lacteos RioCasa
  (10, 'INGREDIENTE', 6, 2000, 4.30, 2000),
  (10, 'INGREDIENTE', 9, 800, 3.10, 800),
  (10, 'INGREDIENTE', 10, 300, 2.15, 0);

-- =============================================================================
-- SECCION 7: RECETAS (5 recetas para productos existentes)
-- Productos: 1=Torta chocolate, 2=Torta 3 leches, 3=Brownie, 6=Cupcake vainilla
--            7=Cupcake chocolate, 4=Galleta decorada
-- =============================================================================

INSERT INTO receta (producto_id, nombre, rendimiento_base, observaciones, es_activa) VALUES
  (1, 'Torta de Chocolate Mediana - Receta Base', 1, 'Receta para torta de chocolate mediana (20cm). Rendimiento: 1 torta.', TRUE),
  (2, 'Torta Tres Leches Pequena - Receta Base', 1, 'Receta para torta tres leches pequena (15cm). Rendimiento: 1 torta.', TRUE),
  (3, 'Brownie Individual - Receta Base', 12, 'Receta para brownie estilo americano. Rendimiento: 12 brownies individuales.', TRUE),
  (6, 'Cupcake de Vainilla - Receta Base', 12, 'Receta para cupcakes de vainilla. Rendimiento: 12 unidades.', TRUE),
  (7, 'Cupcake de Chocolate - Receta Base', 12, 'Receta para cupcakes de chocolate. Rendimiento: 12 unidades.', TRUE);

-- =============================================================================
-- SECCION 8: DETALLES DE RECETA
-- =============================================================================

-- Receta 1: Torta de Chocolate Mediana
INSERT INTO detalle_receta (receta_id, ingrediente_id, cantidad_base, observaciones) VALUES
  (1, 1, 500.0000, 'Harina 0000 cernida'),
  (1, 3, 400.0000, 'Azucar blanca refinada'),
  (1, 11, 6.0000, 'Huevos frescos (6 unidades)'),
  (1, 6, 250.0000, 'Mantequilla a temperatura ambiente'),
  (1, 8, 200.0000, 'Leche entera (200ml)'),
  (1, 15, 10.0000, 'Esencia de vainilla (10ml)'),
  (1, 12, 200.0000, 'Chocolate cobertura negro derretido'),
  (1, 14, 50.0000, 'Cacao en polvo para intensificar sabor'),
  (1, 19, 30.0000, 'Fecula de maiz para textura'),
  (1, 20, 5.0000, 'Sal refinada'),
  (1, 10, 100.0000, 'Crema de leche para ganache (100ml)');

-- Receta 2: Torta Tres Leches Pequena
INSERT INTO detalle_receta (receta_id, ingrediente_id, cantidad_base, observaciones) VALUES
  (2, 1, 350.0000, 'Harina 0000 cernida'),
  (2, 3, 300.0000, 'Azucar blanca refinada'),
  (2, 11, 4.0000, 'Huevos frescos (4 unidades)'),
  (2, 6, 150.0000, 'Mantequilla suavizada'),
  (2, 15, 8.0000, 'Esencia de vainilla (8ml)'),
  (2, 19, 20.0000, 'Fecula de maiz'),
  (2, 20, 3.0000, 'Sal refinada'),
  -- Mezcla de tres leches (se registra como leche condensada y crema principalmente)
  (2, 9, 1.0000, 'Leche condensada (1 lata de 397g)'),
  (2, 10, 250.0000, 'Crema de leche para mezcla (250ml)'),
  (2, 8, 0.5000, 'Leche entera para completar (500ml)');

-- Receta 3: Brownie Individual (rendimiento 12 unidades)
INSERT INTO detalle_receta (receta_id, ingrediente_id, cantidad_base, observaciones) VALUES
  (3, 1, 200.0000, 'Harina 0000'),
  (3, 3, 350.0000, 'Azucar blanca'),
  (3, 11, 4.0000, 'Huevos (4 unidades)'),
  (3, 6, 200.0000, 'Mantequilla derretida'),
  (3, 12, 300.0000, 'Chocolate cobertura negro'),
  (3, 14, 30.0000, 'Cacao en polvo'),
  (3, 15, 5.0000, 'Esencia de vainilla'),
  (3, 18, 100.0000, 'Nueces picadas (opcional)');

-- Receta 4: Cupcake de Vainilla (rendimiento 12 unidades)
INSERT INTO detalle_receta (receta_id, ingrediente_id, cantidad_base, observaciones) VALUES
  (4, 1, 250.0000, 'Harina 0000 cernida'),
  (4, 3, 220.0000, 'Azucar blanca'),
  (4, 11, 3.0000, 'Huevos (3 unidades)'),
  (4, 6, 120.0000, 'Mantequilla a temperatura ambiente'),
  (4, 8, 120.0000, 'Leche entera (120ml)'),
  (4, 15, 10.0000, 'Esencia de vainilla'),
  (4, 2, 10.0000, 'Polvo de hornear (incluido en harina leudante)'),
  (4, 20, 2.0000, 'Sal refinada'),
  -- Cobertura
  (4, 4, 300.0000, 'Azucar impalpable para glase'),
  (4, 6, 100.0000, 'Mantequilla para cobertura'),
  (4, 10, 30.0000, 'Crema de leche para cobertura');

-- Receta 5: Cupcake de Chocolate (rendimiento 12 unidades)
INSERT INTO detalle_receta (receta_id, ingrediente_id, cantidad_base, observaciones) VALUES
  (5, 1, 220.0000, 'Harina 0000'),
  (5, 3, 250.0000, 'Azucar blanca'),
  (5, 11, 3.0000, 'Huevos (3 unidades)'),
  (5, 6, 110.0000, 'Mantequilla'),
  (5, 8, 100.0000, 'Leche entera'),
  (5, 12, 80.0000, 'Chocolate cobertura derretido'),
  (5, 14, 40.0000, 'Cacao en polvo'),
  (5, 15, 8.0000, 'Esencia de vainilla'),
  (5, 20, 2.0000, 'Sal'),
  -- Cobertura chocolate
  (5, 12, 150.0000, 'Chocolate cobertura para ganache'),
  (5, 10, 100.0000, 'Crema de leche para ganache');

-- =============================================================================
-- SECCION 9: ACTUALIZAR COSTOS ESTIMADOS DE RECETAS
-- =============================================================================

UPDATE receta SET costo_estimado = (
  SELECT SUM(dr.cantidad_base * i.costo_referencial)
  FROM detalle_receta dr
  JOIN ingrediente i ON dr.ingrediente_id = i.ingrediente_id
  WHERE dr.receta_id = receta.receta_id
) WHERE costo_estimado IS NULL;

-- =============================================================================
-- SECCION 10: ORDENES DE PRODUCCION
-- Vinculadas a producciones existentes de 03_seed_enterprise.sql
-- produccion_id: 1=PED-0101(PENDIENTE), 2=PED-0102(EN_PROCESO), 3=PED-0103(FINALIZADO)
--               4=PED-0104(PENDIENTE), 5=PED-0105(FINALIZADO), 6=PED-0107(PENDIENTE)
-- =============================================================================

INSERT INTO orden_produccion (produccion_id, codigo, estado, observaciones, fecha_creacion, fecha_inicio, fecha_finalizacion, responsable_id) VALUES
  -- Produccion PED-0101: Torta de vainilla y frutos + 6 cupcakes
  (1, 'OP-2026-0001', 'PENDIENTE', 'Orden para PED-0101 - Torta de vainilla y cupcakes', NOW() - INTERVAL '2 days', NULL, NULL, 3),
  
  -- Produccion PED-0102: Mesa dulce 25 personas (URGENTE - EN PROCESO)
  (2, 'OP-2026-0002', 'EN_PREPARACION', 'Orden para PED-0102 - Mesa dulce corporativa urgente', NOW() - INTERVAL '3 days', NOW() - INTERVAL '2 hour', NULL, 3),
  
  -- Produccion PED-0103: Cheesecake + cupcakes (FINALIZADO)
  (3, 'OP-2026-0003', 'FINALIZADA', 'Orden para PED-0103 - Cheesecake y cupcakes completada', NOW() - INTERVAL '5 days', NOW() - INTERVAL '4 days', NOW() - INTERVAL '3 hour', 3),
  
  -- Produccion PED-0104: Torta red velvet + alfajores (PENDIENTE)
  (4, 'OP-2026-0004', 'PENDIENTE', 'Orden para PED-0104 - Torta red velvet premium', NOW() - INTERVAL '1 day', NULL, NULL, 3),
  
  -- Produccion PED-0105: Brownies + cafe (FINALIZADO)
  (5, 'OP-2026-0005', 'FINALIZADA', 'Orden para PED-0105 - Brownies para oficina completada', NOW() - INTERVAL '4 days', NOW() - INTERVAL '3 days', NOW() - INTERVAL '6 hour', 3),
  
  -- Produccion PED-0107: Mesa dulce 25 personas (URGENTE)
  (6, 'OP-2026-0006', 'PENDIENTE', 'Orden para PED-0107 - Mesa dulce evento fin de semana', NOW() - INTERVAL '1 day', NULL, NULL, 3);

-- =============================================================================
-- SECCION 11: DETALLES DE ORDEN DE PRODUCCION
-- =============================================================================

-- Nota: Las recetas se vinculan donde aplica. Para productos sin receta (cheesecake, alfajores)
-- se deja receta_id NULL y se usara el flujo manual de produccion

INSERT INTO orden_produccion_detalle (orden_produccion_id, pedido_detalle_id, producto_id, receta_id, cantidad_producir, cantidad_producida, estado_detalle, observaciones) VALUES
  -- OP-2026-0001: PED-0101
  (1, 1, (SELECT producto_id FROM producto WHERE codigo = 'PROD-TORTA-VAIN-FR'), 2, 1, 0, 'PENDIENTE', 'Torta de vainilla - usar receta tres leches adaptada'),
  (1, 2, (SELECT producto_id FROM producto WHERE codigo = 'PROD-CUPCAKE-VAIN'), 4, 6, 0, 'PENDIENTE', '6 cupcakes de vainilla'),
  
  -- OP-2026-0002: PED-0102 (EN PROCESO)
  (2, 3, (SELECT producto_id FROM producto WHERE codigo = 'PROD-MESA-DULCE-25'), NULL, 1, 0, 'EN_PRODUCCION', 'Mesa dulce completa - produccion en curso'),
  (2, 4, (SELECT producto_id FROM producto WHERE codigo = 'PROD-CAFE-AMER'), NULL, 20, 20, 'FINALIZADO', 'Cafes preparados'),
  (2, 5, (SELECT producto_id FROM producto WHERE codigo = 'PROD-BROWNIE-IND'), 3, 4, 2, 'EN_PRODUCCION', '4 brownies - 2 listos, 2 en horno'),
  
  -- OP-2026-0003: PED-0103 (FINALIZADO)
  (3, 6, (SELECT producto_id FROM producto WHERE codigo = 'PROD-CHEESECAKE-FR'), NULL, 1, 1, 'FINALIZADO', 'Cheesecake completado'),
  (3, 7, (SELECT producto_id FROM producto WHERE codigo = 'PROD-CUPCAKE-CHOCO'), 5, 12, 12, 'FINALIZADO', '12 cupcakes de chocolate listos'),
  
  -- OP-2026-0004: PED-0104 (PENDIENTE)
  (4, 8, (SELECT producto_id FROM producto WHERE codigo = 'PROD-TORTA-RV-G'), NULL, 1, 0, 'PENDIENTE', 'Torta red velvet grande - pendiente inicio'),
  (4, 9, (SELECT producto_id FROM producto WHERE codigo = 'PROD-ALFAJOR-PREM'), NULL, 10, 0, 'PENDIENTE', '10 alfajores premium'),
  
  -- OP-2026-0005: PED-0105 (FINALIZADO)
  (5, 10, (SELECT producto_id FROM producto WHERE codigo = 'PROD-BROWNIE-IND'), 3, 12, 12, 'FINALIZADO', '12 brownies entregados'),
  (5, 11, (SELECT producto_id FROM producto WHERE codigo = 'PROD-CAFE-AMER'), NULL, 10, 10, 'FINALIZADO', '10 cafes entregados'),
  
  -- OP-2026-0006: PED-0107 (PENDIENTE - URGENTE)
  (6, 12, (SELECT producto_id FROM producto WHERE codigo = 'PROD-MESA-DULCE-25'), NULL, 1, 0, 'PENDIENTE', 'Mesa dulce evento fin de semana - reservado');

-- =============================================================================
-- SECCION 12: CONSUMO DE PRODUCCION (Datos realistas)
-- Solo para ordenes finalizadas o en produccion
-- =============================================================================

-- Consumos de OP-2026-0002 (EN PROPARACION)
INSERT INTO consumo_produccion (orden_produccion_detalle_id, item_tipo, item_id, cantidad_consumida, cantidad_merma, observaciones) VALUES
  -- Consumo para brownies en produccion
  (3, 'INGREDIENTE', 1, 200.00, 10.00, 'Harina para 4 brownies'),
  (3, 'INGREDIENTE', 3, 350.00, 5.00, 'Azucar para brownies'),
  (3, 'INGREDIENTE', 11, 1.50, 0.00, 'Huevos (1.5 unidades proporcional)'),
  (3, 'INGREDIENTE', 6, 200.00, 5.00, 'Mantequilla'),
  (3, 'INGREDIENTE', 12, 300.00, 0.00, 'Chocolate'),
  (3, 'INSUMO', 6, 2.00, 0.00, 'Cajas para brownies'),
  
  -- Cafes ya preparados (no usan ingredientes del abastecimiento directamente)
  (4, 'INGREDIENTE', 9, 0.100, 0.00, 'Leche condensada para cafes');

-- Consumos de OP-2026-0003 (FINALIZADO)
INSERT INTO consumo_produccion (orden_produccion_detalle_id, item_tipo, item_id, cantidad_consumida, cantidad_merma, observaciones) VALUES
  -- Cupcakes de chocolate (12 unidades)
  (6, 'INGREDIENTE', 1, 220.00, 15.00, 'Harina para cupcakes'),
  (6, 'INGREDIENTE', 3, 250.00, 8.00, 'Azucar'),
  (6, 'INGREDIENTE', 11, 3.00, 0.00, 'Huevos'),
  (6, 'INGREDIENTE', 6, 110.00, 5.00, 'Mantequilla masa'),
  (6, 'INGREDIENTE', 6, 100.00, 0.00, 'Mantequilla cobertura'),
  (6, 'INGREDIENTE', 12, 230.00, 10.00, 'Chocolate total'),
  (6, 'INGREDIENTE', 14, 40.00, 0.00, 'Cacao'),
  (6, 'INGREDIENTE', 10, 130.00, 5.00, 'Crema de leche'),
  (6, 'INSUMO', 9, 2.00, 0.00, 'Cajas cupcake'),
  
  -- Cheesecake (ingredientes especificos no en tabla, se marcan como genericos)
  (5, 'INGREDIENTE', 10, 500.00, 20.00, 'Crema de leche para cheesecake'),
  (5, 'INGREDIENTE', 9, 2.00, 0.00, 'Leche condensada (2 latas)'),
  (5, 'INGREDIENTE', 11, 3.00, 0.00, 'Huevos para cheesecake'),
  (5, 'INSUMO', 2, 1.00, 0.00, 'Molde 20cm');

-- Consumos de OP-2026-0005 (FINALIZADO)
INSERT INTO consumo_produccion (orden_produccion_detalle_id, item_tipo, item_id, cantidad_consumida, cantidad_merma, observaciones) VALUES
  -- Brownies (12 unidades)
  (9, 'INGREDIENTE', 1, 600.00, 30.00, 'Harina para 12 brownies'),
  (9, 'INGREDIENTE', 3, 1050.00, 20.00, 'Azucar'),
  (9, 'INGREDIENTE', 11, 4.00, 0.00, 'Huevos'),
  (9, 'INGREDIENTE', 6, 600.00, 15.00, 'Mantequilla'),
  (9, 'INGREDIENTE', 12, 900.00, 25.00, 'Chocolate'),
  (9, 'INGREDIENTE', 18, 300.00, 10.00, 'Nueces'),
  (9, 'INSUMO', 6, 6.00, 0.00, 'Cajas individuales'),
  
  -- Cafes (uso de leche condensada)
  (10, 'INGREDIENTE', 9, 0.200, 0.00, 'Leche condensada para cafes');

-- =============================================================================
-- SECCION 13: INGREDIENTES CON STOCK CRITICO (para alertas)
-- Estos ingredientes quedan por debajo del minimo para probar alertas
-- =============================================================================

-- Actualizar algunos ingredientes para tener stock critico
UPDATE ingrediente SET stock_actual = 100 WHERE ingrediente_id = 15;  -- Esencia vainilla (min 50, ahora 100 - no critico)
UPDATE ingrediente SET stock_actual = 300 WHERE ingrediente_id = 16;  -- Esencia almendra (min 30, ahora 300 - no critico)
UPDATE ingrediente SET stock_actual = 400 WHERE ingrediente_id = 13;  -- Chocolate blanco (min 1000 - CRITICO!)
UPDATE ingrediente SET stock_actual = 150 WHERE ingrediente_id = 14;  -- Cacao (min 500 - CRITICO!)

-- Insertar movimientos de salida adicionales para crear stocks criticos
INSERT INTO inventario_movimiento (item_tipo, item_id, tipo_movimiento, cantidad, saldo_posterior, referencia_tipo, referencia_id, observaciones, fecha_movimiento) VALUES
  ('INGREDIENTE', 13, 'SALIDA_PRODUCCION', 2600, 400, 'PRODUCCION', 'PROD-004', 'Consumo excesivo chocolate blanco', NOW() - INTERVAL '1 day'),
  ('INGREDIENTE', 14, 'SALIDA_PRODUCCION', 1850, 150, 'PRODUCCION', 'PROD-004', 'Consumo cacao en polvo', NOW() - INTERVAL '1 day'),
  ('INGREDIENTE', 4, 'SALIDA_PRODUCCION', 2850, 150, 'PRODUCCION', 'PROD-005', 'Consumo azucar impalpable decoracion', NOW() - INTERVAL '2 days');

-- Actualizar stock_actual de ingredientes críticos
UPDATE ingrediente SET stock_actual = 400 WHERE ingrediente_id = 13;  -- Chocolate blanco CRITICO
UPDATE ingrediente SET stock_actual = 150 WHERE ingrediente_id = 14;  -- Cacao CRITICO
UPDATE ingrediente SET stock_actual = 150 WHERE ingrediente_id = 4;   -- Azucar impalpable CRITICO

COMMIT;

-- =============================================================================
-- RESUMEN DE DATOS CREADOS
-- =============================================================================
-- Ingredientes: 20
-- Insumos: 15
-- Proveedores: 8
-- Item-Proveedor relaciones: 35
-- Movimientos inventario: 33
-- Ordenes compra: 10
-- Detalles OC: 25
-- Recetas: 5
-- Detalles receta: 45
-- Ordenes produccion: 6
-- Detalles OP: 12
-- Consumos produccion: 22
-- =============================================================================
