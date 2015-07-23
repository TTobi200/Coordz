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

public class CooRectangle extends CooData
{
	protected String name;
	protected int width;
	protected int height;
	protected int length;
	protected int x;
	protected int y;
	protected int z;
	protected int d1;
	protected int d2;
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element rectangle = addElement(doc, root, "Rectangle");
		rectangle.setAttribute("Name", name);
		rectangle.setAttribute("Width", String.valueOf(width));
		rectangle.setAttribute("Height", String.valueOf(height));
		rectangle.setAttribute("Length", String.valueOf(length));
		rectangle.setAttribute("X", String.valueOf(x));
		rectangle.setAttribute("Y", String.valueOf(y));
		rectangle.setAttribute("Z", String.valueOf(z));
		rectangle.setAttribute("D1", String.valueOf(d1));
		rectangle.setAttribute("D2", String.valueOf(d2));
	}
	
	@Override
	public void fromXML(Element root)
	{
		Element rectangle = getSingleElement(root, "Rectangle");
		if(Objects.nonNull(rectangle))
		{
			name = rectangle.getAttribute("Name");
			width = Integer.valueOf(rectangle.getAttribute("Width"));
			height = Integer.valueOf(rectangle.getAttribute("Height"));
			length = Integer.valueOf(rectangle.getAttribute("Length"));
			x = Integer.valueOf(rectangle.getAttribute("X"));
			y = Integer.valueOf(rectangle.getAttribute("Y"));
			z = Integer.valueOf(rectangle.getAttribute("Z"));
			d1 = Integer.valueOf(rectangle.getAttribute("D1"));
			d2 = Integer.valueOf(rectangle.getAttribute("D2"));
		}
	}
}