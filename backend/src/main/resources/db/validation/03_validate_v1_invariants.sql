DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM public.ingrediente WHERE stock_actual < 0) THEN
    RAISE EXCEPTION 'Hay ingredientes con stock negativo.';
  END IF;

  IF EXISTS (SELECT 1 FROM public.insumo WHERE stock_actual < 0) THEN
    RAISE EXCEPTION 'Hay insumos con stock negativo.';
  END IF;

  IF EXISTS (
    SELECT 1 FROM public.caso_uso_operativo cu
    WHERE cu.activo = true
      AND NOT EXISTS (SELECT 1 FROM public.paso_caso_uso p WHERE p.caso_uso_id = cu.caso_uso_id)
  ) THEN
    RAISE EXCEPTION 'Hay casos de uso activos sin pasos.';
  END IF;

  IF EXISTS (SELECT 1 FROM public.job_reporte WHERE estado = 'COMPLETADO' AND archivo_id IS NULL) THEN
    RAISE EXCEPTION 'Hay reportes completados sin archivo.';
  END IF;
END $$;
