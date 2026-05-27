package com.pasteleria.erpbridge.application;

import java.util.List;

import com.pasteleria.cartera.application.CarteraCommandService;
import com.pasteleria.cartera.application.CarteraMapper;
import com.pasteleria.cartera.application.CrearDocumentoCobrarRequest;
import com.pasteleria.cartera.infrastructure.persistence.repository.DocumentoCobrarRepository;
import com.pasteleria.common.error.BusinessRuleException;
import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.contabilidad.application.AsientoContableDetalleRequest;
import com.pasteleria.erp.application.ErpFinancialPolicy;
import com.pasteleria.pedidos.application.port.OrderRepositoryPort;
import com.pasteleria.pedidos.infrastructure.persistence.entity.OrderEntity;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Bridge de ventas/pedidos hacia consecuencias ERP.
 *
 * <p>Se mantiene separado de cartera y contabilidad para evitar que pedidos
 * termine con responsabilidades financieras directas.</p>
 */
@Service
public class VentaErpBridgeService {

  private static final String ORIGEN_DOCUMENTO_COBRAR = "DOCUMENTO_COBRAR";

  private final OrderRepositoryPort orderRepository;
  private final DocumentoCobrarRepository documentoCobrarRepository;
  private final CarteraCommandService carteraCommandService;
  private final CarteraMapper carteraMapper;
  private final ContabilidadBridgeService contabilidadBridgeService;

  public VentaErpBridgeService(
      OrderRepositoryPort orderRepository,
      DocumentoCobrarRepository documentoCobrarRepository,
      CarteraCommandService carteraCommandService,
      CarteraMapper carteraMapper,
      ContabilidadBridgeService contabilidadBridgeService
  ) {
    this.orderRepository = orderRepository;
    this.documentoCobrarRepository = documentoCobrarRepository;
    this.carteraCommandService = carteraCommandService;
    this.carteraMapper = carteraMapper;
    this.contabilidadBridgeService = contabilidadBridgeService;
  }

  @Transactional
  public ErpBridgeOperationResult generarDocumentoCobrarDesdePedido(Long pedidoId, HttpServletRequest request) {
    OrderEntity pedido = orderRepository.findById(pedidoId)
        .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado."));
    return documentoCobrarRepository.findByPedidoId(pedido.getId())
        .map(existing -> {
          var summary = carteraMapper.toDocumento(existing);
          return new ErpBridgeOperationResult(
              "GENERAR_DOCUMENTO_COBRAR_DESDE_PEDIDO",
              "PEDIDO",
              pedido.getId().toString(),
              "DOCUMENTO_COBRAR",
              summary.id(),
              summary.codigo(),
              false,
              "El pedido ya tenia documento por cobrar; no se duplico."
          );
        })
        .orElseGet(() -> {
          var summary = carteraCommandService.crearDocumento(
              new CrearDocumentoCobrarRequest(
                  pedido.getId(),
                  null,
                  "CXC-" + pedido.getCode(),
                  null,
                  null,
                  pedido.getEstimatedTotal(),
                  "Documento por cobrar generado por bridge ERP desde pedido " + pedido.getCode() + "."
              ),
              request
          );
          return new ErpBridgeOperationResult(
              "GENERAR_DOCUMENTO_COBRAR_DESDE_PEDIDO",
              "PEDIDO",
              pedido.getId().toString(),
              "DOCUMENTO_COBRAR",
              summary.id(),
              summary.codigo(),
              true,
              "Documento por cobrar creado desde pedido."
          );
        });
  }

  @Transactional
  public ErpBridgeOperationResult registrarAsientoVentaDesdeDocumentoCobrar(Long documentoCobrarId, HttpServletRequest request) {
    var documento = documentoCobrarRepository.findById(documentoCobrarId)
        .orElseThrow(() -> new ResourceNotFoundException("Documento por cobrar no encontrado."));
    var total = ErpFinancialPolicy.money(documento.getTotal());
    ErpFinancialPolicy.exigirMontoPositivo(total, "El documento por cobrar debe tener total mayor a cero.");

    List<AsientoContableDetalleRequest> lineas = List.of(
        contabilidadBridgeService.debe("1.2.01", "Cuenta por cobrar cliente - " + documento.getCodigo(), total),
        contabilidadBridgeService.haber("4.1.01", "Venta de pasteleria - " + documento.getCodigo(), total)
    );

    return contabilidadBridgeService.registrarAsientoSiNoExiste(
        "REGISTRAR_ASIENTO_VENTA_DESDE_DOCUMENTO_COBRAR",
        ORIGEN_DOCUMENTO_COBRAR,
        documento.getId().toString(),
        "VENTAS",
        "ASI-VTA-" + documento.getCodigo(),
        documento.getFechaEmision(),
        "Asiento interno de venta generado desde documento por cobrar " + documento.getCodigo() + ".",
        lineas,
        request
    );
  }
}
