export type ProductionStatusValue =
  | "PENDIENTE"
  | "PREPARACION"
  | "DECORACION"
  | "EMPAQUE"
  | "FINALIZADO"
  | "CANCELADO";

export interface ProductionSummary {
  id: number;
  orderId: number;
  orderCode: string;
  clientName: string;
  status: ProductionStatusValue;
  priority: string;
  startedAt: string | null;
  finishedAt: string | null;
  productionNotes: string | null;
  createdAt: string;
}

export interface UpdateProductionStatusPayload {
  status: ProductionStatusValue;
  reason: string | null;
}

export interface ProductionConsumptionSummary {
  id: number;
  productionId: number;
  orderDetailId: number;
  recetaId: number | null;
  ingredienteId: number;
  ingredienteNombre: string;
  cantidadTeorica: number;
  cantidadConsumida: number;
  costoUnitario: number | null;
  costoTotal: number | null;
  movimientoInventarioId: number | null;
  observaciones: string | null;
  createdAt: string;
}

export interface ProductionBatchSummary {
  id: number;
  productionId: number;
  orderDetailId: number;
  productId: number;
  productName: string;
  codigoLote: string;
  fechaProduccion: string;
  cantidadProducida: number;
  cantidadDisponible: number;
  costoTotalEstimado: number | null;
  estado: string;
  observaciones: string | null;
  createdAt: string;
}

export interface ProductionFinishedEntrySummary {
  id: number;
  batchId: number;
  productionId: number;
  productId: number;
  productName: string;
  cantidad: number;
  costoTotalEstimado: number | null;
  observaciones: string | null;
  createdAt: string;
}
