package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.pasteleria.abastecimiento.application.CreateOrdenCompraRequest.CreateOrdenCompraDetalleItem;
import com.pasteleria.abastecimiento.application.OrdenCompraDetailDto.OrdenCompraDetalleDto;
import com.pasteleria.abastecimiento.application.RecibirOrdenCompraRequest.RecibirOrdenCompraItem;
import com.pasteleria.abastecimiento.application.UpdateOrdenCompraRequest.UpdateOrdenCompraDetalleItem;
import com.pasteleria.abastecimiento.application.port.IngredienteRepositoryPort;
import com.pasteleria.abastecimiento.application.port.InsumoRepositoryPort;
import com.pasteleria.abastecimiento.application.port.ProveedorRepositoryPort;
import com.pasteleria.abastecimiento.domain.model.EstadoOrdenCompra;
import com.pasteleria.abastecimiento.domain.model.ItemTipo;
import com.pasteleria.abastecimiento.domain.model.TipoMovimiento;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.IngredienteEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InsumoEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.OrdenCompraDetalleEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.OrdenCompraEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.ProveedorEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.repository.OrdenCompraRepository;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.security.AuthenticatedUserService;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class OrdenCompraCommandService {

  private final OrdenCompraRepository ordenCompraRepository;
  private final ProveedorRepositoryPort proveedorRepository;
  private final IngredienteRepositoryPort ingredienteRepository;
  private final InsumoRepositoryPort insumoRepository;
  private final InventarioMovimientoCommandService inventarioMovimientoCommandService;
  private final AuthenticatedUserService authenticatedUserService;
  private final AuditTrailService auditTrailService;

  public OrdenCompraCommandService(
      OrdenCompraRepository ordenCompraRepository,
      ProveedorRepositoryPort proveedorRepository,
      IngredienteRepositoryPort ingredienteRepository,
      InsumoRepositoryPort insumoRepository,
      InventarioMovimientoCommandService inventarioMovimientoCommandService,
      AuthenticatedUserService authenticatedUserService,
      AuditTrailService auditTrailService
  ) {
    this.ordenCompraRepository = ordenCompraRepository;
    this.proveedorRepository = proveedorRepository;
    this.ingredienteRepository = ingredienteRepository;
    this.insumoRepository = insumoRepository;
    this.inventarioMovimientoCommandService = inventarioMovimientoCommandService;
    this.authenticatedUserService = authenticatedUserService;
    this.auditTrailService = auditTrailService;
  }

  @Transactional
  public OrdenCompraSummaryDto createOrdenCompra(CreateOrdenCompraRequest request, HttpServletRequest httpRequest) {
    ProveedorEntity proveedor = resolveProveedorActiva(request.proveedorId());

    OrdenCompraEntity entity = new OrdenCompraEntity();
    LocalDateTime now = LocalDateTime.now();

    entity.setProveedor(proveedor);
    entity.setCodigo(normalizeCode(request.codigo()));
    entity.setEstado(EstadoOrdenCompra.BORRADOR.name());
    entity.setFechaEmision(null);
    entity.setFechaEntregaEsperada(parseOptionalDate(request.fechaEntregaEstimada()));
    entity.setFechaEntregaReal(null);
    entity.setObservaciones(blankToNull(request.observaciones()));
    entity.setActivo(Boolean.TRUE);
    entity.setCreatedAt(now);
    entity.setUpdatedAt(now);

    applyCreateDetalles(entity, request.detalles(), now);

    OrdenCompraEntity saved = ordenCompraRepository.save(entity);
    OrdenCompraSummaryDto current = toSummaryDto(saved);

    auditTrailService.recordChange(
        "ORDEN_COMPRA_CREADA",
        "ABASTECIMIENTO",
        "orden_compra",
        saved.getOrdenCompraId().toString(),
        "CREAR_ORDEN_COMPRA",
        null,
        current,
        "Alta de orden de compra en borrador.",
        httpRequest
    );

    return current;
  }

  @Transactional
  public OrdenCompraSummaryDto updateOrdenCompra(
      Long ordenId,
      UpdateOrdenCompraRequest request,
      HttpServletRequest httpRequest
  ) {
    OrdenCompraEntity entity = findOrden(ordenId);
    ensureDraft(entity, "Solo las órdenes en borrador pueden editarse.");

    OrdenCompraSummaryDto previous = toSummaryDto(entity);

    if (request.proveedorId() != null) {
      entity.setProveedor(resolveProveedorActiva(request.proveedorId()));
    }
    if (request.codigo() != null && !request.codigo().isBlank()) {
      entity.setCodigo(normalizeCode(request.codigo()));
    }
    if (request.observaciones() != null) {
      entity.setObservaciones(blankToNull(request.observaciones()));
    }
    if (request.fechaEntregaEstimada() != null) {
      entity.setFechaEntregaEsperada(parseOptionalDate(request.fechaEntregaEstimada()));
    }
    if (request.detalles() != null) {
      if (request.detalles().isEmpty()) {
        throw new BusinessRuleException("La orden de compra debe conservar al menos una línea.");
      }
      replaceDetalles(entity, request.detalles(), LocalDateTime.now());
    }

    entity.setUpdatedAt(LocalDateTime.now());
    OrdenCompraEntity saved = ordenCompraRepository.save(entity);
    OrdenCompraSummaryDto current = toSummaryDto(saved);

    auditTrailService.recordChange(
        "ORDEN_COMPRA_ACTUALIZADA",
        "ABASTECIMIENTO",
        "orden_compra",
        saved.getOrdenCompraId().toString(),
        "ACTUALIZAR_ORDEN_COMPRA",
        previous,
        current,
        "Actualización de orden de compra en borrador.",
        httpRequest
    );

    return current;
  }

  @Transactional
  public OrdenCompraSummaryDto updateEstado(
      Long ordenId,
      UpdateOrdenCompraEstadoRequest request,
      HttpServletRequest httpRequest
  ) {
    OrdenCompraEntity entity = findOrden(ordenId);
    EstadoOrdenCompra currentStatus = parseEstado(entity.getEstado());
    EstadoOrdenCompra targetStatus = request.estado();

    validateManualTransition(currentStatus, targetStatus);
    OrdenCompraSummaryDto previous = toSummaryDto(entity);

    if (targetStatus == EstadoOrdenCompra.ENVIADA && entity.getFechaEmision() == null) {
      entity.setFechaEmision(LocalDateTime.now());
    }
    entity.setEstado(targetStatus.name());
    entity.setUpdatedAt(LocalDateTime.now());

    if (request.observaciones() != null) {
      entity.setObservaciones(blankToNull(request.observaciones()));
    }

    OrdenCompraEntity saved = ordenCompraRepository.save(entity);
    OrdenCompraSummaryDto current = toSummaryDto(saved);

    auditTrailService.recordChange(
        "ORDEN_COMPRA_ESTADO_ACTUALIZADO",
        "ABASTECIMIENTO",
        "orden_compra",
        saved.getOrdenCompraId().toString(),
        "ACTUALIZAR_ESTADO_ORDEN_COMPRA",
        previous,
        current,
        request.observaciones(),
        httpRequest
    );

    return current;
  }

  @Transactional
  public OrdenCompraDetailDto receiveOrdenCompra(
      Long ordenId,
      RecibirOrdenCompraRequest request,
      HttpServletRequest httpRequest
  ) {
    OrdenCompraEntity entity = findOrden(ordenId);
    EstadoOrdenCompra currentStatus = parseEstado(entity.getEstado());

    if (currentStatus != EstadoOrdenCompra.ENVIADA && currentStatus != EstadoOrdenCompra.RECIBIDA_PARCIAL) {
      throw new IllegalStateException("Solo se pueden recibir órdenes enviadas o parcialmente recibidas.");
    }

    OrdenCompraDetailDto previous = toDetailDto(entity);
    Map<Long, OrdenCompraDetalleEntity> detallesPorId = new LinkedHashMap<>();
    for (OrdenCompraDetalleEntity detalle : entity.getDetalles()) {
      detallesPorId.put(detalle.getOrdenCompraDetalleId(), detalle);
    }

    boolean huboRecepcion = false;
    UserEntity actor = authenticatedUserService.currentUser().orElse(null);

    for (RecibirOrdenCompraItem item : request.items()) {
      OrdenCompraDetalleEntity detalle = detallesPorId.get(item.detalleId());
      if (detalle == null) {
        throw new ResourceNotFoundException("No existe el detalle de orden de compra indicado.");
      }

      int cantidadRecibida = toExactInteger(item.cantidadRecibida(), "La cantidad recibida debe ser un entero.");
      if (cantidadRecibida <= 0) {
        continue;
      }

      int yaRecibida = defaultInt(detalle.getCantidadRecibida());
      int faltante = detalle.getCantidad() - yaRecibida;
      if (cantidadRecibida > faltante) {
        throw new BusinessRuleException("La recepción excede la cantidad pendiente del item " + detalle.getItemNombre() + ".");
      }

      detalle.setCantidadRecibida(yaRecibida + cantidadRecibida);

      inventarioMovimientoCommandService.createMovimiento(
          new CreateInventarioMovimientoRequest(
              detalle.getItemTipo(),
              detalle.getItemId(),
              TipoMovimiento.ENTRADA_COMPRA.name(),
              BigDecimal.valueOf(cantidadRecibida),
              "ORDEN_COMPRA",
              entity.getCodigo(),
              null,
              "Recepción de mercadería de la orden " + entity.getCodigo()
          ),
          actor,
          httpRequest
      );

      huboRecepcion = true;
    }

    if (!huboRecepcion) {
      throw new BusinessRuleException("Debe registrar al menos una cantidad recibida mayor a cero.");
    }

    boolean completa = entity.getDetalles().stream()
        .allMatch(detalle -> defaultInt(detalle.getCantidadRecibida()) >= detalle.getCantidad());

    entity.setEstado((completa ? EstadoOrdenCompra.RECIBIDA : EstadoOrdenCompra.RECIBIDA_PARCIAL).name());
    if (completa) {
      entity.setFechaEntregaReal(LocalDateTime.now());
    }
    entity.setUpdatedAt(LocalDateTime.now());

    OrdenCompraEntity saved = ordenCompraRepository.save(entity);
    OrdenCompraDetailDto current = toDetailDto(saved);

    auditTrailService.recordChange(
        "ORDEN_COMPRA_RECIBIDA",
        "ABASTECIMIENTO",
        "orden_compra",
        saved.getOrdenCompraId().toString(),
        "RECIBIR_ORDEN_COMPRA",
        previous,
        current,
        completa ? "Recepción completa de orden de compra." : "Recepción parcial de orden de compra.",
        httpRequest
    );

    return current;
  }

  private ProveedorEntity resolveProveedorActiva(Long proveedorId) {
    ProveedorEntity proveedor = proveedorRepository.findById(proveedorId)
        .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado."));
    if (!proveedor.isActive()) {
      throw new BusinessRuleException("No se puede operar con un proveedor inactivo.");
    }
    return proveedor;
  }

  private void applyCreateDetalles(
      OrdenCompraEntity entity,
      List<CreateOrdenCompraDetalleItem> detalles,
      LocalDateTime now
  ) {
    List<OrdenCompraDetalleEntity> nuevasLineas = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO;

    for (CreateOrdenCompraDetalleItem detalle : detalles) {
      ItemSnapshot item = resolveActiveItem(detalle.itemTipo(), detalle.itemId());
      OrdenCompraDetalleEntity linea = buildDetalleEntity(
          entity,
          item,
          detalle.cantidad(),
          detalle.precioUnitario(),
          now
      );
      nuevasLineas.add(linea);
      total = total.add(linea.getSubtotal());
    }

    entity.setDetalles(nuevasLineas);
    entity.setTotalEstimado(total.setScale(2, RoundingMode.HALF_UP));
  }

  private void replaceDetalles(
      OrdenCompraEntity entity,
      List<UpdateOrdenCompraDetalleItem> detalles,
      LocalDateTime now
  ) {
    entity.getDetalles().clear();

    List<OrdenCompraDetalleEntity> nuevasLineas = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO;

    for (UpdateOrdenCompraDetalleItem detalle : detalles) {
      ItemSnapshot item = resolveActiveItem(detalle.itemTipo(), detalle.itemId());
      OrdenCompraDetalleEntity linea = buildDetalleEntity(
          entity,
          item,
          detalle.cantidad(),
          detalle.precioUnitario(),
          now
      );
      nuevasLineas.add(linea);
      total = total.add(linea.getSubtotal());
    }

    entity.getDetalles().addAll(nuevasLineas);
    entity.setTotalEstimado(total.setScale(2, RoundingMode.HALF_UP));
  }

  private OrdenCompraDetalleEntity buildDetalleEntity(
      OrdenCompraEntity ordenCompra,
      ItemSnapshot item,
      BigDecimal cantidadRaw,
      BigDecimal precioUnitarioRaw,
      LocalDateTime now
  ) {
    int cantidad = toExactInteger(cantidadRaw, "La cantidad de la orden debe ser un entero.");
    BigDecimal precioUnitario = precioUnitarioRaw.setScale(4, RoundingMode.HALF_UP);
    BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad)).setScale(2, RoundingMode.HALF_UP);

    OrdenCompraDetalleEntity detalle = new OrdenCompraDetalleEntity();
    detalle.setOrdenCompra(ordenCompra);
    detalle.setItemTipo(item.itemTipo().name());
    detalle.setItemId(item.itemId());
    detalle.setItemNombre(item.itemNombre());
    detalle.setItemCodigo(item.itemCodigo());
    detalle.setCantidad(cantidad);
    detalle.setPrecioUnitario(precioUnitario);
    detalle.setSubtotal(subtotal);
    detalle.setCantidadRecibida(0);
    detalle.setObservaciones(null);
    detalle.setCreatedAt(now);
    return detalle;
  }

  private ItemSnapshot resolveActiveItem(String itemTipoRaw, Long itemId) {
    ItemTipo itemTipo = parseItemTipo(itemTipoRaw);

    if (itemTipo == ItemTipo.INGREDIENTE) {
      IngredienteEntity ingrediente = ingredienteRepository.findById(itemId)
          .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado."));
      if (!ingrediente.isActive()) {
        throw new BusinessRuleException("No se puede comprar un ingrediente inactivo.");
      }
      return new ItemSnapshot(ItemTipo.INGREDIENTE, ingrediente.getId(), ingrediente.getCode(), ingrediente.getName());
    }

    InsumoEntity insumo = insumoRepository.findById(itemId)
        .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado."));
    if (!insumo.isActive()) {
      throw new BusinessRuleException("No se puede comprar un insumo inactivo.");
    }
    return new ItemSnapshot(ItemTipo.INSUMO, insumo.getId(), insumo.getCode(), insumo.getName());
  }

  private OrdenCompraEntity findOrden(Long ordenId) {
    return ordenCompraRepository.findById(ordenId)
        .orElseThrow(() -> new ResourceNotFoundException("Orden de compra no encontrada."));
  }

  private void ensureDraft(OrdenCompraEntity entity, String message) {
    if (parseEstado(entity.getEstado()) != EstadoOrdenCompra.BORRADOR) {
      throw new IllegalStateException(message);
    }
  }

  private void validateManualTransition(EstadoOrdenCompra current, EstadoOrdenCompra target) {
    if (current == target) {
      return;
    }

    boolean valid = switch (current) {
      case BORRADOR -> target == EstadoOrdenCompra.ENVIADA || target == EstadoOrdenCompra.CANCELADA;
      case ENVIADA, RECIBIDA_PARCIAL, RECIBIDA, CANCELADA -> false;
    };

    if (!valid) {
      throw new IllegalStateException("La transición manual de estado de orden de compra no es válida.");
    }
  }

  private EstadoOrdenCompra parseEstado(String value) {
    try {
      return EstadoOrdenCompra.valueOf(value);
    } catch (IllegalArgumentException exception) {
      throw new IllegalStateException("La orden de compra tiene un estado no reconocido.");
    }
  }

  private ItemTipo parseItemTipo(String value) {
    try {
      return ItemTipo.valueOf(value);
    } catch (IllegalArgumentException exception) {
      throw new BusinessRuleException("El tipo de item de la orden de compra no es válido.");
    }
  }

  private String normalizeCode(String code) {
    return code.trim().toUpperCase();
  }

  private String blankToNull(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return value.trim();
  }

  private LocalDateTime parseOptionalDate(String raw) {
    if (raw == null) {
      return null;
    }
    String value = raw.trim();
    if (value.isEmpty()) {
      return null;
    }

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

    throw new BusinessRuleException("La fecha de entrega estimada no tiene un formato válido.");
  }

  private int toExactInteger(BigDecimal value, String message) {
    try {
      return value.stripTrailingZeros().intValueExact();
    } catch (ArithmeticException exception) {
      throw new BusinessRuleException(message);
    }
  }

  private int defaultInt(Integer value) {
    return value != null ? value : 0;
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
        .toList();

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

  private record ItemSnapshot(ItemTipo itemTipo, Long itemId, String itemCodigo, String itemNombre) {
  }
}
