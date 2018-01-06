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

import de.coordz.db.gen.dao.DaoTarget;
import de.coordz.db.xml.CooDBXML;

public class CooTarget extends DaoTarget implements CooDBXML
{
	@Override
	public void toXML(Document doc, Element root)
	{
		Element target = addElement(doc, root, "Target");
		target.setAttribute("Name", nameProperty().get());
		target.setAttribute("X", String.valueOf(xProperty().get()));
		target.setAttribute("Y", String.valueOf(yProperty().get()));
		target.setAttribute("Z", String.valueOf(zProperty().get()));
	}
	
	@Override
	public void fromXML(Element target)
	{
		if(Objects.nonNull(target))
		{
			nameProperty().set(target.getAttribute("Name"));
			xProperty().set(Integer.valueOf(target.getAttribute("X")));
			yProperty().set(Integer.valueOf(target.getAttribute("Y")));
			zProperty().set(Integer.valueOf(target.getAttribute("Z")));
		}
	}
	
	@Override
	public String toString()
	{
		// Build a string describing this target
		StringBuilder target = new StringBuilder("Target");
		target.append(" [")
		.append("Name=" + nameProperty().get()).append("; ")
		.append("X=" + xProperty().get()).append("; ")
		.append("Y=" + yProperty().get()).append("; ")
		.append("Z=" + zProperty().get()).append("]");
		
		return target.toString();
	}
}