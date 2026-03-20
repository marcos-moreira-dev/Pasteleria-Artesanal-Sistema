import { CommonModule, DatePipe } from "@angular/common";
import { Component, DestroyRef, OnInit, computed, inject, signal } from "@angular/core";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";
import { FormBuilder, FormControl, ReactiveFormsModule, Validators } from "@angular/forms";
import { debounceTime, distinctUntilChanged } from "rxjs";
import { ClientsFacadeService } from "./state/clients.facade";
import type { ClientSummary } from "./models/client.models";
import { ADMIN_SURFACE_STYLES } from "../../shared/ui/admin-surface.styles";

@Component({
  selector: "app-clients-page",
  standalone: true,
  imports: [CommonModule, DatePipe, ReactiveFormsModule],
  template: `
    <div class="admin-grid admin-grid--split">
      <section class="surface-card surface-card--tinted">
        <header class="surface-header">
          <p class="surface-kicker">Atención y mostrador</p>
          <div class="surface-title-row">
            <img src="assets/icons/clients.svg" alt="" width="28" height="28" aria-hidden="true" />
            <h3>{{ editingClient() ? "Editar cliente" : "Registrar cliente" }}</h3>
          </div>
          <p class="surface-copy">Ficha comercial para ventas directas, pedidos especiales y seguimiento de celebraciones.</p>
          <div class="chip-row">
            <span class="summary-chip">
              <img src="assets/icons/clients.svg" alt="" aria-hidden="true" />
              {{ clientPage().totalElements }} clientes registrados
            </span>
            <span class="summary-chip" *ngIf="editingClient()">
              <img src="assets/icons/edit.svg" alt="" aria-hidden="true" />
              Edición activa
            </span>
          </div>
        </header>

        <form [formGroup]="form" (ngSubmit)="submit()" class="surface-form">
          <label>Nombre completo<input type="text" formControlName="fullName" /></label>
          <div class="surface-row surface-row--2">
            <label>Telefono<input type="text" formControlName="phone" /></label>
            <label>Correo<input type="email" formControlName="email" /></label>
          </div>
          <label>Observaciones<textarea rows="4" formControlName="notes"></textarea></label>
          <div class="action-row">
            <button type="submit" class="surface-button" [disabled]="form.invalid">
              {{ editingClient() ? "Guardar cambios" : "Guardar cliente" }}
            </button>
            <button type="button" class="mini-button" *ngIf="editingClient()" (click)="resetForm()">
              Cancelar edición
            </button>
          </div>
        </form>
      </section>

      <section class="surface-card">
        <header class="surface-header">
          <p class="surface-kicker">Base de clientes</p>
          <div class="surface-title-row">
            <img src="assets/icons/dashboard.svg" alt="" width="28" height="28" aria-hidden="true" />
            <h3>Clientes registrados</h3>
          </div>
          <p class="table-note">Vista operativa para contacto, antecedentes, correcciones rápidas y limpieza del tablero.</p>
        </header>

        <div class="page-toolbar page-toolbar--search" *ngIf="clientPage().totalElements">
          <label class="search-box">
            <span>Buscar cliente</span>
            <input
              type="search"
              [formControl]="searchControl"
              placeholder="Escribe nombre, correo o teléfono"
              autocomplete="off"
            />
          </label>
          <div class="page-toolbar__meta">
            <p class="pager__meta">{{ pageSummary() }}</p>
            <label class="pager__size">
              Filas por pagina
              <select [value]="clientPage().size" (change)="changePageSize($any($event.target).value)">
                <option value="6">6</option>
                <option value="8">8</option>
                <option value="12">12</option>
              </select>
            </label>
          </div>
        </div>

        <div class="table-shell" *ngIf="clientPage().content.length; else empty">
          <table class="surface-table surface-table--wide">
            <thead>
              <tr>
                <th>Cliente</th>
                <th>Contacto</th>
                <th>Registro</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let client of clientPage().content">
                <td>
                  <strong>{{ client.fullName }}</strong>
                  <p class="surface-meta">{{ client.notes || "Sin observaciones registradas." }}</p>
                </td>
                <td>
                  <strong>{{ client.phone || "Sin teléfono" }}</strong>
                  <p class="surface-meta">{{ client.email || "Sin correo" }}</p>
                </td>
                <td>{{ client.registeredAt | date:"medium" }}</td>
                <td>
                  <div class="action-row action-row--stacked">
                    <button type="button" class="mini-button mini-button--icon" (click)="startEdit(client)">
                      <img src="assets/icons/edit.svg" alt="" aria-hidden="true" />
                      Editar
                    </button>
                    <button type="button" class="mini-button mini-button--danger" (click)="deleteClient(client.id, client.fullName)">
                      Eliminar
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <ng-template #empty>
          <div class="empty-state">Todavía no hay clientes cargados en esta sesión.</div>
        </ng-template>

        <div class="pager" *ngIf="clientPage().totalElements">
          <p class="pager__meta">Pagina {{ clientPage().page + 1 }} de {{ clientPage().totalPages || 1 }}</p>
          <div class="pager__controls">
            <button type="button" class="mini-button" [disabled]="clientPage().first" (click)="goToPage(clientPage().page - 1)">
              Anterior
            </button>
            <button type="button" class="mini-button" [disabled]="clientPage().last" (click)="goToPage(clientPage().page + 1)">
              Siguiente
            </button>
          </div>
        </div>
      </section>
    </div>
  `,
  styles: [ADMIN_SURFACE_STYLES, `
    .page-toolbar--search {
      align-items: end;
      grid-template-columns: minmax(16rem, 1.3fr) minmax(14rem, auto);
      gap: 1rem;
    }

    .page-toolbar__meta {
      display: inline-grid;
      justify-items: end;
      gap: 0.65rem;
    }

    .search-box {
      display: grid;
      gap: 0.45rem;
      min-width: 0;
      color: #6c4b3b;
      font-weight: 600;
    }

    .search-box input {
      min-height: 3rem;
    }

    .table-shell {
      width: 100%;
      overflow-x: auto;
      padding-bottom: 0.2rem;
    }

    .table-shell::-webkit-scrollbar {
      height: 10px;
    }

    .table-shell::-webkit-scrollbar-thumb {
      background: #d4bda9;
    }

    .surface-table--wide {
      min-width: 820px;
    }

    .action-row--stacked {
      justify-content: flex-start;
    }

    .mini-button--icon {
      display: inline-flex;
      align-items: center;
      gap: 0.45rem;
    }

    .mini-button--icon img {
      width: 16px;
      height: 16px;
    }

    .mini-button--danger {
      background: #f7ddd8;
      color: #8a2f2c;
    }

    @media (max-width: 1080px) {
      .page-toolbar--search {
        grid-template-columns: 1fr;
      }

      .page-toolbar__meta {
        justify-items: start;
      }
    }
  `]
})
export class ClientsPageComponent implements OnInit {
  readonly facade = inject(ClientsFacadeService);
  private readonly fb = inject(FormBuilder);
  private readonly destroyRef = inject(DestroyRef);
  readonly editingClient = signal<ClientSummary | null>(null);
  readonly clientPage = computed(() => this.facade.clientsPage());
  readonly searchControl = new FormControl("", { nonNullable: true });

  readonly form = this.fb.nonNullable.group({
    fullName: ["", Validators.required],
    phone: [""],
    email: [""],
    notes: [""]
  });

  ngOnInit() {
    this.facade.loadPage();
    this.searchControl.valueChanges
      .pipe(debounceTime(180), distinctUntilChanged(), takeUntilDestroyed(this.destroyRef))
      .subscribe((query) => {
        this.facade.loadPage(0, this.clientPage().size || 8, query);
      });
  }

  submit() {
    if (this.form.invalid) {
      return;
    }

    const value = this.form.getRawValue();
    const payload = {
      fullName: value.fullName.trim(),
      phone: value.phone.trim() || null,
      email: value.email.trim() || null,
      notes: value.notes.trim() || null
    };

    const editing = this.editingClient();
    if (editing) {
      this.facade.updateClient(editing.id, payload, () => this.resetForm());
      return;
    }

    this.facade.createClient(payload, () => this.resetForm());
  }

  startEdit(client: ClientSummary) {
    this.editingClient.set(client);
    this.form.reset({
      fullName: client.fullName,
      phone: client.phone ?? "",
      email: client.email ?? "",
      notes: client.notes ?? ""
    });
  }

  resetForm() {
    this.editingClient.set(null);
    this.form.reset({ fullName: "", phone: "", email: "", notes: "" });
  }

  deleteClient(clientId: number, fullName: string) {
    if (!window.confirm(`Se eliminará a ${fullName} si no tiene pedidos ni cotizaciones asociadas. ¿Deseas continuar?`)) {
      return;
    }
    this.facade.deleteClient(clientId);
    if (this.editingClient()?.id === clientId) {
      this.resetForm();
    }
  }

  goToPage(page: number) {
    this.facade.loadPage(page, this.clientPage().size, this.searchControl.getRawValue());
  }

  changePageSize(size: string) {
    this.facade.loadPage(0, Number(size), this.searchControl.getRawValue());
  }

  pageSummary(): string {
    const page = this.clientPage();
    if (!page.totalElements) {
      return "Sin registros para mostrar.";
    }
    const from = page.page * page.size + 1;
    const to = page.page * page.size + page.numberOfElements;
    return `Mostrando ${from}-${to} de ${page.totalElements} clientes.`;
  }
}
