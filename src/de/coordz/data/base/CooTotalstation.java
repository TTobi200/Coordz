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

public class CooTotalstation extends CooData
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
	
	public IntegerProperty xProperty()
	{
		return x;
	}
	
	public IntegerProperty yProperty()
	{
		return y;
	}
	
	public IntegerProperty zProperty()
	{
		return z;
	}
	
	public DoubleProperty deltaXProperty()
	{
		return deltaX;
	}
	
	public DoubleProperty deltaYProperty()
	{
		return deltaY;
	}
}