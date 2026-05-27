package com.pasteleria.cotizaciones.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.pasteleria.clientes.application.port.ClientRepositoryPort;
import com.pasteleria.clientes.infrastructure.persistence.entity.ClientEntity;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.text.TextSupport;
import com.pasteleria.cotizaciones.application.port.QuotationRepositoryPort;
import com.pasteleria.cotizaciones.infrastructure.persistence.entity.QuotationDetailEntity;
import com.pasteleria.cotizaciones.infrastructure.persistence.entity.QuotationEntity;
import com.pasteleria.cotizaciones.domain.model.QuotationStatus;
import com.pasteleria.cotizaciones.application.mapper.QuotationDtoMapper;
import com.pasteleria.productos.application.port.ProductRepositoryPort;
import com.pasteleria.productos.infrastructure.persistence.entity.ProductEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Orquesta el alta de cotizaciones internas y publicas.
 */
@Service
public class QuotationCommandService {

  private final QuotationRepositoryPort quotationRepository;
  private final ClientRepositoryPort clientRepository;
  private final ProductRepositoryPort productRepository;
  private final AuditTrailService auditTrailService;
  private final QuotationDtoMapper quotationDtoMapper;

  public QuotationCommandService(
      QuotationRepositoryPort quotationRepository,
      ClientRepositoryPort clientRepository,
      ProductRepositoryPort productRepository,
      AuditTrailService auditTrailService,
      QuotationDtoMapper quotationDtoMapper
  ) {
    this.quotationRepository = quotationRepository;
    this.clientRepository = clientRepository;
    this.productRepository = productRepository;
    this.auditTrailService = auditTrailService;
    this.quotationDtoMapper = quotationDtoMapper;
  }

  /**
   * Registra una cotizacion operada desde backoffice.
   */
  @Transactional
  public QuotationSummary createQuotation(CreateQuotationRequest request, HttpServletRequest httpRequest) {
    ClientEntity client = clientRepository.findById(request.clientId())
        .orElseThrow(() -> new ResourceNotFoundException("El cliente indicado no existe."));

    OffsetDateTime now = OffsetDateTime.now();
    QuotationEntity quotation = new QuotationEntity();
    quotation.setClient(client);
    quotation.setCode("COT-" + System.currentTimeMillis());
    quotation.setStatus(QuotationStatus.PENDIENTE);
    quotation.setOrigin(request.origin());
    quotation.setNotes(TextSupport.trimToNull(request.notes()));
    quotation.setCreatedAt(now);
    quotation.setUpdatedAt(now);

    BigDecimal total = BigDecimal.ZERO;
    for (CreateQuotationDetailRequest detailRequest : request.details()) {
      QuotationDetailEntity detail = new QuotationDetailEntity();
      detail.setQuotation(quotation);
      detail.setItemDescription(detailRequest.itemDescription().trim());
      detail.setQuantity(detailRequest.quantity());
      detail.setEstimatedPrice(detailRequest.estimatedPrice());
      detail.setSubtotal(detailRequest.estimatedPrice().multiply(BigDecimal.valueOf(detailRequest.quantity())));
      detail.setNotes(TextSupport.trimToNull(detailRequest.notes()));
      detail.setCreatedAt(now);

      if (detailRequest.productId() != null) {
        ProductEntity product = productRepository.findById(detailRequest.productId())
            .orElseThrow(() -> new ResourceNotFoundException("Uno de los productos indicados no existe."));
        detail.setProduct(product);
      }

      quotation.getDetails().add(detail);
      total = total.add(detail.getSubtotal());
    }

    quotation.setEstimatedTotal(total);
    QuotationEntity savedQuotation = quotationRepository.save(quotation);
    auditTrailService.record(
        "COTIZACION_CREADA",
        "COTIZACIONES",
        "cotizacion",
        savedQuotation.getId().toString(),
        "CREAR_COTIZACION",
        "Registro inicial de la cotizacion.",
        httpRequest
    );
    return quotationDtoMapper.toSummary(savedQuotation);
  }

  /**
   * Registra una solicitud comercial nacida desde la landing publica.
   *
   * <p>Si el correo ya existe, se reutiliza el cliente para no fragmentar historial.</p>
   */
  @Transactional
  public QuotationSummary createPublicQuotation(PublicQuotationRequest request, HttpServletRequest httpRequest) {
    ClientEntity client = resolvePublicClient(request);

    OffsetDateTime now = OffsetDateTime.now();
    QuotationEntity quotation = new QuotationEntity();
    quotation.setClient(client);
    quotation.setCode("COT-WEB-" + System.currentTimeMillis());
    quotation.setStatus(QuotationStatus.PENDIENTE);
    quotation.setOrigin(com.pasteleria.cotizaciones.domain.model.QuotationOrigin.PUBLICO);
    quotation.setNotes(buildPublicNotes(request));
    quotation.setCreatedAt(now);
    quotation.setUpdatedAt(now);

    QuotationDetailEntity detail = new QuotationDetailEntity();
    detail.setQuotation(quotation);
    detail.setItemDescription(request.requestedProduct().trim());
    detail.setQuantity(1);
    detail.setEstimatedPrice(request.estimatedBudget());
    detail.setSubtotal(request.estimatedBudget());
    detail.setNotes("Evento: " + request.celebrationType().trim() + ". Porciones estimadas: " + request.estimatedServings());
    detail.setCreatedAt(now);
    quotation.getDetails().add(detail);
    quotation.setEstimatedTotal(request.estimatedBudget());

    QuotationEntity savedQuotation = quotationRepository.save(quotation);
    auditTrailService.record(
        "COTIZACION_PUBLICA_CREADA",
        "COTIZACIONES",
        "cotizacion",
        savedQuotation.getId().toString(),
        "CREAR_COTIZACION_PUBLICA",
        "Solicitud recibida desde la landing publica.",
        httpRequest
    );
    return quotationDtoMapper.toSummary(savedQuotation);
  }

  private ClientEntity resolvePublicClient(PublicQuotationRequest request) {
    String normalizedEmail = TextSupport.trimToNull(request.email());
    if (normalizedEmail != null) {
      // Reusar cliente por correo evita duplicar historial cuando la misma persona vuelve a cotizar.
      return clientRepository.findByEmailIgnoreCase(normalizedEmail)
          .orElseGet(() -> createPublicClient(request, normalizedEmail));
    }
    return createPublicClient(request, null);
  }

  private ClientEntity createPublicClient(PublicQuotationRequest request, String normalizedEmail) {
    OffsetDateTime now = OffsetDateTime.now();
    ClientEntity client = new ClientEntity();
    client.setFullName(request.fullName().trim());
    client.setPhone(TextSupport.trimToNull(request.phone()));
    client.setEmail(normalizedEmail);
    client.setNotes("Cliente originado desde formulario publico.");
    client.setRegisteredAt(now);
    client.setCreatedAt(now);
    client.setUpdatedAt(now);
    return clientRepository.save(client);
  }

  private String buildPublicNotes(PublicQuotationRequest request) {
    StringBuilder notes = new StringBuilder();
    notes.append("Solicitud web para ").append(request.celebrationType().trim()).append(". ");
    notes.append("Porciones estimadas: ").append(request.estimatedServings()).append(". ");
    if (TextSupport.trimToNull(request.notes()) != null) {
      notes.append("Detalle adicional: ").append(request.notes().trim());
    }
    return notes.toString();
  }
}


