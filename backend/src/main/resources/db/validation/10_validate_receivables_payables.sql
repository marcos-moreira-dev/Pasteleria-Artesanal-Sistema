DO $$
DECLARE
  invalid_count integer;
BEGIN
  SELECT COUNT(*) INTO invalid_count
  FROM documento_cobrar
  WHERE saldo < 0 OR saldo > total;
  IF invalid_count > 0 THEN
    RAISE EXCEPTION 'Documentos por cobrar con saldo invalido: %', invalid_count;
  END IF;

  SELECT COUNT(*) INTO invalid_count
  FROM documento_cobrar
  WHERE (estado = 'PAGADO' AND saldo <> 0)
     OR (estado = 'PENDIENTE' AND saldo <> total)
     OR (estado = 'PARCIAL' AND NOT (saldo > 0 AND saldo < total));
  IF invalid_count > 0 THEN
    RAISE EXCEPTION 'Documentos por cobrar con estado inconsistente: %', invalid_count;
  END IF;

  SELECT COUNT(*) INTO invalid_count
  FROM cobranza_detalle
  WHERE monto_aplicado <= 0
     OR saldo_posterior < 0
     OR saldo_posterior > saldo_anterior
     OR saldo_anterior - monto_aplicado <> saldo_posterior;
  IF invalid_count > 0 THEN
    RAISE EXCEPTION 'Aplicaciones de cobranza inconsistentes: %', invalid_count;
  END IF;

  SELECT COUNT(*) INTO invalid_count
  FROM documento_pagar
  WHERE saldo < 0 OR saldo > total;
  IF invalid_count > 0 THEN
    RAISE EXCEPTION 'Documentos por pagar con saldo invalido: %', invalid_count;
  END IF;

  SELECT COUNT(*) INTO invalid_count
  FROM documento_pagar
  WHERE (estado = 'PAGADO' AND saldo <> 0)
     OR (estado = 'PENDIENTE' AND saldo <> total)
     OR (estado = 'PAGADO_PARCIAL' AND NOT (saldo > 0 AND saldo < total));
  IF invalid_count > 0 THEN
    RAISE EXCEPTION 'Documentos por pagar con estado inconsistente: %', invalid_count;
  END IF;

  SELECT COUNT(*) INTO invalid_count
  FROM pago_proveedor_aplicacion
  WHERE monto_aplicado <= 0
     OR saldo_posterior < 0
     OR saldo_posterior > saldo_anterior
     OR saldo_anterior - monto_aplicado <> saldo_posterior;
  IF invalid_count > 0 THEN
    RAISE EXCEPTION 'Aplicaciones de pago proveedor inconsistentes: %', invalid_count;
  END IF;
END $$;
