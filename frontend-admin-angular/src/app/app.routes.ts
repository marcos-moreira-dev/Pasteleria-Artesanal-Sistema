import { Routes } from "@angular/router";
import { authGuard } from "./core/auth/auth.guard";
import { LoginComponent } from "./features/login/login.component";
import { ShellComponent } from "./layout/shell/shell.component";
import { DashboardComponent } from "./features/dashboard/dashboard.component";
import { ClientsPageComponent } from "./features/clientes/clients-page.component";
import { ProductsPageComponent } from "./features/productos/products-page.component";
import { QuotationsPageComponent } from "./features/cotizaciones/quotations-page.component";
import { OrdersPageComponent } from "./features/pedidos/orders-page.component";
import { ProductionPageComponent } from "./features/produccion/production-page.component";
import { ReportsPageComponent } from "./features/reportes/reports-page.component";
import { GuiaOperativaPageComponent } from "./features/guia-operativa/guia-operativa-page.component";
import { AbastecimientoDashboardComponent } from "./features/abastecimiento/pages/abastecimiento-dashboard.component";
import { AbastecimientoInventarioComponent } from "./features/abastecimiento/pages/abastecimiento-inventario.component";
import { AbastecimientoComprasComponent } from "./features/abastecimiento/pages/abastecimiento-compras.component";
import { AbastecimientoProveedoresComponent } from "./features/abastecimiento/pages/abastecimiento-proveedores.component";
import { AbastecimientoMovimientosComponent } from "./features/abastecimiento/pages/abastecimiento-movimientos.component";
import { ErpWorkspacesPageComponent } from "./features/erp-workspaces/erp-workspaces-page.component";

export const routes: Routes = [
  { path: "login", component: LoginComponent },
  {
    path: "",
    component: ShellComponent,
    canActivate: [authGuard],
    children: [
      { path: "", pathMatch: "full", component: DashboardComponent },
      { path: "clientes", component: ClientsPageComponent },
      { path: "productos", component: ProductsPageComponent },
      { path: "cotizaciones", component: QuotationsPageComponent },
      { path: "reportes", component: ReportsPageComponent },
      { path: "pedidos", component: OrdersPageComponent },
      { path: "produccion", component: ProductionPageComponent },
      { path: "guia-operativa", component: GuiaOperativaPageComponent },
      { path: "erp", component: ErpWorkspacesPageComponent },
      {
        path: "abastecimiento",
        children: [
          {
            path: "",
            pathMatch: "full",
            redirectTo: "/abastecimiento/dashboard",
          },
          { path: "dashboard", component: AbastecimientoDashboardComponent },
          { path: "inventario", component: AbastecimientoInventarioComponent },
          { path: "compras", component: AbastecimientoComprasComponent },
          {
            path: "proveedores",
            component: AbastecimientoProveedoresComponent,
          },
          {
            path: "movimientos",
            component: AbastecimientoMovimientosComponent,
          },
        ],
      },
    ],
  },
  { path: "**", redirectTo: "" },
];
