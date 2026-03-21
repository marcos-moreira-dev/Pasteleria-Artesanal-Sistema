import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { RouterLink } from "@angular/router";
import { ADMIN_SURFACE_STYLES } from "../../../shared/ui/admin-surface.styles";

interface AbastCard {
  title: string;
  subtitle: string;
  route: string;
  icon: string;
}

@Component({
  selector: "app-abastecimiento-shell",
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="admin-grid">
      <section class="surface-card">
        <header class="surface-header">
          <p class="surface-kicker">Operaciones de abastecimiento</p>
          <div class="surface-title-row">
            <img
              src="assets/icons/abastecimiento/package.svg"
              alt=""
              width="28"
              height="28"
              aria-hidden="true"
            />
            <h3>Gestión de Abastecimiento</h3>
          </div>
          <p class="surface-copy">
            Control integral de inventario, compras y proveedores
          </p>
        </header>

        <div class="cards-grid">
          @for (card of cards; track card.route) {
            <a [routerLink]="card.route" class="abast-card">
              <img [src]="card.icon" [alt]="card.title" />
              <h4>{{ card.title }}</h4>
              <p>{{ card.subtitle }}</p>
            </a>
          }
        </div>
      </section>

      <section class="surface-card surface-card--tinted">
        <header class="surface-header">
          <p class="surface-kicker">Resumen operativo</p>
          <div class="surface-title-row">
            <img
              src="assets/icons/abastecimiento/bar-chart-3.svg"
              alt=""
              width="28"
              height="28"
              aria-hidden="true"
            />
            <h3>Indicadores clave</h3>
          </div>
          <p class="surface-copy">Métricas del módulo de abastecimiento</p>
        </header>

        <div class="kpi-strip">
          <div class="kpi-item">
            <strong>—</strong>
            <span>Items en inventario</span>
          </div>
          <div class="kpi-item">
            <strong>—</strong>
            <span>Órdenes activas</span>
          </div>
          <div class="kpi-item">
            <strong class="text-danger">—</strong>
            <span>Stock crítico</span>
          </div>
          <div class="kpi-item">
            <strong>—</strong>
            <span>Proveedores activos</span>
          </div>
        </div>
      </section>

      <section class="surface-card">
        <header class="surface-header">
          <p class="surface-kicker">Acciones rápidas</p>
          <div class="surface-title-row">
            <img
              src="assets/icons/abastecimiento/plus.svg"
              alt=""
              width="28"
              height="28"
              aria-hidden="true"
            />
            <h3>Operaciones frecuentes</h3>
          </div>
        </header>

        <div class="action-row">
          <a routerLink="/abastecimiento/compras/nueva" class="surface-button">
            <img
              src="assets/icons/abastecimiento/shopping-cart.svg"
              alt=""
              width="16"
              height="16"
            />
            Nueva orden de compra
          </a>
          <a routerLink="/abastecimiento/inventario" class="mini-button">
            <img
              src="assets/icons/abastecimiento/package.svg"
              alt=""
              width="14"
              height="14"
            />
            Ajustar inventario
          </a>
          <a routerLink="/abastecimiento/movimientos" class="mini-button">
            <img
              src="assets/icons/abastecimiento/history.svg"
              alt=""
              width="14"
              height="14"
            />
            Registrar movimiento
          </a>
        </div>
      </section>
    </div>
  `,
  styles: [
    ADMIN_SURFACE_STYLES,
    `
      .abast-card {
        display: grid;
        gap: 0.6rem;
        padding: 1rem;
        border-radius: 4px;
        background: #fff9f4;
        border: 1px solid #f0dfd4;
        text-decoration: none;
        color: inherit;
        transition: all 150ms ease;
      }

      .abast-card:hover {
        border-color: #c96e4a;
        box-shadow: 0 4px 12px rgba(92, 60, 42, 0.08);
      }

      .abast-card img {
        width: 24px;
        height: 24px;
        padding: 0.3rem;
        border-radius: 2px;
        background: rgba(255, 232, 220, 0.9);
      }

      .abast-card h4 {
        margin: 0;
        font: 600 1rem/1.2 var(--font-body, "Inter", system-ui, sans-serif);
        color: #2d201a;
      }

      .abast-card p {
        margin: 0;
        font-size: 0.8rem;
        color: #7f6f68;
        line-height: 1.4;
      }

      .kpi-strip {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 1rem;
        margin-top: 0.5rem;
      }

      .kpi-item {
        display: flex;
        flex-direction: column;
        gap: 0.3rem;
        padding: 0.9rem;
        background: linear-gradient(
          180deg,
          rgba(255, 255, 255, 0.95),
          rgba(255, 250, 245, 0.92)
        );
        border: 1px solid #eaded4;
        border-radius: 4px;
      }

      .kpi-item strong {
        font: 700 1.6rem/1
          var(--font-display, "Cormorant Garamond", Georgia, serif);
        color: #2d201a;
      }

      .kpi-item strong.text-danger {
        color: #b71c1c;
      }

      .kpi-item span {
        font-size: 0.78rem;
        color: #7f6f68;
        text-transform: uppercase;
        letter-spacing: 0.05em;
      }

      /* Botones con padding consistente */
      .surface-button {
        display: inline-flex;
        align-items: center;
        gap: 0.5rem;
        padding: 0.75rem 1.25rem;
        border: 2px solid #5a3424;
        background: linear-gradient(180deg, #fffaf6, #f6ede6);
        color: #4d2a1d;
        font-weight: 600;
        border-radius: 2px;
        cursor: pointer;
        transition: all 150ms ease;
        box-shadow: 0 10px 24px rgba(92, 60, 42, 0.08);
      }

      .surface-button:hover {
        background: linear-gradient(180deg, #fff5ed, #f0e4db);
        border-color: #4a2a1d;
        transform: translateY(-1px);
      }

      .mini-button {
        display: inline-flex;
        align-items: center;
        gap: 0.4rem;
        padding: 0.6rem 1rem;
        border: 1px solid #8a5c46;
        background: #fff;
        color: #5f3929;
        font-size: 0.85rem;
        font-weight: 600;
        border-radius: 2px;
        cursor: pointer;
        transition: all 150ms ease;
      }

      .mini-button:hover {
        background: #faf6f2;
        border-color: #5a3424;
      }

      .surface-button img,
      .mini-button img {
        opacity: 0.9;
        width: 16px;
        height: 16px;
      }

      @media (max-width: 960px) {
        .kpi-strip {
          grid-template-columns: 1fr;
        }
      }
    `,
  ],
})
export class AbastecimientoShellComponent {
  readonly cards: AbastCard[] = [
    {
      title: "Dashboard",
      subtitle: "Resumen y métricas generales",
      route: "/abastecimiento/dashboard",
      icon: "assets/icons/abastecimiento/bar-chart-3.svg",
    },
    {
      title: "Inventario",
      subtitle: "Control de stock de ingredientes e insumos",
      route: "/abastecimiento/inventario",
      icon: "assets/icons/abastecimiento/package.svg",
    },
    {
      title: "Órdenes de compra",
      subtitle: "Crear y gestionar órdenes de compra",
      route: "/abastecimiento/compras",
      icon: "assets/icons/abastecimiento/shopping-cart.svg",
    },
    {
      title: "Proveedores",
      subtitle: "Directorio de proveedores",
      route: "/abastecimiento/proveedores",
      icon: "assets/icons/abastecimiento/store.svg",
    },
    {
      title: "Movimientos",
      subtitle: "Historial de entradas, salidas y ajustes",
      route: "/abastecimiento/movimientos",
      icon: "assets/icons/abastecimiento/history.svg",
    },
  ];
}
