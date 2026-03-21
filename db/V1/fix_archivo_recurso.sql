-- Fix script: Add missing columns to archivo_recurso table
-- Run this if you already have the database created and don't want to recreate it

-- Add missing columns one by one (will fail silently if they already exist)
ALTER TABLE archivo_recurso ADD COLUMN IF NOT EXISTS codigo_archivo VARCHAR(80);
ALTER TABLE archivo_recurso ADD COLUMN IF NOT EXISTS origen_modulo VARCHAR(40);
ALTER TABLE archivo_recurso ADD COLUMN IF NOT EXISTS tipo_archivo VARCHAR(40);
ALTER TABLE archivo_recurso ADD COLUMN IF NOT EXISTS nombre_fisico VARCHAR(255);
ALTER TABLE archivo_recurso ADD COLUMN IF NOT EXISTS mime_type VARCHAR(120);
ALTER TABLE archivo_recurso ADD COLUMN IF NOT EXISTS extension VARCHAR(20);
ALTER TABLE archivo_recurso ADD COLUMN IF NOT EXISTS checksum VARCHAR(128);
ALTER TABLE archivo_recurso ADD COLUMN IF NOT EXISTS ruta_relativa VARCHAR(500);
ALTER TABLE archivo_recurso ADD COLUMN IF NOT EXISTS estado VARCHAR(30) DEFAULT 'ACTIVO';
ALTER TABLE archivo_recurso ADD COLUMN IF NOT EXISTS fecha_creacion TIMESTAMPTZ DEFAULT NOW();
ALTER TABLE archivo_recurso ADD COLUMN IF NOT EXISTS fecha_expiracion TIMESTAMPTZ;

-- Rename columns to match JPA entity (if using old column names)
ALTER TABLE archivo_recurso RENAME COLUMN nombre_almacenado TO nombre_fisico;
ALTER TABLE archivo_recurso RENAME COLUMN tipo_mime TO mime_type;
ALTER TABLE archivo_recurso RENAME COLUMN ruta_almacenamiento TO ruta_relativa;
ALTER TABLE archivo_recurso RENAME COLUMN created_at TO fecha_creacion;

-- Add NOT NULL constraints where needed (after data migration if necessary)
-- ALTER TABLE archivo_recurso ALTER COLUMN codigo_archivo SET NOT NULL;
-- ALTER TABLE archivo_recurso ALTER COLUMN origen_modulo SET NOT NULL;
-- ALTER TABLE archivo_recurso ALTER COLUMN tipo_archivo SET NOT NULL;

-- Add unique constraint on codigo_archivo
ALTER TABLE archivo_recurso ADD CONSTRAINT uk_archivo_codigo UNIQUE (codigo_archivo);

-- Verify the fix
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'archivo_recurso' 
ORDER BY ordinal_position;
