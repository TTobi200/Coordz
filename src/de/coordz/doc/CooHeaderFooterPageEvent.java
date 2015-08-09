package de.coordz.doc;
import java.time.LocalDate;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class CooHeaderFooterPageEvent extends PdfPageEventHelper
{
	@Override
	public void onStartPage(PdfWriter writer, Document document)
	{
		ColumnText.showTextAligned(writer.getDirectContent(),
			Element.ALIGN_CENTER, new Phrase(""), 30, 800, 0);
		ColumnText.showTextAligned(writer.getDirectContent(),
			Element.ALIGN_CENTER, new Phrase(String.valueOf(LocalDate.now())), 550, 800, 0);
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document)
	{
		ColumnText.showTextAligned(writer.getDirectContent(),
			Element.ALIGN_CENTER, new Phrase(
				"Erstellt mit Coordz © 2015"), 110, 20, 0);
		ColumnText.showTextAligned(writer.getDirectContent(),
			Element.ALIGN_CENTER,
			new Phrase("Seite " + document.getPageNumber()), 550, 20, 0);
	}
}