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

import de.coordz.db.gen.dao.DaoTotalstation;
import de.coordz.db.xml.CooDBXML;

public class CooTotalstation extends DaoTotalstation implements CooDBXML
{
	@Override
	public void toXML(Document doc, Element root)
	{
		Element totalstation = addElement(doc, root, "Totalstation");
		totalstation.setAttribute("X", String.valueOf(xProperty().get()));
		totalstation.setAttribute("Y", String.valueOf(yProperty().get()));
		totalstation.setAttribute("Z", String.valueOf(zProperty().get()));
		totalstation.setAttribute("DeltaX", String.valueOf(deltaXProperty().get()));
		totalstation.setAttribute("DeltaY", String.valueOf(deltaYProperty().get()));
	}
	
	@Override
	public void fromXML(Element totalstation)
	{
		if(Objects.nonNull(totalstation))
		{
			xProperty().set(Integer.valueOf(totalstation.getAttribute("X")));
			yProperty().set(Integer.valueOf(totalstation.getAttribute("Y")));
			zProperty().set(Integer.valueOf(totalstation.getAttribute("Z")));
			deltaXProperty().set(Double.valueOf(totalstation.getAttribute("DeltaX")));
			deltaYProperty().set(Double.valueOf(totalstation.getAttribute("DeltaY")));
		}
	}
}