package com.pasteleria.fiscal.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Solicitud para preparar un documento fiscal interno.
 *
 * <p>Debe venir desde exactamente un origen: documento por cobrar o documento de
 * compra. Si no se envían subtotales para un documento por cobrar, el sistema
 * asume una descomposición prudente subtotal=total, impuesto=0. Para documentos
 * de compra se toman los totales del documento registrado.</p>
 */
public record RegistrarDocumentoFiscalRequest(
    Long documentoCobrarId,
    Long documentoCompraId,
    TipoComprobanteFiscal tipoComprobante,
    LocalDateTime fechaEmision,
    @Pattern(regexp = "\\d{3}", message = "El establecimiento debe tener 3 digitos.")
    String establecimiento,
    @Pattern(regexp = "\\d{3}", message = "El punto de emision debe tener 3 digitos.")
    String puntoEmision,
    @Pattern(regexp = "\\d{1,9}", message = "El secuencial debe tener hasta 9 digitos.")
    String secuencial,
    BigDecimal subtotal,
    BigDecimal impuesto,
    BigDecimal total,
    @Size(max = 500)
    String observaciones
) {
}
