package com.pasteleria.contratos.api;

import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.contratos.application.ApiContractRegistry;
import com.pasteleria.contratos.application.ApiContractSnapshot;
import com.pasteleria.contratos.application.EndpointContract;
import com.pasteleria.contratos.application.EnumContract;
import com.pasteleria.contratos.application.PaginationContract;
import com.pasteleria.contratos.application.PermissionContract;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Publica contratos API para alinear backend, frontend administrativo y tests.
 */
@Tag(name = "Contratos API", description = "Registro central de endpoints, permisos, enums y paginacion.")
@RestController
@RequestMapping("/api/v1/contratos")
public class ApiContractController {

  private final ApiContractRegistry registry;

  public ApiContractController(ApiContractRegistry registry) {
    this.registry = registry;
  }

  @Operation(summary = "Obtener contrato API completo.")
  @GetMapping
  public ResponseEntity<ApiResponse<ApiContractSnapshot>> snapshot(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Contratos API obtenidos correctamente.",
        registry.snapshot(),
        request
    ));
  }

  @Operation(summary = "Listar endpoints registrados.")
  @GetMapping("/endpoints")
  public ResponseEntity<ApiResponse<List<EndpointContract>>> endpoints(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Endpoints de contrato obtenidos correctamente.",
        registry.endpoints(),
        request
    ));
  }

  @Operation(summary = "Listar permisos registrados.")
  @GetMapping("/permisos")
  public ResponseEntity<ApiResponse<List<PermissionContract>>> permissions(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Permisos de contrato obtenidos correctamente.",
        registry.permissions(),
        request
    ));
  }

  @Operation(summary = "Listar enums y catalogos tecnicos registrados.")
  @GetMapping("/enums")
  public ResponseEntity<ApiResponse<List<EnumContract>>> enums(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Enums de contrato obtenidos correctamente.",
        registry.enums(),
        request
    ));
  }

  @Operation(summary = "Obtener politica de paginacion administrativa.")
  @GetMapping("/paginacion")
  public ResponseEntity<ApiResponse<PaginationContract>> pagination(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Politica de paginacion obtenida correctamente.",
        registry.pagination(),
        request
    ));
  }
}
