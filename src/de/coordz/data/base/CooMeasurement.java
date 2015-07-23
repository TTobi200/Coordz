/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.addElement;

import java.time.LocalDate;

import org.w3c.dom.*;

import de.coordz.data.CooData;

public class CooMeasurement extends CooData
{
	protected String name;
	protected LocalDate date;
	protected String from;
	protected String to;
	protected String weather;
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element measurement = addElement(doc, root, "Measurement");
		measurement.setAttribute("Name", name);
		measurement.setAttribute("Date", String.valueOf(date));
		measurement.setAttribute("From", from);
		measurement.setAttribute("To", to);
		measurement.setAttribute("Weather", weather);
	}
}