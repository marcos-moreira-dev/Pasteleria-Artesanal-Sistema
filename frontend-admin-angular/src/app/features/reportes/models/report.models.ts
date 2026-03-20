export type ReportTypeValue = "RESUMEN_NEGOCIO" | "COLA_PRODUCCION";

export interface ReportJobSummary {
  id: number;
  jobCode: string;
  reportType: ReportTypeValue;
  status: "PENDIENTE" | "EN_PROCESO" | "COMPLETADO" | "ERROR" | "CANCELADO" | "EXPIRADO";
  attempts: number;
  errorMessage: string | null;
  requestId: string | null;
  requestedByUsername: string | null;
  requestedAt: string;
  startedAt: string | null;
  finishedAt: string | null;
  fileId: number | null;
  fileName: string | null;
}

export interface ReportActionResult {
  affectedCount: number;
}
