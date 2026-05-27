import { CommonModule, DatePipe, DecimalPipe } from "@angular/common";
import { Component, OnInit, computed, inject, signal } from "@angular/core";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";
import { ActivatedRoute, RouterLink, RouterLinkActive } from "@angular/router";
import { ADMIN_SURFACE_STYLES } from "../../../shared/ui/admin-surface.styles";
import { ErpCoreFacade } from "../state/erp-core.facade";

type ErpWorkspaceDomain =
  | "terceros"
  | "cartera"
  | "cuentas-pagar"
  | "contabilidad"
  | "fiscalidad"
  | "inteligencia"
  | "bridges";

interface WorkspaceTab {
  readonly path: string;
  readonly label: string;
  readonly description: string;
  readonly domain: ErpWorkspaceDomain;
}

const WORKSPACE_TABS: readonly WorkspaceTab[] = [
  {
    path: "/terceros",
    label: "Terceros",
    description: "Clientes, proveedores y perfiles comunes.",
    domain: "terceros",
  },
  {
    path: "/cartera",
    label: "Cartera",
    description: "Documentos por cobrar y cobranzas.",
    domain: "cartera",
  },
  {
    path: "/cuentas-pagar",
    label: "Cuentas por pagar",
    description: "Documentos y pagos a proveedores.",
    domain: "cuentas-pagar",
  },
  {
    path: "/contabilidad",
    label: "Contabilidad",
    description: "Plan de cuentas, diarios y asientos.",
    domain: "contabilidad",
  },
  {
    path: "/fiscalidad",
    label: "Fiscalidad",
    description: "Documentos fiscales internos.",
    domain: "fiscalidad",
  },
  {
    path: "/inteligencia",
    label: "Inteligencia",
    description: "Indicadores y vistas semánticas ERP.",
    domain: "inteligencia",
  },
  {
    path: "/erp-bridges",
    label: "Bridges ERP",
    description: "Acciones idempotentes operación → ERP.",
    domain: "bridges",
  },
];

@Component({
  selector: "app-erp-workspace-page",
  standalone: true,
  imports: [CommonModule, DatePipe, DecimalPipe, RouterLink, RouterLinkActive],
  template: `
    <div class="admin-grid erp-workspace-shell">
      <section class="surface-card surface-card--tinted workspace-hero">
        <header class="surface-header">
          <p class="surface-kicker">ERP administrativo</p>
          <div class="surface-title-row">
            <img
              src="assets/icons/abastecimiento/bar-chart-3.svg"
              alt=""
              width="28"
              height="28"
              aria-hidden="true"
            />
            <h3>{{ workspaceTitle() }}</h3>
          </div>
          <p class="surface-copy">{{ workspaceCopy() }}</p>
        </header>

        <nav class="workspace-tabs" aria-label="Módulos ERP">
          <a
            *ngFor="let tab of workspaceTabs"
            [routerLink]="tab.path"
            routerLinkActive="is-active"
            [routerLinkActiveOptions]="{ exact: true }"
          >
            <strong>{{ tab.label }}</strong>
            <span>{{ tab.description }}</span>
          </a>
        </nav>
      </section>

      <section class="status-banner" *ngIf="facade.loading()">
        Cargando información del módulo ERP...
      </section>
      <section class="status-banner is-error" *ngIf="facade.error()">
        {{ facade.error() }}
      </section>
      <section class="status-banner is-success" *ngIf="facade.actionMessage()">
        {{ facade.actionMessage() }}
      </section>

      <ng-container [ngSwitch]="domain()">
        <section *ngSwitchCase="'terceros'" class="surface-card">
          <header class="surface-header">
            <p class="surface-kicker">Base común</p>
            <div class="surface-title-row">
              <img src="assets/icons/clients.svg" alt="" aria-hidden="true" />
              <h3>Terceros unificados</h3>
            </div>
            <p class="table-note">
              Vista de consulta: los registros base se gestionan desde Clientes y Proveedores para no duplicar pantallas operativas.
            </p>
          </header>

          <div class="page-toolbar">
            <p class="pager__meta">{{ facade.terceros().length }} terceros cargados</p>
            <div class="action-row">
              <button type="button" class="mini-button" (click)="loadTerceros('')">Todos</button>
              <button type="button" class="mini-button" (click)="loadTerceros('CLIENTE')">Clientes</button>
              <button type="button" class="mini-button" (click)="loadTerceros('PROVEEDOR')">Proveedores</button>
            </div>
          </div>

          <div class="table-shell" *ngIf="facade.terceros().length; else emptyState">
            <table class="surface-table surface-table--wide">
              <thead>
                <tr>
                  <th>Tercero</th>
                  <th>Contacto</th>
                  <th>Perfiles</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let tercero of facade.terceros()">
                  <td>
                    <strong>{{ tercero.nombreLegal }}</strong>
                    <p class="surface-meta">{{ tercero.nombreComercial || "Sin nombre comercial" }}</p>
                  </td>
                  <td>
                    <strong>{{ tercero.telefono || "Sin teléfono" }}</strong>
                    <p class="surface-meta">{{ tercero.correo || "Sin correo" }}</p>
                  </td>
                  <td>
                    <div class="chip-row">
                      <span class="status-pill" *ngFor="let perfil of tercero.perfiles">{{ perfil }}</span>
                    </div>
                  </td>
                  <td>
                    <span class="status-pill" [class.status-pill--success]="tercero.activo">
                      {{ tercero.activo ? "Activo" : "Inactivo" }}
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>

        <section *ngSwitchCase="'cartera'" class="admin-grid admin-grid--split">
          <article class="surface-card">
            <header class="surface-header">
              <p class="surface-kicker">Cuentas por cobrar</p>
              <div class="surface-title-row">
                <img src="assets/icons/abastecimiento/dollar-sign.svg" alt="" aria-hidden="true" />
                <h3>Documentos por cobrar</h3>
              </div>
            </header>
            <div class="usecase-panel">
              <p class="surface-kicker">Caso de uso</p>
              <h4>Registrar documento por cobrar</h4>
              <div class="usecase-grid">
                <label>Pedido ID opcional<input type="number" min="1" (input)="docCobrarPedidoId.set($any($event.target).value)" /></label>
                <label>Cliente ID opcional<input type="number" min="1" (input)="docCobrarClienteId.set($any($event.target).value)" /></label>
                <label>Código opcional<input type="text" (input)="docCobrarCodigo.set($any($event.target).value)" /></label>
                <label>Total<input type="number" min="0.01" step="0.01" (input)="docCobrarTotal.set($any($event.target).value)" /></label>
              </div>
              <label class="usecase-wide">Observaciones<input type="text" (input)="docCobrarObservaciones.set($any($event.target).value)" /></label>
              <button type="button" class="surface-button" (click)="crearDocumentoCobrarManual()">Registrar documento por cobrar</button>
            </div>
            <table class="surface-table" *ngIf="facade.documentosCobrar().length; else emptyState">
              <thead>
                <tr><th>Documento</th><th>Cliente</th><th>Estado</th><th>Saldo</th></tr>
              </thead>
              <tbody>
                <tr *ngFor="let documento of facade.documentosCobrar()">
                  <td><strong>{{ documento.codigo }}</strong><p class="surface-meta">{{ documento.fechaEmision | date:"mediumDate" }}</p></td>
                  <td>{{ documento.clienteNombre }}</td>
                  <td><span class="status-pill">{{ documento.estado }}</span></td>
                  <td><strong>{{ documento.saldo | number:"1.2-2" }}</strong><p class="surface-meta">Total {{ documento.total | number:"1.2-2" }}</p></td>
                </tr>
              </tbody>
            </table>
          </article>

          <article class="surface-card surface-card--tinted">
            <header class="surface-header">
              <p class="surface-kicker">Cobros</p>
              <div class="surface-title-row">
                <img src="assets/icons/abastecimiento/receipt.svg" alt="" aria-hidden="true" />
                <h3>Cobranzas registradas</h3>
              </div>
            </header>
            <div class="usecase-panel">
              <p class="surface-kicker">Caso de uso</p>
              <h4>Registrar cobranza aplicada</h4>
              <div class="usecase-grid">
                <label>Documento por cobrar ID<input type="number" min="1" (input)="cobranzaDocumentoId.set($any($event.target).value)" /></label>
                <label>Cliente ID opcional<input type="number" min="1" (input)="cobranzaClienteId.set($any($event.target).value)" /></label>
                <label>Monto<input type="number" min="0.01" step="0.01" (input)="cobranzaMonto.set($any($event.target).value)" /></label>
                <label>Medio de pago<input type="text" (input)="cobranzaMedio.set($any($event.target).value)" /></label>
              </div>
              <label class="usecase-wide">Referencia<input type="text" (input)="cobranzaReferencia.set($any($event.target).value)" /></label>
              <button type="button" class="surface-button" (click)="registrarCobranzaManual()">Registrar cobranza</button>
            </div>
            <table class="surface-table" *ngIf="facade.cobranzas().length; else emptyState">
              <thead>
                <tr><th>Cobranza</th><th>Cliente</th><th>Estado</th><th>Monto</th></tr>
              </thead>
              <tbody>
                <tr *ngFor="let cobranza of facade.cobranzas()">
                  <td><strong>{{ cobranza.codigo }}</strong><p class="surface-meta">{{ cobranza.fechaCobranza | date:"mediumDate" }}</p></td>
                  <td>{{ cobranza.clienteNombre }}</td>
                  <td><span class="status-pill">{{ cobranza.estado }}</span></td>
                  <td>{{ cobranza.montoTotal | number:"1.2-2" }}</td>
                </tr>
              </tbody>
            </table>
          </article>
        </section>

        <section *ngSwitchCase="'cuentas-pagar'" class="admin-grid admin-grid--split">
          <article class="surface-card">
            <header class="surface-header">
              <p class="surface-kicker">Proveedores</p>
              <div class="surface-title-row">
                <img src="assets/icons/abastecimiento/truck.svg" alt="" aria-hidden="true" />
                <h3>Documentos por pagar</h3>
              </div>
            </header>
            <table class="surface-table" *ngIf="facade.documentosPagar().length; else emptyState">
              <thead>
                <tr><th>Documento</th><th>Proveedor</th><th>Estado</th><th>Saldo</th></tr>
              </thead>
              <tbody>
                <tr *ngFor="let documento of facade.documentosPagar()">
                  <td><strong>{{ documento.codigo }}</strong><p class="surface-meta">{{ documento.fechaEmision | date:"mediumDate" }}</p></td>
                  <td>{{ documento.proveedorNombre }}</td>
                  <td><span class="status-pill">{{ documento.estado }}</span></td>
                  <td><strong>{{ documento.saldo | number:"1.2-2" }}</strong><p class="surface-meta">Total {{ documento.total | number:"1.2-2" }}</p></td>
                </tr>
              </tbody>
            </table>
          </article>

          <article class="surface-card surface-card--tinted">
            <header class="surface-header">
              <p class="surface-kicker">Pagos</p>
              <div class="surface-title-row">
                <img src="assets/icons/abastecimiento/check-circle.svg" alt="" aria-hidden="true" />
                <h3>Pagos proveedor</h3>
              </div>
            </header>
            <div class="usecase-panel">
              <p class="surface-kicker">Caso de uso</p>
              <h4>Registrar pago proveedor</h4>
              <div class="usecase-grid">
                <label>Documento por pagar ID<input type="number" min="1" (input)="pagoDocumentoId.set($any($event.target).value)" /></label>
                <label>Proveedor ID opcional<input type="number" min="1" (input)="pagoProveedorId.set($any($event.target).value)" /></label>
                <label>Monto<input type="number" min="0.01" step="0.01" (input)="pagoMonto.set($any($event.target).value)" /></label>
                <label>Medio de pago<input type="text" (input)="pagoMedio.set($any($event.target).value)" /></label>
              </div>
              <label class="usecase-wide">Referencia<input type="text" (input)="pagoReferencia.set($any($event.target).value)" /></label>
              <button type="button" class="surface-button" (click)="registrarPagoProveedorManual()">Registrar pago proveedor</button>
            </div>
            <table class="surface-table" *ngIf="facade.pagosProveedor().length; else emptyState">
              <thead>
                <tr><th>Pago</th><th>Proveedor</th><th>Estado</th><th>Monto</th></tr>
              </thead>
              <tbody>
                <tr *ngFor="let pago of facade.pagosProveedor()">
                  <td><strong>{{ pago.codigo }}</strong><p class="surface-meta">{{ pago.fechaPago | date:"mediumDate" }}</p></td>
                  <td>{{ pago.proveedorNombre }}</td>
                  <td><span class="status-pill">{{ pago.estado }}</span></td>
                  <td>{{ pago.montoTotal | number:"1.2-2" }}</td>
                </tr>
              </tbody>
            </table>
          </article>
        </section>

        <section *ngSwitchCase="'contabilidad'" class="admin-grid admin-grid--split">
          <article class="surface-card">
            <header class="surface-header">
              <p class="surface-kicker">Plan contable</p>
              <div class="surface-title-row">
                <img src="assets/icons/abastecimiento/file-spreadsheet.svg" alt="" aria-hidden="true" />
                <h3>Cuentas y diarios</h3>
              </div>
            </header>
            <div class="cards-grid cards-grid--compact">
              <article *ngFor="let diario of facade.tiposDiario()">
                <strong>{{ diario.codigo }}</strong>
                <p class="card-copy">{{ diario.nombre }}</p>
              </article>
            </div>
            <table class="surface-table" *ngIf="facade.cuentasContables().length; else emptyState">
              <thead><tr><th>Cuenta</th><th>Tipo</th><th>Naturaleza</th><th>Uso</th></tr></thead>
              <tbody>
                <tr *ngFor="let cuenta of facade.cuentasContables()">
                  <td><strong>{{ cuenta.codigo }}</strong><p class="surface-meta">{{ cuenta.nombre }}</p></td>
                  <td>{{ cuenta.tipoCuenta }}</td>
                  <td>{{ cuenta.naturaleza }}</td>
                  <td>{{ cuenta.imputable ? "Imputable" : "Agrupadora" }}</td>
                </tr>
              </tbody>
            </table>
          </article>

          <article class="surface-card surface-card--tinted">
            <header class="surface-header">
              <p class="surface-kicker">Partida doble</p>
              <div class="surface-title-row">
                <img src="assets/icons/abastecimiento/scale.svg" alt="" aria-hidden="true" />
                <h3>Asientos registrados</h3>
              </div>
            </header>
            <div class="usecase-panel">
              <p class="surface-kicker">Caso de uso</p>
              <h4>Registrar asiento simple</h4>
              <div class="usecase-grid">
                <label>Diario<input type="text" value="GENERAL" (input)="asientoDiario.set($any($event.target).value)" /></label>
                <label>Cuenta debe<input type="text" placeholder="110101" (input)="asientoCuentaDebe.set($any($event.target).value)" /></label>
                <label>Cuenta haber<input type="text" placeholder="410101" (input)="asientoCuentaHaber.set($any($event.target).value)" /></label>
                <label>Monto<input type="number" min="0.01" step="0.01" (input)="asientoMonto.set($any($event.target).value)" /></label>
              </div>
              <label class="usecase-wide">Descripción<input type="text" (input)="asientoDescripcion.set($any($event.target).value)" /></label>
              <button type="button" class="surface-button" (click)="registrarAsientoSimple()">Registrar asiento</button>
            </div>
            <table class="surface-table" *ngIf="facade.asientos().length; else emptyState">
              <thead><tr><th>Asiento</th><th>Origen</th><th>Debe</th><th>Haber</th></tr></thead>
              <tbody>
                <tr *ngFor="let asiento of facade.asientos()">
                  <td><strong>{{ asiento.codigo }}</strong><p class="surface-meta">{{ asiento.fechaAsiento | date:"mediumDate" }}</p></td>
                  <td>{{ asiento.origenTipo || "Manual" }}</td>
                  <td>{{ asiento.totalDebe | number:"1.2-2" }}</td>
                  <td>{{ asiento.totalHaber | number:"1.2-2" }}</td>
                </tr>
              </tbody>
            </table>
          </article>
        </section>

        <section *ngSwitchCase="'fiscalidad'" class="surface-card">
          <header class="surface-header">
            <p class="surface-kicker">Fiscalidad prudente</p>
            <div class="surface-title-row">
              <img src="assets/icons/abastecimiento/receipt.svg" alt="" aria-hidden="true" />
              <h3>Documentos fiscales internos</h3>
            </div>
            <p class="table-note">Esta pantalla no declara autorización SRI; solo muestra trazabilidad fiscal interna preparada.</p>
          </header>
          <div class="usecase-panel">
            <p class="surface-kicker">Caso de uso</p>
            <h4>Preparar / emitir documento fiscal interno</h4>
            <div class="usecase-grid">
              <label>Doc. cobrar ID<input type="number" min="1" (input)="fiscalDocumentoCobrarId.set($any($event.target).value)" /></label>
              <label>Doc. compra ID<input type="number" min="1" (input)="fiscalDocumentoCompraId.set($any($event.target).value)" /></label>
              <label>Tipo<select (change)="fiscalTipo.set($any($event.target).value)"><option value="FACTURA">FACTURA</option><option value="NOTA_VENTA">NOTA_VENTA</option><option value="COMPROBANTE_COMPRA_INTERNO">COMPRA INTERNA</option></select></label>
              <label>Secuencial<input type="text" placeholder="1" (input)="fiscalSecuencial.set($any($event.target).value)" /></label>
            </div>
            <button type="button" class="surface-button" (click)="prepararDocumentoFiscalManual()">Preparar documento fiscal</button>
            <div class="usecase-grid usecase-grid--inline">
              <label>Documento fiscal ID<input type="number" min="1" (input)="fiscalAccionId.set($any($event.target).value)" /></label>
              <label>Motivo anulación<input type="text" (input)="fiscalMotivoAnulacion.set($any($event.target).value)" /></label>
            </div>
            <div class="action-row">
              <button type="button" class="mini-button" (click)="emitirDocumentoFiscalManual()">Emitir interno</button>
              <button type="button" class="mini-button" (click)="anularDocumentoFiscalManual()">Anular</button>
            </div>
          </div>
          <table class="surface-table surface-table--wide" *ngIf="facade.documentosFiscales().length; else emptyState">
            <thead><tr><th>Documento</th><th>Tercero</th><th>Origen</th><th>Estado</th><th>Total</th></tr></thead>
            <tbody>
              <tr *ngFor="let documento of facade.documentosFiscales()">
                <td><strong>{{ documento.numeroComprobante }}</strong><p class="surface-meta">{{ documento.tipoComprobante }}</p></td>
                <td>{{ documento.terceroNombre }}</td>
                <td>{{ documento.origenTipo }}</td>
                <td><span class="status-pill">{{ documento.estado }}</span></td>
                <td>{{ documento.total | number:"1.2-2" }}</td>
              </tr>
            </tbody>
          </table>
        </section>

        <section *ngSwitchCase="'inteligencia'" class="admin-grid">
          <div class="metric-grid" *ngIf="facade.erpDashboard() as dashboard">
            <article class="surface-card metric-card">
              <span>Cartera</span>
              <strong>{{ dashboard.saldoCartera | number:"1.2-2" }}</strong>
              <p>{{ dashboard.documentosCobrarAbiertos }} documentos abiertos</p>
            </article>
            <article class="surface-card metric-card">
              <span>Cuentas por pagar</span>
              <strong>{{ dashboard.saldoCuentasPagar | number:"1.2-2" }}</strong>
              <p>{{ dashboard.documentosPagarAbiertos }} documentos abiertos</p>
            </article>
            <article class="surface-card metric-card">
              <span>Caja</span>
              <strong>{{ dashboard.saldoCaja | number:"1.2-2" }}</strong>
              <p>Saldo operativo semántico</p>
            </article>
            <article class="surface-card metric-card">
              <span>Alertas</span>
              <strong>{{ dashboard.itemsStockBajo }}</strong>
              <p>Ítems con stock bajo</p>
            </article>
          </div>

          <section class="surface-card">
            <header class="surface-header">
              <p class="surface-kicker">Lectura gerencial</p>
              <div class="surface-title-row">
                <img src="assets/icons/reports.svg" alt="" aria-hidden="true" />
                <h3>Últimos documentos por cobrar</h3>
              </div>
            </header>
            <table class="surface-table" *ngIf="facade.carteraSemantica().length; else emptyState">
              <thead><tr><th>Documento</th><th>Cliente</th><th>Estado</th><th>Saldo</th></tr></thead>
              <tbody>
                <tr *ngFor="let row of facade.carteraSemantica()">
                  <td>{{ row.codigo }}</td>
                  <td>{{ row.clienteNombre }}</td>
                  <td><span class="status-pill">{{ row.estado }}</span></td>
                  <td>{{ row.saldo | number:"1.2-2" }}</td>
                </tr>
              </tbody>
            </table>
          </section>
        </section>

        <section *ngSwitchCase="'bridges'" class="surface-card surface-card--tinted">
          <header class="surface-header">
            <p class="surface-kicker">Operación → ERP</p>
            <div class="surface-title-row">
              <img src="assets/icons/abastecimiento/arrow-down-up.svg" alt="" aria-hidden="true" />
              <h3>Bridges manuales e idempotentes</h3>
            </div>
            <p class="surface-copy">Acciones controladas para generar consecuencias ERP sin duplicar documentos ni asientos.</p>
          </header>

          <div class="bridge-grid">
            <label>
              Pedido ID
              <input type="number" min="1" (input)="bridgePedidoId.set(readNumber($any($event.target).value))" />
              <button type="button" class="surface-button" [disabled]="bridgePedidoId() === null" (click)="bridgePedido()">
                Crear documento por cobrar
              </button>
            </label>
            <label>
              Documento por cobrar ID
              <input type="number" min="1" (input)="bridgeDocumentoCobrarId.set(readNumber($any($event.target).value))" />
              <button type="button" class="surface-button" [disabled]="bridgeDocumentoCobrarId() === null" (click)="bridgeAsientoVenta()">
                Crear asiento de venta
              </button>
            </label>
            <label>
              Cobranza ID
              <input type="number" min="1" (input)="bridgeCobranzaId.set(readNumber($any($event.target).value))" />
              <button type="button" class="surface-button" [disabled]="bridgeCobranzaId() === null" (click)="bridgeAsientoCobro()">
                Crear asiento de cobro
              </button>
            </label>
            <label>
              Documento por pagar ID
              <input type="number" min="1" (input)="bridgeDocumentoPagarId.set(readNumber($any($event.target).value))" />
              <button type="button" class="surface-button" [disabled]="bridgeDocumentoPagarId() === null" (click)="bridgeAsientoCompra()">
                Crear asiento de compra
              </button>
            </label>
            <label>
              Pago proveedor ID
              <input type="number" min="1" (input)="bridgePagoProveedorId.set(readNumber($any($event.target).value))" />
              <button type="button" class="surface-button" [disabled]="bridgePagoProveedorId() === null" (click)="bridgeAsientoPago()">
                Crear asiento de pago
              </button>
            </label>
          </div>

          <article class="bridge-result" *ngIf="facade.lastBridgeResult() as result">
            <strong>{{ result.destinoCodigo }}</strong>
            <p>{{ result.mensaje }}</p>
            <span class="status-pill">{{ result.creado ? "Creado" : "Existente" }}</span>
          </article>
        </section>
      </ng-container>

      <ng-template #emptyState>
        <div class="empty-state">No hay información para mostrar en este módulo todavía.</div>
      </ng-template>
    </div>
  `,
  styles: [
    ADMIN_SURFACE_STYLES,
    `
      .erp-workspace-shell {
        align-items: start;
      }

      .workspace-hero {
        display: grid;
        gap: 1rem;
      }

      .workspace-tabs {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(13.5rem, 1fr));
        gap: 0.7rem;
      }

      .workspace-tabs a {
        display: grid;
        gap: 0.35rem;
        padding: 0.9rem;
        text-decoration: none;
        background: rgba(255, 255, 255, 0.72);
        border: 1px solid #eaded4;
        color: #5a392c;
      }

      .workspace-tabs a.is-active,
      .workspace-tabs a:hover {
        border-color: #6c3a27;
        background: #fffaf6;
      }

      .workspace-tabs span {
        color: #7b6b62;
        line-height: 1.45;
        font-size: 0.86rem;
      }

      .table-shell {
        overflow-x: auto;
      }

      .surface-table--wide {
        min-width: 54rem;
      }

      .cards-grid--compact {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(10rem, 1fr));
        gap: 0.7rem;
        margin-bottom: 1rem;
      }

      .cards-grid--compact article,
      .bridge-result {
        padding: 0.9rem;
        border: 1px solid #efdfd3;
        background: rgba(255, 251, 247, 0.92);
      }

      .metric-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(13rem, 1fr));
        gap: 1rem;
      }

      .metric-card {
        display: grid;
        gap: 0.4rem;
      }

      .metric-card span {
        color: #8a5c46;
        text-transform: uppercase;
        letter-spacing: 0.12em;
        font-size: 0.74rem;
      }

      .metric-card strong {
        font: 700 1.65rem/1.1 var(--font-display, "Cormorant Garamond", Georgia, serif);
        color: #2d201a;
      }

      .metric-card p,
      .bridge-result p {
        margin: 0;
        color: #74645b;
      }

      .bridge-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(16rem, 1fr));
        gap: 1rem;
      }

      .usecase-panel {
        display: grid;
        gap: 0.75rem;
        margin: 1rem 0;
        padding: 1rem;
        border: 1px dashed #cfa98f;
        background: rgba(255, 248, 242, 0.85);
      }

      .usecase-panel h4 {
        margin: 0;
        color: #2d201a;
      }

      .usecase-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(10rem, 1fr));
        gap: 0.75rem;
      }

      .usecase-grid--inline {
        margin-top: 0.4rem;
      }

      .usecase-panel label,
      .usecase-wide {
        display: grid;
        gap: 0.35rem;
        color: #5a473f;
        font-weight: 700;
      }

      .usecase-panel input,
      .usecase-panel select {
        border: 1px solid #dccdc2;
        padding: 0.65rem 0.75rem;
        background: #fffdfb;
        color: #332821;
      }

      .bridge-grid label {
        display: grid;
        gap: 0.65rem;
        padding: 1rem;
        border: 1px solid #eaded4;
        background: #fffdfb;
        color: #5a473f;
        font-weight: 700;
      }

      .bridge-grid input {
        border: 1px solid #dccdc2;
        padding: 0.8rem 0.9rem;
        background: #fffaf6;
        color: #332821;
      }

      .bridge-result {
        margin-top: 1rem;
        display: grid;
        gap: 0.4rem;
      }
    `,
  ],
})
export class ErpWorkspacePageComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  readonly facade = inject(ErpCoreFacade);

  readonly workspaceTabs = WORKSPACE_TABS;
  readonly domain = signal<ErpWorkspaceDomain>("inteligencia");

  readonly bridgePedidoId = signal<number | null>(null);
  readonly bridgeDocumentoCobrarId = signal<number | null>(null);
  readonly bridgeCobranzaId = signal<number | null>(null);
  readonly bridgeDocumentoPagarId = signal<number | null>(null);
  readonly bridgePagoProveedorId = signal<number | null>(null);
  readonly docCobrarPedidoId = signal("");
  readonly docCobrarClienteId = signal("");
  readonly docCobrarCodigo = signal("");
  readonly docCobrarTotal = signal("");
  readonly docCobrarObservaciones = signal("");

  readonly cobranzaDocumentoId = signal("");
  readonly cobranzaClienteId = signal("");
  readonly cobranzaMonto = signal("");
  readonly cobranzaMedio = signal("EFECTIVO");
  readonly cobranzaReferencia = signal("");

  readonly pagoDocumentoId = signal("");
  readonly pagoProveedorId = signal("");
  readonly pagoMonto = signal("");
  readonly pagoMedio = signal("EFECTIVO");
  readonly pagoReferencia = signal("");

  readonly asientoDiario = signal("GENERAL");
  readonly asientoCuentaDebe = signal("");
  readonly asientoCuentaHaber = signal("");
  readonly asientoMonto = signal("");
  readonly asientoDescripcion = signal("Asiento manual ERP");

  readonly fiscalDocumentoCobrarId = signal("");
  readonly fiscalDocumentoCompraId = signal("");
  readonly fiscalTipo = signal("FACTURA");
  readonly fiscalSecuencial = signal("");
  readonly fiscalAccionId = signal("");
  readonly fiscalMotivoAnulacion = signal("Anulación administrativa interna");

  readonly workspaceTitle = computed(() => {
    const current = this.currentTab();
    return current?.label ?? "ERP administrativo";
  });

  readonly workspaceCopy = computed(() => {
    const current = this.currentTab();
    return current?.description ?? "Lectura transversal de los módulos ERP preparados para la pastelería.";
  });

  private readonly currentTab = computed(() =>
    this.workspaceTabs.find((tab) => tab.domain === this.domain()),
  );

  ngOnInit() {
    this.route.data.pipe(takeUntilDestroyed()).subscribe((data) => {
      const domain = data["domain"] as ErpWorkspaceDomain | undefined;
      this.domain.set(domain ?? "inteligencia");
      this.loadCurrentWorkspace();
    });
  }

  loadTerceros(perfil: "" | "CLIENTE" | "PROVEEDOR" | "EMPLEADO") {
    this.facade.loadTerceros(perfil);
  }

  readNumber(value: string): number | null {
    const parsed = Number(value);
    return Number.isFinite(parsed) && parsed > 0 ? parsed : null;
  }


  private optionalNumber(value: string): number | null {
    const parsed = Number(value);
    return Number.isFinite(parsed) && parsed > 0 ? parsed : null;
  }

  private requiredNumber(value: string, label: string): number | null {
    const parsed = this.optionalNumber(value);
    if (parsed === null) {
      this.facade.error.set(`${label} debe ser un número mayor que cero.`);
    }
    return parsed;
  }

  private textOrNull(value: string): string | null {
    const trimmed = value.trim();
    return trimmed ? trimmed : null;
  }

  crearDocumentoCobrarManual() {
    const total = this.requiredNumber(this.docCobrarTotal(), "El total");
    if (total === null) return;
    const pedidoId = this.optionalNumber(this.docCobrarPedidoId());
    const clienteId = this.optionalNumber(this.docCobrarClienteId());
    if (pedidoId === null && clienteId === null) {
      this.facade.error.set("Indica un pedido ID o un cliente ID.");
      return;
    }
    this.facade.crearDocumentoCobrar({
      pedidoId,
      clienteId,
      codigo: this.textOrNull(this.docCobrarCodigo()),
      total,
      observaciones: this.textOrNull(this.docCobrarObservaciones()),
    });
  }

  registrarCobranzaManual() {
    const documentoCobrarId = this.requiredNumber(this.cobranzaDocumentoId(), "El documento por cobrar ID");
    const monto = this.requiredNumber(this.cobranzaMonto(), "El monto");
    if (documentoCobrarId === null || monto === null) return;
    this.facade.registrarCobranza({
      clienteId: this.optionalNumber(this.cobranzaClienteId()),
      montoTotal: monto,
      medioPago: this.textOrNull(this.cobranzaMedio()),
      referenciaPago: this.textOrNull(this.cobranzaReferencia()),
      aplicaciones: [{ documentoCobrarId, monto }],
    });
  }

  registrarPagoProveedorManual() {
    const documentoPagarId = this.requiredNumber(this.pagoDocumentoId(), "El documento por pagar ID");
    const monto = this.requiredNumber(this.pagoMonto(), "El monto");
    if (documentoPagarId === null || monto === null) return;
    this.facade.registrarPagoProveedor({
      proveedorId: this.optionalNumber(this.pagoProveedorId()),
      montoTotal: monto,
      medioPago: this.textOrNull(this.pagoMedio()),
      referenciaPago: this.textOrNull(this.pagoReferencia()),
      aplicaciones: [{ documentoPagarId, monto }],
    });
  }

  registrarAsientoSimple() {
    const monto = this.requiredNumber(this.asientoMonto(), "El monto del asiento");
    const cuentaDebe = this.textOrNull(this.asientoCuentaDebe());
    const cuentaHaber = this.textOrNull(this.asientoCuentaHaber());
    const diario = this.textOrNull(this.asientoDiario()) ?? "GENERAL";
    if (monto === null || !cuentaDebe || !cuentaHaber) {
      this.facade.error.set("Indica cuenta debe, cuenta haber y monto.");
      return;
    }
    this.facade.registrarAsientoContable({
      tipoDiarioCodigo: diario,
      descripcion: this.textOrNull(this.asientoDescripcion()) ?? "Asiento manual ERP",
      lineas: [
        { cuentaCodigo: cuentaDebe, descripcion: "Debe", debe: monto, haber: 0 },
        { cuentaCodigo: cuentaHaber, descripcion: "Haber", debe: 0, haber: monto },
      ],
    });
  }

  prepararDocumentoFiscalManual() {
    const documentoCobrarId = this.optionalNumber(this.fiscalDocumentoCobrarId());
    const documentoCompraId = this.optionalNumber(this.fiscalDocumentoCompraId());
    if ((documentoCobrarId === null && documentoCompraId === null) || (documentoCobrarId !== null && documentoCompraId !== null)) {
      this.facade.error.set("Indica exactamente un origen fiscal: documento por cobrar o documento de compra.");
      return;
    }
    this.facade.prepararDocumentoFiscal({
      documentoCobrarId,
      documentoCompraId,
      tipoComprobante: this.fiscalTipo() as any,
      establecimiento: "001",
      puntoEmision: "001",
      secuencial: this.textOrNull(this.fiscalSecuencial()),
    });
  }

  emitirDocumentoFiscalManual() {
    const id = this.requiredNumber(this.fiscalAccionId(), "El documento fiscal ID");
    if (id !== null) this.facade.emitirDocumentoFiscalInterno(id);
  }

  anularDocumentoFiscalManual() {
    const id = this.requiredNumber(this.fiscalAccionId(), "El documento fiscal ID");
    if (id !== null) {
      this.facade.anularDocumentoFiscal(id, { motivo: this.textOrNull(this.fiscalMotivoAnulacion()) ?? "Anulación administrativa interna" });
    }
  }

  bridgePedido() {
    const id = this.bridgePedidoId();
    if (id !== null) this.facade.generarDocumentoCobrarDesdePedido(id);
  }

  bridgeAsientoVenta() {
    const id = this.bridgeDocumentoCobrarId();
    if (id !== null) this.facade.generarAsientoVentaDesdeDocumentoCobrar(id);
  }

  bridgeAsientoCobro() {
    const id = this.bridgeCobranzaId();
    if (id !== null) this.facade.generarAsientoCobroDesdeCobranza(id);
  }

  bridgeAsientoCompra() {
    const id = this.bridgeDocumentoPagarId();
    if (id !== null) this.facade.generarAsientoCompraDesdeDocumentoPagar(id);
  }

  bridgeAsientoPago() {
    const id = this.bridgePagoProveedorId();
    if (id !== null) this.facade.generarAsientoPagoProveedor(id);
  }

  private loadCurrentWorkspace() {
    switch (this.domain()) {
      case "terceros":
        this.facade.loadTerceros();
        break;
      case "cartera":
        this.facade.loadCartera();
        break;
      case "cuentas-pagar":
        this.facade.loadCuentasPagar();
        break;
      case "contabilidad":
        this.facade.loadContabilidad();
        break;
      case "fiscalidad":
        this.facade.loadFiscalidad();
        break;
      case "inteligencia":
        this.facade.loadInteligencia(20);
        break;
      case "bridges":
        this.facade.loadInteligencia(10);
        break;
    }
  }
}
