package com.pasteleria.cotizaciones.api;

import java.util.List;

import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.RequestIdSupport;
import com.pasteleria.common.pagination.PageResponseDto;
import com.pasteleria.cotizaciones.application.CreateQuotationRequest;
import com.pasteleria.cotizaciones.application.QuotationCommandService;
import com.pasteleria.cotizaciones.application.QuotationQueryService;
import com.pasteleria.cotizaciones.application.QuotationSummary;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cotizaciones")
public class QuotationController {

  private final QuotationQueryService quotationQueryService;
  private final QuotationCommandService quotationCommandService;

  public QuotationController(
      QuotationQueryService quotationQueryService,
      QuotationCommandService quotationCommandService
  ) {
    this.quotationQueryService = quotationQueryService;
    this.quotationCommandService = quotationCommandService;
  }

  @GetMapping
  public ApiResponse<List<QuotationSummary>> listQuotations(HttpServletRequest request) {
    return ApiResponse.ok(
        "Cotizaciones obtenidas correctamente.",
        quotationQueryService.listQuotations(),
        RequestIdSupport.resolve(request)
    );
  }

  @GetMapping("/paginado")
  public ApiResponse<PageResponseDto<QuotationSummary>> pagedQuotations(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "8") @Min(1) @Max(25) int size,
      HttpServletRequest request
  ) {
    return ApiResponse.ok(
        "Cotizaciones paginadas obtenidas correctamente.",
        quotationQueryService.pagedQuotations(page, size),
        RequestIdSupport.resolve(request)
    );
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<QuotationSummary> createQuotation(
      @Valid @RequestBody CreateQuotationRequest body,
      HttpServletRequest request
  ) {
    return ApiResponse.ok(
        "Cotizacion registrada correctamente.",
        quotationCommandService.createQuotation(body, request),
        RequestIdSupport.resolve(request)
    );
  }
}


