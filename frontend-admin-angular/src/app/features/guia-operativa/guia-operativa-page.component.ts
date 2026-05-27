import { CommonModule } from "@angular/common";
import { Component, DestroyRef, OnInit, computed, inject, signal } from "@angular/core";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";
import { catchError, finalize, map } from "rxjs";
import { ApiClientService } from "../../core/api/api-client.service";
import { ADMIN_SURFACE_STYLES } from "../../shared/ui/admin-surface.styles";
import type {
  CasoUsoHubResponse,
  CasoUsoModuloResponse,
  CasoUsoOperativoResponse,
  PasoCasoUsoResponse,
} from "./models/guia-operativa.models";

@Component({
  selector: "app-guia-operativa-page",
  standalone: true,
  imports: [CommonModule],
  template: `
    <section class="surface-card surface-card--tinted guide-hero">
      <header class="surface-header guide-hero__content">
        <p class="surface-kicker">Manual vivo de trabajo</p>
        <div class="surface-title-row">
          <img src="assets/icons/guide.svg" alt="" width="28" height="28" aria-hidden="true" />
          <h3>Guía operativa</h3>
        </div>
        <p class="surface-copy">
          Consulta rápida de procedimientos: elige un área, haz clic en un caso de uso y sigue los pasos concretos del sistema.
        </p>
        <div class="chip-row">
          <span class="summary-chip">
            <img src="assets/icons/guide.svg" alt="" aria-hidden="true" />
            {{ modules().length }} áreas documentadas
          </span>
          <span class="summary-chip">
            <img src="assets/icons/orders.svg" alt="" aria-hidden="true" />
            {{ totalCases() }} guías disponibles
          </span>
          <span class="summary-chip">
            <img src="assets/icons/production.svg" alt="" aria-hidden="true" />
            {{ selectedModule()?.nombre || "Área sin seleccionar" }}
          </span>
        </div>
      </header>

      <div class="guide-actions">
        <button type="button" class="surface-button guide-refresh" [disabled]="loading()" (click)="loadHub()">
          {{ loading() ? "Actualizando..." : "Actualizar guía" }}
        </button>
        <button type="button" class="surface-button surface-button--ghost guide-refresh" [disabled]="downloadingManual()" (click)="downloadManual()">
          {{ downloadingManual() ? "Generando PDF..." : "Descargar manual PDF" }}
        </button>
      </div>
    </section>

    <p class="form-error" *ngIf="errorMessage()">{{ errorMessage() }}</p>

    <div class="loading-panel" *ngIf="loading(); else loadedGuide">
      <span class="loading-dot" aria-hidden="true"></span>
      <p>Cargando la guía operativa de la pastelería...</p>
    </div>

    <ng-template #loadedGuide>
      <section class="guide-layout" *ngIf="modules().length > 0; else emptyGuide">
        <aside class="surface-card guide-sidebar" aria-label="Áreas de la guía operativa">
          <header class="surface-header compact-header">
            <p class="surface-kicker">Áreas</p>
            <h3>Haz clic en el frente de trabajo</h3>
            <p class="surface-copy">Cada área agrupa varios procedimientos operativos. Haz clic en una tarjeta para ver sus casos.</p>
          </header>

          <button
            type="button"
            class="guide-module-button"
            *ngFor="let modulo of modules(); trackBy: trackModule"
            [class.is-active]="selectedModule()?.codigo === modulo.codigo"
            (click)="selectModule(modulo.codigo)"
          >
            <span>
              <strong>{{ modulo.nombre }}</strong>
              <small>{{ modulo.descripcion || "Procedimientos operativos de la pastelería" }}</small>
            </span>
            <em>{{ modulo.casos.length }}</em>
          </button>
        </aside>

        <section class="surface-card guide-workspace">
          <div class="guide-case-strip" *ngIf="selectedModule() as modulo">
            <header class="surface-header compact-header">
              <p class="surface-kicker">Guías de {{ modulo.nombre }}</p>
              <h3>Haz clic en el procedimiento</h3>
              <p class="surface-copy">{{ modulo.descripcion || "Haz clic en una guía para ver objetivo, punto de inicio y pasos." }}</p>
            </header>

            <div class="guide-case-scroll" role="list">
              <button
                type="button"
                class="guide-case-button"
                *ngFor="let caso of modulo.casos; trackBy: trackCase"
                [class.is-active]="selectedCase()?.codigo === caso.codigo"
                (click)="selectCase(caso.codigo)"
              >
                <span>{{ caso.codigo }}</span>
                <strong>{{ caso.titulo }}</strong>
              </button>
            </div>
          </div>

          <article class="guide-detail" *ngIf="selectedCase() as caso">
            <div class="guide-detail__header">
              <div>
                <span class="guide-module-chip">{{ selectedModule()?.nombre || prettyModuleName(caso.modulo) }}</span>
                <p class="surface-kicker code-kicker">{{ caso.codigo }}</p>
                <h2>{{ caso.titulo }}</h2>
              </div>
              <span class="status-pill" [class.status-pill--success]="prettyStatus(caso.estado) === 'Lista'">
                {{ prettyStatus(caso.estado) }} · v{{ caso.version || 1 }}
              </span>
            </div>

            <div class="guide-meta-grid">
              <div>
                <span>Responsable principal</span>
                <strong>{{ caso.actorPrincipal || "Equipo autorizado" }}</strong>
              </div>
              <div>
                <span>Punto de inicio</span>
                <strong>{{ caso.puntoInicio || "Desde el módulo correspondiente" }}</strong>
              </div>
              <div>
                <span>Pasos</span>
                <strong>{{ caso.pasos.length }}</strong>
              </div>
            </div>

            <section class="guide-objective">
              <p class="surface-kicker">Para qué sirve</p>
              <p>{{ caso.objetivo || "Orientar al equipo para completar una operación interna de forma ordenada." }}</p>
            </section>

            <section class="guide-steps">
              <div class="surface-title-row guide-steps-title">
                <img src="assets/icons/guide.svg" alt="" width="28" height="28" aria-hidden="true" />
                <h3>Pasos de trabajo: dónde hacer clic y qué revisar</h3>
              </div>

              <ol *ngIf="caso.pasos.length > 0; else emptySteps">
                <li *ngFor="let paso of caso.pasos; trackBy: trackStep">
                  <span>{{ paso.numero }}</span>
                  <p>{{ paso.descripcion }}</p>
                </li>
              </ol>

              <ng-template #emptySteps>
                <div class="empty-state">Esta guía existe, pero todavía no tiene pasos cargados.</div>
              </ng-template>
            </section>
          </article>
        </section>
      </section>
    </ng-template>

    <ng-template #emptyGuide>
      <section class="empty-state guide-empty">
        <strong>La guía operativa todavía no tiene datos visibles.</strong>
        <p>Verifica que el backend esté encendido y que la base se haya inicializado con los datos canónicos.</p>
        <button type="button" class="mini-button" (click)="loadHub()">Volver a intentar</button>
      </section>
    </ng-template>
  `,
  styles: [ADMIN_SURFACE_STYLES, `
    .guide-hero {
      display: grid;
      grid-template-columns: minmax(0, 1fr) auto;
      align-items: start;
      gap: 1.2rem;
      margin-bottom: 1rem;
    }

    .guide-hero__content {
      margin-bottom: 0;
    }

    .guide-actions {
      display: flex;
      flex-wrap: wrap;
      justify-content: flex-end;
      gap: 0.7rem;
    }

    .guide-refresh {
      min-width: 172px;
      justify-content: center;
    }

    .surface-button--ghost {
      background: #fffaf6;
      color: #713b2a;
      border: 1px solid #e7d4c7;
    }

    .guide-layout {
      display: grid;
      grid-template-columns: minmax(250px, 0.34fr) minmax(0, 0.66fr);
      gap: 1rem;
      align-items: start;
    }

    .compact-header {
      margin-bottom: 0.85rem;
    }

    .compact-header h3,
    .guide-detail h2 {
      margin: 0;
      color: #2d201a;
      font: 700 1.35rem/1.1 var(--font-display, "Cormorant Garamond", Georgia, serif);
    }

    .guide-sidebar,
    .guide-workspace {
      min-width: 0;
    }

    .guide-sidebar {
      display: grid;
      gap: 0.7rem;
      position: sticky;
      top: 1rem;
    }

    .guide-module-button,
    .guide-case-button {
      border: 1px solid #eadbd0;
      border-radius: 4px;
      background: #fffaf6;
      color: #3b2b24;
      cursor: pointer;
      text-align: left;
      transition: transform 140ms ease, border-color 140ms ease, background 140ms ease, box-shadow 140ms ease;
    }

    .guide-module-button:hover,
    .guide-case-button:hover,
    .guide-module-button.is-active,
    .guide-case-button.is-active {
      border-color: #c96e4a;
      background: #fff2ea;
      box-shadow: 0 12px 24px rgba(92, 60, 42, 0.08);
      transform: translateY(-1px);
    }

    .guide-module-button {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 0.8rem;
      padding: 0.85rem;
    }

    .guide-module-button span {
      display: grid;
      gap: 0.25rem;
    }

    .guide-module-button strong,
    .guide-case-button strong {
      color: #2d201a;
    }

    .guide-module-button small {
      color: #7b6b62;
      line-height: 1.35;
    }

    .guide-module-button em {
      min-width: 2.1rem;
      height: 2.1rem;
      display: inline-grid;
      place-items: center;
      border-radius: 50%;
      background: #f1ded1;
      color: #713b2a;
      font-style: normal;
      font-weight: 800;
    }

    .guide-workspace {
      display: grid;
      gap: 1rem;
    }

    .guide-case-strip {
      display: grid;
      gap: 0.85rem;
      padding-bottom: 1rem;
      border-bottom: 1px solid #f0e4da;
    }

    .guide-case-scroll {
      display: flex;
      gap: 0.65rem;
      overflow-x: auto;
      padding-bottom: 0.25rem;
    }

    .guide-case-button {
      flex: 0 0 230px;
      display: grid;
      gap: 0.4rem;
      padding: 0.8rem;
    }

    .guide-case-button span,
    .code-kicker {
      font-family: "SFMono-Regular", Consolas, "Liberation Mono", monospace;
    }

    .guide-case-button span {
      color: #8a5c46;
      font-size: 0.72rem;
      font-weight: 800;
      letter-spacing: 0.08em;
    }

    .guide-detail {
      display: grid;
      gap: 1rem;
    }

    .guide-detail__header {
      display: flex;
      align-items: start;
      justify-content: space-between;
      gap: 1rem;
    }

    .guide-module-chip {
      display: inline-flex;
      width: fit-content;
      margin-bottom: 0.55rem;
      padding: 0.3rem 0.65rem;
      border-radius: 999px;
      background: #f3e0d6;
      color: #733b2a;
      font-size: 0.78rem;
      font-weight: 800;
    }

    .guide-meta-grid {
      display: grid;
      grid-template-columns: repeat(3, minmax(0, 1fr));
      gap: 0.75rem;
    }

    .guide-meta-grid div,
    .guide-objective,
    .guide-steps {
      padding: 1rem;
      border: 1px solid #f0e4da;
      border-radius: 4px;
      background: #fffaf6;
    }

    .guide-meta-grid span {
      display: block;
      margin-bottom: 0.35rem;
      color: #8a5c46;
      font-size: 0.76rem;
      text-transform: uppercase;
      letter-spacing: 0.08em;
    }

    .guide-meta-grid strong {
      color: #2d201a;
      line-height: 1.35;
    }

    .guide-objective p:last-child {
      margin: 0;
      color: #5f514a;
      line-height: 1.65;
    }

    .guide-steps-title {
      margin-bottom: 0.85rem;
    }

    .guide-steps ol {
      list-style: none;
      display: grid;
      gap: 0.75rem;
      margin: 0;
      padding: 0;
    }

    .guide-steps li {
      display: grid;
      grid-template-columns: auto minmax(0, 1fr);
      gap: 0.8rem;
      align-items: start;
      padding: 0.9rem;
      border-radius: 4px;
      background: #fff4ec;
    }

    .guide-steps li span {
      width: 2rem;
      height: 2rem;
      display: inline-grid;
      place-items: center;
      border-radius: 50%;
      background: linear-gradient(135deg, #4f2519, #2f1912);
      color: #fff;
      font-weight: 800;
    }

    .guide-steps li p {
      margin: 0;
      color: #493a32;
      line-height: 1.55;
    }

    .loading-panel,
    .guide-empty {
      display: grid;
      place-items: center;
      gap: 0.75rem;
      text-align: center;
      min-height: 220px;
    }

    .loading-dot {
      width: 2.25rem;
      height: 2.25rem;
      border: 4px solid #efd9ca;
      border-top-color: #8a3f2c;
      border-radius: 50%;
      animation: spin 900ms linear infinite;
    }

    .status-pill--success {
      background: #e2efd8;
      color: #4d6b33;
    }

    @keyframes spin {
      to { transform: rotate(360deg); }
    }

    @media (max-width: 980px) {
      .guide-hero,
      .guide-layout,
      .guide-meta-grid {
        grid-template-columns: 1fr;
      }

      .guide-sidebar {
        position: static;
      }

      .guide-detail__header {
        display: grid;
      }
    }
  `]
})
export class GuiaOperativaPageComponent implements OnInit {
  private readonly api = inject(ApiClientService);
  private readonly destroyRef = inject(DestroyRef);

  readonly loading = signal(false);
  readonly errorMessage = signal<string | null>(null);
  readonly downloadingManual = signal(false);
  readonly hub = signal<CasoUsoHubResponse | null>(null);
  readonly selectedModuleCode = signal<string | null>(null);
  readonly selectedCaseCode = signal<string | null>(null);

  readonly modules = computed(() => this.hub()?.modulos ?? []);
  readonly totalCases = computed(() => this.modules().reduce((total, modulo) => total + modulo.casos.length, 0));
  readonly selectedModule = computed(() => {
    const modules = this.modules();
    if (modules.length === 0) {
      return null;
    }

    const currentCode = this.selectedModuleCode();
    return modules.find((modulo) => modulo.codigo === currentCode) ?? modules[0];
  });
  readonly selectedCase = computed(() => {
    const modulo = this.selectedModule();
    if (!modulo || modulo.casos.length === 0) {
      return null;
    }

    const currentCode = this.selectedCaseCode();
    return modulo.casos.find((caso) => caso.codigo === currentCode) ?? modulo.casos[0];
  });

  ngOnInit(): void {
    this.loadHub();
  }

  loadHub(): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    this.api.getGuiaOperativaHub()
      .pipe(
        catchError(() => this.api.getGuiaOperativaCasos().pipe(
          map((casos) => this.buildHubFromFlatCases(casos)),
        )),
        map((hub) => this.normalizeHub(hub)),
        finalize(() => this.loading.set(false)),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe({
        next: (hub) => this.applyHub(hub),
        error: (error: unknown) => this.errorMessage.set(this.toUserMessage(error)),
      });
  }

  selectModule(codigo: string): void {
    this.selectedModuleCode.set(codigo);
    const modulo = this.modules().find((item) => item.codigo === codigo);
    this.selectedCaseCode.set(modulo?.casos[0]?.codigo ?? null);
  }

  selectCase(codigo: string): void {
    this.selectedCaseCode.set(codigo);
  }

  downloadManual(): void {
    this.downloadingManual.set(true);
    this.errorMessage.set(null);

    this.api.downloadGuiaOperativaManual()
      .pipe(
        finalize(() => this.downloadingManual.set(false)),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe({
        next: (blob) => this.saveBlob(blob, "guia-operativa-pasteleria.pdf"),
        error: (error: unknown) => this.errorMessage.set(this.toUserMessage(error)),
      });
  }

  trackModule(_index: number, modulo: CasoUsoModuloResponse): string {
    return modulo.codigo;
  }

  trackCase(_index: number, caso: CasoUsoOperativoResponse): string {
    return caso.codigo;
  }

  trackStep(_index: number, paso: PasoCasoUsoResponse): number {
    return paso.numero;
  }

  prettyStatus(status: string | null): string {
    const value = (status || "LISTO").toUpperCase();
    const labels: Record<string, string> = {
      LISTO: "Lista",
      BORRADOR: "Borrador",
      REVISION: "En revisión",
      INACTIVO: "Inactiva",
    };
    return labels[value] ?? value.toLowerCase().replace(/_/g, " ");
  }

  prettyModuleName(code: string): string {
    return code
      .toLowerCase()
      .replace(/_/g, " ")
      .replace(/(^|\s)([a-záéíóúñ])/g, (_match, separator: string, letter: string) => `${separator}${letter.toUpperCase()}`);
  }

  private applyHub(hub: CasoUsoHubResponse): void {
    this.hub.set(hub);
    const selectedModule = hub.modulos.find((modulo) => modulo.codigo === this.selectedModuleCode()) ?? hub.modulos[0] ?? null;
    this.selectedModuleCode.set(selectedModule?.codigo ?? null);
    const selectedCase = selectedModule?.casos.find((caso) => caso.codigo === this.selectedCaseCode()) ?? selectedModule?.casos[0] ?? null;
    this.selectedCaseCode.set(selectedCase?.codigo ?? null);
  }

  private buildHubFromFlatCases(casos: CasoUsoOperativoResponse[]): CasoUsoHubResponse {
    const grouped = new Map<string, CasoUsoOperativoResponse[]>();
    for (const caso of casos.filter((item) => item.activo !== false)) {
      const moduleCode = caso.modulo || "OPERACION";
      grouped.set(moduleCode, [...(grouped.get(moduleCode) ?? []), caso]);
    }

    const modulos = [...grouped.entries()].map(([codigo, moduleCases], index) => ({
      codigo,
      nombre: this.prettyModuleName(codigo),
      descripcion: "Guías disponibles desde el endpoint compatible.",
      grupo: "OPERACION",
      ordenVisual: index + 1,
      casos: moduleCases,
    }));

    return {
      totalCasos: modulos.reduce((total, modulo) => total + modulo.casos.length, 0),
      modulos,
    };
  }

  private normalizeHub(incoming: CasoUsoHubResponse): CasoUsoHubResponse {
    const modules = (incoming.modulos ?? [])
      .map((modulo) => this.normalizeModule(modulo))
      .filter((modulo) => modulo.casos.length > 0);
    modules.sort((left, right) => (left.ordenVisual ?? 999) - (right.ordenVisual ?? 999) || left.codigo.localeCompare(right.codigo));

    return {
      totalCasos: modules.reduce((total, modulo) => total + modulo.casos.length, 0),
      modulos: modules,
    };
  }

  private normalizeModule(modulo: CasoUsoModuloResponse): CasoUsoModuloResponse {
    const casos = (modulo.casos ?? [])
      .filter((caso) => caso.activo !== false)
      .map((caso) => this.normalizeCase(caso));
    casos.sort((left, right) => (left.ordenVisual ?? 999) - (right.ordenVisual ?? 999) || left.codigo.localeCompare(right.codigo));

    return {
      ...modulo,
      nombre: modulo.nombre || this.prettyModuleName(modulo.codigo),
      descripcion: modulo.descripcion || "Procedimientos internos listos para consulta del equipo.",
      grupo: modulo.grupo || "OPERACION",
      ordenVisual: modulo.ordenVisual ?? 999,
      casos,
    };
  }

  private normalizeCase(caso: CasoUsoOperativoResponse): CasoUsoOperativoResponse {
    return {
      ...caso,
      actorPrincipal: caso.actorPrincipal || "Equipo autorizado",
      objetivo: caso.objetivo || "Orientar al equipo para completar una operación interna de forma ordenada.",
      puntoInicio: caso.puntoInicio || "Desde el módulo correspondiente",
      ordenVisual: caso.ordenVisual ?? 999,
      estado: caso.estado || "LISTO",
      version: caso.version ?? 1,
      activo: caso.activo !== false,
      pasos: this.normalizeSteps(caso.pasos),
    };
  }

  private normalizeSteps(pasos: PasoCasoUsoResponse[] | null | undefined): PasoCasoUsoResponse[] {
    return [...(pasos ?? [])]
      .filter((paso) => !!paso.descripcion)
      .sort((left, right) => left.numero - right.numero)
      .map((paso, index) => ({
        numero: paso.numero || index + 1,
        descripcion: paso.descripcion,
      }));
  }

  private saveBlob(blob: Blob, filename: string): void {
    const url = URL.createObjectURL(blob);
    const anchor = document.createElement("a");
    anchor.href = url;
    anchor.download = filename;
    anchor.click();
    URL.revokeObjectURL(url);
  }

  private toUserMessage(error: unknown): string {
    if (error instanceof Error && error.message) {
      return error.message;
    }
    return "No se pudo cargar la guía operativa. Verifica el backend local e intenta de nuevo.";
  }
}
