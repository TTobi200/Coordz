/*
 * $Header$
 * 
 * $Log$
 * Copyright © 2015 T.Ohm . All Rights Reserved.
 */
package de.coordz.data.base;

import static de.util.CooXmlDomUtil.addElement;

import java.util.Objects;

import javafx.beans.property.*;

import org.w3c.dom.*;

import de.coordz.data.CooData;
import de.util.CooPaletType;

public class CooPalet extends CooData
{
	/** {@link StringProperty} for the palet name */
	protected StringProperty name;
	/** {@link ObjectProperty} for the palet {@link CooPaletType} */
	protected ObjectProperty<CooPaletType> type;
	/** {@link IntegerProperty} for the palet width */
	protected IntegerProperty width;
	/** {@link IntegerProperty} for the palet width */
	protected IntegerProperty length;
	
	public CooPalet()
	{
		name = new SimpleStringProperty();
		type = new SimpleObjectProperty<CooPaletType>(
				CooPaletType.U);
		width = new SimpleIntegerProperty();
		length = new SimpleIntegerProperty();
	}
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element palet = addElement(doc, root, "Palet");
		palet.setAttribute("Name", String.valueOf(name.get()));
		palet.setAttribute("Type", String.valueOf(type.get()));
		palet.setAttribute("Width", String.valueOf(width.get()));
		palet.setAttribute("Length", String.valueOf(length.get()));
	}
	
	@Override
	public void fromXML(Element palet)
	{
		if(Objects.nonNull(palet))
		{
			name.set(palet.getAttribute("Name"));
			type.set(CooPaletType.parse(palet.getAttribute("Type")));
			width.set(Integer.valueOf(palet.getAttribute("Width")));
			length.set(Integer.valueOf(palet.getAttribute("Length")));
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
	 * @return {@link #type}
	 */
	public ObjectProperty<CooPaletType> typeProperty()
	{
		return type;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #width}
	 */
	public IntegerProperty widthProperty()
	{
		return width;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #length}
	 */
	public IntegerProperty lengthProperty()
	{
		return length;
	}
	
	@Override
	public String toString()
	{
		return nameProperty().get();
	}
}