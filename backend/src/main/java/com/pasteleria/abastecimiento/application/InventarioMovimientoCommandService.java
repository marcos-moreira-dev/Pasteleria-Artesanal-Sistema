package com.pasteleria.abastecimiento.application;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.pasteleria.abastecimiento.application.mapper.InventarioMovimientoDtoMapper;
import com.pasteleria.abastecimiento.application.port.IngredienteRepositoryPort;
import com.pasteleria.abastecimiento.application.port.InsumoRepositoryPort;
import com.pasteleria.abastecimiento.application.port.InventarioMovimientoRepositoryPort;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.IngredienteEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InsumoEntity;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.InventarioMovimientoEntity;
import com.pasteleria.common.audit.AuditTrailService;
import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.usuarios.infrastructure.persistence.entity.UserEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class InventarioMovimientoCommandService {

  private static final String TIPO_INGREDIENTE = "INGREDIENTE";

  private final InventarioMovimientoRepositoryPort movimientoRepository;
  private final IngredienteRepositoryPort ingredienteRepository;
  private final InsumoRepositoryPort insumoRepository;
  private final InventarioMovimientoDtoMapper mapper;
  private final AuditTrailService auditTrailService;

  public InventarioMovimientoCommandService(
      InventarioMovimientoRepositoryPort movimientoRepository,
      IngredienteRepositoryPort ingredienteRepository,
      InsumoRepositoryPort insumoRepository,
      InventarioMovimientoDtoMapper mapper,
      AuditTrailService auditTrailService
  ) {
    this.movimientoRepository = movimientoRepository;
    this.ingredienteRepository = ingredienteRepository;
    this.insumoRepository = insumoRepository;
    this.mapper = mapper;
    this.auditTrailService = auditTrailService;
  }

  @Transactional
  public InventarioMovimientoSummary createMovimiento(
      CreateInventarioMovimientoRequest request,
      UserEntity registradoPor,
      HttpServletRequest httpRequest
  ) {
    OffsetDateTime now = OffsetDateTime.now();
    BigDecimal cantidad = request.cantidad();
    BigDecimal saldoPosterior;

    if (TIPO_INGREDIENTE.equals(request.itemTipo())) {
      IngredienteEntity ingrediente = ingredienteRepository.findById(request.itemId())
          .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado."));

      saldoPosterior = processStock(ingrediente, request.tipoMovimiento(), cantidad, ingredienteRepository);

      InventarioMovimientoEntity movimiento = buildMovimiento(request, saldoPosterior, now, registradoPor);
      InventarioMovimientoEntity saved = movimientoRepository.save(movimiento);

      auditTrailService.recordChange(
          "INVENTARIO_MOVIMIENTO_REGISTRADO",
          "ABASTECIMIENTO",
          "inventario_movimiento",
          saved.getId().toString(),
          "REGISTRAR_MOVIMIENTO",
          null,
          mapper.toSummary(saved, ingrediente.getName()),
          "Movimiento de inventario registrado para ingrediente.",
          httpRequest
      );

      return mapper.toSummary(saved, ingrediente.getName());
    } else {
      InsumoEntity insumo = insumoRepository.findById(request.itemId())
          .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado."));

      saldoPosterior = processStock(insumo, request.tipoMovimiento(), cantidad, insumoRepository);

      InventarioMovimientoEntity movimiento = buildMovimiento(request, saldoPosterior, now, registradoPor);
      InventarioMovimientoEntity saved = movimientoRepository.save(movimiento);

      auditTrailService.recordChange(
          "INVENTARIO_MOVIMIENTO_REGISTRADO",
          "ABASTECIMIENTO",
          "inventario_movimiento",
          saved.getId().toString(),
          "REGISTRAR_MOVIMIENTO",
          null,
          mapper.toSummary(saved, insumo.getName()),
          "Movimiento de inventario registrado para insumo.",
          httpRequest
      );

      return mapper.toSummary(saved, insumo.getName());
    }
  }

  private <T> BigDecimal processStock(T item, String tipoMovimiento, BigDecimal cantidad, Object repository) {
    BigDecimal stockActual;
    if (item instanceof IngredienteEntity ingrediente) {
      stockActual = ingrediente.getStockActual();
    } else if (item instanceof InsumoEntity insumo) {
      stockActual = insumo.getStockActual();
    } else {
      throw new IllegalArgumentException("Tipo de item no soportado");
    }

    BigDecimal saldoPosterior;
    boolean isEntrada = tipoMovimiento.startsWith("ENTRADA_");

    if (isEntrada) {
      saldoPosterior = stockActual.add(cantidad);
    } else {
      saldoPosterior = stockActual.subtract(cantidad);
      if (saldoPosterior.compareTo(BigDecimal.ZERO) < 0) {
        throw new BusinessRuleException("El stock no puede quedar negativo. Stock actual: " + stockActual + ", cantidad solicitada: " + cantidad);
      }
    }

    if (item instanceof IngredienteEntity ingrediente) {
      ingrediente.setStockActual(saldoPosterior);
      ingrediente.setUpdatedAt(OffsetDateTime.now());
      ((IngredienteRepositoryPort) repository).save(ingrediente);
    } else if (item instanceof InsumoEntity insumo) {
      insumo.setStockActual(saldoPosterior);
      insumo.setUpdatedAt(OffsetDateTime.now());
      ((InsumoRepositoryPort) repository).save(insumo);
    }

    return saldoPosterior;
  }

  private InventarioMovimientoEntity buildMovimiento(
      CreateInventarioMovimientoRequest request,
      BigDecimal saldoPosterior,
      OffsetDateTime now,
      UserEntity registradoPor
  ) {
    InventarioMovimientoEntity movimiento = new InventarioMovimientoEntity();
    movimiento.setItemTipo(request.itemTipo());
    movimiento.setItemId(request.itemId());
    movimiento.setTipoMovimiento(request.tipoMovimiento());
    movimiento.setCantidad(request.cantidad());
    movimiento.setSaldoPosterior(saldoPosterior);
    movimiento.setReferenciaTipo(request.referenciaTipo());
    movimiento.setReferenciaId(request.referenciaId());
    movimiento.setMotivoSalida(request.motivoSalida());
    movimiento.setObservaciones(request.observaciones());
    movimiento.setFechaMovimiento(now);
    movimiento.setRegistradoPor(registradoPor);
    return movimiento;
  }
}
