import { CommonModule, DatePipe } from "@angular/common";
import { Component, OnInit, computed, inject } from "@angular/core";
import { ProductionFacadeService } from "./state/production.facade";
import type { ProductionStatusValue, ProductionSummary } from "./models/production.models";
import { ADMIN_SURFACE_STYLES } from "../../shared/ui/admin-surface.styles";

interface ProductionAction {
  status: ProductionStatusValue;
  label: string;
  reason: string;
}

const PRODUCTION_STAGES: ProductionStatusValue[] = [
  "PENDIENTE",
  "PREPARACION",
  "DECORACION",
  "EMPAQUE",
  "FINALIZADO"
];

@Component({
  selector: "app-production-page",
  standalone: true,
  imports: [CommonModule, DatePipe],
  template: `
    <section class="surface-card">
      <header class="surface-header">
        <p class="surface-kicker">Cocina y decoración</p>
        <div class="surface-title-row">
          <img src="assets/icons/production.svg" alt="" width="28" height="28" aria-hidden="true" />
          <h3>Cola de producción</h3>
        </div>
        <p class="surface-copy">Seguimiento de preparación, decoración, empaque y salida final de cada pedido.</p>
      </header>

      <div class="page-toolbar" *ngIf="productionPage().totalElements">
        <p class="pager__meta">{{ pageSummary() }}</p>
        <label class="pager__size">
          Tarjetas por página
          <select [value]="productionPage().size" (change)="changePageSize($any($event.target).value)">
            <option value="4">4</option>
            <option value="6">6</option>
            <option value="8">8</option>
          </select>
        </label>
      </div>

      <section class="queue-section" *ngIf="activeQueue().length; else empty">
        <div class="queue-header">
          <div>
            <p class="surface-kicker">Frente activo</p>
            <h4>Pedidos en curso</h4>
          </div>
          <span class="summary-chip">{{ activeQueue().length }} en seguimiento</span>
        </div>

        <div class="cards-grid production-grid">
          <article *ngFor="let item of activeQueue()">
            <img src="assets/icons/production.svg" alt="" aria-hidden="true" />
            <p class="surface-kicker">{{ item.orderCode }}</p>
            <h4>{{ item.clientName }}</h4>
            <p class="card-copy">{{ item.productionNotes || 'Sin observaciones operativas.' }}</p>

            <div class="chip-row">
              <span class="status-pill">{{ productionLabel(item.status) }}</span>
              <span class="summary-chip"><img src="assets/icons/dashboard.svg" alt="" aria-hidden="true" /> {{ item.createdAt | date:'short' }}</span>
            </div>

            <div class="stage-track" aria-label="Etapas de producción">
              <span
                *ngFor="let stage of stages"
                class="stage-chip"
                [class.is-done]="isStageDone(item.status, stage)"
                [class.is-active]="item.status === stage"
              >
                {{ productionLabel(stage) }}
              </span>
            </div>

            <div class="action-row action-row--tight" *ngIf="nextAction(item) as action">
              <button type="button" class="mini-button mini-button--primary" (click)="moveProduction(item, action)">
                {{ action.label }}
              </button>
            </div>
          </article>
        </div>
      </section>

      <section class="queue-section queue-section--secondary" *ngIf="recentFinished().length">
        <div class="queue-header">
          <div>
            <p class="surface-kicker">Cierre del día</p>
            <h4>Finalizados recientes</h4>
          </div>
          <span class="summary-chip">{{ recentFinished().length }} cerrados</span>
        </div>

        <div class="cards-grid production-grid production-grid--completed">
          <article *ngFor="let item of recentFinished()">
            <img src="assets/icons/production.svg" alt="" aria-hidden="true" />
            <p class="surface-kicker">{{ item.orderCode }}</p>
            <h4>{{ item.clientName }}</h4>
            <p class="card-copy">{{ item.productionNotes || 'Pedido ya preparado para entrega.' }}</p>

            <div class="chip-row">
              <span class="status-pill">{{ productionLabel(item.status) }}</span>
              <span class="summary-chip"><img src="assets/icons/dashboard.svg" alt="" aria-hidden="true" /> {{ (item.finishedAt || item.createdAt) | date:'short' }}</span>
            </div>

            <div class="stage-track" aria-label="Etapas de producción">
              <span
                *ngFor="let stage of stages"
                class="stage-chip"
                [class.is-done]="isStageDone(item.status, stage)"
                [class.is-active]="item.status === stage"
              >
                {{ productionLabel(stage) }}
              </span>
            </div>
          </article>
        </div>
      </section>

      <ng-template #empty>
        <div class="empty-state">No hay producción activa en este momento.</div>
      </ng-template>

      <div class="pager" *ngIf="productionPage().totalElements">
        <p class="pager__meta">Página {{ productionPage().page + 1 }} de {{ productionPage().totalPages || 1 }}</p>
        <div class="pager__controls">
          <button type="button" class="mini-button" [disabled]="productionPage().first" (click)="goToPage(productionPage().page - 1)">
            Anterior
          </button>
          <button type="button" class="mini-button" [disabled]="productionPage().last" (click)="goToPage(productionPage().page + 1)">
            Siguiente
          </button>
        </div>
      </div>
    </section>
  `,
  styles: [ADMIN_SURFACE_STYLES, `
    .queue-section {
      display: grid;
      gap: 1rem;
    }

    .queue-section + .queue-section {
      margin-top: 1.5rem;
      padding-top: 1.35rem;
      border-top: 1px solid #efdfd3;
    }

    .queue-header {
      display: flex;
      align-items: start;
      justify-content: space-between;
      gap: 1rem;
    }

    .queue-header h4 {
      margin: 0.35rem 0 0;
      font: 700 1.15rem/1.1 var(--font-display, "Cormorant Garamond", Georgia, serif);
      color: #2d201a;
    }

    .production-grid article {
      display: grid;
      gap: 0.85rem;
    }

    .production-grid--completed article {
      background: linear-gradient(180deg, #fffaf6, #fff6ef);
    }

    .stage-track {
      display: flex;
      flex-wrap: wrap;
      gap: 0.45rem;
      margin-top: 0.2rem;
    }

    .stage-chip {
      display: inline-flex;
      align-items: center;
      padding: 0.35rem 0.6rem;
      border: 1px solid #e7d8cd;
      background: #fff8f3;
      color: #866d61;
      font-size: 0.76rem;
      font-weight: 700;
      letter-spacing: 0.03em;
    }

    .stage-chip.is-done {
      background: #f6e2d5;
      border-color: #ddc1b0;
      color: #6d3e2f;
    }

    .stage-chip.is-active {
      background: #4b2519;
      border-color: #4b2519;
      color: #fff4ee;
    }

    .mini-button--primary {
      background: linear-gradient(180deg, #4f2519, #2f1912);
      color: #fff7f2;
    }

    .action-row--tight {
      margin-top: 0.2rem;
    }
  `]
})
export class ProductionPageComponent implements OnInit {
  readonly facade = inject(ProductionFacadeService);
  readonly stages = PRODUCTION_STAGES;
  readonly productionPage = computed(() => this.facade.productionPage());
  readonly activeQueue = computed(() =>
    this.productionPage().content.filter((item) => item.status !== "FINALIZADO")
  );
  readonly recentFinished = computed(() =>
    this.productionPage().content.filter((item) => item.status === "FINALIZADO")
  );

  ngOnInit() {
    this.facade.loadPage();
  }

  productionLabel(status: ProductionStatusValue): string {
    switch (status) {
      case "PENDIENTE":
        return "Pendiente";
      case "PREPARACION":
        return "Preparación";
      case "DECORACION":
        return "Decoración";
      case "EMPAQUE":
        return "Empaque";
      case "FINALIZADO":
        return "Finalizado";
    }
  }

  nextAction(item: ProductionSummary): ProductionAction | null {
    switch (item.status) {
      case "PENDIENTE":
        return {
          status: "PREPARACION",
          label: "Iniciar preparación",
          reason: "Inicio de preparación en cocina."
        };
      case "PREPARACION":
        return {
          status: "DECORACION",
          label: "Pasar a decoración",
          reason: "La base del pedido está lista y pasa a decoración."
        };
      case "DECORACION":
        return {
          status: "EMPAQUE",
          label: "Pasar a empaque",
          reason: "Decoración terminada; el pedido pasa a empaque."
        };
      case "EMPAQUE":
        return {
          status: "FINALIZADO",
          label: "Marcar listo",
          reason: "Empaque completado y pedido listo para entrega."
        };
      case "FINALIZADO":
        return null;
    }
  }

  isStageDone(current: ProductionStatusValue, stage: ProductionStatusValue): boolean {
    return this.stages.indexOf(stage) < this.stages.indexOf(current);
  }

  moveProduction(item: ProductionSummary, action: ProductionAction) {
    this.facade.updateProductionStatus(
      item.id,
      action.status,
      action.reason,
      `${item.orderCode} pasó a ${this.productionLabel(action.status)}.`
    );
  }

  goToPage(page: number) {
    this.facade.loadPage(page, this.productionPage().size);
  }

  changePageSize(size: string) {
    this.facade.loadPage(0, Number(size));
  }

  pageSummary(): string {
    const page = this.productionPage();
    if (!page.totalElements) {
      return "Sin producción para mostrar.";
    }
    const from = page.page * page.size + 1;
    const to = page.page * page.size + page.numberOfElements;
    return `Mostrando ${from}-${to} de ${page.totalElements} frentes de producción.`;
  }
}
