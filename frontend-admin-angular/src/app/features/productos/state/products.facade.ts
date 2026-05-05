import { Injectable, inject } from "@angular/core";
import { BackofficeStoreService } from "../../../core/store/backoffice-store.service";
import { ApiClientService } from "../../../core/api/api-client.service";
import { Observable } from "rxjs";

@Injectable({ providedIn: "root" })
export class ProductsFacadeService {
  private readonly store = inject(BackofficeStoreService);
  private readonly api = inject(ApiClientService);

  readonly products = this.store.products;
  readonly productsPage = this.store.productsPage;
  readonly categories = this.store.categories;

  loadPage(page = this.productsPage().page, size = this.productsPage().size || 8) {
    this.store.loadProductsPage(page, size);
  }

  createProduct(payload: Parameters<BackofficeStoreService["createProduct"]>[0], afterSuccess?: () => void) {
    this.store.createProduct(payload, afterSuccess);
  }

  updateProduct(productId: number, payload: Parameters<BackofficeStoreService["updateProduct"]>[1], afterSuccess?: () => void) {
    this.store.updateProduct(productId, payload, afterSuccess);
  }

  deleteProduct(productId: number) {
    this.store.deleteProduct(productId);
  }

  uploadProductImage(productId: number, formData: FormData): Observable<string> {
    return this.api.uploadProductImage(productId, formData);
  }

  downloadRecipePdf(productId: number): Observable<Blob> {
    return this.api.downloadRecipePdf(productId);
  }
}
