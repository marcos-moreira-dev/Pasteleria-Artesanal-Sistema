package com.pasteleria.fiscal.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pasteleria.abastecimiento.infrastructure.persistence.entity.DocumentoCompraEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.DocumentoCompraRepository;
import com.pasteleria.cartera.infrastructure.persistence.entity.DocumentoCobrarEntity;
import com.pasteleria.cartera.infrastructure.persistence.repository.DocumentoCobrarRepository;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.security.OperacionAutorizacionService;
import com.pasteleria.common.security.Permisos;
import com.pasteleria.erp.application.ErpFinancialPolicy;
import com.pasteleria.fiscal.infrastructure.persistence.entity.DocumentoFiscalEntity;
import com.pasteleria.fiscal.infrastructure.persistence.repository.DocumentoFiscalRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FiscalCommandService {

  private static final String AMBIENTE_PREPARADO = "INTERNO";

  private final DocumentoFiscalRepository documentoFiscalRepository;
  private final DocumentoCobrarRepository documentoCobrarRepository;
  private final DocumentoCompraRepository documentoCompraRepository;
  private final FiscalMapper mapper;
  private final OperacionAutorizacionService autorizacionService;
  private final AuditTrailService auditTrailService;

  public FiscalCommandService(
      DocumentoFiscalRepository documentoFiscalRepository,
      DocumentoCobrarRepository documentoCobrarRepository,
      DocumentoCompraRepository documentoCompraRepository,
      FiscalMapper mapper,
      OperacionAutorizacionService autorizacionService,
      AuditTrailService auditTrailService
  ) {
    this.documentoFiscalRepository = documentoFiscalRepository;
    this.documentoCobrarRepository = documentoCobrarRepository;
    this.documentoCompraRepository = documentoCompraRepository;
    this.mapper = mapper;
    this.autorizacionService = autorizacionService;
    this.auditTrailService = auditTrailService;
  }

  @Transactional
  public DocumentoFiscalSummary preparar(RegistrarDocumentoFiscalRequest request, HttpServletRequest httpRequest) {
    autorizacionService.exigirPermisoGlobal(Permisos.DOCUMENTOS_FISCALES_EMITIR);
    ErpFinancialPolicy.validarOrigenFiscal(request.documentoCobrarId(), request.documentoCompraId());
    if (request.tipoComprobante() == null) {
      throw new BusinessRuleException("El tipo de comprobante fiscal es obligatorio.");
    }

    LocalDateTime now = LocalDateTime.now();
    FiscalTotals totals;
    DocumentoFiscalEntity entity = new DocumentoFiscalEntity();
    entity.setTipoComprobante(request.tipoComprobante().name());
    entity.setEstado(EstadoDocumentoFiscal.BORRADOR.name());
    entity.setFechaEmision(request.fechaEmision() == null ? now : request.fechaEmision());
    entity.setEstablecimiento(normalizarPunto(request.establecimiento(), "001"));
    entity.setPuntoEmision(normalizarPunto(request.puntoEmision(), "001"));
    entity.setSecuencial(normalizarSecuencial(request.secuencial()));
    entity.setAmbiente(AMBIENTE_PREPARADO);
    entity.setObservaciones(blankToNull(request.observaciones()));
    entity.setCreatedAt(now);
    entity.setUpdatedAt(now);

    if (request.documentoCobrarId() != null) {
      if (documentoFiscalRepository.existsByDocumentoCobrarId(request.documentoCobrarId())) {
        throw new BusinessRuleException("Ya existe un documento fiscal para el documento por cobrar indicado.");
      }
      DocumentoCobrarEntity documento = documentoCobrarRepository.findById(request.documentoCobrarId())
          .orElseThrow(() -> new ResourceNotFoundException("Documento por cobrar no encontrado."));
      totals = totalsFromReceivable(request, documento);
      entity.setDocumentoCobrarId(documento.getId());
      entity.setOrigenTipo(OrigenDocumentoFiscal.DOCUMENTO_COBRAR.name());
      entity.setTerceroTipo("CLIENTE");
      entity.setTerceroId(documento.getCliente().getId());
      entity.setTerceroNombre(documento.getCliente().getFullName());
    } else {
      if (documentoFiscalRepository.existsByDocumentoCompraId(request.documentoCompraId())) {
        throw new BusinessRuleException("Ya existe un documento fiscal para el documento de compra indicado.");
      }
      DocumentoCompraEntity documento = documentoCompraRepository.findById(request.documentoCompraId())
          .orElseThrow(() -> new ResourceNotFoundException("Documento de compra no encontrado."));
      totals = totalsFromPurchase(documento);
      entity.setDocumentoCompraId(documento.getDocumentoCompraId());
      entity.setOrigenTipo(OrigenDocumentoFiscal.DOCUMENTO_COMPRA.name());
      entity.setTerceroTipo("PROVEEDOR");
      entity.setTerceroId(documento.getProveedor().getId());
      entity.setTerceroNombre(documento.getProveedor().getName());
    }

    ErpFinancialPolicy.validarTotalesFiscales(totals.subtotal(), totals.impuesto(), totals.total());
    entity.setSubtotal(totals.subtotal());
    entity.setImpuesto(totals.impuesto());
    entity.setTotal(totals.total());
    entity.setNumeroComprobante(numeroComprobante(entity.getEstablecimiento(), entity.getPuntoEmision(), entity.getSecuencial()));
    entity.setCodigo(codigo(entity.getTipoComprobante(), entity.getNumeroComprobante()));
    if (documentoFiscalRepository.existsByNumeroComprobante(entity.getNumeroComprobante())
        || documentoFiscalRepository.existsByCodigo(entity.getCodigo())) {
      throw new BusinessRuleException("Ya existe un documento fiscal con el numero indicado.");
    }

    DocumentoFiscalEntity saved = documentoFiscalRepository.save(entity);
    DocumentoFiscalSummary summary = mapper.toSummary(saved);
    auditTrailService.recordChange(
        "DOCUMENTO_FISCAL_PREPARADO",
        "FISCAL",
        "documento_fiscal",
        saved.getId().toString(),
        "PREPARAR_DOCUMENTO_FISCAL",
        null,
        summary,
        "Documento fiscal interno preparado sin integracion SRI.",
        httpRequest
    );
    return summary;
  }

  @Transactional
  public DocumentoFiscalSummary emitirInterno(Long id, HttpServletRequest httpRequest) {
    autorizacionService.exigirPermisoGlobal(Permisos.DOCUMENTOS_FISCALES_EMITIR);
    DocumentoFiscalEntity entity = documentoFiscalRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Documento fiscal no encontrado."));
    if (!EstadoDocumentoFiscal.BORRADOR.name().equals(entity.getEstado())) {
      throw new BusinessRuleException("Solo se puede emitir internamente un documento fiscal en borrador.");
    }
    entity.setEstado(EstadoDocumentoFiscal.EMITIDO_INTERNO.name());
    entity.setUpdatedAt(LocalDateTime.now());
    DocumentoFiscalSummary summary = mapper.toSummary(documentoFiscalRepository.save(entity));
    auditTrailService.recordChange(
        "DOCUMENTO_FISCAL_EMITIDO_INTERNO",
        "FISCAL",
        "documento_fiscal",
        entity.getId().toString(),
        "EMITIR_DOCUMENTO_FISCAL_INTERNO",
        null,
        summary,
        "Documento fiscal marcado como emitido interno. No implica autorizacion SRI.",
        httpRequest
    );
    return summary;
  }

  @Transactional
  public DocumentoFiscalSummary anular(Long id, AnularDocumentoFiscalRequest request, HttpServletRequest httpRequest) {
    autorizacionService.exigirPermisoGlobal(Permisos.DOCUMENTOS_FISCALES_EMITIR);
    DocumentoFiscalEntity entity = documentoFiscalRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Documento fiscal no encontrado."));
    if (EstadoDocumentoFiscal.ANULADO.name().equals(entity.getEstado())) {
      throw new BusinessRuleException("El documento fiscal ya esta anulado.");
    }
    entity.setEstado(EstadoDocumentoFiscal.ANULADO.name());
    entity.setObservaciones(appendObservation(entity.getObservaciones(), "Anulacion: " + request.motivo().trim()));
    entity.setUpdatedAt(LocalDateTime.now());
    DocumentoFiscalSummary summary = mapper.toSummary(documentoFiscalRepository.save(entity));
    auditTrailService.recordChange(
        "DOCUMENTO_FISCAL_ANULADO",
        "FISCAL",
        "documento_fiscal",
        entity.getId().toString(),
        "ANULAR_DOCUMENTO_FISCAL",
        null,
        summary,
        "Documento fiscal interno anulado.",
        httpRequest
    );
    return summary;
  }

  private FiscalTotals totalsFromReceivable(RegistrarDocumentoFiscalRequest request, DocumentoCobrarEntity documento) {
    BigDecimal total = request.total() == null ? documento.getTotal() : request.total();
    BigDecimal impuesto = request.impuesto() == null ? BigDecimal.ZERO : request.impuesto();
    BigDecimal subtotal = request.subtotal() == null ? ErpFinancialPolicy.money(total).subtract(ErpFinancialPolicy.money(impuesto)) : request.subtotal();
    return new FiscalTotals(ErpFinancialPolicy.money(subtotal), ErpFinancialPolicy.money(impuesto), ErpFinancialPolicy.money(total));
  }

  private FiscalTotals totalsFromPurchase(DocumentoCompraEntity documento) {
    return new FiscalTotals(
        ErpFinancialPolicy.money(documento.getSubtotal()),
        ErpFinancialPolicy.money(documento.getImpuesto()),
        ErpFinancialPolicy.money(documento.getTotal())
    );
  }

  private String normalizarPunto(String value, String defaultValue) {
    String limpio = blankToNull(value);
    return limpio == null ? defaultValue : limpio;
  }

  private String normalizarSecuencial(String secuencial) {
    String limpio = blankToNull(secuencial);
    if (limpio == null) {
      long next = Math.max(1L, documentoFiscalRepository.count() + 1L);
      return String.format("%09d", next);
    }
    return String.format("%09d", Long.parseLong(limpio));
  }

  private String numeroComprobante(String establecimiento, String puntoEmision, String secuencial) {
    return establecimiento + "-" + puntoEmision + "-" + secuencial;
  }

  private String codigo(String tipoComprobante, String numeroComprobante) {
    return tipoComprobante + "-" + numeroComprobante;
  }

  private String appendObservation(String actual, String nueva) {
    String base = blankToNull(actual);
    return base == null ? nueva : base + System.lineSeparator() + nueva;
  }

  private String blankToNull(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return value.trim();
  }

  private record FiscalTotals(BigDecimal subtotal, BigDecimal impuesto, BigDecimal total) {
  }
}
