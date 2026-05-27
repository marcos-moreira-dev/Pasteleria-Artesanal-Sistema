\echo '== Validate audit, support and evidence =='

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.tables
    WHERE table_schema = 'public'
      AND table_name = 'auditoria_evento'
  ) THEN
    RAISE EXCEPTION 'No existe la tabla public.auditoria_evento';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM auditoria_evento
    WHERE codigo_evento IS NULL OR btrim(codigo_evento) = ''
       OR modulo IS NULL OR btrim(modulo) = ''
       OR entidad IS NULL OR btrim(entidad) = ''
       OR entidad_id IS NULL OR btrim(entidad_id) = ''
       OR accion IS NULL OR btrim(accion) = ''
       OR fecha_evento IS NULL
  ) THEN
    RAISE EXCEPTION 'Hay eventos de auditoria con datos obligatorios incompletos';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM auditoria_evento) THEN
    RAISE EXCEPTION 'No hay eventos de auditoria semilla para evidencia inicial';
  END IF;

  IF NOT EXISTS (
    SELECT 1 FROM seguridad.permiso_operacion WHERE codigo = 'AUDITORIA_VER'
  ) THEN
    RAISE EXCEPTION 'No existe permiso AUDITORIA_VER';
  END IF;

  IF NOT EXISTS (
    SELECT 1 FROM seguridad.permiso_operacion WHERE codigo = 'SOPORTE_VER'
  ) THEN
    RAISE EXCEPTION 'No existe permiso SOPORTE_VER';
  END IF;

  IF NOT EXISTS (
    SELECT 1 FROM seguridad.permiso_operacion WHERE codigo = 'SOPORTE_GESTIONAR'
  ) THEN
    RAISE EXCEPTION 'No existe permiso SOPORTE_GESTIONAR';
  END IF;

  IF NOT EXISTS (
    SELECT 1 FROM core.erp_migration_marker WHERE codigo = 'T25_AUDITORIA_SOPORTE_EVIDENCIA'
  ) THEN
    RAISE EXCEPTION 'No existe marcador T25_AUDITORIA_SOPORTE_EVIDENCIA';
  END IF;
END $$;
