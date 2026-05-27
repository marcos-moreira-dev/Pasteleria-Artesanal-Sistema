package com.pasteleria.casosuso.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.pasteleria.casosuso.api.dto.CasoUsoHubResponse;
import com.pasteleria.casosuso.api.dto.CasoUsoModuloResponse;
import com.pasteleria.casosuso.api.dto.CasoUsoOperativoResponse;
import com.pasteleria.casosuso.api.dto.PasoCasoUsoResponse;
import java.util.List;
import org.junit.jupiter.api.Test;

class CasoUsoManualPdfDocumentServiceTest {

  private final CasoUsoManualPdfDocumentService service = new CasoUsoManualPdfDocumentService();

  @Test
  void shouldGenerateNonEmptyPdfFromOperationalGuideHub() {
    CasoUsoHubResponse hub = new CasoUsoHubResponse(
        1,
        List.of(new CasoUsoModuloResponse(
            "PEDIDOS",
            "Pedidos",
            "Flujos comerciales de pedidos pasteleros.",
            "OPERACION",
            1,
            List.of(new CasoUsoOperativoResponse(
                1L,
                "CU-PED-001",
                "PEDIDOS",
                "Registrar pedido pastelero",
                "Atencion",
                "Crear un pedido con cliente, productos y fecha esperada.",
                "Modulo Pedidos",
                1,
                "LISTO",
                1,
                true,
                List.of(
                    new PasoCasoUsoResponse(1, "Abrir el modulo Pedidos."),
                    new PasoCasoUsoResponse(2, "Registrar cliente, fecha y productos solicitados.")
                )
            ))
        ))
    );

    byte[] pdf = service.generarManual(hub);

    assertThat(pdf).isNotEmpty();
    assertThat(new String(pdf, 0, 4)).isEqualTo("%PDF");
  }
}
