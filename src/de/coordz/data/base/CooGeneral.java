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

public class CooGeneral extends CooData
{
	protected String prjNr;
	protected LocalDate date;
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element general = addElement(doc, root, "General");
		general.setAttribute("PrjNumber", prjNr);
		general.setAttribute("Date", String.valueOf(date));
	}
}
