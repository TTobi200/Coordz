/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.doc;

import java.io.*;
import java.util.*;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;

import javax.imageio.ImageIO;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import de.coordz.data.*;
import de.coordz.data.base.*;
import de.util.CooXMLDBUtil;
import de.util.log.CooLog;

public class CooPdfDocument extends CooDocument
{
	protected int chapterIdx = 1;
	protected CooHeaderFooterPageEvent pageEvent;

	protected void addCustomer(Document doc, CooCustomer customer)
		throws DocumentException
	{
		Chapter chapter = addChapter(doc, "Kunde "
					+ customer.nameProperty().get());

		doc.add(new Paragraph("Name:" + customer.nameProperty().get()));
		doc.add(new Paragraph("Straﬂe:" + customer.streetProperty().get()));
		doc.add(new Paragraph("PLZ:" + customer.plzProperty().get()));
		doc.add(new Paragraph("Ort:" + customer.locationProperty().get()));

		for(Content c : getContent())
		{
			switch(c)
			{
				case CONTACTS:
					addContacts(doc, chapter, customer);
					break;
				case PALETS:
					addPalets(doc, chapter, customer);
					break;
				default:
					break;
			}
		}
	}

	protected void addProject(Document doc, CooProject project)
		throws DocumentException
	{
		Chapter chapter = addChapter(doc, "Projekt "
						+ project.nameProperty().get());
		doc.add(new Paragraph("Name:" + project.nameProperty().get()));

		for(Content c : getContent())
		{
			switch(c)
			{
				case LAP_SOFTWARE:
					addLAPSoftware(doc, chapter, project.lapSoftwareProperty()
						.get());
					break;
				case STATIONS:
					addStations(doc, chapter, project.getStations());
					break;
				default:
					break;
			}
		}
	}

	protected void addStations(Document doc, Chapter chapter,
					ObservableList<CooStation> stations)
		throws DocumentException
	{
		// Chapter chapter = addChapter(doc, "Stationen");
		// Section section = addCaption(doc, chapter, "Stationen");

		for(CooStation s : stations)
		{
			// addSubCaption(doc, "Station " + s.nameProperty().get());
			Section sec = addCaption(doc, chapter, "Station "
													+ s.nameProperty().get());
			// Section sec = addSubCaption(doc, section, "Station " +
			// s.nameProperty().get());

			addField(doc, sec, "Name", s.nameProperty().get());
			addField(doc, sec, "Datei", s.fileProperty().get());
			addField(doc, sec, "X-Offset",
				String.valueOf(s.xOffsetProperty().get()));
			addField(doc, sec, "Y-Offset",
				String.valueOf(s.yOffsetProperty().get()));
			addField(doc, sec, "Z-Offset",
				String.valueOf(s.zOffsetProperty().get()));
			doc.add(sec);

			for(int j = 0; j < getContent().size(); j++)
			{
				switch(getContent().get(j))
				{
					case GATEWAY:
						addGateway(doc, sec, s.gatewayProperty().get());
						break;
					case TOTALSTATION:
						addTotalStation(doc, sec, s.totalStationProperty()
							.get());
						break;
					case MEASUREMENTS:
						addMeasurements(doc, sec, s.getMeasurements());
						break;
					case VERIFY_MEASUREMENT:
						addVerifyMeasurement(doc, sec,
							s.verifyMeasurementProperty()
								.get());
						break;
					case REGION_DIVIDING:
						addRegionDividing(doc, sec,
							s.regionDevidingProperty().get());
						break;
					default:
						break;
				}
			}
		}
	}

	protected void addRegionDividing(Document doc, Section parent,
					CooRegionDividing regionDividing)
		throws DocumentException
	{
		Section section = addSubCaption(doc, parent, "Bereichsaufteilung");
		section.add(new Paragraph("Die sogennate Bereichsaufteilung wird in der "
						+ "LAP-Software hinterlegt. Sie gibt an, welcher Laser "
						+ "bis zu welchen Koordinaten projizieren soll. Somit erh‰lt "
						+ "jeder Laser im System seinen eigenen Bereich, was zu "
						+ "einem verbessertem Laserbild f¸hrt."));
		doc.add(section);

		for(Content c : getContent())
		{
			switch(c)
			{
				case LASER:
					addLaser(doc, regionDividing.getLaser());
					break;
				default:
					break;
			}
		}
	}

	protected void addMeasurements(Document doc, Section parent,
					ObservableList<CooMeasurement> measurements)
		throws DocumentException
	{
		for(CooMeasurement m : measurements)
		{
			// addSubCaption(doc, "Messung " + m.nameProperty().get());
			Section sec = addSubCaption(doc, parent, "Messung "
								+ m.nameProperty().get());
			// Section sec = addSubCaption(doc, section, "Messung " +
			// m.nameProperty().get());

			addField(doc, sec, "Name", m.nameProperty().get());
			addField(doc, sec, "Datum", String.valueOf(m.dateProperty().get()));
			addField(doc, sec, "Von", m.fromProperty().get());
			addField(doc, sec, "Bis", m.toProperty().get());
			addField(doc, sec, "Wetter", m.weatherProperty().get());
			doc.add(sec);

			for(int j = 0; j < getContent().size(); j++)
			{
				switch(getContent().get(j))
				{
					case RETICLES:
						addReticles(doc, m.getReticles());
						break;
					case TARGETS:
						addTargets(doc, m.getTargets());
						break;
					default:
						break;
				}
			}
			
		}
	}

	protected void addVerifyMeasurement(Document doc, Section parent,
					CooVerifyMeasurement verifyMeasurement)
		throws DocumentException
	{
		Section section = addSubCaption(doc, parent, "Kontroll Messung");
		section.add(new Paragraph("Die Kontroll Messung dient zur ‹berpr¸fung der "
						+ "korrekten Einmessung des Laser-Systems. Die Vorgabe sind "
						+ "Daten aus dem CAD-System des Kunden und das Ergebnis "
						+ "sind die dann tats‰chlich gemessenen Werte in der Realit‰t."));
		section.add(new Paragraph("Hierbei ist eine Abweichung im Milimeterbereich nicht weiter tragisch."));
		doc.add(section);

		for(Content c : getContent())
		{
			switch(c)
			{
				case RESULT:
					addResut(doc, verifyMeasurement.getResult());
					break;
				case SPECIFICATION:
					addSpecification(doc, verifyMeasurement.getSpecification());
					break;
				default:
					break;
			}
		}
	}

	protected void addSpecification(Document doc,
					ObservableList<CooRectangle> specification)
		throws DocumentException
	{
		// addSubCaption(doc, "Vorgabe");
		PdfPTable table = createTable("Vorgabe",
			"Name", "L‰nge", "Breite", "Hˆhe", "X", "Y", "Z", "D1", "D2");

		specification.forEach(s ->
		{
			table.addCell(s.nameProperty().get());
			table.addCell(String.valueOf(s.lengthProperty().get()));
			table.addCell(String.valueOf(s.widthProperty().get()));
			table.addCell(String.valueOf(s.heightProperty().get()));
			table.addCell(String.valueOf(s.xProperty().get()));
			table.addCell(String.valueOf(s.yProperty().get()));
			table.addCell(String.valueOf(s.zProperty().get()));
			table.addCell(String.valueOf(s.d1Property().get()));
			table.addCell(String.valueOf(s.d2Property().get()));
		});

		addTable(doc, table, 90, 70, 70, 70, 40, 40, 40, 40, 40);
	}

	protected void addResut(Document doc,
					ObservableList<CooRectangle> results)
		throws DocumentException
	{
		// addSubCaption(doc, "Ergebnis");
		PdfPTable table = createTable("Ergebnis",
			"Name", "L‰nge", "Breite", "Hˆhe", "X", "Y", "Z", "D1", "D2");

		results.forEach(r ->
		{
			table.addCell(r.nameProperty().get());
			table.addCell(String.valueOf(r.lengthProperty().get()));
			table.addCell(String.valueOf(r.widthProperty().get()));
			table.addCell(String.valueOf(r.heightProperty().get()));
			table.addCell(String.valueOf(r.xProperty().get()));
			table.addCell(String.valueOf(r.yProperty().get()));
			table.addCell(String.valueOf(r.zProperty().get()));
			table.addCell(String.valueOf(r.d1Property().get()));
			table.addCell(String.valueOf(r.d2Property().get()));
		});

		addTable(doc, table, 90, 70, 70, 70, 40, 40, 40, 40, 40);
	}

	protected void addReticles(Document doc,
					ObservableList<CooReticle> reticles)
		throws DocumentException
	{
		// addSubCaption(doc, "Zielmarken");
		PdfPTable table = createTable("Zielmarken",
			"Name", "X", "Y", "Z");

		reticles.forEach(r ->
		{
			table.addCell(r.nameProperty().get());
			table.addCell(String.valueOf(r.xProperty().get()));
			table.addCell(String.valueOf(r.yProperty().get()));
			table.addCell(String.valueOf(r.zProperty().get()));
		});
		
		addTable(doc, table, 200, 100, 100, 100);
	}

	protected void addTargets(Document doc,
					ObservableList<CooTarget> targets)
		throws DocumentException
	{
		// addSubCaption(doc, "Targets");
		PdfPTable table = createTable("Targets",
			"Name", "X", "Y", "Z");

		targets.forEach(t ->
		{
			table.addCell(t.nameProperty().get());
			table.addCell(String.valueOf(t.xProperty().get()));
			table.addCell(String.valueOf(t.yProperty().get()));
			table.addCell(String.valueOf(t.zProperty().get()));
		});

		addTable(doc, table, 200, 100, 100, 100);
	}

	protected void addGateway(Document doc, Section parent,
					CooGateway gateway)
		throws DocumentException
	{
		Section section = addSubCaption(doc, parent, "Gateway");

		section.add(new Paragraph("Ip:" + gateway.ipProperty().get()));
		section.add(new Paragraph("MAC:" + gateway.macProperty().get()));

		doc.add(section);
	}

	protected void addLAPSoftware(Document doc, Chapter chapter,
					CooLAPSoftware lapSoftware)
		throws DocumentException
	{
		Section section = addCaption(doc, chapter, "LAP-Software");

		section.add(new Paragraph("Name:" + lapSoftware.nameProperty().get()));
		section.add(new Paragraph("Version:"
									+ lapSoftware.versionProperty().get()));

		doc.add(section);
	}

	protected void addTotalStation(Document doc, Section parent,
					CooTotalstation totalStation)
		throws DocumentException
	{
		Section section = addSubCaption(doc, parent, "Totalstation");

		section.add(new Paragraph("X:" + totalStation.xProperty().get()));
		section.add(new Paragraph("Y:" + totalStation.yProperty().get()));
		section.add(new Paragraph("Z:" + totalStation.zProperty().get()));
		section.add(new Paragraph("Delta-X:"
									+ totalStation.deltaXProperty().get()));
		section.add(new Paragraph("Delta-Y:"
									+ totalStation.deltaYProperty().get()));

		doc.add(section);
	}

	protected void addLaser(Document doc,
					ObservableList<CooLaser> laser)
		throws DocumentException
	{
		// addSubCaption(doc, "Laser");
		PdfPTable table = createTable("Laser",
			"Name", "MAC", "Seriennummer", "X", "Y", "Z", "Gesamtabweichung");

		laser.forEach(l ->
		{
			table.addCell(l.nameProperty().get());
			table.addCell(l.macProperty().get());
			table.addCell(l.serialNrProperty().get());
			table.addCell(String.valueOf(l.xProperty().get()));
			table.addCell(String.valueOf(l.yProperty().get()));
			table.addCell(String.valueOf(l.zProperty().get()));
			table.addCell(String.valueOf(l.totalDeviationProperty().get()));
		});

        addTable(doc, table, new float[]{ 80, 80, 100, 40, 40, 40, 120 });
	}

	protected void addContacts(Document doc, Chapter chapter,
					CooCustomer customer)
		throws DocumentException
	{
		Section section = addCaption(doc, chapter, "Kontakte");
		section.add(new Paragraph(
			String.format(
			"Kontakte und Ansprechpartner des Kunden %s.",
			customer.nameProperty().get())));

		PdfPTable table = createTable("Kontakte",
			"Vorname", "Nachname", "E-Mail", "Telefon");

		customer.getContacts().forEach(c ->
		{
			table.addCell(c.firstNameProperty().get());
			table.addCell(c.lastNameProperty().get());
			table.addCell(c.mailProperty().get());
			table.addCell(c.phoneProperty().get());
		});
		
		doc.add(section);
		addTable(doc, table, 100, 100, 200, 100);
	}

	protected void addPalets(Document doc, Chapter chapter,
					CooCustomer customer)
		throws DocumentException
	{
		Section section = addCaption(doc, chapter, "Paletten");
		section.add(new Paragraph(
			String.format(
			"Alle Paletten die beim Kunden %s momentan verwendet werden.",
			customer.nameProperty().get())));
		
		PdfPTable table = createTable("Paletten", 
			"Typ", "L‰nge", "Breite");

		customer.getPalets().forEach(c ->
		{
			table.addCell(String.valueOf(c.typeProperty().get()));
			table.addCell(String.valueOf(c.lengthProperty().get()));
			table.addCell(String.valueOf(c.widthProperty().get()));
		});

		doc.add(section);
		addTable(doc, table, 100, 200, 200);
	}

	protected void addField(Document doc, Section section, String string,
					String prop) throws DocumentException
	{
		Paragraph field = new Paragraph(string + ": " + prop,
			FontFactory.getFont(
				FontFactory.HELVETICA, 12));
		section.add(field);
	}

	protected Section addCaption(Document doc, Chapter chapter, String caption)
		throws DocumentException
	{
		Paragraph pCaption = new Paragraph(caption, FontFactory.getFont(
			FontFactory.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY));

		Section section = chapter.addSection(pCaption);
		// section.setBookmarkTitle(caption);
		// section.setBookmarkOpen(false);
		section.setIndentation(13);
		section.setNumberStyle(Section.NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT);

		return section;
	}

	protected Section addSubCaption(Document doc, Section parent, String caption)
		throws DocumentException
	{
		Paragraph pCaption = new Paragraph(caption, FontFactory.getFont(
			FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.LIGHT_GRAY));

		Section section = parent.addSection(pCaption);
		// section.setBookmarkTitle(caption);
		section.setBookmarkOpen(false);
		section.setIndentation(13);
		section.setNumberStyle(Section.NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT);

		return section;
	}

	protected void addSubCaption(Document doc, String caption)
		throws DocumentException
	{
		Paragraph pCaption = new Paragraph(caption, FontFactory.getFont(
			FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.LIGHT_GRAY));

		doc.add(pCaption);
	}

	protected Chapter addChapter(Document doc, String chapter)
		throws DocumentException
	{
		Paragraph pChapter = new Paragraph(chapter, FontFactory.getFont(
			FontFactory.HELVETICA, 18, Font.BOLDITALIC, BaseColor.BLACK));
		Chapter c = new Chapter(pChapter, chapterIdx++);

		pageEvent.setHeader(chapter);
		doc.add(c);

		return c;
	}

	protected PdfPTable createTable(String name, String... columns)
		throws DocumentException
	{
		PdfPTable table = new PdfPTable(columns.length);
		PdfPCell cell = new PdfPCell(new Phrase(name, FontFactory.getFont(
			FontFactory.HELVETICA, 12, Font.ITALIC)));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		// cell.setBorder(Rectangle.NO_BORDER);
		cell.setGrayFill(0.9f);
		cell.setColspan(columns.length);
		table.addCell(cell);
		table.setWidthPercentage(100);

		Arrays.asList(columns).forEach(c -> table.addCell(new Paragraph(c,
			FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD))));

		return table;
	}
	
	protected void addTable(Document doc, PdfPTable table, float... widths) throws DocumentException
	{
		if(Objects.nonNull(widths) && widths.length > 0)
		{
			table.setTotalWidth(widths);
	        table.setLockedWidth(true);
		}
		
		doc.add(new Paragraph(" "));
		doc.add(table);		
	}

	protected void addTitlePage(Document doc, CooCustomer customer)
		throws DocumentException
	{
		Image customerLogo = customer.logoProprty().get();

		if(Objects.nonNull(customerLogo))
		{
			try
			{
				customer.logoProprty().get();
				ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
				ImageIO.write(SwingFXUtils.fromFXImage(customerLogo, null),
					CooXMLDBUtil.CUSTOMER_LOGO_PIC_TYPE, byteOutput);
				com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(byteOutput.toByteArray());
				img.setAlignment(Element.ALIGN_CENTER);
				doc.add(img);
			}
			catch(IOException e)
			{
				CooLog.error("Could not load customer logo", e);
			}
		}

		Font titleFont = FontFactory.getFont(
			FontFactory.HELVETICA, 24, Font.BOLDITALIC, BaseColor.BLACK);
		Paragraph custName = new Paragraph(customer.nameProperty().get(),
			titleFont);
		custName.setAlignment(Element.ALIGN_CENTER);

		Font subTitleFont = FontFactory.getFont(
			FontFactory.HELVETICA, 20, Font.BOLDITALIC, BaseColor.DARK_GRAY);
		Paragraph street = new Paragraph(customer.streetProperty().get(),
			subTitleFont);
		street.setAlignment(Element.ALIGN_CENTER);
		Paragraph location = new Paragraph(
			customer.plzProperty().get() + " "
							+ customer.locationProperty().get(), subTitleFont);
		location.setAlignment(Element.ALIGN_CENTER);

		doc.add(custName);
		doc.add(street);
		doc.add(location);
	}

	@Override
	public void save(File file, CooCustomer customer, CooProject... projects)
	{
		try
		{
			// Create document
			Document doc = new Document(PageSize.A4, 50, 20, 63, 35);
			OutputStream outStream = new FileOutputStream(file);
			PdfWriter writer = PdfWriter.getInstance(doc, outStream);

			// Add Header and footer
			pageEvent = new CooHeaderFooterPageEvent(
				getContent().contains(Content.TITLE_PAGE));
			if(getContent().contains(Content.HEADER_FOOTER))
			{
				writer.setPageEvent(pageEvent);
			}

			// Open document and add necessary content
			doc.open();
			if(Objects.nonNull(customer))
			{
				for(Content c : getContent())
				{
					switch(c)
					{
						case TITLE_PAGE:
							addTitlePage(doc, customer);
							break;
						case CUSTOMER:
							addCustomer(doc, customer);
							break;
						case LAP_SOFTWARE:
							break;
						case PROJECT:
							for(CooProject prj : projects)
							{
								addProject(doc, prj);
							}
							break;
						default:
							break;
					}
				}
			}

			// Close the document
			doc.close();
			outStream.close();
		}
		catch(DocumentException | IOException e)
		{
			CooLog.error("Error while saving document", e);
		}
	}

	@Override
	public List<Content> getAvailableContent()
	{
		return Arrays.asList(Content.CUSTOM, Content.TITLE_PAGE,
			Content.HEADER_FOOTER, Content.CUSTOMER, Content.CONTACTS,
			Content.PALETS, Content.PROJECT, Content.LAP_SOFTWARE,
			Content.STATIONS, Content.TOTALSTATION, Content.REGION_DIVIDING,
			Content.LASER, Content.MEASUREMENTS, Content.RETICLES,
			Content.TARGETS,
			Content.VERIFY_MEASUREMENT, Content.SPECIFICATION, Content.RESULT,
			Content.GATEWAY);
	}

	@Override
	public ExtensionFilter getFileFilter()
	{
		return new FileChooser.ExtensionFilter("Adobe PDF", "*.pdf");
	}
}