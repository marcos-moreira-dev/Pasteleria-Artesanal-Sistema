import { CommonModule, CurrencyPipe, DatePipe } from "@angular/common";
import { Component, DestroyRef, OnInit, computed, inject, signal } from "@angular/core";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";
import { FormBuilder, FormControl, ReactiveFormsModule, Validators } from "@angular/forms";
import { distinctUntilChanged, merge } from "rxjs";
import { QuotationsFacadeService } from "./state/quotations.facade";
import { ADMIN_SURFACE_STYLES } from "../../shared/ui/admin-surface.styles";
import { filterClientsByQuery } from "../../shared/utils/client-filter.util";

@Component({
  selector: "app-quotations-page",
  standalone: true,
  imports: [CommonModule, CurrencyPipe, DatePipe, ReactiveFormsModule],
  template: `
    <div class="admin-grid admin-grid--split">
      <section class="surface-card surface-card--tinted">
        <header class="surface-header">
          <p class="surface-kicker">Venta consultiva</p>
          <div class="surface-title-row">
            <img src="assets/icons/quotations.svg" alt="" width="28" height="28" aria-hidden="true" />
            <h3>Nueva cotización</h3>
          </div>
          <p class="surface-copy">Registro rápido para solicitudes artesanales, celebraciones y pedidos especiales.</p>
          <div class="chip-row">
            <span class="summary-chip"><img src="assets/icons/quotations.svg" alt="" aria-hidden="true" /> {{ quotationPage().totalElements }} cotizaciones</span>
              <span class="summary-chip"><img src="assets/icons/clients.svg" alt="" aria-hidden="true" /> {{ facade.clients().length }} clientes disponibles</span>
          </div>
        </header>

        <form [formGroup]="form" (ngSubmit)="submit()" class="surface-form">
          <label>Buscar cliente
            <input type="search" [formControl]="clientSearchControl" placeholder="Filtra por nombre, correo o telefono" />
          </label>

          <label>Cliente
            <select formControlName="clientId">
              <option value="">Selecciona un cliente</option>
              <option *ngFor="let client of filteredClients()" [value]="client.id">{{ client.fullName }}</option>
            </select>
          </label>

          <div class="surface-row surface-row--2">
            <label>Origen
              <select formControlName="origin">
                <option value="INTERNO">Interno</option>
              <option value="PUBLICO">Público</option>
              </select>
            </label>
            <label>Producto
              <select formControlName="productId">
                <option value="">Sin producto fijo</option>
                <option *ngFor="let product of facade.products()" [value]="product.id">{{ product.name }}</option>
              </select>
            </label>
          </div>

          <label>Descripción del ítem<textarea rows="3" formControlName="itemDescription"></textarea></label>

          <div class="surface-row surface-row--3">
            <label>Cantidad<input type="number" min="1" formControlName="quantity" /></label>
            <label>Precio estimado<input type="number" min="0" step="0.01" formControlName="estimatedPrice" /></label>
            <label>Notas internas<input type="text" formControlName="notes" placeholder="Opcional" /></label>
          </div>

          <button type="submit" class="surface-button" [disabled]="form.invalid">Guardar cotización</button>
        </form>
      </section>

      <section class="surface-card">
        <header class="surface-header">
          <p class="surface-kicker">Seguimiento comercial</p>
          <div class="surface-title-row">
            <img src="assets/icons/dashboard.svg" alt="" width="28" height="28" aria-hidden="true" />
            <h3>Cotizaciones activas</h3>
          </div>
          <p class="table-note">Flujo previo al pedido con contexto de cliente, estado comercial y monto estimado.</p>
        </header>

        <div class="page-toolbar" *ngIf="quotationPage().totalElements">
          <p class="pager__meta">{{ pageSummary() }}</p>
          <label class="pager__size">
            Filas por página
            <select [value]="quotationPage().size" (change)="changePageSize($any($event.target).value)">
              <option value="6">6</option>
              <option value="8">8</option>
              <option value="12">12</option>
            </select>
          </label>
        </div>

        <div class="table-shell" *ngIf="quotationPage().content.length; else empty">
          <table class="surface-table surface-table--wide">
            <thead><tr><th>Codigo</th><th>Cliente</th><th>Estado</th><th>Total</th><th>Fecha</th></tr></thead>
            <tbody>
              <tr *ngFor="let quotation of quotationPage().content">
                <td><strong>{{ quotation.code }}</strong></td>
                <td>{{ quotation.clientName }}</td>
                <td><span class="status-pill">{{ quotation.status }}</span></td>
                <td>{{ quotation.estimatedTotal | currency:"USD":"symbol":"1.2-2" }}</td>
                <td>{{ quotation.createdAt | date:"medium" }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <ng-template #empty>
          <div class="empty-state">Todavía no se han registrado cotizaciones en esta sesión.</div>
        </ng-template>

        <div class="pager" *ngIf="quotationPage().totalElements">
          <p class="pager__meta">Página {{ quotationPage().page + 1 }} de {{ quotationPage().totalPages || 1 }}</p>
          <div class="pager__controls">
            <button type="button" class="mini-button" [disabled]="quotationPage().first" (click)="goToPage(quotationPage().page - 1)">
              Anterior
            </button>
            <button type="button" class="mini-button" [disabled]="quotationPage().last" (click)="goToPage(quotationPage().page + 1)">
              Siguiente
            </button>
          </div>
        </div>
      </section>
    </div>
  `,
  styles: [ADMIN_SURFACE_STYLES, `
    .table-shell {
      width: 100%;
      overflow-x: auto;
      padding-bottom: 0.2rem;
    }

    .surface-table--wide {
      min-width: 720px;
    }
  `]
})
export class QuotationsPageComponent implements OnInit {
  readonly facade = inject(QuotationsFacadeService);
  private readonly fb = inject(FormBuilder);
  private readonly destroyRef = inject(DestroyRef);
  readonly quotationPage = computed(() => this.facade.quotationsPage());
  readonly clientSearchControl = new FormControl("", { nonNullable: true });
  private readonly clientSearchQuery = signal("");
  private readonly lastAutoDescription = signal<string | null>(null);
  readonly filteredClients = computed(() => filterClientsByQuery(this.facade.clients(), this.clientSearchQuery()));

  readonly form = this.fb.nonNullable.group({
    clientId: ["", Validators.required],
    origin: ["INTERNO" as "INTERNO" | "PUBLICO", Validators.required],
    itemDescription: ["", Validators.required],
    productId: [""],
    quantity: [1, Validators.required],
    estimatedPrice: [0, Validators.required],
    notes: [""]
  });

  ngOnInit() {
    this.facade.loadPage();

    this.clientSearchControl.valueChanges
      .pipe(distinctUntilChanged(), takeUntilDestroyed(this.destroyRef))
      .subscribe((query) => this.clientSearchQuery.set(query));

    merge(
      this.form.controls.productId.valueChanges,
      this.form.controls.quantity.valueChanges
    )
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.syncCatalogPricing());
  }

  submit() {
    if (this.form.invalid) {
      return;
    }

    const value = this.form.getRawValue();
    const product = this.facade.products().find((item) => item.id === Number(value.productId));
    const description = value.itemDescription.trim() || product?.name || "";

    this.facade.createQuotation({
      clientId: Number(value.clientId),
      origin: value.origin,
      notes: value.notes.trim() || null,
      details: [{
        productId: value.productId ? Number(value.productId) : null,
        itemDescription: description,
        quantity: Number(value.quantity),
        estimatedPrice: Number(value.estimatedPrice),
        notes: null
      }]
    }, () => this.resetForm());
  }

  goToPage(page: number) {
    this.facade.loadPage(page, this.quotationPage().size);
  }

  changePageSize(size: string) {
    this.facade.loadPage(0, Number(size));
  }

  pageSummary(): string {
    const page = this.quotationPage();
    if (!page.totalElements) {
      return "Sin registros para mostrar.";
    }
    const from = page.page * page.size + 1;
    const to = page.page * page.size + page.numberOfElements;
    return `Mostrando ${from}-${to} de ${page.totalElements} cotizaciones.`;
  }

  private syncCatalogPricing() {
    const productId = this.form.controls.productId.getRawValue();
    if (!productId) {
      this.lastAutoDescription.set(null);
      return;
    }

    const product = this.facade.products().find((item) => item.id === Number(productId));
    if (!product) {
      return;
    }

    const quantity = Math.max(1, Number(this.form.controls.quantity.getRawValue()) || 1);
    const currentDescription = this.form.controls.itemDescription.getRawValue().trim();
    const autoDescription = this.lastAutoDescription();

    if (!currentDescription || (autoDescription !== null && currentDescription === autoDescription)) {
      this.form.controls.itemDescription.setValue(product.name, { emitEvent: false });
      this.lastAutoDescription.set(product.name);
    }

    this.form.controls.estimatedPrice.setValue(Number((product.basePrice * quantity).toFixed(2)), { emitEvent: false });
  }

  private resetForm() {
    this.lastAutoDescription.set(null);
    this.form.reset({
      clientId: "",
      origin: "INTERNO",
      itemDescription: "",
      productId: "",
      quantity: 1,
      estimatedPrice: 0,
      notes: ""
    });
    this.clientSearchControl.setValue("", { emitEvent: true });
  }
}
