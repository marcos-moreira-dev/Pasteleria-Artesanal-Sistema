import { CommonModule, DecimalPipe, DatePipe } from "@angular/common";
import { Component, OnInit, computed, inject, signal } from "@angular/core";
import { ReactiveFormsModule, FormControl } from "@angular/forms";
import { ApiClientService } from "../../../core/api/api-client.service";
import { ADMIN_SURFACE_STYLES } from "../../../shared/ui/admin-surface.styles";
import type {
  InventarioMovimientoSummary,
  TipoMovimiento,
} from "../models/abastecimiento.models";

@Component({
  selector: "app-abastecimiento-movimientos",
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, DecimalPipe, DatePipe],
  template: `
    <div class="admin-grid">
      <section class="surface-card">
        <header class="surface-header">
          <p class="surface-kicker">Historial de movimientos</p>
          <div class="surface-title-row">
            <img
              src="assets/icons/abastecimiento/history.svg"
              alt=""
              width="28"
              height="28"
              aria-hidden="true"
            />
            <h3>Movimientos de Inventario</h3>
          </div>
          <p class="surface-copy">Registro de entradas, salidas y ajustes</p>
          <div class="chip-row">
            <button
              class="mini-button"
              (click)="showFilters.set(!showFilters())"
            >
              {{ showFilters() ? "Ocultar filtros" : "Mostrar filtros" }}
            </button>
            <button class="mini-button" (click)="onExportCsv()">
              <img
                src="assets/icons/abastecimiento/export.svg"
                alt=""
                width="14"
                height="14"
              />
              Exportar CSV
            </button>
          </div>
        </header>

        <div class="filters-panel" *ngIf="showFilters()">
          <div class="surface-row surface-row--4">
            <label
              >Tipo de movimiento
              <select [formControl]="tipoMovimientoCtrl">
                <option value="">Todos</option>
                <option value="ENTRADA_COMPRA">Entrada por compra</option>
                <option value="ENTRADA_AJUSTE">Entrada por ajuste</option>
                <option value="SALIDA_PRODUCCION">Salida a producción</option>
                <option value="SALIDA_MERMA">Salida por merma</option>
                <option value="SALIDA_AJUSTE">Salida por ajuste</option>
              </select>
            </label>
            <label
              >Fecha desde
              <input type="date" [formControl]="fechaDesdeCtrl" />
            </label>
            <label
              >Fecha hasta
              <input type="date" [formControl]="fechaHastaCtrl" />
            </label>
            <div class="filter-actions">
              <button class="surface-button" (click)="onApplyFilters()">
                Aplicar
              </button>
              <button class="mini-button" (click)="onCleanFilters()">
                Limpiar
              </button>
            </div>
          </div>
          <p class="pager__meta filters-panel__meta">
            {{ movimientos().length }} movimientos encontrados
          </p>
        </div>

        <div class="table-shell" *ngIf="!loading(); else loadingState">
          <table
            class="surface-table surface-table--wide"
            *ngIf="paginatedMovimientos().length; else emptyState"
          >
            <thead>
              <tr>
                <th>Fecha</th>
                <th>Tipo</th>
                <th>Ítem</th>
                <th class="text-right">Cantidad</th>
                <th class="text-right">Saldo</th>
                <th>Referencia</th>
                <th>Motivo</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let mov of paginatedMovimientos()">
                <td>
                  <span class="mono">{{
                    mov.fechaMovimiento | date: "dd/MM/yy HH:mm"
                  }}</span>
                </td>
                <td>
                  <span
                    class="pill"
                    [class]="'pill--' + getTipoClass(mov.tipoMovimiento)"
                  >
                    {{ formatTipo(mov.tipoMovimiento) }}
                  </span>
                </td>
                <td>
                  <strong>{{ mov.itemNombre }}</strong>
                  <p class="surface-meta mono">{{ mov.itemId }}</p>
                </td>
                <td class="text-right">
                  <span [class]="getCantidadClass(mov.tipoMovimiento)">
                    {{ getCantidadPrefix(mov.tipoMovimiento)
                    }}{{ mov.cantidad | number: "1.1-3" }}
                  </span>
                </td>
                <td class="text-right mono">
                  {{ mov.saldoPosterior | number: "1.1-3" }}
                </td>
                <td>{{ mov.referenciaTipo || "—" }}</td>
                <td>{{ mov.motivoSalida || "—" }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <ng-template #loadingState>
          <div class="loading-state">
            <div class="spinner"></div>
            <p>Cargando movimientos...</p>
          </div>
        </ng-template>

        <ng-template #emptyState>
          <div class="empty-state">
            <p>No se encontraron movimientos</p>
            <button
              *ngIf="hasActiveFilters()"
              class="mini-button"
              (click)="onCleanFilters()"
            >
              Limpiar filtros
            </button>
          </div>
        </ng-template>

        <div class="pager" *ngIf="totalPages() > 1">
          <p class="pager__meta">
            {{ paginationStart() + 1 }} - {{ paginationEnd() }} de
            {{ movimientos().length }}
          </p>
          <div class="pager__controls">
            <button
              class="mini-button"
              [disabled]="currentPage() === 1"
              (click)="onPageChange(currentPage() - 1)"
            >
              Anterior
            </button>
            <button
              *ngFor="let page of visiblePages()"
              class="mini-button"
              [class.active]="page === currentPage()"
              (click)="onPageChange(page)"
            >
              {{ page }}
            </button>
            <button
              class="mini-button"
              [disabled]="currentPage() === totalPages()"
              (click)="onPageChange(currentPage() + 1)"
            >
              Siguiente
            </button>
          </div>
        </div>
      </section>
    </div>
  `,
  styles: [
    ADMIN_SURFACE_STYLES,
    `
      .filters-panel {
        padding: 1rem;
        background: linear-gradient(
          180deg,
          rgba(255, 247, 241, 0.95),
          rgba(255, 252, 249, 0.95)
        );
        border: 1px solid #eaded4;
        border-radius: 4px;
        margin-bottom: 1rem;
      }

      .surface-row--4 {
        grid-template-columns: repeat(4, 1fr);
      }

      .filter-actions {
        display: flex;
        align-items: flex-end;
        gap: 0.5rem;
      }

      .filters-panel__meta {
        margin-top: 0.75rem;
      }

      .mono {
        font-family: "JetBrains Mono", monospace;
        font-size: 0.8rem;
      }

      .text-right {
        text-align: right;
      }

      .text-positive {
        color: #1b5e20;
        font-weight: 600;
      }

      .text-negative {
        color: #b71c1c;
        font-weight: 600;
      }

      .pill--entrada {
        background: #e8f5e9;
        color: #1b5e20;
      }

      .pill--salida-produccion {
        background: #e3f2fd;
        color: #1565c0;
      }

      .pill--salida-merma {
        background: #fdecea;
        color: #b71c1c;
      }

      .pill--ajuste {
        background: #fff8e1;
        color: #f57f17;
      }

      .pager__controls .mini-button.active {
        background: #5a3424;
        color: white;
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
        .surface-row--4 {
          grid-template-columns: 1fr;
        }
      }
    `,
  ],
})
export class AbastecimientoMovimientosComponent implements OnInit {
  private readonly api = inject(ApiClientService);

  readonly PAGE_SIZE = 20;
  readonly loading = signal(false);
  readonly showFilters = signal(true);
  readonly currentPage = signal(1);

  readonly filtros = signal<{
    tipoMovimiento: TipoMovimiento | "";
    itemTipo: "";
    itemId: number | null;
    fechaDesde: string;
    fechaHasta: string;
  }>({
    tipoMovimiento: "",
    itemTipo: "",
    itemId: null,
    fechaDesde: "",
    fechaHasta: "",
  });

  readonly movimientos = signal<InventarioMovimientoSummary[]>([]);

  readonly tipoMovimientoCtrl = new FormControl("");
  readonly fechaDesdeCtrl = new FormControl("");
  readonly fechaHastaCtrl = new FormControl("");

  readonly hasActiveFilters = computed(() => {
    const f = this.filtros();
    return !!(f.tipoMovimiento || f.fechaDesde || f.fechaHasta);
  });

  readonly totalPages = computed(() =>
    Math.ceil(this.movimientos().length / this.PAGE_SIZE),
  );

  readonly paginationStart = computed(
    () => (this.currentPage() - 1) * this.PAGE_SIZE,
  );

  readonly paginationEnd = computed(() =>
    Math.min(
      this.paginationStart() + this.PAGE_SIZE,
      this.movimientos().length,
    ),
  );

  readonly paginatedMovimientos = computed(() => {
    const start = this.paginationStart();
    const end = this.paginationEnd();
    return this.movimientos().slice(start, end);
  });

  readonly visiblePages = computed(() => {
    const total = this.totalPages();
    const current = this.currentPage();
    const pages: number[] = [];
    const start = Math.max(1, current - 2);
    const end = Math.min(total, current + 2);
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    return pages;
  });

  ngOnInit(): void {
    this.loadMovimientos();
  }

  loadMovimientos(): void {
    this.loading.set(true);
    this.currentPage.set(1);
    const f = this.filtros();
    this.api
      .getMovimientos(
        undefined,
        undefined,
        f.tipoMovimiento || undefined,
        f.fechaDesde || undefined,
        f.fechaHasta || undefined,
      )
      .subscribe({
        next: (movimientos) => {
          this.movimientos.set(movimientos);
          this.loading.set(false);
        },
        error: () => {
          this.movimientos.set([]);
          this.loading.set(false);
        },
      });
  }

  onApplyFilters(): void {
    this.filtros.set({
      tipoMovimiento: (this.tipoMovimientoCtrl.value as TipoMovimiento) || "",
      itemTipo: "",
      itemId: null,
      fechaDesde: this.fechaDesdeCtrl.value || "",
      fechaHasta: this.fechaHastaCtrl.value || "",
    });
    this.loadMovimientos();
  }

  onCleanFilters(): void {
    this.tipoMovimientoCtrl.reset("");
    this.fechaDesdeCtrl.reset("");
    this.fechaHastaCtrl.reset("");
    this.filtros.set({
      tipoMovimiento: "",
      itemTipo: "",
      itemId: null,
      fechaDesde: "",
      fechaHasta: "",
    });
    this.loadMovimientos();
  }

  onPageChange(page: number): void {
    if (page >= 1 && page <= this.totalPages()) {
      this.currentPage.set(page);
    }
  }

  getTipoClass(tipo: string): string {
    switch (tipo) {
      case "ENTRADA_COMPRA":
      case "ENTRADA_AJUSTE":
        return "entrada";
      case "SALIDA_PRODUCCION":
        return "salida-produccion";
      case "SALIDA_MERMA":
        return "salida-merma";
      case "SALIDA_AJUSTE":
        return "ajuste";
      default:
        return "";
    }
  }

  formatTipo(tipo: string): string {
    switch (tipo) {
      case "ENTRADA_COMPRA":
        return "Entrada compra";
      case "ENTRADA_AJUSTE":
        return "Entrada ajuste";
      case "SALIDA_PRODUCCION":
        return "Salida producción";
      case "SALIDA_MERMA":
        return "Salida merma";
      case "SALIDA_AJUSTE":
        return "Salida ajuste";
      default:
        return tipo;
    }
  }

  getCantidadPrefix(tipo: string): string {
    if (tipo.startsWith("ENTRADA")) return "+";
    if (tipo.startsWith("SALIDA")) return "-";
    return "";
  }

  getCantidadClass(tipo: string): string {
    if (tipo.startsWith("ENTRADA")) return "text-positive";
    if (tipo.startsWith("SALIDA")) return "text-negative";
    return "";
  }

  onExportCsv(): void {
    const movimientos = this.movimientos();
    if (movimientos.length === 0) return;

    const headers = [
      "Fecha y hora",
      "Tipo",
      "Ítem",
      "ID",
      "Cantidad",
      "Saldo posterior",
      "Ref. Tipo",
      "Motivo",
    ];
    const rows = movimientos.map((m) => [
      new Date(m.fechaMovimiento).toLocaleString("es-EC"),
      m.tipoMovimiento,
      m.itemNombre,
      String(m.itemId),
      m.cantidad.toString(),
      m.saldoPosterior.toString(),
      m.referenciaTipo || "",
      m.motivoSalida || "",
    ]);

    const csvContent = [
      headers.join(";"),
      ...rows.map((row) =>
        row.map((cell) => `"${String(cell).replace(/"/g, '""')}"`).join(";"),
      ),
    ].join("\n");

    const BOM = "\uFEFF";
    const blob = new Blob([BOM + csvContent], {
      type: "text/csv;charset=utf-8;",
    });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    const today = new Date().toISOString().split("T")[0];
    link.setAttribute("href", url);
    link.setAttribute("download", `movimientos_${today}.csv`);
    link.style.visibility = "hidden";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
  }
}
