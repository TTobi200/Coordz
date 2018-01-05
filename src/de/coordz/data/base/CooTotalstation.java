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

import de.coordz.db.xml.CooDBXML;
import javafx.beans.property.*;

public class CooTotalstation implements CooDBXML
{
	/** {@link IntegerProperty} for the totalstation x coordinate */
	protected IntegerProperty x;
	/** {@link IntegerProperty} for the totalstation y coordinate */
	protected IntegerProperty y;
	/** {@link IntegerProperty} for the totalstation z coordinate */
	protected IntegerProperty z;
	/** {@link IntegerProperty} for the totalstation delta x */
	protected DoubleProperty deltaX;
	/** {@link IntegerProperty} for the totalstation delta y */
	protected DoubleProperty deltaY;
	
	public CooTotalstation()
	{
		x = new SimpleIntegerProperty();
		y = new SimpleIntegerProperty();
		z = new SimpleIntegerProperty();
		deltaX = new SimpleDoubleProperty();
		deltaY = new SimpleDoubleProperty();
	}
	
	@Override
	public void toXML(Document doc, Element root)
	{
		Element totalstation = addElement(doc, root, "Totalstation");
		totalstation.setAttribute("X", String.valueOf(x.get()));
		totalstation.setAttribute("Y", String.valueOf(y.get()));
		totalstation.setAttribute("Z", String.valueOf(z.get()));
		totalstation.setAttribute("DeltaX", String.valueOf(deltaX.get()));
		totalstation.setAttribute("DeltaY", String.valueOf(deltaY.get()));
	}
	
	@Override
	public void fromXML(Element totalstation)
	{
		if(Objects.nonNull(totalstation))
		{
			x.set(Integer.valueOf(totalstation.getAttribute("X")));
			y.set(Integer.valueOf(totalstation.getAttribute("Y")));
			z.set(Integer.valueOf(totalstation.getAttribute("Z")));
			deltaX.set(Double.valueOf(totalstation.getAttribute("DeltaX")));
			deltaY.set(Double.valueOf(totalstation.getAttribute("DeltaY")));
		}
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
	
	/**
	 * Method to access Property
	 * @return {@link #deltaX}
	 */
	public DoubleProperty deltaXProperty()
	{
		return deltaX;
	}
	
	/**
	 * Method to access Property
	 * @return {@link #deltaY}
	 */
	public DoubleProperty deltaYProperty()
	{
		return deltaY;
	}
}