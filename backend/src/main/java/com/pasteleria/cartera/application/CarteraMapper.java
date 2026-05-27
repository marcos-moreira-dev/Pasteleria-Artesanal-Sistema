package com.pasteleria.cartera.application;

import java.util.List;

import com.pasteleria.cartera.infrastructure.persistence.entity.CobranzaDetalleEntity;
import com.pasteleria.cartera.infrastructure.persistence.entity.CobranzaEntity;
import com.pasteleria.cartera.infrastructure.persistence.entity.DocumentoCobrarEntity;

import org.springframework.stereotype.Component;

@Component
public class CarteraMapper {

  public DocumentoCobrarSummary toDocumento(DocumentoCobrarEntity entity) {
    Long pedidoId = entity.getPedido() == null ? null : entity.getPedido().getId();
    String pedidoCodigo = entity.getPedido() == null ? null : entity.getPedido().getCode();
    return new DocumentoCobrarSummary(
        entity.getId(),
        entity.getCliente().getId(),
        entity.getCliente().getFullName(),
        pedidoId,
        pedidoCodigo,
        entity.getCodigo(),
        entity.getEstado(),
        entity.getFechaEmision(),
        entity.getFechaVencimiento(),
        entity.getTotal(),
        entity.getSaldo(),
        entity.getObservaciones()
    );
  }

  public CobranzaSummary toCobranza(CobranzaEntity entity, List<CobranzaDetalleEntity> detalles) {
    return new CobranzaSummary(
        entity.getId(),
        entity.getCliente().getId(),
        entity.getCliente().getFullName(),
        entity.getCodigo(),
        entity.getFechaCobranza(),
        entity.getMontoTotal(),
        entity.getMedioPago(),
        entity.getReferenciaPago(),
        entity.getEstado(),
        entity.getObservaciones(),
        detalles.stream().map(this::toDetalle).toList()
    );
  }

  public CobranzaDetalleSummary toDetalle(CobranzaDetalleEntity entity) {
    return new CobranzaDetalleSummary(
        entity.getId(),
        entity.getDocumentoCobrar().getId(),
        entity.getDocumentoCobrar().getCodigo(),
        entity.getMontoAplicado(),
        entity.getSaldoAnterior(),
        entity.getSaldoPosterior()
    );
  }
}
