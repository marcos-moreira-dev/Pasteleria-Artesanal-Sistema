package com.pasteleria.cuentaspagar.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.pasteleria.abastecimiento.domain.model.EstadoDocumentoPagar;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.DocumentoPagarEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.ProveedorEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.DocumentoPagarRepository;
import com.pasteleria.abastecimiento.application.port.ProveedorRepositoryPort;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.security.OperacionAutorizacionService;
import com.pasteleria.common.security.Permisos;
import com.pasteleria.common.security.UserAccessPolicy;
import com.pasteleria.cuentaspagar.infrastructure.persistence.entity.PagoProveedorAplicacionEntity;
import com.pasteleria.cuentaspagar.infrastructure.persistence.entity.PagoProveedorEntity;
import com.pasteleria.cuentaspagar.infrastructure.persistence.repository.PagoProveedorAplicacionRepository;
import com.pasteleria.cuentaspagar.infrastructure.persistence.repository.PagoProveedorRepository;
import com.pasteleria.erp.application.ErpFinancialPolicy;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CuentasPagarCommandService {

  private static final String SUCURSAL_TRANSICIONAL = UserAccessPolicy.DEFAULT_SUCURSAL_ID;

  private final DocumentoPagarRepository documentoRepository;
  private final PagoProveedorRepository pagoRepository;
  private final PagoProveedorAplicacionRepository aplicacionRepository;
  private final ProveedorRepositoryPort proveedorRepository;
  private final CuentasPagarMapper mapper;
  private final OperacionAutorizacionService autorizacionService;
  private final AuditTrailService auditTrailService;

  public CuentasPagarCommandService(
      DocumentoPagarRepository documentoRepository,
      PagoProveedorRepository pagoRepository,
      PagoProveedorAplicacionRepository aplicacionRepository,
      ProveedorRepositoryPort proveedorRepository,
      CuentasPagarMapper mapper,
      OperacionAutorizacionService autorizacionService,
      AuditTrailService auditTrailService
  ) {
    this.documentoRepository = documentoRepository;
    this.pagoRepository = pagoRepository;
    this.aplicacionRepository = aplicacionRepository;
    this.proveedorRepository = proveedorRepository;
    this.mapper = mapper;
    this.autorizacionService = autorizacionService;
    this.auditTrailService = auditTrailService;
  }

  @Transactional
  public PagoProveedorSummary registrarPago(RegistrarPagoProveedorRequest request, HttpServletRequest httpRequest) {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.PAGOS_PROVEEDOR_REGISTRAR);
    LocalDateTime now = LocalDateTime.now();
    BigDecimal montoTotal = ErpFinancialPolicy.money(request.montoTotal());
    ErpFinancialPolicy.exigirMontoPositivo(montoTotal, "El monto total del pago a proveedor debe ser mayor a cero.");

    List<AplicacionPagoProveedorRequest> aplicaciones = request.aplicaciones();
    BigDecimal sumaAplicaciones = aplicaciones.stream()
        .map(AplicacionPagoProveedorRequest::monto)
        .map(ErpFinancialPolicy::money)
        .reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
    if (sumaAplicaciones.compareTo(montoTotal) != 0) {
      throw new BusinessRuleException("La suma de aplicaciones debe coincidir con el monto total del pago a proveedor.");
    }

    DocumentoPagarEntity primerDocumento = documentoRepository.findByIdForUpdate(aplicaciones.get(0).documentoPagarId())
        .orElseThrow(() -> new ResourceNotFoundException("Documento por pagar no encontrado."));
    ProveedorEntity proveedor = primerDocumento.getProveedor();
    if (request.proveedorId() != null && !Objects.equals(request.proveedorId(), proveedor.getId())) {
      throw new BusinessRuleException("El proveedor del pago no coincide con el primer documento aplicado.");
    }
    proveedorRepository.findById(proveedor.getId())
        .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado."));

    PagoProveedorEntity pago = new PagoProveedorEntity();
    pago.setProveedor(proveedor);
    pago.setCodigo("PAG-PROV-" + System.currentTimeMillis());
    pago.setFechaPago(request.fechaPago() == null ? now : request.fechaPago());
    pago.setMontoTotal(montoTotal);
    pago.setMedioPago(blankToNull(request.medioPago()));
    pago.setReferenciaPago(blankToNull(request.referenciaPago()));
    pago.setEstado(EstadoPagoProveedor.REGISTRADO.name());
    pago.setObservaciones(blankToNull(request.observaciones()));
    pago.setCreatedAt(now);
    pago.setUpdatedAt(now);
    PagoProveedorEntity savedPago = pagoRepository.save(pago);

    for (AplicacionPagoProveedorRequest aplicacion : aplicaciones) {
      DocumentoPagarEntity documento = documentoRepository.findByIdForUpdate(aplicacion.documentoPagarId())
          .orElseThrow(() -> new ResourceNotFoundException("Documento por pagar no encontrado."));
      if (!Objects.equals(documento.getProveedor().getId(), proveedor.getId())) {
        throw new BusinessRuleException("Todas las aplicaciones de un pago deben pertenecer al mismo proveedor.");
      }
      if (EstadoDocumentoPagar.ANULADO.name().equals(documento.getEstado())) {
        throw new BusinessRuleException("No se puede aplicar un pago a un documento anulado.");
      }
      BigDecimal montoAplicado = ErpFinancialPolicy.money(aplicacion.monto());
      ErpFinancialPolicy.validarAplicacionContraSaldo(
          montoAplicado,
          documento.getSaldo(),
          "El pago no puede superar el saldo del documento por pagar."
      );
      BigDecimal saldoAnterior = ErpFinancialPolicy.money(documento.getSaldo());
      BigDecimal saldoPosterior = ErpFinancialPolicy.money(saldoAnterior.subtract(montoAplicado));
      documento.setSaldo(saldoPosterior);
      documento.setEstado(estadoDocumentoPagar(saldoPosterior, documento.getTotal()));
      documento.setUpdatedAt(now);
      DocumentoPagarEntity savedDocumento = documentoRepository.save(documento);

      PagoProveedorAplicacionEntity detalle = new PagoProveedorAplicacionEntity();
      detalle.setPagoProveedor(savedPago);
      detalle.setDocumentoPagar(savedDocumento);
      detalle.setMontoAplicado(montoAplicado);
      detalle.setSaldoAnterior(saldoAnterior);
      detalle.setSaldoPosterior(saldoPosterior);
      detalle.setCreatedAt(now);
      aplicacionRepository.save(detalle);
    }

    PagoProveedorSummary summary = mapper.toPago(savedPago, aplicacionRepository.findByPagoProveedorIdOrderByIdAsc(savedPago.getId()));
    auditTrailService.recordChange(
        "CUENTAS_PAGAR_PAGO_PROVEEDOR_REGISTRADO",
        "CUENTAS_PAGAR",
        "pago_proveedor",
        savedPago.getId().toString(),
        "REGISTRAR_PAGO_PROVEEDOR",
        null,
        summary,
        "Pago a proveedor registrado contra saldos pendientes.",
        httpRequest
    );
    return summary;
  }

  private String estadoDocumentoPagar(BigDecimal saldo, BigDecimal total) {
    String estado = ErpFinancialPolicy.estadoPorSaldo(saldo, total);
    if ("PARCIAL".equals(estado)) {
      return EstadoDocumentoPagar.PAGADO_PARCIAL.name();
    }
    return estado;
  }

  private String blankToNull(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return value.trim();
  }
}
