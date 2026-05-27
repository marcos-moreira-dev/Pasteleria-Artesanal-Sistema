package com.pasteleria.abastecimiento.api;

import com.pasteleria.abastecimiento.application.RecetaPdfService;
import com.pasteleria.productos.application.ProductSummary;
import com.pasteleria.productos.application.RecetaJsonDto;
import com.pasteleria.productos.application.port.ProductRepositoryPort;
import com.pasteleria.productos.application.mapper.ProductDtoMapper;
import com.pasteleria.productos.infrastructure.persistence.entity.ProductEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@Tag(name = "Recetas PDF", description = "Exportación de recetas a PDF")
@RequestMapping("/api/v1/abastecimiento/recetas")
public class RecetaPdfController {

  private final RecetaPdfService pdfService;
  private final ProductRepositoryPort productRepository;
  private final ProductDtoMapper productDtoMapper;

  public RecetaPdfController(RecetaPdfService pdfService, ProductRepositoryPort productRepository, ProductDtoMapper productDtoMapper) {
    this.pdfService = pdfService;
    this.productRepository = productRepository;
    this.productDtoMapper = productDtoMapper;
  }

  @Operation(summary = "Descargar receta en PDF", description = "Genera y descarga la receta de un producto en formato PDF")
  @Transactional(readOnly = true)
  @GetMapping(value = "/producto/{productoId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> descargarRecetaPdf(@PathVariable Long productoId) {
    ProductEntity product = productRepository.findById(productoId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado: " + productoId));
    
    ProductSummary producto = productDtoMapper.toSummary(product);
    RecetaJsonDto receta = producto.receta();
    byte[] pdfBytes = pdfService.generarRecetaPdf(producto, receta);
    
    String filename = "Receta_" + producto.slug() + ".pdf";
    
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
        .body(pdfBytes);
  }
}
