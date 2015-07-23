/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.*;

import java.util.Objects;

import org.w3c.dom.*;

import de.coordz.data.CooData;
import de.util.CooPaletType;

public class CooPalet extends CooData
{
	protected CooPaletType type;
	protected int width;
	protected int length;
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element palet = addElement(doc, root, "Palet");
		palet.setAttribute("Type", String.valueOf(type));
		palet.setAttribute("Width", String.valueOf(width));
		palet.setAttribute("Length", String.valueOf(length));
	}
	
	@Override
	public void fromXML(Element root)
	{
		Element palet = getSingleElement(root, "Palet");
		if(Objects.nonNull(palet))
		{
			// TODO parse the Type
//			type = palet.getAttribute("Type");
			width = Integer.valueOf(palet.getAttribute("Width"));
			length = Integer.valueOf(palet.getAttribute("Length"));
		}
	}
}