export interface PasoCasoUsoResponse {
  numero: number;
  descripcion: string;
}

export interface CasoUsoOperativoResponse {
  id: number;
  codigo: string;
  modulo: string;
  titulo: string;
  actorPrincipal: string | null;
  objetivo: string | null;
  puntoInicio: string | null;
  ordenVisual: number | null;
  estado: string | null;
  version: number | null;
  activo: boolean;
  pasos: PasoCasoUsoResponse[];
}

export interface CasoUsoModuloResponse {
  codigo: string;
  nombre: string;
  descripcion: string | null;
  grupo: string | null;
  ordenVisual: number | null;
  casos: CasoUsoOperativoResponse[];
}

export interface CasoUsoHubResponse {
  totalCasos: number;
  modulos: CasoUsoModuloResponse[];
}
