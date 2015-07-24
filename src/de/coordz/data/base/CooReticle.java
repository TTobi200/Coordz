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
		reticle.setAttribute("Name", name);
		reticle.setAttribute("X", String.valueOf(x));
		reticle.setAttribute("Y", String.valueOf(y));
		reticle.setAttribute("Z", String.valueOf(z));
	}
	
	@Override
	public void fromXML(Element reticle)
	{
		// Not needed we get the single reticle here
//		Element reticle = getSingleElement(root, "Reticle");
		if(Objects.nonNull(reticle))
		{
			name = reticle.getAttribute("Name");
			x = Integer.valueOf(reticle.getAttribute("X"));
			y = Integer.valueOf(reticle.getAttribute("Y"));
			z = Integer.valueOf(reticle.getAttribute("Z"));
		}
	}
}