-- T13 - Validaciones de recetas técnicas, consumos y lotes.
DO $$
BEGIN
  IF EXISTS (
    SELECT 1
    FROM consumo_material_produccion
    WHERE cantidad_teorica <= 0 OR cantidad_consumida <= 0
  ) THEN
    RAISE EXCEPTION 'Hay consumos de producción con cantidades inválidas';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM consumo_material_produccion c
    WHERE c.movimiento_inventario_id IS NULL
  ) THEN
    RAISE EXCEPTION 'Hay consumos de producción sin movimiento de inventario asociado';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM lote_produccion
    WHERE cantidad_producida <= 0 OR cantidad_disponible < 0
  ) THEN
    RAISE EXCEPTION 'Hay lotes de producción con cantidades inválidas';
  END IF;

  IF EXISTS (
    SELECT codigo_lote
    FROM lote_produccion
    GROUP BY codigo_lote
    HAVING COUNT(*) > 1
  ) THEN
    RAISE EXCEPTION 'Hay códigos de lote duplicados';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM entrada_producto_terminado e
    WHERE NOT EXISTS (
      SELECT 1 FROM lote_produccion l WHERE l.lote_produccion_id = e.lote_produccion_id
    )
  ) THEN
    RAISE EXCEPTION 'Hay entradas de producto terminado sin lote válido';
  END IF;
END $$;
