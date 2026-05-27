import { CommonModule, CurrencyPipe, DatePipe, DecimalPipe } from "@angular/common";
import { Component, OnInit, computed, inject, signal } from "@angular/core";
import { ErpCoreFacade } from "../erp-core/state/erp-core.facade";
import { ADMIN_SURFACE_STYLES } from "../../shared/ui/admin-surface.styles";

interface WorkspaceTab {
  readonly key: WorkspaceKey;
  readonly label: string;
  readonly helper: string;
}

type WorkspaceKey =
  | "inteligencia"
  | "terceros"
  | "cartera"
  | "cuentasPagar"
  | "contabilidad"
  | "fiscalidad"
  | "bridges";

const WORKSPACE_TABS: readonly WorkspaceTab[] = [
  { key: "inteligencia", label: "Inteligencia", helper: "Pulso ERP" },
  { key: "terceros", label: "Terceros", helper: "Base común" },
  { key: "cartera", label: "Cartera", helper: "Cobros" },
  { key: "cuentasPagar", label: "Cuentas por pagar", helper: "Proveedores" },
  { key: "contabilidad", label: "Contabilidad", helper: "Asientos" },
  { key: "fiscalidad", label: "Fiscalidad", helper: "Interna" },
  { key: "bridges", label: "Bridges ERP", helper: "Trazabilidad" },
];

@Component({
  selector: "app-erp-workspaces-page",
  standalone: true,
  imports: [CommonModule, CurrencyPipe, DatePipe, DecimalPipe],
  template: `
    <section class="admin-grid admin-grid--split erp-hero-grid">
      <article class="surface-card surface-card--tinted">
        <header class="surface-header">
          <p class="surface-kicker">ERP interno</p>
          <div class="surface-title-row">
            <img src="assets/icons/reports.svg" alt="" width="28" height="28" aria-hidden="true" />
            <h3>Workspaces administrativos</h3>
          </div>
          <p class="surface-copy">
            Espacios de consulta para terceros, cartera, cuentas por pagar,
            contabilidad, fiscalidad e inteligencia. Mantienen la estética actual
            de Pastelería y consumen el núcleo Angular creado en T22.
          </p>
          <div class="chip-row">
            <span class="summary-chip">{{ tabs.length }} dominios ERP</span>
            <span class="summary-chip">Solo lectura prudente</span>
            <span class="summary-chip">UX/UI conservada</span>
          </div>
        </header>
      </article>

      <article class="surface-card">
        <header class="surface-header">
          <p class="surface-kicker">Resumen financiero</p>
          <div class="surface-title-row">
            <img src="assets/icons/dashboard.svg" alt="" width="28" height="28" aria-hidden="true" />
            <h3>Indicadores ERP</h3>
          </div>
        </header>

        <div class="erp-kpi-grid" *ngIf="facade.resumenFinanciero() as resumen; else noDashboard">
          <article>
            <span>Cartera</span>
            <strong>{{ resumen.saldoCartera | currency:'USD':'symbol':'1.2-2' }}</strong>
          </article>
          <article>
            <span>Cuentas por pagar</span>
            <strong>{{ resumen.saldoCuentasPagar | currency:'USD':'symbol':'1.2-2' }}</strong>
          </article>
          <article>
            <span>Caja</span>
            <strong>{{ resumen.saldoCaja | currency:'USD':'symbol':'1.2-2' }}</strong>
          </article>
          <article>
            <span>Asientos</span>
            <strong>{{ resumen.asientosRegistrados }}</strong>
          </article>
        </div>
      </article>
    </section>

    <section class="surface-card erp-workspace-shell">
      <header class="surface-header workspace-header">
        <div>
          <p class="surface-kicker">Dominio activo</p>
          <div class="surface-title-row">
            <img src="assets/icons/guide.svg" alt="" width="28" height="28" aria-hidden="true" />
            <h3>{{ activeTabMeta().label }}</h3>
          </div>
          <p class="surface-copy">{{ activeTabDescription() }}</p>
        </div>
        <button type="button" class="surface-button surface-button--secondary" (click)="refreshActive()">
          Actualizar vista
        </button>
      </header>

      <nav class="workspace-tabs" aria-label="Workspaces ERP">
        <button
          *ngFor="let tab of tabs"
          type="button"
          [class.is-active]="activeTab() === tab.key"
          (click)="selectTab(tab.key)"
        >
          <strong>{{ tab.label }}</strong>
          <span>{{ tab.helper }}</span>
        </button>
      </nav>

      <div class="inline-alert inline-alert--danger" *ngIf="facade.error()">
        {{ facade.error() }}
      </div>
      <div class="inline-alert inline-alert--success" *ngIf="facade.actionMessage()">
        {{ facade.actionMessage() }}
      </div>
      <p class="table-note" *ngIf="facade.loading()">Cargando información del dominio...</p>

      <ng-container [ngSwitch]="activeTab()">
        <section *ngSwitchCase="'inteligencia'" class="workspace-panel">
          <div class="cards-grid cards-grid--actions" *ngIf="facade.erpDashboard() as dashboard">
            <article>
              <h4>Operación activa</h4>
              <p class="card-copy">{{ dashboard.pedidosActivos }} pedidos activos · {{ dashboard.produccionesActivas }} producciones activas.</p>
            </article>
            <article>
              <h4>Documentos abiertos</h4>
              <p class="card-copy">{{ dashboard.documentosCobrarAbiertos }} por cobrar · {{ dashboard.documentosPagarAbiertos }} por pagar.</p>
            </article>
            <article>
              <h4>Fiscalidad interna</h4>
              <p class="card-copy">{{ dashboard.documentosFiscalesBorrador }} documentos fiscales en borrador interno.</p>
            </article>
            <article>
              <h4>Stock bajo</h4>
              <p class="card-copy">{{ dashboard.itemsStockBajo }} ítems requieren revisión de abastecimiento.</p>
            </article>
          </div>

          <div class="table-scroll" *ngIf="facade.stockBajoSemantico().length; else emptyStock">
            <table class="surface-table">
              <thead><tr><th>Ítem</th><th>Tipo</th><th>Stock</th><th>Mínimo</th><th>Unidad</th></tr></thead>
              <tbody>
                <tr *ngFor="let item of facade.stockBajoSemantico()">
                  <td><strong>{{ item.nombre }}</strong><p class="surface-meta">{{ item.codigo }}</p></td>
                  <td>{{ item.tipoItem }}</td>
                  <td>{{ item.stockActual | number:'1.2-2' }}</td>
                  <td>{{ item.stockMinimo | number:'1.2-2' }}</td>
                  <td>{{ item.unidad }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>

        <section *ngSwitchCase="'terceros'" class="workspace-panel">
          <div class="page-toolbar">
            <div class="filter-row">
              <button type="button" class="mini-button" (click)="loadTerceros('')">Todos</button>
              <button type="button" class="mini-button mini-button--ghost" (click)="loadTerceros('CLIENTE')">Clientes</button>
              <button type="button" class="mini-button mini-button--ghost" (click)="loadTerceros('PROVEEDOR')">Proveedores</button>
            </div>
            <p class="pager__meta">{{ facade.terceros().length }} terceros cargados</p>
          </div>
          <div class="table-scroll" *ngIf="facade.terceros().length; else emptyRows">
            <table class="surface-table">
              <thead><tr><th>Nombre</th><th>Perfiles</th><th>Contacto</th><th>Estado</th></tr></thead>
              <tbody>
                <tr *ngFor="let tercero of facade.terceros()">
                  <td><strong>{{ tercero.nombreLegal }}</strong><p class="surface-meta">{{ tercero.nombreComercial || 'Sin nombre comercial' }}</p></td>
                  <td><span class="status-pill" *ngFor="let perfil of tercero.perfiles">{{ perfil }}</span></td>
                  <td>{{ tercero.telefono || tercero.correo || 'Sin contacto' }}</td>
                  <td>{{ tercero.activo ? 'Activo' : 'Inactivo' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>

        <section *ngSwitchCase="'cartera'" class="workspace-panel">
          <div class="table-scroll" *ngIf="facade.documentosCobrar().length; else emptyRows">
            <table class="surface-table">
              <thead><tr><th>Documento</th><th>Cliente</th><th>Estado</th><th>Total</th><th>Saldo</th><th>Emisión</th></tr></thead>
              <tbody>
                <tr *ngFor="let doc of facade.documentosCobrar()">
                  <td><strong>{{ doc.codigo }}</strong><p class="surface-meta">{{ doc.pedidoCodigo || 'Sin pedido vinculado' }}</p></td>
                  <td>{{ doc.clienteNombre }}</td>
                  <td><span class="status-pill">{{ doc.estado }}</span></td>
                  <td>{{ doc.total | currency:'USD':'symbol':'1.2-2' }}</td>
                  <td>{{ doc.saldo | currency:'USD':'symbol':'1.2-2' }}</td>
                  <td>{{ doc.fechaEmision | date:'shortDate' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
          <h4 class="workspace-subtitle">Cobranzas recientes</h4>
          <div class="table-scroll" *ngIf="facade.cobranzas().length; else emptyCollections">
            <table class="surface-table">
              <thead><tr><th>Cobranza</th><th>Cliente</th><th>Monto</th><th>Medio</th><th>Fecha</th></tr></thead>
              <tbody>
                <tr *ngFor="let cobro of facade.cobranzas()">
                  <td><strong>{{ cobro.codigo }}</strong></td>
                  <td>{{ cobro.clienteNombre }}</td>
                  <td>{{ cobro.montoTotal | currency:'USD':'symbol':'1.2-2' }}</td>
                  <td>{{ cobro.medioPago || 'No especificado' }}</td>
                  <td>{{ cobro.fechaCobranza | date:'shortDate' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>

        <section *ngSwitchCase="'cuentasPagar'" class="workspace-panel">
          <div class="table-scroll" *ngIf="facade.documentosPagar().length; else emptyRows">
            <table class="surface-table">
              <thead><tr><th>Documento</th><th>Proveedor</th><th>Estado</th><th>Total</th><th>Saldo</th><th>Emisión</th></tr></thead>
              <tbody>
                <tr *ngFor="let doc of facade.documentosPagar()">
                  <td><strong>{{ doc.codigo }}</strong><p class="surface-meta">Compra #{{ doc.documentoCompraId }}</p></td>
                  <td>{{ doc.proveedorNombre }}</td>
                  <td><span class="status-pill">{{ doc.estado }}</span></td>
                  <td>{{ doc.total | currency:'USD':'symbol':'1.2-2' }}</td>
                  <td>{{ doc.saldo | currency:'USD':'symbol':'1.2-2' }}</td>
                  <td>{{ doc.fechaEmision | date:'shortDate' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
          <h4 class="workspace-subtitle">Pagos proveedor</h4>
          <div class="table-scroll" *ngIf="facade.pagosProveedor().length; else emptyPayments">
            <table class="surface-table">
              <thead><tr><th>Pago</th><th>Proveedor</th><th>Monto</th><th>Medio</th><th>Fecha</th></tr></thead>
              <tbody>
                <tr *ngFor="let pago of facade.pagosProveedor()">
                  <td><strong>{{ pago.codigo }}</strong></td>
                  <td>{{ pago.proveedorNombre }}</td>
                  <td>{{ pago.montoTotal | currency:'USD':'symbol':'1.2-2' }}</td>
                  <td>{{ pago.medioPago || 'No especificado' }}</td>
                  <td>{{ pago.fechaPago | date:'shortDate' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>

        <section *ngSwitchCase="'contabilidad'" class="workspace-panel">
          <div class="cards-grid cards-grid--actions">
            <article><h4>Cuentas</h4><p class="card-copy">{{ facade.cuentasContables().length }} cuentas contables disponibles.</p></article>
            <article><h4>Diarios</h4><p class="card-copy">{{ facade.tiposDiario().length }} diarios contables activos o configurados.</p></article>
            <article><h4>Asientos</h4><p class="card-copy">{{ facade.asientos().length }} asientos registrados para trazabilidad interna.</p></article>
          </div>
          <div class="table-scroll" *ngIf="facade.asientos().length; else emptyRows">
            <table class="surface-table">
              <thead><tr><th>Asiento</th><th>Diario</th><th>Origen</th><th>Debe</th><th>Haber</th><th>Fecha</th></tr></thead>
              <tbody>
                <tr *ngFor="let asiento of facade.asientos()">
                  <td><strong>{{ asiento.codigo }}</strong><p class="surface-meta">{{ asiento.descripcion }}</p></td>
                  <td>{{ asiento.tipoDiarioCodigo }}</td>
                  <td>{{ asiento.origenTipo || 'Manual' }} {{ asiento.origenId || '' }}</td>
                  <td>{{ asiento.totalDebe | currency:'USD':'symbol':'1.2-2' }}</td>
                  <td>{{ asiento.totalHaber | currency:'USD':'symbol':'1.2-2' }}</td>
                  <td>{{ asiento.fechaAsiento | date:'shortDate' }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>

        <section *ngSwitchCase="'fiscalidad'" class="workspace-panel">
          <p class="table-note">Fiscalidad interna preparada. Esta vista no afirma autorización SRI ni emisión tributaria real.</p>
          <div class="table-scroll" *ngIf="facade.documentosFiscales().length; else emptyRows">
            <table class="surface-table">
              <thead><tr><th>Documento</th><th>Tipo</th><th>Estado</th><th>Tercero</th><th>Total</th><th>Número</th></tr></thead>
              <tbody>
                <tr *ngFor="let doc of facade.documentosFiscales()">
                  <td><strong>{{ doc.codigo }}</strong><p class="surface-meta">{{ doc.origenTipo }}</p></td>
                  <td>{{ doc.tipoComprobante }}</td>
                  <td><span class="status-pill">{{ doc.estado }}</span></td>
                  <td>{{ doc.terceroNombre }}</td>
                  <td>{{ doc.total | currency:'USD':'symbol':'1.2-2' }}</td>
                  <td>{{ doc.numeroComprobante }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>

        <section *ngSwitchCase="'bridges'" class="workspace-panel">
          <div class="cards-grid cards-grid--actions">
            <article>
              <h4>Pedido → documento por cobrar</h4>
              <p class="card-copy">Disponible desde backend por endpoint bridge. La ejecución se hará con controles operativos en tandas posteriores.</p>
            </article>
            <article>
              <h4>Cobranza → asiento</h4>
              <p class="card-copy">Generación idempotente preparada para conectar caja, cartera y contabilidad.</p>
            </article>
            <article>
              <h4>Compra/pago → asiento</h4>
              <p class="card-copy">Puentes separados para no mezclar compras, cuentas por pagar y contabilidad.</p>
            </article>
          </div>
          <div class="bridge-result" *ngIf="facade.lastBridgeResult() as result; else noBridgeResult">
            <h4>Última operación bridge</h4>
            <p>{{ result.mensaje }}</p>
            <small>{{ result.origenTipo }} {{ result.origenId }} → {{ result.destinoTipo }} {{ result.destinoCodigo }}</small>
          </div>
        </section>
      </ng-container>
    </section>

    <ng-template #noDashboard>
      <p class="table-note">Aún no hay dashboard ERP cargado. Usa Actualizar vista.</p>
    </ng-template>
    <ng-template #emptyRows>
      <p class="empty-state">No hay registros para esta vista todavía.</p>
    </ng-template>
    <ng-template #emptyStock>
      <p class="empty-state">No hay alertas de stock bajo en la vista actual.</p>
    </ng-template>
    <ng-template #emptyCollections>
      <p class="empty-state">No hay cobranzas registradas para mostrar.</p>
    </ng-template>
    <ng-template #emptyPayments>
      <p class="empty-state">No hay pagos proveedor registrados para mostrar.</p>
    </ng-template>
    <ng-template #noBridgeResult>
      <p class="empty-state">Aún no se ha ejecutado una operación bridge desde esta sesión.</p>
    </ng-template>
  `,
  styles: [
    ADMIN_SURFACE_STYLES,
    `
      .erp-hero-grid { align-items: stretch; }
      .erp-kpi-grid {
        display: grid;
        grid-template-columns: repeat(2, minmax(0, 1fr));
        gap: 0.75rem;
      }
      .erp-kpi-grid article {
        border: 1px solid rgba(138, 63, 44, 0.12);
        background: rgba(255, 248, 240, 0.72);
        padding: 0.9rem;
      }
      .erp-kpi-grid span,
      .surface-meta {
        color: #8f776c;
        font-size: 0.82rem;
      }
      .erp-kpi-grid strong { display: block; margin-top: 0.25rem; color: #4f2d22; }
      .erp-workspace-shell { margin-top: 1rem; }
      .workspace-header {
        display: flex;
        justify-content: space-between;
        gap: 1rem;
        align-items: flex-start;
      }
      .workspace-tabs {
        display: flex;
        gap: 0.5rem;
        overflow-x: auto;
        padding: 0.5rem 0 1rem;
        margin-bottom: 1rem;
        border-bottom: 1px solid rgba(138, 63, 44, 0.12);
      }
      .workspace-tabs button {
        min-width: 9rem;
        border: 1px solid rgba(138, 63, 44, 0.16);
        background: #fffaf5;
        color: #4f2d22;
        padding: 0.75rem;
        text-align: left;
        cursor: pointer;
      }
      .workspace-tabs button.is-active {
        background: #8a3f2c;
        color: #fffaf5;
      }
      .workspace-tabs span { display: block; font-size: 0.76rem; opacity: 0.78; }
      .workspace-panel { display: grid; gap: 1rem; }
      .workspace-subtitle { margin: 0.5rem 0 0; color: #4f2d22; }
      .table-scroll { overflow-x: auto; }
      .surface-table td, .surface-table th { white-space: nowrap; }
      .filter-row { display: flex; gap: 0.5rem; flex-wrap: wrap; }
      .inline-alert {
        padding: 0.75rem 0.9rem;
        margin: 0.75rem 0;
        border: 1px solid rgba(138, 63, 44, 0.14);
        background: #fff8f0;
      }
      .inline-alert--danger { color: #8a2d2d; }
      .inline-alert--success { color: #426b34; }
      .empty-state {
        padding: 1rem;
        background: rgba(255, 248, 240, 0.72);
        border: 1px dashed rgba(138, 63, 44, 0.22);
        color: #8f776c;
      }
      .bridge-result {
        border: 1px solid rgba(138, 63, 44, 0.12);
        background: #fffaf5;
        padding: 1rem;
      }
      @media (max-width: 900px) {
        .workspace-header { flex-direction: column; }
        .erp-kpi-grid { grid-template-columns: 1fr; }
      }
    `,
  ],
})
export class ErpWorkspacesPageComponent implements OnInit {
  readonly facade = inject(ErpCoreFacade);
  readonly tabs = WORKSPACE_TABS;
  readonly activeTab = signal<WorkspaceKey>("inteligencia");

  readonly activeTabMeta = computed(() => {
    return this.tabs.find((tab) => tab.key === this.activeTab()) ?? this.tabs[0];
  });

  ngOnInit() {
    this.facade.loadInteligencia(20);
  }

  selectTab(tab: WorkspaceKey) {
    this.activeTab.set(tab);
    this.refreshActive();
  }

  refreshActive() {
    switch (this.activeTab()) {
      case "inteligencia":
        this.facade.loadInteligencia(20);
        break;
      case "terceros":
        this.facade.loadTerceros();
        break;
      case "cartera":
        this.facade.loadCartera();
        break;
      case "cuentasPagar":
        this.facade.loadCuentasPagar();
        break;
      case "contabilidad":
        this.facade.loadContabilidad();
        break;
      case "fiscalidad":
        this.facade.loadFiscalidad();
        break;
      case "bridges":
        this.facade.loadInteligencia(10);
        break;
    }
  }

  loadTerceros(perfil: "" | "CLIENTE" | "PROVEEDOR") {
    this.facade.loadTerceros(perfil);
  }

  activeTabDescription() {
    switch (this.activeTab()) {
      case "inteligencia":
        return "Indicadores consolidados de operación, saldos, fiscalidad interna y stock bajo.";
      case "terceros":
        return "Consulta de la base común de clientes, proveedores y otros perfiles del negocio.";
      case "cartera":
        return "Documentos por cobrar y cobranzas registradas sin mezclar aún con pantallas operativas.";
      case "cuentasPagar":
        return "Documentos por pagar y pagos proveedor como capa financiera separada.";
      case "contabilidad":
        return "Plan de cuentas, diarios y asientos internos con partida doble.";
      case "fiscalidad":
        return "Documentos fiscales internos preparados, sin afirmar autorización SRI.";
      case "bridges":
        return "Puentes ERP idempotentes preparados para conectar operación y consecuencias financieras.";
    }
  }
}
