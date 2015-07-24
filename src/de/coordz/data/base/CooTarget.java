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

public class CooTarget extends CooData
{
	/** {@link StringProperty} for the target name */
	protected StringProperty name;
	/** {@link IntegerProperty} for the target x coordinate */
	protected IntegerProperty x;
	/** {@link IntegerProperty} for the target y coordinate */
	protected IntegerProperty y;
	/** {@link IntegerProperty} for the target z coordinate */
	protected IntegerProperty z;
	
	public CooTarget()
	{
		name = new SimpleStringProperty();
		x = new SimpleIntegerProperty();
		y = new SimpleIntegerProperty();
		z = new SimpleIntegerProperty();
	}
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element target = addElement(doc, root, "Target");
		target.setAttribute("Name", name.get());
		target.setAttribute("X", String.valueOf(x.get()));
		target.setAttribute("Y", String.valueOf(y.get()));
		target.setAttribute("Z", String.valueOf(z.get()));
	}
	
	@Override
	public void fromXML(Element target)
	{
		if(Objects.nonNull(target))
		{
			name.set(target.getAttribute("Name"));
			x.set(Integer.valueOf(target.getAttribute("X")));
			y.set(Integer.valueOf(target.getAttribute("Y")));
			z.set(Integer.valueOf(target.getAttribute("Z")));
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
	 * @return {@link #x}
	 */
	public IntegerProperty xProperty()
	{
		return x;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #y}
	 */
	public IntegerProperty yProperty()
	{
		return y;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #z}
	 */
	public IntegerProperty zProperty()
	{
		return z;
	}
}