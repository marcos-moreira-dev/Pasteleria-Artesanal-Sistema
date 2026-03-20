import { Injectable, inject } from "@angular/core";
import { BackofficeStoreService } from "../../../core/store/backoffice-store.service";

@Injectable({ providedIn: "root" })
export class DashboardFacadeService {
  private readonly store = inject(BackofficeStoreService);

  readonly products = this.store.products;
  readonly categories = this.store.categories;
  readonly quotations = this.store.quotations;
  readonly orders = this.store.orders;
  readonly productionQueue = this.store.productionQueue;
  readonly reportJobs = this.store.reportJobs;
  readonly dashboardMetrics = this.store.dashboardMetrics;

  requestReport(reportType: "RESUMEN_NEGOCIO" | "COLA_PRODUCCION") {
    this.store.requestReport(reportType);
  }
}
