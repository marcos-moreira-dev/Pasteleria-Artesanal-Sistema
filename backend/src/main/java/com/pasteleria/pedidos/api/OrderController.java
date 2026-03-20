package com.pasteleria.pedidos.api;

import java.util.List;

import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.util.ResponseFactory;
import com.pasteleria.common.pagination.PageResponseDto;
import com.pasteleria.pedidos.application.CreateOrderRequest;
import com.pasteleria.pedidos.application.OrderCommandService;
import com.pasteleria.pedidos.application.OrderQueryService;
import com.pasteleria.pedidos.application.OrderSummary;
import com.pasteleria.pedidos.application.UpdateOrderStatusRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador administrativo del módulo de pedidos.
 */
@Validated
@Tag(name = "Pedidos", description = "Operaciones comerciales y de seguimiento del pedido.")
@RestController
@RequestMapping("/api/v1/pedidos")
public class OrderController {

  private final OrderQueryService orderQueryService;
  private final OrderCommandService orderCommandService;

  public OrderController(
      OrderQueryService orderQueryService,
      OrderCommandService orderCommandService
  ) {
    this.orderQueryService = orderQueryService;
    this.orderCommandService = orderCommandService;
  }

  @Operation(summary = "Listar pedidos registrados.")
  @GetMapping
  public ResponseEntity<ApiResponse<List<OrderSummary>>> listOrders(HttpServletRequest request) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Pedidos obtenidos correctamente.",
        orderQueryService.listOrders(),
        request
    ));
  }

  @Operation(summary = "Listar pedidos en formato paginado para el tablero administrativo.")
  @GetMapping("/paginado")
  public ResponseEntity<ApiResponse<PageResponseDto<OrderSummary>>> listOrdersPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "8") int size,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Pagina de pedidos obtenida correctamente.",
        orderQueryService.listOrdersPage(page, size),
        request
    ));
  }

  @Operation(summary = "Registrar pedido.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ApiResponse<OrderSummary>> createOrder(
      @Valid @RequestBody CreateOrderRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.created(
        "Pedido registrado correctamente.",
        orderCommandService.createOrder(body, request),
        request
    ));
  }

  @Operation(summary = "Actualizar estado operativo del pedido.")
  @PatchMapping("/{orderId}/estado")
  public ResponseEntity<ApiResponse<OrderSummary>> updateOrderStatus(
      @PathVariable Long orderId,
      @Valid @RequestBody UpdateOrderStatusRequest body,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(ResponseFactory.ok(
        "Estado del pedido actualizado correctamente.",
        orderCommandService.updateOrderStatus(orderId, body, request),
        request
    ));
  }

  @Operation(summary = "Eliminar pedido previamente cancelado.")
  @DeleteMapping("/{orderId}")
  public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long orderId, HttpServletRequest request) {
    orderCommandService.deleteOrder(orderId, request);
    return ResponseEntity.ok(ResponseFactory.ok(
        "Pedido eliminado correctamente.",
        null,
        request
    ));
  }
}


