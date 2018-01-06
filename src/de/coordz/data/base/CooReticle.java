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

public class CooReticle extends CooTarget
{
	@Override
	public void toXML(Document doc, Element root)
	{
		Element reticle = addElement(doc, root, "Reticle");
		reticle.setAttribute("Name", nameProperty().get());
		reticle.setAttribute("X", String.valueOf(xProperty().get()));
		reticle.setAttribute("Y", String.valueOf(yProperty().get()));
		reticle.setAttribute("Z", String.valueOf(zProperty().get()));
	}
	
	@Override
	public void fromXML(Element reticle)
	{
		if(Objects.nonNull(reticle))
		{
			nameProperty().set(reticle.getAttribute("Name"));
			xProperty().set(Integer.valueOf(reticle.getAttribute("X")));
			yProperty().set(Integer.valueOf(reticle.getAttribute("Y")));
			zProperty().set(Integer.valueOf(reticle.getAttribute("Z")));
		}
	}
}