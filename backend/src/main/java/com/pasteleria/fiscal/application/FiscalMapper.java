package com.pasteleria.fiscal.application;

import com.pasteleria.fiscal.infrastructure.persistence.entity.DocumentoFiscalEntity;

import org.springframework.stereotype.Component;

@Component
public class FiscalMapper {

  public DocumentoFiscalSummary toSummary(DocumentoFiscalEntity entity) {
    return new DocumentoFiscalSummary(
        entity.getId(),
        entity.getCodigo(),
        entity.getTipoComprobante(),
        entity.getEstado(),
        entity.getDocumentoCobrarId(),
        entity.getDocumentoCompraId(),
        entity.getOrigenTipo(),
        entity.getTerceroTipo(),
        entity.getTerceroId(),
        entity.getTerceroNombre(),
        entity.getFechaEmision(),
        entity.getEstablecimiento(),
        entity.getPuntoEmision(),
        entity.getSecuencial(),
        entity.getNumeroComprobante(),
        entity.getSubtotal(),
        entity.getImpuesto(),
        entity.getTotal(),
        entity.getClaveAcceso(),
        entity.getNumeroAutorizacion(),
        entity.getFechaAutorizacion(),
        entity.getAmbiente(),
        entity.getObservaciones()
    );
  }
}
