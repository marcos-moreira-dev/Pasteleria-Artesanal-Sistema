package com.pasteleria.erpbridge.application;

import java.util.List;

import com.pasteleria.cartera.infrastructure.persistence.repository.CobranzaRepository;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.contabilidad.application.AsientoContableDetalleRequest;
import com.pasteleria.erp.application.ErpFinancialPolicy;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Bridge de caja/cobranzas hacia contabilidad interna. */
@Service
public class CajaErpBridgeService {

  private final CobranzaRepository cobranzaRepository;
  private final ContabilidadBridgeService contabilidadBridgeService;

  public CajaErpBridgeService(CobranzaRepository cobranzaRepository, ContabilidadBridgeService contabilidadBridgeService) {
    this.cobranzaRepository = cobranzaRepository;
    this.contabilidadBridgeService = contabilidadBridgeService;
  }

  @Transactional
  public ErpBridgeOperationResult registrarAsientoCobroDesdeCobranza(Long cobranzaId, HttpServletRequest request) {
    var cobranza = cobranzaRepository.findById(cobranzaId)
        .orElseThrow(() -> new ResourceNotFoundException("Cobranza no encontrada."));
    var total = ErpFinancialPolicy.money(cobranza.getMontoTotal());
    ErpFinancialPolicy.exigirMontoPositivo(total, "La cobranza debe tener monto mayor a cero.");

    List<AsientoContableDetalleRequest> lineas = List.of(
        contabilidadBridgeService.debe("1.1.01", "Ingreso de caja por cobranza - " + cobranza.getCodigo(), total),
        contabilidadBridgeService.haber("1.2.01", "Disminucion de cuentas por cobrar - " + cobranza.getCodigo(), total)
    );

    return contabilidadBridgeService.registrarAsientoSiNoExiste(
        "REGISTRAR_ASIENTO_COBRO_DESDE_COBRANZA",
        "COBRANZA",
        cobranza.getId().toString(),
        "CAJA",
        "ASI-COB-" + cobranza.getCodigo(),
        cobranza.getFechaCobranza(),
        "Asiento interno de cobro generado desde cobranza " + cobranza.getCodigo() + ".",
        lineas,
        request
    );
  }
}
