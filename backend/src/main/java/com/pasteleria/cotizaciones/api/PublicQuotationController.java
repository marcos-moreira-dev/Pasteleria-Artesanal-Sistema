package com.pasteleria.cotizaciones.api;

import com.pasteleria.common.api.ApiResponse;
import com.pasteleria.common.api.RequestIdSupport;
import com.pasteleria.cotizaciones.application.PublicQuotationRequest;
import com.pasteleria.cotizaciones.application.QuotationCommandService;
import com.pasteleria.cotizaciones.application.QuotationSummary;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/cotizaciones")
public class PublicQuotationController {

  private final QuotationCommandService quotationCommandService;

  public PublicQuotationController(QuotationCommandService quotationCommandService) {
    this.quotationCommandService = quotationCommandService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<QuotationSummary> createPublicQuotation(
      @Valid @RequestBody PublicQuotationRequest body,
      HttpServletRequest request
  ) {
    return ApiResponse.ok(
        "Solicitud recibida correctamente. Pronto nos pondremos en contacto.",
        quotationCommandService.createPublicQuotation(body, request),
        RequestIdSupport.resolve(request)
    );
  }
}


