-- =============================================================================
-- Pasteleria — Modulo Abastecimiento
-- Migration: V9__abastecimiento_inventario_recetas
-- Base: PostgreSQL 17
-- Dependencias: V1 a V8 del esquema existente
-- =============================================================================

-- -----------------------------------------------------------------------------
-- ABASTECIMIENTO — UNIDADES DE MEDIDA
-- -----------------------------------------------------------------------------

CREATE TABLE umedida (
  umedida_id      BIGSERIAL PRIMARY KEY,
  codigo          VARCHAR(20)  NOT NULL UNIQUE,
  nombre          VARCHAR(60)  NOT NULL,
  abreviatura     VARCHAR(10)  NOT NULL,
  tipo            VARCHAR(20)  NOT NULL,
  decimales       SMALLINT     NOT NULL DEFAULT 0,
  activo          BOOLEAN      NOT NULL DEFAULT TRUE,
  created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  version         BIGINT       NOT NULL DEFAULT 0,
  CONSTRAINT ck_umedida_tipo CHECK (
    tipo IN ('PESO', 'VOLUMEN', 'UNIDAD', 'ENVASE')
  ),
  CONSTRAINT ck_umedida_decimales CHECK (
    decimales >= 0 AND decimales <= 6
  )
);

INSERT INTO umedida (codigo, nombre, abreviatura, tipo, decimales) VALUES
  ('g',      'gramo',     'g',    'PESO',   2),
  ('kg',     'kilogramo', 'kg',   'PESO',   3),
  ('ml',     'mililitro', 'ml',   'VOLUMEN',3),
  ('l',      'litro',     'L',    'VOLUMEN',3),
  ('u',      'unidad',    'u',    'UNIDAD', 0),
  ('caja',   'caja',      'caja', 'ENVASE', 0),
  ('bandeja','bandeja',   'band.','ENVASE', 0),
  ('paq',    'paquete',   'paq',  'ENVASE', 0),
  ('bolsa',  'bolsa',     'bol',  'ENVASE', 0),
  ('docena', 'docena',    'doc',  'UNIDAD', 1);

-- -----------------------------------------------------------------------------
-- ABASTECIMIENTO — INGREDIENTES
-- -----------------------------------------------------------------------------

CREATE TABLE ingrediente (
  ingrediente_id       BIGSERIAL PRIMARY KEY,
  umedida_id          BIGINT       NOT NULL REFERENCES umedida(umedida_id),
  codigo              VARCHAR(40)  NOT NULL UNIQUE,
  nombre              VARCHAR(120) NOT NULL,
  descripcion         TEXT,
  stock_minimo        NUMERIC(10, 3) NOT NULL DEFAULT 0,
  stock_actual        NUMERIC(10, 3) NOT NULL DEFAULT 0,
  costo_referencial   NUMERIC(10, 4) NOT NULL DEFAULT 0,
  activo              BOOLEAN      NOT NULL DEFAULT TRUE,
  created_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  version             BIGINT       NOT NULL DEFAULT 0,
  CONSTRAINT ck_ingrediente_stock_minimo CHECK (stock_minimo >= 0),
  CONSTRAINT ck_ingrediente_costo CHECK (costo_referencial >= 0),
  CONSTRAINT ck_ingrediente_stock CHECK (stock_actual >= 0)
);

-- -----------------------------------------------------------------------------
-- ABASTECIMIENTO — INSUMOS / EMPAQUES
-- -----------------------------------------------------------------------------

CREATE TABLE insumo (
  insumo_id           BIGSERIAL PRIMARY KEY,
  umedida_id          BIGINT       NOT NULL REFERENCES umedida(umedida_id),
  codigo              VARCHAR(40)  NOT NULL UNIQUE,
  nombre              VARCHAR(120) NOT NULL,
  descripcion         TEXT,
  stock_minimo        NUMERIC(10, 3) NOT NULL DEFAULT 0,
  stock_actual        NUMERIC(10, 3) NOT NULL DEFAULT 0,
  costo_referencial   NUMERIC(10, 4) NOT NULL DEFAULT 0,
  activo              BOOLEAN      NOT NULL DEFAULT TRUE,
  created_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  version             BIGINT       NOT NULL DEFAULT 0,
  CONSTRAINT ck_insumo_stock_minimo CHECK (stock_minimo >= 0),
  CONSTRAINT ck_insumo_costo CHECK (costo_referencial >= 0),
  CONSTRAINT ck_insumo_stock CHECK (stock_actual >= 0)
);

-- -----------------------------------------------------------------------------
-- ABASTECIMIENTO — PROVEEDORES
-- -----------------------------------------------------------------------------

CREATE TABLE proveedor (
  proveedor_id    BIGSERIAL PRIMARY KEY,
  codigo          VARCHAR(40)  NOT NULL UNIQUE,
  nombre          VARCHAR(160) NOT NULL,
  telefono        VARCHAR(30),
  correo         VARCHAR(120),
  direccion      TEXT,
  observaciones  TEXT,
  activo         BOOLEAN      NOT NULL DEFAULT TRUE,
  created_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  version        BIGINT       NOT NULL DEFAULT 0
);

-- -----------------------------------------------------------------------------
-- ABASTECIMIENTO — LIGA PROVEEDOR <-> ITEM
-- -----------------------------------------------------------------------------

CREATE TABLE item_proveedor (
  item_proveedor_id    BIGSERIAL PRIMARY KEY,
  item_tipo            VARCHAR(20)  NOT NULL,
  item_id              BIGINT       NOT NULL,
  proveedor_id         BIGINT       NOT NULL REFERENCES proveedor(proveedor_id),
  precio_suministro     NUMERIC(10, 4) NOT NULL,
  es_principal         BOOLEAN      NOT NULL DEFAULT FALSE,
  activo              BOOLEAN      NOT NULL DEFAULT TRUE,
  created_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  CONSTRAINT ck_item_proveedor_tipo CHECK (
    item_tipo IN ('INGREDIENTE', 'INSUMO')
  ),
  CONSTRAINT ck_item_proveedor_precio CHECK (precio_suministro >= 0)
);

-- -----------------------------------------------------------------------------
-- ABASTECIMIENTO — ORDENES DE COMPRA
-- -----------------------------------------------------------------------------

CREATE TABLE orden_compra (
  orden_compra_id          BIGSERIAL PRIMARY KEY,
  proveedor_id             BIGINT       NOT NULL REFERENCES proveedor(proveedor_id),
  codigo                   VARCHAR(50)  NOT NULL UNIQUE,
  estado                   VARCHAR(30)  NOT NULL DEFAULT 'BORRADOR',
  observaciones            TEXT,
  fecha_entrega_estimada   TIMESTAMPTZ,
  created_at               TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at               TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  created_by_user_id       BIGINT       REFERENCES usuario_sistema(usuario_id),
  version                  BIGINT       NOT NULL DEFAULT 0,
  CONSTRAINT ck_orden_compra_estado CHECK (
    estado IN ('BORRADOR', 'ENVIADA', 'RECIBIDA_PARCIAL', 'RECIBIDA', 'CANCELADA')
  )
);

CREATE TABLE orden_compra_detalle (
  orden_compra_detalle_id  BIGSERIAL PRIMARY KEY,
  orden_compra_id          BIGINT       NOT NULL REFERENCES orden_compra(orden_compra_id) ON DELETE CASCADE,
  item_tipo                VARCHAR(20)  NOT NULL,
  item_id                  BIGINT       NOT NULL,
  cantidad                 NUMERIC(10, 3) NOT NULL,
  cantidad_recibida        NUMERIC(10, 3) NOT NULL DEFAULT 0,
  precio_unitario          NUMERIC(10, 4) NOT NULL,
  created_at               TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  CONSTRAINT ck_oc_detalle_tipo CHECK (item_tipo IN ('INGREDIENTE', 'INSUMO')),
  CONSTRAINT ck_oc_detalle_cantidad CHECK (cantidad > 0),
  CONSTRAINT ck_oc_detalle_recibida CHECK (cantidad_recibida >= 0),
  CONSTRAINT ck_oc_detalle_precio CHECK (precio_unitario >= 0)
);

-- -----------------------------------------------------------------------------
-- ABASTECIMIENTO — MOVIMIENTOS DE INVENTARIO
-- -----------------------------------------------------------------------------

CREATE TABLE inventario_movimiento (
  inventario_movimiento_id  BIGSERIAL PRIMARY KEY,
  item_tipo                VARCHAR(20)  NOT NULL,
  item_id                  BIGINT       NOT NULL,
  tipo_movimiento          VARCHAR(30)  NOT NULL,
  cantidad                 NUMERIC(10, 3) NOT NULL,
  saldo_posterior          NUMERIC(10, 3) NOT NULL,
  referencia_tipo          VARCHAR(40),
  referencia_id            VARCHAR(80),
  motivo_salida           VARCHAR(100),
  observaciones           TEXT,
  fecha_movimiento        TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  registrado_por_user_id  BIGINT       REFERENCES usuario_sistema(usuario_id),
  CONSTRAINT ck_inv_mov_tipo_item CHECK (
    item_tipo IN ('INGREDIENTE', 'INSUMO')
  ),
  CONSTRAINT ck_inv_mov_tipo_movimiento CHECK (
    tipo_movimiento IN ('ENTRADA_COMPRA', 'ENTRADA_AJUSTE', 'SALIDA_PRODUCCION', 'SALIDA_MERMA', 'SALIDA_AJUSTE')
  ),
  CONSTRAINT ck_inv_mov_cantidad CHECK (cantidad > 0)
);

-- -----------------------------------------------------------------------------
-- ABASTECIMIENTO — RECETAS / FICHAS TECNICAS
-- -----------------------------------------------------------------------------

CREATE TABLE receta (
  receta_id           BIGSERIAL PRIMARY KEY,
  producto_id         BIGINT       REFERENCES producto(producto_id),
  nombre              VARCHAR(160) NOT NULL,
  rendimiento_base    NUMERIC(10, 2) NOT NULL DEFAULT 1,
  costo_estimado      NUMERIC(10, 4),
  observaciones      TEXT,
  es_activa          BOOLEAN      NOT NULL DEFAULT FALSE,
  created_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  created_by_user_id  BIGINT       REFERENCES usuario_sistema(usuario_id),
  version            BIGINT       NOT NULL DEFAULT 0,
  CONSTRAINT ck_receta_rendimiento CHECK (rendimiento_base > 0)
);

CREATE UNIQUE INDEX idx_receta_unica_activa
  ON receta (producto_id)
  WHERE es_activa = TRUE;

CREATE TABLE detalle_receta (
  detalle_receta_id       BIGSERIAL PRIMARY KEY,
  receta_id               BIGINT       NOT NULL REFERENCES receta(receta_id) ON DELETE CASCADE,
  ingrediente_id          BIGINT       NOT NULL REFERENCES ingrediente(ingrediente_id),
  cantidad_base          NUMERIC(10, 4) NOT NULL,
  rendimiento_por_unidad NUMERIC(10, 4) NOT NULL DEFAULT 1,
  es_para_porcion        BOOLEAN      NOT NULL DEFAULT TRUE,
  observaciones          TEXT,
  created_at             TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  CONSTRAINT ck_detalle_receta_cantidad CHECK (cantidad_base > 0),
  CONSTRAINT ck_detalle_receta_rendimiento CHECK (rendimiento_por_unidad > 0)
);

-- -----------------------------------------------------------------------------
-- ABASTECIMIENTO — ORDENES DE PRODUCCION
-- -----------------------------------------------------------------------------

CREATE TABLE orden_produccion (
  orden_produccion_id   BIGSERIAL PRIMARY KEY,
  produccion_id         BIGINT       NOT NULL UNIQUE REFERENCES produccion(produccion_id) ON DELETE CASCADE,
  codigo                VARCHAR(50)  NOT NULL UNIQUE,
  estado                VARCHAR(30)  NOT NULL DEFAULT 'PENDIENTE',
  observaciones         TEXT,
  fecha_creacion        TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  fecha_inicio         TIMESTAMPTZ,
  fecha_finalizacion   TIMESTAMPTZ,
  responsable_id       BIGINT       REFERENCES usuario_sistema(usuario_id),
  created_at           TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at           TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  version              BIGINT       NOT NULL DEFAULT 0,
  CONSTRAINT ck_orden_produccion_estado CHECK (
    estado IN ('PENDIENTE', 'EN_PREPARACION', 'FINALIZADA', 'CANCELADA')
  )
);

CREATE TABLE orden_produccion_detalle (
  orden_produccion_detalle_id  BIGSERIAL PRIMARY KEY,
  orden_produccion_id           BIGINT       NOT NULL REFERENCES orden_produccion(orden_produccion_id) ON DELETE CASCADE,
  pedido_detalle_id            BIGINT       REFERENCES pedido_detalle(pedido_detalle_id),
  producto_id                  BIGINT       NOT NULL REFERENCES producto(producto_id),
  receta_id                    BIGINT       REFERENCES receta(receta_id),
  cantidad_producir            INTEGER      NOT NULL,
  cantidad_producida           INTEGER      NOT NULL DEFAULT 0,
  estado_detalle               VARCHAR(30)  NOT NULL DEFAULT 'PENDIENTE',
  observaciones                TEXT,
  created_at                   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at                  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  CONSTRAINT ck_op_detalle_cantidad CHECK (cantidad_producir > 0),
  CONSTRAINT ck_op_detalle_prod CHECK (cantidad_producida >= 0),
  CONSTRAINT ck_op_detalle_estado CHECK (
    estado_detalle IN ('PENDIENTE', 'EN_PRODUCCION', 'FINALIZADO', 'CANCELADO')
  )
);

CREATE TABLE consumo_produccion (
  consumo_produccion_id       BIGSERIAL PRIMARY KEY,
  orden_produccion_detalle_id  BIGINT      NOT NULL REFERENCES orden_produccion_detalle(orden_produccion_detalle_id) ON DELETE CASCADE,
  item_tipo                   VARCHAR(20) NOT NULL,
  item_id                     BIGINT      NOT NULL,
  cantidad_consumida          NUMERIC(10, 3) NOT NULL,
  cantidad_merma              NUMERIC(10, 3) NOT NULL DEFAULT 0,
  observaciones               TEXT,
  created_at                  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  CONSTRAINT ck_consumo_tipo CHECK (
    item_tipo IN ('INGREDIENTE', 'INSUMO')
  ),
  CONSTRAINT ck_consumo_cantidad CHECK (cantidad_consumida > 0),
  CONSTRAINT ck_consumo_merma CHECK (cantidad_merma >= 0)
);

-- -----------------------------------------------------------------------------
-- INDICES
-- -----------------------------------------------------------------------------

CREATE INDEX idx_ingrediente_codigo          ON ingrediente (codigo);
CREATE INDEX idx_ingrediente_activo         ON ingrediente (activo);
CREATE INDEX idx_insumo_codigo              ON insumo (codigo);
CREATE INDEX idx_insumo_activo              ON insumo (activo);
CREATE INDEX idx_proveedor_activo           ON proveedor (activo);
CREATE INDEX idx_item_proveedor_item       ON item_proveedor (item_tipo, item_id);
CREATE INDEX idx_item_proveedor_proveedor  ON item_proveedor (proveedor_id);
CREATE INDEX idx_orden_compra_estado       ON orden_compra (estado, created_at DESC);
CREATE INDEX idx_orden_compra_proveedor    ON orden_compra (proveedor_id, estado);
CREATE INDEX idx_oc_detalle_orden          ON orden_compra_detalle (orden_compra_id);
CREATE INDEX idx_movimiento_item_fecha    ON inventario_movimiento (item_tipo, item_id, fecha_movimiento DESC);
CREATE INDEX idx_movimiento_referencia     ON inventario_movimiento (referencia_tipo, referencia_id);
CREATE INDEX idx_receta_producto_activa    ON receta (producto_id, es_activa);
CREATE INDEX idx_receta_activa             ON receta (es_activa) WHERE es_activa = TRUE;
CREATE INDEX idx_detalle_receta_receta     ON detalle_receta (receta_id);
CREATE INDEX idx_detalle_receta_ingrediente ON detalle_receta (ingrediente_id);
CREATE INDEX idx_orden_produccion_estado  ON orden_produccion (estado, created_at DESC);
CREATE INDEX idx_orden_produccion_prod    ON orden_produccion (produccion_id);
CREATE INDEX idx_op_detalle_orden         ON orden_produccion_detalle (orden_produccion_id);
CREATE INDEX idx_op_detalle_pedido_detalle ON orden_produccion_detalle (pedido_detalle_id);
CREATE INDEX idx_consumo_detalle          ON consumo_produccion (orden_produccion_detalle_id);

-- -----------------------------------------------------------------------------
-- ACTUALIZACIONES AL ESQUEMA EXISTENTE
-- -----------------------------------------------------------------------------

-- P1 + P3: Corregir ck_produccion_estado
-- El constraint actual puede decir EN_PROCESO (stale de V1).
-- V5 lo refactorizo a PREPARACION/DECORACION/EMPAQUE.
-- Aqui se asegura que el constraint final incluya CANCELADO.

ALTER TABLE produccion
  DROP CONSTRAINT IF EXISTS ck_produccion_estado;

ALTER TABLE produccion
  ADD CONSTRAINT ck_produccion_estado CHECK (
    estado_produccion IN (
      'PENDIENTE', 'PREPARACION', 'DECORACION', 'EMPAQUE', 'FINALIZADO', 'CANCELADO'
    )
  );

-- Asegurar que version en produccion es NOT NULL
ALTER TABLE produccion
  ALTER COLUMN version SET NOT NULL;
