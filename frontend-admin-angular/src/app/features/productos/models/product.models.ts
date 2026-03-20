export interface ProductCategorySummary {
  id: number;
  code: string;
  name: string;
  description: string | null;
  visualOrder: number;
}

export interface ProductSummary {
  id: number;
  code: string;
  slug: string;
  name: string;
  description: string | null;
  basePrice: number;
  quotationRequired: boolean;
  categoryCode: string;
  categoryName: string;
  imagePath: string;
  imageAlt: string;
  active: boolean;
  published: boolean;
}

export interface CreateProductPayload {
  categoryId: number;
  code: string;
  name: string;
  description: string | null;
  basePrice: number;
  quotationRequired: boolean;
  active: boolean;
  published: boolean;
}

export interface UpdateProductPayload extends CreateProductPayload {
}
