/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.addElement;

import java.util.Objects;

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
	
	@Override
	public void fromXML(Element lapSoftware)
	{
		if(Objects.nonNull(lapSoftware))
		{
			name = lapSoftware.getAttribute("Name");
			version = lapSoftware.getAttribute("Version");
		}
	}
}