package com.pasteleria.productos.application;

import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.productos.application.port.ProductRepositoryPort;
import com.pasteleria.productos.infrastructure.persistence.entity.ProductEntity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Guarda imágenes del catálogo bajo una convención estable basada en el slug
 * del producto. Así el frontend solo necesita conocer la ruta pública
 * `/assets/products/<slug>.<ext>` y el backend conserva la responsabilidad de
 * validar formato, tamaño y ubicación física.
 */
@Service
public class ProductImageService {

  private static final String STORAGE_BASE = "./storage";
  private static final String PRODUCTS_ASSETS_PATH = "/assets/products/";
  private static final List<String> ALLOWED_EXTENSIONS = List.of(".png", ".jpg", ".jpeg", ".webp");
  private static final long MAX_FILE_SIZE = 25L * 1024 * 1024;

  private final ProductRepositoryPort productRepository;

  public ProductImageService(ProductRepositoryPort productRepository) {
    this.productRepository = productRepository;
  }

  /**
   * Reemplaza o crea la imagen operativa de un producto.
   *
   * <p>La ruta se resuelve desde el slug persistido en base de datos para no
   * acoplar la URL pública al nombre editable del archivo subido por el
   * usuario.</p>
   */
  public String uploadProductImage(Long productId, MultipartFile image) {
    ProductEntity product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("El producto indicado no existe."));

    validateImageFile(image);

    Path productsDir = Paths.get(STORAGE_BASE, PRODUCTS_ASSETS_PATH);
    createProductsDirectory(productsDir);

    String extension = resolveExtension(image.getOriginalFilename());
    String filename = product.getSlug() + extension;
    Path targetPath = productsDir.resolve(filename);

    try {
      Files.copy(image.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException exception) {
      throw new RuntimeException("No se pudo guardar la imagen del producto.", exception);
    }

    return PRODUCTS_ASSETS_PATH + filename;
  }

  private void createProductsDirectory(Path productsDir) {
    try {
      Files.createDirectories(productsDir);
    } catch (IOException exception) {
      throw new RuntimeException("No se pudo preparar el directorio de imágenes del catálogo.", exception);
    }
  }

  private void validateImageFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("Debe seleccionar una imagen.");
    }

    if (file.getSize() > MAX_FILE_SIZE) {
      throw new IllegalArgumentException("La imagen no debe superar los 25 MB.");
    }

    String filename = file.getOriginalFilename();
    if (filename == null || filename.isBlank()) {
      throw new IllegalArgumentException("El archivo no tiene un nombre válido.");
    }

    String extension = resolveExtension(filename);
    if (!ALLOWED_EXTENSIONS.contains(extension)) {
      throw new IllegalArgumentException("Formato no soportado. Use PNG, JPG, JPEG o WebP.");
    }
  }

  private String resolveExtension(String filename) {
    if (filename == null) {
      return ".png";
    }

    int lastDot = filename.lastIndexOf('.');
    if (lastDot < 0) {
      return ".png";
    }

    return filename.substring(lastDot).toLowerCase(Locale.ROOT);
  }
}
