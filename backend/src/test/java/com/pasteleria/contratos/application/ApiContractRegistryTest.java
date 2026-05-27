package com.pasteleria.contratos.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.pasteleria.common.security.Permisos;

import org.junit.jupiter.api.Test;

class ApiContractRegistryTest {

  private final ApiContractRegistry registry = new ApiContractRegistry();

  @Test
  void shouldExposeMainContractSections() {
    ApiContractSnapshot snapshot = registry.snapshot();

    assertThat(snapshot.endpoints()).isNotEmpty();
    assertThat(snapshot.permissions()).isNotEmpty();
    assertThat(snapshot.enums()).isNotEmpty();
    assertThat(snapshot.pagination().responseType()).isEqualTo("PageResponseDto<T>");
  }

  @Test
  void shouldRegisterMainApiContractEndpoints() {
    assertThat(registry.endpoints())
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/contratos"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/auth/me"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/clientes/paginado") && endpoint.paginated())
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/terceros"))
        .anyMatch(endpoint -> endpoint.method().equals("POST") && endpoint.path().equals("/api/v1/pedidos"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/casos-uso/hub"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/casos-uso/manual.pdf"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/caja/estado"))
        .anyMatch(endpoint -> endpoint.method().equals("POST") && endpoint.path().equals("/api/v1/caja/abrir"))
        .anyMatch(endpoint -> endpoint.method().equals("POST") && endpoint.path().equals("/api/v1/caja/cerrar"))
        .anyMatch(endpoint -> endpoint.method().equals("POST") && endpoint.path().equals("/api/v1/abastecimiento/ordenes-compra/{id}/documento-compra"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/abastecimiento/cuentas-pagar"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/cartera/documentos-cobrar"))
        .anyMatch(endpoint -> endpoint.method().equals("POST") && endpoint.path().equals("/api/v1/cartera/cobranzas"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/cuentas-pagar/documentos"))
        .anyMatch(endpoint -> endpoint.method().equals("POST") && endpoint.path().equals("/api/v1/cuentas-pagar/pagos"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/contabilidad/cuentas"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/contabilidad/asientos"))
        .anyMatch(endpoint -> endpoint.method().equals("POST") && endpoint.path().equals("/api/v1/contabilidad/asientos"))
        .anyMatch(endpoint -> endpoint.method().equals("POST") && endpoint.path().equals("/api/v1/erp-bridges/pedidos/{pedidoId}/documento-cobrar"))
        .anyMatch(endpoint -> endpoint.method().equals("POST") && endpoint.path().equals("/api/v1/erp-bridges/documentos-cobrar/{documentoCobrarId}/asiento-venta"))
        .anyMatch(endpoint -> endpoint.method().equals("POST") && endpoint.path().equals("/api/v1/erp-bridges/cobranzas/{cobranzaId}/asiento-cobro"))
        .anyMatch(endpoint -> endpoint.method().equals("POST") && endpoint.path().equals("/api/v1/erp-bridges/documentos-pagar/{documentoPagarId}/asiento-compra"))
        .anyMatch(endpoint -> endpoint.method().equals("POST") && endpoint.path().equals("/api/v1/erp-bridges/pagos-proveedor/{pagoProveedorId}/asiento-pago"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/inteligencia/dashboard"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/inteligencia/cartera"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/inteligencia/contabilidad"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/fiscal/documentos"))
        .anyMatch(endpoint -> endpoint.method().equals("POST") && endpoint.path().equals("/api/v1/fiscal/documentos"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/auditoria/resumen"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/auditoria/eventos"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/soporte/evidencia"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/soporte/checklist"))
        .anyMatch(endpoint -> endpoint.method().equals("POST") && endpoint.path().equals("/api/v1/fiscal/documentos/{id}/emitir-interno"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/produccion/{productionId}/consumos"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/produccion/{productionId}/lotes"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/archivos/{archivoId}/descargar"))
        .anyMatch(endpoint -> endpoint.method().equals("GET") && endpoint.path().equals("/api/v1/assets/{type}/{filename}") && endpoint.publicEndpoint());
  }

  @Test
  void shouldExposeCentralPermissionCatalog() {
    assertThat(registry.permissions())
        .anyMatch(permission -> permission.code().equals(Permisos.CLIENTES_VER))
        .anyMatch(permission -> permission.code().equals(Permisos.TERCEROS_VER))
        .anyMatch(permission -> permission.code().equals(Permisos.PRODUCTOS_GESTIONAR))
        .anyMatch(permission -> permission.code().equals(Permisos.GUIA_OPERATIVA_VER))
        .anyMatch(permission -> permission.code().equals(Permisos.CAJA_VER))
        .anyMatch(permission -> permission.code().equals(Permisos.CAJA_OPERAR))
        .anyMatch(permission -> permission.code().equals(Permisos.CARTERA_VER))
        .anyMatch(permission -> permission.code().equals(Permisos.COBRANZAS_REGISTRAR))
        .anyMatch(permission -> permission.code().equals(Permisos.CUENTAS_PAGAR_VER))
        .anyMatch(permission -> permission.code().equals(Permisos.PAGOS_PROVEEDOR_REGISTRAR))
        .anyMatch(permission -> permission.code().equals(Permisos.CONTABILIDAD_VER))
        .anyMatch(permission -> permission.code().equals(Permisos.ASIENTOS_REGISTRAR))
        .anyMatch(permission -> permission.code().equals(Permisos.FISCAL_VER))
        .anyMatch(permission -> permission.code().equals(Permisos.DOCUMENTOS_FISCALES_EMITIR))
        .anyMatch(permission -> permission.code().equals(Permisos.AUDITORIA_VER))
        .anyMatch(permission -> permission.code().equals(Permisos.SOPORTE_VER))
        .anyMatch(permission -> permission.code().equals(Permisos.SOPORTE_GESTIONAR))
        .anyMatch(permission -> permission.code().equals(Permisos.CONTRATOS_API_VER))
        .anyMatch(permission -> permission.code().equals(Permisos.ARCHIVOS_DESCARGAR));
  }
}
