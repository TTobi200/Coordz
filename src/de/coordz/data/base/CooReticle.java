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
		reticle.setAttribute("Name", name.get());
		reticle.setAttribute("X", String.valueOf(x.get()));
		reticle.setAttribute("Y", String.valueOf(y.get()));
		reticle.setAttribute("Z", String.valueOf(z.get()));
	}
	
	@Override
	public void fromXML(Element reticle)
	{
		if(Objects.nonNull(reticle))
		{
			name.set(reticle.getAttribute("Name"));
			x.set(Integer.valueOf(reticle.getAttribute("X")));
			y.set(Integer.valueOf(reticle.getAttribute("Y")));
			z.set(Integer.valueOf(reticle.getAttribute("Z")));
		}
	}
}