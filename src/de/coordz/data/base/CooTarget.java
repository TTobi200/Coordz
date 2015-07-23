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
}