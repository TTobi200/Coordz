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

import de.coordz.db.gen.dao.DaoPalet;
import de.coordz.db.xml.CooDBXML;

public class CooPalet extends DaoPalet implements CooDBXML
{
	@Override
	public void toXML(Document doc, Element root)
	{
		Element palet = addElement(doc, root, "Palet");
		palet.setAttribute("Name", String.valueOf(nameProperty().get()));
		palet.setAttribute("Type", String.valueOf(typeProperty().get()));
		palet.setAttribute("Width", String.valueOf(widthProperty().get()));
		palet.setAttribute("Length", String.valueOf(lengthProperty().get()));
	}
	
	@Override
	public void fromXML(Element palet)
	{
		if(Objects.nonNull(palet))
		{
			nameProperty().set(palet.getAttribute("Name"));
			// FIXME $TO: Convert to palet type
//			typeProperty().set(CooPaletType.parse(palet.getAttribute("Type")));
			widthProperty().set(Integer.valueOf(palet.getAttribute("Width")));
			lengthProperty().set(Integer.valueOf(palet.getAttribute("Length")));
		}
	}
	
	@Override
	public String toString()
	{
		return nameProperty().get();
	}
}