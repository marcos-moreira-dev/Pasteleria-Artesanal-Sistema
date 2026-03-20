import { Injectable, inject } from "@angular/core";
import { BackofficeStoreService } from "../../../core/store/backoffice-store.service";

@Injectable({ providedIn: "root" })
export class ProductsFacadeService {
  private readonly store = inject(BackofficeStoreService);

  readonly products = this.store.products;
  readonly productsPage = this.store.productsPage;
  readonly categories = this.store.categories;

  loadPage(page = this.productsPage().page, size = this.productsPage().size || 8) {
    this.store.loadProductsPage(page, size);
  }

  createProduct(payload: Parameters<BackofficeStoreService["createProduct"]>[0]) {
    this.store.createProduct(payload);
  }

  updateProduct(productId: number, payload: Parameters<BackofficeStoreService["updateProduct"]>[1]) {
    this.store.updateProduct(productId, payload);
  }

  deleteProduct(productId: number) {
    this.store.deleteProduct(productId);
  }
}
