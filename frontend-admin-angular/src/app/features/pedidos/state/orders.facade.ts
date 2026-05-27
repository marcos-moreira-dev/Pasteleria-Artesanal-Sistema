import { Injectable, inject } from "@angular/core";
import { BackofficeStoreService } from "../../../core/store/backoffice-store.service";

@Injectable({ providedIn: "root" })
export class OrdersFacadeService {
  private readonly store = inject(BackofficeStoreService);

  readonly clients = this.store.clients;
  readonly products = this.store.products;
  readonly orders = this.store.orders;
  readonly ordersPage = this.store.ordersPage;
  readonly dashboardMetrics = this.store.dashboardMetrics;

  loadPage(page = this.ordersPage().page, size = this.ordersPage().size || 8) {
    this.store.loadOrdersPage(page, size);
  }

  createOrder(payload: Parameters<BackofficeStoreService["createOrder"]>[0], afterSuccess?: () => void) {
    this.store.createOrder(payload, afterSuccess);
  }

  updateOrderStatus(orderId: number, status: string, reason: string | null) {
    this.store.updateOrderStatus(orderId, status, reason);
  }

  deleteOrder(orderId: number) {
    this.store.deleteOrder(orderId);
  }
}
