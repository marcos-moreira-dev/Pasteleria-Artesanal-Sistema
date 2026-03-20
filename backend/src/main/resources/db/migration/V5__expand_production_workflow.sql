ALTER TABLE produccion
  DROP CONSTRAINT ck_produccion_estado;

UPDATE produccion
SET estado_produccion = 'PREPARACION'
WHERE estado_produccion = 'EN_PROCESO';

ALTER TABLE produccion
  ADD CONSTRAINT ck_produccion_estado CHECK (
    estado_produccion IN ('PENDIENTE', 'PREPARACION', 'DECORACION', 'EMPAQUE', 'FINALIZADO')
  );
