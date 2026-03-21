import { CommonModule, DecimalPipe } from "@angular/common";
import { Component, OnInit, computed, inject, signal } from "@angular/core";
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  FormArray,
  Validators,
} from "@angular/forms";
import { FormsModule } from "@angular/forms";
import { BackofficeStoreService } from "../../../core/store/backoffice-store.service";
import { ADMIN_SURFACE_STYLES } from "../../../shared/ui/admin-surface.styles";
import type { OrdenCompraDetailSummary } from "../models/abastecimiento.models";

interface ReceiveItem {
  detalleId: number;
  itemNombre: string;
  itemTipo: string;
  solicitado: number;
  recibido: number;
  precioUnitario: number;
  cantidadRecibir: number;
  observaciones: string;
}

@Component({
  selector: "app-abastecimiento-compras",
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, DecimalPipe],
  template: `
    <div class="admin-grid">
      <!-- LIST VIEW -->
      <section class="surface-card" *ngIf="view() === 'list'">
        <header class="surface-header">
          <p class="surface-kicker">Gestión de compras</p>
          <div class="surface-title-row">
            <img
              src="assets/icons/abastecimiento/shopping-cart.svg"
              alt=""
              width="28"
              height="28"
              aria-hidden="true"
            />
            <h3>Órdenes de Compra</h3>
          </div>
          <div class="chip-row">
            <button class="surface-button" (click)="navigateToCreate()">
              <img
                src="assets/icons/abastecimiento/plus.svg"
                alt=""
                width="16"
                height="16"
              />
              Nueva orden
            </button>
          </div>
        </header>

        <div class="page-toolbar page-toolbar--search">
          <label class="search-box">
            <span>Buscar</span>
            <input
              type="search"
              [(ngModel)]="searchQueryValue"
              (ngModelChange)="onSearchChange($event)"
              placeholder="Código o proveedor..."
            />
          </label>

          <div class="chip-row">
            <span class="surface-meta">Estado:</span>
            <button
              *ngFor="let estado of allEstados"
              class="chip"
              [class.chip--active]="estadoFilter().includes(estado)"
              (click)="toggleEstadoFilter(estado)"
            >
              {{ formatEstado(estado) }}
            </button>
          </div>

          <div class="surface-row surface-row--2" style="max-width: 320px;">
            <label>
              <span class="surface-meta">Desde</span>
              <input
                type="date"
                [(ngModel)]="fechaDesdeValue"
                (ngModelChange)="onFechaDesdeChange($event)"
              />
            </label>
            <label>
              <span class="surface-meta">Hasta</span>
              <input
                type="date"
                [(ngModel)]="fechaHastaValue"
                (ngModelChange)="onFechaHastaChange($event)"
              />
            </label>
          </div>
        </div>

        <div
          class="table-shell"
          *ngIf="
            !loading() && paginatedOrdenes().length > 0;
            else emptyOrLoading
          "
        >
          <table class="surface-table surface-table--wide">
            <thead>
              <tr>
                <th>Código</th>
                <th>Proveedor</th>
                <th>Fecha</th>
                <th>Estado</th>
                <th class="text-center">Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let orden of paginatedOrdenes()">
                <td class="mono">{{ orden.codigo }}</td>
                <td>
                  <strong>{{ orden.proveedorNombre }}</strong>
                </td>
                <td>
                  {{ formatDate(orden.createdAt) }}
                  <small
                    *ngIf="isOverdue(orden.fechaEntregaEstimada)"
                    class="text-danger"
                    >(Vencida)</small
                  >
                </td>
                <td>
                  <span
                    class="pill"
                    [class]="'pill--' + getEstadoClass(orden.estado)"
                  >
                    {{ formatEstado(orden.estado) }}
                  </span>
                </td>
                <td class="text-center">
                  <div class="action-row action-row--centered">
                    <button
                      class="mini-button"
                      (click)="openDetail(orden)"
                      title="Ver"
                    >
                      <img
                        src="assets/icons/abastecimiento/eye.svg"
                        alt=""
                        width="14"
                        height="14"
                      />
                    </button>
                    <button
                      *ngIf="orden.estado === 'BORRADOR'"
                      class="mini-button"
                      (click)="editOrden(orden)"
                      title="Editar"
                    >
                      <img
                        src="assets/icons/abastecimiento/edit.svg"
                        alt=""
                        width="14"
                        height="14"
                      />
                    </button>
                    <button
                      *ngIf="
                        orden.estado === 'ENVIADA' ||
                        orden.estado === 'RECIBIDA_PARCIAL'
                      "
                      class="mini-button mini-button--primary"
                      (click)="startReceiving(orden)"
                      title="Recibir"
                    >
                      <img
                        src="assets/icons/abastecimiento/receive.svg"
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

        <ng-template #emptyOrLoading>
          <div class="loading-state" *ngIf="loading()">
            <div class="spinner"></div>
            <p>Cargando órdenes...</p>
          </div>
          <div
            class="empty-state"
            *ngIf="!loading() && paginatedOrdenes().length === 0"
          >
            <p>No se encontraron órdenes de compra</p>
            <button class="surface-button" (click)="navigateToCreate()">
              Crear primera orden
            </button>
          </div>
        </ng-template>

        <div class="pager" *ngIf="totalPages() > 1">
          <p class="pager__meta">
            Página {{ currentPage() }} de {{ totalPages() }}
          </p>
          <div class="pager__controls">
            <button
              class="mini-button"
              [disabled]="currentPage() === 1"
              (click)="goToPage(currentPage() - 1)"
            >
              Anterior
            </button>
            <button
              class="mini-button"
              [disabled]="currentPage() === totalPages()"
              (click)="goToPage(currentPage() + 1)"
            >
              Siguiente
            </button>
          </div>
        </div>
      </section>

      <!-- CREATE / EDIT VIEW -->
      <section class="surface-card" *ngIf="view() === 'create'">
        <header class="surface-header">
          <p class="surface-kicker">
            {{ editingOrdenId() ? "Edición" : "Nueva orden" }}
          </p>
          <div class="surface-title-row">
            <h3>
              {{
                editingOrdenId()
                  ? "Editar Orden de Compra"
                  : "Nueva Orden de Compra"
              }}
            </h3>
          </div>
        </header>

        <form
          [formGroup]="ordenForm"
          (ngSubmit)="saveOrden()"
          class="surface-form"
        >
          <div class="surface-row surface-row--3">
            <label
              >Proveedor *
              <select formControlName="proveedorId">
                <option value="">Seleccionar...</option>
                <option
                  *ngFor="let prov of store.proveedores()"
                  [value]="prov.id"
                >
                  {{ prov.nombre }}
                </option>
              </select>
            </label>
            <label
              >Código *
              <input
                type="text"
                formControlName="codigo"
                placeholder="OC-2026-XXXX"
              />
            </label>
            <label
              >Fecha entrega estimada
              <input type="date" formControlName="fechaEntregaEstimada" />
            </label>
          </div>

          <label
            >Observaciones
            <textarea
              formControlName="observaciones"
              rows="2"
              placeholder="Notas adicionales..."
            ></textarea>
          </label>

          <div class="panel-section">
            <h4 class="section-title">Líneas de orden</h4>
            <div formArrayName="lineas">
              <div
                *ngFor="let linea of lineasArray.controls; let i = index"
                [formGroupName]="i"
                class="line-item"
              >
                <div class="surface-row surface-row--5">
                  <label style="grid-column: span 2;"
                    >Item
                    <select formControlName="itemId" (change)="onItemChange(i)">
                      <option value="">Seleccionar...</option>
                      <option
                        *ngFor="
                          let item of getFilteredItems(
                            linea.get('itemTipo')?.value
                          )
                        "
                        [value]="item.id"
                      >
                        {{ item.nombre }}
                      </option>
                    </select>
                  </label>
                  <label
                    >Tipo
                    <select formControlName="itemTipo">
                      <option value="INGREDIENTE">Ingrediente</option>
                      <option value="INSUMO">Insumo</option>
                    </select>
                  </label>
                  <label
                    >Cantidad
                    <input
                      type="number"
                      formControlName="cantidad"
                      min="0.01"
                      step="0.01"
                    />
                  </label>
                  <label
                    >Precio unit.
                    <input
                      type="number"
                      formControlName="precioUnitario"
                      min="0"
                      step="0.01"
                    />
                  </label>
                  <div class="line-total">
                    <span class="surface-meta">Subtotal</span>
                    <strong>{{ getLineaSubtotal(i) | number: "1.2-2" }}</strong>
                  </div>
                </div>
                <button
                  type="button"
                  class="mini-button mini-button--danger"
                  (click)="removeLinea(i)"
                >
                  Eliminar
                </button>
              </div>
            </div>

            <button
              type="button"
              class="mini-button"
              (click)="addLinea()"
              style="width: 100%; margin-top: 0.5rem;"
            >
              + Agregar línea
            </button>
          </div>

          <div class="total-box">
            <span class="surface-meta">Total estimado</span>
            <strong>{{ getTotalEstimado() | number: "1.2-2" }}</strong>
          </div>

          <div class="action-row" style="justify-content: flex-end;">
            <button type="button" class="mini-button" (click)="backToList()">
              Cancelar
            </button>
            <button
              type="submit"
              class="surface-button"
              [disabled]="ordenForm.invalid || saving()"
            >
              <span *ngIf="saving()">Guardando...</span>
              <span *ngIf="!saving()">{{
                editingOrdenId() ? "Actualizar" : "Guardar"
              }}</span>
            </button>
          </div>
        </form>
      </section>

      <!-- DETAIL VIEW -->
      <section
        class="surface-card"
        *ngIf="view() === 'detail' && detailOrden()"
      >
        <header class="surface-header">
          <p class="surface-kicker">Detalle de orden</p>
          <div class="surface-title-row">
            <button class="mini-button" (click)="backToList()">
              <img
                src="assets/icons/abastecimiento/back.svg"
                alt=""
                width="14"
                height="14"
              />
            </button>
            <h3 class="mono">{{ detailOrden()!.codigo }}</h3>
            <span
              class="pill"
              [class]="'pill--' + getEstadoClass(detailOrden()!.estado)"
            >
              {{ formatEstado(detailOrden()!.estado) }}
            </span>
          </div>
          <div class="chip-row">
            <button
              *ngIf="detailOrden()!.estado === 'BORRADOR'"
              class="mini-button"
              (click)="editOrdenFromDetail()"
            >
              <img
                src="assets/icons/abastecimiento/edit.svg"
                alt=""
                width="14"
                height="14"
              />
              Editar
            </button>
            <button
              *ngIf="detailOrden()!.estado === 'BORRADOR'"
              class="surface-button"
              (click)="enviarOrden()"
            >
              <img
                src="assets/icons/abastecimiento/send.svg"
                alt=""
                width="14"
                height="14"
              />
              Enviar
            </button>
            <button
              *ngIf="detailOrden()!.estado === 'BORRADOR'"
              class="mini-button mini-button--danger"
              (click)="cancelOrden()"
            >
              <img
                src="assets/icons/abastecimiento/cancel.svg"
                alt=""
                width="14"
                height="14"
              />
              Cancelar
            </button>
            <button
              *ngIf="
                detailOrden()!.estado === 'ENVIADA' ||
                detailOrden()!.estado === 'RECIBIDA_PARCIAL'
              "
              class="surface-button"
              (click)="showReceiveSection.set(true)"
            >
              <img
                src="assets/icons/abastecimiento/receive.svg"
                alt=""
                width="14"
                height="14"
              />
              Recibir
            </button>
          </div>
        </header>

        <div class="info-grid">
          <div class="info-item">
            <span class="surface-meta">Proveedor</span>
            <strong>{{ detailOrden()!.proveedorNombre }}</strong>
          </div>
          <div class="info-item">
            <span class="surface-meta">Fecha creación</span>
            <strong>{{ formatDate(detailOrden()!.createdAt) }}</strong>
          </div>
          <div class="info-item">
            <span class="surface-meta">Entrega estimada</span>
            <strong
              [class.text-danger]="
                isOverdue(detailOrden()!.fechaEntregaEstimada)
              "
            >
              {{ formatDate(detailOrden()!.fechaEntregaEstimada) }}
            </strong>
          </div>
          <div class="info-item">
            <span class="surface-meta">Creado por</span>
            <strong>{{ detailOrden()!.createdByNombre }}</strong>
          </div>
        </div>

        <div class="table-shell">
          <table class="surface-table">
            <thead>
              <tr>
                <th>Item</th>
                <th>Tipo</th>
                <th class="text-right">Solicitado</th>
                <th class="text-right">Recibido</th>
                <th class="text-right">Faltante</th>
                <th class="text-right">Precio</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let linea of detailOrden()!.detalles">
                <td>{{ linea.itemNombre }}</td>
                <td>
                  <span class="pill pill--small">{{ linea.itemTipo }}</span>
                </td>
                <td class="text-right">
                  {{ linea.cantidad | number: "1.2-2" }}
                </td>
                <td class="text-right">
                  {{ linea.cantidadRecibida | number: "1.2-2" }}
                </td>
                <td
                  class="text-right"
                  [class.text-danger]="
                    linea.cantidad - linea.cantidadRecibida > 0
                  "
                >
                  {{
                    linea.cantidad - linea.cantidadRecibida | number: "1.2-2"
                  }}
                </td>
                <td class="text-right">
                  {{ linea.precioUnitario | number: "1.2-2" }}
                </td>
              </tr>
            </tbody>
            <tfoot>
              <tr>
                <td colspan="4" class="text-right"><strong>Total:</strong></td>
                <td colspan="2" class="text-right">
                  <strong>{{
                    detailOrden()!.totalEstimado | number: "1.2-2"
                  }}</strong>
                </td>
              </tr>
            </tfoot>
          </table>
        </div>

        <!-- RECEIVE SECTION -->
        <div class="panel-section receive-section" *ngIf="showReceiveSection()">
          <h4 class="section-title">
            <img
              src="assets/icons/abastecimiento/receive.svg"
              alt=""
              width="18"
              height="18"
            />
            Recepción de mercadería
          </h4>

          <div class="receive-grid">
            <div class="receive-header">
              <span>Item</span>
              <span class="text-center">Solicitado</span>
              <span class="text-center">Recibido</span>
              <span class="text-center">A recibir</span>
              <span>Observaciones</span>
            </div>
            <div
              *ngFor="let item of receiveItems(); let i = index"
              class="receive-row"
            >
              <span>
                <strong>{{ item.itemNombre }}</strong>
                <small>{{ item.itemTipo }}</small>
              </span>
              <span class="text-center">{{
                item.solicitado | number: "1.2-2"
              }}</span>
              <span class="text-center">{{
                item.recibido | number: "1.2-2"
              }}</span>
              <input
                type="number"
                [(ngModel)]="item.cantidadRecibir"
                [max]="item.solicitado - item.recibido"
                min="0"
                step="0.01"
              />
              <input
                type="text"
                [(ngModel)]="item.observaciones"
                placeholder="Observaciones..."
              />
            </div>
          </div>

          <div
            class="action-row"
            style="justify-content: flex-end; margin-top: 1rem;"
          >
            <button class="mini-button" (click)="showReceiveSection.set(false)">
              Cancelar
            </button>
            <button
              class="surface-button"
              (click)="confirmarRecepcion()"
              [disabled]="saving()"
            >
              <span *ngIf="saving()">Confirmando...</span>
              <span *ngIf="!saving()">Confirmar recepción</span>
            </button>
          </div>
        </div>
      </section>
    </div>
  `,
  styles: [
    ADMIN_SURFACE_STYLES,
    `
      .mono {
        font-family: "JetBrains Mono", monospace;
        font-size: 0.9rem;
      }

      .text-center {
        text-align: center;
      }

      .text-right {
        text-align: right;
      }

      .text-danger {
        color: #b71c1c;
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

      .pill--BORRADOR {
        background: #f5f5f5;
        color: #616161;
      }
      .pill--ENVIADA {
        background: #e3f2fd;
        color: #1565c0;
      }
      .pill--RECIBIDA_PARCIAL {
        background: #fff8e1;
        color: #f57f17;
      }
      .pill--RECIBIDA {
        background: #e8f5e9;
        color: #1b5e20;
      }
      .pill--CANCELADA {
        background: #fdecea;
        color: #b71c1c;
      }

      .pill--small {
        font-size: 0.65rem;
        padding: 0.15rem 0.4rem;
      }

      .mini-button--primary {
        background: #1b5e20;
        color: white;
      }

      .mini-button--danger {
        background: #fdecea;
        color: #b71c1c;
      }

      .action-row--centered {
        justify-content: center;
      }

      .info-grid {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 1rem;
        padding: 1rem;
        background: linear-gradient(
          180deg,
          rgba(255, 247, 241, 0.95),
          rgba(255, 252, 249, 0.95)
        );
        border-radius: 4px;
        margin-bottom: 1rem;
      }

      .info-item {
        display: flex;
        flex-direction: column;
        gap: 0.25rem;
      }

      .info-item strong {
        font: 600 0.95rem/1.2 var(--font-body, "Inter", system-ui, sans-serif);
        color: #2d201a;
      }

      .panel-section {
        border-top: 1px solid #eaded4;
        padding-top: 1rem;
        margin-top: 1rem;
      }

      .section-title {
        margin: 0 0 0.75rem;
        font-size: 0.8rem;
        font-weight: 600;
        color: #8a5c46;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        display: flex;
        align-items: center;
        gap: 0.5rem;
      }

      .section-title img {
        width: 18px;
        height: 18px;
      }

      .line-item {
        display: grid;
        gap: 0.5rem;
        padding: 1rem;
        background: #fff9f4;
        border: 1px solid #f0dfd4;
        border-radius: 4px;
        margin-bottom: 0.75rem;
      }

      .surface-row--5 {
        grid-template-columns: 2fr 1fr 1fr 1fr 1fr;
      }

      .line-total {
        display: flex;
        flex-direction: column;
        justify-content: flex-end;
        gap: 0.25rem;
      }

      .line-total strong {
        font: 700 1.1rem/1
          var(--font-display, "Cormorant Garamond", Georgia, serif);
        color: #2d201a;
      }

      .total-box {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 1rem;
        background: linear-gradient(135deg, #4f2519, #2f1912);
        color: white;
        border-radius: 4px;
        margin: 1rem 0;
      }

      .total-box strong {
        font: 700 1.5rem/1
          var(--font-display, "Cormorant Garamond", Georgia, serif);
      }

      .receive-section {
        background: #fffde7;
        border: 1px solid #f9a825;
        padding: 1rem;
        border-radius: 4px;
      }

      .receive-grid {
        display: grid;
        gap: 0.5rem;
      }

      .receive-header {
        display: grid;
        grid-template-columns: 2fr 1fr 1fr 1fr 2fr;
        gap: 0.5rem;
        padding: 0.5rem;
        font-size: 0.75rem;
        font-weight: 600;
        color: #8a5c46;
        text-transform: uppercase;
        border-bottom: 2px solid #e8ddd0;
      }

      .receive-row {
        display: grid;
        grid-template-columns: 2fr 1fr 1fr 1fr 2fr;
        gap: 0.5rem;
        align-items: center;
        padding: 0.5rem;
        border-bottom: 1px solid #f0e8e0;
      }

      .receive-row:last-child {
        border-bottom: none;
      }

      .receive-row span small {
        display: block;
        font-size: 0.75rem;
        color: #8a5c46;
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

      @media (max-width: 960px) {
        .info-grid {
          grid-template-columns: repeat(2, 1fr);
        }
        .surface-row--5 {
          grid-template-columns: 1fr;
        }
        .receive-header,
        .receive-row {
          grid-template-columns: 1fr;
        }
      }
    `,
  ],
})
export class AbastecimientoComprasComponent implements OnInit {
  store = inject(BackofficeStoreService);
  fb = inject(FormBuilder);

  view = signal<"list" | "create" | "detail">("list");
  editingOrdenId = signal<number | null>(null);
  detailOrden = signal<OrdenCompraDetailSummary | null>(null);
  receivingOrdenId = signal<number | null>(null);
  receiveItems = signal<ReceiveItem[]>([]);
  showReceiveSection = signal(false);
  searchQuery = signal("");
  estadoFilter = signal<string[]>([]);
  fechaDesde = signal("");
  fechaHasta = signal("");

  loading = signal(false);
  saving = signal(false);

  currentPage = signal(1);
  pageSize = signal(10);

  searchQueryValue = "";
  fechaDesdeValue = "";
  fechaHastaValue = "";

  allEstados = [
    "BORRADOR",
    "ENVIADA",
    "RECIBIDA_PARCIAL",
    "RECIBIDA",
    "CANCELADA",
  ];

  ordenForm!: FormGroup;

  filteredOrdenes = computed(() => {
    let ordenes = [...this.store.ordenesCompraPage().content];
    const q = this.searchQuery().toLowerCase().trim();
    if (q) {
      ordenes = ordenes.filter(
        (o) =>
          o.codigo.toLowerCase().includes(q) ||
          o.proveedorNombre.toLowerCase().includes(q),
      );
    }
    const estados = this.estadoFilter();
    if (estados.length > 0) {
      ordenes = ordenes.filter((o) => estados.includes(o.estado));
    }
    const desde = this.fechaDesde();
    if (desde) {
      ordenes = ordenes.filter((o) => o.createdAt >= desde);
    }
    const hasta = this.fechaHasta();
    if (hasta) {
      ordenes = ordenes.filter((o) => o.createdAt <= hasta);
    }
    return ordenes;
  });

  totalPages = computed(() =>
    Math.max(1, Math.ceil(this.filteredOrdenes().length / this.pageSize())),
  );

  paginatedOrdenes = computed(() => {
    const start = (this.currentPage() - 1) * this.pageSize();
    return this.filteredOrdenes().slice(start, start + this.pageSize());
  });

  get lineasArray(): FormArray {
    return this.ordenForm.get("lineas") as FormArray;
  }

  ngOnInit(): void {
    this.initForm();
    this.loadData();
  }

  initForm(): void {
    this.ordenForm = this.fb.group({
      proveedorId: ["", Validators.required],
      codigo: ["", Validators.required],
      fechaEntregaEstimada: [""],
      observaciones: [""],
      lineas: this.fb.array([], Validators.required),
    });
  }

  loadData(): void {
    this.loading.set(true);
    this.store.loadOrdenesCompraPage(0, 100, "", "");
    this.store.loadProveedores();
    this.loading.set(false);
  }

  onSearchChange(value: string): void {
    this.searchQuery.set(value);
    this.currentPage.set(1);
  }

  onFechaDesdeChange(value: string): void {
    this.fechaDesde.set(value);
    this.currentPage.set(1);
  }

  onFechaHastaChange(value: string): void {
    this.fechaHasta.set(value);
    this.currentPage.set(1);
  }

  navigateToCreate(): void {
    this.editingOrdenId.set(null);
    this.initForm();
    this.ordenForm.patchValue({ codigo: this.generateCodigo() });
    this.view.set("create");
  }

  generateCodigo(): string {
    const year = new Date().getFullYear();
    const random = Math.floor(1000 + Math.random() * 9000);
    return `OC-${year}-${random}`;
  }

  addLinea(): void {
    const lineaGroup = this.fb.group({
      itemTipo: ["INGREDIENTE", Validators.required],
      itemId: ["", Validators.required],
      cantidad: [null, [Validators.required, Validators.min(0.01)]],
      precioUnitario: [null, [Validators.required, Validators.min(0)]],
    });
    this.lineasArray.push(lineaGroup);
  }

  removeLinea(index: number): void {
    this.lineasArray.removeAt(index);
  }

  getFilteredItems(tipo: string): any[] {
    if (tipo === "INGREDIENTE") {
      return this.store.ingredientesPage().content;
    }
    if (tipo === "INSUMO") {
      return this.store.insumosPage().content;
    }
    return [
      ...this.store.ingredientesPage().content,
      ...this.store.insumosPage().content,
    ];
  }

  onItemChange(index: number): void {
    const linea = this.lineasArray.at(index);
    const itemId = linea.get("itemId")?.value;
    const tipo = linea.get("itemTipo")?.value;
    let items: any[] = [];
    if (tipo === "INGREDIENTE") {
      items = this.store.ingredientesPage().content;
    } else {
      items = this.store.insumosPage().content;
    }
    const item = items.find((i) => i.id === Number(itemId));
    if (item) {
      linea.patchValue({ precioUnitario: item.costoReferencial || 0 });
    }
  }

  getLineaSubtotal(index: number): number {
    const linea = this.lineasArray.at(index);
    const cantidad = Number(linea.get("cantidad")?.value) || 0;
    const precio = Number(linea.get("precioUnitario")?.value) || 0;
    return cantidad * precio;
  }

  getTotalEstimado(): number {
    let total = 0;
    for (let i = 0; i < this.lineasArray.length; i++) {
      total += this.getLineaSubtotal(i);
    }
    return total;
  }

  saveOrden(): void {
    if (this.ordenForm.invalid) {
      this.ordenForm.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    const formValue = this.ordenForm.value;
    const ordenData = {
      proveedorId: Number(formValue.proveedorId),
      codigo: formValue.codigo,
      fechaEntregaEstimada: formValue.fechaEntregaEstimada || null,
      observaciones: formValue.observaciones || "",
      detalles: formValue.lineas.map((l: any) => ({
        itemId: Number(l.itemId),
        itemTipo: l.itemTipo,
        cantidad: Number(l.cantidad),
        precioUnitario: Number(l.precioUnitario),
      })),
    };
    if (this.editingOrdenId()) {
      this.store.updateOrdenCompra(this.editingOrdenId()!, ordenData, () => {
        this.saving.set(false);
        this.backToList();
      });
    } else {
      this.store.createOrdenCompra(ordenData, () => {
        this.saving.set(false);
        this.backToList();
      });
    }
  }

  editOrden(orden: any): void {
    this.store.loadOrdenCompraDetail(orden.id).subscribe({
      next: (detalle) => {
        this.editingOrdenId.set(orden.id);
        this.initForm();
        this.ordenForm.patchValue({
          proveedorId: detalle.proveedorId,
          codigo: detalle.codigo,
          fechaEntregaEstimada: detalle.fechaEntregaEstimada || "",
          observaciones: detalle.observaciones || "",
        });
        this.lineasArray.clear();
        detalle.detalles.forEach((linea: any) => {
          const group = this.fb.group({
            itemTipo: [linea.itemTipo, Validators.required],
            itemId: [linea.itemId, Validators.required],
            cantidad: [
              linea.cantidad,
              [Validators.required, Validators.min(0.01)],
            ],
            precioUnitario: [
              linea.precioUnitario,
              [Validators.required, Validators.min(0)],
            ],
          });
          this.lineasArray.push(group);
        });
        this.view.set("create");
      },
    });
  }

  editOrdenFromDetail(): void {
    const orden = this.detailOrden();
    if (orden) {
      this.editingOrdenId.set(orden.id);
      this.initForm();
      this.ordenForm.patchValue({
        proveedorId: orden.proveedorId,
        codigo: orden.codigo,
        fechaEntregaEstimada: orden.fechaEntregaEstimada || "",
        observaciones: orden.observaciones || "",
      });
      this.lineasArray.clear();
      orden.detalles.forEach((linea: any) => {
        const group = this.fb.group({
          itemTipo: [linea.itemTipo, Validators.required],
          itemId: [linea.itemId, Validators.required],
          cantidad: [
            linea.cantidad,
            [Validators.required, Validators.min(0.01)],
          ],
          precioUnitario: [
            linea.precioUnitario,
            [Validators.required, Validators.min(0)],
          ],
        });
        this.lineasArray.push(group);
      });
      this.view.set("create");
    }
  }

  openDetail(orden: any): void {
    this.store.loadOrdenCompraDetail(orden.id).subscribe({
      next: (detalle) => {
        this.detailOrden.set(detalle);
        this.showReceiveSection.set(false);
        this.view.set("detail");
      },
    });
  }

  startReceiving(orden: any): void {
    this.store.loadOrdenCompraDetail(orden.id).subscribe({
      next: (detalle) => {
        this.detailOrden.set(detalle);
        this.receivingOrdenId.set(orden.id);
        const items: ReceiveItem[] = detalle.detalles.map((l: any) => ({
          detalleId: l.id,
          itemNombre: l.itemNombre,
          itemTipo: l.itemTipo,
          solicitado: l.cantidad,
          recibido: l.cantidadRecibida,
          precioUnitario: l.precioUnitario,
          cantidadRecibir: l.cantidad - l.cantidadRecibida,
          observaciones: "",
        }));
        this.receiveItems.set(items);
        this.showReceiveSection.set(true);
        this.view.set("detail");
      },
    });
  }

  confirmarRecepcion(): void {
    this.saving.set(true);
    const ordenId = this.receivingOrdenId();
    if (!ordenId) return;
    const items = this.receiveItems().map((item) => ({
      detalleId: item.detalleId,
      cantidadRecibida: item.cantidadRecibir,
    }));
    this.store.receiveOrdenCompra(ordenId, { items }, () => {
      this.store.loadOrdenCompraDetail(ordenId).subscribe({
        next: (detalle) => {
          this.detailOrden.set(detalle);
        },
      });
      this.showReceiveSection.set(false);
      this.receivingOrdenId.set(null);
      this.receiveItems.set([]);
      this.saving.set(false);
    });
  }

  enviarOrden(): void {
    const orden = this.detailOrden();
    if (orden) {
      this.store.updateOrdenCompraEstado(orden.id, "ENVIADA");
      this.store.loadOrdenCompraDetail(orden.id).subscribe({
        next: (updated) => this.detailOrden.set(updated),
      });
    }
  }

  cancelOrden(): void {
    const orden = this.detailOrden();
    if (orden) {
      this.store.updateOrdenCompraEstado(orden.id, "CANCELADA");
      this.store.loadOrdenCompraDetail(orden.id).subscribe({
        next: (updated) => this.detailOrden.set(updated),
      });
    }
  }

  backToList(): void {
    this.view.set("list");
    this.editingOrdenId.set(null);
    this.detailOrden.set(null);
    this.receivingOrdenId.set(null);
    this.receiveItems.set([]);
    this.showReceiveSection.set(false);
    this.currentPage.set(1);
    this.loadData();
  }

  toggleEstadoFilter(estado: string): void {
    const current = this.estadoFilter();
    if (current.includes(estado)) {
      this.estadoFilter.set(current.filter((e) => e !== estado));
    } else {
      this.estadoFilter.set([...current, estado]);
    }
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages()) {
      this.currentPage.set(page);
    }
  }

  formatDate(dateStr: string | undefined): string {
    if (!dateStr) return "-";
    try {
      const parts = dateStr.split("-");
      if (parts.length === 3) {
        return `${parts[2]}/${parts[1]}/${parts[0].substring(2)}`;
      }
      return dateStr;
    } catch {
      return dateStr;
    }
  }

  isOverdue(dateStr: string | undefined): boolean {
    if (!dateStr) return false;
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const delivery = new Date(dateStr);
    return delivery < today;
  }

  formatEstado(estado: string): string {
    const map: Record<string, string> = {
      BORRADOR: "Borrador",
      ENVIADA: "Enviada",
      RECIBIDA_PARCIAL: "Parcial",
      RECIBIDA: "Recibida",
      CANCELADA: "Cancelada",
    };
    return map[estado] || estado;
  }

  getEstadoClass(estado: string): string {
    return estado;
  }
}
