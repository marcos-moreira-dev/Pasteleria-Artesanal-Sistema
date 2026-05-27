import { Injectable, inject } from "@angular/core";
import { BackofficeStoreService } from "../../../core/store/backoffice-store.service";

@Injectable({ providedIn: "root" })
export class ReportsFacadeService {
  private readonly store = inject(BackofficeStoreService);

  readonly reportJobsPage = this.store.reportJobsPage;

  loadPage(page = this.reportJobsPage().page, size = this.reportJobsPage().size || 8) {
    this.store.loadReportJobsPage(page, size);
  }

  requestReport(reportType: "RESUMEN_NEGOCIO" | "COLA_PRODUCCION") {
    this.store.requestReport(reportType);
  }

  downloadReport(jobId: number, fileName: string | null) {
    this.store.downloadReport(jobId, fileName);
  }

  deleteSelectedReports(jobIds: number[]) {
    this.store.deleteSelectedReportJobs(jobIds);
  }

  cleanupOldReports(keepLatest = 10) {
    this.store.cleanupOldReportJobs(keepLatest);
  }
}
