export type ProductionStatusValue =
  | "PENDIENTE"
  | "PREPARACION"
  | "DECORACION"
  | "EMPAQUE"
  | "FINALIZADO";

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
