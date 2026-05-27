package com.pasteleria.abastecimiento.application;

public record CompraFinancieraSummary(
    DocumentoCompraSummary documentoCompra,
    DocumentoPagarSummary documentoPagar
) {
}
