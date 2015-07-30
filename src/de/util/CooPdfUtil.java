/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.util;

import java.io.*;
import java.util.*;

import javafx.collections.ObservableList;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import de.coordz.data.*;
import de.coordz.data.base.*;

public class CooPdfUtil<T extends CooData>
{
	private static int chapterIdx = 1;

	public static void crePdfDoc(CooCustomer customer)
	{
		try
		{
			chapterIdx = 1;
			
			Document doc = new Document();
			OutputStream file = new FileOutputStream(new File(
				"./" + customer.nameProperty().get() + ".pdf"));
			PdfWriter.getInstance(doc, file);
			doc.open();

			if(Objects.nonNull(customer))
			{
				addCustomer(doc, customer, true, true);
			}

			for(int i = 0; i < customer.getProjects().size(); i++)
			{
				doc.newPage();
				addProject(doc, customer.getProjects().get(i), true);
			}

			doc.close();
			file.close();
		}
		catch(DocumentException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public static void crePdfDoc(CooCustomer customer, CooProject project)
	// {
	// try
	// {
	// Document document = new Document();
	// document.open();
	//
	// if(Objects.nonNull(customer))
	// {
	// addCustomer(document, customer, true, true);
	// }
	// if(Objects.nonNull(project))
	// {
	// addProject(document, project, true);
	// }
	//
	// OutputStream file = new FileOutputStream(new File(
	// "./" + customer.nameProperty().get() + ".pdf"));
	// PdfWriter.getInstance(document, file);
	// document.close();
	// file.close();
	// }
	// catch(DocumentException | IOException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public static Document creDoc()
	{
		Document document = new Document();
		document.open();
		return document;
	}

	private static void addProject(Document doc, CooProject project,
					boolean addStations)
		throws DocumentException
	{

		addChapter(doc, "Projekt:" + project.nameProperty().get());
		
		doc.add(new Paragraph("Name:" + project.nameProperty().get()));

		if(addStations)
		{
			doc.add(new Paragraph(" ")); // For spacing
			addStations(doc, project.getStations(), true, true);
		}
	}

	private static void addStations(Document doc,
					ObservableList<CooStation> stations, boolean addGateway, boolean addTotalStation)
		throws DocumentException
	{
		addCaption(doc, "Stationen");
		doc.add(new Paragraph(" ")); // For spacing

		for(int i = 0; i < stations.size(); i++)
		{
			CooStation s = stations.get(i);
			doc.add(new Paragraph("Station"));
			addField(doc, "Name", s.nameProperty().get());
			addField(doc, "Datei", s.fileProperty().get());
			addField(doc, "X-Offset", String.valueOf(s.xOffsetProperty().get()));
			addField(doc, "Y-Offset", String.valueOf(s.yOffsetProperty().get()));
			addField(doc, "Z-Offset", String.valueOf(s.zOffsetProperty().get()));

			if(addGateway)
			{
				addGateway(doc, s.gatewayProperty().get(), true);
			}
			if(addTotalStation)
			{
				addTotalStation(doc, s.totalStationProperty().get());
			}
		}
	}

	private static void addField(Document doc, String string,
					String prop) throws DocumentException
	{
		Paragraph field = new Paragraph(string+ ":" + prop, FontFactory.getFont(
			FontFactory.HELVETICA, 12, Font.BOLD));
		doc.add(field);		
	}

	private static void addCaption(Document doc, String caption) throws DocumentException
	{
		Paragraph pCaption = new Paragraph(caption, FontFactory.getFont(
			FontFactory.HELVETICA, 18, Font.BOLDITALIC, BaseColor.MAGENTA));
		
		doc.add(pCaption);		
	}
	
	private static void addChapter(Document doc, String chapter) throws DocumentException
	{
		Paragraph pChapter = new Paragraph(chapter, FontFactory.getFont(
			FontFactory.HELVETICA, 18, Font.BOLDITALIC, BaseColor.BLUE));
		Chapter c = new Chapter(pChapter, chapterIdx++);
		
		doc.add(c);		
	}

	private static void addGateway(Document doc,
					CooGateway gateway, boolean addLaser)
		throws DocumentException
	{
		// TODO Make as header
		doc.add(new Paragraph("Gateway"));

		doc.add(new Paragraph("Ip:" + gateway.ipProperty().get()));
		doc.add(new Paragraph("X:" + gateway.macProperty().get()));
		
		if(addLaser)
		{
			addLaser(doc, gateway.getLaser());
		}
	}

	private static void addTotalStation(Document doc,
					CooTotalstation totalStation)
		throws DocumentException
	{
		// TODO Make as header
		doc.add(new Paragraph("Totalstation"));

		doc.add(new Paragraph("X:" + totalStation.xProperty().get()));
		doc.add(new Paragraph("Y:" + totalStation.yProperty().get()));
		doc.add(new Paragraph("Z:" + totalStation.zProperty().get()));
		doc.add(new Paragraph("Delta-X:" + totalStation.deltaXProperty().get()));
		doc.add(new Paragraph("Delta-Y:" + totalStation.deltaYProperty().get()));
	}

	private static void addCustomer(Document doc, CooCustomer customer,
					boolean addContacts, boolean addPalets)
		throws DocumentException
	{
		addChapter(doc, "Kunde " + customer.nameProperty().get());

		doc.add(new Paragraph("Name:" + customer.nameProperty().get()));
		doc.add(new Paragraph("Adresse:" + customer.adressProperty().get()));
		doc.add(new Paragraph("Straße:" + customer.streetProperty().get()));
		doc.add(new Paragraph("PLZ:" + customer.plzProperty().get()));
		doc.add(new Paragraph("Ort:" + customer.locationProperty().get()));

		if(addContacts)
		{
			doc.add(new Paragraph(" ")); // For spacing
			addContacts(doc, customer.getContacts());
		}
		if(addPalets)
		{
			doc.add(new Paragraph(" ")); // For spacing
			addPalets(doc, customer.getPalets());
		}
	}
	
	private static void addLaser(Document doc,
					ObservableList<CooLaser> laser)
		throws DocumentException
	{
		doc.add(new Paragraph("Laser"));
		doc.add(new Paragraph(" ")); // For spacing
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

	private static void addContacts(Document doc,
					ObservableList<CooContact> contacts)
		throws DocumentException
	{
		doc.add(new Paragraph("Kontakte"));
		doc.add(new Paragraph(" ")); // For spacing
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

	private static void addPalets(Document doc,
					ObservableList<CooPalet> contacts)
		throws DocumentException
	{
		doc.add(new Paragraph("Paletten"));
		doc.add(new Paragraph(" ")); // For spacing
		PdfPTable table = addTable("Paletten", "Typ", "Länge", "Breite");

		contacts.forEach(c ->
		{
			table.addCell(String.valueOf(c.typeProperty().get()));
			table.addCell(String.valueOf(c.lengthProperty().get()));
			table.addCell(String.valueOf(c.widthProperty().get()));
		});

		doc.add(table);
	}

	private static PdfPTable addTable(String name, String... columns)
		throws DocumentException
	{
		PdfPTable table = new PdfPTable(columns.length);
		PdfPCell cell = new PdfPCell(new Phrase(name));
		cell.setColspan(columns.length);
		table.addCell(cell);
		table.setWidthPercentage(100);

		Arrays.asList(columns).forEach(c -> table.addCell(c));

		return table;
	}
}