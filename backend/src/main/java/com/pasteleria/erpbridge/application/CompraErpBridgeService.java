package com.pasteleria.erpbridge.application;

import java.util.List;

import com.pasteleria.abastecimiento.infrastructure.persistence.repository.DocumentoPagarRepository;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.contabilidad.application.AsientoContableDetalleRequest;
import com.pasteleria.erp.application.ErpFinancialPolicy;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Bridge de compras/cuentas por pagar hacia contabilidad interna. */
@Service
public class CompraErpBridgeService {

  private final DocumentoPagarRepository documentoPagarRepository;
  private final ContabilidadBridgeService contabilidadBridgeService;

  public CompraErpBridgeService(DocumentoPagarRepository documentoPagarRepository, ContabilidadBridgeService contabilidadBridgeService) {
    this.documentoPagarRepository = documentoPagarRepository;
    this.contabilidadBridgeService = contabilidadBridgeService;
  }

  @Transactional
  public ErpBridgeOperationResult registrarAsientoCompraDesdeDocumentoPagar(Long documentoPagarId, HttpServletRequest request) {
    var documento = documentoPagarRepository.findById(documentoPagarId)
        .orElseThrow(() -> new ResourceNotFoundException("Documento por pagar no encontrado."));
    var total = ErpFinancialPolicy.money(documento.getTotal());
    ErpFinancialPolicy.exigirMontoPositivo(total, "El documento por pagar debe tener total mayor a cero.");

    List<AsientoContableDetalleRequest> lineas = List.of(
        contabilidadBridgeService.debe("5.1.01", "Costo/insumos de produccion - " + documento.getCodigo(), total),
        contabilidadBridgeService.haber("2.1.01", "Cuenta por pagar proveedor - " + documento.getCodigo(), total)
    );

    return contabilidadBridgeService.registrarAsientoSiNoExiste(
        "REGISTRAR_ASIENTO_COMPRA_DESDE_DOCUMENTO_PAGAR",
        "DOCUMENTO_PAGAR",
        documento.getDocumentoPagarId().toString(),
        "COMPRAS",
        "ASI-CMP-" + documento.getCodigo(),
        documento.getFechaEmision(),
        "Asiento interno de compra generado desde documento por pagar " + documento.getCodigo() + ".",
        lineas,
        request
    );
  }
}
