import { CommonModule, CurrencyPipe } from "@angular/common";
import { Component, OnInit, computed, inject, signal } from "@angular/core";
import { FormBuilder, ReactiveFormsModule, Validators } from "@angular/forms";
import { ProductsFacadeService } from "./state/products.facade";
import type { ProductSummary } from "./models/product.models";
import { ADMIN_SURFACE_STYLES } from "../../shared/ui/admin-surface.styles";
import { buildBackendAssetUrl } from "../../shared/utils/backend-asset.util";

@Component({
  selector: "app-products-page",
  standalone: true,
  imports: [CommonModule, CurrencyPipe, ReactiveFormsModule],
  template: `
    <div class="admin-grid admin-grid--split">
      <section class="surface-card surface-card--tinted">
        <header class="surface-header">
          <p class="surface-kicker">Carta de la casa</p>
          <div class="surface-title-row">
            <img src="assets/icons/products.svg" alt="" width="28" height="28" aria-hidden="true" />
            <h3>{{ editingProduct() ? 'Editar producto' : 'Nuevo producto' }}</h3>
          </div>
          <p class="surface-copy">Gestión interna del catálogo para vitrina, pedidos y solicitudes personalizadas.</p>
          <div class="chip-row">
            <span class="summary-chip">
              <img src="assets/icons/products.svg" alt="" aria-hidden="true" />
              {{ productPage().totalElements }} productos registrados
            </span>
            <span class="summary-chip" *ngIf="editingProduct()">
              <img src="assets/icons/edit.svg" alt="" aria-hidden="true" />
              Estás corrigiendo {{ editingProduct()?.name }}
            </span>
          </div>
        </header>

        <form [formGroup]="form" (ngSubmit)="submit()" class="surface-form">
          <div class="surface-row surface-row--2">
            <label>Código<input type="text" formControlName="code" /></label>
            <label>Categoría
              <select formControlName="categoryId">
                <option value="">Selecciona una categoría</option>
                <option *ngFor="let category of facade.categories()" [value]="category.id">{{ category.name }}</option>
              </select>
            </label>
          </div>
          <label>Nombre del producto<input type="text" formControlName="name" /></label>
          <label>Descripción<textarea rows="4" formControlName="description"></textarea></label>
          <div class="surface-row surface-row--2">
            <label>Precio base<input type="number" min="0" step="0.01" formControlName="basePrice" /></label>
            <label class="toggle-row">
              <input type="checkbox" formControlName="quotationRequired" />
              Requiere cotización previa
            </label>
          </div>
          <div class="surface-row surface-row--2">
            <label class="toggle-row">
              <input type="checkbox" formControlName="active" />
              Producto activo
            </label>
            <label class="toggle-row">
              <input type="checkbox" formControlName="published" />
              Visible en catálogo público
            </label>
          </div>

          <div class="action-row">
            <button type="submit" class="surface-button" [disabled]="form.invalid">
              {{ editingProduct() ? 'Guardar cambios' : 'Registrar producto' }}
            </button>
            <button type="button" class="mini-button" *ngIf="editingProduct()" (click)="resetForm()">Cancelar edición</button>
          </div>
        </form>
      </section>

      <section class="surface-card">
        <header class="surface-header">
          <p class="surface-kicker">Catálogo administrativo</p>
          <div class="surface-title-row">
            <img src="assets/icons/dashboard.svg" alt="" width="28" height="28" aria-hidden="true" />
            <h3>Productos del sistema</h3>
          </div>
          <p class="surface-copy">{{ activePublishedCount() }} publicados y {{ inactiveCount() }} fuera de vitrina.</p>
        </header>

        <div class="page-toolbar" *ngIf="productPage().totalElements">
          <p class="pager__meta">{{ pageSummary() }}</p>
          <label class="pager__size">
            Tarjetas por página
            <select [value]="productPage().size" (change)="changePageSize($any($event.target).value)">
              <option value="6">6</option>
              <option value="8">8</option>
              <option value="12">12</option>
            </select>
          </label>
        </div>

        <div class="cards-grid" *ngIf="productPage().content.length; else empty">
          <article *ngFor="let product of productPage().content">
            <div class="product-visual">
              <img class="product-visual__logo" [src]="resolveProductImage(product)" [alt]="product.imageAlt" />
              <div class="product-visual__copy">
                <strong>{{ product.slug }}</strong>
                <span>Si reemplazas la imagen del backend con este slug, el panel y la landing la tomarán automáticamente.</span>
              </div>
            </div>
            <div class="chip-row">
              <span class="status-pill">{{ product.categoryName }}</span>
              <span class="summary-chip">{{ product.active ? 'Activo' : 'Inactivo' }}</span>
              <span class="summary-chip">{{ product.published ? 'Publicado' : 'Interno' }}</span>
            </div>
            <h4>{{ product.name }}</h4>
            <p class="card-copy">{{ product.description || 'Producto sin descripción pública.' }}</p>
            <strong class="product-price">{{ product.basePrice | currency:'USD':'symbol':'1.2-2' }}</strong>
            <p class="surface-meta">{{ product.code }} · {{ product.quotationRequired ? 'Con cotización' : 'Venta directa' }}</p>
            <div class="action-row">
              <button type="button" class="mini-button mini-button--icon" (click)="startEdit(product)">
                <img src="assets/icons/edit.svg" alt="" aria-hidden="true" />
                Editar
              </button>
              <button type="button" class="mini-button mini-button--danger" (click)="deleteProduct(product)">
                Eliminar
              </button>
            </div>
          </article>
        </div>

        <ng-template #empty>
          <div class="empty-state">No hay productos cargados todavía.</div>
        </ng-template>

        <div class="pager" *ngIf="productPage().totalElements">
          <p class="pager__meta">Página {{ productPage().page + 1 }} de {{ productPage().totalPages || 1 }}</p>
          <div class="pager__controls">
            <button type="button" class="mini-button" [disabled]="productPage().first" (click)="goToPage(productPage().page - 1)">
              Anterior
            </button>
            <button type="button" class="mini-button" [disabled]="productPage().last" (click)="goToPage(productPage().page + 1)">
              Siguiente
            </button>
          </div>
        </div>
      </section>
    </div>
  `,
  styles: [ADMIN_SURFACE_STYLES, `
    .toggle-row {
      display: flex !important;
      align-items: center;
      gap: 0.65rem;
      min-height: 100%;
    }

    .toggle-row input {
      width: auto;
      margin: 0;
    }

    .mini-button--icon {
      display: inline-flex;
      align-items: center;
      gap: 0.45rem;
    }

    .mini-button--icon img {
      width: 16px;
      height: 16px;
    }

    .product-visual {
      display: grid;
      grid-template-columns: 96px 1fr;
      gap: 0.8rem;
      align-items: center;
      padding: 0.8rem;
      margin-bottom: 0.9rem;
      border: 1px dashed #e7d2c4;
      background: linear-gradient(135deg, rgba(255, 244, 237, 0.98), rgba(252, 235, 225, 0.85));
    }

    .product-visual__logo {
      width: 96px !important;
      height: 72px !important;
      padding: 0 !important;
      border-radius: 4px !important;
      background: transparent !important;
      object-fit: cover;
      border: 1px solid #ead6ca;
    }

    .product-visual__copy {
      display: grid;
      gap: 0.25rem;
      color: #6c564c;
      line-height: 1.45;
    }

    .product-visual__copy strong {
      color: #4d3023;
      font-size: 0.92rem;
    }

    .product-visual__copy span {
      font-size: 0.83rem;
    }

    .product-price {
      font-size: 1.15rem;
      color: #4f2519;
    }

    .mini-button--danger {
      background: #f7ddd8;
      color: #8a2f2c;
    }
  `]
})
export class ProductsPageComponent implements OnInit {
  readonly facade = inject(ProductsFacadeService);
  private readonly fb = inject(FormBuilder);
  readonly editingProduct = signal<ProductSummary | null>(null);
  readonly productPage = computed(() => this.facade.productsPage());
  readonly activePublishedCount = computed(() =>
    this.facade.products().filter((item) => item.active && item.published).length
  );
  readonly inactiveCount = computed(() =>
    this.facade.products().filter((item) => !item.active || !item.published).length
  );

  readonly form = this.fb.nonNullable.group({
    categoryId: ["", Validators.required],
    code: ["", Validators.required],
    name: ["", Validators.required],
    description: [""],
    basePrice: [0, Validators.required],
    quotationRequired: [false],
    active: [true],
    published: [true]
  });

  ngOnInit() {
    this.facade.loadPage();
  }

  submit() {
    if (this.form.invalid) return;
    const value = this.form.getRawValue();
    const payload = {
      categoryId: Number(value.categoryId),
      code: value.code.trim(),
      name: value.name.trim(),
      description: value.description.trim() || null,
      basePrice: Number(value.basePrice),
      quotationRequired: value.quotationRequired,
      active: value.active,
      published: value.published
    };

    const editing = this.editingProduct();
    if (editing) {
      this.facade.updateProduct(editing.id, payload);
    } else {
      this.facade.createProduct(payload);
    }

    this.resetForm();
  }

  startEdit(product: ProductSummary) {
    this.editingProduct.set(product);
    const category = this.facade.categories().find((item) => item.code === product.categoryCode);
    this.form.reset({
      categoryId: category ? String(category.id) : "",
      code: product.code,
      name: product.name,
      description: product.description ?? "",
      basePrice: product.basePrice,
      quotationRequired: product.quotationRequired,
      active: product.active,
      published: product.published
    });
  }

  resetForm() {
    this.editingProduct.set(null);
    this.form.reset({
      categoryId: "",
      code: "",
      name: "",
      description: "",
      basePrice: 0,
      quotationRequired: false,
      active: true,
      published: true
    });
  }

  deleteProduct(product: ProductSummary) {
    if (!window.confirm(`Se eliminará ${product.name} si no tiene pedidos o cotizaciones asociados. ¿Deseas continuar?`)) {
      return;
    }
    this.facade.deleteProduct(product.id);
    if (this.editingProduct()?.id === product.id) {
      this.resetForm();
    }
  }

  goToPage(page: number) {
    this.facade.loadPage(page, this.productPage().size);
  }

  changePageSize(size: string) {
    this.facade.loadPage(0, Number(size));
  }

  resolveProductImage(product: ProductSummary): string {
    return buildBackendAssetUrl(product.imagePath);
  }

  pageSummary(): string {
    const page = this.productPage();
    if (!page.totalElements) {
      return "Sin productos para mostrar.";
    }
    const from = page.page * page.size + 1;
    const to = page.page * page.size + page.numberOfElements;
    return `Mostrando ${from}-${to} de ${page.totalElements} productos.`;
  }
}
