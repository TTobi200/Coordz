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
	
	@Override
	public void fromXML(Element root)
	{
		Element totalstation = getSingleElement(root, "Totalstation");
		if(Objects.nonNull(totalstation))
		{
			x = Integer.valueOf(totalstation.getAttribute("X"));
			y = Integer.valueOf(totalstation.getAttribute("Y"));
			z = Integer.valueOf(totalstation.getAttribute("Z"));
			deltaX = Double.valueOf(totalstation.getAttribute("DeltaX"));
			deltaY = Double.valueOf(totalstation.getAttribute("DeltaY"));
		}
	}
}