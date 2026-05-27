import { CommonModule } from "@angular/common";
import { Component, computed, inject, signal } from "@angular/core";
import {
  Router,
  RouterLink,
  RouterLinkActive,
  RouterOutlet,
} from "@angular/router";
import { AuthService } from "../../core/auth/auth.service";
import { ShellFacadeService } from "./shell-facade.service";
import { getBackendBrandingAsset } from "../../shared/utils/backend-asset.util";
import { businessShellConfig } from "../../core/config/business-shell.config";

@Component({
  selector: "app-shell",
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="admin-shell">
      <aside class="admin-sidebar">
        <div class="sidebar-top">
          <section class="brand-panel">
            <div class="brand-lockup">
              <img
                [src]="logoSquareUrl"
                [alt]="shellConfig.brand.logoAlt"
                width="56"
                height="56"
              />
              <div>
                <p class="brand-kicker">{{ shellConfig.brand.kicker }}</p>
                <h1 class="brand-title">{{ shellConfig.brand.title }}</h1>
              </div>
            </div>
            <p class="brand-copy">{{ shellConfig.brand.copy }}</p>
          </section>

          <nav class="admin-nav">
            <a
              routerLink="/"
              [routerLinkActiveOptions]="{ exact: true }"
              routerLinkActive="is-active"
            >
              <img
                src="assets/icons/dashboard.svg"
                alt=""
                width="20"
                height="20"
                aria-hidden="true"
              />
              <span>Resumen</span>
            </a>
            <a routerLink="/clientes" routerLinkActive="is-active">
              <img
                src="assets/icons/clients.svg"
                alt=""
                width="20"
                height="20"
                aria-hidden="true"
              />
              <span>Clientes</span>
            </a>
            <a routerLink="/productos" routerLinkActive="is-active">
              <img
                src="assets/icons/products.svg"
                alt=""
                width="20"
                height="20"
                aria-hidden="true"
              />
              <span>Productos</span>
            </a>
            <a routerLink="/cotizaciones" routerLinkActive="is-active">
              <img
                src="assets/icons/quotations.svg"
                alt=""
                width="20"
                height="20"
                aria-hidden="true"
              />
              <span>Cotizaciones</span>
            </a>
            <a routerLink="/reportes" routerLinkActive="is-active">
              <img
                src="assets/icons/reports.svg"
                alt=""
                width="20"
                height="20"
                aria-hidden="true"
              />
              <span>Reportes</span>
            </a>
            <a routerLink="/pedidos" routerLinkActive="is-active">
              <img
                src="assets/icons/orders.svg"
                alt=""
                width="20"
                height="20"
                aria-hidden="true"
              />
              <span>Pedidos</span>
            </a>
            <a routerLink="/produccion" routerLinkActive="is-active">
              <img
                src="assets/icons/production.svg"
                alt=""
                width="20"
                height="20"
                aria-hidden="true"
              />
              <span>Producción</span>
            </a>
            <a routerLink="/guia-operativa" routerLinkActive="is-active">
              <img
                src="assets/icons/guide.svg"
                alt=""
                width="20"
                height="20"
                aria-hidden="true"
              />
              <span>Guía operativa</span>
            </a>
            <a
              routerLink="/abastecimiento"
              [routerLinkActiveOptions]="{ exact: true }"
              routerLinkActive="is-active"
            >
              <img
                src="assets/icons/abastecimiento/warehouse.svg"
                alt=""
                width="20"
                height="20"
                aria-hidden="true"
              />
              <span>Abastecimiento</span>
            </a>
            <a routerLink="/erp" routerLinkActive="is-active">
              <img
                src="assets/icons/reports.svg"
                alt=""
                width="20"
                height="20"
                aria-hidden="true"
              />
              <span>ERP interno</span>
            </a>
          </nav>
        </div>

        <section class="session-card">
          <div class="session-heading">
            <img
              src="assets/icons/session.svg"
              alt=""
              width="22"
              height="22"
              aria-hidden="true"
            />
            <p class="session-label">Sesión</p>
          </div>
          <strong>{{ authService.username() }}</strong>
          <span>{{ authService.role() }}</span>
          <button type="button" (click)="logout()">Cerrar sesión</button>
        </section>
      </aside>

      <main class="admin-main">
        <header class="admin-header">
          <div>
            <p class="header-eyebrow">{{ eyebrowTitle() }}</p>
            <h2>{{ pageTitle() }}</h2>
          </div>
          <div class="header-actions">
            <div class="notification-shell">
              <button
                type="button"
                class="notification-button"
                (click)="toggleNotifications()"
              >
                <span class="refresh-button__icon">
                  <img
                    src="assets/icons/notifications.svg"
                    alt=""
                    width="18"
                    height="18"
                    aria-hidden="true"
                  />
                </span>
                <span>Notificaciones</span>
                <strong
                  *ngIf="shellFacade.notificationCounters().unread > 0"
                  class="notification-badge"
                >
                  {{ shellFacade.notificationCounters().unread }}
                </strong>
              </button>

              <section class="notification-panel" *ngIf="notificationsOpen()">
                <header>
                  <div>
                    <p class="header-eyebrow">Buzón interno</p>
                    <h3>Eventos recientes</h3>
                  </div>
                  <strong
                    >{{ shellFacade.notificationCounters().unread }} sin
                    leer</strong
                  >
                </header>

                <div
                  class="notification-actions"
                  *ngIf="shellFacade.notifications().length"
                >
                  <label class="selection-toggle">
                    <input
                      type="checkbox"
                      [checked]="allVisibleNotificationsSelected()"
                      (change)="
                        toggleAllVisibleNotifications(
                          $any($event.target).checked
                        )
                      "
                    />
                    <span>Seleccionar visibles</span>
                  </label>

                  <div class="action-row">
                    <button
                      type="button"
                      class="mini-button"
                      [disabled]="selectedNotificationIds().length === 0"
                      (click)="archiveSelectedNotifications()"
                    >
                      Ocultar seleccionadas
                    </button>
                    <button
                      type="button"
                      class="mini-button mini-button--ghost"
                      (click)="archiveReadNotifications()"
                    >
                      Ocultar leídas
                    </button>
                  </div>
                </div>

                <div
                  class="notification-list"
                  *ngIf="
                    shellFacade.notifications().length;
                    else emptyNotifications
                  "
                >
                  <article
                    *ngFor="let notification of shellFacade.notifications()"
                  >
                    <div class="notification-row">
                      <label
                        class="selection-check"
                        aria-label="Seleccionar notificación"
                      >
                        <input
                          type="checkbox"
                          [checked]="isNotificationSelected(notification.id)"
                          (change)="
                            toggleNotificationSelection(
                              notification.id,
                              $any($event.target).checked
                            )
                          "
                        />
                      </label>
                      <p>{{ notification.title }}</p>
                      <span class="status-pill">{{
                        prettyNotificationPriority(notification.priority)
                      }}</span>
                    </div>
                    <small
                      >{{ notification.module }} ·
                      {{ notification.createdAt | date: "short" }}</small
                    >
                    <p class="notification-copy">{{ notification.message }}</p>
                    <div class="action-row">
                      <button
                        type="button"
                        class="mini-button"
                        *ngIf="notification.status === 'NO_LEIDA'"
                        (click)="markNotificationAsRead(notification.id)"
                      >
                        Marcar leída
                      </button>
                      <button
                        type="button"
                        class="mini-button mini-button--ghost"
                        (click)="archiveNotification(notification.id)"
                      >
                        Ocultar
                      </button>
                    </div>
                  </article>
                </div>

                <ng-template #emptyNotifications>
                  <p class="notification-empty">
                    Todavía no hay notificaciones para esta sesión.
                  </p>
                </ng-template>
              </section>
            </div>

            <button type="button" class="refresh-button" (click)="reload()">
              <span class="refresh-button__icon">
                <img
                  src="assets/icons/refresh.svg"
                  alt=""
                  width="18"
                  height="18"
                  aria-hidden="true"
                />
              </span>
              <span>Actualizar datos</span>
            </button>
          </div>
        </header>

        <section class="status-banner" *ngIf="shellFacade.loading()">
          Actualizando pedidos, clientes y producción...
        </section>
        <section
          class="status-banner is-success"
          *ngIf="shellFacade.actionMessage()"
        >
          {{ shellFacade.actionMessage() }}
        </section>
        <section class="status-banner is-error" *ngIf="shellFacade.error()">
          {{ shellFacade.error() }}
        </section>

        <router-outlet />
      </main>
    </div>
  `,
  styles: [
    `
      .admin-shell {
        display: grid;
        grid-template-columns: minmax(252px, 292px) 1fr;
        min-height: 100vh;
        align-items: start;
      }

      .admin-sidebar {
        position: sticky;
        top: 0;
        align-self: start;
        min-height: 100vh;
        max-height: 100vh;
        overflow-y: auto;
        display: grid;
        grid-template-rows: auto 1fr auto;
        gap: 1.4rem;
        padding: 1.35rem 1.2rem 1.15rem;
        background: linear-gradient(
          180deg,
          #462019 0%,
          #5c2c19 38%,
          #2b140e 100%
        );
        color: #fff2eb;
        border-right: 1px solid rgba(255, 255, 255, 0.1);
      }

      .admin-sidebar::-webkit-scrollbar {
        width: 10px;
      }

      .admin-sidebar::-webkit-scrollbar-thumb {
        background: rgba(255, 232, 220, 0.18);
      }

      .sidebar-top {
        display: grid;
        align-content: start;
        gap: 1.2rem;
      }

      .brand-panel {
        display: grid;
        gap: 0.95rem;
        padding-bottom: 1.15rem;
        border-bottom: 1px solid rgba(255, 242, 235, 0.12);
      }

      .brand-lockup {
        display: grid;
        grid-template-columns: auto 1fr;
        gap: 0.85rem;
        align-items: center;
      }

      .brand-lockup img {
        width: 56px;
        height: 56px;
        display: block;
      }

      .brand-kicker {
        margin: 0;
        text-transform: uppercase;
        letter-spacing: 0.24em;
        font-size: 0.7rem;
        opacity: 0.7;
      }

      .brand-title {
        margin: 0.5rem 0 0;
        font: 700 2rem/1.05
          var(--font-display, "Cormorant Garamond", Georgia, serif);
      }

      .brand-copy {
        margin: 0;
        color: rgba(255, 242, 235, 0.78);
        line-height: 1.6;
      }

      .admin-nav {
        display: grid;
        gap: 0.55rem;
        align-content: start;
      }

      .admin-nav a {
        display: grid;
        grid-template-columns: 20px 1fr;
        align-items: center;
        gap: 0.75rem;
        color: #ffe2d4;
        text-decoration: none;
        padding: 0.85rem 1rem;
        border-radius: 2px;
        background: rgba(255, 255, 255, 0.05);
        border: 1px solid transparent;
        transition: 160ms ease;
      }

      .admin-nav a:hover,
      .admin-nav a.is-active {
        background: rgba(255, 244, 238, 0.13);
        border-color: rgba(255, 233, 220, 0.22);
      }

      .admin-nav a img {
        display: block;
        filter: brightness(0) saturate(100%) invert(94%) sepia(22%)
          saturate(428%) hue-rotate(309deg) brightness(102%) contrast(92%);
        opacity: 0.95;
      }

      .admin-nav a:hover img,
      .admin-nav a.is-active img {
        opacity: 1;
      }

      .session-card {
        align-self: end;
        padding: 1.1rem;
        border-radius: 4px;
        background: rgba(255, 248, 244, 0.08);
        border: 1px solid rgba(255, 242, 235, 0.1);
        display: grid;
        gap: 0.35rem;
      }

      .session-heading {
        display: flex;
        align-items: center;
        gap: 0.55rem;
      }

      .session-label {
        margin: 0;
        font-size: 0.72rem;
        text-transform: uppercase;
        letter-spacing: 0.2em;
        opacity: 0.7;
      }

      .session-card button {
        margin-top: 0.8rem;
        border: 0;
        border-radius: 2px;
        padding: 0.8rem 1rem;
        cursor: pointer;
        font-weight: 600;
        background: #fff2eb;
        color: #4b1f14;
      }

      .admin-main {
        min-width: 0;
        padding: 1.8rem;
        overflow-x: hidden;
        background: radial-gradient(
          circle at top left,
          #fff2e8 0%,
          #f6efe9 30%,
          #f3f4f6 75%
        );
      }

      .admin-header {
        display: grid;
        grid-template-columns: minmax(0, 1fr) auto;
        align-items: start;
        gap: 1rem;
        margin-bottom: 1.5rem;
      }

      .header-actions {
        display: grid;
        grid-template-columns: repeat(2, 15rem);
        align-items: stretch;
        justify-content: end;
        gap: 0.8rem;
        position: relative;
      }

      .header-eyebrow {
        margin: 0;
        color: #8a5c46;
        text-transform: uppercase;
        letter-spacing: 0.2em;
        font-size: 0.72rem;
      }

      .admin-header h2 {
        margin: 0.35rem 0 0;
        font: 700 1.8rem/1.1
          var(--font-display, "Cormorant Garamond", Georgia, serif);
        color: #2d201a;
      }

      .refresh-button,
      .notification-button {
        display: inline-flex;
        align-items: center;
        justify-content: space-between;
        gap: 0.7rem;
        min-height: 4.2rem;
        width: 15rem;
        margin-top: 0;
        padding: 0.8rem 1rem;
        cursor: pointer;
        font-weight: 600;
        background: linear-gradient(180deg, #fffaf6, #f6ede6);
        color: #4d2a1d;
        border: 2px solid #5a3424;
        box-shadow: 0 10px 24px rgba(92, 60, 42, 0.08);
        border-radius: 2px;
      }

      .refresh-button {
        width: 15rem;
      }

      .refresh-button__icon {
        width: 1.95rem;
        height: 1.95rem;
        display: inline-grid;
        place-items: center;
        background: #f0ddd0;
        border: 1px solid #e2cbbb;
        border-radius: 2px;
        flex-shrink: 0;
      }

      .refresh-button__icon img {
        display: block;
      }

      .notification-shell {
        position: relative;
        width: 15rem;
      }

      .notification-badge {
        min-width: 1.8rem;
        height: 1.8rem;
        padding: 0 0.35rem;
        display: inline-grid;
        place-items: center;
        background: #8a3f2c;
        color: #fff8f3;
        border-radius: 999px;
        font-size: 0.82rem;
        line-height: 1;
      }

      .notification-panel {
        position: absolute;
        top: calc(100% + 0.55rem);
        right: 0;
        width: min(28rem, 78vw);
        padding: 1rem;
        display: grid;
        gap: 0.85rem;
        background: #fffaf6;
        border: 2px solid #5a3424;
        box-shadow: 0 20px 48px rgba(59, 34, 24, 0.16);
        z-index: 20;
      }

      .notification-panel header {
        display: flex;
        justify-content: space-between;
        gap: 1rem;
        align-items: start;
      }

      .notification-panel h3 {
        margin: 0.3rem 0 0;
        font: 700 1.1rem/1.1
          var(--font-display, "Cormorant Garamond", Georgia, serif);
        color: #2d201a;
      }

      .notification-actions {
        display: grid;
        gap: 0.7rem;
        padding-bottom: 0.2rem;
        border-bottom: 1px solid #ebd7c9;
      }

      .selection-toggle {
        display: inline-flex;
        align-items: center;
        gap: 0.5rem;
        color: #6e5346;
        font-size: 0.92rem;
      }

      .notification-list {
        display: grid;
        gap: 0.7rem;
        max-height: 24rem;
        overflow: auto;
        padding-right: 0.2rem;
      }

      .notification-list article {
        display: grid;
        gap: 0.45rem;
        padding: 0.9rem;
        background: #fff;
        border: 2px solid #6a4332;
      }

      .notification-row {
        display: grid;
        grid-template-columns: auto minmax(0, 1fr) auto;
        gap: 0.7rem;
        align-items: start;
      }

      .notification-row p,
      .notification-copy,
      .notification-empty {
        margin: 0;
        color: #664d42;
        line-height: 1.5;
      }

      .notification-row p {
        color: #2d201a;
        font-weight: 700;
      }

      .selection-check {
        display: inline-flex;
        align-items: center;
        margin-top: 0.12rem;
      }

      .selection-check input,
      .selection-toggle input {
        width: 1rem;
        height: 1rem;
        accent-color: #7e402c;
      }

      .status-pill {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        min-width: 3.8rem;
        padding: 0.32rem 0.58rem;
        background: #f3e4d8;
        color: #7a4a37;
        font-size: 0.8rem;
        font-weight: 700;
        border-radius: 2px;
      }

      .action-row {
        display: flex;
        flex-wrap: wrap;
        gap: 0.55rem;
      }

      .mini-button {
        border: 1px solid #8a5c46;
        background: #fff;
        color: #5f3929;
        padding: 0.42rem 0.7rem;
        border-radius: 2px;
        cursor: pointer;
        font-weight: 600;
      }

      .mini-button:disabled {
        opacity: 0.45;
        cursor: not-allowed;
      }

      .mini-button--ghost {
        background: #f6eee7;
        color: #725142;
      }

      .status-banner {
        margin-bottom: 1rem;
        padding: 0.9rem 1rem;
        border-radius: 2px;
        background: #fff7e7;
        color: #8b5a11;
      }

      .status-banner.is-success {
        background: #e9f8ee;
        color: #2b6b3f;
        border: 2px solid #2d4d37;
        animation: toastFadeOut 6s forwards;
      }

      .status-banner.is-error {
        background: #ffe9e8;
        color: #a33c32;
        border: 2px solid #6f2d27;
        animation: toastFadeOut 6s forwards;
      }

      @keyframes toastFadeOut {
        0%,
        70% {
          opacity: 1;
          transform: translateY(0);
        }

        100% {
          opacity: 0;
          transform: translateY(-8px);
        }
      }

      @media (max-width: 960px) {
        .admin-shell {
          grid-template-columns: 1fr;
        }

        .admin-sidebar {
          position: static;
          min-height: auto;
          max-height: none;
          gap: 1.2rem;
        }

        .session-card {
          align-self: stretch;
        }

        .admin-main {
          padding: 1.2rem;
        }

        .admin-header {
          grid-template-columns: 1fr;
          align-items: flex-start;
        }

        .header-actions {
          width: 100%;
          display: grid;
          grid-template-columns: 1fr;
        }

        .notification-shell,
        .refresh-button {
          width: 100%;
          flex-basis: auto;
        }

        .notification-panel {
          left: 0;
          right: auto;
        }
      }
    `,
  ],
})
export class ShellComponent {
  readonly authService = inject(AuthService);
  readonly shellFacade = inject(ShellFacadeService);
  private readonly router = inject(Router);

  readonly shellConfig = businessShellConfig;
  readonly logoSquareUrl = getBackendBrandingAsset(this.shellConfig.brand.logoSquareFileName);

  readonly notificationsOpen = signal(false);
  readonly selectedNotificationIds = signal<number[]>([]);

  readonly pageTitle = computed(() => this.resolveCurrentPageMeta().title);

  readonly eyebrowTitle = computed(() => this.resolveCurrentPageMeta().eyebrow);

  private resolveCurrentPageMeta() {
    const currentUrl = this.router.url;
    const routeKey = Object.keys(this.shellConfig.pageMetaByRoute)
      .find((candidate) => currentUrl.includes(candidate));

    if (!routeKey) {
      return this.shellConfig.defaultPage;
    }

    return this.shellConfig.pageMetaByRoute[routeKey] ?? this.shellConfig.defaultPage;
  }

  readonly allVisibleNotificationsSelected = computed(() => {
    const visibleIds = this.shellFacade
      .notifications()
      .map((notification) => notification.id);
    return (
      visibleIds.length > 0 &&
      visibleIds.every((notificationId) =>
        this.selectedNotificationIds().includes(notificationId),
      )
    );
  });

  constructor() {
    this.shellFacade.loadShellData();
  }

  reload() {
    this.shellFacade.reload();
  }

  toggleNotifications() {
    this.notificationsOpen.update((current) => {
      const next = !current;
      if (next) {
        this.selectedNotificationIds.set([]);
      }
      return next;
    });
  }

  toggleNotificationSelection(notificationId: number, checked: boolean) {
    this.selectedNotificationIds.update((current) => {
      if (checked) {
        return current.includes(notificationId)
          ? current
          : [...current, notificationId];
      }
      return current.filter((id) => id !== notificationId);
    });
  }

  toggleAllVisibleNotifications(checked: boolean) {
    const visibleIds = this.shellFacade
      .notifications()
      .map((notification) => notification.id);
    this.selectedNotificationIds.set(checked ? visibleIds : []);
  }

  isNotificationSelected(notificationId: number) {
    return this.selectedNotificationIds().includes(notificationId);
  }

  markNotificationAsRead(notificationId: number) {
    this.shellFacade.markNotificationAsRead(notificationId);
    this.selectedNotificationIds.update((current) =>
      current.filter((id) => id !== notificationId),
    );
  }

  archiveNotification(notificationId: number) {
    this.shellFacade.archiveNotification(notificationId);
    this.selectedNotificationIds.update((current) =>
      current.filter((id) => id !== notificationId),
    );
  }

  archiveSelectedNotifications() {
    const ids = this.selectedNotificationIds();
    if (ids.length === 0) {
      return;
    }
    this.shellFacade.archiveSelectedNotifications(ids);
    this.selectedNotificationIds.set([]);
  }

  archiveReadNotifications() {
    this.shellFacade.archiveReadNotifications();
    this.selectedNotificationIds.set([]);
  }

  prettyNotificationPriority(priority: "BAJA" | "MEDIA" | "ALTA") {
    switch (priority) {
      case "BAJA":
        return "Baja";
      case "MEDIA":
        return "Media";
      case "ALTA":
        return "Alta";
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigateByUrl("/login");
  }
}
