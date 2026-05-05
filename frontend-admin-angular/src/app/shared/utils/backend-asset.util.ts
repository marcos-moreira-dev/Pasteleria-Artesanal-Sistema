import { apiConfig } from "../../core/config/api.config";

const backendOrigin = apiConfig.baseUrl.replace(/\/api\/v1\/?$/, "");
const catalogFallbackAsset = "/assets/branding/logo-cuadrado.png";

export function buildBackendAssetUrl(path: string | null | undefined): string {
  if (!path) {
    return `${backendOrigin}${catalogFallbackAsset}`;
  }

  if (/^https?:\/\//.test(path)) {
    return path;
  }

  return `${backendOrigin}${path}`;
}

export function getBackendBrandingAsset(fileName: string): string {
  return `${backendOrigin}/assets/branding/${fileName}`;
}
