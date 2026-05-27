package com.pasteleria.erpbridge.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.contabilidad.application.AsientoContableDetalleRequest;
import com.pasteleria.contabilidad.application.AsientoContableSummary;
import com.pasteleria.contabilidad.application.ContabilidadCommandService;
import com.pasteleria.contabilidad.application.ContabilidadMapper;
import com.pasteleria.contabilidad.application.RegistrarAsientoContableRequest;
import com.pasteleria.contabilidad.infrastructure.persistence.repository.AsientoContableDetalleRepository;
import com.pasteleria.contabilidad.infrastructure.persistence.repository.AsientoContableRepository;
import com.pasteleria.erp.application.ErpFinancialPolicy;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fachada interna para crear asientos contables desde bridges operativos.
 *
 * <p>No conoce pedidos, compras ni caja. Solo resuelve idempotencia contable
 * por origen y delega la validación de partida doble al módulo de contabilidad.</p>
 */
@Service
public class ContabilidadBridgeService {

  private final AsientoContableRepository asientoRepository;
  private final AsientoContableDetalleRepository detalleRepository;
  private final ContabilidadCommandService contabilidadCommandService;
  private final ContabilidadMapper mapper;

  public ContabilidadBridgeService(
      AsientoContableRepository asientoRepository,
      AsientoContableDetalleRepository detalleRepository,
      ContabilidadCommandService contabilidadCommandService,
      ContabilidadMapper mapper
  ) {
    this.asientoRepository = asientoRepository;
    this.detalleRepository = detalleRepository;
    this.contabilidadCommandService = contabilidadCommandService;
    this.mapper = mapper;
  }

  @Transactional
  public ErpBridgeOperationResult registrarAsientoSiNoExiste(
      String operacion,
      String origenTipo,
      String origenId,
      String tipoDiarioCodigo,
      String codigo,
      LocalDateTime fechaAsiento,
      String descripcion,
      List<AsientoContableDetalleRequest> lineas,
      HttpServletRequest request
  ) {
    String origenTipoNormalizado = requerido(origenTipo, "El tipo de origen contable es obligatorio.");
    String origenIdNormalizado = requerido(origenId, "El identificador de origen contable es obligatorio.");

    return asientoRepository.findByOrigenTipoAndOrigenId(origenTipoNormalizado, origenIdNormalizado)
        .map(existing -> {
          AsientoContableSummary summary = mapper.toAsiento(
              existing,
              detalleRepository.findByAsientoIdOrderByIdAsc(existing.getId())
          );
          return new ErpBridgeOperationResult(
              operacion,
              origenTipoNormalizado,
              origenIdNormalizado,
              "ASIENTO_CONTABLE",
              summary.id(),
              summary.codigo(),
              false,
              "El asiento contable ya existia para este origen; no se duplico."
          );
        })
        .orElseGet(() -> {
          AsientoContableSummary summary = contabilidadCommandService.registrarAsiento(
              new RegistrarAsientoContableRequest(
                  tipoDiarioCodigo,
                  codigo,
                  fechaAsiento,
                  descripcion,
                  origenTipoNormalizado,
                  origenIdNormalizado,
                  lineas
              ),
              request
          );
          return new ErpBridgeOperationResult(
              operacion,
              origenTipoNormalizado,
              origenIdNormalizado,
              "ASIENTO_CONTABLE",
              summary.id(),
              summary.codigo(),
              true,
              "Asiento contable creado desde bridge ERP."
          );
        });
  }

  public AsientoContableDetalleRequest debe(String cuentaCodigo, String descripcion, BigDecimal monto) {
    return new AsientoContableDetalleRequest(cuentaCodigo, descripcion, ErpFinancialPolicy.money(monto), BigDecimal.ZERO);
  }

  public AsientoContableDetalleRequest haber(String cuentaCodigo, String descripcion, BigDecimal monto) {
    return new AsientoContableDetalleRequest(cuentaCodigo, descripcion, BigDecimal.ZERO, ErpFinancialPolicy.money(monto));
  }

  private String requerido(String value, String message) {
    if (value == null || value.isBlank()) {
      throw new BusinessRuleException(message);
    }
    return value.trim().toUpperCase();
  }
}
