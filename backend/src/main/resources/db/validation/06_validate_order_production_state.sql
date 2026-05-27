-- T12: validaciones basicas de coherencia entre pedido y produccion.

DO $$
BEGIN
  IF EXISTS (
    SELECT 1
      FROM pedido p
      JOIN produccion pr ON pr.pedido_id = p.pedido_id
     WHERE p.estado_pedido IN ('LISTO', 'ENTREGADO')
       AND pr.estado_produccion <> 'FINALIZADO'
  ) THEN
    RAISE EXCEPTION 'Hay pedidos LISTO/ENTREGADO con produccion no finalizada';
  END IF;

  IF EXISTS (
    SELECT 1
      FROM pedido p
      JOIN produccion pr ON pr.pedido_id = p.pedido_id
     WHERE p.estado_pedido = 'CANCELADO'
       AND pr.estado_produccion NOT IN ('CANCELADO', 'FINALIZADO')
  ) THEN
    RAISE EXCEPTION 'Hay pedidos cancelados con produccion activa';
  END IF;

  IF EXISTS (
    SELECT 1
      FROM produccion pr
      JOIN pedido p ON p.pedido_id = pr.pedido_id
     WHERE pr.estado_produccion = 'FINALIZADO'
       AND p.estado_pedido NOT IN ('LISTO', 'ENTREGADO', 'CANCELADO')
  ) THEN
    RAISE EXCEPTION 'Hay producciones finalizadas sin pedido listo/entregado/cancelado';
  END IF;
END $$;
