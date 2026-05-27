\echo '== Validate accounting =='

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM tipo_diario_contable WHERE codigo = 'GENERAL' AND activo = true) THEN
    RAISE EXCEPTION 'Falta diario contable GENERAL activo';
  END IF;

  IF NOT EXISTS (SELECT 1 FROM cuenta_contable WHERE codigo = '1.1.01' AND imputable = true AND activa = true) THEN
    RAISE EXCEPTION 'Falta cuenta imputable Caja principal';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM cuenta_contable
    WHERE imputable = true AND activa = true AND nivel < 2
  ) THEN
    RAISE EXCEPTION 'Hay cuentas imputables de nivel demasiado general';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM asiento_contable
    WHERE total_debe <> total_haber
  ) THEN
    RAISE EXCEPTION 'Hay asientos contables descuadrados';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM asiento_contable_detalle
    WHERE (debe > 0 AND haber > 0) OR (debe = 0 AND haber = 0)
  ) THEN
    RAISE EXCEPTION 'Hay lineas contables con debe/haber invalidos';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM asiento_contable a
    WHERE a.estado = 'REGISTRADO'
      AND NOT EXISTS (
        SELECT 1 FROM asiento_contable_detalle d WHERE d.asiento_contable_id = a.asiento_contable_id
      )
  ) THEN
    RAISE EXCEPTION 'Hay asientos registrados sin lineas';
  END IF;

  IF EXISTS (
    SELECT a.asiento_contable_id
    FROM asiento_contable a
    JOIN asiento_contable_detalle d ON d.asiento_contable_id = a.asiento_contable_id
    GROUP BY a.asiento_contable_id, a.total_debe, a.total_haber
    HAVING SUM(d.debe) <> a.total_debe OR SUM(d.haber) <> a.total_haber OR COUNT(*) < 2
  ) THEN
    RAISE EXCEPTION 'Hay asientos con totales inconsistentes frente al detalle';
  END IF;
END $$;
