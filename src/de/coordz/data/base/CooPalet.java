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
import de.util.CooPaletType;

public class CooPalet extends CooData
{
	protected CooPaletType type;
	protected int width;
	protected int height;
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element palet = addElement(doc, root, "Palet");
		palet.setAttribute("Type", String.valueOf(type));
		palet.setAttribute("Width", String.valueOf(width));
		palet.setAttribute("Height", String.valueOf(height));
	}
}