export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  errorCode: string | null;
  requestId: string | null;
  timestamp: string;
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
}

export interface ProductCategorySummary {
  id: number;
  code: string;
  name: string;
  description: string | null;
  visualOrder: number;
}

export interface BrandingAssets {
  logoSquarePath: string;
  logoHorizontalPath: string;
  bannerPath: string;
  productPlaceholderPath: string;
  categoryBannerPath: string;
}

const publicApiBaseUrl =
  import.meta.env.PUBLIC_API_BASE_URL ?? "http://localhost:8080/api/v1";
const publicAssetBaseUrl = publicApiBaseUrl.replace(/\/api\/v1\/?$/, "");

export function buildPublicApiUrl(path: string): string {
  return `${publicApiBaseUrl}${path}`;
}

export function buildPublicAssetUrl(path: string | null | undefined): string {
  if (!path) {
    return `${publicAssetBaseUrl}/assets/branding/logo-placeholder.svg`;
  }

  if (/^https?:\/\//.test(path)) {
    return path;
  }

  return `${publicAssetBaseUrl}${path}`;
}

async function getJson<T>(path: string): Promise<T | null> {
  try {
    const response = await fetch(buildPublicApiUrl(path));
    if (!response.ok) {
      return null;
    }

    const payload = (await response.json()) as ApiResponse<T>;
    return payload.data;
  } catch {
    return null;
  }
}

export async function getPublishedProducts(): Promise<ProductSummary[]> {
  return (await getJson<ProductSummary[]>("/public/catalogo/productos")) ?? [];
}

export async function getActiveCategories(): Promise<ProductCategorySummary[]> {
  return (
    (await getJson<ProductCategorySummary[]>("/public/catalogo/categorias")) ??
    []
  );
}

export async function getBrandingAssets(): Promise<BrandingAssets> {
  return (
    (await getJson<BrandingAssets>("/public/catalogo/branding")) ?? {
      logoSquarePath: "/assets/branding/logo-placeholder.svg",
      logoHorizontalPath: "/assets/branding/logo-placeholder.svg",
      bannerPath: "/assets/placeholders/landing-placeholder.png",
      productPlaceholderPath: "/assets/branding/logo-placeholder.svg",
      categoryBannerPath: "/assets/placeholders/landing-placeholder.png",
    }
  );
}
