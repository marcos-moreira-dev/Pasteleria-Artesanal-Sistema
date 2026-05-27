package com.pasteleria.clientes.application;

import java.time.OffsetDateTime;

import com.pasteleria.clientes.application.port.ClientRepositoryPort;
import com.pasteleria.clientes.infrastructure.persistence.entity.ClientEntity;
import com.pasteleria.clientes.application.mapper.ClientDtoMapper;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.cotizaciones.application.port.QuotationRepositoryPort;
import com.pasteleria.pedidos.application.port.OrderRepositoryPort;
import com.pasteleria.terceros.application.TerceroSynchronizationService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Encapsula las escrituras del modulo de clientes para mantener separadas las
 * mutaciones de la lectura operativa.
 */
@Service
public class ClientCommandService {

  private final ClientRepositoryPort clientRepository;
  private final OrderRepositoryPort orderRepository;
  private final QuotationRepositoryPort quotationRepository;
  private final AuditTrailService auditTrailService;
  private final ClientDtoMapper clientDtoMapper;
  private final TerceroSynchronizationService terceroSynchronizationService;

  public ClientCommandService(
      ClientRepositoryPort clientRepository,
      OrderRepositoryPort orderRepository,
      QuotationRepositoryPort quotationRepository,
      AuditTrailService auditTrailService,
      ClientDtoMapper clientDtoMapper,
      TerceroSynchronizationService terceroSynchronizationService
  ) {
    this.clientRepository = clientRepository;
    this.orderRepository = orderRepository;
    this.quotationRepository = quotationRepository;
    this.auditTrailService = auditTrailService;
    this.clientDtoMapper = clientDtoMapper;
    this.terceroSynchronizationService = terceroSynchronizationService;
  }

  @Transactional
  public ClientSummary createClient(CreateClientRequest request, HttpServletRequest httpRequest) {
    ClientEntity client = new ClientEntity();
    OffsetDateTime now = OffsetDateTime.now();
    clientDtoMapper.applyCreateRequest(client, request, now);
    ClientEntity saved = clientRepository.save(client);
    terceroSynchronizationService.syncCliente(
        saved.getId(),
        saved.getFullName(),
        saved.getPhone(),
        saved.getEmail(),
        saved.getNotes()
    );
    ClientSummary summary = clientDtoMapper.toSummary(saved);
    auditTrailService.recordChange(
        "CLIENTE_CREADO",
        "CLIENTES",
        "cliente",
        summary.id().toString(),
        "CREAR_CLIENTE",
        null,
        summary,
        "Alta de ficha comercial.",
        httpRequest
    );
    return summary;
  }

  /**
   * Permite complementar o corregir la ficha comercial del cliente sin tocar
   * pedidos ni cotizaciones.
   */
  @Transactional
  public ClientSummary updateClient(Long clientId, UpdateClientRequest request, HttpServletRequest httpRequest) {
    ClientEntity client = clientRepository.findById(clientId)
        .orElseThrow(() -> new ResourceNotFoundException("El cliente indicado no existe."));

    ClientSummary previous = clientDtoMapper.toSummary(client);
    clientDtoMapper.applyUpdateRequest(client, request, OffsetDateTime.now());
    ClientEntity saved = clientRepository.save(client);
    terceroSynchronizationService.syncCliente(
        saved.getId(),
        saved.getFullName(),
        saved.getPhone(),
        saved.getEmail(),
        saved.getNotes()
    );
    ClientSummary current = clientDtoMapper.toSummary(saved);
    auditTrailService.recordChange(
        "CLIENTE_ACTUALIZADO",
        "CLIENTES",
        "cliente",
        client.getId().toString(),
        "ACTUALIZAR_CLIENTE",
        previous,
        current,
        "Actualizacion de datos comerciales del cliente.",
        httpRequest
    );
    return current;
  }

  @Transactional
  public void deleteClient(Long clientId, HttpServletRequest httpRequest) {
    ClientEntity client = clientRepository.findById(clientId)
        .orElseThrow(() -> new ResourceNotFoundException("El cliente indicado no existe o ya fue eliminado."));

    if (orderRepository.existsByClientId(clientId) || quotationRepository.existsByClientId(clientId)) {
      throw new BusinessRuleException(
          "No se puede eliminar el cliente porque ya tiene pedidos o cotizaciones asociados."
      );
    }

    auditTrailService.recordChange(
        "CLIENTE_ELIMINADO",
        "CLIENTES",
        "cliente",
        client.getId().toString(),
        "ELIMINAR_CLIENTE",
        clientDtoMapper.toSummary(client),
        null,
        "Retiro del cliente sin trazas operativas asociadas.",
        httpRequest
    );
    clientRepository.delete(client);
  }
}


