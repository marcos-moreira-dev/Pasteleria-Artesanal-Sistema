DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM public.inventario_movimiento WHERE cantidad <= 0) THEN
    RAISE EXCEPTION 'Hay movimientos de inventario con cantidad no positiva.';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM public.inventario_movimiento
    WHERE saldo_posterior < 0
  ) THEN
    RAISE EXCEPTION 'Hay movimientos de inventario con saldo posterior negativo.';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM public.inventario_movimiento
    WHERE tipo_movimiento LIKE 'SALIDA_%'
      AND COALESCE(NULLIF(TRIM(motivo_salida), ''), NULLIF(TRIM(observaciones), '')) IS NULL
  ) THEN
    RAISE EXCEPTION 'Hay salidas de inventario sin motivo ni observacion.';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM public.inventario_movimiento
    WHERE COALESCE(NULLIF(TRIM(referencia_tipo), ''), NULLIF(TRIM(referencia_id), '')) IS NULL
  ) THEN
    RAISE EXCEPTION 'Hay movimientos de inventario sin referencia operativa.';
  END IF;
END $$;
