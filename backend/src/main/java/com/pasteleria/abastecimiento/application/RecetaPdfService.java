package com.pasteleria.abastecimiento.application;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.pasteleria.productos.application.ProductSummary;
import com.pasteleria.productos.application.RecetaJsonDto;

import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

@Service
public class RecetaPdfService {

  private static final Color COLOR_PRIMARY = new Color(92, 46, 32);
  private static final Color COLOR_SECONDARY = new Color(201, 166, 107);
  private static final Color COLOR_TEXT = new Color(45, 32, 26);
  private static final Color COLOR_LIGHT = new Color(245, 235, 228);
  private static final Color COLOR_SECTION = new Color(255, 248, 243);

  public byte[] generarRecetaPdf(ProductSummary producto, RecetaJsonDto receta) {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      Document document = new Document(PageSize.A4, 48, 48, 48, 52);
      PdfWriter writer = PdfWriter.getInstance(document, baos);
      document.open();

      agregarHeader(document);
      agregarTituloPrincipal(document, producto);
      agregarFichaProducto(document, producto);
      document.add(new Paragraph(" "));

      if (receta != null && tieneContenido(receta)) {
        agregarReceta(document, receta);
      } else {
        agregarSinReceta(document);
      }

      agregarFooter(document, writer);
      document.close();
      return baos.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("Error generando PDF de receta", e);
    }
  }

  private void agregarHeader(Document doc) throws Exception {
    PdfPTable table = new PdfPTable(1);
    table.setWidthPercentage(100);

    PdfPCell cell = new PdfPCell();
    cell.setBorder(Rectangle.NO_BORDER);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    cell.setPaddingBottom(10);

    Image logo = cargarImagen(Path.of("storage", "assets", "branding", "logo-horizontal.png"));
    if (logo == null) {
      logo = cargarImagen(Path.of("storage", "assets", "branding", "logo-cuadrado.png"));
    }

    if (logo != null) {
      logo.scaleToFit(150, 72);
      logo.setAlignment(Element.ALIGN_CENTER);
      cell.addElement(logo);
    } else {
      Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, COLOR_SECONDARY);
      Paragraph header = new Paragraph("PASTELERÍA ARTESANAL", fontHeader);
      header.setAlignment(Element.ALIGN_CENTER);
      cell.addElement(header);
    }

    Font fontSub = FontFactory.getFont(FontFactory.HELVETICA, 9, COLOR_PRIMARY);
    Paragraph sub = new Paragraph("Casa de Producción · Ficha técnica interna", fontSub);
    sub.setAlignment(Element.ALIGN_CENTER);
    sub.setSpacingBefore(4);
    cell.addElement(sub);
    table.addCell(cell);
    doc.add(table);

    PdfPTable lineTable = new PdfPTable(1);
    lineTable.setWidthPercentage(52);
    lineTable.setHorizontalAlignment(Element.ALIGN_CENTER);
    PdfPCell lineCell = new PdfPCell();
    lineCell.setBorder(Rectangle.BOTTOM);
    lineCell.setBorderColor(COLOR_SECONDARY);
    lineCell.setBorderWidth(2);
    lineCell.setPaddingBottom(8);
    lineTable.addCell(lineCell);
    lineTable.setSpacingAfter(16);
    doc.add(lineTable);
  }

  private void agregarTituloPrincipal(Document doc, ProductSummary producto) throws DocumentException {
    Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, COLOR_PRIMARY);
    Paragraph title = new Paragraph("RECETA TÉCNICA", fontTitle);
    title.setAlignment(Element.ALIGN_CENTER);
    title.setSpacingAfter(5);
    doc.add(title);

    Font fontProduct = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 17, COLOR_TEXT);
    Paragraph product = new Paragraph(producto.name().toUpperCase(), fontProduct);
    product.setAlignment(Element.ALIGN_CENTER);
    product.setSpacingAfter(18);
    doc.add(product);
  }

  private void agregarFichaProducto(Document doc, ProductSummary producto) throws Exception {
    PdfPTable table = new PdfPTable(2);
    table.setWidthPercentage(100);
    table.setWidths(new float[] { 34, 66 });
    table.setSpacingAfter(10);

    PdfPCell imageCell = new PdfPCell();
    imageCell.setBorder(Rectangle.NO_BORDER);
    imageCell.setBackgroundColor(Color.WHITE);
    imageCell.setPadding(4);
    imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    imageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

    Image productImage = cargarImagenProducto(producto.imagePath());
    if (productImage != null) {
      productImage.scaleToFit(150, 150);
      productImage.setAlignment(Element.ALIGN_CENTER);
      imageCell.addElement(productImage);
    } else {
      Font fallback = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, COLOR_PRIMARY);
      Paragraph text = new Paragraph("Imagen no disponible", fallback);
      text.setAlignment(Element.ALIGN_CENTER);
      imageCell.addElement(text);
    }
    table.addCell(imageCell);

    PdfPCell infoCell = new PdfPCell();
    infoCell.setBorder(Rectangle.NO_BORDER);
    infoCell.setBackgroundColor(Color.WHITE);
    infoCell.setPadding(12);

    PdfPTable info = new PdfPTable(2);
    info.setWidthPercentage(100);
    info.setWidths(new float[] { 34, 66 });
    agregarInfo(info, "Código", producto.code());
    agregarInfo(info, "Categoría", producto.categoryName());
    agregarInfo(info, "Precio base", "$" + producto.basePrice());
    agregarInfo(info, "Tipo", producto.quotationRequired() ? "Requiere cotización" : "Venta directa");
    agregarInfo(info, "Slug", producto.slug());
    infoCell.addElement(info);
    table.addCell(infoCell);

    doc.add(table);
  }

  private void agregarInfo(PdfPTable table, String label, String value) {
    Font fontLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, COLOR_PRIMARY);
    Font fontValue = FontFactory.getFont(FontFactory.HELVETICA, 9, COLOR_TEXT);

    PdfPCell labelCell = new PdfPCell(new Phrase(label + ":", fontLabel));
    labelCell.setBorder(Rectangle.NO_BORDER);
    labelCell.setPadding(4);
    labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    table.addCell(labelCell);

    PdfPCell valueCell = new PdfPCell(new Phrase(value == null ? "—" : value, fontValue));
    valueCell.setBorder(Rectangle.NO_BORDER);
    valueCell.setPadding(4);
    table.addCell(valueCell);
  }

  private void agregarReceta(Document doc, RecetaJsonDto receta) throws DocumentException {
    if (receta.titulo() != null && !receta.titulo().isBlank()) {
      Font fontRecetaTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, COLOR_SECONDARY);
      Paragraph recetaTitle = new Paragraph(normalizarTextoPdf(receta.titulo()).toUpperCase(), fontRecetaTitle);
      recetaTitle.setAlignment(Element.ALIGN_CENTER);
      recetaTitle.setSpacingAfter(14);
      doc.add(recetaTitle);
    }

    if (receta.ingredientes() != null && !receta.ingredientes().isBlank()) {
      agregarSeccion(doc, receta.tituloIngredientes() != null ? receta.tituloIngredientes() : "Ingredientes", receta.ingredientes(), new Color(255, 243, 230));
    }
    if (receta.pasos() != null && !receta.pasos().isBlank()) {
      agregarSeccion(doc, receta.tituloPasos() != null ? receta.tituloPasos() : "Preparación", receta.pasos(), new Color(245, 255, 245));
    }
    if (receta.observaciones() != null && !receta.observaciones().isBlank()) {
      agregarSeccion(doc, receta.tituloObservaciones() != null ? receta.tituloObservaciones() : "Notas", receta.observaciones(), new Color(255, 250, 240));
    }
  }

  private void agregarSeccion(Document doc, String titulo, String contenido, Color bgColor) throws DocumentException {
    PdfPTable table = new PdfPTable(1);
    table.setWidthPercentage(100);
    table.setSpacingAfter(14);

    Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
    PdfPCell titleCell = new PdfPCell(new Phrase(normalizarTextoPdf(titulo).toUpperCase(), fontTitle));
    titleCell.setBackgroundColor(COLOR_PRIMARY);
    titleCell.setPadding(8);
    titleCell.setBorder(Rectangle.NO_BORDER);
    table.addCell(titleCell);

    Font fontContent = FontFactory.getFont(FontFactory.HELVETICA, 10, COLOR_TEXT);
    PdfPCell contentCell = new PdfPCell();
    contentCell.setBackgroundColor(bgColor);
    contentCell.setPadding(11);
    contentCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
    contentCell.setBorderColor(COLOR_SECONDARY);
    contentCell.setBorderWidth(1);

    Paragraph content = new Paragraph(normalizarTextoPdf(contenido), fontContent);
    content.setLeading(13);
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
    Paragraph spacer = new Paragraph(" ");
    spacer.setSpacingBefore(24);
    doc.add(spacer);

    PdfPTable lineTable = new PdfPTable(1);
    lineTable.setWidthPercentage(100);
    PdfPCell lineCell = new PdfPCell();
    lineCell.setBorder(Rectangle.TOP);
    lineCell.setBorderColor(COLOR_LIGHT);
    lineCell.setBorderWidth(1);
    lineCell.setPaddingTop(10);
    lineTable.addCell(lineCell);
    doc.add(lineTable);

    Font fontFooter = FontFactory.getFont(FontFactory.HELVETICA, 8, COLOR_PRIMARY);
    Paragraph footer = new Paragraph("Documento interno generado por Pastelería Artesanal · Casa de Producción", fontFooter);
    footer.setAlignment(Element.ALIGN_CENTER);
    footer.setSpacingBefore(10);
    doc.add(footer);

    Paragraph date = new Paragraph("Fecha: " + new Date(), fontFooter);
    date.setAlignment(Element.ALIGN_CENTER);
    doc.add(date);
  }

  private String normalizarTextoPdf(String value) {
    if (value == null) {
      return "";
    }
    return value
        .replace("•", "-")
        .replace("\uFFFD", "")
        .replace("\r\n", "\n")
        .replace("\r", "\n");
  }

  private Image cargarImagenProducto(String imagePath) {
    if (imagePath == null || imagePath.isBlank()) {
      return null;
    }
    String normalized = imagePath.startsWith("/") ? imagePath.substring(1) : imagePath;
    if (normalized.startsWith("assets/")) {
      normalized = "storage/" + normalized;
    }
    return cargarImagen(Path.of(normalized));
  }

  private Image cargarImagen(Path path) {
    try {
      Path absolute = path.toAbsolutePath().normalize();
      if (!Files.exists(absolute)) {
        return null;
      }
      return Image.getInstance(absolute.toString());
    } catch (Exception ignored) {
      return null;
    }
  }

  private boolean tieneContenido(RecetaJsonDto receta) {
    return (receta.titulo() != null && !receta.titulo().isBlank())
        || (receta.ingredientes() != null && !receta.ingredientes().isBlank())
        || (receta.pasos() != null && !receta.pasos().isBlank())
        || (receta.observaciones() != null && !receta.observaciones().isBlank());
  }
}
