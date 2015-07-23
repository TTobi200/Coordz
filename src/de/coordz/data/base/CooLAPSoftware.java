/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.addElement;

import org.w3c.dom.*;

import de.coordz.data.CooData;

public class CooLAPSoftware	extends CooData
{
	protected String name;
	protected String version;
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element lapSoftware = addElement(doc, root, "LAPSoftware");
		lapSoftware.setAttribute("Name", name);
		lapSoftware.setAttribute("Version", version);
	}	
}