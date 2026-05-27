package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.pasteleria.abastecimiento.application.mapper.InventarioMovimientoDtoMapper;
import com.pasteleria.abastecimiento.application.port.IngredienteRepositoryPort;
import com.pasteleria.abastecimiento.application.port.InsumoRepositoryPort;
import com.pasteleria.abastecimiento.application.port.InventarioMovimientoRepositoryPort;
import com.pasteleria.abastecimiento.domain.model.ItemTipo;
import com.pasteleria.abastecimiento.domain.model.TipoMovimiento;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.IngredienteEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InsumoEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InventarioMovimientoEntity;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.common.security.AuthenticatedUserService;
import com.pasteleria.common.security.OperacionAutorizacionService;
import com.pasteleria.common.security.Permisos;
import com.pasteleria.common.security.UserAccessPolicy;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class InventarioMovimientoCommandService {

  private static final String SUCURSAL_TRANSICIONAL = UserAccessPolicy.DEFAULT_SUCURSAL_ID;

  private final InventarioMovimientoRepositoryPort movimientoRepository;
  private final IngredienteRepositoryPort ingredienteRepository;
  private final InsumoRepositoryPort insumoRepository;
  private final InventarioMovimientoDtoMapper mapper;
  private final InventarioMovimientoPolicy movimientoPolicy;
  private final OperacionAutorizacionService autorizacionService;
  private final AuthenticatedUserService authenticatedUserService;
  private final AuditTrailService auditTrailService;

  public InventarioMovimientoCommandService(
      InventarioMovimientoRepositoryPort movimientoRepository,
      IngredienteRepositoryPort ingredienteRepository,
      InsumoRepositoryPort insumoRepository,
      InventarioMovimientoDtoMapper mapper,
      InventarioMovimientoPolicy movimientoPolicy,
      OperacionAutorizacionService autorizacionService,
      AuthenticatedUserService authenticatedUserService,
      AuditTrailService auditTrailService
  ) {
    this.movimientoRepository = movimientoRepository;
    this.ingredienteRepository = ingredienteRepository;
    this.insumoRepository = insumoRepository;
    this.mapper = mapper;
    this.movimientoPolicy = movimientoPolicy;
    this.autorizacionService = autorizacionService;
    this.authenticatedUserService = authenticatedUserService;
    this.auditTrailService = auditTrailService;
  }

  @Transactional
  public InventarioMovimientoSummary createMovimiento(
      CreateInventarioMovimientoRequest request,
      UserEntity registradoPor,
      HttpServletRequest httpRequest
  ) {
    autorizacionService.exigirAlgunoPermisoSucursal(
        SUCURSAL_TRANSICIONAL,
        List.of(Permisos.INVENTARIO_OPERAR, Permisos.COMPRAS_GESTIONAR, Permisos.PRODUCCION_OPERAR)
    );

    ItemTipo itemTipo = movimientoPolicy.normalizarItemTipo(request.itemTipo());
    TipoMovimiento tipoMovimiento = movimientoPolicy.normalizarTipoMovimiento(request.tipoMovimiento());
    BigDecimal cantidad = movimientoPolicy.normalizarCantidad(request.cantidad());
    movimientoPolicy.validarJustificacion(tipoMovimiento, request.motivoSalida(), request.observaciones());

    UserEntity actor = registradoPor != null ? registradoPor : authenticatedUserService.currentUser().orElse(null);

    return switch (itemTipo) {
      case INGREDIENTE -> registrarMovimientoIngrediente(request, tipoMovimiento, cantidad, actor, httpRequest);
      case INSUMO -> registrarMovimientoInsumo(request, tipoMovimiento, cantidad, actor, httpRequest);
    };
  }

  private InventarioMovimientoSummary registrarMovimientoIngrediente(
      CreateInventarioMovimientoRequest request,
      TipoMovimiento tipoMovimiento,
      BigDecimal cantidad,
      UserEntity actor,
      HttpServletRequest httpRequest
  ) {
    IngredienteEntity ingrediente = ingredienteRepository.findByIdForUpdate(request.itemId())
        .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado."));

    BigDecimal saldoAnterior = defaultZero(ingrediente.getStockActual());
    BigDecimal saldoPosterior = movimientoPolicy.calcularSaldoPosterior(saldoAnterior, tipoMovimiento, cantidad);

    ingrediente.setStockActual(saldoPosterior);
    ingrediente.setUpdatedAt(OffsetDateTime.now());
    ingredienteRepository.save(ingrediente);

    InventarioMovimientoEntity saved = movimientoRepository.save(buildMovimiento(
        request,
        ItemTipo.INGREDIENTE,
        tipoMovimiento,
        cantidad,
        saldoAnterior,
        saldoPosterior,
        actor
    ));

    InventarioMovimientoSummary summary = mapper.toSummary(saved, ingrediente.getName());
    audit(saved, summary, null, "Movimiento de inventario registrado para ingrediente.", httpRequest);
    return summary;
  }

  private InventarioMovimientoSummary registrarMovimientoInsumo(
      CreateInventarioMovimientoRequest request,
      TipoMovimiento tipoMovimiento,
      BigDecimal cantidad,
      UserEntity actor,
      HttpServletRequest httpRequest
  ) {
    InsumoEntity insumo = insumoRepository.findByIdForUpdate(request.itemId())
        .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado."));

    BigDecimal saldoAnterior = defaultZero(insumo.getStockActual());
    BigDecimal saldoPosterior = movimientoPolicy.calcularSaldoPosterior(saldoAnterior, tipoMovimiento, cantidad);

    insumo.setStockActual(saldoPosterior);
    insumo.setUpdatedAt(OffsetDateTime.now());
    insumoRepository.save(insumo);

    InventarioMovimientoEntity saved = movimientoRepository.save(buildMovimiento(
        request,
        ItemTipo.INSUMO,
        tipoMovimiento,
        cantidad,
        saldoAnterior,
        saldoPosterior,
        actor
    ));

    InventarioMovimientoSummary summary = mapper.toSummary(saved, insumo.getName());
    audit(saved, summary, null, "Movimiento de inventario registrado para insumo.", httpRequest);
    return summary;
  }

  private InventarioMovimientoEntity buildMovimiento(
      CreateInventarioMovimientoRequest request,
      ItemTipo itemTipo,
      TipoMovimiento tipoMovimiento,
      BigDecimal cantidad,
      BigDecimal saldoAnterior,
      BigDecimal saldoPosterior,
      UserEntity registradoPor
  ) {
    InventarioMovimientoEntity movimiento = new InventarioMovimientoEntity();
    movimiento.setItemTipo(itemTipo.name());
    movimiento.setItemId(request.itemId());
    movimiento.setTipoMovimiento(tipoMovimiento.name());
    movimiento.setCantidad(cantidad);
    movimiento.setSaldoAnterior(saldoAnterior);
    movimiento.setSaldoPosterior(saldoPosterior);
    movimiento.setReferenciaTipo(movimientoPolicy.referenciaTipoOrDefault(request.referenciaTipo(), tipoMovimiento));
    movimiento.setReferenciaId(movimientoPolicy.referenciaIdOrDefault(request.referenciaId(), tipoMovimiento));
    movimiento.setMotivoSalida(blankToNull(request.motivoSalida()));
    movimiento.setObservaciones(blankToNull(request.observaciones()));
    movimiento.setFechaMovimiento(OffsetDateTime.now());
    movimiento.setRegistradoPor(registradoPor);
    return movimiento;
  }

  private void audit(
      InventarioMovimientoEntity saved,
      InventarioMovimientoSummary current,
      Object previous,
      String description,
      HttpServletRequest httpRequest
  ) {
    auditTrailService.recordChange(
        "INVENTARIO_MOVIMIENTO_REGISTRADO",
        "ABASTECIMIENTO",
        "inventario_movimiento",
        saved.getId().toString(),
        "REGISTRAR_MOVIMIENTO",
        previous,
        current,
        description,
        httpRequest
    );
  }

  private BigDecimal defaultZero(BigDecimal value) {
    return value == null ? BigDecimal.ZERO : value;
  }

  private String blankToNull(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return value.trim();
  }
}
