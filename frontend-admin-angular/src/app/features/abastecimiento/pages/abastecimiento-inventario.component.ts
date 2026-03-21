import { CommonModule, DecimalPipe, DatePipe } from "@angular/common";
import { Component, OnInit, computed, inject, signal } from "@angular/core";
import { ReactiveFormsModule, FormBuilder, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { BackofficeStoreService } from "../../../core/store/backoffice-store.service";
import { ADMIN_SURFACE_STYLES } from "../../../shared/ui/admin-surface.styles";
import type {
  IngredienteSummary,
  InsumoSummary,
  ItemProveedorSummary,
  InventarioMovimientoSummary,
} from "../models/abastecimiento.models";

@Component({
  selector: "app-abastecimiento-inventario",
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, DecimalPipe, DatePipe],
  template: `
    <div class="admin-grid">
      <section class="surface-card">
        <header class="surface-header">
          <p class="surface-kicker">Control de existencias</p>
          <div class="surface-title-row">
            <img
              src="assets/icons/abastecimiento/package.svg"
              alt=""
              width="28"
              height="28"
              aria-hidden="true"
            />
            <h3>Inventario de Abastecimiento</h3>
          </div>
          <p class="surface-copy">Gestión de ingredientes e insumos</p>
          <div class="chip-row">
            <span class="summary-chip">
              <img
                src="assets/icons/abastecimiento/package.svg"
                alt=""
                aria-hidden="true"
              />
              {{ totalItems() }} items registrados
            </span>
            <span class="summary-chip" *ngIf="estadoFilter().length > 0">
              {{ estadoFilter().length }} filtros activos
            </span>
          </div>
        </header>

        <div class="page-toolbar page-toolbar--search">
          <div class="tabs">
            <button
              class="tab"
              [class.tab--active]="activeTab() === 'ingredientes'"
              (click)="switchTab('ingredientes')"
            >
              Ingredientes
            </button>
            <button
              class="tab"
              [class.tab--active]="activeTab() === 'insumos'"
              (click)="switchTab('insumos')"
            >
              Insumos
            </button>
          </div>

          <label class="search-box">
            <span>Buscar item</span>
            <input
              type="search"
              [value]="searchQuery()"
              (input)="onSearchInput($event)"
              placeholder="Código o nombre..."
            />
          </label>

          <div class="chip-row">
            <button
              class="chip"
              [class.chip--active]="estadoFilter().length === 0"
              (click)="clearEstadoFilter()"
            >
              Todos
            </button>
            <button
              *ngFor="let estado of availableEstados"
              class="chip"
              [class.chip--active]="isEstadoSelected(estado.value)"
              (click)="toggleEstado(estado.value)"
            >
              {{ estado.label }}
            </button>
          </div>
        </div>

        <div class="table-shell" *ngIf="!isLoading(); else loadingState">
          <table
            class="surface-table surface-table--wide"
            *ngIf="currentItems().length; else emptyState"
          >
            <thead>
              <tr>
                <th>Código</th>
                <th>Nombre</th>
                <th>Und</th>
                <th class="text-right">Stock</th>
                <th class="text-right">Mín.</th>
                <th>Estado</th>
                <th class="text-center">Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr
                *ngFor="let item of currentItems()"
                (click)="openItemPanel(item)"
                class="clickable-row"
              >
                <td class="mono">{{ item.codigo }}</td>
                <td>
                  <strong>{{ item.nombre }}</strong>
                </td>
                <td>{{ item.umedidaNombre }}</td>
                <td class="text-right" [ngClass]="getStockClass(item)">
                  {{ item.stockActual | number: "1.2-2" }}
                </td>
                <td class="text-right">
                  {{ item.stockMinimo | number: "1.2-2" }}
                </td>
                <td>
                  <span
                    class="pill"
                    [class]="'pill--' + getEstadoClass(item)"
                    >{{ getEstadoLabel(item) }}</span
                  >
                </td>
                <td class="text-center">
                  <div
                    class="action-row action-row--centered"
                    (click)="$event.stopPropagation()"
                  >
                    <button
                      class="mini-button"
                      (click)="openItemPanel(item)"
                      title="Ver detalle"
                    >
                      <img
                        src="assets/icons/abastecimiento/eye.svg"
                        alt=""
                        width="14"
                        height="14"
                      />
                    </button>
                    <button
                      class="mini-button"
                      (click)="toggleAjusteForm(item)"
                      title="Ajustar stock"
                    >
                      <img
                        src="assets/icons/abastecimiento/scale.svg"
                        alt=""
                        width="14"
                        height="14"
                      />
                    </button>
                    <button
                      class="mini-button"
                      (click)="goToCompras(item)"
                      title="Crear orden"
                    >
                      <img
                        src="assets/icons/abastecimiento/cart.svg"
                        alt=""
                        width="14"
                        height="14"
                      />
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <ng-template #loadingState>
          <div class="loading-state">
            <div class="spinner"></div>
            <p>Cargando inventario...</p>
          </div>
        </ng-template>

        <ng-template #emptyState>
          <div class="empty-state">No se encontraron items</div>
        </ng-template>

        <div class="pager" *ngIf="totalItems() > 0">
          <p class="pager__meta">{{ getPaginationText() }}</p>
          <div class="pager__controls">
            <button
              class="mini-button"
              [disabled]="currentPage() === 0"
              (click)="goToPage(currentPage() - 1)"
            >
              Anterior
            </button>
            <button
              class="mini-button"
              [disabled]="currentPage() >= getTotalPages() - 1"
              (click)="goToPage(currentPage() + 1)"
            >
              Siguiente
            </button>
          </div>
          <label class="pager__size">
            Filas por página
            <select
              [value]="pageSize()"
              (change)="changePageSize($any($event.target).value)"
            >
              <option value="10">10</option>
              <option value="20">20</option>
              <option value="50">50</option>
            </select>
          </label>
        </div>
      </section>
    </div>

    <!-- Ajuste Form Modal -->
    <div
      class="panel-overlay"
      *ngIf="showAjusteForm()"
      (click)="hideAjusteForm()"
    ></div>
    <aside class="item-panel" *ngIf="showAjusteForm()">
      <div class="surface-header">
        <p class="surface-kicker">Ajuste de inventario</p>
        <div class="surface-title-row">
          <h3>{{ selectedItemForAjuste()?.nombre }}</h3>
        </div>
      </div>

      <form
        [formGroup]="ajusteForm"
        (ngSubmit)="submitAjuste()"
        class="surface-form"
      >
        <div class="surface-row surface-row--2">
          <label
            >Tipo de movimiento
            <select formControlName="tipoMovimiento">
              <option value="SALIDA_AJUSTE">Salida (negativo)</option>
              <option value="ENTRADA_AJUSTE">Entrada (positivo)</option>
            </select>
          </label>
          <label
            >Cantidad
            <input
              type="number"
              formControlName="cantidad"
              placeholder="0.00"
              step="0.01"
              min="0.001"
            />
          </label>
        </div>
        <label
          >Motivo
          <input
            type="text"
            formControlName="motivoSalida"
            placeholder="Ej: Merma, Vencimiento..."
          />
        </label>
        <label
          >Observaciones (opcional)
          <textarea
            formControlName="observaciones"
            rows="2"
            placeholder="Notas adicionales..."
          ></textarea>
        </label>
        <div class="action-row">
          <button type="button" class="mini-button" (click)="hideAjusteForm()">
            Cancelar
          </button>
          <button
            type="submit"
            class="surface-button"
            [disabled]="ajusteForm.invalid || isSubmitting()"
          >
            <span *ngIf="isSubmitting()">Procesando...</span>
            <span *ngIf="!isSubmitting()">Guardar ajuste</span>
          </button>
        </div>
      </form>
    </aside>

    <!-- Item Detail Panel -->
    <div
      class="panel-overlay"
      *ngIf="showItemPanel()"
      (click)="closeItemPanel()"
    ></div>
    <aside class="item-panel" *ngIf="showItemPanel()">
      <div class="surface-header">
        <p class="surface-kicker">Detalle de item</p>
        <div class="surface-title-row">
          <h3>{{ getSelectedItemName() }}</h3>
          <button class="mini-button" (click)="closeItemPanel()">
            <img
              src="assets/icons/abastecimiento/close.svg"
              alt=""
              width="14"
              height="14"
            />
          </button>
        </div>
        <p class="surface-meta mono">{{ getSelectedItemCode() }}</p>
      </div>

      <div class="kpi-strip">
        <div class="kpi-mini">
          <span>Stock actual</span>
          <strong>{{ getSelectedItemDisp() | number: "1.2-2" }}</strong>
        </div>
        <div class="kpi-mini">
          <span>Disponible</span>
          <strong [class]="getDispClass()">{{
            getSelectedItemDisp() | number: "1.2-2"
          }}</strong>
        </div>
      </div>

      <div class="panel-section">
        <h4 class="section-title">Últimos movimientos</h4>
        <div class="list" *ngIf="panelMovimientos().length; else noMovimientos">
          <div class="list-row" *ngFor="let mov of panelMovimientos()">
            <div class="list-row__main">
              <div class="list-row__heading">
                <span
                  class="pill"
                  [class]="'pill--' + getMovimientoClass(mov.tipoMovimiento)"
                >
                  {{ formatTipoMovimiento(mov.tipoMovimiento) }}
                </span>
                <small>{{ mov.fechaMovimiento | date: "dd/MM/yy" }}</small>
              </div>
              <p [class]="getCantidadClass(mov.cantidad)">
                {{ mov.cantidad > 0 ? "+" : ""
                }}{{ mov.cantidad | number: "1.2-2" }}
              </p>
            </div>
          </div>
        </div>
        <ng-template #noMovimientos>
          <div class="empty-state small">Sin movimientos registrados</div>
        </ng-template>
      </div>

      <div class="panel-section">
        <h4 class="section-title">Proveedores</h4>
        <div class="list" *ngIf="panelProveedores().length; else noProveedores">
          <div class="list-row" *ngFor="let prov of panelProveedores()">
            <div class="list-row__main">
              <strong>{{ prov.proveedorNombre }}</strong>
              <p>{{ prov.precioSuministro | number: "1.2-2" }}</p>
            </div>
            <button class="mini-button" (click)="createOCForProveedor(prov)">
              Crear OC
            </button>
          </div>
        </div>
        <ng-template #noProveedores>
          <div class="empty-state small">Sin proveedores asignados</div>
        </ng-template>
      </div>

      <div class="action-row panel-actions">
        <button class="mini-button" (click)="editItem()">
          <img
            src="assets/icons/abastecimiento/edit.svg"
            alt=""
            width="14"
            height="14"
          />
          Editar
        </button>
        <button class="surface-button" (click)="createOrdenCompra()">
          <img
            src="assets/icons/abastecimiento/cart.svg"
            alt=""
            width="14"
            height="14"
          />
          Crear OC
        </button>
      </div>
    </aside>
  `,
  styles: [
    ADMIN_SURFACE_STYLES,
    `
      .tabs {
        display: flex;
        background: #f0e8e2;
        border-radius: 4px;
        padding: 0.25rem;
      }

      .tab {
        padding: 0.5rem 1rem;
        border: none;
        background: transparent;
        color: #8a5c46;
        font-size: 0.85rem;
        font-weight: 500;
        cursor: pointer;
        border-radius: 2px;
        transition: all 0.15s;
      }

      .tab:hover {
        color: #2d201a;
      }

      .tab--active {
        background: #fff;
        color: #2d201a;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
      }

      .page-toolbar--search {
        align-items: end;
        grid-template-columns: auto minmax(16rem, 1fr) auto;
      }

      .chip {
        padding: 0.35rem 0.75rem;
        border: 1px solid #eaded4;
        background: #fff;
        color: #8a5c46;
        font-size: 0.75rem;
        font-weight: 500;
        cursor: pointer;
        border-radius: 2px;
        transition: all 0.15s;
      }

      .chip:hover {
        border-color: #5a3424;
      }

      .chip--active {
        background: #5a3424;
        border-color: #5a3424;
        color: #fff;
      }

      .mono {
        font-family: "JetBrains Mono", monospace;
        font-size: 0.8rem;
        color: #8a5c46;
      }

      .clickable-row {
        cursor: pointer;
      }

      .clickable-row:hover {
        background: #faf6f2;
      }

      .text-right {
        text-align: right;
      }

      .text-center {
        text-align: center;
      }

      .action-row--centered {
        justify-content: center;
      }

      .stock-critical {
        color: #b71c1c;
        font-weight: 600;
      }

      .stock-warning {
        color: #d68910;
        font-weight: 600;
      }

      .stock-ok {
        color: #1b5e20;
        font-weight: 600;
      }

      .pill--critico {
        background: #fdecea;
        color: #b71c1c;
      }

      .pill--bajo {
        background: #fff8e1;
        color: #d68910;
      }

      .pill--normal {
        background: #e8f5e9;
        color: #1b5e20;
      }

      .pill--agotado {
        background: #f3e5f5;
        color: #7b1fa2;
      }

      .pill--entrada {
        background: #e8f5e9;
        color: #1b5e20;
      }

      .pill--salida {
        background: #fdecea;
        color: #b71c1c;
      }

      .pill--ajuste {
        background: #fff8e1;
        color: #d68910;
      }

      .text-positive {
        color: #1b5e20;
      }

      .text-negative {
        color: #b71c1c;
      }

      .panel-overlay {
        position: fixed;
        inset: 0;
        background: rgba(45, 32, 26, 0.4);
        z-index: 25;
      }

      .item-panel {
        position: fixed;
        top: 0;
        right: 0;
        width: min(420px, 96vw);
        height: 100vh;
        background: linear-gradient(
          180deg,
          rgba(255, 255, 255, 0.98),
          rgba(255, 249, 244, 0.96)
        );
        border-left: 2px solid #5a3424;
        box-shadow: -24px 0 60px rgba(59, 34, 24, 0.14);
        z-index: 30;
        overflow-y: auto;
        padding: 1.25rem;
        display: flex;
        flex-direction: column;
        gap: 1rem;
      }

      .kpi-strip {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 0.75rem;
      }

      .kpi-mini {
        display: flex;
        flex-direction: column;
        gap: 0.25rem;
        padding: 0.75rem;
        background: #fff;
        border: 1px solid #eaded4;
        border-radius: 2px;
      }

      .kpi-mini span {
        font-size: 0.72rem;
        color: #8a5c46;
        text-transform: uppercase;
        letter-spacing: 0.05em;
      }

      .kpi-mini strong {
        font: 700 1.3rem/1
          var(--font-display, "Cormorant Garamond", Georgia, serif);
        color: #2d201a;
      }

      .panel-section {
        border-top: 1px solid #eaded4;
        padding-top: 1rem;
      }

      .section-title {
        margin: 0 0 0.75rem;
        font-size: 0.8rem;
        font-weight: 600;
        color: #8a5c46;
        text-transform: uppercase;
        letter-spacing: 0.05em;
      }

      .list {
        display: grid;
        gap: 0.5rem;
      }

      .list-row {
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 0.75rem;
        padding: 0.6rem 0;
        border-bottom: 1px solid #f0e8e2;
      }

      .list-row:last-child {
        border-bottom: none;
      }

      .list-row__main {
        display: grid;
        gap: 0.2rem;
        flex: 1;
      }

      .list-row__heading {
        display: flex;
        align-items: center;
        gap: 0.5rem;
      }

      .panel-actions {
        margin-top: auto;
        padding-top: 1rem;
        border-top: 1px solid #eaded4;
      }

      .loading-state {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        padding: 3rem;
        gap: 1rem;
      }

      .spinner {
        width: 32px;
        height: 32px;
        border: 3px solid #eaded4;
        border-top-color: #5a3424;
        border-radius: 50%;
        animation: spin 0.8s linear infinite;
      }

      @keyframes spin {
        to {
          transform: rotate(360deg);
        }
      }
    `,
  ],
})
export class AbastecimientoInventarioComponent implements OnInit {
  private readonly store = inject(BackofficeStoreService);
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);

  activeTab = signal<"ingredientes" | "insumos">("ingredientes");
  searchQuery = signal("");
  estadoFilter = signal<string[]>([]);
  showItemPanel = signal(false);
  selectedItemId = signal<number | null>(null);
  selectedItemTipo = signal<"INGREDIENTE" | "INSUMO">("INGREDIENTE");
  showAjusteForm = signal(false);
  selectedItemForAjuste = signal<IngredienteSummary | InsumoSummary | null>(
    null,
  );

  currentPage = signal(0);
  pageSize = signal(20);
  totalItems = signal(0);
  isLoading = signal(false);
  isSubmitting = signal(false);

  panelMovimientos = signal<InventarioMovimientoSummary[]>([]);
  panelProveedores = signal<ItemProveedorSummary[]>([]);

  availableEstados = [
    { value: "CRITICO", label: "Crítico" },
    { value: "BAJO", label: "Bajo" },
    { value: "NORMAL", label: "Normal" },
    { value: "AGOTADO", label: "Agotado" },
  ];

  readonly ajusteForm = this.fb.nonNullable.group({
    tipoMovimiento: ["SALIDA_AJUSTE", Validators.required],
    cantidad: [
      null as number | null,
      [Validators.required, Validators.min(0.001)],
    ],
    motivoSalida: ["", Validators.required],
    observaciones: [""],
  });

  currentItems = computed(() => {
    if (this.activeTab() === "ingredientes") {
      return this.store.ingredientesPage()?.content || [];
    }
    return this.store.insumosPage()?.content || [];
  });

  ngOnInit(): void {
    this.loadData();
  }

  switchTab(tab: "ingredientes" | "insumos"): void {
    this.activeTab.set(tab);
    this.currentPage.set(0);
    this.loadData();
  }

  onSearchInput(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.searchQuery.set(value);
    setTimeout(() => {
      this.currentPage.set(0);
      this.loadData();
    }, 300);
  }

  isEstadoSelected(estado: string): boolean {
    return this.estadoFilter().includes(estado);
  }

  toggleEstado(estado: string): void {
    const current = this.estadoFilter();
    if (current.includes(estado)) {
      this.estadoFilter.set(current.filter((e) => e !== estado));
    } else {
      this.estadoFilter.set([...current, estado]);
    }
    this.currentPage.set(0);
    this.loadData();
  }

  clearEstadoFilter(): void {
    this.estadoFilter.set([]);
    this.currentPage.set(0);
    this.loadData();
  }

  loadData(): void {
    this.isLoading.set(true);
    const tipo = this.activeTab() === "ingredientes" ? "INGREDIENTE" : "INSUMO";
    const query = this.searchQuery() || "";

    if (this.activeTab() === "ingredientes") {
      this.totalItems.set(this.store.ingredientesPage()?.totalElements || 0);
      this.store.loadIngredientesPage(
        this.currentPage(),
        this.pageSize(),
        query,
        tipo,
      );
    } else {
      this.totalItems.set(this.store.insumosPage()?.totalElements || 0);
      this.store.loadInsumosPage(
        this.currentPage(),
        this.pageSize(),
        query,
        tipo,
      );
    }
    this.isLoading.set(false);
  }

  getStockClass(item: IngredienteSummary | InsumoSummary): string {
    if (item.stockActual <= 0) return "stock-critical";
    if (item.stockActual <= item.stockMinimo) return "stock-warning";
    return "stock-ok";
  }

  getEstadoClass(item: IngredienteSummary | InsumoSummary): string {
    const stockActual = item.stockActual;
    const stockMinimo = item.stockMinimo;

    if (stockActual <= 0) return "agotado";
    if (stockActual <= stockMinimo * 0.5) return "critico";
    if (stockActual <= stockMinimo) return "bajo";
    return "normal";
  }

  getEstadoLabel(item: IngredienteSummary | InsumoSummary): string {
    const stockActual = item.stockActual;
    const stockMinimo = item.stockMinimo;

    if (stockActual <= 0) return "AGOTADO";
    if (stockActual <= stockMinimo * 0.5) return "CRÍTICO";
    if (stockActual <= stockMinimo) return "BAJO";
    return "NORMAL";
  }

  getMovimientoClass(tipo: string): string {
    if (tipo.startsWith("ENTRADA")) return "entrada";
    if (tipo.startsWith("SALIDA")) return "salida";
    return "ajuste";
  }

  getCantidadClass(cantidad: number): string {
    return cantidad > 0 ? "text-positive" : "text-negative";
  }

  formatTipoMovimiento(tipo: string): string {
    const map: Record<string, string> = {
      ENTRADA_COMPRA: "Compra",
      ENTRADA_AJUSTE: "Ajuste +",
      SALIDA_PRODUCCION: "Producción",
      SALIDA_MERMA: "Merma",
      SALIDA_AJUSTE: "Ajuste -",
    };
    return map[tipo] || tipo;
  }

  openItemPanel(item: IngredienteSummary | InsumoSummary): void {
    this.selectedItemId.set(item.id);
    this.selectedItemTipo.set(
      this.activeTab() === "ingredientes" ? "INGREDIENTE" : "INSUMO",
    );
    this.showItemPanel.set(true);
    this.loadPanelData(item);
  }

  loadPanelData(item: IngredienteSummary | InsumoSummary): void {
    const tipo = this.activeTab() === "ingredientes" ? "INGREDIENTE" : "INSUMO";
    this.store["api"].getMovimientos(tipo, item.id).subscribe({
      next: (movs) => this.panelMovimientos.set(movs.slice(0, 5)),
    });
    this.store["api"].getItemsProveedorPorItem(tipo, item.id).subscribe({
      next: (provs) => this.panelProveedores.set(provs),
    });
  }

  closeItemPanel(): void {
    this.showItemPanel.set(false);
    this.selectedItemId.set(null);
  }

  getSelectedItem(): IngredienteSummary | InsumoSummary | null {
    const id = this.selectedItemId();
    if (!id) return null;
    return this.currentItems().find((item) => item.id === id) || null;
  }

  getSelectedItemName(): string {
    return this.getSelectedItem()?.nombre || "";
  }

  getSelectedItemCode(): string {
    return this.getSelectedItem()?.codigo || "";
  }

  getSelectedItemDisp(): number {
    return this.getSelectedItem()?.stockActual || 0;
  }

  getDispClass(): string {
    const item = this.getSelectedItem();
    if (!item) return "";
    if (item.stockActual <= 0) return "text-negative";
    if (item.stockActual <= item.stockMinimo) return "text-warning";
    return "text-positive";
  }

  toggleAjusteForm(item: IngredienteSummary | InsumoSummary): void {
    this.selectedItemId.set(item.id);
    this.selectedItemTipo.set(
      this.activeTab() === "ingredientes" ? "INGREDIENTE" : "INSUMO",
    );
    this.selectedItemForAjuste.set(item);
    this.showAjusteForm.set(!this.showAjusteForm());
    this.ajusteForm.reset({ tipoMovimiento: "SALIDA_AJUSTE" });
  }

  hideAjusteForm(): void {
    this.showAjusteForm.set(false);
    this.selectedItemForAjuste.set(null);
    this.ajusteForm.reset({ tipoMovimiento: "SALIDA_AJUSTE" });
  }

  submitAjuste(): void {
    if (this.ajusteForm.invalid) {
      this.ajusteForm.markAllAsTouched();
      return;
    }

    this.isSubmitting.set(true);
    const formValue = this.ajusteForm.getRawValue();

    this.store.createMovimiento(
      {
        itemTipo: this.selectedItemTipo(),
        itemId: this.selectedItemId()!,
        tipoMovimiento: formValue.tipoMovimiento as any,
        cantidad: formValue.cantidad!,
        motivoSalida: formValue.motivoSalida,
        observaciones: formValue.observaciones,
      },
      () => {
        this.isSubmitting.set(false);
        this.hideAjusteForm();
        this.loadData();
      },
    );
  }

  goToPage(page: number): void {
    this.currentPage.set(page);
    this.loadData();
  }

  changePageSize(size: string): void {
    this.pageSize.set(Number(size));
    this.currentPage.set(0);
    this.loadData();
  }

  getTotalPages(): number {
    return Math.ceil(this.totalItems() / this.pageSize());
  }

  getPaginationText(): string {
    const from = this.currentPage() * this.pageSize() + 1;
    const to = Math.min(
      (this.currentPage() + 1) * this.pageSize(),
      this.totalItems(),
    );
    return `Mostrando ${from}-${to} de ${this.totalItems()} items`;
  }

  goToCompras(item: IngredienteSummary | InsumoSummary): void {
    this.router.navigate(["/abastecimiento/compras"], {
      queryParams: {
        itemId: item.id,
        itemTipo:
          this.activeTab() === "ingredientes" ? "INGREDIENTE" : "INSUMO",
      },
    });
  }

  editItem(): void {
    const id = this.selectedItemId();
    const tipo = this.selectedItemTipo();
    if (id) {
      this.router.navigate(["/abastecimiento/editar", tipo.toLowerCase(), id]);
    }
  }

  createOrdenCompra(): void {
    const id = this.selectedItemId();
    const tipo = this.selectedItemTipo();
    if (id) {
      this.router.navigate(["/abastecimiento/compras/nueva"], {
        queryParams: { itemId: id, itemTipo: tipo },
      });
    }
  }

  createOCForProveedor(proveedor: ItemProveedorSummary): void {
    const id = this.selectedItemId();
    if (id) {
      this.router.navigate(["/abastecimiento/compras/nueva"], {
        queryParams: {
          itemId: id,
          itemTipo: this.selectedItemTipo(),
          proveedorId: proveedor.proveedorId,
        },
      });
    }
  }
}
