package com.pasteleria.cartera.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.pasteleria.cartera.infrastructure.persistence.entity.CobranzaDetalleEntity;
import com.pasteleria.cartera.infrastructure.persistence.entity.CobranzaEntity;
import com.pasteleria.cartera.infrastructure.persistence.entity.DocumentoCobrarEntity;
import com.pasteleria.cartera.infrastructure.persistence.repository.CobranzaDetalleRepository;
import com.pasteleria.cartera.infrastructure.persistence.repository.CobranzaRepository;
import com.pasteleria.cartera.infrastructure.persistence.repository.DocumentoCobrarRepository;
import com.pasteleria.clientes.infrastructure.persistence.entity.ClientEntity;
import com.pasteleria.clientes.application.port.ClientRepositoryPort;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.security.OperacionAutorizacionService;
import com.pasteleria.common.security.Permisos;
import com.pasteleria.common.security.UserAccessPolicy;
import com.pasteleria.erp.application.ErpFinancialPolicy;
import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderEntity;
import com.pasteleria.pedidos.application.port.OrderRepositoryPort;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarteraCommandService {

  private static final String SUCURSAL_TRANSICIONAL = UserAccessPolicy.DEFAULT_SUCURSAL_ID;

  private final DocumentoCobrarRepository documentoRepository;
  private final CobranzaRepository cobranzaRepository;
  private final CobranzaDetalleRepository detalleRepository;
  private final ClientRepositoryPort clientRepository;
  private final OrderRepositoryPort orderRepository;
  private final CarteraMapper mapper;
  private final OperacionAutorizacionService autorizacionService;
  private final AuditTrailService auditTrailService;

  public CarteraCommandService(
      DocumentoCobrarRepository documentoRepository,
      CobranzaRepository cobranzaRepository,
      CobranzaDetalleRepository detalleRepository,
      ClientRepositoryPort clientRepository,
      OrderRepositoryPort orderRepository,
      CarteraMapper mapper,
      OperacionAutorizacionService autorizacionService,
      AuditTrailService auditTrailService
  ) {
    this.documentoRepository = documentoRepository;
    this.cobranzaRepository = cobranzaRepository;
    this.detalleRepository = detalleRepository;
    this.clientRepository = clientRepository;
    this.orderRepository = orderRepository;
    this.mapper = mapper;
    this.autorizacionService = autorizacionService;
    this.auditTrailService = auditTrailService;
  }

  @Transactional
  public DocumentoCobrarSummary crearDocumento(CrearDocumentoCobrarRequest request, HttpServletRequest httpRequest) {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.COBRANZAS_REGISTRAR);
    LocalDateTime now = LocalDateTime.now();
    OrderEntity pedido = null;
    ClientEntity cliente;

    if (request.pedidoId() != null) {
      pedido = orderRepository.findById(request.pedidoId())
          .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado."));
      if (documentoRepository.findByPedidoId(pedido.getId()).isPresent()) {
        throw new BusinessRuleException("El pedido ya tiene un documento por cobrar asociado.");
      }
      cliente = pedido.getClient();
    } else {
      if (request.clienteId() == null) {
        throw new BusinessRuleException("Debe indicar clienteId o pedidoId para crear un documento por cobrar.");
      }
      cliente = clientRepository.findById(request.clienteId())
          .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado."));
    }

    BigDecimal total = request.total();
    if (total == null && pedido != null) {
      total = pedido.getEstimatedTotal();
    }
    total = ErpFinancialPolicy.money(total);
    ErpFinancialPolicy.exigirMontoPositivo(total, "El total del documento por cobrar debe ser mayor a cero.");

    DocumentoCobrarEntity entity = new DocumentoCobrarEntity();
    entity.setCliente(cliente);
    entity.setPedido(pedido);
    entity.setCodigo(codigoDocumento(request.codigo(), pedido));
    entity.setEstado(EstadoDocumentoCobrar.PENDIENTE.name());
    entity.setFechaEmision(request.fechaEmision() == null ? now : request.fechaEmision());
    entity.setFechaVencimiento(request.fechaVencimiento());
    entity.setTotal(total);
    entity.setSaldo(total);
    entity.setObservaciones(blankToNull(request.observaciones()));
    entity.setCreatedAt(now);
    entity.setUpdatedAt(now);

    DocumentoCobrarEntity saved = documentoRepository.save(entity);
    DocumentoCobrarSummary summary = mapper.toDocumento(saved);
    auditTrailService.recordChange(
        "CARTERA_DOCUMENTO_COBRAR_CREADO",
        "CARTERA",
        "documento_cobrar",
        saved.getId().toString(),
        "CREAR_DOCUMENTO_COBRAR",
        null,
        summary,
        "Documento por cobrar creado.",
        httpRequest
    );
    return summary;
  }

  @Transactional
  public CobranzaSummary registrarCobranza(RegistrarCobranzaRequest request, HttpServletRequest httpRequest) {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.COBRANZAS_REGISTRAR);
    LocalDateTime now = LocalDateTime.now();
    BigDecimal montoTotal = ErpFinancialPolicy.money(request.montoTotal());
    ErpFinancialPolicy.exigirMontoPositivo(montoTotal, "El monto total de la cobranza debe ser mayor a cero.");

    List<AplicacionCobranzaRequest> aplicaciones = request.aplicaciones();
    BigDecimal sumaAplicaciones = aplicaciones.stream()
        .map(AplicacionCobranzaRequest::monto)
        .map(ErpFinancialPolicy::money)
        .reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
    if (sumaAplicaciones.compareTo(montoTotal) != 0) {
      throw new BusinessRuleException("La suma de aplicaciones debe coincidir con el monto total de la cobranza.");
    }

    DocumentoCobrarEntity primerDocumento = documentoRepository.findByIdForUpdate(aplicaciones.get(0).documentoCobrarId())
        .orElseThrow(() -> new ResourceNotFoundException("Documento por cobrar no encontrado."));
    ClientEntity cliente = primerDocumento.getCliente();
    if (request.clienteId() != null && !Objects.equals(request.clienteId(), cliente.getId())) {
      throw new BusinessRuleException("El cliente de la cobranza no coincide con el primer documento aplicado.");
    }

    CobranzaEntity cobranza = new CobranzaEntity();
    cobranza.setCliente(cliente);
    cobranza.setCodigo("COB-" + System.currentTimeMillis());
    cobranza.setFechaCobranza(request.fechaCobranza() == null ? now : request.fechaCobranza());
    cobranza.setMontoTotal(montoTotal);
    cobranza.setMedioPago(blankToNull(request.medioPago()));
    cobranza.setReferenciaPago(blankToNull(request.referenciaPago()));
    cobranza.setEstado(EstadoCobranza.REGISTRADA.name());
    cobranza.setObservaciones(blankToNull(request.observaciones()));
    cobranza.setCreatedAt(now);
    cobranza.setUpdatedAt(now);
    CobranzaEntity savedCobranza = cobranzaRepository.save(cobranza);

    for (AplicacionCobranzaRequest aplicacion : aplicaciones) {
      DocumentoCobrarEntity documento = documentoRepository.findByIdForUpdate(aplicacion.documentoCobrarId())
          .orElseThrow(() -> new ResourceNotFoundException("Documento por cobrar no encontrado."));
      if (!Objects.equals(documento.getCliente().getId(), cliente.getId())) {
        throw new BusinessRuleException("Todas las aplicaciones de una cobranza deben pertenecer al mismo cliente.");
      }
      if (EstadoDocumentoCobrar.ANULADO.name().equals(documento.getEstado())) {
        throw new BusinessRuleException("No se puede aplicar una cobranza a un documento anulado.");
      }
      BigDecimal montoAplicado = ErpFinancialPolicy.money(aplicacion.monto());
      ErpFinancialPolicy.validarAplicacionContraSaldo(
          montoAplicado,
          documento.getSaldo(),
          "La cobranza no puede superar el saldo del documento por cobrar."
      );
      BigDecimal saldoAnterior = ErpFinancialPolicy.money(documento.getSaldo());
      BigDecimal saldoPosterior = ErpFinancialPolicy.money(saldoAnterior.subtract(montoAplicado));
      documento.setSaldo(saldoPosterior);
      documento.setEstado(ErpFinancialPolicy.estadoPorSaldo(saldoPosterior, documento.getTotal()));
      documento.setUpdatedAt(now);
      DocumentoCobrarEntity savedDocumento = documentoRepository.save(documento);

      CobranzaDetalleEntity detalle = new CobranzaDetalleEntity();
      detalle.setCobranza(savedCobranza);
      detalle.setDocumentoCobrar(savedDocumento);
      detalle.setMontoAplicado(montoAplicado);
      detalle.setSaldoAnterior(saldoAnterior);
      detalle.setSaldoPosterior(saldoPosterior);
      detalle.setCreatedAt(now);
      detalleRepository.save(detalle);
    }

    CobranzaSummary summary = mapper.toCobranza(savedCobranza, detalleRepository.findByCobranzaIdOrderByIdAsc(savedCobranza.getId()));
    auditTrailService.recordChange(
        "CARTERA_COBRANZA_REGISTRADA",
        "CARTERA",
        "cobranza",
        savedCobranza.getId().toString(),
        "REGISTRAR_COBRANZA",
        null,
        summary,
        "Cobranza registrada contra saldos pendientes.",
        httpRequest
    );
    return summary;
  }

  private String codigoDocumento(String codigo, OrderEntity pedido) {
    String limpio = blankToNull(codigo);
    if (limpio != null) {
      return limpio;
    }
    if (pedido != null && pedido.getCode() != null && !pedido.getCode().isBlank()) {
      return "CXC-" + pedido.getCode();
    }
    return "CXC-" + System.currentTimeMillis();
  }

  private String blankToNull(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return value.trim();
  }
}
