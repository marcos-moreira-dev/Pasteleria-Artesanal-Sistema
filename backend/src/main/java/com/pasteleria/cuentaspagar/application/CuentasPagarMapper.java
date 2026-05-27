package com.pasteleria.cuentaspagar.application;

import java.util.List;

import com.pasteleria.abastecimiento.application.DocumentoPagarSummary;
import com.pasteleria.abastecimiento.infrastructure.persistence.entity.DocumentoPagarEntity;
import com.pasteleria.cuentaspagar.infrastructure.persistence.entity.PagoProveedorAplicacionEntity;
import com.pasteleria.cuentaspagar.infrastructure.persistence.entity.PagoProveedorEntity;

import org.springframework.stereotype.Component;

@Component
public class CuentasPagarMapper {

  public DocumentoPagarSummary toDocumento(DocumentoPagarEntity entity) {
    return new DocumentoPagarSummary(
        entity.getDocumentoPagarId(),
        entity.getDocumentoCompra().getDocumentoCompraId(),
        entity.getProveedor().getId(),
        entity.getProveedor().getName(),
        entity.getCodigo(),
        entity.getEstado(),
        entity.getFechaEmision(),
        entity.getFechaVencimiento(),
        entity.getTotal(),
        entity.getSaldo(),
        entity.getObservaciones(),
        entity.getCreatedAt()
    );
  }

  public PagoProveedorSummary toPago(PagoProveedorEntity entity, List<PagoProveedorAplicacionEntity> aplicaciones) {
    return new PagoProveedorSummary(
        entity.getId(),
        entity.getProveedor().getId(),
        entity.getProveedor().getName(),
        entity.getCodigo(),
        entity.getFechaPago(),
        entity.getMontoTotal(),
        entity.getMedioPago(),
        entity.getReferenciaPago(),
        entity.getEstado(),
        entity.getObservaciones(),
        aplicaciones.stream().map(this::toAplicacion).toList()
    );
  }

  public PagoProveedorAplicacionSummary toAplicacion(PagoProveedorAplicacionEntity entity) {
    return new PagoProveedorAplicacionSummary(
        entity.getId(),
        entity.getDocumentoPagar().getDocumentoPagarId(),
        entity.getDocumentoPagar().getCodigo(),
        entity.getMontoAplicado(),
        entity.getSaldoAnterior(),
        entity.getSaldoPosterior()
    );
  }
}
