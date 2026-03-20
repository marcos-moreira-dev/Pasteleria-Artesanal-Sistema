package com.pasteleria.catalogos.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import com.pasteleria.auth.infrastructure.JwtAuthenticationFilter;
import com.pasteleria.catalogos.application.CatalogQueryService;
import com.pasteleria.catalogos.application.ProductCategorySummary;
import com.pasteleria.common.assets.StaticCatalogAssetService;
import com.pasteleria.productos.application.ProductSummary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PublicCatalogController.class)
@AutoConfigureMockMvc(addFilters = false)
class PublicCatalogControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CatalogQueryService catalogQueryService;

  @MockitoBean
  private StaticCatalogAssetService assetService;

  @MockitoBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldReturnPublishedProducts() throws Exception {
    given(catalogQueryService.listPublishedProducts())
        .willReturn(List.of(
            new ProductSummary(
                1L,
                "PROD-TORTA-CHOCO-M",
                "torta-chocolate-mediana",
                "Torta de chocolate mediana",
                "Descripcion",
                new BigDecimal("28.50"),
                false,
                "TORTAS",
                "TORTAS",
                "/assets/products/torta-chocolate-mediana.png",
                "Imagen de Torta de chocolate mediana",
                true,
                true
            )
        ));

    mockMvc.perform(get("/api/v1/public/catalogo/productos").header("X-Request-Id", "req-cat-001"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data[0].code").value("PROD-TORTA-CHOCO-M"))
        .andExpect(jsonPath("$.data[0].categoryCode").value("TORTAS"));
  }

  @Test
  void shouldReturnActiveCategories() throws Exception {
    given(catalogQueryService.listActiveCategories())
        .willReturn(List.of(new ProductCategorySummary(1L, "TORTAS", "TORTAS", "Categoria principal", 1)));

    mockMvc.perform(get("/api/v1/public/catalogo/categorias").header("X-Request-Id", "req-cat-002"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data[0].code").value("TORTAS"));
  }
}


