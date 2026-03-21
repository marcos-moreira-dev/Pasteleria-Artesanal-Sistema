package com.pasteleria.productos.application;

import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.productos.application.port.ProductRepositoryPort;
import com.pasteleria.productos.infrastructure.persistence.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ============================================================
 * TESTS UNITARIOS PARA ProductImageService
 * ============================================================
 * 
 * ESTE ARCHIVO DEMUESTRA:
 * 
 * 1. JUNIT 5 (JUPITER) - Framework de testing moderno
 *    - @Test: Marca métodos como tests
 *    - @BeforeEach: Setup antes de cada test
 *    - @DisplayName: Descripción legible del test
 * 
 * 2. MOCKITO - Framework de mocking
 *    - @Mock: Crea mocks automáticamente
 *    - @ExtendWith: Integra Mockito con JUnit 5
 *    - when/thenReturn: Define comportamiento de mocks
 *    - verify: Verifica interacciones con mocks
 * 
 * 3. PATRÓN AAA (Arrange-Act-Assert)
 *    - Arrange: Preparar datos y mocks
 *    - Act: Ejecutar el método a probar
 *    - Assert: Verificar resultados
 * 
 * 4. ASSERTJ - Librería de assertions fluida
 *    - assertThat(): Punto de entrada
 *    - isEqualTo(), contains(), etc.: Assertions legibles
 *    - hasMessage(): Verificar excepciones
 * 
 * 5. PRUEBAS NEGATIVAS
 *    - assertThatThrownBy(): Verificar excepciones
 *    - Casos de error y validaciones
 */
@ExtendWith(MockitoExtension.class)  // Integra Mockito con JUnit 5
@DisplayName("Tests para ProductImageService")
class ProductImageServiceTest {

  /**
   * DEPENDENCIAS MOCKS
   * 
   * @Mock crea una implementación "falsa" de la interfaz.
   * En lugar de usar la base de datos real, usamos objetos
   * que podemos controlar y verificar.
   */
  @Mock
  private ProductRepositoryPort productRepository;

  /**
   * SUT (System Under Test)
   * El componente que estamos probando.
   */
  private ProductImageService imageService;

  /**
   * ==========================================================
   * SETUP: Configuración inicial antes de cada test
   * ==========================================================
   * 
   * @BeforeEach garantiza que cada test tenga un estado limpio.
   * Esto evita que tests afecten entre sí (test isolation).
   */
  @BeforeEach
  void setUp() {
    // Creamos el servicio con el mock inyectado
    imageService = new ProductImageService(productRepository);
  }

  /**
   * ==========================================================
   * TEST 1: Flujo exitoso de subida de imagen
   * ==========================================================
   * 
   * Escenario: Producto existe, archivo válido
   * Resultado esperado: Imagen se guarda, ruta retornada
   */
  @Test
  @DisplayName("Debería subir imagen exitosamente cuando producto existe")
  void shouldUploadImageSuccessfully() throws IOException {
    // =========================================================
    // ARRANGE (Preparar)
    // =========================================================
    
    // ID del producto a buscar
    Long productId = 1L;
    
    // Crear un producto mock con slug
    ProductEntity product = new ProductEntity();
    product.setId(productId);
    product.setSlug("torta-chocolate");
    
    // Configurar mock: cuando busquen el producto, retornar el producto
    when(productRepository.findById(productId))
        .thenReturn(Optional.of(product));
    
    // Crear archivo de prueba (MockMultipartFile)
    // Simula un archivo PNG subido por usuario
    MultipartFile imageFile = new MockMultipartFile(
        "imagen",                    // Nombre del campo
        "foto.jpg",                  // Nombre original del archivo
        "image/jpeg",                // Tipo MIME
        "contenido-fake".getBytes()  // Contenido (bytes)
    );

    // =========================================================
    // ACT (Ejecutar)
    // =========================================================
    
    // Ejecutar el método bajo prueba
    String result = imageService.uploadProductImage(productId, imageFile);

    // =========================================================
    // ASSERT (Verificar)
    // =========================================================
    
    // 1. Verificar que la ruta retornada es correcta
    assertThat(result)
        .isEqualTo("/assets/products/torta-chocolate.jpg");
    
    // 2. Verificar que se buscó el producto en BD
    verify(productRepository).findById(productId);
    
    // 3. Verificar que SOLO se buscó una vez (no más, no menos)
    verify(productRepository, times(1)).findById(productId);
  }

  /**
   * ==========================================================
   * TEST 2: Producto no existe
   * ==========================================================
   * 
   * Escenario: ID de producto no existe en BD
   * Resultado esperado: Lanza ResourceNotFoundException
   */
  @Test
  @DisplayName("Debería lanzar excepción cuando producto no existe")
  void shouldThrowExceptionWhenProductNotFound() {
    // ARRANGE
    Long nonExistentProductId = 999L;
    
    // Configurar mock: producto no existe
    when(productRepository.findById(nonExistentProductId))
        .thenReturn(Optional.empty());
    
    MultipartFile imageFile = new MockMultipartFile(
        "imagen",
        "foto.jpg",
        "image/jpeg",
        "contenido".getBytes()
    );

    // ACT & ASSERT
    // assertThatThrownBy captura y verifica excepciones
    assertThatThrownBy(() -> 
        imageService.uploadProductImage(nonExistentProductId, imageFile)
    )
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("no existe");
    
    // Verificar que se intentó buscar el producto
    verify(productRepository).findById(nonExistentProductId);
  }

  /**
   * ==========================================================
   * TEST 3: Archivo vacío
   * ==========================================================
   * 
   * Escenario: Usuario no selecciona archivo
   * Resultado esperado: Lanza IllegalArgumentException
   */
  @Test
  @DisplayName("Debería rechazar archivo vacío")
  void shouldRejectEmptyFile() {
    // ARRANGE
    Long productId = 1L;
    ProductEntity product = new ProductEntity();
    product.setId(productId);
    product.setSlug("producto-test");
    
    when(productRepository.findById(productId))
        .thenReturn(Optional.of(product));
    
    // Archivo vacío (0 bytes)
    MultipartFile emptyFile = new MockMultipartFile(
        "imagen",
        "",
        "image/jpeg",
        new byte[0]
    );

    // ACT & ASSERT
    assertThatThrownBy(() -> 
        imageService.uploadProductImage(productId, emptyFile)
    )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("seleccionar");
  }

  /**
   * ==========================================================
   * TEST 4: Archivo demasiado grande
   * ==========================================================
   * 
   * Escenario: Archivo excede límite de 25MB
   * Resultado esperado: Lanza IllegalArgumentException
   */
  @Test
  @DisplayName("Debería rechazar archivo que excede tamaño máximo")
  void shouldRejectOversizedFile() {
    // ARRANGE
    Long productId = 1L;
    ProductEntity product = new ProductEntity();
    product.setId(productId);
    product.setSlug("producto-test");
    
    when(productRepository.findById(productId))
        .thenReturn(Optional.of(product));
    
    // Crear archivo de 30MB (30 * 1024 * 1024 bytes)
    byte[] largeContent = new byte[30 * 1024 * 1024];
    MultipartFile largeFile = new MockMultipartFile(
        "imagen",
        "foto-grande.jpg",
        "image/jpeg",
        largeContent
    );

    // ACT & ASSERT
    assertThatThrownBy(() -> 
        imageService.uploadProductImage(productId, largeFile)
    )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("25MB");
  }

  /**
   * ==========================================================
   * TEST 5: Extensión no permitida
   * ==========================================================
   * 
   * Escenario: Archivo con extensión peligrosa (.exe)
   * Resultado esperado: Lanza IllegalArgumentException
   */
  @Test
  @DisplayName("Debería rechazar archivo con extensión no permitida")
  void shouldRejectInvalidExtension() {
    // ARRANGE
    Long productId = 1L;
    ProductEntity product = new ProductEntity();
    product.setId(productId);
    product.setSlug("producto-test");
    
    when(productRepository.findById(productId))
        .thenReturn(Optional.of(product));
    
    // Intentar subir ejecutable (¡peligroso!)
    MultipartFile exeFile = new MockMultipartFile(
        "imagen",
        "virus.exe",  // ¡Extensión no permitida!
        "application/octet-stream",
        "contenido-malicioso".getBytes()
    );

    // ACT & ASSERT
    assertThatThrownBy(() -> 
        imageService.uploadProductImage(productId, exeFile)
    )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Formato no soportado");
  }

  /**
   * ==========================================================
   * TEST 6: Extensión con mayúsculas
   * ==========================================================
   * 
   * Escenario: Archivo con extensión en mayúsculas (.PNG)
   * Resultado esperado: Acepta el archivo (case-insensitive)
   */
  @Test
  @DisplayName("Debería aceptar extensión en mayúsculas")
  void shouldAcceptUppercaseExtension() throws IOException {
    // ARRANGE
    Long productId = 1L;
    ProductEntity product = new ProductEntity();
    product.setId(productId);
    product.setSlug("producto-test");
    
    when(productRepository.findById(productId))
        .thenReturn(Optional.of(product));
    
    // Archivo con extensión en mayúsculas
    MultipartFile imageFile = new MockMultipartFile(
        "imagen",
        "foto.PNG",  // Mayúsculas
        "image/png",
        "contenido".getBytes()
    );

    // ACT
    String result = imageService.uploadProductImage(productId, imageFile);

    // ASSERT
    // Convierte a minúsculas como debe ser
    assertThat(result)
        .isEqualTo("/assets/products/producto-test.png");
  }
}
