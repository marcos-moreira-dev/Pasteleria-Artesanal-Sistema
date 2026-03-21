import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable, map } from "rxjs";
import { apiConfig } from "../config/api.config";
import type { ApiResponse, PageResponseDto } from "../contracts/api-contracts";
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
  UpdateOrderStatusPayload,
} from "../../features/pedidos/models/order.models";
import type {
  NotificationActionResult,
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
  ReportActionResult,
  ReportJobSummary,
  ReportTypeValue,
} from "../../features/reportes/models/report.models";
import type {
  ProductionSummary,
  UpdateProductionStatusPayload,
} from "../../features/produccion/models/production.models";
import type {
  AbastecimientoDashboard,
  ConsumoProduccionSummary,
  CreateInventarioMovimientoRequest,
  CreateItemProveedorRequest,
  CreateOrdenCompraRequest,
  CreateOrdenProduccionRequest,
  CreateProveedorRequest,
  CreateRecetaRequest,
  FinalizarProduccionDetalleRequest,
  IniciarProduccionRequest,
  IngredienteSummary,
  InsumoSummary,
  InventarioMovimientoSummary,
  ItemProveedorSummary,
  OrdenCompraDetailSummary,
  OrdenCompraSummary,
  OrdenProduccionDetalleSummary,
  OrdenProduccionDetailSummary,
  OrdenProduccionSummary,
  ProveedorSummary,
  RecetaDetailSummary,
  RecetaSummary,
  RecibirOrdenCompraRequest,
  RegistrarConsumoRequest,
  UpdateItemProveedorRequest,
  UpdateOrdenCompraEstadoRequest,
  UpdateOrdenCompraRequest,
  UpdateOrdenProduccionEstadoRequest,
  UpdateOrdenProduccionRequest,
  UpdateProveedorRequest,
  UpdateRecetaRequest,
  UmedidaSummary,
} from "../../features/abastecimiento/models/abastecimiento.models";

@Injectable({ providedIn: "root" })
export class ApiClientService {
  private readonly http = inject(HttpClient);
  readonly baseUrl = apiConfig.baseUrl;

  getClients(): Observable<ClientSummary[]> {
    return this.get<ClientSummary[]>("/clientes");
  }

  getClientsPage(
    page = 0,
    size = 8,
    query = "",
  ): Observable<PageResponseDto<ClientSummary>> {
    const search = query.trim();
    const querySuffix = search ? `&query=${encodeURIComponent(search)}` : "";
    return this.get<PageResponseDto<ClientSummary>>(
      `/clientes/paginado?page=${page}&size=${size}${querySuffix}`,
    );
  }

  getReportJobs(limit = 6): Observable<ReportJobSummary[]> {
    return this.get<ReportJobSummary[]>(`/reportes?limit=${limit}`);
  }

  getReportJobsPage(
    page = 0,
    size = 8,
  ): Observable<PageResponseDto<ReportJobSummary>> {
    return this.get<PageResponseDto<ReportJobSummary>>(
      `/reportes/paginado?page=${page}&size=${size}`,
    );
  }

  getNotifications(limit = 8): Observable<NotificationSummary[]> {
    return this.get<NotificationSummary[]>(`/notificaciones?limit=${limit}`);
  }

  getNotificationCounters(): Observable<NotificationCounters> {
    return this.get<NotificationCounters>("/notificaciones/resumen");
  }

  getProducts(): Observable<ProductSummary[]> {
    return this.get<ProductSummary[]>("/productos");
  }

  getProductsPage(
    page = 0,
    size = 8,
  ): Observable<PageResponseDto<ProductSummary>> {
    return this.get<PageResponseDto<ProductSummary>>(
      `/productos/paginado?page=${page}&size=${size}`,
    );
  }

  getCategories(): Observable<ProductCategorySummary[]> {
    return this.get<ProductCategorySummary[]>("/public/catalogo/categorias");
  }

  getQuotations(): Observable<QuotationSummary[]> {
    return this.get<QuotationSummary[]>("/cotizaciones");
  }

  getQuotationsPage(
    page = 0,
    size = 8,
  ): Observable<PageResponseDto<QuotationSummary>> {
    return this.get<PageResponseDto<QuotationSummary>>(
      `/cotizaciones/paginado?page=${page}&size=${size}`,
    );
  }

  getOrders(): Observable<OrderSummary[]> {
    return this.get<OrderSummary[]>("/pedidos");
  }

  getOrdersPage(page = 0, size = 8): Observable<PageResponseDto<OrderSummary>> {
    return this.get<PageResponseDto<OrderSummary>>(
      `/pedidos/paginado?page=${page}&size=${size}`,
    );
  }

  getProductionQueue(): Observable<ProductionSummary[]> {
    return this.get<ProductionSummary[]>("/produccion");
  }

  getProductionQueuePage(
    page = 0,
    size = 8,
  ): Observable<PageResponseDto<ProductionSummary>> {
    return this.get<PageResponseDto<ProductionSummary>>(
      `/produccion/paginado?page=${page}&size=${size}`,
    );
  }

  createClient(payload: CreateClientPayload): Observable<ClientSummary> {
    return this.post<ClientSummary>("/clientes", payload);
  }

  updateClient(
    clientId: number,
    payload: UpdateClientPayload,
  ): Observable<ClientSummary> {
    return this.put<ClientSummary>(`/clientes/${clientId}`, payload);
  }

  createQuotation(
    payload: CreateQuotationPayload,
  ): Observable<QuotationSummary> {
    return this.post<QuotationSummary>("/cotizaciones", payload);
  }

  createOrder(payload: CreateOrderPayload): Observable<OrderSummary> {
    return this.post<OrderSummary>("/pedidos", payload);
  }

  createProduct(payload: CreateProductPayload): Observable<ProductSummary> {
    return this.post<ProductSummary>("/productos", payload);
  }

  updateOrderStatus(
    orderId: number,
    payload: UpdateOrderStatusPayload,
  ): Observable<OrderSummary> {
    return this.patch<OrderSummary>(`/pedidos/${orderId}/estado`, payload);
  }

  updateProduct(
    productId: number,
    payload: UpdateProductPayload,
  ): Observable<ProductSummary> {
    return this.put<ProductSummary>(`/productos/${productId}`, payload);
  }

  updateProductionStatus(
    productionId: number,
    payload: UpdateProductionStatusPayload,
  ): Observable<ProductionSummary> {
    return this.patch<ProductionSummary>(
      `/produccion/${productionId}/estado`,
      payload,
    );
  }

  deleteClient(clientId: number): Observable<void> {
    return this.delete<void>(`/clientes/${clientId}`);
  }

  deleteProduct(productId: number): Observable<void> {
    return this.delete<void>(`/productos/${productId}`);
  }

  uploadProductImage(productId: number, formData: FormData): Observable<string> {
    return this.http.post<ApiResponse<string>>(
      `${this.baseUrl}/productos/${productId}/imagen`,
      formData
    ).pipe(map(response => response.data || ""));
  }

  deleteOrder(orderId: number): Observable<void> {
    return this.delete<void>(`/pedidos/${orderId}`);
  }

  requestReport(reportType: ReportTypeValue): Observable<ReportJobSummary> {
    return this.post<ReportJobSummary>("/reportes", { reportType });
  }

  downloadReport(jobId: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/reportes/${jobId}/descargar`, {
      responseType: "blob",
    });
  }

  markNotificationAsRead(
    notificationId: number,
  ): Observable<NotificationSummary> {
    return this.patch<NotificationSummary>(
      `/notificaciones/${notificationId}/leer`,
      {},
    );
  }

  archiveNotification(notificationId: number): Observable<NotificationSummary> {
    return this.patch<NotificationSummary>(
      `/notificaciones/${notificationId}/archivar`,
      {},
    );
  }

  archiveSelectedNotifications(
    notificationIds: number[],
  ): Observable<NotificationActionResult> {
    return this.patch<NotificationActionResult>(
      "/notificaciones/archivar-seleccion",
      { notificationIds },
    );
  }

  archiveReadNotifications(): Observable<NotificationActionResult> {
    return this.patch<NotificationActionResult>(
      "/notificaciones/archivar-leidas",
      {},
    );
  }

  deleteSelectedReportJobs(jobIds: number[]): Observable<ReportActionResult> {
    return this.requestWithBody<ReportActionResult>(
      "DELETE",
      "/reportes/seleccionados",
      { jobIds },
    );
  }

  cleanupOldReportJobs(keepLatest = 10): Observable<ReportActionResult> {
    return this.delete<ReportActionResult>(
      `/reportes/antiguos?keepLatest=${keepLatest}`,
    );
  }

  // === ABASTECIMIENTO ===

  // Unidades de medida
  getUmedidas(): Observable<UmedidaSummary[]> {
    return this.get<UmedidaSummary[]>("/abastecimiento/umedidas");
  }

  // Ingredientes
  getIngredientesPage(
    page = 0,
    size = 8,
    query = "",
    tipo = "",
  ): Observable<PageResponseDto<IngredienteSummary>> {
    const search = query.trim();
    const tipoSuffix = tipo ? `&tipo=${encodeURIComponent(tipo)}` : "";
    const querySuffix = search ? `&query=${encodeURIComponent(search)}` : "";
    return this.get<PageResponseDto<IngredienteSummary>>(
      `/abastecimiento/ingredientes/paginado?page=${page}&size=${size}${querySuffix}${tipoSuffix}`,
    );
  }

  getIngredientes(): Observable<IngredienteSummary[]> {
    return this.get<IngredienteSummary[]>("/abastecimiento/ingredientes");
  }

  // Insumos
  getInsumosPage(
    page = 0,
    size = 8,
    query = "",
    tipo = "",
  ): Observable<PageResponseDto<InsumoSummary>> {
    const search = query.trim();
    const tipoSuffix = tipo ? `&tipo=${encodeURIComponent(tipo)}` : "";
    const querySuffix = search ? `&query=${encodeURIComponent(search)}` : "";
    return this.get<PageResponseDto<InsumoSummary>>(
      `/abastecimiento/insumos/paginado?page=${page}&size=${size}${querySuffix}${tipoSuffix}`,
    );
  }

  getInsumos(): Observable<InsumoSummary[]> {
    return this.get<InsumoSummary[]>("/abastecimiento/insumos");
  }

  // Proveedores
  getProveedoresPage(
    page = 0,
    size = 8,
    query = "",
  ): Observable<PageResponseDto<ProveedorSummary>> {
    const search = query.trim();
    const querySuffix = search ? `&query=${encodeURIComponent(search)}` : "";
    return this.get<PageResponseDto<ProveedorSummary>>(
      `/abastecimiento/proveedores/paginado?page=${page}&size=${size}${querySuffix}`,
    );
  }

  getProveedores(): Observable<ProveedorSummary[]> {
    return this.get<ProveedorSummary[]>("/abastecimiento/proveedores");
  }

  createProveedor(
    payload: CreateProveedorRequest,
  ): Observable<ProveedorSummary> {
    return this.post<ProveedorSummary>("/abastecimiento/proveedores", payload);
  }

  updateProveedor(
    proveedorId: number,
    payload: UpdateProveedorRequest,
  ): Observable<ProveedorSummary> {
    return this.put<ProveedorSummary>(
      `/abastecimiento/proveedores/${proveedorId}`,
      payload,
    );
  }

  deleteProveedor(proveedorId: number): Observable<void> {
    return this.delete<void>(`/abastecimiento/proveedores/${proveedorId}`);
  }

  toggleProveedorActivo(proveedorId: number): Observable<ProveedorSummary> {
    return this.patch<ProveedorSummary>(
      `/abastecimiento/proveedores/${proveedorId}/toggle-activo`,
      {},
    );
  }

  // Items Proveedor
  getItemsProveedorPorItem(
    itemTipo: string,
    itemId: number,
  ): Observable<ItemProveedorSummary[]> {
    return this.get<ItemProveedorSummary[]>(
      `/abastecimiento/items-proveedor/por-item?tipo=${encodeURIComponent(itemTipo)}&itemId=${itemId}`,
    );
  }

  getItemsProveedorPorProveedor(
    proveedorId: number,
  ): Observable<ItemProveedorSummary[]> {
    return this.get<ItemProveedorSummary[]>(
      `/abastecimiento/items-proveedor/por-proveedor/${proveedorId}`,
    );
  }

  createItemProveedor(
    payload: CreateItemProveedorRequest,
  ): Observable<ItemProveedorSummary> {
    return this.post<ItemProveedorSummary>(
      "/abastecimiento/items-proveedor",
      payload,
    );
  }

  updateItemProveedor(
    itemId: number,
    payload: UpdateItemProveedorRequest,
  ): Observable<ItemProveedorSummary> {
    return this.put<ItemProveedorSummary>(
      `/abastecimiento/items-proveedor/${itemId}`,
      payload,
    );
  }

  deleteItemProveedor(itemId: number): Observable<void> {
    return this.delete<void>(`/abastecimiento/items-proveedor/${itemId}`);
  }

  // Inventario (Movimientos)
  getMovimientos(
    itemTipo?: string,
    itemId?: number,
    tipoMovimiento?: string,
    fechaDesde?: string,
    fechaHasta?: string,
  ): Observable<InventarioMovimientoSummary[]> {
    const params = new URLSearchParams();
    if (itemTipo) params.append("itemTipo", itemTipo);
    if (itemId !== undefined) params.append("itemId", String(itemId));
    if (tipoMovimiento) params.append("tipoMovimiento", tipoMovimiento);
    if (fechaDesde) params.append("fechaDesde", fechaDesde);
    if (fechaHasta) params.append("fechaHasta", fechaHasta);
    const query = params.toString();
    return this.get<InventarioMovimientoSummary[]>(
      `/abastecimiento/inventario/movimientos${query ? `?${query}` : ""}`,
    );
  }

  getMovimientosByReferencia(
    referenciaTipo: string,
    referenciaId: string,
  ): Observable<InventarioMovimientoSummary[]> {
    return this.get<InventarioMovimientoSummary[]>(
      `/abastecimiento/inventario/movimientos/por-referencia?tipo=${encodeURIComponent(referenciaTipo)}&referenciaId=${encodeURIComponent(referenciaId)}`,
    );
  }

  createMovimiento(
    payload: CreateInventarioMovimientoRequest,
  ): Observable<InventarioMovimientoSummary> {
    return this.post<InventarioMovimientoSummary>(
      "/abastecimiento/inventario/movimientos",
      payload,
    );
  }

  // Recetas
  getRecetasPage(
    page = 0,
    size = 8,
    query = "",
  ): Observable<PageResponseDto<RecetaSummary>> {
    const search = query.trim();
    const querySuffix = search ? `&query=${encodeURIComponent(search)}` : "";
    return this.get<PageResponseDto<RecetaSummary>>(
      `/abastecimiento/recetas/paginado?page=${page}&size=${size}${querySuffix}`,
    );
  }

  getRecetas(): Observable<RecetaSummary[]> {
    return this.get<RecetaSummary[]>("/abastecimiento/recetas");
  }

  getRecetaDetail(recetaId: number): Observable<RecetaDetailSummary> {
    return this.get<RecetaDetailSummary>(`/abastecimiento/recetas/${recetaId}`);
  }

  getRecetasPorProducto(productoId: number): Observable<RecetaSummary[]> {
    return this.get<RecetaSummary[]>(
      `/abastecimiento/recetas/por-producto/${productoId}`,
    );
  }

  createReceta(payload: CreateRecetaRequest): Observable<RecetaSummary> {
    return this.post<RecetaSummary>("/abastecimiento/recetas", payload);
  }

  updateReceta(
    recetaId: number,
    payload: UpdateRecetaRequest,
  ): Observable<RecetaSummary> {
    return this.put<RecetaSummary>(
      `/abastecimiento/recetas/${recetaId}`,
      payload,
    );
  }

  deleteReceta(recetaId: number): Observable<void> {
    return this.delete<void>(`/abastecimiento/recetas/${recetaId}`);
  }

  toggleRecetaActiva(recetaId: number): Observable<RecetaSummary> {
    return this.patch<RecetaSummary>(
      `/abastecimiento/recetas/${recetaId}/toggle-activa`,
      {},
    );
  }

  // Órdenes de Compra
  getOrdenesCompraPage(
    page = 0,
    size = 8,
    query = "",
    estado = "",
  ): Observable<PageResponseDto<OrdenCompraSummary>> {
    const search = query.trim();
    const querySuffix = search ? `&query=${encodeURIComponent(search)}` : "";
    const estadoSuffix = estado ? `&estado=${encodeURIComponent(estado)}` : "";
    return this.get<PageResponseDto<OrdenCompraSummary>>(
      `/abastecimiento/ordenes-compra/paginado?page=${page}&size=${size}${querySuffix}${estadoSuffix}`,
    );
  }

  getOrdenCompraDetail(ordenId: number): Observable<OrdenCompraDetailSummary> {
    return this.get<OrdenCompraDetailSummary>(
      `/abastecimiento/ordenes-compra/${ordenId}`,
    );
  }

  getOrdenesCompraPorProveedor(
    proveedorId: number,
  ): Observable<OrdenCompraSummary[]> {
    return this.get<OrdenCompraSummary[]>(
      `/abastecimiento/ordenes-compra/por-proveedor/${proveedorId}`,
    );
  }

  createOrdenCompra(
    payload: CreateOrdenCompraRequest,
  ): Observable<OrdenCompraSummary> {
    return this.post<OrdenCompraSummary>(
      "/abastecimiento/ordenes-compra",
      payload,
    );
  }

  updateOrdenCompra(
    ordenId: number,
    payload: UpdateOrdenCompraRequest,
  ): Observable<OrdenCompraSummary> {
    return this.put<OrdenCompraSummary>(
      `/abastecimiento/ordenes-compra/${ordenId}`,
      payload,
    );
  }

  updateOrdenCompraEstado(
    ordenId: number,
    payload: UpdateOrdenCompraEstadoRequest,
  ): Observable<OrdenCompraSummary> {
    return this.patch<OrdenCompraSummary>(
      `/abastecimiento/ordenes-compra/${ordenId}/estado`,
      payload,
    );
  }

  receiveOrdenCompra(
    ordenId: number,
    payload: RecibirOrdenCompraRequest,
  ): Observable<OrdenCompraDetailSummary> {
    return this.post<OrdenCompraDetailSummary>(
      `/abastecimiento/ordenes-compra/${ordenId}/recibir`,
      payload,
    );
  }

  // Órdenes de Producción
  getOrdenesProduccionPage(
    page = 0,
    size = 8,
    query = "",
  ): Observable<PageResponseDto<OrdenProduccionSummary>> {
    const search = query.trim();
    const querySuffix = search ? `&query=${encodeURIComponent(search)}` : "";
    return this.get<PageResponseDto<OrdenProduccionSummary>>(
      `/abastecimiento/ordenes-produccion/paginado?page=${page}&size=${size}${querySuffix}`,
    );
  }

  getOrdenProduccionDetail(
    ordenId: number,
  ): Observable<OrdenProduccionDetailSummary> {
    return this.get<OrdenProduccionDetailSummary>(
      `/abastecimiento/ordenes-produccion/${ordenId}`,
    );
  }

  createOrdenProduccion(
    payload: CreateOrdenProduccionRequest,
  ): Observable<OrdenProduccionSummary> {
    return this.post<OrdenProduccionSummary>(
      "/abastecimiento/ordenes-produccion",
      payload,
    );
  }

  updateOrdenProduccion(
    ordenId: number,
    payload: UpdateOrdenProduccionRequest,
  ): Observable<OrdenProduccionSummary> {
    return this.put<OrdenProduccionSummary>(
      `/abastecimiento/ordenes-produccion/${ordenId}`,
      payload,
    );
  }

  updateOrdenProduccionEstado(
    ordenId: number,
    payload: UpdateOrdenProduccionEstadoRequest,
  ): Observable<OrdenProduccionSummary> {
    return this.patch<OrdenProduccionSummary>(
      `/abastecimiento/ordenes-produccion/${ordenId}/estado`,
      payload,
    );
  }

  iniciarProduccion(
    payload: IniciarProduccionRequest,
  ): Observable<OrdenProduccionDetailSummary> {
    return this.post<OrdenProduccionDetailSummary>(
      "/abastecimiento/ordenes-produccion/iniciar",
      payload,
    );
  }

  finalizarDetalleProduccion(
    payload: FinalizarProduccionDetalleRequest,
  ): Observable<OrdenProduccionDetalleSummary> {
    return this.post<OrdenProduccionDetalleSummary>(
      "/abastecimiento/ordenes-produccion/finalizar-detalle",
      payload,
    );
  }

  // Consumos
  getConsumosPorDetalle(
    detalleId: number,
  ): Observable<ConsumoProduccionSummary[]> {
    return this.get<ConsumoProduccionSummary[]>(
      `/abastecimiento/consumos/por-detalle/${detalleId}`,
    );
  }

  registrarConsumo(
    payload: RegistrarConsumoRequest,
  ): Observable<ConsumoProduccionSummary[]> {
    return this.post<ConsumoProduccionSummary[]>(
      "/abastecimiento/consumos",
      payload,
    );
  }

  // Dashboard
  getAbastecimientoDashboard(): Observable<AbastecimientoDashboard> {
    return this.get<AbastecimientoDashboard>("/abastecimiento/dashboard");
  }

  private get<T>(path: string): Observable<T> {
    return this.http
      .get<ApiResponse<T>>(`${this.baseUrl}${path}`)
      .pipe(map((response) => response.data));
  }

  private post<T>(path: string, payload: unknown): Observable<T> {
    return this.http
      .post<ApiResponse<T>>(`${this.baseUrl}${path}`, payload)
      .pipe(map((response) => response.data));
  }

  private patch<T>(path: string, payload: unknown): Observable<T> {
    return this.http
      .patch<ApiResponse<T>>(`${this.baseUrl}${path}`, payload)
      .pipe(map((response) => response.data));
  }

  private put<T>(path: string, payload: unknown): Observable<T> {
    return this.http
      .put<ApiResponse<T>>(`${this.baseUrl}${path}`, payload)
      .pipe(map((response) => response.data));
  }

  private delete<T>(path: string): Observable<T> {
    return this.http
      .delete<ApiResponse<T>>(`${this.baseUrl}${path}`)
      .pipe(map((response) => response.data));
  }

  private requestWithBody<T>(
    method: string,
    path: string,
    payload: unknown,
  ): Observable<T> {
    return this.http
      .request<
        ApiResponse<T>
      >(method, `${this.baseUrl}${path}`, { body: payload })
      .pipe(map((response) => response.data));
  }
}
