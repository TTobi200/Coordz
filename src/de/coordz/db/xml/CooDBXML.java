/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2017 T.Ohm . All Rights Reserved.
 */
package de.coordz.db.xml;

import org.w3c.dom.*;

/**
 * Interface for xml functions.
 * @author tobias.ohm
 */
public interface CooDBXML
{
	/**
	 * Method to format Class into xml-Tag.
	 * @param doc = the {@link Document} to add
	 * @param root = the root {@link Element}
	 */
	public void toXML(Document doc, Element root);
	
	/**
	 * Method to parse Class out of xml-{@link Element}
	 * @param parent = the parent {@link Element}
	 */
	public void fromXML(Element parent);
}
