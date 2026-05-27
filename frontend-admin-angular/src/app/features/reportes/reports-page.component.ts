import { CommonModule, DatePipe } from "@angular/common";
import { Component, DestroyRef, OnInit, computed, inject, signal } from "@angular/core";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";
import { interval, startWith } from "rxjs";
import { ReportsFacadeService } from "./state/reports.facade";
import type { ReportJobSummary } from "./models/report.models";
import { ADMIN_SURFACE_STYLES } from "../../shared/ui/admin-surface.styles";

@Component({
  selector: "app-reports-page",
  standalone: true,
  imports: [CommonModule, DatePipe],
  template: `
    <div class="admin-grid admin-grid--split">
      <section class="surface-card surface-card--tinted">
        <header class="surface-header">
          <p class="surface-kicker">Trabajo diferido</p>
          <div class="surface-title-row">
            <img src="assets/icons/reports.svg" alt="" width="28" height="28" aria-hidden="true" />
            <h3>Centro de reportes</h3>
          </div>
          <p class="surface-copy">Solicita resúmenes del negocio o cortes de producción sin bloquear el trabajo del mostrador.</p>
          <div class="chip-row">
            <span class="summary-chip">
              <img src="assets/icons/reports.svg" alt="" aria-hidden="true" />
              {{ reportPage().totalElements }} jobs acumulados
            </span>
            <span class="summary-chip">
              <img src="assets/icons/notifications.svg" alt="" aria-hidden="true" />
              {{ completedReportsCount() }} completados
            </span>
          </div>
        </header>

        <div class="cards-grid cards-grid--actions">
          <article>
            <img src="assets/icons/dashboard.svg" alt="" aria-hidden="true" />
            <h4>Resumen de negocio</h4>
            <p class="card-copy">Cierra clientes, catálogo activo, pedidos y facturación proxy del tablero.</p>
            <button type="button" class="surface-button" (click)="requestReport('RESUMEN_NEGOCIO')">
              Generar resumen
            </button>
          </article>

          <article>
            <img src="assets/icons/production.svg" alt="" aria-hidden="true" />
            <h4>Cola de producción</h4>
            <p class="card-copy">Entrega un corte de preparación, decoración, empaque y prioridades activas.</p>
            <button type="button" class="surface-button surface-button--secondary" (click)="requestReport('COLA_PRODUCCION')">
              Generar corte
            </button>
          </article>
        </div>
      </section>

      <section class="surface-card reports-shell">
        <header class="surface-header">
          <p class="surface-kicker">Seguimiento del worker</p>
          <div class="surface-title-row">
            <img src="assets/icons/reports.svg" alt="" width="28" height="28" aria-hidden="true" />
            <h3>Jobs y descargas</h3>
          </div>
          <p class="table-note">Control visual del estado del worker, reintentos y archivos disponibles para descarga local.</p>
        </header>

        <div class="page-toolbar" *ngIf="reportPage().totalElements">
          <p class="pager__meta">{{ pageSummary() }}</p>
          <div class="page-toolbar__actions">
            <button
              type="button"
              class="mini-button"
              [disabled]="selectedJobIds().length === 0"
              (click)="deleteSelected()"
            >
              Eliminar seleccionados
            </button>
            <button type="button" class="mini-button mini-button--ghost" (click)="cleanupOldReports()">
              Conservar últimas 10
            </button>
            <label class="pager__size">
              Filas por página
              <select [value]="reportPage().size" (change)="changePageSize($any($event.target).value)">
                <option value="6">6</option>
                <option value="8">8</option>
                <option value="12">12</option>
              </select>
            </label>
          </div>
        </div>

        <div class="reports-table-shell" *ngIf="reportPage().content.length; else empty">
          <table class="surface-table">
            <thead>
              <tr>
                <th class="selection-col">
                  <input
                    type="checkbox"
                    [checked]="allVisibleDeletableSelected()"
                    (change)="toggleAllVisible($any($event.target).checked)"
                    aria-label="Seleccionar jobs visibles"
                  />
                </th>
                <th>Job</th>
                <th>Tipo</th>
                <th>Estado</th>
                <th>Solicitado</th>
                <th>Archivo</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let job of reportPage().content">
                <td class="selection-col">
                  <input
                    type="checkbox"
                    [disabled]="!isDeletable(job)"
                    [checked]="isJobSelected(job.id)"
                    (change)="toggleJobSelection(job.id, $any($event.target).checked)"
                    aria-label="Seleccionar job"
                  />
                </td>
                <td>
                  <strong>{{ job.jobCode }}</strong>
                  <p class="surface-meta">{{ job.requestId || "Sin requestId" }}</p>
                </td>
                <td>{{ prettyReportType(job.reportType) }}</td>
                <td>
                  <span class="status-pill" [class.status-pill--success]="job.status === 'COMPLETADO'" [class.status-pill--danger]="job.status === 'ERROR'">
                    {{ prettyReportStatus(job.status) }}
                  </span>
                  <p class="surface-meta" *ngIf="job.errorMessage">{{ job.errorMessage }}</p>
                </td>
                <td>{{ job.requestedAt | date:"medium" }}</td>
                <td>
                  <strong>{{ job.fileName || "Pendiente" }}</strong>
                  <p class="surface-meta">
                    {{
                      job.finishedAt
                        ? ("Terminado " + (job.finishedAt | date:"short"))
                        : (job.startedAt ? "Procesando en worker" : "Esperando ejecución")
                    }}
                  </p>
                </td>
                <td>
                  <div class="action-row">
                    <button
                      type="button"
                      class="mini-button mini-button--icon"
                      *ngIf="job.fileId"
                      (click)="download(job)"
                    >
                      <img src="assets/icons/reports.svg" alt="" aria-hidden="true" />
                      Descargar
                    </button>
                    <span class="surface-meta" *ngIf="!job.fileId">Sin descarga aún</span>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <ng-template #empty>
          <div class="empty-state">Todavía no hay jobs de reporte registrados para esta sesión.</div>
        </ng-template>

        <div class="pager" *ngIf="reportPage().totalElements">
          <p class="pager__meta">Página {{ reportPage().page + 1 }} de {{ reportPage().totalPages || 1 }}</p>
          <div class="pager__controls">
            <button type="button" class="mini-button" [disabled]="reportPage().first" (click)="goToPage(reportPage().page - 1)">
              Anterior
            </button>
            <button type="button" class="mini-button" [disabled]="reportPage().last" (click)="goToPage(reportPage().page + 1)">
              Siguiente
            </button>
          </div>
        </div>
      </section>
    </div>
  `,
  styles: [ADMIN_SURFACE_STYLES, `
    .admin-grid--split {
      align-items: start;
    }

    .reports-shell {
      min-width: 0;
    }

    .cards-grid--actions article {
      display: grid;
      gap: 0.8rem;
      align-content: start;
      min-height: 100%;
      padding: 1.15rem;
      border: 1px solid #efdfd3;
      border-radius: 4px;
      background: rgba(255, 251, 247, 0.92);
    }

    .cards-grid--actions .surface-button {
      margin-top: auto;
      justify-content: center;
    }

    .surface-button--secondary {
      background: linear-gradient(135deg, #7a4a37, #4c291d);
    }

    .page-toolbar {
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;
      align-items: center;
      gap: 0.9rem;
    }

    .page-toolbar__actions {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      gap: 0.7rem;
    }

    .reports-table-shell {
      width: 100%;
      overflow-x: auto;
      overflow-y: hidden;
      padding-bottom: 0.2rem;
    }

    .reports-table-shell::-webkit-scrollbar {
      height: 10px;
    }

    .reports-table-shell::-webkit-scrollbar-thumb {
      background: #d7c0b2;
    }

    .reports-shell .surface-table {
      min-width: 980px;
    }

    .selection-col {
      width: 3rem;
      text-align: center;
    }

    .surface-table input[type="checkbox"] {
      width: 1rem;
      height: 1rem;
      accent-color: #7e402c;
    }

    .mini-button--icon {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      gap: 0.45rem;
      min-width: 8.8rem;
    }

    .mini-button--icon img {
      width: 18px;
      height: 18px;
      flex-shrink: 0;
    }

    .status-pill--success {
      background: #dce8d2;
      color: #48643b;
    }

    .status-pill--danger {
      background: #f2d7d4;
      color: #8a2f2c;
    }

    @media (max-width: 1380px) {
      .admin-grid--split {
        grid-template-columns: 1fr;
      }
    }
  `]
})
export class ReportsPageComponent implements OnInit {
  readonly facade = inject(ReportsFacadeService);
  private readonly destroyRef = inject(DestroyRef);

  readonly reportPage = computed(() => this.facade.reportJobsPage());
  readonly selectedJobIds = signal<number[]>([]);

  readonly allVisibleDeletableSelected = computed(() => {
    const visibleDeletableIds = this.reportPage().content.filter((job) => this.isDeletable(job)).map((job) => job.id);
    return visibleDeletableIds.length > 0 && visibleDeletableIds.every((jobId) => this.selectedJobIds().includes(jobId));
  });

  ngOnInit() {
    interval(2000)
      .pipe(startWith(0), takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.facade.loadPage(this.reportPage().page, this.reportPage().size));
  }

  requestReport(reportType: "RESUMEN_NEGOCIO" | "COLA_PRODUCCION") {
    this.facade.requestReport(reportType);
  }

  download(job: ReportJobSummary) {
    this.facade.downloadReport(job.id, job.fileName);
  }

  goToPage(page: number) {
    this.facade.loadPage(page, this.reportPage().size);
    this.selectedJobIds.set([]);
  }

  changePageSize(size: string) {
    this.facade.loadPage(0, Number(size));
    this.selectedJobIds.set([]);
  }

  toggleJobSelection(jobId: number, checked: boolean) {
    this.selectedJobIds.update((current) => {
      if (checked) {
        return current.includes(jobId) ? current : [...current, jobId];
      }
      return current.filter((id) => id !== jobId);
    });
  }

  toggleAllVisible(checked: boolean) {
    const visibleDeletableIds = this.reportPage().content.filter((job) => this.isDeletable(job)).map((job) => job.id);
    this.selectedJobIds.set(checked ? visibleDeletableIds : []);
  }

  isJobSelected(jobId: number) {
    return this.selectedJobIds().includes(jobId);
  }

  isDeletable(job: ReportJobSummary) {
    return ["COMPLETADO", "ERROR", "CANCELADO", "EXPIRADO"].includes(job.status);
  }

  deleteSelected() {
    const selectedIds = this.selectedJobIds();
    if (selectedIds.length === 0) {
      return;
    }
    this.facade.deleteSelectedReports(selectedIds);
    this.selectedJobIds.set([]);
  }

  cleanupOldReports() {
    this.facade.cleanupOldReports(10);
    this.selectedJobIds.set([]);
  }

  pageSummary(): string {
    const page = this.reportPage();
    if (!page.totalElements) {
      return "Sin registros para mostrar.";
    }
    const from = page.page * page.size + 1;
    const to = page.page * page.size + page.numberOfElements;
    return `Mostrando ${from}-${to} de ${page.totalElements} jobs de reporte.`;
  }

  completedReportsCount() {
    return this.reportPage().content.filter((job) => job.status === "COMPLETADO").length;
  }

  prettyReportType(type: ReportJobSummary["reportType"]) {
    return type === "COLA_PRODUCCION" ? "Cola de producción" : "Resumen de negocio";
  }

  prettyReportStatus(status: ReportJobSummary["status"]) {
    switch (status) {
      case "PENDIENTE":
        return "Pendiente";
      case "EN_PROCESO":
        return "En proceso";
      case "COMPLETADO":
        return "Completado";
      case "ERROR":
        return "Error";
      case "CANCELADO":
        return "Cancelado";
      case "EXPIRADO":
        return "Expirado";
    }
  }
}
