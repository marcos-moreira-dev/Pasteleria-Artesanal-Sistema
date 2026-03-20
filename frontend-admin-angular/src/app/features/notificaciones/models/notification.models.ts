export interface NotificationSummary {
  id: number;
  type: string;
  title: string;
  message: string;
  module: string;
  referenceType: string | null;
  referenceId: string | null;
  status: "NO_LEIDA" | "LEIDA" | "ARCHIVADA";
  priority: "BAJA" | "MEDIA" | "ALTA";
  payloadJson: string;
  createdAt: string;
  readAt: string | null;
}

export interface NotificationCounters {
  unread: number;
}

export interface NotificationActionResult {
  affectedCount: number;
}
