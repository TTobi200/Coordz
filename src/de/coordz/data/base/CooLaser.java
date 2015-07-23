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
	
	@Override
	public void fromXML(Element root)
	{
		Element laser = getSingleElement(root, "Laser");
		if(Objects.nonNull(laser))
		{
			name = laser.getAttribute("Name");
			mac = laser.getAttribute("MAC");
			serialNr = laser.getAttribute("SerialNr");
			x = Integer.valueOf(laser.getAttribute("X"));
			y = Integer.valueOf(laser.getAttribute("Y"));
			z = Integer.valueOf(laser.getAttribute("Z"));
			totalDeviation = Double.valueOf(laser.getAttribute(
				"TotalDeviation"));
		}
	}
}