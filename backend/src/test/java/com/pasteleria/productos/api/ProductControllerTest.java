package com.pasteleria.productos.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasteleria.auth.infrastructure.JwtAuthenticationFilter;
import com.pasteleria.productos.application.CreateProductRequest;
import com.pasteleria.productos.application.ProductCommandService;
import com.pasteleria.productos.application.ProductQueryService;
import com.pasteleria.productos.application.ProductSummary;
import com.pasteleria.productos.application.RecetaJsonDto;
import com.pasteleria.productos.application.UpdateProductRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @MockitoBean
  private ProductQueryService productQueryService;

  @MockitoBean
  private ProductCommandService productCommandService;

  @MockitoBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldListProducts() throws Exception {
    given(productQueryService.listProducts()).willReturn(List.of(sampleProductSummary()));

    mockMvc.perform(get("/api/v1/productos").header("X-Request-Id", "req-product-001"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data[0].name").value("Torta de chocolate"));
  }

  @Test
  void shouldCreateProduct() throws Exception {
    given(productCommandService.createProduct(any(CreateProductRequest.class), any()))
        .willReturn(sampleProductSummary());

    RecetaJsonDto receta = new RecetaJsonDto(
        "Torta de Chocolate",
        "Ingredientes",
        "• 500g harina\n• 300g azúcar",
        "Preparación",
        "1. Mezclar ingredientes\n2. Hornear",
        "Notas",
        "Hornear a 180°C"
    );
    mockMvc.perform(post("/api/v1/productos")
            .header("X-Request-Id", "req-product-002")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(
                new CreateProductRequest(1L, "TORTA-CHOCO", "Torta de chocolate", "Clasica", receta, new BigDecimal("28.50"), false, true, true)
            )))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.code").value("TORTA-CHOCO"));
  }

  @Test
  void shouldUpdateProduct() throws Exception {
    given(productCommandService.updateProduct(eq(1L), any(UpdateProductRequest.class), any()))
        .willReturn(sampleProductSummary());

    RecetaJsonDto recetaUpdate = new RecetaJsonDto(
        "Torta de Chocolate",
        "Ingredientes",
        "• 500g harina\n• 300g azúcar",
        "Preparación",
        "1. Mezclar ingredientes\n2. Hornear",
        "Notas",
        "Hornear a 180°C"
    );
    mockMvc.perform(put("/api/v1/productos/1")
            .header("X-Request-Id", "req-product-003")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(
                new UpdateProductRequest(1L, "TORTA-CHOCO", "Torta de chocolate", "Clasica", recetaUpdate, new BigDecimal("28.50"), false, true, true)
            )))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.name").value("Torta de chocolate"));
  }

  @Test
  void shouldDeleteProduct() throws Exception {
    willDoNothing().given(productCommandService).deleteProduct(eq(1L), any());

    mockMvc.perform(delete("/api/v1/productos/1").header("X-Request-Id", "req-product-004"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }

  private ProductSummary sampleProductSummary() {
    RecetaJsonDto receta = new RecetaJsonDto(
        "Torta de Chocolate",
        "Ingredientes",
        "• 500g harina\n• 300g azúcar",
        "Preparación",
        "1. Mezclar ingredientes\n2. Hornear",
        "Notas",
        "Hornear a 180°C"
    );
    return new ProductSummary(
        1L,
        "TORTA-CHOCO",
        "torta-de-chocolate",
        "Torta de chocolate",
        "Clasica",
        receta,
        new BigDecimal("28.50"),
        false,
        "TORTAS",
        "Tortas",
        "/assets/products/torta-de-chocolate.png",
        "Imagen de Torta de chocolate",
        true,
        true
    );
  }
}


