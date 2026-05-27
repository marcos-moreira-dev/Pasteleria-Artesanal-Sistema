package com.pasteleria.abastecimiento.application;

import com.pasteleria.abastecimiento.application.OrdenCompraDetailDto.OrdenCompraDetalleDto;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.OrdenCompraEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.OrdenCompraRepository;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.security.OperacionAutorizacionService;
import com.pasteleria.common.security.Permisos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrdenCompraQueryService {

  private static final String SUCURSAL_TRANSICIONAL = "MATRIZ";

  private final OrdenCompraRepository ordenCompraRepository;
  private final OperacionAutorizacionService autorizacionService;

  public OrdenCompraQueryService(
      OrdenCompraRepository ordenCompraRepository,
      OperacionAutorizacionService autorizacionService
  ) {
    this.ordenCompraRepository = ordenCompraRepository;
    this.autorizacionService = autorizacionService;
  }

  public Page<OrdenCompraSummaryDto> findOrdenes(Pageable pageable, String query, String estado) {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.COMPRAS_VER);
    Page<OrdenCompraEntity> page;
    
    if (query != null && !query.trim().isEmpty()) {
      // Búsqueda por código o nombre de proveedor
      page = ordenCompraRepository.findByCodigoContainingIgnoreCaseOrProveedorNameContainingIgnoreCase(
          query, query, pageable);
    } else if (estado != null && !estado.trim().isEmpty()) {
      page = ordenCompraRepository.findByEstado(estado, pageable);
    } else {
      page = ordenCompraRepository.findAll(pageable);
    }
    
    return page.map(this::toSummaryDto);
  }

  public OrdenCompraDetailDto findOrdenById(Long id) {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.COMPRAS_VER);
    OrdenCompraEntity orden = ordenCompraRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Orden de compra no encontrada: " + id));
    
    return toDetailDto(orden);
  }

  public List<OrdenCompraSummaryDto> findOrdenesByProveedor(Long proveedorId) {
    autorizacionService.exigirPermisoSucursal(SUCURSAL_TRANSICIONAL, Permisos.COMPRAS_VER);
    return ordenCompraRepository.findByProveedorId(proveedorId).stream()
        .map(this::toSummaryDto)
        .collect(Collectors.toList());
  }

  private OrdenCompraSummaryDto toSummaryDto(OrdenCompraEntity orden) {
    return new OrdenCompraSummaryDto(
        orden.getOrdenCompraId(),
        orden.getCodigo(),
        orden.getEstado(),
        orden.getProveedor() != null ? orden.getProveedor().getId() : null,
        orden.getProveedor() != null ? orden.getProveedor().getName() : "N/A",
        orden.getFechaEmision(),
        orden.getFechaEntregaEsperada(),
        orden.getTotalEstimado(),
        orden.getCreatedAt()
    );
  }

  private OrdenCompraDetailDto toDetailDto(OrdenCompraEntity orden) {
    List<OrdenCompraDetalleDto> detalles = orden.getDetalles().stream()
        .map(det -> new OrdenCompraDetalleDto(
            det.getOrdenCompraDetalleId(),
            det.getItemTipo(),
            det.getItemId(),
            det.getItemNombre(),
            det.getItemCodigo(),
            det.getCantidad(),
            det.getPrecioUnitario(),
            det.getSubtotal(),
            det.getCantidadRecibida(),
            det.getObservaciones()
        ))
        .collect(Collectors.toList());

    return new OrdenCompraDetailDto(
        orden.getOrdenCompraId(),
        orden.getCodigo(),
        orden.getEstado(),
        orden.getProveedor() != null ? orden.getProveedor().getId() : null,
        orden.getProveedor() != null ? orden.getProveedor().getName() : "N/A",
        orden.getProveedor() != null ? orden.getProveedor().getPhone() : null,
        orden.getProveedor() != null ? orden.getProveedor().getEmail() : null,
        orden.getFechaEmision(),
        orden.getFechaEntregaEsperada(),
        orden.getFechaEntregaReal(),
        orden.getTotalEstimado(),
        orden.getObservaciones(),
        detalles,
        orden.getCreatedAt(),
        orden.getUpdatedAt()
    );
  }
}
