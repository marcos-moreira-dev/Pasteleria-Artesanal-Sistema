package com.pasteleria.productos.application;

import com.pasteleria.common.error.ResourceNotFoundException;
import com.pasteleria.productos.application.port.ProductRepositoryPort;
import com.pasteleria.productos.infrastructure.persistence.entity.ProductEntity;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;

/**
 * ============================================================
 * SERVICIO DE GESTIÓN DE IMÁGENES DE PRODUCTOS
 * ============================================================
 * 
 * Este servicio demuestra patrón de diseño:
 * - Inyección de dependencias (constructor injection)
 * - Validación de entrada (defense programming)
 * - Manejo de excepciones (fail fast)
 * - Convención de nombres (slug-based naming)
 * 
 * CONCEPTOS CLAVE PARA ESTUDIAR:
 * 1. Uso de @Service para marcar clase como bean de Spring
 * 2. Inyección de dependencias vía constructor
 * 3. Manejo de archivos con java.nio (New IO)
 * 4. Validaciones preventivas antes de operaciones críticas
 * 5. Separación de responsabilidades (validación vs. lógica)
 */
@Service  // Marca esta clase como un componente de servicio de Spring
public class ProductImageService {

  /**
   * CONSTANTES DE CONFIGURACIÓN
   * 
   * Buena práctica: Centralizar valores configurables como constantes.
   * Esto facilita cambios futuros sin buscar en todo el código.
   */
  
  /** Ruta base donde se almacenan todos los archivos */
  private static final String STORAGE_BASE = "./storage";
  
  /** Subdirectorio específico para imágenes de productos */
  private static final String PRODUCTS_ASSETS_PATH = "/assets/products/";
  
  /** 
   * Extensiones permitidas para imágenes.
   * Se incluyen en minúsculas y mayúsculas para ser case-insensitive.
   */
  private static final List<String> ALLOWED_EXTENSIONS = List.of(
      ".png", ".jpg", ".jpeg", ".webp",     // Minúsculas
      ".PNG", ".JPG", ".JPEG", ".WEBP"      // Mayúsculas
  );
  
  /** 
   * Límite máximo de tamaño de archivo: 25MB
   * Se convierte a bytes: 25 * 1024 * 1024 = 26,214,400 bytes
   * 
   * NOTA IMPORTANTE: Este valor debe coincidir con la configuración
   * en application-docker.yml (spring.servlet.multipart.max-file-size)
   */
  private static final long MAX_FILE_SIZE = 25 * 1024 * 1024;

  /**
   * DEPENDENCIAS
   * 
   * Se usa inyección por constructor (Constructor Injection), que es la
   * forma recomendada en Spring porque:
   * - Hace las dependencias explícitas
   * - Facilita testing (inyección de mocks)
   * - Garantiza que el objeto esté completamente inicializado
   */
  private final ProductRepositoryPort productRepository;

  public ProductImageService(ProductRepositoryPort productRepository) {
    this.productRepository = productRepository;
  }

  /**
   * ============================================================
   * MÉTODO PRINCIPAL: SUBIR IMAGEN DE PRODUCTO
   * ============================================================
   * 
   * FLUJO DE OPERACIÓN:
   * 1. Buscar el producto en base de datos
   * 2. Validar que el archivo cumple requisitos
   * 3. Crear directorio de destino si no existe
   * 4. Determinar extensión del archivo
   * 5. Renombrar archivo usando el slug del producto
   * 6. Guardar archivo en disco
   * 7. Retornar ruta pública para acceso
   * 
   * PATRÓN: Fail Fast - Si algo falla, se lanza excepción inmediatamente
   * 
   * @param productId ID único del producto (de la URL)
   * @param imagen Archivo multipart enviado desde el frontend
   * @return Ruta pública de la imagen guardada
   * @throws ResourceNotFoundException Si el producto no existe
   * @throws IllegalArgumentException Si el archivo no es válido
   * @throws RuntimeException Si hay error de I/O al guardar
   */
  public String uploadProductImage(Long productId, MultipartFile imagen) {
    
    // ----------------------------------------------------------------
    // PASO 1: OBTENER PRODUCTO
    // ----------------------------------------------------------------
    // Buscamos el producto por ID. Si no existe, lanzamos excepción.
    // Esto previene guardar imágenes huérfanas (sin producto asociado).
    // 
    // orElseThrow() es un método de Optional que:
    // - Si hay valor, lo retorna
    // - Si está vacío, lanza la excepción proporcionada
    // ----------------------------------------------------------------
    ProductEntity product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "El producto indicado no existe."
        ));

    // ----------------------------------------------------------------
    // PASO 2: VALIDAR ARCHIVO
    // ----------------------------------------------------------------
    // Validamos antes de cualquier operación de I/O.
    // Si el archivo es inválido, fallamos rápido sin crear directorios
    // ni procesar nada más.
    // 
    // Esta es la técnica "Fail Fast" - detectar errores lo antes posible.
    // ----------------------------------------------------------------
    validateImageFile(imagen);

    // ----------------------------------------------------------------
    // PASO 3: PREPARAR DIRECTORIO
    // ----------------------------------------------------------------
    // Creamos la ruta completa: ./storage/assets/products/
    // 
    // Files.createDirectories() es idempotente:
    // - Si el directorio existe, no hace nada
    // - Si no existe, lo crea (incluyendo directorios padre si faltan)
    // 
    // Paths.get() une segmentos de ruta de forma segura,
    // manejando automáticamente separadores (/ vs \)
    // ----------------------------------------------------------------
    Path productsDir = Paths.get(STORAGE_BASE, PRODUCTS_ASSETS_PATH);
    try {
      Files.createDirectories(productsDir);
    } catch (IOException e) {
      // Si falla la creación del directorio, no podemos continuar
      throw new RuntimeException(
          "No se pudo crear el directorio de imágenes", 
          e  // Preservamos la causa original para debugging
      );
    }

    // ----------------------------------------------------------------
    // PASO 4: DETERMINAR EXTENSIÓN DEL ARCHIVO
    // ----------------------------------------------------------------
    // Obtenemos el nombre original y extraemos la extensión.
    // Ejemplo: "mi-foto.jpg" -> ".jpg"
    // 
    // Si no tiene extensión, usamos .png por defecto.
    // ----------------------------------------------------------------
    String originalFilename = imagen.getOriginalFilename();
    String extension = getFileExtension(originalFilename);
    if (extension.isEmpty()) {
      extension = ".png"; // Formato por defecto seguro
    }

    // ----------------------------------------------------------------
    // PASO 5: GENERAR NOMBRE DE ARCHIVO ÚNICO
    // ----------------------------------------------------------------
    // Usamos el SLUG del producto como nombre de archivo.
    // 
    // ¿Por qué usar slug?
    // - Es único (validado al crear producto)
    // - Es legible (torta-chocolate vs TORTA_001)
    // - Es SEO-friendly para URLs
    // - Evita caracteres especiales problemáticos
    // 
    // Convertimos a minúsculas para consistencia.
    // ----------------------------------------------------------------
    String slug = product.getSlug();
    String filename = slug + extension.toLowerCase(Locale.ROOT);
    Path targetPath = productsDir.resolve(filename);

    // ----------------------------------------------------------------
    // PASO 6: GUARDAR ARCHIVO EN DISCO
    // ----------------------------------------------------------------
    // Files.copy() transfiere bytes del InputStream al archivo.
    // 
    // StandardCopyOption.REPLACE_EXISTING:
    // - Si ya existe una imagen con ese nombre, la reemplaza
    // - Esto permite "actualizar" la imagen de un producto
    // 
    // El try-with-resources no es necesario porque MultipartFile
    // maneja automáticamente el cierre del InputStream.
    // ----------------------------------------------------------------
    try {
      Files.copy(
          imagen.getInputStream(), 
          targetPath, 
          StandardCopyOption.REPLACE_EXISTING
      );
    } catch (IOException e) {
      throw new RuntimeException("Error al guardar la imagen", e);
    }

    // ----------------------------------------------------------------
    // PASO 7: RETORNAR RUTA PÚBLICA
    // ----------------------------------------------------------------
    // Retornamos la ruta relativa que el frontend usará para acceder
    // a la imagen vía el backend.
    // 
    // Ejemplo: /assets/products/torta-chocolate.png
    // 
    // El backend expone este directorio como recurso estático.
    // ----------------------------------------------------------------
    return PRODUCTS_ASSETS_PATH + filename;
  }

  /**
   * ============================================================
   * VALIDACIÓN DE ARCHIVO DE IMAGEN
   * ============================================================
   * 
   * Este método centraliza todas las validaciones de seguridad
   * y negocio para archivos subidos.
   * 
   * VALIDACIONES REALIZADAS:
   * 1. Archivo no es null ni vacío
   * 2. Tamaño no excede el límite (25MB)
   * 3. Nombre de archivo es válido
   * 4. Extensión está en lista de permitidos
   * 
   * PATRÓN: Defensive Programming - Asumir que entrada puede ser maliciosa
   * 
   * @param file Archivo a validar
   * @throws IllegalArgumentException Si el archivo no cumple alguna validación
   */
  private void validateImageFile(MultipartFile file) {
    
    // VALIDACIÓN 1: Verificar que se envió un archivo
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("Debe seleccionar una imagen");
    }

    // VALIDACIÓN 2: Verificar tamaño máximo
    // Esto previene ataques de denegación de servicio (DoS)
    // subiendo archivos gigantes para llenar el disco.
    if (file.getSize() > MAX_FILE_SIZE) {
      throw new IllegalArgumentException(
          "La imagen no debe superar los 25MB"
      );
    }

    // VALIDACIÓN 3: Verificar nombre de archivo
    String filename = file.getOriginalFilename();
    if (filename == null || filename.isBlank()) {
      throw new IllegalArgumentException("Nombre de archivo inválido");
    }

    // VALIDACIÓN 4: Verificar extensión permitida
    // Esto previene ejecución de archivos maliciosos
    // (ej. alguien sube virus.exe renombrado a foto.jpg.exe)
    String extension = getFileExtension(filename).toLowerCase(Locale.ROOT);
    if (!ALLOWED_EXTENSIONS.contains(extension)) {
      throw new IllegalArgumentException(
          "Formato no soportado. Use: PNG, JPG, JPEG o WebP"
      );
    }
  }

  /**
   * ============================================================
   * EXTRACCIÓN DE EXTENSIÓN DE ARCHIVO
   * ============================================================
   * 
   * Extrae la extensión de un nombre de archivo.
   * 
   * EJEMPLOS:
   * - "foto.jpg"      -> ".jpg"
   * - "archivo.JPEG"  -> ".JPEG"
   * - "sin_extension" -> "" (string vacío)
   * - null            -> "" (string vacío)
   * 
   * IMPLEMENTACIÓN:
   * Busca el último punto (.) en el nombre y retorna todo desde
   * ese punto hasta el final.
   * 
   * @param filename Nombre del archivo
   * @return Extensión con el punto (ej: ".jpg") o string vacío
   */
  private String getFileExtension(String filename) {
    // Validación defensiva: si es null, retornamos vacío
    if (filename == null || filename.lastIndexOf(".") == -1) {
      return "";
    }
    
    // substring desde la última ocurrencia de "."
    // Esto maneja correctamente nombres como "mi.archivo.jpg"
    // Retornando ".jpg", no ".archivo.jpg"
    return filename.substring(filename.lastIndexOf("."));
  }
}
