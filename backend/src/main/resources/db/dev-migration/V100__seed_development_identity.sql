-- =============================================================================
-- DEV SEED - Identidad local de desarrollo
-- Tanda T07: placeholder controlado.
--
-- La V1 ya contiene usuarios de arranque. Este archivo existe para separar
-- oficialmente datos dev de migraciones productivas. Las tandas T09/T24 podrán
-- ampliarlo sin contaminar V1/V2.
-- =============================================================================

DO $$
BEGIN
  RAISE NOTICE 'V100 dev identity seed: V1 ya contiene usuarios locales base.';
END $$;
