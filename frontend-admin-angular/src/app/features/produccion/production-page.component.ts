import { CommonModule, DatePipe } from "@angular/common";
import { Component, OnInit, computed, inject, signal } from "@angular/core";
import { FormsModule } from "@angular/forms";
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

const ESTADO_LABELS: Record<string, string> = {
  "TODOS": "Todos",
  "PENDIENTE": "Pendiente",
  "PREPARACION": "Preparación",
  "DECORACION": "Decoración",
  "EMPAQUE": "Empaque",
  "FINALIZADO": "Finalizado"
};

@Component({
  selector: "app-production-page",
  standalone: true,
  imports: [CommonModule, DatePipe, FormsModule],
  template: `
    <section class="surface-card">
      <header class="surface-header">
        <p class="surface-kicker">Cocina y decoración</p>
        <div class="surface-title-row">
          <img src="assets/icons/production.svg" alt="" width="28" height="28" aria-hidden="true" />
          <h3>Cola de producción</h3>
        </div>
        <p class="surface-copy">Seguimiento de preparación, decoración, empaque y salida final de cada pedido.</p>
        
        <!-- FILTROS -->
        <div class="filter-toolbar">
          <label class="search-box filter-toolbar__search">
            <span>Buscar pedido</span>
            <input 
              type="search" 
              [ngModel]="searchQuery()"
              (ngModelChange)="updateSearchQuery($event)"
              placeholder="Código o nombre de cliente..."
            />
          </label>
          
          <div class="chip-row chip-row--nowrap">
            <span class="surface-meta">Estado:</span>
            <button
              *ngFor="let estado of estadosFiltro"
              class="chip"
              [class.chip--active]="estadoFilter() === estado"
              (click)="updateEstadoFilter(estado)"
            >
              {{ ESTADO_LABELS[estado] }}
            </button>
          </div>
        </div>
      </header>

      <!-- PAGINACIÓN Y RESUMEN -->
      <div class="page-toolbar" *ngIf="allFilteredProductionsList().length">
        <p class="pager__meta">
          Mostrando {{ (currentPage() * pageSize()) + 1 }} - 
          {{ Math.min((currentPage() + 1) * pageSize(), allFilteredProductionsList().length) }} 
          de {{ allFilteredProductionsList().length }} trabajos
          <span *ngIf="searchQuery() || estadoFilter() !== 'TODOS'" class="pager__meta pager__meta--accent">
            (filtrado)
          </span>
        </p>
        <div class="pager-inline">
          <button 
            class="mini-button" 
            [disabled]="currentPage() === 0"
            (click)="goToPage(currentPage() - 1)"
          >
            ← Anterior
          </button>
          <span class="page-chip">
            {{ currentPage() + 1 }} / {{ totalPages() }}
          </span>
          <button 
            class="mini-button" 
            [disabled]="currentPage() >= totalPages() - 1"
            (click)="goToPage(currentPage() + 1)"
          >
            Siguiente →
          </button>
        </div>
        <label class="pager__size">
          Por página
          <select [value]="pageSize()" (change)="changePageSize($any($event.target).value)">
            <option value="4">4</option>
            <option value="6">6</option>
            <option value="8">8</option>
            <option value="12">12</option>
          </select>
        </label>
      </div>

      <!-- PEDIDO CLAVADO -->
      <section class="queue-section queue-section--pinned" *ngIf="pinnedProduction() as pinned">
        <div class="queue-header">
          <div>
            <p class="surface-kicker">Pedido fijado</p>
            <h4>Siempre visible</h4>
          </div>
          <button class="mini-button" (click)="pinnedProduction.set(null)">
            ✕ Desfijar
          </button>
        </div>
        <div class="cards-grid production-grid">
          <article class="is-pinned">
            <div class="card-header-row">
              <p class="surface-kicker">{{ pinned.orderCode }}</p>
              <button class="mini-button" (click)="pinnedProduction.set(null)">
                ✕ Desfijar
              </button>
            </div>

            <h4>{{ pinned.clientName }}</h4>
            <p class="card-copy">{{ pinned.productionNotes || 'Sin observaciones operativas.' }}</p>

            <div class="chip-row">
              <span class="status-pill">{{ productionLabel(pinned.status) }}</span>
              <span class="summary-chip"><img src="assets/icons/dashboard.svg" alt="" aria-hidden="true" /> {{ pinned.createdAt | date:'short' }}</span>
            </div>

            <div class="stage-track" aria-label="Etapas de producción">
              <span
                *ngFor="let stage of stages"
                class="stage-chip"
                [class.is-done]="isStageDone(pinned.status, stage)"
                [class.is-active]="pinned.status === stage"
              >
                {{ productionLabel(stage) }}
              </span>
            </div>

            <div class="action-row action-row--tight">
              <button 
                *ngIf="previousAction(pinned) as prevAction" 
                type="button" 
                class="mini-button" 
                (click)="moveProduction(pinned, prevAction)"
              >
                ← {{ prevAction.label }}
              </button>
              <button 
                *ngIf="nextAction(pinned) as nextAction" 
                type="button" 
                class="mini-button mini-button--primary" 
                (click)="moveProduction(pinned, nextAction)"
              >
                {{ nextAction.label }} →
              </button>
            </div>
          </article>
        </div>
      </section>

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
            <div class="card-header-row">
              <img src="assets/icons/production.svg" alt="" aria-hidden="true" />
              <button 
                type="button" 
                class="mini-button pin-button" 
                (click)="pinnedProduction.set(item)"
                title="Fijar este pedido para que siempre sea visible"
              >
                <img src="assets/icons/pin.svg" alt="" width="14" height="14" />
                Fijar
              </button>
            </div>
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

            <div class="action-row action-row--tight">
              <button 
                *ngIf="previousAction(item) as prevAction" 
                type="button" 
                class="mini-button" 
                (click)="moveProduction(item, prevAction)"
              >
                ← {{ prevAction.label }}
              </button>
              <button 
                *ngIf="nextAction(item) as nextAction" 
                type="button" 
                class="mini-button mini-button--primary" 
                (click)="moveProduction(item, nextAction)"
              >
                {{ nextAction.label }} →
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
          <button type="button" class="mini-button" [disabled]="currentPage() === 0" (click)="goToPage(currentPage() - 1)">
            ← Anterior
          </button>
          <span class="pager__current">{{ currentPage() + 1 }} / {{ totalPages() }}</span>
          <button type="button" class="mini-button" [disabled]="currentPage() >= totalPages() - 1" (click)="goToPage(currentPage() + 1)">
            Siguiente →
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

    /* Pedido clavado */
    .queue-section--pinned {
      background: linear-gradient(180deg, #fff9f0, #fff3e6);
      border: 2px solid #c9a66b;
      border-radius: 8px;
      padding: 1rem;
    }

    .queue-section--pinned .queue-header {
      background: rgba(201, 166, 107, 0.15);
      padding: 0.75rem 1rem;
      border-radius: 6px;
      margin: -1rem -1rem 1rem -1rem;
    }

    .is-pinned {
      border: 2px solid #c9a66b !important;
      box-shadow: 0 4px 12px rgba(201, 166, 107, 0.3) !important;
    }

    .card-header-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 0.5rem;
    }

    .pin-button {
      display: inline-flex;
      align-items: center;
      gap: 0.35rem;
      background: linear-gradient(180deg, #fdf6ed, #f5e6d3);
      border: 1px solid #d4a574;
      color: #8b5a2b;
      font-size: 0.75rem;
      font-weight: 600;
      padding: 0.35rem 0.7rem;
      border-radius: 4px;
      cursor: pointer;
      transition: all 0.2s ease;
    }

    .pin-button:hover {
      background: linear-gradient(180deg, #f5e6d3, #e8d4c0);
      border-color: #b8956a;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    .pin-button img {
      filter: sepia(0.6) saturate(2) brightness(0.9);
    }

    /* Chip styles for filters */
    .chip {
      display: inline-flex;
      align-items: center;
      gap: 0.4rem;
      padding: 0.5rem 0.85rem;
      border: 1px solid #d4c4b8;
      background: #fff;
      color: #5f3929;
      font-size: 0.8rem;
      font-weight: 500;
      border-radius: 4px;
      cursor: pointer;
      transition: all 150ms ease;
    }

    .chip:hover {
      background: #faf6f2;
      border-color: #b8956a;
    }

    .chip--active {
      background: #f3e0d6;
      border-color: #8a5c46;
      color: #4d2a1d;
      font-weight: 600;
    }

    .chip-row--nowrap {
      flex-wrap: nowrap;
    }

    .filter-toolbar__search {
      flex: 1;
      min-width: 200px;
    }

    .pager-inline {
      display: flex;
      gap: 0.5rem;
      align-items: center;
    }

    .page-chip {
      padding: 0.4rem 0.8rem;
      background: #f3e0d6;
      border-radius: 4px;
      font-weight: 600;
      color: #5f3929;
    }

    .pager__meta--accent {
      color: #8a5c46;
      font-weight: 600;
    }

    .filter-toolbar {
      display: flex;
      gap: 1rem;
      flex-wrap: wrap;
      align-items: center;
      background: linear-gradient(180deg, #fdf9f6, #f9f3ed);
      border: 1px solid #eaddd4;
      border-radius: 6px;
      padding: 1rem;
    }

    .search-box input {
      width: 100%;
      padding: 0.6rem 0.9rem;
      border: 1px solid #d4c4b8;
      border-radius: 4px;
      font-size: 0.95rem;
      background: #fff;
    }

    .search-box input:focus {
      outline: none;
      border-color: #8a5c46;
      box-shadow: 0 0 0 3px rgba(138, 92, 70, 0.1);
    }
  `]
})
export class ProductionPageComponent implements OnInit {
  readonly facade = inject(ProductionFacadeService);
  readonly stages = PRODUCTION_STAGES;
  readonly ESTADO_LABELS = ESTADO_LABELS;
  readonly Math = Math;
  
  // FILTROS Y PAGINACIÓN
  readonly searchQuery = signal('');
  readonly estadoFilter = signal<string>('TODOS');
  readonly estadosFiltro = ['TODOS', 'PENDIENTE', 'PREPARACION', 'DECORACION', 'EMPAQUE', 'FINALIZADO'];
  readonly pageSize = signal<number>(6);
  readonly currentPage = signal<number>(0);
  
  readonly productionPage = computed(() => this.facade.productionPage());
  
  // Todas las producciones filtradas (sin paginar)
  readonly allFilteredProductions = computed(() => {
    const search = this.searchQuery().toLowerCase().trim();
    const estado = this.estadoFilter();
    
    return this.productionPage().content.filter((item) => {
      // Filtro por estado específico (si no es TODOS)
      if (estado !== 'TODOS' && item.status !== estado) return false;
      
      // Filtro por búsqueda (código o nombre de cliente)
      if (search) {
        const matchCode = item.orderCode?.toLowerCase().includes(search);
        const matchClient = item.clientName?.toLowerCase().includes(search);
        if (!matchCode && !matchClient) return false;
      }
      
      return true;
    });
  });
  
  // PEDIDO "CLAVADO" - Se mantiene visible independientemente de filtros/paginación
  readonly pinnedProduction = signal<ProductionSummary | null>(null);
  
  // Lista única de todas las producciones filtradas (sin paginar, sin el clavado)
  readonly allFilteredProductionsList = computed(() => {
    const pinned = this.pinnedProduction();
    return this.allFilteredProductions().filter(
      item => !pinned || item.id !== pinned.id
    );
  });
  
  // Producciones paginadas (lista única, excluye el clavado)
  readonly paginatedProductions = computed(() => {
    const all = this.allFilteredProductionsList();
    const page = this.currentPage();
    const size = this.pageSize();
    const start = page * size;
    return all.slice(start, start + size);
  });
  
  // Separar en activos y finalizados para la vista (después de paginar)
  readonly activeQueue = computed(() => 
    this.paginatedProductions().filter(item => item.status !== "FINALIZADO")
  );
  
  readonly recentFinished = computed(() => 
    this.paginatedProductions().filter(item => item.status === "FINALIZADO")
  );
  
  // Total de páginas basado en datos filtrados
  readonly totalPages = computed(() => {
    const totalItems = this.allFilteredProductionsList().length;
    return Math.ceil(totalItems / this.pageSize()) || 1;
  });

  ngOnInit() {
    this.facade.loadPage();
  }

  // Métodos para cambiar filtros que resetean la página
  updateSearchQuery(value: string) {
    this.searchQuery.set(value);
    this.currentPage.set(0);
  }

  updateEstadoFilter(value: string) {
    this.estadoFilter.set(value);
    this.currentPage.set(0);
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

  previousAction(item: ProductionSummary): ProductionAction | null {
    switch (item.status) {
      case "PENDIENTE":
        return null;
      case "PREPARACION":
        return {
          status: "PENDIENTE",
          label: "Volver a pendiente",
          reason: "Retorna a estado pendiente."
        };
      case "DECORACION":
        return {
          status: "PREPARACION",
          label: "Volver a preparación",
          reason: "Retorna a preparación para correcciones."
        };
      case "EMPAQUE":
        return {
          status: "DECORACION",
          label: "Volver a decoración",
          reason: "Retorna a decoración para ajustes."
        };
      case "FINALIZADO":
        return {
          status: "EMPAQUE",
          label: "Reabrir empaque",
          reason: "Reabre el pedido para correcciones."
        };
    }
  }

  isStageDone(current: ProductionStatusValue, stage: ProductionStatusValue): boolean {
    return this.stages.indexOf(stage) < this.stages.indexOf(current);
  }

  moveProduction(item: ProductionSummary, action: ProductionAction) {
    const pinned = this.pinnedProduction();
    const isPinned = pinned && pinned.id === item.id;
    
    this.facade.updateProductionStatus(
      item.id,
      action.status,
      action.reason,
      `${item.orderCode} pasó a ${this.productionLabel(action.status)}.`
    );
    
    // Si es el pedido fijado, actualizarlo manualmente para reflejar el cambio inmediatamente
    if (isPinned) {
      this.pinnedProduction.set({
        ...pinned,
        status: action.status
      });
    }
  }

  changePageSize(size: string) {
    const newSize = Number(size);
    this.pageSize.set(newSize);
    
    // Calcular la última página válida con el nuevo tamaño
    const totalItems = this.allFilteredProductionsList().length;
    const maxPage = Math.max(0, Math.ceil(totalItems / newSize) - 1);
    
    // Si la página actual es mayor que la máxima, ir a la última válida
    if (this.currentPage() > maxPage) {
      this.currentPage.set(maxPage);
    }
  }

  goToPage(page: number) {
    const maxPage = this.totalPages() - 1;
    const validPage = Math.max(0, Math.min(page, maxPage));
    this.currentPage.set(validPage);
  }

  pageSummary(): string {
    const total = this.allFilteredProductionsList().length;
    if (!total) {
      return "Sin producción para mostrar.";
    }
    const from = this.currentPage() * this.pageSize() + 1;
    const to = Math.min((this.currentPage() + 1) * this.pageSize(), total);
    return `Mostrando ${from}-${to} de ${total} trabajos`;
  }
}
