import { apiConfig } from "../../core/config/api.config";

const backendOrigin = apiConfig.baseUrl.replace(/\/api\/v1\/?$/, "");

export function buildBackendAssetUrl(path: string | null | undefined): string {
  if (!path) {
    return `${backendOrigin}/assets/placeholders/product-placeholder.png`;
  }

  if (/^https?:\/\//.test(path)) {
    return path;
  }

  return `${backendOrigin}${path}`;
}

export function getBackendBrandingAsset(fileName: string): string {
  return `${backendOrigin}/assets/branding/${fileName}`;
}
