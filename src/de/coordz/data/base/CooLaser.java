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

import de.coordz.db.gen.dao.DaoLaser;
import de.coordz.db.xml.CooDBXML;

public class CooLaser extends DaoLaser implements CooDBXML
{
	@Override
	public void toXML(Document doc, Element root)
	{
		Element laser = addElement(doc, root, "Laser");
		laser.setAttribute("Name", nameProperty().get());
		laser.setAttribute("ArticleNr", articleNoProperty().get());
		laser.setAttribute("SerialNr", serialNoProperty().get());
		laser.setAttribute("X", String.valueOf(xProperty().get()));
		laser.setAttribute("Y", String.valueOf(yProperty().get()));
		laser.setAttribute("Z", String.valueOf(zProperty().get()));
		laser.setAttribute("TotalDeviation", 
			String.valueOf(totalDeviationProperty().get()));
		laser.setAttribute("From", String.valueOf(fromProperty().get()));
		laser.setAttribute("To", String.valueOf(toProperty().get()));
	}
	
	@Override
	public void fromXML(Element laser)
	{
		if(Objects.nonNull(laser))
		{
			nameProperty().set(laser.getAttribute("Name"));
			articleNoProperty().set(laser.getAttribute("ArticleNr"));
			serialNoProperty().set(laser.getAttribute("SerialNr"));
			xProperty().set(Integer.valueOf(laser.getAttribute("X")));
			yProperty().set(Integer.valueOf(laser.getAttribute("Y")));
			zProperty().set(Integer.valueOf(laser.getAttribute("Z")));
			totalDeviationProperty().set(Double.valueOf(laser.getAttribute(
				"TotalDeviation")));
			fromProperty().set(Integer.valueOf(laser.getAttribute("From")));
			toProperty().set(Integer.valueOf(laser.getAttribute("To")));
		}
	}
}