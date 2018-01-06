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

import de.coordz.db.gen.dao.DaoRectangle;
import de.coordz.db.xml.CooDBXML;

public class CooRectangle extends DaoRectangle implements CooDBXML
{
	@Override
	public void toXML(Document doc, Element root)
	{
		Element rectangle = addElement(doc, root, "Rectangle");
		rectangle.setAttribute("Name", nameProperty().get());
		rectangle.setAttribute("Width", String.valueOf(widthProperty().get()));
		rectangle.setAttribute("Height", String.valueOf(heightProperty().get()));
		rectangle.setAttribute("Length", String.valueOf(lengthProperty().get()));
		rectangle.setAttribute("X", String.valueOf(xProperty().get()));
		rectangle.setAttribute("Y", String.valueOf(yProperty().get()));
		rectangle.setAttribute("Z", String.valueOf(zProperty().get()));
		rectangle.setAttribute("D1", String.valueOf(d1Property().get()));
		rectangle.setAttribute("D2", String.valueOf(d2Property().get()));
	}
	
	@Override
	public void fromXML(Element rectangle)
	{
		if(Objects.nonNull(rectangle))
		{
			nameProperty().set(rectangle.getAttribute("Name"));
			widthProperty().set(Integer.valueOf(rectangle.getAttribute("Width")));
			heightProperty().set(Integer.valueOf(rectangle.getAttribute("Height")));
			lengthProperty().set(Integer.valueOf(rectangle.getAttribute("Length")));
			xProperty().set(Integer.valueOf(rectangle.getAttribute("X")));
			yProperty().set(Integer.valueOf(rectangle.getAttribute("Y")));
			zProperty().set(Integer.valueOf(rectangle.getAttribute("Z")));
			d1Property().set(Integer.valueOf(rectangle.getAttribute("D1")));
			d2Property().set(Integer.valueOf(rectangle.getAttribute("D2")));
		}
	}
}