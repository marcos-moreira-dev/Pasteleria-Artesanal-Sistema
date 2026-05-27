export interface ApiMeta {
  requestId: string | null;
  timestamp: string;
}

export interface ApiErrorDetail {
  field: string | null;
  message: string;
}

export interface ApiError {
  code: string;
  message: string;
  details: ApiErrorDetail[];
}

export interface ApiResponse<T> {
  // Contrato nuevo tipo Cedro.
  ok?: boolean;
  data: T;
  error?: ApiError | null;
  meta?: ApiMeta | null;

  // Campos legacy conservados durante la transición backend/frontend.
  success: boolean;
  message: string;
  errorCode: string | null;
  requestId: string | null;
  timestamp: string;
}

export interface PageResponseDto<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  numberOfElements: number;
  first: boolean;
  last: boolean;
  sort: string | null;
}

export interface SucursalOperable {
  id: string;
  codigo: string;
  nombre: string;
  principal: boolean;
  permisos: string[];
}

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
  userId?: number;
  username: string;
  displayName?: string;
  role: string;
  rolesGlobales?: string[];
  roles?: string[];
  permisosGlobales?: string[];
  permisos?: string[];
  sucursalesOperables?: SucursalOperable[];
}

export type AuthenticatedUserContext = Omit<AuthResponse, "accessToken" | "tokenType" | "expiresIn">;

export interface EndpointContract {
  method: string;
  path: string;
  module: string;
  summary: string;
  requiredPermission: string | null;
  scope: string;
  publicEndpoint: boolean;
  paginated: boolean;
}

export interface PermissionContract {
  code: string;
  module: string;
  description: string;
  scope: string;
}

export interface EnumContract {
  name: string;
  values: string[];
}

export interface PaginationContract {
  pageParam: string;
  sizeParam: string;
  defaultPage: number;
  defaultSize: number;
  maxSize: number;
  responseType: string;
}

export interface ApiContractSnapshot {
  endpoints: EndpointContract[];
  permissions: PermissionContract[];
  enums: EnumContract[];
  pagination: PaginationContract;
}
