import { HttpClient } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable, map } from "rxjs";
import { apiConfig } from "../config/api.config";
import type { ApiResponse, PageResponseDto } from "../contracts/api-contracts";
import type { ClientSummary, CreateClientPayload, UpdateClientPayload } from "../../features/clientes/models/client.models";
import type { CreateQuotationPayload, QuotationSummary } from "../../features/cotizaciones/models/quotation.models";
import type { CreateOrderPayload, OrderSummary, UpdateOrderStatusPayload } from "../../features/pedidos/models/order.models";
import type { NotificationActionResult, NotificationCounters, NotificationSummary } from "../../features/notificaciones/models/notification.models";
import type { ProductCategorySummary, ProductSummary, CreateProductPayload, UpdateProductPayload } from "../../features/productos/models/product.models";
import type { ReportActionResult, ReportJobSummary, ReportTypeValue } from "../../features/reportes/models/report.models";
import type { ProductionSummary, UpdateProductionStatusPayload } from "../../features/produccion/models/production.models";

@Injectable({ providedIn: "root" })
export class ApiClientService {
  private readonly http = inject(HttpClient);
  readonly baseUrl = apiConfig.baseUrl;

  getClients(): Observable<ClientSummary[]> {
    return this.get<ClientSummary[]>("/clientes");
  }

  getClientsPage(page = 0, size = 8, query = ""): Observable<PageResponseDto<ClientSummary>> {
    const search = query.trim();
    const querySuffix = search ? `&query=${encodeURIComponent(search)}` : "";
    return this.get<PageResponseDto<ClientSummary>>(`/clientes/paginado?page=${page}&size=${size}${querySuffix}`);
  }

  getReportJobs(limit = 6): Observable<ReportJobSummary[]> {
    return this.get<ReportJobSummary[]>(`/reportes?limit=${limit}`);
  }

  getReportJobsPage(page = 0, size = 8): Observable<PageResponseDto<ReportJobSummary>> {
    return this.get<PageResponseDto<ReportJobSummary>>(`/reportes/paginado?page=${page}&size=${size}`);
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

  getProductsPage(page = 0, size = 8): Observable<PageResponseDto<ProductSummary>> {
    return this.get<PageResponseDto<ProductSummary>>(`/productos/paginado?page=${page}&size=${size}`);
  }

  getCategories(): Observable<ProductCategorySummary[]> {
    return this.get<ProductCategorySummary[]>("/public/catalogo/categorias");
  }

  getQuotations(): Observable<QuotationSummary[]> {
    return this.get<QuotationSummary[]>("/cotizaciones");
  }

  getQuotationsPage(page = 0, size = 8): Observable<PageResponseDto<QuotationSummary>> {
    return this.get<PageResponseDto<QuotationSummary>>(`/cotizaciones/paginado?page=${page}&size=${size}`);
  }

  getOrders(): Observable<OrderSummary[]> {
    return this.get<OrderSummary[]>("/pedidos");
  }

  getOrdersPage(page = 0, size = 8): Observable<PageResponseDto<OrderSummary>> {
    return this.get<PageResponseDto<OrderSummary>>(`/pedidos/paginado?page=${page}&size=${size}`);
  }

  getProductionQueue(): Observable<ProductionSummary[]> {
    return this.get<ProductionSummary[]>("/produccion");
  }

  getProductionQueuePage(page = 0, size = 8): Observable<PageResponseDto<ProductionSummary>> {
    return this.get<PageResponseDto<ProductionSummary>>(`/produccion/paginado?page=${page}&size=${size}`);
  }

  createClient(payload: CreateClientPayload): Observable<ClientSummary> {
    return this.post<ClientSummary>("/clientes", payload);
  }

  updateClient(clientId: number, payload: UpdateClientPayload): Observable<ClientSummary> {
    return this.put<ClientSummary>(`/clientes/${clientId}`, payload);
  }

  createQuotation(payload: CreateQuotationPayload): Observable<QuotationSummary> {
    return this.post<QuotationSummary>("/cotizaciones", payload);
  }

  createOrder(payload: CreateOrderPayload): Observable<OrderSummary> {
    return this.post<OrderSummary>("/pedidos", payload);
  }

  createProduct(payload: CreateProductPayload): Observable<ProductSummary> {
    return this.post<ProductSummary>("/productos", payload);
  }

  updateOrderStatus(orderId: number, payload: UpdateOrderStatusPayload): Observable<OrderSummary> {
    return this.patch<OrderSummary>(`/pedidos/${orderId}/estado`, payload);
  }

  updateProduct(productId: number, payload: UpdateProductPayload): Observable<ProductSummary> {
    return this.put<ProductSummary>(`/productos/${productId}`, payload);
  }

  updateProductionStatus(productionId: number, payload: UpdateProductionStatusPayload): Observable<ProductionSummary> {
    return this.patch<ProductionSummary>(`/produccion/${productionId}/estado`, payload);
  }

  deleteClient(clientId: number): Observable<void> {
    return this.delete<void>(`/clientes/${clientId}`);
  }

  deleteProduct(productId: number): Observable<void> {
    return this.delete<void>(`/productos/${productId}`);
  }

  deleteOrder(orderId: number): Observable<void> {
    return this.delete<void>(`/pedidos/${orderId}`);
  }

  requestReport(reportType: ReportTypeValue): Observable<ReportJobSummary> {
    return this.post<ReportJobSummary>("/reportes", { reportType });
  }

  downloadReport(jobId: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/reportes/${jobId}/descargar`, { responseType: "blob" });
  }

  markNotificationAsRead(notificationId: number): Observable<NotificationSummary> {
    return this.patch<NotificationSummary>(`/notificaciones/${notificationId}/leer`, {});
  }

  archiveNotification(notificationId: number): Observable<NotificationSummary> {
    return this.patch<NotificationSummary>(`/notificaciones/${notificationId}/archivar`, {});
  }

  archiveSelectedNotifications(notificationIds: number[]): Observable<NotificationActionResult> {
    return this.patch<NotificationActionResult>("/notificaciones/archivar-seleccion", { notificationIds });
  }

  archiveReadNotifications(): Observable<NotificationActionResult> {
    return this.patch<NotificationActionResult>("/notificaciones/archivar-leidas", {});
  }

  deleteSelectedReportJobs(jobIds: number[]): Observable<ReportActionResult> {
    return this.requestWithBody<ReportActionResult>("DELETE", "/reportes/seleccionados", { jobIds });
  }

  cleanupOldReportJobs(keepLatest = 10): Observable<ReportActionResult> {
    return this.delete<ReportActionResult>(`/reportes/antiguos?keepLatest=${keepLatest}`);
  }

  private get<T>(path: string): Observable<T> {
    return this.http.get<ApiResponse<T>>(`${this.baseUrl}${path}`).pipe(map((response) => response.data));
  }

  private post<T>(path: string, payload: unknown): Observable<T> {
    return this.http.post<ApiResponse<T>>(`${this.baseUrl}${path}`, payload).pipe(map((response) => response.data));
  }

  private patch<T>(path: string, payload: unknown): Observable<T> {
    return this.http.patch<ApiResponse<T>>(`${this.baseUrl}${path}`, payload).pipe(map((response) => response.data));
  }

  private put<T>(path: string, payload: unknown): Observable<T> {
    return this.http.put<ApiResponse<T>>(`${this.baseUrl}${path}`, payload).pipe(map((response) => response.data));
  }

  private delete<T>(path: string): Observable<T> {
    return this.http.delete<ApiResponse<T>>(`${this.baseUrl}${path}`).pipe(map((response) => response.data));
  }

  private requestWithBody<T>(method: string, path: string, payload: unknown): Observable<T> {
    return this.http.request<ApiResponse<T>>(method, `${this.baseUrl}${path}`, { body: payload })
      .pipe(map((response) => response.data));
  }
}
