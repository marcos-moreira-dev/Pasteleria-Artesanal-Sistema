/**
 * @fileoverview Modelos de datos del Módulo de Abastecimiento
 *
 * Este archivo define todas las interfaces TypeScript que representan
 * los contratos de datos entre el frontend Angular y el backend Java.
 *
 * <h2>ARQUITECTURA DE TIPOS</h2>
 *
 * <h3>Type Aliases vs Interfaces</h3>
 * Se usan type aliases para:
 * <ul>
 *   <li>Union types (Estados, Tipos enumerados)</li>
 *   <li>Tipos primitivos con semántica específica</li>
 * </ul>
 *
 * Se usan interfaces para:
 * <ul>
 *   <li>Objetos complejos (DTOs)</li>
 *   <li>Estructuras que pueden extenderse</li>
 *   <li>Contratos de API</li>
 * </ul>
 *
 * <h3>CONVENCIONES DE NOMBRES</h3>
 * <ul>
 *   <li><b>*Summary</b>: DTO de respuesta (GET) - datos para mostrar en tablas/listados</li>
 *   <li><b>*Request</b>: DTO de solicitud (POST/PUT/PATCH) - datos para crear/actualizar</li>
 *   <li><b>*Detail*</b>: DTO extendido con relaciones (ej: Receta + Detalles)</li>
 * </ul>
 *
 * <h2>INTEGRACIÓN CON BACKEND</h2>
 *
 * <h3>Mapeo de tipos Java a TypeScript</h3>
 * <table>
 *   <tr><th>Java</th><th>TypeScript</th></tr>
 *   <tr><td>Long</td><td>number</td></tr>
 *   <tr><td>String</td><td>string</td></tr>
 *   <tr><td>BigDecimal</td><td>number</td></tr>
 *   <tr><td>Boolean</td><td>boolean</td></tr>
 *   <tr><td>OffsetDateTime</td><td>string (ISO 8601)</td></tr>
 *   <tr><td>Enum</td><td>Union type</td></tr>
 *   <tr><td>Optional&lt;T&gt;</td><td>T | undefined</td></tr>
 * </table>
 *
 * <h2>USO CON SIGNALS</h2>
 *
 * Estas interfaces se usan con Angular Signals para estado reactivo:
 * <pre>
 * // Definir señal tipada
 * readonly proveedores = signal&lt;ProveedorSummary[]&gt;([]);
 *
 * // Señal computada
 * readonly proveedoresActivos = computed(() =>
 *   this.proveedores().filter(p => p.activo)
 * );
 *
 * // Cargar datos
 * this.proveedores.set(response.data);
 * </pre>
 *
 * <h2>VALIDACIÓN</h2>
 *
 * Las validaciones se hacen en tres niveles:
 * <ol>
 *   <li><b>Frontend (UI)</b>: Feedback inmediato al usuario</li>
 *   <li><b>Angular Forms</b>: Validadores (Validators.required, etc.)</li>
 *   <li><b>Backend</b>: Bean Validation (@NotNull, @Size, etc.)</li>
 * </ol>
 *
 * @module AbastecimientoModels
 * @author Pastelería Development Team
 * @since 1.0.0
 */

// ============================================================
// Common / Shared Types
// ============================================================

export type ItemTipo = "INGREDIENTE" | "INSUMO";

export type TipoUnidadMedida = "PESO" | "VOLUMEN" | "UNIDAD" | "ENVASE";

export type TipoMovimiento =
  | "ENTRADA_COMPRA"
  | "ENTRADA_AJUSTE"
  | "SALIDA_PRODUCCION"
  | "SALIDA_MERMA"
  | "SALIDA_AJUSTE";

export type EstadoOrdenCompra =
  | "BORRADOR"
  | "ENVIADA"
  | "RECIBIDA_PARCIAL"
  | "RECIBIDA"
  | "CANCELADA";

export type EstadoDocumentoCompra = "BORRADOR" | "REGISTRADO" | "ANULADO";

export type EstadoDocumentoPagar =
  | "PENDIENTE"
  | "PAGADO_PARCIAL"
  | "PAGADO"
  | "ANULADO";

export type EstadoOrdenProduccion =
  | "PENDIENTE"
  | "EN_PREPARACION"
  | "FINALIZADA"
  | "CANCELADA";

export type EstadoDetalleProduccion =
  | "PENDIENTE"
  | "EN_PRODUCCION"
  | "FINALIZADO"
  | "CANCELADO";

export type AlertaTipo = "CRITICO" | "BAJO" | "RIESGO" | "BLOQUEO";

// ============================================================
// Unidades de Medida
// ============================================================

export interface UmedidaSummary {
  id: number;
  codigo: string;
  nombre: string;
  abreviatura: string;
  tipo: TipoUnidadMedida;
  decimales: number;
  activo: boolean;
}

export interface CreateUmedidaRequest {
  codigo: string;
  nombre: string;
  abreviatura: string;
  tipo: TipoUnidadMedida;
  decimales?: number;
}

export interface UpdateUmedidaRequest {
  codigo?: string;
  nombre?: string;
  abreviatura?: string;
  tipo?: TipoUnidadMedida;
  decimales?: number;
  activo?: boolean;
}

// ============================================================
// Ingredientes
// ============================================================

export interface IngredienteSummary {
  id: number;
  umedidaId: number;
  umedidaNombre: string;
  codigo: string;
  nombre: string;
  descripcion?: string;
  stockMinimo: number;
  stockActual: number;
  costoReferencial: number;
  activo: boolean;
  createdAt: string;
}

export interface CreateIngredienteRequest {
  umedidaId: number;
  codigo: string;
  nombre: string;
  descripcion?: string;
  stockMinimo?: number;
  stockActual?: number;
  costoReferencial?: number;
}

export interface UpdateIngredienteRequest {
  umedidaId?: number;
  codigo?: string;
  nombre?: string;
  descripcion?: string;
  stockMinimo?: number;
  stockActual?: number;
  costoReferencial?: number;
  activo?: boolean;
}

// ============================================================
// Insumos
// ============================================================

export interface InsumoSummary {
  id: number;
  umedidaId: number;
  umedidaNombre: string;
  codigo: string;
  nombre: string;
  descripcion?: string;
  stockMinimo: number;
  stockActual: number;
  costoReferencial: number;
  activo: boolean;
  createdAt: string;
}

export interface CreateInsumoRequest {
  umedidaId: number;
  codigo: string;
  nombre: string;
  descripcion?: string;
  stockMinimo?: number;
  stockActual?: number;
  costoReferencial?: number;
}

export interface UpdateInsumoRequest {
  umedidaId?: number;
  codigo?: string;
  nombre?: string;
  descripcion?: string;
  stockMinimo?: number;
  stockActual?: number;
  costoReferencial?: number;
  activo?: boolean;
}

// ============================================================
// Proveedores
// ============================================================

export interface ProveedorSummary {
  id: number;
  codigo: string;
  nombre: string;
  telefono?: string;
  correo?: string;
  direccion?: string;
  observaciones?: string;
  activo: boolean;
  createdAt: string;
}

export interface CreateProveedorRequest {
  codigo: string;
  nombre: string;
  telefono?: string;
  correo?: string;
  direccion?: string;
  observaciones?: string;
}

export interface UpdateProveedorRequest {
  codigo?: string;
  nombre?: string;
  telefono?: string;
  correo?: string;
  direccion?: string;
  observaciones?: string;
  activo?: boolean;
}

// ============================================================
// Items Proveedor
// ============================================================

export interface ItemProveedorSummary {
  id: number;
  itemTipo: ItemTipo;
  itemId: number;
  itemNombre: string;
  proveedorId: number;
  proveedorNombre: string;
  precioSuministro: number;
  esPrincipal: boolean;
  activo: boolean;
}

export interface CreateItemProveedorRequest {
  itemTipo: ItemTipo;
  itemId: number;
  proveedorId: number;
  precioSuministro: number;
  esPrincipal?: boolean;
}

export interface UpdateItemProveedorRequest {
  proveedorId?: number;
  precioSuministro?: number;
  esPrincipal?: boolean;
  activo?: boolean;
}

// ============================================================
// Inventario / Movimientos
// ============================================================

export interface InventarioMovimientoSummary {
  id: number;
  itemTipo: ItemTipo;
  itemId: number;
  itemNombre: string;
  tipoMovimiento: TipoMovimiento;
  cantidad: number;
  saldoAnterior?: number;
  saldoPosterior: number;
  referenciaTipo?: string;
  referenciaId?: string;
  motivoSalida?: string;
  observaciones?: string;
  fechaMovimiento: string;
  registradoPorId: number;
  registradoPorNombre: string;
}

export interface CreateInventarioMovimientoRequest {
  itemTipo: ItemTipo;
  itemId: number;
  tipoMovimiento: TipoMovimiento;
  cantidad: number;
  referenciaTipo?: string;
  referenciaId?: string;
  motivoSalida?: string;
  observaciones?: string;
}

export interface InventarioItemSummary {
  itemTipo: ItemTipo;
  itemId: number;
  itemNombre: string;
  itemCodigo: string;
  stockMinimo: number;
  stockActual: number;
  umedidaNombre: string;
  costoReferencial: number;
}

// ============================================================
// Recetas
// ============================================================

export interface RecetaSummary {
  id: number;
  productoId: number | null;
  productoNombre?: string;
  nombre: string;
  rendimientoBase: number;
  costoEstimado: number;
  observaciones?: string;
  esActiva: boolean;
  createdAt: string;
  createdById: number;
  createdByNombre: string;
}

export interface DetalleRecetaSummary {
  id: number;
  recetaId: number;
  ingredienteId: number;
  ingredienteNombre: string;
  ingredienteUnidad: string;
  cantidadBase: number;
  rendimientoPorUnidad?: number;
  esParaPorcion: boolean;
  observaciones?: string;
}

export interface RecetaDetailSummary extends RecetaSummary {
  detalles: DetalleRecetaSummary[];
}

export interface DetalleRecetaItem {
  ingredienteId: number;
  cantidadBase: number;
  rendimientoPorUnidad?: number;
  esParaPorcion?: boolean;
  observaciones?: string;
}

export interface CreateRecetaRequest {
  productoId: number | null;
  nombre: string;
  rendimientoBase: number;
  observaciones?: string;
  esActiva?: boolean;
  detalles: DetalleRecetaItem[];
}

export interface UpdateRecetaRequest {
  nombre?: string;
  rendimientoBase?: number;
  observaciones?: string;
  esActiva?: boolean;
  detalles?: DetalleRecetaItem[];
}

// ============================================================
// Órdenes de Compra
// ============================================================

export interface OrdenCompraSummary {
  id: number;
  proveedorId: number;
  proveedorNombre: string;
  codigo: string;
  estado: EstadoOrdenCompra;
  observaciones?: string;
  fechaEntregaEstimada?: string;
  createdAt: string;
  createdById?: number;
  createdByNombre?: string;
}

export interface OrdenCompraDetalleSummary {
  id: number;
  ordenCompraId: number;
  itemTipo: ItemTipo;
  itemId: number;
  itemNombre: string;
  cantidad: number;
  cantidadRecibida: number;
  precioUnitario: number;
}

export interface OrdenCompraDetailSummary extends OrdenCompraSummary {
  detalles: OrdenCompraDetalleSummary[];
  totalEstimado: number;
}

export interface OrdenCompraDetalleItem {
  itemTipo: ItemTipo;
  itemId: number;
  cantidad: number;
  precioUnitario: number;
}

export interface CreateOrdenCompraRequest {
  proveedorId: number;
  codigo: string;
  observaciones?: string;
  fechaEntregaEstimada?: string | null;
  detalles: OrdenCompraDetalleItem[];
}

export interface UpdateOrdenCompraRequest {
  proveedorId?: number;
  codigo?: string;
  observaciones?: string;
  fechaEntregaEstimada?: string | null;
  detalles?: OrdenCompraDetalleItem[];
}

export interface UpdateOrdenCompraEstadoRequest {
  estado: EstadoOrdenCompra;
  observaciones?: string;
}

export interface RecibirItem {
  detalleId: number;
  cantidadRecibida: number;
}

export interface RecibirOrdenCompraRequest {
  items: RecibirItem[];
}

export interface RegistrarDocumentoCompraRequest {
  numeroDocumento?: string;
  fechaEmision?: string;
  fechaVencimiento?: string;
  impuesto?: number;
  observaciones?: string;
}

export interface DocumentoCompraSummary {
  id: number;
  ordenCompraId: number;
  ordenCompraCodigo: string;
  proveedorId: number;
  proveedorNombre: string;
  numeroDocumento: string;
  estado: EstadoDocumentoCompra;
  fechaEmision: string;
  subtotal: number;
  impuesto: number;
  total: number;
  observaciones?: string;
  createdAt: string;
}

export interface DocumentoPagarSummary {
  id: number;
  documentoCompraId: number;
  proveedorId: number;
  proveedorNombre: string;
  codigo: string;
  estado: EstadoDocumentoPagar;
  fechaEmision: string;
  fechaVencimiento?: string;
  total: number;
  saldo: number;
  observaciones?: string;
  createdAt: string;
}

export interface CompraFinancieraSummary {
  documentoCompra: DocumentoCompraSummary;
  documentoPagar: DocumentoPagarSummary;
}

// ============================================================
// Órdenes de Producción
// ============================================================

export interface OrdenProduccionSummary {
  id: number;
  produccionId: number | null;
  codigo: string;
  estado: EstadoOrdenProduccion;
  observaciones?: string;
  fechaCreacion: string;
  fechaInicio?: string;
  fechaFinalizacion?: string;
  responsableId: number | null;
  responsableNombre?: string;
}

export interface OrdenProduccionDetalleSummary {
  id: number;
  ordenProduccionId: number;
  pedidoDetalleId: number | null;
  productoId: number;
  productoNombre: string;
  recetaId: number | null;
  recetaNombre?: string;
  cantidadProducir: number;
  cantidadProducida: number;
  estadoDetalle: EstadoDetalleProduccion;
  observaciones?: string;
}

export interface OrdenProduccionDetailSummary extends OrdenProduccionSummary {
  detalles: OrdenProduccionDetalleSummary[];
}

export interface CreateOrdenProduccionRequest {
  observaciones?: string;
  detalles: CreateOrdenProduccionDetalleItem[];
}

export interface CreateOrdenProduccionDetalleItem {
  productoId: number;
  cantidadProducir: number;
  observaciones?: string;
}

export interface UpdateOrdenProduccionRequest {
  observaciones?: string;
}

export interface UpdateOrdenProduccionEstadoRequest {
  estado: EstadoOrdenProduccion;
  observaciones?: string;
}

export interface IniciarProduccionRequest {
  detalleIds?: number[];
}

export interface FinalizarProduccionDetalleRequest {
  detalleId: number;
  cantidadProducida: number;
  observaciones?: string;
}

// ============================================================
// Consumos
// ============================================================

export interface ConsumoProduccionSummary {
  id: number;
  ordenProduccionDetalleId: number;
  itemTipo: ItemTipo;
  itemId: number;
  itemNombre: string;
  cantidadConsumida: number;
  cantidadMerma: number;
  observaciones?: string;
}

export interface ConsumoItem {
  itemTipo: ItemTipo;
  itemId: number;
  cantidadConsumida: number;
  cantidadMerma?: number;
  observaciones?: string;
}

export interface RegistrarConsumoRequest {
  ordenProduccionDetalleId: number;
  items: ConsumoItem[];
}

// ============================================================
// Dashboard
// ============================================================

export interface AbastecimientoAlerta {
  tipo: AlertaTipo;
  itemTipo: ItemTipo;
  itemId: number;
  itemNombre: string;
  itemCodigo: string;
  mensaje: string;
  cantidadActual: number;
  cantidadMinima: number;
  coberturaDias?: number;
}

export interface SugerenciaReposicion {
  itemTipo: ItemTipo;
  itemId: number;
  itemNombre: string;
  codigo: string;
  stockActual: number;
  stockMinimo: number;
  cantidadSugerida: number;
  proveedorPrincipal?: string;
  precioUnitario?: number;
}

export interface ProveedorOConActiva {
  id: number;
  nombre: string;
  telefono?: string;
  ordenesActivas: number;
  ultimaOrdenFecha?: string;
}

export interface AbastecimientoDashboard {
  itemsCriticos: number;
  itemsBajoMinimo: number;
  ordenesPendientes: number;
  recepcionesHoy: number;
  costoReposicionEstimado: number;
  alertas: AbastecimientoAlerta[];
  sugerenciasReposicion: SugerenciaReposicion[];
  movimientosRecientes: InventarioMovimientoSummary[];
  proveedoresConOCActivas: ProveedorOConActiva[];
}
