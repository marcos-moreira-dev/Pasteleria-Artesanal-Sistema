export interface QuotationDetailSummary {
  id: number;
  productId: number | null;
  itemDescription: string;
  quantity: number;
  estimatedPrice: number;
  subtotal: number;
  notes: string | null;
}

export interface QuotationSummary {
  id: number;
  code: string;
  clientId: number;
  clientName: string;
  status: string;
  origin: string;
  notes: string | null;
  estimatedTotal: number;
  createdAt: string;
  details: QuotationDetailSummary[];
}

export interface CreateQuotationDetailPayload {
  productId: number | null;
  itemDescription: string;
  quantity: number;
  estimatedPrice: number;
  notes: string | null;
}

export interface CreateQuotationPayload {
  clientId: number;
  origin: "PUBLICO" | "INTERNO";
  notes: string | null;
  details: CreateQuotationDetailPayload[];
}
