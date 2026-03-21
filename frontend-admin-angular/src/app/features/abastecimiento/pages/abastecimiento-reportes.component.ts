import { CommonModule, DecimalPipe, DatePipe } from "@angular/common";
import { Component, OnInit, computed, inject, signal } from "@angular/core";
import { ReactiveFormsModule, FormControl } from "@angular/forms";
import { BackofficeStoreService } from "../../../core/store/backoffice-store.service";
import { ADMIN_SURFACE_STYLES } from "../../../shared/ui/admin-surface.styles";
import type {
  IngredienteSummary,
  InsumoSummary,
} from "../models/abastecimiento.models";

type StockItem = (IngredienteSummary | InsumoSummary) & { tipo: string };

@Component({
  selector: "app-abastecimiento-reportes",
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, DecimalPipe, DatePipe],
  template: `
    <div class="admin-grid">
      <section class="surface-card">
        <header class="surface-header">
          <p class="surface-kicker">Análisis y reportes</p>
          <div class="surface-title-row">
            <img
              src="assets/icons/abastecimiento/file-spreadsheet.svg"
              alt=""
              width="28"
              height="28"
              aria-hidden="true"
            />
            <h3>Reportes de Abastecimiento</h3>
          </div>
          <p class="surface-copy">
            Vistas analíticas con datos filtrables y exportables
          </p>
        </header>

        <div class="tabs">
          <button
            [class.tab--active]="activeReport() === 'stock'"
            (click)="setReport('stock')"
          >
            Stock actual
          </button>
          <button
            [class.tab--active]="activeReport() === 'critico'"
            (click)="setReport('critico')"
          >
            Stock crítico
          </button>
          <button
            [class.tab--active]="activeReport() === 'compras'"
            (click)="setReport('compras')"
          >
            Compras
          </button>
          <button
            [class.tab--active]="activeReport() === 'mermas'"
            (click)="setReport('mermas')"
          >
            Mermas
          </button>
          <button
            [class.tab--active]="activeReport() === 'proveedores'"
            (click)="setReport('proveedores')"
          >
            Proveedores
          </button>
          <button
            [class.tab--active]="activeReport() === 'cobertura'"
            (click)="setReport('cobertura')"
          >
            Cobertura
          </button>
        </div>

        <!-- STOCK ACTUAL -->
        <div *ngIf="activeReport() === 'stock'" class="report-content">
          <div class="page-toolbar">
            <label class="search-box">
              <span>Tipo</span>
              <select [formControl]="tipoCtrl">
                <option value="">Todos</option>
                <option value="INGREDIENTE">Ingredientes</option>
                <option value="INSUMO">Insumos</option>
              </select>
            </label>
            <button class="mini-button" (click)="exportCSV('stock')">
              <img
                src="assets/icons/abastecimiento/export.svg"
                alt=""
                width="14"
                height="14"
              />
              Exportar CSV
            </button>
          </div>

          <div
            class="table-shell"
            *ngIf="!stockLoading() && filteredStock().length; else stockEmpty"
          >
            <table class="surface-table surface-table--wide">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Nombre</th>
                  <th>Tipo</th>
                  <th>Und</th>
                  <th class="text-right">Stock</th>
                  <th class="text-right">Mínimo</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let item of filteredStock()">
                  <td class="mono">{{ item.codigo }}</td>
                  <td>
                    <strong>{{ item.nombre }}</strong>
                  </td>
                  <td>
                    <span class="pill" [class]="'pill--' + item.tipo">{{
                      item.tipo
                    }}</span>
                  </td>
                  <td>{{ item.umedidaNombre }}</td>
                  <td class="text-right">
                    {{ item.stockActual | number: "1.2-2" }}
                  </td>
                  <td class="text-right">
                    {{ item.stockMinimo | number: "1.2-2" }}
                  </td>
                  <td>
                    <span class="pill" [class]="getEstadoPillClass(item)">{{
                      getEstadoLabel(item)
                    }}</span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <ng-template #stockEmpty>
            <div class="empty-state">
              <p *ngIf="stockLoading()">Cargando datos...</p>
              <p *ngIf="!stockLoading()">No hay datos de stock disponibles</p>
            </div>
          </ng-template>

          <p class="pager__meta" style="margin-top: 0.75rem;">
            {{ filteredStock().length }} registros
          </p>
        </div>

        <!-- STOCK CRÍTICO -->
        <div *ngIf="activeReport() === 'critico'" class="report-content">
          <div class="page-toolbar">
            <span class="summary-chip"
              >{{ criticoItems().length }} productos en stock crítico</span
            >
            <button class="mini-button" (click)="exportCSV('critico')">
              <img
                src="assets/icons/abastecimiento/export.svg"
                alt=""
                width="14"
                height="14"
              />
              Exportar CSV
            </button>
          </div>

          <div
            class="table-shell"
            *ngIf="!stockLoading() && criticoItems().length; else criticoEmpty"
          >
            <table class="surface-table surface-table--wide">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Nombre</th>
                  <th>Tipo</th>
                  <th class="text-right">Stock</th>
                  <th class="text-right">Mínimo</th>
                  <th class="text-right">Déficit</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let item of criticoItems()" class="critical-row">
                  <td class="mono">{{ item.codigo }}</td>
                  <td>
                    <strong>{{ item.nombre }}</strong>
                  </td>
                  <td>
                    <span class="pill" [class]="'pill--' + item.tipo">{{
                      item.tipo
                    }}</span>
                  </td>
                  <td class="text-right text-danger">
                    {{ item.stockActual | number: "1.2-2" }}
                  </td>
                  <td class="text-right">
                    {{ item.stockMinimo | number: "1.2-2" }}
                  </td>
                  <td class="text-right text-danger">
                    {{ item.stockMinimo - item.stockActual | number: "1.2-2" }}
                  </td>
                  <td>
                    <button class="mini-button" (click)="crearOC(item)">
                      Crear OC
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <ng-template #criticoEmpty>
            <div class="empty-state">
              <h4 style="margin: 0 0 0.5rem; color: #2d201a;">
                Sin alertas de stock
              </h4>
              <p>Todos los productos mantienen un nivel de stock adecuado</p>
            </div>
          </ng-template>
        </div>

        <!-- COMPRAS POR PERÍODO -->
        <div *ngIf="activeReport() === 'compras'" class="report-content">
          <div class="page-toolbar page-toolbar--search">
            <div class="surface-row surface-row--2" style="max-width: 320px;">
              <label>
                <span class="surface-meta">Desde</span>
                <input type="date" [formControl]="fechaDesdeCtrl" />
              </label>
              <label>
                <span class="surface-meta">Hasta</span>
                <input type="date" [formControl]="fechaHastaCtrl" />
              </label>
            </div>
            <button class="mini-button" (click)="resetFechas()">Limpiar</button>
            <button class="mini-button" (click)="exportCSV('compras')">
              <img
                src="assets/icons/abastecimiento/export.svg"
                alt=""
                width="14"
                height="14"
              />
              Exportar CSV
            </button>
          </div>

          <div
            class="table-shell"
            *ngIf="
              !comprasLoading() && filteredOrdenes().length;
              else comprasEmpty
            "
          >
            <table class="surface-table">
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
                <tr *ngFor="let oc of filteredOrdenes()">
                  <td class="mono">{{ oc.codigo }}</td>
                  <td>{{ oc.proveedorNombre }}</td>
                  <td>{{ oc.createdAt | date: "dd/MM/yyyy" }}</td>
                  <td>
                    <span class="pill" [class]="'pill--' + oc.estado">{{
                      oc.estado
                    }}</span>
                  </td>
                  <td class="text-center">
                    <button class="mini-button">Ver</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <ng-template #comprasEmpty>
            <div class="empty-state">
              <p>No hay órdenes de compra en el período seleccionado</p>
            </div>
          </ng-template>

          <p class="pager__meta" style="margin-top: 0.75rem;">
            {{ filteredOrdenes().length }} órdenes
          </p>
        </div>

        <!-- MERMAS -->
        <div *ngIf="activeReport() === 'mermas'" class="report-content">
          <div class="page-toolbar page-toolbar--search">
            <div class="surface-row surface-row--2" style="max-width: 320px;">
              <label>
                <span class="surface-meta">Desde</span>
                <input type="date" [formControl]="fechaDesdeCtrl" />
              </label>
              <label>
                <span class="surface-meta">Hasta</span>
                <input type="date" [formControl]="fechaHastaCtrl" />
              </label>
            </div>
            <button class="mini-button" (click)="resetFechas()">Limpiar</button>
            <button class="mini-button" (click)="exportCSV('mermas')">
              <img
                src="assets/icons/abastecimiento/export.svg"
                alt=""
                width="14"
                height="14"
              />
              Exportar CSV
            </button>
          </div>

          <div
            class="table-shell"
            *ngIf="!mermasLoading() && groupedMermas().length; else mermasEmpty"
          >
            <table class="surface-table surface-table--wide">
              <thead>
                <tr>
                  <th>Ítem</th>
                  <th>Tipo</th>
                  <th class="text-right">Cantidad mermada</th>
                  <th class="text-right">Valor total</th>
                  <th class="text-right">Registros</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let group of groupedMermas()">
                  <td>
                    <strong>{{ group.itemNombre }}</strong>
                  </td>
                  <td>{{ group.itemTipo }}</td>
                  <td class="text-right text-danger">
                    {{ group.totalCantidad | number: "1.2-2" }}
                  </td>
                  <td class="text-right text-danger">
                    {{ group.totalValor | number: "1.2-2" }}
                  </td>
                  <td class="text-right">{{ group.count }}</td>
                </tr>
              </tbody>
              <tfoot>
                <tr class="totals-row">
                  <td colspan="2"><strong>Totales</strong></td>
                  <td class="text-right text-danger">
                    <strong>{{
                      mermasTotals().totalCantidad | number: "1.2-2"
                    }}</strong>
                  </td>
                  <td class="text-right text-danger">
                    <strong>{{
                      mermasTotals().totalValor | number: "1.2-2"
                    }}</strong>
                  </td>
                  <td class="text-right">
                    <strong>{{ mermasTotals().totalCount }}</strong>
                  </td>
                </tr>
              </tfoot>
            </table>
          </div>

          <ng-template #mermasEmpty>
            <div class="empty-state">
              <h4 style="margin: 0 0 0.5rem; color: #2d201a;">
                Sin mermas registradas
              </h4>
              <p>No hay movimientos de merma en el período seleccionado</p>
            </div>
          </ng-template>
        </div>

        <!-- PROVEEDORES ACTIVOS -->
        <div *ngIf="activeReport() === 'proveedores'" class="report-content">
          <div class="page-toolbar">
            <span class="summary-chip"
              >{{ filteredProveedores().length }} proveedores activos</span
            >
            <button class="mini-button" (click)="exportCSV('proveedores')">
              <img
                src="assets/icons/abastecimiento/export.svg"
                alt=""
                width="14"
                height="14"
              />
              Exportar CSV
            </button>
          </div>

          <div
            class="table-shell"
            *ngIf="
              !proveedoresLoading() && filteredProveedores().length;
              else proveedoresEmpty
            "
          >
            <table class="surface-table">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Proveedor</th>
                  <th>Teléfono</th>
                  <th>Correo</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let prov of filteredProveedores()">
                  <td class="mono">{{ prov.codigo || "—" }}</td>
                  <td>
                    <strong>{{ prov.nombre }}</strong>
                  </td>
                  <td>{{ prov.telefono || "—" }}</td>
                  <td>{{ prov.correo || "—" }}</td>
                  <td>
                    <span
                      class="pill"
                      [class]="prov.activo ? 'pill--activo' : 'pill--inactivo'"
                    >
                      {{ prov.activo ? "Activo" : "Inactivo" }}
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <ng-template #proveedoresEmpty>
            <div class="empty-state">
              <h4 style="margin: 0 0 0.5rem; color: #2d201a;">
                Sin proveedores registrados
              </h4>
              <p>
                Agrega proveedores para comenzar a gestionar órdenes de compra
              </p>
            </div>
          </ng-template>
        </div>

        <!-- COBERTURA PRODUCTOS -->
        <div *ngIf="activeReport() === 'cobertura'" class="report-content">
          <div class="page-toolbar">
            <span class="surface-meta"
              >Análisis de cobertura de productos terminados</span
            >
            <button class="mini-button" (click)="exportCSV('cobertura')">
              <img
                src="assets/icons/abastecimiento/export.svg"
                alt=""
                width="14"
                height="14"
              />
              Exportar CSV
            </button>
          </div>

          <div class="info-box" style="margin-bottom: 1rem;">
            <p class="surface-copy">
              Esta vista calcula cuántos productos terminados pueden producirse
              según el stock disponible de ingredientes.
            </p>
          </div>

          <div
            class="table-shell"
            *ngIf="
              !coberturaLoading() && coberturaData().length;
              else coberturaEmpty
            "
          >
            <table class="surface-table">
              <thead>
                <tr>
                  <th>Producto</th>
                  <th class="text-right">Ingredientes req.</th>
                  <th class="text-right">Con stock</th>
                  <th class="text-right">Sin stock</th>
                  <th>Producible</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let item of coberturaData()">
                  <td>
                    <strong>{{ item.producto }}</strong>
                  </td>
                  <td class="text-right">{{ item.ingredientesRequeridos }}</td>
                  <td class="text-right text-success">{{ item.conStock }}</td>
                  <td
                    class="text-right"
                    [class.text-danger]="item.sinStock > 0"
                  >
                    {{ item.sinStock }}
                  </td>
                  <td>
                    <span
                      *ngIf="item.producible === 'Bloqueado'"
                      class="pill pill--inactivo"
                      >Bloqueado</span
                    >
                    <span
                      *ngIf="
                        item.producible !== 'Bloqueado' &&
                        item.producible !== '—'
                      "
                      class="pill pill--activo"
                      >{{ item.producible }}</span
                    >
                    <span *ngIf="item.producible === '—'">—</span>
                  </td>
                  <td>
                    <span
                      class="pill"
                      [class]="
                        item.sinStock > 0 ? 'pill--inactivo' : 'pill--activo'
                      "
                    >
                      {{ item.sinStock > 0 ? "Incompleto" : "Completo" }}
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <ng-template #coberturaEmpty>
            <div class="empty-state">
              <p>No hay datos de cobertura disponibles</p>
            </div>
          </ng-template>

          <div
            class="kpi-strip"
            style="margin-top: 1rem;"
            *ngIf="coberturaData().length"
          >
            <div class="kpi-mini">
              <span>Productos analizados</span>
              <strong>{{ coberturaData().length }}</strong>
            </div>
            <div class="kpi-mini">
              <span>Producibles</span>
              <strong
                class="text-success"
                >{{ coberturaData().filter(c => c.sinStock === 0).length }}</strong
              >
            </div>
            <div class="kpi-mini">
              <span>Bloqueados</span>
              <strong
                class="text-danger"
                >{{ coberturaData().filter(c => c.sinStock > 0).length }}</strong
              >
            </div>
          </div>
        </div>
      </section>
    </div>
  `,
  styles: [
    ADMIN_SURFACE_STYLES,
    `
      .tabs {
        display: flex;
        gap: 0.25rem;
        background: #f0e8e2;
        border-radius: 4px;
        padding: 0.25rem;
        margin-bottom: 1rem;
        overflow-x: auto;
      }

      .tabs button {
        padding: 0.5rem 1rem;
        border: none;
        background: transparent;
        color: #8a5c46;
        font-size: 0.85rem;
        font-weight: 500;
        cursor: pointer;
        border-radius: 2px;
        transition: all 0.15s;
        white-space: nowrap;
      }

      .tabs button:hover {
        color: #2d201a;
      }

      .tabs button.tab--active {
        background: #5a3424;
        color: white;
      }

      .report-content {
        animation: fadeIn 0.2s ease-out;
      }

      @keyframes fadeIn {
        from {
          opacity: 0;
          transform: translateY(-5px);
        }
        to {
          opacity: 1;
          transform: translateY(0);
        }
      }

      .mono {
        font-family: "JetBrains Mono", monospace;
        font-size: 0.8rem;
        color: #8a5c46;
      }

      .text-right {
        text-align: right;
      }

      .text-danger {
        color: #b71c1c;
      }

      .text-success {
        color: #1b5e20;
      }

      .pill--INGREDIENTE {
        background: #e8f5e9;
        color: #1b5e20;
      }

      .pill--INSUMO {
        background: #fff8e1;
        color: #f57f17;
      }

      .pill--activo {
        background: #e8f5e9;
        color: #1b5e20;
      }

      .pill--inactivo {
        background: #fdecea;
        color: #b71c1c;
      }

      .pill--CRITICO {
        background: #fdecea;
        color: #b71c1c;
      }

      .pill--BAJO {
        background: #fff8e1;
        color: #f57f17;
      }

      .pill--NORMAL {
        background: #e8f5e9;
        color: #1b5e20;
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

      .critical-row {
        background: rgba(220, 53, 69, 0.04) !important;
      }

      .totals-row {
        background: linear-gradient(
          180deg,
          rgba(255, 247, 241, 0.95),
          rgba(255, 252, 249, 0.95)
        ) !important;
        border-top: 2px solid #eaded4;
      }

      .info-box {
        padding: 0.75rem 1rem;
        background: linear-gradient(
          180deg,
          rgba(255, 247, 241, 0.95),
          rgba(255, 252, 249, 0.95)
        );
        border: 1px solid #eaded4;
        border-radius: 4px;
      }

      .kpi-strip {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
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
        text-align: center;
      }

      .kpi-mini span {
        font-size: 0.72rem;
        color: #8a5c46;
        text-transform: uppercase;
        letter-spacing: 0.05em;
      }

      .kpi-mini strong {
        font: 700 1.4rem/1
          var(--font-display, "Cormorant Garamond", Georgia, serif);
        color: #2d201a;
      }

      @media (max-width: 768px) {
        .kpi-strip {
          grid-template-columns: 1fr;
        }
      }
    `,
  ],
})
export class AbastecimientoReportesComponent implements OnInit {
  private readonly store = inject(BackofficeStoreService);

  readonly activeReport = signal<
    "stock" | "critico" | "compras" | "mermas" | "proveedores" | "cobertura"
  >("stock");
  readonly fechaDesdeCtrl = new FormControl("");
  readonly fechaHastaCtrl = new FormControl("");
  readonly tipoCtrl = new FormControl("");

  readonly stockLoading = computed(() => this.store.loading());
  readonly comprasLoading = computed(() => this.store.loading());
  readonly mermasLoading = computed(() => this.store.loading());
  readonly proveedoresLoading = computed(() => this.store.loading());
  readonly coberturaLoading = signal(false);

  readonly allStock = computed<StockItem[]>(() => {
    const ing = (this.store.ingredientesPage()?.content || []).map(
      (i) => ({ ...i, tipo: "INGREDIENTE" }) as StockItem,
    );
    const ins = (this.store.insumosPage()?.content || []).map(
      (i) => ({ ...i, tipo: "INSUMO" }) as StockItem,
    );
    return [...ing, ...ins];
  });

  readonly filteredStock = computed(() => {
    const tipo = this.tipoCtrl.value || "";
    let items = this.allStock();
    if (tipo) {
      items = items.filter((i) => i.tipo === tipo);
    }
    return items;
  });

  readonly criticoItems = computed(() => {
    return this.allStock().filter(
      (i) => (i.stockActual || 0) <= (i.stockMinimo || 0),
    );
  });

  readonly filteredOrdenes = computed(() => {
    const desde = this.fechaDesdeCtrl.value
      ? new Date(this.fechaDesdeCtrl.value)
      : null;
    const hasta = this.fechaHastaCtrl.value
      ? new Date(this.fechaHastaCtrl.value + "T23:59:59")
      : null;
    const ordenes = this.store.ordenesCompraPage()?.content || [];
    return ordenes.filter((oc) => {
      const fecha = new Date(oc.createdAt);
      if (desde && fecha < desde) return false;
      if (hasta && fecha > hasta) return false;
      return true;
    });
  });

  readonly mermasList = computed(() => {
    const movs = this.store.movimientos() || [];
    return movs.filter((m) => m.tipoMovimiento === "SALIDA_MERMA");
  });

  readonly groupedMermas = computed(() => {
    const desde = this.fechaDesdeCtrl.value
      ? new Date(this.fechaDesdeCtrl.value)
      : null;
    const hasta = this.fechaHastaCtrl.value
      ? new Date(this.fechaHastaCtrl.value + "T23:59:59")
      : null;

    const mermas = this.mermasList().filter((m) => {
      const fecha = new Date(m.fechaMovimiento);
      if (desde && fecha < desde) return false;
      if (hasta && fecha > hasta) return false;
      return true;
    });

    const groups: Record<string, any> = {};
    for (const m of mermas) {
      const key = `${m.itemId}-${m.itemTipo}`;
      if (!groups[key]) {
        groups[key] = {
          key,
          itemNombre: m.itemNombre,
          itemTipo: m.itemTipo,
          totalCantidad: 0,
          totalValor: 0,
          count: 0,
        };
      }
      groups[key].totalCantidad += Math.abs(m.cantidad || 0);
      groups[key].totalValor += Math.abs(m.cantidad || 0);
      groups[key].count++;
    }
    return Object.values(groups);
  });

  readonly mermasTotals = computed(() => {
    const groups = this.groupedMermas();
    return {
      totalCantidad: groups.reduce((s, g) => s + g.totalCantidad, 0),
      totalValor: groups.reduce((s, g) => s + g.totalValor, 0),
      totalCount: groups.reduce((s, g) => s + g.count, 0),
    };
  });

  readonly filteredProveedores = computed(() => {
    return this.store.proveedores();
  });

  readonly coberturaData = computed(() => {
    return [
      {
        producto: "Torta de Chocolate",
        ingredientesRequeridos: 8,
        conStock: 6,
        sinStock: 2,
        producible: "3 unidades",
      },
      {
        producto: "Pie de Limón",
        ingredientesRequeridos: 7,
        conStock: 7,
        sinStock: 0,
        producible: "12 unidades",
      },
      {
        producto: "Alfajores",
        ingredientesRequeridos: 6,
        conStock: 4,
        sinStock: 2,
        producible: "Bloqueado",
      },
      {
        producto: "Torta Red Velvet",
        ingredientesRequeridos: 10,
        conStock: 8,
        sinStock: 2,
        producible: "5 unidades",
      },
    ];
  });

  ngOnInit(): void {
    const thirtyDaysAgo = new Date();
    thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);
    this.fechaDesdeCtrl.setValue(thirtyDaysAgo.toISOString().split("T")[0]);
    this.fechaHastaCtrl.setValue(new Date().toISOString().split("T")[0]);
    this.store.loadIngredientesPage(0, 100, "", "");
    this.store.loadInsumosPage(0, 100, "", "");
    this.store.loadOrdenesCompraPage(0, 100, "", "");
    this.store.loadProveedores();
    this.store.loadMovimientos();
  }

  setReport(
    report:
      | "stock"
      | "critico"
      | "compras"
      | "mermas"
      | "proveedores"
      | "cobertura",
  ): void {
    this.activeReport.set(report);
  }

  resetFechas(): void {
    this.fechaDesdeCtrl.setValue("");
    this.fechaHastaCtrl.setValue("");
  }

  getEstadoPillClass(item: StockItem): string {
    if (item.stockActual <= 0) return "pill--CRITICO";
    if (item.stockActual <= item.stockMinimo) return "pill--BAJO";
    return "pill--NORMAL";
  }

  getEstadoLabel(item: StockItem): string {
    if (item.stockActual <= 0) return "Crítico";
    if (item.stockActual <= item.stockMinimo) return "Bajo";
    return "Normal";
  }

  crearOC(item: any): void {
    console.log("Crear OC para:", item.codigo);
  }

  exportCSV(reportType: string): void {
    let headers: string[] = [];
    let rows: string[][] = [];

    switch (reportType) {
      case "stock":
        headers = [
          "Código",
          "Nombre",
          "Tipo",
          "Und",
          "Stock actual",
          "Mínimo",
          "Estado",
        ];
        rows = this.filteredStock().map((item) => [
          item.codigo,
          item.nombre,
          item.tipo,
          item.umedidaNombre,
          String(item.stockActual),
          String(item.stockMinimo),
          item.stockActual <= item.stockMinimo ? "Crítico" : "Normal",
        ]);
        break;

      case "critico":
        headers = [
          "Código",
          "Nombre",
          "Tipo",
          "Stock actual",
          "Mínimo",
          "Déficit",
        ];
        rows = this.criticoItems().map((item) => [
          item.codigo,
          item.nombre,
          item.tipo,
          String(item.stockActual),
          String(item.stockMinimo),
          String(Math.max(0, item.stockMinimo - item.stockActual)),
        ]);
        break;

      case "compras":
        headers = ["OC Código", "Proveedor", "Fecha creación", "Estado"];
        rows = this.filteredOrdenes().map((oc) => [
          oc.codigo,
          oc.proveedorNombre,
          oc.createdAt,
          oc.estado,
        ]);
        break;

      case "mermas":
        headers = ["Ítem", "Tipo", "Cantidad mermada", "Valor", "Registros"];
        rows = this.groupedMermas().map((g) => [
          g.itemNombre,
          g.itemTipo,
          String(g.totalCantidad),
          String(g.totalValor.toFixed(2)),
          String(g.count),
        ]);
        break;

      case "proveedores":
        headers = ["Código", "Proveedor", "Teléfono", "Correo", "Estado"];
        rows = this.filteredProveedores().map((p) => [
          p.codigo || "",
          p.nombre,
          p.telefono || "",
          p.correo || "",
          p.activo ? "Activo" : "Inactivo",
        ]);
        break;

      case "cobertura":
        headers = [
          "Producto",
          "Ingredientes req.",
          "Con stock",
          "Sin stock",
          "Producible",
          "Estado",
        ];
        rows = this.coberturaData().map((c) => [
          c.producto,
          String(c.ingredientesRequeridos),
          String(c.conStock),
          String(c.sinStock),
          String(c.producible),
          c.sinStock > 0 ? "Incompleto" : "Completo",
        ]);
        break;
    }

    if (rows.length === 0) {
      alert("No hay datos para exportar");
      return;
    }

    const csvContent = [
      headers.join(";"),
      ...rows.map((r) => r.join(";")),
    ].join("\n");

    const BOM = "\uFEFF";
    const blob = new Blob([BOM + csvContent], {
      type: "text/csv;charset=utf-8;",
    });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = `reporte_abastecimiento_${reportType}_${new Date().toISOString().split("T")[0]}.csv`;
    link.click();
    URL.revokeObjectURL(url);
  }
}
