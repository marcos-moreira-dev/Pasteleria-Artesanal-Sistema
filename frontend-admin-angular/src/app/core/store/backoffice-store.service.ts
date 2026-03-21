import { Injectable, computed, inject, signal } from "@angular/core";
import { Observable, catchError, forkJoin, of } from "rxjs";
import { ApiClientService } from "../api/api-client.service";
import type { PageResponseDto } from "../contracts/api-contracts";
import type {
  ClientSummary,
  CreateClientPayload,
  UpdateClientPayload,
} from "../../features/clientes/models/client.models";
import type {
  CreateQuotationPayload,
  QuotationSummary,
} from "../../features/cotizaciones/models/quotation.models";
import type {
  CreateOrderPayload,
  OrderSummary,
} from "../../features/pedidos/models/order.models";
import type {
  NotificationCounters,
  NotificationSummary,
} from "../../features/notificaciones/models/notification.models";
import type {
  ProductCategorySummary,
  ProductSummary,
  CreateProductPayload,
  UpdateProductPayload,
} from "../../features/productos/models/product.models";
import type {
  ReportJobSummary,
  ReportTypeValue,
} from "../../features/reportes/models/report.models";
import type {
  ProductionStatusValue,
  ProductionSummary,
} from "../../features/produccion/models/production.models";
import type {
  AbastecimientoDashboard,
  FinalizarProduccionDetalleRequest,
  IniciarProduccionRequest,
  IngredienteSummary,
  InsumoSummary,
  InventarioMovimientoSummary,
  ItemTipo,
  OrdenCompraSummary,
  OrdenProduccionDetailSummary,
  OrdenProduccionSummary,
  ProveedorSummary,
  RecetaSummary,
  RegistrarConsumoRequest,
  TipoMovimiento,
} from "../../features/abastecimiento/models/abastecimiento.models";

interface BackofficeSnapshot {
  clients: ClientSummary[];
  notifications: NotificationSummary[];
  notificationCounters: NotificationCounters;
  products: ProductSummary[];
  reportJobs: ReportJobSummary[];
  categories: ProductCategorySummary[];
  quotations: QuotationSummary[];
  orders: OrderSummary[];
  productionQueue: ProductionSummary[];
  abastecimientoDashboard: AbastecimientoDashboard | null;
}

interface LoadAllOptions {
  preserveActionMessage?: boolean;
}

function emptyPage<T>(size = 8): PageResponseDto<T> {
  return {
    content: [],
    page: 0,
    size,
    totalElements: 0,
    totalPages: 0,
    numberOfElements: 0,
    first: true,
    last: true,
    sort: null,
  };
}

@Injectable({ providedIn: "root" })
export class BackofficeStoreService {
  private readonly api = inject(ApiClientService);
  private actionMessageTimer: number | null = null;
  private errorTimer: number | null = null;
  private reportPollingTimer: number | null = null;

  readonly clients = signal<ClientSummary[]>([]);
  readonly clientsPage =
    signal<PageResponseDto<ClientSummary>>(emptyPage<ClientSummary>());
  readonly clientSearchQuery = signal("");
  readonly notifications = signal<NotificationSummary[]>([]);
  readonly notificationCounters = signal<NotificationCounters>({ unread: 0 });
  readonly products = signal<ProductSummary[]>([]);
  readonly productsPage =
    signal<PageResponseDto<ProductSummary>>(emptyPage<ProductSummary>());
  readonly reportJobs = signal<ReportJobSummary[]>([]);
  readonly reportJobsPage =
    signal<PageResponseDto<ReportJobSummary>>(emptyPage<ReportJobSummary>());
  readonly categories = signal<ProductCategorySummary[]>([]);
  readonly quotations = signal<QuotationSummary[]>([]);
  readonly quotationsPage =
    signal<PageResponseDto<QuotationSummary>>(emptyPage<QuotationSummary>());
  readonly orders = signal<OrderSummary[]>([]);
  readonly ordersPage =
    signal<PageResponseDto<OrderSummary>>(emptyPage<OrderSummary>());
  readonly productionQueue = signal<ProductionSummary[]>([]);
  readonly productionPage =
    signal<PageResponseDto<ProductionSummary>>(emptyPage<ProductionSummary>());
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly actionMessage = signal<string | null>(null);

  readonly ingredientesPage =
    signal<PageResponseDto<IngredienteSummary>>(
      emptyPage<IngredienteSummary>(),
    );
  readonly insumosPage =
    signal<PageResponseDto<InsumoSummary>>(emptyPage<InsumoSummary>());
  readonly proveedoresPage =
    signal<PageResponseDto<ProveedorSummary>>(emptyPage<ProveedorSummary>());
  readonly ordenesCompraPage =
    signal<PageResponseDto<OrdenCompraSummary>>(
      emptyPage<OrdenCompraSummary>(),
    );
  readonly ordenesProduccionPage =
    signal<PageResponseDto<OrdenProduccionSummary>>(
      emptyPage<OrdenProduccionSummary>(),
    );
  readonly movimientos = signal<InventarioMovimientoSummary[]>([]);
  readonly recetasPage =
    signal<PageResponseDto<RecetaSummary>>(emptyPage<RecetaSummary>());
  readonly proveedores = signal<ProveedorSummary[]>([]);
  readonly ingredientes = signal<IngredienteSummary[]>([]);
  readonly insumos = signal<InsumoSummary[]>([]);
  readonly abastecimientoDashboard = signal<AbastecimientoDashboard | null>(
    null,
  );

  readonly dashboardMetrics = computed(() => ({
    clients: this.clients().length,
    products: this.products().filter((item) => item.active && item.published)
      .length,
    quotationsPending: this.quotations().filter(
      (item) => item.status === "PENDIENTE",
    ).length,
    urgentOrders: this.orders().filter((item) => item.priority === "URGENTE")
      .length,
    activeProduction: this.productionQueue().filter(
      (item) => item.status !== "FINALIZADO",
    ).length,
  }));

  readonly abastecimientoMetrics = computed(() => {
    const dash = this.abastecimientoDashboard();
    if (!dash) return null;
    return {
      criticos: dash.itemsCriticos,
      bajoMinimo: dash.itemsBajoMinimo,
      ordenesPendientes: dash.ordenesPendientes,
      recepcionesHoy: dash.recepcionesHoy,
      costoReposicion: dash.costoReposicionEstimado,
    };
  });

  loadAll(options: LoadAllOptions = {}) {
    this.loading.set(true);
    this.setErrorMessage(null);
    if (!options.preserveActionMessage) {
      this.setActionMessage(null);
    }

    forkJoin({
      clients: this.withCollectionFallback(this.api.getClients(), "clientes"),
      notifications: this.withCollectionFallback(
        this.api.getNotifications(),
        "notificaciones",
      ),
      notificationCounters: this.withObjectFallback(
        this.api.getNotificationCounters(),
        "resumen de notificaciones",
        { unread: 0 },
      ),
      products: this.withCollectionFallback(
        this.api.getProducts(),
        "productos",
      ),
      reportJobs: this.withCollectionFallback(
        this.api.getReportJobs(),
        "jobs de reportes",
      ),
      categories: this.withCollectionFallback(
        this.api.getCategories(),
        "categorías",
      ),
      quotations: this.withCollectionFallback(
        this.api.getQuotations(),
        "cotizaciones",
      ),
      orders: this.withCollectionFallback(this.api.getOrders(), "pedidos"),
      productionQueue: this.withCollectionFallback(
        this.api.getProductionQueue(),
        "producción",
      ),
      abastecimientoDashboard: this.withObjectFallback(
        this.api.getAbastecimientoDashboard(),
        "dashboard de abastecimiento",
        null,
      ),
    }).subscribe({
      next: (payload) => {
        this.applySnapshot(payload);
        this.loading.set(false);
      },
      error: () => {
        this.setErrorMessage("No fue posible cargar el panel administrativo.");
        this.loading.set(false);
      },
    });
  }

  loadClientsPage(
    page = this.clientsPage().page,
    size = this.clientsPage().size || 8,
    query = this.clientSearchQuery(),
  ) {
    this.clientSearchQuery.set(query);
    return this.api.getClientsPage(page, size, query).subscribe({
      next: (pageData) =>
        this.applyPagedResult(
          pageData,
          this.clientsPage,
          (nextPage, nextSize) =>
            this.loadClientsPage(nextPage, nextSize, this.clientSearchQuery()),
        ),
      error: () =>
        this.setErrorMessage("No se pudo cargar la tabla de clientes."),
    });
  }

  loadProductsPage(
    page = this.productsPage().page,
    size = this.productsPage().size || 8,
  ) {
    return this.api.getProductsPage(page, size).subscribe({
      next: (pageData) =>
        this.applyPagedResult(
          pageData,
          this.productsPage,
          (nextPage, nextSize) => this.loadProductsPage(nextPage, nextSize),
        ),
      error: () =>
        this.setErrorMessage("No se pudo cargar la tabla de productos."),
    });
  }

  loadReportJobsPage(
    page = this.reportJobsPage().page,
    size = this.reportJobsPage().size || 8,
  ) {
    return this.api.getReportJobsPage(page, size).subscribe({
      next: (pageData) =>
        this.applyPagedResult(
          pageData,
          this.reportJobsPage,
          (nextPage, nextSize) => this.loadReportJobsPage(nextPage, nextSize),
        ),
      error: () =>
        this.setErrorMessage("No se pudo cargar la tabla de reportes."),
    });
  }

  loadQuotationsPage(
    page = this.quotationsPage().page,
    size = this.quotationsPage().size || 8,
  ) {
    return this.api.getQuotationsPage(page, size).subscribe({
      next: (pageData) =>
        this.applyPagedResult(
          pageData,
          this.quotationsPage,
          (nextPage, nextSize) => this.loadQuotationsPage(nextPage, nextSize),
        ),
      error: () =>
        this.setErrorMessage("No se pudo cargar la tabla de cotizaciones."),
    });
  }

  loadOrdersPage(
    page = this.ordersPage().page,
    size = this.ordersPage().size || 8,
  ) {
    return this.api.getOrdersPage(page, size).subscribe({
      next: (pageData) =>
        this.applyPagedResult(pageData, this.ordersPage, (nextPage, nextSize) =>
          this.loadOrdersPage(nextPage, nextSize),
        ),
      error: () =>
        this.setErrorMessage("No se pudo cargar la tabla de pedidos."),
    });
  }

  loadProductionPage(
    page = this.productionPage().page,
    size = this.productionPage().size || 8,
  ) {
    return this.api.getProductionQueuePage(page, size).subscribe({
      next: (pageData) =>
        this.applyPagedResult(
          pageData,
          this.productionPage,
          (nextPage, nextSize) => this.loadProductionPage(nextPage, nextSize),
        ),
      error: () =>
        this.setErrorMessage("No se pudo cargar la cola de producción."),
    });
  }

  loadIngredientesPage(
    page = 0,
    size = 8,
    query = "",
    tipo: ItemTipo | "" = "",
  ) {
    return this.api.getIngredientesPage(page, size, query, tipo).subscribe({
      next: (pageData) =>
        this.applyPagedResult(
          pageData,
          this.ingredientesPage,
          (nextPage, nextSize) =>
            this.loadIngredientesPage(nextPage, nextSize, query, tipo),
        ),
      error: () =>
        this.setErrorMessage("No se pudo cargar la tabla de ingredientes."),
    });
  }

  loadInsumosPage(page = 0, size = 8, query = "", tipo: ItemTipo | "" = "") {
    return this.api.getInsumosPage(page, size, query, tipo).subscribe({
      next: (pageData) =>
        this.applyPagedResult(
          pageData,
          this.insumosPage,
          (nextPage, nextSize) =>
            this.loadInsumosPage(nextPage, nextSize, query, tipo),
        ),
      error: () =>
        this.setErrorMessage("No se pudo cargar la tabla de insumos."),
    });
  }

  loadProveedoresPage(page = 0, size = 8, query = "") {
    return this.api.getProveedoresPage(page, size, query).subscribe({
      next: (pageData) =>
        this.applyPagedResult(
          pageData,
          this.proveedoresPage,
          (nextPage, nextSize) =>
            this.loadProveedoresPage(nextPage, nextSize, query),
        ),
      error: () =>
        this.setErrorMessage("No se pudo cargar la tabla de proveedores."),
    });
  }

  loadProveedores() {
    return this.api.getProveedores().subscribe({
      next: (data) => this.proveedores.set(data),
      error: () =>
        this.setErrorMessage("No se pudo cargar la lista de proveedores."),
    });
  }

  loadOrdenesCompraPage(page = 0, size = 8, query = "", estado = "") {
    return this.api.getOrdenesCompraPage(page, size, query, estado).subscribe({
      next: (pageData) =>
        this.applyPagedResult(
          pageData,
          this.ordenesCompraPage,
          (nextPage, nextSize) =>
            this.loadOrdenesCompraPage(nextPage, nextSize, query, estado),
        ),
      error: () =>
        this.setErrorMessage(
          "No se pudo cargar la tabla de órdenes de compra.",
        ),
    });
  }

  loadOrdenCompraDetail(ordenId: number) {
    return this.api.getOrdenCompraDetail(ordenId);
  }

  loadOrdenProduccionPage(page = 0, size = 8, query = "") {
    return this.api.getOrdenesProduccionPage(page, size, query).subscribe({
      next: (pageData) =>
        this.applyPagedResult(
          pageData,
          this.ordenesProduccionPage,
          (nextPage, nextSize) =>
            this.loadOrdenProduccionPage(nextPage, nextSize, query),
        ),
      error: () =>
        this.setErrorMessage(
          "No se pudo cargar la tabla de órdenes de producción.",
        ),
    });
  }

  loadOrdenProduccionDetail(ordenId: number) {
    return this.api.getOrdenProduccionDetail(ordenId);
  }

  loadMovimientos(
    itemTipo?: ItemTipo,
    itemId?: number,
    tipoMovimiento?: TipoMovimiento,
  ) {
    return this.api.getMovimientos(itemTipo, itemId, tipoMovimiento).subscribe({
      next: (data) => this.movimientos.set(data),
      error: () =>
        this.setErrorMessage("No se pudo cargar el historial de movimientos."),
    });
  }

  loadRecetasPage(page = 0, size = 8, query = "") {
    return this.api.getRecetasPage(page, size, query).subscribe({
      next: (pageData) =>
        this.applyPagedResult(
          pageData,
          this.recetasPage,
          (nextPage, nextSize) =>
            this.loadRecetasPage(nextPage, nextSize, query),
        ),
      error: () =>
        this.setErrorMessage("No se pudo cargar la tabla de recetas."),
    });
  }

  loadAbastecimientoDashboard() {
    return this.api.getAbastecimientoDashboard().subscribe({
      next: (data) => this.abastecimientoDashboard.set(data),
      error: () =>
        this.setErrorMessage(
          "No se pudo cargar el dashboard de abastecimiento.",
        ),
    });
  }

  createClient(payload: CreateClientPayload, afterSuccess?: () => void) {
    return this.runMutation(this.api.createClient(payload), {
      successMessage: "Cliente registrado correctamente.",
      errorMessage: "No se pudo registrar el cliente.",
      onSuccess: (client) => {
        this.clients.update((items) => [client, ...items]);
        this.loadClientsPage(
          0,
          this.clientsPage().size,
          this.clientSearchQuery(),
        );
        afterSuccess?.();
      },
    });
  }

  updateClient(
    clientId: number,
    payload: UpdateClientPayload,
    afterSuccess?: () => void,
  ) {
    return this.runMutation(this.api.updateClient(clientId, payload), {
      successMessage: "Cliente actualizado correctamente.",
      errorMessage: "No se pudo actualizar el cliente.",
      onSuccess: (client) => {
        this.clients.update((items) =>
          items.map((item) => (item.id === client.id ? client : item)),
        );
        this.loadClientsPage(
          this.clientsPage().page,
          this.clientsPage().size,
          this.clientSearchQuery(),
        );
        afterSuccess?.();
      },
    });
  }

  createQuotation(payload: CreateQuotationPayload, afterSuccess?: () => void) {
    return this.runMutation(this.api.createQuotation(payload), {
      successMessage: "Cotización registrada correctamente.",
      errorMessage: "No se pudo registrar la cotización.",
      onSuccess: (quotation) => {
        this.quotations.update((items) => [quotation, ...items]);
        this.loadQuotationsPage(0, this.quotationsPage().size);
        afterSuccess?.();
      },
    });
  }

  createOrder(payload: CreateOrderPayload, afterSuccess?: () => void) {
    return this.runMutation(this.api.createOrder(payload), {
      successMessage: "Pedido registrado correctamente.",
      errorMessage: "No se pudo registrar el pedido.",
      onSuccess: (order) => {
        this.orders.update((items) => [order, ...items]);
        this.loadOrdersPage(0, this.ordersPage().size);
        this.loadProductionPage(
          this.productionPage().page,
          this.productionPage().size,
        );
        afterSuccess?.();
      },
      reloadAfter: true,
    });
  }

  createProduct(payload: CreateProductPayload, afterSuccess?: () => void) {
    return this.runMutation(this.api.createProduct(payload), {
      successMessage: "Producto registrado correctamente.",
      errorMessage: "No se pudo registrar el producto.",
      onSuccess: (product) => {
        this.products.update((items) => [product, ...items]);
        this.loadProductsPage(0, this.productsPage().size);
        afterSuccess?.();
      },
    });
  }

  requestReport(reportType: ReportTypeValue) {
    return this.runMutation(this.api.requestReport(reportType), {
      successMessage: "Reporte enviado a cola correctamente.",
      errorMessage: "No se pudo solicitar el reporte.",
      onSuccess: (job) => {
        this.reportJobs.update((items) => [job, ...items].slice(0, 6));
        this.loadReportJobsPage(0, this.reportJobsPage().size);
        this.startReportPolling();
      },
      reloadAfter: true,
    });
  }

  updateProduct(
    productId: number,
    payload: UpdateProductPayload,
    afterSuccess?: () => void,
  ) {
    return this.runMutation(this.api.updateProduct(productId, payload), {
      successMessage: "Producto actualizado correctamente.",
      errorMessage: "No se pudo actualizar el producto.",
      onSuccess: (product) => {
        this.products.update((items) =>
          items.map((item) => (item.id === product.id ? product : item)),
        );
        this.loadProductsPage(
          this.productsPage().page,
          this.productsPage().size,
        );
        afterSuccess?.();
      },
    });
  }

  deleteProduct(productId: number) {
    return this.runMutation(this.api.deleteProduct(productId), {
      successMessage: "Producto eliminado correctamente.",
      errorMessage: "No se pudo eliminar el producto.",
      onSuccess: () => {
        this.products.update((items) =>
          items.filter((item) => item.id !== productId),
        );
        this.loadProductsPage(
          this.productsPage().page,
          this.productsPage().size,
        );
      },
    });
  }

  deleteClient(clientId: number) {
    return this.runMutation(this.api.deleteClient(clientId), {
      successMessage: "Cliente eliminado correctamente.",
      errorMessage: "No se pudo eliminar el cliente.",
      onSuccess: () => {
        this.clients.update((items) =>
          items.filter((item) => item.id !== clientId),
        );
        this.loadClientsPage(
          this.clientsPage().page,
          this.clientsPage().size,
          this.clientSearchQuery(),
        );
      },
    });
  }

  updateOrderStatus(orderId: number, status: string, reason: string | null) {
    return this.runMutation(
      this.api.updateOrderStatus(orderId, { status, reason }),
      {
        successMessage: "Estado del pedido actualizado.",
        errorMessage: "No se pudo actualizar el estado del pedido.",
        onSuccess: () => {
          this.loadOrdersPage(this.ordersPage().page, this.ordersPage().size);
          this.loadProductionPage(
            this.productionPage().page,
            this.productionPage().size,
          );
        },
        reloadAfter: true,
      },
    );
  }

  deleteOrder(orderId: number) {
    return this.runMutation(this.api.deleteOrder(orderId), {
      successMessage: "Pedido eliminado correctamente.",
      errorMessage: "No se pudo eliminar el pedido.",
      onSuccess: () => {
        this.orders.update((items) =>
          items.filter((item) => item.id !== orderId),
        );
        this.loadOrdersPage(this.ordersPage().page, this.ordersPage().size);
        this.loadProductionPage(
          this.productionPage().page,
          this.productionPage().size,
        );
      },
      reloadAfter: true,
    });
  }

  updateProductionStatus(
    productionId: number,
    status: ProductionStatusValue,
    reason: string | null,
    successMessage = "Estado de producción actualizado.",
  ) {
    this.runMutation(
      this.api.updateProductionStatus(productionId, { status, reason }),
      {
        successMessage,
        errorMessage: "No se pudo actualizar el estado de producción.",
        onSuccess: () => {
          this.loadProductionPage(
            this.productionPage().page,
            this.productionPage().size,
          );
          this.loadOrdersPage(this.ordersPage().page, this.ordersPage().size);
        },
        reloadAfter: true,
      },
    );
  }

  markNotificationAsRead(notificationId: number) {
    return this.runMutation(this.api.markNotificationAsRead(notificationId), {
      successMessage: "Notificación marcada como leída.",
      errorMessage: "No se pudo actualizar la notificación.",
      onSuccess: () => this.refreshNotificationsState(),
    });
  }

  archiveNotification(notificationId: number) {
    return this.runMutation(this.api.archiveNotification(notificationId), {
      successMessage: "Notificación ocultada del buzón.",
      errorMessage: "No se pudo ocultar la notificación.",
      onSuccess: () => this.refreshNotificationsState(),
    });
  }

  archiveSelectedNotifications(notificationIds: number[]) {
    return this.runMutation(
      this.api.archiveSelectedNotifications(notificationIds),
      {
        successMessage: "Notificaciones ocultadas correctamente.",
        errorMessage:
          "No se pudieron ocultar las notificaciones seleccionadas.",
        onSuccess: () => this.refreshNotificationsState(),
      },
    );
  }

  archiveReadNotifications() {
    return this.runMutation(this.api.archiveReadNotifications(), {
      successMessage: "Notificaciones leídas ocultadas correctamente.",
      errorMessage: "No se pudieron ocultar las notificaciones leídas.",
      onSuccess: () => this.refreshNotificationsState(),
    });
  }

  deleteSelectedReportJobs(jobIds: number[]) {
    return this.runMutation(this.api.deleteSelectedReportJobs(jobIds), {
      successMessage: "Reportes eliminados correctamente.",
      errorMessage: "No se pudieron eliminar los reportes seleccionados.",
      onSuccess: () => this.refreshReportsState(),
    });
  }

  cleanupOldReportJobs(keepLatest = 10) {
    return this.runMutation(this.api.cleanupOldReportJobs(keepLatest), {
      successMessage: `Historial depurado. Se conservan las ${keepLatest} ejecuciones más recientes.`,
      errorMessage: "No se pudo depurar el historial de reportes.",
      onSuccess: () => this.refreshReportsState(),
    });
  }

  downloadReport(jobId: number, fileName: string | null) {
    this.setErrorMessage(null);
    return this.api.downloadReport(jobId).subscribe({
      next: (blob) => {
        const blobUrl = window.URL.createObjectURL(blob);
        const anchor = document.createElement("a");
        anchor.href = blobUrl;
        anchor.download = fileName || `reporte-${jobId}.pdf`;
        anchor.click();
        window.URL.revokeObjectURL(blobUrl);
        this.setActionMessage("Reporte descargado correctamente.");
      },
      error: () => {
        this.setErrorMessage("No se pudo descargar el archivo del reporte.");
      },
    });
  }

  createProveedor(payload: any, afterSuccess?: () => void) {
    return this.runMutation(this.api.createProveedor(payload), {
      successMessage: "Proveedor registrado correctamente.",
      errorMessage: "No se pudo registrar el proveedor.",
      onSuccess: (proveedor) => {
        this.proveedores.update((items) => [proveedor, ...items]);
        this.loadProveedoresPage(0, this.proveedoresPage().size);
        afterSuccess?.();
      },
    });
  }

  updateProveedor(
    proveedorId: number,
    payload: any,
    afterSuccess?: () => void,
  ) {
    return this.runMutation(this.api.updateProveedor(proveedorId, payload), {
      successMessage: "Proveedor actualizado correctamente.",
      errorMessage: "No se pudo actualizar el proveedor.",
      onSuccess: (proveedor) => {
        this.proveedores.update((items) =>
          items.map((item) => (item.id === proveedor.id ? proveedor : item)),
        );
        this.loadProveedoresPage(
          this.proveedoresPage().page,
          this.proveedoresPage().size,
        );
        afterSuccess?.();
      },
    });
  }

  deleteProveedor(proveedorId: number) {
    return this.runMutation(this.api.deleteProveedor(proveedorId), {
      successMessage: "Proveedor eliminado correctamente.",
      errorMessage: "No se pudo eliminar el proveedor.",
      onSuccess: () => {
        this.proveedores.update((items) =>
          items.filter((item) => item.id !== proveedorId),
        );
        this.loadProveedoresPage(
          this.proveedoresPage().page,
          this.proveedoresPage().size,
        );
      },
    });
  }

  toggleProveedorActivo(proveedorId: number) {
    return this.runMutation(this.api.toggleProveedorActivo(proveedorId), {
      successMessage: "Estado del proveedor actualizado.",
      errorMessage: "No se pudo actualizar el estado del proveedor.",
      onSuccess: (proveedor) => {
        this.proveedores.update((items) =>
          items.map((item) => (item.id === proveedor.id ? proveedor : item)),
        );
        this.loadProveedoresPage(
          this.proveedoresPage().page,
          this.proveedoresPage().size,
        );
      },
    });
  }

  createItemProveedor(payload: any, afterSuccess?: () => void) {
    return this.runMutation(this.api.createItemProveedor(payload), {
      successMessage: "Item de proveedor registrado correctamente.",
      errorMessage: "No se pudo registrar el item de proveedor.",
      onSuccess: () => afterSuccess?.(),
    });
  }

  updateItemProveedor(itemId: number, payload: any, afterSuccess?: () => void) {
    return this.runMutation(this.api.updateItemProveedor(itemId, payload), {
      successMessage: "Item de proveedor actualizado correctamente.",
      errorMessage: "No se pudo actualizar el item de proveedor.",
      onSuccess: () => afterSuccess?.(),
    });
  }

  deleteItemProveedor(itemId: number) {
    return this.runMutation(this.api.deleteItemProveedor(itemId), {
      successMessage: "Item de proveedor eliminado correctamente.",
      errorMessage: "No se pudo eliminar el item de proveedor.",
      onSuccess: () => {},
    });
  }

  createMovimiento(payload: any, afterSuccess?: () => void) {
    return this.runMutation(this.api.createMovimiento(payload), {
      successMessage: "Movimiento registrado correctamente.",
      errorMessage: "No se pudo registrar el movimiento.",
      onSuccess: (movimiento) => {
        this.movimientos.update((items) => [movimiento, ...items]);
        afterSuccess?.();
      },
    });
  }

  createReceta(payload: any, afterSuccess?: () => void) {
    return this.runMutation(this.api.createReceta(payload), {
      successMessage: "Receta registrada correctamente.",
      errorMessage: "No se pudo registrar la receta.",
      onSuccess: () => {
        this.loadRecetasPage(0, this.recetasPage().size);
        afterSuccess?.();
      },
    });
  }

  updateReceta(recetaId: number, payload: any, afterSuccess?: () => void) {
    return this.runMutation(this.api.updateReceta(recetaId, payload), {
      successMessage: "Receta actualizada correctamente.",
      errorMessage: "No se pudo actualizar la receta.",
      onSuccess: () => {
        this.loadRecetasPage(this.recetasPage().page, this.recetasPage().size);
        afterSuccess?.();
      },
    });
  }

  deleteReceta(recetaId: number) {
    return this.runMutation(this.api.deleteReceta(recetaId), {
      successMessage: "Receta eliminada correctamente.",
      errorMessage: "No se pudo eliminar la receta.",
      onSuccess: () => {
        this.loadRecetasPage(this.recetasPage().page, this.recetasPage().size);
      },
    });
  }

  toggleRecetaActiva(recetaId: number) {
    return this.runMutation(this.api.toggleRecetaActiva(recetaId), {
      successMessage: "Estado de receta actualizado.",
      errorMessage: "No se pudo actualizar el estado de la receta.",
      onSuccess: () => {
        this.loadRecetasPage(this.recetasPage().page, this.recetasPage().size);
      },
    });
  }

  createOrdenCompra(payload: any, afterSuccess?: () => void) {
    return this.runMutation(this.api.createOrdenCompra(payload), {
      successMessage: "Orden de compra registrada correctamente.",
      errorMessage: "No se pudo registrar la orden de compra.",
      onSuccess: () => {
        this.loadOrdenesCompraPage(0, this.ordenesCompraPage().size);
        this.loadAbastecimientoDashboard();
        afterSuccess?.();
      },
    });
  }

  updateOrdenCompra(ordenId: number, payload: any, afterSuccess?: () => void) {
    return this.runMutation(this.api.updateOrdenCompra(ordenId, payload), {
      successMessage: "Orden de compra actualizada correctamente.",
      errorMessage: "No se pudo actualizar la orden de compra.",
      onSuccess: () => {
        this.loadOrdenesCompraPage(
          this.ordenesCompraPage().page,
          this.ordenesCompraPage().size,
        );
        this.loadAbastecimientoDashboard();
        afterSuccess?.();
      },
    });
  }

  updateOrdenCompraEstado(
    ordenId: number,
    estado: string,
    observaciones?: string,
  ) {
    const payload = {
      estado: estado as any,
      observaciones,
    };
    return this.runMutation(
      this.api.updateOrdenCompraEstado(ordenId, payload),
      {
        successMessage: "Estado de orden de compra actualizado.",
        errorMessage: "No se pudo actualizar el estado de la orden de compra.",
        onSuccess: () => {
          this.loadOrdenesCompraPage(
            this.ordenesCompraPage().page,
            this.ordenesCompraPage().size,
          );
          this.loadAbastecimientoDashboard();
        },
      },
    );
  }

  receiveOrdenCompra(ordenId: number, payload: any, afterSuccess?: () => void) {
    return this.runMutation(this.api.receiveOrdenCompra(ordenId, payload), {
      successMessage: "Orden de compra recibida correctamente.",
      errorMessage: "No se pudo recibir la orden de compra.",
      onSuccess: () => {
        this.loadOrdenesCompraPage(
          this.ordenesCompraPage().page,
          this.ordenesCompraPage().size,
        );
        this.loadAbastecimientoDashboard();
        afterSuccess?.();
      },
    });
  }

  createOrdenProduccion(payload: any, afterSuccess?: () => void) {
    return this.runMutation(this.api.createOrdenProduccion(payload), {
      successMessage: "Orden de producción registrada correctamente.",
      errorMessage: "No se pudo registrar la orden de producción.",
      onSuccess: () => {
        this.loadOrdenProduccionPage(0, this.ordenesProduccionPage().size);
        afterSuccess?.();
      },
    });
  }

  updateOrdenProduccionEstado(
    ordenId: number,
    estado: string,
    observaciones?: string,
  ) {
    const payload = {
      estado: estado as any,
      observaciones,
    };
    return this.runMutation(
      this.api.updateOrdenProduccionEstado(ordenId, payload),
      {
        successMessage: "Estado de orden de producción actualizado.",
        errorMessage:
          "No se pudo actualizar el estado de la orden de producción.",
        onSuccess: () => {
          this.loadOrdenProduccionPage(
            this.ordenesProduccionPage().page,
            this.ordenesProduccionPage().size,
          );
        },
      },
    );
  }

  iniciarProduccion(detalleIds?: number[]) {
    const payload: IniciarProduccionRequest = { detalleIds };
    return this.runMutation(this.api.iniciarProduccion(payload), {
      successMessage: "Producción iniciada correctamente.",
      errorMessage: "No se pudo iniciar la producción.",
      onSuccess: () => {
        this.loadOrdenProduccionPage(
          this.ordenesProduccionPage().page,
          this.ordenesProduccionPage().size,
        );
      },
    });
  }

  finalizarDetalleProduccion(
    detalleId: number,
    cantidadProducida: number,
    observaciones?: string,
  ) {
    const payload: FinalizarProduccionDetalleRequest = {
      detalleId,
      cantidadProducida,
      observaciones,
    };
    return this.runMutation(this.api.finalizarDetalleProduccion(payload), {
      successMessage: "Detalle de producción finalizado correctamente.",
      errorMessage: "No se pudo finalizar el detalle de producción.",
      onSuccess: () => {
        this.loadOrdenProduccionPage(
          this.ordenesProduccionPage().page,
          this.ordenesProduccionPage().size,
        );
      },
    });
  }

  registrarConsumo(
    payload: RegistrarConsumoRequest,
    afterSuccess?: () => void,
  ) {
    return this.runMutation(this.api.registrarConsumo(payload), {
      successMessage: "Consumo registrado correctamente.",
      errorMessage: "No se pudo registrar el consumo.",
      onSuccess: () => {
        this.loadMovimientos();
        afterSuccess?.();
      },
    });
  }

  private applySnapshot(payload: BackofficeSnapshot) {
    this.clients.set(payload.clients);
    this.notifications.set(payload.notifications);
    this.notificationCounters.set(payload.notificationCounters);
    this.products.set(payload.products);
    this.reportJobs.set(payload.reportJobs);
    this.categories.set(payload.categories);
    this.quotations.set(payload.quotations);
    this.orders.set(payload.orders);
    this.productionQueue.set(payload.productionQueue);
    this.abastecimientoDashboard.set(payload.abastecimientoDashboard ?? null);
  }

  private runMutation<T>(
    source$: Observable<T>,
    options: {
      successMessage: string;
      errorMessage: string;
      onSuccess?: (value: T) => void;
      reloadAfter?: boolean;
    },
  ) {
    this.setErrorMessage(null);
    this.setActionMessage(null);

    return source$.subscribe({
      next: (value) => {
        options.onSuccess?.(value);
        this.setActionMessage(options.successMessage);
        if (options.reloadAfter) {
          this.loadAll({ preserveActionMessage: true });
        }
      },
      error: () => {
        this.setErrorMessage(options.errorMessage);
      },
    });
  }

  private withCollectionFallback<T>(
    source$: Observable<T[]>,
    section: string,
  ): Observable<T[]> {
    return source$.pipe(
      catchError(() => {
        this.appendLoadError(section);
        return of([] as T[]);
      }),
    );
  }

  private withObjectFallback<T>(
    source$: Observable<T>,
    section: string,
    fallbackValue: T,
  ): Observable<T> {
    return source$.pipe(
      catchError(() => {
        this.appendLoadError(section);
        return of(fallbackValue);
      }),
    );
  }

  private applyPagedResult<T>(
    pageData: PageResponseDto<T>,
    target: { set(value: PageResponseDto<T>): void },
    reloadPreviousPage: (page: number, size: number) => void,
  ) {
    if (
      pageData.content.length === 0 &&
      pageData.totalElements > 0 &&
      pageData.page > 0
    ) {
      reloadPreviousPage(pageData.page - 1, pageData.size);
      return;
    }
    target.set(pageData);
  }

  private appendLoadError(section: string) {
    const current = this.error();
    const next = current
      ? `${current} También falló ${section}.`
      : `No fue posible cargar por completo el panel administrativo. Falló ${section}.`;
    this.setErrorMessage(next);
  }

  private setActionMessage(message: string | null) {
    if (this.actionMessageTimer !== null) {
      window.clearTimeout(this.actionMessageTimer);
      this.actionMessageTimer = null;
    }
    this.actionMessage.set(message);
    if (message) {
      this.actionMessageTimer = window.setTimeout(() => {
        this.actionMessage.set(null);
        this.actionMessageTimer = null;
      }, 6000);
    }
  }

  private setErrorMessage(message: string | null) {
    if (this.errorTimer !== null) {
      window.clearTimeout(this.errorTimer);
      this.errorTimer = null;
    }
    this.error.set(message);
    if (message) {
      this.errorTimer = window.setTimeout(() => {
        this.error.set(null);
        this.errorTimer = null;
      }, 6000);
    }
  }

  private startReportPolling() {
    if (this.reportPollingTimer !== null) {
      window.clearTimeout(this.reportPollingTimer);
      this.reportPollingTimer = null;
    }

    let attempts = 0;
    const poll = () => {
      attempts += 1;

      forkJoin({
        pageData: this.api.getReportJobsPage(
          this.reportJobsPage().page,
          this.reportJobsPage().size || 8,
        ),
        latestJobs: this.api.getReportJobs(),
        notifications: this.api.getNotifications(),
        counters: this.api.getNotificationCounters(),
      }).subscribe({
        next: (payload) => {
          this.reportJobsPage.set(payload.pageData);
          this.reportJobs.set(payload.latestJobs);
          this.notifications.set(payload.notifications);
          this.notificationCounters.set(payload.counters);

          const hasPendingJobs = payload.pageData.content.some(
            (job) => job.status === "PENDIENTE" || job.status === "EN_PROCESO",
          );

          if (hasPendingJobs && attempts < 12) {
            this.reportPollingTimer = window.setTimeout(poll, 2000);
            return;
          }

          this.reportPollingTimer = null;
        },
        error: () => {
          if (attempts < 6) {
            this.reportPollingTimer = window.setTimeout(poll, 2000);
            return;
          }

          this.reportPollingTimer = null;
        },
      });
    };

    poll();
  }

  private refreshNotificationsState() {
    forkJoin({
      notifications: this.api.getNotifications(),
      counters: this.api.getNotificationCounters(),
    }).subscribe({
      next: (payload) => {
        this.notifications.set(payload.notifications);
        this.notificationCounters.set(payload.counters);
      },
      error: () => {
        this.setErrorMessage("No se pudo refrescar el buzón interno.");
      },
    });
  }

  private refreshReportsState() {
    forkJoin({
      latestJobs: this.api.getReportJobs(),
      pageData: this.api.getReportJobsPage(
        this.reportJobsPage().page,
        this.reportJobsPage().size || 8,
      ),
    }).subscribe({
      next: (payload) => {
        this.reportJobs.set(payload.latestJobs);
        this.applyPagedResult(
          payload.pageData,
          this.reportJobsPage,
          (nextPage, nextSize) => this.loadReportJobsPage(nextPage, nextSize),
        );
      },
      error: () => {
        this.setErrorMessage("No se pudo refrescar la bandeja de reportes.");
      },
    });
  }
}
