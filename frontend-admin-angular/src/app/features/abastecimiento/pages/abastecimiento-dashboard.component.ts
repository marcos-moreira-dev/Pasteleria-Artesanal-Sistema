import { CommonModule, DecimalPipe } from "@angular/common";
import { Component, OnInit, computed, inject, signal } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { Router } from "@angular/router";
import { BackofficeStoreService } from "../../../core/store/backoffice-store.service";
import { ADMIN_SURFACE_STYLES } from "../../../shared/ui/admin-surface.styles";

/**
 * @fileoverview Componente Dashboard del Módulo de Abastecimiento
 *
 * Este componente implementa la vista principal del módulo de abastecimiento,
 * mostrando métricas clave, alertas de stock, sugerencias de reposición,
 * movimientos recientes y proveedores con órdenes activas.
 *
 * <h2>CARACTERÍSTICAS PRINCIPALES</h2>
 *
 * <h3>1. KPI Cards</h3>
 * Cuatro tarjetas de métricas principales:
 * <ul>
 *   <li>Items con stock crítico (requieren atención inmediata)</li>
 *   <li>Items bajo el mínimo (planificar reposición)</li>
 *   <li>Órdenes de compra pendientes</li>
 *   <li>Recepciones programadas para hoy</li>
 * </ul>
 *
 * <h3>2. Alertas de Inventario</h3>
 * Lista de items que requieren atención, clasificadas por severidad:
 * <ul>
 *   <li>CRÍTICO: Stock ≤ 50% del mínimo</li>
 *   <li>BAJO: Stock ≤ mínimo</li>
 *   <li>RIESGO: Cobertura < 7 días</li>
 *   <li>BLOQUEO: Stock agotado</li>
 * </ul>
 *
 * <h3>3. Sugerencias de Reposición</h3>
 * Recomendaciones automáticas basadas en:
 * <ul>
 *   <li>Stock actual vs mínimo</li>
 *   <li>Consumo histórico</li>
 *   <li>Proveedor principal con mejor precio</li>
 * </ul>
 *
 * <h3>4. Movimientos Recientes</h3>
 * Historial de los últimos movimientos de inventario con:
 * <ul>
 *   <li>Visualización de entradas (verde) y salidas (rojo)</li>
 *   <li>Fecha, tipo, item y cantidad</li>
 *   <li>Navegación al detalle del item</li>
 * </ul>
 *
 * <h2>ARQUITECTURA DE ESTADO</h2>
 *
 * <h3>Signals (Angular 16+)</h3>
 * Se usan signals para estado reactivo granular:
 * <pre>
 * // Señal de estado
 * readonly loading = signal(true);
 *
 * // Señal computada (derivada)
 * readonly dashboard = computed(() => this.store.abastecimientoDashboard());
 *
 * // Señal computada con transformación
 * readonly metrics = computed(() => this.store.abastecimientoMetrics());
 * </pre>
 *
 * <h3>Store Pattern</h3>
 * El componente no mantiene estado local complejo, sino que delega
 * al {@link BackofficeStoreService} que centraliza:
 * <ul>
 *   <li>Caché de datos</li>
 *   <li>Llamadas a API</li>
 *   <li>Estado de carga</li>
 *   <li>Errores</li>
 * </ul>
 *
 * <h2>NAVEGACIÓN</h2>
 *
 * Las tarjetas KPI son clickeables y navegan a:
 * <ul>
 *   <li>Alertas críticas → Inventario con filtro "crítico"</li>
 *   <li>Órdenes pendientes → Lista de órdenes de compra</li>
 *   <li>Recepciones → Pantalla de recepciones</li>
 * </ul>
 *
 * <h2>PERFORMANCE</h2>
 *
 * <h3>Optimizaciones implementadas</h3>
 * <ul>
 *   <li>OnPush change detection (implícito en standalone components)</li>
 *   <li>Signals para actualizaciones eficientes</li>
 *   <li>Lazy loading del módulo</li>
 *   <li>Skeleton loading states</li>
 * </ul>
 *
 * <h2>ESTILOS</h2>
 *
 * Usa el sistema de diseño ADMIN_SURFACE_STYLES que proporciona:
 * <ul>
 *   <li>Variables CSS consistentes</li>
 *   <li>Componentes surface-card, surface-header</li>
 *   <li>Paleta de colores de la marca</li>
 *   <li>Responsive design</li>
 * </ul>
 *
 * <h2>FLUJO DE DATOS</h2>
 * <pre>
 * ngOnInit()
 *   ↓
 * store.loadAbastecimientoDashboard()  →  HTTP GET /api/v1/abastecimiento/dashboard
 *   ↓
 * store.abastecimientoDashboard()  →  Signal actualizada
 *   ↓
 * computed()  →  Transformación de datos
 *   ↓
 * Template  →  Renderizado reactivo
 * </pre>
 *
 * @example
 * ```typescript
 * // Uso del componente
 * <app-abastecimiento-dashboard />
 *
 * // En routing
 * {
 *   path: 'abastecimiento',
 *   component: AbastecimientoShellComponent,
 *   children: [
 *     { path: '', component: AbastecimientoDashboardComponent }
 *   ]
 * }
 * ```
 *
 * @see BackofficeStoreService
 * @see AbastecimientoShellComponent
 * @author Pastelería Development Team
 */
interface Alert {
  id: string;
  tipo: "CRITICO" | "BAJO" | "RIESGO" | "BLOQUEO";
  itemId: string;
  itemNombre: string;
  itemCodigo: string;
  mensaje: string;
  stockActual: number;
  stockMinimo: number;
  unidad: string;
}

interface Suggestion {
  id: string;
  itemId: string;
  itemNombre: string;
  itemCodigo: string;
  stockActual: number;
  cantidadSugerida: number;
  unidad: string;
  proveedorId: string;
  proveedorNombre: string;
}

interface Movement {
  id: string;
  fecha: string;
  tipo: "ENTRADA" | "SALIDA" | "AJUSTE" | "TRASLADO";
  itemId: string;
  itemNombre: string;
  cantidad: number;
  saldo: number;
  unidad: string;
}

interface Provider {
  id: string;
  nombre: string;
  telefono: string;
  ordenesActivas: number;
}

interface Dashboard {
  alertas: Alert[];
  sugerenciasReposicion: Suggestion[];
  movimientosRecientes: Movement[];
  proveedoresConOC: Provider[];
}

@Component({
  selector: "app-abastecimiento-dashboard",
  standalone: true,
  imports: [CommonModule, DecimalPipe, ReactiveFormsModule],
  template: `
    <div class="admin-grid">
      <!-- KPI Cards -->
      <section class="kpi-grid">
        <article
          class="kpi-card"
          (click)="navigateToInventory('critico')"
          role="button"
        >
          <p class="kpi-eyebrow">Alertas</p>
          <strong>{{ metrics()?.criticos ?? 0 }}</strong>
          <p class="kpi-copy">Items con stock crítico</p>
          <small>Requieren atención inmediata</small>
        </article>

        <article
          class="kpi-card"
          (click)="navigateToInventory('bajo')"
          role="button"
        >
          <p class="kpi-eyebrow">Preventivo</p>
          <strong>{{ metrics()?.bajoMinimo ?? 0 }}</strong>
          <p class="kpi-copy">Items bajo el mínimo</p>
          <small>Planificar reposición</small>
        </article>

        <article class="kpi-card" (click)="navigateToOrders()" role="button">
          <p class="kpi-eyebrow">Compras</p>
          <strong>{{ metrics()?.ordenesPendientes ?? 0 }}</strong>
          <p class="kpi-copy">Órdenes pendientes</p>
          <small>En seguimiento</small>
        </article>

        <article
          class="kpi-card"
          (click)="navigateToReceptions()"
          role="button"
        >
          <p class="kpi-eyebrow">Recepciones</p>
          <strong>{{ metrics()?.recepcionesHoy ?? 0 }}</strong>
          <p class="kpi-copy">Recepciones hoy</p>
          <small>Mercadería esperada</small>
        </article>
      </section>

      <!-- Two Column Layout -->
      <div class="admin-grid--split">
        <!-- Left Column: Alertas -->
        <section class="surface-card">
          <header class="surface-header">
            <p class="surface-kicker">Riesgos y alertas</p>
            <div class="surface-title-row">
              <img
                src="assets/icons/abastecimiento/alert-triangle.svg"
                alt=""
                width="28"
                height="28"
              />
              <h3>Alertas de inventario</h3>
              <span class="summary-chip" *ngIf="alertasFiltradas().length">
                {{ alertasFiltradas().length }}
              </span>
            </div>
            
            <!-- FILTROS -->
            <div class="chip-row" style="margin-top: 0.75rem;">
              <button
                class="chip"
                [class.chip--active]="alertasFilter() === 'TODAS'"
                (click)="setAlertasFilter('TODAS')"
              >
                Todas
              </button>
              <button
                class="chip"
                [class.chip--active]="alertasFilter() === 'CRITICO'"
                (click)="setAlertasFilter('CRITICO')"
                style="color: #b71c1c;"
              >
                Crítico
              </button>
              <button
                class="chip"
                [class.chip--active]="alertasFilter() === 'BAJO'"
                (click)="setAlertasFilter('BAJO')"
                style="color: #f57f17;"
              >
                Bajo
              </button>
            </div>
          </header>

          <div
            class="list"
            *ngIf="alertasPaginadas().length; else emptyAlerts"
          >
            <div class="list-row" *ngFor="let alert of alertasPaginadas()">
              <div class="list-row__main">
                <div class="list-row__heading">
                  <strong>{{ alert.itemNombre }}</strong>
                  <span
                    class="pill"
                    [style.background]="getAlertColor(alert.tipo)"
                    >{{ alert.tipo }}</span
                  >
                </div>
                <p>{{ alert.itemCodigo }}</p>
                <small>{{ alert.mensaje }}</small>
              </div>
              <div class="align-end">
                <strong>{{ alert.cantidadActual | number: "1.0-0" }}</strong>
                <p>Stock actual</p>
                <small>Mín: {{ alert.cantidadMinima | number: "1.0-0" }}</small>
              </div>
            </div>
          </div>

          <!-- PAGINACIÓN -->
          <div
            class="pagination-row"
            *ngIf="alertasTotalPages() > 1"
            style="display: flex; justify-content: space-between; align-items: center; padding: 1rem 0; border-top: 1px solid #f1e8e2; margin-top: 0.5rem;"
          >
            <small style="color: #7a6054;">
              Mostrando {{ alertasPage() * alertasPageSize() + 1 }} - 
              {{ Math.min((alertasPage() + 1) * alertasPageSize(), alertasFiltradas().length) }} 
              de {{ alertasFiltradas().length }} alertas
            </small>
            <div class="chip-row" style="gap: 0.5rem;">
              <button
                class="mini-button"
                [disabled]="alertasPage() === 0"
                (click)="alertasPage.set(alertasPage() - 1)"
              >
                ← Anterior
              </button>
              <span style="padding: 0.5rem 1rem; background: #f3e0d6; border-radius: 4px; font-size: 0.85rem; color: #5f3929;">
                {{ alertasPage() + 1 }} / {{ alertasTotalPages() }}
              </span>
              <button
                class="mini-button"
                [disabled]="alertasPage() >= alertasTotalPages() - 1"
                (click)="alertasPage.set(alertasPage() + 1)"
              >
                Siguiente →
              </button>
            </div>
          </div>

          <ng-template #emptyAlerts>
            <div class="empty-state">
              <p>No hay alertas activas</p>
              <small>Tu inventario está en óptimas condiciones</small>
            </div>
          </ng-template>
        </section>

        <!-- Right Column: Suggestions + Movements -->
        <div class="stack-sections">
          <!-- Sugerencias -->
          <section class="surface-card">
            <header class="surface-header">
              <p class="surface-kicker">Recomendaciones</p>
              <div class="surface-title-row">
                <img
                  src="assets/icons/abastecimiento/trending-up.svg"
                  alt=""
                  width="28"
                  height="28"
                />
                <h3>Reposición sugerida</h3>
              </div>
            </header>

            <div
              class="list"
              *ngIf="
                sugerenciasPaginadas().length;
                else emptySuggestions
              "
            >
              <div
                class="list-row"
                *ngFor="let suggestion of sugerenciasPaginadas()"
              >
                <div class="list-row__main">
                  <div class="list-row__heading">
                    <strong>{{ suggestion.itemNombre }}</strong>
                    <span class="pill pill--success"
                      >+{{
                        suggestion.cantidadSugerida | number: "1.1-3"
                      }}</span
                    >
                  </div>
                  <p>Stock: {{ suggestion.stockActual | number: "1.1-3" }}</p>
                </div>
                <div class="align-end">
                  <button
                    class="mini-button"
                    (click)="createOrder('' + suggestion.itemId)"
                  >
                    Crear OC
                  </button>
                </div>
              </div>
            </div>

            <!-- PAGINACIÓN SUGERENCIAS -->
            <div
              class="pagination-row"
              *ngIf="sugerenciasTotalPages() > 1"
              style="display: flex; justify-content: space-between; align-items: center; padding: 1rem 0; border-top: 1px solid #f1e8e2; margin-top: 0.5rem;"
            >
              <small style="color: #7a6054;">
                {{ sugerenciasPage() * sugerenciasPageSize() + 1 }} - 
                {{ Math.min((sugerenciasPage() + 1) * sugerenciasPageSize(), sugerenciasFiltradas().length) }} 
                de {{ sugerenciasFiltradas().length }}
              </small>
              <div class="chip-row" style="gap: 0.5rem;">
                <button
                  class="mini-button"
                  [disabled]="sugerenciasPage() === 0"
                  (click)="sugerenciasPage.set(sugerenciasPage() - 1)"
                >
                  ←
                </button>
                <span style="padding: 0.5rem 1rem; background: #f3e0d6; border-radius: 4px; font-size: 0.85rem; color: #5f3929;">
                  {{ sugerenciasPage() + 1 }} / {{ sugerenciasTotalPages() }}
                </span>
                <button
                  class="mini-button"
                  [disabled]="sugerenciasPage() >= sugerenciasTotalPages() - 1"
                  (click)="sugerenciasPage.set(sugerenciasPage() + 1)"
                >
                  →
                </button>
              </div>
            </div>

            <ng-template #emptySuggestions>
              <div class="empty-state">
                <p>No hay sugerencias de reposición</p>
              </div>
            </ng-template>
          </section>

          <!-- Movimientos -->
          <section class="surface-card">
            <header class="surface-header">
              <p class="surface-kicker">Actividad reciente</p>
              <div class="surface-title-row">
                <img
                  src="assets/icons/abastecimiento/history.svg"
                  alt=""
                  width="28"
                  height="28"
                />
                <h3>Últimos movimientos</h3>
              </div>
            </header>

            <div
              class="table-shell"
              *ngIf="
                dashboard()?.movimientosRecientes?.length;
                else emptyMovements
              "
            >
              <table class="surface-table">
                <thead>
                  <tr>
                    <th>Fecha</th>
                    <th>Tipo</th>
                    <th>Item</th>
                    <th class="text-right">Cant.</th>
                  </tr>
                </thead>
                <tbody>
                  <tr
                    *ngFor="
                      let mov of dashboard()!.movimientosRecientes.slice(0, 5)
                    "
                  >
                    <td>{{ mov.fechaMovimiento | date: "dd/MM/yy" }}</td>
                    <td>
                      <span
                        class="pill"
                        [class]="
                          'pill--' + getMovementClass(mov.tipoMovimiento)
                        "
                      >
                        {{ formatTipoMovimiento(mov.tipoMovimiento) }}
                      </span>
                    </td>
                    <td>{{ mov.itemNombre }}</td>
                    <td
                      class="text-right"
                      [class]="getCantidadClass(mov.tipoMovimiento)"
                    >
                      {{ mov.cantidad > 0 ? "+" : ""
                      }}{{ mov.cantidad | number: "1.1-3" }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

            <ng-template #emptyMovements>
              <div class="empty-state">
                <p>Sin movimientos recientes</p>
              </div>
            </ng-template>
          </section>
        </div>
      </div>

      <!-- Proveedores con OC Activas -->
      <section class="surface-card">
        <header class="surface-header">
          <p class="surface-kicker">Relaciones comerciales</p>
          <div class="surface-title-row">
            <img
              src="assets/icons/abastecimiento/store.svg"
              alt=""
              width="28"
              height="28"
            />
            <h3>Proveedores con órdenes activas</h3>
          </div>
        </header>

        <div
          class="cards-grid"
          *ngIf="
            dashboard()?.proveedoresConOCActivas?.length;
            else emptyProviders
          "
        >
          <article
            *ngFor="let provider of dashboard()!.proveedoresConOCActivas"
            class="provider-card"
            (click)="viewProviderOrders('' + provider.id)"
            role="button"
          >
            <img
              src="assets/icons/abastecimiento/store.svg"
              alt=""
              width="24"
              height="24"
            />
            <h4>{{ provider.nombre }}</h4>
            <p>{{ provider.telefono }}</p>
            <div class="provider-stats">
              <strong>{{ provider.ordenesActivas }}</strong>
              <span>OC activas</span>
            </div>
          </article>
        </div>

        <ng-template #emptyProviders>
          <div class="empty-state">
            <p>No hay proveedores con órdenes activas</p>
          </div>
        </ng-template>
      </section>
    </div>
  `,
  styles: [
    ADMIN_SURFACE_STYLES,
    `
      .kpi-grid {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 1rem;
      }

      .kpi-card {
        padding: 1.2rem;
        border-radius: 4px;
        background: linear-gradient(
          180deg,
          rgba(255, 255, 255, 0.95),
          rgba(255, 250, 245, 0.92)
        );
        border: 1px solid #eaded4;
        box-shadow: 0 18px 45px rgba(92, 60, 42, 0.08);
        cursor: pointer;
        transition: all 150ms ease;
      }

      .kpi-card:hover {
        border-color: #c96e4a;
        transform: translateY(-2px);
      }

      .kpi-eyebrow {
        margin: 0 0 0.5rem;
        color: #8a5c46;
        font-size: 0.72rem;
        letter-spacing: 0.12em;
        text-transform: uppercase;
      }

      .kpi-card strong {
        display: block;
        font: 700 2.2rem/1
          var(--font-display, "Cormorant Garamond", Georgia, serif);
        color: #2d201a;
      }

      .kpi-card strong.text-danger {
        color: #b71c1c;
      }

      .kpi-copy {
        margin: 0.5rem 0 0;
        color: #7a6054;
        font-size: 0.9rem;
      }

      .kpi-card small {
        display: block;
        margin-top: 0.5rem;
        color: #8a6c60;
        font-size: 0.78rem;
      }

      .stack-sections {
        display: grid;
        gap: 1rem;
      }

      .list {
        display: grid;
        gap: 0.75rem;
      }

      .list-row {
        display: flex;
        justify-content: space-between;
        gap: 1rem;
        padding: 0.85rem 0;
        border-bottom: 1px solid #f1e8e2;
      }

      .list-row:first-child {
        padding-top: 0;
      }

      .list-row:last-child {
        border-bottom: none;
        padding-bottom: 0;
      }

      .list-row__main {
        display: grid;
        gap: 0.25rem;
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
        gap: 0.25rem;
        justify-items: end;
      }

      .align-end strong {
        font: 700 1.1rem/1
          var(--font-display, "Cormorant Garamond", Georgia, serif);
        color: #2d201a;
      }

      .align-end p {
        margin: 0;
        font-size: 0.82rem;
        color: #7a6054;
      }

      .pill {
        display: inline-block;
        padding: 0.25rem 0.55rem;
        border-radius: 2px;
        background: #f3e0d6;
        color: #733b2a;
        font-size: 0.72rem;
        font-weight: 700;
        text-transform: uppercase;
      }

      .pill--success {
        background: #e8f5e9;
        color: #1b5e20;
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
        color: #f57f17;
      }

      .text-positive {
        color: #1b5e20;
        font-weight: 600;
      }

      .text-negative {
        color: #b71c1c;
        font-weight: 600;
      }

      .provider-card {
        display: grid;
        gap: 0.4rem;
        padding: 1rem;
        border-radius: 4px;
        background: #fff9f4;
        border: 1px solid #f0dfd4;
        cursor: pointer;
        transition: all 150ms ease;
      }

      .provider-card:hover {
        border-color: #c96e4a;
      }

      .provider-card img {
        width: 24px;
        height: 24px;
        padding: 0.3rem;
        border-radius: 2px;
        background: rgba(255, 232, 220, 0.9);
      }

      .provider-card h4 {
        margin: 0;
        font: 600 0.95rem/1.2 var(--font-body, "Inter", system-ui, sans-serif);
        color: #2d201a;
      }

      .provider-card p {
        margin: 0;
        font-size: 0.78rem;
        color: #7f6f68;
      }

      .provider-stats {
        display: flex;
        align-items: center;
        gap: 0.4rem;
        margin-top: 0.3rem;
      }

      .provider-stats strong {
        font: 700 1.2rem/1
          var(--font-display, "Cormorant Garamond", Georgia, serif);
        color: #8a5c46;
      }

      .provider-stats span {
        font-size: 0.72rem;
        color: #7f6f68;
        text-transform: uppercase;
      }

      @media (max-width: 1080px) {
        .kpi-grid {
          grid-template-columns: repeat(2, 1fr);
        }
      }

      @media (max-width: 768px) {
        .kpi-grid {
          grid-template-columns: 1fr;
        }
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
    `,
  ],
})
/**
 * Componente principal del dashboard de abastecimiento.
 *
 * Implementa OnInit para cargar datos al inicializarse.
 * Usa Angular Signals (v16+) para estado reactivo.
 */
export class AbastecimientoDashboardComponent implements OnInit {
  /**
   * Store service inyectado.
   * Centraliza el estado y las operaciones del backoffice.
   * @readonly - Propiedad de solo lectura para evitar reasignación
   */
  readonly store = inject(BackofficeStoreService);

  /**
   * Router de Angular inyectado.
   * Usado para navegación programática.
   */
  readonly router = inject(Router);

  /**
   * Signal que indica si el componente está cargando datos.
   * Inicialmente true, se establece a false después de 500ms.
   * Usado para mostrar estados de carga (spinners, skeletons).
   */
  readonly loading = signal(true);

  // PAGINACIÓN Y FILTROS DE ALERTAS
  readonly alertasPage = signal(0);
  readonly alertasPageSize = signal(5);
  readonly alertasFilter = signal<string>('TODAS');

  readonly alertasFiltradas = computed(() => {
    const alertas = this.dashboard()?.alertas || [];
    const filtro = this.alertasFilter();
    if (filtro === 'TODAS') return alertas;
    return alertas.filter(a => a.tipo === filtro);
  });

  readonly alertasPaginadas = computed(() => {
    const alertas = this.alertasFiltradas();
    const page = this.alertasPage();
    const size = this.alertasPageSize();
    const start = page * size;
    return alertas.slice(start, start + size);
  });

  readonly alertasTotalPages = computed(() => {
    const total = this.alertasFiltradas().length;
    return Math.ceil(total / this.alertasPageSize());
  });

  // PAGINACIÓN DE SUGERENCIAS
  readonly sugerenciasPage = signal(0);
  readonly sugerenciasPageSize = signal(5);

  readonly sugerenciasFiltradas = computed(() => {
    return this.dashboard()?.sugerenciasReposicion || [];
  });

  readonly sugerenciasPaginadas = computed(() => {
    const sugerencias = this.sugerenciasFiltradas();
    const page = this.sugerenciasPage();
    const size = this.sugerenciasPageSize();
    const start = page * size;
    return sugerencias.slice(start, start + size);
  });

  readonly sugerenciasTotalPages = computed(() => {
    const total = this.sugerenciasFiltradas().length;
    return Math.ceil(total / this.sugerenciasPageSize());
  });

  /**
   * Ciclo de vida: Inicialización del componente.
   *
   * Carga los datos del dashboard desde el store.
   * Simula un tiempo de carga mínimo para mejor UX (evita flash de contenido).
   *
   * @implements OnInit
   */
  ngOnInit() {
    // Dispara la carga de datos del dashboard
    this.store.loadAbastecimientoDashboard();

    // Simula tiempo de carga para evitar parpadeo en UI
    setTimeout(() => this.loading.set(false), 500);
  }

  /**
   * Signal computada que expone los datos del dashboard.
   * Se actualiza automáticamente cuando el store cambia.
   * @returns AbastecimientoDashboard | null
   */
  readonly dashboard = computed(() => this.store.abastecimientoDashboard());

  // Exponer Math para el template
  readonly Math = Math;

  /**
   * Cambia el filtro de alertas y reinicia la paginación
   */
  setAlertasFilter(filtro: string): void {
    this.alertasFilter.set(filtro);
    this.alertasPage.set(0);
  }

  /**
   * Signal computada que expone las métricas resumidas.
   * Calculada a partir de los datos del dashboard.
   * @returns Object con métricas clave (críticos, bajoMinimo, ordenesPendientes, etc.)
   */
  readonly metrics = computed(() => this.store.abastecimientoMetrics());

  /**
   * Obtiene el color de fondo para una alerta según su tipo.
   *
   * @param tipo - Tipo de alerta (CRITICO, BAJO, RIESGO, BLOQUEO)
   * @returns Código de color hexadecimal
   *
   * @example
   * ```typescript
   * getAlertColor('CRITICO') // returns "#fdecea" (rojo claro)
   * getAlertColor('BAJO')    // returns "#fff8e1" (amarillo claro)
   * ```
   */
  getAlertColor(tipo: string): string {
    const colors: Record<string, string> = {
      CRITICO: "#fdecea", // Rojo claro - stock ≤ 50% mínimo
      BAJO: "#fff8e1", // Amarillo claro - stock ≤ mínimo
      RIESGO: "#e3f2fd", // Azul claro - cobertura < 7 días
      BLOQUEO: "#f3e5f5", // Púrpura claro - stock = 0
    };
    return colors[tipo] || "#f3e0d6";
  }

  /**
   * Clasifica el tipo de movimiento para aplicar estilos CSS.
   *
   * @param tipo - Tipo de movimiento (ENTRADA_*, SALIDA_*)
   * @returns Clase CSS para el badge (entrada, salida, ajuste)
   *
   * @example
   * ```typescript
   * getMovementClass('ENTRADA_COMPRA')  // returns "entrada"
   * getMovementClass('SALIDA_PRODUCCION') // returns "salida"
   * ```
   */
  getMovementClass(tipo: string): string {
    if (tipo.startsWith("ENTRADA")) return "entrada";
    if (tipo.startsWith("SALIDA")) return "salida";
    return "ajuste";
  }

  /**
   * Determina la clase CSS para el color de la cantidad.
   *
   * @param tipo - Tipo de movimiento
   * @returns Clase CSS (text-positive, text-negative, o vacío)
   *
   * Entradas se muestran en verde (positivo),
   * Salidas en rojo (negativo).
   */
  getCantidadClass(tipo: string): string {
    if (tipo.startsWith("ENTRADA")) return "text-positive";
    if (tipo.startsWith("SALIDA")) return "text-negative";
    return "";
  }

  /**
   * Formatea el tipo de movimiento para mostrar en UI.
   *
   * Convierte los códigos técnicos a etiquetas amigables.
   *
   * @param tipo - Código del tipo de movimiento
   * @returns Etiqueta legible en español
   *
   * @example
   * ```typescript
   * formatTipoMovimiento('ENTRADA_COMPRA')      // returns "Compra"
   * formatTipoMovimiento('SALIDA_PRODUCCION')   // returns "Producción"
   * formatTipoMovimiento('TIPO_DESCONOCIDO')    // returns "TIPO_DESCONOCIDO"
   * ```
   */
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

  /**
   * Navega a la pantalla de inventario con filtro aplicado.
   *
   * @param filter - Tipo de filtro a aplicar (critico, bajo, todos)
   *
   * @example
   * ```typescript
   * navigateToInventory('critico') // Navega a /abastecimiento/inventario?estado=critico
   * ```
   */
  navigateToInventory(filter: "critico" | "bajo" | "todos"): void {
    this.router.navigate(["/abastecimiento/inventario"], {
      queryParams: { estado: filter },
    });
  }

  /**
   * Navega a la lista de órdenes de compra.
   */
  navigateToOrders(): void {
    this.router.navigate(["/abastecimiento/ordenes"]);
  }

  /**
   * Navega a la pantalla de recepciones.
   */
  navigateToReceptions(): void {
    this.router.navigate(["/abastecimiento/recepciones"]);
  }

  /**
   * Navega al formulario de nueva orden de compra con un item preseleccionado.
   *
   * @param itemId - ID del item a incluir en la orden
   *
   * @example
   * ```typescript
   * createOrder('123') // Navega a /abastecimiento/ordenes/nueva?item=123
   * ```
   */
  createOrder(itemId: string): void {
    this.router.navigate(["/abastecimiento/ordenes/nueva"], {
      queryParams: { item: itemId },
    });
  }

  /**
   * Navega a la lista de órdenes filtrada por proveedor.
   *
   * @param providerId - ID del proveedor
   *
   * @example
   * ```typescript
   * viewProviderOrders('456') // Navega a /abastecimiento/ordenes?proveedor=456
   * ```
   */
  viewProviderOrders(providerId: string): void {
    this.router.navigate(["/abastecimiento/ordenes"], {
      queryParams: { proveedor: providerId },
    });
  }
}
