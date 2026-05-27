export interface OrderDetailSummary {
  id: number;
  productId: number;
  itemDescription: string;
  quantity: number;
  unitPrice: number;
  subtotal: number;
  notes: string | null;
}

export interface OrderSummary {
  id: number;
  code: string;
  clientId: number;
  clientName: string;
  quotationId: number | null;
  status: string;
  priority: string;
  origin: string;
  orderDate: string;
  estimatedDeliveryAt: string;
  actualDeliveryAt: string | null;
  notes: string | null;
  estimatedTotal: number;
  productionStatus: string | null;
  productionPriority: string | null;
  details: OrderDetailSummary[];
}

export interface CreateOrderDetailPayload {
  productId: number;
  itemDescription: string | null;
  quantity: number;
  unitPrice: number;
  notes: string | null;
}

export interface CreateOrderPayload {
  clientId: number;
  quotationId: number | null;
  estimatedDeliveryAt: string;
  priority: "NORMAL" | "URGENTE";
  origin: "PUBLICO" | "INTERNO";
  notes: string | null;
  details: CreateOrderDetailPayload[];
}

export interface UpdateOrderStatusPayload {
  status: string;
  reason: string | null;
}
