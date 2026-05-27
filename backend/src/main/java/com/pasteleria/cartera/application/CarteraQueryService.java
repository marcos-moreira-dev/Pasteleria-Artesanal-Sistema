package com.pasteleria.cartera.application;

import java.util.List;

import com.pasteleria.cartera.infrastructure.persistence.repository.CobranzaDetalleRepository;
import com.pasteleria.cartera.infrastructure.persistence.repository.CobranzaRepository;
import com.pasteleria.cartera.infrastructure.persistence.repository.DocumentoCobrarRepository;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.security.OperacionAutorizacionService;
import com.pasteleria.common.security.Permisos;
import com.pasteleria.common.security.UserAccessPolicy;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarteraQueryService {

  private static final String SUCURSAL_TRANSICIONAL = UserAccessPolicy.DEFAULT_SUCURSAL_ID;

  private final DocumentoCobrarRepository documentoRepository;
  private final CobranzaRepository cobranzaRepository;
  private final CobranzaDetalleRepository detalleRepository;
  private final CarteraMapper mapper;
  private final OperacionAutorizacionService autorizacionService;

  public CarteraQueryService(
      DocumentoCobrarRepository documentoRepository,
      CobranzaRepository cobranzaRepository,
      CobranzaDetalleRepository detalleRepository,
      CarteraMapper mapper,
      OperacionAutorizacionService autorizacionService
  ) {
    this.documentoRepository = documentoRepository;
    this.cobranzaRepository = cobranzaRepository;
    this.detalleRepository = detalleRepository;
    this.mapper = mapper;
    this.autorizacionService = autorizacionService;
  }

  @Transactional(readOnly = true)
  public List<DocumentoCobrarSummary> documentos(String estado) {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.CARTERA_VER);
    if (estado != null && !estado.isBlank()) {
      return documentoRepository.findByEstadoOrderByFechaVencimientoAsc(estado.trim().toUpperCase()).stream()
          .map(mapper::toDocumento)
          .toList();
    }
    return documentoRepository.findTop80ByOrderByCreatedAtDesc().stream().map(mapper::toDocumento).toList();
  }

  @Transactional(readOnly = true)
  public DocumentoCobrarSummary documento(Long id) {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.CARTERA_VER);
    return documentoRepository.findById(id)
        .map(mapper::toDocumento)
        .orElseThrow(() -> new ResourceNotFoundException("Documento por cobrar no encontrado."));
  }

  @Transactional(readOnly = true)
  public List<CobranzaSummary> cobranzas() {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.CARTERA_VER);
    return cobranzaRepository.findTop80ByOrderByFechaCobranzaDesc().stream()
        .map(cobranza -> mapper.toCobranza(cobranza, detalleRepository.findByCobranzaIdOrderByIdAsc(cobranza.getId())))
        .toList();
  }
}
