import { Injectable, inject } from "@angular/core";
import { BackofficeStoreService } from "../../../core/store/backoffice-store.service";

@Injectable({ providedIn: "root" })
export class ClientsFacadeService {
  private readonly store = inject(BackofficeStoreService);

  readonly clientsPage = this.store.clientsPage;
  readonly clientSearchQuery = this.store.clientSearchQuery;

  loadPage(page = this.clientsPage().page, size = this.clientsPage().size || 8, query = this.clientSearchQuery()) {
    this.store.loadClientsPage(page, size, query);
  }

  createClient(payload: Parameters<BackofficeStoreService["createClient"]>[0], afterSuccess?: () => void) {
    this.store.createClient(payload, afterSuccess);
  }

  updateClient(clientId: number, payload: Parameters<BackofficeStoreService["updateClient"]>[1], afterSuccess?: () => void) {
    this.store.updateClient(clientId, payload, afterSuccess);
  }

  deleteClient(clientId: number) {
    this.store.deleteClient(clientId);
  }
}
