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

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import de.coordz.data.*;
import de.coordz.data.base.*;
import de.util.log.CooLog;

public class CooPdfDocument extends CooDocument
{
	protected int chapterIdx = 1;
	protected CooCustomer customer;
	
	public CooPdfDocument(CooCustomer customer)
	{
		this.customer = customer;
	}
	
	protected void addProject(Document doc, CooProject project)
		throws DocumentException
	{

		addChapter(doc, "Projekt:" + project.nameProperty().get());
		
		doc.add(new Paragraph("Name:" + project.nameProperty().get()));
	}

	protected void addStations(Document doc,
					ObservableList<CooStation> stations)
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
		}
	}

	protected void addGateway(Document doc,
					CooGateway gateway, boolean addLaser)
		throws DocumentException
	{
		addCaption(doc, "Gateway");

		doc.add(new Paragraph("Ip:" + gateway.ipProperty().get()));
		doc.add(new Paragraph("X:" + gateway.macProperty().get()));
		
		if(addLaser)
		{
			addLaser(doc, gateway.getLaser());
		}
	}

	protected  void addTotalStation(Document doc,
					CooTotalstation totalStation)
		throws DocumentException
	{
		// TODO Make as header
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
		doc.add(new Paragraph("Straße:" + customer.streetProperty().get()));
		doc.add(new Paragraph("PLZ:" + customer.plzProperty().get()));
		doc.add(new Paragraph("Ort:" + customer.locationProperty().get()));
	}
	
	protected void addLaser(Document doc,
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

	protected void addContacts(Document doc,
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

	protected void addPalets(Document doc,
					ObservableList<CooPalet> palets)
		throws DocumentException
	{
		doc.add(new Paragraph("Paletten"));
		doc.add(new Paragraph(" ")); // For spacing
		PdfPTable table = addTable("Paletten", "Typ", "Länge", "Breite");

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
		Paragraph field = new Paragraph(string+ ":" + prop, FontFactory.getFont(
			FontFactory.HELVETICA, 12, Font.BOLD));
		doc.add(field);		
	}

	protected void addCaption(Document doc, String caption) throws DocumentException
	{
		Paragraph pCaption = new Paragraph(caption, FontFactory.getFont(
			FontFactory.HELVETICA, 18, Font.BOLDITALIC, BaseColor.MAGENTA));
		
		doc.add(pCaption);		
	}
	
	protected void addChapter(Document doc, String chapter) throws DocumentException
	{
		Paragraph pChapter = new Paragraph(chapter, FontFactory.getFont(
			FontFactory.HELVETICA, 18, Font.BOLDITALIC, BaseColor.BLUE));
		Chapter c = new Chapter(pChapter, chapterIdx++);
		
		doc.add(c);		
	}

	protected PdfPTable addTable(String name, String... columns)
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
	
	@Override
	public void save(File file)
	{
		try
		{
			Document doc = new Document();
			OutputStream outStream = new FileOutputStream(file);
			PdfWriter.getInstance(doc, outStream);
			doc.open();
			
			if(Objects.nonNull(customer))
			{
				for(int i = 0; i < getContent().size(); i++)
				{
					Content c = getContent().get(i);
					
					switch(c)
					{
						case CONTACTS:
							addContacts(doc, customer.getContacts());
							break;
						case CUSTOMER:
							addCustomer(doc, customer);
							break;
						case GATEWAY:
							break;
						case LAP_SOFTWARE:
							break;
						case MEASUREMENTS:
							break;
						case PALETS:
							break;
						case PROJECT:
							break;
						case REGION_DIVIDING:
							break;
						case RESULT:
							break;
						case RETICLES:
							break;
						case SPECIFICATION:
							break;
						case STATION:
							break;
						case TARGETS:
							break;
						case VERIFY_MEASUREMENT:
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
}