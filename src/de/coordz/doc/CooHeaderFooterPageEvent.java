package de.coordz.doc;

import java.time.LocalDate;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class CooHeaderFooterPageEvent extends PdfPageEventHelper
{
	/** The header text. */
	protected String header;
	/** The template with the total number of pages. */
	protected PdfTemplate total;
	/** Flag if document has title page */
	protected boolean hasTitlePage;

	public CooHeaderFooterPageEvent(boolean hasTitlePage)
	{
		this.hasTitlePage = hasTitlePage;
	}

	@Override
	public void onStartPage(PdfWriter writer, Document document)
	{
		if(!(hasTitlePage && document.getPageNumber() == 1))
		{
			PdfPTable table = new PdfPTable(3);
			try
			{
				table.setWidths(new int[] { 24, 24, 2 });
				table.setTotalWidth(527);
				table.setLockedWidth(true);
				table.getDefaultCell().setFixedHeight(20);
				table.getDefaultCell().setBorder(Rectangle.BOTTOM);
				table.addCell(header);
				table.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_RIGHT);
				table.addCell(String.valueOf(LocalDate.now()));
				PdfPCell cell = new PdfPCell();
				cell.setBorder(Rectangle.BOTTOM);
				table.addCell(cell);
				table.writeSelectedRows(0, -1, 34, 803,
					writer.getDirectContent());
			}
			catch(DocumentException de)
			{
				throw new ExceptionConverter(de);
			}
		}
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document)
	{
		if(!(hasTitlePage && document.getPageNumber() == 1))
		{
			PdfPTable table = new PdfPTable(3);
			try
			{
				table.setWidths(new int[] { 24, 24, 2 });
				table.setTotalWidth(527);
				table.setLockedWidth(true);
				table.getDefaultCell().setFixedHeight(20);
				table.getDefaultCell().setBorder(Rectangle.TOP);
				table.addCell("Erstellt mit Coordz © 2015");
				table.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_RIGHT);
				table.addCell(String.format("Seite %d von",
					writer.getPageNumber()));
				PdfPCell cell = new PdfPCell(Image.getInstance(total));
				cell.setBorder(Rectangle.TOP);
				table.addCell(cell);
				table.writeSelectedRows(0, -1, 34, 30,
					writer.getDirectContent());
			}
			catch(DocumentException de)
			{
				throw new ExceptionConverter(de);
			}
		}
	}
	
	/**
	 * Creates the PdfTemplate that will hold the total number of pages.
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	@Override
	public void onOpenDocument(PdfWriter writer, Document document)
	{
		total = writer.getDirectContent().createTemplate(30, 16);
	}

	/**
	 * Fills out the total number of pages before the document is closed.
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	@Override
	public void onCloseDocument(PdfWriter writer, Document document)
	{
		ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
			new Phrase(String.valueOf(writer.getPageNumber() - 1)),
			2, 2, 0);
	}

	/**
	 * Allows us to change the content of the header.
	 * @param header The new header String
	 */
	public void setHeader(String header)
	{
		this.header = header;
	}
}