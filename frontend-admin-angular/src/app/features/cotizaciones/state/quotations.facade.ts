import { Injectable, inject } from "@angular/core";
import { BackofficeStoreService } from "../../../core/store/backoffice-store.service";

@Injectable({ providedIn: "root" })
export class QuotationsFacadeService {
  private readonly store = inject(BackofficeStoreService);

  readonly clients = this.store.clients;
  readonly products = this.store.products;
  readonly quotationsPage = this.store.quotationsPage;

  loadPage(page = this.quotationsPage().page, size = this.quotationsPage().size || 8) {
    this.store.loadQuotationsPage(page, size);
  }

  createQuotation(payload: Parameters<BackofficeStoreService["createQuotation"]>[0], afterSuccess?: () => void) {
    this.store.createQuotation(payload, afterSuccess);
  }
}
