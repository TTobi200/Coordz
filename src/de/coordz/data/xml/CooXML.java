/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.xml;

import org.w3c.dom.*;

public interface CooXML
{
	// Method to create xml-doc from object
	public void toXML(Document doc, Element root);
	
	// Method to load object from xml
	public void fromXML(Element root);
}
