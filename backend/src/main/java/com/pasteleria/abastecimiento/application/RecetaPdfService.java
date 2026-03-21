package com.pasteleria.abastecimiento.application;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.pasteleria.productos.application.ProductSummary;
import com.pasteleria.productos.application.RecetaJsonDto;

import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

@Service
public class RecetaPdfService {

  // Colores pastel elegantes
  private static final Color COLOR_PRIMARY = new Color(139, 90, 70);      // Marrón café
  private static final Color COLOR_SECONDARY = new Color(201, 166, 107); // Dorado suave
  private static final Color COLOR_BACKGROUND = new Color(255, 250, 245); // Crema
  private static final Color COLOR_TEXT = new Color(45, 32, 26);         // Marrón oscuro
  private static final Color COLOR_LIGHT = new Color(245, 235, 228);     // Beige claro

  public byte[] generarRecetaPdf(ProductSummary producto, RecetaJsonDto receta) {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      Document document = new Document(PageSize.A4, 50, 50, 60, 60);
      PdfWriter writer = PdfWriter.getInstance(document, baos);
      document.open();

      // Header decorativo
      agregarHeader(document);
      
      // Título principal
      agregarTituloPrincipal(document, producto);
      
      // Info del producto en caja
      agregarInfoProducto(document, producto);
      
      document.add(new Paragraph(" "));
      
      // Contenido de la receta
      if (receta != null && tieneContenido(receta)) {
        agregarReceta(document, receta);
      } else {
        agregarSinReceta(document);
      }
      
      // Footer
      agregarFooter(document, writer);

      document.close();
      return baos.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("Error generando PDF de receta", e);
    }
  }

  private void agregarHeader(Document doc) throws DocumentException {
    // Línea decorativa superior
    Paragraph line = new Paragraph(" ");
    line.setSpacingAfter(10);
    doc.add(line);
    
    // Título del documento
    Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COLOR_SECONDARY);
    Paragraph header = new Paragraph("PASTELERÍA ARTESANAL", fontHeader);
    header.setAlignment(Element.ALIGN_CENTER);
    doc.add(header);
    
    Font fontSub = FontFactory.getFont(FontFactory.HELVETICA, 8, COLOR_PRIMARY);
    Paragraph sub = new Paragraph("Casa de Producción", fontSub);
    sub.setAlignment(Element.ALIGN_CENTER);
    sub.setSpacingAfter(15);
    doc.add(sub);
    
    // Línea divisoria decorativa
    PdfPTable lineTable = new PdfPTable(1);
    lineTable.setWidthPercentage(40);
    PdfPCell lineCell = new PdfPCell();
    lineCell.setBorder(Rectangle.BOTTOM);
    lineCell.setBorderColor(COLOR_SECONDARY);
    lineCell.setBorderWidth(2);
    lineCell.setPaddingBottom(5);
    lineTable.addCell(lineCell);
    lineTable.setSpacingAfter(20);
    doc.add(lineTable);
  }

  private void agregarTituloPrincipal(Document doc, ProductSummary producto) throws DocumentException {
    Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, COLOR_PRIMARY);
    Paragraph title = new Paragraph("RECETA TÉCNICA", fontTitle);
    title.setAlignment(Element.ALIGN_CENTER);
    title.setSpacingAfter(5);
    doc.add(title);
    
    Font fontProduct = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, COLOR_TEXT);
    Paragraph product = new Paragraph(producto.name().toUpperCase(), fontProduct);
    product.setAlignment(Element.ALIGN_CENTER);
    product.setSpacingAfter(20);
    doc.add(product);
  }

  private void agregarInfoProducto(Document doc, ProductSummary producto) throws DocumentException {
    PdfPTable table = new PdfPTable(2);
    table.setWidthPercentage(70);
    table.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.setSpacingAfter(10);
    
    // Estilo de celdas
    Font fontLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COLOR_PRIMARY);
    Font fontValue = FontFactory.getFont(FontFactory.HELVETICA, 10, COLOR_TEXT);
    
    // Código
    PdfPCell cellLabel = crearCeldaInfo("Código:", fontLabel, Element.ALIGN_RIGHT);
    PdfPCell cellValue = crearCeldaInfo(producto.code(), fontValue, Element.ALIGN_LEFT);
    table.addCell(cellLabel);
    table.addCell(cellValue);
    
    // Categoría
    cellLabel = crearCeldaInfo("Categoría:", fontLabel, Element.ALIGN_RIGHT);
    cellValue = crearCeldaInfo(producto.categoryName(), fontValue, Element.ALIGN_LEFT);
    table.addCell(cellLabel);
    table.addCell(cellValue);
    
    // Precio
    cellLabel = crearCeldaInfo("Precio Base:", fontLabel, Element.ALIGN_RIGHT);
    cellValue = crearCeldaInfo("$" + producto.basePrice(), fontValue, Element.ALIGN_LEFT);
    table.addCell(cellLabel);
    table.addCell(cellValue);
    
    doc.add(table);
  }

  private PdfPCell crearCeldaInfo(String texto, Font font, int align) {
    PdfPCell cell = new PdfPCell(new Phrase(texto, font));
    cell.setBorder(Rectangle.NO_BORDER);
    cell.setHorizontalAlignment(align);
    cell.setPadding(3);
    return cell;
  }

  private void agregarReceta(Document doc, RecetaJsonDto receta) throws DocumentException {
    // Título de la receta
    if (receta.titulo() != null && !receta.titulo().isEmpty()) {
      Font fontRecetaTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, COLOR_SECONDARY);
      Paragraph recetaTitle = new Paragraph(receta.titulo().toUpperCase(), fontRecetaTitle);
      recetaTitle.setAlignment(Element.ALIGN_CENTER);
      recetaTitle.setSpacingAfter(15);
      doc.add(recetaTitle);
    }
    
    // Ingredientes
    if (receta.ingredientes() != null && !receta.ingredientes().isEmpty()) {
      agregarSeccion(doc, 
        receta.tituloIngredientes() != null ? receta.tituloIngredientes() : "Ingredientes",
        receta.ingredientes(),
        new Color(255, 243, 230)); // Naranja muy suave
    }
    
    // Preparación
    if (receta.pasos() != null && !receta.pasos().isEmpty()) {
      agregarSeccion(doc,
        receta.tituloPasos() != null ? receta.tituloPasos() : "Preparación", 
        receta.pasos(),
        new Color(245, 255, 245)); // Verde muy suave
    }
    
    // Observaciones
    if (receta.observaciones() != null && !receta.observaciones().isEmpty()) {
      agregarSeccion(doc,
        receta.tituloObservaciones() != null ? receta.tituloObservaciones() : "Notas",
        receta.observaciones(),
        new Color(255, 250, 240)); // Amarillo muy suave
    }
  }

  private void agregarSeccion(Document doc, String titulo, String contenido, Color bgColor) throws DocumentException {
    // Caja con título
    PdfPTable table = new PdfPTable(1);
    table.setWidthPercentage(100);
    table.setSpacingAfter(15);
    
    // Título de sección
    Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
    PdfPCell titleCell = new PdfPCell(new Phrase(titulo.toUpperCase(), fontTitle));
    titleCell.setBackgroundColor(COLOR_PRIMARY);
    titleCell.setPadding(8);
    titleCell.setBorder(Rectangle.NO_BORDER);
    titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
    table.addCell(titleCell);
    
    // Contenido
    Font fontContent = FontFactory.getFont(FontFactory.HELVETICA, 11, COLOR_TEXT);
    PdfPCell contentCell = new PdfPCell();
    contentCell.setBackgroundColor(bgColor);
    contentCell.setPadding(12);
    contentCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
    contentCell.setBorderColor(COLOR_SECONDARY);
    contentCell.setBorderWidth(1);
    
    // Agregar contenido preservando saltos de línea
    Paragraph content = new Paragraph(contenido, fontContent);
    content.setLeading(14);
    contentCell.addElement(content);
    
    table.addCell(contentCell);
    doc.add(table);
  }

  private void agregarSinReceta(Document doc) throws DocumentException {
    Font font = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 11, COLOR_PRIMARY);
    Paragraph msg = new Paragraph("Este producto aún no tiene receta registrada.", font);
    msg.setAlignment(Element.ALIGN_CENTER);
    msg.setSpacingBefore(20);
    msg.setSpacingAfter(20);
    doc.add(msg);
  }

  private void agregarFooter(Document doc, PdfWriter writer) throws DocumentException {
    // Espacio antes del footer
    Paragraph spacer = new Paragraph(" ");
    spacer.setSpacingBefore(30);
    doc.add(spacer);
    
    // Línea decorativa
    PdfPTable lineTable = new PdfPTable(1);
    lineTable.setWidthPercentage(100);
    PdfPCell lineCell = new PdfPCell();
    lineCell.setBorder(Rectangle.TOP);
    lineCell.setBorderColor(COLOR_LIGHT);
    lineCell.setBorderWidth(1);
    lineCell.setPaddingTop(10);
    lineTable.addCell(lineCell);
    doc.add(lineTable);
    
    // Texto del footer
    Font fontFooter = FontFactory.getFont(FontFactory.HELVETICA, 8, COLOR_PRIMARY);
    Paragraph footer = new Paragraph("Documento generado por Pastelería Artesanal - Casa de Producción", fontFooter);
    footer.setAlignment(Element.ALIGN_CENTER);
    footer.setSpacingBefore(10);
    doc.add(footer);
    
    Paragraph date = new Paragraph("Fecha: " + new java.util.Date().toString(), fontFooter);
    date.setAlignment(Element.ALIGN_CENTER);
    doc.add(date);
  }

  private boolean tieneContenido(RecetaJsonDto receta) {
    return (receta.titulo() != null && !receta.titulo().isEmpty()) ||
           (receta.ingredientes() != null && !receta.ingredientes().isEmpty()) ||
           (receta.pasos() != null && !receta.pasos().isEmpty()) ||
           (receta.observaciones() != null && !receta.observaciones().isEmpty());
  }
}
