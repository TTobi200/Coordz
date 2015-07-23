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

public class CooTarget extends CooData
{
	protected String name;
	protected int x;
	protected int y;
	protected int z;
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element target = addElement(doc, root, "Target");
		target.setAttribute("Name", name);
		target.setAttribute("X", String.valueOf(x));
		target.setAttribute("Y", String.valueOf(y));
		target.setAttribute("Z", String.valueOf(z));
	}
	
	@Override
	public void fromXML(Element root)
	{
		Element target = getSingleElement(root, "Target");
		if(Objects.nonNull(target))
		{
			name = target.getAttribute("Name");
			x = Integer.valueOf(target.getAttribute("X"));
			y = Integer.valueOf(target.getAttribute("Y"));
			z = Integer.valueOf(target.getAttribute("Z"));
		}
	}
}