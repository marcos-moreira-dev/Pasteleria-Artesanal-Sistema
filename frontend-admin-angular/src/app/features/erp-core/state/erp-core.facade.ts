import { Injectable, computed, inject, signal } from "@angular/core";
import { finalize, forkJoin } from "rxjs";
import { ApiClientService } from "../../../core/api/api-client.service";
import type { DocumentoPagarSummary } from "../../abastecimiento/models/abastecimiento.models";
import type {
  AsientoContableSummary,
  CajaMovimientoSemanticRow,
  CarteraSemanticRow,
  CobranzaSummary,
  ContabilidadSemanticRow,
  CuentaContableSummary,
  CuentasPagarSemanticRow,
  DashboardErpSummary,
  DocumentoCobrarSummary,
  DocumentoFiscalSummary,
  ErpBridgeOperationResult,
  FiscalSemanticRow,
  PagoProveedorSummary,
  StockBajoSemanticRow,
  TerceroPerfil,
  TerceroSummary,
  TipoDiarioSummary,
  CrearDocumentoCobrarRequest,
  RegistrarCobranzaRequest,
  RegistrarPagoProveedorRequest,
  RegistrarAsientoContableRequest,
  RegistrarDocumentoFiscalRequest,
  AnularDocumentoFiscalRequest,
} from "../models/erp-core.models";

/**
 * Fachada transversal para las capacidades ERP ya expuestas por backend.
 *
 * T22 no crea pantallas nuevas. Esta fachada deja un punto de entrada limpio
 * para T23, evitando que los futuros workspaces llamen al ApiClient de forma
 * desordenada o dupliquen manejo de loading/error.
 */
@Injectable({ providedIn: "root" })
export class ErpCoreFacade {
  private readonly api = inject(ApiClientService);

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly actionMessage = signal<string | null>(null);

  readonly terceros = signal<TerceroSummary[]>([]);
  readonly documentosCobrar = signal<DocumentoCobrarSummary[]>([]);
  readonly cobranzas = signal<CobranzaSummary[]>([]);
  readonly documentosPagar = signal<DocumentoPagarSummary[]>([]);
  readonly pagosProveedor = signal<PagoProveedorSummary[]>([]);
  readonly cuentasContables = signal<CuentaContableSummary[]>([]);
  readonly tiposDiario = signal<TipoDiarioSummary[]>([]);
  readonly asientos = signal<AsientoContableSummary[]>([]);
  readonly documentosFiscales = signal<DocumentoFiscalSummary[]>([]);
  readonly erpDashboard = signal<DashboardErpSummary | null>(null);
  readonly carteraSemantica = signal<CarteraSemanticRow[]>([]);
  readonly cuentasPagarSemantica = signal<CuentasPagarSemanticRow[]>([]);
  readonly cajaSemantica = signal<CajaMovimientoSemanticRow[]>([]);
  readonly contabilidadSemantica = signal<ContabilidadSemanticRow[]>([]);
  readonly fiscalSemantica = signal<FiscalSemanticRow[]>([]);
  readonly stockBajoSemantico = signal<StockBajoSemanticRow[]>([]);
  readonly lastBridgeResult = signal<ErpBridgeOperationResult | null>(null);

  readonly resumenFinanciero = computed(() => {
    const dashboard = this.erpDashboard();
    if (!dashboard) return null;
    return {
      saldoCartera: dashboard.saldoCartera,
      saldoCuentasPagar: dashboard.saldoCuentasPagar,
      saldoCaja: dashboard.saldoCaja,
      asientosRegistrados: dashboard.asientosRegistrados,
      documentosFiscalesBorrador: dashboard.documentosFiscalesBorrador,
    };
  });

  loadTerceros(perfil: TerceroPerfil | "" = "", query = "") {
    this.loading.set(true);
    this.error.set(null);
    return this.api
      .getTerceros(perfil, query)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (items) => this.terceros.set(items),
        error: () => this.error.set("No se pudo cargar terceros."),
      });
  }

  loadCartera(estado = "") {
    this.loading.set(true);
    this.error.set(null);
    return forkJoin({
      documentos: this.api.getDocumentosCobrar(estado),
      cobranzas: this.api.getCobranzas(),
    })
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: ({ documentos, cobranzas }) => {
          this.documentosCobrar.set(documentos);
          this.cobranzas.set(cobranzas);
        },
        error: () => this.error.set("No se pudo cargar cartera."),
      });
  }

  loadCuentasPagar(estado = "") {
    this.loading.set(true);
    this.error.set(null);
    return forkJoin({
      documentos: this.api.getDocumentosPagar(estado),
      pagos: this.api.getPagosProveedor(),
    })
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: ({ documentos, pagos }) => {
          this.documentosPagar.set(documentos);
          this.pagosProveedor.set(pagos);
        },
        error: () => this.error.set("No se pudo cargar cuentas por pagar."),
      });
  }

  loadContabilidad() {
    this.loading.set(true);
    this.error.set(null);
    return forkJoin({
      cuentas: this.api.getCuentasContables(),
      diarios: this.api.getTiposDiarioContable(),
      asientos: this.api.getAsientosContables(),
    })
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: ({ cuentas, diarios, asientos }) => {
          this.cuentasContables.set(cuentas);
          this.tiposDiario.set(diarios);
          this.asientos.set(asientos);
        },
        error: () => this.error.set("No se pudo cargar contabilidad."),
      });
  }

  loadFiscalidad() {
    this.loading.set(true);
    this.error.set(null);
    return this.api
      .getDocumentosFiscales()
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (items) => this.documentosFiscales.set(items),
        error: () => this.error.set("No se pudo cargar fiscalidad interna."),
      });
  }

  loadInteligencia(limit = 25) {
    this.loading.set(true);
    this.error.set(null);
    return forkJoin({
      dashboard: this.api.getErpDashboard(),
      cartera: this.api.getInteligenciaCartera(limit),
      cuentasPagar: this.api.getInteligenciaCuentasPagar(limit),
      caja: this.api.getInteligenciaCaja(limit),
      contabilidad: this.api.getInteligenciaContabilidad(limit),
      fiscal: this.api.getInteligenciaFiscal(limit),
      stockBajo: this.api.getInteligenciaStockBajo(limit),
    })
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (data) => {
          this.erpDashboard.set(data.dashboard);
          this.carteraSemantica.set(data.cartera);
          this.cuentasPagarSemantica.set(data.cuentasPagar);
          this.cajaSemantica.set(data.caja);
          this.contabilidadSemantica.set(data.contabilidad);
          this.fiscalSemantica.set(data.fiscal);
          this.stockBajoSemantico.set(data.stockBajo);
        },
        error: () => this.error.set("No se pudo cargar inteligencia ERP."),
      });
  }


  crearDocumentoCobrar(payload: CrearDocumentoCobrarRequest) {
    this.loading.set(true);
    this.error.set(null);
    this.actionMessage.set(null);
    return this.api
      .createDocumentoCobrar(payload)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (documento) => {
          this.actionMessage.set(`Documento por cobrar ${documento.codigo} registrado.`);
          this.loadCartera();
        },
        error: () => this.error.set("No se pudo registrar el documento por cobrar."),
      });
  }

  registrarCobranza(payload: RegistrarCobranzaRequest) {
    this.loading.set(true);
    this.error.set(null);
    this.actionMessage.set(null);
    return this.api
      .registrarCobranza(payload)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (cobranza) => {
          this.actionMessage.set(`Cobranza ${cobranza.codigo} registrada.`);
          this.loadCartera();
        },
        error: () => this.error.set("No se pudo registrar la cobranza."),
      });
  }

  registrarPagoProveedor(payload: RegistrarPagoProveedorRequest) {
    this.loading.set(true);
    this.error.set(null);
    this.actionMessage.set(null);
    return this.api
      .registrarPagoProveedor(payload)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (pago) => {
          this.actionMessage.set(`Pago proveedor ${pago.codigo} registrado.`);
          this.loadCuentasPagar();
        },
        error: () => this.error.set("No se pudo registrar el pago proveedor."),
      });
  }

  registrarAsientoContable(payload: RegistrarAsientoContableRequest) {
    this.loading.set(true);
    this.error.set(null);
    this.actionMessage.set(null);
    return this.api
      .registrarAsientoContable(payload)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (asiento) => {
          this.actionMessage.set(`Asiento ${asiento.codigo} registrado.`);
          this.loadContabilidad();
        },
        error: () => this.error.set("No se pudo registrar el asiento contable."),
      });
  }

  prepararDocumentoFiscal(payload: RegistrarDocumentoFiscalRequest) {
    this.loading.set(true);
    this.error.set(null);
    this.actionMessage.set(null);
    return this.api
      .prepararDocumentoFiscal(payload)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (documento) => {
          this.actionMessage.set(`Documento fiscal ${documento.codigo} preparado.`);
          this.loadFiscalidad();
        },
        error: () => this.error.set("No se pudo preparar el documento fiscal interno."),
      });
  }

  emitirDocumentoFiscalInterno(documentoId: number) {
    this.loading.set(true);
    this.error.set(null);
    this.actionMessage.set(null);
    return this.api
      .emitirDocumentoFiscalInterno(documentoId)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (documento) => {
          this.actionMessage.set(`Documento fiscal ${documento.codigo} emitido internamente.`);
          this.loadFiscalidad();
        },
        error: () => this.error.set("No se pudo emitir internamente el documento fiscal."),
      });
  }

  anularDocumentoFiscal(documentoId: number, payload: AnularDocumentoFiscalRequest) {
    this.loading.set(true);
    this.error.set(null);
    this.actionMessage.set(null);
    return this.api
      .anularDocumentoFiscal(documentoId, payload)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (documento) => {
          this.actionMessage.set(`Documento fiscal ${documento.codigo} anulado.`);
          this.loadFiscalidad();
        },
        error: () => this.error.set("No se pudo anular el documento fiscal."),
      });
  }

  generarDocumentoCobrarDesdePedido(pedidoId: number) {
    return this.runBridgeOperation(
      () => this.api.generarDocumentoCobrarDesdePedido(pedidoId),
      "No se pudo generar el documento por cobrar desde el pedido.",
    );
  }

  generarAsientoVentaDesdeDocumentoCobrar(documentoCobrarId: number) {
    return this.runBridgeOperation(
      () => this.api.generarAsientoVentaDesdeDocumentoCobrar(documentoCobrarId),
      "No se pudo generar el asiento de venta.",
    );
  }

  generarAsientoCobroDesdeCobranza(cobranzaId: number) {
    return this.runBridgeOperation(
      () => this.api.generarAsientoCobroDesdeCobranza(cobranzaId),
      "No se pudo generar el asiento de cobro.",
    );
  }

  generarAsientoCompraDesdeDocumentoPagar(documentoPagarId: number) {
    return this.runBridgeOperation(
      () => this.api.generarAsientoCompraDesdeDocumentoPagar(documentoPagarId),
      "No se pudo generar el asiento de compra.",
    );
  }

  generarAsientoPagoProveedor(pagoProveedorId: number) {
    return this.runBridgeOperation(
      () => this.api.generarAsientoPagoProveedor(pagoProveedorId),
      "No se pudo generar el asiento de pago a proveedor.",
    );
  }

  registrarResultadoBridge(result: ErpBridgeOperationResult) {
    this.lastBridgeResult.set(result);
    this.actionMessage.set(result.mensaje);
  }

  private runBridgeOperation(
    operation: () => ReturnType<ApiClientService["generarDocumentoCobrarDesdePedido"]>,
    errorMessage: string,
  ) {
    this.loading.set(true);
    this.error.set(null);
    this.actionMessage.set(null);
    return operation()
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (result) => this.registrarResultadoBridge(result),
        error: () => this.error.set(errorMessage),
      });
  }
}
