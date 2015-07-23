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

public class CooLaser extends CooData
{
	protected String name;
	protected String mac;
	protected String serialNr;

	protected int x;
	protected int y;
	protected int z;
	protected double totalDeviation;

	@Override
	public void toXML(Document doc, Element root)
	{
		Element laser = addElement(doc, root, "Laser");
		laser.setAttribute("Name", name);
		laser.setAttribute("MAC", mac);
		laser.setAttribute("SerialNr", serialNr);
		laser.setAttribute("X", String.valueOf(x));
		laser.setAttribute("Y", String.valueOf(y));
		laser.setAttribute("Z", String.valueOf(z));
		laser.setAttribute("TotalDeviation", 
			String.valueOf(totalDeviation));
	}
}