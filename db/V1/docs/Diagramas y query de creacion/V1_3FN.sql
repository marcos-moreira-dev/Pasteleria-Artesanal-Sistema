-- =============================================================================
-- Pasteleria — DDL Completo en Tercera Forma Normal (3FN)
-- Version: V1_3FN — refleja el estado actual del esquema luego de V1 a V9
-- Base: PostgreSQL 17
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. SEGURIDAD Y USUARIOS
-- -----------------------------------------------------------------------------

CREATE TABLE rol_usuario (
  rol_id             BIGSERIAL PRIMARY KEY,
  codigo             VARCHAR(40)  NOT NULL UNIQUE,
  nombre_rol         VARCHAR(80)  NOT NULL UNIQUE,
  descripcion        TEXT,
  activo             BOOLEAN      NOT NULL DEFAULT TRUE,
  created_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE usuario_sistema (
  usuario_id         BIGSERIAL PRIMARY KEY,
  rol_id             BIGINT       NOT NULL REFERENCES rol_usuario(rol_id),
  nombre_usuario     VARCHAR(120) NOT NULL UNIQUE,
  password_hash      VARCHAR(255) NOT NULL,
  nombres            VARCHAR(120) NOT NULL,
  apellidos          VARCHAR(120) NOT NULL,
  correo             VARCHAR(120),
  activo             BOOLEAN      NOT NULL DEFAULT TRUE,
  created_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  version            BIGINT       NOT NULL DEFAULT 0
);

-- -----------------------------------------------------------------------------
-- 2. CATALOGO DE PRODUCTOS
-- -----------------------------------------------------------------------------

CREATE TABLE categoria_producto (
  categoria_id       BIGSERIAL PRIMARY KEY,
  codigo             VARCHAR(40)  NOT NULL UNIQUE,
  nombre             VARCHAR(100) NOT NULL UNIQUE,
  descripcion        TEXT,
  orden_visual       INTEGER      NOT NULL DEFAULT 0,
  activo             BOOLEAN      NOT NULL DEFAULT TRUE,
  created_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  version            BIGINT       NOT NULL DEFAULT 0
);

CREATE TABLE producto (
  producto_id        BIGSERIAL PRIMARY KEY,
  categoria_id       BIGINT       NOT NULL REFERENCES categoria_producto(categoria_id),
  codigo             VARCHAR(50)  NOT NULL UNIQUE,
  slug               VARCHAR(160) NOT NULL UNIQUE,
  nombre             VARCHAR(150) NOT NULL,
  descripcion        TEXT,
  precio_base        NUMERIC(10, 2) NOT NULL,
  requiere_cotizacion BOOLEAN    NOT NULL DEFAULT FALSE,
  activo             BOOLEAN      NOT NULL DEFAULT TRUE,
  publicado          BOOLEAN      NOT NULL DEFAULT TRUE,
  created_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  version            BIGINT       NOT NULL DEFAULT 0,
  CONSTRAINT ck_producto_precio_base CHECK (precio_base >= 0)
);

-- -----------------------------------------------------------------------------
-- 3. CLIENTES
-- -----------------------------------------------------------------------------

CREATE TABLE cliente (
  cliente_id         BIGSERIAL PRIMARY KEY,
  nombre_completo    VARCHAR(160) NOT NULL,
  telefono           VARCHAR(30),
  correo             VARCHAR(120),
  observaciones      TEXT,
  fecha_registro    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  created_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  version            BIGINT       NOT NULL DEFAULT 0
);

-- -----------------------------------------------------------------------------
-- 4. COTIZACIONES
-- -----------------------------------------------------------------------------

CREATE TABLE cotizacion (
  cotizacion_id      BIGSERIAL PRIMARY KEY,
  cliente_id         BIGINT       NOT NULL REFERENCES cliente(cliente_id),
  codigo             VARCHAR(50)  NOT NULL UNIQUE,
  fecha_cotizacion   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  estado_cotizacion  VARCHAR(40)  NOT NULL,
  origen             VARCHAR(20)  NOT NULL DEFAULT 'PUBLICO',
  observaciones      TEXT,
  total_estimado    NUMERIC(10, 2),
  created_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at         TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  version            BIGINT       NOT NULL DEFAULT 0,
  CONSTRAINT ck_cotizacion_estado CHECK (
    estado_cotizacion IN ('PENDIENTE', 'APROBADA', 'RECHAZADA', 'CONVERTIDA')
  ),
  CONSTRAINT ck_cotizacion_origen CHECK (origen IN ('PUBLICO', 'INTERNO')),
  CONSTRAINT ck_cotizacion_total_estimado CHECK (total_estimado IS NULL OR total_estimado >= 0)
);

CREATE TABLE cotizacion_detalle (
  cotizacion_detalle_id BIGSERIAL PRIMARY KEY,
  cotizacion_id         BIGINT     NOT NULL REFERENCES cotizacion(cotizacion_id) ON DELETE CASCADE,
  producto_id           BIGINT     REFERENCES producto(producto_id),
  descripcion_item      TEXT       NOT NULL,
  cantidad              INTEGER     NOT NULL,
  precio_estimado       NUMERIC(10, 2) NOT NULL,
  subtotal              NUMERIC(10, 2) NOT NULL,
  notas                 TEXT,
  created_at            TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT ck_cotizacion_detalle_cantidad CHECK (cantidad > 0),
  CONSTRAINT ck_cotizacion_detalle_precio  CHECK (precio_estimado >= 0),
  CONSTRAINT ck_cotizacion_detalle_subtotal CHECK (subtotal >= 0)
);

-- -----------------------------------------------------------------------------
-- 5. PEDIDOS
-- -----------------------------------------------------------------------------

CREATE TABLE pedido (
  pedido_id                 BIGSERIAL PRIMARY KEY,
  cliente_id                BIGINT       NOT NULL REFERENCES cliente(cliente_id),
  cotizacion_id             BIGINT       UNIQUE REFERENCES cotizacion(cotizacion_id),
  codigo                    VARCHAR(50)  NOT NULL UNIQUE,
  fecha_pedido              TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  fecha_entrega_estimada    TIMESTAMPTZ  NOT NULL,
  fecha_entrega_real        TIMESTAMPTZ,
  estado_pedido             VARCHAR(40)  NOT NULL,
  prioridad                 VARCHAR(20)  NOT NULL DEFAULT 'NORMAL',
  origen                    VARCHAR(20)  NOT NULL DEFAULT 'INTERNO',
  observaciones             TEXT,
  total_estimado            NUMERIC(10, 2),
  created_at               TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at               TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  version                  BIGINT       NOT NULL DEFAULT 0,
  CONSTRAINT ck_pedido_estado CHECK (
    estado_pedido IN ('REGISTRADO', 'EN_PREPARACION', 'LISTO', 'ENTREGADO', 'CANCELADO')
  ),
  CONSTRAINT ck_pedido_prioridad CHECK (prioridad IN ('NORMAL', 'URGENTE')),
  CONSTRAINT ck_pedido_origen CHECK (origen IN ('PUBLICO', 'INTERNO')),
  CONSTRAINT ck_pedido_total CHECK (total_estimado IS NULL OR total_estimado >= 0),
  CONSTRAINT ck_pedido_fechas CHECK (fecha_entrega_estimada >= fecha_pedido)
);

CREATE TABLE pedido_detalle (
  pedido_detalle_id         BIGSERIAL PRIMARY KEY,
  pedido_id                 BIGINT       NOT NULL REFERENCES pedido(pedido_id) ON DELETE CASCADE,
  producto_id               BIGINT       NOT NULL REFERENCES producto(producto_id),
  descripcion_item          TEXT         NOT NULL,
  cantidad                  INTEGER      NOT NULL,
  precio_unitario           NUMERIC(10, 2) NOT NULL,
  subtotal                  NUMERIC(10, 2) NOT NULL,
  notas                     TEXT,
  created_at               TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  CONSTRAINT ck_pedido_detalle_cantidad CHECK (cantidad > 0),
  CONSTRAINT ck_pedido_detalle_precio  CHECK (precio_unitario >= 0),
  CONSTRAINT ck_pedido_detalle_subtotal CHECK (subtotal >= 0)
);

-- -----------------------------------------------------------------------------
-- 6. PRODUCCION
-- -----------------------------------------------------------------------------

CREATE TABLE produccion (
  produccion_id              BIGSERIAL PRIMARY KEY,
  pedido_id                  BIGINT       NOT NULL UNIQUE REFERENCES pedido(pedido_id) ON DELETE CASCADE,
  estado_produccion          VARCHAR(40)  NOT NULL,
  prioridad_produccion       VARCHAR(20)  NOT NULL DEFAULT 'NORMAL',
  fecha_inicio               TIMESTAMPTZ,
  fecha_finalizacion         TIMESTAMPTZ,
  observaciones_produccion   TEXT,
  created_at                 TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at                 TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  version                    BIGINT       NOT NULL DEFAULT 0,
  CONSTRAINT ck_produccion_estado CHECK (
    estado_produccion IN ('PENDIENTE', 'PREPARACION', 'DECORACION', 'EMPAQUE', 'FINALIZADO')
  ),
  CONSTRAINT ck_produccion_prioridad CHECK (prioridad_produccion IN ('NORMAL', 'URGENTE')),
  CONSTRAINT ck_produccion_fechas CHECK (
    fecha_finalizacion IS NULL OR fecha_inicio IS NULL OR fecha_finalizacion >= fecha_inicio
  )
);

-- -----------------------------------------------------------------------------
-- 7. ARCHIVOS Y RECURSOS
-- -----------------------------------------------------------------------------

CREATE TABLE archivo_recurso (
  archivo_id           BIGSERIAL PRIMARY KEY,
  codigo_archivo       VARCHAR(80)  NOT NULL UNIQUE,
  origen_modulo        VARCHAR(40)  NOT NULL,
  tipo_archivo         VARCHAR(40)  NOT NULL,
  nombre_original      VARCHAR(255) NOT NULL,
  nombre_fisico        VARCHAR(255) NOT NULL,
  mime_type            VARCHAR(120) NOT NULL,
  extension            VARCHAR(20),
  tamano_bytes         BIGINT       NOT NULL,
  checksum             VARCHAR(128),
  ruta_relativa        VARCHAR(500) NOT NULL,
  estado               VARCHAR(30)  NOT NULL,
  fecha_creacion       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  fecha_expiracion     TIMESTAMPTZ,
  creado_por_usuario_id BIGINT      REFERENCES usuario_sistema(usuario_id),
  CONSTRAINT ck_archivo_tamano CHECK (tamano_bytes >= 0),
  CONSTRAINT ck_archivo_estado CHECK (estado IN ('DISPONIBLE', 'EXPIRADO', 'ELIMINADO'))
);

-- -----------------------------------------------------------------------------
-- 8. JOBS Y REPORTES
-- -----------------------------------------------------------------------------

CREATE TABLE job_reporte (
  job_reporte_id           BIGSERIAL PRIMARY KEY,
  codigo_job               VARCHAR(80)  NOT NULL UNIQUE,
  tipo_reporte             VARCHAR(60)  NOT NULL,
  parametros_json          JSONB        NOT NULL DEFAULT '{}'::jsonb,
  estado                   VARCHAR(30)  NOT NULL,
  solicitado_por_usuario_id BIGINT     REFERENCES usuario_sistema(usuario_id),
  fecha_solicitud          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  fecha_inicio             TIMESTAMPTZ,
  fecha_fin                TIMESTAMPTZ,
  intentos                 SMALLINT     NOT NULL DEFAULT 0,
  mensaje_error            TEXT,
  archivo_id               BIGINT       REFERENCES archivo_recurso(archivo_id),
  request_id               VARCHAR(120),
  version                  BIGINT       NOT NULL DEFAULT 0,
  CONSTRAINT ck_job_reporte_estado CHECK (
    estado IN ('PENDIENTE', 'EN_PROCESO', 'COMPLETADO', 'ERROR', 'CANCELADO', 'EXPIRADO')
  ),
  CONSTRAINT ck_job_reporte_intentos CHECK (intentos >= 0)
);

-- -----------------------------------------------------------------------------
-- 9. NOTIFICACIONES
-- -----------------------------------------------------------------------------

CREATE TABLE notificacion (
  notificacion_id       BIGSERIAL PRIMARY KEY,
  tipo_notificacion     VARCHAR(60)  NOT NULL,
  titulo                VARCHAR(160) NOT NULL,
  mensaje               TEXT         NOT NULL,
  modulo                VARCHAR(40)  NOT NULL,
  referencia_tipo       VARCHAR(40),
  referencia_id         VARCHAR(80),
  usuario_destino_id    BIGINT       NOT NULL REFERENCES usuario_sistema(usuario_id),
  estado                VARCHAR(30)  NOT NULL,
  prioridad             VARCHAR(20)  NOT NULL DEFAULT 'MEDIA',
  payload_json          JSONB        NOT NULL DEFAULT '{}'::jsonb,
  fecha_creacion        TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  fecha_lectura         TIMESTAMPTZ,
  CONSTRAINT ck_notificacion_estado CHECK (estado IN ('NO_LEIDA', 'LEIDA', 'ARCHIVADA')),
  CONSTRAINT ck_notificacion_prioridad CHECK (prioridad IN ('BAJA', 'MEDIA', 'ALTA'))
);

-- -----------------------------------------------------------------------------
-- 10. AUDITORIA
-- -----------------------------------------------------------------------------

CREATE TABLE auditoria_evento (
  auditoria_evento_id  BIGSERIAL PRIMARY KEY,
  codigo_evento         VARCHAR(80)  NOT NULL,
  modulo                VARCHAR(40)  NOT NULL,
  entidad               VARCHAR(80)  NOT NULL,
  entidad_id            VARCHAR(80) NOT NULL,
  accion                VARCHAR(80) NOT NULL,
  actor_usuario_id      BIGINT       REFERENCES usuario_sistema(usuario_id),
  actor_rol             VARCHAR(80),
  fecha_evento          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  valor_anterior_json   JSONB,
  valor_nuevo_json      JSONB,
  motivo                TEXT,
  request_id            VARCHAR(120),
  ip_origen             VARCHAR(64)
);

-- -----------------------------------------------------------------------------
-- INDICES
-- -----------------------------------------------------------------------------

CREATE INDEX idx_producto_categoria_activo    ON producto (categoria_id, activo);
CREATE INDEX idx_cotizacion_cliente_estado    ON cotizacion (cliente_id, estado_cotizacion, fecha_cotizacion DESC);
CREATE INDEX idx_pedido_cliente_estado         ON pedido (cliente_id, estado_pedido, fecha_pedido DESC);
CREATE INDEX idx_pedido_entrega_estado         ON pedido (estado_pedido, fecha_entrega_estimada);
CREATE INDEX idx_pedido_detalle_pedido          ON pedido_detalle (pedido_id);
CREATE INDEX idx_produccion_estado_prioridad    ON produccion (estado_produccion, prioridad_produccion);
CREATE INDEX idx_notificacion_usuario_estado   ON notificacion (usuario_destino_id, estado, fecha_creacion DESC);
CREATE INDEX idx_job_reporte_estado_solicitud  ON job_reporte (estado, fecha_solicitud DESC);
CREATE INDEX idx_auditoria_evento_modulo_fecha ON auditoria_evento (modulo, fecha_evento DESC);
CREATE INDEX idx_auditoria_evento_entidad      ON auditoria_evento (entidad, entidad_id, fecha_evento DESC);

-- =============================================================================
-- 11. ABASTECIMIENTO — UNIDADES DE MEDIDA
-- =============================================================================

CREATE TABLE umedida (
  umedida_id      BIGSERIAL PRIMARY KEY,
  codigo          VARCHAR(20)  NOT NULL UNIQUE,
  nombre          VARCHAR(60)  NOT NULL,
  abreviatura     VARCHAR(10)  NOT NULL,
  tipo            VARCHAR(20)  NOT NULL,
  decimales       SMALLINT     NOT NULL DEFAULT 0,
  activo          BOOLEAN      NOT NULL DEFAULT TRUE,
  created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  CONSTRAINT ck_umedida_tipo CHECK (
    tipo IN ('PESO', 'VOLUMEN', 'UNIDAD', 'ENVASE')
  ),
  CONSTRAINT ck_umedida_decimales CHECK (
    decimales >= 0 AND decimales <= 6
  )
);

-- =============================================================================
-- 12. ABASTECIMIENTO — INGREDIENTES E INSUMOS
-- =============================================================================

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

-- =============================================================================
-- 13. ABASTECIMIENTO — PROVEEDORES
-- =============================================================================

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

CREATE TABLE item_proveedor (
  item_proveedor_id    BIGSERIAL PRIMARY KEY,
  item_tipo            VARCHAR(20)  NOT NULL,
  item_id              BIGINT       NOT NULL,
  proveedor_id         BIGINT       NOT NULL REFERENCES proveedor(proveedor_id),
  precio_suministro    NUMERIC(10, 4) NOT NULL,
  es_principal         BOOLEAN      NOT NULL DEFAULT FALSE,
  activo              BOOLEAN      NOT NULL DEFAULT TRUE,
  created_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  CONSTRAINT ck_item_proveedor_tipo CHECK (
    item_tipo IN ('INGREDIENTE', 'INSUMO')
  ),
  CONSTRAINT ck_item_proveedor_precio CHECK (precio_suministro >= 0)
);

-- =============================================================================
-- 14. ABASTECIMIENTO — ORDENES DE COMPRA
-- =============================================================================

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

-- =============================================================================
-- 15. ABASTECIMIENTO — MOVIMIENTOS DE INVENTARIO
-- =============================================================================

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

-- =============================================================================
-- 16. ABASTECIMIENTO — RECETAS
-- =============================================================================

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

-- =============================================================================
-- 17. ABASTECIMIENTO — ORDENES DE PRODUCCION
-- =============================================================================

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

-- =============================================================================
-- INDICES — ABASTECIMIENTO
-- =============================================================================

CREATE INDEX idx_ingrediente_codigo           ON ingrediente (codigo);
CREATE INDEX idx_ingrediente_activo          ON ingrediente (activo);
CREATE INDEX idx_insumo_codigo               ON insumo (codigo);
CREATE INDEX idx_insumo_activo               ON insumo (activo);
CREATE INDEX idx_proveedor_activo            ON proveedor (activo);
CREATE INDEX idx_item_proveedor_item         ON item_proveedor (item_tipo, item_id);
CREATE INDEX idx_item_proveedor_proveedor    ON item_proveedor (proveedor_id);
CREATE INDEX idx_orden_compra_estado         ON orden_compra (estado, fecha_creacion DESC);
CREATE INDEX idx_orden_compra_proveedor      ON orden_compra (proveedor_id, estado);
CREATE INDEX idx_oc_detalle_orden            ON orden_compra_detalle (orden_compra_id);
CREATE INDEX idx_movimiento_item_fecha        ON inventario_movimiento (item_tipo, item_id, fecha_movimiento DESC);
CREATE INDEX idx_movimiento_referencia        ON inventario_movimiento (referencia_tipo, referencia_id);
CREATE INDEX idx_receta_producto_activa       ON receta (producto_id, es_activa);
CREATE INDEX idx_receta_activa                ON receta (es_activa) WHERE es_activa = TRUE;
CREATE INDEX idx_detalle_receta_receta        ON detalle_receta (receta_id);
CREATE INDEX idx_detalle_receta_ingrediente   ON detalle_receta (ingrediente_id);
CREATE INDEX idx_orden_produccion_estado      ON orden_produccion (estado, fecha_creacion DESC);
CREATE INDEX idx_orden_produccion_prod        ON orden_produccion (produccion_id);
CREATE INDEX idx_op_detalle_orden             ON orden_produccion_detalle (orden_produccion_id);
CREATE INDEX idx_op_detalle_pedido_detalle    ON orden_produccion_detalle (pedido_detalle_id);
CREATE INDEX idx_consumo_detalle              ON consumo_produccion (orden_produccion_detalle_id);

-- =============================================================================
-- ACTUALIZACIONES AL ESQUEMA EXISTENTE (V9)
-- =============================================================================

-- Corregir ck_produccion_estado para incluir CANCELADO
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
