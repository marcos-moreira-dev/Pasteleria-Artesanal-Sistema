package com.pasteleria.casosuso.application;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Rectangle;
import com.pasteleria.casosuso.api.dto.CasoUsoHubResponse;
import com.pasteleria.casosuso.api.dto.CasoUsoModuloResponse;
import com.pasteleria.casosuso.api.dto.CasoUsoOperativoResponse;
import com.pasteleria.casosuso.api.dto.PasoCasoUsoResponse;
import com.pasteleria.common.pdf.PdfWhiteBackgroundPageEvent;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Genera el manual PDF de la Guía Operativa a partir del mismo catálogo vivo
 * expuesto por /api/v1/casos-uso/hub.
 *
 * <p>La intención es evitar manuales paralelos: si cambia el seed/base de casos
 * de uso, el PDF refleja el catálogo vigente. El diseño visual respeta la línea
 * sobria de la pastelería y reutiliza la defensa técnica de PDFs con fondo blanco.</p>
 */
@Service
public class CasoUsoManualPdfDocumentService {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  private static final Color BRAND = new Color(89, 45, 32);
  private static final Color BRAND_DARK = new Color(48, 26, 20);
  private static final Color MUTED = new Color(117, 91, 79);
  private static final Color BORDER = new Color(230, 216, 205);
  private static final Color SOFT = new Color(252, 247, 242);

  private static final Font KICKER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9f, MUTED);
  private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20f, BRAND_DARK);
  private static final Font SUBTITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10.5f, MUTED);
  private static final Font SECTION_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13f, BRAND_DARK);
  private static final Font CASE_TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11.5f, BRAND_DARK);
  private static final Font BODY_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10f, new Color(60, 47, 40));
  private static final Font SMALL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 8.5f, MUTED);
  private static final Font CODE_FONT = FontFactory.getFont(FontFactory.COURIER_BOLD, 8.5f, BRAND);

  public byte[] generarManual(CasoUsoHubResponse hub) {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      Document document = new Document(PageSize.A4, 42, 42, 58, 48);
      PdfWriter writer = PdfWriter.getInstance(document, outputStream);
      writer.setPageEvent(new FooterPageEvent());
      document.open();

      addCover(document, hub);
      addHowToUse(document);
      addModuleIndex(document, hub.modulos());
      for (CasoUsoModuloResponse modulo : hub.modulos()) {
        addModule(document, modulo);
      }
      addClosingNote(document);

      document.close();
      return outputStream.toByteArray();
    } catch (Exception exception) {
      throw new IllegalStateException("No se pudo generar el manual PDF de la guía operativa.", exception);
    }
  }

  private void addCover(Document document, CasoUsoHubResponse hub) throws DocumentException {
    Paragraph kicker = new Paragraph("PASTELERÍA ARTESANAL · GUÍA OPERATIVA", KICKER_FONT);
    kicker.setSpacingAfter(6f);
    document.add(kicker);

    Paragraph title = new Paragraph("Manual operativo de casos de uso", TITLE_FONT);
    title.setSpacingAfter(8f);
    document.add(title);

    Paragraph description = new Paragraph(
        "Documento interno para orientar al equipo en los flujos principales del sistema administrativo. "
            + "La guía se genera desde el catálogo vivo de casos de uso y no reemplaza la validación humana de operaciones críticas.",
        SUBTITLE_FONT
    );
    description.setSpacingAfter(18f);
    document.add(description);

    PdfPTable summary = table(new float[]{2.2f, 1f});
    addSummaryRow(summary, "Casos activos", String.valueOf(hub.totalCasos()));
    addSummaryRow(summary, "Áreas documentadas", String.valueOf(hub.modulos().size()));
    addSummaryRow(summary, "Fecha de generación", DATE_FORMATTER.format(OffsetDateTime.now()));
    document.add(summary);

    Paragraph warning = new Paragraph(
        "Uso recomendado: leer el objetivo, abrir el módulo indicado en el sistema, seguir los pasos y comprobar el resultado esperado.",
        BODY_FONT
    );
    warning.setSpacingBefore(16f);
    document.add(warning);
  }

  private void addHowToUse(Document document) throws DocumentException {
    addSectionTitle(document, "Cómo usar este manual");
    addBullet(document, "Primero ubica el área de trabajo: clientes, productos, cotizaciones, pedidos, producción, abastecimiento, reportes o administración.");
    addBullet(document, "Después abre el caso de uso por código y título. Cada caso indica responsable, objetivo, punto de inicio y pasos.");
    addBullet(document, "Si un flujo toca caja, inventario, producción, contabilidad o fiscalidad, verifica el dato antes de cerrar la operación.");
    addBullet(document, "Si una pantalla cambia en futuras tandas, este manual debe actualizarse desde el seed/catálogo canónico.");
  }

  private void addModuleIndex(Document document, List<CasoUsoModuloResponse> modulos) throws DocumentException {
    addSectionTitle(document, "Índice por áreas");
    PdfPTable index = table(new float[]{1.2f, 2.4f, 0.7f});
    addHeader(index, List.of("Código", "Área", "Casos"));
    for (CasoUsoModuloResponse modulo : modulos) {
      addCell(index, modulo.codigo(), CODE_FONT);
      addCell(index, modulo.nombre(), BODY_FONT);
      addCell(index, String.valueOf(modulo.casos().size()), BODY_FONT, Element.ALIGN_RIGHT);
    }
    document.add(index);
  }

  private void addModule(Document document, CasoUsoModuloResponse modulo) throws DocumentException {
    addSectionTitle(document, modulo.nombre());
    if (modulo.descripcion() != null && !modulo.descripcion().isBlank()) {
      Paragraph description = new Paragraph(modulo.descripcion(), SUBTITLE_FONT);
      description.setSpacingAfter(8f);
      document.add(description);
    }

    for (CasoUsoOperativoResponse caso : modulo.casos()) {
      addCase(document, caso);
    }
  }

  private void addCase(Document document, CasoUsoOperativoResponse caso) throws DocumentException {
    PdfPTable wrapper = table(new float[]{1f});
    wrapper.setSpacingBefore(7f);
    wrapper.setSpacingAfter(8f);

    PdfPCell cell = new PdfPCell();
    cell.setBorderColor(BORDER);
    cell.setBackgroundColor(Color.WHITE);
    cell.setPadding(10f);

    Paragraph code = new Paragraph(caso.codigo(), CODE_FONT);
    code.setSpacingAfter(3f);
    cell.addElement(code);

    Paragraph title = new Paragraph(caso.titulo(), CASE_TITLE_FONT);
    title.setSpacingAfter(6f);
    cell.addElement(title);

    addInlineMeta(cell, "Responsable", emptyDefault(caso.actorPrincipal(), "Equipo autorizado"));
    addInlineMeta(cell, "Punto de inicio", emptyDefault(caso.puntoInicio(), "Módulo correspondiente"));
    addInlineMeta(cell, "Objetivo", emptyDefault(caso.objetivo(), "Guiar la ejecución ordenada del flujo."));

    if (caso.pasos() == null || caso.pasos().isEmpty()) {
      Paragraph empty = new Paragraph("Este caso aún no tiene pasos cargados.", SMALL_FONT);
      empty.setSpacingBefore(5f);
      cell.addElement(empty);
    } else {
      Paragraph stepTitle = new Paragraph("Pasos", KICKER_FONT);
      stepTitle.setSpacingBefore(7f);
      stepTitle.setSpacingAfter(3f);
      cell.addElement(stepTitle);
      for (PasoCasoUsoResponse paso : caso.pasos()) {
        Paragraph step = new Paragraph(paso.numero() + ". " + paso.descripcion(), BODY_FONT);
        step.setSpacingAfter(3f);
        cell.addElement(step);
      }
    }

    wrapper.addCell(cell);
    document.add(wrapper);
  }

  private void addClosingNote(Document document) throws DocumentException {
    Paragraph note = new Paragraph(
        "Fin del manual. Si se detecta un procedimiento incompleto, corregir primero el catálogo canónico de casos de uso y luego volver a generar este PDF.",
        SMALL_FONT
    );
    note.setSpacingBefore(14f);
    document.add(note);
  }

  private void addSectionTitle(Document document, String text) throws DocumentException {
    Paragraph section = new Paragraph(text, SECTION_FONT);
    section.setSpacingBefore(16f);
    section.setSpacingAfter(8f);
    document.add(section);
  }

  private void addBullet(Document document, String text) throws DocumentException {
    Paragraph bullet = new Paragraph("• " + text, BODY_FONT);
    bullet.setSpacingAfter(4f);
    document.add(bullet);
  }

  private PdfPTable table(float[] widths) throws DocumentException {
    PdfPTable table = new PdfPTable(widths);
    table.setWidthPercentage(100f);
    table.setSpacingAfter(10f);
    return table;
  }

  private void addSummaryRow(PdfPTable table, String label, String value) {
    PdfPCell labelCell = new PdfPCell(new Phrase(label, BODY_FONT));
    labelCell.setPadding(8f);
    labelCell.setBorderColor(BORDER);
    labelCell.setBackgroundColor(SOFT);

    PdfPCell valueCell = new PdfPCell(new Phrase(value, CASE_TITLE_FONT));
    valueCell.setPadding(8f);
    valueCell.setBorderColor(BORDER);
    valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    table.addCell(labelCell);
    table.addCell(valueCell);
  }

  private void addHeader(PdfPTable table, List<String> headings) {
    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9f, Color.WHITE);
    for (String heading : headings) {
      PdfPCell cell = new PdfPCell(new Phrase(heading, headerFont));
      cell.setPadding(7f);
      cell.setBorderColor(BRAND);
      cell.setBackgroundColor(BRAND);
      table.addCell(cell);
    }
  }

  private void addCell(PdfPTable table, String value, Font font) {
    addCell(table, value, font, Element.ALIGN_LEFT);
  }

  private void addCell(PdfPTable table, String value, Font font, int alignment) {
    PdfPCell cell = new PdfPCell(new Phrase(emptyDefault(value, "—"), font));
    cell.setPadding(7f);
    cell.setBorderColor(BORDER);
    cell.setHorizontalAlignment(alignment);
    table.addCell(cell);
  }

  private void addInlineMeta(PdfPCell cell, String label, String value) {
    Paragraph paragraph = new Paragraph(label + ": " + value, BODY_FONT);
    paragraph.setSpacingAfter(3f);
    cell.addElement(paragraph);
  }

  private String emptyDefault(String value, String fallback) {
    return value == null || value.isBlank() ? fallback : value;
  }

  private static final class FooterPageEvent extends PdfWhiteBackgroundPageEvent {
    private static final Font FOOTER_FONT = FontFactory.getFont(FontFactory.HELVETICA, 8f, MUTED);

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
      super.onEndPage(writer, document);
      Rectangle bounds = document.getPageSize();
      Phrase footer = new Phrase("Guía Operativa · Pastelería Artesanal · Página " + writer.getPageNumber(), FOOTER_FONT);
      ColumnText.showTextAligned(
          writer.getDirectContent(),
          Element.ALIGN_CENTER,
          footer,
          (bounds.getLeft() + bounds.getRight()) / 2,
          bounds.getBottom() + 18,
          0
      );
    }
  }
}
