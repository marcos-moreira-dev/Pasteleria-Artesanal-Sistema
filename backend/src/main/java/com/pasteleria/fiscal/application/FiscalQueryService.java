package com.pasteleria.fiscal.application;

import java.util.List;

import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.security.OperacionAutorizacionService;
import com.pasteleria.common.security.Permisos;
import com.pasteleria.fiscal.infrastructure.persistence.repository.DocumentoFiscalRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FiscalQueryService {

  private final DocumentoFiscalRepository repository;
  private final FiscalMapper mapper;
  private final OperacionAutorizacionService autorizacionService;

  public FiscalQueryService(
      DocumentoFiscalRepository repository,
      FiscalMapper mapper,
      OperacionAutorizacionService autorizacionService
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.autorizacionService = autorizacionService;
  }

  @Transactional(readOnly = true)
  public List<DocumentoFiscalSummary> documentos() {
    autorizacionService.exigirPermisoGlobal(Permisos.FISCAL_VER);
    return repository.findTop80ByOrderByFechaEmisionDescIdDesc().stream()
        .map(mapper::toSummary)
        .toList();
  }

  @Transactional(readOnly = true)
  public DocumentoFiscalSummary documento(Long id) {
    autorizacionService.exigirPermisoGlobal(Permisos.FISCAL_VER);
    return repository.findById(id)
        .map(mapper::toSummary)
        .orElseThrow(() -> new ResourceNotFoundException("Documento fiscal no encontrado."));
  }
}
