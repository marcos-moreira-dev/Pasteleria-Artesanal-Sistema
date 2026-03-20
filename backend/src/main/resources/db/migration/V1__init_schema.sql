CREATE TABLE rol_usuario (
  rol_id BIGSERIAL PRIMARY KEY,
  codigo VARCHAR(40) NOT NULL UNIQUE,
  nombre_rol VARCHAR(80) NOT NULL UNIQUE,
  descripcion TEXT,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE usuario_sistema (
  usuario_id BIGSERIAL PRIMARY KEY,
  rol_id BIGINT NOT NULL REFERENCES rol_usuario(rol_id),
  nombre_usuario VARCHAR(120) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  nombres VARCHAR(120) NOT NULL,
  apellidos VARCHAR(120) NOT NULL,
  correo VARCHAR(120),
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE cliente (
  cliente_id BIGSERIAL PRIMARY KEY,
  nombre_completo VARCHAR(160) NOT NULL,
  telefono VARCHAR(30),
  correo VARCHAR(120),
  observaciones TEXT,
  fecha_registro TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE categoria_producto (
  categoria_id BIGSERIAL PRIMARY KEY,
  codigo VARCHAR(40) NOT NULL UNIQUE,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  descripcion TEXT,
  orden_visual INTEGER NOT NULL DEFAULT 0,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE producto (
  producto_id BIGSERIAL PRIMARY KEY,
  categoria_id BIGINT NOT NULL REFERENCES categoria_producto(categoria_id),
  codigo VARCHAR(50) NOT NULL UNIQUE,
  slug VARCHAR(160) NOT NULL UNIQUE,
  nombre VARCHAR(150) NOT NULL,
  descripcion TEXT,
  precio_base NUMERIC(10, 2) NOT NULL,
  requiere_cotizacion BOOLEAN NOT NULL DEFAULT FALSE,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  publicado BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT ck_producto_precio_base CHECK (precio_base >= 0)
);

CREATE TABLE cotizacion (
  cotizacion_id BIGSERIAL PRIMARY KEY,
  cliente_id BIGINT NOT NULL REFERENCES cliente(cliente_id),
  codigo VARCHAR(50) NOT NULL UNIQUE,
  fecha_cotizacion TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  estado_cotizacion VARCHAR(40) NOT NULL,
  origen VARCHAR(20) NOT NULL DEFAULT 'PUBLICO',
  observaciones TEXT,
  total_estimado NUMERIC(10, 2),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT ck_cotizacion_estado CHECK (
    estado_cotizacion IN ('PENDIENTE', 'APROBADA', 'RECHAZADA', 'CONVERTIDA')
  ),
  CONSTRAINT ck_cotizacion_origen CHECK (origen IN ('PUBLICO', 'INTERNO')),
  CONSTRAINT ck_cotizacion_total_estimado CHECK (total_estimado IS NULL OR total_estimado >= 0)
);

CREATE TABLE cotizacion_detalle (
  cotizacion_detalle_id BIGSERIAL PRIMARY KEY,
  cotizacion_id BIGINT NOT NULL REFERENCES cotizacion(cotizacion_id) ON DELETE CASCADE,
  producto_id BIGINT REFERENCES producto(producto_id),
  descripcion_item TEXT NOT NULL,
  cantidad INTEGER NOT NULL,
  precio_estimado NUMERIC(10, 2) NOT NULL,
  subtotal NUMERIC(10, 2) NOT NULL,
  notas TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT ck_cotizacion_detalle_cantidad CHECK (cantidad > 0),
  CONSTRAINT ck_cotizacion_detalle_precio CHECK (precio_estimado >= 0),
  CONSTRAINT ck_cotizacion_detalle_subtotal CHECK (subtotal >= 0)
);

CREATE TABLE pedido (
  pedido_id BIGSERIAL PRIMARY KEY,
  cliente_id BIGINT NOT NULL REFERENCES cliente(cliente_id),
  cotizacion_id BIGINT UNIQUE REFERENCES cotizacion(cotizacion_id),
  codigo VARCHAR(50) NOT NULL UNIQUE,
  fecha_pedido TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  fecha_entrega_estimada TIMESTAMPTZ NOT NULL,
  fecha_entrega_real TIMESTAMPTZ,
  estado_pedido VARCHAR(40) NOT NULL,
  prioridad VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
  origen VARCHAR(20) NOT NULL DEFAULT 'INTERNO',
  observaciones TEXT,
  total_estimado NUMERIC(10, 2),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
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

CREATE TABLE produccion (
  produccion_id BIGSERIAL PRIMARY KEY,
  pedido_id BIGINT NOT NULL UNIQUE REFERENCES pedido(pedido_id) ON DELETE CASCADE,
  estado_produccion VARCHAR(40) NOT NULL,
  prioridad_produccion VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
  fecha_inicio TIMESTAMPTZ,
  fecha_finalizacion TIMESTAMPTZ,
  observaciones_produccion TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT ck_produccion_estado CHECK (
    estado_produccion IN ('PENDIENTE', 'EN_PROCESO', 'FINALIZADO')
  ),
  CONSTRAINT ck_produccion_prioridad CHECK (prioridad_produccion IN ('NORMAL', 'URGENTE')),
  CONSTRAINT ck_produccion_fechas CHECK (
    fecha_finalizacion IS NULL OR fecha_inicio IS NULL OR fecha_finalizacion >= fecha_inicio
  )
);

CREATE TABLE archivo_recurso (
  archivo_id BIGSERIAL PRIMARY KEY,
  codigo_archivo VARCHAR(80) NOT NULL UNIQUE,
  origen_modulo VARCHAR(40) NOT NULL,
  tipo_archivo VARCHAR(40) NOT NULL,
  nombre_original VARCHAR(255) NOT NULL,
  nombre_fisico VARCHAR(255) NOT NULL,
  mime_type VARCHAR(120) NOT NULL,
  extension VARCHAR(20),
  tamano_bytes BIGINT NOT NULL,
  checksum VARCHAR(128),
  ruta_relativa VARCHAR(500) NOT NULL,
  estado VARCHAR(30) NOT NULL,
  fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  fecha_expiracion TIMESTAMPTZ,
  creado_por_usuario_id BIGINT REFERENCES usuario_sistema(usuario_id),
  CONSTRAINT ck_archivo_tamano CHECK (tamano_bytes >= 0),
  CONSTRAINT ck_archivo_estado CHECK (estado IN ('DISPONIBLE', 'EXPIRADO', 'ELIMINADO'))
);

CREATE TABLE job_reporte (
  job_reporte_id BIGSERIAL PRIMARY KEY,
  codigo_job VARCHAR(80) NOT NULL UNIQUE,
  tipo_reporte VARCHAR(60) NOT NULL,
  parametros_json JSONB NOT NULL DEFAULT '{}'::jsonb,
  estado VARCHAR(30) NOT NULL,
  solicitado_por_usuario_id BIGINT REFERENCES usuario_sistema(usuario_id),
  fecha_solicitud TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  fecha_inicio TIMESTAMPTZ,
  fecha_fin TIMESTAMPTZ,
  intentos SMALLINT NOT NULL DEFAULT 0,
  mensaje_error TEXT,
  archivo_id BIGINT REFERENCES archivo_recurso(archivo_id),
  request_id VARCHAR(120),
  version BIGINT NOT NULL DEFAULT 0,
  CONSTRAINT ck_job_reporte_estado CHECK (
    estado IN ('PENDIENTE', 'EN_PROCESO', 'COMPLETADO', 'ERROR', 'CANCELADO', 'EXPIRADO')
  ),
  CONSTRAINT ck_job_reporte_intentos CHECK (intentos >= 0)
);

CREATE TABLE notificacion (
  notificacion_id BIGSERIAL PRIMARY KEY,
  tipo_notificacion VARCHAR(60) NOT NULL,
  titulo VARCHAR(160) NOT NULL,
  mensaje TEXT NOT NULL,
  modulo VARCHAR(40) NOT NULL,
  referencia_tipo VARCHAR(40),
  referencia_id VARCHAR(80),
  usuario_destino_id BIGINT NOT NULL REFERENCES usuario_sistema(usuario_id),
  estado VARCHAR(30) NOT NULL,
  prioridad VARCHAR(20) NOT NULL DEFAULT 'MEDIA',
  payload_json JSONB NOT NULL DEFAULT '{}'::jsonb,
  fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  fecha_lectura TIMESTAMPTZ,
  CONSTRAINT ck_notificacion_estado CHECK (estado IN ('NO_LEIDA', 'LEIDA', 'ARCHIVADA')),
  CONSTRAINT ck_notificacion_prioridad CHECK (prioridad IN ('BAJA', 'MEDIA', 'ALTA'))
);

CREATE TABLE auditoria_evento (
  auditoria_evento_id BIGSERIAL PRIMARY KEY,
  codigo_evento VARCHAR(80) NOT NULL,
  modulo VARCHAR(40) NOT NULL,
  entidad VARCHAR(80) NOT NULL,
  entidad_id VARCHAR(80) NOT NULL,
  accion VARCHAR(80) NOT NULL,
  actor_usuario_id BIGINT REFERENCES usuario_sistema(usuario_id),
  actor_rol VARCHAR(80),
  fecha_evento TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  valor_anterior_json JSONB,
  valor_nuevo_json JSONB,
  motivo TEXT,
  request_id VARCHAR(120),
  ip_origen VARCHAR(64)
);

CREATE INDEX idx_producto_categoria_activo ON producto (categoria_id, activo);
CREATE INDEX idx_cotizacion_cliente_estado ON cotizacion (cliente_id, estado_cotizacion, fecha_cotizacion DESC);
CREATE INDEX idx_pedido_cliente_estado ON pedido (cliente_id, estado_pedido, fecha_pedido DESC);
CREATE INDEX idx_pedido_entrega_estado ON pedido (estado_pedido, fecha_entrega_estimada);
CREATE INDEX idx_pedido_detalle_pedido ON pedido_detalle (pedido_id);
CREATE INDEX idx_produccion_estado_prioridad ON produccion (estado_produccion, prioridad_produccion);
CREATE INDEX idx_notificacion_usuario_estado ON notificacion (usuario_destino_id, estado, fecha_creacion DESC);
CREATE INDEX idx_job_reporte_estado_solicitud ON job_reporte (estado, fecha_solicitud DESC);
CREATE INDEX idx_auditoria_evento_modulo_fecha ON auditoria_evento (modulo, fecha_evento DESC);
CREATE INDEX idx_auditoria_evento_entidad ON auditoria_evento (entidad, entidad_id, fecha_evento DESC);
