package com.pasteleria.fiscal.application;

/**
 * Estados internos del documento fiscal preparado.
 *
 * <p>No representan estados oficiales del SRI. La integración real, autorización
 * y firma electrónica quedan para una fase posterior.</p>
 */
public enum EstadoDocumentoFiscal {
  BORRADOR,
  EMITIDO_INTERNO,
  ANULADO
}
