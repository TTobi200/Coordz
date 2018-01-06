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

import de.coordz.db.gen.dao.DaoLAPSoftware;
import de.coordz.db.xml.CooDBXML;

public class CooLAPSoftware	extends DaoLAPSoftware implements CooDBXML
{
	@Override
	public void toXML(Document doc, Element root)
	{
		Element lapSoftware = addElement(doc, root, "LAPSoftware");
		lapSoftware.setAttribute("Name", nameProperty().get());
		lapSoftware.setAttribute("Version", versionProperty().get());
	}	
	
	@Override
	public void fromXML(Element lapSoftware)
	{
		if(Objects.nonNull(lapSoftware))
		{
			nameProperty().set(lapSoftware.getAttribute("Name"));
			versionProperty().set(lapSoftware.getAttribute("Version"));
		}
	}
}