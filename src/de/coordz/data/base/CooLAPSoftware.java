/*
 * $Header$
 * 
 * $Log$
 * Copyright � 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.addElement;

import java.util.Objects;

import org.w3c.dom.*;

import de.coordz.db.xml.CooDBXML;
import javafx.beans.property.*;

public class CooLAPSoftware	implements CooDBXML
{
	/** {@link StringProperty} for the lap software name */
	protected StringProperty name;
	/** {@link StringProperty} for the lap software version */
	protected StringProperty version;
	
	public CooLAPSoftware()
	{
		name = new SimpleStringProperty();
		version = new SimpleStringProperty();
	}
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element lapSoftware = addElement(doc, root, "LAPSoftware");
		lapSoftware.setAttribute("Name", name.get());
		lapSoftware.setAttribute("Version", version.get());
	}	
	
	@Override
	public void fromXML(Element lapSoftware)
	{
		if(Objects.nonNull(lapSoftware))
		{
			name.set(lapSoftware.getAttribute("Name"));
			version.set(lapSoftware.getAttribute("Version"));
		}
	}
	
	/**
	 * Method to access Property
	 * @return {@link #name}
	 */
	public StringProperty nameProperty()
	{
		return name;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #version}
	 */
	public StringProperty versionProperty()
	{
		return version;
	}
}