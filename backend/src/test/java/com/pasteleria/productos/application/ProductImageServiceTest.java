package com.pasteleria.productos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.productos.application.port.ProductRepositoryPort;
import com.pasteleria.productos.infrastructure.persistence.entity.ProductEntity;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * Verifica la política mínima de subida de imágenes: producto existente,
 * extensión permitida, tamaño máximo y nombre final basado en el slug.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductImageService")
class ProductImageServiceTest {

  @Mock
  private ProductRepositoryPort productRepository;

  private ProductImageService imageService;

  @BeforeEach
  void setUp() {
    imageService = new ProductImageService(productRepository);
  }

  @Test
  @DisplayName("guarda la imagen con el slug del producto")
  void shouldUploadImageSuccessfully() throws IOException {
    Long productId = 1L;
    ProductEntity product = new ProductEntity();
    product.setId(productId);
    product.setSlug("torta-chocolate");
    when(productRepository.findById(productId)).thenReturn(Optional.of(product));

    MultipartFile imageFile = new MockMultipartFile(
        "imagen",
        "foto.jpg",
        "image/jpeg",
        "contenido-fake".getBytes()
    );

    String result = imageService.uploadProductImage(productId, imageFile);

    assertThat(result).isEqualTo("/assets/products/torta-chocolate.jpg");
    verify(productRepository, times(1)).findById(productId);
  }

  @Test
  @DisplayName("rechaza la carga si el producto no existe")
  void shouldThrowExceptionWhenProductNotFound() {
    Long productId = 999L;
    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    MultipartFile imageFile = new MockMultipartFile(
        "imagen",
        "foto.jpg",
        "image/jpeg",
        "contenido".getBytes()
    );

    assertThatThrownBy(() -> imageService.uploadProductImage(productId, imageFile))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("no existe");
  }

  @Test
  @DisplayName("rechaza archivos vacíos")
  void shouldRejectEmptyFile() {
    mockProduct("producto-test");

    MultipartFile emptyFile = new MockMultipartFile("imagen", "", "image/jpeg", new byte[0]);

    assertThatThrownBy(() -> imageService.uploadProductImage(1L, emptyFile))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("seleccionar");
  }

  @Test
  @DisplayName("rechaza archivos que superan 25 MB")
  void shouldRejectOversizedFile() {
    mockProduct("producto-test");

    byte[] largeContent = new byte[30 * 1024 * 1024];
    MultipartFile largeFile = new MockMultipartFile(
        "imagen",
        "foto-grande.jpg",
        "image/jpeg",
        largeContent
    );

    assertThatThrownBy(() -> imageService.uploadProductImage(1L, largeFile))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("25 MB");
  }

  @Test
  @DisplayName("rechaza extensiones no permitidas")
  void shouldRejectInvalidExtension() {
    mockProduct("producto-test");

    MultipartFile exeFile = new MockMultipartFile(
        "imagen",
        "virus.exe",
        "application/octet-stream",
        "contenido-malicioso".getBytes()
    );

    assertThatThrownBy(() -> imageService.uploadProductImage(1L, exeFile))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Formato no soportado");
  }

  @Test
  @DisplayName("normaliza extensiones en mayúsculas a minúsculas")
  void shouldAcceptUppercaseExtension() throws IOException {
    mockProduct("producto-test");

    MultipartFile imageFile = new MockMultipartFile(
        "imagen",
        "foto.PNG",
        "image/png",
        "contenido".getBytes()
    );

    String result = imageService.uploadProductImage(1L, imageFile);

    assertThat(result).isEqualTo("/assets/products/producto-test.png");
  }

  private void mockProduct(String slug) {
    ProductEntity product = new ProductEntity();
    product.setId(1L);
    product.setSlug(slug);
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
  }
}
