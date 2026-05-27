/**
 * Contratos frontend para las capacidades ERP agregadas entre T15 y T21.
 *
 * Esta capa no define pantallas. Solo concentra tipos estables para que T23
 * pueda construir workspaces sin llenar componentes con `any` ni strings sueltos.
 */

export type TerceroPerfil = "CLIENTE" | "PROVEEDOR" | "EMPLEADO";

export interface TerceroSummary {
  id: number;
  nombreLegal: string;
  nombreComercial?: string | null;
  telefono?: string | null;
  correo?: string | null;
  direccionPrincipal?: string | null;
  perfiles: TerceroPerfil[];
  clienteId?: number | null;
  proveedorId?: number | null;
  activo: boolean;
  createdAt: string;
}

export type EstadoDocumentoCobrar =
  | "PENDIENTE"
  | "PARCIAL"
  | "PAGADO"
  | "ANULADO";
export type EstadoCobranza = "REGISTRADA" | "ANULADA";
export type EstadoPagoProveedor = "REGISTRADO" | "ANULADO";
export type EstadoAsientoContable = "REGISTRADO" | "ANULADO";
export type TipoCuentaContable =
  | "ACTIVO"
  | "PASIVO"
  | "PATRIMONIO"
  | "INGRESO"
  | "COSTO"
  | "GASTO";
export type NaturalezaCuenta = "DEUDORA" | "ACREEDORA";
export type EstadoDocumentoFiscal = "BORRADOR" | "EMITIDO_INTERNO" | "ANULADO";
export type TipoComprobanteFiscal =
  | "FACTURA"
  | "NOTA_VENTA"
  | "COMPROBANTE_COMPRA_INTERNO";
export type OrigenDocumentoFiscal = "DOCUMENTO_COBRAR" | "DOCUMENTO_COMPRA";

export interface DocumentoCobrarSummary {
  id: number;
  clienteId: number;
  clienteNombre: string;
  pedidoId?: number | null;
  pedidoCodigo?: string | null;
  codigo: string;
  estado: EstadoDocumentoCobrar;
  fechaEmision: string;
  fechaVencimiento?: string | null;
  total: number;
  saldo: number;
  observaciones?: string | null;
}

export interface CrearDocumentoCobrarRequest {
  pedidoId?: number | null;
  clienteId?: number | null;
  codigo?: string | null;
  fechaEmision?: string | null;
  fechaVencimiento?: string | null;
  total?: number | null;
  observaciones?: string | null;
}

export interface AplicacionCobranzaRequest {
  documentoCobrarId: number;
  monto: number;
}

export interface RegistrarCobranzaRequest {
  clienteId?: number | null;
  fechaCobranza?: string | null;
  montoTotal: number;
  medioPago?: string | null;
  referenciaPago?: string | null;
  observaciones?: string | null;
  aplicaciones: AplicacionCobranzaRequest[];
}

export interface CobranzaDetalleSummary {
  id: number;
  documentoCobrarId: number;
  documentoCodigo: string;
  montoAplicado: number;
  saldoAnterior: number;
  saldoPosterior: number;
}

export interface CobranzaSummary {
  id: number;
  clienteId: number;
  clienteNombre: string;
  codigo: string;
  fechaCobranza: string;
  montoTotal: number;
  medioPago?: string | null;
  referenciaPago?: string | null;
  estado: EstadoCobranza;
  observaciones?: string | null;
  detalles: CobranzaDetalleSummary[];
}

export interface AplicacionPagoProveedorRequest {
  documentoPagarId: number;
  monto: number;
}

export interface RegistrarPagoProveedorRequest {
  proveedorId?: number | null;
  fechaPago?: string | null;
  montoTotal: number;
  medioPago?: string | null;
  referenciaPago?: string | null;
  observaciones?: string | null;
  aplicaciones: AplicacionPagoProveedorRequest[];
}

export interface PagoProveedorAplicacionSummary {
  id: number;
  documentoPagarId: number;
  documentoCodigo: string;
  montoAplicado: number;
  saldoAnterior: number;
  saldoPosterior: number;
}

export interface PagoProveedorSummary {
  id: number;
  proveedorId: number;
  proveedorNombre: string;
  codigo: string;
  fechaPago: string;
  montoTotal: number;
  medioPago?: string | null;
  referenciaPago?: string | null;
  estado: EstadoPagoProveedor;
  observaciones?: string | null;
  aplicaciones: PagoProveedorAplicacionSummary[];
}

export interface TipoDiarioSummary {
  id: number;
  codigo: string;
  nombre: string;
  descripcion?: string | null;
  activo: boolean;
}

export interface CuentaContableSummary {
  id: number;
  codigo: string;
  nombre: string;
  tipoCuenta: TipoCuentaContable;
  naturaleza: NaturalezaCuenta;
  nivel: number;
  cuentaPadreId?: number | null;
  imputable: boolean;
  activa: boolean;
}

export interface AsientoContableDetalleRequest {
  cuentaCodigo: string;
  descripcion?: string | null;
  debe: number;
  haber: number;
}

export interface RegistrarAsientoContableRequest {
  tipoDiarioCodigo: string;
  codigo?: string | null;
  fechaAsiento?: string | null;
  descripcion: string;
  origenTipo?: string | null;
  origenId?: string | null;
  lineas: AsientoContableDetalleRequest[];
}

export interface AsientoContableDetalleSummary {
  id: number;
  cuentaCodigo: string;
  cuentaNombre: string;
  descripcion?: string | null;
  debe: number;
  haber: number;
}

export interface AsientoContableSummary {
  id: number;
  codigo: string;
  fechaAsiento: string;
  tipoDiarioCodigo: string;
  descripcion: string;
  origenTipo?: string | null;
  origenId?: string | null;
  estado: EstadoAsientoContable;
  totalDebe: number;
  totalHaber: number;
  lineas: AsientoContableDetalleSummary[];
}

export interface RegistrarDocumentoFiscalRequest {
  documentoCobrarId?: number | null;
  documentoCompraId?: number | null;
  tipoComprobante?: TipoComprobanteFiscal | null;
  fechaEmision?: string | null;
  establecimiento?: string | null;
  puntoEmision?: string | null;
  secuencial?: string | null;
  subtotal?: number | null;
  impuesto?: number | null;
  total?: number | null;
  observaciones?: string | null;
}

export interface AnularDocumentoFiscalRequest {
  motivo: string;
}

export interface DocumentoFiscalSummary {
  id: number;
  codigo: string;
  tipoComprobante: TipoComprobanteFiscal;
  estado: EstadoDocumentoFiscal;
  documentoCobrarId?: number | null;
  documentoCompraId?: number | null;
  origenTipo: OrigenDocumentoFiscal;
  terceroTipo: string;
  terceroId: number;
  terceroNombre: string;
  fechaEmision: string;
  establecimiento: string;
  puntoEmision: string;
  secuencial: string;
  numeroComprobante: string;
  subtotal: number;
  impuesto: number;
  total: number;
  claveAcceso?: string | null;
  numeroAutorizacion?: string | null;
  fechaAutorizacion?: string | null;
  ambiente: string;
  observaciones?: string | null;
}

export interface ErpBridgeOperationResult {
  operacion: string;
  origenTipo: string;
  origenId: string;
  destinoTipo: string;
  destinoId: number;
  destinoCodigo: string;
  creado: boolean;
  mensaje: string;
}

export interface DashboardErpSummary {
  fechaCorte: string;
  pedidosActivos: number;
  produccionesActivas: number;
  documentosCobrarAbiertos: number;
  saldoCartera: number;
  documentosPagarAbiertos: number;
  saldoCuentasPagar: number;
  saldoCaja: number;
  asientosRegistrados: number;
  documentosFiscalesBorrador: number;
  itemsStockBajo: number;
}

export interface CarteraSemanticRow {
  documentoCobrarId: number;
  codigo: string;
  estado: string;
  fechaEmision: string;
  fechaVencimiento?: string | null;
  total: number;
  saldo: number;
  montoCobrado: number;
  diasVencido: number;
  clienteId: number;
  clienteNombre: string;
  pedidoId?: number | null;
  pedidoCodigo?: string | null;
}

export interface CuentasPagarSemanticRow {
  documentoPagarId: number;
  codigo: string;
  estado: string;
  fechaEmision: string;
  fechaVencimiento?: string | null;
  total: number;
  saldo: number;
  montoPagado: number;
  diasVencido: number;
  proveedorId: number;
  proveedorNombre: string;
  documentoCompraId?: number | null;
  numeroDocumentoCompra?: string | null;
}

export interface CajaMovimientoSemanticRow {
  movimientoCajaId: number;
  fechaMovimiento: string;
  tipoMovimiento: string;
  naturaleza: string;
  monto: number;
  moneda: string;
  estado: string;
  referenciaTipo?: string | null;
  referenciaId?: string | null;
  descripcion?: string | null;
  turnoCajaId?: number | null;
  estadoTurno?: string | null;
  cajaId?: number | null;
  cajaCodigo?: string | null;
  cajaNombre?: string | null;
}

export interface ContabilidadSemanticRow {
  asientoContableId: number;
  codigo: string;
  fechaAsiento: string;
  estado: string;
  descripcion: string;
  origenTipo?: string | null;
  origenId?: string | null;
  totalDebe: number;
  totalHaber: number;
  diarioCodigo: string;
  diarioNombre: string;
  lineas: number;
}

export interface FiscalSemanticRow {
  documentoFiscalId: number;
  codigo: string;
  tipoComprobante: string;
  estado: string;
  origenTipo: string;
  terceroTipo: string;
  terceroId: number;
  terceroNombre: string;
  fechaEmision: string;
  numeroComprobante: string;
  subtotal: number;
  impuesto: number;
  total: number;
  ambiente: string;
  claveAcceso?: string | null;
  numeroAutorizacion?: string | null;
  fechaAutorizacion?: string | null;
}

export interface StockBajoSemanticRow {
  tipoItem: string;
  itemId: number;
  codigo: string;
  nombre: string;
  stockActual: number;
  stockMinimo: number;
  unidad: string;
}
