import { CommonModule, CurrencyPipe, DatePipe } from "@angular/common";
import { Component, DestroyRef, OnInit, computed, inject, signal } from "@angular/core";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";
import { FormBuilder, FormControl, ReactiveFormsModule, Validators } from "@angular/forms";
import { distinctUntilChanged } from "rxjs";
import { OrdersFacadeService } from "./state/orders.facade";
import type { OrderSummary } from "./models/order.models";
import { ADMIN_SURFACE_STYLES } from "../../shared/ui/admin-surface.styles";
import { filterClientsByQuery } from "../../shared/utils/client-filter.util";

@Component({
  selector: "app-orders-page",
  standalone: true,
  imports: [CommonModule, CurrencyPipe, DatePipe, ReactiveFormsModule],
  template: `
    <div class="admin-grid admin-grid--split">
      <section class="surface-card surface-card--tinted">
        <header class="surface-header">
          <p class="surface-kicker">Operación comercial</p>
          <div class="surface-title-row">
            <img src="assets/icons/orders.svg" alt="" width="28" height="28" aria-hidden="true" />
            <h3>Nuevo pedido</h3>
          </div>
          <p class="surface-copy">Registro operativo conectado con producción desde el primer guardado.</p>
          <div class="chip-row">
            <span class="summary-chip">
              <img src="assets/icons/orders.svg" alt="" aria-hidden="true" />
              {{ activeOrdersCount() }} pedidos activos
            </span>
            <span class="summary-chip">
              <img src="assets/icons/production.svg" alt="" aria-hidden="true" />
              {{ facade.dashboardMetrics().activeProduction }} frentes en producción
            </span>
          </div>
        </header>

        <form [formGroup]="form" (ngSubmit)="submit()" class="surface-form">
          <label>Buscar cliente
            <input type="search" [formControl]="clientSearchControl" placeholder="Filtra por nombre, correo o teléfono" />
          </label>

          <div class="surface-row surface-row--2">
            <label>Cliente
              <select formControlName="clientId">
                <option value="">Selecciona un cliente</option>
                <option *ngFor="let client of filteredClients()" [value]="client.id">{{ client.fullName }}</option>
              </select>
            </label>
            <label>Prioridad
              <select formControlName="priority">
                <option value="NORMAL">Normal</option>
                <option value="URGENTE">Urgente</option>
              </select>
            </label>
          </div>

          <div class="surface-row surface-row--3">
            <label>Producto
              <select formControlName="productId">
                <option value="">Selecciona un producto</option>
                <option *ngFor="let product of availableProducts()" [value]="product.id">{{ product.name }}</option>
              </select>
            </label>
            <label>Cantidad<input type="number" min="1" formControlName="quantity" /></label>
            <label>Precio unitario<input type="number" min="0" step="0.01" formControlName="unitPrice" /></label>
          </div>

          <label>Fecha estimada de entrega<input type="datetime-local" formControlName="estimatedDeliveryAt" /></label>
          <label>Observaciones<textarea rows="3" formControlName="notes"></textarea></label>
          <button type="submit" class="surface-button" [disabled]="form.invalid">Guardar pedido</button>
        </form>
      </section>

      <section class="surface-card">
        <header class="surface-header">
          <p class="surface-kicker">Coordinación diaria</p>
          <div class="surface-title-row">
            <img src="assets/icons/dashboard.svg" alt="" width="28" height="28" aria-hidden="true" />
            <h3>Pedidos en seguimiento</h3>
          </div>
          <p class="table-note">Vista comercial y operativa para despacho, preparación, rechazo, cancelación y depuración.</p>
        </header>

        <div class="page-toolbar" *ngIf="ordersPage().totalElements">
          <p class="pager__meta">{{ pageSummary() }}</p>
          <label class="pager__size">
            Filas por página
            <select [value]="ordersPage().size" (change)="changePageSize($any($event.target).value)">
              <option value="6">6</option>
              <option value="8">8</option>
              <option value="12">12</option>
            </select>
          </label>
        </div>

        <div class="table-shell" *ngIf="ordersPage().content.length; else empty">
          <table class="surface-table surface-table--wide">
            <thead>
              <tr>
                <th>Pedido</th>
                <th>Cliente</th>
                <th>Estado</th>
                <th>Entrega</th>
                <th>Total</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let order of ordersPage().content">
                <td>
                  <strong>{{ order.code }}</strong>
                  <p class="surface-meta">{{ order.priority }} · {{ order.origin }}</p>
                </td>
                <td>{{ order.clientName }}</td>
                <td>
                  <span class="status-pill">{{ prettyOrderStatus(order.status) }}</span>
                  <p class="surface-meta">{{ order.productionStatus ? prettyProductionStatus(order.productionStatus) : "Sin producción asociada" }}</p>
                  <div class="action-row">
                    <button type="button" class="mini-button" *ngIf="order.status === 'LISTO'" (click)="moveOrder(order.id, 'ENTREGADO', 'Entrega al cliente')">
                      Entregar
                    </button>
                    <button type="button" class="mini-button mini-button--warning" *ngIf="canCancel(order)" (click)="cancelOrder(order)">
                      {{ order.status === "REGISTRADO" ? "Rechazar" : "Cancelar" }}
                    </button>
                    <button type="button" class="mini-button mini-button--danger" *ngIf="order.status === 'CANCELADO'" (click)="deleteOrder(order)">
                      Eliminar
                    </button>
                  </div>
                </td>
                <td>{{ order.estimatedDeliveryAt | date:"medium" }}</td>
                <td>{{ order.estimatedTotal | currency:"USD":"symbol":"1.2-2" }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <ng-template #empty>
          <div class="empty-state">Todavía no se han registrado pedidos en esta sesión.</div>
        </ng-template>

        <div class="pager" *ngIf="ordersPage().totalElements">
          <p class="pager__meta">Página {{ ordersPage().page + 1 }} de {{ ordersPage().totalPages || 1 }}</p>
          <div class="pager__controls">
            <button type="button" class="mini-button" [disabled]="ordersPage().first" (click)="goToPage(ordersPage().page - 1)">
              Anterior
            </button>
            <button type="button" class="mini-button" [disabled]="ordersPage().last" (click)="goToPage(ordersPage().page + 1)">
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
      min-width: 860px;
    }

    .mini-button--warning {
      background: #f5e8cc;
      color: #885b13;
    }

    .mini-button--danger {
      background: #f7ddd8;
      color: #8a2f2c;
    }
  `]
})
export class OrdersPageComponent implements OnInit {
  readonly facade = inject(OrdersFacadeService);
  private readonly fb = inject(FormBuilder);
  private readonly destroyRef = inject(DestroyRef);
  readonly ordersPage = computed(() => this.facade.ordersPage());
  readonly availableProducts = computed(() =>
    this.facade.products().filter((item) => item.active)
  );
  readonly activeOrdersCount = computed(() =>
    this.facade.orders().filter((item) => item.status !== "CANCELADO" && item.status !== "ENTREGADO").length
  );
  readonly clientSearchControl = new FormControl("", { nonNullable: true });
  private readonly clientSearchQuery = signal("");
  readonly filteredClients = computed(() => filterClientsByQuery(this.facade.clients(), this.clientSearchQuery()));

  readonly form = this.fb.nonNullable.group({
    clientId: ["", Validators.required],
    priority: ["NORMAL" as "NORMAL" | "URGENTE", Validators.required],
    productId: ["", Validators.required],
    quantity: [1, Validators.required],
    unitPrice: [0, Validators.required],
    estimatedDeliveryAt: [buildDefaultDeliverySlot(), Validators.required],
    notes: [""]
  });

  ngOnInit() {
    this.facade.loadPage();

    this.clientSearchControl.valueChanges
      .pipe(distinctUntilChanged(), takeUntilDestroyed(this.destroyRef))
      .subscribe((query) => this.clientSearchQuery.set(query));

    this.form.controls.productId.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((productId) => this.syncSelectedProduct(productId));
  }

  submit() {
    if (this.form.invalid) {
      return;
    }

    const value = this.form.getRawValue();
    const product = this.facade.products().find((item) => item.id === Number(value.productId));
    const estimatedDeliveryAt = toOffsetDateTime(value.estimatedDeliveryAt);
    if (!estimatedDeliveryAt) {
      return;
    }

    this.facade.createOrder({
      clientId: Number(value.clientId),
      quotationId: null,
      estimatedDeliveryAt,
      priority: value.priority,
      origin: "INTERNO",
      notes: value.notes.trim() || null,
      details: [{
        productId: Number(value.productId),
        itemDescription: product?.name ?? null,
        quantity: Number(value.quantity),
        unitPrice: Number(value.unitPrice),
        notes: null
      }]
    }, () => this.resetForm());
  }

  moveOrder(orderId: number, status: string, reason: string) {
    this.facade.updateOrderStatus(orderId, status, reason);
  }

  canCancel(order: OrderSummary): boolean {
    return order.status !== "ENTREGADO" && order.status !== "CANCELADO";
  }

  cancelOrder(order: OrderSummary) {
    const isRejected = order.status === "REGISTRADO";
    const actionLabel = isRejected ? "rechazará" : "cancelará";
    const reason = isRejected
      ? "Pedido rechazado desde administración antes de entrar al flujo final."
      : "Cancelación administrativa del pedido.";

    if (!window.confirm(`Se ${actionLabel} ${order.code}. Luego podrás eliminarlo del tablero. ¿Deseas continuar?`)) {
      return;
    }
    this.facade.updateOrderStatus(order.id, "CANCELADO", reason);
  }

  deleteOrder(order: OrderSummary) {
    if (!window.confirm(`Se eliminará definitivamente ${order.code}. Esta acción solo se permite porque ya está cancelado. ¿Deseas continuar?`)) {
      return;
    }
    this.facade.deleteOrder(order.id);
  }

  goToPage(page: number) {
    this.facade.loadPage(page, this.ordersPage().size);
  }

  changePageSize(size: string) {
    this.facade.loadPage(0, Number(size));
  }

  pageSummary(): string {
    const page = this.ordersPage();
    if (!page.totalElements) {
      return "Sin pedidos para mostrar.";
    }
    const from = page.page * page.size + 1;
    const to = page.page * page.size + page.numberOfElements;
    return `Mostrando ${from}-${to} de ${page.totalElements} pedidos.`;
  }

  prettyOrderStatus(status: string): string {
    return status.toLowerCase().split("_").map((item) => item.charAt(0).toUpperCase() + item.slice(1)).join(" ");
  }

  prettyProductionStatus(status: string): string {
    return status.toLowerCase().split("_").map((item) => item.charAt(0).toUpperCase() + item.slice(1)).join(" ");
  }

  private syncSelectedProduct(productId: string) {
    if (!productId) {
      this.form.controls.unitPrice.setValue(0, { emitEvent: false });
      return;
    }

    const product = this.facade.products().find((item) => item.id === Number(productId));
    if (!product) {
      return;
    }

    this.form.controls.unitPrice.setValue(Number(product.basePrice.toFixed(2)), { emitEvent: false });
  }

  private resetForm() {
    this.form.reset({
      clientId: "",
      priority: "NORMAL",
      productId: "",
      quantity: 1,
      unitPrice: 0,
      estimatedDeliveryAt: buildDefaultDeliverySlot(),
      notes: ""
    });
    this.clientSearchControl.setValue("", { emitEvent: true });
  }
}

function buildDefaultDeliverySlot(): string {
  const date = new Date();
  date.setDate(date.getDate() + 1);
  date.setHours(10, 0, 0, 0);
  return toLocalDateTimeInputValue(date);
}

function toLocalDateTimeInputValue(date: Date): string {
  const pad = (value: number) => String(value).padStart(2, "0");
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
}

function toOffsetDateTime(localValue: string): string | null {
  if (!localValue) {
    return null;
  }

  const date = new Date(localValue);
  if (Number.isNaN(date.getTime())) {
    return null;
  }

  const pad = (value: number) => String(value).padStart(2, "0");
  const offsetMinutes = -date.getTimezoneOffset();
  const sign = offsetMinutes >= 0 ? "+" : "-";
  const absoluteOffset = Math.abs(offsetMinutes);
  const offsetHours = pad(Math.floor(absoluteOffset / 60));
  const offsetRemainder = pad(absoluteOffset % 60);

  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}`
    + `T${pad(date.getHours())}:${pad(date.getMinutes())}:00${sign}${offsetHours}:${offsetRemainder}`;
}
