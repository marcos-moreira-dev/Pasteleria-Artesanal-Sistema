-- =============================================================================
-- PASTELERÍA - SCRIPT MAESTRO COMPLETO (Schema + Seeds)
-- Versión: 3.0 Documentado para Estudio
-- Fecha: 2026-03-20
-- Autor: Pastelería Development Team
-- 
-- PROPÓSITO:
-- Este archivo es un recurso educativo completo que demuestra el diseño de
-- una base de datos relacional para un sistema de gestión de pastelería.
-- Incluye desde la estructura del esquema hasta datos de ejemplo reales.
--
-- ESTRUCTURA DEL ARCHIVO:
-- 1. Limpieza de tablas existentes (para regeneración limpia)
-- 2. Creación del esquema (27 tablas organizadas por módulos)
-- 3. Inserción de datos (seeds con datos reales de ejemplo)
-- 4. Verificación final
--
-- TECNOLOGÍA:
-- - PostgreSQL 17
-- - JSONB para almacenamiento de recetas estructuradas
-- - Constraints y foreign keys para integridad referencial
-- - Índices para optimización de consultas
-- =============================================================================

-- =============================================================================
-- SECCIÓN 0: CONFIGURACIÓN INICIAL
-- =============================================================================
-- Comenzamos una transacción para garantizar atomicidad.
-- Si algo falla, toda la operación se revierte automáticamente.
BEGIN;

-- =============================================================================
-- SECCIÓN 1: LIMPIEZA DE TABLAS EXISTENTES
-- =============================================================================
-- Por qué DROP CASCADE: Elimina tablas y todas sus dependencias (foreign keys,
-- índices, constraints) en el orden correcto sin errores de dependencia.
-- 
-- ORDEN IMPORTANTE: Debemos eliminar primero las tablas "hijas" (las que tienen
-- foreign keys) antes que las "padres" (las que son referenciadas).

-- Módulo de Producción (tablas más dependientes)
DROP TABLE IF EXISTS consumo_produccion CASCADE;
DROP TABLE IF EXISTS orden_produccion_detalle CASCADE;
DROP TABLE IF EXISTS orden_produccion CASCADE;

-- Módulo de Recetas
DROP TABLE IF EXISTS detalle_receta CASCADE;
DROP TABLE IF EXISTS receta CASCADE;

-- Módulo de Abastecimiento
DROP TABLE IF EXISTS inventario_movimiento CASCADE;
DROP TABLE IF EXISTS orden_compra_detalle CASCADE;
DROP TABLE IF EXISTS orden_compra CASCADE;
DROP TABLE IF EXISTS item_proveedor CASCADE;
DROP TABLE IF EXISTS proveedor CASCADE;
DROP TABLE IF EXISTS insumo CASCADE;
DROP TABLE IF EXISTS ingrediente CASCADE;
DROP TABLE IF EXISTS umedida CASCADE;

-- Módulo de Producción (tabla principal)
DROP TABLE IF EXISTS produccion CASCADE;

-- Módulo de Pedidos
DROP TABLE IF EXISTS pedido_detalle CASCADE;
DROP TABLE IF EXISTS pedido CASCADE;

-- Módulo de Cotizaciones
DROP TABLE IF EXISTS cotizacion_detalle CASCADE;
DROP TABLE IF EXISTS cotizacion CASCADE;

-- Módulo de Catálogo
DROP TABLE IF EXISTS producto CASCADE;
DROP TABLE IF EXISTS categoria_producto CASCADE;

-- Módulo de Clientes
DROP TABLE IF EXISTS cliente CASCADE;

-- Módulo de Seguridad
DROP TABLE IF EXISTS usuario_sistema CASCADE;
DROP TABLE IF EXISTS rol_usuario CASCADE;

-- Tablas de Soporte/Auditoría
DROP TABLE IF EXISTS auditoria_evento CASCADE;
DROP TABLE IF EXISTS notificacion CASCADE;
DROP TABLE IF EXISTS job_reporte CASCADE;
DROP TABLE IF EXISTS archivo_recurso CASCADE;

-- =============================================================================
-- SECCIÓN 2: MÓDULO DE SEGURIDAD Y USUARIOS
-- =============================================================================
-- Este módulo implementa autenticación y autorización básica.
-- Diseño: Separación de roles (quién puede hacer qué) de usuarios (quién entra).

-- ---------------------------------------------------------------------------
-- TABLA: rol_usuario
-- ---------------------------------------------------------------------------
-- Propósito: Definir roles/permisos del sistema.
-- 
-- Roles típicos en una pastelería:
-- - ADMIN: Control total del sistema
-- - ATENCION: Gestión de clientes, pedidos, cotizaciones
-- - PRODUCCION: Gestión de órdenes de producción e inventario
--
-- Por qué no usar ENUM: Los roles pueden cambiar y crecer. Una tabla permite
-- agregar nuevos roles sin modificar código.
CREATE TABLE rol_usuario (
  -- BIGSERIAL: Auto-incremento de 64 bits. PostgreSQL crea automáticamente
  -- una secuencia y la asocia a esta columna.
  rol_id BIGSERIAL PRIMARY KEY,
  
  -- VARCHAR vs TEXT: Usamos VARCHAR cuando sabemos el tamaño máximo esperado.
  -- 40 caracteres es suficiente para códigos como 'ADMIN', 'ATENCION', etc.
  codigo VARCHAR(40) NOT NULL UNIQUE,
  
  -- Nombre legible para mostrar en la interfaz
  nombre_rol VARCHAR(80) NOT NULL UNIQUE,
  
  -- TEXT: Sin límite de tamaño. Ideal para descripciones largas.
  descripcion TEXT,
  
  -- BOOLEAN: Tipo nativo de PostgreSQL (true/false/null)
  -- DEFAULT TRUE: Nuevos roles son activos por defecto
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  
  -- TIMESTAMPTZ: Timestamp con zona horaria. Siempre usar esto en lugar de
  -- TIMESTAMP sin zona para evitar problemas de horarios.
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Comentario en la tabla (metadata para documentación)
COMMENT ON TABLE rol_usuario IS 'Roles del sistema para control de acceso basado en roles (RBAC)';

-- ---------------------------------------------------------------------------
-- TABLA: usuario_sistema
-- ---------------------------------------------------------------------------
-- Propósito: Almacenar credenciales y datos de usuarios del sistema.
--
-- Consideraciones de seguridad:
-- - password_hash: NUNCA almacenar passwords en texto plano
-- - Usamos BCrypt (hash de 255 caracteres aprox)
-- - Versión para optimistic locking (evitar updates concurrentes)
CREATE TABLE usuario_sistema (
  usuario_id BIGSERIAL PRIMARY KEY,
  
  -- Foreign Key: Referencia a la tabla de roles
  -- NOT NULL: Todo usuario DEBE tener un rol
  rol_id BIGINT NOT NULL REFERENCES rol_usuario(rol_id),
  
  -- UNIQUE: No pueden existir dos usuarios con el mismo nombre
  nombre_usuario VARCHAR(120) NOT NULL UNIQUE,
  
  -- Password hasheado con BCrypt (60-255 caracteres)
  password_hash VARCHAR(255) NOT NULL,
  
  -- Separar nombres y apellidos facilita búsquedas y saludos personalizados
  nombres VARCHAR(120) NOT NULL,
  apellidos VARCHAR(120) NOT NULL,
  
  -- NULL permitido: No todos los usuarios necesitan email
  correo VARCHAR(120),
  
  -- Control de estado: Usuarios desactivados no pueden loguearse
  -- pero mantenemos su historial en la BD
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  
  -- Auditoría: Cuándo se creó y última modificación
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  
  -- Optimistic Locking: Previene que dos usuarios editen el mismo registro
  -- simultáneamente. Se incrementa automáticamente en cada UPDATE.
  version BIGINT NOT NULL DEFAULT 0
);

COMMENT ON TABLE usuario_sistema IS 'Usuarios del sistema con autenticación y autorización';

-- =============================================================================
-- SECCIÓN 3: MÓDULO DE CATÁLOGO DE PRODUCTOS
-- =============================================================================
-- Este módulo gestiona el menú/productos disponibles para venta.
-- Separación de categorías permite organizar el catálogo (tortas, postres, etc.)

-- ---------------------------------------------------------------------------
-- TABLA: categoria_producto
-- ---------------------------------------------------------------------------
-- Ejemplos de categorías en una pastelería:
-- - Tortas (cumpleaños, bodas, especiales)
-- - Postres (individuales, para compartir)
-- - Galletas (artesanales, masas secas)
-- - Bebidas (cafés, jugos)
CREATE TABLE categoria_producto (
  categoria_id BIGSERIAL PRIMARY KEY,
  codigo VARCHAR(40) NOT NULL UNIQUE,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  descripcion TEXT,
  
  -- orden_visual: Permite ordenar categorías en la UI (drag & drop)
  orden_visual INTEGER NOT NULL DEFAULT 0,
  
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version BIGINT NOT NULL DEFAULT 0
);

COMMENT ON TABLE categoria_producto IS 'Categorías para organizar el catálogo de productos';

-- ---------------------------------------------------------------------------
-- TABLA: producto
-- ---------------------------------------------------------------------------
-- PRODUCTO ESTRELLA: Esta tabla demuestra varios conceptos importantes:
--
-- 1. SLUG: Identificador amigable para URLs (ej: "torta-chocolate-clasica")
-- 2. JSONB: Almacenamiento flexible para la receta estructurada
-- 3. Constraints: Validación a nivel de base de datos
-- 4. Flags de estado: active (puede venderse) vs published (visible al público)
CREATE TABLE producto (
  producto_id BIGSERIAL PRIMARY KEY,
  
  -- Relación con categoría
  categoria_id BIGINT NOT NULL REFERENCES categoria_producto(categoria_id),
  
  -- SKU: Código único de inventario (ej: "TORTA-001", "GALLETA-015")
  codigo VARCHAR(50) NOT NULL UNIQUE,
  
  -- SLUG: Identificador para URLs amigables
  -- Ejemplo: "Torta de Chocolate" → "torta-de-chocolate"
  -- Importante: Usado para naming de imágenes (torta-de-chocolate.jpg)
  slug VARCHAR(160) NOT NULL UNIQUE,
  
  nombre VARCHAR(150) NOT NULL,
  descripcion TEXT,
  
  -- ============================================================
  -- CAMPO ESTRELLA: receta_json
  -- ============================================================
  -- Tipo: JSONB (JSON Binary)
  -- 
  -- ¿Por qué JSONB y no JSON?
  -- - JSONB es almacenado en formato binario (más rápido)
  -- - Soporta índices (GIN indexes)
  -- - Permite operaciones de búsqueda dentro del JSON
  -- - Valida automáticamente que sea JSON válido
  --
  -- Estructura esperada:
  -- {
  --   "titulo": "Torta de Chocolate Especial",
  --   "tituloIngredientes": "Ingredientes",
  --   "ingredientes": "• 500g harina\n• 300g azúcar...",
  --   "tituloPasos": "Preparación",
  --   "pasos": "1. Precalentar horno...\n2. Mezclar...",
  --   "tituloObservaciones": "Notas",
  --   "observaciones": "• Hornear 45 minutos\n• Temp: 180°C"
  -- }
  --
  -- Ventajas de esta estructura:
  -- 1. Flexible: Puedes agregar campos sin modificar la tabla
  -- 2. Tipado débil pero estructurado: Sabes qué campos esperar
  -- 3. Ideal para contenido semi-estructurado (recetas, configuraciones)
  -- 4. PostgreSQL tiene operadores JSON (->, ->>, #>) para consultas
  receta_json JSONB,
  
  -- NUMERIC(10, 2): Número decimal con precisión exacta
  -- 10 dígitos totales, 2 decimales (ej: 99999999.99)
  -- Ideal para dinero (evita errores de punto flotante)
  precio_base NUMERIC(10, 2) NOT NULL,
  
  -- Productos que requieren cotización previa (ej: tortas personalizadas)
  requiere_cotizacion BOOLEAN NOT NULL DEFAULT FALSE,
  
  -- Dos flags de estado:
  -- active: Si el producto está disponible para venta (lógica de negocio)
  -- published: Si aparece en el catálogo público (marketing)
  -- Ejemplo: Un producto puede estar activo (se vende) pero no publicado
  -- porque es temporada baja.
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  publicado BOOLEAN NOT NULL DEFAULT TRUE,
  
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version BIGINT NOT NULL DEFAULT 0,
  
  -- CONSTRAINT: Regla de validación a nivel de base de datos
  -- CHECK: Asegura que el precio sea >= 0
  CONSTRAINT ck_producto_precio_base CHECK (precio_base >= 0)
);

COMMENT ON TABLE producto IS 'Catálogo de productos disponibles para venta';
COMMENT ON COLUMN producto.slug IS 'Identificador URL-friendly usado también para naming de imágenes';
COMMENT ON COLUMN producto.receta_json IS 'Receta estructurada en JSON con título, ingredientes, pasos y observaciones';

-- =============================================================================
-- SECCIÓN 4: MÓDULO DE CLIENTES
-- =============================================================================
-- Gestión básica de clientes para CRM y seguimiento de pedidos.
-- Diseño simple pero extensible (puede agregarse dirección, historial, etc.)

CREATE TABLE cliente (
  cliente_id BIGSERIAL PRIMARY KEY,
  nombre_completo VARCHAR(160) NOT NULL,
  telefono VARCHAR(30),
  correo VARCHAR(120),
  observaciones TEXT,  -- Notas libres (ej: "Alergia a frutos secos", "Cliente VIP")
  fecha_registro TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version BIGINT NOT NULL DEFAULT 0
);

COMMENT ON TABLE cliente IS 'Clientes registrados del sistema';

-- =============================================================================
-- SECCIÓN 5: MÓDULO DE COTIZACIONES
-- =============================================================================
-- Una cotización es una propuesta de precio para un cliente.
-- Puede convertirse en pedido o ser rechazada.
-- Flujo: PENDIENTE → (APROBADA → CONVERTIDA) o RECHAZADA

CREATE TABLE cotizacion (
  cotizacion_id BIGSERIAL PRIMARY KEY,
  cliente_id BIGINT NOT NULL REFERENCES cliente(cliente_id),
  codigo VARCHAR(50) NOT NULL UNIQUE,  -- Ej: "COT-2026-0001"
  fecha_cotizacion TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  
  -- Estados posibles con constraint CHECK
  estado_cotizacion VARCHAR(40) NOT NULL,
  origen VARCHAR(20) NOT NULL DEFAULT 'PUBLICO',  -- PUBLICO (web) o INTERNO (mostrador)
  observaciones TEXT,
  total_estimado NUMERIC(10, 2),
  
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version BIGINT NOT NULL DEFAULT 0,
  
  -- Múltiples constraints CHECK en una sola tabla
  CONSTRAINT ck_cotizacion_estado CHECK (
    estado_cotizacion IN ('PENDIENTE', 'APROBADA', 'RECHAZADA', 'CONVERTIDA')
  ),
  CONSTRAINT ck_cotizacion_origen CHECK (origen IN ('PUBLICO', 'INTERNO')),
  CONSTRAINT ck_cotizacion_total_estimado CHECK (total_estimado IS NULL OR total_estimado >= 0)
);

-- Tabla hija: Detalles de la cotización (items incluidos)
CREATE TABLE cotizacion_detalle (
  cotizacion_detalle_id BIGSERIAL PRIMARY KEY,
  cotizacion_id BIGINT NOT NULL REFERENCES cotizacion(cotizacion_id) ON DELETE CASCADE,
  producto_id BIGINT REFERENCES producto(producto_id),  -- NULL si es ítem personalizado
  descripcion_item TEXT NOT NULL,  -- Descripción del producto o ítem personalizado
  cantidad INTEGER NOT NULL,
  precio_estimado NUMERIC(10, 2) NOT NULL,
  subtotal NUMERIC(10, 2) NOT NULL,  -- Redundancia controlada para performance
  notas TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  
  CONSTRAINT ck_cotizacion_detalle_cantidad CHECK (cantidad > 0),
  CONSTRAINT ck_cotizacion_detalle_precio CHECK (precio_estimado >= 0),
  CONSTRAINT ck_cotizacion_detalle_subtotal CHECK (subtotal >= 0)
);

-- =============================================================================
-- SECCIÓN 6: MÓDULO DE PEDIDOS
-- =============================================================================
-- Un pedido es un compromiso formal de compra.
-- Relación con cotización: Un pedido puede venir de una cotización aprobada
-- o ser creado directamente.

CREATE TABLE pedido (
  pedido_id BIGSERIAL PRIMARY KEY,
  cliente_id BIGINT NOT NULL REFERENCES cliente(cliente_id),
  cotizacion_id BIGINT UNIQUE REFERENCES cotizacion(cotizacion_id),  -- UNIQUE: 1 pedido por cotización
  codigo VARCHAR(50) NOT NULL UNIQUE,  -- Ej: "PED-2026-0001"
  fecha_pedido TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  fecha_entrega_estimada TIMESTAMPTZ NOT NULL,
  fecha_entrega_real TIMESTAMPTZ,  -- NULL hasta que se entrega
  estado_pedido VARCHAR(40) NOT NULL,
  prioridad VARCHAR(20) NOT NULL DEFAULT 'NORMAL',  -- NORMAL o URGENTE
  origen VARCHAR(20) NOT NULL DEFAULT 'INTERNO',
  observaciones TEXT,
  total_estimado NUMERIC(10, 2),
  
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version BIGINT NOT NULL DEFAULT 0,
  
  CONSTRAINT ck_pedido_estado CHECK (
    estado_pedido IN ('REGISTRADO', 'EN_PREPARACION', 'LISTO', 'ENTREGADO', 'CANCELADO')
  ),
  CONSTRAINT ck_pedido_prioridad CHECK (prioridad IN ('NORMAL', 'URGENTE')),
  CONSTRAINT ck_pedido_origen CHECK (origen IN ('PUBLICO', 'INTERNO')),
  CONSTRAINT ck_pedido_total CHECK (total_estimado IS NULL OR total_estimado >= 0),
  CONSTRAINT ck_pedido_fechas CHECK (fecha_entrega_estimada >= fecha_pedido)
);

CREATE TABLE pedido_detalle (
  pedido_detalle_id BIGSERIAL PRIMARY KEY,
  pedido_id BIGINT NOT NULL REFERENCES pedido(pedido_id) ON DELETE CASCADE,
  producto_id BIGINT NOT NULL REFERENCES producto(producto_id),
  descripcion_item TEXT NOT NULL,
  cantidad INTEGER NOT NULL,
  precio_unitario NUMERIC(10, 2) NOT NULL,
  subtotal NUMERIC(10, 2) NOT NULL,
  notas TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  
  CONSTRAINT ck_pedido_detalle_cantidad CHECK (cantidad > 0),
  CONSTRAINT ck_pedido_detalle_precio CHECK (precio_unitario >= 0),
  CONSTRAINT ck_pedido_detalle_subtotal CHECK (subtotal >= 0)
);

-- =============================================================================
-- SECCIÓN 7: MÓDULO DE PRODUCCIÓN
-- =============================================================================
-- Gestión del flujo de trabajo en la cocina.
-- Producción representa el trabajo necesario para cumplir un pedido.

CREATE TABLE produccion (
  produccion_id BIGSERIAL PRIMARY KEY,
  pedido_id BIGINT NOT NULL UNIQUE REFERENCES pedido(pedido_id),  -- 1 producción por pedido
  codigo VARCHAR(50) NOT NULL UNIQUE,  -- Ej: "PROD-2026-0001"
  fecha_inicio TIMESTAMPTZ,  -- NULL hasta que comienza
  fecha_fin TIMESTAMPTZ,     -- NULL hasta que termina
  estado_produccion VARCHAR(40) NOT NULL DEFAULT 'PENDIENTE',
  observaciones TEXT,
  
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version BIGINT NOT NULL DEFAULT 0,
  
  CONSTRAINT ck_produccion_estado CHECK (
    estado_produccion IN ('PENDIENTE', 'EN_PREPARACION', 'EN_DECORACION', 'EN_EMPAQUE', 'FINALIZADO', 'CANCELADO')
  )
);

COMMENT ON TABLE produccion IS 'Órdenes de producción para elaborar productos de pedidos';

-- =============================================================================
-- SECCIÓN 8: MÓDULO DE ABASTECIMIENTO - UNIDADES DE MEDIDA
-- =============================================================================
-- Normalización de unidades para consistencia en inventario.
-- Ejemplos: gramos (g), kilogramos (kg), mililitros (ml), litros (L), unidades (u)

CREATE TABLE umedida (
  umedida_id BIGSERIAL PRIMARY KEY,
  codigo VARCHAR(20) NOT NULL UNIQUE,      -- "g", "kg", "ml"
  nombre VARCHAR(60) NOT NULL,             -- "gramo", "kilogramo"
  abreviatura VARCHAR(10) NOT NULL,        -- "g", "kg"
  tipo VARCHAR(20) NOT NULL,               -- Categoría: PESO, VOLUMEN, UNIDAD, ENVASE
  decimales SMALLINT NOT NULL DEFAULT 0,   -- Precisión decimal (ej: 2 para 0.00)
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version BIGINT NOT NULL DEFAULT 0,
  
  -- Restricciones de dominio
  CONSTRAINT ck_umedida_tipo CHECK (
    tipo IN ('PESO', 'VOLUMEN', 'UNIDAD', 'ENVASE')
  ),
  CONSTRAINT ck_umedida_decimales CHECK (
    decimales >= 0 AND decimales <= 6
  )
);

COMMENT ON TABLE umedida IS 'Unidades de medida normalizadas para inventario';

-- =============================================================================
-- SECCIÓN 9: MÓDULO DE ABASTECIMIENTO - INVENTARIO
-- =============================================================================
-- Separación en dos tablas: INGREDIENTES (comestibles) e INSUMOS (empaques, etc.)
-- Aunque tienen estructura similar, semánticamente son diferentes.

-- INGREDIENTES: Materias primas que forman parte de la receta
-- Ejemplos: harina, azúcar, huevos, chocolate, mantequilla
CREATE TABLE ingrediente (
  ingrediente_id BIGSERIAL PRIMARY KEY,
  umedida_id BIGINT NOT NULL REFERENCES umedida(umedida_id),
  codigo VARCHAR(40) NOT NULL UNIQUE,      -- Ej: "HAR-001", "AZU-002"
  nombre VARCHAR(120) NOT NULL,
  descripcion TEXT,
  
  -- Control de stock: niveles mínimos para alertas de reabastecimiento
  stock_minimo NUMERIC(10, 3) NOT NULL DEFAULT 0,  -- 3 decimales para precisión
  stock_actual NUMERIC(10, 3) NOT NULL DEFAULT 0,
  
  -- Costo referencial para cálculos de costo de producción
  costo_referencial NUMERIC(10, 4) NOT NULL DEFAULT 0,  -- 4 decimales para precisión
  
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version BIGINT NOT NULL DEFAULT 0,
  
  CONSTRAINT ck_ingrediente_stock_minimo CHECK (stock_minimo >= 0),
  CONSTRAINT ck_ingrediente_costo CHECK (costo_referencial >= 0),
  CONSTRAINT ck_ingrediente_stock CHECK (stock_actual >= 0)
);

-- INSUMOS: Artículos de empaque y decoración (no comestibles)
-- Ejemplos: cajas, moldes, velas, toppers
CREATE TABLE insumo (
  insumo_id BIGSERIAL PRIMARY KEY,
  umedida_id BIGINT NOT NULL REFERENCES umedida(umedida_id),
  codigo VARCHAR(40) NOT NULL UNIQUE,      -- Ej: "CAJ-001", "VEL-002"
  nombre VARCHAR(120) NOT NULL,
  descripcion TEXT,
  stock_minimo NUMERIC(10, 3) NOT NULL DEFAULT 0,
  stock_actual NUMERIC(10, 3) NOT NULL DEFAULT 0,
  costo_referencial NUMERIC(10, 4) NOT NULL DEFAULT 0,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version BIGINT NOT NULL DEFAULT 0,
  
  CONSTRAINT ck_insumo_stock_minimo CHECK (stock_minimo >= 0),
  CONSTRAINT ck_insumo_costo CHECK (costo_referencial >= 0),
  CONSTRAINT ck_insumo_stock CHECK (stock_actual >= 0)
);

COMMENT ON TABLE ingrediente IS 'Ingredientes comestibles para producción';
COMMENT ON TABLE insumo IS 'Insumos de empaque y decoración (no comestibles)';

-- =============================================================================
-- SECCIÓN 10: MÓDULO DE ABASTECIMIENTO - PROVEEDORES
-- =============================================================================
-- Gestión de proveedores y sus relaciones con items de inventario.

CREATE TABLE proveedor (
  proveedor_id BIGSERIAL PRIMARY KEY,
  codigo VARCHAR(40) NOT NULL UNIQUE,      -- Ej: "PROV-001"
  nombre VARCHAR(160) NOT NULL,
  telefono VARCHAR(30),
  correo VARCHAR(120),
  direccion TEXT,
  observaciones TEXT,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version BIGINT NOT NULL DEFAULT 0
);

COMMENT ON TABLE proveedor IS 'Proveedores de ingredientes e insumos';

-- Tabla de relación muchos-a-muchos: Qué proveedor vende qué item
-- Un proveedor puede vender múltiples items
-- Un item puede ser vendido por múltiples proveedores
CREATE TABLE item_proveedor (
  item_proveedor_id BIGSERIAL PRIMARY KEY,
  item_tipo VARCHAR(20) NOT NULL,          -- 'INGREDIENTE' o 'INSUMO'
  item_id BIGINT NOT NULL,                 -- ID del ingrediente o insumo
  proveedor_id BIGINT NOT NULL REFERENCES proveedor(proveedor_id),
  precio_suministro NUMERIC(10, 4) NOT NULL,  -- Precio de este proveedor
  es_principal BOOLEAN NOT NULL DEFAULT FALSE,  -- Proveedor principal para este item
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  
  CONSTRAINT ck_item_proveedor_tipo CHECK (
    item_tipo IN ('INGREDIENTE', 'INSUMO')
  ),
  CONSTRAINT ck_item_proveedor_precio CHECK (precio_suministro >= 0)
);

COMMENT ON TABLE item_proveedor IS 'Relación proveedores-items (qué proveedor vende qué)';

-- =============================================================================
-- SECCIÓN 11: MÓDULO DE ABASTECIMIENTO - ÓRDENES DE COMPRA
-- =============================================================================
-- Gestión de compras a proveedores.
-- Flujo: BORRADOR → ENVIADA → (RECIBIDA_PARCIAL → RECIBIDA) o CANCELADA

CREATE TABLE orden_compra (
  orden_compra_id BIGSERIAL PRIMARY KEY,
  proveedor_id BIGINT NOT NULL REFERENCES proveedor(proveedor_id),
  codigo VARCHAR(50) NOT NULL UNIQUE,      -- Ej: "OC-2026-0001"
  estado VARCHAR(30) NOT NULL DEFAULT 'BORRADOR',
  observaciones TEXT,
  fecha_entrega_estimada TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by_user_id BIGINT REFERENCES usuario_sistema(usuario_id),  -- Quién creó
  version BIGINT NOT NULL DEFAULT 0,
  
  CONSTRAINT ck_orden_compra_estado CHECK (
    estado IN ('BORRADOR', 'ENVIADA', 'RECIBIDA_PARCIAL', 'RECIBIDA', 'CANCELADA')
  )
);

-- Detalles de la orden: Qué items y en qué cantidad
CREATE TABLE orden_compra_detalle (
  orden_compra_detalle_id BIGSERIAL PRIMARY KEY,
  orden_compra_id BIGINT NOT NULL REFERENCES orden_compra(orden_compra_id) ON DELETE CASCADE,
  item_tipo VARCHAR(20) NOT NULL,          -- 'INGREDIENTE' o 'INSUMO'
  item_id BIGINT NOT NULL,
  cantidad NUMERIC(10, 3) NOT NULL,
  cantidad_recibida NUMERIC(10, 3) NOT NULL DEFAULT 0,  -- Para tracking de recepción parcial
  precio_unitario NUMERIC(10, 4) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  
  CONSTRAINT ck_oc_detalle_tipo CHECK (item_tipo IN ('INGREDIENTE', 'INSUMO')),
  CONSTRAINT ck_oc_detalle_cantidad CHECK (cantidad > 0),
  CONSTRAINT ck_oc_detalle_recibida CHECK (cantidad_recibida >= 0),
  CONSTRAINT ck_oc_detalle_precio CHECK (precio_unitario >= 0)
);

-- =============================================================================
-- SECCIÓN 12: MÓDULO DE ABASTECIMIENTO - MOVIMIENTOS DE INVENTARIO
-- =============================================================================
-- Registro histórico de todas las entradas y salidas de inventario.
-- Concepto de Kardex: Cada movimiento afecta el saldo.
--
-- Tipos de movimiento:
-- - ENTRADA_COMPRA: Compra a proveedor
-- - ENTRADA_AJUSTE: Corrección de inventario (positiva)
-- - SALIDA_PRODUCCION: Consumo en producción
-- - SALIDA_MERMA: Pérdida, vencimiento, daño
-- - SALIDA_AJUSTE: Corrección de inventario (negativa)

CREATE TABLE inventario_movimiento (
  inventario_movimiento_id BIGSERIAL PRIMARY KEY,
  item_tipo VARCHAR(20) NOT NULL,          -- 'INGREDIENTE' o 'INSUMO'
  item_id BIGINT NOT NULL,
  tipo_movimiento VARCHAR(30) NOT NULL,
  cantidad NUMERIC(10, 3) NOT NULL,        -- Siempre positivo
  saldo_posterior NUMERIC(10, 3) NOT NULL, -- Saldo después del movimiento
  referencia_tipo VARCHAR(40),             -- 'ORDEN_COMPRA', 'PRODUCCION', 'AJUSTE'
  referencia_id VARCHAR(80),               -- Código de referencia
  motivo_salida VARCHAR(100),              -- Razón si es salida
  observaciones TEXT,
  fecha_movimiento TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  registrado_por_user_id BIGINT REFERENCES usuario_sistema(usuario_id),
  
  CONSTRAINT ck_inv_mov_tipo_item CHECK (
    item_tipo IN ('INGREDIENTE', 'INSUMO')
  ),
  CONSTRAINT ck_inv_mov_tipo_movimiento CHECK (
    tipo_movimiento IN ('ENTRADA_COMPRA', 'ENTRADA_AJUSTE', 'SALIDA_PRODUCCION', 'SALIDA_MERMA', 'SALIDA_AJUSTE')
  ),
  CONSTRAINT ck_inv_mov_cantidad CHECK (cantidad > 0)
);

COMMENT ON TABLE inventario_movimiento IS 'Historial de movimientos de inventario (Kardex)';

-- =============================================================================
-- SECCIÓN 13: MÓDULO DE ABASTECIMIENTO - RECETAS / FICHAS TÉCNICAS
-- =============================================================================
-- Las recetas vinculan productos del catálogo con ingredientes.
-- Una receta define cuánto de cada ingrediente se necesita.
--
-- Ejemplo: Receta "Torta de Chocolate" usa:
-- - 500g harina
-- - 300g azúcar
-- - 200g chocolate
-- etc.

CREATE TABLE receta (
  receta_id BIGSERIAL PRIMARY KEY,
  producto_id BIGINT REFERENCES producto(producto_id),  -- A qué producto corresponde
  nombre VARCHAR(160) NOT NULL,
  rendimiento_base NUMERIC(10, 2) NOT NULL DEFAULT 1,   -- Para cuántas unidades rinde
  costo_estimado NUMERIC(10, 4),                        -- Costo calculado de materiales
  observaciones TEXT,
  es_activa BOOLEAN NOT NULL DEFAULT FALSE,             -- Solo una receta activa por producto
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_by_user_id BIGINT REFERENCES usuario_sistema(usuario_id),
  version BIGINT NOT NULL DEFAULT 0,
  
  CONSTRAINT ck_receta_rendimiento CHECK (rendimiento_base > 0)
);

-- Índice parcial: Solo permite una receta activa por producto
-- Esto evita ambigüedad (¿cuál receta usar?)
CREATE UNIQUE INDEX idx_receta_unica_activa
  ON receta (producto_id)
  WHERE es_activa = TRUE;

-- Detalle de receta: Qué ingrediente y en qué cantidad
CREATE TABLE detalle_receta (
  detalle_receta_id BIGSERIAL PRIMARY KEY,
  receta_id BIGINT NOT NULL REFERENCES receta(receta_id) ON DELETE CASCADE,
  ingrediente_id BIGINT NOT NULL REFERENCES ingrediente(ingrediente_id),
  cantidad_base NUMERIC(10, 4) NOT NULL,
  rendimiento_por_unidad NUMERIC(10, 4) NOT NULL DEFAULT 1,
  es_para_porcion BOOLEAN NOT NULL DEFAULT TRUE,  -- Si aplica a porciones individuales
  observaciones TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  
  CONSTRAINT ck_detalle_receta_cantidad CHECK (cantidad_base > 0),
  CONSTRAINT ck_detalle_receta_rendimiento CHECK (rendimiento_por_unidad > 0)
);

COMMENT ON TABLE receta IS 'Fichas técnicas de producción para productos';
COMMENT ON TABLE detalle_receta IS 'Ingredientes y cantidades por receta';

-- =============================================================================
-- SECCIÓN 14: MÓDULO DE PRODUCCIÓN - ÓRDENES DE PRODUCCIÓN
-- =============================================================================
-- Cuando un pedido requiere producción, se crea una orden de producción.
-- Esto desencadena el consumo de materiales y la elaboración.

CREATE TABLE orden_produccion (
  orden_produccion_id BIGSERIAL PRIMARY KEY,
  produccion_id BIGINT NOT NULL UNIQUE REFERENCES produccion(produccion_id) ON DELETE CASCADE,
  codigo VARCHAR(50) NOT NULL UNIQUE,      -- Ej: "OP-2026-0001"
  estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
  observaciones TEXT,
  fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  fecha_inicio TIMESTAMPTZ,                -- Cuando comienza la elaboración
  fecha_finalizacion TIMESTAMPTZ,          -- Cuando termina
  responsable_id BIGINT REFERENCES usuario_sistema(usuario_id),  -- Quién supervisa
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  version BIGINT NOT NULL DEFAULT 0,
  
  CONSTRAINT ck_orden_produccion_estado CHECK (
    estado IN ('PENDIENTE', 'EN_PREPARACION', 'FINALIZADA', 'CANCELADA')
  )
);

-- Detalle de la orden: Qué productos específicos se van a producir
CREATE TABLE orden_produccion_detalle (
  orden_produccion_detalle_id BIGSERIAL PRIMARY KEY,
  orden_produccion_id BIGINT NOT NULL REFERENCES orden_produccion(orden_produccion_id) ON DELETE CASCADE,
  pedido_detalle_id BIGINT REFERENCES pedido_detalle(pedido_detalle_id),  -- Si viene de un pedido
  producto_id BIGINT NOT NULL REFERENCES producto(producto_id),
  receta_id BIGINT REFERENCES receta(receta_id),  -- Qué receta usar
  cantidad_producir INTEGER NOT NULL,
  cantidad_producida INTEGER NOT NULL DEFAULT 0,
  fecha_inicio TIMESTAMPTZ,
  fecha_fin TIMESTAMPTZ,
  estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
  observaciones TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  
  CONSTRAINT ck_op_detalle_cantidad CHECK (cantidad_producir > 0),
  CONSTRAINT ck_op_detalle_producida CHECK (cantidad_producida >= 0),
  CONSTRAINT ck_op_detalle_estado CHECK (
    estado IN ('PENDIENTE', 'EN_PREPARACION', 'FINALIZADA', 'CANCELADA')
  )
);

-- Consumo real: Qué materiales se usaron en la producción
-- Esto genera automáticamente salidas de inventario
CREATE TABLE consumo_produccion (
  consumo_produccion_id BIGSERIAL PRIMARY KEY,
  orden_produccion_detalle_id BIGINT NOT NULL REFERENCES orden_produccion_detalle(orden_produccion_detalle_id) ON DELETE CASCADE,
  item_tipo VARCHAR(20) NOT NULL,          -- 'INGREDIENTE' o 'INSUMO'
  item_id BIGINT NOT NULL,
  cantidad_consumida NUMERIC(10, 3) NOT NULL,
  costo_unitario NUMERIC(10, 4) NOT NULL,  -- Para cálculo de costo real
  fecha_consumo TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  observaciones TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  
  CONSTRAINT ck_consumo_tipo CHECK (item_tipo IN ('INGREDIENTE', 'INSUMO')),
  CONSTRAINT ck_consumo_cantidad CHECK (cantidad_consumida > 0),
  CONSTRAINT ck_consumo_costo CHECK (costo_unitario >= 0)
);

-- =============================================================================
-- SECCIÓN 15: TABLAS DE SOPORTE Y AUDITORÍA
-- =============================================================================
-- Estas tablas soportan funcionalidades transversales del sistema.

-- Archivos subidos al sistema (imágenes, PDFs, etc.)
CREATE TABLE archivo_recurso (
  archivo_id BIGSERIAL PRIMARY KEY,
  codigo_archivo VARCHAR(80) NOT NULL UNIQUE,     -- Código único del archivo
  origen_modulo VARCHAR(40) NOT NULL,             -- Módulo que generó el archivo
  tipo_archivo VARCHAR(40) NOT NULL,              -- Tipo/categoría del archivo
  nombre_original VARCHAR(255) NOT NULL,          -- Nombre que tenía al subir
  nombre_fisico VARCHAR(255) NOT NULL,            -- Nombre único en disco
  mime_type VARCHAR(120) NOT NULL,                -- image/jpeg, application/pdf, etc.
  extension VARCHAR(20),                          -- Extensión del archivo
  tamano_bytes BIGINT NOT NULL,
  checksum VARCHAR(128),                          -- Hash para verificación de integridad
  ruta_relativa VARCHAR(500) NOT NULL,            -- Ruta relativa de almacenamiento
  estado VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',   -- Estado del archivo (ACTIVO, EXPIRADO, etc.)
  fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  fecha_expiracion TIMESTAMPTZ,                   -- Fecha de expiración opcional
  creado_por_usuario_id BIGINT REFERENCES usuario_sistema(usuario_id)
);

-- Jobs de reportes asíncronos
CREATE TABLE job_reporte (
  job_id BIGSERIAL PRIMARY KEY,
  tipo_reporte VARCHAR(50) NOT NULL,
  estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
  parametros JSONB,                          -- Parámetros del reporte en JSON
  resultado_url VARCHAR(500),                -- URL donde se puede descargar
  archivo_id BIGINT REFERENCES archivo_recurso(archivo_id),
  solicitado_por_usuario_id BIGINT REFERENCES usuario_sistema(usuario_id),
  fecha_inicio TIMESTAMPTZ,
  fecha_fin TIMESTAMPTZ,
  error_mensaje TEXT,
  intentos INTEGER NOT NULL DEFAULT 0,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Sistema de notificaciones interno
CREATE TABLE notificacion (
  notificacion_id BIGSERIAL PRIMARY KEY,
  usuario_destino_id BIGINT NOT NULL REFERENCES usuario_sistema(usuario_id),
  tipo VARCHAR(40) NOT NULL,
  titulo VARCHAR(200) NOT NULL,
  mensaje TEXT NOT NULL,
  datos_contexto JSONB,                      -- Datos adicionales en JSON
  estado VARCHAR(20) NOT NULL DEFAULT 'NO_LEIDA',  -- NO_LEIDA, LEIDA, ARCHIVADA
  fecha_envio TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  fecha_lectura TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Auditoría: Log de cambios importantes
CREATE TABLE auditoria_evento (
  evento_id BIGSERIAL PRIMARY KEY,
  actor_usuario_id BIGINT REFERENCES usuario_sistema(usuario_id),
  tipo_evento VARCHAR(60) NOT NULL,          -- CREAR, ACTUALIZAR, ELIMINAR
  entidad_tipo VARCHAR(60) NOT NULL,         -- 'producto', 'pedido', etc.
  entidad_id VARCHAR(80) NOT NULL,           -- ID de la entidad afectada
  datos_anteriores JSONB,                    -- Estado antes del cambio
  datos_nuevos JSONB,                        -- Estado después del cambio
  ip_origen VARCHAR(45),                     -- IPv4 o IPv6
  user_agent TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- =============================================================================
-- SECCIÓN 16: ÍNDICES PARA OPTIMIZACIÓN
-- =============================================================================
-- Los índices aceleran las búsquedas pero ralentizan las escrituras.
-- Solo crear índices en columnas frecuentemente consultadas.

-- Índices de búsqueda por relaciones (foreign keys)
CREATE INDEX idx_producto_categoria ON producto(categoria_id);
CREATE INDEX idx_cotizacion_cliente ON cotizacion(cliente_id);
CREATE INDEX idx_pedido_cliente ON pedido(cliente_id);
CREATE INDEX idx_pedido_estado ON pedido(estado_pedido);
CREATE INDEX idx_produccion_pedido ON produccion(pedido_id);

-- Índices del módulo de abastecimiento
CREATE INDEX idx_ingrediente_umedida ON ingrediente(umedida_id);
CREATE INDEX idx_insumo_umedida ON insumo(umedida_id);
CREATE INDEX idx_item_proveedor_proveedor ON item_proveedor(proveedor_id);
CREATE INDEX idx_orden_compra_proveedor ON orden_compra(proveedor_id);
CREATE INDEX idx_orden_compra_estado ON orden_compra(estado);
CREATE INDEX idx_inventario_mov_item ON inventario_movimiento(item_tipo, item_id);
CREATE INDEX idx_inventario_mov_fecha ON inventario_movimiento(fecha_movimiento DESC);
CREATE INDEX idx_receta_producto ON receta(producto_id);
CREATE INDEX idx_detalle_receta_receta ON detalle_receta(receta_id);
CREATE INDEX idx_orden_prod_produccion ON orden_produccion(produccion_id);

-- Índices de soporte
CREATE INDEX idx_notificacion_usuario ON notificacion(usuario_destino_id, estado);

-- =============================================================================
-- SECCIÓN 17: SEEDS - DATOS INICIALES
-- =============================================================================
-- Estos datos permiten probar el sistema inmediatamente después de crear la BD.
-- Incluyen usuarios de prueba, categorías, productos de ejemplo, etc.

-- ---------------------------------------------------------------------------
-- 17.1 ROLES DE USUARIO
-- ---------------------------------------------------------------------------
-- Tres roles básicos para empezar:
-- - ADMIN: Control total
-- - ATENCION: Gestión de clientes y pedidos
-- - PRODUCCION: Control de inventario y producción

INSERT INTO rol_usuario (codigo, nombre_rol, descripcion) VALUES
  ('ADMIN', 'Administrador', 'Acceso completo al sistema. Gestión de usuarios, configuración y reportes.'),
  ('ATENCION', 'Atención al Cliente', 'Gestión de cotizaciones, pedidos y clientes.'),
  ('PRODUCCION', 'Producción', 'Gestión de órdenes de producción y consumo de materiales.');

-- ---------------------------------------------------------------------------
-- 17.2 USUARIOS DE PRUEBA
-- ---------------------------------------------------------------------------
-- Passwords hasheados con BCrypt:
-- admin123    → $2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqJFmJ6L2aJzPO9/2XgG4gU8W0kG
-- atencion123 → (mismo hash por simplicidad en desarrollo)
-- produccion123 → (mismo hash)

INSERT INTO usuario_sistema (rol_id, nombre_usuario, password_hash, nombres, apellidos, correo) VALUES
  (1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqJFmJ6L2aJzPO9/2XgG4gU8W0kG', 'Administrador', 'Sistema', 'admin@pasteleria.com'),
  (2, 'atencion1', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqJFmJ6L2aJzPO9/2XgG4gU8W0kG', 'María', 'González', 'atencion@pasteleria.com'),
  (3, 'produccion1', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqJFmJ6L2aJzPO9/2XgG4gU8W0kG', 'Carlos', 'Rodríguez', 'produccion@pasteleria.com');

-- ---------------------------------------------------------------------------
-- 17.3 CATEGORÍAS DE PRODUCTOS
-- ---------------------------------------------------------------------------
INSERT INTO categoria_producto (codigo, nombre, descripcion, orden_visual) VALUES
  ('TORTAS', 'Tortas', 'Tortas para toda ocasión: cumpleaños, bodas, aniversarios', 1),
  ('POSTRES', 'Postres', 'Postres individuales y para compartir', 2),
  ('GALLETAS', 'Galletas', 'Galletas artesanales y masas secas', 3),
  ('BEBIDAS', 'Bebidas', 'Cafés, jugos naturales y bebidas calientes', 4);

-- ---------------------------------------------------------------------------
-- 17.4 PRODUCTOS CON RECETAS JSON
-- ---------------------------------------------------------------------------
-- Aquí se demuestra el uso del campo receta_json con datos reales.
-- Cada producto incluye su receta completa en formato JSON.

INSERT INTO producto (categoria_id, codigo, slug, nombre, descripcion, receta_json, precio_base, requiere_cotizacion, activo, publicado) VALUES
  
  -- Producto 1: Torta de Chocolate
  (1, 'PROD-001', 'torta-chocolate', 'Torta de Chocolate', 
   'Deliciosa torta de chocolate con cobertura de ganache',
   '{
     "titulo": "Torta de Chocolate Especial",
     "tituloIngredientes": "Ingredientes",
     "ingredientes": "• 500g harina de trigo\n• 300g azúcar blanca\n• 200g mantequilla sin sal\n• 300g chocolate cobertura 55%\n• 4 huevos frescos\n• 10ml esencia de vainilla\n• 200ml leche entera\n• 15g polvo de hornear\n• 5g sal",
     "tituloPasos": "Preparación",
     "pasos": "1. Precalentar el horno a 180°C.\n2. Tamizar la harina con el polvo de hornear y la sal. Reservar.\n3. Derretir el chocolate a baño María junto con la mantequilla.\n4. Batir los huevos con el azúcar hasta que tripliquen su volumen.\n5. Incorporar el chocolate derretido a la mezcla de huevos.\n6. Agregar alternando la harina tamizada y la leche.\n7. Verter en molde engrasado y enharinado.\n8. Hornear por 45-50 minutos hasta que al insertar un palillo salga limpio.\n9. Dejar enfriar 10 minutos antes de desmoldar.",
     "tituloObservaciones": "Notas y Tips",
     "observaciones": "• Tiempo de horneado: 45-50 minutos según el horno\n• Temperatura: 180°C (350°F)\n• Conservación: 3 días en refrigeración, 5 días en congelador\n• Para la cobertura: usar 200g de chocolate con 100ml de crema de leche"
   }'::jsonb,
   25.00, FALSE, TRUE, TRUE),

  -- Producto 2: Torta Tres Leches
  (1, 'PROD-002', 'torta-tres-leches', 'Torta Tres Leches',
   'Esponjosa torta bañada en tres tipos de leche',
   '{
     "titulo": "Torta Tres Leches Clásica",
     "tituloIngredientes": "Ingredientes",
     "ingredientes": "• 400g harina de trigo\n• 250g azúcar blanca\n• 6 huevos frescos\n• 1L leche entera\n• 300ml leche condensada\n• 200ml crema de leche\n• 5ml esencia de vainilla\n• 15g polvo de hornear",
     "tituloPasos": "Preparación",
     "pasos": "1. Precalentar horno a 175°C.\n2. Batir huevos con azúcar hasta obtener punto letra.\n3. Incorporar harina tamizada con movimientos envolventes.\n4. Verter en molde y hornear 35 minutos.\n5. Mezclar las tres leches (entera, condensada, crema).\n6. Cuando la torta esté tibia, perforar con un tenedor.\n7. Bañar gradualmente con la mezcla de leches.\n8. Refrigerar mínimo 4 horas antes de servir.",
     "tituloObservaciones": "Notas Importantes",
     "observaciones": "• La torta debe absorber toda la mezcla de leches\n• Decorar con merengue o crema chantilly\n• Ideal preparar un día antes para mejor sabor\n• Duración: 3 días refrigerada"
   }'::jsonb,
   22.00, FALSE, TRUE, TRUE),

  -- Producto 3: Brownie
  (2, 'PROD-003', 'brownie-chocolate', 'Brownie de Chocolate',
   'Brownie húmedo con trozos de nuez',
   '{
     "titulo": "Brownies de Chocolate con Nuez",
     "tituloIngredientes": "Ingredientes",
     "ingredientes": "• 300g harina de trigo\n• 400g azúcar blanca\n• 200g chocolate cobertura\n• 150g mantequilla sin sal\n• 3 huevos frescos\n• 100g nueces picadas\n• 5g esencia de vainilla",
     "tituloPasos": "Preparación",
     "pasos": "1. Precalentar horno a 175°C.\n2. Derretir chocolate con mantequilla.\n3. Batir huevos con azúcar.\n4. Incorporar chocolate derretido.\n5. Agregar harina tamizada sin batir en exceso.\n6. Añadir nueces picadas.\n7. Verter en molde cuadrado engrasado.\n8. Hornear 25-30 minutos (el centro debe quedar húmedo).",
     "tituloObservaciones": "Tips",
     "observaciones": "• No sobrehornear - debe quedar húmedo en el centro\n• Cortar cuando esté completamente frío\n• Rinde 12 porciones cuadradas\n• Se conserva 5 días en refrigeración"
   }'::jsonb,
   3.50, FALSE, TRUE, TRUE),

  -- Producto 4: Galletas de Mantequilla
  (3, 'PROD-004', 'galletas-mantequilla', 'Galletas de Mantequilla',
   'Crocantes galletas con aroma a vainilla',
   '{
     "titulo": "Galletas de Mantequilla Artesanales",
     "tituloIngredientes": "Ingredientes",
     "ingredientes": "• 250g harina de trigo\n• 150g azúcar blanca\n• 100g mantequilla sin sal\n• 1 huevo fresco\n• 5ml esencia de vainilla\n• 2g sal",
     "tituloPasos": "Preparación",
     "pasos": "1. Batir mantequilla con azúcar hasta cremar.\n2. Agregar huevo y esencia de vainilla.\n3. Incorporar harina tamizada y sal.\n4. Formar disco, envolver en plástico.\n5. Refrigerar 30 minutos.\n6. Estirar a 5mm de grosor.\n7. Cortar con cortadores de formas.\n8. Hornear a 180°C por 12-15 minutos hasta dorar.",
     "tituloObservaciones": "Observaciones",
     "observaciones": "• La masa debe estar fría para cortar bien\n• Hornear hasta que estén doradas en los bordes\n• Rinde aproximadamente 24 galletas\n• Duración: 1 semana en recipiente hermético"
   }'::jsonb,
   2.00, FALSE, TRUE, TRUE),

  -- Producto 5: Café Especial
  (4, 'PROD-005', 'cafe-especial', 'Café Especial',
   'Café aromático de grano selecto',
   NULL,  -- Bebidas no requieren receta de preparación
   2.50, FALSE, TRUE, TRUE);

-- ---------------------------------------------------------------------------
-- 17.5 CLIENTES DE EJEMPLO
-- ---------------------------------------------------------------------------
INSERT INTO cliente (nombre_completo, telefono, correo, observaciones) VALUES
  ('Juan Pérez', '099-1234-567', 'juan.perez@email.com', 'Cliente frecuente, prefiere tortas de chocolate'),
  ('María López', '098-9876-543', 'maria.lopez@email.com', 'Siempre pide con anticipación'),
  ('Carlos Ruiz', '097-4567-890', 'carlos.ruiz@email.com', 'Paga siempre en efectivo');

-- ---------------------------------------------------------------------------
-- 17.6 UNIDADES DE MEDIDA
-- ---------------------------------------------------------------------------
INSERT INTO umedida (codigo, nombre, abreviatura, tipo, decimales) VALUES
  ('g',      'gramo',                'g',     'PESO',    2),
  ('kg',     'kilogramo',            'kg',    'PESO',    3),
  ('ml',     'mililitro',            'ml',    'VOLUMEN', 3),
  ('l',      'litro',                'L',     'VOLUMEN', 3),
  ('u',      'unidad',               'u',     'UNIDAD',  0),
  ('caja',   'caja',                 'caja',  'ENVASE',  0),
  ('bandeja','bandeja',              'band.', 'ENVASE',  0),
  ('paq',    'paquete',              'paq',   'ENVASE',  0),
  ('bolsa',  'bolsa',                'bol',   'ENVASE',  0),
  ('docena', 'docena',               'doc',   'UNIDAD',  0);

-- ---------------------------------------------------------------------------
-- 17.7 INGREDIENTES (20 ingredientes base)
-- ---------------------------------------------------------------------------
INSERT INTO ingrediente (umedida_id, codigo, nombre, descripcion, stock_minimo, stock_actual, costo_referencial) VALUES
  (1,  'HAR-001',  'Harina de trigo 0000',      'Harina refinada para repostería',              5000,  25000, 1.20),
  (1,  'HAR-002',  'Harina de trigo leudante',  'Harina con polvo de hornear incorporado',      2000,   8000, 1.35),
  (1,  'AZU-001',  'Azúcar blanca refinada',   'Azúcar fina para repostería',                  3000,  15000, 1.10),
  (1,  'AZU-002',  'Azúcar impalpable',        'Azúcar glass para decoración',                 1000,   5000, 2.50),
  (2,  'MAN-001',  'Mantequilla sin sal',      'Mantequilla 82% MG, importada',                2000,   8000, 4.50),
  (4,  'LEC-001',  'Leche entera UHT',         'Leche fresca larga vida 1L',                   1000,   5000, 1.80),
  (3,  'LEC-002',  'Leche condensada',         'Leche condensada azucarada 397g',               300,   1500, 3.20),
  (5,  'HUE-001',  'Huevos frescos',           'Huevos de gallina categoría A',                 200,    800, 0.50),
  (1,  'CHO-001',  'Chocolate cobertura negro', 'Cobertura 55% cacao',                         2000,   6000, 5.80),
  (4,  'ESC-001',  'Esencia de vainilla',      'Esencia pura de vainilla 100ml',                 50,    200, 8.00),
  (1,  'AZU-003',  'Azúcar mascabo',           'Azúcar morena para textura',                    500,   2500, 1.80),
  (1,  'MAN-002',  'Margarina vegetal',        'Margarina para masas hojaldres',               1500,   6000, 2.80),
  (3,  'CRE-001',  'Crema de leche',           'Crema para batir 250ml',                        200,   1000, 2.20),
  (1,  'CHO-002',  'Chocolate cobertura blanco', 'Cobertura blanco premium',                   1000,   3000, 6.20),
  (1,  'CAC-001',  'Cacao en polvo',           'Cacao puro en polvo',                          500,   2000, 4.50),
  (4,  'ESC-002',  'Esencia de almendra',      'Esencia de almendra 50ml',                       30,    120, 6.50),
  (1,  'COC-001',  'Coco rallado natural',     'Coco deshidratado sin azúcar',                  500,   2000, 3.50),
  (1,  'NUE-001',  'Nueces picadas',           'Nueces naturales troceadas',                    300,   1200, 8.50),
  (1,  'FEC-001',  'Fécula de maíz',           'Maicena para espesantes',                      1000,   4000, 1.40),
  (1,  'SAL-001',  'Sal refinada',             'Sal de mesa fina',                              500,   2000, 0.60);

-- ---------------------------------------------------------------------------
-- 17.8 INSUMOS (15 insumos de empaque)
-- ---------------------------------------------------------------------------
INSERT INTO insumo (umedida_id, codigo, nombre, descripcion, stock_minimo, stock_actual, costo_referencial) VALUES
  (8,  'MOL-001',  'Molde papel redondo 15cm', 'Molde desechable redondo chico',                200,    800, 0.30),
  (8,  'MOL-002',  'Molde papel redondo 20cm', 'Molde desechable redondo mediano',              150,    500, 0.45),
  (8,  'MOL-003',  'Molde papel redondo 25cm', 'Molde desechable redondo grande',               100,    400, 0.60),
  (8,  'MOL-004',  'Molde papel cuadrado 20cm', 'Molde desechable cuadrado',                    120,    480, 0.40),
  (8,  'MOL-005',  'Molde papel rectangular',   'Molde desechable rectangular 30x20',            80,    320, 0.55),
  (8,  'CAJ-001',  'Caja pastel pequeña',      'Caja cartón 20x20x10cm',                        100,    300, 0.80),
  (8,  'CAJ-002',  'Caja pastel mediana',      'Caja cartón 25x25x12cm',                         80,    240, 1.00),
  (8,  'CAJ-003',  'Caja pastel grande',       'Caja cartón 30x30x15cm',                         60,    180, 1.20),
  (8,  'CAJ-004',  'Caja cupcake 6 unidades',  'Caja especial para 6 cupcakes',                 150,    600, 0.65),
  (7,  'BAN-001',  'Bandeja aluminio',         'Bandeja aluminio desechable',                    50,    150, 1.50),
  (8,  'VEL-001',  'Velas numéricas',          'Velas números 0-9 surtidas',                     100,    400, 0.20),
  (8,  'VEL-002',  'Velas cilíndricas pack',   'Velas cilíndricas colores x12',                  50,    200, 0.90),
  (8,  'TOP-001',  'Topper feliz cumpleaños',  'Topper decorativo acrílico',                     30,    120, 1.80),
  (8,  'BOL-001',  'Bolsa kraft pequeña',      'Bolsa papel kraft 15x20cm',                     200,   1000, 0.15),
  (5,  'ESP-001',  'Espátula de plástico',     'Espátula para decorar desechable',               20,     80, 0.45);

-- ---------------------------------------------------------------------------
-- 17.9 PROVEEDORES (8 proveedores)
-- ---------------------------------------------------------------------------
INSERT INTO proveedor (codigo, nombre, telefono, correo, direccion, observaciones) VALUES
  ('PROV-001', 'Distribuidora El Norte S.A.',      '04-2100-9901', 'ventas@elnorte.com',        'Av. Carlos Julio Arosemena Km 8, Guayaquil',      'Proveedor principal de harinas y azúcares. Entrega 24-48h.'),
  ('PROV-002', 'Lácteos RioCasa Ecuador',          '03-2400-1234', 'pedidos@riocasa.com',       'Panamericana Norte Km 5, Riobamba',               'Lácteos frescos y mantequilla. Excelente calidad.'),
  ('PROV-003', 'Importadora Sabores Premium',      '04-2300-5678', 'compras@saborespremium.com', 'Cdla. Kennedy Norte, Guayaquil',                  'Chocolate, vainilla, esencias importadas.'),
  ('PROV-004', 'Mayorista del Litoral',            '04-2500-3344', 'ventas@litoralmayorista.ec', 'Urdesa Central, Guayaquil',                       'Insumos varios de respaldo. Precios competitivos.'),
  ('PROV-005', 'Papeles y Empaques del Guayas',    '04-2600-7788', 'ventas@pyeguayas.com',       'Vía a Daule Km 4.5, Guayaquil',                   'Cajas, moldes, bolsas. Especialistas en empaques.'),
  ('PROV-006', 'Huevos Frescos San Mateo',         '02-2200-4455', 'pedidos@sanmateo.com',       'Valle de los Chillos, Quito',                     'Huevos frescos diarios.'),
  ('PROV-007', 'Frutos Secos Andinos',             '03-2800-9900', 'ventas@andinossecos.com',    'Ambato, sector industrial',                       'Nueces, almendras, frutos secos.'),
  ('PROV-008', 'Decoraciones Dulces Express',      '099-1234-567', 'info@dulcesexpress.com',     'Vía Perimetral, Guayaquil',                       'Velas, toppers, decoraciones. Entrega express.');

-- ---------------------------------------------------------------------------
-- 17.10 RELACIONES PROVEEDOR-ITEM
-- ---------------------------------------------------------------------------
INSERT INTO item_proveedor (item_tipo, item_id, proveedor_id, precio_suministro, es_principal) VALUES
  -- El Norte vende harinas y azúcares
  ('INGREDIENTE', 1, 1, 1.15, TRUE),
  ('INGREDIENTE', 2, 1, 1.30, TRUE),
  ('INGREDIENTE', 3, 1, 1.05, TRUE),
  ('INGREDIENTE', 4, 1, 2.40, TRUE),
  -- RioCasa vende lácteos
  ('INGREDIENTE', 5, 2, 4.30, TRUE),
  ('INGREDIENTE', 6, 2, 1.70, TRUE),
  ('INGREDIENTE', 7, 2, 3.10, TRUE),
  -- Sabores Premium vende chocolates
  ('INGREDIENTE', 9, 3, 5.50, TRUE),
  ('INGREDIENTE', 14, 3, 5.90, TRUE),
  -- etc...
  ('INGREDIENTE', 8, 6, 0.48, TRUE);

-- ---------------------------------------------------------------------------
-- 17.11 ÓRDENES DE COMPRA DE EJEMPLO
-- ---------------------------------------------------------------------------
INSERT INTO orden_compra (proveedor_id, codigo, estado, observaciones, fecha_entrega_estimada, created_by_user_id) VALUES
  (1, 'OC-2026-001', 'BORRADOR', 'Reposición mensual harinas', NOW() + INTERVAL '7 days', 1),
  (2, 'OC-2026-002', 'ENVIADA', 'Pedido lácteos urgente', NOW() + INTERVAL '3 days', 1),
  (3, 'OC-2026-003', 'RECIBIDA', 'Chocolates temporada navidad', NOW() - INTERVAL '5 days', 1);

-- =============================================================================
-- SECCIÓN 18: VERIFICACIÓN FINAL
-- =============================================================================
-- Esta consulta muestra un resumen de todo lo creado

SELECT '============================================' as info;
SELECT '   RESUMEN DE LA BASE DE DATOS CREADA' as info;
SELECT '============================================' as info;

SELECT 
  'TABLAS CREADAS' as categoria,
  COUNT(*) as cantidad
FROM information_schema.tables 
WHERE table_schema = 'public';

SELECT 
  'ROLES' as entidad, 
  COUNT(*) as registros 
FROM rol_usuario
UNION ALL
SELECT 'USUARIOS', COUNT(*) FROM usuario_sistema
UNION ALL
SELECT 'CATEGORÍAS', COUNT(*) FROM categoria_producto
UNION ALL
SELECT 'PRODUCTOS', COUNT(*) FROM producto
UNION ALL
SELECT 'CLIENTES', COUNT(*) FROM cliente
UNION ALL
SELECT 'UMEDIDAS', COUNT(*) FROM umedida
UNION ALL
SELECT 'INGREDIENTES', COUNT(*) FROM ingrediente
UNION ALL
SELECT 'INSUMOS', COUNT(*) FROM insumo
UNION ALL
SELECT 'PROVEEDORES', COUNT(*) FROM proveedor
UNION ALL
SELECT 'ORDENES COMPRA', COUNT(*) FROM orden_compra;

-- =============================================================================
-- FIN DEL SCRIPT
-- =============================================================================
COMMIT;

-- Mensaje de confirmación
SELECT 'Script ejecutado exitosamente. Base de datos lista para usar.' as mensaje;
