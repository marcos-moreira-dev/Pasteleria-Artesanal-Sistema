import { Injectable, inject } from "@angular/core";
import { BackofficeStoreService } from "../../../core/store/backoffice-store.service";

@Injectable({ providedIn: "root" })
export class ProductionFacadeService {
  private readonly store = inject(BackofficeStoreService);

  readonly productionPage = this.store.productionPage;

  loadPage(page = this.productionPage().page, size = this.productionPage().size || 8) {
    this.store.loadProductionPage(page, size);
  }

  updateProductionStatus(
      productionId: number,
      status: Parameters<BackofficeStoreService["updateProductionStatus"]>[1],
      reason: string | null,
      successMessage?: string
  ) {
    this.store.updateProductionStatus(productionId, status, reason, successMessage);
  }
}
