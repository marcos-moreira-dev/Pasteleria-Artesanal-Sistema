-- =============================================================================
-- T11 - Validacion de caja operativa
-- =============================================================================
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'caja_operativa') THEN
    RAISE EXCEPTION 'No existe la tabla caja_operativa';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM caja_operativa WHERE codigo = 'CAJA_MATRIZ' AND activa = TRUE) THEN
    RAISE EXCEPTION 'No existe la caja principal activa CAJA_MATRIZ';
  END IF;

  IF EXISTS (
    SELECT caja_id
    FROM turno_caja
    WHERE estado = 'ABIERTO'
    GROUP BY caja_id
    HAVING COUNT(*) > 1
  ) THEN
    RAISE EXCEPTION 'Hay cajas con mas de un turno abierto';
  END IF;

  IF EXISTS (SELECT 1 FROM movimiento_caja WHERE monto <= 0) THEN
    RAISE EXCEPTION 'Hay movimientos de caja con monto no positivo';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM movimiento_caja m
    JOIN turno_caja t ON t.turno_caja_id = m.turno_caja_id
    WHERE t.estado = 'CERRADO'
      AND m.estado = 'REGISTRADO'
      AND m.fecha_movimiento > t.fecha_cierre
  ) THEN
    RAISE EXCEPTION 'Hay movimientos registrados despues del cierre del turno';
  END IF;
END $$;
