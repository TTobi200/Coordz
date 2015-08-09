/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.doc;

import java.io.*;
import java.util.*;

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

	protected void addProject(Document doc, CooProject project)
		throws DocumentException
	{
		addChapter(doc, "Projekt:" + project.nameProperty().get());
		doc.add(new Paragraph("Name:" + project.nameProperty().get()));

		for(Content c : getContent())
		{
			switch(c)
			{
				case LAP_SOFTWARE:
					addLAPSoftware(doc, project.lapSoftwareProperty().get());
					break;
				case STATIONS:
					addStations(doc, project.getStations());
					break;
				default:
					break;
			}
		}
	}

	protected void addStations(Document doc,
					ObservableList<CooStation> stations)
		throws DocumentException
	{
		addCaption(doc, "Stationen");

		for(CooStation s : stations)
		{
			addSubCaption(doc, "Station " + s.nameProperty().get());
			addField(doc, "Name", s.nameProperty().get());
			addField(doc, "Datei", s.fileProperty().get());
			addField(doc, "X-Offset", String.valueOf(s.xOffsetProperty().get()));
			addField(doc, "Y-Offset", String.valueOf(s.yOffsetProperty().get()));
			addField(doc, "Z-Offset", String.valueOf(s.zOffsetProperty().get()));

			for(int j = 0; j < getContent().size(); j++)
			{
				switch(getContent().get(j))
				{
					case GATEWAY:
						addGateway(doc, s.gatewayProperty().get());
						break;
					case TOTALSTATION:
						addTotalStation(doc, s.totalStationProperty().get());
						break;
					case MEASUREMENTS:
						addMeasurements(doc, s.getMeasurements());
						break;
					case VERIFY_MEASUREMENT:
						addVerifyMeasurement(doc, s.verifyMeasurementProperty()
							.get());
						break;
					case REGION_DIVIDING:
						addRegionDividing(doc, s.regionDevidingProperty().get());
						break;
					default:
						break;
				}
			}
		}
	}

	protected void addRegionDividing(Document doc,
					CooRegionDividing regionDividing)
		throws DocumentException
	{
		addCaption(doc, "Bereichsaufteilung");

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

	protected void addMeasurements(Document doc,
					ObservableList<CooMeasurement> measurements)
		throws DocumentException
	{
		addCaption(doc, "Messungen");

		for(CooMeasurement m : measurements)
		{
			addSubCaption(doc, "Messung " + m.nameProperty().get());
			addField(doc, "Name", m.nameProperty().get());
			addField(doc, "Datum", String.valueOf(m.dateProperty().get()));
			addField(doc, "Von", m.fromProperty().get());
			addField(doc, "Bis", m.toProperty().get());
			addField(doc, "Wetter", m.weatherProperty().get());

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

	protected void addVerifyMeasurement(Document doc,
					CooVerifyMeasurement verifyMeasurement)
		throws DocumentException
	{
		addCaption(doc, "Kontroll Messung");

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
		addSubCaption(doc, "Vorgabe");
		PdfPTable table = addTable("Vorgabe",
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

		doc.add(table);
	}

	protected void addResut(Document doc,
					ObservableList<CooRectangle> results)
		throws DocumentException
	{
		addSubCaption(doc, "Ergebnis");
		PdfPTable table = addTable("Ergebnis",
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

		doc.add(table);
	}

	protected void addReticles(Document doc,
					ObservableList<CooReticle> reticles)
		throws DocumentException
	{
		addSubCaption(doc, "Zielmarken");
		PdfPTable table = addTable("Zielmarken",
			"Name", "X", "Y", "Z");

		reticles.forEach(r ->
		{
			table.addCell(r.nameProperty().get());
			table.addCell(String.valueOf(r.xProperty().get()));
			table.addCell(String.valueOf(r.yProperty().get()));
			table.addCell(String.valueOf(r.zProperty().get()));
		});

		doc.add(table);
	}

	protected void addTargets(Document doc,
					ObservableList<CooTarget> targets)
		throws DocumentException
	{
		addSubCaption(doc, "Targets");
		PdfPTable table = addTable("Targets",
			"Name", "X", "Y", "Z");

		targets.forEach(t ->
		{
			table.addCell(t.nameProperty().get());
			table.addCell(String.valueOf(t.xProperty().get()));
			table.addCell(String.valueOf(t.yProperty().get()));
			table.addCell(String.valueOf(t.zProperty().get()));
		});

		doc.add(table);
	}

	protected void addGateway(Document doc,
					CooGateway gateway)
		throws DocumentException
	{
		addCaption(doc, "Gateway");

		doc.add(new Paragraph("Ip:" + gateway.ipProperty().get()));
		doc.add(new Paragraph("X:" + gateway.macProperty().get()));
	}

	protected void addLAPSoftware(Document doc,
					CooLAPSoftware lapSoftware)
		throws DocumentException
	{
		addCaption(doc, "LAP-Software");

		doc.add(new Paragraph("Name:" + lapSoftware.nameProperty().get()));
		doc.add(new Paragraph("Version:" + lapSoftware.versionProperty().get()));
	}

	protected void addTotalStation(Document doc,
					CooTotalstation totalStation)
		throws DocumentException
	{
		addCaption(doc, "Totalstation");

		doc.add(new Paragraph("X:" + totalStation.xProperty().get()));
		doc.add(new Paragraph("Y:" + totalStation.yProperty().get()));
		doc.add(new Paragraph("Z:" + totalStation.zProperty().get()));
		doc.add(new Paragraph("Delta-X:" + totalStation.deltaXProperty().get()));
		doc.add(new Paragraph("Delta-Y:" + totalStation.deltaYProperty().get()));
	}

	protected void addCustomer(Document doc, CooCustomer customer)
		throws DocumentException
	{
		addChapter(doc, "Kunde " + customer.nameProperty().get());

		doc.add(new Paragraph("Name:" + customer.nameProperty().get()));
		doc.add(new Paragraph("Adresse:" + customer.adressProperty().get()));
		doc.add(new Paragraph("Straﬂe:" + customer.streetProperty().get()));
		doc.add(new Paragraph("PLZ:" + customer.plzProperty().get()));
		doc.add(new Paragraph("Ort:" + customer.locationProperty().get()));
	}

	protected void addLaser(Document doc,
					ObservableList<CooLaser> laser)
		throws DocumentException
	{
		addSubCaption(doc, "Laser");
		PdfPTable table = addTable("Laser",
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

		doc.add(table);
	}

	protected void addContacts(Document doc,
					ObservableList<CooContact> contacts)
		throws DocumentException
	{
		addSubCaption(doc, "Kontakte");
		PdfPTable table = addTable("Kontakte",
			"Vorname", "Nachname", "E-Mail", "Telefon");

		contacts.forEach(c ->
		{
			table.addCell(c.firstNameProperty().get());
			table.addCell(c.lastNameProperty().get());
			table.addCell(c.mailProperty().get());
			table.addCell(c.phoneProperty().get());
		});

		doc.add(table);
	}

	protected void addPalets(Document doc,
					ObservableList<CooPalet> palets)
		throws DocumentException
	{
		addSubCaption(doc, "Paletten");
		PdfPTable table = addTable("Paletten", "Typ", "L‰nge", "Breite");

		palets.forEach(c ->
		{
			table.addCell(String.valueOf(c.typeProperty().get()));
			table.addCell(String.valueOf(c.lengthProperty().get()));
			table.addCell(String.valueOf(c.widthProperty().get()));
		});

		doc.add(table);
	}

	protected void addField(Document doc, String string,
					String prop) throws DocumentException
	{
		Paragraph field = new Paragraph(string + ": " + prop,
			FontFactory.getFont(
				FontFactory.HELVETICA, 12));
		doc.add(field);
	}

	protected void addCaption(Document doc, String caption)
		throws DocumentException
	{
		Paragraph pCaption = new Paragraph(caption, FontFactory.getFont(
			FontFactory.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY));

		doc.add(pCaption);
	}

	protected void addSubCaption(Document doc, String caption)
		throws DocumentException
	{
		Paragraph pCaption = new Paragraph(caption, FontFactory.getFont(
			FontFactory.HELVETICA, 14, Font.BOLD, BaseColor.LIGHT_GRAY));

		doc.add(pCaption);
	}

	protected void addChapter(Document doc, String chapter)
		throws DocumentException
	{
		Paragraph pChapter = new Paragraph(chapter, FontFactory.getFont(
			FontFactory.HELVETICA, 18, Font.BOLDITALIC, BaseColor.BLACK));
		Chapter c = new Chapter(pChapter, chapterIdx++);

		doc.add(c);
	}

	protected PdfPTable addTable(String name, String... columns)
		throws DocumentException
	{
		PdfPTable table = new PdfPTable(columns.length);
		PdfPCell cell = new PdfPCell(new Phrase(name, FontFactory.getFont(
			FontFactory.HELVETICA, 12, Font.ITALIC)));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(columns.length);
		table.addCell(cell);
		table.setWidthPercentage(100);

		Arrays.asList(columns).forEach(c -> table.addCell(new Paragraph(c,
			FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD))));

		return table;
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
			Document doc = new Document(PageSize.A4, 20, 20, 50, 25);
			OutputStream outStream = new FileOutputStream(file);
			PdfWriter writer = PdfWriter.getInstance(doc, outStream);
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
						case HEADER_FOOTER:
							writer.setPageEvent(new CooHeaderFooterPageEvent());
							break;
						case CONTACTS:
							addContacts(doc, customer.getContacts());
							break;
						case CUSTOMER:
							addCustomer(doc, customer);
							break;
						case LAP_SOFTWARE:
							break;
						case PALETS:
							addPalets(doc, customer.getPalets());
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

			doc.close();
			outStream.close();
		}
		catch(DocumentException | IOException e)
		{
			CooLog.error("Error while saving document", e);
		}
	}

	@Override
	public ExtensionFilter getFileFilter()
	{
		return new FileChooser.ExtensionFilter("Adobe PDF", "*.pdf");
	}
}