package com.pasteleria.reportes.application;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

/**
 * Construye documentos PDF sencillos pero presentables para los reportes del
 * tablero administrativo.
 */
@Service
public class ReportPdfDocumentService {

  private static final Locale REPORT_LOCALE = Locale.US;
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18f, new Color(59, 35, 28));
  private static final Font SUBTITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10f, new Color(132, 91, 69));
  private static final Font SECTION_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11f, new Color(76, 41, 29));
  private static final Font BODY_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10.5f, new Color(58, 47, 43));
  private static final Font SMALL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 8.5f, new Color(112, 93, 82));
  private static final Font TABLE_HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9.5f, Color.WHITE);

  public byte[] buildBusinessSummaryPdf(BusinessSummaryData data) {
    return buildDocument("Resumen de negocio", document -> {
      addSummaryTable(document, List.of(
          new SummaryItem("Clientes registrados", String.valueOf(data.clientCount())),
          new SummaryItem("Productos publicados", String.valueOf(data.publishedProductCount())),
          new SummaryItem("Cotizaciones registradas", String.valueOf(data.quotationCount())),
          new SummaryItem("Pedidos registrados", String.valueOf(data.orderCount())),
          new SummaryItem("Produccion activa", String.valueOf(data.activeProductionCount())),
          new SummaryItem("Facturacion cerrada", formatMoney(data.deliveredRevenue()))
      ));

      addSectionTitle(document, "Pedidos recientes");
      PdfPTable table = createTable(new float[]{2.3f, 2.8f, 1.6f, 1.5f});
      addHeaderRow(table, List.of("Codigo", "Cliente", "Estado", "Total"));

      if (data.recentOrders().isEmpty()) {
        addEmptyRow(table, "No hay pedidos suficientes para poblar este corte.");
      } else {
        data.recentOrders().forEach(order -> {
          addBodyCell(table, order.code());
          addBodyCell(table, order.clientName());
          addBodyCell(table, order.status());
          addBodyCell(table, formatMoney(order.total()));
        });
      }

      document.add(table);
    }, data.generatedAt());
  }

  public byte[] buildProductionQueuePdf(ProductionQueueData data) {
    return buildDocument("Cola de produccion", document -> {
      addSummaryTable(document, List.of(
          new SummaryItem("Frentes en curso", String.valueOf(data.items().size())),
          new SummaryItem("Urgentes", String.valueOf(data.items().stream().filter(item -> "URGENTE".equals(item.priority())).count())),
          new SummaryItem("Pendientes", String.valueOf(data.items().stream().filter(item -> "PENDIENTE".equals(item.status())).count())),
          new SummaryItem("En decoracion/empaque", String.valueOf(data.items().stream().filter(item -> "DECORACION".equals(item.status()) || "EMPAQUE".equals(item.status())).count()))
      ));

      addSectionTitle(document, "Detalle operativo");
      PdfPTable table = createTable(new float[]{2.1f, 2.5f, 1.6f, 1.5f, 2.6f});
      addHeaderRow(table, List.of("Pedido", "Cliente", "Etapa", "Prioridad", "Observacion"));

      if (data.items().isEmpty()) {
        addEmptyRow(table, "No hay trabajos activos en la cola de produccion.");
      } else {
        data.items().forEach(item -> {
          addBodyCell(table, item.orderCode());
          addBodyCell(table, item.clientName());
          addBodyCell(table, item.status());
          addBodyCell(table, item.priority());
          addBodyCell(table, item.productionNotes());
        });
      }

      document.add(table);
    }, data.generatedAt());
  }

  private byte[] buildDocument(String title, DocumentConsumer consumer, OffsetDateTime generatedAt) {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      Document document = new Document(PageSize.A4, 42, 42, 58, 48);
      PdfWriter writer = PdfWriter.getInstance(document, outputStream);
      writer.setPageEvent(new FooterPageEvent());
      document.open();

      addHeader(document, title, generatedAt);
      consumer.accept(document);

      Paragraph footer = new Paragraph(
          "Documento interno de Pasteleria Artesanal. Uso administrativo y de seguimiento operativo.",
          SMALL_FONT
      );
      footer.setSpacingBefore(18f);
      document.add(footer);
      document.close();
      return outputStream.toByteArray();
    } catch (Exception exception) {
      throw new IllegalStateException("No se pudo construir el PDF del reporte.", exception);
    }
  }

  private void addHeader(Document document, String title, OffsetDateTime generatedAt) throws DocumentException {
    Paragraph kicker = new Paragraph("PASTELERIA ARTESANAL", SUBTITLE_FONT);
    kicker.setSpacingAfter(4f);
    document.add(kicker);

    Paragraph heading = new Paragraph(title, TITLE_FONT);
    heading.setSpacingAfter(6f);
    document.add(heading);

    Paragraph metadata = new Paragraph(
        "Generado el " + DATE_TIME_FORMATTER.format(generatedAt) + " · Centro de reportes administrativo",
        SUBTITLE_FONT
    );
    metadata.setSpacingAfter(18f);
    document.add(metadata);
  }

  private void addSectionTitle(Document document, String text) throws DocumentException {
    Paragraph heading = new Paragraph(text, SECTION_FONT);
    heading.setSpacingBefore(10f);
    heading.setSpacingAfter(8f);
    document.add(heading);
  }

  private void addSummaryTable(Document document, List<SummaryItem> items) throws DocumentException {
    PdfPTable table = createTable(new float[]{2.3f, 1.7f});
    table.setSpacingAfter(12f);

    items.forEach(item -> {
      PdfPCell labelCell = new PdfPCell(new Phrase(item.label(), BODY_FONT));
      labelCell.setPadding(8f);
      labelCell.setBorderColor(new Color(225, 210, 200));
      labelCell.setBackgroundColor(new Color(252, 247, 242));

      PdfPCell valueCell = new PdfPCell(new Phrase(item.value(), SECTION_FONT));
      valueCell.setPadding(8f);
      valueCell.setBorderColor(new Color(225, 210, 200));
      valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
      valueCell.setBackgroundColor(Color.WHITE);

      table.addCell(labelCell);
      table.addCell(valueCell);
    });

    document.add(table);
  }

  private PdfPTable createTable(float[] widths) throws DocumentException {
    PdfPTable table = new PdfPTable(widths);
    table.setWidthPercentage(100f);
    table.setSpacingBefore(4f);
    return table;
  }

  private void addHeaderRow(PdfPTable table, List<String> headings) {
    headings.forEach(heading -> {
      PdfPCell cell = new PdfPCell(new Phrase(heading, TABLE_HEADER_FONT));
      cell.setPadding(8f);
      cell.setBackgroundColor(new Color(106, 55, 38));
      cell.setBorderColor(new Color(106, 55, 38));
      table.addCell(cell);
    });
  }

  private void addBodyCell(PdfPTable table, String value) {
    PdfPCell cell = new PdfPCell(new Phrase(value == null || value.isBlank() ? "—" : value, BODY_FONT));
    cell.setPadding(7f);
    cell.setBorderColor(new Color(232, 223, 216));
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    table.addCell(cell);
  }

  private void addEmptyRow(PdfPTable table, String message) {
    PdfPCell cell = new PdfPCell(new Phrase(message, BODY_FONT));
    cell.setColspan(table.getNumberOfColumns());
    cell.setPadding(10f);
    cell.setBorderColor(new Color(232, 223, 216));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    table.addCell(cell);
  }

  private String formatMoney(BigDecimal amount) {
    return NumberFormat.getCurrencyInstance(REPORT_LOCALE).format(amount == null ? BigDecimal.ZERO : amount);
  }

  @FunctionalInterface
  private interface DocumentConsumer {
    void accept(Document document) throws DocumentException;
  }

  private record SummaryItem(String label, String value) {
  }

  private static final class FooterPageEvent extends PdfPageEventHelper {
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
      Rectangle bounds = document.getPageSize();
      Phrase footer = new Phrase(
          "Pasteleria Artesanal · Reporte interno · Pagina " + writer.getPageNumber(),
          SMALL_FONT
      );
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

  public record BusinessSummaryData(
      OffsetDateTime generatedAt,
      long clientCount,
      long publishedProductCount,
      int quotationCount,
      int orderCount,
      long activeProductionCount,
      BigDecimal deliveredRevenue,
      List<RecentOrderItem> recentOrders
  ) {
  }

  public record RecentOrderItem(
      String code,
      String clientName,
      String status,
      BigDecimal total
  ) {
  }

  public record ProductionQueueData(
      OffsetDateTime generatedAt,
      List<ProductionQueueItem> items
  ) {
  }

  public record ProductionQueueItem(
      String orderCode,
      String clientName,
      String status,
      String priority,
      String productionNotes
  ) {
  }
}
