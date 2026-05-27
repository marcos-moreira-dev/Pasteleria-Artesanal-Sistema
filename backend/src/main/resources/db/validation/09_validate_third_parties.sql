-- =============================================================================
-- T15 - Validacion de terceros unificados transicionales
-- =============================================================================
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM cliente WHERE tercero_id IS NULL) THEN
    RAISE EXCEPTION 'Hay clientes sin tercero_id sincronizado';
  END IF;

  IF EXISTS (SELECT 1 FROM proveedor WHERE tercero_id IS NULL) THEN
    RAISE EXCEPTION 'Hay proveedores sin tercero_id sincronizado';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM cliente c
    WHERE NOT EXISTS (
      SELECT 1 FROM cliente_perfil cp
      WHERE cp.cliente_id = c.cliente_id
        AND cp.tercero_id = c.tercero_id
    )
  ) THEN
    RAISE EXCEPTION 'Hay clientes sin perfil cliente asociado al mismo tercero';
  END IF;

  IF EXISTS (
    SELECT 1
    FROM proveedor p
    WHERE NOT EXISTS (
      SELECT 1 FROM proveedor_perfil pp
      WHERE pp.proveedor_id = p.proveedor_id
        AND pp.tercero_id = p.tercero_id
    )
  ) THEN
    RAISE EXCEPTION 'Hay proveedores sin perfil proveedor asociado al mismo tercero';
  END IF;
END $$;
