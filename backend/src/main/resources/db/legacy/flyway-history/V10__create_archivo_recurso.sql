-- =============================================================================
-- Pasteleria — Tabla archivo_recurso para FileResourceEntity
-- Migration: V10__create_archivo_recurso.sql
-- Base: PostgreSQL 17
-- =============================================================================

CREATE TABLE archivo_recurso (
  archivo_id              BIGSERIAL PRIMARY KEY,
  codigo_archivo          VARCHAR(80)  NOT NULL UNIQUE,
  origen_modulo           VARCHAR(40)  NOT NULL,
  tipo_archivo            VARCHAR(40)  NOT NULL,
  nombre_original         VARCHAR(255) NOT NULL,
  nombre_fisico           VARCHAR(255) NOT NULL,
  mime_type               VARCHAR(120) NOT NULL,
  extension               VARCHAR(20),
  tamano_bytes            BIGINT       NOT NULL,
  checksum                VARCHAR(128),
  ruta_relativa           VARCHAR(500) NOT NULL,
  estado                  VARCHAR(30)  NOT NULL DEFAULT 'DISPONIBLE',
  fecha_creacion          TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  fecha_expiracion        TIMESTAMPTZ,
  creado_por_usuario_id   BIGINT       REFERENCES usuario_sistema(usuario_id),
  CONSTRAINT ck_archivo_estado CHECK (
    estado IN ('DISPONIBLE', 'EXPIRADO', 'ELIMINADO')
  ),
  CONSTRAINT ck_archivo_tamano CHECK (tamano_bytes >= 0)
);

CREATE INDEX idx_archivo_codigo ON archivo_recurso (codigo_archivo);
CREATE INDEX idx_archivo_origen ON archivo_recurso (origen_modulo, tipo_archivo);
CREATE INDEX idx_archivo_estado ON archivo_recurso (estado, fecha_creacion DESC);
