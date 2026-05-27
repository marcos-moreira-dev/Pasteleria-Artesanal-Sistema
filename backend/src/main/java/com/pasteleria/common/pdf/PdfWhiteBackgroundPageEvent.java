package com.pasteleria.common.pdf;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;

/**
 * Fuerza fondo blanco en cada pagina PDF.
 *
 * <p>Algunos visores o navegadores en modo oscuro muestran fondos raros cuando
 * el PDF no pinta explicitamente la pagina. Este evento mantiene los PDFs de la
 * pasteleria legibles y consistentes.</p>
 */
public class PdfWhiteBackgroundPageEvent extends PdfPageEventHelper {

  @Override
  public void onEndPage(PdfWriter writer, com.lowagie.text.Document document) {
    Rectangle pageSize = document.getPageSize();
    var canvas = writer.getDirectContentUnder();
    canvas.saveState();
    canvas.setColorFill(Color.WHITE);
    canvas.rectangle(
        pageSize.getLeft(),
        pageSize.getBottom(),
        pageSize.getRight() - pageSize.getLeft(),
        pageSize.getTop() - pageSize.getBottom()
    );
    canvas.fill();
    canvas.restoreState();
  }
}
