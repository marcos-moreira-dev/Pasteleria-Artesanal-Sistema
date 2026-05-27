DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM public.usuario_sistema WHERE nombre_usuario = 'admin') THEN
    RAISE EXCEPTION 'Falta usuario admin.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM public.producto WHERE activo = true) THEN
    RAISE EXCEPTION 'Faltan productos activos.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM public.caso_uso_operativo WHERE activo = true) THEN
    RAISE EXCEPTION 'Faltan casos de uso activos.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM public.caja_operativa WHERE codigo = 'CAJA_MATRIZ' AND activa = true) THEN
    RAISE EXCEPTION 'Falta caja principal CAJA_MATRIZ.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM core.organizacion_empresa WHERE codigo = 'PASTELERIA_BASE') THEN
    RAISE EXCEPTION 'Falta empresa base ERP.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM core.sucursal_operativa WHERE codigo = 'MATRIZ') THEN
    RAISE EXCEPTION 'Falta sucursal principal ERP.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM seguridad.rol_operacion WHERE codigo = 'ADMIN_GLOBAL') THEN
    RAISE EXCEPTION 'Falta rol ADMIN_GLOBAL ERP.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM seguridad.permiso_operacion WHERE codigo = 'CONTRATOS_API_VER') THEN
    RAISE EXCEPTION 'Falta permiso CONTRATOS_API_VER ERP.';
  END IF;


  IF NOT EXISTS (SELECT 1 FROM public.tipo_diario_contable WHERE codigo = 'GENERAL') THEN
    RAISE EXCEPTION 'Falta diario contable GENERAL.';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM public.cuenta_contable WHERE codigo = '1.1.01' AND activa = true) THEN
    RAISE EXCEPTION 'Falta cuenta contable Caja principal.';
  END IF;
END $$;
