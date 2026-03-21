export interface ProductCategorySummary {
  id: number;
  code: string;
  name: string;
  description: string | null;
  visualOrder: number;
}

export interface RecetaJson {
  titulo: string;
  tituloIngredientes: string;
  ingredientes: string;
  tituloPasos: string;
  pasos: string;
  tituloObservaciones: string;
  observaciones: string;
}

export interface ProductSummary {
  id: number;
  code: string;
  slug: string;
  name: string;
  description: string | null;
  receta: RecetaJson | null;
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
  receta: RecetaJson | null;
  basePrice: number;
  quotationRequired: boolean;
  active: boolean;
  published: boolean;
}

export interface UpdateProductPayload extends CreateProductPayload {}
