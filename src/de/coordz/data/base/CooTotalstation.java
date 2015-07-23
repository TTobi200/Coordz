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

public class CooTotalstation extends CooData
{
	protected int x;
	protected int y;
	protected int z;
	protected double deltaX;
	protected double deltaY;
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element totalstation = addElement(doc, root, "Totalstation");
		totalstation.setAttribute("X", String.valueOf(x));
		totalstation.setAttribute("Y", String.valueOf(y));
		totalstation.setAttribute("Z", String.valueOf(z));
		totalstation.setAttribute("DeltaX", String.valueOf(deltaX));
		totalstation.setAttribute("DeltaY", String.valueOf(deltaY));
	}
}