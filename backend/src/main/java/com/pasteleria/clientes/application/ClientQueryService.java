package com.pasteleria.clientes.application;

import java.util.List;
import java.util.Locale;

import com.pasteleria.clientes.application.port.ClientRepositoryPort;
import com.pasteleria.clientes.application.mapper.ClientDtoMapper;
import com.pasteleria.common.pagination.PageMapper;
import com.pasteleria.common.pagination.PageRequestFactory;
import com.pasteleria.common.pagination.PageResponseDto;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Concentra la lectura del modulo de clientes para que los controladores no
 * dependan de servicios mixtos de lectura y escritura.
 */
@Service
@Transactional(readOnly = true)
public class ClientQueryService {

  private final ClientRepositoryPort clientRepository;
  private final ClientDtoMapper clientDtoMapper;
  private final PageMapper pageMapper;
  private final PageRequestFactory pageRequestFactory;

  public ClientQueryService(
      ClientRepositoryPort clientRepository,
      ClientDtoMapper clientDtoMapper,
      PageMapper pageMapper,
      PageRequestFactory pageRequestFactory
  ) {
    this.clientRepository = clientRepository;
    this.clientDtoMapper = clientDtoMapper;
    this.pageMapper = pageMapper;
    this.pageRequestFactory = pageRequestFactory;
  }

  public List<ClientSummary> listClients() {
    return clientRepository.findAllByOrderByCreatedAtDesc().stream()
        .map(clientDtoMapper::toSummary)
        .toList();
  }

  public PageResponseDto<ClientSummary> listClientsPage(int page, int size, String query) {
    var pageable = pageRequestFactory.create(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    String normalizedQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);

    return pageMapper.toPageResponseDto(
        (normalizedQuery.isBlank()
            ? clientRepository.findAllByOrderByCreatedAtDesc(pageable)
            : clientRepository.findBySearchTerm(normalizedQuery, pageable))
            .map(clientDtoMapper::toSummary)
    );
  }
}


