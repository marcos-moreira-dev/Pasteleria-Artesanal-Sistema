package com.pasteleria.terceros.api;

import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.common.security.OperacionAutorizacionService;
import com.pasteleria.common.security.Permisos;
import com.pasteleria.terceros.application.TerceroQueryService;
import com.pasteleria.terceros.application.TerceroSummary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Consulta transicional de terceros unificados.
 */
@Validated
@Tag(name = "Terceros", description = "Vista unificada de clientes, proveedores y futuros empleados.")
@RestController
@RequestMapping("/api/v1/terceros")
public class TerceroController {

  private final TerceroQueryService terceroQueryService;
  private final OperacionAutorizacionService autorizacionService;

  public TerceroController(TerceroQueryService terceroQueryService, OperacionAutorizacionService autorizacionService) {
    this.terceroQueryService = terceroQueryService;
    this.autorizacionService = autorizacionService;
  }

  @Operation(summary = "Listar terceros unificados.")
  @GetMapping
  public ResponseEntity<ApiResponse<List<TerceroSummary>>> list(
      @RequestParam(required = false) String perfil,
      @RequestParam(defaultValue = "") String query,
      HttpServletRequest request
  ) {
    autorizacionService.exigirAlgunoPermisoGlobal(Permisos.TERCEROS_VER, Permisos.CLIENTES_VER, Permisos.COMPRAS_VER);
    return ResponseEntity.ok(ResponseFactory.ok(
        "Terceros obtenidos correctamente.",
        terceroQueryService.list(perfil, query),
        request
    ));
  }
}
