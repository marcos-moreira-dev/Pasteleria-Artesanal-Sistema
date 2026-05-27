package com.pasteleria.abastecimiento.application;

import com.pasteleria.abastecimiento.domain.model.EstadoDocumentoCompra;
import com.pasteleria.abastecimiento.domain.model.EstadoDocumentoPagar;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.DocumentoCompraEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.DocumentoPagarEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.OrdenCompraEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.DocumentoCompraRepository;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.DocumentoPagarRepository;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.OrdenCompraRepository;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.security.OperacionAutorizacionService;
import com.pasteleria.common.security.Permisos;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompraFinancieraService {

  private static final String SUCURSAL_TRANSICIONAL = "MATRIZ";

  private final OrdenCompraRepository ordenCompraRepository;
  private final DocumentoCompraRepository documentoCompraRepository;
  private final DocumentoPagarRepository documentoPagarRepository;
  private final CompraFinancieraPolicy policy;
  private final OperacionAutorizacionService autorizacionService;
  private final AuditTrailService auditTrailService;

  public CompraFinancieraService(
      OrdenCompraRepository ordenCompraRepository,
      DocumentoCompraRepository documentoCompraRepository,
      DocumentoPagarRepository documentoPagarRepository,
      CompraFinancieraPolicy policy,
      OperacionAutorizacionService autorizacionService,
      AuditTrailService auditTrailService
  ) {
    this.ordenCompraRepository = ordenCompraRepository;
    this.documentoCompraRepository = documentoCompraRepository;
    this.documentoPagarRepository = documentoPagarRepository;
    this.policy = policy;
    this.autorizacionService = autorizacionService;
    this.auditTrailService = auditTrailService;
  }

  @Transactional
  public CompraFinancieraSummary registrarDocumentoCompra(
      Long ordenCompraId,
      RegistrarDocumentoCompraRequest request,
      HttpServletRequest httpRequest
  ) {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.COMPRAS_GESTIONAR);

    OrdenCompraEntity orden = ordenCompraRepository.findById(ordenCompraId)
        .orElseThrow(() -> new ResourceNotFoundException("Orden de compra no encontrada."));

    if (documentoCompraRepository.existsByOrdenCompraOrdenCompraId(ordenCompraId)) {
      throw new BusinessRuleException("La orden de compra ya tiene un documento de compra registrado.");
    }

    policy.exigirOrdenDocumentable(orden);

    String numeroDocumento = policy.normalizarNumeroDocumento(request.numeroDocumento(), orden.getCodigo());
    if (documentoCompraRepository.existsByNumeroDocumentoIgnoreCase(numeroDocumento)) {
      throw new BusinessRuleException("Ya existe un documento de compra con ese número.");
    }

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime fechaEmision = parseOptionalDateTime(request.fechaEmision(), now);
    LocalDateTime fechaVencimiento = parseOptionalDateTime(request.fechaVencimiento(), fechaEmision.plusDays(15));
    BigDecimal subtotal = policy.calcularSubtotalRecibido(orden);
    BigDecimal impuesto = policy.normalizarImpuesto(request.impuesto());
    BigDecimal total = policy.calcularTotal(subtotal, impuesto);

    DocumentoCompraEntity documento = new DocumentoCompraEntity();
    documento.setOrdenCompra(orden);
    documento.setProveedor(orden.getProveedor());
    documento.setNumeroDocumento(numeroDocumento);
    documento.setEstado(EstadoDocumentoCompra.REGISTRADO.name());
    documento.setFechaEmision(fechaEmision);
    documento.setSubtotal(subtotal);
    documento.setImpuesto(impuesto);
    documento.setTotal(total);
    documento.setObservaciones(blankToNull(request.observaciones()));
    documento.setCreatedAt(now);
    documento.setUpdatedAt(now);

    DocumentoCompraEntity savedDocumento = documentoCompraRepository.save(documento);

    DocumentoPagarEntity documentoPagar = new DocumentoPagarEntity();
    documentoPagar.setDocumentoCompra(savedDocumento);
    documentoPagar.setProveedor(orden.getProveedor());
    documentoPagar.setCodigo(policy.construirCodigoDocumentoPagar(orden.getCodigo()));
    documentoPagar.setEstado(EstadoDocumentoPagar.PENDIENTE.name());
    documentoPagar.setFechaEmision(fechaEmision);
    documentoPagar.setFechaVencimiento(fechaVencimiento);
    documentoPagar.setTotal(total);
    documentoPagar.setSaldo(total);
    documentoPagar.setObservaciones("Cuenta por pagar generada desde documento de compra " + numeroDocumento + ".");
    documentoPagar.setCreatedAt(now);
    documentoPagar.setUpdatedAt(now);

    DocumentoPagarEntity savedPagar = documentoPagarRepository.save(documentoPagar);
    CompraFinancieraSummary summary = new CompraFinancieraSummary(toDocumentoCompraSummary(savedDocumento), toDocumentoPagarSummary(savedPagar));

    auditTrailService.recordChange(
        "DOCUMENTO_COMPRA_REGISTRADO",
        "ABASTECIMIENTO",
        "documento_compra",
        savedDocumento.getDocumentoCompraId().toString(),
        "REGISTRAR_DOCUMENTO_COMPRA",
        null,
        summary,
        "Documento de compra y cuenta por pagar generados desde orden " + orden.getCodigo() + ".",
        httpRequest
    );

    return summary;
  }

  @Transactional(readOnly = true)
  public CompraFinancieraSummary findByOrdenCompra(Long ordenCompraId) {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.COMPRAS_VER);
    DocumentoCompraEntity documento = documentoCompraRepository.findByOrdenCompraOrdenCompraId(ordenCompraId)
        .orElseThrow(() -> new ResourceNotFoundException("La orden de compra aún no tiene documento de compra registrado."));
    DocumentoPagarEntity pagar = documentoPagarRepository.findByDocumentoCompraDocumentoCompraId(documento.getDocumentoCompraId())
        .orElseThrow(() -> new ResourceNotFoundException("El documento de compra no tiene cuenta por pagar asociada."));
    return new CompraFinancieraSummary(toDocumentoCompraSummary(documento), toDocumentoPagarSummary(pagar));
  }

  @Transactional(readOnly = true)
  public List<DocumentoPagarSummary> findCuentasPagar(String estado) {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.CUENTAS_PAGAR_VER);
    List<DocumentoPagarEntity> cuentas = estado == null || estado.isBlank()
        ? documentoPagarRepository.findTop50ByOrderByCreatedAtDesc()
        : documentoPagarRepository.findByEstadoOrderByCreatedAtDesc(estado.trim().toUpperCase());
    return cuentas.stream().map(this::toDocumentoPagarSummary).toList();
  }

  private DocumentoCompraSummary toDocumentoCompraSummary(DocumentoCompraEntity documento) {
    return new DocumentoCompraSummary(
        documento.getDocumentoCompraId(),
        documento.getOrdenCompra() != null ? documento.getOrdenCompra().getOrdenCompraId() : null,
        documento.getOrdenCompra() != null ? documento.getOrdenCompra().getCodigo() : null,
        documento.getProveedor() != null ? documento.getProveedor().getId() : null,
        documento.getProveedor() != null ? documento.getProveedor().getName() : "N/A",
        documento.getNumeroDocumento(),
        documento.getEstado(),
        documento.getFechaEmision(),
        documento.getSubtotal(),
        documento.getImpuesto(),
        documento.getTotal(),
        documento.getObservaciones(),
        documento.getCreatedAt()
    );
  }

  private DocumentoPagarSummary toDocumentoPagarSummary(DocumentoPagarEntity documento) {
    return new DocumentoPagarSummary(
        documento.getDocumentoPagarId(),
        documento.getDocumentoCompra() != null ? documento.getDocumentoCompra().getDocumentoCompraId() : null,
        documento.getProveedor() != null ? documento.getProveedor().getId() : null,
        documento.getProveedor() != null ? documento.getProveedor().getName() : "N/A",
        documento.getCodigo(),
        documento.getEstado(),
        documento.getFechaEmision(),
        documento.getFechaVencimiento(),
        documento.getTotal(),
        documento.getSaldo(),
        documento.getObservaciones(),
        documento.getCreatedAt()
    );
  }

  private LocalDateTime parseOptionalDateTime(String raw, LocalDateTime fallback) {
    if (raw == null || raw.isBlank()) {
      return fallback;
    }
    String value = raw.trim();
    try {
      return OffsetDateTime.parse(value).toLocalDateTime();
    } catch (DateTimeParseException ignored) {
    }
    try {
      return LocalDateTime.parse(value);
    } catch (DateTimeParseException ignored) {
    }
    try {
      return LocalDate.parse(value).atStartOfDay();
    } catch (DateTimeParseException ignored) {
    }
    throw new BusinessRuleException("La fecha del documento de compra no tiene un formato válido.");
  }

  private String blankToNull(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return value.trim();
  }
}
