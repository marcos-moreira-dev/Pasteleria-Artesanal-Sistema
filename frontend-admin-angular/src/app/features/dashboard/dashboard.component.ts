import { CommonModule, CurrencyPipe, DatePipe } from "@angular/common";
import { Component, computed, inject } from "@angular/core";
import { DashboardFacadeService } from "./state/dashboard.facade";
import type { OrderSummary } from "../pedidos/models/order.models";

interface ChartSegment {
  label: string;
  value: number;
  percent: number;
  color: string;
}

interface ChartBar {
  label: string;
  value: number;
  percent: number;
  color: string;
  helper: string;
}

const DAILY_REVENUE_TARGET = 90;
const WEEKLY_REVENUE_TARGET = 420;

const ORDER_STATUS_META = [
  { key: "REGISTRADO", label: "Registrado", color: "#8a3f2c" },
  { key: "EN_PREPARACION", label: "En preparación", color: "#c96e4a" },
  { key: "LISTO", label: "Listo", color: "#d8a164" },
  { key: "ENTREGADO", label: "Entregado", color: "#6d8b57" },
  { key: "CANCELADO", label: "Cancelado", color: "#8f776c" }
] as const;

const PRODUCTION_STATUS_LABELS: Record<string, string> = {
  PENDIENTE: "Pendiente",
  PREPARACION: "Preparación",
  DECORACION: "Decoración",
  EMPAQUE: "Empaque",
  FINALIZADO: "Finalizado"
};

const CATEGORY_COLORS = ["#8a3f2c", "#c96e4a", "#d8a164", "#6d8b57", "#a86f5a", "#8f776c"];

@Component({
  selector: "app-dashboard",
  standalone: true,
  imports: [CommonModule, CurrencyPipe, DatePipe],
  template: `
    <section class="kpi-grid">
      <article class="kpi-card">
        <p class="kpi-eyebrow">Caja del día</p>
        <strong>{{ revenueToday() | currency:'USD':'symbol':'1.2-2' }}</strong>
        <p class="kpi-copy">Facturación cerrada hoy en pedidos entregados.</p>
        <div class="mini-progress">
          <span class="mini-progress__fill" [style.width.%]="dailyGoalProgress()"></span>
        </div>
        <small>{{ deliveredTodayCount() }} cierres hoy · meta base {{ DAILY_REVENUE_TARGET | currency:'USD':'symbol':'1.0-0' }}</small>
      </article>

      <article class="kpi-card">
        <p class="kpi-eyebrow">Ritmo semanal</p>
        <strong>{{ revenueThisWeek() | currency:'USD':'symbol':'1.2-2' }}</strong>
        <p class="kpi-copy">Ingreso acumulado esta semana en pedidos ya entregados.</p>
        <small>{{ deliveredThisWeekCount() }} cierres registrados durante la semana</small>
      </article>

      <article class="kpi-card">
        <p class="kpi-eyebrow">Ticket promedio</p>
        <strong>{{ averageClosedTicket() | currency:'USD':'symbol':'1.2-2' }}</strong>
        <p class="kpi-copy">Promedio por pedido cerrado en la semana actual.</p>
        <small *ngIf="deliveredThisWeekCount(); else noTicketData">Sirve para medir qué tan saludable viene cada cierre.</small>
      </article>

      <article class="kpi-card kpi-card--goal">
        <p class="kpi-eyebrow">Meta mínima semanal</p>
        <strong>{{ weeklyGoalProgress() | number:'1.0-0' }}%</strong>
        <p class="kpi-copy">{{ revenueThisWeek() | currency:'USD':'symbol':'1.2-2' }} de {{ WEEKLY_REVENUE_TARGET | currency:'USD':'symbol':'1.0-0' }} cerrados.</p>
        <div class="goal-meter">
          <span class="goal-meter__fill" [style.width.%]="weeklyGoalProgress()"></span>
        </div>
        <small>
          {{ weeklyGoalHelper() }}
          <span class="goal-potential">Con el pipeline activo la cobertura potencial sube a {{ weeklyCoverageWithPipeline() | number:'1.0-0' }}%.</span>
        </small>
      </article>
    </section>

    <section class="metrics-grid">
      <article class="metric-card">
        <img src="assets/icons/clients.svg" alt="" width="28" height="28" aria-hidden="true" />
        <span>Clientes activos</span>
        <strong>{{ facade.dashboardMetrics().clients }}</strong>
        <small>Base comercial disponible para seguimiento y recompra.</small>
      </article>
      <article class="metric-card">
        <img src="assets/icons/quotations.svg" alt="" width="28" height="28" aria-hidden="true" />
        <span>Cotizaciones pendientes</span>
        <strong>{{ facade.dashboardMetrics().quotationsPending }}</strong>
        <small>Solicitudes que todavía requieren seguimiento comercial.</small>
      </article>
      <article class="metric-card accent">
        <img src="assets/icons/orders.svg" alt="" width="28" height="28" aria-hidden="true" />
        <span>Pedidos listos para salida</span>
        <strong>{{ readyOrdersCount() }}</strong>
        <small>Pedidos que ya pueden coordinar entrega o retiro.</small>
      </article>
      <article class="metric-card accent">
        <img src="assets/icons/production.svg" alt="" width="28" height="28" aria-hidden="true" />
        <span>Producción activa</span>
        <strong>{{ activeProductionCount() }}</strong>
        <small>Trabajo vivo en cocina, decoración y empaque.</small>
      </article>
    </section>

    <section class="visual-grid">
      <article class="chart-card chart-card--donut">
        <div class="chart-card__header">
          <p class="chart-kicker">Pastel estadístico</p>
          <h3>Estados de pedidos</h3>
          <p>Vista rápida del flujo operativo actual entre registro, preparación y entrega.</p>
        </div>

        <div class="donut-shell" *ngIf="orderStatusTotal() > 0; else emptyOrdersChart">
          <div class="donut-chart" [style.background]="orderDonutStyle()">
            <div class="donut-chart__center">
              <strong>{{ orderStatusTotal() }}</strong>
              <span>pedidos</span>
            </div>
          </div>

          <div class="chart-legend">
            <div class="legend-row" *ngFor="let segment of orderSegments()">
              <span class="legend-swatch" [style.background]="segment.color"></span>
              <span>{{ segment.label }}</span>
              <strong>{{ segment.value }}</strong>
            </div>
          </div>
        </div>
      </article>

      <article class="chart-card">
        <div class="chart-card__header">
          <p class="chart-kicker">Barras del catálogo</p>
          <h3>Productos por categoría</h3>
          <p>Cómo se reparte el catálogo entre tortas, postres, bebidas y líneas complementarias.</p>
        </div>

        <div class="bar-list" *ngIf="categoryBars().length; else emptyCategoriesChart">
          <div class="bar-row" *ngFor="let item of categoryBars()">
            <div class="bar-row__meta">
              <span>{{ item.label }}</span>
              <strong>{{ item.value }}</strong>
            </div>
            <div class="bar-track">
              <span class="bar-fill" [style.width.%]="item.percent" [style.background]="item.color"></span>
            </div>
            <small>{{ item.helper }}</small>
          </div>
        </div>
      </article>

      <article class="chart-card">
        <div class="chart-card__header">
          <p class="chart-kicker">Pulso comercial</p>
          <h3>Señales del tablero</h3>
          <p>Lectura rápida para ventas, cocina y seguimiento del día.</p>
        </div>

        <div class="bar-list" *ngIf="commercialPulse().length; else emptyPulseChart">
          <div class="bar-row" *ngFor="let item of commercialPulse()">
            <div class="bar-row__meta">
              <span>{{ item.label }}</span>
              <strong>{{ item.helper }}</strong>
            </div>
            <div class="bar-track">
              <span class="bar-fill" [style.width.%]="item.percent" [style.background]="item.color"></span>
            </div>
          </div>
        </div>

        <div class="chart-card__footer">
          <span>Monto estimado en pedidos activos</span>
          <strong>{{ activePipelineTotal() | currency:'USD':'symbol':'1.2-2' }}</strong>
        </div>
      </article>
    </section>

    <section class="panel-grid">
      <article class="panel-card">
        <div class="panel-card__header">
          <div>
            <p class="chart-kicker">Seguimiento diario</p>
            <h3><img src="assets/icons/orders.svg" alt="" width="22" height="22" aria-hidden="true" /> Pedidos recientes</h3>
          </div>
          <strong>{{ activePipelineTotal() | currency:'USD':'symbol':'1.2-2' }} en juego</strong>
        </div>

        <div class="list" *ngIf="facade.orders().length; else emptyOrders">
          <div class="list-row" *ngFor="let order of facade.orders().slice(0, 5)">
            <div class="list-row__main">
              <div class="list-row__heading">
                <strong>{{ order.code }}</strong>
                <span class="pill">{{ prettyOrderStatus(order.status) }}</span>
              </div>
              <p>{{ order.clientName }}</p>
              <small>Entrega estimada: {{ order.estimatedDeliveryAt | date:'short' }}</small>
            </div>

            <div class="align-end">
              <strong>{{ order.estimatedTotal | currency:'USD':'symbol':'1.2-2' }}</strong>
              <p>{{ order.origin === 'PUBLICO' ? 'Canal público' : 'Gestión interna' }}</p>
            </div>
          </div>
        </div>
      </article>

      <article class="panel-card">
        <div class="panel-card__header">
          <div>
            <p class="chart-kicker">Obrador</p>
            <h3><img src="assets/icons/production.svg" alt="" width="22" height="22" aria-hidden="true" /> Producción en foco</h3>
          </div>
          <strong>{{ activeProductionCount() }} activas</strong>
        </div>

        <div class="list" *ngIf="facade.productionQueue().length; else emptyProduction">
          <div class="list-row" *ngFor="let item of facade.productionQueue().slice(0, 5)">
            <div class="list-row__main">
              <div class="list-row__heading">
                <strong>{{ item.orderCode }}</strong>
                <span class="pill">{{ prettyProductionStatus(item.status) }}</span>
              </div>
              <p>{{ item.clientName }}</p>
              <small>Registrado: {{ item.createdAt | date:'short' }}</small>
            </div>

            <div class="align-end">
              <strong>{{ item.priority }}</strong>
              <p>{{ item.productionNotes || 'Sin observaciones registradas.' }}</p>
            </div>
          </div>
        </div>
      </article>
    </section>

    <section class="panel-grid panel-grid--reports">
      <article class="panel-card">
        <div class="panel-card__header">
          <div>
            <p class="chart-kicker">Trabajo diferido</p>
            <h3><img src="assets/icons/quotations.svg" alt="" width="22" height="22" aria-hidden="true" /> Reportes operativos</h3>
          </div>
          <strong>{{ facade.reportJobs().length }} recientes</strong>
        </div>

        <div class="action-row action-row--reports">
          <button type="button" class="surface-button" (click)="requestReport('RESUMEN_NEGOCIO')">Generar resumen de negocio</button>
          <button type="button" class="mini-button mini-button--ghost" (click)="requestReport('COLA_PRODUCCION')">Generar cola de producción</button>
        </div>

        <div class="list" *ngIf="facade.reportJobs().length; else emptyReports">
          <div class="list-row" *ngFor="let job of facade.reportJobs()">
            <div class="list-row__main">
              <div class="list-row__heading">
                <strong>{{ job.jobCode }}</strong>
                <span class="pill">{{ prettyReportStatus(job.status) }}</span>
              </div>
              <p>{{ prettyReportType(job.reportType) }}</p>
              <small>Solicitado: {{ job.requestedAt | date:'short' }}</small>
            </div>

            <div class="align-end">
              <strong>{{ job.fileName || 'Sin archivo aún' }}</strong>
              <p>{{ job.errorMessage || (job.fileId ? 'Archivo generado en storage local.' : 'Esperando worker.') }}</p>
            </div>
          </div>
        </div>
      </article>
    </section>

    <ng-template #noTicketData>
      <small>Todavía no hay suficientes cierres esta semana para comparar.</small>
    </ng-template>
    <ng-template #emptyOrdersChart>
      <p class="empty">Todavía no hay pedidos suficientes para dibujar el pastel operativo.</p>
    </ng-template>
    <ng-template #emptyCategoriesChart>
      <p class="empty">Todavía no hay categorías con productos publicados.</p>
    </ng-template>
    <ng-template #emptyPulseChart>
      <p class="empty">Todavía no hay señal suficiente para mostrar comparativas.</p>
    </ng-template>
    <ng-template #emptyOrders>
      <p class="empty">No hay pedidos disponibles.</p>
    </ng-template>
    <ng-template #emptyProduction>
      <p class="empty">No hay cola de producción activa.</p>
    </ng-template>
    <ng-template #emptyReports>
      <p class="empty">Todavía no hay jobs de reporte en esta sesión.</p>
    </ng-template>
  `,
  styles: [`
    .kpi-grid,
    .metrics-grid,
    .visual-grid,
    .panel-grid {
      display: grid;
      gap: 1rem;
    }

    .kpi-grid,
    .metrics-grid {
      grid-template-columns: repeat(4, minmax(0, 1fr));
    }

    .metrics-grid,
    .visual-grid,
    .panel-grid {
      margin-top: 1rem;
    }

    .visual-grid {
      grid-template-columns: 1.1fr 1fr 1fr;
      align-items: start;
    }

    .panel-grid {
      grid-template-columns: 1.15fr 0.95fr;
    }

    .panel-grid--reports {
      margin-top: 1rem;
      grid-template-columns: 1fr;
    }

    .kpi-card,
    .metric-card,
    .chart-card,
    .panel-card {
      padding: 1.2rem;
      border-radius: 4px;
      background: linear-gradient(180deg, rgba(255, 255, 255, 0.95), rgba(255, 250, 245, 0.92));
      border: 1px solid #eaded4;
      box-shadow: 0 18px 45px rgba(92, 60, 42, 0.08);
    }

    .kpi-card {
      display: grid;
      gap: 0.7rem;
      position: relative;
      overflow: hidden;
    }

    .kpi-card::after {
      content: "";
      position: absolute;
      inset: auto 0 0 0;
      height: 4px;
      background: linear-gradient(90deg, #8a3f2c, #d8a164);
      opacity: 0.82;
    }

    .kpi-card--goal {
      background: linear-gradient(180deg, rgba(255, 247, 241, 0.97), rgba(255, 252, 249, 0.95));
    }

    .kpi-eyebrow,
    .metric-card span,
    .chart-card__header p,
    .chart-kicker {
      display: block;
      margin: 0;
      color: #8a5c46;
      font-size: 0.82rem;
      letter-spacing: 0.12em;
      text-transform: uppercase;
    }

    .kpi-card strong,
    .metric-card strong {
      display: block;
      margin: 0;
      font: 700 2.2rem/1 var(--font-display, "Cormorant Garamond", Georgia, serif);
      color: #2d201a;
    }

    .kpi-copy,
    .metric-card small,
    .chart-card__header p:last-child,
    .list-row p,
    .list-row small,
    .align-end p {
      margin: 0;
      color: #7a6054;
      line-height: 1.55;
    }

    .mini-progress,
    .goal-meter {
      width: 100%;
      height: 12px;
      background: #f1e5dc;
      border: 1px solid #ecdccf;
      overflow: hidden;
    }

    .mini-progress__fill,
    .goal-meter__fill {
      display: block;
      height: 100%;
      min-width: 2%;
      background: linear-gradient(90deg, #8a3f2c, #d8a164);
    }

    .goal-potential {
      display: block;
      margin-top: 0.25rem;
    }

    .metric-card {
      position: relative;
      overflow: hidden;
    }

    .metric-card::after {
      content: "";
      position: absolute;
      inset: auto 0 0 0;
      height: 4px;
      background: linear-gradient(90deg, #8a3f2c, #d8a164);
      opacity: 0.75;
    }

    .metric-card img {
      display: block;
      margin-bottom: 0.8rem;
    }

    .metric-card strong {
      margin-top: 0.65rem;
      font-size: 2.05rem;
    }

    .metric-card small {
      display: block;
      margin-top: 0.75rem;
    }

    .metric-card.accent {
      background: linear-gradient(180deg, rgba(255, 246, 240, 0.96), rgba(255, 253, 250, 0.94));
    }

    .chart-card {
      display: grid;
      gap: 1rem;
      min-height: 100%;
    }

    .chart-card__header {
      display: grid;
      gap: 0.4rem;
    }

    .chart-card__header h3,
    .panel-card__header h3 {
      margin: 0;
      display: flex;
      align-items: center;
      gap: 0.55rem;
      font: 700 1.35rem/1.08 var(--font-display, "Cormorant Garamond", Georgia, serif);
      color: #2d201a;
    }

    .chart-card__header p:last-child {
      text-transform: none;
      letter-spacing: 0;
      font-size: 0.98rem;
    }

    .donut-shell {
      display: grid;
      grid-template-columns: 190px 1fr;
      gap: 1rem;
      align-items: center;
    }

    .donut-chart {
      width: 190px;
      height: 190px;
      border-radius: 50%;
      display: grid;
      place-items: center;
      position: relative;
      box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.45);
    }

    .donut-chart::after {
      content: "";
      width: 110px;
      height: 110px;
      border-radius: 50%;
      background: #fffaf6;
      border: 1px solid #eaded4;
      box-shadow: 0 8px 24px rgba(92, 60, 42, 0.08);
    }

    .donut-chart__center {
      position: absolute;
      z-index: 1;
      display: grid;
      text-align: center;
      gap: 0.2rem;
    }

    .donut-chart__center strong {
      font: 700 2rem/1 var(--font-display, "Cormorant Garamond", Georgia, serif);
      color: #2d201a;
    }

    .donut-chart__center span {
      color: #7a6054;
      font-size: 0.84rem;
      text-transform: uppercase;
      letter-spacing: 0.12em;
    }

    .chart-legend,
    .bar-list {
      display: grid;
      gap: 0.8rem;
    }

    .legend-row,
    .bar-row__meta {
      display: grid;
      grid-template-columns: auto 1fr auto;
      gap: 0.7rem;
      align-items: center;
    }

    .legend-row span,
    .bar-row__meta span {
      color: #4d3b34;
    }

    .legend-row strong,
    .bar-row__meta strong,
    .chart-card__footer strong,
    .panel-card__header > strong {
      color: #2d201a;
      font: 700 1rem/1.1 var(--font-display, "Cormorant Garamond", Georgia, serif);
    }

    .legend-swatch {
      width: 12px;
      height: 12px;
      border-radius: 50%;
      display: inline-block;
    }

    .bar-row {
      display: grid;
      gap: 0.45rem;
    }

    .bar-track {
      width: 100%;
      height: 12px;
      background: #f1e5dc;
      border: 1px solid #ecdccf;
      overflow: hidden;
    }

    .bar-fill {
      display: block;
      height: 100%;
      min-width: 2%;
    }

    .bar-row small {
      color: #8a6c60;
    }

    .chart-card__footer {
      display: flex;
      justify-content: space-between;
      gap: 1rem;
      align-items: center;
      padding-top: 0.8rem;
      border-top: 1px solid #f0e3d8;
      color: #7a6054;
    }

    .panel-card {
      display: grid;
      grid-template-rows: auto minmax(0, 1fr);
      gap: 1rem;
      min-height: 100%;
    }

    .panel-card__header {
      display: flex;
      justify-content: space-between;
      gap: 1rem;
      align-items: start;
    }

    .list {
      display: grid;
      gap: 0.85rem;
      max-height: 33rem;
      overflow: auto;
      padding-right: 0.3rem;
    }

    .list::-webkit-scrollbar {
      width: 9px;
    }

    .list::-webkit-scrollbar-thumb {
      background: #d7c0b2;
    }

    .list-row {
      display: flex;
      justify-content: space-between;
      gap: 1rem;
      padding-top: 0.95rem;
      border-top: 1px solid #f1e8e2;
    }

    .list-row:first-child {
      border-top: 0;
      padding-top: 0;
    }

    .list-row__main {
      display: grid;
      gap: 0.3rem;
    }

    .list-row__heading {
      display: flex;
      align-items: center;
      gap: 0.55rem;
      flex-wrap: wrap;
    }

    .align-end {
      text-align: right;
      display: grid;
      gap: 0.35rem;
      justify-items: end;
    }

    .align-end strong {
      font: 700 1.02rem/1.1 var(--font-display, "Cormorant Garamond", Georgia, serif);
      color: #2d201a;
    }

    .pill {
      display: inline-block;
      padding: 0.3rem 0.65rem;
      border-radius: 2px;
      background: #f3e0d6;
      color: #733b2a;
      font-size: 0.78rem;
      font-weight: 700;
    }

    .action-row--reports {
      margin-bottom: 1rem;
      flex-wrap: wrap;
    }

    .mini-button--ghost {
      background: #f6eee7;
      color: #725142;
    }

    .empty {
      margin: 0;
      padding: 1rem 0;
      color: #7f746d;
      line-height: 1.6;
    }

    @media (max-width: 1180px) {
      .kpi-grid,
      .metrics-grid,
      .visual-grid,
      .panel-grid {
        grid-template-columns: 1fr 1fr;
      }

      .visual-grid article:last-child,
      .panel-grid article:last-child {
        grid-column: 1 / -1;
      }
    }

    @media (max-width: 820px) {
      .kpi-grid,
      .metrics-grid,
      .visual-grid,
      .panel-grid,
      .donut-shell {
        grid-template-columns: 1fr;
      }

      .donut-chart {
        margin-inline: auto;
      }
    }
  `]
})
export class DashboardComponent {
  readonly facade = inject(DashboardFacadeService);
  readonly DAILY_REVENUE_TARGET = DAILY_REVENUE_TARGET;
  readonly WEEKLY_REVENUE_TARGET = WEEKLY_REVENUE_TARGET;

  readonly orderSegments = computed(() => this.buildStatusSegments(
    ORDER_STATUS_META,
    this.facade.orders().map((order) => order.status)
  ));

  readonly orderStatusTotal = computed(() =>
    this.orderSegments().reduce((total, item) => total + item.value, 0)
  );

  readonly orderDonutStyle = computed(() => this.buildDonutStyle(this.orderSegments()));

  readonly categoryBars = computed(() => {
    const products = this.facade.products();
    const categories = this.facade.categories();
    const max = Math.max(
      1,
      ...categories.map((category) =>
        products.filter((product) => product.categoryCode === category.code).length
      )
    );

    return categories.map((category, index) => {
      const value = products.filter((product) => product.categoryCode === category.code).length;
      return {
        label: category.name,
        value,
        percent: max === 0 ? 0 : (value / max) * 100,
        color: CATEGORY_COLORS[index % CATEGORY_COLORS.length],
        helper: value === 1 ? "1 producto en esta línea" : `${value} productos en esta línea`
      };
    });
  });

  readonly commercialPulse = computed(() => {
    const quotations = this.facade.quotations();
    const orders = this.facade.orders();
    const production = this.facade.productionQueue();

    const publicQuotations = quotations.filter((item) => item.origin === "PUBLICO").length;
    const urgentOrders = orders.filter((item) => item.priority === "URGENTE").length;
    const activeProduction = production.filter((item) => item.status !== "FINALIZADO").length;
    const convertedQuotations = quotations.filter((item) => item.status === "CONVERTIDA").length;

    return [
      this.buildPulseBar("Solicitudes públicas", publicQuotations, quotations.length, "#8a3f2c"),
      this.buildPulseBar("Pedidos urgentes", urgentOrders, orders.length, "#c96e4a"),
      this.buildPulseBar("Producción activa", activeProduction, production.length, "#6d8b57"),
      this.buildPulseBar("Cotizaciones convertidas", convertedQuotations, quotations.length, "#d8a164")
    ];
  });

  readonly deliveredOrders = computed(() =>
    this.facade.orders().filter((order) => order.status === "ENTREGADO")
  );

  readonly revenueToday = computed(() =>
    this.deliveredOrders()
      .filter((order) => this.isToday(this.resolveClosedDate(order)))
      .reduce((total, order) => total + order.estimatedTotal, 0)
  );

  readonly deliveredTodayCount = computed(() =>
    this.deliveredOrders().filter((order) => this.isToday(this.resolveClosedDate(order))).length
  );

  readonly revenueThisWeek = computed(() =>
    this.deliveredOrders()
      .filter((order) => this.isCurrentWeek(this.resolveClosedDate(order)))
      .reduce((total, order) => total + order.estimatedTotal, 0)
  );

  readonly deliveredThisWeekCount = computed(() =>
    this.deliveredOrders().filter((order) => this.isCurrentWeek(this.resolveClosedDate(order))).length
  );

  readonly averageClosedTicket = computed(() => {
    const count = this.deliveredThisWeekCount();
    return count === 0 ? 0 : this.revenueThisWeek() / count;
  });

  readonly dailyGoalProgress = computed(() =>
    this.clampPercent((this.revenueToday() / DAILY_REVENUE_TARGET) * 100)
  );

  readonly weeklyGoalProgress = computed(() =>
    this.clampPercent((this.revenueThisWeek() / WEEKLY_REVENUE_TARGET) * 100)
  );

  readonly activePipelineTotal = computed(() =>
    this.facade.orders()
      .filter((order) => order.status !== "ENTREGADO" && order.status !== "CANCELADO")
      .reduce((total, order) => total + order.estimatedTotal, 0)
  );

  readonly weeklyCoverageWithPipeline = computed(() =>
    this.clampPercent(((this.revenueThisWeek() + this.activePipelineTotal()) / WEEKLY_REVENUE_TARGET) * 100)
  );

  readonly readyOrdersCount = computed(() =>
    this.facade.orders().filter((order) => order.status === "LISTO").length
  );

  readonly activeProductionCount = computed(() =>
    this.facade.productionQueue().filter((item) => item.status !== "FINALIZADO").length
  );

  weeklyGoalHelper(): string {
    const remaining = Math.max(WEEKLY_REVENUE_TARGET - this.revenueThisWeek(), 0);
    if (remaining === 0) {
      return "La meta mínima semanal ya quedó cubierta con cierres reales.";
    }
    return `Faltan ${new Intl.NumberFormat("es-EC", { style: "currency", currency: "USD" }).format(remaining)} para cubrir la meta solo con cierres.`;
  }

  prettyOrderStatus(status: string): string {
    return ORDER_STATUS_META.find((item) => item.key === status)?.label ?? this.prettyLabel(status);
  }

  prettyProductionStatus(status: string): string {
    return PRODUCTION_STATUS_LABELS[status] ?? this.prettyLabel(status);
  }

  prettyReportType(reportType: "RESUMEN_NEGOCIO" | "COLA_PRODUCCION"): string {
    switch (reportType) {
      case "RESUMEN_NEGOCIO":
        return "Resumen de negocio";
      case "COLA_PRODUCCION":
        return "Cola de producción";
    }
  }

  prettyReportStatus(status: "PENDIENTE" | "EN_PROCESO" | "COMPLETADO" | "ERROR" | "CANCELADO" | "EXPIRADO"): string {
    return this.prettyLabel(status);
  }

  requestReport(reportType: "RESUMEN_NEGOCIO" | "COLA_PRODUCCION") {
    this.facade.requestReport(reportType);
  }

  private buildStatusSegments(
      metadata: readonly { key: string; label: string; color: string }[],
      sourceValues: string[]
  ): ChartSegment[] {
    const total = sourceValues.length || 1;

    return metadata
      .map((item) => {
        const value = sourceValues.filter((current) => current === item.key).length;
        return {
          label: item.label,
          value,
          percent: value === 0 ? 0 : Number(((value / total) * 100).toFixed(2)),
          color: item.color
        };
      })
      .filter((item) => item.value > 0);
  }

  private buildPulseBar(label: string, value: number, total: number, color: string): ChartBar {
    const safeTotal = Math.max(total, 1);
    return {
      label,
      value,
      percent: (value / safeTotal) * 100,
      color,
      helper: `${value} de ${total || 0}`
    };
  }

  private buildDonutStyle(segments: ChartSegment[]): string {
    if (!segments.length) {
      return "conic-gradient(#efe2d7 0 100%)";
    }

    let cursor = 0;
    const stops = segments.map((item) => {
      const start = cursor;
      cursor += item.percent;
      const end = cursor >= 99.5 ? 100 : cursor;
      return `${item.color} ${start}% ${end}%`;
    });

    return `conic-gradient(${stops.join(", ")})`;
  }

  private resolveClosedDate(order: OrderSummary): Date | null {
    const reference = order.actualDeliveryAt ?? order.estimatedDeliveryAt ?? order.orderDate;
    return reference ? new Date(reference) : null;
  }

  private isToday(value: Date | null): boolean {
    if (!value) {
      return false;
    }

    const now = new Date();
    return value.getFullYear() === now.getFullYear()
      && value.getMonth() === now.getMonth()
      && value.getDate() === now.getDate();
  }

  private isCurrentWeek(value: Date | null): boolean {
    if (!value) {
      return false;
    }

    const start = this.startOfWeek(new Date());
    const end = new Date(start);
    end.setDate(end.getDate() + 7);
    return value >= start && value < end;
  }

  private startOfWeek(value: Date): Date {
    const date = new Date(value);
    date.setHours(0, 0, 0, 0);
    const day = date.getDay();
    const diff = day === 0 ? -6 : 1 - day;
    date.setDate(date.getDate() + diff);
    return date;
  }

  private clampPercent(value: number): number {
    if (!Number.isFinite(value) || value < 0) {
      return 0;
    }
    return Math.min(100, Number(value.toFixed(2)));
  }

  private prettyLabel(value: string): string {
    return value
      .toLowerCase()
      .split("_")
      .map((chunk) => chunk.charAt(0).toUpperCase() + chunk.slice(1))
      .join(" ");
  }
}
