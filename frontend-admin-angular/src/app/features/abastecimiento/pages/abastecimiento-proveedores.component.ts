import { CommonModule, DecimalPipe } from "@angular/common";
import { Component, OnInit, computed, inject, signal } from "@angular/core";
import { ReactiveFormsModule, FormBuilder, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { ApiClientService } from "../../../core/api/api-client.service";
import { BackofficeStoreService } from "../../../core/store/backoffice-store.service";
import { ADMIN_SURFACE_STYLES } from "../../../shared/ui/admin-surface.styles";
import type {
  ProveedorSummary,
  ItemProveedorSummary,
  CreateProveedorRequest,
  UpdateProveedorRequest,
} from "../models/abastecimiento.models";

@Component({
  selector: "app-abastecimiento-proveedores",
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, DecimalPipe],
  template: `
    <div class="admin-grid admin-grid--split">
      <!-- LEFT PANEL: Form -->
      <section class="surface-card surface-card--tinted">
        <header class="surface-header">
          <p class="surface-kicker">Ficha de proveedor</p>
          <div class="surface-title-row">
            <img
              src="assets/icons/abastecimiento/store.svg"
              alt=""
              width="28"
              height="28"
              aria-hidden="true"
            />
            <h3>
              {{ editingProveedor() ? "Editar proveedor" : "Nuevo proveedor" }}
            </h3>
          </div>
          <p class="surface-copy">
            Datos de contacto y condiciones comerciales
          </p>
        </header>

        <form
          *ngIf="editingProveedor() || isCreating(); else emptyForm"
          [formGroup]="proveedorForm"
          (ngSubmit)="onSaveProveedor()"
          class="surface-form"
        >
          <div class="surface-row surface-row--2">
            <label
              >Código
              <input
                type="text"
                formControlName="codigo"
                placeholder="Ej: PROV-001"
              />
            </label>
            <label
              >Nombre *
              <input
                type="text"
                formControlName="nombre"
                placeholder="Nombre del proveedor"
              />
            </label>
          </div>

          <div class="surface-row surface-row--2">
            <label
              >Teléfono
              <input
                type="tel"
                formControlName="telefono"
                placeholder="(55) 1234-5678"
              />
            </label>
            <label
              >Correo
              <input
                type="email"
                formControlName="correo"
                placeholder="correo@ejemplo.com"
              />
            </label>
          </div>

          <label
            >Dirección
            <textarea
              formControlName="direccion"
              rows="2"
              placeholder="Dirección completa"
            ></textarea>
          </label>

          <label
            >Observaciones
            <textarea
              formControlName="observaciones"
              rows="2"
              placeholder="Notas adicionales"
            ></textarea>
          </label>

          <label class="toggle-row">
            <input type="checkbox" formControlName="activo" />
            Proveedor activo
          </label>

          <div class="action-row">
            <button type="button" class="mini-button" (click)="onCancelEdit()">
              Cancelar
            </button>
            <button
              type="submit"
              class="surface-button"
              [disabled]="proveedorForm.invalid || saving()"
            >
              <span *ngIf="saving()">Guardando...</span>
              <span *ngIf="!saving()">Guardar proveedor</span>
            </button>
          </div>
        </form>

        <ng-template #emptyForm>
          <div class="empty-state">
            <p>Seleccione un proveedor para editar sus datos</p>
            <button class="surface-button" (click)="onNewProveedor()">
              <img
                src="assets/icons/abastecimiento/plus.svg"
                alt=""
                width="16"
                height="16"
              />
              Nuevo proveedor
            </button>
          </div>
        </ng-template>
      </section>

      <!-- RIGHT PANEL: Table -->
      <section class="surface-card">
        <header class="surface-header">
          <p class="surface-kicker">Base de proveedores</p>
          <div class="surface-title-row">
            <img
              src="assets/icons/abastecimiento/store.svg"
              alt=""
              width="28"
              height="28"
              aria-hidden="true"
            />
            <h3>Directorio de proveedores</h3>
          </div>
          <div class="chip-row">
            <span class="summary-chip">
              {{ filteredProveedores().length }} proveedores
            </span>
          </div>
        </header>

        <div class="page-toolbar page-toolbar--search">
          <label class="search-box">
            <span>Buscar proveedor</span>
            <input
              type="search"
              [value]="searchQuery()"
              (input)="onSearchChange($event)"
              placeholder="Nombre, código o teléfono..."
            />
          </label>
        </div>

        <div class="table-shell" *ngIf="!loading(); else loadingState">
          <table
            class="surface-table surface-table--wide"
            *ngIf="filteredProveedores().length; else emptyState"
          >
            <thead>
              <tr>
                <th>Código</th>
                <th>Nombre</th>
                <th>Contacto</th>
                <th>Estado</th>
                <th class="text-center">Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr
                *ngFor="let proveedor of filteredProveedores()"
                [class.selected]="editingProveedor()?.id === proveedor.id"
              >
                <td class="mono">{{ proveedor.codigo || "—" }}</td>
                <td>
                  <strong>{{ proveedor.nombre }}</strong>
                </td>
                <td>
                  <p class="surface-meta">
                    {{ proveedor.telefono || "Sin teléfono" }}
                  </p>
                  <p class="surface-meta">
                    {{ proveedor.correo || "Sin correo" }}
                  </p>
                </td>
                <td>
                  <span
                    class="pill"
                    [class]="
                      proveedor.activo ? 'pill--activo' : 'pill--inactivo'
                    "
                  >
                    {{ proveedor.activo ? "ACTIVO" : "INACTIVO" }}
                  </span>
                </td>
                <td class="text-center">
                  <div class="action-row action-row--centered">
                    <button
                      class="mini-button"
                      (click)="onOpenCatalog(proveedor)"
                      title="Ver catálogo"
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
                      (click)="onEditProveedor(proveedor)"
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
                      class="mini-button"
                      [class.mini-button--danger]="!proveedor.activo"
                      (click)="onToggleActive(proveedor)"
                      [title]="proveedor.activo ? 'Desactivar' : 'Activar'"
                    >
                      <img
                        [src]="
                          proveedor.activo
                            ? 'assets/icons/abastecimiento/cancel.svg'
                            : 'assets/icons/abastecimiento/check.svg'
                        "
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
            <p>Cargando proveedores...</p>
          </div>
        </ng-template>

        <ng-template #emptyState>
          <div class="empty-state">No se encontraron proveedores</div>
        </ng-template>
      </section>
    </div>

    <!-- CATALOG PANEL -->
    <div
      class="panel-overlay"
      *ngIf="catalogProveedorId()"
      (click)="onCloseCatalog()"
    ></div>
    <aside class="item-panel catalog-panel" *ngIf="catalogProveedorId()">
      <header class="surface-header">
        <p class="surface-kicker">Catálogo de artículos</p>
        <div class="surface-title-row">
          <h3>{{ catalogProveedor()?.nombre }}</h3>
          <button class="mini-button" (click)="onCloseCatalog()">
            <img
              src="assets/icons/abastecimiento/close.svg"
              alt=""
              width="14"
              height="14"
            />
          </button>
        </div>
      </header>

      <div class="kpi-strip">
        <div class="kpi-mini">
          <span>Total items</span>
          <strong>{{ providerItems().length }}</strong>
        </div>
        <div class="kpi-mini">
          <span>Principales</span>
          <strong>{{ principalesCount() }}</strong>
        </div>
      </div>

      <div
        class="table-shell"
        *ngIf="!loadingCatalog(); else loadingCatalogState"
      >
        <table
          class="surface-table"
          *ngIf="providerItems().length; else emptyCatalog"
        >
          <thead>
            <tr>
              <th>Artículo</th>
              <th>Tipo</th>
              <th class="text-right">Precio</th>
              <th class="text-center">Principal</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let item of providerItems()">
              <td>{{ item.itemNombre }}</td>
              <td>
                <span class="pill pill--small">{{ item.itemTipo }}</span>
              </td>
              <td class="text-right">
                {{ item.precioSuministro | number: "1.2-2" }}
              </td>
              <td class="text-center">
                <span *ngIf="item.esPrincipal" class="text-success">✓</span>
                <span *ngIf="!item.esPrincipal" class="text-muted">—</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <ng-template #loadingCatalogState>
        <div class="loading-state">
          <div class="spinner"></div>
        </div>
      </ng-template>

      <ng-template #emptyCatalog>
        <div class="empty-state">
          Este proveedor no tiene artículos registrados
        </div>
      </ng-template>

      <div class="panel-actions">
        <button class="surface-button" (click)="onNuevaOrden()">
          <img
            src="assets/icons/abastecimiento/cart.svg"
            alt=""
            width="16"
            height="16"
          />
          Nueva orden
        </button>
      </div>
    </aside>
  `,
  styles: [
    ADMIN_SURFACE_STYLES,
    `
      .toggle-row {
        display: flex !important;
        align-items: center;
        gap: 0.65rem;
      }

      .toggle-row input {
        width: auto;
        margin: 0;
      }

      .mono {
        font-family: "JetBrains Mono", monospace;
        font-size: 0.8rem;
        color: #8a5c46;
      }

      .text-center {
        text-align: center;
      }

      .text-right {
        text-align: right;
      }

      .text-success {
        color: #1b5e20;
        font-weight: 600;
      }

      .text-muted {
        color: #b0a098;
      }

      tr.selected {
        background: #fdf3e7 !important;
      }

      .pill--activo {
        background: #e8f5e9;
        color: #1b5e20;
      }

      .pill--inactivo {
        background: #eceff1;
        color: #546e7a;
      }

      .pill--small {
        font-size: 0.65rem;
        padding: 0.15rem 0.4rem;
      }

      .mini-button--danger {
        background: #fdecea;
        color: #b71c1c;
      }

      .action-row--centered {
        justify-content: center;
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
        background: linear-gradient(
          180deg,
          rgba(255, 247, 241, 0.95),
          rgba(255, 252, 249, 0.95)
        );
        border: 1px solid #eaded4;
        border-radius: 4px;
      }

      .kpi-mini span {
        font-size: 0.72rem;
        color: #8a5c46;
        text-transform: uppercase;
        letter-spacing: 0.05em;
      }

      .kpi-mini strong {
        font: 700 1.5rem/1
          var(--font-display, "Cormorant Garamond", Georgia, serif);
        color: #2d201a;
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
export class AbastecimientoProveedoresComponent implements OnInit {
  private readonly api = inject(ApiClientService);
  private readonly store = inject(BackofficeStoreService);
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);

  readonly loading = this.store.loading;
  readonly saving = signal(false);
  readonly loadingCatalog = signal(false);
  readonly isCreating = signal(false);

  readonly editingProveedor = signal<ProveedorSummary | null>(null);
  readonly catalogProveedorId = signal<number | null>(null);
  readonly catalogProveedor = signal<ProveedorSummary | null>(null);
  readonly providerItems = signal<ItemProveedorSummary[]>([]);
  readonly searchQuery = signal("");

  readonly filteredProveedores = computed(() => {
    const query = this.searchQuery().toLowerCase();
    const proveedores = this.store.proveedores();
    if (!query) return proveedores;
    return proveedores.filter(
      (p) =>
        p.nombre.toLowerCase().includes(query) ||
        (p.codigo && p.codigo.toLowerCase().includes(query)) ||
        (p.telefono && p.telefono.includes(query)),
    );
  });

  readonly principalesCount = computed(
    () => this.providerItems().filter((i) => i.esPrincipal).length,
  );

  readonly proveedorForm = this.fb.group({
    codigo: [""],
    nombre: ["", Validators.required],
    telefono: [""],
    correo: [""],
    direccion: [""],
    observaciones: [""],
    activo: [true],
  });

  ngOnInit(): void {
    this.store.loadProveedores();
  }

  onSearchChange(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.searchQuery.set(value);
  }

  onEditProveedor(proveedor: ProveedorSummary): void {
    this.isCreating.set(false);
    this.editingProveedor.set(proveedor);
    this.proveedorForm.patchValue({
      codigo: proveedor.codigo || "",
      nombre: proveedor.nombre,
      telefono: proveedor.telefono || "",
      correo: proveedor.correo || "",
      direccion: proveedor.direccion || "",
      observaciones: proveedor.observaciones || "",
      activo: proveedor.activo,
    });
  }

  onNewProveedor(): void {
    this.isCreating.set(true);
    this.editingProveedor.set(null);
    this.proveedorForm.reset({ activo: true });
  }

  onCancelEdit(): void {
    this.editingProveedor.set(null);
    this.isCreating.set(false);
    this.proveedorForm.reset({ activo: true });
  }

  async onSaveProveedor(): Promise<void> {
    if (this.proveedorForm.invalid) return;

    const formValue = this.proveedorForm.value;
    this.saving.set(true);

    try {
      if (this.isCreating()) {
        const request: CreateProveedorRequest = {
          codigo: formValue.codigo || "",
          nombre: formValue.nombre!,
          telefono: formValue.telefono || "",
          correo: formValue.correo || "",
          direccion: formValue.direccion || "",
          observaciones: formValue.observaciones || "",
        };
        await this.store.createProveedor(request);
      } else if (this.editingProveedor()) {
        const request: UpdateProveedorRequest = {
          codigo: formValue.codigo || "",
          nombre: formValue.nombre!,
          telefono: formValue.telefono || "",
          correo: formValue.correo || "",
          direccion: formValue.direccion || "",
          observaciones: formValue.observaciones || "",
          activo: formValue.activo ?? true,
        };
        await this.store.updateProveedor(this.editingProveedor()!.id, request);
      }
      this.onCancelEdit();
      this.store.loadProveedores();
    } finally {
      this.saving.set(false);
    }
  }

  async onToggleActive(proveedor: ProveedorSummary): Promise<void> {
    this.store.toggleProveedorActivo(proveedor.id);
  }

  onOpenCatalog(proveedor: ProveedorSummary): void {
    this.catalogProveedorId.set(proveedor.id);
    this.catalogProveedor.set(proveedor);
    this.loadingCatalog.set(true);
    this.api.getItemsProveedorPorProveedor(proveedor.id).subscribe({
      next: (items) => {
        this.providerItems.set(items);
        this.loadingCatalog.set(false);
      },
      error: () => {
        this.providerItems.set([]);
        this.loadingCatalog.set(false);
      },
    });
  }

  onCloseCatalog(): void {
    this.catalogProveedorId.set(null);
    this.catalogProveedor.set(null);
    this.providerItems.set([]);
  }

  onNuevaOrden(): void {
    const proveedorId = this.catalogProveedorId();
    if (proveedorId) {
      this.router.navigate(["/abastecimiento/compras"], {
        queryParams: { proveedorId },
      });
    }
  }
}
